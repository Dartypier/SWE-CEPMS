package it.unifi.swe.cepms.server_module.domain_model;

import it.unifi.swe.cepms.server_module.helpers.HashedPassword;

import java.io.Serializable;

public class User {
    private String email;
    private String name;
    private String surname;
    private String encodedPassword;

    public User(String email, String name, String surname, String password) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.encodedPassword = HashedPassword.createHash(password);
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEncodedPassword(String encodedPassword){
        this.encodedPassword = encodedPassword;
    }
}