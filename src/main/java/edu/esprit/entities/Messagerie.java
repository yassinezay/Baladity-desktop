package edu.esprit.entities;

import java.util.Date;
import java.util.Objects;

public class Messagerie {
    private int id_message;
    private Date date_message;
    private String contenu_message;
    private EndUser receiver_message;
    private EndUser sender_message;
    private String type_message;

    public Messagerie() {
    }

    public Messagerie(Date date_message, String contenu_message, EndUser receiver_message, EndUser sender_message, String type_message) {
        this.date_message = date_message;
        this.contenu_message = contenu_message;
        this.receiver_message = receiver_message;
        this.sender_message = sender_message;
        this.type_message = type_message;
    }

    public Messagerie(int id_message, Date date_message, String contenu_message, EndUser receiver_message, EndUser sender_message, String type_message) {
        this.id_message = id_message;
        this.date_message = date_message;
        this.contenu_message = contenu_message;
        this.receiver_message = receiver_message;
        this.sender_message = sender_message;
        this.type_message = type_message;
    }

    public int getId_message() {
        return id_message;
    }

    public void setId_message(int id_message) {
        this.id_message = id_message;
    }

    public java.sql.Date getDate_message() {
        return (java.sql.Date) date_message;
    }

    public void setDate_message(Date date_message) {
        this.date_message = date_message;
    }

    public String getContenu_message() {
        return contenu_message;
    }

    public void setContenu_message(String contenu_message) {
        this.contenu_message = contenu_message;
    }

    public EndUser getReceiver_message() {
        return receiver_message;
    }

    public void setReceiver_message(EndUser receiver_message) {
        this.receiver_message = receiver_message;
    }

    public EndUser getSender_message() {
        return sender_message;
    }

    public void setSender_message(EndUser sender_message) {
        this.sender_message = sender_message;
    }

    public String getType_message() {
        return type_message;
    }

    public void setType_message(String type_message) {
        this.type_message = type_message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Messagerie that = (Messagerie) o;
        return id_message == that.id_message;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_message);
    }

    @Override
    public String toString() {
        return "Messagerie{" +
                "date_message=" + date_message +
                ", contenu_message='" + contenu_message + '\'' +
                ", receiverId_message=" + receiver_message +
                ", senderId_message=" + sender_message +
                ", type_message='" + type_message + '\'' +
                '}';
    }
}
