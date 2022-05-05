
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;

public class Controller implements Initializable {

	/**
	 * Initializing all variables that will be used in the main user interface
	 */
	@FXML
	private AnchorPane canvas;
	@FXML
	private Rectangle squareIcon;
	@FXML
	private Circle circleIcon;
	@FXML
	private Line lineIcon;
	@FXML
	private Polygon triangleIcon;
	@FXML
	private Rectangle background;
	@FXML
	private ColorPicker backgroundColorPicker;
	@FXML
	private AnchorPane toolbar;
	@FXML
	private ColorPicker colorPicker;
	@FXML
	private ColorPicker colorPickerBrush;
	@FXML
	private VBox brushSizeBox;
	@FXML
	private Button frameColor;
	@FXML
	private Button fillColor;
	@FXML
	private Label hideLabel;
	@FXML
	private Button RemoveShapeButton;
	@FXML
	private Button UndoButton;
	@FXML
	private CheckBox paintModeCheckBox;
	@FXML
	private TextField widthBox;
	@FXML
	private TextField hightBox;
	@FXML
	private TextField rotationBox;
	@FXML
	private Slider rotationSlider;
	@FXML
	private Slider brushSizeSlider;
	@FXML
	private MenuItem menuNew;
	@FXML
	private MenuItem menuOpen;
	@FXML
	private MenuItem menuSaveAs;
	@FXML
	private MenuItem menuQuit;

	@SuppressWarnings("unused")
	private double x;
	@SuppressWarnings("unused")
	private double y;
	private ArrayList<Shape> shapes = new ArrayList<Shape>();
	private ArrayList<Integer> undos = new ArrayList<Integer>();
	boolean showToolbar;
	boolean paintMode;
	int idCounter;// shapes id's
	int selectedID;

	/**
	 * Will handle the new canvas button from the menu on the top
	 * 
	 * @param event
	 */
	@FXML
	public void menuNewHandler(ActionEvent event) {
		// We erase all shapes on the canvas
		for (int i = 0; i < shapes.size(); i++) {
			shapes.get(i).setVisible(false);
		}
		/**
		 * And then link the array of shapes to a new one so the old one will be removed
		 */
		shapes = new ArrayList<Shape>();
		/**
		 * Resetting the counter and selected shape ID and remove all undo traces as
		 * well as resetting the background
		 */

		idCounter = 0;
		selectedID = 0;
		undos.clear();
		background.setFill(javafx.scene.paint.Color.WHITE);

	}

	/**
	 * Will handle the file opening button on the to list on the file menu
	 * 
	 * @param event
	 */
	@FXML
	public void menuOpenHandler(ActionEvent event) {
		// Resetting the shapes array as well as the counter and the current selected
		// shape
		shapes.clear();
		idCounter = 0;
		selectedID = 0;
		// Resetting the undo traces
		undos.clear();
		/**
		 * This will handle opening a new file as an Image and will place it as a
		 * background so it can be edited by the user
		 */

		FileChooser fc = new FileChooser();
		File image = fc.showOpenDialog(PaintApp.stage);
		String path = image.getPath().toString();
		path = path.replace("\\", "/");
		System.out.println(path);
		Image img = new Image("file:" + image.getPath());
		background.setFill(new ImagePattern(img));
	}

	/**
	 * Will exit the program on clicking quit option on the top menu
	 * 
	 */
	@FXML
	public void menuQuitHandler(ActionEvent event) {
		System.exit(1);
	}

	/**
	 * This will handle the save as button on the top of the stage where the user
	 * can specify a directory as well as a name for the saved file
	 */

	@FXML
	public void menuSaveAsHandler(ActionEvent event) throws IOException {
		WritableImage image = canvas.snapshot(new SnapshotParameters(), null);
		FileChooser fc = new FileChooser();
		File file = fc.showSaveDialog(PaintApp.stage);
		ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
	}

	/**
	 * This method is main objective to trigger the paint mode on the canvas so the
	 * user can toggel the drawing mode on the canvas upon clicking on the canvas
	 */

	public void canvasClicked(MouseEvent event) {
		paintMode = !paintMode;
	}

	/**
	 * This will allow the user to draw using the brush by tracking the mouse
	 * position and adding rectangles of the desired size using a javafx slider
	 * [1-20] range and choosing specific colors using Color picker JAVAFX, whenever
	 * the paintMode is triggered by the method above and the paint mode check box
	 * is selected
	 */

	public void canvasPaintMode(MouseEvent event) {
		if (paintModeCheckBox.isSelected() && paintMode) {
			x = event.getX();
			y = event.getY();

			Rectangle s = new Rectangle(brushSizeSlider.getValue(), brushSizeSlider.getValue());
			s.setArcWidth(2);
			s.setFill(colorPickerBrush.getValue());
			s.setLayoutX(event.getSceneX() - 144);
			s.setLayoutY(event.getSceneY() - 40);
			canvas.getChildren().add(s);
		}

	}

	/**
	 * This will show the javafx VBox that is underneath the paintmode checkbox
	 * which going to include the options for the brush (size,color)
	 */

	public void paintModePressed(MouseEvent e) {
		if (paintModeCheckBox.isSelected()) {
			toolbar.setVisible(false);
			brushSizeBox.setVisible(true);
		} else {
			brushSizeBox.setVisible(false);
		}

	}

	/**
	 * This method is going to create a new rectangle whenever the icon of the
	 * rectangle on the left is dragged and placed on the canvas and it includes
	 * setting handlers to allow the user to drag and move current placed shapes on
	 * the canvas as well as it will change the selectedID so that the user can
	 * change the properties of the selected shape
	 */

	public void squareDragged(MouseEvent e) {
		/**
		 * Creating rectangle and initializing to default properties
		 */

		Rectangle s = new Rectangle(80, 80);
		s.setId("" + idCounter++);
		s.setFill(javafx.scene.paint.Color.TRANSPARENT);
		s.setStrokeWidth(5.5);
		s.setStroke(javafx.scene.paint.Color.BLACK);
		/**
		 * Setting mouse event which will allow the user to drag the shape across the
		 * canvas
		 */

		s.setOnMouseDragged(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent arg0) {
				s.setLayoutX(arg0.getSceneX() - 144);
				s.setLayoutY(arg0.getSceneY() - 50);

			}
		});

		/**
		 * Setting a mouse even upon clicking which will make the shape react and set
		 * 
		 * the selected id to its ID as well as showing the toolbar so the user can
		 * change the properties on the toolbar
		 */
		s.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				System.out.println("selected = " + s.getId() + "\t Type: " + s.getClass().toString());
				selectedID = Integer.parseInt(s.getId());
				toolbar.setVisible(true);

			}
		});

		/**
		 * placing the rectangle on the dropped place on the canvas
		 */
		s.setLayoutX(e.getSceneX() - 144);
		s.setLayoutY(e.getSceneY() - 50);
		/**
		 * adding the shape to the canvas as well as the array to track it later when
		 * the user edit its properties
		 */

		canvas.getChildren().add(s);
		shapes.add(s);

	}

	/**
	 * This method is going to create a new circle whenever the icon of the circle
	 * on the left is dragged and placed on the canvas and it includes setting
	 * handlers to allow the user to drag and move current placed shapes on the
	 * canvas as well as it will change the selectedID so that the user can change
	 * the properties of the selected shape
	 * 
	 *
	 */

	public void circleDragged(MouseEvent e) {
		/**
		 * Creating circle and initializing to default properties
		 */

		Circle s = new Circle(40);
		s.setFill(javafx.scene.paint.Color.TRANSPARENT);
		s.setStrokeWidth(5.5);
		s.setStroke(javafx.scene.paint.Color.BLACK);
		s.setId("" + idCounter++);
		s.setLayoutX(e.getSceneX() - 144);
		s.setLayoutY(e.getSceneY() - 50);
		/**
		 * Setting mouse event which will allow the user to drag the shape across the
		 * canvas
		 */
		s.setOnMouseDragged(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent arg0) {
				s.setLayoutX(arg0.getSceneX() - 144);
				s.setLayoutY(arg0.getSceneY() - 50);

			}
		});
		/**
		 * Setting a mouse even upon clicking which will make the shape react and set
		 * the selected id to its ID as well as showing the toolbar so the user can
		 * change the properies on the toolbar
		 */

		s.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				System.out.println("selected = " + s.getId() + "\t Type: " + s.getClass().toString());
				selectedID = Integer.parseInt(s.getId());
				toolbar.setVisible(true);

			}
		});

		/**
		 * adding the shape to the canvas as well as the array to track it later when
		 * the user edit its properties
		 */

		canvas.getChildren().add(s);
		shapes.add(s);

	}

	/**
	 * This method is going to create a new line whenever the icon of the line on
	 * the left is dragged and placed on the canvas and it includes setting handlers
	 * to allow the user to drag and move current placed shapes on the canvas as
	 * well as it will change the selectedID so that the user can change the
	 * properties of the selected shape
	 * 
	 * 
	 */

	public void lineDragged(MouseEvent e) {
		/**
		 * Creating line and initializing to default properties
		 */

		Line s = new Line(0, 0, 80, 80);
		s.setStrokeWidth(5.5);
		s.setId("" + idCounter++);

		/**
		 * Setting mouse event which will allow the user to drag the shape across the
		 * canvas
		 */
		s.setOnMouseDragged(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				s.setLayoutY(arg0.getSceneY() - 50);
				s.setLayoutX(arg0.getSceneX() - 144);

			}
		});

		/**
		 * Setting a mouse even upon clicking which will make the shape react and set
		 * the selected id to its ID as well as showing the toolbar so the user can
		 * change the properies on the toolbar
		 */
		s.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				System.out.println("selected = " + s.getId() + "\t Type: " + s.getClass().toString());
				selectedID = Integer.parseInt(s.getId());
				toolbar.setVisible(true);

			}
		});

		/**
		 * Setting it on the dragged place on the canvas
		 */

		s.setLayoutX(e.getSceneX() - 144);
		s.setLayoutY(e.getSceneY() - 50);
		/**
		 * adding the shape to the canvas as well as the array to track it later when
		 * the user edit its properties
		 */

		canvas.getChildren().add(s);
		shapes.add(s);

	}

	/**
	 * This method is going to create a new line whenever the icon of the line on
	 * the left is dragged and placed on the canvas and it includes setting handlers
	 * to allow the user to drag and move current placed shapes on the canvas as
	 * well as it will change the selectedID so that the user can change the
	 * properties of the selected shape
	 * 
	 *
	 */

	public void triangleDragged(MouseEvent e) {
		/**
		 * Creating polygon of type triangle and initializing to default properties
		 */
		Polygon s = new Polygon();
		s.setRotate(180.0);
		s.getPoints().addAll(new Double[] { 0.0, 0.0, 80.0, 0.0, 40.0, 80.0 });
		s.setId("" + idCounter++);
		s.setFill(javafx.scene.paint.Color.TRANSPARENT);
		s.setStrokeWidth(5.5);
		s.setStroke(javafx.scene.paint.Color.BLACK);
		/**
		 * Setting mouse event which will allow the user to drag the shape across the
		 * canvas
		 */

		s.setOnMouseDragged(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent arg0) {
				s.setLayoutX(arg0.getSceneX() - 144);
				s.setLayoutY(arg0.getSceneY() - 50);

			}
		});
		/**
		 * Setting a mouse even upon clicking which will make the shape react and set
		 * the selected id to its ID as well as showing the toolbar so the user can
		 * change the properies on the toolbar
		 */

		s.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				System.out.println("selected = " + s.getId() + "\t Type: " + s.getClass().toString());
				selectedID = Integer.parseInt(s.getId());
				toolbar.setVisible(true);

			}
		});
		/**
		 * placing it to the dragged coordinate
		 */

		s.setLayoutX(e.getSceneX() - 144);
		s.setLayoutY(e.getSceneY() - 50);
		/**
		 * adding it to the canvas as well as the shapes array so it can be edited when
		 * selected
		 */

		canvas.getChildren().add(s);
		shapes.add(s);

	}

	/**
	 * this method will change the background colour of the canvas which is a
	 * rectangle to a specific color which can be choosen on the bottom left of the
	 * screen using the Color picker from javafx library
	 */

	public void backgroundColorHandler(ActionEvent e) {
		background.setFill(backgroundColorPicker.getValue());
	}

	/**
	 * This method will get the selected id which is triggered by the clicked shape
	 * then it will change the selected id shape stroke(frame) to the choosen color
	 * on the color picker on the toolbar
	 */

	public void frameButtonAction(ActionEvent e) {
		shapes.get(selectedID).setStroke(colorPicker.getValue());

	}

	/**
	 * This method will get the selected id which is triggered by the clicked shape
	 * then it will change the selected id shape fill to the choosen color on the
	 * color picker on the toolbar
	 */

	public void fillButtonAction(ActionEvent e) {
		shapes.get(selectedID).setFill(colorPicker.getValue());
	}

	/**
	 * this method is to handle the (hide) label on the toolbar so the user can
	 * click it and hide the toolbar
	 */

	public void hideLabelHandler(MouseEvent e) {
		toolbar.setVisible(false);
	}

	/**
	 * this method will remove the shapes from the canvas and store them on the undo
	 * array so the user can undo his removed shapes when he decides so
	 */

	public void RemoveShapeButtonHandler(ActionEvent e) {
		if (!undos.contains(selectedID)) {
			shapes.get(selectedID).setVisible(false);
			undos.add(selectedID);
			System.out.println("undos: " + undos.toString());
			System.out.println("selected = " + selectedID + "	 has been removed");
		}
	}

	/**
	 * this method will use the undo array to restore removed shapes and can retrive
	 * all removed shapes
	 */

	public void UndoButtonHandle(ActionEvent e) {
		System.out.println("undo clicked");
		if (undos.size() > 0) {
			selectedID = undos.get(undos.size() - 1);
			undos.remove(undos.size() - 1);
			shapes.get(selectedID).setVisible(true);
		}
	}

	/**
	 * this method is to get the value of the of the rotation slider [0-360] and
	 * apply it to the selected shape id and change it's rotation
	 */

	public void rotationSliderAction(MouseEvent e) {
		shapes.get(selectedID).setRotate(rotationSlider.getValue());
	}

	/**
	 * this method will handle the width box on the toolbar so it will set the width
	 * of the selected shape to what the user enters and change it in real time when
	 * pressing enter
	 */

	public void widthAction(KeyEvent e) {
		if (e.getCode().equals(KeyCode.ENTER)) {
			shapes.get(selectedID).setScaleX(Double.parseDouble(widthBox.getText()));
		}

	}

	/**
	 * this method will handle the hight box on the toolbar so it will set the width
	 * of the selected shape to what the user enters and change it in real time when
	 * pressing enter
	 * 
	 */

	public void hightAction(KeyEvent e) {
		if (e.getCode().equals(KeyCode.ENTER))
			shapes.get(selectedID).setScaleY(Double.parseDouble(hightBox.getText()));
	}

	public void initialize(URL url, ResourceBundle rb) {
		/**
		 * Required by Initializable interface Called to initialize a controller after
		 * the root element has been processed
		 */

	}

}
