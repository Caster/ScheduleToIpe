
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
	
	@override
	public String toString(){
		return "Task " + getName();
	}

}
