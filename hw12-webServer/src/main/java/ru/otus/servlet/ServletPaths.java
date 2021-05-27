package ru.otus.servlet;

public enum ServletPaths {

    CLIENT_LIST("/clients"),
    ADD_CLIENT("/addClient"),
    API_CLIENT("/api/client/*");

    private String path;

    ServletPaths(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return this.path;
    }

}
