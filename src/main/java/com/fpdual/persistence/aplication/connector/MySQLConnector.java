package com.fpdual.persistence.aplication.connector;

import lombok.Getter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase para establecer una conexión con una base de datos MySQL.
 */
public class MySQLConnector {

    /**
     * Propiedades de configuración para la conexión.
     */
    @Getter
    private Properties prop = new Properties();

    /**
     * Constructor de la clase. Carga las propiedades de configuración desde el archivo "config.properties".
     */
    public MySQLConnector() {
        try {
            prop.load(getClass().getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene una conexión a la base de datos MySQL.
     *
     * @return Objeto Connection que representa la conexión establecida.
     * @throws ClassNotFoundException Si no se encuentra el controlador de la base de datos.
     * @throws SQLException           Si ocurre un error al establecer la conexión.
     */
    public Connection getMySQLConnection() throws ClassNotFoundException, SQLException {
        try {
            Class.forName(prop.getProperty(MySQLConstants.DRIVER));
            return DriverManager.getConnection(getURL());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Construye la URL de conexión a la base de datos MySQL a partir de las propiedades de configuración.
     *
     * @return URL de conexión a la base de datos MySQL.
     */
    private String getURL() {
        return new StringBuilder()
                .append(prop.getProperty(MySQLConstants.URL_PREFIX))
                .append(prop.getProperty(MySQLConstants.URL_HOST)).append(":")
                .append(prop.getProperty(MySQLConstants.URL_PORT)).append("/")
                .append(prop.getProperty(MySQLConstants.URL_SCHEMA)).append("?user=")
                .append(prop.getProperty(MySQLConstants.USER)).append("&password=")
                .append(prop.getProperty(MySQLConstants.PASSWD)).append("&useSSL=")
                .append(prop.getProperty(MySQLConstants.URL_SSL)).append(("&allowPublicKeyRetrieval="))
                .append(prop.getProperty(MySQLConstants.ALLOW_PUBLIC_KEY_RETRIEVAL)).append(("&useJDBCCompliantTimezoneShift="))
                .append(prop.getProperty(MySQLConstants.USE_JDBC_COMPLIANT_TIMEZONE_SHIFT)).append(("&useLegacyDatetimeCode="))
                .append(prop.getProperty(MySQLConstants.USE_LEGACY_DATE_TIME_CODE)).append(("&serverTimezone="))
                .append(prop.getProperty(MySQLConstants.SERVER_TIMEZONE)).toString();
    }
}
