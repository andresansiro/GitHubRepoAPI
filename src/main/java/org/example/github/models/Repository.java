package org.example.github.models;

import java.util.List;

public class Repository {
    private String name;
    private String ownerLogin;
    private List<Branch> branches;

    public Repository(String name, String ownerLogin, List<Branch> branches) {
        this.name = name;
        this.ownerLogin = ownerLogin;
        this.branches = branches;
    }

    public String getName() {
        return name;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public List<Branch> getBranches() {
        return branches;
    }
}