package bdr.projet.beans;

public class Modder extends User {
    private String pseudo;

    public Modder(String username, String password, boolean isAdmin) {
        super(username, password, isAdmin);
    }
}
