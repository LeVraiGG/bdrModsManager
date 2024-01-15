package bdr.projet.beans;

import bdr.projet.helpers.Transformator;
import javafx.scene.image.Image;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import static bdr.projet.helpers.Constantes.URL_IMG_NOT_FOUND;

public class Mod {
    private String name;
    private Game game;
    private ArrayList<String> images;
    private String desciption;
    private String downloadLink;
    private int nbDownload;
    private Modder creator;
    private ArrayList<Mod> dependencies;
    private String version;
    private ArrayList<String> impacts;


    public Mod(String name, Game game, String logo, String description, String downloadLink, int nbDownload) { //TODO manage versions, impact, ...
        if (game == null) throw new RuntimeException();
        this.name = name;
        this.game = game;
        images = new ArrayList<>();
        images.add(logo);
        this.desciption = description == null ? "" : description;
        this.downloadLink = downloadLink;
        this.nbDownload = nbDownload;
    }

    // Constructeur de copie pour la copie profonde
    public Mod(Mod other) {
        this(other.getName(), other.getGame(), other.getLogoUrl(), other.getDesciption(), other.getDownloadLink(), other.getNbDownload());
        images.addAll(other.getScreenshots());
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

    public String getLogoUrl() {
        return images.get(0);
    }

    public Image getLogo() {
        String imgPath = Objects.requireNonNull(getClass().getResource(URL_IMG_NOT_FOUND)).toExternalForm();
        Image defaultImage = new Image(imgPath);
        try {
            URL url = new URL(images.get(0));
            return Transformator.internetUrlToImage(url, defaultImage);
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
            return Transformator.internetUrlToImage(url, defaultImage);
        } catch (MalformedURLException e) {
            return defaultImage;
        }
    }

    public void addScreenshot(String image) {
        images.add(image);
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public int getNbDownload() {
        return nbDownload;
    }

    public void setNbDownload(int nbDownload) {
        this.nbDownload = nbDownload;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
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
