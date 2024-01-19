package bdr.projet.beans;

import java.util.Objects;

public class Comment {
    int id;
    String content;
    long nbLike;
    Mod mod;
    User user;

    public Comment(int id, String content, long nbLike, Mod mod, User user) {
        if (content == null || content.isEmpty() || nbLike < 0 || mod == null)
            throw new RuntimeException("Comment is invalid: " + content + ":" + nbLike + ":" + mod);
        this.id = id;
        this.content = content;
        this.nbLike = nbLike;
        this.mod = new Mod(mod); //get a copy to be sure to don't modify the original mod from here
        this.user = user == null ? null : new User(user); //same
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        if (content == null || content.isEmpty()) return;
        this.content = content;
    }

    public long getNbLike() {
        return nbLike;
    }

    public void setNbLike(long nbLike) {
        if (nbLike < 0) return;
        this.nbLike = nbLike;
    }

    public void addLike() {
        nbLike++;
    }

    public Mod getMod() {
        return mod;
    }

    public void setMod(Mod mod) {
        if (mod == null) return;
        this.mod = new Mod(mod);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user == null ? null : new User(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id == comment.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "[" + user == null ? "Deleted Account" : user + "]\n" +
                content;
    }
}
