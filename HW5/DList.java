// DList Class

public class DList {
	protected DListNode head;
	protected long size;

	// Return size
	public long size(){
		return size;
	}

	// 0 parameter constructor
	public DList() {
		head = new DListNode();
		head.item = new Run();
		head.next = head;
		head.prev = head;
		size = 0;
	}

	// 1 parameter constructor
	public DList(Run o) {
		head = new DListNode();
		head.item = new Run();
		head.next = new DListNode(o);
		head.prev = head.next;
		head.prev.next = head;
		head.next.prev = head;
		size = 1;
	}

	// 2 parameters constructor
	public DList(Run i, Run j) {
		head = new DListNode();
		head.item = new Run();
		head.next = new DListNode(i);
		head.prev = new DListNode(j);
		head.next.prev = head;
		head.next.next = head.prev;
		head.prev.next = head;
		head.prev.prev = head.next;
		size = 2;
	}

	// Insert DListNode at the beginning of the DList
	public void insertFront(Run o) {
		if(size==0) {
			head.next = new DListNode(o);
			head.prev = head.next;
			head.next.prev = head;
			head.prev.next = head;
		} else {
			head.next.prev = new DListNode(o);
			head.next.prev.next = head.next;
			head.next = head.next.prev;
			head.next.prev = head;
		}
		size++;
	}

	// Remove the first DListNode in the DList
	public void removeFront() {
		if(size==1) {
			head.prev = head;
			head.next = head;
			size =0;
		} else if(size>0) {
			head.next = head.next.next;
			head.next.prev = head;
			size--;
		}
	}

	// Insert DListNode at the end of the DList
	public void insertEnd(Run o) {
		if(size==0) {
			head.next = new DListNode(o);
			head.prev = head.next;
			head.next.prev = head;
			head.prev.next = head;
		} else {
			head.prev.next = new DListNode(o);
			head.prev.next.prev = head.prev;
			head.prev = head.prev.next;
			head.prev.next = head;
		}
		size++;
	}

	// Remove the DListNode at the end of DList
	public void removeEnd() {
		if(size==1) {
			head.prev = head;
			head.next = head;
			size =0;
		} else {
			head.prev = head.prev.prev;
			head.prev.next = head;
			size--;
		}
	}

	// Insert item after Node1;
	public void insertAfter(DListNode node, Run o) {
		if(node==head) {
			insertFront(o);
		} else if(node.next==head) {
			insertEnd(o);
		} else {
			node.next.prev = new DListNode(o);
			node.next.prev.next = node.next;
			node.next = node.next.prev;
			node.next.prev = node;
		}
		size++;
	}

	// Remove the node from the DList
	public void removeNode(DListNode node) {
		if(size>0&&node!=head) {
			node.prev.next = node.next;
			node.next.prev = node.prev;
			size--;
		}
	}

	public static void main(String[] args) {
		DList l = new DList(new Run());
		l.insertEnd(new Run(1,2,1));
		l.insertEnd(new Run(2,3));
		l.insertEnd(new Run(0,4));
		DListNode node1 = l.head.next.next;
		DListNode node2 = l.head.next.next.next;
		l.insertAfter(node1, new Run(0,1));
		l.insertAfter(node2, new Run(1,1,3));
		node1 = l.head.next.next;
		node2 = l.head.next.next.next;
		l.insertAfter(node1, new Run(0,1));
		l.insertAfter(node2, new Run(1,1,3));
		DListNode n = l.head.next;
		for(int i=0;i<8;i++) {
			System.out.print(n.item.species+";");
			n = n.next;
		}
	}
}