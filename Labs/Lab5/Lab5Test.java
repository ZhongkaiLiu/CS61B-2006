public class Lab5Test {
	public static void main(String[] args) {
		Y y = new Y();
		((X) y).f();
		X x = new X();
		(x).f();
	}
}


class X {
	public void f() {
		System.out.println(1);
	}
}

class Y extends X{
 	public void f() {
 		System.out.println(2);
 	}
}


