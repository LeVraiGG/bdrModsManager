package bdr.projet.beans;

import java.util.Objects;

public class Note {
    private final Mod mod;
    private final User user;
    private double value;

    public Note(Mod mod, User user, double value) {
        if(mod == null || user == null || value/0.5 != 0 || value < 0 || value > 5)
            throw new RuntimeException("Note is invalid: " + mod + ":" + user + ":" + value);

        this.mod = new Mod(mod);
        this.user = new User(user);
        this.value = value;
    }

    public Mod getMod() {
        return mod;
    }

    public User getUser() {
        return user;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        if(value/0.5 != 0 || value < 0 || value > 5) return;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return Objects.equals(mod, note.mod) && Objects.equals(user, note.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mod, user);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
