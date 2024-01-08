package bdr.projet.helpers;

public class Constantes {
    public final static String APP_TITLE = "SUPER MODS MANAGER 2000";
    public final static String DB_USER = "u_app";
    public final static String DB_PASSWORD = "super_secret";
    public final static String DB_NAME = "db_app";

    //URL
    public final static String URL_PSQL = "jdbc:postgresql://localhost:5432/";
    public final static String URL_CSS_SHEET = "/bdr/projet/stylesheet.css";
    public final static String URL_IMG_NOT_FOUND = "/bdr/projet/images/not_found.jpg";

    //MSG
    public final static String MSG_DB_CONNECT_SUCCESS = "Successfully connected to the db :)";
    public final static String MSG_DB_CONNECT_FAILURE = "Failed to connect to the db :(";
    public final static String MSG_EX_JDBC_INVALID = "JDBC is null or not connected!";

    //DB REQUEST
    public final static String DB_RQ_GET_MODS = "SELECT * FROM mod;";
    public final static String DB_RQ_GET_GAMES = "SELECT name FROM game;";

}
