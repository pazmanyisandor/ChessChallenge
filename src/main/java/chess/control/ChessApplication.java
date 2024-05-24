package chess.control;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;
import java.util.Objects;

/**
 * The main entry point for the Chess Game application.
 * This class extends {@link javafx.application.Application} and sets up the primary stage.
 */
public class ChessApplication extends Application {

    /**
     * The main entry point for all JavaFX applications.
     * The {@code start} method is called after the {@code init} method has returned,
     * and after the system is ready for the application to begin running.
     *
     * @param stage The primary stage for this application, onto which
     *              the application scene can be set.
     * @throws IOException If loading the FXML resource fails.
     */
    @Override
    public void start(Stage stage) throws IOException {
        Logger.info("Starting Chess Game's UI...");

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ui.fxml")));
        stage.setTitle("Chess Game");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

}