package bdr.projet.beans;

import bdr.projet.helpers.Transformator;
import javafx.scene.image.Image;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import static bdr.projet.helpers.Constantes.URL_IMG_NOT_FOUND;

public class ModCollection {
    private final String name;
    private final User user;
    private String relative_path_to_folder;
    private String logo;
    private String description;
    private Game game;
    private ArrayList<Mod> mods;


    public ModCollection(String name, User user, String relative_path_to_folder, String logo, String description, Game game) { //TODO manage mods
        if (name == null || name.isBlank() || relative_path_to_folder == null || relative_path_to_folder.isBlank() || user == null)
            throw new RuntimeException("ModCollection is invalid: " + name + ":" + relative_path_to_folder + ":" + user);
        this.name = name;
        this.user = new User(user);
        this.relative_path_to_folder = relative_path_to_folder;
        this.logo = logo;
        this.description = description == null ? "" : description;
        this.game = game;

        mods = new ArrayList<>();
    }

    // Constructeur de copie pour la copie profonde
    public ModCollection(ModCollection other) {
        this(other.getName(), other.getUser(), other.getRelative_path_to_folder(), other.getLogoUrl(), other.getDescription(), other.getGame());
        mods.addAll(other.getMods());
    }

    public String getName() {
        return name;
    }

    public User getUser() {
        return user;
    }

    public String getRelative_path_to_folder() {
        return relative_path_to_folder;
    }

    public void setRelative_path_to_folder(String relative_path_to_folder) {
        if(relative_path_to_folder == null || relative_path_to_folder.isBlank()) return;
        this.relative_path_to_folder = relative_path_to_folder;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public ArrayList<Mod> getMods() {
        return new ArrayList<Mod>(mods);
    }

    public void addMod(Mod mod) {
        if(mod == null || !mod.getGame().equals(game) || mods.contains(mod)) return;
        if(mods.isEmpty()) game = mod.getGame();
        mods.add(mod);
    }

    public void removeMod(Mod mod) {
        mods.removeIf(m -> m.equals(mod));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModCollection that = (ModCollection) o;
        return Objects.equals(name, that.name) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, user);
    }

    @Override
    public String toString() {
        return name;
    }
}
