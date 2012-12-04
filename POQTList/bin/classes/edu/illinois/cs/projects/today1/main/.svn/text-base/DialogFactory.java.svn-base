package edu.illinois.cs.projects.today1.main;

import java.util.Calendar;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.util.Log;
import edu.illinois.cs.projects.today1.POQTListConstants;
import edu.illinois.cs.projects.today1.R;

/**
 * Class for creating various Dialog objects
 * 
 * @author Kevin Today
 *
 */
public class DialogFactory {
	// ========== Member Variables ==========
	private Activity m_activity;
	
	
	// ========== Member Variables ==========
	/**
	 * Instantiates a new DialogLibrary object
	 * 
	 * @param activity Handle to activity creating Dialogs
	 */
	public DialogFactory(Activity activity) {
		if (activity == null) {
			throw new IllegalArgumentException("Cannot instantiate DialogLibrary with null context");
		}
		
		m_activity = activity;
	}
	

	// ========== Member Methods ==========
	/**
	 * Creates and returns an AlertDialog warning the user about invalid Task description input
	 * 
	 * @return AlertDialog showing a warning about invalid Task description input
	 */
	public AlertDialog makeWarningDlg(int warningResource) {
		String warning = m_activity.getResources().getString(warningResource);
		
		// build up AlertDialog
		AlertDialog.Builder warningBuilder = new AlertDialog.Builder(m_activity);
		warningBuilder.setMessage(warning);
		
		// OK button to dismiss error
		warningBuilder.setPositiveButton(R.string.ok_string, new DialogInterface.OnClickListener() {
			
			/**
			 * Dismisses dialog
			 */
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		return warningBuilder.create();
	}
	
	
	/**
	 * Creates and returns a DatePickerDialog for picking the Task's due date
	 * 
	 * @return DatePickerDialog to facilitate easy date choosing
	 */
	public DatePickerDialog makeDatePickerDlg(Calendar defaultDate, OnDateSetListener listener) {
		// ease-of-use decoding
		int defaultYear = defaultDate.get(Calendar.YEAR);
		int defaultMonth = defaultDate.get(Calendar.MONTH);
		int defaultDay = defaultDate.get(Calendar.DAY_OF_MONTH);
		
		DatePickerDialog datePickerDialog = new DatePickerDialog(m_activity, listener, defaultYear, defaultMonth, defaultDay);
		return datePickerDialog;
	}
	
	/**
	 * Creates and returns a TimePickerDialog for picking the Task's due time
	 * 
	 * @return TimePickerDialog to facilitate easy time choosing
	 */
	public TimePickerDialog makeTimePickerDlg(Calendar defaultTime, OnTimeSetListener listener) {
		// ease-of-use decoding
		int defaultHour = defaultTime.get(Calendar.HOUR_OF_DAY);
		int defaultMinute = defaultTime.get(Calendar.MINUTE);
		
		TimePickerDialog timePickerDialog = new TimePickerDialog(m_activity, listener, defaultHour, defaultMinute, true);
		return timePickerDialog;
	}
	
	//public AlertDialog makeCustomListSelectionDlg()
}
