package model.scheduleralgorithms;

import java.util.Set;

import model.Schedule;
import model.Task;

/**
 * This class is a "wrapper class" that can be queried to see
 * which schedulers are available. Also, it is possible to
 * generate a schedule through this class.
 * 
 * @author Thom Castermans
 */
public class SupportedSchedulers {

	/**
	 * Supported (implemented) schedulers.
	 * This list is used by the GUI to show possible choices
	 * for scheduling algorithms.
	 */
	public static enum SUPPORTED_SCHEDULING_ALGORITHMS {
		/** Deadline Monotonic scheduling algorithm. */
		DM,
		/** Rate Monotonic scheduling algorithm. */
		RM,
		/** Earliest Deadline First scheduling algorithm. */
		EDF,
		/** Round Robin scheduling algorithm. */
		RR
	}
	
	private SupportedSchedulers() {
		// you cannot instantiate this class
	}
	
	/**
	 * Schedule a set of tasks with the given algorithm.
	 * 
	 * @param tasks The set of tasks to be scheduled.
	 * @param algorithm The scheduling algorithm to use.
	 * @return A schedule for the given set of tasks, generated
	 *         by the given algorithm.
	 */
	public static Schedule createSchedule(Set<Task> tasks, SUPPORTED_SCHEDULING_ALGORITHMS algorithm) {
		// Depending on the algorithm that is chosen, schedule the set of tasks
		switch (algorithm) {
			case DM :
				DeadlineMonotonic dm = new DeadlineMonotonic();
				return dm.createSchedule(tasks);
			case RM :
				RateMonotonic rm = new RateMonotonic();
				return rm.createSchedule(tasks);
			case EDF :
				EarliestDeadlineFirst edf = new EarliestDeadlineFirst();
				return edf.createSchedule(tasks);
			case RR :
				RoundRobin rr = new RoundRobin(1);
				return rr.createSchedule(tasks);
		}
		// We should never get here, above switch should always cover all available algorithms
		return null;
	}
}
