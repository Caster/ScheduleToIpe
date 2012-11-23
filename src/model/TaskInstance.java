package model;

/**
 * A TaskInstance is an instance of a task, also called a job.
 * It not only has a Task, but also a start and an end time.
 * The start time is when the instance started running on the CPU,
 * the end time is when it stopped.
 * 
 * Note that we do not really use the term "TaskInstance" here properly,
 * as the end time may also indicate the task was preempted. Actually,
 * a TaskInstance is one instance of a task, thus, every period a Task
 * has a TaskInstance. For us, a task may have one or more instance in
 * a period, depending on if preemption occurred or not. This has to do
 * with implementation details - it makes it easier to work with.
 * 
 * @author Thom Castermans
 */
public class TaskInstance implements Comparable<TaskInstance> {

	private Task task;
	private double start;
	private double end;
	
	/**
	 * Create a new TaskInstance of the given task with given
	 * start and end time.
	 * 
	 * @param parentTask The Task of which this TaskInstance is an instance.
	 * @param startTime Time when instance starts running on the CPU.
	 * @param endTime Time when instance stops running on the CPU.
	 */
	public TaskInstance(Task parentTask, double startTime, double endTime) {
		this.setTask(parentTask);
		this.setStart(startTime);
		this.setEnd(endTime);
	}

	/**
	 * Return the Task of which this TaskInstance is an instance.
	 * 
	 * @return Task of which this TaskInstance is an instance.
	 */
	public Task getTask() {
		return task;
	}

	/**
	 * Change the Task of which this TaskInstance is an instance.
	 * 
	 * @param newParentTask The Task of which this TaskInstance
	 *                      is an instance now.
	 */
	public void setTask(Task newParentTask) {
		this.task = newParentTask;
	}

	/**
	 * Return time at which this TaskInstance starts running
	 * on the CPU.
	 * 
	 * @return Time at which this TaskInstance starts running
	 *         on the CPU.
	 */
	public double getStart() {
		return start;
	}

	/**
	 * Change the time at which this TaskInstance starts running
	 * on the CPU.
	 * 
	 * @param newStartTime Time at which this TaskInstance starts
	 *                     running on the CPU now.
	 */
	public void setStart(double newStartTime) {
		this.start = newStartTime;
	}

	/**
	 * Return time at which this TaskInstance stops running
	 * on the CPU.
	 * 
	 * @return Time at which this TaskInstance stops running
	 *         on the CPU.
	 */
	public double getEnd() {
		return end;
	}

	/**
	 * Change the time at which this TaskInstance stops running
	 * on the CPU.
	 * 
	 * @param newEndTime Time at which this TaskInstance stops
	 *                   running on the CPU now.
	 */
	public void setEnd(double newEndTime) {
		this.end = newEndTime;
	}

	@Override
	public int compareTo(TaskInstance that) {
		return (int) Math.signum(this.start - that.start);
	}
	
	@Override
	public String toString() {
		return "TaskInstance [" + task.getName() + ": " + start + ", " + end + "]";
	}
}
