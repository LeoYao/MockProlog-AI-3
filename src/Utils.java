import java.util.regex.Pattern;

/**
 * @author Haiyu Yao
 * @unityId hyao4
 * @studentId 200066531
 * 
 */
public class Utils {

	public Utils() {
		// TODO Auto-generated constructor stub
	}

	private static Pattern pat = Pattern.compile("^[A-Z]");
	
	public static boolean checkCaptialLeading(String s)
	{
		return pat.matcher(s).find();
	}
}
