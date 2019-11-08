package dict;

public class Prime {
	public static boolean isPrime(int n) {
		for(int i=2;i * i < n; i++) {
			if(n%i==0) {
				return false;
			}
		}
		return true;
	}

	public static int primeBetween(int min, int max) {
		for(int i=max; i>=min; i--) {
			if(isPrime(i)) {
				return i;
			}
		}
		return -1;
	}

	public static int primeLarge(int n) {
		return primeBetween(n*2, n*100+101);
	}
}