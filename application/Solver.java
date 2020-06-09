package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

/**
 * a optimization solution based on scpsolver build:
 * https://gist.github.com/huberflores/2d422581dc6657badc21
 * Another algorithm ojalgo
 * @author Shaokang Jiang
 *
 */
public class Solver {

	public static void main(String[] args) throws FileNotFoundException, RuntimeException {
		// TODO Auto-generated method stub
		DataFrame fr = CSVFileReader.readCSV(new File("OptimizationData.csv"));
		handleFrame(fr, 10000, 1325 * 365, 1000);

	}

	public static double[] handleFrame(DataFrame frame, int B, int U, double freq) {
		if (frame.validateHeader(
				"wind_value,wind_count,light_value,light_count,wave_value,wave_count,current_value,current_count")) {
			freq = freq / 3600000.0;
			List<Double> val_wind = new ArrayList<Double>();
			List<Double> val_light = new ArrayList<Double>();
			List<Double> val_wave = new ArrayList<Double>();
			List<Double> val_current = new ArrayList<Double>();
			List<Integer> count_wind = new ArrayList<Integer>();
			List<Integer> count_light = new ArrayList<Integer>();
			List<Integer> count_wave = new ArrayList<Integer>();
			List<Integer> count_current = new ArrayList<Integer>();
			// organizing values
			for (Object[] rows : frame.rows) {
				if (rows[frame.getColumnPos("wind_value")] != null
						&& !((String) rows[frame.getColumnPos("wind_value")]).trim().isEmpty()) {
					val_wind.add(Double.parseDouble((String) rows[frame.getColumnPos("wind_value")]));
					count_wind.add(Integer.parseInt((String) rows[frame.getColumnPos("wind_count")]));
				}
				if (rows[frame.getColumnPos("light_value")] != null
						&& !((String) rows[frame.getColumnPos("light_value")]).trim().isEmpty()) {
					val_light.add(Double.parseDouble((String) rows[frame.getColumnPos("light_value")]));
					count_light.add(Integer.parseInt((String) rows[frame.getColumnPos("light_count")]));
				}
				if (rows[frame.getColumnPos("wave_value")] != null
						&& !((String) rows[frame.getColumnPos("wave_value")]).trim().isEmpty()) {
					val_wave.add(Double.parseDouble((String) rows[frame.getColumnPos("wave_value")]));
					count_wave.add(Integer.parseInt((String) rows[frame.getColumnPos("wave_count")]));
				}
				if (rows[frame.getColumnPos("current_value")] != null
						&& !((String) rows[frame.getColumnPos("current_value")]).trim().isEmpty()) {
					val_current.add(Double.parseDouble((String) rows[frame.getColumnPos("current_value")]));
					count_current.add(Integer.parseInt((String) rows[frame.getColumnPos("current_count")]));
				}
			}
			double W = 0 - B - U;
			// wt: results.data["light_value"] * results.data["light_count"] * freq
			// val: count: results.data["light_count"]
			// constructing above two
			List<Double> value = new ArrayList<Double>();// for wt
			List<Integer> count = new ArrayList<Integer>();// for val
			List<Boolean> isInteger = new ArrayList<Boolean>();
			for (int i = 0; i < val_wind.size(); i++) {
				value.add(val_wind.get(i) * count_wind.get(i) * freq);
				count.add(count_wind.get(i));
				isInteger.add(true);
			}
			for (int i = 0; i < val_light.size(); i++) {
				value.add(val_light.get(i) * count_light.get(i) * freq);
				count.add(count_light.get(i));
				isInteger.add(true);
			}
			for (int i = 0; i < val_wave.size(); i++) {
				value.add(val_wave.get(i) * count_wave.get(i) * freq);
				count.add(count_wave.get(i));
				isInteger.add(true);
			}
			for (int i = 0; i < val_current.size(); i++) {
				value.add(val_current.get(i) * count_current.get(i) * freq);
				count.add(count_current.get(i));
				isInteger.add(true);
			}
			System.out.print("value:");
			printList(value);
			System.out.print("count:");
			printList(count);

			// The model is the main component of Choco Solver
			Model model = new Model("Choco Solver Hello World");
			// Integer variables

			BoolVar[] a = model.boolVarArray(value.size());
			IntVar k = model.intVar("k", IntVar.MIN_INT_BOUND, IntVar.MAX_INT_BOUND);
			// Add an arithmetic constraint between a and b
			// BEWARE : do not forget to call post() to force this constraint to be
			// satisfied
			model.scalar(a, convertDouble(value), ">=", B + U).post();
			model.scalar(a, convertInteger(count), "=", k).post();
			org.chocosolver.solver.Solver solver = model.getSolver();
			Solution best = solver.findOptimalSolution(k, Model.MINIMIZE);
			 model.setObjective(Model.MINIMIZE, k);
			System.out.print(model.toString());
			// Find a solution that minimizes 'tot_cost'

			int i = 1;
			// Computes all solutions : Solver.solve() returns true whenever a new feasible
			// solution has been found
			try {
				if (solver.solve()) {
					// do something, e.g. print out variable values
			//		System.out.println(best.getIntVal(k));
					System.out.println(a[0].getValue());
				} else {
					System.out.println("The solver has proved the problem has no solution");
				//	System.out.println(best.getIntVal(k));
					for (BoolVar j : a)
						System.out.println(j.getValue());
				}
			} catch (Exception e) {
				//System.out.println(best.getIntVal(k));
				for (BoolVar j : a)
					System.out.println(j.getValue());
			}
		}
		return null;
	}

	public static double[] convertDouble(List<Double> a) {
		double[] tmp = new double[a.size()];
		for (int i = 0; i < a.size(); i++) {
			tmp[i] = a.get(i);
		}
		return tmp;
	}

	public static int[] convertInteger(List<Integer> a) {
		int[] tmp = new int[a.size()];
		for (int i = 0; i < a.size(); i++) {
			tmp[i] = a.get(i);
		}
		return tmp;

	}

	public static boolean[] convertBoolean(List<Boolean> a) {
		boolean[] tmp = new boolean[a.size()];
		for (int i = 0; i < a.size(); i++) {
			tmp[i] = a.get(i);
		}
		return tmp;

	}

	private static void printList(List<? extends Object> a) {
		for (int i = 0; i < a.size(); i++) {
			System.out.print(a.get(i) + ",");
		}
		System.out.println();
	}

}
