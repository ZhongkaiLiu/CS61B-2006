// DListNode Class

public class DListNode {
	public Run item;
	public DListNode next;
	public DListNode prev;

	// 0 parameter constructor
	public DListNode(){
		next = null;
		prev = null;
	}

	// 1 parameter constructor
	public DListNode(Run o) {
		item = o;
		next = null;
		prev = null;
	}
}