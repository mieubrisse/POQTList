package edu.illinois.cs.projects.today1.database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateFormat;
import android.util.Log;
import edu.illinois.cs.projects.today1.POQTListConstants;
import edu.illinois.cs.projects.today1.task.Task;
import edu.illinois.cs.projects.today1.task.Task.Alarm;
import edu.illinois.cs.projects.today1.task.Task.Priority;
import edu.illinois.cs.projects.today1.task.TaskAdapter;
import edu.illinois.cs.projects.today1.task.TaskList;

/**
 * Contains all functionality for the database containing tasks
 * 
 * @author Kevin Today
 * 
 * Code helped by tutorial at www.vogella.de
 *
 */
public class TaskDBApparatus {
	// ========== Member Variables ==========
	private Context m_context;				// handle to context database is within
	private SQLiteDatabase m_database;		// handle to database
	private TaskDBHelper m_helper;			// helper to manage database operation
	
	
	// ========== Constructors ==========
	/**
	 * Creates a new TaskDBApparatus in the given context and opens the database it should be managing
	 * WARNING: The user is responsible for calling close() on the apparatus when they are finished with it!
	 * 
	 * @param context Context to create TaskDBAdapter in
	 */
	public TaskDBApparatus(Context context) {
		if (context == null) {
			throw new IllegalArgumentException("TaskDBAdapter context cannot be null");
		}
		
		m_context = context;
		open();
	}
	
	
	// ========== Public Functions ==========
	/**
	 * Opens the apparatus
	 */
	public void open() {
		m_helper = new TaskDBHelper(m_context);
		m_database = m_helper.getWritableDatabase();
	}
	
	/**
	 * Closes the apparatus
	 */
	public void close() {
		if (m_helper != null) {
			m_helper.close();
		}
	}
	
	/**
	 * Adds the given Task to the database
	 * 
	 * @param task Task to add
	 * @return False if the Task is already in the database; true otherwise
	 */
	public boolean addTask(Task task) {
		// sanity check
		if (task == null) {
			return false;
		}
		
		// if the Task already exists, fail
		if (doesTaskExist(task.getID())) {
			Log.d(POQTListConstants.LOG_TAG, "Cannot add Task with ID: " + task.getID() + " to database; ID already exists");
			return false;
		}
		
		// encode DueDate into String using date storage format string constant
		Calendar dueDate = task.getDueDate();
		String encodedDueDate;
		if (dueDate == null) {
			encodedDueDate = null;
		}
		else {
			final String FORMAT = POQTListConstants.DB_DATE_STORAGE_FORMAT;
			encodedDueDate = DateFormat.format(FORMAT, task.getDueDate()).toString();
		}
		
		// encode alarm time into date in milliseconds
		long alarmDateMillis = 0;
		if (dueDate != null) {
			long dueDateMillis = dueDate.getTimeInMillis();
			long alarmOffsetMillis = task.getAlarm().getMillisOffset();
			alarmDateMillis = dueDateMillis - alarmOffsetMillis;
		}

		// insert new task into database
		// INSERT INTO Task (ID, description, dueDate, priority) VALUES(task id, description, due date, priority)
		ContentValues taskValues = new ContentValues();
		taskValues.put(POQTListConstants.DB_TASK_KEY_ID, task.getID());
		taskValues.put(POQTListConstants.DB_TASK_COLUMN_DESCRIPTION, task.getDescription());
		taskValues.put(POQTListConstants.DB_TASK_COLUMN_DUEDATE, encodedDueDate);
		taskValues.put(POQTListConstants.DB_TASK_COLUMN_PRIORITY, task.getPriority().ordinal());
		taskValues.put(POQTListConstants.DB_TASK_COLUMN_ALARM_MILLIS, alarmDateMillis);
		taskValues.put(POQTListConstants.DB_TASK_COLUMN_ALARM_ORDINAL, task.getAlarm().ordinal());
		m_database.insert(POQTListConstants.DB_TASK_TABLE_NAME, null, taskValues);
		
		return true;
	}
	
	/**
	 * Adds the given TaskList to the database.
	 * 
	 * @param list TaskList to add to the database
	 * @return False if the TaskList is already in the database; true otherwise
	 */
	public boolean addTaskList(TaskList list) {
		// sanity check
		if (list == null) {
			return false;
		}
		
		// check if already in database
		if (doesListExist(list.getID())) {
			Log.d(POQTListConstants.LOG_TAG, "List with ID: " + list.getID() + " and name: " + list.getName() + " already exists");
			return false;
		}
		
		// insert the given TaskList into the database
		// INSERT INTO TaskList(ID, name, type ordinal) VALUES(ID, new name, new type ordinal)
		int adapterTypeOrdinal = list.getAdapter().getType().ordinal();
		ContentValues values = new ContentValues();
		values.put(POQTListConstants.DB_TASKLIST_KEY_ID, list.getID());
		values.put(POQTListConstants.DB_TASKLIST_COLUMN_NAME, list.getName());
		values.put(POQTListConstants.DB_TASKLIST_COLUMN_ADAPTERTYPE_ORDINAL, adapterTypeOrdinal);
		m_database.insert(POQTListConstants.DB_TASKLIST_TABLE_NAME, null, values);
		
		Log.d(POQTListConstants.LOG_TAG, "List with ID: " + list.getID() + " and name: " + list.getName() + " successfully inserted");
		
		return true;
	}
	
	/**
	 * Adds the given Task to given TaskList
	 * 
	 * @param task Task to add
	 * @param list TaskList to add to
	 * @return True if the Task was successfully added to the TaskList, false otherwise
	 */
	public boolean addTaskToList(Task task, TaskList list) {
		// sanity check
		if (task == null || list == null) {
			return false;
		}
		
		// if the relation already exists, no need to do insertion
		if (doesListContain(list.getID(), task.getID())) {
			return true;
		}
		
		// if the TaskList doesn't exist, fail
		if (doesListExist(list.getID()) == false) {
			return false;
		}
		
		// if the Task doesn't exist, fail
		if (doesTaskExist(task.getID()) == false) {
			return false;
		}
		
		// INSERT INTO HasTask(listID, taskID) VALUES (list ID, task ID)
		ContentValues customHasTaskValues = new ContentValues();
		customHasTaskValues.put(POQTListConstants.DB_HASTASK_KEY_LISTID, list.getID());
		customHasTaskValues.put(POQTListConstants.DB_HASTASK_KEY_TASKID, task.getID());
		m_database.insert(POQTListConstants.DB_HASTASK_TABLE_NAME, null, customHasTaskValues);
			
		return true;
	}
	
	/**
	 * Deletes the Task from the database if it exists; do nothing otherwise
	 * 
	 * @param task Task to delete
	 */
	public boolean deleteTask(Task task) {
		// sanity check
		if (task == null) {
			return false;
		}
		
		// if Task doesn't exist, do nothing
		if (!doesTaskExist(task.getID())) {
			return false;
		}
		
		// remove from all relations in HasTask table first, in keeping with foreign key constraints
		m_database.delete(POQTListConstants.DB_HASTASK_TABLE_NAME, POQTListConstants.DB_HASTASK_KEY_TASKID + " = '" + task.getID() + "'", null);
		
		// now remove Task from Task table
		m_database.delete(POQTListConstants.DB_TASK_TABLE_NAME, POQTListConstants.DB_TASK_KEY_ID + " = '" + task.getID() + "'", null);
		
		return true;
	}
	
	/**
	 * Deletes the TaskList from the database if it exists; do nothing otherwise
	 * 
	 * @param list TaskList to delete
	 */
	public void deleteTaskList(TaskList list) {
		// sanity check
		if (list == null) {
			return;
		}
		
		// if TaskList doesn't exist, do nothing
		if (!doesListExist(list.getID())) {
			return;
		}
		
		// remove from all relations in HasTask table first, in keeping with foreign key constraints
		m_database.delete(POQTListConstants.DB_HASTASK_TABLE_NAME, POQTListConstants.DB_HASTASK_KEY_LISTID + " = " + list.getID(), null);
		
		// now remove TaskList from Task table
		m_database.delete(POQTListConstants.DB_TASKLIST_TABLE_NAME, POQTListConstants.DB_TASKLIST_KEY_ID + " = " + list.getID(), null);
	}
	
	/**
	 * Removes Task from given custom TaskList if it is already a member
	 * 
	 * @param task Task to remove from list
	 * @return True if the Task was successfully removed from the given TaskList; false otherwise
	 */
	public boolean removeTaskFromList(Task task, TaskList list) {
		// sanity check
		if (task == null || list == null) {
			return false;
		}
		
		// if relation doesn't exist, do nothing
		if (!doesListContain(list.getID(), task.getID())) {
			return false;
		}
		
		// remove relation
		m_database.delete(POQTListConstants.DB_HASTASK_TABLE_NAME,								// table name
				POQTListConstants.DB_HASTASK_KEY_TASKID + " = '" + task.getID() + "' AND " +	// WHERE arguments
				POQTListConstants.DB_HASTASK_KEY_LISTID + " = '" + list.getID() + "'",
				null);
		
		return true;
	}
	
	/**
	 * Writes any non-ID changes made to the given Task to the database
	 * NOTE: Performs updates for Task information only; does NOT change TaskList membership
	 * 
	 * @param task Task that needs updating
	 * @return True if successful; false otherwise
	 */
	public boolean updateTask(Task task) {
		// sanity check
		if (task == null) {
			return false;
		}
		
		// if task doesn't exist, fail
		if (!doesTaskExist(task.getID())) {
			return false;
		}
		
		// encode DueDate into String using date storage format string constant
		Calendar dueDate = task.getDueDate();
		String encodedDueDate;
		if (dueDate == null) {
			encodedDueDate = null;
		}
		else {
			final String FORMAT = POQTListConstants.DB_DATE_STORAGE_FORMAT;
			encodedDueDate = DateFormat.format(FORMAT, task.getDueDate()).toString();
		}
		
		// encode alarm time into date in milliseconds
		long alarmDateMillis = 0;
		if (dueDate != null) {
			long dueDateMillis = dueDate.getTimeInMillis();
			long alarmOffsetMillis = task.getAlarm().getMillisOffset();
			alarmDateMillis = dueDateMillis - alarmOffsetMillis;
		}
		
		ContentValues newValues = new ContentValues();
		newValues.put(POQTListConstants.DB_TASK_COLUMN_DESCRIPTION, task.getDescription());
		newValues.put(POQTListConstants.DB_TASK_COLUMN_DUEDATE, encodedDueDate);
		newValues.put(POQTListConstants.DB_TASK_COLUMN_PRIORITY, task.getPriority().ordinal());
		newValues.put(POQTListConstants.DB_TASK_COLUMN_ALARM_MILLIS, alarmDateMillis);
		newValues.put(POQTListConstants.DB_TASK_COLUMN_ALARM_ORDINAL, task.getAlarm().ordinal());
		
		// UPDATE Task SET (description = new descrip, dueDate = new due date, priority = new priority) WHERE ID = task ID
		m_database.update(POQTListConstants.DB_TASK_TABLE_NAME,						// table name
				newValues,															// values mapping
				POQTListConstants.DB_TASK_KEY_ID + "='" + task.getID() + "'",	// where clause
				null);
		return true;
	}
	
	/**
	 * Updates the given TaskList's information in the database
	 * 
	 * @param list TaskList that needs updating
	 * @return True if the update was successful, false otherwise
	 */
	public boolean updateTaskList(TaskList list) {
		// sanity check
		if (list == null) {
			return false;
		}
		
		// if TaskList doesn't exist, fail
		if (!doesListExist(list.getID())) {
			return false;
		}
		
		// encode TaskList type
		TaskAdapter adapter = list.getAdapter();
		int adapterTypeOrdinal = adapter.getType().ordinal();
		
		// pack up updated values
		ContentValues updatedValues = new ContentValues();
		updatedValues.put(POQTListConstants.DB_TASKLIST_COLUMN_NAME, list.getName());
		updatedValues.put(POQTListConstants.DB_TASKLIST_COLUMN_ADAPTERTYPE_ORDINAL, adapterTypeOrdinal);
		
		// UPDATE TaskList SET (listName = new list name, adapterTypeOrdinal = new adapter type ordinal) WHERE listID = list's ID
		m_database.update(POQTListConstants.DB_TASKLIST_TABLE_NAME,						// table name
				updatedValues,															// values mapping
				POQTListConstants.DB_TASKLIST_KEY_ID + "='" + list.getID() + "'",		// where clause
				null);
		return true;
	}
	
	
	
	
	
	/**
	 * Checks if the given TaskList exists in the database
	 * 
	 * @param listID Unique ID of TaskList to search for
	 * @return True if the TaskList exists in the database; false otherwise
	 */
	public boolean doesListExist(long listID) {
		// SELECT some column FROM TaskList WHERE TaskList.ID = list ID
		Cursor existsResult = m_database.query(POQTListConstants.DB_TASKLIST_TABLE_NAME, new String[]{POQTListConstants.DB_TASKLIST_COLUMN_NAME},
				POQTListConstants.DB_TASKLIST_KEY_ID + " = '" + listID + "'", null, null, null, null);
		if (existsResult.getCount() != 0) {
			existsResult.close();
			return true;
		}
		
		existsResult.close();
		return false;
	}
	
	/**
	 * Checks if the given Task exists in the database
	 * 
	 * @param taskID Identifier for Task to check for
	 * @return True if the Task exists in the database; false otherwise
	 */
	public boolean doesTaskExist(long taskID) {
		// SELECT some column FROM Task WHERE Task.ID = task ID
		Cursor existsResult = m_database.query(POQTListConstants.DB_TASK_TABLE_NAME, new String[]{POQTListConstants.DB_TASK_KEY_ID},
				POQTListConstants.DB_TASK_KEY_ID + " = '" + taskID + "'", null, null, null, null);
		if (existsResult.getCount() != 0) {
			existsResult.close();
			return true;
		}
		
		existsResult.close();
		return false;
	}
	
	/**
	 * Checks if a relation between the given TaskList and the given Task exists
	 * 
	 * @param listID Unique identifier of TaskList to check for
	 * @param taskID Unique identifier of Task to check for
	 * @return True if the relation exists; false otherwise
	 */
	public boolean doesListContain(long listID, long taskID) {
		// SELECT some column FROM HasTask WHERE HasTask.listID = list ID AND HasTask.taskID = task ID
		Cursor existsResult = m_database.query(POQTListConstants.DB_HASTASK_TABLE_NAME,
				new String[]{POQTListConstants.DB_HASTASK_KEY_LISTID},	// select only one arbitrary column to minimize data footprint
				POQTListConstants.DB_HASTASK_KEY_LISTID + "='" + listID + "' AND " + POQTListConstants.DB_HASTASK_KEY_TASKID + "='" + taskID + "'",
				null, null, null, null);
		if (existsResult.getCount() != 0) {
			existsResult.close();
			return true;
		}
		
		existsResult.close();
		return false;
	}
	
	/**
	 * Build a TaskList object for the TaskList database entry identified by the given name
	 * 
	 * @param listName Unique name of TaskList in database to build TaskList object for
	 * @param adapterType 
	 * @return TaskList object generated by the database, or null if no entry for the given name exists in the database
	 */
	public TaskList getTaskList(long listID) {
		// check if list exists in database
		if (!doesListExist(listID)) {
			Log.d(POQTListConstants.LOG_TAG, "Could not find list with ID: " + listID + " in database");
			return null;
		}
		
		// SELECT * FROM TaskList WHERE TaskList.ID = list ID
		Cursor taskListCursor = m_database.query(POQTListConstants.DB_TASKLIST_TABLE_NAME,
				null,
				POQTListConstants.DB_TASKLIST_KEY_ID + "='" + listID + "'",
				null, null, null, null);
		// ensure results were found
		if (taskListCursor.getCount() == 0) {
			return null;
		}
		taskListCursor.moveToFirst();
		
		// retrieve list information from database query results
		int nameIndex = taskListCursor.getColumnIndex(POQTListConstants.DB_TASKLIST_COLUMN_NAME);
		int adapterTypeOrdinalIndex = taskListCursor.getColumnIndex(POQTListConstants.DB_TASKLIST_COLUMN_ADAPTERTYPE_ORDINAL);
		String listName = taskListCursor.getString(nameIndex);
		int adapterTypeOrdinal = taskListCursor.getInt(adapterTypeOrdinalIndex);
		TaskAdapter.Type adapterType = TaskAdapter.Type.findType(adapterTypeOrdinal);
		taskListCursor.close();
		
		// SELECT taskID FROM HasTask WHERE listID = given list ID
		String containedTasksQuery = "SELECT " + POQTListConstants.DB_HASTASK_KEY_TASKID + " FROM  " + POQTListConstants.DB_HASTASK_TABLE_NAME +
				" WHERE " + POQTListConstants.DB_HASTASK_KEY_LISTID + "='" + listID + "'";
		// contained tasks INNER JOIN Task ON contained.taskID = Task.ID
		String joinQuery = "(" + containedTasksQuery + ") INNER JOIN " + POQTListConstants.DB_TASK_TABLE_NAME + " ON " +
				POQTListConstants.DB_HASTASK_KEY_TASKID + "=" + POQTListConstants.DB_TASK_KEY_ID;
		// SELECT * FROM contained tasks joined with task data
		String selectQuery  = "SELECT * FROM (" + joinQuery + ");";
		Cursor containedTasksResults = m_database.rawQuery(selectQuery, null);
		
		// build list of tasks from database results
		List<Task> containedTasks = unpackTasks(containedTasksResults);
		containedTasksResults.close();
		TaskAdapter listAdapter = new TaskAdapter(m_context, adapterType, containedTasks);
		
		return new TaskList(listID, listName, listAdapter);
	}
	
	/**
	 * Generates all lists from database that do not have the "main" or "completed" list's names
	 * 
	 * @return List of custom TaskLists in database
	 */
	public ArrayList<TaskList> getCustomLists() {
		ArrayList<TaskList> customLists = new ArrayList<TaskList>();
		
		// SELECT TaskList.listID FROM TaskList WHERE TaskList.listID != main task list || completed task list
		long mainListID = POQTListConstants.MAIN_TASKLIST_ID;
		long completedListID = POQTListConstants.COMPLETED_TASKLIST_ID;
		Cursor customListsResult = m_database.query(POQTListConstants.DB_TASKLIST_TABLE_NAME, 
				new String[]{POQTListConstants.DB_TASKLIST_KEY_ID},
				POQTListConstants.DB_TASKLIST_KEY_ID + "!='" + mainListID + "' AND " 
					+ POQTListConstants.DB_TASKLIST_KEY_ID + "!='" + completedListID + "'",
				null, null, null, null);
		Log.d(POQTListConstants.LOG_TAG, "Cursor of custom lists has size: " + customListsResult.getCount());
		
		// iterate through each custom list, build it into a TaskList object, and add to list of custom TaskLists
		while (customListsResult.moveToNext()) {
			long listID = customListsResult.getLong(0);
			Log.d(POQTListConstants.LOG_TAG, "Examining custom list with ID: " + listID);
			TaskList newList = getTaskList(listID);
			
			// skip ID if list cannot be built
			if (newList == null) {
				Log.d(POQTListConstants.LOG_TAG, "Did not add list with ID: " + listID + " as it was null");
				continue;
			}
			
			customLists.add(newList);
		}
		
		customListsResult.close();
		return customLists;
	}
	
	/**
	 * Gets all Tasks which have an alarm set for some time in the future
	 * 
	 * @return List of tasks with future alarms
	 */
	public List<Task> getAlarmTasks() {
		Calendar currentTime = Calendar.getInstance();
		long currentMillis = currentTime.getTimeInMillis();
		
		// SELECT description, alarmInMillis FROM Task WHERE alarmInMillis > current time in millis
		Cursor alarmTasksResult = m_database.query(POQTListConstants.DB_TASK_TABLE_NAME,
				null,
				POQTListConstants.DB_TASK_COLUMN_ALARM_MILLIS + " > " + currentMillis,
				null, null, null, null);
		
		List<Task> alarmTasks = unpackTasks(alarmTasksResult);
		alarmTasksResult.close();
		return alarmTasks;
	}
	
	
	// ========== Helper Functions ==========
	/**
	 * Unpacks the results from a query containing all the columns in the Task table into a List of Task objects.
	 * Cursor should contain at least all the columns of the Task table.
	 * 
	 * @param results List of constructed Tasks
	 */
	private List<Task> unpackTasks(Cursor results) {
		List<Task> unpackedTasks = new ArrayList<Task>();
		
		// sanity check
		if (results == null) {
			throw new IllegalArgumentException("Cannot unpack null Cursor");
		}
				
		// get column indices
		int IDColumnIndex = results.getColumnIndex(POQTListConstants.DB_TASK_KEY_ID);
		int descriptionColumnIndex = results.getColumnIndex(POQTListConstants.DB_TASK_COLUMN_DESCRIPTION);
		int dueDateColumnIndex = results.getColumnIndex(POQTListConstants.DB_TASK_COLUMN_DUEDATE);
		int priorityOrdinalColumnIndex = results.getColumnIndex(POQTListConstants.DB_TASK_COLUMN_PRIORITY);
		int alarmOrdinalColumnIndex = results.getColumnIndex(POQTListConstants.DB_TASK_COLUMN_ALARM_ORDINAL);
		
		// ensure all columns are in Cursor
		boolean allColumnsPresent = IDColumnIndex != -1
				&& descriptionColumnIndex != -1
				&& dueDateColumnIndex != -1
				&& priorityOrdinalColumnIndex != -1
				&& alarmOrdinalColumnIndex != -1;
		if (!allColumnsPresent) {
			throw new IllegalArgumentException("Cursor to unpack should contain all columns of Task table");
		}
		
		// unpack each task's information from database result into Task object
		while (results.moveToNext()) {
			// unpack ID and description
			long taskID = results.getLong(IDColumnIndex);
			String description = results.getString(descriptionColumnIndex);
			
			// decode due date
			String encodedDueDate = results.getString(dueDateColumnIndex);
			GregorianCalendar dueDate;
			if (encodedDueDate != null) {
				// !!!! MAY NOT ACCOUNT FOR TIME ZONES PROPERLY !!!
				SimpleDateFormat format = new SimpleDateFormat(POQTListConstants.DB_DATE_STORAGE_FORMAT);
				Date tempDueDate;
				try {
					tempDueDate = format.parse(encodedDueDate);
				} catch (java.text.ParseException exc) {
					continue;
				}
				dueDate = new GregorianCalendar();
				dueDate.setTime(tempDueDate);
			}
			else {
				dueDate = null;
			}
			
			// decode priority
			int priorityOrdinal = results.getInt(priorityOrdinalColumnIndex);
			Priority priority = Priority.findPriority(priorityOrdinal);
			
			// decode alarm
			int alarmOrdinal = results.getInt(alarmOrdinalColumnIndex);
			Alarm alarm = Alarm.findAlarm(alarmOrdinal);
			
			Log.d(POQTListConstants.LOG_TAG, "Unpacking task with ID: " + taskID);

			// build Task object and add to adapter
			Task newTask = new Task(taskID, description, dueDate, priority, alarm);
			unpackedTasks.add(newTask);
		}
		
		return unpackedTasks;
	}
}
