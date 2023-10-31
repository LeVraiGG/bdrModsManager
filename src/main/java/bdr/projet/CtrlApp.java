package bdr.projet;

import bdr.projet.helpers.PostgesqlJDBC;
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

    @FXML
    protected void onHelloButtonClick() {
        initView();

        l_welcome.setText(testDb());
        tp.getSelectionModel().select(t_demo_view);
    }

    void initView() {
        setCSS();

        ArrayList<String> initValues = new ArrayList<>();
        initValues.add("Bontania");
        initValues.add("Optifine");
        initValues.add("Lorem");
        initValues.add("Ipsum");
        initValues.add("Titi");
        initValues.add("Toto");
        initValues.add("Lulu");
        initValues.add("Lala");
        initValues.add("Lolo");
        initValues.add("Lili");
        initValues.add("Lola");
        initValues.add("Lilu");
        initValues.add("Tito");


        ObservableList<String> current = lv_mods.getItems();
        current.addAll(initValues);
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

    String testDb() {
        PostgesqlJDBC mods = new PostgesqlJDBC(URL_DB_MODS, "bdr", "bdr");
        try {
            mods.connect();
            return MSG_DB_CONNECT_SUCCESS;
        } catch (SQLException ex) {
            return MSG_DB_CONNECT_FAILURE;
        }
    }
}