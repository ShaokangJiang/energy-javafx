package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ComponentsController implements Initializable{

	@FXML
	Label wind_speed;
	@FXML
	Label current_speed;
	@FXML
	Label wave_speed;
	@FXML
	Label wave_period;
	@FXML
	Label light_speed;
	@FXML
	Label light_status;
	@FXML
	Label wind_status;
	@FXML
	Label current_status;
	@FXML
	Label wave_status;
	@FXML
	Label light_production;
	@FXML
	Label wind_production;
	@FXML
	Label current_production;
	@FXML
	Label wave_production;
	
	@FXML
	Label battery_status;
	@FXML
	Label battery_Current_flow;
	@FXML
	Label battery_Current_Usage;
	@FXML
	Label battery_Remain;
	@FXML
	Label battery_Current_inflow;
	
	
	private final static String DOUBLE_FORMAT = "%.2f";
	static Stage mainStage;
	static protected String path1 = System.getProperty("user.home");
	static protected FileChooser fileChooser;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	
	public void combine(runner run) {
		wind_speed.textProperty().bind(run.wind_speed.asString(DOUBLE_FORMAT));
		current_speed.textProperty().bind(run.current_speed.asString(DOUBLE_FORMAT));
		wave_speed.textProperty().bind(run.wave_speed.asString(DOUBLE_FORMAT));
		light_speed.textProperty().bind(run.light_speed.asString(DOUBLE_FORMAT));
		wave_period.textProperty().bind(run.wave_period.asString(DOUBLE_FORMAT));
		
		light_status.textProperty().bind(run.light_status);
		wind_status.textProperty().bind(run.wind_status);
		current_status.textProperty().bind(run.current_status);
		wave_status.textProperty().bind(run.wave_status);
		light_production.textProperty().bind(run.light_current.asString(DOUBLE_FORMAT));;
		wind_production.textProperty().bind(run.wind_current.asString(DOUBLE_FORMAT));
		current_production.textProperty().bind(run.current_current.asString(DOUBLE_FORMAT));
		wave_production.textProperty().bind(run.wave_current.asString(DOUBLE_FORMAT));
		battery_status.textProperty().bind(run.battery_status);
		battery_Current_flow.textProperty().bind(run.battery_current_flow.asString(DOUBLE_FORMAT));
		battery_Current_Usage.textProperty().bind(run.user_current_usage.asString(DOUBLE_FORMAT));
		battery_Remain.textProperty().bind(run.battery_percent.asString(DOUBLE_FORMAT));
		battery_Current_inflow.textProperty().bind(run.battery_inflow.asString(DOUBLE_FORMAT));
		
	}
	
}
