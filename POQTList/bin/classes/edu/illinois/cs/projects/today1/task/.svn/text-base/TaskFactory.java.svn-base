package edu.illinois.cs.projects.today1.task;

import java.util.Calendar;

import edu.illinois.cs.projects.today1.POQTListConstants;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Class to craft Task objects so the calling application doesn't have to keep track of creation details
 * (no need to track Task ID, for example)
 * 
 * @author Kevin Today
 *
 */
public class TaskFactory {
	// ========== Member Variables ==========
	private Context m_context;					// context to create the Task within
	
	
	// ========== Constructors ==========
	/**
	 * Instantiates a new TaskFactory object with the given Context
	 * 
	 * @param context Context to create the TaskFactory object within (cannot be null)
	 */
	public TaskFactory(Context context) {
		if (context == null) {
			throw new IllegalArgumentException("Context cannot be null");
		}
		
		m_context = context;
	}
	
	
	// ========== Public Methods ==========
	/**
	 * Manufactures a new Task with the given parameters.
	 * 
	 * @param description The Task's description
	 * @param dueDate The date the Task is due (null for no due date)
	 * @param priority The Task's priority (null defaults to normal priority)
	 * @param alarm The Task's alarm offset (null defaults to no alarm)
	 * @return The new Task, or null if an error occurred
	 */
	public Task buildTask(String description, Calendar dueDate, Task.Priority priority, Task.Alarm alarm) {
		try {
			long freeID = getUnusedID();
			
			Log.d(POQTListConstants.LOG_TAG, "Got free ID: " + freeID);
			
			Task newTask = new Task(freeID, description, dueDate, priority, alarm);
			return newTask;
		}
		catch (IllegalArgumentException exc) {
			return null;
		}
	}
	
	
	// ========== Private Methods ==========
	/**
	 * Gets a task ID not currently in use
	 * 
	 * @return Free task ID
	 */
	private synchronized long getUnusedID() {
		// get free ID
		SharedPreferences preferences = m_context.getSharedPreferences(POQTListConstants.PREFERENCES_FILEPATH, Context.MODE_PRIVATE);
		long freeID = preferences.getLong(POQTListConstants.PREF_KEY_TASK_ID_COUNTER, 0);

		// increment ID counter
		SharedPreferences.Editor editor = preferences.edit();
		editor.putLong(POQTListConstants.PREF_KEY_TASK_ID_COUNTER, freeID + 1);
		editor.commit();
		
		return freeID;
	}
}
