package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The Schedule object.
 * This object contains information about our schedule.
 * 
 * @author Barnabbas
 * @author Thom Castermans
 */
public class Schedule {
	
	private final Set<Task> tasks;
	private ArrayList<TaskInstance> taskSchedule;
	private final boolean isFeasible;
	private final Task taskThatMissedDeadline;
	
	/**
	 * Constructs a new Schedule from a given list of TaskInstances and Tasks,
	 * where the schedule is feasible.
	 * 
	 * @param sSchedule The schedule for the Tasks.
	 */
	public Schedule (List<TaskInstance> sSchedule) {
		ArrayList<TaskInstance> sScheduleArrayList = new ArrayList<TaskInstance>(sSchedule);
		Collections.sort(sScheduleArrayList); // sort taskinstances on start-time
		taskSchedule = sScheduleArrayList;
		// build up set of tasks in schedule
		HashSet<Task> sTasks = new HashSet<Task>();
		for (TaskInstance ti : sSchedule) {
			if (!sTasks.contains(ti.getTask())) {
				sTasks.add(ti.getTask());
			}
		}
		this.tasks = Collections.unmodifiableSet(sTasks);
		this.isFeasible = true;
		this.taskThatMissedDeadline = null;
	}
	
	/**
	 * Constructs a new Schedule from a given list of TaskInstances and Tasks,
	 * where the schedule is not feasible and the given task has missed its
	 * deadline.
	 * 
	 * @param sSchedule The schedule for the Tasks.
	 * @param sMissedDeadline The task that missed its deadline.
	 */
	public Schedule (List<TaskInstance> sSchedule, Task sMissedDeadline) {
		ArrayList<TaskInstance> sScheduleArrayList = new ArrayList<TaskInstance>(sSchedule);
		Collections.sort(sScheduleArrayList); // sort taskinstances on start-time
		taskSchedule = sScheduleArrayList;
		// build up set of tasks in schedule
		HashSet<Task> sTasks = new HashSet<Task>();
		for (TaskInstance ti : sSchedule) {
			if (!sTasks.contains(ti.getTask())) {
				sTasks.add(ti.getTask());
			}
		}
		this.tasks = Collections.unmodifiableSet(sTasks);
		this.isFeasible = false;
		this.taskThatMissedDeadline = sMissedDeadline;
	}
	
	/**
	 * Compress this schedule. That is, if two TaskInstances next to eachother
	 * run the same task, then merge those.
	 */
	public void compress() {
		for (int i = taskSchedule.size() - 1; i > 0; i--) {
			if (taskSchedule.get(i).getTask().equals(taskSchedule.get(i - 1).getTask())) {
				TaskInstance newTI = new TaskInstance(
						taskSchedule.get(i).getTask(),
						taskSchedule.get(i - 1).getStart(),
						taskSchedule.get(i).getEnd()
					);
				taskSchedule.remove(i);
				taskSchedule.remove(i - 1);
				taskSchedule.add(i - 1, newTI);
			}
		}
	}
	
	/**
	 * Return the last task instance in this schedule.
	 * 
	 * @return The last task instance in this schedule.
	 */
	public TaskInstance getLastTaskInstance() {
		TaskInstance[] taskInstances = taskSchedule.toArray(new TaskInstance[] {});
		return taskInstances[taskInstances.length - 1];
	}
	
	/**
	 * Return the last instance of the task that missed its deadline.
	 * 
	 * @return Last instance of the task that missed its deadline.
	 *         {@code null} if no such task exists in this schedule.
	 */
	public TaskInstance getMissedTaskLastInstance() {
		if (isFeasible)  return null;
		TaskInstance[] taskInstances = taskSchedule.toArray(new TaskInstance[] {});
		for (int i = taskInstances.length - 1; i >= 0; i--) {
			if (taskInstances[i].getTask().equals(taskThatMissedDeadline)) {
				return taskInstances[i];
			}
		}
		return null;
	}
	
	/**
	 * Return the next task running after given time (given time is included
	 * in finding tasks). If no task runs at or after given time, then
	 * {@code null} is returned.
	 * 
	 * <p>The start time of the returned TaskInstance is the time when the task
	 * starts running on the CPU and the end time is when the task stops running.
	 * Note that when the task stops running, either another task may start running
	 * or no task may be scheduled for some time, or no task may be scheduled at all
	 * anymore, all of which cases can be found by calling this method with the end
	 * time of the returned TaskInstance.
	 * 
	 * @param time Time to check.
	 * @return TaskInstance of task running at or after given time, or
	 *         {@code null} if no such tasks exists in this schedule.
	 */
	public TaskInstance getNextTaskAt(double time) {
		for (TaskInstance ti : taskSchedule) {
			if (ti.getStart() >= time)  return ti;
		}
		return null;
	}
	
	/**
	 * Return the tasks of this Schedule.
	 * 
	 * @return A {@link Set} containing the tasks of this {@link Schedule}.
	 */
	public Set<Task> getTasks() {
		return tasks;
	}
	
	/**
	 * Returns the task that runs at the given time.
	 * Returns {@code null} when no Task will run at that moment.
	 * 
	 * @param time The system time at which caller wants to know which task runs.
	 * @return The Task that runs after the given time or {@code null} if there is no such Task.
	 */
	public Task getTaskAt(double time) {
		for (TaskInstance ti : taskSchedule) {
			if (ti.getStart() <= time && time < ti.getEnd())  return ti.getTask();
		}
		return null;
	}
	
	/**
	 * Returns the task instance that runs at the given time.
	 * Returns {@code null} when no Task will run at that moment.
	 * 
	 * @param time The system time at which caller wants to know which task runs.
	 * @return The Task that runs after the given time or {@code null} if there is no such Task.
	 */
	public TaskInstance getTaskInstanceAt(double time) {	
		for (TaskInstance ti : taskSchedule) {
			if (ti.getStart() <= time && time < ti.getEnd())  return ti;
		}
		return null;
	}
	
	/**
	 * The length of a cycle of this Schedule.
	 * 
	 * <p>This is the least common multiple of the periods of the Tasks.
	 * 
	 * @return Length of a cycle of this Schedule.
	 */
	public int getLcm(){
		return Utils.lcm(tasks);
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
