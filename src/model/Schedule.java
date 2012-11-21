
package Model

/**
 * The Schedule object.
 * This object contains information about our schedule.
 * 
 * @author Barnabbas
 */
public class Schedule {
	
	/**
	 * Gets the tasks of this Schedule.
	 * @return a List containing the tasks of this Schedule.
	 */
	public List<Task> getTasks(){
		return null;
	}
	
	/**
	 * The task that runs immediatly after the given time.
	 * @param time the timestamp of the moment you want the task of.
	 * @return the Task that runs immediatly after the given timestamp.
	 */
	public Task getTaskAt(int time){
		return null;
	}
	
	/**
	 * The length of a cycle of this Schedule.<br>
	 * This is the least common multiplier of the periods of the Tasks.
	 * @return Length of a cycle of this Schedule.
	 */
	public int lcm(){
		return null;
	}

}
