package edu.illinois.cs.projects.today1.main;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import edu.illinois.cs.projects.today1.POQTListConstants;
import edu.illinois.cs.projects.today1.R;

/**
 * Activity for entering and modifying task information
 * @author Kevin Today
 *
 */
public class TaskActivity extends Activity {
	// ========== Constants ==========
	private static enum DialogType {
		TASK_INVALID_DESCRIPTION,
		TASK_DATE_PICKER,
		TASK_TIME_PICKER;
	}
	private final String DUE_DATE_FORMAT = "MMMM dd, yyyy";
	private final String DUE_TIME_FORMAT = "hh:mmaa";

	// ========== Member Variables ==========
	private DialogFactory m_dialogFactory;
	private Calendar m_dueDate;		// object representing time displayed on due date & time buttons

	
	// ========== Overriden Activity Functions ==========
	/**
	 * Initializes activity's components. If task information was provided when
	 * 	Activity was created, fill components with given information
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_dialogFactory = new DialogFactory(this);
		setContentView(R.layout.taskactivity);
		
		// set Activity title based on purpose
		int requestCodeOrdinal = getIntent().getIntExtra(POQTListConstants.ACTIVITY_REQUEST_CODE, POQTListActivity.RequestCode.ADD_TASK.ordinal());
		POQTListActivity.RequestCode requestCode = POQTListActivity.RequestCode.findRequestCode(requestCodeOrdinal);
		if (requestCode == POQTListActivity.RequestCode.ADD_TASK) {
			setTitle(R.string.activity_add_task);
		}
		else if (requestCode == POQTListActivity.RequestCode.EDIT_TASK) {
			setTitle(R.string.activity_edit_task);
		}
		
		// set up spinner controlling priority selection
		Spinner prioritySpinner = (Spinner)findViewById(R.id.spinner_task_priority);
		ArrayAdapter priorityAdapter = ArrayAdapter.createFromResource(this, R.array.task_priorities, android.R.layout.simple_spinner_item);
		priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		prioritySpinner.setAdapter(priorityAdapter);
		
		// set up spinner controlling alarm selection
		Spinner alarmSpinner = (Spinner)findViewById(R.id.spinner_task_alarm);
		ArrayAdapter alarmAdapter = ArrayAdapter.createFromResource(this, R.array.task_alarms, android.R.layout.simple_spinner_item);
		alarmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		alarmSpinner.setAdapter(alarmAdapter);
		
		// Activity is being created from main Activity; set up 
		if (savedInstanceState == null) {
			// if given task data, loads it into fields. otherwise, sets fields to default
			loadDataToFields();
		}
		// Activity is being created from a screen rotation; re-load values for fields
		else {
			// !!! DO LOADING HERE; PROBABLY NEED TO IMPLEMENT OTHER CALLBACKS FOR ACTIVITY !!!
			
			
			// !!! FOR THE MOMENT, USE REGULAR LOADING !!!!
			loadDataToFields();
		}
	}
	
	/**
	 * Called when a dialog is created for the first time
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		// process warning Dialog requests
		if (id == DialogType.TASK_INVALID_DESCRIPTION.ordinal()) {
			return m_dialogFactory.makeWarningDlg(R.string.error_task_empty_description);
		}
		// process DatePickerDialog requests
		else if (id == DialogType.TASK_DATE_PICKER.ordinal()) {
			return m_dialogFactory.makeDatePickerDlg(m_dueDate, new DatePickerDialog.OnDateSetListener() {
				
				/**
				 * Called on picking a date. Updates Calendar member variable and changes button text
				 */
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					// update new due date while leaving due time alone
					m_dueDate.set(year, monthOfYear, dayOfMonth);
					
					// update button text
					Button dueDate = (Button)findViewById(R.id.button_task_duedate);
					dueDate.setText(DateFormat.format(DUE_DATE_FORMAT, m_dueDate));
				}
			});
		}
		// process TimePickerDialog requests
		else if (id == DialogType.TASK_TIME_PICKER.ordinal()) {
			return m_dialogFactory.makeTimePickerDlg(m_dueDate, new TimePickerDialog.OnTimeSetListener() {
				
				/**
				 * Called on setting the time. Updates stored Calendar and Button text
				 */
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					// update new due time while preserving due date
					m_dueDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
					m_dueDate.set(Calendar.MINUTE, minute);
					
					// update button text
					Button dueTime = (Button)findViewById(R.id.button_task_duetime);
					dueTime.setText(DateFormat.format(DUE_TIME_FORMAT, m_dueDate));
				}
			});
		}
		// send all other requests to parent's method
		else {
			return super.onCreateDialog(id);
		}
	}
	
	
	// ========== GUI Functions ==========
	/**
	 * Brings up a DatePickerDialog when the user presses the Button with the due date on it
	 * 
	 * @param view The View (Button) that called the method
	 */
	public void onDueDatePress(View view) {
		showDialog(DialogType.TASK_DATE_PICKER.ordinal());
	}
	
	/**
	 * Brings up a TimePickerDialog when the user presses the Button with the due time on it
	 * 
	 * @param view The View (Button) that called the method
	 */
	public void onDueTimePress(View view) {
		showDialog(DialogType.TASK_TIME_PICKER.ordinal());
	}
	
	/**
	 * Called when the user presses the "OK" button
	 * Error-checks user input, finishes the Activity, and sends a packaged Task object back to the main Activity
	 * 
	 * @param view The View (Button) that called the function
	 */
	public void onOKPress(View view) {
		/*	Retrieve user-inputted information	*/
		// sanitize description input
		EditText descriptionText = (EditText)findViewById(R.id.text_task_description);
		String newDescription = descriptionText.getText().toString();
		if (newDescription == null || newDescription.trim().length() == 0) {
			showDialog(DialogType.TASK_INVALID_DESCRIPTION.ordinal());
			return;
		}
		// retrieve priority input
		Spinner prioritySpinner = (Spinner)findViewById(R.id.spinner_task_priority);
		int priorityIndex = prioritySpinner.getSelectedItemPosition();
		// retrieve alarm input
		Spinner alarmSpinner = (Spinner)findViewById(R.id.spinner_task_alarm);
		int alarmIndex = alarmSpinner.getSelectedItemPosition();
		
		/*	Package up user-inputted information	*/
		Bundle taskInfo = new Bundle();
		taskInfo.putString(POQTListConstants.TASK_INFO_KEY_DESCRIPTION, newDescription);	// description
		// pack up due date information
		CheckBox hasDueDate = (CheckBox)findViewById(R.id.checkbox_duedate_enabled);
		if (hasDueDate.isChecked()) {
			taskInfo.putAll(packDueDate(m_dueDate));
		}
		else {
			taskInfo.putAll(packDueDate(null));
		}
		// encode priority
		taskInfo.putInt(POQTListConstants.TASK_INFO_KEY_PRIORITY_ORDINAL, priorityIndex);
		// encode alarm
		taskInfo.putInt(POQTListConstants.TASK_INFO_KEY_ALARM_ORDINAL, alarmIndex);
		
		/*	Pass back packaged result	*/
		Intent returnIntent = new Intent();
		returnIntent.putExtras(taskInfo);
		setResult(Activity.RESULT_OK, returnIntent);
		finish();
	}
	
	/**
	 * Called when the user presses the "Cancel" button
	 * Closes the activity with no data returned
	 */
	public void onCancelPress(View view) {
		setResult(Activity.RESULT_CANCELED);
		finish();
	}
	
	/**
	 * Enables or disables GUI elements when user clicks checkbox denoting whether task has
	 * a due date or not.
	 */
	public void onChangeCheckbox(View view) {
		CheckBox checkbox = (CheckBox)view;
		if (checkbox.isChecked()) {
			setGUIState(true);
		}
		else {
			setGUIState(false);
		}
	}
	
	
	// ========== Helper Functions ==========
	/**
	 * Helps in initializing GUI states to match task data
	 * 	Uses due date within class
	 * 
	 * @param dueDateTime Calendar object to read data from.
	 * 	- If null, the task will be assumed to have no due date
	 *  - If not null, appropriate GUI elements will be set accordingly
	 */
	private void setGUIState(boolean hasDueDate) {
		Button dueDateButton = (Button)findViewById(R.id.button_task_duedate);
		Button dueTimeButton = (Button)findViewById(R.id.button_task_duetime);
		CheckBox hasDueDateBox = (CheckBox)findViewById(R.id.checkbox_duedate_enabled);
		Spinner alarmSpinner = (Spinner)findViewById(R.id.spinner_task_alarm);
		
		// set GUI states appropriately
		if (hasDueDate) {
			hasDueDateBox.setChecked(true);
			dueDateButton.setEnabled(true);
			dueTimeButton.setEnabled(true);
			alarmSpinner.setEnabled(true);
		}
		else {
			hasDueDateBox.setChecked(false);
			dueDateButton.setEnabled(false);
			dueTimeButton.setEnabled(false);
			alarmSpinner.setEnabled(false);
		}
		
		dueDateButton.setText(DateFormat.format(DUE_DATE_FORMAT, m_dueDate));
		dueTimeButton.setText(DateFormat.format(DUE_TIME_FORMAT, m_dueDate));
	}
	
	/**
	 * Attempts to fill task data passed to the Activity in to the Activity's fields.
	 * Otherwise, fields are filled in with default values
	 */
	private void loadDataToFields() {
		// fetch fields to fill
		EditText descriptionBox = (EditText)findViewById(R.id.text_task_description);
		Spinner prioritySpinner = (Spinner)findViewById(R.id.spinner_task_priority);
		Spinner alarmSpinner = (Spinner)findViewById(R.id.spinner_task_alarm);
		
		// get defaults that might be used
		SharedPreferences preferences = getSharedPreferences(POQTListConstants.PREFERENCES_FILEPATH, MODE_PRIVATE);
		String defaultDescription = preferences.getString(POQTListConstants.PREF_KEY_NEW_TASK_DESCRIPTION, 
				POQTListConstants.DEFAULT_NEW_TASK_DESCRIPTION);
		int defaultPrioritySelection = preferences.getInt(POQTListConstants.PREF_KEY_NEW_TASK_PRIORITY_ORDINAL, 
				POQTListConstants.DEFAULT_NEW_TASK_PRIORITY.ordinal());
		int defaultAlarmSelection = preferences.getInt(POQTListConstants.PREF_KEY_NEW_TASK_ALARM_ORDINAL, 
				POQTListConstants.DEFAULT_NEW_TASK_ALARM.ordinal());
		
		Bundle taskData = getIntent().getExtras();
		// load defaults if no task data was given
		if (taskData == null) {
			// default due date to one day from current day and give task no due date
			m_dueDate = Calendar.getInstance();
			m_dueDate.add(Calendar.DAY_OF_MONTH, 1);
			setGUIState(false);
			
			// set spinners to default positions
			prioritySpinner.setSelection(defaultPrioritySelection);
			alarmSpinner.setSelection(defaultAlarmSelection);
			return;
		}
		
		// fill in description
		String currentDescription = taskData.getString(POQTListConstants.TASK_INFO_KEY_DESCRIPTION);
		if (currentDescription != null) {
			descriptionBox.setText(currentDescription);
		}
		else {
			descriptionBox.setText(defaultDescription);
		}
		
		// fill in due date & time elements from bundle if they exist
		Calendar currentDueDate = unpackDueDate(taskData);
		if (currentDueDate != null) {
			m_dueDate = currentDueDate;
			setGUIState(true);
		}
		else {
			m_dueDate = Calendar.getInstance();
			m_dueDate.add(Calendar.DAY_OF_MONTH, 1);
			setGUIState(false);
		}
		
		// fill in priority element
		// !!!! RELIES ON DEPENDENCE BETWEEN ORDERING OR PRIORITY ORDINAL AND TEXT WITHIN SPINNER !!!
		// !!! THIS IS BAD; CHANGE THIS !!!
		int currentPriorityOrdinal = taskData.getInt(POQTListConstants.TASK_INFO_KEY_PRIORITY_ORDINAL, defaultPrioritySelection);
		prioritySpinner.setSelection(currentPriorityOrdinal);
		
		// fill in alarm element
		// !!!! RELIES ON DEPENDENCE BETWEEN ORDERING OF ALARM ORDINAL AND TEXT WITHIN SPINNER !!!
		// !!! THIS IS BAD; CHANGE THIS !!!
		int currentAlarmOrdinal = taskData.getInt(POQTListConstants.TASK_INFO_KEY_ALARM_ORDINAL, defaultAlarmSelection);
		alarmSpinner.setSelection(currentAlarmOrdinal);
	}
	
	/**
	 * Reassembles a Calendar object representing a task's due date from a Bundle
	 * 
	 * @param source Bundle containing mappings representing the due date's components
	 * @return Calendar representing the due date if it could be successfully constructed; null otherwise
	 */
	public static Calendar unpackDueDate(Bundle source) {
		// ensure bundle is supposed to have due date components
		boolean hasDueDate = source.getBoolean(POQTListConstants.TASK_INFO_KEY_HAS_DUE_DATE, false);
		if (!hasDueDate) {
			return null;
		}
		
		// ensure the proper components exist
		boolean hasAllComponents = source.containsKey(POQTListConstants.TASK_INFO_KEY_DUE_DAY)
			|| source.containsKey(POQTListConstants.TASK_INFO_KEY_DUE_MONTH)
			|| source.containsKey(POQTListConstants.TASK_INFO_KEY_DUE_YEAR)
			|| source.containsKey(POQTListConstants.TASK_INFO_KEY_DUE_HOUR)
			|| source.containsKey(POQTListConstants.TASK_INFO_KEY_DUE_MINUTE);
		if (!hasAllComponents) {
			return null;
		}
		
		// build components into return object
		Calendar dueDate = new GregorianCalendar();
		int day = source.getInt(POQTListConstants.TASK_INFO_KEY_DUE_DAY);
		int month = source.getInt(POQTListConstants.TASK_INFO_KEY_DUE_MONTH);
		int year = source.getInt(POQTListConstants.TASK_INFO_KEY_DUE_YEAR);
		int hour = source.getInt(POQTListConstants.TASK_INFO_KEY_DUE_HOUR);
		int minute = source.getInt(POQTListConstants.TASK_INFO_KEY_DUE_MINUTE);
		dueDate.set(year, month, day, hour, minute);
		
		return dueDate;
	}
	
	/**
	 * Packs the given Calendar due date into a Bundle
	 * 
	 * @param dueDate Calendar object representing the due date, or null to signify the task has no due date
	 * @return Bundle containing the due date's components
	 */
	public static Bundle packDueDate(Calendar dueDate) {
		Bundle target = new Bundle();
		// the task has a due date, pack all its components up
		if (dueDate != null) {
			int currentDay = dueDate.get(Calendar.DAY_OF_MONTH);
			int currentMonth = dueDate.get(Calendar.MONTH);
			int currentYear = dueDate.get(Calendar.YEAR);
			int currentHour = dueDate.get(Calendar.HOUR_OF_DAY);
			int currentMinute = dueDate.get(Calendar.MINUTE);
			
			target.putBoolean(POQTListConstants.TASK_INFO_KEY_HAS_DUE_DATE, true);
			target.putInt(POQTListConstants.TASK_INFO_KEY_DUE_DAY, currentDay);
			target.putInt(POQTListConstants.TASK_INFO_KEY_DUE_MONTH, currentMonth);
			target.putInt(POQTListConstants.TASK_INFO_KEY_DUE_YEAR, currentYear);
			target.putInt(POQTListConstants.TASK_INFO_KEY_DUE_HOUR, currentHour);
			target.putInt(POQTListConstants.TASK_INFO_KEY_DUE_MINUTE, currentMinute);
		}
		// otherwise, only set flag indicating there is no due date
		else {
			target.putBoolean(POQTListConstants.TASK_INFO_KEY_HAS_DUE_DATE, false);
		}
		
		return target;
	}
}
