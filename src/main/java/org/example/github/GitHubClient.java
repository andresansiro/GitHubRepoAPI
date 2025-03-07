package org.example.github;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.example.github.models.Branch;
import org.example.github.models.Repository;

import java.util.List;

@Path("/")
@RegisterRestClient(configKey = "github-api")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
public interface GitHubClient {
    @GET
    @Path("/users/{user}/repos")
    Uni<List<Repository>> getRepositories(@PathParam("user") String user);

    @GET
    @Path("/repos/{user}/{repo}/branches")
    Uni<List<Branch>> getBranches(@PathParam("user") String user, @PathParam("repo") String repo);
}