package com.momentum.dosein.models;

import java.io.Serializable;

public class Doctor implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String specialty;
    private String location;
    private String phone;
    private String email;
    private String additional;

    public Doctor() { }

    public Doctor(String name, String specialty, String location,
                  String phone, String email, String additional) {
        this.name = name;
        this.specialty = specialty;
        this.location = location;
        this.phone = phone;
        this.email = email;
        this.additional = additional;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAdditional() { return additional; }
    public void setAdditional(String additional) { this.additional = additional; }

    @Override
    public String toString() {
        // shown in the ListView
        return name;
    }
}
