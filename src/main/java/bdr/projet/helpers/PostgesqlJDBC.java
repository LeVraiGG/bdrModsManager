package bdr.projet.helpers;

import java.sql.*;
import java.util.Properties;

public class PostgesqlJDBC {

    Connection db;
    Properties props;
    String url;

    public PostgesqlJDBC(String url, String username, String password) {
        props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);

        this.url = url;
    }

    public void connect() throws SQLException {
        db = DriverManager.getConnection(url, props);
    }

    public boolean isConnect() {
        return db != null;
    }

    public PreparedStatement getPrepareStatement(String request) throws SQLException { //could do variable parameter but would complexity the code for nothing
        return db.prepareStatement(request);
    }

    public ResultSet R(PreparedStatement request) throws SQLException {
        return request.executeQuery();
    }

    public int CUD(PreparedStatement request) throws SQLException {
        int res = request.executeUpdate();
        request.close();
        return res;
    }

    public boolean LDD(PreparedStatement request) throws SQLException {
        boolean res = request.execute();
        request.close();
        return res;
    }

    public void disconnect() throws SQLException {
        db.close();
        db = null;
    }
}
