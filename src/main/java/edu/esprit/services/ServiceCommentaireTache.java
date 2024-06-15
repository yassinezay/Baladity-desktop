package edu.esprit.services;

import edu.esprit.entities.CommentaireTache;
import edu.esprit.entities.EndUser;
import edu.esprit.entities.Tache;
import edu.esprit.utils.DataSource;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class ServiceCommentaireTache implements IService<CommentaireTache> {
    Connection cnx = DataSource.getInstance().getCnx();
    ServiceUser serviceUser = new ServiceUser();
    ServiceTache serviceTache = new ServiceTache();

    @Override
    public void ajouter(CommentaireTache ct) {
        // Check if the task exists
        Tache tache = ct.getTache();
        if (tache == null || serviceTache.getOneByID(tache.getId_T()) == null) {
            System.out.println("La tâche associée n'existe pas.");
            return;
        }

        String insertQuery = "INSERT INTO commentairetache (id_user, id_T, date_C, texte_C) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(insertQuery)) {
            ps.setInt(1, ct.getUser().getId());
            ps.setInt(2, tache.getId_T());
            ps.setDate(3, new java.sql.Date(ct.getDate_C().getTime()));
            ps.setString(4, ct.getText_C());
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("L'ajout du commentaire a échoué.");
            }
            System.out.println("Commentaire ajouté avec succès.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du commentaire: " + e.getMessage());
        }
    }

    @Override
    public void modifier(CommentaireTache ct) {
        String req = "UPDATE commentairetache SET texte_C=? WHERE id_C=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, ct.getText_C());
            ps.setInt(2, ct.getId_Cmnt());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification du commentaire: " + e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) {
        String req = "DELETE FROM commentairetache WHERE id_C=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du commentaire: " + e.getMessage());
        }
    }

    @Override
    public Set<CommentaireTache> getAll() {
        Set<CommentaireTache> commentaires = new HashSet<>();
        String req = "SELECT * FROM commentairetache";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                int id_C = rs.getInt("id_C");
                int id_user = rs.getInt("id_user");
                EndUser user = serviceUser.getOneByID(id_user);
                int id_T = rs.getInt("id_T");
                Tache tache = serviceTache.getOneByID(id_T);
                Date date_C = rs.getDate("date_C");
                String texte_C = rs.getString("texte_C");
                CommentaireTache commentaireTache = new CommentaireTache(id_C, user, tache, date_C, texte_C);
                commentaires.add(commentaireTache);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des commentaires: " + e.getMessage());
        }
        return commentaires;
    }

    @Override
    public CommentaireTache getOneByID(int id) {
        String req = "SELECT * FROM commentairetache WHERE id_C=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id_user = rs.getInt("id_user");
                    EndUser user = serviceUser.getOneByID(id_user);
                    int id_T = rs.getInt("id_T");
                    Tache tache = serviceTache.getOneByID(id_T);
                    Date date_C = rs.getDate("date_C");
                    String texte_C = rs.getString("texte_C");
                    return new CommentaireTache(id, user, tache, date_C, texte_C);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du commentaire: " + e.getMessage());
        }
        return null;
    }

    public boolean isValidC(CommentaireTache ct) throws IllegalArgumentException {
        if (ct.getText_C() == null || ct.getText_C().isEmpty()) {
            throw new IllegalArgumentException("Le commentaire est obligatoire.");
        }
        return true;
    }

    public Set<CommentaireTache> getCommentairesForTask(Tache tache) {
        Set<CommentaireTache> commentaires = new HashSet<>();
        String req = "SELECT * FROM commentairetache WHERE id_T=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, tache.getId_T());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id_C = rs.getInt("id_C");
                    int id_user = rs.getInt("id_user");
                    EndUser user = serviceUser.getOneByID(id_user);
                    Date date_C = rs.getDate("date_C");
                    String texte_C = rs.getString("texte_C");
                    CommentaireTache commentaireTache = new CommentaireTache(id_C, user, tache, date_C, texte_C);
                    commentaires.add(commentaireTache);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des commentaires: " + e.getMessage());
        }
        return commentaires;
    }
}
