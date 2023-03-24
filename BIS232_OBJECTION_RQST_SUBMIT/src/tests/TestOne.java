package tests;
import static GuiLibs.GuiTools.checkElementExists;
import static GuiLibs.GuiTools.clickElementJs;
import static GuiLibs.GuiTools.enterTextFile;
import static GuiLibs.GuiTools.failTestCase;
import static GuiLibs.GuiTools.failTestSuite;
import static GuiLibs.GuiTools.guiMap;
import static GuiLibs.GuiTools.highlightElement;
import static GuiLibs.GuiTools.holdSeconds;
import static GuiLibs.GuiTools.navigateTo;
import static GuiLibs.GuiTools.scrollToElement;
import static GuiLibs.GuiTools.setBrowserTimeOut;
import static GuiLibs.GuiTools.testCaseStatus;
import static GuiLibs.GuiTools.updateHtmlReport;
import static ReportLibs.ReportTools.printLog;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.testng.TestNG;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.collections.Lists;
import GuiLibs.GuiTools;
import InitLibs.InitTools;
import OfficeLibs.XlsxTools;
import ReportLibs.HtmlReport;
import libs.BisFormLib;

public class TestOne {
	public static GuiTools guiTools;
	HashMap<String, String> mapConfInfos;
	String browserType;
	static XlsxTools xlsxTools;
	static ArrayList<LinkedHashMap<String, String>> scenarios, dataPoolStep1, dataPoolStep2, 
	dataPoolStep3, dataPoolStep4, dataPoolStep5;
	ArrayList<LinkedHashMap<String, String>> guiPool;
	static BisFormLib bisFormLib;
	//public static boolean testCaseStatus;
	public static Timestamp startTime, suiteStartTime;
	public static Timestamp endTime;
	public static boolean logged = false;
	public static Calendar cal = Calendar.getInstance();
	public static void main(String[] args) throws Exception 
	{
		guiTools = new GuiTools();
		xlsxTools = new XlsxTools();
		bisFormLib = new BisFormLib();
		//TestListenerAdapter tla = new TestListenerAdapter();
		TestNG testng = new TestNG();
		List<String> suites = Lists.newArrayList();
		String dataPoolPath = InitTools.getInputDataFolder()+"\\datapool\\OBJECTION_REQUEST.xlsx";
		scenarios = XlsxTools.readXlsxSheetInOrderAndFilter(dataPoolPath, "Scenarios", "Active=TRUE");
		dataPoolStep1  = XlsxTools.readXlsxSheetInOrderAndFilter(dataPoolPath, "Step 1", "Active=TRUE");
		dataPoolStep2  = XlsxTools.readXlsxSheetInOrderAndFilter(dataPoolPath, "Step 2", "Active=TRUE");
		dataPoolStep3  = XlsxTools.readXlsxSheetInOrderAndFilter(dataPoolPath, "Step 3", "Active=TRUE");
		dataPoolStep4  = XlsxTools.readXlsxSheetInOrderAndFilter(dataPoolPath, "Step 4", "Active=TRUE");
		dataPoolStep5  = XlsxTools.readXlsxSheetInOrderAndFilter(dataPoolPath, "Step 5", "Active=TRUE");
		String testNgPath = InitTools.getRootFolder()+"\\testng.xml";
		suites.add(testNgPath);//path to xml..
		testng.setTestSuites(suites);
		testng.run();
	}
	@BeforeClass
	void beforeClass() throws IOException
	{
		printLog("Executing Before class");
		GuiTools.guiMap = new LinkedHashMap<String, LinkedHashMap<String, String>>();
		mapConfInfos = guiTools.getConfigInfos();
		browserType = mapConfInfos.get("browser_type");
		String guiMapFilePath = InitTools.getInputDataFolder()+"\\script\\gui_map.xlsx";
		guiPool = XlsxTools.readXlsxSheetInOrderAndFilter(guiMapFilePath, "guiMap", "");
		guiMap = XlsxTools.readGuiMap(guiPool);
		HtmlReport.setTestSuiteName(mapConfInfos.get("project_name"));
		HtmlReport.setEnvironmentName(mapConfInfos.get("env_name"));
		HtmlReport.setTotalTcs(scenarios.size());
		java.util.Date date = new java.util.Date();
		suiteStartTime = new Timestamp(date.getTime());
	}
	@AfterClass
	void afterClass() throws IOException
	{
		printLog("Executing After class");
		java.util.Date date = new java.util.Date();
		endTime = new Timestamp(date.getTime());
		HtmlReport.setSuiteExecutionTime(endTime.getTime() - suiteStartTime.getTime());
		HtmlReport.buildHtmalReportForTestSuite();
		guiTools.closeBrowser();
	}
	@BeforeMethod
	public static void beforeMethod() throws IOException
	{
		printLog("beforeMethod()");
		testCaseStatus = true;
		printLog("beforeMethod()");
		java.util.Date date = new java.util.Date();
		startTime = new Timestamp(date.getTime());
	}
	@AfterMethod
	public void afterMethod() throws IOException
	{
		printLog("afterMethod()");
		java.util.Date date = new java.util.Date();
		endTime = new Timestamp(date.getTime());
	    printLog(GuiTools.getTestCaseName());
	    HtmlReport.setTitle(GuiTools.getTestCaseName());
	    HtmlReport.setTcStatus(testCaseStatus);
	    HtmlReport.setTcExecutionTime(endTime.getTime() - startTime.getTime());
	    HtmlReport.buildHtmlReportForTestCase();
	    HtmlReport.addTestCaseToSuite(GuiTools.getTestCaseName(), testCaseStatus);
	    HtmlReport.testCaseSteps.clear();
	    HtmlReport.setStepNumber(0);
	    if (GuiTools.tearDown)
	    {
			endTime = new Timestamp(date.getTime());
			HtmlReport.setSuiteExecutionTime(endTime.getTime() - suiteStartTime.getTime());
	    	HtmlReport.buildHtmalReportForTestSuite();
			guiTools.closeBrowser();
			System.exit(0);
	    }
	}
	@DataProvider(name = "fetchingData")
	public static Object[][] fetchData() 
	{
		Object obj [][]= new  Object[scenarios.size()][2];
		int i = 0;
		for (HashMap<String, String> map : scenarios)
		{
			obj[i][0] = i;
			obj[i][1] =  map;
			i++;
		}
		return (Object[][]) obj;
  }
	/**
	 * This method is validation of all scenarios
	*/
	 @Test(dataProvider = "fetchingData")
	void validate(int i, LinkedHashMap<String, String> row) throws Exception
	{
		String scenarioName = row.get("Scenarios");
		System.out.println(i);
		System.out.println(row);
		GuiTools.setTestCaseName(row.get("Scenarios"));
		GuiTools.setTestCaseDescription(row.get("Scenarios"));
		printLog(GuiTools.getTestCaseName());
		String url = mapConfInfos.get("url");
		String user = mapConfInfos.get("user_name");
		String password = mapConfInfos.get("password");		
		if (!logged) 
		{
			guiTools.openBrowser(browserType);
			navigateTo(url);
			if(! checkElementExists(guiMap.get("Home")))
			{
				failTestSuite("Login to BIS 232 App", "User is able to login", 
					"Not as expected", "Step", "fail", "Login failed");
			}else
			{
				highlightElement(guiMap.get("Home"), "green");
				holdSeconds(2);
				updateHtmlReport("Login to BIS232 App",  "User able to login", "As expected", 
						"Step", "pass", "Login to BIS 232");
				logged = true;
			}
		}
		else
		{
			clickElementJs(guiMap.get("Home"));
		}
		holdSeconds(1);
		BisFormLib.searchExclusiveRequestById(row.get("Request_Id"));
		int currentWait = setBrowserTimeOut(2);
		/*if (!logged) 
		{*/
		logged = BisFormLib.loginToBis(url, user, password);
		/*}*/
		setBrowserTimeOut(currentWait);
		String fileName = "";
		if(!row.get("File_Name").equals(""))
		{
			if(!row.get("File_Name").endsWith(".pdf"))
			{
				testCaseStatus = false;
				failTestCase("Submission", "The given file for upload needs to be PDF file", 
						"Not as expected", "VP", "fail", "Submission");
			}
			else
			{
				fileName = InitTools.getInputDataFolder()+"/input_files/"+row.get("File_Name");
				File file = new File(fileName);
				if (!file.exists())
				{
					testCaseStatus = false;
					failTestCase("Submission", "The given file for upload should be available in input folder", 
							"Not as expected", "VP", "fail", "Submission");
				}
			}
		}
		LinkedHashMap<String, String> step1Row = getTestCaseInfo(dataPoolStep1, scenarioName);
		if(step1Row != null)
		BisFormLib.fillUpStepOne(step1Row);
		LinkedHashMap<String, String> step2Row = getTestCaseInfo(dataPoolStep2, scenarioName);
		if(step2Row != null)
		BisFormLib.fillUpStepTwo(step2Row);
		LinkedHashMap<String, String> step3Row = getTestCaseInfo(dataPoolStep3, scenarioName);
		if(step3Row != null)
		BisFormLib.fillUpStepThree(step3Row);
		LinkedHashMap<String, String> step4Row = getTestCaseInfo(dataPoolStep4, scenarioName);
		if(step4Row != null)
		BisFormLib.fillUpStepFour(step4Row);
		LinkedHashMap<String, String> step5Row = getTestCaseInfo(dataPoolStep5, scenarioName);
		if(step5Row != null)
		BisFormLib.fillUpStepFive(step5Row);
		if (!BisFormLib.previewBisForm())
		{
			testCaseStatus = false;
			updateHtmlReport("Submission", "User is not able to submit form", 
					"Not as expected", "VP", "fail", "Submission");
		}else
		{
			String timeToUpload = "";
			clickElementJs(guiMap.get("submitForm"));
			holdSeconds(2);
			if(!row.get("File_Name").equals(""))
			{
				scrollToElement(guiMap.get("fileUploadButton"));
				enterTextFile(guiMap.get("fileUploadButton"), fileName);
				Date dateUpStart = new Date();
				clickElementJs(guiMap.get("fileUploadFileButton"));
				Date dateUpEnd = new Date();
				long diff = dateUpEnd.getTime() - dateUpStart.getTime();
				timeToUpload = "Time to upload: "+getTimeToUpload(diff);
				if(! checkElementExists(guiMap.get("uploadSuccess")))
				{
					logged= false;
					testCaseStatus = false;
					failTestCase("Submission", "The given file should be uploaded correctly. "+timeToUpload, 
							"Not as expected", "VP", "fail", "Submission");
				}
			}
			clickElementJs(guiMap.get("ButtonDone"));
			holdSeconds(2);
			updateHtmlReport("Submission", "User is able to submit the form. "+timeToUpload, 
					"As expected", "VP", "pass", "Submission");
		}
	}

	/**
	 * This method if for getting the current test case information
	*/
	public LinkedHashMap<String, String> getTestCaseInfo(ArrayList<LinkedHashMap<String, String>> dataPool, String scenName)
	{
		for(LinkedHashMap<String, String> map : dataPool)
		{
			if(scenName.equalsIgnoreCase(map.get("Scenarios")))
			{
				return map;
			}
		}
		return null;
	}
	public String getTimeToUpload(long temps)
	{
		int  minutes=0, hours=0;
		int seconds = (int) temps / 1000;
	    hours = seconds / 3600;
	    minutes = (seconds % 3600) / 60;
	    seconds = (seconds % 3600) % 60;
	    String hour = (hours<10)?"0"+hours:""+hours;
	    String minute = minutes<10?"0"+minutes:""+minutes;
	    String second = seconds<10?"0"+seconds:""+seconds;
		return ""+hour+":"+minute+":"+second;
	}
}
