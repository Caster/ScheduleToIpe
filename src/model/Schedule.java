package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * The Schedule object.
 * This object contains information about our schedule.
 * 
 * @author Barnabbas
 */
public class Schedule {
	
	private final Set<Task> tasks;
	private final List<Task> taskSchedule;
	private final boolean isFeasible;
	
	/**
	 * Constructs a new Schedule.<br>
	 * The schedule will consists of the Tasks in {@code schedule} such that 
	 * {@code getTask(i) == schedule.get(i)}.
	 * When there is no Task at a certain moment, then {@code null} has to be placed there.
	 * The length of {@code schedule} indicates the LCM for this Schedule.
	 * 
	 * @param sSchedule the schedule for the Tasks.
	 * @param sTasks the Tasks this schedule contains (can contain more then schedule).
	 * @param sIsFeasible if this schedule is a schedule without deadline miss
	 */
	Schedule(List<Task> sSchedule, Set<Task> sTasks, boolean sIsFeasible){
		// using unmodifiables to guarantee immutability.
		taskSchedule = Collections.unmodifiableList(new ArrayList<Task>(sSchedule));
		this.tasks = Collections.unmodifiableSet(sTasks);
		this.isFeasible = sIsFeasible;
	}
	
	/**
	 * Gets the tasks of this Schedule.
	 * @return a List containing the tasks of this Schedule.
	 */
	public Set<Task> getTasks(){
		return tasks;
	}
	
	/**
	 * The task that runs immediatly after the given time.
	 * Returns {@code null} when no Task will run at that moment. <br>
	 * {@code time} must be smaller then the LCM of this Schedule.
	 * 
	 * 
	 * @param time the timestamp of the moment you want the task of.
	 * @return the Task that runs immediatly after the given timestamp or 
	 * {@code null} if there is no such Task.
	 * 
	 * @throws IllegalArgumentException if {@code time} is larger then the LCM
	 */
	public Task getTaskAt(int time) throws IllegalArgumentException{
		if (time >= taskSchedule.size()){
			throw new IllegalArgumentException("The given time is not within lcm, time is " + time);
		}
		
		return taskSchedule.get(time);
	}
	
	/**
	 * The length of a cycle of this Schedule.<br>
	 * This is the least common multiplier of the periods of the Tasks.
	 * @return Length of a cycle of this Schedule.
	 */
	public int getLcm(){
		// this assumes that the given taskSchedule is valid
		return taskSchedule.size();
	}
	
	/**
	 * Determines whether this Schedule is a valid Schedule. A Schedule is valid when there are no deadline misses.
	 * @return if this Schedule is feasible.
	 */
	public boolean isFeasible(){
		return isFeasible;
	}

	@Override
	public String toString() {
		return "Schedule [taskSchedule=" + taskSchedule + ", isFeasible="
				+ isFeasible + "]";
	}

	
}
