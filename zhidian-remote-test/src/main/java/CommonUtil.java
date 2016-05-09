
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;


public class CommonUtil {
	
	/**
	 * 集合判断是否为空
	 * @param collection
	 * @return
	 */
	public static boolean collectionIsNull(Collection collection) {
		if(collection == null || collection.size() == 0) {
			return true;
		}
		return false;
	}
}
