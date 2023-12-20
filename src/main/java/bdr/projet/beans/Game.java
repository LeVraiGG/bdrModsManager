package bdr.projet.beans;

public class Game {
    private String name;

    public Game(String name) {
        this.name = name;
    }

    // Constructeur de copie pour la copie profonde
    public Game(Game other) {
        this(other.getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return ((Game) o).getName().equals(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
