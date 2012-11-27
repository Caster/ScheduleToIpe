package model.scheduleralgorithms;

import model.Task;

/**
 * The EDF, for Earliest Deadline First, scheduler algorithm schedules
 * tasks using the deadlines of tasks: the closer a task is to its deadline,
 * the higher its priority is.
 * 
 * @author Thom Castermans
 */
public class EarliestDeadlineFirst extends DynamicPriorityScheduler {
	
	@Override
	protected int getPriority(Task task, double time) {
		// calculate absolute deadline and invert this, as the priority is
		// opposite of the deadline
		return -task.getAbsoluteDeadline(time);
	}

}
