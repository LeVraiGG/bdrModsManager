package bdr.projet.worker;

import bdr.projet.beans.Game;
import bdr.projet.beans.Mod;
import bdr.projet.beans.ModCollection;
import bdr.projet.beans.User;
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
    ArrayList<User> users;
    ArrayList<ModCollection> currentModCollections;

    boolean needToFetch = true;

    public DbWrk(PostgesqlJDBC jdbc) throws RuntimeException {
        if (jdbc == null || !jdbc.isConnect()) throw new RuntimeException(MSG_EX_JDBC_INVALID);
        this.jdbc = jdbc;

        games = new ArrayList<>();
        mods = new ArrayList<>();
        users = new ArrayList<>();

        fetchAll();
    }

    public Game getGame(String name) {
        for (Game game : games) {
            if (game.getName().equals(name)) return game;
        }

        return null;
    }

    private void fetchAll() {
        if (!needToFetch) return;
        fetchGames();
        fetchMods();
        fetchUsers();
        needToFetch = false;
    }

    private void fetchGames() {
        try {
            PreparedStatement request = jdbc.getPrepareStatement(DB_RQ_GET_GAMES);
            ResultSet rs = jdbc.R(request);

            while (rs.next()) {
                Game g = new Game(rs.getString(1), rs.getString(2));
                int i = games.indexOf(g);
                if (i == -1) {
                    games.add(g);
                    i = games.size() - 1;
                } else {
                    games.set(i, g);
                }
            }

            rs.close();
            request.close();
        } catch (SQLException ignored) {
            System.out.println("");
        }
    }

    private void fetchMods() {
        fetchGames();

        try {
            PreparedStatement request = jdbc.getPrepareStatement(DB_RQ_GET_MODS);
            ResultSet rs = jdbc.R(request);

            while (rs.next()) {
                Mod m = new Mod(
                        rs.getString(1), getGame(rs.getString(2)),
                        rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getInt(6));

                int i = mods.indexOf(m);
                if (i == -1) {
                    mods.add(m);
                } else {
                    mods.set(i, m);
                }
            }

            rs.close();
            request.close();
        } catch (SQLException ignored) {
        }
    }

    private void fetchUsers() {
        try {
            PreparedStatement request = jdbc.getPrepareStatement(DB_RQ_GET_USERS);
            ResultSet rs = jdbc.R(request);

            while (rs.next()) {
                User u = new User(rs.getString(1), rs.getString(2));
                int i = users.indexOf(u);
                if (i == -1) {
                    users.add(u);
                } else {
                    users.set(i, u);
                }
            }

            rs.close();
            request.close();
        } catch (SQLException ignored) {
        }
    }

    private ArrayList<Mod> getModCollectionMods(ModCollection mc) {
        fetchMods();
        ArrayList<Mod> res = new ArrayList<>();

        try {
            PreparedStatement request = jdbc.getPrepareStatement(DB_RQ_GET_MOD_COLLECTIONS_MODS);
            request.setString(1, mc.getName());
            request.setString(2, mc.getUser().getUsername());
            ResultSet rs = jdbc.R(request);

            while (rs.next()) {
                Mod m = getMod(rs.getString(1), getGame(rs.getString(2)));
                if(m != null) res.add(m);
            }

            rs.close();
            request.close();
            return res;
        } catch (SQLException e) {
            return res;
        }
    }

    public ArrayList<ModCollection> getModCollection(User user) {
        try {
            currentModCollections = new ArrayList<>();
            PreparedStatement request = jdbc.getPrepareStatement(DB_RQ_GET_MOD_COLLECTIONS);
            request.setString(1, user.getUsername());
            ResultSet rs = jdbc.R(request);

            while (rs.next()) {
                ModCollection mc = new ModCollection(rs.getString(1), user, rs.getString(3),
                        rs.getString(4), rs.getString(5), getGame(rs.getString(6)));
                int i = currentModCollections.indexOf(mc);
                if (i == -1) {
                    currentModCollections.add(mc);
                    i = currentModCollections.size() - 1;
                } else {
                    currentModCollections.set(i, mc);
                }

                for(Mod m : getModCollectionMods(mc)) {
                    mc.addMod(m);
                }
            }

            rs.close();
            request.close();
            return currentModCollections;
        } catch (SQLException ignored) {
            return currentModCollections;
        }
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

    private Mod getMod(String name, Game game) {
        if(name == null || name.isBlank() || game == null) return null;
        for(Mod m : getMods(game)) {
            if(m.getName().equals(name))
                return m;
        }
        return null;
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

    public ArrayList<User> getUsers() {
        fetchUsers();

        ArrayList<User> res = new ArrayList<>();
        if (users.isEmpty()) return res;

        for (User user : users) {
            User newUser = new User(user); //deep copy because only dbWrk should be able to modify users ArrayList
            res.add(newUser);
        }

        return res;
    }

    public void createUser(User user) {
        if (user == null) return;
        try {
            PreparedStatement request = jdbc.getPrepareStatement(DB_RQ_CREATE_USER);
            request.setString(1, user.getUsername());
            request.setString(2, user.getPassword());
            request.setBoolean(3, user.isAdmin());
            int rs = jdbc.CUD(request);
            request.close();
            needToFetch = true;
        } catch (SQLException ignored) {
        }
    }

    public void updateUser(User user) {
        if (user == null) return;
        try {
            PreparedStatement request = jdbc.getPrepareStatement(DB_RQ_UPDATE_USER);
            request.setString(1, user.getPassword());
            request.setBoolean(2, user.isAdmin());
            request.setString(3, user.getUsername());
            int rs = jdbc.CUD(request);
            request.close();
            needToFetch = true;
        } catch (SQLException ignored) {
        }
    }

    public void deleteUser(User user) {
        if (user == null) return;
        try {
            PreparedStatement request = jdbc.getPrepareStatement(DB_RQ_DELETE_USER);
            request.setString(1, user.getUsername());
            int rs = jdbc.CUD(request);
            request.close();
            needToFetch = true;
        } catch (SQLException ignored) {
        }
    }

    //TODO
}
