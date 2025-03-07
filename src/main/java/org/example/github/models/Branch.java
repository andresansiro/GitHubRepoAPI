package org.example.github.models;

public class Branch {
    private String name;
    private String lastCommitSha;

    public Branch(String name, String lastCommitSha) {
        this.name = name;
        this.lastCommitSha = lastCommitSha;
    }

    public String getName() {
        return name;
    }

    public String getLastCommitSha() {
        return lastCommitSha;
    }
}
