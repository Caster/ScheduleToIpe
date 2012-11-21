
/**
 * A Task. Represents one task in the Schedule.
 * @author Barnabbas
 *
 */
public class Task {
	
	
	/**
	 * The name of this Task.
	 * @return The name of this Task.
	 */
	public String getName(){
		
	}
	
	/**
	 * The period of this Task.
	 * @return The period of this Task.
	 */
	public int getPeriod(){
		
	}
	
	/**
	 * The relative deadline of this Task. 
	 * @return the relative deadline of this Task.
	 */
	public int getDeadline(){
		
	}
	
	/**
	 * The execution time of this Task. That is the running time of each Task instance.
	 * @return the execution time of this Task.
	 */
	public int getExecutionTime(){
		
	}
	
	@override
	public String toString(){
		return "Task " + getName();
	}

}
