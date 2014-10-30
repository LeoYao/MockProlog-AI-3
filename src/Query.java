import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Haiyu Yao
 * @unityId hyao4
 * @studentId 200066531
 * 
 */
public class Query {

	/**
	 * 
	 */
	public Query(String funcW, List<String> args) {
		
		this.funcWord = funcW;
		if (this.funcWord == null)
		{
			this.funcWord = "";
		}
		
		arguments = new ArrayList<String>();
		for (String s : args) {
			arguments.add(s);
		}
	}
	
	public List<String> getArguments()
	{
		return arguments;
	}
	
	public String getFuncWord()
	{
		return funcWord;
	}
	
	//public Map<String, String> getValues()
	//{
	//	if (values == null)
	//	{
	//		values = new HashMap<String, String>();
	//	}
	//	
	//	return values;
	//}
	
	public List<AnswerValue> getAnswers()
	{
		return answers;
	}
	
	public void addAnswer(Map<String, String> value)
	{
		AnswerValue ans = new AnswerValue(value);
		
		answers.add(ans);
	}
	
	public AnswerValue getCurrentAnswer()
	{
		return answers.get(answers.size() - 1);
	}
	
	public void removeCurrentAnswer()
	{
		answers.remove(answers.size() - 1);
	}
	
	//public void clearValues()
	//{
	//	values = new HashMap<String,String>();
	//}
	
	public List<Map<String, String>> getFinalAnswers()
	{
		List<Map<String, String>> tmpAnswers = new ArrayList<Map<String, String>>();
		if (answers != null && answers.size() > 0)
		{
			for (AnswerValue ans : answers)
			{
				tmpAnswers.addAll(ans.getFinalAnswers());
			}
		}
		
		return tmpAnswers;
	}
	
	private String funcWord = "";
	private List<String> arguments;

	//private Map<String, String> values; 
	private List<AnswerValue> answers = new ArrayList<AnswerValue>();
}
