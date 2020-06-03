package application;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXNodesList;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainController implements Initializable{

	@FXML
	JFXComboBox<String> language;
	@FXML
	JFXTextField wind;
	@FXML
	JFXTextField Light;
	@FXML
	JFXTextField Wave;
	@FXML
	JFXTextField Battery_capacity;
	@FXML
	JFXTextField freq;
	@FXML
	JFXTextField path;
	@FXML
	JFXTextField current;
	@FXML
	JFXTextField user;
	
	@FXML
	JFXCheckBox future_optimize;
	@FXML
	JFXCheckBox future_user_data;

	ToggleGroup group;
	@FXML
	JFXRadioButton button1;
	@FXML
	JFXRadioButton button2;
	@FXML
	JFXButton fileChoicer;
	@FXML
	JFXButton submit;
	
	static Stage mainStage;
	static protected String path1 = System.getProperty("user.home");
	static protected FileChooser fileChooser;
	
	public static void setStage(Stage a) {
		mainStage = a;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		language.setItems(FXCollections.observableArrayList("Chinese","English"));
		group = new ToggleGroup();
		button1.setToggleGroup(group);
		button2.setToggleGroup(group);
		button2.setSelected(true);
		submit.setGraphic(new ImageView(new Image("send.png")));
		fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Comma-Separated Values",
						"*.csv"));
		fileChooser.setTitle("Choose files path...");
		fileChooser.setInitialDirectory(new File(path1));
		
	}
	
    @FXML
    private void loadFile(ActionEvent event) {
    	path.setText("");
		List<File> files = fileChooser.showOpenMultipleDialog(mainStage);
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

}
