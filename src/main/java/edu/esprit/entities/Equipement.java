package edu.esprit.entities;

import java.util.Date;
import java.util.Objects;

public class Equipement {
    private int id_equipement;
    private String reference_eq;
    private String nom_eq;
    private String categorie_eq;
    private Date date_ajouteq;
    private int quantite_eq;
    private String image_eq;
    private String description_eq;
    private EndUser user;
    private Municipality muni;

    public int getId_equipement() {
        return id_equipement;
    }

    public void setId_equipement(int id_equipement) {
        this.id_equipement = id_equipement;
    }

    public String getReference_eq() {
        return reference_eq;
    }

    public void setReference_eq(String reference_eq) {
        this.reference_eq = reference_eq;
    }

    public String getNom_eq() {
        return nom_eq;
    }

    public void setNom_eq(String nom_eq) {
        this.nom_eq = nom_eq;
    }

    public String getCategorie_eq() {
        return categorie_eq;
    }

    public void setCategorie_eq(String categorie_eq) {
        this.categorie_eq = categorie_eq;
    }

    public Date getDate_ajouteq() {
        return date_ajouteq;
    }

    public void setDate_ajouteq(Date date_ajouteq) {
        this.date_ajouteq = date_ajouteq;
    }

    public int getQuantite_eq() {
        return quantite_eq;
    }

    public void setQuantite_eq(int quantite_eq) {
        this.quantite_eq = quantite_eq;
    }

    public String getImage_eq() {
        return image_eq;
    }

    public void setImage_eq(String image_eq) {
        this.image_eq = image_eq;
    }

    public String getDescription_eq() {
        return description_eq;
    }

    public void setDescription_eq(String description_eq) {
        this.description_eq = description_eq;
    }

    public EndUser getUser() {
        return user;
    }

    public void setUser(EndUser user) {
        this.user = user;
    }

    public Municipality getMuni() {
        return muni;
    }

    public void setMuni(Municipality muni) {
        this.muni = muni;
    }
    public Equipement() {
    }
    public Equipement(int id_equipement) {
        this.id_equipement = id_equipement;
    }
    public Equipement(int id_equipement,String reference_eq,String nom_eq,String categorie_eq,Date date_ajouteq,int quantite_eq,String image_eq,String description_eq,EndUser user,Municipality muni) {
        this.id_equipement = id_equipement;
        this.reference_eq = reference_eq;
        this.nom_eq = nom_eq;
        this.categorie_eq = categorie_eq;
        this.date_ajouteq = date_ajouteq;
        this.quantite_eq = quantite_eq;
        this.image_eq = image_eq;
        this.description_eq = description_eq;
        this.user = user;
        this.muni= muni;
    }

    public Equipement(String reference_eq,String nom_eq,String categorie_eq,Date date_ajouteq,int quantite_eq,String image_eq,String description_eq,EndUser user,Municipality muni) {
        this.reference_eq = reference_eq;
        this.nom_eq = nom_eq;
        this.categorie_eq = categorie_eq;
        this.date_ajouteq = date_ajouteq;
        this.quantite_eq = quantite_eq;
        this.image_eq = image_eq;
        this.description_eq = description_eq;
        this.user = user;
        this.muni= muni;
    }
    public Equipement(String reference_eq,String nom_eq,String categorie_eq,Date date_ajouteq,int quantite_eq,String image_eq,String description_eq) {
        this.reference_eq = reference_eq;
        this.nom_eq = nom_eq;
        this.categorie_eq = categorie_eq;
        this.date_ajouteq = date_ajouteq;
        this.quantite_eq = quantite_eq;
        this.image_eq = image_eq;
        this.description_eq = description_eq;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipement that = (Equipement) o;
        return id_equipement == that.id_equipement;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_equipement);
    }

    @Override
    public String toString() {
        return "Equipement{" +
                ", reference_eq='" + reference_eq + '\'' +
                ", nom_eq='" + nom_eq + '\'' +
                ", categorie_eq='" + categorie_eq + '\'' +
                ", date_ajouteq=" + date_ajouteq +
                ", quantite_eq=" + quantite_eq +
                ", image_eq='" + image_eq + '\'' +
                ", description_eq='" + description_eq + '\'' +
                ", user=" + user +
                ", muni=" + muni +
                '}';
    }
}