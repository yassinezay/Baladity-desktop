package edu.esprit.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class CommentaireTache implements Serializable {
    private int id_Cmnt;
    private Tache tache;
    private EndUser user;
    private Date date_C;
    private String text_C;

    public CommentaireTache() {
    }

    public CommentaireTache(EndUser user, Tache tache, Date date_C, String text_C) {
        this.user = user;
        this.tache = tache;
        this.date_C = date_C;
        this.text_C = text_C;
    }

    public CommentaireTache(int id_Cmnt, String text_C) {
        this.id_Cmnt = id_Cmnt;
        this.text_C = text_C;
    }

    public CommentaireTache(int id_Cmnt, EndUser user, Tache tache, Date date_C, String text_C) {
        this.id_Cmnt = id_Cmnt;
        this.user = user;
        this.tache = tache;
        this.date_C = date_C;
        this.text_C = text_C;
    }

    public int getId_Cmnt() {
        return id_Cmnt;
    }

    public void setId_Cmnt(int id_Cmnt) {
        this.id_Cmnt = id_Cmnt;
    }

    public EndUser getUser() {
        return user;
    }

    public void setUser(EndUser user) {
        this.user = user;
    }

    public Tache getTache() {
        return tache;
    }

    public void setTache(Tache tache) {
        this.tache = tache;
    }

    public String getText_C() {
        return text_C;
    }

    public void setText_C(String text_C) {
        this.text_C = text_C;
    }

    public Date getDate_C() {
        return date_C;
    }

    public void setDate_C(Date date_C) {
        this.date_C = date_C;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentaireTache that)) return false;
        return getId_Cmnt() == that.getId_Cmnt();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId_Cmnt());
    }

    @Override
    public String toString() {
        return "CommentaireTache{" +
                "date_C=" + date_C + "|" +
                "text_C='" + text_C + "|" +
                '}' +
                System.lineSeparator();
    }
}

