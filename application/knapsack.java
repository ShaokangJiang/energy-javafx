package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * NOT A REAL knapsack Modified version with memorize feature
 * 
 * @author Shaokang Jiang
 *
 */
public class knapsack {

	static HashMap<String, Double> use;
	private static DecimalFormat df = new DecimalFormat("#.000");

	// A utility function that returns
	// maximum of two integers
	static double max(double d, double e) {
		return (d > e) ? d : e;
	}

	static double knapSackHelper(double W, Double wt[], Integer val[], int n) {
		if (use.get("" + n + "," + df.format(W)) != null) {
			//System.out.print("Hit");
			return use.get("" + n + "," + df.format(W));
		}
		return knapSack(W, wt, val, n);
	}

	// Returns the maximum value that
	// can be put in a knapsack of
	// capacity W
	static double knapSack(double W, Double wt[], Integer val[], int n) {
		// Base Case
		if (n == 0 || W >= -0.1) {
			use.put("" + n + "," + df.format(W), 0.00);
			return 0;
		}

		// If weight of the nth item is
		// more than Knapsack capacity W,
		// then this item cannot be included
		// in the optimal solution
		if (wt[n - 1] < W) {
			double value = knapSackHelper(W, wt, val, n - 1);
			use.put("" + n + "," + df.format(W), value);
			return value;
		}
		// Return the maximum of two cases:
		// (1) nth item included
		// (2) not included
		else {
			double case1 = knapSackHelper(W - wt[n - 1], wt, val, n - 1);
			double case2 = knapSackHelper(W, wt, val, n - 1);
			double end = max(val[n - 1] + case1, case2);
			if(end==0) end = val[n - 1] + case1;
			use.put("" + n + "," + df.format(W), end);
			return end;
		}
	}

	public static double driver(double W, Double wt[], Integer val[], int n) {
		use = new HashMap<String, Double>();
		return knapSack(W, wt, val, n);
	}

	public static boolean[] getLastSelection(int n, double res, Double wt[], Integer val[], double w) {
		boolean[] toRe = new boolean[wt.length];
		for (int i = n; i > 0 && res > 0; i--) {

			// either the result comes from the top
			// (K[i-1][w]) or from (val[i-1] + K[i-1]
			// [w-wt[i-1]]) as in Knapsack table. If
			// it comes from the latter one/ it means
			// the item is included.
			if (res == use.get("" + (i - 1) + "," + df.format(w))) {
				toRe[i - 1] = false;
				continue;
			} else {

				// This item is included.
				System.out.print(wt[i - 1] + " ");
				toRe[i - 1] = true;

				// Since this weight is included its
				// value is deducted
				res = res - val[i - 1];
				w = w - wt[i - 1];
			}
		}
		return toRe;
	}

	public static boolean[] handler(double W, Double wt[], Integer val[]) {
		use = new HashMap<String, Double>();
		int n = val.length;
		double res = knapSack(W, wt, val, n);
		System.out.print("Optimal Solution:" + res);
		return getLastSelection(n, res, wt, val, W);
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
			for (int i = 0; i < val_wind.size(); i++) {
				value.add(-val_wind.get(i) * count_wind.get(i) * freq);
				count.add(-count_wind.get(i));
			}
			for (int i = 0; i < val_light.size(); i++) {
				value.add(-val_light.get(i) * count_light.get(i) * freq);
				count.add(-count_light.get(i));
			}
			for (int i = 0; i < val_wave.size(); i++) {
				value.add(-val_wave.get(i) * count_wave.get(i) * freq);
				count.add(-count_wave.get(i));
			}
			for (int i = 0; i < val_current.size(); i++) {
				value.add(-val_current.get(i) * count_current.get(i) * freq);
				count.add(-count_current.get(i));
			}
			System.out.print("value:");
			printList(value);
			System.out.print("count:");
			printList(count);

			for (boolean e : handler(W, value.toArray(new Double[0]), count.toArray(new Integer[0]))) {
				System.out.print((e) ? 1 : 0);
			}
		}
		;
		return null;
	}

	private static void printList(List<? extends Object> a) {
		for (int i = 0; i < a.size(); i++) {
			System.out.print(a.get(i) + ",");
		}
		System.out.println();
	}

	// Driver program to test
	// above function
	public static void main(String args[]) throws FileNotFoundException, RuntimeException {
		/**
		 * int val[] = new int[] { 92,57,49,68,60,43,67,84,87,72 }; int wt[] = new int[]
		 * { 23,31, 29,44,53,38,63,85,89,82 }; double W = 165;
		 * 
		 * P07 is a set of 15 weights and profits for a knapsack of capacity 750, from
		 * Kreher and Stinson, with an optimal profit of 1458.
		 **/
		Integer val[] = new Integer[] { 135, 139, 149, 150, 156, 163, 173, 184, 192, 201, 210, 214, 221, 229, 240 };
		Double wt[] = new Double[] { 70.0, 73.0, 77.0, 80.0, 82.0, 87.0, 90.0, 94.0, 98.0, 106.0, 110.0, 113.0, 115.0,
				118.0, 120.0 };
		// wt: coefficiency of limitation <=
		// val: profit, same as total day count
		double W = 750;

		//for (boolean e : handler(W, wt, val)) {
		//	System.out.print((e) ? 1 : 0);
		//}

		System.out.println();

		DataFrame fr = CSVFileReader.readCSV(new File("OptimizationData.csv"));
		handleFrame(fr, 10000, 1342*365, 1000);

	}

}
