package edu.illinois.cs.projects.today1.main;

import java.util.Calendar;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import edu.illinois.cs.projects.today1.POQTListConstants;
import edu.illinois.cs.projects.today1.R;
import edu.illinois.cs.projects.today1.task.Task;
import edu.illinois.cs.projects.today1.task.TaskAdapter;
import edu.illinois.cs.projects.today1.task.TaskApparatus;

/**
 * Main activity for the POQTList application. Controls all task list display and editing
 * Actual task implementation should be black-boxed; this application should never know how it works
 * 
 * @author Kevin Today
 */
public class POQTListActivity extends ListActivity {
	// ========== Constants ==========
	public static enum RequestCode {
		ADD_TASK,
		EDIT_TASK,
		ADD_TASK_LIST,
		EDIT_TASK_LIST;
		
		/**
		 * Find the RequestCode object corresponding to the given ordinal
		 * 
		 * @param ordinal Ordinal of request code
		 * @return RequestCode object representing the ordinal, or null if the ordinal was invalid
		 */
		public static RequestCode findRequestCode(int ordinal) {
			switch (ordinal) {
			case 0:
				return ADD_TASK;
			case 1:
				return EDIT_TASK;
			case 2:
				return ADD_TASK_LIST;
			case 3:
				return EDIT_TASK_LIST;
			default:
				return null;
			}
		}
	}
	
	// ========== Member Variables ==========
	private DialogFactory m_dialogFactory;		// factory to produce dialogs for this Activity
	private TaskApparatus m_apparatus;			// TaskApparatus containing TaskList information and functions
	private GestureDetector m_detector;			// GestureDetector to listen for fling gestures to change lists
	private Integer m_editIndex;				// index of selected task (used for editing tasks)
	
	
	// ========== Overridden Activity Functions ==========
	/** 
	 * Called when the activity is first created
	 *  Initializes everything
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainactivity);
		
		SharedPreferences preferences = this.getSharedPreferences(POQTListConstants.PREFERENCES_FILEPATH, MODE_PRIVATE);
		// load default preferences if first time running app
		boolean prefLoaded = preferences.getBoolean(POQTListConstants.PREF_KEY_PREFERENCES_LOADED, false);
		if (!prefLoaded) {
			setDefaultPreferences();
		}
		
		// set list title attributes and selection (to initiate marquee)
		TextView listTitle = (TextView)findViewById(R.id.list_title);
		listTitle.setTextColor(preferences.getInt(POQTListConstants.PREF_KEY_TASK_LIST_TITLE_COLR, POQTListConstants.DEFAULT_TASK_LIST_TITLE_COLOR));
		listTitle.setSelected(true);
		
		// create TaskApparatus
		m_dialogFactory = new DialogFactory(this);
		m_apparatus = new TaskApparatus(this);
		
		// detect fling gestures for cycling through lists
		m_detector = new GestureDetector(this, new CycleListListener(this, m_apparatus));
		getListView().setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return m_detector.onTouchEvent(event);
			}
		});
		
		// registers every item in the ListView to display the Task menu on long click
		registerForContextMenu(getListView());
		m_editIndex = null;
	}
	
	/**
	 * Attempt to free database resources when Activity dies
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (m_apparatus != null) {
			m_apparatus.close();
		}
	}
	
	/**
	 * Keeps display current when Activity resumes
	 */
	@Override
	protected void onResume() {
		super.onResume();
		this.refreshDisplay();
	}
	
	
	/**
	 * Show ContextMenu when Views representing Tasks are short clicked
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		System.out.println("ITEM CLICKED");
		super.onListItemClick(l, v, position, id);
		v.showContextMenu();
	}
	
	/**
	 * Inflate the ContextMenu to display Task menu information
	 * 
	 * @param menu The ContextMenu being created
	 * @param v The View for which the ContextMenu is being created for
	 * @param menuInfo Information about the ContextMenu
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.taskmenu, menu);
		
		// customize menu based on which list user is looking at
		TaskApparatus.ListCategory listType = m_apparatus.getListCategory();
		// "main" list doesn't have "restore" or "remove from list"
		if (listType == TaskApparatus.ListCategory.MAIN) {
			menu.removeItem(R.id.task_remove_from_list);
			menu.removeItem(R.id.task_restore);
		}
		// "completed" list doesn't have "done", "edit", "add to list", or "remove from list"
		else if (listType == TaskApparatus.ListCategory.COMPLETED) {
			menu.removeItem(R.id.task_done);
			menu.removeItem(R.id.task_edit);
			menu.removeItem(R.id.task_add_to_lists);
			menu.removeItem(R.id.task_remove_from_list);
		}
		// custom list doesn't have "restore"
		else if (listType == TaskApparatus.ListCategory.CUSTOM) {
			menu.removeItem(R.id.task_restore);
		}

	}
	
	/**
	 * When a menu item is clicked within a ContextMenu for a task object, perform appropriate actions
	 * 
	 * @param item Information on the mnu item clicked
	 * @return True on success; false otherwise
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		super.onContextItemSelected(item);
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo)item.getMenuInfo();
		int index = menuInfo.position;	// position of Task object within TaskList
		switch (item.getItemId()) {
		case R.id.task_done:
			m_apparatus.completeTask(index);
			break;
		case R.id.task_edit:
			editTask(index);
			break;
		case R.id.task_add_to_lists:
			// bring up context menu to select which lists to add to
		case R.id.task_remove_from_list:
			m_apparatus.removeTaskFromList(index);
			break;
		case R.id.task_delete:
			m_apparatus.deleteTask(index);
			break;
		}
		
		return true;
	}
	
	/**
	 * Create options menu, displayed when user presses MENU button.
	 * Options menus are customized based on which task list the user is looking at.
	 * 
	 * @param menu Menu object that ends up getting displayed
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		
		TaskApparatus.ListCategory listType = m_apparatus.getListCategory();
		// "Main" list has Refresh, Settings, and Add Task List buttons
		if (listType == TaskApparatus.ListCategory.MAIN) {
			menu.add(Menu.NONE, R.id.option_refresh, 0, R.string.option_refresh);
			menu.add(Menu.NONE, R.id.option_settings, 1, R.string.option_settings);
			menu.add(Menu.NONE, R.id.option_add_tasklist, 2, R.string.option_add_tasklist);
		}
		// "Completed" list has Refresh, Settings, and Add Task List buttons
		else if (listType == TaskApparatus.ListCategory.COMPLETED) {
			menu.add(Menu.NONE, R.id.option_refresh, 0, R.string.option_refresh);
			menu.add(Menu.NONE, R.id.option_settings, 1, R.string.option_settings);
			menu.add(Menu.NONE, R.id.option_add_tasklist, 2, R.string.option_add_tasklist);
		}
		// "Custom" lists have Refresh, Settings, Add Task List, Edit Task List, and Delete Task List buttons
		else if (listType == TaskApparatus.ListCategory.CUSTOM) {
			menu.add(Menu.NONE, R.id.option_refresh, 0, R.string.option_refresh);
			menu.add(Menu.NONE, R.id.option_settings, 1, R.string.option_settings);
			menu.add(Menu.NONE, R.id.option_add_tasklist, 2, R.string.option_add_tasklist);
			menu.add(Menu.NONE, R.id.option_edit_tasklist, 3, R.string.option_edit_tasklist);
			menu.add(Menu.NONE, R.id.option_delete_tasklist, 4, R.string.option_delete_tasklist);
		}
		return true;
	}
	
	/**
	 * Defines on-push behavior for each Option Menu button
	 * 
	 * @param item Item the user selected
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.option_refresh:
			refreshDisplay();
			return true;
		case R.id.option_settings:
			// !!!DO SETTINGS STUFF HERE !!!!
			return true;
		case R.id.option_add_tasklist:
			// launch Activity for adding task list
			Intent launchAddTaskList = new Intent(this, TaskListActivity.class);
			launchAddTaskList.putExtra(POQTListConstants.ACTIVITY_REQUEST_CODE, RequestCode.ADD_TASK_LIST.ordinal());
			startActivityForResult(launchAddTaskList, RequestCode.ADD_TASK_LIST.ordinal());
			return true;
		case R.id.option_edit_tasklist:
			// launch Activity for editing task list
			Bundle listInfo = m_apparatus.getTaskListInformation();
			Intent launchEditTaskList = new Intent(this, TaskListActivity.class);
			launchEditTaskList.putExtras(listInfo);
			launchEditTaskList.putExtra(POQTListConstants.ACTIVITY_REQUEST_CODE, RequestCode.EDIT_TASK_LIST.ordinal());
			startActivityForResult(launchEditTaskList, RequestCode.EDIT_TASK_LIST.ordinal());
			return true;
		case R.id.option_delete_tasklist:
			m_apparatus.deleteTaskList();
			refreshDisplay();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * Handler method for whenever a child Activity returns
	 * 
	 * @param requestCode Code for the request that started the child Activity
	 * @param resultCode Code returned by the child Activity
	 * @param data Data returned by the child Activity
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		// if TaskActivity just returned from an "Add Task" command, unpack results and attempt to add task
		if (requestCode == RequestCode.ADD_TASK.ordinal() && resultCode == Activity.RESULT_OK) {
			addTaskFromActivity(data);
		}
		// if TaskActivity just returned from an "Edit Task" command, unpack results and attempt to change the target task
		else if (requestCode == RequestCode.EDIT_TASK.ordinal() && resultCode == Activity.RESULT_OK) {
			editTaskFromActivity(data);
		}
		// if TaskListActivity just returned from "Add Task List" command, unpack results and attempt to add task
		else if (requestCode == RequestCode.ADD_TASK_LIST.ordinal() && resultCode == Activity.RESULT_OK) {
			addTaskListFromActivity(data);
		}
		// if TaskListActivity just returned from "Edit Task List" command, unpack results and attempt to change the target task list 
		else if (requestCode == RequestCode.EDIT_TASK_LIST.ordinal() && resultCode == Activity.RESULT_OK) {
			editTaskListFromActivity(data);
		}
	}
	
	/**
	 * Checks gestures against fling gesture detector for list cycling
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		return m_detector.onTouchEvent(event);
	}
	
	
	// ========== Public Functions ==========
	/**
	 * Callback invoked when user presses the "Add Task" button.
	 * Creates an Activity for adding a new Task to the selected Task List.
	 * 
	 * @param view View calling the addTask method (in this case, the "Add Task" button)
	 */
	public void onAddTaskPress(View view) {
		Intent launchAddTask = new Intent(this, TaskActivity.class);
		launchAddTask.putExtra(POQTListConstants.ACTIVITY_REQUEST_CODE, RequestCode.ADD_TASK.ordinal());
		startActivityForResult(launchAddTask, RequestCode.ADD_TASK.ordinal());
	}
	
	/**
	 * Refreshes the list being displayed in the Activity
	 */
	public void refreshDisplay() {
		setListAdapter(m_apparatus.getSelectedAdapter());	// load ListView with 
		
		TextView listTitle = (TextView)findViewById(R.id.list_title);
		listTitle.setText(m_apparatus.getSelectedTitle());
		
		// task list-specific details
		// disable "add task" for the completed list
		Button addTask = (Button)findViewById(R.id.add_task);
		TaskApparatus.ListCategory selectedListType = m_apparatus.getListCategory();
		if (selectedListType == TaskApparatus.ListCategory.COMPLETED) {
			addTask.setVisibility(View.GONE);
		}
		else {
			addTask.setVisibility(View.VISIBLE);
		}
	}
	
	
	// ========== Helper Functions ==========
	/**
	 * Creates an Activity for editing the selected task
	 * 
	 * @param index Index of selected item within currently displayed task list
	 */
	private void editTask(int index) {
		m_editIndex = new Integer(index);		// set indicator of index being edited for when Activity returns
		Bundle currentInfo = m_apparatus.getTaskInformation(index);
		Intent launchEditTask = new Intent(this, TaskActivity.class);
		launchEditTask.putExtras(currentInfo);
		launchEditTask.putExtra(POQTListConstants.ACTIVITY_REQUEST_CODE, RequestCode.EDIT_TASK.ordinal());
		startActivityForResult(launchEditTask, RequestCode.EDIT_TASK.ordinal());
	}
	
	/**
	 * Creates an Activity for editing the currently-selected task list
	 * 
	 * @param index Index of item to edit within currently displayed task list
	 */
	private void editTaskList() {

	}
	
	/**
	 * Sets the application's preferences to the default configuration
	 */
	private void setDefaultPreferences() {
		SharedPreferences preferences = this.getSharedPreferences(POQTListConstants.PREFERENCES_FILEPATH, MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = preferences.edit();
		
		// set default task list title color
		prefEditor.putInt(POQTListConstants.PREF_KEY_TASK_LIST_TITLE_COLR, POQTListConstants.DEFAULT_TASK_LIST_TITLE_COLOR);
		
		// set defaults for Task urgency calculation
		prefEditor.putInt(POQTListConstants.PREF_KEY_TIME_WINDOW, POQTListConstants.DEFAULT_TIME_WINDOW);
		prefEditor.putInt(POQTListConstants.PREF_KEY_URGENCY_SCALE, POQTListConstants.DEFAULT_URGENCY_SCALE);
		prefEditor.putFloat(POQTListConstants.PREF_KEY_LOW_PRIORITY_WEIGHT, POQTListConstants.DEFAULT_LOW_PRIORITY_WEIGHT);
		prefEditor.putFloat(POQTListConstants.PREF_KEY_NORMAL_PRIORITY_WEIGHT, POQTListConstants.DEFAULT_NORMAL_PRIORITY_WEIGHT);
		prefEditor.putFloat(POQTListConstants.PREF_KEY_HIGH_PRIORITY_WEIGHT, POQTListConstants.DEFAULT_HIGH_PRIORITY_WEIGHT);
		prefEditor.putFloat(POQTListConstants.PREF_KEY_MID_URGENCY_WEIGHT, POQTListConstants.DEFAULT_MID_URGENCY_WEIGHT);
		prefEditor.putFloat(POQTListConstants.PREF_KEY_HIGH_URGENCY_WEIGHT, POQTListConstants.DEFAULT_HIGH_URGENCY_WEIGHT);

		// defaults for a newly-created Task
		prefEditor.putString(POQTListConstants.PREF_KEY_NEW_TASK_DESCRIPTION, POQTListConstants.DEFAULT_NEW_TASK_DESCRIPTION);
		prefEditor.putInt(POQTListConstants.PREF_KEY_NEW_TASK_PRIORITY_ORDINAL, POQTListConstants.DEFAULT_NEW_TASK_PRIORITY.ordinal());
		prefEditor.putInt(POQTListConstants.PREF_KEY_NEW_TASK_ALARM_ORDINAL, POQTListConstants.DEFAULT_NEW_TASK_ALARM.ordinal());
		
		// defaults for Task background
		prefEditor.putInt(POQTListConstants.PREF_KEY_NO_URGNCY_BKGRND_COLR, POQTListConstants.DEFAULT_NO_URGENCY_BKGRND_COLOR);
		prefEditor.putInt(POQTListConstants.PREF_KEY_LOW_URGNCY_BKGRND_COLR, POQTListConstants.DEFAULT_LOW_URGENCY_BKGRND_COLOR);
		prefEditor.putInt(POQTListConstants.PREF_KEY_MID_URGNCY_BKGRND_COLR, POQTListConstants.DEFAULT_MID_URGENCY_BKGRND_COLOR);
		prefEditor.putInt(POQTListConstants.PREF_KEY_HI_URGNCY_BKGRND_COLR, POQTListConstants.DEFAULT_HIGH_URGENCY_BKGRND_COLOR);
		prefEditor.putInt(POQTListConstants.PREF_KEY_DUE_BKGRND_COLR, POQTListConstants.DEFAULT_DUE_BKGRND_COLOR);
		prefEditor.putInt(POQTListConstants.PREF_KEY_OVERDUE_BKGRND_COLR, POQTListConstants.DEFAULT_OVERDUE_BKGRND_COLOR);
		
		// defaults for Task text
		prefEditor.putInt(POQTListConstants.PREF_KEY_NO_URGNCY_TEXT_COLR, POQTListConstants.DEFAULT_NO_URGENCY_TEXT_COLOR);
		prefEditor.putInt(POQTListConstants.PREF_KEY_LOW_URGNCY_TEXT_COLR, POQTListConstants.DEFAULT_LOW_URGENCY_TEXT_COLOR);
		prefEditor.putInt(POQTListConstants.PREF_KEY_MID_URGNCY_TEXT_COLR, POQTListConstants.DEFAULT_MID_URGENCY_TEXT_COLOR);
		prefEditor.putInt(POQTListConstants.PREF_KEY_HI_URGNCY_TEXT_COLR, POQTListConstants.DEFAULT_HIGH_URGENCY_TEXT_COLOR);
		prefEditor.putInt(POQTListConstants.PREF_KEY_DUE_TEXT_COLR, POQTListConstants.DEFAULT_DUE_TEXT_COLOR);
		prefEditor.putInt(POQTListConstants.PREF_KEY_OVERDUE_TEXT_COLR, POQTListConstants.DEFAULT_OVERDUE_TEXT_COLOR);
		
		// defaults for Task subtext
		prefEditor.putInt(POQTListConstants.PREF_KEY_NO_URGNCY_SUBTEXT_COLR, POQTListConstants.DEFAULT_NO_URGENCY_SUBTEXT_COLOR);
		prefEditor.putInt(POQTListConstants.PREF_KEY_LOW_URGNCY_SUBTEXT_COLR, POQTListConstants.DEFAULT_LOW_URGENCY_SUBTEXT_COLOR);
		prefEditor.putInt(POQTListConstants.PREF_KEY_MID_URGNCY_SUBTEXT_COLR, POQTListConstants.DEFAULT_MID_URGENCY_SUBTEXT_COLOR);
		prefEditor.putInt(POQTListConstants.PREF_KEY_HI_URGNCY_SUBTEXT_COLR, POQTListConstants.DEFAULT_HIGH_URGENCY_SUBTEXT_COLOR);
		prefEditor.putInt(POQTListConstants.PREF_KEY_DUE_SUBTEXT_COLR, POQTListConstants.DEFAULT_DUE_SUBTEXT_COLOR);
		prefEditor.putInt(POQTListConstants.PREF_KEY_OVERDUE_SUBTEXT_COLR, POQTListConstants.DEFAULT_OVERDUE_SUBTEXT_COLOR);
		
		// ID counter initializations
		prefEditor.putLong(POQTListConstants.PREF_KEY_TASK_ID_COUNTER, POQTListConstants.INITIAL_TASK_ID_COUNTER);
		prefEditor.putLong(POQTListConstants.PREF_KEY_TASKLIST_ID_COUNTER, POQTListConstants.INITIAL_TASKLIST_ID_COUNTER);
		
		// mark preferences as loaded
		prefEditor.putBoolean(POQTListConstants.PREF_KEY_PREFERENCES_LOADED, true);
		
		prefEditor.commit();		// atomically commit changes
	}
	
	/**
	 * Interprets results from Activity for adding a new task and adds the task if the Activity's results
	 * were valid.
	 * 
	 * @param data New task information passed back from Activity
	 */
	private void addTaskFromActivity(Intent data) {
		Bundle newTaskData = data.getExtras();
		
		if (newTaskData == null) {
			return;
		}
		
		// retrieve task information from Bundle passed back
		String newDescription = newTaskData.getString(POQTListConstants.TASK_INFO_KEY_DESCRIPTION);
		Calendar newDueDate = TaskActivity.unpackDueDate(newTaskData);
		// decode selected priority index into Priority object
		int newPriorityIndex = newTaskData.getInt(POQTListConstants.TASK_INFO_KEY_PRIORITY_ORDINAL);
		Task.Priority newPriority = Task.Priority.findPriority(newPriorityIndex);
		// decode selected alarm index into Task.Alarm object
		int newAlarmIndex = newTaskData.getInt(POQTListConstants.TASK_INFO_KEY_ALARM_ORDINAL);
		Task.Alarm newAlarm = Task.Alarm.findAlarm(newAlarmIndex);
		
		// add task item through TaskApparatus
		m_apparatus.createTask(newDescription, newDueDate, newPriority, newAlarm);
		refreshDisplay();
	}
	
	/**
	 * Interprets results from Activity for editing an existing task and edits the task if the Activity's
	 * results were valid.
	 * 
	 * @param data Updated task information passed back from Activity
	 */
	private void editTaskFromActivity(Intent data) {
		Bundle changedTaskData = data.getExtras();
		
		if (changedTaskData == null) {
			return;
		}
		
		// retrieve task information from Bundle passed back
		String changedDescription = changedTaskData.getString(POQTListConstants.TASK_INFO_KEY_DESCRIPTION);
		Calendar changedDueDate = TaskActivity.unpackDueDate(changedTaskData);
		// decode the selected priority index into Priority object
		int changedPriorityIndex = changedTaskData.getInt(POQTListConstants.TASK_INFO_KEY_PRIORITY_ORDINAL);
		Task.Priority changedPriority = Task.Priority.findPriority(changedPriorityIndex);
		// decode selected alarm index into Alarm object
		int changedAlarmIndex = changedTaskData.getInt(POQTListConstants.TASK_INFO_KEY_ALARM_ORDINAL);
		Task.Alarm changedAlarm = Task.Alarm.findAlarm(changedAlarmIndex);
		
		// add task item through TaskApparatus
		m_apparatus.modifyTask(m_editIndex.intValue(), changedDescription, changedDueDate, changedPriority, changedAlarm);
		m_editIndex = null;
		refreshDisplay();
	}
	
	/**
	 * Interprets results from Activity for adding task lists and adds the list if the Activity's results were
	 * valid
	 * 
	 * @param data New task list information passed back from Activity
	 */
	private void addTaskListFromActivity(Intent data) {
		Bundle newTaskListData = data.getExtras();
		
		// retrieve task list information from Bundle passed back
		String newName = newTaskListData.getString(POQTListConstants.TASKLIST_INFO_KEY_NAME);
		int newAdapterTypeOrdinal = newTaskListData.getInt(POQTListConstants.TASKLIST_INFO_KEY_ADAPTER_TYPE_ORDINAL);
		TaskAdapter.Type newType = TaskAdapter.Type.findType(newAdapterTypeOrdinal);
		
		// add task list item through TaskApparatus
		m_apparatus.createTaskList(newName, newType);
	}
	
	/**
	 * Interprets results from Activity for editing task lists and edits the given list's information if the Activity's results
	 * were valid.
	 * 
	 * @param data Changed task list information passed back from Activity
	 */
	private void editTaskListFromActivity(Intent data) {
		Bundle changedTaskListData = data.getExtras();
		
		// retrieve task list information from Bundle passed back
		String changedName = changedTaskListData.getString(POQTListConstants.TASKLIST_INFO_KEY_NAME);
		int changedAdapterTypeOrdinal = changedTaskListData.getInt(POQTListConstants.TASKLIST_INFO_KEY_ADAPTER_TYPE_ORDINAL);
		TaskAdapter.Type changedType = TaskAdapter.Type.findType(changedAdapterTypeOrdinal);
		
		// edit task list item through TaskApparatus
		m_apparatus.modifyTaskList(changedName, changedType);
	}
}