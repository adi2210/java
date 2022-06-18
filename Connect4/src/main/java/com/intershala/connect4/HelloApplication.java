package com.intershala.connect4;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    private HelloController controller;

    @Override
    public void start(Stage primarystage) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("game.fxml"));
        GridPane gridPane= loader.load();
        controller = loader.getController();
        controller.creatplayground();


        MenuBar menuBar=creatmenu();

       Pane menupane= (Pane) gridPane.getChildren().get(0);
       menupane.getChildren().addAll(menuBar);
       menuBar.prefWidthProperty().bind(primarystage.widthProperty());

        Scene scene=new Scene(gridPane);


        primarystage.setScene(scene);
        primarystage.setTitle("Connect Four");
        primarystage.setResizable(false);
        primarystage.show();

    }

    public MenuBar creatmenu(){
        Menu filemenu=new Menu("File");

        MenuItem newgame=new MenuItem("New Game");
        newgame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
               resetgame();
            }
        });
        MenuItem resetgame=new MenuItem("Reset Game");
        resetgame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                resetgame();
            }
        });
        SeparatorMenuItem separatorMenuItem=new SeparatorMenuItem();
        MenuItem exitgame=new MenuItem("Exit Game");
        exitgame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Platform.exit();
                System.exit(0);
            }
        });

        filemenu.getItems().addAll(newgame,resetgame,separatorMenuItem,exitgame);

        Menu helpmenu=new Menu("Help");

        MenuItem aboutgame=new MenuItem("About game");
        aboutgame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
              aboutgame();
            }
        });
        SeparatorMenuItem separatorMenuItem1=new SeparatorMenuItem();
        MenuItem aboutme=new MenuItem("About Me");
        aboutme.setOnAction(event-> aboutme());
        helpmenu.getItems().addAll(aboutgame,separatorMenuItem1,aboutme);

        MenuBar menuBar=new MenuBar();
        menuBar.getMenus().addAll(filemenu,helpmenu);
        return menuBar;
    }

    private void resetgame() {
        controller.resetgame();
    }

    private void aboutme() {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About The Devloper");
        alert.setHeaderText("Adi Satyajit Singh");
        alert.setContentText("I love to play with the code ");
        alert.show();
    }

    private void aboutgame() {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Connect Four");
        alert.setHeaderText("How To Play");
        alert.setContentText("Connect-Four is a tic-tac-toe-like two-player game in which players alternately place pieces on a vertical board 7 columns across and 6 rows high. ... Both players begin with 21 identical pieces, and the first player to achieve a line of four connected pieces wins the game.");
        alert.show();
    }



    public static void main(String[] args) {
        launch();
    }
}