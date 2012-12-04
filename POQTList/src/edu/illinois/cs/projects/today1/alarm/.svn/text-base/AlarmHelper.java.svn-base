package edu.illinois.cs.projects.today1.alarm;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import edu.illinois.cs.projects.today1.POQTListConstants;
import edu.illinois.cs.projects.today1.task.Task;
import edu.illinois.cs.projects.today1.task.Task.Priority;

/**
 * Class to facilitate easy task alarm manipulation
 * 
 * @author Kevin Today
 *
 */
public class AlarmHelper {
	// ========== Constants ==========
	public static enum AlarmType {
		CUSTOM,		// type for user-defined alarms
		DUE;		// type for alarms caused by task being due
		
		/**
		 * Returns an AlarmType object associated with the given AlarmType ordinal
		 * 
		 * @param ordinal Ordinal of AlarmType object
		 * @return AlarmType object designated by the given ordinal or null if ordinal is invalid
		 */
		public static AlarmType findAlarmType(int ordinal) {
			switch (ordinal) {
			case 0:
				return CUSTOM;
			case 1:
				return DUE;
			default:
				return null;
			}
		}
	}
	
		
	// ========== Member Variables ==========
	private Context m_context;		// context AlarmHelper is working within

	
	// ========== Constructors ==========
	/**
	 * Instantiates a new AlarmApparatus object within the given Context
	 * 
	 * @param context Context for new AlarmApparatus instance
	 */
	public AlarmHelper(Context context) {
		// sanity check
		if (context == null) {
			throw new IllegalArgumentException("AlarmHelper context cannot be null");
		}
		
		m_context = context;
	}
	
	
	// ========== Public Functions ==========
	/**
	 * Adds the alarms associated with the given Task to the AlarmManager
	 * 
	 * @param task Task to set alarms for
	 * @return True if the addition was successful, false otherwise
	 */
	public boolean addTask(Task task) {
		Log.d(POQTListConstants.LOG_TAG, "Adding alarms for '" + task.getDescription() + "'");
		
		boolean dueAlarmAdded = addDueAlarm(task);
		Log.d(POQTListConstants.LOG_TAG, "dueAlarmAdded: " + dueAlarmAdded);
		boolean customAlarmAdded = addCustomAlarm(task);
		Log.d(POQTListConstants.LOG_TAG, "customAlarmAdded: " + customAlarmAdded);

		return dueAlarmAdded && customAlarmAdded;
	}
	
	/**
	 * Updates the alarms for the given Task to represent its current information
	 * 
	 * @param task Task information to use in update
	 * @return True if the update was successful, false otherwise
	 */
	public boolean updateTask(Task task) {
		Log.d(POQTListConstants.LOG_TAG, "Updating alarms for '" + task.getDescription() + "'");
		
		boolean oldCustomAlarmDeleted = deleteCustomAlarm(task);	// deletes old custom alarm
		Log.d(POQTListConstants.LOG_TAG, "oldAlarmDeleted: " + oldCustomAlarmDeleted);
		boolean newCustomAlarmAdded = addCustomAlarm(task);		// adds new one based on current task information
		Log.d(POQTListConstants.LOG_TAG, "newAlarmAdded: " + newCustomAlarmAdded);
		
		boolean oldDueAlarmDeleted = deleteDueAlarm(task);
		boolean newDueAlarmAdded = addDueAlarm(task);
		
		return oldCustomAlarmDeleted && newCustomAlarmAdded && oldDueAlarmDeleted && newDueAlarmAdded;
		
	}
	
	/**
	 * Removes all alarms associated with the given Task from the AlarmManager
	 * 
	 * @param task Task information to use in deletion
	 * @return True if the deletion was successful, false otherwise
	 */
	public boolean deleteTask(Task task) {
		Log.d(POQTListConstants.LOG_TAG, "Deleting alarms for '" + task.getDescription() + "'");
		
		boolean dueAlarmDeleted = deleteDueAlarm(task);
		Log.d(POQTListConstants.LOG_TAG, "dueAlarmDeleted: " + dueAlarmDeleted);
		boolean customAlarmDeleted = deleteCustomAlarm(task);
		Log.d(POQTListConstants.LOG_TAG, "customAlarmDeleted: " + customAlarmDeleted);

		return dueAlarmDeleted && customAlarmDeleted;
	}

	
	// ========== Alarm Toolkit (for encapsulating AlarmManager ineraction) ==========
	
	/**
	 * Adds standard alarm triggered upon task due time for the given Task
	 * 
	 * @param task Task for which alarm will be added
	 * @return True if the due alarm was successfully added, false otherwise
	 */
	private boolean addDueAlarm(Task task) {
		try {
			// sanity check
			if (task.getDueDate() == null) {
				return false;
			}
			
			// get task information
			long dueDateMillis = task.getDueDate().getTimeInMillis();
			
			// prepare alarm information
			Bundle alarmInformation = new Bundle();
			alarmInformation.putLong(POQTListConstants.ALARM_INFO_KEY_TASK_ID, task.getID());
			alarmInformation.putString(POQTListConstants.ALARM_INFO_KEY_TASK_DESCRIPTION, task.getDescription());
			alarmInformation.putInt(POQTListConstants.ALARM_INFO_KEY_TYPE_ORDINAL, AlarmType.DUE.ordinal());
			
			// package up alarm information and set alarm
			Intent alarmIntent = new Intent(m_context, DueAlarmReceiver.class);
			alarmIntent.putExtras(alarmInformation);
			PendingIntent pendingAlarmIntent = PendingIntent.getBroadcast(m_context, (int)task.getID(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			
			AlarmManager alarmManager = (AlarmManager)m_context.getSystemService(Context.ALARM_SERVICE);
			alarmManager.set(AlarmManager.RTC_WAKEUP, dueDateMillis, pendingAlarmIntent);
			return true;
		}
		catch (Exception caught) {
			return false;
		}
	}
	
	/**
	 * Deletes alarm triggered upon Task due time
	 * 
	 * @param task Task information identifying which due alarm to delete
	 * @return True if the due alarm was successfully deleted, false otherwise
	 */
	private boolean deleteDueAlarm(Task task) {
		try {
			// sanity check
			if (task.getDueDate() == null) {
				return false;
			}
			
			// don't add alarm when due date is in past
			Calendar currentTime = Calendar.getInstance();
			long dueDateMillis = task.getDueDate().getTimeInMillis();
			if (dueDateMillis < currentTime.getTimeInMillis()) {
				return true;
			}
			
			// re-create PendingIntent object for use in removal
			Intent alarmIntent = new Intent(m_context, DueAlarmReceiver.class);
			PendingIntent intentToCancel = PendingIntent.getBroadcast(m_context, (int)task.getID(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			
			AlarmManager alarmManager = (AlarmManager)m_context.getSystemService(Context.ALARM_SERVICE);
			alarmManager.cancel(intentToCancel);
			return true;
		}
		catch (Exception caught) {
			return false;
		}
	}
	
	/**
	 * Sets an alarm to go off for the given Task, or does nothing if the task has no alarm
	 * 
	 * @param task Task information to use for alarm information
	 * @return True if the custom alarm was successfully added, false otherwise
	 */
	private boolean addCustomAlarm(Task task) {
		try {
			// sanity check
			if (task.getDueDate() == null || task.getAlarm() == Task.Alarm.NONE) {
				return false;
			}
			
			// get time alarm should go off in millis
			long dueDateMillis = task.getDueDate().getTimeInMillis();
			long alarmMillisOffset = task.getAlarm().getMillisOffset();
			long alarmTimeMillis = dueDateMillis - alarmMillisOffset;
			
			// don't add alarm when reminder is in past
			Calendar currentTime = Calendar.getInstance();
			if (alarmTimeMillis < currentTime.getTimeInMillis()) {
				return true;
			}
			
			// prepare alarm information
			Bundle alarmInformation = new Bundle();
			alarmInformation.putLong(POQTListConstants.ALARM_INFO_KEY_TASK_ID, task.getID());
			alarmInformation.putString(POQTListConstants.ALARM_INFO_KEY_TASK_DESCRIPTION, task.getDescription());
			alarmInformation.putInt(POQTListConstants.ALARM_INFO_KEY_TASK_ALARM_ORDINAL, task.getAlarm().ordinal());
			alarmInformation.putInt(POQTListConstants.ALARM_INFO_KEY_TYPE_ORDINAL, AlarmType.CUSTOM.ordinal());
			
			// package up alarm information and set alarm
			Intent alarmIntent = new Intent(m_context, CustomAlarmReceiver.class);
			alarmIntent.putExtras(alarmInformation);
			PendingIntent pendingAlarmIntent = PendingIntent.getBroadcast(m_context, (int)task.getID(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			
			AlarmManager alarmManager = (AlarmManager)m_context.getSystemService(Context.ALARM_SERVICE);
			alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingAlarmIntent);
			return true;
		}
		catch (Exception caught) {
			return false;
		}
	}
	
	/**
	 * Deletes the custom alarm set for the given Task, or does nothing if given Task has no alarm
	 * 
	 * @param task Task information identifying which alarm to delete
	 * @return True if the custom alarm was successfully deleted, false otherwise
	 */
	private boolean deleteCustomAlarm(Task task) {
		try {
			// sanity check
			if (task.getDueDate() == null || task.getAlarm() == Task.Alarm.NONE) {
				return false;
			}
			
			// re-create PendingIntent object for use in removal
			Intent alarmIntent = new Intent(m_context, CustomAlarmReceiver.class);
			PendingIntent intentToCancel = PendingIntent.getBroadcast(m_context, (int)task.getID(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			
			AlarmManager alarmManager = (AlarmManager)m_context.getSystemService(Context.ALARM_SERVICE);
			alarmManager.cancel(intentToCancel);
			return true;
		}
		catch (Exception caught) {
			return false;
		}
	}
}
