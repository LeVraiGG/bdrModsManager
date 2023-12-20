package bdr.projet.beans;

import java.util.Objects;

public class Mod {
    private String name;
    private Game game;

    public Mod(String name, Game game){
        if (game == null) throw new RuntimeException();
        this.name = name;
        this.game = game;
    }

    // Constructeur de copie pour la copie profonde
    public Mod(Mod other) {
        this(other.getName(), other.getGame());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mod mod = (Mod) o;
        return name.equals(mod.getName()) && game.equals(mod.getGame());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, game);
    }

    @Override
    public String toString() {
        return name; // name [min(version) - max(version)]
    }
}
