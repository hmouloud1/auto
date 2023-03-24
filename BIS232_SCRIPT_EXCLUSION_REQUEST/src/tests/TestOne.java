package tests;
import static GuiLibs.GuiTools.guiMap;
import static GuiLibs.GuiTools.holdSeconds;
import static GuiLibs.GuiTools.updateHtmlReport;
import static ReportLibs.ReportTools.printLog;
import static GuiLibs.GuiTools.clickElementJs;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.JSONArray;
import org.json.simple.parser.JSONParser;
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
import bsh.ParseException;
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
	public static boolean testCaseStatus;
	public static Timestamp startTime, suiteStartTime;
	public static Timestamp endTime;
	public static boolean logged = false;
	public static Calendar cal = Calendar.getInstance();
	public static void main(String[] args) throws Exception 
	{
		guiTools = new GuiTools();
		xlsxTools = new XlsxTools();
		bisFormLib = new BisFormLib();
		TestNG testng = new TestNG();
		List<String> suites = Lists.newArrayList();
		String dataPoolPath = InitTools.getInputDataFolder()+"\\datapool\\EXCLUSION_REQUEST.xlsx";
		scenarios = XlsxTools.readXlsxSheetInOrderAndFilter(dataPoolPath, "Scenarios", "Active=TRUE");
		dataPoolStep1  = XlsxTools.readXlsxSheetInOrderAndFilter(dataPoolPath, "Step 1", "Active=TRUE");
		dataPoolStep2  = XlsxTools.readXlsxSheetInOrderAndFilter(dataPoolPath, "Step 2", "Active=TRUE");
		dataPoolStep3  = XlsxTools.readXlsxSheetInOrderAndFilter(dataPoolPath, "Step 3", "Active=TRUE");
		dataPoolStep4  = XlsxTools.readXlsxSheetInOrderAndFilter(dataPoolPath, "Step 4", "Active=TRUE");
		dataPoolStep5  = XlsxTools.readXlsxSheetInOrderAndFilter(dataPoolPath, "Step 5", "Active=TRUE");
		String testNgPath = InitTools.getRootFolder()+"\\testng.xml";
		//build
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
			logged = BisFormLib.loginToBis(url, user, password);
		}
		holdSeconds(2);
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
		///Validate
		if (!BisFormLib.submitBisForm())
		{
			if(step1Row != null)
			{
				HtmlReport.addHtmlStepTitle("					Validate the form of step 1","Title");
				testCaseStatus =  testCaseStatus & BisFormLib.ValidateStepOne(step1Row);
			}
			if(step2Row != null)
			{
				HtmlReport.addHtmlStepTitle("					Validate the form of step 2","Title");
				testCaseStatus =  testCaseStatus & BisFormLib.ValidateStepTwo(step2Row);
			}
			if(step3Row != null)
			{
				HtmlReport.addHtmlStepTitle("					Validate the form of step 3","Title");
			    testCaseStatus =  testCaseStatus & BisFormLib.ValidateStepThree(step3Row);
			}
			if(step4Row != null)
			{
				HtmlReport.addHtmlStepTitle("					Validate the form of step 4","Title");
			    testCaseStatus =  testCaseStatus & BisFormLib.ValidateStepFour(step4Row);
			}
			if(step5Row != null)
			{
				HtmlReport.addHtmlStepTitle("					Validate the form of step 5","Title");
			    testCaseStatus =  testCaseStatus & BisFormLib.ValidateStepFive(step5Row);
			}
		}else
		{
			updateHtmlReport("Preview", "At least one error "
					+ "should be fired in order to verify the error messages", 
					"All pass", "VP", "warning", "Preview");
			clickElementJs(guiMap.get("Home"));
			clickElementJs(guiMap.get("CreateNewExclusionRequest"));
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

}
