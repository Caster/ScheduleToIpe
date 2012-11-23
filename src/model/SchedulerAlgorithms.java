package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * The scheduler algorithms supported by this application.
 * 
 * @author Barnabbas
 */
public final class SchedulerAlgorithms {

	/**
	 * A Rate Monotonic scheduler; a static priority scheduler that uses the
	 * period for priority
	 */
	public final static SchedulerAlgorithm RATE_MONOTONIC = new StaticPriorityScheduler() {

		@Override
		protected double getPriority(Task task) {
			return -task.getPeriod();
		}
	};

	/**
	 * A Deadline Monotonic scheduler; a static priority scheduler that uses the
	 * deadline for priority
	 */
	public final static SchedulerAlgorithm DEADLINE_MONOTONIC = new StaticPriorityScheduler() {

		@Override
		protected double getPriority(Task task) {
			return -task.getDeadline();
		}
	};

	/**
	 * An Earliest Deadline First scheduler; a dynamic priority scheduler that always
	 * schedules the task with the earliest deadline.
	 */
	public final static SchedulerAlgorithm EARLIEST_DEADLINE_FIRST = new DynamicPriorityScheduler() {

		@Override
		protected double getPriority(Task task, int time) {
			double relativeTime = time % task.getPeriod();
			return -(task.getDeadline() - relativeTime);
		}
	};

	private SchedulerAlgorithms() {
		// not used, this is a singleton object...
	}

	/**
	 * An abstract class used to create Dynamic Priority schedulers.
	 */
	private static abstract class DynamicPriorityScheduler implements
			SchedulerAlgorithm {
		/**
		 * Assigns a priority to a Task at a given time. A higher priority will
		 * be scheduled first.
		 * 
		 * @param task
		 *            the Task to assign a priority.
		 * @param time
		 *            the current timestamp
		 * @return a priority for Task {@code task}.
		 */
		protected abstract double getPriority(Task task, int time);

		public Schedule createSchedule(Set<Task> tasks) {

			// the cyclus of this task set
			int lcm = lcm(tasks);

			// the final schedule
			List<Task> schedule = new ArrayList<Task>(lcm);

			// if there is a deadline missed
			boolean deadlineMissed = false;

			// the tasks that are left, for each time unit of execution time
			// left, there will be a task.
			List<Task> tasktimeLeft = new LinkedList<Task>();

			// walking over the timestamps
			for (int t = 0; t < lcm && !deadlineMissed; t++) {

				// adding new tasks
				for (Task task : getReleasedTasksAt(t, tasks)) {
					
					// checking for deadline miss from previous instance
					if (tasktimeLeft.contains(task)) { 
						// this task has still time left from its previous instance
						deadlineMissed = true;
						continue;
					}
					
					// adding all required time units of the task to the queue
					for (int i = 0; i < task.getExecutionTime(); i++) {
						
						tasktimeLeft.add(task);
					}
				}

				// the task with the highest priority
				Task nextTask = null;
				// the priority of nextTask
				double nextPriority = Double.MIN_VALUE;

				// getting task with highest priority
				for (Task task : tasktimeLeft) {
					double priority = getPriority(task, t);
					if (priority > nextPriority) {
						nextTask = task;
						nextPriority = priority;
					}
				}

				// removing the time of the Task
				tasktimeLeft.remove(nextTask);

				// setting the task into the schedule (or determine deadline
				// miss)
				if (nextTask != null
						&& nextTask.getDeadline() <= t % nextTask.getPeriod()) { // deadline
																					// missed
					deadlineMissed = true;
				} else { // can schedule this
					schedule.add(nextTask);
				}

			}

			// set deadlineMissed when there is stuff left in the list
			deadlineMissed |= !tasktimeLeft.isEmpty();

			return new Schedule(schedule, tasks, !deadlineMissed);
		}

	}

	/**
	 * An abstract class used to created static priority Scheduler algorithms.
	 */
	private static abstract class StaticPriorityScheduler implements
			SchedulerAlgorithm {

		/**
		 * Assigns a priority to a Task. A higher priority will be scheduled
		 * first.
		 * 
		 * @param task
		 *            the Task to assign a priority.
		 * @return a priority for Task {@code task}.
		 */
		protected abstract double getPriority(Task task);

		@SuppressWarnings("boxing")
		public Schedule createSchedule(Set<Task> tasks) {

			// getting the comperator
			Map<Task, Double> priorities = new HashMap<Task, Double>();
			for (Task task : tasks) {
				priorities.put(task, getPriority(task));
			}
			TaskComparator comp = new TaskComparator(priorities);

			// the cyclus of this task set
			int lcm = lcm(tasks);

			// the queue containing an instance of a task for each execution
			// time left
			PriorityQueue<Task> queue = new PriorityQueue<Task>(lcm + 1, comp);

			// the final schedule
			List<Task> schedule = new ArrayList<Task>();

			boolean deadlineMissed = false;

			// walking over the timestamps
			for (int t = 0; t < lcm && !deadlineMissed; t++) {

				// adding new tasks
				for (Task task : getReleasedTasksAt(t, tasks)) {

					// checking for deadline misses
					if (queue.contains(task)) { 
						// this task has still time left from its previous instance
						deadlineMissed = true;
						continue;
					}

					// adding all required time units of the task to the queue
					for (int i = 0; i < task.getExecutionTime(); i++) {
						queue.add(task);
					}
				}

				// apparantly the queue returns null when empty, so should
				// work...
				Task task = queue.poll();

				if (task != null && task.getDeadline() <= t % task.getPeriod()) { // deadline
																					// missed
					deadlineMissed = true;
				} else { // can schedule this
					schedule.add(t, task);
				}

			}

			// set deadlineMissed when there is stuff left in the queue
			deadlineMissed |= !queue.isEmpty();

			return new Schedule(schedule, tasks, !deadlineMissed);
		}

	}

	/**
	 * An utility class for the Schedulers.
	 */
	private static class TaskComparator implements Comparator<Task> {

		private Map<Task, Double> priorities;

		TaskComparator(Map<Task, Double> tPriorities) {
			this.priorities = tPriorities;
		}

		@SuppressWarnings("boxing")
		public int compare(Task t1, Task t2) {
			// reversed, because a higher priority needs to be at the front.
			return (int) Math.signum(priorities.get(t2) - priorities.get(t1));
		}
	}

	/**
	 * Gets the Tasks that are released at the given time.
	 * 
	 * @param time
	 *            the time stamp to investigate
	 * @param tasks
	 *            the tasks to search through
	 * @return the Tasks that are in {@code task} and are released at
	 *         {@code time}
	 */
	private static Collection<Task> getReleasedTasksAt(int time, Set<Task> tasks) {
		Collection<Task> result = new LinkedList<Task>();
		for (Task task : tasks) {
			if (time % task.getPeriod() == 0) { // we are at the start of his
												// period...
				result.add(task);
			}
		}

		return result;
	}

	/**
	 * calculates the lcm for the given tasks.
	 * 
	 * @param tasks
	 * @return
	 */
	private static int lcm(Collection<Task> tasks) {
		int[] periods = new int[tasks.size()];
		int i = 0;
		for (Task task : tasks) {
			periods[i] = (int) Math.ceil(task.getPeriod());
			i++;
		}

		return lcm(periods);
	}

	// code copied from stackoverflow:
	// http://stackoverflow.com/questions/4201860/how-to-find-gcf-lcm-on-a-set-of-numbers
	private static int gcd(int a, int b) {
		while (b > 0) {
			int temp = b;
			b = a % b; // % is remainder
			a = temp;
		}
		return a;
	}

	private static int lcm(int a, int b) {
		return a * (b / gcd(a, b));
	}

	private static int lcm(int... input) {
		int result = input[0];
		for (int i = 1; i < input.length; i++)
			result = lcm(result, input[i]);
		return result;
	}
}
