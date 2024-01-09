package bdr.projet.worker;

import bdr.projet.beans.Game;
import bdr.projet.beans.Mod;
import bdr.projet.helpers.PostgesqlJDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static bdr.projet.helpers.Constantes.*;

public class DbWrk {
    PostgesqlJDBC jdbc;

    ArrayList<Game> games;
    ArrayList<Mod> mods;

    boolean needToFetchMods = true, needToFetchGames = true;

    public DbWrk(PostgesqlJDBC jdbc) throws RuntimeException {
        if (jdbc == null || !jdbc.isConnect()) throw new RuntimeException(MSG_EX_JDBC_INVALID);
        this.jdbc = jdbc;

        games = new ArrayList<>();
        mods = new ArrayList<>();

        fetchAll();
    }

    public Game getGame(String name) {
        for (Game game : games) {
            if (game.getName().equals(name)) return game;
        }

        return null;
    }

    private void fetchAll() {
        fetchGames();
        fetchMods();
    }

    private void fetchGames() {
        if(!needToFetchGames) return;

        try {
            PreparedStatement request = jdbc.getPrepareStatement(DB_RQ_GET_GAMES);
            ResultSet rs = jdbc.R(request);

            while (rs.next()) {
                Game g = new Game(rs.getString(1), rs.getString(2));
                int i = games.indexOf(g);
                if(i == -1){
                    games.add(g);
                }else{
                    games.set(i, g);
                }
            }

            rs.close();
            request.close();
            needToFetchGames = false;
        } catch (SQLException ignored) {
        }
    }

    private void fetchMods() {
        //Test function. TODO mod class and changes this function next
        if(!needToFetchMods) return;
        fetchGames();

        try {
            PreparedStatement request = jdbc.getPrepareStatement(DB_RQ_GET_MODS);
            ResultSet rs = jdbc.R(request);

            while (rs.next()) { //TODO DEBUG
                Mod m = new Mod(
                        rs.getString(1), getGame(rs.getString(2)),
                        rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getInt(6));
                        
                int i = mods.indexOf(m);
                if(i == -1){
                    mods.add(m);
                }else{
                    mods.set(i, m);
                }
            }

            rs.close();
            request.close();
            needToFetchMods = false;
        } catch (SQLException ignored) {
        }
    }

    public ArrayList<Mod> getMods() {
        return getMods(null);
    }

    public ArrayList<Mod> getMods(Game game) {
        fetchMods();

        ArrayList<Mod> res = new ArrayList<>();
        if (mods.isEmpty()) return res;

        for (Mod mod : mods) {
            if (!mod.getGame().equals(game)) continue;
            Mod newMod = new Mod(mod); //deep copy because only dbWrk should be able to modify mods ArrayList
            res.add(newMod);
        }

        return res;
    }

    public ArrayList<Game> getGames() {
        fetchGames();

        ArrayList<Game> res = new ArrayList<>();
        if (games.isEmpty()) return res;

        for (Game game : games) {
            Game newGame = new Game(game); //deep copy because only dbWrk should be able to modify games ArrayList
            res.add(newGame);
        }

        return res;
    }

    //TODO
}
