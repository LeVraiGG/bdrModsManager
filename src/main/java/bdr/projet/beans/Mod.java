package bdr.projet.beans;

import javafx.scene.image.Image;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import static bdr.projet.helpers.Constantes.URL_IMG_NOT_FOUND;

import bdr.projet.helpers.Utilities;

public class Mod {
    private final String name;
    private final Game game;
    private ArrayList<String> images;
    private String description;
    private String downloadLink;
    private int nbDownload;
    private double note;

    private ArrayList<Comment> comments;


    public Mod(String name, Game game, String logo, String description, String downloadLink, int nbDownload) throws RuntimeException {
        this(name, game, logo, description, downloadLink, nbDownload, 3);
    }

    public Mod(String name, Game game, String logo, String description, String downloadLink, int nbDownload, double note) throws RuntimeException {
        //TODO manage version, impact, moder, dependence
        if (game == null || name == null || name.isEmpty() || downloadLink == null || downloadLink.isEmpty() || nbDownload < 0)
            throw new RuntimeException("Mod is invalid: " + game + ":" + name + ":" + downloadLink + ":" + nbDownload);
        this.name = name;
        this.game = new Game(game); //get a copy to be sure to don't modify the original game from here
        this.description = description == null ? "" : description;
        this.downloadLink = downloadLink;
        this.nbDownload = nbDownload;
        this.note = note > 6 || note < 0 ? 3 : note;

        images = new ArrayList<>();
        images.add(logo);

        comments = new ArrayList<>();
    }

    // Constructeur de copie pour la copie profonde
    public Mod(Mod other) throws RuntimeException {
        this(other.getName(), other.getGame(), other.getLogoUrl(), other.getDescription(), other.getDownloadLink(), other.getNbDownload(), other.getNote());
        images.addAll(other.getScreenshots());
        comments.addAll(other.getComments());
    }

    public String getName() {
        return name;
    }

    public Game getGame() {
        return game;
    }

    public String getLogoUrl() {
        return images.get(0);
    }

    public Image getLogo() {
        String imgPath = Objects.requireNonNull(getClass().getResource(URL_IMG_NOT_FOUND)).toExternalForm();
        Image defaultImage = new Image(imgPath);
        try {
            URL url = new URL(images.get(0));
            return Utilities.internetUrlToImage(url, defaultImage);
        } catch (MalformedURLException e) {
            return defaultImage;
        }
    }

    public void setLogo(String logo) {
        images.set(0, logo);
    }

    public ArrayList<String> getScreenshots() {
        return new ArrayList<>(images.subList(1, images.size()));
    }

    public Image getScreenshot(int id) {
        String imgPath = Objects.requireNonNull(getClass().getResource(URL_IMG_NOT_FOUND)).toExternalForm();
        Image defaultImage = new Image(imgPath);
        if (id >= images.size()) return defaultImage;

        try {
            URL url = new URL(images.get(id));
            return Utilities.internetUrlToImage(url, defaultImage);
        } catch (MalformedURLException e) {
            return defaultImage;
        }
    }

    public void addScreenshot(String image) {
        if (image == null || image.isEmpty()) return;
        images.add(image);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? "" : description;
    }

    public int getNbDownload() {
        return nbDownload;
    }

    public void setNbDownload(int nbDownload) {
        if (nbDownload < 0) return;
        this.nbDownload = nbDownload;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        if (downloadLink == null || downloadLink.isEmpty()) return;
        this.downloadLink = downloadLink;
    }

    public double getNote() {
        return note;
    }

    public void setNote(double note) {
        if (note > 6 || note < 0) return;
        this.note = note;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void addComment(Comment comment) {
        if (comment == null) return;
        this.comments.add(comment);
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
