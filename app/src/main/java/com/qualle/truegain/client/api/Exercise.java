package com.qualle.truegain.client.api;

import java.io.Serializable;
import java.util.List;

public class Exercise implements Serializable {

    private long id;
    private String name;
    private String equipment;
    private String description;
    private String imageLink;
    private List<Record> records;

    public Exercise() {
    }

    public Exercise(long id, List<Record> records) {
        this.id = id;
        this.records = records;
    }

    public Exercise(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Exercise(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Exercise(long id, String name, String equipment, String description) {
        this.id = id;
        this.name = name;
        this.equipment = equipment;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }
}
