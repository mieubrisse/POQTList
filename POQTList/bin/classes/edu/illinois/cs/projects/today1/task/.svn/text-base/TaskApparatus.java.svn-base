package edu.illinois.cs.projects.today1.task;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import edu.illinois.cs.projects.today1.POQTListConstants;
import edu.illinois.cs.projects.today1.alarm.AlarmHelper;
import edu.illinois.cs.projects.today1.database.TaskDBApparatus;
import edu.illinois.cs.projects.today1.main.TaskActivity;

/**
 * Manager class to oversee all Task operations for a main TaskList, completed TaskList, and
 * 	a set of custom TaskLists
 * 
 * @author Kevin Today
 *
 */
public class TaskApparatus {
	// ========== Constants ==========
	// enumerated constant representing categories of TaskLists used in the TaskApparatus
	public static enum ListCategory {
		MAIN,
		COMPLETED,
		CUSTOM
	}

	
	// ========== Member Variables ==========
	private Context m_context;						// Context the TaskListApparatus is operating for
	private TaskList m_selectedList;				// current TaskList the user is interacting with
	private TaskList m_mainList;					// TaskList representing the main task list for the Activity
	private TaskList m_completedList;				// TaskList representing completed tasks
	private ArrayList<TaskList> m_customLists;		// Ordered list of TaskLists representing user-defined task lists
	private TaskDBApparatus m_databaseApparatus;	// apparatus to manage task information in the database
	private TaskFactory m_taskFactory;				// factory to produce Tasks for the apparatus
	private TaskListFactory m_taskListFactory;		// factory to produce TaskLists for the apparatus
	private AlarmHelper m_alarmHelper;				// task alarm helper

	
	// ========== Constructors ==========
	/**
	 * Loads a TaskApparatus object from file if possible, or instantiates a new one if not
	 * WARNING: The user is responsible for calling dismantle() on the TaskApparatus when they are done with it
	 * 
	 * @param context Context the TaskApparatus is bound to
	 */
	public TaskApparatus (Context context) {
		// sanity check
		if (context == null) {
			throw new IllegalArgumentException("TaskApparatus activity cannot be null");
		}
		
		m_context = context;
		m_databaseApparatus = new TaskDBApparatus(m_context);	// apparatus to manage all database activity
		m_taskFactory = new TaskFactory(m_context);
		m_taskListFactory = new TaskListFactory(m_context);
		m_alarmHelper = new AlarmHelper(m_context);
		
		// load lists from database
		TaskList storedMainList = m_databaseApparatus.getTaskList(POQTListConstants.MAIN_TASKLIST_ID);
		TaskList storedCompletedList = m_databaseApparatus.getTaskList(POQTListConstants.COMPLETED_TASKLIST_ID);
		ArrayList<TaskList> storedCustomLists = m_databaseApparatus.getCustomLists();
		
		Log.d(POQTListConstants.LOG_TAG, "Size of custom list array: " + storedCustomLists.size());
		
		// if any of the loaded task lists had issues, start a fresh instance
		if (storedMainList == null || storedCompletedList == null || storedCustomLists == null) {
			// build "main" task list
			TaskAdapter mainAdapter = new TaskAdapter(m_context, POQTListConstants.MAIN_TASKADAPTER_TYPE);
			m_mainList = new TaskList(POQTListConstants.MAIN_TASKLIST_ID, POQTListConstants.MAIN_TASKLIST_NAME, mainAdapter);
			m_databaseApparatus.addTaskList(m_mainList);
			m_selectedList = m_mainList;
			
			// build "completed" task list
			TaskAdapter completedAdapter = new TaskAdapter(m_context, POQTListConstants.COMPLETED_TASKADAPTER_TYPE);
			m_completedList = new TaskList(POQTListConstants.COMPLETED_TASKLIST_ID, POQTListConstants.COMPLETED_TASKLIST_NAME, completedAdapter);
			m_databaseApparatus.addTaskList(m_completedList);
			
			m_customLists = new ArrayList<TaskList>();		// custom TaskLists
		}
		// otherwise, use loaded data
		else {
			m_mainList = storedMainList;
			m_completedList = storedCompletedList;
			m_customLists = storedCustomLists;
			m_selectedList = m_mainList;
		}
	}
	
	
	// ========== Interface Methods (methods exposed to outside applications for accessing the apparatus' data) ==========
	// ---------- Information Bundling Methods ----------
	/**
	 * Packages information of a task
	 * 
	 * @param index Index within currently selected task list of the task
	 * @return Bundle containing the task's information, mapped to keys named in POQTListConstants (or null if the desired
	 * 		task could not be found)
	 */
	public Bundle getTaskInformation(int index) {
		Task task = (Task)m_selectedList.getAdapter().getItem(index);
		
		// sanity check
		if (task == null) {
			System.out.println("COULD NOT GET TASK INFORMATION!");
			return null;
		}
		
		String description = task.getDescription();
		Calendar dueDate = task.getDueDate();
		Task.Priority priority = task.getPriority();
		Task.Alarm alarm = task.getAlarm();
		
		// build bundle to pass back
		Bundle taskInfo = new Bundle();
		taskInfo.putString(POQTListConstants.TASK_INFO_KEY_DESCRIPTION, description);
		taskInfo.putAll(TaskActivity.packDueDate(dueDate));
		taskInfo.putInt(POQTListConstants.TASK_INFO_KEY_PRIORITY_ORDINAL, priority.ordinal());
		taskInfo.putInt(POQTListConstants.TASK_INFO_KEY_ALARM_ORDINAL, alarm.ordinal());
		return taskInfo;
	}
	
	/**
	 * Packages information of about the currently selected task list
	 * 
	 * @return Bundle containing the task list's information, mapped to keys named in POQTListConstants (or null if an error occurred)
	 */
	public Bundle getTaskListInformation() {
		// sanity check
		if (m_selectedList == null) {
			return null;
		}
		
		String listName = m_selectedList.getName();
		int adapterTypeOrdinal = m_selectedList.getAdapter().getType().ordinal();
		
		// build bundle to pass back
		Bundle taskListInfo = new Bundle();
		taskListInfo.putString(POQTListConstants.TASKLIST_INFO_KEY_NAME, listName);
		taskListInfo.putInt(POQTListConstants.TASKLIST_INFO_KEY_ADAPTER_TYPE_ORDINAL, adapterTypeOrdinal);
		return taskListInfo;
	}
	
	// ---------- Task Modification Methods ----------
	/**
	 * Creates a new task with the given parameters
	 * 
	 * @param description Task description (must not be null or whitespace)
	 * @param dueDate Task due date (null value signifies no due date)
	 * @param priority Task priority (null value defaults to "Normal" priority)
	 * @param alarm Task alarm offset from due date (null value indicates no alarm)
	 * @return True if the task was created successfully; false otherwise
	 */
	public boolean createTask(String description, Calendar dueDate, Task.Priority priority, Task.Alarm alarm) {
		// description must be valid, non-whitespace string
		if (description == null || description.trim().length() == 0) {
			throw new IllegalArgumentException("New task's description must be valid, non-whitespace string");
		}
		Task newTask = m_taskFactory.buildTask(description, dueDate, priority, alarm);
		boolean informationAdded = addTaskTool(newTask, m_selectedList);
		boolean alarmsAdded = m_alarmHelper.addTask(newTask);
		return (informationAdded && alarmsAdded);
	}
	
	/**
	 * Modifies the task at the given index within the current task list to have the given parameters
	 * 
	 * @param index Index of task within current task list
	 * @param description Modified description for task
	 * @param dueDate Modified due date for task
	 * @param priority Modified priority for task
	 * @param alarm Modified alarm offset for task
	 * @return True if the modification was successful, false otherwise
	 */
	public boolean modifyTask(int index, String description, Calendar dueDate, Task.Priority priority, Task.Alarm alarm) {
		Task changedTask = (Task)m_selectedList.getAdapter().getItem(index);
		// sanity check
		if (changedTask == null) {
			return false;
		}
		// description must be valid non-whitespace string
		if (description == null || description.trim().length() == 0) {
			return false;
		}
		changedTask.setDescription(description);
		changedTask.setDueDate(dueDate);
		changedTask.setPriority(priority);
		changedTask.setAlarm(alarm);
		return updateTaskInfoTool(changedTask);
	}
	
	/**
	 * Marks the task at the given index within the current task list as complete
	 * 
	 * @param index Index of task within current task list
	 * @return True if the task was marked completed successfully, false otherwise
	 */
	public boolean completeTask(int index) {
		Task completedTask = (Task)m_selectedList.getAdapter().getItem(index);
		// sanity check
		if (completedTask == null) {
			return false;
		}
		boolean informationRemoved = this.deleteTaskTool(completedTask);	// remove completed Task from all lists containing it
		boolean informationAdded = this.addTaskTool(completedTask, m_completedList);	// re-add Task to completed list only
		boolean alarmsDeleted = m_alarmHelper.deleteTask(completedTask);
		return informationRemoved && informationAdded && alarmsDeleted;
	}
	
	/**
	 * Deletes the task at the given index within the current task list
	 * 
	 * @param index Index of task within current task list
	 * @return True if the task was deleted successfully, false otherwise
	 */
	public boolean deleteTask(int index) {
		Task deletion = (Task)m_selectedList.getAdapter().getItem(index);
		// sanity check
		if (deletion == null) {
			return false;
		}
		boolean informationDeleted = deleteTaskTool(deletion);
		boolean alarmsDeleted = m_alarmHelper.deleteTask(deletion);
		return informationDeleted && alarmsDeleted;
	}
	
	/**
	 * Removes the task at the given index from the current list
	 * 
	 * @param index Index of task within current task list
	 * @return True if the task was successfully removed, false otherwise
	 */
	public boolean removeTaskFromList(int index) {
		Task removal = (Task)m_selectedList.getAdapter().getItem(index);
		// sanity check
		if (removal == null) {
			return false;
		}
		
		// cannot remove from "main" or "completed" task lists
		if (m_selectedList == m_mainList || m_selectedList == m_completedList) {
			throw new IllegalArgumentException("Cannot remove task from 'main' or 'completed' lists");
		}
		
		return removeTaskFromListTool(removal, m_selectedList);
	}
	
	// ---------- Task List Modification Methods ----------
	/**
	 * Creates a new task list with the given name
	 * 
	 * @param name Name for the new task list
	 * @param type Type of task list
	 * @return True if the task list was created successfully; false otherwise
	 */
	public boolean createTaskList(String name, TaskAdapter.Type type) {
		// name must be a valid, non-whitespace string
		if (name == null || name.trim().length() == 0) {
			return false;
		}
		// adapter type must be valid
		if (type == null) {
			return false;
		}
		TaskList newList = m_taskListFactory.buildTaskList(name, type);
		return addCustomListTool(newList);
	}
	
	/**
	 * Modifies the currently selected custom task list to have the given new name and type
	 * 
	 * @param newName New name the task list should have
	 * @param newAdapterType New type the task list should have
	 * @return True if the modification was successful, false otherwise
	 */
	public boolean modifyTaskList(String newName, TaskAdapter.Type newAdapterType) {
		// ensure new name is valid
		if (newName == null || newName.trim().length() == 0) {
			throw new IllegalArgumentException("New task list name must be valid, non-whitespace string");
		}
		// ensure new adapter type is valid
		if (newAdapterType == null) {
			throw new IllegalArgumentException("New task list category must be non-null");
		}
		
		m_selectedList.setName(newName);
		m_selectedList.getAdapter().setType(newAdapterType);
		return this.updateCustomListInfoTool(m_selectedList);
	}
	
	/**
	 * Deletes the currently selected task list
	 * 
	 * @return True if the task list was successfully deleted, false otherwise
	 */
	public boolean deleteTaskList() {
		// cannot delete "main" or "completed" task lists
		if (m_selectedList == m_mainList || m_selectedList == m_completedList) {
			throw new IllegalArgumentException("Cannot delete 'main' or 'completed' task lists");
		}
		
		return deleteCustomListTool(m_selectedList);
	}
	
	// ---------- UI Control Methods ----------
	/**
	 * Cycles to the TaskList left of the current TaskList being displayed
	 * List Arrangement: {"completed"  "main"  custom lists...}
	 */
	public void cycleLeft()	 {
		int numCustomLists = m_customLists.size();
		// wrap around to the far right if the current list is the completed TaskList
		if (m_selectedList == m_completedList) {
			if (numCustomLists > 0) {
				m_selectedList = m_customLists.get(numCustomLists - 1);
			}
			else {
				m_selectedList = m_mainList;
			}
		}
		// go left to the completed TaskList if the current TaskList is the completed list
		else if (m_selectedList == m_mainList) {
			m_selectedList = m_completedList;
		}
		// otherwise, the current list is a custom list
		else {
			// go to the main list if the current TaskList is the leftmost custom list
			if (m_selectedList == m_customLists.get(0)) {
				m_selectedList = m_mainList;
			}
			// otherwise, cycle to the custom TaskList left of the current one
			else {
				int position = m_customLists.indexOf(m_selectedList);
				m_selectedList = m_customLists.get(position - 1);
			}
		}
	}
	
	/**
	 * Cycles to the TaskList right of the current TaskList being displayed
	 * List Arrangement: {"completed"  "main"  custom lists...}
	 */
	public void cycleRight()	 {
		Log.d("POQTList", "Inside cycleRight()");
		System.out.println("CustomList size: " + m_customLists.size());
		System.out.println("Selected list: " + m_selectedList.getName());
		for (TaskList list : m_customLists) {
			System.out.println(list.getName() + "   " + list.getID());
		}
		
		
		int numCustomLists = m_customLists.size();
		// cycle right to main list if current list is the completed list
		if (m_selectedList == m_completedList) {
			m_selectedList = m_mainList;
		}
		// go right to the first custom TaskList if the current list is the completed List
		// wrap around to the main TaskList otherwise
		else if (m_selectedList == m_mainList) {
			if (numCustomLists > 0) {
				
				
				try{
					
					
					
				m_selectedList = m_customLists.get(0);
				
				
				
				}
				catch (IndexOutOfBoundsException exc) {
					Log.e("POQTList", "ERROR 1");
				}
				
				
				
				
			}
			else {
				m_selectedList = m_completedList;
			}
		}
		// otherwise, the current list is a custom list
		else {
			// go to the main list if the current TaskList is the rightmost custom list
			if (m_selectedList == m_customLists.get(numCustomLists - 1)) {
				System.out.println("Wrapping around when cycling right");
				m_selectedList = m_completedList;
			}
			// otherwise, cycle to the custom TaskList right of the current
			else {
				System.out.println("Cycling right in custom lists");
				int position = m_customLists.indexOf(m_selectedList);
				
				
				
				try {
				
				m_selectedList = m_customLists.get(position + 1);
				
				
				
				
				}
				catch (IndexOutOfBoundsException exc) {
					Log.e("POQTList", "ERROR 1");
				}
				
				
				
			}
		}
	}
	
	/**
	 * Opens TaskApparatus to free resources and save state
	 */
	public void open() {
		m_databaseApparatus.open();
	}
	
	/**
	 * Closes TaskApparatus to free resources and save state
	 */
	public void close() {
		m_databaseApparatus.close();
	}
	
	
	// ========== Private Task Toolkit ==========
	/**
	 * Adds given Task to given TaskList
	 * 
	 * @param list The TaskList to add the Task to
	 * @param addition The Task to add
	 * @return True if the Task was successfully added; false otherwise
	 */
	private boolean addTaskTool(Task addition, TaskList list) {
		TaskApparatus.ListCategory listStatus = findListCategory(list);
		// if destination list is "main", add to "main" only
		if (listStatus == ListCategory.MAIN) {
			boolean addedToDB = m_databaseApparatus.addTask(addition);
			boolean addedToList = this.addTaskToListTool(addition, m_mainList);
			return addedToDB && addedToList;
		}
		// if destination list is a custom one, add to "main" and destination list
		else if (listStatus == ListCategory.CUSTOM) {
			boolean addedToDB = m_databaseApparatus.addTask(addition);
			boolean addedToMain = this.addTaskToListTool(addition, m_mainList);
			boolean addedToCustom = this.addTaskToListTool(addition, list);
			return addedToDB && addedToMain && addedToCustom;
		}
		// if destination list is "completed", add to "completed" list
		else if (listStatus == ListCategory.COMPLETED) {
			boolean addedToDB = m_databaseApparatus.addTask(addition);
			boolean addedToList = this.addTaskToListTool(addition, m_completedList);
			return addedToDB && addedToList;
		}
		
		// otherwise, the list is invalid
		return false;
	}
	
	/**
	 * Commits given Task's changes to database and alarm manager
	 * 
	 * @param modification Task whose information needs saving
	 * @return True if the update was successful, false otherwise
	 */
	private boolean updateTaskInfoTool(Task modification) {
		// ensure valid Task
		if (modification == null) {
			return false;
		}
		
		boolean informationUpdated = m_databaseApparatus.updateTask(modification);
		boolean alarmsUpdated = m_alarmHelper.updateTask(modification);
		return informationUpdated && alarmsUpdated;
	}
	
	/**
	 * Permanently deletes the given Task from all lists
	 * 
	 * @param elimination Task to be deleted
	 * @return True if the Task was successfuly deleted, false otherwise
	 */
	private boolean deleteTaskTool(Task elimination) {
		// delete given Task from all TaskLists that may have contained it
		m_mainList.getAdapter().remove(elimination);
		m_completedList.getAdapter().remove(elimination);
		for (TaskList customList : m_customLists) {
			customList.getAdapter().remove(elimination);
		}
		return m_databaseApparatus.deleteTask(elimination);
	}
	
	/**
	 * Adds the given Task to the given TaskList
	 * 
	 * @param addition Task to add
	 * @param list TaskList to be added to
	 * @return True if the Task was successfully added to the list; false otherwise
	 */
	private boolean addTaskToListTool(Task addition, TaskList list) {
		// ensure valid list
		TaskApparatus.ListCategory listCategory = findListCategory(list);
		if (listCategory == null) {
			return false;
		}
		
		list.getAdapter().add(addition);
		return m_databaseApparatus.addTaskToList(addition, list);
	}
	
	/**
	 * Removes the given Task from the given TaskList
	 * 
	 * @param removal Task to be removed
	 * @param list TaskList to remove Task from
	 * @return True if the task was successfully removed from the list; false otherwise
	 */
	private boolean removeTaskFromListTool(Task removal, TaskList list) {
		// ensure valid list
		TaskApparatus.ListCategory listCategory = findListCategory(list);
		if (listCategory == null) {
			return false;
		}
		
		
		// !!! DEBUGGING !!!
		Toast toast = Toast.makeText(m_context, "From: " + list.getName(), Toast.LENGTH_SHORT);
		toast.show();
		
		
		
		list.getAdapter().remove(removal);
		return m_databaseApparatus.removeTaskFromList(removal, list);
	}
	
	/**
	 * Adds given TaskList to custom lists
	 * 
	 * @param addition TaskList to add to custom lists
	 * @return True if the list was successfully added; false otherwise
	 */
	private boolean addCustomListTool(TaskList addition) {
		// do not add if list already exists in custom or is "main" or "completed" list
		TaskApparatus.ListCategory listCategory = findListCategory(addition);
		if (listCategory != null) {
			return false;
		}
		
		m_customLists.add(addition);
		m_selectedList = addition;
		return m_databaseApparatus.addTaskList(addition);
	}
	
	/**
	 * Updates the TaskList's information within the database
	 * 
	 * @param modification TaskList whose information needs updating
	 * @return True if the update was successful, false otherwise
	 */
	private boolean updateCustomListInfoTool(TaskList modification) {
		// ensure valid list
		TaskApparatus.ListCategory listCategory = findListCategory(modification);
		if (modification == null || listCategory != ListCategory.CUSTOM) {
			return false;
		}
		
		return m_databaseApparatus.updateTaskList(modification);
	}
	
	/**
	 * Deletes the given custom TaskList
	 * 
	 * @param deletion TaskList to remove from custom lists
	 * @return True if the list was successfully removed; false otherwise
	 */
	private boolean deleteCustomListTool(TaskList deletion) {
		// ensure deletion is a custom list
		TaskApparatus.ListCategory listCategory = findListCategory(deletion);
		if (listCategory != ListCategory.CUSTOM) {
			return false;
		}
		
		// if deletion is currently selected list, switch selected list before deleting
		if (deletion == m_selectedList) {
			// if last custom list, default to main task list
			if (m_customLists.size() == 1) {
				m_selectedList = m_mainList;
			}
			else {
				// otherwise, select appropriate custom list
				int deletionIndex = findCustomListIndex(deletion);
				if (deletionIndex == m_customLists.size() - 1) {
					m_selectedList = m_customLists.get(deletionIndex - 1);
				}
				else {
					m_selectedList = m_customLists.get(deletionIndex + 1);
				}
			}
		}
		
		m_customLists.remove(deletion);
		m_databaseApparatus.deleteTaskList(deletion);
		return true;
	}
	
	/**
	 * Finds the given list's category
	 * 
	 * @param list TaskList to check status for
	 * @return Constant representing list's category (null if the list is not in the TaskApparatus)
	 */
	private TaskApparatus.ListCategory findListCategory(TaskList list) {
		if (list == m_mainList) {
			return ListCategory.MAIN;
		}
		else if (list == m_completedList) {
			return ListCategory.COMPLETED;
		}
		else if (m_customLists.contains(list)) {
			return ListCategory.CUSTOM;
		}
		else {
			return null;
		}
	}
	
	/**
	 * Finds the given list's index in the list of custom TaskLists
	 * 
	 * @param list TaskList to find position for
	 * @return Index of given list or -1 if list not found
	 */
	private int findCustomListIndex(TaskList list) {
		// ensure list category is custom
		if (findListCategory(list) != ListCategory.CUSTOM) {
			return -1;
		}
		
		for (int i = 0; i < m_customLists.size(); i++) {
			TaskList compareList = m_customLists.get(i);
			if (compareList.getID() == list.getID()) {
				return i;
			}
		}
		
		return -1;
	}
	
	
	// ========== Getter Methods ==========
	/**
	 * Gets the TaskAdapter for the currently selected task list.
	 * NOTE: To preserve encapsulation, do not modify the return value
	 * 
	 * @return The list's TaskAdapter
	 */
	public final TaskAdapter getSelectedAdapter() {
		return m_selectedList.getAdapter();
	}
	
	/**
	 * Get the currently selected list's title
	 * 
	 * @return The task list's title
	 */
	public String getSelectedTitle() {
		return m_selectedList.getName();
	}
	
	/**
	 * Gets the currently selected task list's category
	 * 
	 * @return The task list's category
	 */
	public TaskApparatus.ListCategory getListCategory() {
		return findListCategory(m_selectedList);
	}
}
