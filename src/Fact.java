import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Haiyu Yao
 * @unityId hyao4
 * @studentId 200066531
 * 
 */
public class Fact extends Query {

	public Fact(String funcW, List<String> args) {
		super(funcW, args);
	}

	public boolean match(String funcW, List<String> args,
			Map<String, String> values) {

		String tmpFuncW = funcW;
		if (tmpFuncW == null) {
			tmpFuncW = "";
		}
		if (!this.getFuncWord().equals(tmpFuncW)) {
			return false;
		}

		Iterator<String> itr = args.iterator();
		for (String s : this.getArguments()) {
			if (itr.hasNext()) {
				String arg = itr.next();

				// Var case
				if (Utils.checkCaptialLeading(arg)) {
					if (!values.containsKey(arg)) {
						values.put(arg, s);
						continue;
					}
					else
					{
						arg = values.get(arg);
					}
				}

				// Constant case or Var with value case
				if (!s.equals(arg))
					return false;

			} else {
				return false;
			}
		}

		return true;
	}

	public boolean match(Query qry, Map<String, String> value) {
		
		boolean rst = this.match(qry.getFuncWord(), qry.getArguments(), value);
		if (rst) {
			qry.addAnswer(value);
		}
		return rst;
	}

}
