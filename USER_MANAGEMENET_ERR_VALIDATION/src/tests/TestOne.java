package tests;
import static GuiLibs.GuiTools.guiMap;
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
import libs.UserManagementLib;
public class TestOne {
	public static GuiTools guiTools;
	HashMap<String, String> mapConfInfos;
	String browserType;
	static XlsxTools xlsxTools;
	static ArrayList<LinkedHashMap<String, String>> scenarios, data, dataByScenario;
	ArrayList<LinkedHashMap<String, String>> guiPool;
	public static boolean testCaseStatus;
	public static Timestamp startTime, suiteStartTime;
	public static Timestamp endTime;
	public static String  dataPoolPath ;
	public static boolean logged = false;
	public static Calendar cal = Calendar.getInstance();
	public static void main(String[] args) throws Exception 
	{
		guiTools = new GuiTools();
		xlsxTools = new XlsxTools();
		//userManagementLib = new UserManagementLib();
		TestNG testng = new TestNG();
		List<String> suites = Lists.newArrayList();
		dataPoolPath = InitTools.getInputDataFolder()+"\\datapool\\User_Management_Err_Validation.xlsx";
		scenarios = XlsxTools.readXlsxSheetInOrderAndFilter(dataPoolPath, "scenarios", "Active=TRUE");
		String testNgPath = InitTools.getRootFolder()+"\\testng.xml";
		buildTestNgFromDataPool(scenarios, testNgPath);
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
		String guiMapFilePath = InitTools.getInputDataFolder()+"\\script\\gui_map.xlsx";
		guiPool = XlsxTools.readXlsxSheetInOrderAndFilter(guiMapFilePath, "guiMap", "");
		guiMap = XlsxTools.readGuiMap(guiPool);
		HtmlReport.setTestSuiteName(mapConfInfos.get("project_name"));
		HtmlReport.setEnvironmentName(mapConfInfos.get("env_name"));
		HtmlReport.setTotalTcs(scenarios.size());
		java.util.Date date = new java.util.Date();
		suiteStartTime = new Timestamp(date.getTime());
		guiTools.openBrowser(browserType);
		UserManagementLib.loginUserManagement(mapConfInfos.get("url"), "", "");
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
	/**
	 * This method is used for first name validation
	*/
	@Test(enabled = true, priority=1)
	void Validate_First_Name() throws Exception
	{
		printLog("Validate_First_Name "); 	
		ArrayList<LinkedHashMap<String, String>> row = XlsxTools.filterArrayList(scenarios, "Test_Case_Name=Validate_First_Name");
		data = XlsxTools.readXlsxSheetInOrderAndFilter(dataPoolPath, "data", "Test_Case_Name=Validate_First_Name");
		GuiTools.setTestCaseName(row.get(0).get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get(0).get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		
		testCaseStatus = UserManagementLib.validateFiledsErrors("First Name", data);
	}
	/**
	 * This method is used for last name validation
	*/
	@Test(enabled = true, priority=2)
	void Validate_Last_Name() throws Exception
	{
		printLog("Validate_Last_Name"); 	
		ArrayList<LinkedHashMap<String, String>> row = XlsxTools.filterArrayList(scenarios, "Test_Case_Name=Validate_Last_Name");
		data = XlsxTools.readXlsxSheetInOrderAndFilter(dataPoolPath, "data", "Test_Case_Name=Validate_Last_Name");
		GuiTools.setTestCaseName(row.get(0).get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get(0).get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		
		testCaseStatus = UserManagementLib.validateFiledsErrors("Last Name", data);
	}
	/**
	 * This method is used for email validation
	*/
	@Test(enabled = true, priority=3)
	void Validate_User_Email() throws Exception
	{
		printLog("Validate_User_Email"); 	
		ArrayList<LinkedHashMap<String, String>> row = XlsxTools.filterArrayList(scenarios, "Test_Case_Name=Validate_User_Email");
		data = XlsxTools.readXlsxSheetInOrderAndFilter(dataPoolPath, "data", "Test_Case_Name=Validate_User_Email");
		GuiTools.setTestCaseName(row.get(0).get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get(0).get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		
		testCaseStatus = UserManagementLib.validateFiledsErrors("Email", data);
	}
	/**
	 * This method is used for job title validation
	*/
	@Test(enabled = true, priority=4)
	void Validate_User_Job_Title() throws Exception
	{
		printLog("Validate_User_Job_Title"); 	
		ArrayList<LinkedHashMap<String, String>> row = XlsxTools.filterArrayList(scenarios, "Test_Case_Name=Validate_User_Job_Title");
		data = XlsxTools.readXlsxSheetInOrderAndFilter(dataPoolPath, "data", "Test_Case_Name=Validate_User_Job_Title");
		GuiTools.setTestCaseName(row.get(0).get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get(0).get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		
		testCaseStatus = UserManagementLib.validateFiledsErrors("Job Title", data);
	}
	/**
	 * This method is used for employee type validation
	*/
	@Test(enabled = true, priority=5)
	void Validate_User_Employee_Type() throws Exception
	{
		printLog("Validate_User_Employee_Type"); 	
		ArrayList<LinkedHashMap<String, String>> row = XlsxTools.filterArrayList(scenarios, "Test_Case_Name=Validate_User_Employee_Type");
		data = XlsxTools.readXlsxSheetInOrderAndFilter(dataPoolPath, "data", "Test_Case_Name=Validate_User_Employee_Type");
		GuiTools.setTestCaseName(row.get(0).get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get(0).get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		
		testCaseStatus = UserManagementLib.validateEmployeeType("Employee Type", data);
	}
	/**
	 * This method is used for work phone validation
	*/
	@Test(enabled = true, priority=6)
	void Validate_User_Work_Phone() throws Exception
	{
		printLog("Validate_User_Work_Phone"); 	
		ArrayList<LinkedHashMap<String, String>> row = XlsxTools.filterArrayList(scenarios, "Test_Case_Name=Validate_User_Work_Phone");
		data = XlsxTools.readXlsxSheetInOrderAndFilter(dataPoolPath, "data", "Test_Case_Name=Validate_User_Work_Phone");
		GuiTools.setTestCaseName(row.get(0).get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get(0).get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		
		testCaseStatus = UserManagementLib.validateFiledsErrors("Work Phone", data);
	}
	/**
	 * This method is used for mobile phone validation
	*/
	@Test(enabled = true, priority=7)
	void Validate_User_Mobile_Phone() throws Exception
	{
		printLog("Validate_User_Mobile_Phone"); 	
		ArrayList<LinkedHashMap<String, String>> row = XlsxTools.filterArrayList(scenarios, "Test_Case_Name=Validate_User_Mobile_Phone");
		data = XlsxTools.readXlsxSheetInOrderAndFilter(dataPoolPath, "data", "Test_Case_Name=Validate_User_Mobile_Phone");
		GuiTools.setTestCaseName(row.get(0).get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get(0).get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		
		testCaseStatus = UserManagementLib.validateFiledsErrors("Mobile Phone", data);
	}
	
	/**
	 * This method is used for mobile phone validation
	*/
	@Test(enabled = true, priority=7)
	void Validate_User_Office() throws Exception
	{
		printLog("Validate_User_Mobile_Phone"); 	
		ArrayList<LinkedHashMap<String, String>> row = XlsxTools.filterArrayList(scenarios, "Test_Case_Name=Validate_User_Office");
		data = XlsxTools.readXlsxSheetInOrderAndFilter(dataPoolPath, "data", "Test_Case_Name=Validate_User_Office");
		GuiTools.setTestCaseName(row.get(0).get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get(0).get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		testCaseStatus = UserManagementLib.validateOffice("Office", data);
	}
	
	
	/**
	 * This method is used for mobile phone validation
	*/
	@Test(enabled = true, priority=7)
	void Validate_User_Manager() throws Exception
	{
		printLog("Validate_User_Mobile_Phone"); 	
		ArrayList<LinkedHashMap<String, String>> row = XlsxTools.filterArrayList(scenarios, "Test_Case_Name=Validate_User_Manager");
		data = XlsxTools.readXlsxSheetInOrderAndFilter(dataPoolPath, "data", "Test_Case_Name=Validate_User_Manager");
		GuiTools.setTestCaseName(row.get(0).get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get(0).get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		testCaseStatus = UserManagementLib.validateManager("Manager", data);
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

}
