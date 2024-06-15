package edu.esprit.entities;

import java.util.Objects;
import java.util.Date;
public class Reclamation{

    private int id_reclamation;
    private EndUser user;
    private Municipality Municipality;
    private  String sujet_reclamation;
    private Date date_reclamation;
    private String type_reclamation;
    private String description_reclamation;
    private String status_reclamation;
    private String image_reclamation;
    private String adresse_reclamation;
    public Reclamation(EndUser user, Municipality Municipality, int i, java.sql.Date sqlDate, String nonUrgent, String imageReclamation, String adresseReclamation){

    }
    public Reclamation(EndUser user, Municipality Municipality,String sujet_reclamation,Date date_reclamation, String type_reclamation, String description_reclamation, String image_reclamation, String adresse_reclamation) {
        this.user = user;
        this.Municipality = Municipality;
        this.sujet_reclamation=sujet_reclamation;
        this.date_reclamation = date_reclamation;
        this.type_reclamation = type_reclamation;
        this.description_reclamation = description_reclamation;
        this.image_reclamation = image_reclamation;
        this.adresse_reclamation = adresse_reclamation;
    }

    public Reclamation(int id_reclamation, EndUser user, Municipality Municipality, String sujet_reclamation,Date date_reclamation, String type_reclamation, String description_reclamation, String status_reclamation, String image_reclamation,String adresse_reclamation) {
        this.id_reclamation = id_reclamation;
        this.user = user;
        this.Municipality = Municipality;
        this.sujet_reclamation=sujet_reclamation;
        this.date_reclamation = date_reclamation;
        this.type_reclamation = type_reclamation;
        this.description_reclamation = description_reclamation;
        this.status_reclamation = status_reclamation;
        this.image_reclamation = image_reclamation;
        this.adresse_reclamation = adresse_reclamation;
    }
    public Reclamation(int id_reclamation, EndUser user, Municipality Municipality, String sujet_reclamation,Date date_reclamation, String type_reclamation, String description_reclamation, String image_reclamation,String adresse_reclamation) {
        this.id_reclamation = id_reclamation;
        this.user = user;
        this.Municipality = Municipality;
        this.sujet_reclamation=sujet_reclamation;
        this.date_reclamation = date_reclamation;
        this.type_reclamation = type_reclamation;
        this.description_reclamation = description_reclamation;
        this.image_reclamation = image_reclamation;
        this.adresse_reclamation = adresse_reclamation;
    }

    public int getId_reclamation() {
        return id_reclamation;
    }

    public void setId_reclamation(int id_reclamation) {
        this.id_reclamation = id_reclamation;
    }

    public EndUser getUser() {
        return user;
    }

    public void setUser(EndUser user) {
        this.user = user;
    }

    public Municipality getMunicipality() {
        return Municipality;
    }

    public void setMunicipality(Municipality Municipality) {
        this.Municipality = Municipality;
    }

    public String getSujet_reclamation() {
        return sujet_reclamation;
    }

    public void setSujet_reclamation(String sujet_reclamation) {
        this.sujet_reclamation = sujet_reclamation;
    }

    public java.sql.Date getDate_reclamation() {
        return (java.sql.Date) date_reclamation;
    }

    public void setDate_reclamation(Date date_reclamation) {
        this.date_reclamation = date_reclamation;
    }

    public String getType_reclamation() {
        return type_reclamation;
    }

    public void setType_reclamation(String type_reclamation) {
        this.type_reclamation = type_reclamation;
    }

    public String getDescription_reclamation() {
        return description_reclamation;
    }

    public void setDescription_reclamation(String description_reclamation) {
        this.description_reclamation = description_reclamation;
    }

    public String getStatus_reclamation() {
        return status_reclamation;
    }

    public void setStatus_reclamation(String status_reclamation) {
        this.status_reclamation = status_reclamation;
    }

    public String getImage_reclamation() {
        return image_reclamation;
    }

    public void setImage_reclamation(String image_reclamation) {
        this.image_reclamation = image_reclamation;
    }

    public String getAdresse_reclamation() {
        return adresse_reclamation;
    }

    public void setAdresse_reclamation(String adresse_reclamation) {
        this.adresse_reclamation = adresse_reclamation;
    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "user=" + user +
                ", Municipality=" + Municipality +
                ", sujet_reclamation='" + sujet_reclamation + '\'' +
                ", date_reclamation=" + date_reclamation +
                ", type_reclamation='" + type_reclamation + '\'' +
                ", description_reclamation='" + description_reclamation + '\'' +
                ", status_reclamation='" + status_reclamation + '\'' +
                ", image_reclamation='" + image_reclamation + '\'' +
                ", adresse_reclamation='" + adresse_reclamation + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reclamation that = (Reclamation) o;
        return id_reclamation == that.id_reclamation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_reclamation);
    }
}
