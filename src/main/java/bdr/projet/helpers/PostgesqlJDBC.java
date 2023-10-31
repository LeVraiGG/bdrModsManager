package bdr.projet.helpers;
import java.sql.*;
import java.util.Properties;

public class PostgesqlJDBC {

    Connection db;
    Properties props;
    String url;

    public PostgesqlJDBC(String url, String username, String password){
        props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);

        this.url = url;
    }

    public void connect() throws SQLException {
         db = DriverManager.getConnection(url, props);
    }


}
