package tests;
import static GuiLibs.GuiTools.guiMap;
import static GuiLibs.GuiTools.holdSeconds;
import static ReportLibs.ReportTools.printLog;

import java.io.File;
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
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.testng.TestNG;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.collections.Lists;
import static GuiLibs.GuiTools.failTestSuite;
import static GuiLibs.GuiTools.failTestCase;

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
	public static LinkedHashMap<String, String> steelConditions;
	public static Timestamp endTime;
	public static boolean logged = false;
	public static Calendar cal = Calendar.getInstance();
	public static String jsonFolder;
	public static LinkedHashMap<String, String> jFile;
	public static void main(String[] args) throws Exception 
	{
		guiTools = new GuiTools();
		xlsxTools = new XlsxTools();
		bisFormLib = new BisFormLib();
		TestNG testng = new TestNG();
		List<String> suites = Lists.newArrayList();
		String dataPoolPath = InitTools.getInputDataFolder()+"/datapool/EXCLUSION_REQUEST.xlsx";
		String conditionSheet = InitTools.getInputDataFolder()+"/script/232_Conditions.xlsx";
		steelConditions = 
				XlsxTools.readXlsxSheetWithFirstColKey(conditionSheet, "Steel");
		
		///
		jsonFolder = InitTools.getInputDataFolder()+"\\json_files";
		jFile = new LinkedHashMap<String, String>();
		File folder = new File(jsonFolder);
		File[] listOfFiles = folder.listFiles();
		for (File file : listOfFiles) {
		    if (file.isFile() && file.getName().endsWith(".json")) {
		    	jFile.put(file.getName(), file.getAbsolutePath());
		    }
		}
	
		///
		/*scenarios = XlsxTools.readXlsxSheetInOrderAndFilter(dataPoolPath, "Scenarios", "Active=TRUE");
		dataPoolStep1  = XlsxTools.readXlsxSheetInOrderAndFilter(dataPoolPath, "Step 1", "Active=TRUE");
		dataPoolStep2  = XlsxTools.readXlsxSheetInOrderAndFilter(dataPoolPath, "Step 2", "Active=TRUE");
		dataPoolStep3  = XlsxTools.readXlsxSheetInOrderAndFilter(dataPoolPath, "Step 3", "Active=TRUE");
		dataPoolStep4  = XlsxTools.readXlsxSheetInOrderAndFilter(dataPoolPath, "Step 4", "Active=TRUE");
		dataPoolStep5  = XlsxTools.readXlsxSheetInOrderAndFilter(dataPoolPath, "Step 5", "Active=TRUE");*/
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
		HtmlReport.setTotalTcs(jFile.size());
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
		
		Object obj [][]= new  Object[jFile.size()][2];
		int i=0;
		for (HashMap.Entry<String, String> entry : jFile.entrySet()) 
		{
			System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
			obj[i][0] = entry.getKey();
			obj[i][1] = entry.getValue();
			i++;
		}
				
		/*int i = 0;
		for (HashMap<String, String> map : scenarios)
		{
			obj[i][0] = i;
			obj[i][1] =  map;
			i++;
		}*/
		return (Object[][]) obj;
		
  }
	/**
	 * This method is validation of all scenarios
	*/
	 @Test(dataProvider = "fetchingData")
	void validate(String fileName, String filePath) throws Exception
	{
		 	String htsUsCode ="", productType = "";
		 	JSONObject jsonObject = null;
		 	FileReader file = new  FileReader(filePath);
			// parsing file "JSONExample.json" 
			JSONParser parser = new JSONParser();
			try
			{
				Object object = parser.parse(file);
				jsonObject = (JSONObject) object;
				htsUsCode= (String) jsonObject.get("HTSUSCode");
				productType= (String) jsonObject.get("Product");
				
	          // JSONObject  xxx = (JSONObject) jsonObject.get("ChemicalComposition");
			}
			catch(FileNotFoundException e) {e.printStackTrace();}
			catch(IOException e){e.printStackTrace();}
			catch(Exception e) {e.printStackTrace();}
		 
		 GuiTools.setTestCaseName(fileName.replace(".json", "_")+htsUsCode);
		 GuiTools.setTestCaseDescription(fileName.replace(".json", "_")+htsUsCode+"_"+productType);
		 
		 
		 if (!steelConditions.containsKey(htsUsCode))
		 {
			 testCaseStatus=false;
			 failTestCase(fileName.replace(".json", "_")+htsUsCode,htsUsCode+" Should be In The List" , 
					 "Not As Expected", "VP", "fail", "");
		 }
		 else
		 {
			 testCaseStatus =  testCaseStatus & 
				BisFormLib.ValidateConditions(jsonObject, productType, steelConditions.get(htsUsCode));
		 }
		//GuiTools.setTestCaseDescription(row.get("Scenarios"));
		printLog(GuiTools.getTestCaseName());
		
		/*
		if(step5Row != null)
		{
			HtmlReport.addHtmlStepTitle("					Validate the form of step 5","Title");
		    testCaseStatus =  testCaseStatus & BisFormLib.ValidateStepFive(step5Row);
		}*/
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
