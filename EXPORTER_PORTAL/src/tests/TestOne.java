package tests;
import static GuiLibs.GuiTools.guiMap;
import static GuiLibs.GuiTools.holdSeconds;
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
import libs.ExporterLib;

public class TestOne {
	public static GuiTools guiTools;
	//static HtmlReport htmlReport;
	HashMap<String, String> mapConfInfos;
	String browserType;
	static XlsxTools xlsxTools;
	static ArrayList<LinkedHashMap<String, String>> dataPool;
	ArrayList<LinkedHashMap<String, String>> guiPool;
	static ExporterLib exporterLib;
	//public static boolean testCaseStatus;
	public static Timestamp startTime, suiteStartTime;
	public static Timestamp endTime;
	public static Calendar cal = Calendar.getInstance();
	public boolean loginOn = false;
	public static void main(String[] args) throws Exception 
	{
		guiTools = new GuiTools();
		xlsxTools = new XlsxTools();
		exporterLib = new ExporterLib();
		TestNG testng = new TestNG();
		List<String> suites = Lists.newArrayList();
		String dataPoolPath = InitTools.getInputDataFolder()+"/datapool/Exporter_Regression.xlsx";
		System.out.println("dataPoolPath "+dataPoolPath);
		dataPool  = XlsxTools.readXlsxSheetAndFilter(dataPoolPath, "Regression", "Active=TRUE");
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
	void Create_Content_Biography() throws Exception
	{
		printLog("Create_Content_Biography");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "Biography");
		testCaseStatus = ExporterLib.createContent(row);
	}
	
	
	/**
	 * This method is creating Feature Article content
	*/
	@Test(enabled = true)
	void Create_Content_Feature_Article() throws Exception
	{
		printLog("Create_Content_Feature_Article");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "Feature Article");
		testCaseStatus = ExporterLib.createContent(row);
	}
	
	/**
	 * This method is creating How To content
	*/
	@Test(enabled = true)
	void Create_Content_How_To() throws Exception
	{
		printLog("Create_Content_How_To");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "How To");
		testCaseStatus = ExporterLib.createContent(row);
	}
	
	
	/**
	 * This method is creating Image Library content
	*/
	@Test(enabled = true)
	void Create_Content_Image_Library() throws Exception
	{
		printLog("Create_Content_Image_Library");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "Image Library");
		testCaseStatus = ExporterLib.createContent(row);
	}


	/**
	 * This method is creating Internship content
	*/
	@Test(enabled = true)
	void Create_Content_Internship() throws Exception
	{
		printLog("Create_Content_Internship");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "Internship");
		testCaseStatus = ExporterLib.createContent(row);
	}
	
	
	/**
	 * This method is creating Knowledge Product content
	*/
	@Test(enabled = true)
	void Create_Content_Knowledge_Product() throws Exception
	{
		printLog("Create_Content_Knowledge_Product");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "Knowledge Product");
		testCaseStatus = ExporterLib.createContent(row);
	}
	
	
	/**
	 * This method is creating News Blog content
	*/
	@Test(enabled = true)
	void Create_Content_News_Blog() throws Exception
	{
		printLog("Create_Content_News_Blog");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "News Blog");
		testCaseStatus = ExporterLib.createContent(row);
	}
	
	
	/**
	 * This method is creating Office content
	*/
	@Test(enabled = true)
	void Create_Content_Office() throws Exception
	{
		printLog("Create_Content_Office");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "Office");
		testCaseStatus = ExporterLib.createContent(row);
	}
	
	
	/**
	 * This method is creating Press Release content
	*/
	@Test(enabled = true)
	void Create_Content_Press_Release() throws Exception
	{
		printLog("Create_Content_Press_Release");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "Press Release");
		testCaseStatus = ExporterLib.createContent(row);
	}
	
	
	/**
	 * This method is creating Series Aggregator content
	*/
	@Test(enabled = true)
	void Create_Content_Series_Aggregator() throws Exception
	{
		printLog("Create_Content_Series_Aggregator");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "Series Aggregator");
		testCaseStatus = ExporterLib.createContent(row);
	}
	
	/**
	 * This method is creating Service Offering content
	*/
	@Test(enabled = true)
	void Create_Content_Service_Offering() throws Exception
	{
		printLog("Create_Content_Service_Offering");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "Service Offering");
		testCaseStatus = ExporterLib.createContent(row);
	}
	
	/**
	 * This method is creating Success Story content
	*/
	@Test(enabled = true)
	void Create_Content_Success_Story() throws Exception
	{
		printLog("Create_Content_Success_Story");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "Success Story");
		testCaseStatus = ExporterLib.createContent(row);
	}
	
	/**
	 * This method is creating Sub-topic Page content
	*/
	@Test(enabled = true)
	void Create_Content_Sub_topic_Page() throws Exception
	{
		printLog("Create_Content_Sub_topic_Page");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "Sub-topic Page");
		testCaseStatus = ExporterLib.createContent(row);
	}
	
	
	/**
	 * This method is creating Trade Lead content
	*/
	@Test(enabled = true)
	void Create_Content_Trade_Lead() throws Exception
	{
		printLog("Create_Content_Trade_Lead");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "Trade Lead");
		testCaseStatus = ExporterLib.createContent(row);
	}
	
	/**
	 * This method is creating Video Library content
	*/
	@Test(enabled = true)
	void Create_Content_Video_Library() throws Exception
	{
		printLog("Create_Content_Video_Library");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "Video Library");
		testCaseStatus = ExporterLib.createContent(row);
	}
	
	
	/**
	 * This method is creating About Us content
	*/
	@Test(enabled = true)
	void Create_Content_About_Us() throws Exception
	{
		printLog("Create_Content_About_Us");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "About Us");
		testCaseStatus = ExporterLib.createContent(row);
	}
	
	
	/**
	 * This method is creating S1 content
	*/
	@Test(enabled = true)
	void Create_Section_Landing_Page_S1() throws Exception
	{
		printLog("Create_Section_Landing_Page_S1");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "Section Landing Page - S1");
		testCaseStatus = ExporterLib.createContent(row);
	}
	

	/**
	 * This method is creating S2 content
	*/
	@Test(enabled = true)
	void Create_Section_Landing_Page_S2() throws Exception
	{
		printLog("Create_Section_Landing_Page_S2");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "Section Landing Page - S2");
		testCaseStatus = ExporterLib.createContent(row);
	}
	
	/**
	 * This method is creating S3 content
	*/
	@Test(enabled = true)
	void Create_Section_Landing_Page_S3() throws Exception
	{
		printLog("Create_Section_Landing_Page_S2");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "Section Landing Page - S3");
		testCaseStatus = ExporterLib.createContent(row);
	}
	
	/**
	 * This method is creating S4 content
	*/
	@Test(enabled = true)
	void Create_Section_Landing_Page_S4() throws Exception
	{
		printLog("Create_Section_Landing_Page_S4");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "Section Landing Page - S4");
		testCaseStatus = ExporterLib.createContent(row);
	}
	
	
	/**
	 * This method is creating S5 content
	*/
	@Test(enabled = true)
	void Create_Section_Landing_Page_S5() throws Exception
	{
		printLog("Create_Section_Landing_Page_S5");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "Section Landing Page - S5");
		testCaseStatus = ExporterLib.createContent(row);
	}
	
	
	/**
	 * This method is creating S6 content
	*/
	@Test(enabled = true)
	void Create_Section_Landing_Page_S6() throws Exception
	{
		printLog("Create_Section_Landing_Page_S6");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_022");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "Section Landing Page - S6");
		testCaseStatus = ExporterLib.createContent(row);
	}
	
	/**
	 * This method is creating S7 content
	*/
	@Test(enabled = true)
	void Create_Section_Landing_Page_S7() throws Exception
	{
		printLog("Create_Section_Landing_Page_S7");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "Section Landing Page - S7");
		testCaseStatus = ExporterLib.createContent(row);
	}
	
	/**
	 * This method is creating S8 content
	*/
	@Test(enabled = true)
	void Create_Section_Landing_Page_S8() throws Exception
	{
		printLog("Create_Section_Landing_Page_S8");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_024");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "Section Landing Page - S8");
		testCaseStatus = ExporterLib.createContent(row);
	}
	
	
	/**
	 * This method is creating S8 content
	*/
	@Test(enabled = true)
	void Create_Section_Landing_Page_S9() throws Exception
	{
		printLog("Create_Section_Landing_Page_S9");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_025");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "Section Landing Page - S9");
		testCaseStatus = ExporterLib.createContent(row);
	}
	
	/**
	 * This method is creating T1 content
	*/
	@Test(enabled = true)
	void Create_Topic_Landing_Page_T1() throws Exception
	{
		printLog("Create_Topic_Landing_Page_T1");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_026");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "Topic Landing Page - T1");
		testCaseStatus = ExporterLib.createContent(row);
	}
	
	
	/**
	 * This method is creating T2 content
	*/
	@Test(enabled = true)
	void Create_Topic_Landing_Page_T2() throws Exception
	{
		printLog("Create_Topic_Landing_Page_T2");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_027");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "Topic Landing Page - T2");
		testCaseStatus = ExporterLib.createContent(row);
	}
	
	/**
	 * This method is creating T3 content
	*/
	@Test(enabled = true)
	void Create_Topic_Landing_Page_T3() throws Exception
	{
		printLog("Create_Topic_Landing_Page_T3");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_028");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "Topic Landing Page - T3");
		testCaseStatus = ExporterLib.createContent(row);
	}
	
	/**
	 * This method is creating T4 content
	*/
	@Test(enabled = true)
	void Create_Topic_Landing_Page_T4() throws Exception
	{
		printLog("Create_Topic_Landing_Page_T4");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_029");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "Topic Landing Page - T4");
		testCaseStatus = ExporterLib.createContent(row);
	}
	
	
	/**
	 * This method is creating T5 content
	*/
	@Test(enabled = true)
	void Create_Topic_Landing_Page_T5() throws Exception
	{
		printLog("Create_Topic_Landing_Page_T5");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_030");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "Topic Landing Page - T5");
		testCaseStatus = ExporterLib.createContent(row);
	}
	
	
	
	/**
	 * This method is creating T6 content
	*/
	@Test(enabled = true)
	void Create_Topic_Landing_Page_T6() throws Exception
	{
		printLog("Create_Topic_Landing_Page_T6");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_031");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "Topic Landing Page - T6");
		testCaseStatus = ExporterLib.createContent(row);
	}
	
	
	/**
	 * This method is creating T7 content
	*/
	@Test(enabled = true)
	void Create_Topic_Landing_Page_T7() throws Exception
	{
		printLog("Create_Topic_Landing_Page_T7");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_032");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "Topic Landing Page - T7");
		testCaseStatus = ExporterLib.createContent(row);
	}
	
	/**
	 * This method is creating T8 content
	*/
	@Test(enabled = true)
	void Create_Topic_Landing_Page_T8() throws Exception
	{
		printLog("Create_Topic_Landing_Page_T8");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_033");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "STopic Landing Page - T8");
		testCaseStatus = ExporterLib.createContent(row);
	}
	
	
	/**
	 * This method is creating T9 content
	*/
	@Test(enabled = true)
	void Create_Topic_Landing_Page_T9() throws Exception
	{
		printLog("Create_Topic_Landing_Page_T9");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_034");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "Topic Landing Page - T9");
		testCaseStatus = ExporterLib.createContent(row);
	}
	
	
	/**
	 * This method is creating T10 content
	*/
	@Test(enabled = true)
	void Create_Topic_Landing_Page_T10() throws Exception
	{
		printLog("Create_Topic_Landing_Page_T10");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_035");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "Topic Landing Page - T10");
		testCaseStatus = ExporterLib.createContent(row);
	}
	
	
	/**
	 * This method is creating T11 content
	*/
	@Test(enabled = true)
	void Create_Topic_Landing_Page_T11() throws Exception
	{
		printLog("Create_Topic_Landing_Page_T11");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_036");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "Topic Landing Page - T11");
		testCaseStatus = ExporterLib.createContent(row);
	}
	

	/**
	 * This method is creating T12 content
	*/
	@Test(enabled = true)
	void Create_Topic_Landing_Page_T12() throws Exception
	{
		printLog("Create_Topic_Landing_Page_T12");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_037");
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
			loginOn = ExporterLib.loginToExporter(url, user, password);
		}
		row.put("Content_Type", "Topic Landing Page - T12");
		testCaseStatus = ExporterLib.createContent(row);
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
