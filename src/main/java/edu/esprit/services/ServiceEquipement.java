package edu.esprit.services;


import edu.esprit.entities.EndUser;
import edu.esprit.entities.Equipement;
import edu.esprit.entities.Municipality;
import edu.esprit.utils.DataSource;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class ServiceEquipement implements IService<Equipement> {

    Connection cnx = DataSource.getInstance().getCnx();
    ServiceUser serviceUser = new ServiceUser();
    ServiceMuni serviceMuni = new ServiceMuni();

    @Override
    public void ajouter(Equipement equipement) {
        String req = "INSERT INTO equipement (reference_eq, nom_eq, categorie_eq, date_ajouteq, quantite_eq, image_eq, description_eq, id_user,id_muni) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, equipement.getReference_eq());
            ps.setString(2, equipement.getNom_eq());
            ps.setString(3, equipement.getCategorie_eq());
            ps.setString(4, String.valueOf(equipement.getDate_ajouteq()));
            ps.setInt(5, equipement.getQuantite_eq());
            ps.setString(6, equipement.getImage_eq());
            ps.setString(7, equipement.getDescription_eq());
            ps.setInt(8, equipement.getUser().getId());
            ps.setInt(9, equipement.getMuni().getId_muni());

            ps.executeUpdate();
            System.out.println("Equipement ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'équipement : " + e.getMessage());
        }
    }
    private boolean equipementExists(int id_equipement) {
        String req = "SELECT COUNT(*) FROM `equipement` WHERE `id_equipement`=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id_equipement);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // Retourne true si l'ID existe
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false; // Par défaut, retourne false en cas d'exception
    }
    @Override
    public void modifier(Equipement equipement) {
        if (equipementExists(equipement.getId_equipement())) {
            String req = "UPDATE equipement SET `reference_eq`=?, `nom_eq`=?, `categorie_eq`=?, `date_ajouteq`=?, `quantite_eq`=?, `image_eq`=?, `description_eq`=?, `id_user`=?, `id_muni`=? WHERE `id_equipement`=?";
            try {
                PreparedStatement ps = cnx.prepareStatement(req);
                ps.setString(1, equipement.getReference_eq());
                ps.setString(2, equipement.getNom_eq());
                ps.setString(3, equipement.getCategorie_eq());
                ps.setString(4, String.valueOf(equipement.getDate_ajouteq()));
                ps.setInt(5, equipement.getQuantite_eq());
                ps.setString(6, equipement.getImage_eq());
                ps.setString(7, equipement.getDescription_eq());
                ps.setInt(8, equipement.getUser().getId());
                ps.setInt(9, equipement.getMuni().getId_muni());
                ps.setInt(10, equipement.getId_equipement());
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("equipement avec ID " + equipement.getId_equipement() + " modifiée avec succès !");
                } else {
                    System.out.println("Échec de la modification de l'equipemnt avec ID " + equipement.getId_equipement() + ". equipement non trouvée.");
                }
            } catch (SQLException e) {
                System.out.println("Échec de la modification de l'equipement avec ID " + equipement.getId_equipement() + ". Erreur : " + e.getMessage());
            }
        } else {
            System.out.println("Equipement avec ID " + equipement.getId_equipement()+ " n'existe pas.");
        }
    }


    @Override
    public void supprimer(int id) {
        String req = "DELETE FROM equipement WHERE id_equipement=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Equipement supprimé avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'équipement : " + e.getMessage());
        }
    }


    @Override
    public Set<Equipement> getAll() {
        Set<Equipement> equipements = new HashSet<>();

        String req = "SELECT * FROM equipement";
        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                int id_equipement = rs.getInt("id_equipement");
                String reference_eq = rs.getString("reference_eq");
                String nom_eq = rs.getString("nom_eq");
                String categorie_eq = rs.getString("categorie_eq");
                Date date_ajouteq = rs.getDate("date_ajouteq");
                int quantite_eq = rs.getInt("quantite_eq");
                String image_eq = rs.getString("image_eq");
                String description_eq = rs.getString("description_eq");
                int id_user = rs.getInt("id_user");
                int id_muni = rs.getInt("id_muni");

                // Récupération de l'utilisateur associé à l'équipement
                EndUser user = serviceUser.getOneByID(id_user);
                Municipality muni = serviceMuni.getOneByID(id_muni);


                // Création de l'équipement et ajout à la liste
                Equipement equipement = new Equipement(id_equipement, reference_eq, nom_eq, categorie_eq, date_ajouteq, quantite_eq, image_eq, description_eq, user,muni);
                equipements.add(equipement);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des équipements : " + e.getMessage());
        }

        return equipements;
    }

    @Override
    public Equipement getOneByID(int id) {
        Equipement equipement = null;
        String req = "SELECT * FROM equipement WHERE id_equipement=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id_equipement = rs.getInt("id_equipement");
                String reference_eq = rs.getString("reference_eq");
                String nom_eq = rs.getString("nom_eq");
                String categorie_eq = rs.getString("categorie_eq");
                Date date_ajouteq = rs.getDate("date_ajouteq");
                int quantite_eq = rs.getInt("quantite_eq");
                String image_eq = rs.getString("image_eq");
                String description_eq = rs.getString("description_eq");
                int id_user = rs.getInt("id_user");
                int id_muni = rs.getInt("id_muni");


                // Récupération de l'utilisateur associé à l'équipement
                EndUser user = serviceUser.getOneByID(id_user);
                Municipality muni = serviceMuni.getOneByID(id_muni);

                // Création de l'équipement
                equipement = new Equipement(id_equipement, reference_eq, nom_eq, categorie_eq, date_ajouteq, quantite_eq, image_eq, description_eq, user,muni);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de l'équipement : " + e.getMessage());
        }
        return equipement;
    }
    public boolean isReferenceUnique(String reference) {
        boolean isUnique = true;
        String query = "SELECT COUNT(*) FROM equipement WHERE reference_eq = ?";

        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, reference);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            isUnique = count == 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification de l'unicité de la référence : " + e.getMessage());
        }

        return isUnique;
    }
    public Set<Equipement> searchEquipments(String searchTerm) {
        Set<Equipement> matchingEquipments = new HashSet<>();

        String req = "SELECT * FROM equipement WHERE reference_eq LIKE ? OR nom_eq LIKE ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, "%" + searchTerm + "%");
            ps.setString(2, "%" + searchTerm + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Récupérez les données de l'équipement depuis le ResultSet
                // Créez un objet Equipement et ajoutez-le à l'ensemble matchingEquipments
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche des équipements : " + e.getMessage());
        }

        return matchingEquipments;
    }

    public Set<Equipement> sortEquipmentsByCategory(String category) {
        Set<Equipement> sortedEquipments = new HashSet<>();

        String req = "SELECT * FROM equipement WHERE categorie_eq = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, category);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Récupérez les données de l'équipement depuis le ResultSet
                // Créez un objet Equipement et ajoutez-le à l'ensemble sortedEquipments
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors du tri des équipements par catégorie : " + e.getMessage());
        }

        return sortedEquipments;
    }

}



