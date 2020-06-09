package application;

import java.io.File;
import java.util.List;

import com.jfoenix.controls.JFXCheckBox;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 * A simple window to let user choose data range
 * 
 * @author shaokang
 *
 */
public class ChoiceWindow {

	static protected FileChooser fileChooser;
	//static protected String path1 = System.getProperty("user.home");
	static protected String path1 = System.getProperty("user.home");
	/**
	 * Show this import window
	 * 
	 * 
	 * @param i int to indicate this is in import model or export model
	 */
	public static Pair<File, Boolean> Displayimport(Stage s) {
		// TODO Auto-generated method stub
		Dialog<Pair<File, Boolean>> dialog = new Dialog<>();

		fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Comma-Separated Values",
						"*.csv"));
		fileChooser.setTitle("Choose files path...");
		fileChooser.setInitialDirectory(new File(path1));

		Button buttonM = new Button("Select Files");

		TextField path = new TextField();
		path.setPromptText("The path...");

		buttonM.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
		    	path.setText("");
				List<File> files = fileChooser.showOpenMultipleDialog(s);
				if (files == null || files.isEmpty()) {
					path.setPromptText("File Path");
					return;
				}
				path1 = files.get(0).getParent();
				fileChooser.setInitialDirectory(new File(path1));
				for (File file : files) {
					path.appendText(file.getAbsolutePath() + ";");
				}
				path.setText(path.getText().substring(0,
						path.getText().length() - 1));
			}
		});
		
		JFXCheckBox export = new JFXCheckBox("I don't need export data.");

		GridPane grid = new GridPane();
		grid.setHgap(20);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		grid.setAlignment(Pos.CENTER);

		grid.add(new Label("Path:"), 0, 0);
		grid.add(path, 1, 0);
		grid.add(buttonM, 2, 0);
		BorderPane a = new BorderPane();
		a.setTop(grid);
		a.setBottom(export);

		ButtonType OKButton = new ButtonType("Start", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(OKButton,
				ButtonType.CANCEL);

		Node loginButton = dialog.getDialogPane().lookupButton(OKButton);
		loginButton.setDisable(true);

		// Do some validation (using the Java 8 lambda syntax).
		path.textProperty().addListener((observable, oldValue, newValue) -> {
			loginButton.setDisable(newValue.trim().isEmpty());
		});

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == OKButton) {
					return new Pair<>(new File(path.getText()),!export.isSelected());
			}
			return null;
		});

		dialog.setTitle("Import Files chosen Dialog");
		dialog.setHeaderText("Choose path: ");

		dialog.getDialogPane().setContent(grid);
		return dialog.showAndWait().get();
	}
	
}