package bdr.projet;

import bdr.projet.helpers.Utilities;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
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
    private MenuItem mi_connect;
    @FXML
    private MenuItem mi_change;
    @FXML
    private MenuItem mi_noAdmin;
    @FXML
    private MenuItem mi_disconnect;
    @FXML
    private MenuItem mi_delete;
    @FXML
    private TabPane tp_main;
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
    private TextFlow tf_mod;
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
        setUI();
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

        setUI();
    }

    private void setConnectedUI() {
        if(connectedUser == null) return; //Just a security check

        /*Games*/
        cmb_game.getItems().setAll(db.getGames());

        cmb_game.setOnAction(actionEvent -> {
            Game gameSelected = cmb_game.getSelectionModel().getSelectedItem();
            lv_mods.getItems().setAll(db.getMods(gameSelected));
            imv_game.setImage(gameSelected.getLogo());
        });
        cmb_game.setValue(cmb_game.getItems().getFirst()); //don't need to check if empty because our app, without game on db is just a nonsense

        /*Mods*/
        lv_mods.setOnMouseClicked(mouseEvent -> {
            Mod modSelected = lv_mods.getSelectionModel().getSelectedItem();
            lv_mod.getItems().setAll(screenshotsToString(modSelected.getScreenshots()));
            lv_mod.setOnMouseClicked(mouseEvent1 -> {
                String screenshotSelected = lv_mod.getSelectionModel().getSelectedItem();
                imv_mod.setImage(screenshotSelected.equals("Screenshots:")
                        ? modSelected.getLogo()
                        : modSelected.getScreenshot(Integer.parseInt(screenshotSelected)));
            });
            tf_mod.getChildren().setAll(new Text(modSelected.getDesciption() + "\nDownload at: "), new Hyperlink(modSelected.getDownloadLink()));
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
            cmb_mod_collections.setValue(cmb_mod_collections.getItems().getFirst());


        //Update tabs
        t_home.setDisable(false);
        t_collections.setDisable(false);
        boolean allowed = connectedUser.isAdmin();
        t_manage.setDisable(!allowed);
        t_logs.setDisable(false);
        t_logs_general.setDisable(!allowed);
        tf_logs_general.setVisible(allowed);

        t_logs_user.setDisable(false);

        mi_connect.setVisible(false);
        mi_change.setVisible(true);
        mi_noAdmin.setVisible(allowed);
        mi_disconnect.setVisible(true);
        mi_delete.setVisible(true);

        tp_main.getSelectionModel().select(t_home);

        //setCSSOnConnect();
    }

    @FXML
    protected void disconnect() {
        disconnectDb();
        connectedUser = null;

        setUI();

        log(MSG_USER_DISCONNECT);
    }

    private void setUI() {
        mb.setOnMouseMoved(e->{
            m_user.hide();
        });

        if(connectedUser == null) setDisconnectedUI();
        else setConnectedUI();
    }

    private void setDisconnectedUI() {
        tp_main.getSelectionModel().select(t_logs);
        t_home.setDisable(true);
        t_collections.setDisable(true);
        t_manage.setDisable(true);
        tp_logs.getSelectionModel().select(t_logs_user);
        t_logs_general.setDisable(true);
        tf_logs_general.setVisible(false);
        t_logs_user.setDisable(true);
        tf_logs_user.setDisable(true);
        t_logs.setDisable(true);

        mi_connect.setVisible(true);
        mi_change.setVisible(false);
        mi_noAdmin.setVisible(false);
        mi_disconnect.setVisible(false);
        mi_delete.setVisible(false);

        m_user.setText("");

        //setCSSOnDisconnect();
    }

    @FXML
    protected void updatePassword() {
        String password = Popups.askText("Connection", "Step 3", MSG_USER_CONNECTION_STEP3);
        connectedUser.setPassword(Utilities.encryptSHA256(password));
        db.updateUser(connectedUser);
    }

    @FXML
    protected void setNotAdmin() {
        if (!m_user.getText().contains("Click")) return;
        connectedUser.itSTooMuchResponsibilitySorry();
        db.updateUser(connectedUser);
        setUI();
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
    protected void showLogs() {
        ModCollection mc = Popups.askElInAList("Select Mod Collection", "Select a Mod Collection to show its logs", "Mod collections:",
                new ArrayList<>(cmb_mod_collections.getItems()));
        ArrayList<String> logs = db.getModCollectionLogs(mc);
        tf_logs_user.getChildren().clear();
        for (String log : logs.reversed())
            tf_logs_user.getChildren().addFirst(new Text(log + "\n"));
        tp_logs.getSelectionModel().select(t_logs_user);
        tp_main.getSelectionModel().select(t_logs);

    }

    public void log(String message) {
        tf_logs_general.getChildren().addFirst(new Text(Utilities.getNow() + " " + message + "\n"));
    }

    public void log(Exception e) {
        Hyperlink linkException = new Hyperlink("Show details");
        linkException.setOnMouseClicked(mouseEvent -> Popups.exceptionHandle("Details", e));
        tf_logs_general.getChildren().addFirst(linkException);
        tf_logs_general.getChildren().addFirst(new Text(Utilities.getNow() + " " + e.getClass() + "\n\t"));
    }

    private ArrayList<String> screenshotsToString(ArrayList<String> screenshots) {
        ArrayList<String> res = new ArrayList<>();
        res.add("Screenshots:");
        for (int i = 1; i <= screenshots.size(); i++) res.add(String.valueOf(i));
        return res;
    }


    void setCSS() {
        //add css. ref : https://docs.oracle.com/javafx/2/css_tutorial/jfxpub-css_tutorial.htm#BJEJGIGC
        // Examples:
        /*  inline:
                l_welcome.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
            with class:
                l_welcome.getStyleClass().add("custom-css-class");
            with id:
                l_welcome.setId("custom-css-id");
         */
        //TODO clean up
        //set id
        ap_main.setId("ap-main");
        mb.setId("mb");
        m_user.setId("m-user");
        mi_change.setId("mi-change");
        mi_disconnect.setId("mi-disconnect");
        mi_delete.setId("mi-delete");
        tp_main.setId("tp-main");
        t_home.setId("t-home");
        t_collections.setId("t-collections");
        t_manage.setId("t-manage");
        t_logs.setId("t-logs");
        tf_logs_general.setId("tf-logs");
        l_mods.setId("l-mods");
        lv_mods.setId("lv-mods");
        lv_mod.setId("lv-mod");
        tf_mod.setId("tf-mod");
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

        //setCSSOnDisconnect();
    }

    /*
    private void setCSSOnConnect() {
        m_user.getStyleClass().add("whenConnected");
    }

    private void setCSSOnDisconnect() {
        m_user.getStyleClass().remove("whenConnected");
    }*/

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
        if(jdbc == null) return;
        try {
            if(jdbc.isConnect()) {
                jdbc.disconnect();
                log(MSG_DB_DISCONNECT);
            }
        } catch (SQLException | NullPointerException e) {
            log(MSG_DB_DISCONNECT);
            log(e);
        } finally {
            jdbc = null;
        }
    }
}