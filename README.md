# GitHub Repositories API

This application fetches GitHub repositories for a given user, filters out forked repositories, and retrieves branch details for each repository.

## How to Run 

1. Clone the repository.
2. Build the project (Built with Java 21) :
    ```bash
    mvn clean package
    ```
3. Run the application:
    ```bash
    java -jar target/quarkus-app/quarkus-run.jar
    ```
    Access the API at http://localhost:8080/github/{user}/repositories

Example Response
```bash

[
  {
    "name": "repo1",
    "ownerLogin": "octocat",
    "branches": [
      {
        "name": "main",
        "lastCommitSha": "abc123"
      }
    ]
  }
]
```

## Error Handling

404: User not found.

500: Internal server error.
