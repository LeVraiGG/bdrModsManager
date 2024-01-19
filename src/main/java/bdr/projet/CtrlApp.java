package bdr.projet;

import bdr.projet.helpers.Transformator;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private AnchorPane ap_game_image;
    @FXML
    private AnchorPane ap_mod_image;
    @FXML
    private AnchorPane ap_collections;
    @FXML
    private AnchorPane ap_c_mod_image;
    @FXML
    private AnchorPane ap_manage;
    @FXML
    private AnchorPane ap_logs;
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
    private Tab t_home;
    @FXML
    private Tab t_collections;
    @FXML
    private Tab t_manage_db;
    @FXML
    private Tab t_logs;
    @FXML
    private Tab t_mod_description;
    @FXML
    private Tab t_mod_comments;
    @FXML
    private Label l_mods;
    @FXML
    private Label l_games;
    @FXML
    private ListView<Mod> lv_mods;
    @FXML
    private ListView<String> lv_mod;
    @FXML
    private TextFlow tf_logs;
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
    private PostgesqlJDBC jdbc;
    private DbWrk db;
    private User connectedUser;

    public void initialize() {
        setCSS();
    }

    @FXML
    protected void quit() {
        disconnect();
    }

    @FXML
    protected void connect() {
        log(connectDb()); //try to connect to db and give a feedback


        if (jdbc == null || !jdbc.isConnect()) return;

        //if db connected we can connect users (and get others information from db)
        db = new DbWrk(jdbc);
        // User connection
        boolean haveAccount = Popups.ask("Connection", "Step 1", MSG_USER_CONNECTION_STEP1);
        if (!haveAccount) {
            if (!Popups.ask("Connection", "Step 1", MSG_USER_CONNECTION_STEP1B)) return;
        }
        String username = Popups.askText("Connection", "Step 2", MSG_USER_CONNECTION_STEP2);
        String password = Popups.askText("Connection", "Step 3", MSG_USER_CONNECTION_STEP3);

        ArrayList<User> users = db.getUsers();
        User user = new User(username, Transformator.encryptSHA256(password));

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
                return;
            }
        } else {
            db.createUser(user);
            log(MSG_USER_CREATED);
        }

        //User is connected, we can set the UI
        connectedUser = user;
        m_user.setText("Connected as : " + connectedUser);

        /*Games*/
        cmb_game.getItems().setAll(db.getGames());

        cmb_game.setOnAction(actionEvent -> {
            Game gameSelected = cmb_game.getSelectionModel().getSelectedItem();
            lv_mods.getItems().setAll(db.getMods(gameSelected));
            if (lv_mods.getItems().isEmpty()){
                t_mod_description.setDisable(true);
                t_mod_comments.setDisable(true);
            }

            imv_game.setImage(gameSelected.getLogo());
        });
        cmb_game.setValue(cmb_game.getItems().get(0)); //don't need to check if empty because our app, without game on db is just a nonsense

        /*Mods*/
        lv_mods.setOnMouseClicked(mouseEvent -> {
            Mod modSelected = lv_mods.getSelectionModel().getSelectedItem();
            if(modSelected == null) {
                t_mod_description.setDisable(true);
                t_mod_comments.setDisable(true);
                return;
            } else {
                t_mod_description.setDisable(false);
                t_mod_comments.setDisable(false);
            }
            lv_mod.getItems().setAll(screenshotsToString(modSelected.getScreenshots()));
            lv_mod.setOnMouseClicked(mouseEvent1 -> {
                String screenshotSelected = lv_mod.getSelectionModel().getSelectedItem();
                imv_mod.setImage(screenshotSelected.equals("Screenshots:")
                        ? modSelected.getLogo()
                        : modSelected.getScreenshot(Integer.parseInt(screenshotSelected)));
            });
            tf_mod_description.getChildren().setAll(
                    new Text(modSelected.getDescription() + "\nUsers noted this mod " +modSelected.getNote() + "/6\nDownload at: "),
                    new Hyperlink(modSelected.getDownloadLink()));
            ArrayList<Text> t = new ArrayList<>();
            for (Comment c : modSelected.getComments()) t.add(new Text(c.toString()));
            tf_mod_comments.getChildren().setAll(t.reversed());

            imv_mod.setImage(modSelected.getLogo());
        });

        //Update tabs
        t_home.setDisable(false);
        t_collections.setDisable(false);
        //t_manage_db.setDisable(false);

        mi_change.setDisable(false);
        mi_disconnect.setDisable(false);
        mi_delete.setDisable(false);

        tp.getSelectionModel().select(t_home);
    }

    @FXML
    protected void disconnect() {
        t_home.setDisable(true);
        t_collections.setDisable(true);
        //t_manage_db.setDisable(true);
        tp.getSelectionModel().select(t_logs);

        mi_change.setDisable(true);
        mi_disconnect.setDisable(true);
        mi_delete.setDisable(true);

        m_user.setText("");
        connectedUser = null;
        log(MSG_USER_DISCONNECT);
        log(disconnectDb());
    }

    @FXML
    protected void updatePassword() {
        String password = Popups.askText("Connection", "Step 3", MSG_USER_CONNECTION_STEP3);
        connectedUser.setPassword(Transformator.encryptSHA256(password));
        db.updateUser(connectedUser);
    }

    @FXML
    protected void deleteAccount() {
        db.deleteUser(connectedUser);
        disconnect();
    }

    @FXML
    protected void comment(){

    }

    private void log(String message) {
        String now = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now());
        tf_logs.getChildren().addFirst(new Text(now + " " + message + "\n"));
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

        //set id TODO clean up
        m_user.setId("m-user");
        mi_change.setId("mi-change");
        mi_disconnect.setId("mi-disconnect");
        mi_delete.setId("mi-delete");
        tp.setId("tp");
        t_home.setId("t-home");
        t_collections.setId("t-collections");
        t_manage_db.setId("t-manage-db");
        t_logs.setId("t-logs");
        t_mod_description.setId("t-mod-description");
        t_mod_comments.setId("t-mod-comments");
        tf_logs.setId("tf-logs");
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
        ap_logs.getStyleClass().add("ap");
        ap_home.getStyleClass().add("ap");
        ap_game_image.getStyleClass().add("ap");
        ap_mod_image.getStyleClass().add("ap");
        ap_collections.getStyleClass().add("ap");
        ap_c_mod_image.getStyleClass().add("ap");
        ap_manage.getStyleClass().add("ap");
        tf_logs.getStyleClass().add("tf");
        tf_mod_comments.getStyleClass().add("tf");
        tf_mod_description.getStyleClass().add("tf");
    }

    String connectDb() {
        jdbc = new PostgesqlJDBC(URL_PSQL + DB_NAME, DB_USER, DB_PASSWORD);
        try {
            jdbc.connect();
            return MSG_DB_CONNECT_SUCCESS;
        } catch (SQLException ex) {
            return MSG_DB_CONNECT_FAILURE;
        }
    }

    String disconnectDb() {
        try {
            jdbc.disconnect();
            return MSG_DB_DISCONNECT;
        } catch (SQLException ignored) {
            return MSG_DB_DISCONNECT;
        } finally {
            jdbc = null;
        }
    }
}