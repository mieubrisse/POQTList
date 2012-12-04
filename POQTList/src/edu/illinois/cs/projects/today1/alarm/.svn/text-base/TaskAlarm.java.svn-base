package edu.illinois.cs.projects.today1.alarm;

import java.util.Calendar;

import edu.illinois.cs.projects.today1.task.Task;

/**
 * Class modeling a reminder for a task
 * 
 * @author Kevin Today
 *
 */
public class TaskAlarm {
	// ========== Member Variables ==========
	private Calendar m_time;		// time alarm should go off
	private Task m_task;			// task alarm is for
	
	
	// ========== Constructors ==========
	/**
	 * Instantiates a new TaskAlarm object with the given parameters
	 * 
	 * @param time Time alarm should go off at
	 * @param task Task alarm is for
	 */
	public TaskAlarm(Calendar time, Task task) {
		// sanity check
		if (time == null || task == null) {
			throw new IllegalArgumentException("TaskAlarm constructor parameters cannot be null");
		}
		
		m_time = time;
		m_task = task;
	}
	
	
	// ========== Getters ==========
	public Calendar getTime() {
		return m_time;
	}
	
	public Task getTask() {
		return m_task;
	}
}
