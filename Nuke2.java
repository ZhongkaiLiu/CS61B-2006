import java.io.*;

class Nuke2{
	public static void main(String[] args)throws Exception {
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
		String s = keyboard.readLine();
		System.out.println(s.substring(0,1)+s.substring(2));
	}
}