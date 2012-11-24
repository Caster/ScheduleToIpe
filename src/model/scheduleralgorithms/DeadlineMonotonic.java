package model.scheduleralgorithms;

import model.Task;

/**
 * The DM, for Deadline Monotonic, scheduler algorithm schedules
 * tasks using the deadlines of tasks: the shorter a relative deadline
 * of a task is, the higher its priority is.
 * 
 * @author Thom Castermans
 */
public class DeadlineMonotonic extends StaticPriorityScheduler {

	@Override
	protected int getPriority(Task task) {
		return -task.getDeadline();
	}

}
