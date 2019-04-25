package helpers;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class OpenWindow {
    public static void openWindow(Event event, String resource, String title,
                                  int width, int height, boolean resizeable, String icon) throws IOException {
        Parent root;
        root = FXMLLoader.load(Objects.requireNonNull(OpenWindow.class.getClassLoader().getResource(resource)));
        Stage stage = new Stage();
        Scene scene = new Scene(root, width, height);
        if (icon != null) {
            stage.getIcons().add(new Image(icon));
        }
        stage.setTitle(title);
        stage.setResizable(resizeable);
        stage.setScene(scene);
        stage.show();

        // close current window
        if (event != null)
            ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    public static void openWindowMenuItem(Node label, String recource, String title,
                                          int width, int height, boolean resizeable, String icon) throws IOException {
        Parent root;
        root = FXMLLoader.load(Objects.requireNonNull(OpenWindow.class.getClassLoader().getResource(recource)));
        Stage stage = new Stage();
        Scene scene = new Scene(root, width, height);
        if (icon != null) {
            stage.getIcons().add(new Image(icon));
        }
        stage.setTitle(title);
        stage.setResizable(resizeable);
        stage.setScene(scene);
        stage.show();

        // close current window
        label.getScene().getWindow().hide();
    }
}
