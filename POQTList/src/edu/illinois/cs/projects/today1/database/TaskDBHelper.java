package edu.illinois.cs.projects.today1.database;

import java.text.SimpleDateFormat;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;
import android.util.Log;
import edu.illinois.cs.projects.today1.POQTListConstants;
import edu.illinois.cs.projects.today1.task.Task;
import edu.illinois.cs.projects.today1.task.TaskList;

/**
 * Overwrites the SQLiteOpenHelper class to implement a database helper specific to POQTList
 * 
 * @author Kevin Today
 *
 * Based on code found on the Android developer's site for the SQLiteOpenHelper
 */
public class TaskDBHelper extends SQLiteOpenHelper {
	// ========== Member Variables ==========
	private static final int database_version = 1;

	
	// ========== Constructors ==========
	/**
	 * Instantiates a new TaskDatabaseHelper object
	 * 
	 * @param context Handle to the context the database is being created in
	 */
	public TaskDBHelper(Context context) {
		super(context, POQTListConstants.DB_NAME, null, 1);
	}
	
	
	// ========== Overriden Functions ==========
	/**
	 * Called when creating the SQLite database for the first time
	 * 
	 * @param db The database to execute the SQLite query upon
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// formulate queries to create the database
		// create Task table
		
		// CREATE TALBE IF NOT EXISTS Task (ID, description, dueDate, priority, alarmInMillis)
		String taskTableQuery = "CREATE TABLE IF NOT EXISTS " + POQTListConstants.DB_TASK_TABLE_NAME + " ("
			+ POQTListConstants.DB_TASK_KEY_ID + " INTEGER PRIMARY KEY, "
			+ POQTListConstants.DB_TASK_COLUMN_DESCRIPTION + " TEXT, "
			+ POQTListConstants.DB_TASK_COLUMN_DUEDATE + " TEXT, "
			+ POQTListConstants.DB_TASK_COLUMN_PRIORITY + " INT,"			// index of priority in Task.Priority constant
			+ POQTListConstants.DB_TASK_COLUMN_ALARM_MILLIS + " INT,"		// time of alarm in milliseconds
			+ POQTListConstants.DB_TASK_COLUMN_ALARM_ORDINAL + " INT);";	// index of alarm in Task.Alarm constant
		db.execSQL(taskTableQuery);
		
		// CREATE TABLE IF NOT EXISTS TaskList (ID, name, type)
		String taskListQuery = "CREATE TABLE IF NOT EXISTS " + POQTListConstants.DB_TASKLIST_TABLE_NAME + " ("
			+ POQTListConstants.DB_TASKLIST_KEY_ID + " INTEGER PRIMARY KEY,"
			+ POQTListConstants.DB_TASKLIST_COLUMN_NAME + " TEXT, "
			+ POQTListConstants.DB_TASKLIST_COLUMN_ADAPTERTYPE_ORDINAL + " INTEGER"
			+ ");";
		db.execSQL(taskListQuery);
		
		// create HasTask table, a relation between the TaskList and Task tables
		// CREATE TABLE IF NOT EXISTS HasTask (listID, taskID)
		String hasTaskQuery = "CREATE TABLE IF NOT EXISTS " + POQTListConstants.DB_HASTASK_TABLE_NAME + " ("
			+ POQTListConstants.DB_HASTASK_KEY_LISTID + " INTEGER,"
			+ POQTListConstants.DB_HASTASK_KEY_TASKID + " INTEGER,"
			+ "PRIMARY KEY (" + POQTListConstants.DB_HASTASK_KEY_LISTID + ", " + POQTListConstants.DB_HASTASK_KEY_TASKID + "), "
			+ "FOREIGN KEY(" + POQTListConstants.DB_HASTASK_KEY_LISTID + ") REFERENCES " 
				+ POQTListConstants.DB_TASKLIST_TABLE_NAME + "(" + POQTListConstants.DB_TASKLIST_KEY_ID + "), "
			+ "FOREIGN KEY(" + POQTListConstants.DB_HASTASK_KEY_TASKID + ") REFERENCES " 
				+ POQTListConstants.DB_TASK_TABLE_NAME + "(" + POQTListConstants.DB_TASK_KEY_ID + ")"
			+ ");";
		db.execSQL(hasTaskQuery);
	}

	@Override
	/**
	 * Called when upgrading the database
	 * 
	 * @param db The database to upgrade.
	 * @param oldVersion The database's old version.
	 * @param newVersion The database's new version.
	 */
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// logs that the database is being upgraded
		Log.w("TaskDatabaseHelper", "Upgrading database from " + oldVersion + " to " + newVersion + "; destroying all old data");
		
		// upgrade version 2
		if (oldVersion == 1 && newVersion == 2) {
			// run alteration scripts
		}
		else {
			// drops everything and recreates database
			db.execSQL("DROP TABLE IF EXISTS " + POQTListConstants.DB_TASK_TABLE_NAME + ";");
			db.execSQL("DROP TABLE IF EXISTS " + POQTListConstants.DB_TASKLIST_TABLE_NAME + ";");
			db.execSQL("DROP TABLE IF EXISTS " + POQTListConstants.DB_HASTASK_TABLE_NAME + ";");
			onCreate(db);
		}
	}
}
