package edu.illinois.cs.projects.today1.task;

import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;
import edu.illinois.cs.projects.today1.POQTListConstants;

public class TaskListFactory {
	// ========== Member Variables ==========
	private Context m_context;				// context to create the TaskList within
	
	
	// ========== Constructors ==========
	/**
	 * Instantiates a new TaskListFactory object with the given Context
	 * 
	 * @param context Context to create the TaskListFactory object within (cannot be null)
	 */
	public TaskListFactory(Context context) {
		if (context == null) {
			throw new IllegalArgumentException("TaskListFactory context cannot be null");
		}
		
		m_context = context;
	}
	
	
	// ========== Public Methods ==========
	/**
	 * Manufactures a new TaskList with the given parameters.
	 * 
	 * @param name Name new TaskList should have
	 * @param adapterType Type new TaskList's adapter should have
	 * @return The new TaskList, or null if an error occurred
	 */
	public TaskList buildTaskList(String name, TaskAdapter.Type adapterType) {
		try {
			long freeID = getUnusedID();
			TaskAdapter newAdapter = new TaskAdapter(m_context, adapterType);
			TaskList newTaskList = new TaskList(freeID, name, newAdapter);
			return newTaskList;
		}
		catch (IllegalArgumentException exc) {
			return null;
		}
	}
	
	
	// ========== Private Methods ==========
	/**
	 * Gets a task list ID not currently in use
	 * 
	 * @return Free task list ID
	 */
	private synchronized long getUnusedID() {
		// get free ID
		SharedPreferences preferences = m_context.getSharedPreferences(POQTListConstants.PREFERENCES_FILEPATH, Context.MODE_PRIVATE);
		long freeID = preferences.getLong(POQTListConstants.PREF_KEY_TASKLIST_ID_COUNTER, 0);

		// increment ID counter
		SharedPreferences.Editor editor = preferences.edit();
		editor.putLong(POQTListConstants.PREF_KEY_TASKLIST_ID_COUNTER, freeID + 1);
		editor.commit();
		
		return freeID;
	}
}
