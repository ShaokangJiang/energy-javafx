package application;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXNodesList;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

public class RunController implements Initializable{

	@FXML
	JFXComboBox<String> language;
	@FXML
	JFXToggleButton wind;
	@FXML
	JFXToggleButton global;
	@FXML
	JFXToggleButton light;
	@FXML
	JFXToggleButton wave;
	@FXML
	JFXToggleButton current;
	@FXML
	JFXTabPane tabPane;
	@FXML
	JFXButton fileChoicer;
	@FXML
	JFXButton downRun;
	@FXML
	JFXButton downBattery;
	@FXML
	StackPane displayPane;
	
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
		fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Comma-Separated Values",
						"*.csv"));
		fileChooser.setTitle("Choose files path...");
		fileChooser.setInitialDirectory(new File(path1));
		Tab tab = new Tab();
		tab.setText("Tab 1");
		tab.setContent(new Label("Content"));
		Tab tab1 = new Tab();
		tab1.setText("Tab 1");
		tab1.setContent(new Label("Content"));
		tabPane.getTabs().add(tab);
		tabPane.getTabs().add(tab1);
		tabPane.setPrefHeight(100);
	}
	
    @FXML
    private void loadFile(ActionEvent event) {
    	try {
    		JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Label("Input selection"));
            
    		
    		JFXButton buttonM = new JFXButton("Select Files");

    		JFXTextField path = new JFXTextField();
    		path.setPromptText("The path...");

    		buttonM.setOnAction(new EventHandler<ActionEvent>() {
    			@Override
    			public void handle(ActionEvent event) {
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
    		

            content.setBody(a);
    		JFXDialog dialog = new JFXDialog(displayPane, content, JFXDialog.DialogTransition.CENTER);
            dialog.show();
    		
    		//System.out.println(tmp.getKey()+""+tmp.getValue());
    	}catch(Exception e) {
    		
    	}
    }

}
