package com.bx.lessons.asynctaskloader.model;

import java.io.Serializable;

/**
 * Created by em on 8/06/16.
 */
public class UserEntity implements Serializable {
    private String email;
    private String name;
    private String objectId;
    private String token;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", objectId='" + objectId + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
