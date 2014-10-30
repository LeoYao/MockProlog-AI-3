import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * @author Haiyu Yao
 * @unityId hyao4
 * @studentId 200066531
 * 
 */
public class Rule {

	public Rule() {

	}

	public boolean match(List<Query> qrys, Map<String, String> value) {

		List<Query> restQrys = null;
		Query qry = qrys.get(0);
		
		if (qrys.size() > 1) {
			restQrys = qrys.subList(1, qrys.size());
		}
		else
		{
			restQrys = new ArrayList<Query>();
		}

		if (qry.getFuncWord().equals("edge")) {
			return matchEdge(qry, restQrys, value);
		}
		else if  (qry.getFuncWord().equals("value")){
			return matchValue(qry, restQrys, value);
		}
		else{
			return false;
		}
		
	}

	public boolean matchEdge(Query qry, List<Query> restQrys, Map<String, String> value) {

		boolean match = false;
		for (Fact fact : facts)
		{
			//qry.getValues().clear();
			//qry.getValues().putAll(this.getValues());
			
			//Clone the hypothesized value 
			Map<String, String> tmpValue = new HashMap<String,String>();
			tmpValue.putAll(value);
			
			if(fact.match(qry, tmpValue))
			{
				//Save the current value substitution
				//valueStack.push(qry.getValues());
				AnswerValue currentAns = qry.getCurrentAnswer();
				
				if(notEmpty(restQrys) && !this.match(restQrys, currentAns.getValue()))
				{
					qry.removeCurrentAnswer();
					//Abandon the current value substitution
					//valueStack.pop();
				}
				else
				{
					match = true;
					if (restQrys != null && restQrys.size() > 0) {
						Query firstRestQuery = restQrys.get(0);
						currentAns.setSubAnswer(firstRestQuery.getAnswers());
					}
				}
			}
		}
		return match;
	}

	public boolean matchValue(Query qry, List<Query> restQrys, Map<String, String> value) {
		
		if (qry.getArguments().size() != 3)
		{
			return false;
		}
		
		boolean match = false;
		Query tmpQry1  = null;
		Query tmpQry2 = null;
		List<String> tmpArgs1 = null;
		List<String> tmpArgs2 = null;
		
		////////////////////////////////////////////////////
		//value(Node, Slot, Value):- edge(Node, Slot, Value).
		////////////////////////////////////////////////////
		tmpQry1 = new Query("edge", qry.getArguments()); //Construct a general edge query
		
		if (matchEdge(tmpQry1, restQrys, value))
		{
			qry.getAnswers().addAll(tmpQry1.getAnswers());
			match = true;
		}
		
		////////////////////////////////////////////////////
		//value(Node, Slot, Value):- edge(Node, isa, Node1), value(Node1, Slot, Value).
		////////////////////////////////////////////////////
		tmpArgs1 = new ArrayList<String>();
		tmpArgs1.add(qry.getArguments().get(0));
		tmpArgs1.add("isa");
		tmpArgs1.add("Interim_Node" + interimVarNum);
		tmpQry1 = new Query("edge", tmpArgs1); //Construct an isa edge query
		
		//Add an interim query
		tmpArgs2 = new ArrayList<String>();
		tmpArgs2.add("Interim_Node" + interimVarNum);
		tmpArgs2.add(qry.getArguments().get(1));
		tmpArgs2.add(qry.getArguments().get(2));
		tmpQry2 = new Query("value", tmpArgs2);
		restQrys.add(0, tmpQry2);
		
		//Increase the number of interim var in case of duplicate var name
		interimVarNum++;
		if (matchEdge(tmpQry1, restQrys, value))
		{
			qry.getAnswers().addAll(tmpQry1.getAnswers());
			match = true;
		}
		
		//Remove interim query
		restQrys.remove(0);
		interimVarNum--;
		
		////////////////////////////////////////////////////
		//value(Node, Slot, Value):- edge(Node, ako, Node1), value(Node1, Slot, Value).
		////////////////////////////////////////////////////
		tmpArgs1 = new ArrayList<String>();
		tmpArgs1.add(qry.getArguments().get(0));
		tmpArgs1.add("ako");
		tmpArgs1.add("Interim_Node" + interimVarNum);
		tmpQry1 = new Query("edge", tmpArgs1); //Construct an ako edge query
		
		//Add an interim query
		tmpArgs2 = new ArrayList<String>();
		tmpArgs2.add("Interim_Node" + interimVarNum);
		tmpArgs2.add(qry.getArguments().get(1));
		tmpArgs2.add(qry.getArguments().get(2));
		tmpQry2 = new Query("value", tmpArgs2);
		restQrys.add(0, tmpQry2);
		
		//Increase the number of interim var in case of duplicate var name
		interimVarNum++;
		if (matchEdge(tmpQry1, restQrys, value))
		{
			qry.getAnswers().addAll(tmpQry1.getAnswers());
			match = true;
		}
		//Remove interim query
		restQrys.remove(0);
		interimVarNum--;
		
		////////////////////////////////////////////////////
		//value(Node, shouldAvoid, Node1):- value(Node1, contains, Stuff), value(Node, shouldAvoid, Stuff).
		////////////////////////////////////////////////////
		if (qry.getArguments().get(1).equals("shouldAvoid") || Utils.checkCaptialLeading(qry.getArguments().get(1)))
		{
			tmpArgs1 = new ArrayList<String>();
			tmpArgs1.add(qry.getArguments().get(2));
			tmpArgs1.add("contains");
			tmpArgs1.add("Interim_Stuff" + interimVarNum);
			tmpQry1 = new Query("value", tmpArgs1); //Construct an isa edge query
			
			//Add an interim query
			tmpArgs2 = new ArrayList<String>();
			tmpArgs2.add(qry.getArguments().get(0));
			tmpArgs2.add("shouldAvoid");
			tmpArgs2.add("Interim_Stuff" + interimVarNum);
			tmpQry2 = new Query("value", tmpArgs2);
			restQrys.add(0, tmpQry2);
			
			//Clone the hypothesized value 
			Map<String, String> tmpValue = new HashMap<String,String>();
			tmpValue.putAll(value);
			if (Utils.checkCaptialLeading(qry.getArguments().get(1))) {
				tmpValue.put(qry.getArguments().get(1), "shouldAvoid");

			}
					
			//Increase the number of interim var in case of duplicate var name
			interimVarNum++;
			if (matchValue(tmpQry1, restQrys, tmpValue))
			{
				qry.getAnswers().addAll(tmpQry1.getAnswers());
				match = true;
			}
			//Remove interim query
			restQrys.remove(0);
			interimVarNum--;
		}
		
		return match;
	}

	public Map<String, String> getValues() {
		Map<String, String> values = new HashMap<String, String>();
		Iterator<Map<String, String>> itr = valueStack.iterator();
		while (itr.hasNext()) {
			values.putAll(itr.next());
		}
		return values;
	}

	public Map<String, String> getAnswerValues() {
		Map<String, String> values = this.getValues();
		Map<String, String> finalValues = new HashMap<String,String>();
		//Remove interim vars
		Set<String> keys = values.keySet();
		for (String key : keys)
		{
			if (!key.startsWith("Interim_"))
			{
				finalValues.put(key, values.get(key));
			}
		}
		return finalValues;
	}
	
	public void setFacts(List<Fact> fcts)
	{
		this.facts = new ArrayList<Fact>();
		this.facts.addAll(fcts);
	}
	
	private boolean notEmpty(List<Query> restQrys){
		boolean rst = restQrys != null;
		rst &= restQrys.size() > 0;
		return rst;
	}
	
	private Stack<Map<String, String>> valueStack = new Stack<Map<String,String>>();
	private List<Fact> facts;
	private int interimVarNum = 1;

}
