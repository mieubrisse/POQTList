package edu.illinois.cs.projects.today1.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import edu.illinois.cs.projects.today1.POQTListConstants;
import edu.illinois.cs.projects.today1.R;
import edu.illinois.cs.projects.today1.task.TaskAdapter;

/**
 * Activity for entering and modifying task list information
 * @author Kevin Today
 *
 */
public class TaskListActivity extends Activity {
	// ========== Constants ==========
	private static enum DialogType {
		INVALID_NAME;
	}
	
	
	// ========== Member Variables ==========
	private DialogFactory m_dialogFactory;
	
	
	// ========== Overriden Activity Functions ==========
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tasklistactivity);
		
		// set Activity title based on purpose
		int requestCodeOrdinal = getIntent().getIntExtra(POQTListConstants.ACTIVITY_REQUEST_CODE, POQTListActivity.RequestCode.ADD_TASK_LIST.ordinal());
		POQTListActivity.RequestCode requestCode = POQTListActivity.RequestCode.findRequestCode(requestCodeOrdinal);
		if (requestCode == POQTListActivity.RequestCode.ADD_TASK_LIST) {
			setTitle(R.string.activity_add_tasklist);
		}
		else if (requestCode == POQTListActivity.RequestCode.EDIT_TASK_LIST) {
			setTitle(R.string.activity_edit_tasklist);
		}
		
		m_dialogFactory = new DialogFactory(this);
		
		// set up spinner controlling type selection
		ArrayAdapter typeAdapter = ArrayAdapter.createFromResource(this, R.array.tasklist_types, android.R.layout.simple_spinner_item);
		typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner typeSpinner = (Spinner)findViewById(R.id.spinner_tasklist_type);
		typeSpinner.setAdapter(typeAdapter);
		
		// Activity is being created from main Activity; set up fields for adding Task List
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
	 * Creates a Dialog for the given request code
	 * 
	 * @param id Request code for dialog
	 * @return Created Dialog
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == DialogType.INVALID_NAME.ordinal()) {
			return m_dialogFactory.makeWarningDlg(R.string.error_tasklist_empty_name);
		}
		else {
			return super.onCreateDialog(id);
		}
	}
	
	
	// ========== GUI Functions ==========
	/**
	 * Called when the user presses the OK button
	 * @param view Button user pressed
	 */
	public void onOKPress(View view) {
		/*	Retrieve user-inputted information	*/
		// sanitize description input
		EditText nameBox = (EditText)findViewById(R.id.text_tasklist_name);
		String newName = nameBox.getText().toString();
		if (newName == null || newName.trim().length() == 0) {
			showDialog(DialogType.INVALID_NAME.ordinal());
			return;
		}
		// retrieve type input
		Spinner typeSpinner = (Spinner)findViewById(R.id.spinner_tasklist_type);
		int typeIndex = typeSpinner.getSelectedItemPosition();
		
		/*	Package up user-inputted information	*/
		Bundle taskListInfo = new Bundle();
		taskListInfo.putString(POQTListConstants.TASKLIST_INFO_KEY_NAME, newName);	// description
		// encode type
		taskListInfo.putInt(POQTListConstants.TASKLIST_INFO_KEY_ADAPTER_TYPE_ORDINAL, typeIndex);
		
		/*	Pass back packaged result	*/
		Intent returnIntent = new Intent();
		returnIntent.putExtras(taskListInfo);
		setResult(Activity.RESULT_OK, returnIntent);
		finish();
	}
	
	/**
	 * Called when the user presses the Cancel button
	 * @param view Button user pressed
	 */
	public void onCancelPress(View view) {
		setResult(Activity.RESULT_CANCELED);
		finish();
	}

	
	
	// ========== Helper Functions ==========
	/**
	 * Attempts to fill task list data passed to the Activity in to the Activity's fields.
	 * Otherwise, fields are filled in with default values
	 */
	private void loadDataToFields() {
		// fetch fields to fill
		EditText nameBox = (EditText)findViewById(R.id.text_tasklist_name);
		Spinner typeSpinner = (Spinner)findViewById(R.id.spinner_tasklist_type);
		typeSpinner.setSelection(0);
		
		// if TaskList data was passed in, fill fields with that
		Bundle listInfo = getIntent().getExtras();
		if (listInfo == null) {
			return;
		}
		
		// attempt to fill "Name" field
		String currentName = listInfo.getString(POQTListConstants.TASKLIST_INFO_KEY_NAME);
		if (currentName != null) {
			nameBox.setText(currentName);
		}
		
		// attempt to fill "Type" dropdown
		int adapterTypeOrdinal = listInfo.getInt(POQTListConstants.TASKLIST_INFO_KEY_ADAPTER_TYPE_ORDINAL);
		TaskAdapter.Type adapterType = TaskAdapter.Type.findType(adapterTypeOrdinal);
		if (adapterType != null) {
			typeSpinner.setSelection(adapterTypeOrdinal);
		}
	}
}
