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
    public final static String MSG_USER_CONNECTION_STEP1 = "Have you already an account?";
    public final static String MSG_USER_CONNECTION_STEP1B = "Create a new account?";
    public final static String MSG_USER_CONNECTION_STEP2 = "Username";
    public final static String MSG_USER_CONNECTION_STEP3 = "Password";
    public final static String MSG_DB_CONNECT_SUCCESS = "Successfully connected to the db :)";
    public final static String MSG_DB_CONNECT_FAILURE = "Failed to connect to the db :(";
    public final static String MSG_USER_CONNECT_FAILURE = "User seems to be not valid.";
    public final static String MSG_USER_CREATED = "User created.";
    public final static String MSG_DB_DISCONNECT = "Terminated db connection.";
    public final static String MSG_USER_DISCONNECT = "Terminated user connection.";
    public final static String MSG_EX_JDBC_INVALID = "JDBC is null or not connected!";

    //DB REQUEST
    public final static String DB_RQ_GET_MODS = "SELECT * FROM mod;";
    public final static String DB_RQ_GET_SCREENSHOTS = "SELECT img_path FROM screenshot WHERE fk_mod_name = ? AND fk_mod_game_name = ?;";
    public final static String DB_RQ_GET_NOTE = "SELECT mean(value) FROM note WHERE fk_mod_name = ? AND fk_mod_game_name = ?;";
    public final static String DB_RQ_GET_GAMES = "SELECT name, logo FROM game;";
    public final static String DB_RQ_GET_USERS = "SELECT name, password FROM _user;";
    public final static String DB_RQ_CREATE_USER = "INSERT INTO _user VALUES (?, ?, ?);";
    public final static String DB_RQ_UPDATE_USER = "UPDATE _user SET password=?, isadmin=? WHERE name=?;";
    public final static String DB_RQ_DELETE_USER = "DELETE FROM _user WHERE name=?;";
    public final static String DB_RQ_GET_COMMENTS = "SELECT * FROM comment;";

}
