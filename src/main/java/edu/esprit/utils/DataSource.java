package edu.esprit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource implements AutoCloseable {
    private static DataSource instance;
    private final String URL = "jdbc:mysql://127.0.0.1:3306/baladiaone";
    private final String USER = "root";
    private final String PWD = "123456";
    private Connection cnx;

    public DataSource() {
        try {
            cnx = DriverManager.getConnection(URL, USER, PWD);
            System.out.println("Connected to DB !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static DataSource getInstance() {
        if (instance == null)
            instance = new DataSource();
        return instance;
    }

    public Connection getCnx() {
        return cnx;
    }

    @Override
    public void close() {
        try {
            if (cnx != null && !cnx.isClosed()) {
                cnx.close();
                System.out.println("Connection ferme !");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
