package model;

import java.util.Set;

/**
 * A SchedulerAlgorithm is used to make decisions about what Task should been scheduled when.
 * @author Barnabbas
 */
public interface SchedulerAlgorithm {
	
	/**
	 * Creates a Schedule based on the given tasks.
	 *  
	 * @param tasks The Task Set that must be scheduled.
	 * @return a Schedule based on the given Task Set.
	 */
	public Schedule createSchedule(Set<Task> tasks);
}
