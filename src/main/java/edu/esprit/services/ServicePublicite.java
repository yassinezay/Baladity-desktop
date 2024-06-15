package edu.esprit.services;

import edu.esprit.entities.Actualite;
import edu.esprit.entities.EndUser;
import edu.esprit.entities.Municipality;
import edu.esprit.entities.Publicite;
import edu.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ServicePublicite implements IService<Publicite> {
    Connection cnx = DataSource.getInstance().getCnx();

    @Override
    public void ajouter(Publicite publicite) {
        // Basic input validation to ensure all required fields are filled
        if (isValidPublicite(publicite)) {
            String req = "INSERT INTO `publicite`(`titre_pub`, `description_pub`, `contact_pub`, `localisation_pub`, `image_pub`,`id_user`,`id_a`,`offre_pub`) VALUES (?,?,?,?,?,?,?,?)";
            try {
                PreparedStatement ps = cnx.prepareStatement(req);
                ps.setString(1,publicite.getTitre_pub());
                ps.setString(2,publicite.getDescription_pub());
                ps.setInt(3,publicite.getContact_pub());
                ps.setString(4,publicite.getLocalisation_pub());
                ps.setString(5,publicite.getImage_pub());
                ps.setInt(6,publicite.getEndUser().getId());
                ps.setInt(7,publicite.getActualite().getId_a());
                ps.setString(8,publicite.getOffre_pub());
                ps.executeUpdate();
                System.out.println("Publicite added!");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("remplir tous les champs");
        }

    }


    private boolean isValidPublicite(Publicite publicite) {
        return publicite != null &&
                publicite.getTitre_pub() != null && !publicite.getTitre_pub().isEmpty() &&
                publicite.getDescription_pub() != null && !publicite.getDescription_pub().isEmpty() &&
                publicite.getContact_pub() > 0 && // You may want to adjust this based on your requirements
                publicite.getLocalisation_pub() != null && !publicite.getLocalisation_pub().isEmpty() &&
                publicite.getImage_pub() != null && !publicite.getImage_pub().isEmpty() &&
                publicite.getEndUser().getId() > 0 && // You may want to adjust this based on your requirements
                publicite.getActualite().getId_a() > 0 && // You may want to adjust this based on your requirements
                publicite.getOffre_pub() != null; // Check for offre_pub

    }
    private int getDisplayDuration(String selectedOffer) {
        // Use the ServicePublicite to get the duration from the database
        ServicePublicite servicePublicite = new ServicePublicite();
        Set<Publicite> publicites = servicePublicite.getAll();

        // Find the Publicite object with the selected offer
        Publicite selectedPublicite = publicites.stream()
                .filter(publicite -> publicite.getOffre_pub().equals(selectedOffer))
                .findFirst()
                .orElse(null);

        // If the Publicite with the selected offer is found, return the duration; otherwise, use defaults
        return (selectedPublicite != null) ? selectedPublicite.getDisplayDuration() : getDefaultDisplayDuration(selectedOffer);
    }

    // Add this method to handle default display duration
    private int getDefaultDisplayDuration(String selectedOffer) {
        // Set default display duration based on the value of "offre_pub" from the database
        switch (selectedOffer) {
            case "3 mois :50dt":
            case "6 mois :90dt":
                return 1; // 6 seconds
            case "9 mois :130dt":
                return 5; // 12 seconds
            case "12 mois :170dt":
                return 25; // 18 seconds
            default:
                return 1; // Default duration if offer not recognized
        }
    }

    @Override
    public void modifier(Publicite publicite) {
        // Check if all the required fields are filled
        if (isValidPublicite(publicite)) {
            // Check if the specified Publicite ID exists before attempting to update
            if (publiciteExists(publicite.getId_pub())) {
                // Check if the associated Actualite ID exists in the actualite table
                if (publicite.getActualite() != null && actualiteExists(publicite.getActualite().getId_a())) {
                    String req = "UPDATE `publicite` SET `titre_pub`=?, `description_pub`=?, `contact_pub`=?, `localisation_pub`=? , `image_pub`=?, `id_user`=?, `id_a`=?, `offre_pub`=? WHERE `id_pub`=?";
                    try {
                        // Set up connection for transaction
                        cnx.setAutoCommit(false);

                        PreparedStatement ps = cnx.prepareStatement(req);
                        ps.setString(1, publicite.getTitre_pub());
                        ps.setString(2, publicite.getDescription_pub());
                        ps.setInt(3, publicite.getContact_pub());
                        ps.setString(4, publicite.getLocalisation_pub());
                        ps.setString(5, publicite.getImage_pub());
                        ps.setInt(6, publicite.getEndUser().getId());
                        ps.setInt(7, publicite.getActualite().getId_a());
                        ps.setString(8, publicite.getOffre_pub());
                        ps.setInt(9, publicite.getId_pub());

                        // Execute the update statement
                        ps.executeUpdate();

                        // Commit the changes
                        cnx.commit();

                        System.out.println("Publicite with ID " + publicite.getId_pub() + " modified!");
                    } catch (SQLException e) {
                        // Rollback changes in case of an exception
                        try {
                            cnx.rollback();
                        } catch (SQLException rollbackException) {
                            rollbackException.printStackTrace();
                        }
                        System.out.println(e.getMessage());
                    } finally {
                        // Reset auto-commit to true after transaction
                        try {
                            cnx.setAutoCommit(true);
                        } catch (SQLException autoCommitException) {
                            autoCommitException.printStackTrace();
                        }
                    }
                } else {
                    System.out.println("Associated Actualite is null or Actualite with ID " + publicite.getActualite().getId_a() + " does not exist.");
                }
            } else {
                System.out.println("Publicite with ID " + publicite.getId_pub() + " does not exist.");
            }
        } else {
            System.out.println("Invalid input. Please fill all required fields.");
        }
    }


    // Check if the specified actualite ID exists
    private boolean actualiteExists(int id_a) {
        String req = "SELECT COUNT(*) FROM `actualite` WHERE `id_a` = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id_a);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    public boolean numeroExists(int contact) {
        try {

            String requete = "SELECT * FROM publicite WHERE contact_pub = ?";
            PreparedStatement st = cnx.prepareStatement(requete);
            st.setInt(1, contact);
            ResultSet rs = st.executeQuery();

            return rs.next(); // Returns true if the number exists in the database

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            return false;
        }
    }



    @Override
    public Set<Publicite> getAll() {
        Set<Publicite> publicites = new HashSet<>();

        String req = "Select * from publicite";
        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                int id_pub = rs.getInt("id_pub");
                String titre_pub = rs.getString("titre_pub");
                String description_pub = rs.getString("description_pub");
                int contact_pub = rs.getInt("contact_pub");
                String localisation_pub = rs.getString("localisation_pub");
                String image_pub = rs.getString("image_pub");
                String offre_pub = rs.getString("offre_pub");
                int id_user = rs.getInt("id_user");
                int id_a = rs.getInt("id_a");

                ServiceUser serviceUser = new ServiceUser();
                EndUser endUser = serviceUser.getOneByID(id_user);

                ServiceActualite serviceActualite = new ServiceActualite();
                Actualite actualite = serviceActualite.getOneByID(id_a);

                Publicite pub = new Publicite(id_pub, titre_pub, description_pub, contact_pub, localisation_pub,image_pub,offre_pub,endUser,actualite);
                publicites.add(pub);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return publicites;

    }

    public int getCountPubByOffer(String offer) {
        int count = 0;

        String req = "SELECT COUNT(*) FROM `publicite` WHERE `offre_pub` = ?";

        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, offer); // Set the value for the placeholder
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1); // Retrieve the count from the result set
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return count;
    }


    @Override
    public Publicite getOneByID(int id) {
        String req = "SELECT * FROM `publicite` WHERE `id_pub`=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String titre_pub = rs.getString("titre_pub");
                String description_pub = rs.getString("description_pub");
                int contact_pub = rs.getInt("contact_pub");
                String localisation_pub = rs.getString( "localisation_pub");
                String image_pub = rs.getString("image_pub");
                String offre_pub = rs.getString("offre_pub");
                int id_user = rs.getInt("id_user");
                int id_a = rs.getInt("id_a");
                ServiceUser serviceUser = new ServiceUser();
                EndUser endUser = serviceUser.getOneByID(id_user);

                ServiceActualite serviceActualite = new ServiceActualite();
                Actualite actualite = serviceActualite.getOneByID(id_a);

                Publicite pub = new Publicite(id, titre_pub, description_pub, contact_pub, localisation_pub,image_pub,offre_pub,endUser,actualite);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;

    }

    private Set<Publicite> getPublicites() {
        return null;
    }
    public Set<Publicite> getByActualiteId(int actualiteId) {
        Set<Publicite> publicites = new HashSet<>();

        String req = "SELECT * FROM `publicite` WHERE `id_a`=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, actualiteId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id_pub = rs.getInt("id_pub");
                String titre_pub = rs.getString("titre_pub");
                String description_pub = rs.getString("description_pub");
                int contact_pub = rs.getInt("contact_pub");
                String localisation_pub = rs.getString("localisation_pub");
                String image_pub = rs.getString("image_pub");
                String offre_pub = rs.getString("offre_pub");
                int id_user = rs.getInt("id_user");
                int id_a = rs.getInt("id_a");

                ServiceUser serviceUser = new ServiceUser();
                EndUser endUser = serviceUser.getOneByID(id_user);

                ServiceActualite serviceActualite = new ServiceActualite();
                Actualite actualite = serviceActualite.getOneByID(id_a);

                Publicite pub = new Publicite(id_pub, titre_pub, description_pub, contact_pub, localisation_pub, image_pub, offre_pub, endUser, actualite);
                publicites.add(pub);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return publicites;
    }


    private boolean publiciteExists(int id_pub) {
        String req = "SELECT COUNT(*) FROM `publicite` WHERE `id_pub`=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id_pub);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // Returns true if the ID exists
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false; // Default to false in case of an exception
    }
    @Override
    public void supprimer(int id) {
        // Check if the specified ID exists before attempting to delete
        if (publiciteExists(id)) {
            String req = "DELETE FROM `publicite` WHERE `id_pub`=?";
            try {
                PreparedStatement ps = cnx.prepareStatement(req);
                ps.setInt(1, id);
                ps.executeUpdate();
                System.out.println("Publicite with ID " + id + " deleted!");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Publicite with ID " + id + " does not exist.");
        }
    }

}
