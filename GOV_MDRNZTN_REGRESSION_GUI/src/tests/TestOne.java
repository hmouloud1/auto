package tests;
import static GuiLibs.GuiTools.guiMap;
import static GuiLibs.GuiTools.holdSeconds;
import static GuiLibs.GuiTools.testCaseStatus;
import static GuiLibs.GuiTools.clickElementJs;
import static ReportLibs.ReportTools.printLog;
import static XmlLibs.XmlTools.buildTestNgFromDataPool;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.testng.TestNG;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.collections.Lists;
import GuiLibs.GuiTools;
import InitLibs.InitTools;
import OfficeLibs.XlsxTools;
import ReportLibs.HtmlReport;
import libs.EventModLib;




public class TestOne {
	
	public static GuiTools guiTools;
	//static HtmlReport htmlReport;
	HashMap<String, String> mapConfInfos;
	String browserType;
	static XlsxTools xlsxTools;
	static ArrayList<LinkedHashMap<String, String>> dataPool;
	ArrayList<LinkedHashMap<String, String>> guiPool;
	static EventModLib adcvdLib;
	//public static boolean testCaseStatus;
	public static Timestamp startTime, suiteStartTime;
	public static Timestamp endTime;
	public static Calendar cal = Calendar.getInstance();
	public boolean loginOn = false;
	public static String previousUser = "", previousPassword="";
	public static void main(String[] args) throws Exception 
	{
		printLog("MainMethod()");
		guiTools = new GuiTools();
		xlsxTools = new XlsxTools();
		adcvdLib = new EventModLib();
		TestNG testng = new TestNG();
		List<String> suites = Lists.newArrayList();
		String dataPoolPath = InitTools.getInputDataFolder()+"/datapool/Regession_TC.xlsx";
		System.out.println("dataPoolPath "+dataPoolPath);
		dataPool  = XlsxTools.readXlsxSheetAndFilter(dataPoolPath, "Test_Cases", "Active=TRUE");
		String testNgTemplate = InitTools.getInputDataFolder()+"/template/testng_template.xml";
		String testNgPath = InitTools.getRootFolder()+"/testng.xml";
		System.out.println("testNgTemplate "+testNgTemplate);
		System.out.println("testNgPath "+testNgPath);
		//build
		buildTestNgFromDataPool(dataPool, testNgPath);
		suites.add(testNgPath);//path to xml..
		testng.setTestSuites(suites);
		testng.run();
	}
	
	@BeforeClass
	void beforeClass() throws Exception
	{
		printLog("Executing Before class");
		GuiTools.guiMap = new LinkedHashMap<String, LinkedHashMap<String, String>>();
		mapConfInfos = guiTools.getConfigInfos();
		browserType = mapConfInfos.get("browser_type");
		String guiMapFilePath = InitTools.getInputDataFolder()+"/script/gui_map.xlsx";
		guiPool = XlsxTools.readXlsxSheetAndFilter(guiMapFilePath, "guiMap", "");
		guiMap = XlsxTools.readGuiMap(guiPool);
		HtmlReport.setTestSuiteName(mapConfInfos.get("project_name"));
		HtmlReport.setEnvironmentName(mapConfInfos.get("env_name"));
		HtmlReport.setTotalTcs(dataPool.size());
		java.util.Date date = new java.util.Date();
		suiteStartTime = new Timestamp(date.getTime());
	}
	@AfterClass
	void afterClass() throws Exception
	{
		printLog("Executing After class");
		java.util.Date date = new java.util.Date();
		endTime = new Timestamp(date.getTime());
		HtmlReport.setSuiteExecutionTime(endTime.getTime() - 
										suiteStartTime.getTime());
		HtmlReport.buildHtmalReportForTestSuite();
		guiTools.closeBrowser();
		//stopRecording();
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
	public void afterMethod() throws Exception
	{
		printLog("afterMethod()");
		java.util.Date date = new java.util.Date();
		endTime = new Timestamp(date.getTime());
	    printLog(GuiTools.getTestCaseName());
	    HtmlReport.setTitle(GuiTools.getTestCaseName());
	    HtmlReport.setTcStatus(testCaseStatus);
	   // printLog(GuiTools.getExecutionTime());
	    HtmlReport.setTcExecutionTime(endTime.getTime() - startTime.getTime());
		//htmlReport.fillThetest();
	    HtmlReport.buildHtmlReportForTestCase();
	    HtmlReport.addTestCaseToSuite(GuiTools.getTestCaseName(), testCaseStatus);
	    HtmlReport.testCaseSteps.clear();
	    HtmlReport.setStepNumber(0);
	    if (GuiTools.tearDown)
	    {
	    	//java.util.Date dateFail = new java.util.Date();
			endTime = new Timestamp(date.getTime());
			HtmlReport.setSuiteExecutionTime(endTime.getTime() - suiteStartTime.getTime());
	    	HtmlReport.buildHtmalReportForTestSuite();
			guiTools.closeBrowser();
		//	stopRecording();
			System.exit(0);
	    }
	}
	/**
	 * This method is for ADCVD case creation and validation
	*/
	@Test(enabled = true, priority=1)
	void Core_Team_Campaigns() throws Exception
	{
		printLog("CVD_Align_With_AD");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_001");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		
		if(!row.get("User_Name").equals(previousUser))
		{
			String url = mapConfInfos.get("url");
			guiTools.openBrowser(browserType);
			loginOn = EventModLib.loginToEventModernization(url, row.get("User_Name"), row.get("Password"));
		}	
		previousUser = row.get("User_Name").trim();
		clickElementJs(guiMap.get("homeObjectLink"));
		holdSeconds(2);
		
	}
	
	/**
	 * This method is for ADCVD case creation and validation
	*/
	@Test(enabled = true, priority=2)
	void Core_Team_Attendee() throws Exception
	{
		printLog("Litigation_Dates_Validation");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_002");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		if(!row.get("User_Name").equals(previousUser))
		{
			String url = mapConfInfos.get("url");
			guiTools.openBrowser(browserType);
			loginOn = EventModLib.loginToEventModernization(url, row.get("User_Name"), row.get("Password"));
		}	
		previousUser = row.get("User_Name").trim();
		clickElementJs(guiMap.get("homeObjectLink"));
		holdSeconds(2);
	}
	/**
	 * This method is for ADCVD case creation and validation
	*/
	@Test(enabled = true, priority=3)
	void Core_Team_Meeting() throws Exception
	{
		printLog("Self_Initiated_Petition_Investigation_Dates");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_003");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		if(!row.get("User_Name").equals(previousUser))
		{
			String url = mapConfInfos.get("url");
			guiTools.openBrowser(browserType);
			loginOn = EventModLib.loginToEventModernization(url, row.get("User_Name"), row.get("Password"));
		}	
		previousUser = row.get("User_Name").trim();
		clickElementJs(guiMap.get("homeObjectLink"));
	
	}
	
	/**
	 * This method is for ADCVD case creation and validation
	*/
	@Test(enabled = true, priority=4)
	void Core_Team_Case() throws Exception
	{
		LinkedHashMap<String, String> arDates, nsrDates;
		printLog("Align_NSR_To_AR");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_004");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		if(!row.get("User_Name").equals(previousUser))
		{
			String url = mapConfInfos.get("url");
			guiTools.openBrowser(browserType);
			loginOn = EventModLib.loginToEventModernization(url, row.get("User_Name"), row.get("Password"));
		}	
		previousUser = row.get("User_Name").trim();
		clickElementJs(guiMap.get("homeObjectLink"));
	}
	
	/**
	 * This method is for ADCVD case creation and validation
	*/
	@Test(enabled = true, priority=5)
	void Core_Team_Organizations() throws Exception
	{
		printLog("Petition_Status_Validation");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_005");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		if(!row.get("User_Name").equals(previousUser))
		{
			String url = mapConfInfos.get("url");
			guiTools.openBrowser(browserType);
			loginOn = EventModLib.loginToEventModernization(url, row.get("User_Name"), row.get("Password"));
		}	
		previousUser = row.get("User_Name").trim();
		clickElementJs(guiMap.get("homeObjectLink"));
		holdSeconds(2);
	}
	
	/**
	 * This method is for ADCVD case creation and validation
	*/
	@Test(enabled = true, priority=6)
	void Core_Team_Contact() throws Exception
	{		
		printLog("Investigation_Status_Validation");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_006");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		if(!row.get("User_Name").equals(previousUser))
		{
			String url = mapConfInfos.get("url");
			guiTools.openBrowser(browserType);
			loginOn = EventModLib.loginToEventModernization(url, row.get("User_Name"), row.get("Password"));
		}
		previousUser = row.get("User_Name").trim();
		clickElementJs(guiMap.get("homeObjectLink"));
		holdSeconds(2);
	}

	/**
	 * This method is admin review status
	*/
	@Test(enabled = true, priority=7)
	void GM_Commercial_Service_Campaigns() throws Exception
	{  
		printLog("Admin_Review_Status_Validation");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_007");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		if(!row.get("User_Name").equals(previousUser))
		{
			String url = mapConfInfos.get("url");
			guiTools.openBrowser(browserType);
			loginOn = EventModLib.loginToEventModernization(url, row.get("User_Name"), row.get("Password"));
		}
		previousUser = row.get("User_Name").trim();
		clickElementJs(guiMap.get("homeObjectLink"));
		holdSeconds(2);
	}
	
	/**
	 * This method is Expedited_Review_Status_Validation status
	*/
	@Test(enabled = true, priority=8)
	void GM_Commercial_Service_Attendee() throws Exception
	{
		printLog("Expedited_Review_Status_Validation");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_008");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		if(!row.get("User_Name").equals(previousUser))
		{
			String url = mapConfInfos.get("url");
			guiTools.openBrowser(browserType);
			loginOn = EventModLib.loginToEventModernization(url, row.get("User_Name"), row.get("Password"));
		}	
		previousUser = row.get("User_Name").trim();
		clickElementJs(guiMap.get("homeObjectLink"));
		holdSeconds(2);
	}
	/**
	 * This method is New_Shipper_Review_Status_Validation status
	*/
	@Test(enabled = true, priority=9)
	void GM_Commercial_Service_Meeting() throws Exception
	{
		printLog("New_Shipper_Review_Status_Validation");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_009");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		if(!row.get("User_Name").equals(previousUser))
		{
			String url = mapConfInfos.get("url");
			guiTools.openBrowser(browserType);
			loginOn = EventModLib.loginToEventModernization(url, row.get("User_Name"), row.get("Password"));
		}
		previousUser = row.get("User_Name").trim();
		clickElementJs(guiMap.get("homeObjectLink"));
		holdSeconds(2);
	}
	
	/**
	 * This method is New_Shipper_Review_Status_Validation status
	*/
	@Test(enabled = true, priority=10)
	void GM_Commercial_Service_Case() throws Exception
	{
		printLog("Changed_Circumstance_Review_Status_Validation");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_010");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		if(!row.get("User_Name").equals(previousUser))
		{
			String url = mapConfInfos.get("url");
			guiTools.openBrowser(browserType);
			loginOn = EventModLib.loginToEventModernization(url, row.get("User_Name"), row.get("Password"));
		}	
		previousUser = row.get("User_Name").trim();
		clickElementJs(guiMap.get("homeObjectLink"));
	}
	/**
	 * This method is New_Shipper_Review_Status_Validation status
	*/
	@Test(enabled = true, priority=11)
	void GM_Commercial_Service_Organizations() throws Exception
	{
		printLog("Anticircumvention_Status_Validation");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_011");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		if(!row.get("User_Name").equals(previousUser))
		{
			String url = mapConfInfos.get("url");
			guiTools.openBrowser(browserType);
			loginOn = EventModLib.loginToEventModernization(url, row.get("User_Name"), row.get("Password"));
		}
		previousUser = row.get("User_Name").trim();
		clickElementJs(guiMap.get("homeObjectLink"));
	}
	
	/**
	 * This method is for scope inquirey status validation
	*/
	@Test(enabled = true, priority=12)
	void GM_Commercial_Service_Contact() throws Exception
	{
		printLog("Scope_Status_Validation");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_012");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		if(!row.get("User_Name").equals(previousUser))
		{
			String url = mapConfInfos.get("url");
			guiTools.openBrowser(browserType);
			loginOn = EventModLib.loginToEventModernization(url, row.get("User_Name"), row.get("Password"));
		}	
		previousUser = row.get("User_Name").trim();
		clickElementJs(guiMap.get("homeObjectLink"));
	}
	
	/**
	 * This method if for getting the current test case information
	*/
	public LinkedHashMap<String, String> getTestCaseInfo(ArrayList<LinkedHashMap<String, String>> dataPool, String tcTagName)
	{
		for(LinkedHashMap<String, String> map : dataPool)
		{
			if(tcTagName.equalsIgnoreCase(map.get("Test_Case_Tag")))
			{
				return map;
			}
		}
		return null;
	}
	/**
	 * This method if for merging data from all objects
	*/
	static ArrayList<LinkedHashMap<String, String>> mergeDataPools(ArrayList<LinkedHashMap<String, String>> dataPool1, 
			ArrayList<LinkedHashMap<String, String>> dataPool2, ArrayList<LinkedHashMap<String, String>> dataPool3, 
			ArrayList<LinkedHashMap<String, String>> dataPool4, ArrayList<LinkedHashMap<String, String>> dataPool5)
	{
		ArrayList<LinkedHashMap<String, String>> dataPoolMerged = new ArrayList<LinkedHashMap<String, String>>();
		for(LinkedHashMap<String, String> map: dataPool1)
		{
		dataPoolMerged.add(map);
		}
		for(LinkedHashMap<String, String> map: dataPool2)
		{
		dataPoolMerged.add(map);
		}
		for(LinkedHashMap<String, String> map: dataPool3)
		{
		dataPoolMerged.add(map);
		}
		for(LinkedHashMap<String, String> map: dataPool4)
		{
		dataPoolMerged.add(map);
		}
		for(LinkedHashMap<String, String> map: dataPool5)
		{
		dataPoolMerged.add(map);
		}
		return dataPoolMerged;
	}
}
