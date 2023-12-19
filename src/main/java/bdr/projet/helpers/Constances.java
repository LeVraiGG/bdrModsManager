package bdr.projet.helpers;

import java.util.Arrays;
import java.util.HashMap;

public class Constances {
    public final static String APP_TITLE = "SUPER MODS MANAGER 2000";
    public final static String DB_USER = "u_app";
    public final static String DB_PASSWORD = "super_secret";
    public final static String DB_NAME = "db_app";

    //URL
    public final static String URL_PSQL = "jdbc:postgresql://localhost:5432/";
    public final static String URL_CSS_SHEET = "/bdr/projet/stylesheet.css";

    //MSG
    public final static String MSG_DB_CONNECT_SUCCESS = "Successfully connected to the db :)";
    public final static String MSG_DB_CONNECT_FAILURE = "Failed to connect to the db :(";
    public final static String MSG_EX_JDBC_NULL = "JDBC IS NULL!";

    //DB REQUEST
    public final static String DB_RQ_GET_MODS = "SELECT fk_game, name FROM mod;";

}
