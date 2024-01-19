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
    public final static String MSG_CREATE_COLLECTION_TITLE = "Create a Collection";
    public final static String MSG_CREATE_COLLECTION_HEADER1 = "Choose a Name";
    public final static String MSG_CREATE_COLLECTION_HEADER2 = "Choose a path";
    public final static String MSG_CREATE_COLLECTION_HEADER3 = "Choose a logo (if no logo let empty else internet link of the logo)";
    public final static String MSG_CREATE_COLLECTION_HEADER4 = "Choose a description (if no description let empty)";
    public final static String MSG_CREATE_COLLECTION1 = "Name:";
    public final static String MSG_CREATE_COLLECTION2 = "Path:";
    public final static String MSG_CREATE_COLLECTION3 = "Logo:";
    public final static String MSG_CREATE_COLLECTION4 = "Description:";
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
    public final static String DB_RQ_GET_MOD_COLLECTION_LOGS1 = "SELECT * FROM ";
    public final static String DB_RQ_GET_MOD_COLLECTION_LOGS2 = "_view  WHERE fk_mod_collection_name=?;";
    public final static String DB_RQ_CREATE_USER = "INSERT INTO _user VALUES (?, ?, ?);";
    public final static String DB_RQ_UPDATE_USER = "UPDATE _user SET password=?, isadmin=? WHERE name=?;";
    public final static String DB_RQ_DELETE_USER = "DELETE FROM _user WHERE name=?;";
    public final static String DB_RQ_GET_MOD_COLLECTIONS = "SELECT * FROM mod_collection WHERE fk_user=?;";
    public final static String DB_RQ_GET_MOD_COLLECTIONS_MODS = "SELECT fk_mod_name, fk_mod_game_name FROM mod_mod_collection WHERE fk_mod_collection_name=? AND fk_mod_collection_user_name=?;";
    public final static String DB_RQ_CREATE_MOD_COLLECTION = "INSERT INTO mod_collection VALUES (?, ?, ?, ?, ?, ?);";
    public final static String DB_RQ_DELETE_MOD_COLLECTION = "DELETE FROM mod_collection WHERE name=? AND fk_user=?;";
    public final static String DB_RQ_CREATE_MOD_COLLECTION_MOD = "INSERT INTO mod_mod_collection VALUES (?, ?, ?, ?);";
    public final static String DB_RQ_DELETE_MOD_COLLECTION_MOD = "DELETE FROM mod_mod_collection WHERE fk_mod_name=? AND fk_mod_game_name=? AND fk_mod_collection_name=? AND fk_mod_collection_user_name=?;";
    public final static String DB_RQ_GET_COMMENTS = "SELECT * FROM comment;";

}
