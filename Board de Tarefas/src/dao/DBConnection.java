package dao;

import model.Board;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "...";
    private static final String USER = "...";
    private static final String PASSWORD = "...";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Erro ao tentar conectar com o banco de dados.");
            e.printStackTrace();
            return null;
        }
    }

    public static void main (String args[]) {
        Connection conn = getConnection();
        if (conn != null) {
            System.out.println("Conectado com o banco de dados.");
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Falha na conexão com o banco de dados.");
        }
    }
}

