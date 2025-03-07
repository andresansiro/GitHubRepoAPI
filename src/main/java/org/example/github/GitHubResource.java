package org.example.github;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.github.exceptions.UserNotFoundException;
import org.example.github.models.ErrorResponse;

@Path("/github")
public class GitHubResource {

    private final GitHubService gitHubService;

    public GitHubResource(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GET
    @Path("/{user}/repositories")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getRepositories(@PathParam("user") String user) {
        return gitHubService.getRepositories(user)
                .onItem().transform(repos -> Response.ok(repos).build())
                .onFailure().recoverWithItem(throwable -> {
                    if (throwable instanceof UserNotFoundException) {
                        return Response.status(404)
                                .entity(new ErrorResponse(404, throwable.getMessage()))
                                .build();
                    }
                    return Response.status(500)
                            .entity(new ErrorResponse(500, "Internal server error"))
                            .build();
                });
    }
}