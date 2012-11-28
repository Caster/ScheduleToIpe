package model.scheduleralgorithms;

import model.Task;

/**
 * The RR, for Round Robin, scheduler algorithm schedules
 * tasks one after another. So, if you have tasks A, B and
 * C, then in the first tick, A may run. In the second tick,
 * B runs. In the third thick, C runs and then A again, et
 * cetera.
 * 
 * @author Thom Castermans
 */
public class RoundRobin extends DynamicPriorityScheduler {

	/** The length of one timeslice. */
	private double sL;
	
	/**
	 * Construct a new Round Robin scheduler where each
	 * timeslice has the given length.
	 * 
	 * @param sliceLength The length in time units of one
	 *                    timeslice.
	 */
	public RoundRobin(double sliceLength) {
		setUpdatePriorityEveryTick(true);
		this.sL = sliceLength;
	}
	
	@Override
	protected int getPriority(Task task, double time) {
		// Depending on the time and task, one task has
		// high priority and will run, while all other
		// tasks have low priority
		int i = 0;
		int taskIndex = -1;
		for (Task t : tasksToBeScheduled) {
			if (t.equals(task)) {
				taskIndex = i;
				break;
			}
			i++;
		}
		if (taskIndex == -1)  return 0;
		return (taskIndex == Math.round(time / sL) % tasksToBeScheduled.size() ? 1 : 0);
	}

}
