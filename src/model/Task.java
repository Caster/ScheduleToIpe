package model;

/**
 * A Task. Represents one task in the Schedule.
 * 
 * @author Barnabbas
 * @author Thom Castermans
 */
public class Task implements Comparable<Task> {
	
	private String name;
	private int period;
	private int deadline;
	private int priority;
	private double executionTime;
	
	/**
	 * Constructs a new Task.
	 * 
	 * @param tName The name of this Task
	 * @param tPeriod The period of this Task
	 * @param tDeadline The relative deadline of this Task
	 * @param tExecutionTime the execution time of this Task
	 */
	public Task(String tName, int tPeriod, int tDeadline, double tExecutionTime){
		this.name = tName;
		this.period = tPeriod;
		this.deadline = tDeadline;
		this.setPriority(0);
		this.executionTime = tExecutionTime;
	}
	
	
	/**
	 * The name of this Task.
	 * @return The name of this Task.
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Change the name of this task.
	 * 
	 * @param newName New name for this task.
	 */
	public void setName(String newName) {
		this.name = newName;
	}
	
	/**
	 * The period of this Task.
	 * @return The period of this Task.
	 */
	public int getPeriod(){
		return period;
	}
	
	/**
	 * Change the period of this task.
	 * 
	 * @param newPeriod New period for this task.
	 */
	public void setPeriod(int newPeriod) {
		this.period = newPeriod;
	}
	
	/**
	 * Given the current system time, return the absolute deadline
	 * of this task.
	 * 
	 * @param time The current system time.
	 * @return The absolute deadline of this task at the given time.
	 */
	public int getAbsoluteDeadline(double time) {
		// First, calculate the start of the period. Then, add the relative deadline.
		return ((int) Math.round(time - (time % getPeriod()))) + getDeadline();
	}
	
	/**
	 * The relative deadline of this Task. 
	 * @return the relative deadline of this Task.
	 */
	public int getDeadline(){
		return deadline;
	}
	
	/**
	 * Change the deadline of this task.
	 * 
	 * @param newDeadline New deadline for this task.
	 */
	public void setDeadline(int newDeadline) {
		this.deadline = newDeadline;
	}
	
	/**
	 * The execution time of this Task. That is the running time of each Task instance.
	 * @return the execution time of this Task.
	 */
	public double getExecutionTime(){
		return executionTime;
	}
	
	/**
	 * Change the execution time of this task.
	 * 
	 * @param newExecutionTime New execution time for this task.
	 */
	public void setExecutionTime(double newExecutionTime) {
		this.executionTime = newExecutionTime;
	}
	
	/**
	 * Return the priority of this task.
	 * 
	 * @return The priority of this task.
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * Change the priority of this task.
	 * 
	 * @param newPriority New priority for this task.
	 */
	public void setPriority(int newPriority) {
		this.priority = newPriority;
	}
	
	@Override
	public String toString(){
		return "Task " + getName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + deadline;
		long temp;
		temp = Double.doubleToLongBits(executionTime);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + period;
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		if (deadline != other.deadline)
			return false;
		if (Double.doubleToLongBits(executionTime) != Double
				.doubleToLongBits(other.executionTime))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (period != other.period)
			return false;
		return true;
	}

	@Override
	public int compareTo(Task that) {
		// a task with a higher priority is "less than" this task
		return that.priority - this.priority;
	}
}
