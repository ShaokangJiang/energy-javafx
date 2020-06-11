package application;

import java.io.File;
import java.io.FileNotFoundException;
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

import javafx.application.Platform;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
	
	FXMLLoader loaderTab1;
	ComponentsController controllerTab1;
	runner running;
	
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
			loaderTab1 = new FXMLLoader();
			Parent root = loaderTab1.load(getClass().getResourceAsStream("components.fxml"));
			tab.setContent(root);
			controllerTab1 = loaderTab1.getController();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		wind.setSelected(true);
		light.setSelected(true);
		global.setSelected(true);
		wave.setSelected(true);
		current.setSelected(true);
		downRun.setDisable(true);
		downBattery.setDisable(true);
		running = null;
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
    		Label h = new Label();
    		JFXButton btn_Ok = new JFXButton();
    		btn_Ok.setDisable(true);
    		btn_Ok.setStyle("-jfx-button-type: RAISED;-fx-background-color: #b399ff;-fx-text-fill: white;");
    		buttonM.setOnAction(new EventHandler<ActionEvent>() {
    			@Override
    			public void handle(ActionEvent event) {
    				path.setText("");
    				File files = fileChooser.showOpenDialog(mainStage);
    				if (files == null) {
    					path.setPromptText("File Path");
    					h.setText("Nothing choiced");
    					return;
    				}
    				path1 = files.getParent();
    				fileChooser.setInitialDirectory(new File(path1));
    				path.setText(files.getAbsolutePath());
    				//wind,light,wave, current,funture_optimization, battery,freq
    				//wind,light,wave, current,funture_optimization, battery,freq,user
    				try {
    					System.out.println(vars.length);
    					if(vars.length == 7) {
    						running = new runner(files, (Integer)vars[6], (Double)vars[5], (Double)vars[0], (Double)vars[1], (Double)vars[3], (Double)vars[2]);
    					}else if(vars.length == 8)
    					{
    						running = new runner(files, (Integer)vars[6], (Double)vars[5], (Double)vars[0], (Double)vars[1], (Double)vars[3], (Double)vars[2], (Double)vars[7]);
    				}else {
    						System.out.println("Invalid argument happen");
    						System.exit(1);
    					}
    					btn_Ok.setDisable(false);
    				}catch(IllegalArgumentException e) {
    					h.setText(e.getMessage());
    					h.setTextFill(Color.web("#ff0000"));
    				} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			}
    		});
    		
    		JFXCheckBox export = new JFXCheckBox("I don't need export data.");

    		HBox grid = new HBox();
    		grid.setPadding(new Insets(20, 20, 10, 10));
    		grid.setAlignment(Pos.CENTER);
    		grid.getChildren().addAll(new Text("Path:"),path,buttonM);
    		VBox bottom = new VBox();
    		bottom.getChildren().addAll(export,h);
    		bottom.setPadding(new Insets(20, 20, 10, 10));
    		BorderPane a = new BorderPane();
    		a.setCenter(grid);
    		a.setBottom(bottom);
    		
    		
            btn_Ok.setText("OK");
            btn_Ok.setOnAction((e) -> {
            	if(running != null) {
            		try {
            			controllerTab1.combine(running);
            			wind.selectedProperty().bindBidirectional(running.user_wind);
            			light.selectedProperty().bindBidirectional(running.user_light);
            			wave.selectedProperty().bindBidirectional(running.user_wave);
            			current.selectedProperty().bindBidirectional(running.user_current);
            			global.selectedProperty().bindBidirectional(running.pause);
            			Thread thread = new Thread(new Runnable() {
            	            @Override
            	            public void run() {
            	            	while(running.run) {
									try {
										System.out.print("Into");
										Thread.sleep(running.freq);
										System.out.print("Out");
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									Platform.runLater(running);
            	            	}
                    	        fileChoicer.setDisable(false);
            	                }
            	            });
            	        thread.setDaemon(true);
            	        thread.start();
            	        fileChoicer.setDisable(true);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
            	}
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
