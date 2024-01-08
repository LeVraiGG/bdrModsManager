package bdr.projet.beans;

import javafx.scene.image.Image;

import java.util.Objects;

import static bdr.projet.helpers.Constances.URL_IMG_NOT_FOUND;

public class Game {
    private String name;
    private String logo;

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

    public Image getLogo() {
        String imgPath = Objects.requireNonNull(getClass().getResource(URL_IMG_NOT_FOUND)).toExternalForm(); //TODO download image then -> logo == null ? URL_IMG_NOT_FOUND : logo);
        return new Image(imgPath);
    }

    public void setLogo(String logo) {
        this.logo = logo;
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
