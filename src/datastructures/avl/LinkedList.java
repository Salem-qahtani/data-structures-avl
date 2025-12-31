package datastructures.avl;

public class LinkedList<T> {
	
	private Node<T> head;
	private Node<T> current;
	private int size;
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public LinkedList() {
		head = current = null;
		size = 0;
	
	}
	
	public boolean empty() {
		return head == null;
		
	}
	
	public boolean last() {
		return current.next == null;
		
	}
	
	public boolean full() {
		return false;
		
	}
	
	public void findFirst() {
		current = head;
		
	}
	
	public void findNext() {
		current = current.next;
		
	}
	
	public T retrieve() {
		return current.data;
		
	}
	
	public void update(T val) {
		current.data = val;
		
	}
	
	public void insert (T val) {
		Node<T> tmp;
		size++;
		
		if (empty()) {
			current = head = new Node<T> (val);	
		}
		else {
			tmp = current.next;
			current.next =  new Node<T> (val);
			current = current.next;
			current.next = tmp;	
		}
		
	}
	
	public void remove() {
		if(current == null)
			return;
		
		size--;
		
		if (current == head) {
			head = head.next;
	
		}
		
		else {
			Node <T> tmp = head;
			
			while (tmp.next != current) {
				tmp = tmp.next;
			}
			
			tmp.next =current.next;
			
		}
		
		if (current.next == head) {
			current = head;
		}
		
		else {
			current = current.next;
		}
		
	}

}
