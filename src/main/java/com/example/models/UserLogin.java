package com.example.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by PatrykGrudnik on 06.03.2017.
 */
@Entity
public class UserLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    String password;
}
