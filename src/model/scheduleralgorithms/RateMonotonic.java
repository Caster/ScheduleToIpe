package model.scheduleralgorithms;

import model.Task;

/**
 * The RM, for Rate Monotonic, scheduler algorithm schedules
 * tasks using the periods of tasks: the shorter a period of a task is,
 * the higher its priority is.
 * 
 * @author Thom Castermans
 */
public class RateMonotonic extends StaticPriorityScheduler {

	@Override
	protected int getPriority(Task task) {
		return -task.getPeriod();
	}

}
