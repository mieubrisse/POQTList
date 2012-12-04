package edu.illinois.cs.projects.today1;

import edu.illinois.cs.projects.today1.task.Task;
import edu.illinois.cs.projects.today1.task.TaskAdapter;
import edu.illinois.cs.projects.today1.task.TaskAdapter.Type;


/**
 * Reference class for the constants used across Activities in the POQTList application
 * 
 * @author Kevin Today
 */
public class POQTListConstants {
	// ========== Overhead Constants ==========
	public static final String PREFERENCES_FILEPATH = "POQTListPreferences";	// name of file storing POQTList preferences
	public static final String LOG_TAG = "POQTList";							// name of tag used in log messages
	
	
	// ========== POQTListActivity Constants ==========
	// constants for TaskLists
	public static final String MAIN_TASKLIST_NAME = "All Tasks";
	public static final String COMPLETED_TASKLIST_NAME = "Completed Tasks";
	public static final long MAIN_TASKLIST_ID = 0;
	public static final long COMPLETED_TASKLIST_ID = 1;
	public static final long INITIAL_TASKLIST_ID_COUNTER = 2;	// should be one more than the highest non-custom task list ID
	public static final TaskAdapter.Type MAIN_TASKADAPTER_TYPE = Type.COLORED;
	public static final TaskAdapter.Type COMPLETED_TASKADAPTER_TYPE = Type.UNCOLORED;
	
	// constants for Tasks
	public static final long INITIAL_TASK_ID_COUNTER = 0;
	
	// constants for communication with Activities
	public static final String ACTIVITY_REQUEST_CODE = "activity_request_code";
	
	
	// ========== Alarm Information Keys ==========
	public static final String ALARM_INFO_KEY_TASK_ID = "alarm_info_task_ID";
	public static final String ALARM_INFO_KEY_TASK_DESCRIPTION = "alarm_info_task_description";
	public static final String ALARM_INFO_KEY_TASK_ALARM_ORDINAL = "alarm_info_task_alarm_ordinal";
	public static final String ALARM_INFO_KEY_TYPE_ORDINAL = "alarm_info_type";
	
	
	// ========== Task Information Keys ==========
	// keys for passing information between classes about task information
	public static final String TASK_INFO_KEY_DESCRIPTION = "task_info_description";
	public static final String TASK_INFO_KEY_HAS_DUE_DATE = "task_info_has_due_date";
	public static final String TASK_INFO_KEY_DUE_DAY = "task_info_due_date_day";
	public static final String TASK_INFO_KEY_DUE_MONTH = "task_info_due_date_month";
	public static final String TASK_INFO_KEY_DUE_YEAR = "task_info_due_date_year";
	public static final String TASK_INFO_KEY_DUE_HOUR = "task_info_due_date_hour";
	public static final String TASK_INFO_KEY_DUE_MINUTE = "task_info_due_date_minute";
	public static final String TASK_INFO_KEY_PRIORITY_ORDINAL = "task_info_priority_ordinal";
	public static final String TASK_INFO_KEY_ALARM_ORDINAL = "task_info_alarm_ordinal";
	
	public static final String TASK_INFO_KEY_EDIT_INDEX = "task_info_edit_index";		// index of task being edited within the currently-selected list
	
	
	// ========== TaskList Information Keys ==========
	// keys for passing information between classes about task list information
	public static final String TASKLIST_INFO_KEY_NAME = "tasklist_info_name";
	public static final String TASKLIST_INFO_KEY_ADAPTER_TYPE_ORDINAL = "tasklist_info_adapter_type_ordinal";
	
	
	// ========== SharedPreferences Keys ==========
	public static final String PREF_KEY_PREFERENCES_LOADED = "preferences_loaded";		// key referencing boolean as to whether defaults have been set yet
	
	// key for task list title color
	public static final String PREF_KEY_TASK_LIST_TITLE_COLR = "task_list_title_color";
	
	// keys for calculating Task urgency
	public static final String PREF_KEY_TIME_WINDOW = "time_window";
	public static final String PREF_KEY_URGENCY_SCALE = "urgency_scale";
	public static final String PREF_KEY_LOW_PRIORITY_WEIGHT = "low_priority_weight";
	public static final String PREF_KEY_NORMAL_PRIORITY_WEIGHT = "normal_priority_weight";
	public static final String PREF_KEY_HIGH_PRIORITY_WEIGHT = "high_priority_weight";
	
	public static final String PREF_KEY_MID_URGENCY_WEIGHT = "mid_urgency_weight";
	public static final String PREF_KEY_HIGH_URGENCY_WEIGHT = "high_urgency_weight";
	
	// keys for new Task options
	public static final String PREF_KEY_NEW_TASK_DESCRIPTION = "new_task_description";
	public static final String PREF_KEY_NEW_TASK_PRIORITY_ORDINAL = "new_task_priority_ordinal";
	public static final String PREF_KEY_NEW_TASK_ALARM_ORDINAL = "new_task_alarm_ordinal";
	
	// background color keys
	public static final String PREF_KEY_NO_URGNCY_BKGRND_COLR = "no_urgency_background_color";
	public static final String PREF_KEY_LOW_URGNCY_BKGRND_COLR = "low_urgency_background_color";
	public static final String PREF_KEY_MID_URGNCY_BKGRND_COLR = "mid_urgency_background_color";
	public static final String PREF_KEY_HI_URGNCY_BKGRND_COLR = "high_urgency_background_color";
	public static final String PREF_KEY_DUE_BKGRND_COLR = "due_background_color";
	public static final String PREF_KEY_OVERDUE_BKGRND_COLR	= "overdue_background_color";
	
	// text color keys
	public static final String PREF_KEY_NO_URGNCY_TEXT_COLR = "no_urgency_text_color";
	public static final String PREF_KEY_LOW_URGNCY_TEXT_COLR = "low_urgency_text_color";
	public static final String PREF_KEY_MID_URGNCY_TEXT_COLR = "mid_urgency_text_color";
	public static final String PREF_KEY_HI_URGNCY_TEXT_COLR = "high_urgency_text_color";
	public static final String PREF_KEY_DUE_TEXT_COLR = "due_text_color";
	public static final String PREF_KEY_OVERDUE_TEXT_COLR	= "overdue_text_color";
	
	// subtext color keys
	public static final String PREF_KEY_NO_URGNCY_SUBTEXT_COLR = "no_urgency_subtext_color";
	public static final String PREF_KEY_LOW_URGNCY_SUBTEXT_COLR = "low_urgency_subtext_color";
	public static final String PREF_KEY_MID_URGNCY_SUBTEXT_COLR = "mid_urgency_subtext_color";
	public static final String PREF_KEY_HI_URGNCY_SUBTEXT_COLR = "high_urgency_subtext_color";
	public static final String PREF_KEY_DUE_SUBTEXT_COLR = "due_subtext_color";
	public static final String PREF_KEY_OVERDUE_SUBTEXT_COLR	= "overdue_subtext_color";
	
	// keys for storing TaskApparatus information
	public static final String PREF_KEY_TASK_ID_COUNTER = "task_id_counter_key";
	public static final String PREF_KEY_TASKLIST_ID_COUNTER = "tasklist_id_counter_key";
	
	
	// ========== Default Preferences ==========
	// default task list title color
	public static final int DEFAULT_TASK_LIST_TITLE_COLOR = 0xFFFFFFFF;		// white
	
	// defaults for calculating Task urgency
	public static final int DEFAULT_TIME_WINDOW = 14;		// maximum days from due date Task must be to start gaining urgency
	public static final int DEFAULT_URGENCY_SCALE = 1000;	// scaling value used for Task urgency
	public static final float DEFAULT_LOW_PRIORITY_WEIGHT = 0;	// percent of urgency scale for low priority Tasks to sit at
	public static final float DEFAULT_NORMAL_PRIORITY_WEIGHT = 0.33F;	// percent of urgency scale for mid priority Tasks to sit at
	public static final float DEFAULT_HIGH_PRIORITY_WEIGHT = 0.9F;	// percent of urgency scale for low priority Tasks to sit at
	public static final float DEFAULT_MID_URGENCY_WEIGHT = 0.45F;	// percent of urgency scale where a Task becomes "mid" urgency
	public static final float DEFAULT_HIGH_URGENCY_WEIGHT = 0.8F;	// percent of urgency scale where a Task becomes "high" urgency
	
	// defaults for a new Task
	public static final String DEFAULT_NEW_TASK_DESCRIPTION = "";
	public static final Task.Priority DEFAULT_NEW_TASK_PRIORITY = Task.Priority.NORMAL_PRIORITY;
	public static final Task.Alarm DEFAULT_NEW_TASK_ALARM = Task.Alarm.NONE;
	
	// default Task background colors (all opaque)
	public static final int DEFAULT_NO_URGENCY_BKGRND_COLOR = 0xFF303030;	// dark gray
	public static final int DEFAULT_LOW_URGENCY_BKGRND_COLOR = 0xFF006000;	// dark green
	public static final int DEFAULT_MID_URGENCY_BKGRND_COLOR = 0xFF606000;	// dark yellow
	public static final int DEFAULT_HIGH_URGENCY_BKGRND_COLOR = 0xFF604000;	// dark orange
	public static final int DEFAULT_DUE_BKGRND_COLOR = 0xFF730000;			// dark red
	public static final int DEFAULT_OVERDUE_BKGRND_COLOR = 0xFF730073;		// purple
	
	// default Task text colors (all opaque)
	public static final int DEFAULT_NO_URGENCY_TEXT_COLOR = 0xFFFFFFFF;		// white
	public static final int DEFAULT_LOW_URGENCY_TEXT_COLOR = 0xFFFFFFFF;	// white
	public static final int DEFAULT_MID_URGENCY_TEXT_COLOR = 0xFFFFFFFF;	// white
	public static final int DEFAULT_HIGH_URGENCY_TEXT_COLOR = 0xFFFFFFFF;	// white
	public static final int DEFAULT_DUE_TEXT_COLOR = 0xFFFFFFFF;		// white
	public static final int DEFAULT_OVERDUE_TEXT_COLOR = 0xFFFFFFFF;		// white
	
	// default Task subtext colors (all opaque)
	public static final int DEFAULT_NO_URGENCY_SUBTEXT_COLOR = 0xFFFFFFFF;		// white
	public static final int DEFAULT_LOW_URGENCY_SUBTEXT_COLOR = 0xFFFFFFFF;		// white
	public static final int DEFAULT_MID_URGENCY_SUBTEXT_COLOR = 0xFFFFFFFF;		// white
	public static final int DEFAULT_HIGH_URGENCY_SUBTEXT_COLOR = 0xFFFFFFFF;	// white
	public static final int DEFAULT_DUE_SUBTEXT_COLOR = 0xFFFFFFFF;		// white
	public static final int DEFAULT_OVERDUE_SUBTEXT_COLOR = 0xFFFFFFFF;			// white

	
	// ========== Fling Gesture Constants ==========
	public static final int MIN_FLING_DISTANCE = 120;	// minimum pixels the user must trace for before a fling gesture is triggered
	public static final int MAX_FLING_ERROR_TOLERANCE = 250;	// maximum pixels the user's finger can trail off a fling gesture's path before being considered invalid
	public static final int MIN_FLING_VELOCITY = 200;	// minimum pixels-per-second required to trigger a fling gesture
	
	
	// ========== Database Constants ==========
	public static final String DB_NAME = "TaskDatabase";
	public static final String DB_DATE_STORAGE_FORMAT = "dd MM yyyy hh:mm aa";		// changing this format makes the database unreadable
	
	/*	Task Table	*/
	public static final String DB_TASK_TABLE_NAME = "Task";
	public static final String DB_TASK_KEY_ID = "taskID";
	public static final String DB_TASK_COLUMN_DESCRIPTION = "taskDescription";
	public static final String DB_TASK_COLUMN_DUEDATE = "taskDueDate";
	public static final String DB_TASK_COLUMN_PRIORITY = "taskPriorityOrdinal";
	public static final String DB_TASK_COLUMN_ALARM_MILLIS = "alarmTimeMillis";
	public static final String DB_TASK_COLUMN_ALARM_ORDINAL = "alarmOrdinal";
	
	/*	TaskList Table	*/
	public static final String DB_TASKLIST_TABLE_NAME = "TaskList";
	public static final String DB_TASKLIST_KEY_ID = "listID";
	public static final String DB_TASKLIST_COLUMN_NAME = "listName";
	public static final String DB_TASKLIST_COLUMN_ADAPTERTYPE_ORDINAL = "adapterTypeOrdinal";		// TaskAdapter.Type.ordinal()
	
	/*	HasTask Table	*/
	public static final String DB_HASTASK_TABLE_NAME = "HasTask";
	public static final String DB_HASTASK_KEY_LISTID = "parentListID";
	public static final String DB_HASTASK_KEY_TASKID = "containedTaskID";
	
	
	
	
	// ========== Constructor ==========
	/**
	 * Private constructor; POQTListConstants is for reference only and should not be instantiated
	 */
	private POQTListConstants() {}
}
