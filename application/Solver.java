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
 * https://gist.github.com/huberflores/2d422581dc6657badc21 Another algorithm
 * ojalgo
 * 
 * @author Shaokang Jiang
 *
 */
public class Solver {

	protected List<Double> val_wind;
	protected List<Double> val_light;
	protected List<Double> val_wave;
	protected List<Double> val_current;
	protected List<Integer> count_wind;
	protected List<Integer> count_light;
	protected List<Integer> count_wave;
	protected List<Integer> count_current;

	protected static double wind_limits = 5.0;
	protected static double light_limits = 1100.0;
	protected static double wave_limits = 30;
	protected static double current_limits = 2;

	public Solver() {
		val_wind = new ArrayList<Double>();
		val_light = new ArrayList<Double>();
		val_wave = new ArrayList<Double>();
		val_current = new ArrayList<Double>();
		count_wind = new ArrayList<Integer>();
		count_light = new ArrayList<Integer>();
		count_wave = new ArrayList<Integer>();
		count_current = new ArrayList<Integer>();
	}

	/**
	 * 
	 * @param category
	 *            0 : wind, 1: light, 2: wave, 3 : current
	 * @param value
	 * @param count
	 * @param freq
	 */
	public void add(int category, double value, int count) {
		switch (category) {
		case 0:// wind
			val_wind.add(value);
			count_wind.add(count);
			break;
		case 1:// light
			val_light.add(value);
			count_light.add(count);
			break;
		case 2:// wave
			val_wave.add(value);
			count_wave.add(count);
			break;
		case 3:// current
			val_current.add(value);
			count_current.add(count);
			break;
		default:
			break;
		}
	}
	
	public double[] handleIt(int B, int U, int freq) {
		List<Double> value = new ArrayList<Double>();// for wt
		List<Integer> count = new ArrayList<Integer>();// for val
		for (int i = 0; i < val_wind.size(); i++) {
			value.add(val_wind.get(i) * count_wind.get(i) * freq);
			count.add(count_wind.get(i));
		}
		for (int i = 0; i < val_light.size(); i++) {
			value.add(val_light.get(i) * count_light.get(i) * freq);
			count.add(count_light.get(i));
		}
		for (int i = 0; i < val_wave.size(); i++) {
			value.add(val_wave.get(i) * count_wave.get(i) * freq);
			count.add(count_wave.get(i));
		}
		for (int i = 0; i < val_current.size(); i++) {
			value.add(val_current.get(i) * count_current.get(i) * freq);
			count.add(count_current.get(i));
		}
		Object[] a = toSolve(value, count, B, U);
		return getLimit(val_wind, val_light, val_wave, val_current, (BoolVar[]) a[0], (Integer) a[1]);
	}

	public static void main(String[] args) throws FileNotFoundException, RuntimeException {
		// TODO Auto-generated method stub
		DataFrame fr = CSVFileReader.readCSV(new File("OptimizationData.csv"));
		double[] a = handleFrame(fr, 10000, 1300 * 365, 1000);
		System.out.println(a[0] + " " + a[1] + " " + a[2] + " " + a[3] + " " + a[4]);
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
			// wt: results.data["light_value"] * results.data["light_count"] * freq
			// val: count: results.data["light_count"]
			// constructing above two
			List<Double> value = new ArrayList<Double>();// for wt
			List<Integer> count = new ArrayList<Integer>();// for val
			for (int i = 0; i < val_wind.size(); i++) {
				value.add(val_wind.get(i) * count_wind.get(i) * freq);
				count.add(count_wind.get(i));
			}
			for (int i = 0; i < val_light.size(); i++) {
				value.add(val_light.get(i) * count_light.get(i) * freq);
				count.add(count_light.get(i));
			}
			for (int i = 0; i < val_wave.size(); i++) {
				value.add(val_wave.get(i) * count_wave.get(i) * freq);
				count.add(count_wave.get(i));
			}
			for (int i = 0; i < val_current.size(); i++) {
				value.add(val_current.get(i) * count_current.get(i) * freq);
				count.add(count_current.get(i));
			}
			/**
			 * System.out.print("value:"); printList(value); System.out.print("count:");
			 * printList(count);
			 */
			// The model is the main component of Choco Solver
			Object[] a = toSolve(value, count, B, U);
			return getLimit(val_wind, val_light, val_wave, val_current, (BoolVar[]) a[0], (Integer) a[1]);
		}
		return null;
	}

	public static Object[] toSolve(List<Double> value, List<Integer> count, int B, int U) {
		Model model = new Model("Choco Solver");
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

		// Computes all solutions : Solver.solve() returns true whenever a new feasible
		// solution has been found
		try {
			if (solver.solve()) {

				return new Object[] { a, new Integer(0) };
			} else {
				System.out.println("The solver has proved the problem has no solution");
				return new Object[] { a, new Integer(1) };

			}
		} catch (Exception e) {
			return new Object[] { a, new Integer(2) };
		}
	}

	public static double[] getLimit(List<Double> wind, List<Double> light, List<Double> wave, List<Double> current,
			BoolVar[] a, int status) {
		double wind_limit = -1;
		double light_limit = -1;
		double wave_limit = -1;
		double current_limit = -1;
		/**
		 * for (BoolVar j : a) { System.out.println(j.getValue()); }
		 * System.out.println(status);
		 */
		int i = 0;
		for (; i < wind.size(); i++) {
			if (a[i].getValue() == 1) {
				if (wind_limit == -1) {
					wind_limit = wind.get(i);
				} else if (wind_limit >= wind.get(i)) {
					wind_limit = wind.get(i);
				}
			}
		}
		for (int j = 0; i < wind.size() + light.size(); j++, i++) {
			if (a[i].getValue() == 1) {
				if (light_limit == -1) {
					light_limit = light.get(j);
				} else if (light_limit >= light.get(j)) {
					light_limit = light.get(j);
				}
			}
		}
		for (int j = 0; i < wind.size() + light.size() + wave.size(); j++, i++) {
			if (a[i].getValue() == 1) {
				if (wave_limit == -1) {
					wave_limit = wave.get(j);
				} else if (wave_limit >= wave.get(j)) {
					wave_limit = wave.get(j);
				}
			}
		}
		for (int j = 0; i < wind.size() + light.size() + wave.size() + current.size(); j++, i++) {
			if (a[i].getValue() == 1) {
				if (current_limit == -1) {
					current_limit = current.get(j);
				} else if (current_limit >= current.get(j)) {
					current_limit = current.get(j);
				}
			}
		}
		// System.out.println(i);
		// System.out.println(a.length);
		return new double[] { (wind_limit == -1) ? wind_limits : wind_limit,
				(light_limit == -1) ? light_limits : light_limit, (wave_limit == -1) ? wave_limits : wave_limit,
				(current_limit == -1) ? current_limits : current_limit, status };
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
