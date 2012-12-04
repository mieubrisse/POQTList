package edu.illinois.cs.projects.today1.task;

import java.util.Comparator;

import android.content.Context;

/**
 * Comparator to compare two Task objects and assign priority based on due date
 * 
 * @author Kevin Today
 *
 */
public class TaskComparator implements Comparator<Task> {
	// ========== Member Variables ==========
	private final Context m_context;		// context comparator is being used in

	
	// ========== Constructors ==========
	/**
	 * Instantiates a new TaskComparator object within the given context
	 * 
	 * @param context Context comparator is being used in
	 */
	public TaskComparator(Context context) {
		if (context == null) {
			throw new IllegalArgumentException("TaskComparator context cannot be null");
		}
		
		m_context = context;
	}
	
	// ========== Public Functions ==========
	/**
	 * Compares two Task objects by evaluating their time till due date and priority
	 * Tasks due sooner should be counted as lesser, to put them higher in the queue
	 * 
	 * @param task_1 The Task being compared
	 * @param task_2 The Task to compare to
	 * @return 
	 * 	If task_1 is due sooner than task_2, 1 is returned. 
	 * 	If task_1 is due later than task_2, -1 is returned.
	 * 	If task_1 and task_2 are due at the same time, 0 is returned.
	 */
	@Override
	public int compare(Task task1, Task task2) {
		final long NUM_MILLISEC_IN_MIN = 60000;	// constant for number of milliseconds in a minute
		
		int urgencyScore1 = task1.findUrgencyScore(m_context);
		int urgencyScore2 = task2.findUrgencyScore(m_context);
		
		// in the case of scores where at least one is not 0, compare normally
		if (urgencyScore1 < urgencyScore2) {
			return 1;
		}
		else if (urgencyScore1 > urgencyScore2) {
			return -1;
		}
		else {
			return 0;
		}
	}
}
