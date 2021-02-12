package com.imfnly.nag;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Main extends Application {

    public static final String APPLICATION_NAME = "Nag To Sleep";
    public static final String ICON_IMAGE_FILe = "zzz.png";
    public static final Image ICON_IMAGE = Toolkit.getDefaultToolkit().getImage(ICON_IMAGE_FILe);
    public static final TrayIcon TRAY_ICON = new TrayIcon(ICON_IMAGE, APPLICATION_NAME);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Platform.setImplicitExit(false);
        primaryStage.setTitle(APPLICATION_NAME);
        Pane root = createConfigPane();
        primaryStage.setScene(new Scene(root));
        primaryStage.setOnCloseRequest(event -> {
            primaryStage.hide();
            event.consume();
        });
        primaryStage.show();

        TrayMenu menu = new TrayMenu();
        menu.setClickAction(() -> Platform.runLater(() -> toggleVisibility(primaryStage)));
        menu.addAction("Show", () -> Platform.runLater(() -> primaryStage.show()));
        menu.addAction("Hide", () -> Platform.runLater(() -> primaryStage.hide()));
        menu.addAction("Exit", () -> System.exit(0));
        NagTimer timer = new NagTimer();
        timer.start();
    }

    public static void toggleVisibility(Stage stage) {
        if (stage.isShowing()) {
            stage.hide();
        } else {
            stage.show();
        }
    }

    public static void sendNotification(String title, String message) {
        TRAY_ICON.displayMessage(title, message, MessageType.INFO);
    }

    /**
     * Creates the pane for configuring properties
     * 
     * @return pane
     */
    public static Pane createConfigPane() {
        GridPane grid = new GridPane();
        final double gridHGap = 10.0;
        grid.setHgap(gridHGap);
        ETimeKeys[] timeKeys = ETimeKeys.values();
        for (int i = 0; i < timeKeys.length; i++) {
            ETimeKeys timeKey = timeKeys[i];

            Text name = new Text(" " + timeKey.getDisplayName());
            name.setTextAlignment(TextAlignment.LEFT);
            grid.add(name, 0, i);

            TextField timeField = new TextField();
            timeField.setStyle("-fx-text-box-border: blue");
            final int textColCount = 5;
            timeField.setPrefColumnCount(textColCount);
            timeField.setText(PropertiesWrapper.getTime(timeKey).toString());
            grid.add(timeField, 1, i);

            Button button = new Button("Save");
            button.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                boolean success = PropertiesWrapper.setTime(timeKey, timeField.getText());
                if (success) {
                    timeField.setStyle("-fx-text-box-border: blue");
                } else {
                    timeField.setStyle("-fx-text-box-border: red");
                }
            });
            grid.add(button, 2, i);
        }
        return grid;
    }

}
