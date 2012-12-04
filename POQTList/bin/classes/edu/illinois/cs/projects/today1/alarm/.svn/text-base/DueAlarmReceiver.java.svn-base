package edu.illinois.cs.projects.today1.alarm;

import edu.illinois.cs.projects.today1.POQTListConstants;
import edu.illinois.cs.projects.today1.alarm.AlarmHelper.AlarmType;
import edu.illinois.cs.projects.today1.main.POQTListActivity;
import edu.illinois.cs.projects.today1.task.Task;
import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Receiver for task alarms triggered on task being due
 * 
 * @author Kevin Today
 *
 */
public class DueAlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// retrieve task information from Intent
		Bundle alarmInformation = intent.getExtras();
		long taskID = alarmInformation.getLong(POQTListConstants.ALARM_INFO_KEY_TASK_ID);
		String taskDescription = alarmInformation.getString(POQTListConstants.ALARM_INFO_KEY_TASK_DESCRIPTION);
		
		// retrieve alarm information from Intent
		int alarmTypeOrdinal = alarmInformation.getInt(POQTListConstants.ALARM_INFO_KEY_TYPE_ORDINAL);
		AlarmHelper.AlarmType alarmType = AlarmHelper.AlarmType.findAlarmType(alarmTypeOrdinal);
		
		// create intent to open POQTList when the user clicks the notification
		Intent launchApp = new Intent(context, POQTListActivity.class);
		// !!! MAY NEED TO SET FLAGS FOR INTENT TO SWITCH TO CURRENT INSTANCE OF POQTLIST !!!
		PendingIntent pendingLaunchApp = PendingIntent.getActivity(context, 0, launchApp, PendingIntent.FLAG_CANCEL_CURRENT);
		
		// create notification
		NotificationManager notifyManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.btn_radio, taskDescription, System.currentTimeMillis());	// !!!! HAS NO ICON! FIX THIS !!!
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		// make pretty text display for notification based on alarm type
		String notifyText = "Due";
		notification.setLatestEventInfo(context, taskDescription, notifyText, pendingLaunchApp);
		
		// !!!! USE USER PREFERENCES ON WHETHER LED SHOULD FLASH, SHOULD PLAY, ETC !!!
		// !!! FOR NOW, FLASH LED AND VIBRATE !!!
		long[] vibratePattern = {0, 300, 200, 300};		// no wait, pulse twice
		notification.vibrate = vibratePattern;
		notification.ledOnMS = 100;
		notification.ledOffMS = 3000;
		notification.defaults |= Notification.DEFAULT_SOUND;
		notifyManager.notify((int)taskID, notification);
	}

}
