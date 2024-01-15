package bdr.projet.beans;

import bdr.projet.helpers.Transformator;
import javafx.scene.image.Image;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;

import static bdr.projet.helpers.Constantes.URL_IMG_NOT_FOUND;

public class Game {
    private String name;
    private String logo;

    public Game(String name, String logo) {
        this.name = name;
        this.logo = logo;
    }

    // Constructeur de copie pour la copie profonde
    public Game(Game other) {
        this(other.getName(), other.getLogoUrl());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoUrl() {
        return logo;
    }

    public Image getLogo() {
        String imgPath = Objects.requireNonNull(getClass().getResource(URL_IMG_NOT_FOUND)).toExternalForm();
        Image defaultImage = new Image(imgPath);
        try {
            URL url = new URL(logo);
            return Transformator.internetUrlToImage(url, defaultImage);
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
