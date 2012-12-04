package edu.illinois.cs.projects.today1.test;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Stack;

import junit.framework.Assert;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;
import edu.illinois.cs.projects.today1.POQTListConstants;
import edu.illinois.cs.projects.today1.main.POQTListActivity;
import edu.illinois.cs.projects.today1.task.Task;
import edu.illinois.cs.projects.today1.task.TaskAdapter;
import edu.illinois.cs.projects.today1.task.TaskApparatus;
import edu.illinois.cs.projects.today1.task.TaskComparator;
import edu.illinois.cs.projects.today1.task.TaskFactory;
import edu.illinois.cs.projects.today1.task.TaskList;

/**
 * Testing framework for the POQTListActivity class
 * 
 * @author Kevin Today
 * 
 * Based off helpful code found at <developer.android.com> for Android JUnit testing
 */
public class POQTListTest extends ActivityInstrumentationTestCase2<POQTListActivity> {
	// ========== Constants ==========
	// generic constants for Task-making
	private final String GENERIC_DESCRIPTION = "Test this text here!";
	private final GregorianCalendar GENERIC_DUE_DATE = new GregorianCalendar(2008, 7, 2, 22, 15);
	private final long GENERIC_ID = 0L;
	private final Task.Priority GENERIC_PRIORITY = Task.Priority.NORMAL_PRIORITY;
	private final TaskAdapter.Type GENERIC_ADAPTER_TYPE = TaskAdapter.Type.COLORED;
	private final String GENERIC_LIST_NAME = "Test List Name";

	
	// ========== Member Variables ==========
	private POQTListActivity m_activity;
	private ListView m_view;
	
	
	
	// ========== Constructors ==========
	/**
	 * Constructor to instantiate a testing framework
	 */
	public POQTListTest() {
		super("edu.illinois.cs.projects.today1", POQTListActivity.class);
	}
	
	
	// ========== Testing Functions ==========
	@Override
	/**
	 * Initializes the testing environment
	 */
	protected void setUp() throws Exception {
		super.setUp();
		m_activity = this.getActivity();
		m_view = m_activity.getListView();
	}
	
	/**
	 * Checks preconditions for the application, including:
	 *  - ensures application was started
	 */
	public void testPreconditions() {
		assertNotNull(m_view);
	}
	
	/*	Test Task	*/
	/**
	 * Tests the Task constructor for basic, valid input
	 */
	public void testBasicTaskConstructor() {
		Task testTask = new Task(m_activity, GENERIC_ID, GENERIC_DESCRIPTION, GENERIC_DUE_DATE, GENERIC_PRIORITY);
		
		assertEquals(m_activity, testTask.getContext());
		assertEquals(GENERIC_ID, testTask.getID());
		assertEquals(GENERIC_DESCRIPTION, testTask.getDescription());
		assertEquals(GENERIC_DUE_DATE, testTask.getDueDate());
		assertEquals(GENERIC_PRIORITY, testTask.getPriority());
		

	}
	
	/**
	 * Tests the Task constructor for invalid input
	 */
	public void testInvalidTaskConstructor() {
		// test null context
		try {
			Task testTask1 = new Task(null, GENERIC_ID, GENERIC_DESCRIPTION, GENERIC_DUE_DATE, GENERIC_PRIORITY);
			Assert.fail("Exception should have been thrown!");
		}
		catch (IllegalArgumentException exc){
			// Do nothing; test passes
		}
		
		// test null text
		try {
			Task testTask1 = new Task(m_activity, GENERIC_ID, null, GENERIC_DUE_DATE, GENERIC_PRIORITY);
			Assert.fail("Exception should have been thrown!");
		}
		catch (IllegalArgumentException exc){
			// Do nothing; test passes
		}
		
		
		// test empty text
		try {
			Task testTask1 = new Task(m_activity, GENERIC_ID, "", GENERIC_DUE_DATE, GENERIC_PRIORITY);
			Assert.fail("Exception should have been thrown!");
		}
		catch (IllegalArgumentException exc){
			// Do nothing; test passes
		}
	}
	
	/**
	 * Tests the Task constructor for valid, but edge-case input
	 */
	public void testValidNullTaskConstructor() {
		// ensure null date goes through
		Task testTask1 = new Task(m_activity, GENERIC_ID, GENERIC_DESCRIPTION, null, GENERIC_PRIORITY);
		
		// ensure null priority gets assigned to normal priority
		Task testTask2 = new Task(m_activity, GENERIC_ID, GENERIC_DESCRIPTION, GENERIC_DUE_DATE, null);
		assertEquals(Task.Priority.NORMAL_PRIORITY, testTask2.getPriority());
	}
	
	/**
	 * Tests that custom Task.equals function works
	 */
	public void testTaskEquals() {
		Task testTask1 = new Task(m_activity, 0, GENERIC_DESCRIPTION, GENERIC_DUE_DATE, GENERIC_PRIORITY);
		Task testTask2 = new Task(m_activity, 0, GENERIC_DESCRIPTION, GENERIC_DUE_DATE, GENERIC_PRIORITY);
		Task testTask3 = new Task(m_activity, 1, GENERIC_DESCRIPTION, GENERIC_DUE_DATE, GENERIC_PRIORITY);
		
		// equality should be based on the Task's ID
		assertEquals(true, testTask1.equals(testTask2));
		
		// should be reflexive
		assertEquals(true, testTask2.equals(testTask1));
		
		// should reflexively return false on non-equal IDs
		assertEquals(false, testTask1.equals(testTask3));
		assertEquals(false, testTask1.equals(testTask3));
	}
	
	/*	Test TaskComparator	*/
	/**
	 * Tests the TaskComparator class's functionality
	 */
	public void testTaskComparator() {
		// test null date
		Task testTask1 = new Task(m_activity, 0, "Fly to Denver", new GregorianCalendar(2012, 1, 17, 8, 0, 0), GENERIC_PRIORITY);
		Task testTask2 = new Task(m_activity, 1, "Take plane to Denver", new GregorianCalendar(2012, 1, 17, 8, 0, 0), GENERIC_PRIORITY);
		Task testTask3 = new Task(m_activity, 2, "Hurtle through space towards Denver", new GregorianCalendar(2012, 1, 17, 8, 0, 28), GENERIC_PRIORITY);
		Task testTask4 = new Task(m_activity, 3, "Pack for Denver", new GregorianCalendar(2012, 1, 15, 8, 6, 28), GENERIC_PRIORITY);
		Task testTask5 = new Task(m_activity, 4, "Unpack after Denver", new GregorianCalendar(2012, 1, 22, 8, 6, 28), GENERIC_PRIORITY);
		
		TaskComparator taskComparator = new TaskComparator();
		
		// test exact due date equality
		assertEquals(0, taskComparator.compare(testTask1, testTask2));
		
		// test that equality ignores seconds
		assertEquals(0, taskComparator.compare(testTask2, testTask3));
	}
	
	/*	Test TaskAdapter	*/
	/**
	 * Tests TaskAdapter construction for basic, valid input
	 */
	public void testBasicTaskAdapterConstructor() {
		// prepare variables for use in testing
		Task testTask1 = new Task(m_activity, 0, "Go to grocery store", new GregorianCalendar(2011, 12, 15, 10, 10), null);
		Task testTask2 = new Task(m_activity, 1, "Pick up mom", new GregorianCalendar(2012, 1, 20, 5, 7), null);
		Task testTask3 = new Task(m_activity, 2, "Buy shoes", new GregorianCalendar(2012, 1, 6, 13, 20), null);
		
		// various arrays to feed to constructor
		Task[] taskArrayEmpty = {};
		Task[] taskArraySmall = {testTask1};
		Task[] taskArrayBig = {testTask1, testTask2, testTask3};
		
		// various collections to feed to constructor
		ArrayList<Task> taskArrayListEmpty = new ArrayList<Task>();
		
		ArrayList<Task> taskArrayListSmall = new ArrayList<Task>();
		taskArrayListSmall.add(testTask1);
		
		ArrayList<Task> taskArrayListBig = new ArrayList<Task>();
		taskArrayListBig.add(testTask1);
		taskArrayListBig.add(testTask2);
		taskArrayListBig.add(testTask3);
		
		Stack<Task> taskStack = new Stack<Task>();
		taskStack.push(testTask1);
		taskStack.push(testTask2);

		// use variables to test constructors
		TaskAdapter emptyAdapter = new TaskAdapter(m_activity, GENERIC_ADAPTER_TYPE);
		assertEquals(emptyAdapter.getCount(), 0);
		
		TaskAdapter testAdapter1 = new TaskAdapter(m_activity, GENERIC_ADAPTER_TYPE, taskArrayEmpty);
		assertEquals(testAdapter1.getCount(), taskArrayEmpty.length);
		
		TaskAdapter testAdapter2 = new TaskAdapter(m_activity, GENERIC_ADAPTER_TYPE, taskArraySmall);
		assertEquals(testAdapter2.getCount(), taskArraySmall.length);
		
		TaskAdapter testAdapter3 = new TaskAdapter(m_activity, GENERIC_ADAPTER_TYPE, taskArrayBig);
		assertEquals(testAdapter3.getCount(), taskArrayBig.length);
		
		TaskAdapter testAdapter4 = new TaskAdapter(m_activity, GENERIC_ADAPTER_TYPE, taskArrayListEmpty);
		assertEquals(testAdapter4.getCount(), taskArrayListEmpty.size());
		
		TaskAdapter testAdapter5 = new TaskAdapter(m_activity, GENERIC_ADAPTER_TYPE, taskArrayListSmall);
		assertEquals(testAdapter5.getCount(), taskArrayListSmall.size());
		
		TaskAdapter testAdapter6 = new TaskAdapter(m_activity, GENERIC_ADAPTER_TYPE, taskArrayListBig);
		assertEquals(testAdapter6.getCount(), taskArrayListBig.size());
		
		TaskAdapter testAdapter7 = new TaskAdapter(m_activity, GENERIC_ADAPTER_TYPE, taskStack);
		assertEquals(testAdapter7.getCount(), taskStack.size());
	}
	
	/**
	 * Tests invalid TaskAdapter constructor input
	 */
	public void testInvalidTaskAdapterContructor() {
		// test null context
		try {
			TaskAdapter testAdapter = new TaskAdapter(null, GENERIC_ADAPTER_TYPE);
			Assert.fail("Exception should have been thrown!");
		}
		catch (Exception exc) {
			// do nothing; test passes
		}
	}
	
	/**
	 * Tests valid but edge-case TaskAdapter construction
	 */
	public void testValidNullTaskAdapterContructor() {
		// test null adapter type
		TaskAdapter testAdapter = new TaskAdapter(m_activity, null);
		assertEquals(TaskAdapter.Type.UNCOLORED, testAdapter.getType());
	}
	
	/**
	 * Tests the TaskAdapter's 'add' and 'remove' functionality
	 */
	public void testTaskAdapterAddRemove() {
		// prepare variables for use in testing
		Task testTask1 = new Task(m_activity, 0, "Go to grocery store", new GregorianCalendar(2011, 12, 15, 10, 10), null);
		Task testTask2 = new Task(m_activity, 1, "Pick up mom", new GregorianCalendar(2012, 1, 20, 5, 7), null);
		Task testTask3 = new Task(m_activity, 2, "Buy shoes", new GregorianCalendar(2012, 1, 6, 13, 20), null);
		
		TaskAdapter taskAdapter = new TaskAdapter(m_activity, GENERIC_ADAPTER_TYPE);
		
		// perform series of adds and removes; check states
		taskAdapter.add(testTask1);
		taskAdapter.add(testTask2);
		assertEquals(2, taskAdapter.getCount());
		
		// test valid remove
		assertEquals(true, taskAdapter.remove(testTask1));
		assertEquals(1, taskAdapter.getCount());
		
		// test invalid remove
		assertEquals(false, taskAdapter.remove(testTask3));
		
		// test invalid add
		try {
			taskAdapter.add(null);
			Assert.fail("Should throw exception on 'null' addition");
		}
		catch (IllegalArgumentException exc) {
			// test passes; do nothing
		}
	}
	
	/*	Test TaskList	*/
	/**
	 * Tests basic, valid TaskList construction
	 */
	public void testValidTaskListConstructor() {
		TaskAdapter testAdapter = new TaskAdapter(m_activity, GENERIC_ADAPTER_TYPE);
		TaskList testList = new TaskList(GENERIC_LIST_NAME, testAdapter);
		
		assertEquals(testAdapter, testList.getAdapter());
		assertEquals(GENERIC_LIST_NAME, testList.getName());
	}
	
	/**
	 * Tests invalid TaskList construction
	 */
	public void testInvalidTaskListConstructor() {
		TaskAdapter testAdapter = new TaskAdapter(m_activity, GENERIC_ADAPTER_TYPE);

		// test null name
		try {
			TaskList testList = new TaskList(null, testAdapter);
			Assert.fail("Should throw exception on 'null' name");
		}
		catch (IllegalArgumentException exc) {
			// test passes; do nothing
		}
		
		// test null adapter
		try {
			TaskList testList = new TaskList(GENERIC_LIST_NAME, null);
			Assert.fail("Should throw exception on 'null' adapter");
		}
		catch (IllegalArgumentException exc) {
			// test passes; do nothing
		}
	}
	
	/*	Test TaskApparatus	*/
	/**
	 * Tests valid TaskApparatus constructor and cycle functions
	 */
	public void testTaskApparatusConstructorCycle() {
		TaskApparatus apparatus = new TaskApparatus(m_activity);
		
		assertEquals(POQTListConstants.MAIN_TASKLIST_NAME, apparatus.getSelectedTitle());
		assertEquals(TaskApparatus.ListCategory.MAIN, apparatus.getListCategory());
		apparatus.cycleLeft();
		assertEquals(POQTListConstants.COMPLETED_TASKLIST_NAME, apparatus.getSelectedTitle());
		assertEquals(TaskApparatus.ListCategory.COMPLETED, apparatus.getListCategory());
		apparatus.cycleRight();
		assertEquals(POQTListConstants.MAIN_TASKLIST_NAME, apparatus.getSelectedTitle());
		assertEquals(TaskApparatus.ListCategory.MAIN, apparatus.getListCategory());

		TaskAdapter newAdapter = new TaskAdapter(m_activity, GENERIC_ADAPTER_TYPE);
		TaskList newList = new TaskList(GENERIC_LIST_NAME, newAdapter);
	}
	
	public void testBasicTaskApparatusFunctions() {
		TaskApparatus apparatus = new TaskApparatus(m_activity);
		
		// no exceptions should be thrown
		apparatus.createTask(GENERIC_DESCRIPTION, GENERIC_DUE_DATE, GENERIC_PRIORITY);
		apparatus.createTaskList(GENERIC_LIST_NAME, GENERIC_ADAPTER_TYPE);
	}
	
	/**
	 * Tests invalid TaskApparatus constructor and basic functions
	 */
	public void testTaskApparatusFunctions() {
		try {
			new TaskApparatus(null);
			fail("TaskApparatus should have thrown an exception");
		}
		catch (IllegalArgumentException exc) {
			// do nothing; test passes
		}
	}
	
	/*	Test TaskFactory	*/
	/**
	 * Tests valid TaskFactory constructors
	 */
	public void testValidTaskFactoryConstructor() {
		TaskFactory newFactory = new TaskFactory(m_activity);
	}
	
	/**
	 * Tests invalid TaskFactory constructors
	 */
	public void testInvalidTaskFactoryConstructor() {
		try {
			new TaskFactory(null);
			fail("TaskFactory should throw exception on null argument");
		}
		catch (IllegalArgumentException exc) {
			// do nothing; test passes
		}
	}
	
	/**
	 * Tests valid buildTask function
	 */
	public void testValidBuildTask() {
		TaskFactory newFactory = new TaskFactory(m_activity);
		
		// test that assignments went correctly
		Task testTask1 = newFactory.buildTask(GENERIC_DESCRIPTION, GENERIC_DUE_DATE, GENERIC_PRIORITY);
		assertEquals(GENERIC_DESCRIPTION, testTask1.getDescription());
		assertEquals(GENERIC_DUE_DATE, testTask1.getDueDate());
		assertEquals(GENERIC_PRIORITY, testTask1.getPriority());
		assertEquals(m_activity, testTask1.getContext());
	}
	
	/**
	 * Tests invalid buildTask function
	 */
	public void testInvalidBuildTask() {
		TaskFactory newFactory = new TaskFactory(m_activity);
		
		Task testTask1 = newFactory.buildTask(null, GENERIC_DUE_DATE, GENERIC_PRIORITY);
		assertEquals(null, testTask1);
	}
}
