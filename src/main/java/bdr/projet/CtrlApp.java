package bdr.projet;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static bdr.projet.helpers.Constantes.*;

import bdr.projet.beans.*;
import bdr.projet.helpers.PostgesqlJDBC;
import bdr.projet.worker.DbWrk;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class CtrlApp {


    @FXML
    private AnchorPane ap_main;
    @FXML
    private MenuBar menu;
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
    private TextFlow tf_mod;
    @FXML
    private ImageView imv_mod;
    @FXML
    private ImageView imv_game;
    @FXML
    private ComboBox<Game> cmb_game;
    private PostgesqlJDBC jdbc;

    public void initialize() {
        setCSS();
    }

    @FXML
    protected void quit() {
        if (jdbc == null) return;

        try {
            jdbc.disconnect();
        } catch (SQLException ignored) {
        }
    }

    @FXML
    protected void connect() {
        String now = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now());
        tf_logs.getChildren().add(new Text(now + " " + connectDb())); //try to connect to db and give a feedback


        if (jdbc == null || !jdbc.isConnect()) {
            tp.getSelectionModel().select(t_logs);
            return;
        }

        //if db connected we can connect users (and get others information from db)
        DbWrk db = new DbWrk(jdbc);
        // TODO connect user popups and all of that

        //User is connected, we can set the UI
        /*Games*/
        cmb_game.getItems().setAll(db.getGames());

        cmb_game.setOnAction(actionEvent -> {
            Game gameSelected = cmb_game.getSelectionModel().getSelectedItem();
            lv_mods.getItems().setAll(db.getMods(gameSelected));
            imv_game.setImage(gameSelected.getLogo());
        });
        cmb_game.setValue(cmb_game.getItems().get(0)); //don't need to check if empty because our app, without game on db is just a nonsense

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
            tf_mod.getChildren().setAll(new Text(modSelected.getDesciption() + "\nDownload at: "), new Hyperlink(modSelected.getDownloadLink())); //TODO <a>
            imv_mod.setImage(modSelected.getLogo());
        });

        //Update tabs
        t_home.setDisable(false);
        t_collections.setDisable(false);
        //t_manage_db.setDisable(false);

        tp.getSelectionModel().select(t_home);
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

        //set id
        ap_main.setId("ap.main");
        menu.setId("menu");
        tp.setId("tp");
        t_home.setId("t-home");
        t_collections.setId("t-collections");
        t_manage_db.setId("t-manage-db");
        t_logs.setId("t-logs");
        tf_logs.setId("tf-logs");
        l_mods.setId("l-mods");
        lv_mods.setId("lv-mods");
        lv_mod.setId("lv-mod");
        tf_mod.setId("tf-mod");
        imv_mod.setId("imv-mod");
        imv_game.setId("imv-game");
        l_games.setId("l-mods");
        cmb_game.setId("cmb-games");

        //set classes
        l_mods.getStyleClass().add("l");
        l_games.getStyleClass().add("l");
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
}