package tests;
import static GuiLibs.GuiTools.guiMap;
import static GuiLibs.GuiTools.holdSeconds;
import static GuiLibs.GuiTools.navigateTo;
import static GuiLibs.GuiTools.testCaseStatus;
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
import libs.AccessLib;

public class TestOne {
	public static GuiTools guiTools;
	//static HtmlReport htmlReport;
	HashMap<String, String> mapConfInfos;
	String browserType;
	static XlsxTools xlsxTools;
	static ArrayList<LinkedHashMap<String, String>> dataPool, eFileHelpmsg, manageAPOAHelpmsg,
	updateProfileHelpmsg, dataPoolErrorMsg, eFileRegisterHelpmsg,userRegisterHelpmsg, manageEOAHelpmsg,checkFiles;
	ArrayList<LinkedHashMap<String, String>> guiPool;
	static AccessLib accessLib;
	//public static boolean testCaseStatus;
	public static Timestamp startTime, suiteStartTime;
	public static Timestamp endTime;
	public static Calendar cal = Calendar.getInstance();
	public boolean loginOn = false;
	public static void main(String[] args) throws Exception 
	{
		guiTools = new GuiTools();
		xlsxTools = new XlsxTools();
		accessLib = new AccessLib();
		TestNG testng = new TestNG();
		List<String> suites = Lists.newArrayList();
		String dataPoolPath = InitTools.getInputDataFolder()+"/datapool/Access_Regression.xlsx";
		System.out.println("dataPoolPath "+dataPoolPath);
		dataPool  = XlsxTools.readXlsxSheetAndFilter(dataPoolPath, "Regression", "Active=TRUE");
		eFileHelpmsg  = XlsxTools.readXlsxSheetAndFilter(dataPoolPath, "E-file Fields Help", "");
		updateProfileHelpmsg  = XlsxTools.readXlsxSheetAndFilter(dataPoolPath, "Update Profile Fileds Help", "");
		eFileRegisterHelpmsg  = XlsxTools.readXlsxSheetAndFilter(dataPoolPath, "E-Filer Register Fileds Help", "");
		userRegisterHelpmsg  = XlsxTools.readXlsxSheetAndFilter(dataPoolPath, "Guest Register Fileds Help", "");
		dataPoolErrorMsg  = XlsxTools.readXlsxSheetAndFilter(dataPoolPath, "Fields Error Validation", "");
		manageEOAHelpmsg  = XlsxTools.readXlsxSheetAndFilter(dataPoolPath, "Manage Entry Of Appearance Help", "");
		manageAPOAHelpmsg  = XlsxTools.readXlsxSheetAndFilter(dataPoolPath, "Manage APO Application Help", "");
		checkFiles  = XlsxTools.readXlsxSheetAndFilter(dataPoolPath, "Check Files", "");
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
	 * This method is E-File creation
	*/
	@Test(enabled = true)
	void Submit_With_Add_Files_Excel() throws Exception
	{
		printLog("Submit_With_Add_Files_Excel");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_001");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		String url = mapConfInfos.get("url");
		String user = mapConfInfos.get("user_name");
		String password = mapConfInfos.get("password");	
		System.out.println(url+"___"+user);
		if (!loginOn)
		{
			guiTools.openBrowser(browserType);
			loginOn = AccessLib.loginToAccess(url, user, password);
		}
		row.put("type", "add more files");
		testCaseStatus = AccessLib.createEFileDocument(row);
	}
	
	/**
	 * This method is for message validation
	*/
	@Test(enabled = true)
	void Submit_With_Similar_Submission_Excel() throws Exception
	{
		printLog("Submit_With_Similar_Submission_Excel");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_002");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		String url = mapConfInfos.get("url");
		String user = mapConfInfos.get("user_name");
		String password = mapConfInfos.get("password");	
		System.out.println(url+"___"+user);

		if (!loginOn)
		{
			guiTools.openBrowser(browserType);
			loginOn = AccessLib.loginToAccess(url, user, password);
		}
		row.put("type", "similar submission");
		testCaseStatus = AccessLib.createEFileDocument(row);
	}
	
	/**
	 * This method is E-File creation
	*/
	@Test(enabled = true)
	void Submit_With_Add_Files_Pdf() throws Exception
	{
		printLog("Submit_With_Add_Files_Pdf");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_003");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		String url = mapConfInfos.get("url");
		String user = mapConfInfos.get("user_name");
		String password = mapConfInfos.get("password");	
		System.out.println(url+"___"+user);
		if (!loginOn)
		{
			guiTools.openBrowser(browserType);
			loginOn = AccessLib.loginToAccess(url, user, password);
		}
		row.put("type", "add more files");
		testCaseStatus = AccessLib.createEFileDocument(row);
	}
	
	/**
	 * This method is for message validation
	*/
	@Test(enabled = true)
	void Submit_With_Similar_Submission_Pdf() throws Exception
	{
		printLog("Submit_With_Similar_Submission_Pdf");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_004");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		String url = mapConfInfos.get("url");
		String user = mapConfInfos.get("user_name");
		String password = mapConfInfos.get("password");	
		System.out.println(url+"___"+user);

		if (!loginOn)
		{
			guiTools.openBrowser(browserType);
			loginOn = AccessLib.loginToAccess(url, user, password);
		}
		row.put("type", "similar submission");
		testCaseStatus = AccessLib.createEFileDocument(row);
	}
	
	/**
	 * This method is for error validation
	*/
	@Test(enabled = true)
	void Submit_As_Manual_Submission() throws Exception
	{
		printLog("Submit_As_Manual_Submission");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_005");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		String url = mapConfInfos.get("url");
		String user = mapConfInfos.get("user_name");
		String password = mapConfInfos.get("password");	
		System.out.println(url+"___"+user);
		if (!loginOn)
		{
			guiTools.openBrowser(browserType);
			loginOn = AccessLib.loginToAccess(url, user, password);
		}
		row.put("type", "manual submission");
		testCaseStatus = AccessLib.createEFileDocument(row);
	}
	
	/**
	 * This method is for message validation
	*/
	@Test(enabled = true)
	void Validate_Efile_Help_Messages() throws Exception
	{
		printLog("Validate_Efile_Help_Messages");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_006");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		String url = mapConfInfos.get("url");
		String user = mapConfInfos.get("user_name");
		String password = mapConfInfos.get("password");	
		System.out.println(url+"___"+user);
		if (!loginOn)
		{
			guiTools.openBrowser(browserType);
			loginOn = AccessLib.loginToAccess(url, user, password);
		}
		row = eFileHelpmsg.get(0);
		testCaseStatus =  AccessLib.validateFieldsEFileHelpMessages(row);
	}
	/**
	 * This method is for error validation
	*/
	@Test(enabled = true)
	void Validate_Efile_Error_Messages() throws Exception
	{
		printLog("Validate_Efile_Error_Messages");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_007");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		String url = mapConfInfos.get("url");
		String user = mapConfInfos.get("user_name");
		String password = mapConfInfos.get("password");	
		System.out.println(url+"___"+user);
		if (!loginOn)
		{
			guiTools.openBrowser(browserType);
			loginOn = AccessLib.loginToAccess(url, user, password);
		}
		testCaseStatus =  AccessLib.ValidateFieldsErrorMessages(dataPoolErrorMsg);
	}
	/**
	 * This method is for update profile fields message validation
	*/
	@Test(enabled = true)
	void Validate_Update_Profile_Help_Messages() throws Exception
	{
		printLog("Validate_Update_Profile_Help_Messages");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_008");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		String url = mapConfInfos.get("url");
		String user = mapConfInfos.get("user_name");
		String password = mapConfInfos.get("password");	
		System.out.println(url+"___"+user);
		if (!loginOn)
		{
			guiTools.openBrowser(browserType);
			loginOn = AccessLib.loginToAccess(url, user, password);
		}
		row = updateProfileHelpmsg.get(0);
		testCaseStatus =  AccessLib.validateFieldsUpdateProfileHelpMessages(row);
	}
	
	/**
	 * This method is for message validation
	*/
	
	//manageEOAHelpmsg  = XlsxTools.readXlsxSheetAndFilter(dataPoolPath, "Manage Entry Of Appearance Help", "");
	//manageAPOAHelpmsg  = XlsxTools.readXlsxSheetAndFilter(dataPoolPath, "Manage APO Application Help", "");
	
	
	@Test(enabled = true)
	void Validate_Manage_Entry_Of_Appearance_Help_Messages() throws Exception
	{
		printLog("Validate_Manage_Entry_Of_Appearance_Help_Messages");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_009");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		String url = mapConfInfos.get("url");
		String user = mapConfInfos.get("user_name");
		String password = mapConfInfos.get("password");	
		System.out.println(url+"___"+user);
		if (!loginOn)
		{
			guiTools.openBrowser(browserType);
			loginOn = AccessLib.loginToAccess(url, user, password);
		}
		String cn = row.get("Case_Number");
		row = manageEOAHelpmsg.get(0);
		testCaseStatus =  AccessLib.validateFieldsManageEntryOfAppearanceHelpMessages(row, cn);
	}
	/**
	 * This method is for message validation
	*/
	@Test(enabled = true)
	void Validate_Manage_APO_Application_Help_Messages() throws Exception
	{
		printLog("Validate_Manage_APO_Application_Help_Messages");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_010");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		String url = mapConfInfos.get("url");
		String user = mapConfInfos.get("user_name");
		String password = mapConfInfos.get("password");	
		System.out.println(url+"___"+user);
		if (!loginOn)
		{
			guiTools.openBrowser(browserType);
			loginOn = AccessLib.loginToAccess(url, user, password);
		}
		String cn = row.get("Case_Number");
		row = manageAPOAHelpmsg.get(0);
		testCaseStatus =  AccessLib.validateFieldsManageApoApplicationHelpMessages(row, cn);
	}
	/**
	 * This method is for message validation
	*/
	@Test(enabled = true)
	void Create_Entry_Of_Appearance() throws Exception
	{
		printLog("Create_Entry_Of_Appearance");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_011");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		String url = mapConfInfos.get("url");
		String user = mapConfInfos.get("user_name");
		String password = mapConfInfos.get("password");	
		System.out.println(url+"___"+user);
		if (!loginOn)
		{
			guiTools.openBrowser(browserType);
			loginOn = AccessLib.loginToAccess(url, user, password);
		}
		testCaseStatus =  AccessLib.createEntryOfAppearance(row);
	}
	/**
	 * This method is for message validation
	*/
	@Test(enabled = true)
	void Create_APO_Application() throws Exception
	{
		printLog("Create_APO_Application");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_012");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		String url = mapConfInfos.get("url");
		String user = mapConfInfos.get("user_name");
		String password = mapConfInfos.get("password");	
		System.out.println(url+"___"+user);
		if (!loginOn)
		{
			guiTools.openBrowser(browserType);
			loginOn = AccessLib.loginToAccess(url, user, password);
		}
		String cn = row.get("Case_Number");
		testCaseStatus =  AccessLib.createApoApplication(row);
	}
	/**
	 * This method is for message validation
	*/
	@Test(enabled = true)
	void Check_Files_For_Efiles() throws Exception
	{
		printLog("Check_Files_For_Efiles");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_013");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		String url = mapConfInfos.get("url");
		String user = mapConfInfos.get("user_name");
		String password = mapConfInfos.get("password");	
		System.out.println(url+"___"+user);
		if (!loginOn)
		{
			guiTools.openBrowser(browserType);
			loginOn = AccessLib.loginToAccess(url, user, password);
		}
		//row = eFileRegisterHelpmsg.get(0);
		testCaseStatus =  AccessLib.checkFilesEfile(checkFiles);
	}
	
	/**
	 * This method is for message validation
	*/
	@Test(enabled = true)
	void Validate_EOA_Fields_Populated() throws Exception
	{
		printLog("Other_Entry_Of_Appearance_Check_Fileds");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_014");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		String url = mapConfInfos.get("url");
		String user = mapConfInfos.get("user_name");
		String password = mapConfInfos.get("password");	
		System.out.println(url+"___"+user);
		if (!loginOn)
		{
			guiTools.openBrowser(browserType);
			loginOn = AccessLib.loginToAccess(url, user, password);
		}
		testCaseStatus =  AccessLib.otherEntryOfAppearanceCheckFields(row);
	}
	
	/**
	 * This method is for message validation
	*/
	@Test(enabled = true)
	void Validate_APO_Fields_Populated() throws Exception
	{
		printLog("Validate_APO_Fields_Populated");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_015");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		String url = mapConfInfos.get("url");
		String user = mapConfInfos.get("user_name");
		String password = mapConfInfos.get("password");	
		System.out.println(url+"___"+user);
		if (!loginOn)
		{
			guiTools.openBrowser(browserType);
			loginOn = AccessLib.loginToAccess(url, user, password);
		}
		testCaseStatus =  AccessLib.otherApoCheckFields(row);
	}
	/**
	 * This method is for message validation
	*/
	@Test(enabled = true)
	void Resubmit_EOA_Under_Other_Entries() throws Exception
	{
		printLog("Resubmit_EOA_Under_Other_Entries");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_016");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		String url = mapConfInfos.get("url");
		String user = mapConfInfos.get("user_name");
		String password = mapConfInfos.get("password");	
		System.out.println(url+"___"+user);
		if (!loginOn)
		{
			guiTools.openBrowser(browserType);
			loginOn = AccessLib.loginToAccess(url, user, password);
		}
		testCaseStatus =  AccessLib.resubmitEoa(row);
	}
	
	/**
	 * This method is for message validation
	*/
	@Test(enabled = true)
	void Resubmit_APO_Under_Other_Entries() throws Exception
	{
		printLog("Resubmit_APO_Under_Other_Entries");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_017");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		String url = mapConfInfos.get("url");
		String user = mapConfInfos.get("user_name");
		String password = mapConfInfos.get("password");	
		System.out.println(url+"___"+user);
		if (!loginOn)
		{
			guiTools.openBrowser(browserType);
			loginOn = AccessLib.loginToAccess(url, user, password);
		}
		testCaseStatus =  AccessLib.resubmitApo(row);
	}
	
	/**
	 * This method is for validate required fields based on seg value
	*/
	@Test(enabled = true)
	void Validate_Efile_Segment_Related_Required_Fields() throws Exception
	{
		printLog("Validate_Efile_Segment_Related_Required_Fields");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_018");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		String url = mapConfInfos.get("url");
		String user = mapConfInfos.get("user_name");
		String password = mapConfInfos.get("password");	
		System.out.println(url+"___"+user);
		if (!loginOn)
		{
			guiTools.openBrowser(browserType);
			loginOn = AccessLib.loginToAccess(url, user, password);
		}
		//row = eFileRegisterHelpmsg.get(0);
		testCaseStatus =  AccessLib.validateRequiredFieldsBySegment(row);
	}
	
	/**
	 * This method is for validate required fields based on seg value
	*/
	@Test(enabled = true)
	void Validate_Efile_Search() throws Exception
	{
		printLog("Validate_Efile_Search");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_019");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		String url = mapConfInfos.get("url");
		String user = mapConfInfos.get("user_name");
		String password = mapConfInfos.get("password");	
		System.out.println(url+"___"+user);
		if (!loginOn)
		{
			guiTools.openBrowser(browserType);
			loginOn = AccessLib.loginToAccess(url, user, password);
		}
		//row = eFileRegisterHelpmsg.get(0);
		testCaseStatus =  AccessLib.validateQuickSearch(row);
	}
	
	
	/**
	 * This method is for message validation
	*/
	@Test(enabled = true)
	void Validate_Efile_Register_Help_Messages() throws Exception
	{
		printLog("Validate_Efile_Register_Help_Messages");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_020");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		String url = mapConfInfos.get("url");
		String user = mapConfInfos.get("user_name");
		String password = mapConfInfos.get("password");	
		System.out.println(url+"___"+user);
		if (!loginOn)
		{
			guiTools.openBrowser(browserType);
			//loginOn = AccessLib.loginToAccess(url, user, password);
			navigateTo(url);
		}
		row = eFileRegisterHelpmsg.get(0);
		testCaseStatus =  AccessLib.validateEfileRegisterFieldsHelpMessages(row);
	}
	

	/**
	 * This method is for message validation
	*/
	@Test(enabled = true)
	void Validate_Efile_Register_Required_Fields() throws Exception
	{
		printLog("Validate_Efile_Register_Required_Fields");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_021");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		String url = mapConfInfos.get("url");
		String user = mapConfInfos.get("user_name");
		String password = mapConfInfos.get("password");	
		System.out.println(url+"___"+user);
		if (!loginOn)
		{
			guiTools.openBrowser(browserType);
			navigateTo(url);
			//loginOn = AccessLib.loginToAccess(url, user, password);
		}
		row = eFileRegisterHelpmsg.get(0);
		testCaseStatus =  AccessLib.ValidateEfileRegisterRequiredFields(row);
	}
	
	
	/**
	 * This method is for message validation
	*/
	@Test(enabled = true)
	void Validate_User_Register_Help_Messages() throws Exception
	{
		printLog("Validate_User_Register_Help_Messages");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_022"
				+ "");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		String url = mapConfInfos.get("url");
		String user = mapConfInfos.get("user_name");
		String password = mapConfInfos.get("password");	
		System.out.println(url+"___"+user);
		if (!loginOn)
		{
			guiTools.openBrowser(browserType);
			//loginOn = AccessLib.loginToAccess(url, user, password);
			navigateTo(url);
		}
		row = userRegisterHelpmsg.get(0);
		testCaseStatus =  AccessLib.validateGuestRegisterFieldsHelpMessages(row);
	}
	

	/**
	 * This method is for message validation
	*/
	@Test(enabled = true)
	void Validate_User_Register_Required_Fields() throws Exception
	{
		printLog("Validate_User_Register_Required_Fields");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_023");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		String url = mapConfInfos.get("url");
		String user = mapConfInfos.get("user_name");
		String password = mapConfInfos.get("password");	
		System.out.println(url+"___"+user);
		if (!loginOn)
		{
			guiTools.openBrowser(browserType);
			navigateTo(url);
			//loginOn = AccessLib.loginToAccess(url, user, password);
		}
		row = userRegisterHelpmsg.get(0);
		testCaseStatus =  AccessLib.ValidateUserRegisterRequiredFields(row);
	}
	
	/**
	 * This method if for getting the current test case information
	*/
	public LinkedHashMap<String, String> getTestCaseInfo(ArrayList<LinkedHashMap<String, String>> dataPool, 
																			String tcTagName)
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
	
}
