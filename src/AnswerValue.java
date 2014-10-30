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
public class AnswerValue {

	public AnswerValue(Map<String, String> v) {
		this.value.putAll(v);
	}
	
	public Map<String, String> getValue()
	{
		return value;
	}
	
	public void setSubAnswer(List<AnswerValue> subAnses)
	{
		subAnswers.addAll(subAnses);
	}
	
	public List<Map<String, String>> getFinalAnswers()
	{
		List<Map<String, String>> answers = new ArrayList<Map<String, String>>();
		if (subAnswers != null && subAnswers.size() > 0)
		{
			for (AnswerValue subAns : subAnswers)
			{
				answers.addAll(subAns.getFinalAnswers());
			}
		}
		else
		{
			answers.add(value);
		}
		return answers;
	}
	
	private Map<String, String> value = new HashMap<String,String>();
	
	private List<AnswerValue> subAnswers = new ArrayList<AnswerValue>();

}
