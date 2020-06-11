package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainController implements Initializable {

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
	Label notification;

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

	Run runner;

	DecimalFormat format = new DecimalFormat("#.0");

	static Stage mainStage;
	static protected String path1 = System.getProperty("user.home");
	static protected FileChooser fileChooser;

	public static void setStage(Stage a) {
		mainStage = a;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		language.setItems(FXCollections.observableArrayList("Chinese", "English"));
		group = new ToggleGroup();
		button1.setToggleGroup(group);
		button2.setToggleGroup(group);
		button2.setSelected(true);
		submit.setGraphic(new ImageView(new Image("send.png")));
		fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Comma-Separated Values", "*.csv"));
		fileChooser.setTitle("Choose files path...");
		fileChooser.setInitialDirectory(new File(path1));

		wind.setTextFormatter(new TextFormatter<>(c -> {
			if (c.getControlNewText().isEmpty()) {
				return c;
			}

			ParsePosition parsePosition = new ParsePosition(0);
			Object object = format.parse(c.getControlNewText(), parsePosition);

			if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
				return null;
			} else {
				return c;
			}
		}));
		Light.setTextFormatter(new TextFormatter<>(c -> {
			if (c.getControlNewText().isEmpty()) {
				return c;
			}

			ParsePosition parsePosition = new ParsePosition(0);
			Object object = format.parse(c.getControlNewText(), parsePosition);

			if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
				return null;
			} else {
				return c;
			}
		}));

		Wave.setTextFormatter(new TextFormatter<>(c -> {
			if (c.getControlNewText().isEmpty()) {
				return c;
			}

			ParsePosition parsePosition = new ParsePosition(0);
			Object object = format.parse(c.getControlNewText(), parsePosition);

			if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
				return null;
			} else {
				return c;
			}
		}));

		current.setTextFormatter(new TextFormatter<>(c -> {
			if (c.getControlNewText().isEmpty()) {
				return c;
			}

			ParsePosition parsePosition = new ParsePosition(0);
			Object object = format.parse(c.getControlNewText(), parsePosition);

			if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
				return null;
			} else {
				return c;
			}
		}));

		Battery_capacity.setTextFormatter(new TextFormatter<>(c -> {
			if (c.getControlNewText().isEmpty()) {
				return c;
			}

			ParsePosition parsePosition = new ParsePosition(0);
			Object object = format.parse(c.getControlNewText(), parsePosition);

			if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
				return null;
			} else {
				return c;
			}
		}));

		user.setTextFormatter(new TextFormatter<>(c -> {
			if (c.getControlNewText().isEmpty()) {
				return c;
			}

			ParsePosition parsePosition = new ParsePosition(0);
			Object object = format.parse(c.getControlNewText(), parsePosition);

			if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
				return null;
			} else {
				return c;
			}
		}));

		freq.setTextFormatter(new TextFormatter<>(c -> {
			if (c.getControlNewText().isEmpty()) {
				return c;
			}

			ParsePosition parsePosition = new ParsePosition(0);
			Object object = format.parse(c.getControlNewText(), parsePosition);

			if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
				return null;
			} else {
				return c;
			}
		}));

		wind.setDisable(true);
		Wave.setDisable(true);
		current.setDisable(true);
		future_optimize.setDisable(true);
		future_user_data.setDisable(true);
		Light.setDisable(true);
		RunController.vars = null;
	}

	@FXML
	private void loadFile(ActionEvent event) {
		path.setText("");
		File files = fileChooser.showOpenDialog(mainStage);
		if (files == null) {
			path.setPromptText("File Path");
			return;
		}
		path1 = files.getParent();
		fileChooser.setInitialDirectory(new File(path1));
		path.setText(files.getAbsolutePath());
	}

	@FXML
	private void submission(ActionEvent event) {
		mainStage.close();
		try {//wind,light,wave, current,funture_optimization, battery,freq
			if (future_user_data.isSelected())
				RunController.vars = new Object[] { Double.parseDouble(wind.getText()), Double.parseDouble(Light.getText()), Double.parseDouble(Wave.getText()),
						Double.parseDouble(current.getText()), future_optimize.isSelected(), Double.parseDouble(Battery_capacity.getText()), Integer.parseInt(freq.getText()) };
			else //wind,light,wave, current,funture_optimization, battery,freq,user
				RunController.vars = new Object[] { Double.parseDouble(wind.getText()), Double.parseDouble(Light.getText()), Double.parseDouble(Wave.getText()), Double.parseDouble(current.getText()),
						future_optimize.isSelected(), Double.parseDouble(Battery_capacity.getText()), Integer.parseInt(freq.getText()), Double.parseDouble(user.getText()) };
		} catch (Exception e) {
			notification.setText("Invalid input found");
			notification.setTextFill(Color.web("#ff0000"));
			return;
		}
		runner = new Run();
		try {
			runner.start(mainStage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	private void selectTwo(ActionEvent event) {
		wind.setDisable(true);
		Wave.setDisable(true);
		current.setDisable(true);
		future_optimize.setDisable(true);
		future_user_data.setDisable(true);
		Light.setDisable(true);

		fileChoicer.setDisable(false);
		user.setDisable(false);
		path.setDisable(false);
		freq.setDisable(false);
		Battery_capacity.setDisable(false);
	}

	@FXML
	private void selectOne(ActionEvent event) {
		wind.setDisable(false);
		Wave.setDisable(false);
		current.setDisable(false);
		future_optimize.setDisable(false);
		future_user_data.setDisable(false);
		Light.setDisable(false);

		fileChoicer.setDisable(true);
		path.setDisable(true);
	}

}
