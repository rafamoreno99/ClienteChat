package com.rafamoreno.clientechat.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase auxiliar para lograr la conexión con nuestra base de datos
 *
 * @author Rafael Ruiz Moreno
 * @version 1.0.0
 */
public class ConexionBD {

    Connection con;

    // Nombre de la base de datos
    String db;
    // Url necesaria para la conexión
    String url;
    // Usuario y contraseña para la conexión
    String user;
    String pass;

    /**
     * Método que devuelve la conexión a la base de datos
     * @return La conexión con la base de datos
     * @throws SQLException Excepción lanzada en caso de error
     */
    public Connection conectar() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return con;
    }

    /**
     * Constructor que rellena los datos con los datos personales de nuestra base de datos
     */
    public ConexionBD() {
        url = "jdbc:mysql://localhost:3307/bdchat";
        user = "root";
        pass= "root";
        System.out.println("conectado");
    }

    /**
     * Método para desconectar la base de datos
     * @throws SQLException Excepción lanzada en caso de error
     */
    public void desconectar() throws SQLException{
        con.close();
    }


}