package bdr.projet.beans;

import java.nio.file.Path;
import java.util.ArrayList;

public class ModCollection {
    private String name;
    private Game game;
    private ArrayList<Mod> collection;
    private Path folder;
    private User owner;

    public Path getGameFolder(){
        return game.getFolder();
    }

    public String getName(){
        return name;
    }

    public Game getGame(){
        return game;
    }

    public void addMod(Mod mod){
        if(collection.contains(mod)) return;
        else collection.add(mod);
    }

    public void removeMod(Mod mod){
        collection.remove(mod);
    }
}
