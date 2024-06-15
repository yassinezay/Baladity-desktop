package edu.esprit.services;

import edu.esprit.entities.EndUser;
import edu.esprit.entities.Evenement;
import edu.esprit.utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class ServiceEvenement implements IService<Evenement> {
    Connection cnx = DataSource.getInstance().getCnx();
    @Override
    public void ajouter(Evenement evenement) {
        if (!validateEvenement(evenement)) {
            System.out.println("Tous les champs doivent être remplis !");
            return;
        }

        if (evenement.getUser() == null) {
            System.out.println("L'utilisateur associé à l'événement est null !");
            return;
        }

        String req = "INSERT INTO evenement (id_user, nom_E, date_DHE, date_DHF, capacite_E, categorie_E, imageEvent) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, evenement.getUser().getId());
            ps.setString(2, evenement.getNomEvent());
            ps.setString(3, evenement.getDateEtHeureDeb());
            ps.setString(4, evenement.getDateEtHeureFin());
            ps.setInt(5, evenement.getCapaciteMax());
            ps.setString(6, evenement.getCategorie());
            ps.setString(7, evenement.getImageEvent());
            ps.executeUpdate();
            System.out.println("Evenement ajouté !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Evenement evenement) {

        String req = "UPDATE evenement SET id_user=?, nom_E=?, date_DHE=?, date_DHF=?, capacite_E=?, categorie_E=?, imageEvent=? WHERE id_E=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, evenement.getUser().getId());
            ps.setString(2, evenement.getNomEvent());
            ps.setString(3, evenement.getDateEtHeureDeb());
            ps.setString(4, evenement.getDateEtHeureFin());
            ps.setInt(5, evenement.getCapaciteMax());
            ps.setString(6, evenement.getCategorie());
            ps.setString(7, evenement.getImageEvent());
            ps.setInt(8, evenement.getId_E());
            ps.executeUpdate();
            System.out.println("Evenement modifié !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean validateEvenement(Evenement evenement) {
        return evenement.getUser() != null &&
                !evenement.getNomEvent().isEmpty() &&
                !evenement.getDateEtHeureDeb().isEmpty() &&
                !evenement.getDateEtHeureFin().isEmpty() &&
                !evenement.getCategorie().isEmpty();
    }


    @Override
    public void supprimer(int id) {
        String req = "DELETE FROM evenement WHERE id_E=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Evenement deleted !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Set<Evenement> getAll() {
        Set<Evenement> evenements = new HashSet<>();
        String req = "SELECT * FROM evenement";
        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                int id = rs.getInt("id_E");
                int id_user = rs.getInt("id_user");
                String nom_E = rs.getString("nom_E");
                String date_DHE = rs.getString("date_DHE");
                String date_DHF = rs.getString("date_DHF");
                int capacite_E = rs.getInt("capacite_E");
                String categorie_E = rs.getString("categorie_E");
                String imageEvent = rs.getString("imageEvent"); // Fetch imageEvent from the result set
                ServiceUser serviceUser = new ServiceUser();
                EndUser endUser = serviceUser.getOneByID(id_user);
                Evenement evenement = new Evenement(id, nom_E, endUser, date_DHE, date_DHF, capacite_E, categorie_E, imageEvent);
                evenements.add(evenement);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return evenements;
    }

    @Override
    public Evenement getOneByID(int id) {
        String req = "SELECT * FROM evenement WHERE id_E=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id_user = rs.getInt("id_user");
                String nom_E = rs.getString("nom_E");
                String date_DHE = rs.getString("date_DHE");
                String date_DHF = rs.getString("date_DHF");
                int capacite_E = rs.getInt("capacite_E");
                String categorie_E = rs.getString("categorie_E");
                String imageEvent = rs.getString("imageEvent"); // Fetch imageEvent from the result set
                ServiceUser serviceUser = new ServiceUser();
                EndUser endUser = serviceUser.getOneByID(id_user);
                Evenement evenement = new Evenement(id, nom_E, endUser, date_DHE, date_DHF, capacite_E, categorie_E, imageEvent);
                return evenement; // Return the evenement object
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}

