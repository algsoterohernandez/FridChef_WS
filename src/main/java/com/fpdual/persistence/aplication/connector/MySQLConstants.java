package com.fpdual.persistence.aplication.connector;

/**
 * Clase constantes relacionadas con la configuración de MySQL.
 */
public class MySQLConstants {
    /**
     * Contraseña para la conexión a la base de datos MySQL.
     */
    public static final String PASSWD = "jdbc.mysql.passwd";

    /**
     * Usuario para la conexión a la base de datos MySQL.
     */
    public static final String USER = "jdbc.mysql.user";

    /**
     * Controlador JDBC para la conexión a la base de datos MySQL.
     */
    public static final String DRIVER = "jdbc.mysql.driver";

    /**
     * Prefijo de la URL para la conexión a la base de datos MySQL.
     */
    public static final String URL_PREFIX = "jdbc.mysql.url.prefix";

    /**
     * Host de la URL para la conexión a la base de datos MySQL.
     */
    public static final String URL_HOST = "jdbc.mysql.url.host";

    /**
     * Puerto de la URL para la conexión a la base de datos MySQL.
     */
    public static final String URL_PORT = "jdbc.mysql.url.port";

    /**
     * Esquema de la URL para la conexión a la base de datos MySQL.
     */
    public static final String URL_SCHEMA = "jdbc.mysql.url.schema";

    /**
     * Habilita o deshabilita SSL en la URL para la conexión a la base de datos MySQL.
     */
    public static final String URL_SSL = "jdbc.mysql.url.ssl";

    /**
     * Habilita o deshabilita la recuperación de clave pública en la URL para la conexión a la base de datos MySQL.
     */
    public static final String ALLOW_PUBLIC_KEY_RETRIEVAL = "jdbc.mysql.url.allowPublicKeyRetrieval";

    /**
     * Habilita o deshabilita el uso de desplazamiento de zona horaria compatible con JDBC en la URL para la conexión a la base de datos MySQL.
     */
    public static final String USE_JDBC_COMPLIANT_TIMEZONE_SHIFT = "jdbc.mysql.url.useJDBCCompliantTimezoneShift";

    /**
     * Habilita o deshabilita el uso de código de fecha y hora heredado en la URL para la conexión a la base de datos MySQL.
     */
    public static final String USE_LEGACY_DATE_TIME_CODE = "jdbc.mysql.url.useLegacyDatetimeCode";

    /**
     * Zona horaria del servidor en la URL para la conexión a la base de datos MySQL.
     */
    public static final String SERVER_TIMEZONE = "jdbc.mysql.url.serverTimezone";
}
