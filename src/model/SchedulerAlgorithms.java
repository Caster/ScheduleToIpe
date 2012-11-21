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
 * The schedular algorithms supported by this application.
 * 
 * @author Barnabbas
 */
public final class SchedulerAlgorithms {

	/**
	 * A Rate Monotonic schedular; a static priority schedular that uses the period for priority
	 */
	public final SchedulerAlgorithm RATE_MONOTONIC = new StaticPriorityScheduler(){
		protected int getPriority(Task task){
			return -task.getPeriod();
		}
	};
	
	/**
	 * A Deadline Monotonic schedular; a static priority schedular that uses the deadline for priority
	 */
	public final SchedulerAlgorithm DEADLINE_MONOTONIC = new StaticPriorityScheduler(){
		protected int getPriority(Task task){
			return -task.getDeadline();
		}
	};
	
	public final SchedulerAlgorithm EARLIEST_DEADLINE_FIRST = new DynamicPrioritySchedular() {
		
		@Override
		protected int getPriority(Task task, int time) {
			int relativeTime = time % task.getPeriod();
			return -(task.getDeadline() - relativeTime);
		}
	};

	private SchedulerAlgorithms() {
		// not used, this is a singleton object...
	}
	
	/**
	 * An abstract class used to create Dynamic Priority schedulers.
	 */
	private static abstract class DynamicPrioritySchedular implements SchedulerAlgorithm {
		/**
		 * Assigns a priority to a Task at a given time.
		 * A higher priority will be scheduled first.
		 * 
		 * @param task the Task to assign a priority.
		 * @param time the current timestamp
		 * @return a priority for Task {@code task}.
		 */
		protected abstract int getPriority(Task task, int time);

		public Schedule createSchedule(Set<Task> tasks) {
			
			// the cyclus of this task set
			int lcm = lcm(tasks);
			
			// the final schedule
			List<Task> schedule = new ArrayList<Task>(lcm);
			
			// if there is a deadline missed
			boolean deadlineMissed = false;
			
			// the tasks that are left, for each time unit of execution time left, there will be a task.
			List<Task> tasktimeLeft = new LinkedList<Task>();
			
			// walking over the timestamps
			for (int t = 0; t < lcm && !deadlineMissed; t++){
				
				// adding new tasks
				for (Task task: getReleasedTasksAt(t, tasks)){
					// adding all required time units of the task to the queue
					for (int i = 0; i < task.getExecutionTime(); i++){
						tasktimeLeft.add(task);
					}
				}
				
				// the task with the highest priority
				Task nextTask = null;
				// the priority of nextTask
				int nextPriority = Integer.MIN_VALUE;
				
				// getting task with highest priority
				for (Task task: tasktimeLeft){
					int priority = getPriority(task, t);
					if (priority > nextPriority){
						nextTask = task;
						nextPriority = priority;
					}
				}
				
				// setting the task into the schedule (or determine deadline miss)
				if (nextTask.getDeadline() >= t){ // deadline missed
					deadlineMissed = true;
				} else { // can schedule this
					schedule.add(nextTask);
				}
				
			}
			
			return new Schedule(schedule, !deadlineMissed);
		}
		
	}

	
	/**
	 * An abstract class used to created static priority Scheduler algorithms.
	 */
	private static abstract class StaticPriorityScheduler implements
			SchedulerAlgorithm {

		/**
		 * Assigns a priority to a Task. A higher priority will be scheduled first.
		 * 
		 * @param task the Task to assign a priority.
		 * @return a priority for Task {@code task}.
		 */
		protected abstract int getPriority(Task task);

		public Schedule createSchedule(Set<Task> tasks) {

			// getting the comperator
			Map<Task, Integer> priorities = new HashMap<Task, Integer>();
			for (Task task : tasks) {
				priorities.put(task, getPriority(task));
			}

			TaskComparator comp = new TaskComparator(priorities);
			PriorityQueue<Task> queue = new PriorityQueue<Task>(tasks.size(),
					comp);
			
			// the cyclus of this task set
			int lcm = lcm(tasks);
			
			// the final schedule
			List<Task> schedule = new ArrayList<Task>(lcm);
			
			boolean deadlineMissed = false;
			
			// walking over the timestamps
			for (int t = 0; t < lcm && !deadlineMissed; t++){
				
				// adding new tasks
				for (Task task: getReleasedTasksAt(t, tasks)){
					// adding all required time units of the task to the queue
					for (int i = 0; i < task.getExecutionTime(); i++){
						queue.add(task);
					}
				}
				
				// apparantly the queue returns null when empty, so should work...
				Task task = queue.poll();
				
				if (task.getDeadline() >= t){ // deadline missed
					deadlineMissed = true;
				} else { // can schedule this
					schedule.add(task);
				}
				
			}
			
			return new Schedule(schedule, !deadlineMissed);
		}
		

	}

	/**
	 * An utility class for the Schedulers.
	 */
	private static class TaskComparator implements Comparator<Task> {

		private Map<Task, Integer> priorities;

		TaskComparator(Map<Task, Integer> priorities) {
			this.priorities = priorities;
		}

		public int compare(Task t1, Task t2) {
			// reversed, because a higher priority needs to be at the front.
			return priorities.get(t1) - priorities.get(t2);
		}
	}
	
	/**
	 * Gets the Tasks that are released at the given time.
	 * @param time the time stamp to investigate
	 * @param tasks the tasks to search through
	 * @return the Tasks that are in {@code task} and are released at {@code time}
	 */
	private static Collection<Task> getReleasedTasksAt(int time, Set<Task> tasks){
		Collection<Task> result = new LinkedList<Task>();
		for (Task task: tasks){
			if (time % task.getPeriod() == 0){ // we are at the start of his period...
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
		for (Task task: tasks){
			periods[i] = task.getPeriod();
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
