package com.example.models;

import javax.persistence.*;

/**
 * Created by PatrykGrudnik on 13.12.2016.
 */
@Entity
public class Brand {
    @Id
    @GeneratedValue
    int id;
    String name;

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
}
