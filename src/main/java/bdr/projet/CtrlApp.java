package bdr.projet;

import bdr.projet.helpers.Utilities;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.sql.SQLException;
import java.util.ArrayList;

import static bdr.projet.helpers.Constantes.*;

import bdr.projet.helpers.Popups;
import bdr.projet.helpers.PostgesqlJDBC;
import bdr.projet.beans.*;
import bdr.projet.worker.DbWrk;

public class CtrlApp {

    @FXML
    private AnchorPane ap_main;
    @FXML
    private AnchorPane ap_home;
    @FXML
    private AnchorPane ap_game_img;
    @FXML
    private AnchorPane ap_mod_img;
    @FXML
    private AnchorPane ap_collections;
    @FXML
    private AnchorPane ap_collection_img;
    @FXML
    private AnchorPane ap_collection_vbox;
    @FXML
    private AnchorPane ap_manage;
    @FXML
    private AnchorPane ap_logs;
    @FXML
    private MenuBar mb;
    @FXML
    private Menu m_user;
    @FXML
    private MenuItem mi_change;
    @FXML
    private MenuItem mi_disconnect;
    @FXML
    private MenuItem mi_delete;
    @FXML
    private TabPane tp;
    @FXML
    private TabPane tp_logs;
    @FXML
    private Tab t_home;
    @FXML
    private Tab t_collections;
    @FXML
    private Tab t_manage;
    @FXML
    private Tab t_logs;
    @FXML
    private Tab t_mod_description;
    @FXML
    private Tab t_mod_comments;
    @FXML
    private Tab t_logs_general;
    @FXML
    private Tab t_logs_user;
    @FXML
    private Label l_mods;
    @FXML
    private Label l_games;
    @FXML
    private ListView<Mod> lv_mods;
    @FXML
    private ListView<String> lv_mod;
    @FXML
    private ListView<Mod> lv_mod_collection_mods;
    @FXML
    private ListView<Mod> lv_mods_available;
    @FXML
    private TextFlow tf_logs_general;
    @FXML
    private TextFlow tf_logs_user;
    @FXML
    private TextFlow tf_mod_description;
    @FXML
    private TextFlow tf_mod_comments;
    @FXML
    private ImageView imv_mod;
    @FXML
    private ImageView imv_game;
    @FXML
    private ComboBox<Game> cmb_game;
    @FXML
    private ComboBox<ModCollection> cmb_mod_collections;
    private PostgesqlJDBC jdbc;
    private DbWrk db;
    private User connectedUser;

    public void initialize() {
        setCSS();
    }

    @FXML
    protected void quit() {
        disconnect();
        Platform.exit();
    }

    @FXML
    protected void connect() {
        connectDb();


        if (jdbc == null || !jdbc.isConnect()) return;

        //if db connected we can connect users (and get others information from db)
        db = new DbWrk(jdbc, this);
        // User connection
        boolean haveAccount = Popups.ask("Connection", "Step 1", MSG_USER_CONNECTION_STEP1);
        if (!haveAccount) {
            if (!Popups.ask("Connection", "Step 1", MSG_USER_CONNECTION_STEP1B)) return;
        }
        String username = Popups.askText("Connection", "Step 2", MSG_USER_CONNECTION_STEP2);
        String password = Popups.askText("Connection", "Step 3", MSG_USER_CONNECTION_STEP3);

        ArrayList<User> users = db.getUsers();
        User user = new User(username, Utilities.encryptSHA256(password));

        if (haveAccount) {
            boolean validUser = false;
            for (User u : users) {
                if (u.equals(user)) {
                    validUser = true;
                    break;
                }
            }

            if (!validUser) {
                log(MSG_USER_CONNECT_FAILURE);
                Popups.error("Login Error", MSG_USER_CONNECT_FAILURE);
                return;
            }
        } else {
            db.createUser(user);
            log(MSG_USER_CREATED + " " + user);
        }

        //User is connected, we can set the UI
        connectedUser = user;
        m_user.setText("Connected as : " + connectedUser);
        log("Connected as : " + connectedUser);

        /*Games*/
        cmb_game.getItems().setAll(db.getGames());

        cmb_game.setOnAction(actionEvent -> {
            Game gameSelected = cmb_game.getSelectionModel().getSelectedItem();
            lv_mods.getItems().setAll(db.getMods(gameSelected));
            if (lv_mods.getItems().isEmpty()) {
                t_mod_description.setDisable(true);
                t_mod_comments.setDisable(true);
            }

            imv_game.setImage(gameSelected.getLogo());
        });
        cmb_game.setValue(cmb_game.getItems().get(0)); //don't need to check if empty because our app, without game on db is just a nonsense

        /*Mods*/
        lv_mods.setOnMouseClicked(mouseEvent -> {
            Mod modSelected = lv_mods.getSelectionModel().getSelectedItem();
            if (modSelected == null) {
                t_mod_description.setDisable(true);
                t_mod_comments.setDisable(true);
                return;
            } else {
                t_mod_description.setDisable(false);
                t_mod_comments.setDisable(false);
            }
            lv_mod.getItems().setAll(screenshotsToString(modSelected.getScreenshots()));
            lv_mod.setOnMouseClicked(mouseEvent1 -> {
                int index = lv_mod.getSelectionModel().getSelectedIndex();
                imv_mod.setImage(index == 0
                        ? modSelected.getLogo()
                        : modSelected.getScreenshot(index));
            });
            tf_mod_description.getChildren().setAll(
                    new Text(modSelected.getDescription() + "\nUsers noted this mod " + modSelected.getNote() + "/6\nDownload at: "),
                    new Hyperlink(modSelected.getDownloadLink()));
            ArrayList<Text> t = new ArrayList<>();
            for (Comment c : modSelected.getComments()) t.add(new Text(c.toString()));
            tf_mod_comments.getChildren().setAll(t.reversed());

            imv_mod.setImage(modSelected.getLogo());
        });

        /*Mod Collections*/
        cmb_mod_collections.getItems().setAll(db.getModCollection(connectedUser));

        cmb_mod_collections.setOnAction(actionEvent -> {
            ModCollection modCollectionSelected = cmb_mod_collections.getSelectionModel().getSelectedItem();
            lv_mod_collection_mods.getItems().setAll(modCollectionSelected.getMods());
            ArrayList<Mod> modAvailable = new ArrayList<>();
            for (Mod m : db.getMods(modCollectionSelected.getGame())) {
                if (!modCollectionSelected.getMods().contains(m)) modAvailable.add(m);
            }
            lv_mods_available.getItems().setAll(modAvailable);
        });
        if (!cmb_mod_collections.getItems().isEmpty())
            cmb_mod_collections.setValue(cmb_mod_collections.getItems().get(0));


        //Update tabs
        t_home.setDisable(false);
        t_collections.setDisable(false);
        //t_manage.setDisable(false);
        if (connectedUser.isAdmin()) {
            t_logs_general.setDisable(false);
            tf_logs_general.setVisible(true);
        }
        t_logs_user.setDisable(false);

        mi_change.setDisable(false);
        mi_disconnect.setDisable(false);
        mi_delete.setDisable(false);

        tp.getSelectionModel().select(t_home);
    }

    @FXML
    protected void disconnect() {
        t_home.setDisable(true);
        t_collections.setDisable(true);
        //t_manage.setDisable(true);
        t_logs_general.setDisable(true);
        tf_logs_general.setVisible(false);
        tp.getSelectionModel().select(t_logs);
        tf_logs_user.setDisable(true);

        mi_change.setDisable(true);
        mi_disconnect.setDisable(true);
        mi_delete.setDisable(true);

        m_user.setText("");
        connectedUser = null;
        log(MSG_USER_DISCONNECT);
    }

    @FXML
    protected void updatePassword() {
        String password = Popups.askText("Connection", "Step 3", MSG_USER_CONNECTION_STEP3);
        connectedUser.setPassword(Utilities.encryptSHA256(password));
        db.updateUser(connectedUser);
    }

    @FXML
    protected void deleteAccount() {
        db.deleteUser(connectedUser);
        disconnect();
    }

    @FXML
    protected void createCollection() {
        String name = Popups.askText(MSG_CREATE_COLLECTION_TITLE, MSG_CREATE_COLLECTION_HEADER1, MSG_CREATE_COLLECTION1);
        String path = Popups.askText(MSG_CREATE_COLLECTION_TITLE, MSG_CREATE_COLLECTION_HEADER2, MSG_CREATE_COLLECTION2);//TODO fileManager
        String logo = Popups.askText(MSG_CREATE_COLLECTION_TITLE, MSG_CREATE_COLLECTION_HEADER3, MSG_CREATE_COLLECTION3);
        String description = Popups.askText(MSG_CREATE_COLLECTION_TITLE, MSG_CREATE_COLLECTION_HEADER4, MSG_CREATE_COLLECTION4);
        ModCollection mc = new ModCollection(name, connectedUser, path, logo.isBlank() ? null : logo, description, null);
        db.createModCollection(mc);
        if (db.getModCollection(connectedUser).contains(mc)) {
            cmb_mod_collections.getItems().add(mc);
            cmb_mod_collections.getSelectionModel().select(mc);
        }
    }

    @FXML
    protected void deleteCollection() {
        ModCollection mc = cmb_mod_collections.getSelectionModel().getSelectedItem();
        db.deleteModCollection(mc);
        if (!db.getModCollection(connectedUser).contains(mc)) {
            cmb_mod_collections.getItems().remove(mc);
            if (!cmb_mod_collections.getItems().isEmpty()) cmb_mod_collections.getSelectionModel().select(0);
        }
    }

    @FXML
    protected void addSelectedMod() {
        ModCollection mc = cmb_mod_collections.getSelectionModel().getSelectedItem();
        Mod m = lv_mods_available.getSelectionModel().getSelectedItem();
        mc.addMod(m);
        if (mc.getMods().contains(m)) { //should be always true but w/e
            db.addModCollectionMod(m, mc);
            lv_mod_collection_mods.getItems().add(m);
            lv_mods_available.getItems().remove(m);
        }
    }

    @FXML
    protected void removeSelectedMod() {
        ModCollection mc = cmb_mod_collections.getSelectionModel().getSelectedItem();
        Mod m = lv_mod_collection_mods.getSelectionModel().getSelectedItem();
        mc.removeMod(m);
        if (!mc.getMods().contains(m)) { //should be always true but w/e
            db.removeModCollectionMod(m, mc);
            lv_mod_collection_mods.getItems().remove(m);
            lv_mods_available.getItems().add(m);
        }
    }

    @FXML
    protected void comment() {
        //TODO
    }

    @FXML
    protected void showLogs() {
        ModCollection mc = Popups.askElInAList("Select Mod Collection", "Select a Mod Collection to show its logs", "Mod collections:",
                new ArrayList<>(cmb_mod_collections.getItems()));
        ArrayList<String> logs = db.getModCollectionLogs(mc);
        tf_logs_user.getChildren().clear();
        for (String log : logs.reversed())
            tf_logs_user.getChildren().addFirst(new Text(log + "\n"));
        tp_logs.getSelectionModel().select(t_logs_user);
        tp.getSelectionModel().select(t_logs);
    }

    public void log(String message) {
        tf_logs_general.getChildren().addFirst(new Text("[" + Utilities.getNow() + "] " + message + "\n"));
    }

    public void log(Exception e) {
        Hyperlink linkException = new Hyperlink("\tShow details");
        linkException.setOnMouseClicked(mouseEvent -> Popups.exceptionHandle("Details", e));
        tf_logs_general.getChildren().addFirst(linkException);
        tf_logs_general.getChildren().addFirst(new Text("[" + Utilities.getNow() + "] " + e.getClass() + "\n"));
    }

    private ArrayList<String> screenshotsToString(ArrayList<String> screenshots) {
        ArrayList<String> res = new ArrayList<>();
        res.add("Logo");
        for (int i = 1; i < screenshots.size(); i++) res.add("Screenshot " + i);
        return res;
    }


    void setCSS() {
        /*add css.
          ref : https://docs.oracle.com/javafx/2/css_tutorial/jfxpub-css_tutorial.htm#BJEJGIGC

          Examples:
            inline:
                l_welcome.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
            with class:
                l_welcome.getStyleClass().add("custom-css-class");
            with id:
                l_welcome.setId("custom-css-id");
         */
        //TODO clean up
        //set id
        ap_main.setId("ap.main");
        mb.setId("mb");
        m_user.setId("m-user");
        mi_change.setId("mi-change");
        mi_disconnect.setId("mi-disconnect");
        mi_delete.setId("mi-delete");
        tp.setId("tp");
        t_home.setId("t-home");
        t_collections.setId("t-collections");
        t_manage.setId("t-manage");
        t_logs.setId("t-logs");
        t_mod_description.setId("t-mod-description");
        t_mod_comments.setId("t-mod-comments");
        tf_logs_general.setId("tf-logs-general");
        tf_logs_user.setId("tf-logs-user");
        l_mods.setId("l-mods");
        lv_mods.setId("lv-mods");
        lv_mod.setId("lv-mod");
        tf_mod_description.setId("tf-mod-description");
        tf_mod_comments.setId("tf-mod-comments");
        imv_mod.setId("imv-mod");
        imv_game.setId("imv-game");
        l_games.setId("l-mods");
        cmb_game.setId("cmb-games");

        //set classes
        ap_main.getStyleClass().add("ap");
        ap_home.getStyleClass().add("ap");
        ap_game_img.getStyleClass().add("ap");
        ap_mod_img.getStyleClass().add("ap");
        ap_collections.getStyleClass().add("ap");
        ap_collection_img.getStyleClass().add("ap");
        ap_collection_vbox.getStyleClass().add("ap");
        ap_manage.getStyleClass().add("ap");
        ap_logs.getStyleClass().add("ap");
        tf_mod_comments.getStyleClass().add("tf");
        tf_mod_description.getStyleClass().add("tf");
        tf_logs_general.getStyleClass().add("tf");
        tf_logs_user.getStyleClass().add("tf");
    }

    /**
     * try to connect to db and log result
     */
    void connectDb() {
        jdbc = new PostgesqlJDBC(URL_PSQL + DB_NAME, DB_USER, DB_PASSWORD);
        try {
            jdbc.connect();
            log(MSG_DB_CONNECT_SUCCESS);
        } catch (SQLException e) {
            log(MSG_DB_CONNECT_FAILURE);
            log(e);
        }
    }

    void disconnectDb() {
        try {
            jdbc.disconnect();
            log(MSG_DB_DISCONNECT);
        } catch (SQLException e) {
            log(MSG_DB_DISCONNECT);
            log(e);
        } finally {
            jdbc = null;
        }
    }
}