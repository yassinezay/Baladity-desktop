package edu.esprit.entities;

import java.util.Objects;

public class EndUser {
    private int id;
    private String nom;
    private String email;
    private String password;
    private String type;
    private String phoneNumber;
    private Municipality muni;
    private String location;
    private String image;
    private boolean isBanned;
    private boolean isVerified;

    public EndUser(){}
    public EndUser(int id){
        this.id=id;
    }

    public  EndUser(String nom, String email, String password, String type, String phoneNumber, Municipality muni, String location, String image){
        this.nom = nom;
        this.email = email;
        this.password = password;
        this.type = type;
        this.phoneNumber = phoneNumber;
        this.muni = muni;
        this.location = location;
        this.image = image;
    }

    public  EndUser(String nom, String email, String password, String type, String phoneNumber, Municipality muni, String location, String image, boolean isBanned){
        this.nom = nom;
        this.email = email;
        this.password = password;
        this.type = type;
        this.phoneNumber = phoneNumber;
        this.muni = muni;
        this.location = location;
        this.image = image;
        this.isBanned = isBanned();
    }

    public EndUser(int id, String email, String nom, String password, String type, String phoneNumber, Municipality muni, String location, String image, boolean isBanned) {
        this.id = id;
        this.email = email;
        this.nom = nom;
        this.password = password;
        this.type = type;
        this.phoneNumber = phoneNumber;
        this.muni = muni;
        this.location = location;
        this.image = image;
        this.isBanned = isBanned;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Municipality getMuni() {
        return muni;
    }

    public void setMuni(Municipality muni) {
        this.muni = muni;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }
    public boolean isBanned() {
        return isBanned;
    }

    @Override
    public String toString() {
        return "EndUser{" +
                "nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", type='" + type + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", muni='" + muni + '\'' +
                ", location='" + location + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EndUser endUser = (EndUser) o;
        return id == endUser.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
