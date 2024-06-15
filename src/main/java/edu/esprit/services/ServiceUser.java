package edu.esprit.services;

import edu.esprit.entities.EndUser;
import edu.esprit.entities.Municipality;
import edu.esprit.utils.DataSource;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class ServiceUser implements IService<EndUser> {

    Connection cnx = DataSource.getInstance().getCnx();

    @Override
    public void ajouter(EndUser endUser) {
        String req = "INSERT INTO `enduser`(`email_user`, `nom_user`, `type_user`, `phoneNumber_user`, `id_muni`, `location_user`, `image_user`,`password`,`isBanned`, `is_verified`) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, endUser.getEmail());
            if (!isValidEmail(endUser.getEmail())) {
                throw new IllegalArgumentException("Invalid email address");
            }
            ps.setString(2, endUser.getNom());
            ps.setString(3, endUser.getType());
            ps.setString(4, endUser.getPhoneNumber());
            ps.setInt(5, endUser.getMuni().getId_muni());
            ps.setString(6, endUser.getLocation());
            ps.setString(7, endUser.getImage());
            ps.setString(8, endUser.getPassword());
            ps.setBoolean(9, false);
            ps.setBoolean(10, true);
            ps.executeUpdate();
            System.out.printf("User added!!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public EndUser authenticateUser(String email, String password) {
        String query = "SELECT * FROM `enduser` WHERE `email_user` = ? AND `password` = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id_user");
                String nom = rs.getString("nom_user");
                String type = rs.getString("type_user");
                String phoneNumber = rs.getString("phoneNumber_user");
                int id_muni = rs.getInt("id_muni");
                String location = rs.getString("location_user");
                String image = rs.getString("image_user");
                boolean isBanned = rs.getBoolean("isBanned");
                Municipality muni = new ServiceMuni().getOneByID(id_muni);
                return new EndUser(id, email, nom, password, type, phoneNumber, muni, location, image, isBanned);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null; // Authentication failed
    }

    @Override
    public void modifier(EndUser endUser) {
        String req = "UPDATE `enduser` SET `nom_user`=?, `email_user`=?, `type_user`=?, `phoneNumber_user`=?, `id_muni`=?, `location_user`=?, `image_user`=?, `password`=? WHERE `id_user`=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, endUser.getNom());
            ps.setString(2, endUser.getEmail());
            if (!isValidEmail(endUser.getEmail())) {
                throw new IllegalArgumentException("Invalid email address");
            }
            ps.setString(3, endUser.getType());
            ps.setString(4, endUser.getPhoneNumber());
            ps.setInt(5, endUser.getMuni().getId_muni());
            ps.setString(6, endUser.getLocation());
            ps.setString(7, endUser.getImage());
            ps.setString(8, endUser.getPassword());
            ps.setInt(9, endUser.getId()); // Assuming `id_user` is the primary key
            ps.executeUpdate();
            System.out.println("User updated!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void modifierUserToBanned(int userId, boolean ban) {
        String req = "UPDATE `enduser` SET `isBanned`=? WHERE `id_user`=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setBoolean(1, ban);
            ps.setInt(2, userId);
            ps.executeUpdate();
            System.out.println("User status modified!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void supprimer(int id) {
        if (id <= 0) {
            System.out.println("Invalid Muni ID. Please provide a positive integer.");
            return;
        }
        String deleteQuery = "DELETE FROM `enduser` WHERE `id_user` = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(deleteQuery);
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User deleted successfully!");
            } else {
                System.out.println("User not found or already deleted.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Set<EndUser> getAll() {
        Set<EndUser> users = new HashSet<>();
        String req = "Select * from endUser";

        Statement st = null;
        try {
            st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while(rs.next()){
                int id = rs.getInt("id_user");
                String nom = rs.getString("nom_user");
                String email = rs.getString("email_user");
                String password = rs.getString("password");
                String type = rs.getString("type_user");
                String phoneNumber = rs.getString("phoneNumber_user");
                int id_muni = rs.getInt("id_muni");
                String location = rs.getString("location_user");
                String image = rs.getString("image_user");
                boolean isBanned = rs.getBoolean("isBanned");
                Municipality muni = new ServiceMuni().getOneByID(id_muni);
                EndUser p = new EndUser(id,email,nom,password,type,phoneNumber,muni,location,image, isBanned);
                users.add(p);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return users;
    }

    @Override
    public EndUser getOneByID(int userId) {
        EndUser endUser = null;
        String query = "SELECT * FROM `enduser` WHERE `id_user` = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(query);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int id = rs.getInt("id_user");
                String nom = rs.getString("nom_user");
                String email = rs.getString("email_user");
                String password = rs.getString("password");
                String type = rs.getString("type_user");
                String phoneNumber = rs.getString("phoneNumber_user");
                int id_muni = rs.getInt("id_muni");
                String location = rs.getString("location_user");
                String image = rs.getString("image_user");
                boolean isBanned = rs.getBoolean("isBanned");
                Municipality muni = new ServiceMuni().getOneByID(id_muni);
                endUser = new EndUser(id,email,nom,password,type,phoneNumber,muni,location,image, isBanned);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return endUser;
    }


    public EndUser getOneByEmail(String mail) {
        EndUser endUser = null;
        String query = "SELECT * FROM `enduser` WHERE `email_user` = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(query);
            ps.setString(1, mail);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int id = rs.getInt("id_user");
                String nom = rs.getString("nom_user");
                String email = rs.getString("email_user");
                String password = rs.getString("password");
                String type = rs.getString("type_user");
                String phoneNumber = rs.getString("phoneNumber_user");
                int id_muni = rs.getInt("id_muni");
                String location = rs.getString("location_user");
                String image = rs.getString("image_user");
                boolean isBanned = rs.getBoolean("isBanned");
                Municipality muni = new ServiceMuni().getOneByID(id_muni);
                endUser = new EndUser(id,email,nom,password,type,phoneNumber,muni,location,image,isBanned);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return endUser;
    }


    private boolean isValidEmail(String email) {
        String regexPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return Pattern.compile(regexPattern).matcher(email).matches();
    }
}
