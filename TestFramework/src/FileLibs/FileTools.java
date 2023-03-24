package FileLibs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import static ReportLibs.ReportTools.printLog;

public class FileTools {
	/**
	 * This method reads a text file into a hashmap
	 * @param settingFile: file path
	 * @throws Exception 
	 * 
	 */
	public static HashMap<String, String> readTextFile(String settingFile) 
			throws IOException
	{
		String str;
		HashMap <String, String> map= new HashMap<String, String>();
		File file = new File(settingFile); 
		if (!file.exists())
		{
			printLog(settingFile + " was not found in the root folder");
			return map;
		}
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file)); 
	    while ((str = bufferedReader.readLine()) != null) 
	    {
	    	if (!str.isEmpty() && ! str.startsWith("#"))
	    	{
	    		map.put(str.split("=")[0].trim(),str.split("=")[1].trim());
	    	}
	    } 
	    bufferedReader.close();
		return map;
	}
}
