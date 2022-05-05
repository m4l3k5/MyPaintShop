import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PaintApp extends Application
{
	
	static Stage stage = new Stage();
	
    public void start(Stage stage) throws Exception
    {
    	/**
    	 * opening out FXML file and loding it and setting it as the main stage
    	 */
        Parent root = FXMLLoader.load(getClass().getResource("/MyPaintShop.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("MyPaintShop (PaintTool)");
        stage.show();
       
    }
    public static void main(String[] args) {
        launch(args);
    }
    
    
}
