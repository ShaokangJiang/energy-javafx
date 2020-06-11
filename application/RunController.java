package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
	
	protected static Object[] vars;
	
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
		tab.setText("Components");
		Tab tab1 = new Tab();
		tab1.setText("Monitor");
		tabPane.getTabs().add(tab);
		tabPane.getTabs().add(tab1);
		tabPane.setTabMinWidth(tabPane.getPrefWidth()/3);
		try {
			Parent root = FXMLLoader.load(getClass().getResource("components.fxml"));
			tab.setContent(root);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		wind.setSelected(true);
		light.setSelected(true);
		global.setSelected(true);
		wave.setSelected(true);
		current.setSelected(true);
		/**
		if(vars == null) {
			System.out.print("Invalid visit!");
		}*/
		showNotification(vars[0].toString()+vars[1].toString()+vars[2].toString()+vars[3].toString()+vars[4].toString()+vars[5].toString());
		//FIXME: need binding and set initial value of switchers to true
	}
	
    @FXML
    private void loadFile(ActionEvent event) {
    	try {
    		JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Label("Input selection"));
            

    		JFXDialog dialog = new JFXDialog(displayPane, content, JFXDialog.DialogTransition.CENTER);
    		JFXButton buttonM = new JFXButton("Select Files");
    		buttonM.setStyle("-jfx-button-type: RAISED;-fx-background-color: #00b0ff;-fx-text-fill: white;");
    		JFXTextField path = new JFXTextField();

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

    		HBox grid = new HBox();
    		grid.setPadding(new Insets(20, 20, 10, 10));
    		grid.setAlignment(Pos.CENTER);
    		grid.getChildren().addAll(new Text("Path:"),path,buttonM);
    		
    		BorderPane a = new BorderPane();
    		a.setCenter(grid);
    		a.setBottom(export);
    		
    		JFXButton btn_Ok = new JFXButton();
            btn_Ok.setText("OK");
            btn_Ok.setOnAction((e) -> {
            	dialog.close();//FIXME:Do something at here to interpret file and start simulation
            });
            JFXButton btn_Cancel = new JFXButton();
            btn_Cancel.setText("Cancel");
            btn_Cancel.setOnAction(e -> dialog.close());
            content.setActions(btn_Ok, btn_Cancel);

            content.setBody(a);
            dialog.show();
    		
    		//System.out.println(tmp.getKey()+""+tmp.getValue());
    	}catch(Exception e) {
    		
    	}
    }
    
    private void showNotification(String a) {
    	JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Label("Notification"));
        content.setBody(new Text(a));
        JFXDialog dialog = new JFXDialog(displayPane, content, JFXDialog.DialogTransition.TOP);
        JFXButton btn_Ok = new JFXButton();
        btn_Ok.setText("OK");
        btn_Ok.setOnAction((e) -> {
        	dialog.close();//FIXME:Do something at here to interpret file and start simulation
        });
        content.setActions(btn_Ok);
        dialog.show();
    }

}
