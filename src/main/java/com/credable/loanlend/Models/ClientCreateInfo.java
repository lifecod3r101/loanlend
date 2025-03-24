package com.credable.loanlend.Models;

public class ClientCreateInfo {
    String id;
    String url;
    String name;
    String username;
    String password;

    public ClientCreateInfo() {
    }

    public ClientCreateInfo(String id, String url, String name, String username, String password) {
        this.id = id;
        this.url = url;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
