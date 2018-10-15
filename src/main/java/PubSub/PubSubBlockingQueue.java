package PubSub;

public class PubSubBlockingQueue<T> {
	private T[] items;
	private int start;
	private int end;
	private int size;

	public PubSubBlockingQueue(int size) {
		this.items = (T[]) new Object[size];
		this.start = 0;
		this.end = -1;
		this.size = 0;
	}

	public synchronized void put(T item) {
		
		while(size == items.length) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}				
		int next = (end+1)%items.length;
		items[next] = item;
		end = next;		
		size++;
		if(size == 1) {
		//if(size > 0) {
			this.notifyAll();
		}
		
	}


	public synchronized T take() {
		
		while(size == 0) {
			System.out.println("SIZE = 0");
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}		
		}

		T item = items[start];
		start = (start+1)%items.length;
		size--;
		if(size == items.length-1) {
		//if(size < items.length) {
			this.notifyAll();
		}
		return item;
	}


	public synchronized boolean isEmpty() {
		return size == 0;
	}
	
	public synchronized int getSize() {
		return size;
	}

}
