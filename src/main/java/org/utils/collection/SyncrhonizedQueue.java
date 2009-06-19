package org.utils.collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang.Validate;

public class SyncrhonizedQueue <T> {

	private Queue<T> elements = new LinkedList<T>();
	
	private Lock lock = new ReentrantLock();
	private Condition full = lock.newCondition();
	private Condition empty = lock.newCondition();
	private volatile boolean finished;
	public final int capacity;
	
	public SyncrhonizedQueue() {
		this(Integer.MAX_VALUE);
	}
	
	public SyncrhonizedQueue(int capacity)
	{
		Validate.notNull(capacity, "Capacity should be set");
		this.capacity = capacity;
	}
	/**
	 * @return the finished
	 */
	public boolean isFinished() {
		return finished && elements.isEmpty();
	}

	/**
	 * @param finished the finished to set
	 */
	public void setFinished(boolean finished) {
		this.finished = finished;
	}


	public T pull() throws Exception
	{
		try {
			lock.lock();
			while(elements.size() < 1){
				empty.await();
			}
			T element = elements.poll();
			full.signal();
			return element;
		} finally {
			lock.unlock();
		}
	}

	public void push(T row) throws Exception
	{
		try{
			lock.lock();
			while (elements.size() ==  capacity ){
				full.await();
			}
			elements.add(row);
			empty.signal();
		}finally{
			lock.unlock();
		}
	}
	
}
