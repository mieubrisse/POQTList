package edu.illinois.cs.projects.today1.task;

/**
 * Class containing all the information for a list of Tasks
 * 
 * @author Kevin Today
 */
public class TaskList {
	// ========== Member Variables ==========
	private long m_ID;				// task list ID
	private String m_name;			// task list name
	private TaskAdapter m_adapter;	// TaskAdapter containing Task objects for the TaskList
	
	
	// ========== Constructors ==========
	/**
	 * Instantiates a new TaskList
	 * 
	 * @param name Name for the TaskList (must not be null)
	 * @param adapter TaskAdapter containing Tasks in the TaskList (must not be null)
	 */
	public TaskList(long ID, String name, TaskAdapter adapter) {
		// sanity check
		if (name == null) {
			throw new IllegalArgumentException("TaskList name cannot be null");
		}
		if (adapter == null) {
			throw new IllegalArgumentException("TaskList adapter cannot be null");
		}
		
		m_ID = ID;
		m_name = name;
		m_adapter = adapter;
	}
	
	
	// ========== Getter Functions ==========
	public long getID() {
		return m_ID;
	}
	
	public String getName() {
		return m_name;
	}
	
	public TaskAdapter getAdapter() {
		return m_adapter;
	}
	
	// ========== Setter Functions ==========
	public void setName(String name) {
		// sanity check
		if (name == null) {
			throw new IllegalArgumentException("TaskList name cannot be null");
		}
		
		m_name = name;
	}
	
	public void setAdapter(TaskAdapter adapter) {
		// sanity check
		if (adapter == null) {
			throw new IllegalArgumentException("TaskList adapter cannot be null");
		}
		
		m_adapter = adapter;
	}
}
