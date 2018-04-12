
public class Queue<T> {
	private class Node<T>{
		T data;
		Node<T> next;
		
		public Node(T e){
			data = e;
			next = null;
		}
		public Node(){
			data = null;
			next = null;
		}
	}
	Node<T> head;
	Node<T> tail;
	int n;
	
	public void addLast(T e) {
		Node<T> temp = new Node<T>();
		temp.data = e;
		System.out.println("added #" + temp.data + "\n to Queue at " + n);
		if(n!=0){
			tail.next = temp;
			tail = temp;
		}
		else{
			head = tail = temp;
		}
		n++;
	}

	public T removeFirst() {
		Node<T> temp = head;
		if(n>=1){
			head = head.next;
			if(n==1)
				tail = head;
			n--;
			return temp.data;
		}
		return null;
	}
	
	public T getLast(){
		return tail.data;
	}
	
	public T getFirst(){
		return head.data;
	}
	
	public boolean isEmpty(){
		return n==0;
	}
	
	public Queue(){
		head = tail = null;
		n =0;
	}
	
}
