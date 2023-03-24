package tests;
import static GuiLibs.GuiTools.failTestCase;
import static GuiLibs.GuiTools.failTestSuite;
import static GuiLibs.GuiTools.testCaseStatus;
import static ReportLibs.ReportTools.printLog;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.JSONParser;
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
	static HashMap<String, String> mapConfInfos;
	String browserType;
	static XlsxTools xlsxTools;
	static ArrayList<LinkedHashMap<String, String>> scenarios, dataPoolStep1, dataPoolStep2, 
	dataPoolStep3, dataPoolStep4, dataPoolStep5;
	ArrayList<LinkedHashMap<String, String>> guiPool;
	static BisFormLib bisFormLib;
	//public static boolean testCaseStatus;
	public static Timestamp startTime, suiteStartTime;
	public static LinkedHashMap<String, String> steelConditions, aluminumConditions;
	public static Timestamp endTime;
	public static boolean logged = false;
	public static Calendar cal = Calendar.getInstance();
	public static String jsonFolder;
	//public static LinkedHashMap<String, String> jFile;
	public static void main(String[] args) throws Exception 
	{
		guiTools = new GuiTools();
		xlsxTools = new XlsxTools();
		bisFormLib = new BisFormLib();
		//JSONParser parser = new JSONParser();
		TestNG testng = new TestNG();
		mapConfInfos = guiTools.getConfigInfos();
		List<String> suites = Lists.newArrayList();
		String dataPoolPath = InitTools.getInputDataFolder()+"/datapool/"+mapConfInfos.get("data_pool_sheet");
		String conditionSheet = InitTools.getInputDataFolder()+"/script/232_Conditions.xlsx";
		steelConditions = 
				XlsxTools.readXlsxSheetWithFirstColKey(conditionSheet, "Steel");
		aluminumConditions = 
				XlsxTools.readXlsxSheetWithFirstColKey(conditionSheet, "Aluminum");
		///
		jsonFolder = InitTools.getInputDataFolder()+"/json_files";
		/*jFile = new LinkedHashMap<String, String>();
		File folder = new File(jsonFolder);
		File[] listOfFiles = folder.listFiles();
		for (File file : listOfFiles) {
		    if (file.isFile() && file.getName().endsWith(".json")) {
		    	jFile.put(file.getName(), file.getAbsolutePath());
		    }
		}*/
	
		///
		scenarios = XlsxTools.readXlsxSheetInOrderAndFilter(dataPoolPath, "", "");
		/*dataPoolStep1  = XlsxTools.readXlsxSheetInOrderAndFilter(dataPoolPath, "Step 1", "Active=TRUE");
		dataPoolStep2  = XlsxTools.readXlsxSheetInOrderAndFilter(dataPoolPath, "Step 2", "Active=TRUE");
		dataPoolStep3  = XlsxTools.readXlsxSheetInOrderAndFilter(dataPoolPath, "Step 3", "Active=TRUE");
		dataPoolStep4  = XlsxTools.readXlsxSheetInOrderAndFilter(dataPoolPath, "Step 4", "Active=TRUE");
		dataPoolStep5  = XlsxTools.readXlsxSheetInOrderAndFilter(dataPoolPath, "Step 5", "Active=TRUE");*/
		String testNgPath = InitTools.getRootFolder()+"/testng.xml";
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
		
		browserType = mapConfInfos.get("browser_type");
		//String guiMapFilePath = InitTools.getInputDataFolder()+"\\script\\gui_map.xlsx";
		//guiPool = XlsxTools.readXlsxSheetInOrderAndFilter(guiMapFilePath, "guiMap", "");
		//guiMap = XlsxTools.readGuiMap(guiPool);
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
		System.out.println(endTime.getTime() +" suite-_"+ suiteStartTime.getTime());
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
	    System.out.println(endTime.getTime() +" -test case-_"+ startTime.getTime());
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
		/*System.out.println("");
		try{
		JSONParser parser = new JSONParser();
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		*/
		Object obj [][]= new  Object[scenarios.size()][4];
		/*int i=0;
		for (HashMap.Entry<String, String> entry : jFile.entrySet()) 
		{
			System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
			obj[i][0] = entry.getKey();
			obj[i][1] = entry.getValue();
			i++;
		}*/
				
		int i = 0;
		for (HashMap<String, String> map : scenarios)
		{
			obj[i][0] = map.get("ID");
			obj[i][1] = map.get("HTSUSCode");
			obj[i][2] = map.get("JSONData");
			obj[i][3] = map.get("Product Validation Conditions");
			i++;
		}
		return (Object[][]) obj;
		
  }
	/**
	 * This method is validation of all scenarios
	*/
	 @Test(dataProvider = "fetchingData")
	void validate(String id, String htsusCode, String jsonData, String actualResult) throws Exception
	{
		 if (htsusCode.equals("") || jsonData.equals("")|| actualResult.equals(""))
		 {
			 GuiTools.setTestCaseName(id+" - "+htsusCode);
			 GuiTools.setTestCaseDescription(id+" - "+htsusCode);
			 testCaseStatus=false;
			 failTestCase("Validate form id: "+id ,"htsusCode, jsonData,"
			 		+ " and actualResult shouldn't be empty", 
					"Not as expected", "VP", "fail", "");
		 }
	/*	 if (id.equals("714"))
		 {
			 System.out.println(id);
		 }*/
		 String scenarioName = id+"_"+htsusCode;
		 String filePath = jsonFolder+"/"+scenarioName+".json";
		 FileOutputStream out = new FileOutputStream(filePath);
		 out.write(jsonData.getBytes());
		 out.close();
	 	 String htsUsCode ="", productType = "", conditionList = "";
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
		 }
		 catch(FileNotFoundException e) 
		 {
			 e.printStackTrace();
		}
		 catch(Exception e){e.printStackTrace();}
		 GuiTools.setTestCaseName(scenarioName+" - "+productType);
		 GuiTools.setTestCaseDescription(scenarioName+" - "+productType);
		 
		//Some codes needs to be sent directly to Manual 7211140030, 7220110000 and 7225506000
		
		 if (!steelConditions.containsKey(htsUsCode) && !aluminumConditions.containsKey(htsUsCode) )
		 {
			 String htsUsCode10digits = htsUsCode;
			 htsUsCode = htsUsCode.substring(0, 4);
			 if (!steelConditions.containsKey(htsUsCode) && !aluminumConditions.containsKey(htsUsCode) )
			 { 
				 testCaseStatus=false;
				 failTestCase(scenarioName,htsUsCode+ "OR "+htsUsCode10digits+" Should be found in the "
				 		+ "condition sheet" , 
					"No row found in the condition sheet for the code: "+htsUsCode, "VP", "fail", "");
			 }
		 }
		 if (productType.equalsIgnoreCase("Steel"))
		 {
			 conditionList = steelConditions.get(htsUsCode);
		 } else if (productType.equalsIgnoreCase("Aluminum"))
		 {
			 conditionList = aluminumConditions.get(htsUsCode);
		 }
		 else
		 {
			 failTestSuite("Validate "+ scenarioName, "Product should be Steel or Aluminum", 
					 "Json file should have producat name as Steel or Aluminum", "Step", "fail", "");
		 }
		 if(conditionList==null)
		 {
			 testCaseStatus=false;
			 failTestCase(scenarioName,htsUsCode+" Should be found in the "
			 		+ "condition sheet" , 
				"No row found in the condition sheet for the code: "+htsUsCode, "VP", "fail", "");
		 }
		 else if(conditionList.contains("Condition 146"))
		 {
			 HtmlReport.addHtmlStep("<span class = 'Warning'>Validate {condition 144}</span>", 
						"<span class = 'Warning'>"+ "N/A" +"</span>" ,
						"<span class = 'Warning'>Send it to the manual review.</span>" , 
						"<span class = 'Warning'>N/A</span>",
						"Warning", ""); 
		 }
		 else
		 {
			 testCaseStatus =  testCaseStatus & 
					BisFormLib.ValidateConditions(jsonObject, productType, conditionList, actualResult);
		 }
		//printLog(GuiTools.getTestCaseName());
	 
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
