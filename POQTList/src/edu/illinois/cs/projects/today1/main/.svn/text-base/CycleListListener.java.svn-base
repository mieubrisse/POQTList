package edu.illinois.cs.projects.today1.main;

import edu.illinois.cs.projects.today1.POQTListConstants;
import edu.illinois.cs.projects.today1.task.TaskApparatus;
import android.util.Log;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;

/**
 * Listener to cycle the ListView in POQTList showing the TaskLists on fling left or right
 * Heavily based off code on CodeShogun about implementing fling gesture detectors in Android
 * 
 * @author Kevin Today
 */
public class CycleListListener extends SimpleOnGestureListener {
	// ========== Member Variables ==========
	private POQTListActivity m_activity;
	private TaskApparatus m_apparatus;	// activity to modify with the fling gesture
	
	/**
	 * Instantiates a new CycleListListener hooked up to the given TaskApparatus
	 * 
	 * @param apparatus TaskApparatus to hook the CycleListListener to
	 */
	public CycleListListener(POQTListActivity activity, TaskApparatus apparatus) {
		// sanity check
		if (apparatus == null || activity == null) {
			throw new IllegalArgumentException("CycleListListener apparatus and activity must be non-null");
		}
		
		m_apparatus = apparatus;
		m_activity = activity;
	}
	
	/**
	 * If a fling gesture meets defined fling constants, cycle the given POQTListActivity's task list display in the proper direction
	 * 
	 * @param event1 Event that started the fling gesture
	 * @param event2 Event that ended the fling gesture
	 * @param velocityX X-axis velocity of the fling gesture
	 * @param velocityY Y-axis velocity of the fling gesture
	 * @return If the gesture was consumed, return true; otherwise, false
	 */
	@Override
	public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
		// right-to-left fling, signaling a cycle to the right
		if(event1.getX() - event2.getX() > POQTListConstants.MIN_FLING_DISTANCE && Math.abs(velocityX) > POQTListConstants.MIN_FLING_VELOCITY) {
			System.out.println("STARTING CYCLE RIGHT");
			Log.d("POQTList", "STARTING CYCLE RIGHT");
			m_apparatus.cycleRight();
			Log.d("POQTList", "CYCLE COMPLETED IN CODE");
			m_activity.refreshDisplay();
			Log.d("POQTList", "FINISHED CYCLE RIGHT");
			return true;
		} 
		// left-to-right fling, signaling a cycle to the left
		else if (event2.getX() - event1.getX() > POQTListConstants.MIN_FLING_DISTANCE && Math.abs(velocityX) > POQTListConstants.MIN_FLING_VELOCITY) {
			m_apparatus.cycleLeft();
			m_activity.refreshDisplay();
			return true;
		}
		return false;
	}
}
