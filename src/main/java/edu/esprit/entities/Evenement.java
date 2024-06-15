package edu.esprit.entities;

import java.util.Objects;

public class Evenement{
    private int id_E;
    private String nom_E;
    private EndUser user;
    private String date_DHE;
    private String date_DHF;
    private int capacite_E;
    private String categorie_E;
    private String imageEvent;


    public Evenement(){

    }

    public Evenement(int id_E, String nom_E, EndUser user, String date_DHE, String date_DHF, int capacite_E, String categorie_E, String imageEvent) {
        this.id_E = id_E;
        this.nom_E = nom_E;
        this.user =user;
        this.date_DHE = date_DHE;
        this.date_DHF = date_DHF;
        this.capacite_E = capacite_E;
        this.categorie_E = categorie_E;
        this.imageEvent = imageEvent;
    }

    public Evenement(EndUser user, String nom_E, String date_DHE, String date_DHF, int capacite_E, String categorie_E , String imageEvent) {
        this.user=user;
        this.nom_E=nom_E;
        this.date_DHE = date_DHE;
        this.date_DHF = date_DHF;
        this.capacite_E = capacite_E;
        this.categorie_E = categorie_E;
        this.imageEvent = imageEvent;
    }

    public String getImageEvent() {
        return imageEvent;
    }

    public void setImageEvent(String imageEvent) {
        this.imageEvent = imageEvent;
    }

    public int getId_E() {
        return id_E;
    }

    public void setId(int id) {
        this.id_E = id_E;
    }

    public String getNomEvent() {
        return nom_E;
    }

    public void setNomEvent(String nom_E) {
        this.nom_E = nom_E;
    }

    public EndUser getUser() {
        return user;
    }

    public void setUser(EndUser user) {
        this.user = user;
    }

    public String getDateEtHeureDeb() {
        return date_DHE;
    }

    public void setDateEtHeureDeb(String date_DHE) {
        this.date_DHE = date_DHE;
    }

    public String getDateEtHeureFin() {
        return date_DHF;
    }

    public void setDateEtHeureFin(String date_DHF) {
        this.date_DHF = date_DHF;
    }

    public int getCapaciteMax() {
        return capacite_E;
    }

    public void setCapaciteMax(int capacite_E) {
        this.capacite_E = capacite_E;
    }

    public String getCategorie() {
        return categorie_E;
    }

    public void setCategorie(String categorie_E) {
        this.categorie_E = categorie_E;
    }

    @Override
    public String toString() {
        return "Evenement{" +
                "id_E=" + id_E +
                ", nom_E='" + nom_E + '\'' +
                ", user=" + user +
                ", date_DHE='" + date_DHE + '\'' +
                ", date_DHF='" + date_DHF + '\'' +
                ", capacite_E=" + capacite_E +
                ", categorie_E='" + categorie_E + '\'' +
                ", imageEvent='" + imageEvent + '\'' +

                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Evenement evenement)) return false;
        return getId_E() == evenement.getId_E() && Objects.equals(getNomEvent(), evenement.getNomEvent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId_E(), getNomEvent());
    }


}
