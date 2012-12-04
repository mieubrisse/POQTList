package edu.illinois.cs.projects.today1.task;

import java.util.Calendar;
import java.util.Calendar;

import edu.illinois.cs.projects.today1.POQTListConstants;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Class modeling a task in the task list
 * 
 * @author Kevin Today
 */
public class Task {
	// ========== Constants ==========
	// must preserve same index ordering as @string/priorities!
	public static enum Priority {
		LOW_PRIORITY (POQTListConstants.DEFAULT_LOW_PRIORITY_WEIGHT),
		NORMAL_PRIORITY (POQTListConstants.DEFAULT_NORMAL_PRIORITY_WEIGHT),
		HIGH_PRIORITY (POQTListConstants.DEFAULT_HIGH_PRIORITY_WEIGHT);
		
		private final double m_weight;
		
		private Priority(double weight) { m_weight = weight; }
		
		public double getWeight() { return m_weight; }
		
		/**
		 * Returns a Priority object associated with the given priority ordinal
		 * 
		 * @param ordinal Ordinal of priority object
		 * @return Priority object designated by the given ordinal or null if ordinal is invalid
		 */
		public static Priority findPriority(int ordinal) {
			switch (ordinal) {
			case 0:
				return LOW_PRIORITY;
			case 1:
				return NORMAL_PRIORITY;
			case 2:
				return HIGH_PRIORITY;
			default:
				return null;
			}
		}
	};
	
	// must preserve same index ordering as @string/alarms!
	public static enum Alarm {
		NONE (0, ""),
		TEN_MINUTES_ALARM (1000 * 60 * 10, "10 minutes"),
		THIRTY_MINUTES_ALARM (1000 * 60 * 30, "30 minutes"),
		ONE_HOUR_ALARM (1000 * 60 * 60, "1 hour"),
		TWO_HOURS_ALARM (1000 * 60 * 60 * 2, "2 hours"),
		THREE_HOURS_ALARM (1000 * 60 * 60 * 3, "3 hours"),
		TWELVE_HOURS_ALARM (1000 * 60 * 60 * 12, "12 hours"),
		ONE_DAY_ALARM (1000 * 60 * 60 * 24, "1 day"),
		TWO_DAYS_ALARM (1000 * 60 * 60 * 24 * 2, "2 days"),
		THREE_DAYS_ALARM (1000 * 60 * 60 * 24 * 3, "3 days"),
		ONE_WEEK_ALARM (1000 * 60 * 60 * 24 * 7, "1 week"),
		TWO_WEEKS_ALARM (1000 * 60 * 60 * 24 * 14, "2 weeks");
		
		private final long m_millisOffset;
		private final String m_prettyName;
		
		private Alarm(long millisPrior, String prettyName) {
			if (prettyName == null) {
				throw new IllegalArgumentException("Alarm name cannot be null");
			}
			
			m_millisOffset = millisPrior;
			m_prettyName = prettyName;	
		}
		
		public long getMillisOffset() {	return m_millisOffset;	}
		public String getPrettyName() { return m_prettyName;	}
		
		/**
		 * Returns a Alarm object associated with the given alarm ordinal
		 * 
		 * @param ordinal Ordinal of alarm object
		 * @return Alarm object designated by the given ordinal or null if ordinal is invalid
		 */
		public static Alarm findAlarm(int ordinal) {
			switch (ordinal) {
			case 0:
				return NONE;
			case 1:
				return TEN_MINUTES_ALARM;
			case 2:
				return THIRTY_MINUTES_ALARM;
			case 3:
				return ONE_HOUR_ALARM;
			case 4:
				return TWO_HOURS_ALARM;
			case 5:
				return THREE_HOURS_ALARM;
			case 6:
				return TWELVE_HOURS_ALARM;
			case 7:
				return ONE_DAY_ALARM;
			case 8:
				return TWO_DAYS_ALARM;
			case 9:
				return THREE_DAYS_ALARM;
			case 10:
				return ONE_WEEK_ALARM;
			case 11:
				return TWO_WEEKS_ALARM;
			default:
				return null;
			}
		}
	};
	
	
	// ========== Member Variables ==========
	private long m_id;				// unique int identifying a task
	private String m_description;	// text description 
	private Calendar m_dueDate;		// date and time the task is due
	private Priority m_priority;	// constant denoting the task's priority
	private Alarm m_alarm;		// date and time to remind about task
	
	// ========== Constructors ==========
	/**
	 * Instantiates a new Task object with the given parameters, no due date, normal priority, and no reminder
	 * 
	 * @param id The task's ID
	 * @param text The task's text description. Cannot be "null" or the empty string.
	 */
	public Task(long id, String text) {
		// sanity check for text
		if (text == null || text.length() == 0) {
			throw new IllegalArgumentException("Text must be non-null, non-empty string");
		}
		
		m_id = id;
		m_description = text;
		m_dueDate = null;
		m_priority = Priority.NORMAL_PRIORITY;
		m_alarm = null;
	}
	
	/**
	 * Instantiates a new Task object with the given parameters, normal priority, and no alarm
	 * 
	 * @param id The task's ID
	 * @param text The task's text description. Cannot be "null" or the empty string.
	 * @param dueDate The task's due date (no due date if "null")
	 */
	public Task(long id, String text, Calendar dueDate) {
		this(id, text);
		m_dueDate = dueDate;
	}
	
	/**
	 * Instantiates a new Task object with the given parameters and no alarm
	 * 
	 * @param id The task's ID
	 * @param text The task's text description. Cannot be "null" or the empty string.
	 * @param dueDate The task's due date (no due date if "null")
	 * @param priority The task's priority (normal priority if "null")
	 */
	public Task(long id, String text, Calendar dueDate, Priority priority) {
		this(id, text, dueDate);
		
		if (priority != null) {
			m_priority = priority;
		}
	}
	
	/**
	 * Instantiates a new Task object with the given parameters
	 * 
	 * @param id The task's ID
	 * @param text The task's text description. Cannot be "null" or the empty string.
	 * @param dueDate The task's due date (no due date if "null")
	 * @param priority The task's priority (normal priority if "null")
	 * @param alarm Date and time to remind about task (no alarm if "null")
	 */
	public Task(long id, String text, Calendar dueDate, Priority priority, Alarm alarm) {
		this(id, text, dueDate, priority);
		
		if (alarm != null) {
			m_alarm = alarm;
		}
	}

	
	
	// ========== Methods ==========
	/**
	 * Calculates an Urgency Score for the task based on the given context's preferences
	 * Urgency Scores are based on:
	 *  - Priority
	 *  - Time until due date
	 *  - GPS location (UNIMPLEMENTED YET)
	 *  
	 *  @param context Context of preferences used in Urgency Score calculations
	 */
	public int findUrgencyScore(Context context) {
		// get application preferences for calculating urgency
		SharedPreferences preferences = context.getSharedPreferences(POQTListConstants.PREFERENCES_FILEPATH, context.MODE_PRIVATE);
		int timeWindow = preferences.getInt(POQTListConstants.PREF_KEY_TIME_WINDOW, POQTListConstants.DEFAULT_TIME_WINDOW);
		int urgencyScale = preferences.getInt(POQTListConstants.PREF_KEY_URGENCY_SCALE, POQTListConstants.DEFAULT_URGENCY_SCALE);
		
		// if due date is null, calculate Urgency Score based on priority alone
		if (m_dueDate == null) {
			return (int)(m_priority.getWeight() * ((double)urgencyScale));
		}
		
		long NUM_MIN_IN_DAY = 1440;
		long MIN_IN_TIME_WINDOW = timeWindow * NUM_MIN_IN_DAY;
		
		Calendar cal = Calendar.getInstance();
		
		// !!ALGORITHM DOES NOT COMBINE BOTH PRIORITY DUE DATE!!!!
		// otherwise, use time until due date
		long NUM_MILLIS_IN_MINUTE = 60000;
		long minutesElapsedWhenDue = (long)(m_dueDate.getTimeInMillis() / NUM_MILLIS_IN_MINUTE);
		long minutesElapsedCurrently = (long)(cal.getTimeInMillis() / NUM_MILLIS_IN_MINUTE);
		long minutesDifference = minutesElapsedWhenDue - minutesElapsedCurrently;
		
		// ratio of how long until the task is due to how long the time window is
		double dueDateRatio = ((double) minutesDifference) / ((double) MIN_IN_TIME_WINDOW);
		int urgencyScore = (int)(((double)urgencyScale) * (1.00 - dueDateRatio));
		return urgencyScore;
	}
	
	@Override
	/**
	 * Two Task objects are equal iff their IDs are equal or they're the same object
	 * 
	 * @param obj The Object to compare the calling Task to
	 * @return 'true' if the the calling Task has the same ID or is the same object as 'obj', 'false' otherwise
	 */
	public boolean equals(Object obj) {
		// sanity check
		if (obj == null ||obj.getClass() != Task.class) {
			return false;
		}
		
		Task rhs = (Task) obj;
		// compare IDs
		if (this == obj || m_id == rhs.getID()) {
			return true;
		}
		
		return false;
	}
	
	@Override
	/**
	 * Generates a hash code for the calling Task object based on its ID
	 * 
	 * @return The generated hash code
	 */
	public int hashCode() {
		// Probably a terrible HashCode, but not worrying about optimization at the moment
		return (int)m_id;
	}
	
	
	// ========== Getters ==========
	/**
	 * Gets the Task's description
	 * 
	 * @return Task's description
	 */
	public String getDescription() {
		return m_description;
	}
	
	/**
	 * Gets the Task's due date
	 * 
	 * @return Task's due date or null if no due date
	 */
	public Calendar getDueDate() {
		return m_dueDate;
	}
	
	/**
	 * Gets the Task's priority
	 * 
	 * @return Task's priority
	 */
	public Priority getPriority() {
		return m_priority;
	}
	
	/**
	 * Gets the Task's ID
	 * 
	 * @return Task's ID
	 */
	public long getID() {
		return m_id;
	}
	
	/**
	 * Gets the Task's alarm date
	 * 
	 * @return Task's alarm date or null if no alarm set
	 */
	public Alarm getAlarm() {
		return m_alarm;
	}

	
	// ========== Setters ==========
	/**
	 * Sets the task's description
	 * 
	 * @param description The task's new description. Cannot be null, empty, or whitespace
	 */
	public void setDescription(String description) {
		if (description == null && description.trim().length() == 0) {
			throw new IllegalArgumentException("Cannot set task's description to empty");
		}
		
		m_description = description;
	}
	
	/**
	 * Sets the Task's due date
	 * 
	 * @param dueDate The task's new due date. 'null' indicates no due date
	 */
	public void setDueDate(Calendar dueDate) {
		m_dueDate = dueDate;
	}
	
	/**
	 * Sets the task's priority
	 * 
	 * @param priority The Task's new priority. Cannot be 'null'
	 */
	public void setPriority(Priority priority) {
		if (priority == null) {
			throw new IllegalArgumentException("Cannot set task priority to null");
		}
		
		m_priority = priority;
	}
	
	/**
	 * Sets the Task's alarm date
	 * 
	 * @param alarm The Task's new alarm date ("null" if no alarm)
	 */
	public void setAlarm(Alarm alarm) {
		m_alarm = alarm;
	}

}
