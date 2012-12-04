package edu.illinois.cs.projects.today1.task;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.concurrent.PriorityBlockingQueue;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import edu.illinois.cs.projects.today1.POQTListConstants;
import edu.illinois.cs.projects.today1.R;

/**
 * Custom adapter connecting a ListView and a PriorityBlockingQueue-backed ListAdapter extension
 * 
 * @author Kevin Today
 */
public class TaskAdapter extends BaseAdapter {
	// ========== Constants ==========
	// enumerated constant for type of TaskAdapter
	public enum Type {
		COLORED,
		UNCOLORED;
		
		/**
		 * Returns a Type object associated with the given type ordinal
		 * 
		 * @param ordinal Ordinal of type object
		 * @return Type object designated by the given ordinal
		 */
		public static TaskAdapter.Type findType(int ordinal) {
			switch (ordinal) {
			case 0:
				return COLORED;
			case 1:
				return UNCOLORED;
			default:
				return null;
			}
		}
	}
	private final int DEFAULT_START_CAPACITY = 11;	// 11 is Java's default PriorityBlockingQueue start size

	
	// ========== Member Variables ==========
	private Context m_context;	// context for environment
	private PriorityBlockingQueue<Task> m_tasks;	// priority queue containing items for the environment
	private Type m_type;
	
	
	// ========== Constructors ==========
	/**
	 * Instantiates a new empty TaskAdapter
	 * 
	 * @param context The Context to create the TaskAdapter in
	 * @param type Type of TaskAdapter to create (null defaults to UNCOLORED)
	 */
	public TaskAdapter(Context context, Type type) {
		// sanity checks
		if (context == null) {
			throw new IllegalArgumentException("TaskAdapter context must not be null");
		}
		
		m_context = context;
		m_tasks = new PriorityBlockingQueue<Task>(DEFAULT_START_CAPACITY, new TaskComparator(m_context));
		
		// sanity check for type
		if (type == null) {
			m_type = Type.UNCOLORED;
		}
		else {
			m_type = type;
		}
	}
	
	/**
	 * Instantiates a new TaskAdapter object with the given Tasks in it
	 * 
	 * @param context The context to create the TaskAdapter in.
	 * @param type Type of TaskAdapter to create (null defaults to UNCOLORED)
	 * @param objects A list of objects to add to the PriorityBlockingQueue upon creation. 'null' will add nothing.
	 */
	public TaskAdapter(Context context, Type type, Collection<Task> objects) {
		this(context, type);
		
		// add all the elements in the list
		if (objects != null) {
			for (Task toAdd : objects) {
				m_tasks.offer(toAdd);
			}
		}
	}
	
	/**
	 * Instantiates a new TaskAdapter object with the given Tasks in it
	 * 
	 * @param context The context to create the TaskAdapter in.
	 * @param type Type of TaskAdapter to create (null defaults to UNCOLORED)
	 * @param objects An array of objects to add to the PriorityBlockingQueue upon creation. 'null' will add nothing.
	 */
	public TaskAdapter(Context context, Type type, Task[] objects) {
		this(context, type);
		
		// add all the elements in the list
		if (objects != null) {
			for (Task toAdd : objects) {
				m_tasks.offer(toAdd);
			}
		}
	}

	
	// ========== Member Functions ==========
	public void print() {
		System.out.print("[");
		for (Task t : m_tasks) {
			System.out.print(t.getDescription() + ", ");
		}
		System.out.println("]");
	}
	
	
	@Override
	/**
	 * Counts the number of items in the PriorityBlockingQueue
	 * 
	 * @return The size of the PriorityBlockingQueue
	 */
	public int getCount() {
		return m_tasks.size();
	}

	@Override
	/**
	 * Gets the Task at the given index in the queue
	 * 
	 * @param position Index within the adapter of the desired object
	 * @return The Task at the given position (null if the Task could not be found)
	 */
	public Object getItem(int position) {
		// sanity check; return null if invalid position
		if (position < 0 || position >= m_tasks.size()) {
			return null;
		}
		
		// !!! THIS IS AN INEFFICIENT WAY TO GET ITEMS AT LOCATION! USE MORE EFFICIENT !!!
		// make queue into sorted array and return Task at "position"
		Task[] taskArray = new Task[m_tasks.size()];
		m_tasks.toArray(taskArray);
		Arrays.sort(taskArray, new TaskComparator(m_context));
		return taskArray[position];
	}

	@Override
	/**
	 * Returns the ID of the Task at 'position' in the queue
	 * 
	 * @return The ID of the Task at 'position'
	 */
	public long getItemId(int position) {
		return ((Task)getItem(position)).getID();
	}

	@Override
	/**
	 * Creates a View representation of a Task object from the R.layout.task.xml file
	 * 
	 * @param position The index in the PriorityBlockingQueue that the given object is at
	 * @param convertView A pre-existing View for the given Task if it exists; 'null' otherwise
	 * @param parent The ViewGroup to assign the newly-created View hierarchy to. Unused
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
        Task taskObject = (Task) getItem(position);	// gets the Task object for the given position
        RelativeLayout taskView = (RelativeLayout)convertView;	// layout to add objects to
        
        // inflate the XML into objects
        if (taskView == null) {
        	LayoutInflater inflater = (LayoutInflater) m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	taskView = (RelativeLayout)inflater.inflate(R.layout.task, null);
        }

        // get the newly-inflated TextView objects by their XML id
        TextView descriptionView = (TextView) taskView.findViewById(R.id.textview_task_description);
        TextView dueDateView = (TextView) taskView.findViewById(R.id.textview_task_duedate);
        
        // set views as "selected" to initiate marquee scroll
        descriptionView.setSelected(true);
        dueDateView.setSelected(true);
        
        //Assign the appropriate data from task object above
        descriptionView.setText(taskObject.getDescription());
        
        Calendar temp_due_date = taskObject.getDueDate();
        // if no due date, hide due date display
        if (temp_due_date == null) {
        	dueDateView.setVisibility(View.GONE);
        }
        // otherwise, parse due date into appropriate display string
        else {
        	String dateDisplay = findDueDateString(temp_due_date);
        	dueDateView.setVisibility(View.VISIBLE);
        	dueDateView.setText(dateDisplay);
        }
        
        // color View based on TaskAdapter type, Task urgency, and user settings
        if (m_type == Type.COLORED) {
        	scaleViewColors(taskView, descriptionView, dueDateView, taskObject);
        }
        else if (m_type == Type.UNCOLORED) {
        	descriptionView.setTextColor(0xFFFFFFFF);	// default uncolored main text to white
        	dueDateView.setTextColor(0xFFC0C0C0);		// default uncolored subtext to light gray
        }
 
        return taskView;
	}
	
	
	// ========== Queue Functions ==========
	/**
	 * Offers the given Task to the TaskAdapter, which sorts it according to due date
	 * 
	 * @param addition The Task object to add (non-null)
	 */
	public void add(Task addition) {
		if (addition == null) {
			throw new IllegalArgumentException("Cannot add null Task");
		}
		
		m_tasks.offer(addition);
		notifyDataSetChanged();
	}
	
	/**
	 * Removes the given Task object from the TaskAdapter
	 * 
	 * @param elimination The Task object to remove (non-null)
	 * @return 'true' if the element was in the TaskAdapter; 'false' if not
	 */
	public boolean remove(Task elimination) {
		if (elimination == null) {
			throw new IllegalArgumentException("Cannot remove null Task");
		}
		
		boolean returnValue = m_tasks.remove(elimination);
		notifyDataSetChanged();
		return returnValue;
	}
	
	
	// ========== Getter Functions ==========
	public Type getType() {
		return m_type;
	}
	
	
	// ========== Setter Functions ==========
	public void setType(TaskAdapter.Type newType) {
		// sanity check
		if (newType == null) {
			throw new IllegalArgumentException("New adapter type cannot be null");
		}
		m_type = newType;
	}


	// ========== Helper Functions ==========
	/**
	 * Based on the user's preferences and a Task's urgency, scales the coloring of a View representing a Task object
	 * 	to reflect its urgency
	 * 
	 * NOTE: Cutoffs should never be the same! (e.g. "mid" and "high" urgency cannot both have the cutoff "10" or a
	 * 	divide-by-zero error will occur)
	 * 
	 * @param backgroundView The View objects to change the background color for
	 * @param mainTextView The TextView objects to change the main text color for
	 * @param subTextView TextView to apply the subtext color scaling to
	 * @param task The Task object upon whose urgency coloring is based
	 */
	private void scaleViewColors(View backgroundView, TextView mainTextView, TextView subTextView, Task task) {
       	SharedPreferences preferences = m_context.getSharedPreferences(POQTListConstants.PREFERENCES_FILEPATH, m_context.MODE_PRIVATE);
    	int urgencyScale = preferences.getInt(POQTListConstants.PREF_KEY_URGENCY_SCALE, POQTListConstants.DEFAULT_URGENCY_SCALE);
    	
    	// percents of urgency scale where "mid" and "high" urgencies start
    	double midUrgencyWeight = (double)preferences.getFloat(POQTListConstants.PREF_KEY_MID_URGENCY_WEIGHT, POQTListConstants.DEFAULT_MID_URGENCY_WEIGHT);
    	double highUrgencyWeight = (double)preferences.getFloat(POQTListConstants.PREF_KEY_HIGH_URGENCY_WEIGHT, POQTListConstants.DEFAULT_HIGH_URGENCY_WEIGHT);
    	
    	// values on urgency scale where "mid" and "high" urgencies start
    	int midUrgencyCutoff = (int)(midUrgencyWeight * ((double)urgencyScale));
    	int highUrgencyCutoff = (int)(highUrgencyWeight * ((double)urgencyScale));
    	
    	int taskUrgency = task.findUrgencyScore(m_context);
    	int backgroundColor;
    	int mainTextColor;
    	int subTextColor;
    	
    	// if Task has no urgency, give Task "no urgency" coloring
    	if (taskUrgency < 0) {
    		backgroundColor = preferences.getInt(POQTListConstants.PREF_KEY_NO_URGNCY_BKGRND_COLR, POQTListConstants.DEFAULT_NO_URGENCY_BKGRND_COLOR);
    		mainTextColor = preferences.getInt(POQTListConstants.PREF_KEY_NO_URGNCY_TEXT_COLR, POQTListConstants.DEFAULT_NO_URGENCY_TEXT_COLOR);
    		subTextColor = preferences.getInt(POQTListConstants.PREF_KEY_NO_URGNCY_SUBTEXT_COLR, POQTListConstants.DEFAULT_NO_URGENCY_SUBTEXT_COLOR);
    	}
    	// if Task has some urgency value but less than "mid" urgency, give Task "low" urgency coloring
    	// 	coloring is scaled based on distance to "mid" urgency cutoff
    	else if (taskUrgency >= 0 && taskUrgency < midUrgencyCutoff) {
    		// calculate a percentage representing how close to the midUrgencyCutoff the Task's urgency is
    		double scaleFactor = ((double)taskUrgency) / ((double)midUrgencyCutoff);
    		
    		// find scaled background color
    		int lowUrgencyBkgrndColor = preferences.getInt(POQTListConstants.PREF_KEY_LOW_URGNCY_BKGRND_COLR, POQTListConstants.DEFAULT_LOW_URGENCY_BKGRND_COLOR);
    		int midUrgencyBkgrndColor = preferences.getInt(POQTListConstants.PREF_KEY_MID_URGNCY_BKGRND_COLR, POQTListConstants.DEFAULT_MID_URGENCY_BKGRND_COLOR);
    		backgroundColor = findScaledColor(lowUrgencyBkgrndColor, midUrgencyBkgrndColor, scaleFactor);
    	
    		// find scaled text color
    		int lowUrgencyTextColor = preferences.getInt(POQTListConstants.PREF_KEY_LOW_URGNCY_TEXT_COLR, POQTListConstants.DEFAULT_LOW_URGENCY_TEXT_COLOR);
    		int midUrgencyTextColor = preferences.getInt(POQTListConstants.PREF_KEY_MID_URGNCY_TEXT_COLR, POQTListConstants.DEFAULT_MID_URGENCY_TEXT_COLOR);
    		mainTextColor = findScaledColor(lowUrgencyTextColor, midUrgencyTextColor, scaleFactor);
    		
    		// find scaled subtext color
    		int lowUrgencySubTextColor = preferences.getInt(POQTListConstants.PREF_KEY_LOW_URGNCY_SUBTEXT_COLR, POQTListConstants.DEFAULT_LOW_URGENCY_SUBTEXT_COLOR);
    		int midUrgencySubTextColor = preferences.getInt(POQTListConstants.PREF_KEY_MID_URGNCY_SUBTEXT_COLR, POQTListConstants.DEFAULT_MID_URGENCY_SUBTEXT_COLOR);
    		subTextColor = findScaledColor(lowUrgencySubTextColor, midUrgencySubTextColor, scaleFactor);
    	}
    	// if Task has urgency between "mid" urgency (inclusive) and "high" urgency (exclusive), give Task "mid" urgency
    	// 	coloring, scaled based on distance to "high" urgency cutoff
    	else if (taskUrgency >= midUrgencyCutoff && taskUrgency < highUrgencyCutoff) {
    		// calculate percentage representing how close to highUrgencyCutoff Task's urgency is
    		double scaleFactor = ((double)(taskUrgency - midUrgencyCutoff)) / ((double)(highUrgencyCutoff - midUrgencyCutoff));
    		
    		// find scaled background color and color View backgrounds
    		int midUrgencyBkgrndColor = preferences.getInt(POQTListConstants.PREF_KEY_MID_URGNCY_BKGRND_COLR, POQTListConstants.DEFAULT_MID_URGENCY_BKGRND_COLOR);
    		int highUrgencyBkgrndColor = preferences.getInt(POQTListConstants.PREF_KEY_HI_URGNCY_BKGRND_COLR, POQTListConstants.DEFAULT_HIGH_URGENCY_BKGRND_COLOR);
    		backgroundColor = findScaledColor(midUrgencyBkgrndColor, highUrgencyBkgrndColor, scaleFactor);
    	
    		// find scaled text color and color TextView text
    		int midUrgencyTextColor = preferences.getInt(POQTListConstants.PREF_KEY_MID_URGNCY_TEXT_COLR, POQTListConstants.DEFAULT_MID_URGENCY_TEXT_COLOR);
    		int highUrgencyTextColor = preferences.getInt(POQTListConstants.PREF_KEY_HI_URGNCY_TEXT_COLR, POQTListConstants.DEFAULT_HIGH_URGENCY_TEXT_COLOR);
    		mainTextColor = findScaledColor(midUrgencyTextColor, highUrgencyTextColor, scaleFactor);
    		
    		// find scaled subtext color
    		int lowUrgencySubTextColor = preferences.getInt(POQTListConstants.PREF_KEY_MID_URGNCY_SUBTEXT_COLR, POQTListConstants.DEFAULT_MID_URGENCY_SUBTEXT_COLOR);
    		int midUrgencySubTextColor = preferences.getInt(POQTListConstants.PREF_KEY_HI_URGNCY_SUBTEXT_COLR, POQTListConstants.DEFAULT_HIGH_URGENCY_SUBTEXT_COLOR);
    		subTextColor = findScaledColor(lowUrgencySubTextColor, midUrgencySubTextColor, scaleFactor);
    	}
    	// if Task has urgency between "high" urgency (inclusive) and "overdue" (exclusive), give Task "high" urgency
    	// 	coloring, scaled based on distance to "overdue" cutoff
    	else if (taskUrgency >= highUrgencyCutoff && taskUrgency < urgencyScale) {
    		// calculate percentage representing how close to overdueCutoff Task's urgency is
    		double scaleFactor = ((double)(taskUrgency - highUrgencyCutoff)) / ((double)(urgencyScale - highUrgencyCutoff));
    		
    		// find scaled background color and color View backgrounds
    		int highUrgencyBkgrndColor = preferences.getInt(POQTListConstants.PREF_KEY_HI_URGNCY_BKGRND_COLR, POQTListConstants.DEFAULT_HIGH_URGENCY_BKGRND_COLOR);
    		int overdueBkgrndColor = preferences.getInt(POQTListConstants.PREF_KEY_DUE_BKGRND_COLR, POQTListConstants.DEFAULT_DUE_BKGRND_COLOR);
    		backgroundColor = findScaledColor(highUrgencyBkgrndColor, overdueBkgrndColor, scaleFactor);
    	
    		// find scaled text color and color TextView text
    		int highUrgencyTextColor = preferences.getInt(POQTListConstants.PREF_KEY_HI_URGNCY_TEXT_COLR, POQTListConstants.DEFAULT_HIGH_URGENCY_TEXT_COLOR);
    		int overdueTextColor = preferences.getInt(POQTListConstants.PREF_KEY_DUE_TEXT_COLR, POQTListConstants.DEFAULT_DUE_TEXT_COLOR);
    		mainTextColor = findScaledColor(highUrgencyTextColor, overdueTextColor, scaleFactor);
    		
    		// find scaled subtext color
    		int lowUrgencySubTextColor = preferences.getInt(POQTListConstants.PREF_KEY_HI_URGNCY_SUBTEXT_COLR, POQTListConstants.DEFAULT_HIGH_URGENCY_SUBTEXT_COLOR);
    		int midUrgencySubTextColor = preferences.getInt(POQTListConstants.PREF_KEY_DUE_SUBTEXT_COLR, POQTListConstants.DEFAULT_DUE_SUBTEXT_COLOR);
    		subTextColor = findScaledColor(lowUrgencySubTextColor, midUrgencySubTextColor, scaleFactor);
    	}
    	// if Task is overdue, give Task "overdue" coloring
    	else {
    		backgroundColor = preferences.getInt(POQTListConstants.PREF_KEY_OVERDUE_BKGRND_COLR, POQTListConstants.DEFAULT_OVERDUE_BKGRND_COLOR);
    		mainTextColor = preferences.getInt(POQTListConstants.PREF_KEY_OVERDUE_TEXT_COLR, POQTListConstants.DEFAULT_OVERDUE_TEXT_COLOR);
    		subTextColor = preferences.getInt(POQTListConstants.PREF_KEY_OVERDUE_SUBTEXT_COLR, POQTListConstants.DEFAULT_OVERDUE_SUBTEXT_COLOR);
    	}
    	
		// color views
    	mainTextView.setTextColor(mainTextColor);
    	subTextView.setTextColor(subTextColor);
    	backgroundView.setBackgroundColor(backgroundColor);
	}
	
	/**
	 * Finds a color int at a percent of the RGB-distance between two colors
	 * 
	 * @param startColor The starting color (0% RGB-distance)
	 * @param endColor The ending color (100% RGB-distance)
	 * @param scaleWeight Percent of RGB-distance between startColor and endColor the new color should be
	 * @return An int 
	 */
	private int findScaledColor(int startColor, int endColor, double scaleWeight) {
		// get starting red, green, and blue values
		int startRed = Color.red(startColor);
		int startGreen = Color.green(startColor);
		int startBlue = Color.blue(startColor);
		
		// get red, green, and blue difference between endColor and startColor
		int diffRed = Color.red(endColor) - startRed;
		int diffGreen = Color.green(endColor) - startGreen;
		int diffBlue = Color.blue(endColor) - startBlue;
		
		// find red, green, and blue components of the desired color
		int newRed = startRed + (int)(scaleWeight * ((double) diffRed));
		int newGreen = startGreen + (int)(scaleWeight * ((double) diffGreen));
		int newBlue = startBlue + (int)(scaleWeight * ((double) diffBlue));

		// create and return new color int
		return Color.rgb(newRed, newGreen, newBlue);
	}
	
	/**
	 * Helper function to get a string representing the given due date
	 * 
	 * @param dueDate Calendar object to represent
	 * @return String representing due date
	 */
	private static String findDueDateString(Calendar dueDate) {
		// sanity check
		if (dueDate == null) {
			return null;
		}
		
		// parts of final string to fill out
		String dateString = "";
		String timeString = "";
		String yearString = "";	
		
		// format date
		Calendar today = Calendar.getInstance();
		int daysDifference = daysDifference(today, dueDate);
		if (daysDifference == 0) {
    		dateString = "Today";
		}
		else if (daysDifference == -1) {
    		dateString = "Yesterday";
		}
		else if (daysDifference == 1) {
    		dateString = "Tomorrow";
		}
		// use "last ________" format
		else if (daysDifference < -1 && daysDifference > -7) {
			dateString = "Last " + DateFormat.format("EEEE", dueDate);
		}
		// if upcoming Monday, Tuesday, etc., use plain name
		else if (daysDifference > 1 && daysDifference < 7) {
			dateString = (String)DateFormat.format("EEEE", dueDate);
		}
		// otherwise, use "month date" form
		else {
			dateString = (String)DateFormat.format("MMM d", dueDate);
		}
		
		// format time
		timeString = (String)DateFormat.format("h:mm aa", dueDate);
		
		// format year (if due in year other than current year)
		if (today.get(Calendar.YEAR) != dueDate.get(Calendar.YEAR)) {
			yearString = ", " + dueDate.get(Calendar.YEAR);
		}
		
		return dateString + yearString + " at " + timeString;
	}
	
	/**
	 * Helper function to find the difference in days between the given dates (ignores hours/minutes/seconds!)
	 * 
	 * @param date1 Date to start on
	 * @param date2 Date to end at
	 * @return Number of days difference between start and end date or +/-20; whichever comes first
	 */
	private static int daysDifference(Calendar date1, Calendar date2) {
		// equality check
		boolean datesEqual = date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR)
			&& date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH)
			&& date1.get(Calendar.DATE) == date2.get(Calendar.DATE);
		if (datesEqual) {
			return 0;
		}
		
		int targetYear = date2.get(Calendar.YEAR);
		int targetMonth = date2.get(Calendar.MONTH);
		int targetDay = date2.get(Calendar.DATE);
		
		// set variables pertaining for counting
		int daysDifference = 0;		// counter to keep track of difference in days
		Calendar modifyDate = (Calendar)date1.clone();
		boolean incrementForward = date1.before(date2);		// assign proper direction to count;
		int diffIncrAmount;
		if (incrementForward) {
			diffIncrAmount = 1;
		}
		else {
			diffIncrAmount = -1;
		}
		
		// count days difference either up or down while moving current date towards target date
		while (Math.abs(daysDifference) < 20 
				&& !(modifyDate.get(Calendar.DATE) == targetDay 
				&& modifyDate.get(Calendar.MONTH) == targetMonth
				&& modifyDate.get(Calendar.YEAR) == targetYear)) {
			daysDifference += diffIncrAmount;
			modifyDate.roll(Calendar.DATE, incrementForward);
		}
		
		return daysDifference;
	}
}
