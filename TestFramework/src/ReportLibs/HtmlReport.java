package ReportLibs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import InitLibs.InitTools;

public class HtmlReport extends InitTools{
	private static String testSuiteName;
	private static String environmentName;
	private static int totalTcs;
	private static int tcsPassed;
	private static String title;
	private static boolean tcStatus;
	private Date startingDate;
	private static Date EndingDate;
	private static long  tcExecutionTime, suiteExecutionTime;
	private static int stepNumber =0;
	public static ArrayList<Map<String, String>> testCaseSteps = 
			new ArrayList<Map<String, String>>();
	public static ArrayList<Map<String, String>> testCasesExecuted = 
			new ArrayList<Map<String, String>>();
	/**
	 * This Method adds a step to the HTML report
	 * @param stepDesc:step descrition
	 * @param stepExpectResult: step expected result
	 * @param stepActualResult: step actual result
	 * @param StepVpStep: step or VP
	 * @param StepPassFail: pass or fail
	 * @param StepSs: error message/screen shot
	 * @throws IOException 
	 * 
	 */
	public static void addHtmlStep(String stepDesc, 
							String stepExpectResult, 
							String stepActualResult, 
							String StepVpStep,
							String StepPassFail,
							String StepSs)
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("stepNumber", Integer.toString(getStepNumber()));
		map.put("stepDesc", stepDesc);
		map.put("stepExpectResult", stepExpectResult);
		map.put("stepActualResult", stepActualResult);
		map.put("StepVpStep", StepVpStep);
		map.put("StepPassFail", StepPassFail.replace("p", "P").replace("f", "F"));
		map.put("StepSs", StepSs);
		testCaseSteps.add(map);
	}
	
	/**
	 * This Method adds a step to the HTML report
	 * @param stepDesc:step descrition
	 * @param stepExpectResult: step expected result
	 * @param stepActualResult: step actual result
	 * @param StepVpStep: step or VP
	 * @param StepPassFail: pass or fail
	 * @param StepSs: error message/screen shot
	 * @throws IOException 
	 * 
	 */
	public static void addHtmlStepTitle(String title, 
										String titleType)
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("stepNumber", Integer.toString(getStepNumber()));
		map.put("stepDesc", title);
		map.put("stepExpectResult", "");
		map.put("stepActualResult", "");
		map.put("StepVpStep", "");
		map.put("StepPassFail", titleType);
		map.put("StepSs", "");
		testCaseSteps.add(map);
	}

	/**
	 * This Method creates HTML Report for test suite
	 * @throws IOException 
	 * 
	 */
	@SuppressWarnings("deprecation")
	public static void buildHtmalReportForTestSuite() throws IOException
	{
		 SimpleDateFormat sdFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");  
		 Date now = new Date();  
		String templateString = readTemplateFile(getInputDataFolder()+"/template/suiteTemplate.html");
		StringBuilder htmlBody =   new StringBuilder();
		String line;
		int minutes=0, hours=0;
		int seconds = (int) suiteExecutionTime / 1000;
	    hours = seconds / 3600;
	    minutes = (seconds % 3600) / 60;
	    seconds = (seconds % 3600) % 60;
	    String hour = (hours<10)?"0"+hours:""+hours;
	    String minute = minutes<10?"0"+minutes:""+minutes;
	    String second = seconds<10?"0"+seconds:""+seconds;
		String tcName = null, tcStatus = null;
		for (Map<String, String> entry : testCasesExecuted) 
		{
		    for ( String key : entry.keySet()) {
		    	 tcName = key;
		         tcStatus = entry.get(key);
		    }
		    line = "<tr><td><a href = 'html/"+tcName+".html"+"'>"+tcName+"</a></td>"
					+ "<td style='text-align: center;'><span class='~stepsts~'>".replace("~stepsts~", tcStatus)
					+ tcStatus+"</span></td></tr>";
			htmlBody.append(line);
		}
		templateString = templateString.replace("~TestSuiteName~", testSuiteName);
		templateString = templateString.replace("~EnvironmentName~", environmentName);
		templateString = templateString.replace("~ExecutedBy~", testerName);
		templateString = templateString.replace("~ExecutedDateTime~", sdFormatter.format(now));
		templateString = templateString.replace("~ExecutionDuration~", hour+" : "+minute+" : "+second);
		templateString = templateString.replace("~TestCaseNumber~", ""+totalTcs);
		templateString = templateString.replace("~TestCaseNumberPassed~", ""+tcsPassed);
		templateString = templateString.replace("~TestCaseNumberFailed~", ""+(totalTcs - tcsPassed));
		templateString = templateString.replace("~Body~", htmlBody.toString());
		FileUtils.writeStringToFile(new File(getOutputResultFolder()+"/results.html"), templateString);
	}
	
	/**
	 * This Method creates HTML Report for test case
	 * @throws IOException 
	 * 
	 */
	@SuppressWarnings("deprecation")
	public static  void buildHtmlReportForTestCase() throws IOException 
	{
		//SimpleDateFormat sdFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");  
		//Date now = new Date();  
		int  minutes=0, hours=0;
		int seconds = (int) tcExecutionTime / 1000;
	    hours = seconds / 3600;
	    minutes = (seconds % 3600) / 60;
	    seconds = (seconds % 3600) % 60;
	    String hour = (hours<10)?"0"+hours:""+hours;
	    String minute = minutes<10?"0"+minutes:""+minutes;
	    String second = seconds<10?"0"+seconds:""+seconds;
		String templateString = readTemplateFile(getInputDataFolder()+"/template/tcTemplate.html");
		StringBuilder htmlBody =   new StringBuilder();
		String line;
		for (int i=0; i < testCaseSteps.size(); i++)
		{
			if(testCaseSteps.get(i).get("StepPassFail").equalsIgnoreCase("Title"))
			{
				line = "<tr>"
						+ "<td colspan='7' class='Title'>"
						+  testCaseSteps.get(i).get("stepDesc")+"</td>"
						+ "</tr>"; 
			}
			/*else if(testCaseSteps.get(i).get("StepPassFail").equalsIgnoreCase("Warning"))
			{
				line = "<tr class='Warning'>"
						+ "<td colspan='7' class='Warning'>"
						+  testCaseSteps.get(i).get("stepDesc")+"</td>"
						+ "</tr>"; 
			}*/
			else
			{
					line = "<tr id='"+testCaseSteps.get(i).get("StepPassFail")+"'>"
					+ "<td>"+testCaseSteps.get(i).get("stepNumber")+"</td>"
					+ "<td>"+testCaseSteps.get(i).get("stepDesc")+"</td>"
					+ "<td>"+testCaseSteps.get(i).get("stepExpectResult")+"</td>"
					+ "<td>"+testCaseSteps.get(i).get("stepActualResult")+"</td>"
					+ "<td>"+testCaseSteps.get(i).get("StepVpStep")+"</td>"
					+ "<td><span class='~stepsts~'>".replace("~stepsts~", testCaseSteps.get(i).get("StepPassFail"))
					+ testCaseSteps.get(i).get("StepPassFail")+"</span></td>"
					+ "<td>"+testCaseSteps.get(i).get("StepSs")+"</td>"
					+ "</tr>";
			}
			htmlBody.append(line);
		}
		templateString = templateString.replace("~TestCaseName~", title);
		templateString = templateString.replace("~TestCaseDescription~", testCaseDescription);
		templateString = templateString.replace("~TestStatus~", tcStatus ? "Pass":"Fail");
		
		templateString = templateString.replace("~TestDuration~", hour+" : "+minute+" : "+second);
		//templateString = templateString.replace("~ExecutedBy~", testerName);
		templateString = templateString.replace("~Body~", htmlBody.toString());
		FileUtils.writeStringToFile(new File(getOutputResultFolder()+"/html/"+title+".html"), templateString);
	}
	
	/**
	 * This Method read template file
	 * @param file: File name
	 * @throws IOException 
	 * 
	 */
	private static String readTemplateFile(String file) throws IOException {
	    BufferedReader reader = new BufferedReader(new FileReader (file));
	    String         line = null;
	    StringBuilder  stringBuilder = new StringBuilder();
	    String         ls = System.getProperty("line.separator");

	    try {
	        while((line = reader.readLine()) != null) {
	            stringBuilder.append(line);
	            stringBuilder.append(ls);
	        }

	        return stringBuilder.toString();
	    } finally {
	        reader.close();
	    }
	}
	
	/**
	 * This Method adds step to HTML report
	 * @param stepName: step's name
	 * @param steptype: step's type
	 * @param stepAction: step's action
	 * @param stepStatus: step's status
	 * 
	*/
	public static void addStepToHtmlReport(String stepName, 
										   String steptype, 
										   String stepAction,
										   String stepStatus)
	{
		java.util.Date date = new java.util.Date();
		Timestamp time = new Timestamp(date.getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String strTime  = dateFormat.format(time);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("stepNumber", Integer.toString(getStepNumber()));
		setStepNumber(getStepNumber()+1);
		map.put("stepName", stepName);
		map.put("stepType", steptype);
		map.put("stepTime", strTime);
		map.put("StepAction", stepAction);
		map.put("StepStatus", stepStatus);
		testCaseSteps.add(map);
	}
	
	/**
	 * This Method adds screen shot step to HTML report
	 * @param stepName: step's name
	 * @param linkDesc: step's description
	 * @param ssPath: step's screen shots path
	 * 
	*/
	public static void addLinkStepToHtmlReport(String stepName, 
											   String linkDesc,
											   String ssPath)
	{
		java.util.Date date = new java.util.Date();
		Timestamp time = new Timestamp(date.getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String strTime = dateFormat.format(time);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("stepNumber", Integer.toString(getStepNumber()));
		setStepNumber(getStepNumber()+1);
		map.put("stepName", stepName);
		map.put("stepType", "Screen Shot");
		map.put("stepTime", strTime);
		map.put("StepAction", "<a href = '"+ssPath+"'  target='_blank' >"+linkDesc+"</a>");
		map.put("StepStatus", "sshot");
		testCaseSteps.add(map);
	}
	
	/**
	 * This Method adds test case to test suite
	 * @param tcName: test case name
	 * @param testCaseStatus: test case status
	 * 
	*/
	public static void addTestCaseToSuite(String tcName, 
										  boolean testCaseStatus)
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(tcName, testCaseStatus?"Pass":"Fail");
		testCasesExecuted.add(map);
		if(testCaseStatus)
		{
			tcsPassed++;
		}
	}
	
	public HtmlReport() throws IOException {
	}
	public String getEnvironmentName() {
		return environmentName;
	}
	public static void setEnvironmentName(String envName) {
		environmentName = envName;
	}
	public String getTestSuiteName() {
		return testSuiteName;
	}
	public static void setTestSuiteName(String suiteName) {
		testSuiteName = suiteName;
	}
	public int getTotalTcs() {
		return totalTcs;
	}
	public static void setTotalTcs(int total) {
		totalTcs = total;
	}
	public int getTcsPassed() {
		return tcsPassed;
	}
	public static void setTcsPassed(int tcPass) {
		tcsPassed = tcPass;
	}
	public String getTitle() {
		return title;
	}
	public static void setTitle(String tcName) {
		title = tcName;
	}
	public boolean isTcStatus() {
		return tcStatus;
	}
	public static void setTcStatus(boolean tcSts) {
		tcStatus = tcSts;
	}
	public Date getStartingDate() {
		return startingDate;
	}
	public void setStartingDate(Date startingDate) {
		this.startingDate = startingDate;
	}
	public Date getEndingDate() {
		return EndingDate;
	}
	public void setEndingDate(Date endingDate) {
		EndingDate = endingDate;
	}
	public long getSuiteExecutionTime() {
		return suiteExecutionTime;
	}
	public static void setSuiteExecutionTime(long suiteExeT) {
		suiteExecutionTime = suiteExeT;
	}
	
	public long getTcExecutionTime() {
		return tcExecutionTime;
	}
	public static void setTcExecutionTime(long tcExeT) {
		tcExecutionTime = tcExeT;
	}
	public static int getStepNumber() {
		stepNumber = stepNumber+1;
		return stepNumber;
	}
	public static void setStepNumber(int stpNumber) {
		stepNumber = stpNumber;
	}
}
