/**
 * MilCorp
 * Mouloud Hamdidouche
 * December, 2018
*/
package InitLibs;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import FileLibs.FileTools;

public class InitTools {
	private static String rootFolder;
	private static String testCaseName;
	protected static String testCaseDescription;
	private static String actualResultFolder;
	private static String libFolder;
	private static HashMap<String, String> configInfos;
	private static int timeOut;
	public static String outputResultFolder;
	private static String inputDataFolder;
	protected static String testerName;
	/**
	 * This method initialize the framework
	 * @exception IOException
	 */
	protected InitTools() throws IOException
	{
		actualResultFolder = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss").format(new Date());
		setRootFolder(System.getProperty("user.dir"));
		setLibFolder("C:/libs");
		setConfigInfos(FileTools.readTextFile(getRootFolder()+"/settings.txt"));
		outputResultFolder = getRootFolder()+ "/output_results/"+ actualResultFolder;
		inputDataFolder = getRootFolder()+ "/input_data";
		testerName = System.getProperty("user.name");
		setTesterName(testerName);
	}
	public static String getActualResultFolder() {
		return actualResultFolder;
	}
	public static void setActualResultFolder(String actualRsltFolder) {
		actualResultFolder = actualRsltFolder;
	}
	public static String getTesterName() {
		return testerName;
	}
	public static  void setTesterName(String tstrName) {
		testerName = tstrName;
	}
	public String getTestCaseDescription() {
		return testCaseDescription;
	}
	public static void setTestCaseDescription(String tcdesc) {
		testCaseDescription = tcdesc;
	}
	public static void setTestCaseName(String testName) {
		testCaseName = testName;
	}
	public static String getTestCaseName() {
		return testCaseName;
	}
	public static String getInputDataFolder() {
		System.out.println(inputDataFolder);
		return inputDataFolder;
	}
	public static String getOutputResultFolder() {
		return outputResultFolder;
	}
	public static int getTimeOut() {
		return timeOut;
	}
	public static int setTimeOut(int newOut) {
		int curretTimeOut = getTimeOut();
		timeOut = newOut;
		return curretTimeOut;
	}
	public static String getRootFolder() {
		return rootFolder;
	}
	public void setRootFolder(String rootFolder) {
		InitTools.rootFolder = rootFolder;
	}
	public String getLibFolder() {
		return libFolder;
	}
	public void setLibFolder(String libFolder) {
		InitTools.libFolder = libFolder;
	}
	public HashMap<String, String> getConfigInfos() {
		return configInfos;
	}
	public void setConfigInfos(HashMap<String, String> configInfos) {
		InitTools.configInfos = configInfos;
	}
}
