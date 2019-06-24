package list;

public class LockDList extends DList {
	protected LockDList head;

	protected DListNode newNode(Object item, DListNode prev, DListNode next) {
		return new LockDListNode(item, prev, next);
	}

	public void lockNode(DListNode node) {
		if(node instanceof LockDListNode){
			((LockDListNode) node).locked = true;
		} else {
			System.out.println("This node is not a LockDListNode");
			System.exit(1);
		}
	}

	public void remove(DListNode node) {
		if(!((LockDListNode) node).locked) {
			super.remove(node);
		}
	}

	public static void main(String[] args) {
    LockDList l = new LockDList();
    l.insertFront("abc");
    l.insertBack("123");
    l.insertAfter("ijk", l.front());
    l.insertBefore("890",l.back());
    l.lockNode(l.front());
    l.remove(l.front());
    l.lockNode(l.back());
    l.remove(l.back());
    System.out.println(l.toString());
    }
}