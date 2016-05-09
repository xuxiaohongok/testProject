
import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class FileContentTest {
	public static void main(String[] args) {
		try {
			String jsonString = readString3();
			System.out.println(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String readString3() {
		StringBuffer str = new StringBuffer();
		File file = new File("D:\\MyEclipseWorkspace\\zhidian-remote-test\\src\\main\\resources\\data.txt");
		
		try {
			List<String> fileStringLineList = FileUtils.readLines(file);
			if(!CommonUtil.collectionIsNull(fileStringLineList)) {
				for(String stringLine : fileStringLineList) {
					str.append(stringLine.trim());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return str.toString();
	}

}
