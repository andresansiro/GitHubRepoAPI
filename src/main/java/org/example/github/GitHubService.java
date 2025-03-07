package org.example.github;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.example.github.exceptions.UserNotFoundException;
import org.example.github.models.Branch;
import org.example.github.models.Repository;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class GitHubService {

    private final GitHubClient gitHubClient;

    public GitHubService(@RestClient GitHubClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    public Uni<List<Repository>> getRepositories(String user) {
        return gitHubClient.getRepositories(user)
                .onItem().transformToUni(Unchecked.function(repos -> {
                    if (repos.isEmpty()) {
                        return Uni.createFrom().<List<Repository>>item(Collections.emptyList());
                    }

                    List<Uni<Repository>> repositoryUnis = repos.stream()
                            .map(repo -> gitHubClient.getBranches(user, repo.getName())
                                    .onItem().transform(branches -> new Repository(
                                            repo.getName(),
                                            repo.getOwnerLogin(),
                                            branches.stream()
                                                    .map(branch -> new Branch(
                                                            branch.getName(),
                                                            branch.getLastCommitSha()))
                                                    .toList()
                                    )))
                            .toList();

                    return Uni.combine().all().unis(repositoryUnis)
                            .with(repositories -> repositories.stream()
                                    .map(repo -> (Repository) repo)
                                    .collect(Collectors.toList()));
                }))
                .onFailure(ClientWebApplicationException.class).recoverWithUni(throwable -> {
                    ClientWebApplicationException clientException = (ClientWebApplicationException) throwable;
                    if (clientException.getResponse().getStatus() == 404) {
                        return Uni.createFrom().failure(new UserNotFoundException("GitHub user '" + user + "' does not exist"));
                    }
                    return Uni.createFrom().failure(throwable);
                });
    }
}