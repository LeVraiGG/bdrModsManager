package bdr.projet.beans;

import java.util.ArrayList;
import java.util.Objects;

public class User {
    private String username;
    private String password;
    private boolean isAdmin;

    private ArrayList<Comment> comments;

    public User(String username, String password, boolean isAdmin) throws RuntimeException {
        if(username == null || username.isEmpty() || password == null)
            throw new RuntimeException("User is invalid: " + username + ":" + password);
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;

        comments = new ArrayList<>();
    }

    public User(String username, String password) throws RuntimeException {
        this(username, password, false);
    }

    public User(User other) throws RuntimeException {
        this(other.getUsername(), other.getPassword(), other.isAdmin());
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if(password == null) return;
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void addComment(Comment comment) {
        if(comment == null) return;
        this.comments.add(comment);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        return username;
    }
}
