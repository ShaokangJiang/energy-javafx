package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class runner {

	protected boolean run = false;
	protected Scanner scnr;
	protected Integer freq;
	protected DataFrame frame;// to find index

	// limits
	protected double wind_limit;
	protected double light_limit;
	protected double wave_limit;
	protected double current_limit;
	protected double user_usage = 200;

	// current value
	protected double wind_current;
	protected double light_current;
	protected double wave_current;
	protected double current_current;
	protected double battery_inflow;
	protected double battery_remain;
	protected double battery_capacity;
	

	public runner(File f, int fre, double battery, double wind, double light, double current, double wave)
			throws FileNotFoundException {
		run = true;
		scnr = new Scanner(f);
		frame = new DataFrame(scnr.nextLine().split(","));
		freq = fre;
		wind_limit = wind;
		light_limit = light;
		wave_limit = wave;
		current_limit = current;
		battery_capacity = battery;
		battery_remain = battery *0.7;
	}

	public runner(File f, int fre, double battery, double wind, double light, double current, double wave, double user)
			throws FileNotFoundException {
		run = true;
		scnr = new Scanner(f);
		frame = new DataFrame(scnr.nextLine().split(","));
		freq = fre;
		wind_limit = wind;
		light_limit = light;
		wave_limit = wave;
		current_limit = current;
		user_usage = user;
		battery_capacity = battery;
		battery_remain = battery *0.7;
	}

	// Time,Wind_Speed,Light_H,Wave_Hight,Wave_Period,Current_Speed,User_Usage
	public void run() throws InterruptedException {
		while (run && scnr.hasNextLine()) {
			String[] tmp = scnr.nextLine().split(",");
			double wind_speed = Double.parseDouble(tmp[frame.getColumnPos("Wind_Speed")]);
			double light_h = Double.parseDouble(tmp[frame.getColumnPos("Light_H")]);
			double wave_hight = Double.parseDouble(tmp[frame.getColumnPos("Wave_Hight")]);
			double wave_peroid = Double.parseDouble(tmp[frame.getColumnPos("Wave_Period")]);
			double current_speed = Double.parseDouble(tmp[frame.getColumnPos("Current_Speed")]);
			double user = (frame.getColumnPos("User_Usage") == null) ? user_usage
					: Double.parseDouble(tmp[frame.getColumnPos("User_Usage")]);
			
			
			/**
			 *             if (data["Wind_Speed"] > wind_limit) {
                document.getElementById("wind_Speed").innerText = data["Wind_Speed"];
                wind_produced = 2866 * data["Wind_Speed"] * data["Wind_Speed"] * data["Wind_Speed"];
                document.getElementById("wind_production").innerText = wind_produced;
            }

            if (data["Light_H"] > light_limit) {
                document.getElementById("Light_Speed").innerText = data["Light_H"];
                light_produced = 0.09 * data["Light_H"];
                document.getElementById("Light_production").innerText = light_produced;
            }

            if (data["Wave_Hight"] * data["Wave_Hight"] * data["Wave_Period"] > wave_limit) {
                document.getElementById("wave_Speed").innerText = "Height: " + data["Wave_Hight"] + "Period: " + data["Wave_Period"];
                wave_produced = 6.6 * data["Wave_Hight"] * data["Wave_Hight"] * data["Wave_Period"];
                document.getElementById("wave_production").innerText = wave_produced;
            }

            if (data["Current_Speed"] > current_limit) {
                document.getElementById("current_Speed").innerText = data["Current_Speed"];
                current_produced = 1254 * data["Current_Speed"] * data["Current_Speed"] * data["Current_Speed"];
                document.getElementById("current_production").innerText = current_produced;
            }

            document.getElementById("battery_usage").innerText = userConsumption;
            var pre_battery_current = battery_current;
            battery_current += (wind_produced + light_produced + wave_produced + current_produced) * (time - prevTime) / 3600 - userConsumption;
            if (battery_current > battery_capacity) battery_current = battery_capacity;
            document.getElementById("battery_flow").innerText = (battery_current - pre_battery_current).toFixed(2);
            document.getElementById("battery_remain").innerText = (battery_current * 100 / battery_capacity).toFixed(2);
            if (battery_current - pre_battery_current > 0) document.getElementById("battery_status").innerText = 'Charging';
            else if (battery_current < 0) {
                battery_current = 0;
                document.getElementById("battery_remain").innerText = (battery_current * 100 / battery_capacity).toFixed(2);
                document.getElementById("battery_status").innerText = 'Discharging, extra electricity required.';
            } else {
                document.getElementById("battery_status").innerText = 'Discharging';
            }
            //refresh locals
            prevTime = time;
			 */

			Thread.sleep(freq);
		}
		scnr.close();
	}

	public void stop() {
		run = false;
	}

}
