import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

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
	public static String toHex(String arg) {
	    return String.format("%040x", new BigInteger(1, arg.getBytes(StandardCharsets.US_ASCII)));
	}
}
