package model;

import java.util.Set;

/**
 * This class provides some static methods that come in useful in some cases.
 * 
 * @author Thom Castermans
 */
public class Utils {

	/*
	 * Code for GCD and LCM comes from StackOverflow:
	 *  http://stackoverflow.com/a/4202114/962603
	 * JavaDoc created myself.
	 */
	/**
	 * Calculate the GCD of two integers.
	 * 
	 * @param a First integer.
	 * @param b Second integer.
	 * @return GCD of {@code a} and {@code b}.
	 */
	public static int gcd(int a, int b) {
		while (b > 0) {
			int temp = b;
			b = a % b; // % is remainder
			a = temp;
		}
		return a;
	}

	/**
	 * Calculate the LCM of the periods of the given tasks.
	 * 
	 * @param tasks The tasks of which to calculate the LCM.
	 * @return The LCM of the periods of the given tasks.
	 */
	public static int lcm(Set<Task> tasks) {
		int[] periods = new int[tasks.size()];
		int i = 0;
		for (Task t : tasks) {
			periods[i] = t.getPeriod();
			i++;
		}
		return Utils.lcm(periods);
	}
	
	/**
	 * Calculate the LCM of two integers.
	 * 
	 * @param a First integer.
	 * @param b Second integer.
	 * @return LCM of {@code a} and {@code b}.
	 */
	public static int lcm(int a, int b) {
		return a * (b / gcd(a, b));
	}

	/**
	 * Calculate the LCM of a set of integers.
	 * 
	 * @param input One or more integers to calculate the LCM of.
	 * @return The LCM of the given integers.
	 */
	public static int lcm(int... input) {
		int result = input[0];
		for (int i = 1; i < input.length; i++)
			result = lcm(result, input[i]);
		return result;
	}
	
}
