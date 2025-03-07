package org.example.github;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.smallrye.mutiny.Uni;
import org.example.github.exceptions.UserNotFoundException;
import org.example.github.models.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.when;

@QuarkusTest
public class GitHubResourceTest {

    GitHubClient gitHubClient = Mockito.mock(GitHubClient.class);

    @BeforeEach
    void setUp() {
        List<Repository> emptyList = Collections.emptyList();

        when(gitHubClient.getRepositories("octocat"))
                .thenReturn(Uni.createFrom().item(emptyList));

        when(gitHubClient.getRepositories("nonexistentuser"))
                .thenReturn(Uni.createFrom().failure(new UserNotFoundException("User not found")));
    }


    @Test
    public void testGetRepositoriesForValidUser() {
        RestAssured.given()
                .when()
                .get("/github/octocat/repositories")
                .then()
                .statusCode(200);
    }

    @Test
    public void testGetRepositoriesForNonExistentUser() {
        RestAssured.given()
                .when()
                .get("/github/nonexistentuser/repositories")
                .then()
                .statusCode(404)
                .body("status", equalTo(404))
                .body("message", equalTo("GitHub user 'nonexistentuser' does not exist"));
    }
}