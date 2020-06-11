package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Scanner;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class runner implements Runnable {

	protected boolean run = false;
	protected BooleanProperty pause = new SimpleBooleanProperty(true);
	protected boolean collect = false;// indicate exporting feature is on or not
	protected int time;// in ms
	protected Scanner scnr;
	protected Integer freq;
	protected DataFrame frame;// to find index

	protected DataFrame sourceData;
	protected DataFrame batteryUsage;
	private boolean needInitializeCollection;

	// limits
	protected double wind_limit;
	protected double light_limit;
	protected double wave_limit;
	protected double current_limit;
	protected double user_usage = 200;
	protected BooleanProperty user_wind;
	protected BooleanProperty user_light;
	protected BooleanProperty user_wave;
	protected BooleanProperty user_current;

	// current value
	protected DoubleProperty wind_current;
	protected DoubleProperty light_current;
	protected DoubleProperty wave_current;
	protected DoubleProperty current_current;
	protected DoubleProperty wind_speed;
	protected DoubleProperty light_speed;
	protected DoubleProperty wave_speed;
	protected DoubleProperty wave_period;
	protected DoubleProperty user_current_usage;
	protected DoubleProperty current_speed;
	protected DoubleProperty battery_inflow;
	protected double battery_remain;
	protected double battery_capacity;
	protected DoubleProperty battery_percent;
	protected DoubleProperty battery_current_flow;
	protected StringProperty battery_status;

	protected StringProperty wind_status;
	protected StringProperty light_status;
	protected StringProperty current_status;
	protected StringProperty wave_status;

	/**
	 * 
	 * @param f
	 *            file
	 * @param fre
	 *            frequency
	 * @param battery
	 * @param wind
	 * @param light
	 * @param current
	 * @param wave
	 * @throws FileNotFoundException
	 */
	public runner(File f, int fre, double battery, double wind, double light, double current, double wave)
			throws FileNotFoundException {
		user_wind = new SimpleBooleanProperty(true);
		user_light = new SimpleBooleanProperty(true);
		user_wave = new SimpleBooleanProperty(true);
		user_current = new SimpleBooleanProperty(true);
		battery_status = new SimpleStringProperty("");
		wind_status = new SimpleStringProperty("NAN");
		light_status = new SimpleStringProperty("NAN");
		current_status = new SimpleStringProperty("NAN");
		wave_status = new SimpleStringProperty("NAN");
		battery_current_flow = new SimpleDoubleProperty(0);
		battery_percent = new SimpleDoubleProperty(0);
		battery_inflow = new SimpleDoubleProperty(0);
		current_speed = new SimpleDoubleProperty(0);
		user_current_usage = new SimpleDoubleProperty(0);
		wave_period = new SimpleDoubleProperty(0);
		wave_speed = new SimpleDoubleProperty(0);
		light_speed = new SimpleDoubleProperty(0);
		wind_speed = new SimpleDoubleProperty(0);
		current_current = new SimpleDoubleProperty(0);
		wave_current = new SimpleDoubleProperty(0);
		light_current = new SimpleDoubleProperty(0);
		wind_current = new SimpleDoubleProperty(0);
		run = true;
		scnr = new Scanner(f);
		frame = new DataFrame(scnr.nextLine().split(","));
		if (!frame.validateHeader("Wind_Speed,Light_H,Wave_Hight,Wave_Period,Current_Speed,User_Usage"))
			throw new IllegalArgumentException("File has invalid components");
		freq = fre;
		wind_limit = wind;
		light_limit = light;
		wave_limit = wave;
		current_limit = current;
		battery_capacity = battery;
		battery_remain = battery * 0.7;
		battery_percent.set(70.00);
		time = 0;
	}

	/**
	 * 
	 * @param f
	 * @param fre
	 * @param battery
	 * @param wind
	 * @param light
	 * @param current
	 * @param wave
	 * @param user
	 * @throws FileNotFoundException
	 */
	public runner(File f, int fre, double battery, double wind, double light, double current, double wave, double user)
			throws FileNotFoundException {

		user_wind = new SimpleBooleanProperty(true);
		user_light = new SimpleBooleanProperty(true);
		user_wave = new SimpleBooleanProperty(true);
		user_current = new SimpleBooleanProperty(true);
		battery_status = new SimpleStringProperty("");
		wind_status = new SimpleStringProperty("NAN");
		light_status = new SimpleStringProperty("NAN");
		current_status = new SimpleStringProperty("NAN");
		wave_status = new SimpleStringProperty("NAN");
		battery_current_flow = new SimpleDoubleProperty(0);
		battery_percent = new SimpleDoubleProperty(0);
		battery_inflow = new SimpleDoubleProperty(0);
		current_speed = new SimpleDoubleProperty(0);
		user_current_usage = new SimpleDoubleProperty(0);
		wave_period = new SimpleDoubleProperty(0);
		wave_speed = new SimpleDoubleProperty(0);
		light_speed = new SimpleDoubleProperty(0);
		wind_speed = new SimpleDoubleProperty(0);
		current_current = new SimpleDoubleProperty(0);
		wave_current = new SimpleDoubleProperty(0);
		light_current = new SimpleDoubleProperty(0);
		wind_current = new SimpleDoubleProperty(0);
		run = true;
		scnr = new Scanner(f);
		frame = new DataFrame(scnr.nextLine().split(","));
		if (!frame.validateHeader("Wind_Speed,Light_H,Wave_Hight,Wave_Period,Current_Speed,User_Usage"))
			throw new IllegalArgumentException("File has invalid components");
		freq = fre;
		wind_limit = wind;
		light_limit = light;
		wave_limit = wave;
		current_limit = current;
		user_usage = user;
		battery_capacity = battery;
		battery_remain = battery * 0.7;
		battery_percent.set(70.00);
		time = 0;
	}

	// Time,Wind_Speed,Light_H,Wave_Hight,Wave_Period,Current_Speed,User_Usage
	public void run() {

		System.out.println("Here");
		if (needInitializeCollection) {
			sourceData = new DataFrame("Time, type, status, speed, production".split(","));
			batteryUsage = new DataFrame(
					"Time, battery_usage, battery_flow, battery_status, battery_current".split(","));
			needInitializeCollection = false;
		}
		if (run && scnr.hasNextLine()) {
			if (pause.get()) {
				System.out.println("Here");
				String[] tmp = scnr.nextLine().split(",");
				wind_speed.set(Double.parseDouble(tmp[frame.getColumnPos("Wind_Speed")]));
				light_speed.set(Double.parseDouble(tmp[frame.getColumnPos("Light_H")]));
				wave_speed.set(Double.parseDouble(tmp[frame.getColumnPos("Wave_Hight")]));
				wave_period.set(Double.parseDouble(tmp[frame.getColumnPos("Wave_Period")]));
				current_speed.set(Double.parseDouble(tmp[frame.getColumnPos("Current_Speed")]));
				user_current_usage.set((frame.getColumnPos("User_Usage") == null) ? user_usage
						: Double.parseDouble(tmp[frame.getColumnPos("User_Usage")]));
				if (user_wind.get() && wind_speed.get() > wind_limit) {
					wind_current.set(2866 * wind_speed.get() * wind_speed.get() * wind_speed.get());
					wind_status.set("ON");
				} else {
					wind_current.set(0);
					wind_status.set("OFF");
				}
				if (user_light.get() && light_speed.get() > light_limit) {
					light_current.set(0.09 * light_speed.get());
					light_status.set("ON");
				} else {
					light_current.set(0);
					light_status.set("OFF");
				}
				if (user_wave.get() && wave_speed.get() * wave_speed.get() * wave_period.get() > wave_limit) {
					wave_current.set(6.6 * wave_speed.get() * wave_speed.get() * wave_period.get());
					wave_status.set("ON");
				} else {
					wave_current.set(0);
					wave_status.set("OFF");
				}
				if (user_current.get() && current_speed.get() > current_limit) {
					current_current.set(1254 * current_speed.get() * current_speed.get() * current_speed.get());
					current_status.set("ON");
				} else {
					current_current.set(0);
					current_status.set("OFF");
				}
				battery_inflow
						.set((wind_current.get() + light_current.get() + wave_current.get() + current_current.get())
								* freq / 3600000);
				double pre_remain = battery_remain;
				if (battery_remain + battery_inflow.get() - user_current_usage.get() < 0) {
					battery_remain = 0;
					battery_status.set("Discharging, needs outsource battery...");
					battery_percent.set(0);
				} else if (battery_remain + battery_inflow.get() - user_current_usage.get() > battery_capacity) {
					battery_remain = battery_capacity;
					battery_status.set("Fully charged");
					battery_percent.set(100);
				} else if (battery_inflow.get() - user_current_usage.get() < 0) {
					battery_remain = battery_remain + battery_inflow.get() - user_current_usage.get();
					battery_status.set("Discharging");
					battery_percent.set(battery_remain * 100 / battery_capacity);
				} else {
					battery_remain = battery_remain + battery_inflow.get() - user_current_usage.get();
					battery_status.set("Charging");
					battery_percent.set(battery_remain * 100 / battery_capacity);
				}
				battery_current_flow.set(battery_remain - pre_remain);
			}
			if (collect) {
				if (pause.get())
					collectData();
				else
					collectData("aaa");
			}
		} else {
			scnr.close();
			stop();
		}

		time += freq;
	}
	// sourceData = new DataFrame("Time, type, status, speed,
	// production".split(","));
	// batteryUsage = new DataFrame(
	// "Time, battery_usage, battery_flow, battery_status,
	// battery_current".split(","));

	private void collectData() {
		// TODO Auto-generated method stub
		sourceData.appendRow(
				new String[] { "" + time, "0", wind_status.get(), "" + wind_speed.get(), "" + wind_current.get() });
		sourceData.appendRow(
				new String[] { "" + time, "1", light_status.get(), "" + light_speed.get(), "" + light_current.get() });
		sourceData.appendRow(new String[] { "" + time, "2", wave_status.get(),
				"" + (wave_speed.get() * wave_speed.get() * wave_period.get()), "" + wave_current.get() });
		sourceData.appendRow(new String[] { "" + time, "3", current_status.get(), "" + current_speed.get(),
				"" + current_current.get() });
		batteryUsage.appendRow(new String[] { "" + time, "" + user_current_usage.get(), "" + battery_current_flow.get(),
				battery_status.get(), "" + battery_remain });
	}

	private void collectData(String a) {
		// TODO Auto-generated method stub
		sourceData.appendRow(new String[] { "" + time, "0", "STOP", "" + 0, "" + 0 });
		sourceData.appendRow(new String[] { "" + time, "1", "STOP", "" + 0, "" + 0 });
		sourceData.appendRow(new String[] { "" + time, "2", "STOP", "" + 0, "" + 0 });
		sourceData.appendRow(new String[] { "" + time, "3", "STOP", "" + 0, "" + 0 });
		batteryUsage.appendRow(new String[] { "" + time, "" + 0, "" + 0, "STOP", "" + battery_remain });
	}

	public void enableCollection() {
		collect = true;
		needInitializeCollection = true;
	}

	public void disableCollection() {
		needInitializeCollection = false;
		collect = false;
	}

	public void stop() {
		run = false;
	}

	public void pause() {
		pause.set(false);
	}

	public void unPause() {
		pause.set(true);
	}
}
