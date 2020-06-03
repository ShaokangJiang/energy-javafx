package application;

import java.util.HashMap;

public class knapsack {

	static HashMap<String, Double> use;

	// A utility function that returns
	// maximum of two integers
	static double max(double d, double e) {
		return (d > e) ? d : e;
	}

	static double knapSackHelper(double W, int wt[], int val[], int n) {
		if (use.get("" + n + "," + W) != null) {
			return use.get("" + n + "," + W);
		}
		return knapSack(W, wt, val, n);
	}

	// Returns the maximum value that
	// can be put in a knapsack of
	// capacity W
	static double knapSack(double W, int wt[], int val[], int n) {
		// Base Case
		if (n == 0 || W == 0) {
			use.put("" + n + "," + W, 0.00);
			return 0;
		}

		// If weight of the nth item is
		// more than Knapsack capacity W,
		// then this item cannot be included
		// in the optimal solution
		if (wt[n - 1] > W) {
			double value = knapSackHelper(W, wt, val, n - 1);
			use.put("" + n + "," + W, value);
			return value;
		}
		// Return the maximum of two cases:
		// (1) nth item included
		// (2) not included
		else {
			double case1 = knapSackHelper(W - wt[n - 1], wt, val, n - 1);
			double case2 = knapSackHelper(W, wt, val, n - 1);
			double end = max(val[n - 1] + case1, case2);
			use.put("" + n + "," + W, end);
			return end;
		}
	}

	public static double driver(double W, int wt[], int val[], int n) {
		use = new HashMap<String, Double>();
		return knapSack(W, wt, val, n);
	}

	public static boolean[] getLastSelection(int n, double res, int wt[], int val[], double w) {
		boolean[] toRe = new boolean[wt.length];
		for (int i = n; i > 0 && res > 0; i--) {

			// either the result comes from the top
			// (K[i-1][w]) or from (val[i-1] + K[i-1]
			// [w-wt[i-1]]) as in Knapsack table. If
			// it comes from the latter one/ it means
			// the item is included.
			if (res == use.get("" + (i - 1) + "," + w)) {
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

	// Driver program to test
	// above function
	public static void main(String args[]) {
		/**
		 * int val[] = new int[] { 92,57,49,68,60,43,67,84,87,72 }; int wt[] = new int[]
		 * { 23,31, 29,44,53,38,63,85,89,82 }; double W = 165;
		 * 
		 **/
		int val[] = new int[] { 135, 139, 149, 150, 156, 163, 173, 184, 192, 201, 210, 214, 221, 229, 240 };
		int wt[] = new int[] { 70, 73, 77, 80, 82, 87, 90, 94, 98, 106, 110, 113, 115, 118, 120 };
		double W = 750;

		int n = val.length;
		double res = driver(W, wt, val, n);
		System.out.println(res);
		for (boolean e : getLastSelection(n, res, wt, val, W)) {
			System.out.print((e) ? 1 : 0);
		}

	}

}
