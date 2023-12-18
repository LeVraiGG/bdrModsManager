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

    public void doRequest(String request) throws SQLException {
        int foovalue = 500;
        PreparedStatement st = db.prepareStatement(request);
        ResultSet rs = st.executeQuery();
        while (rs.next()) {

            //TODO
            System.out.print("Column 1 returned ");
            System.out.println(rs.getString(1));
        }
        rs.close();
        st.close();
    }
}
