package bdr.projet.beans;

import java.util.Objects;

public class Comment {
    private final int id;
    private String content;
    private long nbLike;
    private Mod mod;
    private User author;

    public Comment(int id, String content, long nbLike, Mod mod, User author) {
        if (content == null || content.isEmpty() || nbLike < 0 || mod == null)
            throw new RuntimeException("Comment is invalid: " + content + ":" + nbLike + ":" + mod);
        this.id = id;
        this.content = content;
        this.nbLike = nbLike;
        this.mod = new Mod(mod); //get a copy to be sure to don't modify the original mod from here
        this.author = author == null ? null : new User(author); //same
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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author == null ? null : new User(author);
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
        return "[" + author == null ? "Deleted Account" : author + "]\n" +
                content;
    }
}
