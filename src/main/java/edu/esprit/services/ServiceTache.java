package edu.esprit.services;

import edu.esprit.entities.EndUser;
import edu.esprit.entities.Tache;
import edu.esprit.utils.DataSource;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class ServiceTache implements IService<Tache> {
    Connection cnx = DataSource.getInstance().getCnx();
    ServiceUser serviceUser = new ServiceUser();
    //ServiceCategorieT servicecategorie = new ServiceCategorieT();


    @Override
    public void ajouter(Tache t) {
        String req = "INSERT INTO tache (nom_Cat, titre_T, pieceJointe_T, date_DT, date_FT, desc_T, etat_T, id_user) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);
            //ps.setInt(1, t.getCategorie().getId_Cat());
            ps.setString(1, t.getCategorie());
            ps.setString(2, t.getTitre_T());
            ps.setString(3, t.getPieceJointe_T());
            ps.setDate(4, new java.sql.Date(t.getDate_DT().getTime()));
            ps.setDate(5, new java.sql.Date(t.getDate_FT().getTime()));
            ps.setString(6, t.getDesc_T());
            ps.setString(7, t.getEtat_T().toString());
            ps.setInt(8, t.getUser().getId());
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating task failed");
            }
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    t.setId_T(generatedKeys.getInt(1));
                    System.out.println("Tache ajoutee avec succes.");
                } else {
                    throw new SQLException("Creating task failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Tache tache) {
        String req = "UPDATE tache SET nom_Cat=?, titre_T=?, pieceJointe_T=?, date_DT=?, date_FT=?, desc_T=?, etat_T=?, id_user=? WHERE id_T=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            //ps.setInt(1, tache.getCategorie().getId_Cat());
            ps.setString(1, tache.getCategorie());
            ps.setString(2, tache.getTitre_T());
            ps.setString(3, tache.getPieceJointe_T());
            ps.setDate(4, new java.sql.Date(tache.getDate_DT().getTime()));
            ps.setDate(5, new java.sql.Date(tache.getDate_FT().getTime()));
            ps.setString(6, tache.getDesc_T());
            ps.setString(7, tache.getEtat_T().toString());
            ps.setInt(8, tache.getUser().getId());
            ps.setInt(9, tache.getId_T());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(int id) {
        String req = "DELETE FROM tache WHERE id_T=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<Tache> getAll() {
        Set<Tache> t = new HashSet<>();

        String req = "SELECT * FROM tache";
        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                int id = rs.getInt("id_T");
                //int categorieId = rs.getInt("id_Cat");
                //CategorieT categorie = servicecategorie.getOneByID(categorieId);
                String categorie = rs.getString("nom_Cat");
                String titre = rs.getString("titre_T");
                String pieceJointe = rs.getString("pieceJointe_T");
                Date dateDT = rs.getDate("date_DT");
                Date dateFT = rs.getDate("date_FT");
                String desc = rs.getString("desc_T");
                EtatTache etat = EtatTache.valueOf(rs.getString("etat_T"));
                int id_user = rs.getInt("id_user");
                EndUser user = serviceUser.getOneByID(id_user);
                Tache tache = new Tache(id, categorie, titre, pieceJointe, dateDT, dateFT, desc, etat, user);
                t.add(tache);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return t;
    }

    @Override
    public Tache getOneByID(int id) {
        String req = "SELECT * FROM tache WHERE id_T = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                //int categorieId = rs.getInt("id_Cat");
                //CategorieT categorie = servicecategorie.getOneByID(categorieId);
                String categorie = rs.getString("nom_Cat");
                String titre = rs.getString("titre_T");
                String pieceJointe = rs.getString("pieceJointe_T");
                Date dateDT = rs.getDate("date_DT");
                Date dateFT = rs.getDate("date_FT");
                String desc = rs.getString("desc_T");
                EtatTache etat = EtatTache.valueOf(rs.getString("etat_T"));
                int id_user = rs.getInt("id_user");
                EndUser user = serviceUser.getOneByID(id_user);
                return new Tache(id, categorie, titre, pieceJointe, dateDT, dateFT, desc, etat, user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Set<Tache> getTachesByUser(EndUser user) {
        Set<Tache> userTaches = new HashSet<>();
        String req = "SELECT * FROM tache WHERE id_user = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, user.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                //int categorieId = rs.getInt("id_Cat");
                //CategorieT categorie = servicecategorie.getOneByID(categorieId);
                String categorie = rs.getString("nom_Cat");
                String titre = rs.getString("titre_T");
                String pieceJointe = rs.getString("pieceJointe_T");
                Date dateDT = rs.getDate("date_DT");
                Date dateFT = rs.getDate("date_FT");
                String desc = rs.getString("desc_T");
                EtatTache etat = EtatTache.valueOf(rs.getString("etat_T"));
                int id_user = rs.getInt("id_user");
                Tache r = new Tache(user, categorie, titre, pieceJointe, dateDT, dateFT, desc, etat);
                userTaches.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userTaches;
    }

    public boolean isValidT(Tache tache) throws IllegalArgumentException {
        EtatTache etatT = tache.getEtat_T();
        // Vérifier si la catégorie, le titre et l'utilisateur sont non nuls et non vides
        if (tache.getCategorie() == null) {
            throw new IllegalArgumentException("Categorie Obligatoire");
        }
        if (tache.getTitre_T() == null) {
            throw new IllegalArgumentException("Titre Obligatoire");
        }
        if (tache.getDate_DT() == null) {
            throw new IllegalArgumentException("Date Debut Obligatoire");
        }
        if (tache.getDate_FT() == null) {
            throw new IllegalArgumentException("Date fin Obligatoire");
        }
        if (tache.getDate_FT().before(tache.getDate_DT())) {
            throw new IllegalArgumentException("Date fin est avant Date Debut");
        }
        if (etatT != EtatTache.TODO && etatT != EtatTache.DOING && etatT != EtatTache.DONE) {
            throw new IllegalArgumentException("Etat de la tâche doit etre (TO_DO | DOING | DONE)");
        }
        if (tache.getUser().getId() <= 0) {
            throw new IllegalArgumentException("ID User Obligatoire");
        }
        return true;
    }

    public boolean isTitleExist(String title) {
        String req = "SELECT * FROM tache WHERE titre_T = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, title);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // If a result is found, it means the title exists
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // If an exception occurs or no result is found, return false
    }

    public Set<Tache> getTachesByState(EtatTache etat) {
        Set<Tache> taches = new HashSet<>();
        String req = "SELECT * FROM tache WHERE etat_T = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, etat.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id_T");
                //int categorieId = rs.getInt("id_Cat");
                //CategorieT categorie = servicecategorie.getOneByID(categorieId);
                String categorie = rs.getString("nom_Cat");
                String titre = rs.getString("titre_T");
                String pieceJointe = rs.getString("pieceJointe_T");
                Date dateDT = rs.getDate("date_DT");
                Date dateFT = rs.getDate("date_FT");
                String desc = rs.getString("desc_T");
                int id_user = rs.getInt("id_user");
                EndUser user = serviceUser.getOneByID(id_user);
                Tache tache = new Tache(id, categorie, titre, pieceJointe, dateDT, dateFT, desc, etat, user);
                taches.add(tache);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taches;
    }

}
