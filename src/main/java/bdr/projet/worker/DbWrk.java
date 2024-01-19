package bdr.projet.worker;

import bdr.projet.beans.Comment;
import bdr.projet.beans.Game;
import bdr.projet.beans.Mod;
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
    ArrayList<Comment> comments;

    boolean needToFetch = true;

    public DbWrk(PostgesqlJDBC jdbc) throws RuntimeException {
        if (jdbc == null || !jdbc.isConnect()) throw new RuntimeException(MSG_EX_JDBC_INVALID);
        this.jdbc = jdbc;

        games = new ArrayList<>();
        mods = new ArrayList<>();
        users = new ArrayList<>();
        comments = new ArrayList<>();

        fetchAll();
    }

    private void fetchAll() {
        if (!needToFetch) return;
        fetchGames();
        fetchMods();
        fetchUsers();
        fetchComments();
        needToFetch = false;
    }

    public void setNeedToFetch() {
        this.needToFetch = true;
    }

    private void fetchGames() {
        try {
            PreparedStatement request = jdbc.getPrepareStatement(DB_RQ_GET_GAMES);
            ResultSet rs = jdbc.R(request);

            games = new ArrayList<>();
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
        }
    }

    private ArrayList<String> fetchScreenshots(Mod mod) {
        ArrayList<String> res = new ArrayList<>();
        try {
            PreparedStatement request = jdbc.getPrepareStatement(DB_RQ_GET_SCREENSHOTS);
            request.setString(1, mod.getName());
            request.setString(2, mod.getGame().getName());
            ResultSet rs = jdbc.R(request);

            while (rs.next()) {
                res.add(rs.getString(1));
            }

            rs.close();
            request.close();
            return res;
        } catch (SQLException e) {
            return res;
        }
    }

    private double fetchNote(Mod mod) {
        try {
            PreparedStatement request = jdbc.getPrepareStatement(DB_RQ_GET_NOTE);
            request.setString(1, mod.getName());
            request.setString(2, mod.getGame().getName());
            ResultSet rs = jdbc.R(request);

            double note = -1;
            if (rs.next()) {
                return rs.getDouble(1);
            }

            rs.close();
            request.close();
            return note;
        } catch (SQLException e) {
            return -1;
        }
    }

    private void fetchMods() {
        fetchGames();

        try {
            PreparedStatement request = jdbc.getPrepareStatement(DB_RQ_GET_MODS);
            ResultSet rs = jdbc.R(request);

            mods = new ArrayList<>();
            while (rs.next()) {
                Mod m = new Mod(
                        rs.getString(1), getGame(rs.getString(2)),
                        rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getInt(6));

                int i = mods.indexOf(m);
                if (i == -1) {
                    mods.add(m);
                    i = mods.size() - 1;
                } else {
                    mods.set(i, m);
                }

                for (String screenshot : fetchScreenshots(mods.get(i))) mods.get(i).addScreenshot(screenshot);
                mods.get(i).setNote(fetchNote(mods.get(i)));

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

            users = new ArrayList<>();
            while (rs.next()) {
                User u = new User(rs.getString(1), rs.getString(2));
                int i = users.indexOf(u);
                if (i == -1) {
                    users.add(u);
                    i = users.size() - 1;
                } else {
                    users.set(i, u);
                }
            }

            rs.close();
            request.close();
        } catch (SQLException ignored) {
        }
    }

    private void fetchComments() {
        try {
            PreparedStatement request = jdbc.getPrepareStatement(DB_RQ_GET_COMMENTS);
            ResultSet rs = jdbc.R(request);

            comments = new ArrayList<>();
            while (rs.next()) {
                Comment c = new Comment(rs.getInt(1), rs.getString(2), rs.getLong(3),
                        getMod(rs.getString(4), getGame(rs.getString(5))),
                        getUser(rs.getString(6)));
                int i = comments.indexOf(c);
                if (i == -1) {
                    comments.add(c);
                    i = comments.size() - 1;
                } else {
                    comments.set(i, c);
                }

                getMod(c.getMod().getName(), c.getMod().getGame()).addComment(c);
                if(c.getUser() != null)
                    getUser(c.getUser().getUsername()).addComment(c);
            }

            rs.close();
            request.close();
        } catch (SQLException ignored) {
        }
    }

    private Mod getMod(String name, Game game) {
        ArrayList<Mod> possibleMods = getMods(game);
        for (Mod mod : possibleMods) if (mod.getName().equals(name)) return mod;
        return null;
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

    private Game getGame(String name) {
        fetchGames();
        for (Game game : games) if (game.getName().equals(name)) return game;
        return null;
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

    private User getUser(String username) {
        fetchUsers();
        for (User user : users) if (user.getUsername().equals(username)) return user;
        return null;
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
