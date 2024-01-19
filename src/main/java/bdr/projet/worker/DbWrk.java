package bdr.projet.worker;

import bdr.projet.CtrlApp;
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
    CtrlApp ctrl;

    ArrayList<Game> games;
    ArrayList<Mod> mods;
    ArrayList<User> users;
    ArrayList<ModCollection> currentModCollections;

    boolean needToFetch = true;

    public DbWrk(PostgesqlJDBC jdbc, CtrlApp ctrl) throws RuntimeException {
        if (jdbc == null || !jdbc.isConnect() || ctrl == null) throw new RuntimeException(MSG_EX_JDBC_INVALID);
        this.jdbc = jdbc;
        this.ctrl = ctrl;

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
        } catch (SQLException e) {
            ctrl.log(e);
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
        } catch (SQLException e) {
            ctrl.log(e);
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
        } catch (SQLException e) {
            ctrl.log(e);
        }
    }

    public ArrayList<String> getModCollectionLogs(ModCollection mc) {
        ArrayList<String> res = new ArrayList<>();

        try {
            PreparedStatement request = jdbc.getPrepareStatement(DB_RQ_GET_MOD_COLLECTION_LOGS1 + mc.getUser().getUsername() + DB_RQ_GET_MOD_COLLECTION_LOGS2);
            request.setString(1, mc.getName());
            ResultSet rs = jdbc.R(request);

            while (rs.next()) {
                res.add("[" + rs.getTimestamp(1).toString() + "]\n\tpath=" + rs.getString(4) + "\n\tlogo="
                        + rs.getString(5) + "\n\tdescription=" + rs.getString(6)
                        + "\n\tgame=" + rs.getString(7) + "\n\tmods=" + rs.getString(8));
            }

            rs.close();
            request.close();
            return res;
        } catch (SQLException e) {
            ctrl.log(e);
            return res;
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
                if (m != null) res.add(m);
            }

            rs.close();
            request.close();
            return res;
        } catch (SQLException e) {
            ctrl.log(e);
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

                for (Mod m : getModCollectionMods(mc)) {
                    mc.addMod(m);
                }
            }

            rs.close();
            request.close();
            return currentModCollections;
        } catch (SQLException e) {
            ctrl.log(e);
            return currentModCollections;
        }
    }

    public ArrayList<Mod> getMods(Game game) {
        fetchMods();

        if (game == null) return new ArrayList<>(mods);
        ArrayList<Mod> res = new ArrayList<>();

        for (Mod mod : mods) {
            if (!mod.getGame().equals(game)) continue;
            Mod newMod = new Mod(mod); //deep copy because only dbWrk should be able to modify mods ArrayList
            res.add(newMod);
        }

        return res;
    }

    private Mod getMod(String name, Game game) {
        if (name == null || name.isBlank() || game == null) return null;
        for (Mod m : getMods(game)) {
            if (m.getName().equals(name))
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
        } catch (SQLException e) {
            ctrl.log(e);
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
        } catch (SQLException e) {
            ctrl.log(e);
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
        } catch (SQLException e) {
            ctrl.log(e);
        }
    }

    public void createModCollection(ModCollection mc) {
        if (mc == null) return;
        try {
            PreparedStatement request = jdbc.getPrepareStatement(DB_RQ_CREATE_MOD_COLLECTION);
            request.setString(1, mc.getName());
            request.setString(2, mc.getUser().getUsername());
            request.setString(3, mc.getRelative_path_to_folder());
            request.setString(4, mc.getLogoUrl());
            request.setString(5, mc.getDescription().isBlank() ? null : mc.getDescription());
            request.setString(6, mc.getGame() == null ? null : mc.getGame().getName());
            int rs = jdbc.CUD(request);
            request.close();
            needToFetch = true;
        } catch (SQLException e) {
            ctrl.log(e);
        }
    }

    public void deleteModCollection(ModCollection mc) {
        if (mc == null) return;
        try {
            PreparedStatement request = jdbc.getPrepareStatement(DB_RQ_DELETE_MOD_COLLECTION);
            request.setString(1, mc.getName());
            request.setString(2, mc.getUser().getUsername());
            int rs = jdbc.CUD(request);
            request.close();
            needToFetch = true;
        } catch (SQLException e) {
            ctrl.log(e);
        }
    }


    private void CDModCollectionMod(Mod m, ModCollection mc, String dbRqCreateModCollectionMod) {
        if (mc == null || m == null) return;
        try {
            PreparedStatement request = jdbc.getPrepareStatement(dbRqCreateModCollectionMod);
            request.setString(1, m.getName());
            request.setString(2, m.getGame().getName());
            request.setString(3, mc.getName());
            request.setString(4, mc.getUser().getUsername());
            int rs = jdbc.CUD(request);
            request.close();
            needToFetch = true;
        } catch (SQLException e) {
            ctrl.log(e);
        }
    }
    public void addModCollectionMod(Mod m, ModCollection mc) {
        CDModCollectionMod(m, mc, DB_RQ_CREATE_MOD_COLLECTION_MOD);
    }

    public void removeModCollectionMod(Mod m, ModCollection mc) {
        CDModCollectionMod(m, mc, DB_RQ_DELETE_MOD_COLLECTION_MOD);
    }

    //TODO
}
