package edu.esprit.services;

import edu.esprit.entities.EndUser;
import edu.esprit.entities.Vote;
import edu.esprit.utils.DataSource;

import java.util.Set;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class ServiceVote implements IService<Vote> {
    Connection cnx = DataSource.getInstance().getCnx();

        @Override
        public void ajouter(Vote vote) {
            // Vérification des champs requis
            if (!validateVote(vote)) {
                System.out.println("Tous les champs doivent être remplis !");
                return;
            }

            String req = "INSERT INTO vote (id_user, desc_E, date_SV) VALUES (?, ?, ?)";
            try {
                PreparedStatement ps = cnx.prepareStatement(req);
                ps.setInt(1, vote.getUser().getId());
                ps.setString(2, vote.getDesc_E());
                ps.setString(3, vote.getDate_SV());
                ps.executeUpdate();
                System.out.println("Vote added !");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        @Override
        public void modifier(Vote vote) {
            // Vérification des champs requis
            if (!validateVote(vote)) {
                System.out.println("Tous les champs doivent être remplis !");
                return;
            }

            String req = "UPDATE vote SET id_user=?, desc_E=?, date_SV=? WHERE id_V=?";
            try {
                PreparedStatement ps = cnx.prepareStatement(req);
                ps.setInt(1, vote.getUser().getId());
                ps.setString(2, vote.getDesc_E());
                ps.setString(3, vote.getDate_SV());
                ps.setInt(4, vote.getId_V());
                ps.executeUpdate();
                System.out.println("Vote updated !");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        // Méthode pour valider que tous les champs requis sont remplis
        private boolean validateVote(Vote vote) {
            return vote.getUser().getId() > 0 &&
                    !vote.getDesc_E().isEmpty() &&
                    !vote.getDate_SV().isEmpty();
        }


        @Override
    public void supprimer(int id) {
        String req = "DELETE FROM vote WHERE id_V=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Vote deleted !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Set<Vote> getAll() {
        Set<Vote> votes = new HashSet<>();
        String req = "SELECT * FROM vote";
        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                int id_V = rs.getInt("id_V");
                int id_user = rs.getInt("id_user");
                String desc_E = rs.getString("desc_E");
                String date_SV = rs.getString("date_SV");
                ServiceUser serviceUser = new ServiceUser();
                EndUser endUser = serviceUser.getOneByID(id_user);
                Vote vote = new Vote(id_V, endUser, desc_E, date_SV);
                votes.add(vote);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return votes;
    }

    @Override
    public Vote getOneByID(int id) {
        String req = "SELECT * FROM vote WHERE id_V=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id_user = rs.getInt("id_user");
                String desc_E = rs.getString("desc_E");
                String date_SV = rs.getString("date_SV");
                ServiceUser serviceUser = new ServiceUser();
                EndUser endUser = serviceUser.getOneByID(id_user);
                return new Vote(id, endUser, desc_E, date_SV);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
