To do list :

- [x] Connexion au départ
- [ ] Affichage des menus en fonction de la connexion (lié au rôle)
- [x] Affichage de la liste des mods
- [ ] Affichage des mods (logo, screenshots, description, notes, commentaires)
- [ ] Filtre (en fonction du jeu, impact, genre, version, ...)
- [ ] Gestion téléchargements/mises à jour/suppression (via git)
- [ ] Gestion des différentes mod collections (création, remplacement, suppression)
- [ ] Fonctionnalité de notes et commentaires
- [ ] Gestion admin/modeur de la db
- [x] Gestion du compte
- [ ] Logs
- [ ] CSS
- [ ] Quit


Error happen:

Exception in thread "JavaFX Application Thread" java.lang.NullPointerException: Cannot invoke "bdr.projet.beans.Game.getLogo()" because "gameSelected" is null
at bdr.projet/bdr.projet.CtrlApp.lambda$connect$0(CtrlApp.java:127)
Exception in thread "JavaFX Application Thread" java.lang.NullPointerException: Cannot invoke "bdr.projet.beans.Game.getLogo()" because "gameSelected" is null
at bdr.projet/bdr.projet.CtrlApp.lambda$connect$0(CtrlApp.java:127)
cmb_game.getItems().setAll(db.getGames()); //

lines 122-127
``` 
cmb_game.setOnAction(actionEvent -> {
    Game gameSelected = cmb_game.getSelectionModel().getSelectedItem();
    lv_mods.getItems().setAll(db.getMods(gameSelected));
    imv_game.setImage(gameSelected.getLogo());});```