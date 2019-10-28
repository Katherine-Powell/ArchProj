
public class Util {

	public static int logBase2(int num) {
		return (int) ((Math.log(num))/(Math.log(2)));
	}
	
	public static int logBase2(double num) {
		return (int) ((Math.log(num))/(Math.log(2)));
	}
	
	public static int convertToKB(int bytes) {
		return bytes/1024;
	}
}
