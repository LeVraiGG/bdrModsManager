package bdr.projet.worker;

import bdr.projet.helpers.PostgesqlJDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static bdr.projet.helpers.Constances.*;

public class DbWrk {
    PostgesqlJDBC jdbc;

    public DbWrk(PostgesqlJDBC jdbc) throws RuntimeException {
        if (jdbc == null) throw new RuntimeException(MSG_EX_JDBC_NULL);
        this.jdbc = jdbc;
    }

    public ArrayList<String> getModsNames() {
        //Test function. TODO mod class and changes this function next
        try{
            PreparedStatement request = jdbc.getPrepareStatement(DB_RQ_GET_MODS);
            ResultSet rs = jdbc.R(request);

            ArrayList<String> res = new ArrayList<>();
            while (rs.next()) {
               res.add(rs.getString(1) +"."+rs.getString(2));
            }

            rs.close();
            request.close();
            return res;
        } catch (SQLException ex) {
            return null;
        }
    }

    //TODO
}
