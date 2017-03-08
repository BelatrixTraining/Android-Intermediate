package com.carlospinan.myretrofitexample.models;

import java.io.Serializable;
import java.util.List;

/**
 * @author Carlos Piñan
 */

public class Pokemon implements Serializable {

    public enum Type {
        GRASS,
        FIRE,
        WATER,
        FIGHT
    }

    private int id;
    private String name;
    private Type type;
    private List<String> skills;

    private transient long timestamp;

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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


}
