package org.apolunin.learning;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Exercise1 extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        final String initialText = "Hello, JavaFX!";
        final Label message = new Label(initialText);
        final TextField textField = new TextField(initialText);

        final BorderPane pane = new BorderPane();
        pane.setTop(textField);
        pane.setCenter(message);

        message.setFont(new Font(100));
        message.textProperty().bind(textField.textProperty());

        primaryStage.setScene(new Scene(pane));
        primaryStage.setTitle(initialText);
        primaryStage.show();
    }

}
