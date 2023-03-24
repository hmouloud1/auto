package tests;
import static GuiLibs.GuiTools.failTestSuite;
import static GuiLibs.GuiTools.failTestCase;
import static GuiLibs.GuiTools.guiMap;
import static GuiLibs.GuiTools.holdSeconds;
import static GuiLibs.GuiTools.testCaseStatus;
import static GuiLibs.GuiTools.updateHtmlReport;
import static ReportLibs.ReportTools.printLog;
import static XmlLibs.XmlTools.buildTestNgFromDataPool;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.json.JSONObject;
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
import ServiceLibs.APITools;
import libs.ADCVDLib;

public class TestOne {
	public static GuiTools guiTools;
	//static HtmlReport htmlReport;
	static LinkedHashMap<String, String> recordType = new LinkedHashMap<String, String>();
	static HashMap<String, String> mapConfInfos;
	String browserType;
	static XlsxTools xlsxTools;
	static ArrayList<LinkedHashMap<String, String>> dataPool;
	ArrayList<LinkedHashMap<String, String>> guiPool;
	static ADCVDLib adcvdLib;
	//public static boolean testCaseStatus;
	public static Timestamp startTime, suiteStartTime;
	public static Timestamp endTime;
	public static Calendar cal = Calendar.getInstance();
	public boolean loginOn = false;
	public static String todayStr;
	public static void main(String[] args) throws Exception 
	{
		initiateRecordType();
		printLog("MainMethod()");
		guiTools = new GuiTools();
		xlsxTools = new XlsxTools();
		adcvdLib = new ADCVDLib();
		 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date todayDate = new Date();
		todayStr = dateFormat.format(todayDate);
		
		TestNG testng = new TestNG();
		List<String> suites = Lists.newArrayList();
		String dataPoolPath = InitTools.getInputDataFolder()+"/datapool/Regession_TC.xlsx";
		System.out.println("dataPoolPath "+dataPoolPath);
		dataPool  = XlsxTools.readXlsxSheetAndFilter(dataPoolPath, "Regression", "Active=TRUE");
		String testNgTemplate = InitTools.getInputDataFolder()+"/template/testng_template.xml";
		String testNgPath = InitTools.getRootFolder()+"/testng.xml";
		System.out.println("testNgTemplate "+testNgTemplate);
		System.out.println("testNgPath "+testNgPath);
		
		////////////////////////////////////////////////////////
		mapConfInfos = guiTools.getConfigInfos();
		String url = mapConfInfos.get("LOGINURL");
		String grantService = mapConfInfos.get("GRANTSERVICE");
		String grantType = mapConfInfos.get("GRANTTYPE");
		String grantId = mapConfInfos.get("CLIENTID");
		String clientSecret = mapConfInfos.get("CLIENTSECRET");
		String userName = mapConfInfos.get("USERNAME");
		String password = mapConfInfos.get("PASSWORD");
		String accessToken = APITools.getAccesToken(url, grantService, grantType, grantId,
				clientSecret , userName, password);
		if(accessToken==null || accessToken.equals("Nothing"))
		{
			failTestSuite("Create new Case", "Connect through API",
						"Not as expected", "Step", "fail", "");
		}
		/////////////////////////////////////////////////////////
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
	//	GuiTools.guiMap = new LinkedHashMap<String, LinkedHashMap<String, String>>();
		//mapConfInfos = guiTools.getConfigInfos();
		//browserType = mapConfInfos.get("browser_type");
		//String guiMapFilePath = InitTools.getInputDataFolder()+"/script/gui_map.xlsx";
		//guiPool = XlsxTools.readXlsxSheetAndFilter(guiMapFilePath, "guiMap", "");
	//	guiMap = XlsxTools.readGuiMap(guiPool);
		HtmlReport.setTestSuiteName(mapConfInfos.get("PROJECTNAME"));
		HtmlReport.setEnvironmentName(mapConfInfos.get("ENVNAME"));
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
	void CVD_Align_With_AD() throws Exception
	{
		
		printLog("CVD_Align_With_AD");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_001");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		LinkedHashMap<String, String> record = new LinkedHashMap<String, String>();
		String adCaseId = createNewCase(row, "A-");
		String adPetitionId = createNewPetition(row, adCaseId, "Self-Initiated");
		String adInvestigationIdName = createNewInvestigation(row, adPetitionId);
		String cvdCaseId = createNewCase(row, "C-");
		String cvdPetitionId = createNewPetition(row, cvdCaseId, "Self-Initiated");
		String cvdInvestigationIdName = createNewInvestigation(row, cvdPetitionId);
		//record.put("ADCVD_Case_Number__c", caseNameCvd);
		// align CVD to AD
		record.clear();
		record.put("AD_Investigation_Aligned_To__c", adInvestigationIdName.split("###")[0]);
		String code = APITools.updateRecordObject("Investigation__c", cvdInvestigationIdName.split("###")[0], record);
		if(code==null || !code.equals("204"))
		{
			failTestSuite("Align CVD to AD", "User should be able to link a CVD investigation to an AD investigation", 
					"Not as expected", "VP", "fail", "" );
		}
		else
		{
			updateHtmlReport("Align CVD to AD", "User is able to link CVD investigation to AD investigation", 
					 "<span class = 'boldy'>"+ cvdInvestigationIdName.split("###")[1]+"---->"+
							 adInvestigationIdName.split("###")[1]+"</span>", "VP", "pass", "" );
		}
	}
	
	/**
	 * This method is for ADCVD case creation and validation
	*/
	@Test(enabled = true, priority=2)
	void Litigation_Dates_Validation() throws Exception
	{
		printLog("Litigation_Dates_Validation");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_002");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		String sqlString = "select+id,name,Actual_Draft_Remand_Issues_to_DAS__c,Actual_Draft_Remand_Concurrence_to_DAS__c,"
				+ "Next_Office_Deadline__c,Next_Due_to_DAS_Deadline__c,Next_Major_Deadline__c,Final_Team_Meeting_Deadline__c,"
				+ "Final_Issues_Due_to_DAS__c,Final_Concurrence_Due_to_DAS__c,Calculated_Final_Signature__c,"
				+ "Draft_Remand_Concurrence_Due_to_DAS__c,Draft_Remand_Issues_Due_to_DAS__c,Calculated_Draft_Remand_release_to_party__c,"
				+ "Prelim_Team_Meeting_Deadline__c,Prelim_Concurrence_Due_to_DAS__c,Prelim_Issues_Due_to_DAS__c,"
				+ "Calculated_Preliminary_Signature__c,Expected_Final_Signature_Before_Ext__c,Request_Filed__c,"
				+ "Prelim_Extension_of_days__c,Final_Extension_of_days__c+from+litigation__c+where+id='litigationId'";
		//LinkedHashMap<String, String> record = new LinkedHashMap<String, String>();
		String adCaseId = createNewCase(row, "A-");
		String adPetitionId = createNewPetition(row, adCaseId, "");
		HtmlReport.addHtmlStepTitle("Create 'International Litigation' and Validate Dates","Title");
		String litigationId = createNewLitigation(adPetitionId, "International Litigation");
		JSONObject jObj = APITools.getRecordFromObject(sqlString.replace("litigationId", litigationId));
		testCaseStatus =testCaseStatus & ADCVDLib.validateLitigationFields(jObj, "International Litigation");
		//Remand
		HtmlReport.addHtmlStepTitle("Create Remand and Validate Dates","Title");
		String remandId = createNewLitigation(adPetitionId, "Remand");
		jObj = APITools.getRecordFromObject(sqlString.replace("litigationId", remandId));
		testCaseStatus =testCaseStatus & ADCVDLib.validateLitigationFields(jObj, "Remand");
	}
	/**
	 * This method is for ADCVD case creation and validation
	*/
	@Test(enabled = true, priority=3)
	void Self_Initiated_Petition_Investigation_Dates() throws Exception
	{
		printLog("Self_Initiated_Petition_Investigation_Dates");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_003");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		String adCaseId = createNewCase(row, "A-");
		String adPetitionId = createNewPetition(row, adCaseId, "");
		String adInvestigationIdName = createNewInvestigation(row, adPetitionId);
		String query = "select+Name,Calculated_Preliminary_Signature__c,Prelim_Team_Meeting_Deadline__c,"
				+ "Prelim_Issues_Due_to_DAS__c,Prelim_Concurrence_Due_to_DAS__c,Calculated_Final_Signature__c,"
				+ "Final_Issues_Due_to_DAS__c,Final_Concurrence_Due_to_DAS__c,Final_Team_Meeting_Deadline__c,"
				+ "Final_Announcement_Date__c,Est_ITC_Notification_to_DOC_of_Final_Det__c,Estimated_Order_FR_Published__c,"
				+ "Next_Announcement_Date__c,Next_Major_Deadline__c+from+investigation__c+where+id='investigationId'";
		JSONObject jObj = APITools.getRecordFromObject(query.replace("investigationId", adInvestigationIdName.split("###")[0]));
		HtmlReport.addHtmlStepTitle("I. AD CASE","Title");
		HtmlReport.addHtmlStepTitle("Check dates when petition first created","Title");
		testCaseStatus =testCaseStatus & ADCVDLib.checkSelfInitiatedDates(jObj, true);
		LinkedHashMap<String, String> record = new LinkedHashMap<String, String>();
		record.put("Actual_Initiation_Signature__c", todayStr);
		record.put("Calculated_Initiation_Signature__c", todayStr);
		record.put("Petition_Outcome__c", "Self-Initiated");		
		String code = APITools.updateRecordObject("Petition__c", adPetitionId, record);
		HtmlReport.addHtmlStepTitle("Check dates when petition is converted to Self-Initiated","Title");
		jObj = APITools.getRecordFromObject(query.replace("investigationId", adInvestigationIdName.split("###")[0]));
		testCaseStatus =testCaseStatus & ADCVDLib.checkSelfInitiatedDates(jObj, false);
		record.clear();
       	record.put("Petition__c", adPetitionId);
		record.put("Published_Date__c", todayStr);
		record.put("Cite_Number__c", "None");
		record.put("Type__c", "Initiation");
		String fridI = APITools.createObjectRecord("Federal_Register__c", record);
		HtmlReport.addHtmlStepTitle("Check dates when an initaited FR associated with petition","Title");
		jObj = APITools.getRecordFromObject(query.replace("investigationId", adInvestigationIdName.split("###")[0]));
		testCaseStatus =testCaseStatus & ADCVDLib.checkSelfInitiatedDates(jObj, true);
		HtmlReport.addHtmlStepTitle("II. CVD CASE","Title");
		String cvdCaseId = createNewCase(row, "C-");
		String cvdPetitionId = createNewPetition(row, cvdCaseId, "");
		String cvdInvestigationIdName = createNewInvestigation(row, cvdPetitionId);
		jObj = APITools.getRecordFromObject(query.replace("investigationId", cvdInvestigationIdName.split("###")[0]));
		HtmlReport.addHtmlStepTitle("Check dates when petition first created","Title");
		testCaseStatus =testCaseStatus & ADCVDLib.checkSelfInitiatedDates(jObj, true);
		HtmlReport.addHtmlStepTitle("Check dates when petition is converted to Self-Initiated","Title");
		record.clear();
		record.put("Actual_Initiation_Signature__c", todayStr);
		record.put("Calculated_Initiation_Signature__c", todayStr);
		record.put("Petition_Outcome__c", "Self-Initiated");		
		code = APITools.updateRecordObject("Petition__c", cvdPetitionId, record);
		jObj = APITools.getRecordFromObject(query.replace("investigationId", cvdInvestigationIdName.split("###")[0]));
		testCaseStatus =testCaseStatus & ADCVDLib.checkSelfInitiatedDates(jObj, false);
		HtmlReport.addHtmlStepTitle("Check dates when an initaited FR associated with petition","Title");
		record.clear();
       	record.put("Petition__c", cvdPetitionId);
		record.put("Published_Date__c", todayStr);
		record.put("Cite_Number__c", "None");
		record.put("Type__c", "Initiation");
		fridI = APITools.createObjectRecord("Federal_Register__c", record);
		jObj = APITools.getRecordFromObject(query.replace("investigationId", cvdInvestigationIdName.split("###")[0]));
		testCaseStatus =testCaseStatus & ADCVDLib.checkSelfInitiatedDates(jObj, true);
	}
	
	/**
	 * This method is for ADCVD case creation and validation
	*/
	@Test(enabled = true, priority=4)
	void Align_NSR_To_AR() throws Exception
	{
		LinkedHashMap<String, String> arDates = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> nsrDatesBefore = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> nsrDatesAfter = new LinkedHashMap<String, String>();
		printLog("Align_NSR_To_AR");
		JSONObject jObj = null;
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_004");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		String adCaseId = createNewCase(row, "A-");		
		String adPetitionId = createNewPetition(row, adCaseId, "");
		String adInvestigationIdName = createNewInvestigation(row, adPetitionId);
		String orderId = createNewOrder(adInvestigationIdName.split("###")[0]);
		String adminReviewId = createNewSegment(orderId, "Administrative Review");
		String query = "select+id,Name,Prelim_Team_Meeting_Deadline__c,Prelim_Issues_Due_to_DAS__c,"
					+ "Prelim_Concurrence_Due_to_DAS__c,Calculated_Preliminary_Signature__c,Final_Team_Meeting_Deadline__c,"
					+ "Final_Issues_Due_to_DAS__c,Final_Concurrence_Due_to_DAS__c,Calculated_Final_Signature__c,"
					+ "Final_Announcement_Date__c,Next_Office_Deadline__c,Next_Due_to_DAS_Deadline__c,"
					+ "Next_Major_Deadline__c,Next_Announcement_Date__c+from+segment__c+where+id='segmentId'";
		String arName="";
		jObj = APITools.getRecordFromObject(query.replace("segmentId", adminReviewId));
		arDates = ADCVDLib.readSegmentDates(jObj);
		arName = jObj.getString("Name");
		String shipperReviewId = createNewSegment(orderId, "New Shipper Review");
		jObj = APITools.getRecordFromObject(query.replace("segmentId", shipperReviewId));
		nsrDatesBefore = ADCVDLib.readSegmentDates(jObj);
		String nsrName = jObj.getString("Name");
	   //Aling SFR to AR
	    HtmlReport.addHtmlStepTitle("Align New shipper review to Admin review","Title");
	    LinkedHashMap<String, String> record = new LinkedHashMap<String, String>();
	    record.put("Administrative_Review_Aligned_To__c", adminReviewId);
	    String code = APITools.updateRecordObject("Segment__c", shipperReviewId, record);
	    if(code.equals("204"))
	    {
		   updateHtmlReport("Align NSR to AR", "user is able to align NSR segment to AR segement[ <span class = 'boldy'>"+
				   ""+nsrName+"--->"+arName+"]</span>", "As expected",
					"VP", "pass", "");
		   jObj = APITools.getRecordFromObject(query.replace("segmentId", shipperReviewId));
		   nsrDatesAfter = ADCVDLib.readSegmentDates(jObj);
		   testCaseStatus =testCaseStatus & ADCVDLib.alignNsrToArAndValidate(arDates, nsrDatesBefore, nsrDatesAfter);
	    }
	    else
	    {
		   updateHtmlReport("Align NSR to AR", "user is able to align NSR segment to AR segement", "Not as expected",
					"VP", "fail", "");
	    }
	}
	
	/**
	 * This method is for ADCVD case creation and validation
	*/
	@Test(enabled = true, priority=5)
	void Petition_Status_Validation() throws Exception
	{
		printLog("Petition_Status_Validation");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_005");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		String adCaseId = createNewCase(row, "A-");
		String adPetitionId = createNewPetition(row, adCaseId, "");		
		testCaseStatus = testCaseStatus & ADCVDLib.validatePetitionStatus(adPetitionId);
	}
	
	/**
	 * This method is for ADCVD case creation and validation
	*/
	@Test(enabled = true, priority=6)
	void Investigation_Status_Validation() throws Exception
	{		
		printLog("Investigation_Status_Validation");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_006");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		row.put("ADCVD_Case", "A-");
		row.put("ADCVD_Case_Type", "AD ME");
		//HtmlReport.addHtmlStepTitle("Create New AD Investigation","Title");
		String adCaseId = createNewCase(row, "A-");		
		String adPetitionId = createNewPetition(row, adCaseId, "");
		String adInvestigationIdName = createNewInvestigation(row, adPetitionId);
		testCaseStatus = testCaseStatus & ADCVDLib.validateInvestigationStatus(adInvestigationIdName.split("###")[0]);
	}

	/**
	 * This method is Admin review status
	*/
	@Test(enabled = true, priority=7)
	void Admin_Review_Status_Validation() throws Exception
	{  
		printLog("Admin_Review_Status_Validation");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_007");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		String adCaseId = createNewCase(row, "A-");		
		String adPetitionId = createNewPetition(row, adCaseId, "");
		String adInvestigationIdName = createNewInvestigation(row, adPetitionId);
		String orderId = createNewOrder(adInvestigationIdName.split("###")[0]);
		//LinkedHashMap<String, String> record = new LinkedHashMap<String, String>();
		//record.put("ADCVD_Order__c", orderId);
		String adminReviewId = createNewSegment(orderId, "Administrative Review");
		testCaseStatus = testCaseStatus & ADCVDLib.validateSegmentStatus_A(adminReviewId, "Administrative Review");
	}
	
	/**
	 * This method is Expedited_Review_Status_Validation status
	*/
	@Test(enabled = true, priority=8)
	void Expedited_Review_Status_Validation() throws Exception
	{
		printLog("Expedited_Review_Status_Validation");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_008");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		String adCaseId = createNewCase(row, "A-");		
		String adPetitionId = createNewPetition(row, adCaseId, "");
		String adInvestigationIdName = createNewInvestigation(row, adPetitionId);
		String orderId = createNewOrder(adInvestigationIdName.split("###")[0]);
		//LinkedHashMap<String, String> record = new LinkedHashMap<String, String>();
		//record.put("ADCVD_Order__c", orderId);
		String adminReviewId = createNewSegment(orderId, "Expedited Review");
		testCaseStatus = testCaseStatus & ADCVDLib.validateSegmentStatus_A(adminReviewId, "Expedited Review");
	}
	/**
	 * This method is New_Shipper_Review_Status_Validation status
	*/
	@Test(enabled = true, priority=9)
	void New_Shipper_Review_Status_Validation() throws Exception
	{
		printLog("New_Shipper_Review_Status_Validation");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_009");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		String adCaseId = createNewCase(row, "A-");		
		String adPetitionId = createNewPetition(row, adCaseId, "");
		String adInvestigationIdName = createNewInvestigation(row, adPetitionId);
		String orderId = createNewOrder(adInvestigationIdName.split("###")[0]);
		//LinkedHashMap<String, String> record = new LinkedHashMap<String, String>();
		//record.put("ADCVD_Order__c", orderId);
		String adminReviewId = createNewSegment(orderId, "New Shipper Review");
		testCaseStatus = testCaseStatus & ADCVDLib.validateSegmentStatus_A(adminReviewId, "New Shipper Review");
	}
	
	/**
	 * This method is New_Shipper_Review_Status_Validation status
	*/
	@Test(enabled = true, priority=10)
	void Changed_Circumstance_Review_Status_Validation() throws Exception
	{
		printLog("Changed_Circumstance_Review_Status_Validation");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_010");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		String adCaseId = createNewCase(row, "A-");		
		String adPetitionId = createNewPetition(row, adCaseId, "");
		String adInvestigationIdName = createNewInvestigation(row, adPetitionId);
		String orderId = createNewOrder(adInvestigationIdName.split("###")[0]);
		//LinkedHashMap<String, String> record = new LinkedHashMap<String, String>();
		//record.put("ADCVD_Order__c", orderId);
		String adminReviewId = createNewSegment(orderId, "Changed Circumstances Review");
		testCaseStatus = testCaseStatus & ADCVDLib.validateSegmentStatus_B(adminReviewId, "Changed Circumstances Review");
	}
	/**
	 * This method is New_Shipper_Review_Status_Validation status
	*/
	@Test(enabled = true, priority=11)
	void Anticircumvention_Status_Validation() throws Exception
	{
		printLog("Anticircumvention_Status_Validation");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_011");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		String adCaseId = createNewCase(row, "A-");		
		String adPetitionId = createNewPetition(row, adCaseId, "");
		String adInvestigationIdName = createNewInvestigation(row, adPetitionId);
		String orderId = createNewOrder(adInvestigationIdName.split("###")[0]);
		//LinkedHashMap<String, String> record = new LinkedHashMap<String, String>();
		//record.put("ADCVD_Order__c", orderId);
		String adminReviewId = createNewSegment(orderId, "Anti-Circumvention Review");
		testCaseStatus = testCaseStatus & ADCVDLib.validateSegmentStatus_B(adminReviewId, "Anti-Circumvention Review");
	}
	
	/**
	 * This method is for scope inquirey status validation
	*/
	@Test(enabled = true, priority=12)
	void Scope_Status_Validation() throws Exception
	{
		printLog("Scope_Status_Validation");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_012");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		String adCaseId = createNewCase(row, "A-");		
		String adPetitionId = createNewPetition(row, adCaseId, "");
		String adInvestigationIdName = createNewInvestigation(row, adPetitionId);
		String orderId = createNewOrder(adInvestigationIdName.split("###")[0]);
		//LinkedHashMap<String, String> record = new LinkedHashMap<String, String>();
		//record.put("ADCVD_Order__c", orderId);
		String scopeInquiryId = createNewSegment(orderId, "Scope Inquiry");
		testCaseStatus = testCaseStatus & ADCVDLib.validateSegmentStatus_C(scopeInquiryId, "Scope Inquiry");
	}
	
	/**
	 * This method is for Sunset status validation
	*/
	@Test(enabled = true, priority=13)
	void Sunset_Status_Validation() throws Exception
	{
		printLog("Sunset_Status_Validation");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_013");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		System.out.println("start Test");
		String adCaseId = createNewCase(row, "A-");		
		String adPetitionId = createNewPetition(row, adCaseId, "");
		String adInvestigationIdName = createNewInvestigation(row, adPetitionId);
		String orderId = createNewOrder(adInvestigationIdName.split("###")[0]);
		//LinkedHashMap<String, String> record = new LinkedHashMap<String, String>();
		//record.put("ADCVD_Order__c", orderId);
		String sunsetReviewId = createNewSegment(orderId, "Sunset Review");
		testCaseStatus = testCaseStatus & ADCVDLib.validateSegmentStatus_D(sunsetReviewId, "Sunset Review");
	}
	
	/**
	 * This method is Remand Litigation statuses
	*/
	@Test(enabled = true, priority=14)
	void Remand_Status_Validation() throws Exception
	{
		printLog("Remand_Status_Validation");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_014");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		GuiTools.setTestCaseDescription(row.get("Test_Case_Description"));
		printLog(GuiTools.getTestCaseName());
		LinkedHashMap<String, String> record = new LinkedHashMap<String, String>();
		String adCaseId = createNewCase(row, "A-");
		String adPetitionId = createNewPetition(row, adCaseId, "");
		String remandId = createNewLitigation(adPetitionId, "Remand");
		testCaseStatus = testCaseStatus & ADCVDLib.validateLitigationStatus(remandId, "remand");
	}
		
	/**
	 * This method is validating International Litigation statuses
	*/
	@Test(enabled = true, priority=15)
	void International_Litigation_Status_Validation() throws Exception
	{
		printLog("International_Litigation_Status_Validation");
		LinkedHashMap<String, String> row = getTestCaseInfo(dataPool, "TC_TAG_015");
		GuiTools.setTestCaseName(row.get("Test_Case_Name"));
		LinkedHashMap<String, String> record = new LinkedHashMap<String, String>();
		String adCaseId = createNewCase(row, "A-");
		String adPetitionId = createNewPetition(row, adCaseId, "");
		String litigationId = createNewLitigation(adPetitionId, "International Litigation");
		testCaseStatus = testCaseStatus & ADCVDLib.validateLitigationStatus(litigationId, "Interntional Litigation");
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
	 public static  void initiateRecordType()
    {
    	recordType.put("Administrative Review","012t0000000TSjxAAG");
    	recordType.put("Anti-Circumvention Review","012t0000000TSjyAAG");
    	recordType.put("Changed Circumstances Review","012t0000000TSjzAAG");
    	recordType.put("Expedited Review","012t0000000TSk0AAG");
    	recordType.put("New Shipper Review","012t0000000TSk1AAG");
    	recordType.put("Scope Inquiry","012t0000000TSk2AAG");
    	recordType.put("Sunset Review","012t0000000TSk3AAG");
    	recordType.put("Remand","012t0000000TSjsAAG");
    	recordType.put("International Litigation","012t0000000TSjrAAG");
    	
    }
	 
	 
	 /**
	 * This method creates new ADCVD case
	 * @param row: map of test case's data
	 * @param type, type of case, A, CVD
	 * @return true case created correctly, false if not
	 * @exception Exception
	*/
	public static String createNewCase(LinkedHashMap<String, String> row, 
										String type) throws Exception
	{
		String cType = "";
		if(type.equals("A-")) cType = "AD ME";
		else cType = "CVD";
		LinkedHashMap<String, String> record = new LinkedHashMap<String, String>();
		record.clear();
		record.put("Name", type+ADCVDLib.getCaseName());
		record.put("Commodity__c", row.get("Commodity"));
		record.put("ADCVD_Case_Type__c", cType);
		record.put("Product__c", row.get("Product")+"_"+
				new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss").format(new Date()));
		record.put("Product_Short_Name__c", row.get("Product_Short_Name"));
		record.put("Country__c", row.get("Country"));
		//record.put("Record_Type__c", row.get("Record_Type"));
		String caseId = APITools.createObjectRecord("ADCVD_Case__c", record);
		if(caseId!=null)
		{
			String caseNameAd = record.get("Name");
			updateHtmlReport("Create Case", "User is able to create a new "+cType.replace(" ME", "")+" case", 
					"Case: <span class = 'boldy'>"+" "+caseNameAd+"</span>", "Step", "pass", "");
		}else
		{
			failTestCase("Create AD new Case", "User is able to create a new "+cType.replace(" ME", "")+" case",
					"Not as expected", "Step", "fail", "");
		}
		return caseId;
	}
	
	 /**
		 * This method creates new Petition
		 * @param row: map of test case's data
		 * @param caseId, case identifier
		 * @param outcome, petition outcome
		 * @return true case created correctly, false if not
		 * @exception Exception
		*/
		public static String createNewPetition(LinkedHashMap<String, String> row, 
											String caseId, String outcome) throws Exception
		{
			LinkedHashMap<String, String> record = new LinkedHashMap<String, String>();
			record.put("ADCVD_Case__c", caseId);
			record.put("Petition_Filed__c", todayStr);
			if (outcome.equals("Self-Initiated"))
			{
				record.put("Actual_Initiation_Signature__c", todayStr);
				record.put("Calculated_Initiation_Signature__c", todayStr);
				record.put("Petition_Outcome__c", "Self-Initiated");
			}
			String petitionId = APITools.createObjectRecord("Petition__c", record);
			if(petitionId!=null)
			{
				String query = "select+name+from+Petition__c+where+id='"+petitionId+"'";
				JSONObject jObj = APITools.getRecordFromObject(query);
				updateHtmlReport("Create petition", "User is able to create a new petition", 
						"Petition: <span class = 'boldy'>"+" "+jObj.getString("Name")+"</span>", "Step", "pass", "");
			}else
			{
				failTestSuite("Create petition", "User is able to create a new petition", 
						"Not As expected", "Step", "fail", "");
			}
			return petitionId;
		}
		
		/**
		 * This method creates new Investigation
		 * @param row: map of test case's data
		 * @param petitionId, petition identifier
		 * @return true case created correctly, false if not
		 * @exception Exception
		*/
		public static String createNewInvestigation(LinkedHashMap<String, String> row, 
													String petitionId) throws Exception
		{
			LinkedHashMap<String, String> record = new LinkedHashMap<String, String>();
			String investigationName = "";
			record.clear();
			record.put("Petition__c", petitionId);
			String investigationAd = APITools.createObjectRecord("Investigation__c", record);
			if(investigationAd!=null)
			{
				String query = "select+name+from+Investigation__c+where+id='"+investigationAd+"'";
				JSONObject jObj = APITools.getRecordFromObject(query);
		       	investigationName = jObj.getString("Name");
				updateHtmlReport("Create Investigation", "User is able to create a new Investigation", 
						"Investigation: <span class = 'boldy'>"+" "+investigationName+"</span>", "Step", "pass", "" );
			}else
			{
				failTestCase("Create Investigation", "User should be able to create a new Investigation", 
						"Not As expected", "Step", "fail", "");
			}
			return investigationAd+"###"+investigationName;
		}

		/**
		 * This method creates new Order
		 * @param row: map of test case's data
		 * @param petitionId, petition identifier
		 * @return created order
		 * @exception Exception
		*/
		public static String createNewOrder(String investigationId) throws Exception
		{
			LinkedHashMap<String, String> record = new LinkedHashMap<String, String>();
			record.put("Investigation__c", investigationId);
			String orderId = APITools.createObjectRecord("ADCVD_Order__c", record);
			if(orderId != null)
	       {
		       	JSONObject jObj = APITools.getRecordFromObject("Select+Name+From+ADCVD_Order__c+Where+id='"+orderId+"'");
		       	String orderName = jObj.getString("Name");
		       	updateHtmlReport("Create Order", "User is able to create a new Order", 
						"Order <span class = 'boldy'>"+" "+orderName+"</span>", "Step", "pass", "" );
	       }
			else 
			{
				failTestCase("Create new Order", "user is able to create an order", "Not as expected",
							"Step", "fail", "");
			}
			return orderId;
		}
		
		/**
		 * This method creates new Litigation/Remand
		 * @param row: map of test case's data
		 * @param petitionId, petition identifier
		 * @return created order
		 * @exception Exception
		*/
		public static String createNewLitigation(String petitionId, String litigType) throws Exception
		{
			LinkedHashMap<String, String> record = new LinkedHashMap<String, String>();
			JSONObject jObj = null;
			record.clear();
			record.put("Petition__c", petitionId);
			
			if(litigType.equalsIgnoreCase("International Litigation"))
			{
				record.put("RecordTypeId", recordType.get("International Litigation"));
				record.put("Request_Filed__c", todayStr);
			}
			else//Remand
			{
				record.put("RecordTypeId", recordType.get("Remand"));
				record.put("Expected_Final_Signature_Before_Ext__c", todayStr);
				
			}
			String litigationId = APITools.createObjectRecord("Litigation__c", record);
			String sqlString = "select+id,name+from+litigation__c+where+id='litigationId'";
			if(litigationId != null)
	        {
				jObj = APITools.getRecordFromObject(sqlString.replace("litigationId", litigationId));
		       	String litigName = jObj.getString("Name");
		       	updateHtmlReport("Create International Litigation", "User is able to create a new '"+litigType+"'", 
						"Litigation id: <span class = 'boldy'>"+" "+litigName+"</span>", "Step", "pass", "" );
	        }
		   else
		   {
				failTestCase("Create International Litigation", "User is able to create a new '"+litigType+"'", 
						"Not as expected", "Step", "fail", "" );
		   }
			return litigationId;
		}
		
		
		/**
		 * This method creates new Order
		 * @param row: map of test case's data
		 * @param petitionId, petition identifier
		 * @return created order
		 * @exception Exception
		*/
		public static String createNewSegment(String orderId, String segmentType) throws Exception
		{
			LinkedHashMap<String, String> record = new LinkedHashMap<String, String>();
			record.put("ADCVD_Order__c", orderId);
			record.put("RecordTypeId", recordType.get(segmentType));
			String segmentId = null;
			switch (segmentType)
			{
				case "Administrative Review":
				{
					record.put("Will_you_Amend_the_Final__c", "Yes");
					record.put("Final_Date_of_Anniversary_Month__c", todayStr);
					break;
				}	
				
				case "Anti-Circumvention Review":
				{
					record.put("Application_Accepted__c", todayStr);
					record.put("Type_of_Circumvention_Inquiry__c", "Later-Developed Merchandise");
					break;
				}
				
				case "Changed Circumstances Review":
				{
					record.put("Preliminary_Determination__c", "Yes");
					record.put("Request_Filed__c", todayStr);
					break;
				}
				
				case "Expedited Review":
				{
					record.put("Calculated_Initiation_Signature__c", todayStr);
					break;
				}
				
				case "New Shipper Review":
				{
					record.put("Calculated_Initiation_Signature__c", todayStr);
					break;
				}
				
				case "Scope Inquiry":
				{
					record.put("Request_Filed__c", todayStr);
					record.put("Actual_Date_of_Decision_on_HoP__c", todayStr);
					record.put("Decision_on_How_to_Proceed__c", "Formal");
					record.put("Type_of_Scope_Ruling__c", "K (1)");
					break;
				}
				
				case "Sunset Review":
				{
					record.put("Notice_of_intent_to_participate_Ips__c", "Yes");
					record.put("Domestic_Party_File_Substan_Response__c", "No");
					segmentId = APITools.createObjectRecord("Segment__c", record);
					if(segmentId == null) 
				    {
						failTestCase("Create segment "+segmentType, "User is able to create a new '"+segmentType+"' segment", 
								"Not as expected", "Step", "fail", "");
				    }
					//90Days
					record.put("Published_Date__c", todayStr);
					record.put("Cite_Number__c", "None");
					record.put("Type__c", "Initiation");
					String fridI = APITools.createObjectRecord("Federal_Register__c", record);
					//120 Day
					record.clear();
					record.put("Domestic_Party_File_Substan_Response__c", "Yes");
					String code = APITools.updateRecordObject("Segment__c", segmentId, record);
					//240 Day
					record.clear();
					record.put("Review_to_address_zeroing_in_Segments__c", "Yes");
					record.put("Respondent_File_Substantive_Response__c", "Yes");
					code = APITools.updateRecordObject("Segment__c", segmentId, record);
					String query = "select+id,Name+from+segment__c+where+id='"+segmentId+"'";
					JSONObject jObj = APITools.getRecordFromObject(query);
			       	updateHtmlReport("Create segment "+segmentType, "User is able to create a new '"+segmentType+"' segment", 
							"Segment id: <span class = 'boldy'>"+" "+jObj.getString("Name")+"</span>", "Step", "pass", "");
					return segmentId;
					//break;
				}
				default:
				{
					break;
				}
			}
			segmentId = APITools.createObjectRecord("Segment__c", record);
			if(segmentId != null)
	        {
				String query = "select+id,Name+from+segment__c+where+id='"+segmentId+"'";
				JSONObject jObj = APITools.getRecordFromObject(query);
		       	updateHtmlReport("Create segment "+segmentType, "User is able to create a new '"+segmentType+"' segment", 
						"Segment id: <span class = 'boldy'>"+" "+jObj.getString("Name")+"</span>", "Step", "pass", "");
	       }
		   else 
		   {
				failTestCase("Create segment "+segmentType, "User is able to create a new '"+segmentType+"' segment", 
						"Not as expected", "Step", "fail", "");
		   }
		   return segmentId;
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
}
