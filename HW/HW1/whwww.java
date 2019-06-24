import java.io.*;
import java.net.*;

class whwww{
	public static void main(String[] args)throws Exception {
		URL u = new URL("http://www.baidu.com/");
		InputStream ins = u.openStream();
		InputStreamReader isr = new InputStreamReader(ins);
		BufferedReader whiteHouse = new BufferedReader(isr);
		System.out.println(whiteHouse.readLine());
		System.out.println(whiteHouse.readLine());

	}
}