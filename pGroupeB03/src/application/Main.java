package application;
	
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import view.MainView;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			MainView root = new MainView();
			Scene scene = new Scene(root,1024,576);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.getIcons().addAll(new Image("file:./src/resources/images/logo.png"));
			primaryStage.setTitle("FOUR THE WIN");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
