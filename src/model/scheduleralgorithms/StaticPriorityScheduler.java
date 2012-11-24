package model.scheduleralgorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import model.Schedule;
import model.SchedulerAlgorithm;
import model.Task;
import model.TaskExecutionTime;
import model.TaskInstance;
import model.Utils;

/**
 * An abstract class used to create Static Priority schedulers.
 * 
 * @author Barnabbas
 * @author Thom Castermans
 */
public abstract class StaticPriorityScheduler implements SchedulerAlgorithm {
	
	/**
	 * Assigns a priority to a Task.
	 * 
	 * @param task The Task to assign a priority to.
	 * @return A priority for Task {@code task}.
	 */
	protected abstract int getPriority(Task task);

	/**
	 * Create a schedule for the given set of tasks.
	 * 
	 * @param tasks The set of tasks to be scheduled.
	 */
	public Schedule createSchedule(Set<Task> tasks) {
		// the cyclus of this task set
		int lcm = Utils.lcm(tasks);

		// the final schedule
		List<TaskInstance> schedule = new ArrayList<TaskInstance>();

		// queue, used to get the task with highest priority and schedule it
		PriorityQueue<TaskExecutionTime> taskQueue = new PriorityQueue<TaskExecutionTime>();
		for (Task t : tasks) {
			t.setPriority(getPriority(t));
			taskQueue.add(new TaskExecutionTime(t));
		}
		
		double sysTime = 0;
		double newSysTime = 0;
		TaskExecutionTime te;
		while (sysTime < lcm) {
			// If the queue is empty, skip to the time when a task becomes available
			// and add that task to the queue
			if (taskQueue.isEmpty()) {
				double minStartTime = Double.MAX_VALUE;
				Task minStartTask = null;
				for (Task t : tasks) {
					double nextExecution = sysTime - (sysTime % t.getPeriod()) + t.getPeriod();
					if (nextExecution < minStartTime && nextExecution < lcm) {
						minStartTime = nextExecution;
						minStartTask = t;
					}
				}
				
				// No task available until the end? Quit then.
				if (minStartTask == null)  break;
				
				// Skip to task, jaj.
				sysTime = minStartTime;
				taskQueue.add(new TaskExecutionTime(minStartTask));
			}
			
			// Get a task from the queue, let it execute
			te = taskQueue.peek();
			newSysTime = sysTime + te.execute(1);
			schedule.add(new TaskInstance(te.getTask(), sysTime, newSysTime));
			// Remove the task from the queue if it is done with its execution
			if (te.getExecutionTimeLeft() == 0) {
				taskQueue.poll();
			}
			// If the deadline is passed after execution of the task,
			// we have a deadline miss and thus return the schedule so
			// far, that is not feasible.
			if (te.getTask().getAbsoluteDeadline(sysTime) < newSysTime) {
				return new Schedule(schedule, false);
			}
			// If a task becomes available while executing this task,
			// add the new task(s) to the queue
			for (Task t : tasks) {
				/* We check if a new period lies within the interval (sysTime, newSysTime].
				 * We do this by looking at the old and new systime modulo the task period.
				 * 
				 * Now, if the old system time is before a "start of a period" of the task
				 * and the new system time is after that start of a  period, then after this
				 * calculation the result of the old system time will suddenly be greater
				 * than the result of the new system time.
				 */
				if (sysTime % t.getPeriod() > newSysTime % t.getPeriod() && newSysTime < lcm) {
					taskQueue.add(new TaskExecutionTime(t));
				}
			}
			sysTime = newSysTime;
		}

		return new Schedule(schedule, sysTime <= lcm);
	}

}
