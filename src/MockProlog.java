import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.lang.StringBuffer;
/**
 * @author Haiyu Yao
 * @unityId hyao4
 * @studentId 200066531
 * 
 */
public class MockProlog {

	//public static Pattern pat = Pattern
	//		.compile("((value\\(.+?,\\s*.+?,\\s*.+?\\)\\.??)|(edge\\(.+?,\\s*.+?,\\s*.+?\\)\\.?))");

	public static void main(String[] args) {
		
		Rule rule = new Rule();
		rule.setFacts(getFacts());
		
		ArrayList<String> argumentNames = new ArrayList<String>();
		Map<String, String> value = new HashMap<String, String>();
		List<Query> qrys = getQueries(argumentNames);
		boolean rst = rule.match(qrys, value);
		
		System.out.println("======================");
		printResult(rst, qrys.get(0), argumentNames);

	}
	
	public static List<Query> getQueries(List<String> argumentNames)
	{
		List<Query> qrys = new ArrayList<Query>();
		// qrys.add(getQuery("value", "X1", "shouldAvoid", "Y1"));
		// qrys.add(getQuery("value", "X1", "isa", "Y2"));
		// qrys.add(getQuery("value", "X2", "contains", "Y3"));
		// qrys.add(getQuery("value", "X2", "ako", "Y4"));
		// qrys.add(getQuery("value", "X", "Y", "Z"));

		boolean moreInput = true;
		int queryNum = 1;
		Scanner scanIn = null;

		try {
			scanIn = new Scanner(System.in);
			while (moreInput) {

				System.out.println("======================");
				System.out.println("Begin reading the query [" + queryNum++
						+ "]...");

				String funcW = null;
				boolean inputFuncW = true;
				boolean retry = false;
				while (inputFuncW) {

					if (!retry) {
						System.out
								.println("Please enter the predicate name of the query: ");
					} else {
						System.out
								.println("Please enter the predicate name of the query again: ");
					}

					funcW = scanIn.nextLine();

					if (funcW.equals("value") || funcW.equals("edge")) {
						inputFuncW = false;
					} else {
						System.out
								.println("The predicate name should only be 'value' or 'edge'!");
					}
				}

				System.out.println("Enter the first argument of the query: ");
				String arg1 = scanIn.nextLine();
				if (Utils.checkCaptialLeading(arg1) && !argumentNames.contains(arg1))
				{
					argumentNames.add(arg1);
				}
				
				System.out.println("Enter the second argument of the query: ");
				String arg2 = scanIn.nextLine();
				if (Utils.checkCaptialLeading(arg2) && !argumentNames.contains(arg2))
				{
					argumentNames.add(arg2);
				}
				
				System.out.println("Enter the third argument of the query: ");
				String arg3 = scanIn.nextLine();
				if (Utils.checkCaptialLeading(arg3) && !argumentNames.contains(arg3))
				{
					argumentNames.add(arg3);
				}
				
				qrys.add(getQuery(funcW, arg1, arg2, arg3));

				System.out.println("Do you want to add another query (Y/N): ");
				String yesNo = scanIn.nextLine();

				if (!yesNo.startsWith("Y") && !yesNo.startsWith("y")) {
					moreInput = false;
				}
			}
		} 
		finally {
			if (scanIn != null) {
				scanIn.close();
			}
		}
		
		return qrys;
	}

	public static void printResult(boolean result, Query qry, List<String> argumentNames) {
		System.out.println("The result of your query is: " + result);
		
		if (result) {
			
			/*Map<String, String> values = rule.getValues();
			if (!values.isEmpty()) {
				System.out.println();
				System.out.println("The values of your variables are: ");
				for (String argName : argumentNames)
				{
					if (values.containsKey(argName))
					{
						System.out.println(argName + ": " + values.get(argName));
					}
				}
			}*/
			
			List<Map<String, String>> values = qry.getFinalAnswers();
			List<Map<String, String>> distinctValues = new ArrayList<Map<String, String>>();
			Map<String, String> utilityDic = new HashMap<String, String>();
			
			if (!values.isEmpty() && !values.isEmpty()  && !values.get(0).isEmpty()) {
				
				
				//Remove duplicate answers
				for (Map<String, String> value : values)
				{
					StringBuffer sb = new StringBuffer();
					Map<String, String> tmpValue = new HashMap<String, String>();
					for (String argName : argumentNames)
					{
						if (value.containsKey(argName)) {
							sb.append(value.get(argName)).append("|");
							tmpValue.put(argName, value.get(argName));
						}
					}
					if (!utilityDic.containsKey(sb.toString()) && !tmpValue.isEmpty())
					{
						utilityDic.put(sb.toString(), "");
						distinctValues.add(tmpValue);
					}
					
					
				}
				
				//Print answers
				if (!distinctValues.isEmpty()) {
					System.out.println();
					System.out.println("The values of your variables are: ");
					int ansNum = 1;
					for (Map<String, String> value : distinctValues) {
						if (value.size() == 0)
							continue;
						System.out.println("Answer " + ansNum++ + ":");
						for (String argName : argumentNames) {
							if (value.containsKey(argName)) {
								System.out.println(argName + ": "
										+ value.get(argName));
							}
						}
						System.out.println("======================");

					}
				}
			}
		}
	}

	public static Query getQuery(String arg0, String arg1, String arg2,
			String arg3) {
		ArrayList<String> arguments = new ArrayList<String>();
		arguments.add(arg1);
		arguments.add(arg2);
		arguments.add(arg3);

		Query qry = new Query(arg0, arguments);
		return qry;
	}

	public static List<Fact> getFacts() {
		ArrayList<Fact> facts = new ArrayList<Fact>();
		facts.add(getFact("edge", "david", "isa", "diabetics"));
		facts.add(getFact("edge", "diabetics", "shouldAvoid", "sugar"));
		facts.add(getFact("edge", "candy", "contains", "sugar"));
		facts.add(getFact("edge", "snickers", "ako", "candy"));
		return facts;
	}

	public static Fact getFact(String arg0, String arg1, String arg2,
			String arg3) {
		ArrayList<String> argList = new ArrayList<String>();
		argList.add(arg1);
		argList.add(arg2);
		argList.add(arg3);

		return new Fact(arg0, argList);
	}

}
