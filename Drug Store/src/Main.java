import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("views/identification.fxml"));
        primaryStage.getIcons().add(new Image("resources/pictures/Identity.png"));
        primaryStage.setTitle("Apteka");
        primaryStage.setScene(new Scene(root, 400, 450));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
