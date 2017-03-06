package com.bx.lessons.rest.storage.entity;


import com.bx.lessons.rest.entity.NoteEntity;

import java.util.List;

/**
 * Created by em on 8/06/16.
 *
 * {
 "offset": 0,
 "data": [
     {
     "created": 1465414000000,
     "name": "Ingresar notas de la práctica 01",
     "___class": "Notes",
     "description": "Isil",
     "ownerId": null,
     "updated": null,
     "objectId": "D8675151-11F5-B6A7-FF83-2A07C53D7300",
     "__meta": "{\"relationRemovalIds\":{},\"selectedProperties\":[\"created\",\"name\",\"___class\",\"description\",\"ownerId\",\"updated\",\"objectId\"],\"relatedObjects\":{}}"
     }
     ],
 "nextPage": null,
 "totalObjects": 1
 }
 */
public class NotesResponse {

    private String message;

    private int offset;
    private List<NoteEntity> data;

    private Object nextPage;
    private int totalObjects;


    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public List<NoteEntity> getData() {
        return data;
    }

    public void setData(List<NoteEntity> data) {
        this.data = data;
    }

    public Object getNextPage() {
        return nextPage;
    }

    public void setNextPage(Object nextPage) {
        this.nextPage = nextPage;
    }

    public int getTotalObjects() {
        return totalObjects;
    }

    public void setTotalObjects(int totalObjects) {
        this.totalObjects = totalObjects;
    }
}
