package edu.illinois.cs.projects.today1.alarm;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import edu.illinois.cs.projects.today1.POQTListConstants;
import edu.illinois.cs.projects.today1.database.TaskDBApparatus;
import edu.illinois.cs.projects.today1.task.Task;

/**
 * Receiver for loading alarms on device boot
 * @author Kevin Today
 *
 */
public class LoadAlarmsReceiver extends BroadcastReceiver {
	/**
	 * Called when the "done booting" signal is received
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.w(POQTListConstants.LOG_TAG, "Loading alarms into AlarmManager from database");
		
		// get all Tasks with alarms in the future from the database
		TaskDBApparatus DBApparatus = new TaskDBApparatus(context);
		List<Task> alarmTasks = DBApparatus.getAlarmTasks();
		
		// prepare AlarmApparatus to handle task alarm management
		AlarmHelper alarmHelper = new AlarmHelper(context);

		// load alarms into AlarmManager
		for (Task task : alarmTasks) {
			alarmHelper.addTask(task);
		}
		
		Log.w(POQTListConstants.LOG_TAG, "All alarms successfully loaded into AlarmManager");
	}

}
