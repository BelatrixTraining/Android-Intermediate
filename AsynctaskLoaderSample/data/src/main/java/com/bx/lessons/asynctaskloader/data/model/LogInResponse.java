package com.bx.lessons.asynctaskloader.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by em on 8/06/16
 {
 "lastLogin": 1488839776263,
 "created": 1488830146000,
 "name": "belatrix",
 "___class": "Users",
 "user-token": "09AE087C-F95C-6B12-FF68-BC31C2D61600",
 "ownerId": "A8E8D48D-3AA3-F303-FFFF-17CF15593200",
 "updated": 1488839302000,
 "email": "admin@abc.com",
 "objectId": "A8E8D48D-3AA3-F303-FFFF-17CF15593200",
 "__meta": "{\"relationRemovalIds\":{},\"selectedProperties\":[\"__updated__meta\",\"password\",\"created\",\"name\",\"___class\",\"ownerId\",\"updated\",\"email\",\"objectId\"],\"relatedObjects\":{}}"
 }
 */
public class LogInResponse {

    private String message;

    private String name;

    @SerializedName("___class")
    private String type;

    @SerializedName("user-token")
    private String token;

    private String email;

    private String objectId;

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

}
