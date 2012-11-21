package model;

/**
 * A Task. Represents one task in the Schedule.
 * @author Barnabbas
 *
 */
public class Task {
	
	private final String name;
	private final int period;
	private final int deadline;
	private final int executionTime;
	
	/**
	 * Constructs a new Task.
	 * 
	 * @param name The name of this Task
	 * @param period The period of this Task
	 * @param deadline The relative deadline of this Task
	 * @param executionTime the execution time of this Task
	 */
	public Task(String name, int period, int deadline, int executionTime){
		this.name = name;
		this.period = period;
		this.deadline = deadline;
		this.executionTime = executionTime;
	}
	
	
	/**
	 * The name of this Task.
	 * @return The name of this Task.
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * The period of this Task.
	 * @return The period of this Task.
	 */
	public int getPeriod(){
		return period;
	}
	
	/**
	 * The relative deadline of this Task. 
	 * @return the relative deadline of this Task.
	 */
	public int getDeadline(){
		return deadline;
	}
	
	/**
	 * The execution time of this Task. That is the running time of each Task instance.
	 * @return the execution time of this Task.
	 */
	public int getExecutionTime(){
		return executionTime;
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
		result = prime * result + executionTime;
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
		if (executionTime != other.executionTime)
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

}
