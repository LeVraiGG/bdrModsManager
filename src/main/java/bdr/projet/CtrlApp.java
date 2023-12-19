package bdr.projet;

import bdr.projet.helpers.PostgesqlJDBC;
import bdr.projet.worker.DbWrk;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLException;
import java.util.ArrayList;

import static bdr.projet.helpers.Constances.*;

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
    private Tab t_demo_view;
    @FXML
    private Label l_welcome;
    @FXML
    private Label l_mods;
    @FXML
    private ListView<String> lv_mods;

    private PostgesqlJDBC mods;

    @FXML
    protected void quit() {
        if (mods == null) return;

        try {
            mods.disconnect();
        } catch (SQLException ignored) {
        }
    }

    @FXML
    protected void onHelloButtonClick() {
        l_welcome.setText(connectDb());
        initView();

        tp.getSelectionModel().select(t_demo_view);
    }

    void initView() {
        setCSS();
        ArrayList<String> modsName;
        
        if(mods == null || !mods.isConnect()) {
            modsName = new ArrayList<>();
            modsName.add("Bontania");
            modsName.add("Optifine");
            modsName.add("Lorem");
            modsName.add("Ipsum");
            modsName.add("Titi");
            modsName.add("Toto");
            modsName.add("Lulu");
            modsName.add("Lala");
            modsName.add("Lolo");
            modsName.add("Lili");
            modsName.add("Lola");
            modsName.add("Lilu");
            modsName.add("Tito");
        } else {
            modsName = new DbWrk(mods).getModsNames();
            if (modsName == null) modsName = new ArrayList<>();
        }

        ObservableList<String> current = lv_mods.getItems();
        current.addAll(modsName);
        lv_mods.setItems(current);
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
        t_demo_view.setId("t-demo-view");
        l_welcome.setId("l-welcome");
        l_mods.setId("l-mods");
        lv_mods.setId("lv-mods");

        //set classes
        l_welcome.getStyleClass().add("l");
        l_mods.getStyleClass().add("l");
    }

    String connectDb() {
        mods = new PostgesqlJDBC(URL_PSQL+DB_NAME, DB_USER, DB_PASSWORD);
        try {
            mods.connect();
            return MSG_DB_CONNECT_SUCCESS;
        } catch (SQLException ex) {
            return MSG_DB_CONNECT_FAILURE;
        }
    }
}