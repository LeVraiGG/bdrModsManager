package bdr.projet.beans;

import javafx.scene.image.Image;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;

import static bdr.projet.helpers.Constantes.URL_IMG_NOT_FOUND;

import bdr.projet.helpers.Utilities;

public class Game {
    private final String name;
    private String logo;

    public Game(String name, String logo) throws RuntimeException {
        if (name == null || name.isEmpty())
            throw new RuntimeException("Game is invalid: " + name);
        this.name = name;
        this.logo = logo;
    }

    // Constructeur de copie pour la copie profonde
    public Game(Game other) throws RuntimeException {
        this(other.getName(), other.getLogoUrl());
    }

    public String getName() {
        return name;
    }

    public String getLogoUrl() {
        return logo;
    }

    public Image getLogo() {
        String imgPath = Objects.requireNonNull(getClass().getResource(URL_IMG_NOT_FOUND)).toExternalForm();
        Image defaultImage = new Image(imgPath);
        try {
            URL url = new URL(logo);
            return Utilities.internetUrlToImage(url, defaultImage);
        } catch (MalformedURLException e) {
            return defaultImage;
        }
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

    public Path getFolder() {
        return null;
    }
}
