package edu.esprit.entities;

import java.util.Objects;

public class Vote {
    private int id_V;
    private EndUser user;
    private String desc_E;
    private String date_SV;

    public Vote() {
    }

    public Vote(int id_V, EndUser user, String desc_E, String date_SV) {
        this.id_V = id_V;
        this.user = user;
        this.desc_E = desc_E;
        this.date_SV = date_SV;
    }

    public Vote(EndUser user, String desc_E, String date_SV) {
        this.user = user;
        this.desc_E = desc_E;
        this.date_SV = date_SV;
    }

    public int getId_V() {
        return id_V;
    }

    public void setId_V(int id_V) {
        this.id_V = id_V;
    }

    public EndUser getUser() {
        return user;
    }

    public void setUser(EndUser user) {
        this.user = user;
    }

    public String getDesc_E() {
        return desc_E;
    }

    public void setDesc_E(String desc_E) {
        this.desc_E = desc_E;
    }

    public String getDate_SV() {
        return date_SV;
    }

    public void setDate_SV(String date_SV) {
        this.date_SV = date_SV;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id_V=" + id_V +
                ", user=" + user +
                ", desc_E='" + desc_E + '\'' +
                ", date_SV='" + date_SV + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vote vote)) return false;
        return getId_V() == vote.getId_V();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId_V());
    }
}
