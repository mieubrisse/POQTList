package edu.illinois.cs.projects.today1.task;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * !!! THIS STRUCTURE IS NOT COMPLETE OR IN EFFECT YET. IT WILL BE USED IN A LATER IMPLEMENTATION FOR INCREASED SPEED !!!
 * 
 * Data structure for efficient Task management
 * NOTE: This data structure does not use the generic type <T> because, due to silly Java
 * 	language restrictions, you cannot declare a generic array "new T[]". As such, the structure
 * 	is forced to be rather specialized.
 * 
 * !! NOTE !! : This structure has a LOT of room for optimization
 * 
 * @author Kevin Today
 */
public class SortedTaskList implements List<Task>{
	// ========== Constants ==========
	private static final int DEFAULT_SIZE = 10;
	
	// ========== Member Variables ==========
	Task[] m_tasks;	// array of tasks
	int m_numTasks;
	int m_capacity;
	Comparator<Task> m_comparator;
	
	// ========== Constructors ==========
	/**
	 * Constructs an empty SortedTaskList organized on the given Comparator
	 * 
	 * @param comparator Comparator with which to keep the list sorted
	 */
	public SortedTaskList(Comparator<Task> comparator) {
		// sanitize input
		if (comparator == null) {
			throw new IllegalArgumentException("Comparator cannot be null");
		}
		
		m_numTasks = 0;
		m_capacity = DEFAULT_SIZE;
		m_tasks = new Task[m_capacity];
	}
	
	/**
	 * Instantiates a SortedTaskList with the given Tasks organized on the given Comparator
	 * 
	 * @param comparator Comparator with which to keep the list sorted
	 * @param collection Tasks to fill the SortedTaskList with
	 */
	public SortedTaskList(Comparator<Task> comparator, Collection<Task> collection) {
		this(comparator);
		
		for (Task task : collection) {
			this.add(task);
		}
	}
	
	/**
	 * Instantiates a SortedTaskList with the given Tasks organized on the given Comparator
	 * 
	 * @param comparator Comparator with which to keep the list sorted
	 * @param collection Tasks to fill the SortedTaskList with
	 */
	public SortedTaskList(Comparator<Task> comparator, Task[] array) {
		this(comparator);
		
		for (Task task : array) {
			this.add(task);
		}
	}

	@Override
	public boolean add(Task task) {
		if (task == null) {
			return false;
		}
		
		// if array is already full, double array size
		if (m_numTasks == m_capacity) {
			Task[] newTasks = new Task[m_capacity * 2];
			for (int index = 0; index < m_capacity; index++) {
				newTasks[index] = m_tasks[index];
			}
			m_tasks = newTasks;
			m_capacity = m_capacity * 2;
		}
		
		return false;
		
		
	}
	

	/**
	 * NOTE: Does nothing! The SortedTaskList will organize itself
	 */
	@Override
	public void add(int position, Task task) {
		return;
	}

	/**
	 * 
	 */
	@Override
	public boolean addAll(Collection<? extends Task> collection) {
		try {
			for (Task task : collection) {
				this.add(task);
			}
		}
		catch (Exception e) {
			return false;
		}
		
		return true;
	}

	/**
	 * NOTE: Does nothing! The SortedTaskList will organize itself
	 */
	@Override
	public boolean addAll(int arg0, Collection<? extends Task> arg1) {
		return false;
	}

	/**
	 * Clears the SortedTaskList's contents
	 */
	@Override
	public void clear() {
		m_tasks = new Task[DEFAULT_SIZE];
	}

	/**
	 * Checks if the SortedTaskList contains the specified Object
	 * 
	 * @return True if the object is in the SortedTaskList; false otherwise
	 */
	@Override
	public boolean contains(Object obj) {
		for (Task task : m_tasks) {
			if (m_tasks.equals(obj)) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * 
	 */
	@Override
	public boolean containsAll(Collection<?> arg0) {
		return false;
	}

	@Override
	public Task get(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int indexOf(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterator<Task> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int lastIndexOf(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ListIterator<Task> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListIterator<Task> listIterator(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Task remove(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Task set(int arg0, Task arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Task> subList(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
