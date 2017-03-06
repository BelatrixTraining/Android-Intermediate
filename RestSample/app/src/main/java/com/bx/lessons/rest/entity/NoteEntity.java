package com.bx.lessons.rest.entity;

import java.io.Serializable;

/**
 * Created by emedinaa on 15/09/15.
 *
 *  {
 "created": 1465414000000,
 "name": "Ingresar notas de la práctica 01",
 "___class": "Notes",
 "description": "Isil",
 "ownerId": null,
 "updated": null,
 "objectId": "D8675151-11F5-B6A7-FF83-2A07C53D7300",
 "__meta": "{\"relationRemovalIds\":{},\"selectedProperties\":[\"created\",\"name\",\"___class\",\"description\",\"ownerId\",\"updated\",\"objectId\"],\"relatedObjects\":{}}"
 }
 */
public class NoteEntity implements Serializable {

    private int id;
    private String name;
    private String objectId;
    private String description;
    private String path;

    public NoteEntity() {
    }

    public NoteEntity(int id, String name, String description, String path) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.path = path;
    }

    public NoteEntity(String name, String description, String path) {
        this.name = name;
        this.description = description;
        this.path = path;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "NoteEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
