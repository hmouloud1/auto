/**
 * MilCorp
 * Mouloud Hamdidouche
 * September, 2019
*/

package libs;

import static GuiLibs.GuiTools.checkElementExists;
import static GuiLibs.GuiTools.clickElement;
import static GuiLibs.GuiTools.clickElementJs;
import static GuiLibs.GuiTools.elementExists;
import static GuiLibs.GuiTools.enterText;
import static GuiLibs.GuiTools.failTestSuite;
import static GuiLibs.GuiTools.getElementAttribute;
import static GuiLibs.GuiTools.guiMap;
import static GuiLibs.GuiTools.highlightElement;
import static GuiLibs.GuiTools.holdSeconds;
import static GuiLibs.GuiTools.navigateTo;
import static GuiLibs.GuiTools.pageRefresh;
import static GuiLibs.GuiTools.replaceGui;
import static GuiLibs.GuiTools.scrollByPixel;
import static GuiLibs.GuiTools.scrollToElement;
import static GuiLibs.GuiTools.selectElementByValue;
import static GuiLibs.GuiTools.setBrowserTimeOut;
import static GuiLibs.GuiTools.switchBackFromFrame;
import static GuiLibs.GuiTools.switchToFrame;
import static GuiLibs.GuiTools.updateHtmlReport;
import static ReportLibs.ReportTools.printLog;

import java.io.IOException;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ReportLibs.HtmlReport;
import ServiceLibs.APITools;

public class ADCVDLib{
	public static String petitionFiledDate, caseId, caseId2, petitionId,petitionId2, investigationId,
	orderId, segmentId,newShipId2, scopeInqId2, sunsetRevId2,
	adminReviewId, antiCirId, changeCirId, expeditedRevId, orderId2, segmentId2,
	newShipId, scopeInqId, sunsetRevId,	investigationId2,remandId, litigationId,
	 adminReviewId2, antiCirId2, changeCirId2, expeditedRevId2,	
	whereAmI, actualInitiationSignature, calculatedInitiationSignature,  
	petitionOutcome, petitionInitiationAnnouncementDate="";
	public static boolean standardTolling = true;
	public static int petitionInitiationExtension;
	//public static ArrayList<LinkedHashMap<String, String>> tollingDates;
	public static JSONObject  standardTollingDaysObj, recordTollingDaysObj;
	static DateFormat format;
	static Calendar calendar;
	static String caseType;
	public static String standardTollingDaysStr, recordTollingDaysStr, upadtedWith;
	//static String orderId;
	public ADCVDLib() throws IOException {
		//super();
		this.format = new SimpleDateFormat("yyyy-MM-dd");
		this.calendar = Calendar.getInstance();
	}
	/**
	 * This method login to ADCVD web application
	 * @param url: url for the application
	 * @param user: user
	 * @param password: password
	 * @exception Exception
	 */
	public static boolean loginToAdCvd(String url, 
									   String user, 
									   String password) throws Exception
	{
		boolean loginStatus = true;
		navigateTo(url);
		int currentTimeOut = setBrowserTimeOut(3);
		if(checkElementExists(guiMap.get("homeObjectLink")))
		{
			highlightElement(guiMap.get("homeObjectLink"), "green");
			holdSeconds(2);
			updateHtmlReport("Login to AD/CVD App with user "+user,  "User able to log", "As expected", "Step", 
					"pass", "Login Screen Shot");
			setBrowserTimeOut(currentTimeOut);
			return true;
		}
		setBrowserTimeOut(currentTimeOut);
		enterText(guiMap.get("userName"), user);
		enterText(guiMap.get("password"), password);
		holdSeconds(1);
		clickElementJs(guiMap.get("loginTo"));
		holdSeconds(5);
		if(! checkElementExists(guiMap.get("homeObjectLink")))
		{
			failTestSuite("Login to AD/CVD App with user "+user, "User is able to login", 
				"Not as expected", "Step", "fail", "Login failed");
			loginStatus = false;
		}else
		{
			highlightElement(guiMap.get("homeObjectLink"), "green");
			holdSeconds(2);
			updateHtmlReport("Login to AD/CVD App with user "+user,  "User able to login", "As expected", 
					"Step", "pass", "Login to ADCVD");
		}
		return loginStatus;
	}
	
	/**
	 * This method validates petition fields
	 * @param rObj: Object containing record information
	 * @param dateName: name of the date to be validated
	 * @param dateType: type of the date holiday, weekend, tolling day
	 * @param date: the Value of the date
	 * @return true if all dates matches, false if not
	 * @exception Exception
	*/
	//@SuppressWarnings({ "unused", "unused" })
	public static boolean validatePetitionFields(JSONObject rObj,
												String dateName,
												String dateType,
												String date) throws Exception
	{
		boolean allMatches = true;
		String actualValue;
		
		/////////////////////////////////////////////////////////////////////////////////////////////////
		petitionInitiationExtension = readNumberFromDb(noNullVal(rObj.getString("Initiation_Extension_of_days__c")));
		petitionFiledDate = noNullVal(rObj.getString("Petition_Filed__c"));
		petitionOutcome = noNullVal(rObj.getString("Petition_Outcome__c"));
		actualInitiationSignature = noNullVal(rObj.getString("Actual_Initiation_Signature__c"));
		//Calculated Initiation Signature
		String calculatedInitiationSignature = calculateDate(petitionInitiationExtension+20, 
				"Calculated Initiation Signature", "calendar", petitionFiledDate);
		//Initiation Issues Due to DAS		 
		String initiationIssuesDueToDas = calculateDate(-3, "Initiation Concurrence Due to DAS",  
				"business", calculatedInitiationSignature);
		//Initiation Concurrence Due to DAS		 
		String initiationConcurrenceDueToDas = calculateDate(-1, "Initiation Concurrence Due to DAS",  
						"business", calculatedInitiationSignature);
		switch (dateName)
		{
			case "Calculated_Initiation_Signature__c":
			{
				actualValue = noNullVal(rObj.getString("Calculated_Initiation_Signature__c"));
				allMatches = allMatches & compareAndReport(dateType, date, calculatedInitiationSignature, actualValue);
				break;
			}
			case "Initiation_Issues_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Initiation_Issues_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, initiationIssuesDueToDas, actualValue);
				break;
			}
			case "Initiation_Concurrence_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Initiation_Concurrence_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, initiationConcurrenceDueToDas, actualValue);
				break;
			}
			default:
			{
				break;
			}
		}//switch
		return allMatches;
	}
	/**
	 * This method validates Investigation dates
	 * @param rObj, object containing dates
	 * @param dateName, Name of the date to be tested
	 * @param dateType, type of the date (weekend, holiday...etc)
	 * @param date, Date value
	 * @exception Exception
	*/
	public static boolean validateInvestigationFields(	JSONObject rObj,
														String dateName,
														String dateType,
														String date) throws Exception
	{
		boolean allMatches = true;
		String actualValue;
		int daysNum;
		String caseName = noNullVal(rObj.getString("ADCVD_Case_Number_Text__c")); //ADCVD_Case_Number__c ADCVD_Case_Number_Text__c
		int finalExtension = readNumberFromDb(noNullVal(rObj.getString("Final_Extension_of_days__c")));
		String actualPreliminarySignature = noNullVal(rObj.getString("Actual_Preliminary_Signature__c"));
		String investigationOutcome = noNullVal(rObj.getString("Investigation_Outcome__c"));
		String itcNotificationToDocOfFinalDeterm = noNullVal(rObj.getString("ITC_Notification_to_DOC_of_Final_Determ__c"));
		String actualAmendedFinalSignature = noNullVal(rObj.getString("Actual_Amended_Final_Signature__c"));
		String willYouAmendTheFinal = noNullVal(rObj.getString("Will_you_Amend_the_Final__c"));
		int prelimExtension = readNumberFromDb(noNullVal(rObj.getString("Prelim_Extension_of_days__c")));
		String actualfinalSignature = noNullVal(rObj.getString("Actual_Final_Signature__c"));
		String calculatedAmendedFinalSignature = noNullVal(rObj.getString("Calculated_Amended_Final_Signature__c"));
		String petitionQuery = "SELECT+Petition_Outcome__c,Petition_Filed__c,Actual_Initiation_Signature__c,"
				+ "Calculated_Initiation_Signature__c,Initiation_Extension_of_days__c+From+petition__c+where+id='"+petitionId+"'";
		JSONObject pObj = APITools.getRecordFromObject(petitionQuery);
		petitionOutcome = noNullVal(pObj.getString("Petition_Outcome__c"));
	   	petitionFiledDate = noNullVal(pObj.getString("Petition_Filed__c"));
	   	actualInitiationSignature = noNullVal(pObj.getString("Actual_Initiation_Signature__c"));
	   	calculatedInitiationSignature = noNullVal(pObj.getString("Calculated_Initiation_Signature__c"));
	   	petitionInitiationExtension =readNumberFromDb(noNullVal(pObj.getString("Initiation_Extension_of_days__c")));
		////////////////////////////////////////////////////////////////////////
		//Calculated ITC Prelim Determination
		String calculatedITCPrelimDetermination="";
		if(petitionOutcome.equals("Self-Initated"))
		{
			if(petitionInitiationAnnouncementDate!=null && !petitionInitiationAnnouncementDate.equals(""))
			{
				calculatedITCPrelimDetermination = calculateDate(45, 
						"Calculated ITC Prelim Determination", "calendar", petitionInitiationAnnouncementDate);
			}
		}
		else
		{
			calculatedITCPrelimDetermination = calculateDate(petitionInitiationExtension+45, 
			"Calculated ITC Prelim Determination", "calendar", petitionFiledDate);
		}
		//Calculated Prelim Extension Request File
		daysNum = caseName.contains("A-")? 115:40;
		String calculatedPrelimExtensionRequestFile = calculateDate(daysNum,
				"Calculated Prelim Extension Request File", "calendar", 
				!actualInitiationSignature.equals("")?actualInitiationSignature:calculatedInitiationSignature);
		//Calculated Postponement of PrelimDeterFR
		daysNum = caseName.contains("A-")? 120:45;
		String calculatedPostponementOfPrelimDeterFr = calculateDate(daysNum, 
				"Calculated Postponement of PrelimDeterFR", "calendar", 
				!actualInitiationSignature.equals("")?actualInitiationSignature:calculatedInitiationSignature);
		//Calculated Preliminary Signature
		daysNum = caseName.contains("A-")? 140:65;
		String calculatedPreliminarySignature;
		String federalRegisterPublishDate="";
		if(petitionOutcome.equals("Self-Initiated") && ! federalRegisterPublishDate.equals(""))  
		{
			calculatedPreliminarySignature = calculateDate(prelimExtension + daysNum, 
					"Calculated Preliminary Signature", "calendar",
					federalRegisterPublishDate);
		}
		else
		{
			calculatedPreliminarySignature = calculateDate(prelimExtension + daysNum,
					"Calculated Preliminary Signature", "calendar", 
					!actualInitiationSignature.equals("")?actualInitiationSignature:calculatedInitiationSignature);
		}
		//Prelim Team Meeting Deadline
		String prelimTeamMeetingDeadline = calculateDate(-21, "Prelim Team Meeting Deadline", 
				"calendar", calculatedPreliminarySignature);
		//Prelim Issues Due to DAS
		String prelimIssuesDueToDas = calculateDate(-10, "Prelim Issues Due to DAS", 
				"business", calculatedPreliminarySignature);
		//Prelim Concurrence Due to DAS
		String prelimConcurrenceDueToDas = calculateDate(-5, "Prelim Concurrence Due to DAS", 
				"business", 
				calculatedPreliminarySignature);
		//Calculated Final Signature
		federalRegisterPublishDate = "";
		String calculatedFinalSignature = "";
		if (finalExtension>0 && ! federalRegisterPublishDate.equals(""))
		{
			calculatedFinalSignature = calculateDate(75 + finalExtension, 
					"Calculated Final Signature", "calendar",	
					federalRegisterPublishDate);
		}
		else 
		{
			calculatedFinalSignature = calculateDate(75 + finalExtension, 
					"Calculated Final Signature", "calendar",  
				!actualPreliminarySignature.equals("")?actualPreliminarySignature:calculatedPreliminarySignature);
		}
		//Final Team Meeting Deadline
		String finalTeamMeetingDeadline = calculateDate(-21, "Final Team Meeting Deadline", 
				"calendar", calculatedFinalSignature);
		
		//Est ITC Notification to DOC of Final Det
		String estItcNotificationToDocOfFinalDeterm = calculateDate(45, 
				"Est ITC Notification to DOC of Final Det", "calendar",
				!actualfinalSignature.equals("")?actualfinalSignature:calculatedFinalSignature);
		//Estimated Order FR Published
		String estimatedOrderFRPublished;
		if(!investigationOutcome.equals("") && !investigationOutcome.equals("Order"))  
		{
			estimatedOrderFRPublished = "";
		}
		else
		{
			estimatedOrderFRPublished = calculateDate(7, "Estimated Order FR Published", "calendar", 
			!itcNotificationToDocOfFinalDeterm.equals("")?itcNotificationToDocOfFinalDeterm:
				estItcNotificationToDocOfFinalDeterm);
		}
		//Calculated Order FR Signature
		String calculatedOrderFrSignature;
		if(!investigationOutcome.equals("") && !investigationOutcome.equals("Order"))  
		{
			calculatedOrderFrSignature = "";
		}
		else
		{
			calculatedOrderFrSignature = calculateDate(3, "Calculated Order FR Signature", "calendar", 
			!itcNotificationToDocOfFinalDeterm.equals("")?itcNotificationToDocOfFinalDeterm:
				estItcNotificationToDocOfFinalDeterm);
		}
		System.out.println(estItcNotificationToDocOfFinalDeterm + "cal" +calculatedOrderFrSignature+ "est"+estimatedOrderFRPublished);
		//Final Issues Due to DAS
		String finalIssuesDueToDas = calculateDate(-10, "Final Issues Due to DAS", 
				"business", calculatedFinalSignature);
		//Final Concurrence Due to DAS
		String finalConcurrenceDueToDas = calculateDate(-5, "Final Concurrence Due to DAS", 
				"business", calculatedFinalSignature);
		//Final Announcement Date
		String finalAnnouncementDate = calculateDate(1, "Final Announcement Date", "calendar", 
				!actualfinalSignature.equals("")?actualfinalSignature:calculatedFinalSignature);
		
		//Amended Final Announcement Date
		String amendedFinalAnnouncementDate;
		if(! willYouAmendTheFinal.equals("Yes"))
		{
			amendedFinalAnnouncementDate = "";
		}
		else if (!actualAmendedFinalSignature.equals(""))
		{
			amendedFinalAnnouncementDate = calculateDate(1, "Amended Final Announcement Date", 
					"calendar", actualAmendedFinalSignature);
		}
		else if(!calculatedAmendedFinalSignature.equals(""))
		{
			amendedFinalAnnouncementDate = calculateDate(1, "Amended Final Announcement Date", 
					"calendar", calculatedAmendedFinalSignature);
		}
		else
		{
			amendedFinalAnnouncementDate = "";
		}
		//Calculated Amended Final Signature
		String CalculatedAmendedFinalSignature = calculateDate(31, "Calculated Amended Final Signature", 
				"calendar", calculatedFinalSignature);
		//Amend Final Issues Due to DAS
		String amendFinalIssuesDueToDAS = calculateDate(-10, "Amend Final Issues Due to DAS", 
				"business", CalculatedAmendedFinalSignature);
		//Amend Final Concurrence Due to DAS
		String amendFinalConcurrenceDueToDAS = calculateDate(-5, "Amend Final Concurrence Due to DAS", 
						"business", CalculatedAmendedFinalSignature);
		//Calculated Amended Prelim Determination
		String calcAmendedPrelimDeterminationSig = calculateDate(31, "Calculated Amended Preliminary Signature", 
				"calendar", calculatedPreliminarySignature);
		//Amend Prelim Issues Due to DAS
		String amendPrelimIssuesDueToDAS = calculateDate(-10, "Amend Prelim Issues Due to DAS", 
				"business", calcAmendedPrelimDeterminationSig);
		//Amend Prelim Concurrence Due to DAS
		String amendPrelimConcurrenceDueToDAS = calculateDate(-5, "Amend Prelim Concurrence Due to DAS", 
						"business", calcAmendedPrelimDeterminationSig);
		System.out.println(dateName+"_calculatedFinalSignature_"+calculatedFinalSignature);
		System.out.println("finalAnnouncementDate"+finalAnnouncementDate);
		switch (dateName)
		{
			case "Calc_Amended_Prelim_Determination_Sig__c":
			{
				actualValue = noNullVal(rObj.getString("Calc_Amended_Prelim_Determination_Sig__c"));
				allMatches = allMatches & compareAndReport(dateType, date, calcAmendedPrelimDeterminationSig, actualValue);
				break;
			}
			case "Amend_Prelim_Issues_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Amend_Prelim_Issues_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, amendPrelimIssuesDueToDAS, actualValue);
				break;
			}
			case "Amend_Prelim_Concurrence_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Amend_Prelim_Concurrence_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, amendPrelimConcurrenceDueToDAS, actualValue);
				break;
			}
		
			case "Calculated_Amended_Final_Signature__c":
			{
				actualValue = noNullVal(rObj.getString("Calculated_Amended_Final_Signature__c"));
				allMatches = allMatches & compareAndReport(dateType, date, CalculatedAmendedFinalSignature, actualValue);
				break;
			}
			case "Amend_Final_Issues_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Amend_Final_Issues_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, amendFinalIssuesDueToDAS, actualValue);
				break;
			}
			case "Amend_Final_Concurrence_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Amend_Final_Concurrence_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, amendFinalConcurrenceDueToDAS, actualValue);
				break;
			}
			case "Calculated_Prelim_Extension_Request_File__c":
			{
				actualValue = noNullVal(rObj.getString("Calculated_Prelim_Extension_Request_File__c"));
				allMatches = allMatches & compareAndReport(dateType, date, calculatedPrelimExtensionRequestFile, actualValue);
				break;
			}
			case "Calculated_Postponement_of_PrelimDeterFR__c":
			{
				actualValue = noNullVal(rObj.getString("Calculated_Postponement_of_PrelimDeterFR__c"));
				allMatches = allMatches & compareAndReport(dateType, date, calculatedPostponementOfPrelimDeterFr, actualValue);
				break;
			}
			
			case "Calculated_Preliminary_Signature__c":
			{
				actualValue = noNullVal(rObj.getString("Calculated_Preliminary_Signature__c"));
				allMatches = allMatches & compareAndReport(dateType, date, calculatedPreliminarySignature, actualValue);
				break;
			}
			case "Prelim_Team_Meeting_Deadline__c":
			{
				actualValue = noNullVal(rObj.getString("Prelim_Team_Meeting_Deadline__c"));
				allMatches = allMatches & compareAndReport(dateType, date, prelimTeamMeetingDeadline, actualValue);
				break;
			}
			case "Prelim_Issues_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Prelim_Issues_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, prelimIssuesDueToDas, actualValue);
				break;
			}
			case "Prelim_Concurrence_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Prelim_Concurrence_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, prelimConcurrenceDueToDas, actualValue);
				break;
			}
			case "Calculated_Final_Signature__c":
			{
				actualValue = noNullVal(rObj.getString("Calculated_Final_Signature__c"));
				allMatches = allMatches & compareAndReport(dateType, date, calculatedFinalSignature, actualValue);
				break;
			}
			case "Final_Team_Meeting_Deadline__c":
			{
				actualValue = noNullVal(rObj.getString("Final_Team_Meeting_Deadline__c"));
				allMatches = allMatches & compareAndReport(dateType, date, finalTeamMeetingDeadline, actualValue);
				break;
			}
			case "Est_ITC_Notification_to_DOC_of_Final_Det__c":
			{
				actualValue = noNullVal(rObj.getString("Est_ITC_Notification_to_DOC_of_Final_Det__c"));
				allMatches = allMatches & compareAndReport(dateType, date, estItcNotificationToDocOfFinalDeterm, actualValue);
				break;
			}
			case "Estimated_Order_FR_Published__c":
			{
				actualValue = noNullVal(rObj.getString("Estimated_Order_FR_Published__c"));
				allMatches = allMatches & compareAndReport(dateType, date, estimatedOrderFRPublished, actualValue);
				break;
			}
			case "Calculated_Order_FR_Signature__c":
			{
				actualValue = noNullVal(rObj.getString("Calculated_Order_FR_Signature__c"));
				allMatches = allMatches & compareAndReport(dateType, date, calculatedOrderFrSignature, actualValue);
				break;
			}
			case "Final_Issues_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Final_Issues_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, finalIssuesDueToDas, actualValue);
				break;
			}
			case "Final_Concurrence_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Final_Concurrence_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, finalConcurrenceDueToDas, actualValue);
				break;
			}
			case "Final_Announcement_Date__c":
			{
				actualValue = noNullVal(rObj.getString("Final_Announcement_Date__c"));
				allMatches = allMatches & compareAndReport(dateType, date, finalAnnouncementDate, actualValue);
				break;
			}
			case "Amended_Final_Announcement_Date__c":
			{
				actualValue = noNullVal(rObj.getString("Amended_Final_Announcement_Date__c"));
				allMatches = allMatches & compareAndReport(dateType, date, amendedFinalAnnouncementDate, actualValue);
				break;
			}
			case "Calculated_ITC_Prelim_Determination__c":
			{
				actualValue = noNullVal(rObj.getString("Calculated_ITC_Prelim_Determination__c"));
				allMatches = allMatches & compareAndReport(dateType, date, calculatedITCPrelimDetermination, actualValue);
				break;
			}
			
			
			default:
			{
				break;
			}
		}//switch
		return allMatches;
	}
	
	/**
	 * This method validates administrative review dates
	 * @param rObj, object containing dates
	 * @param dateName, Name of the date to be tested
	 * @param dateType, type of the date (weekend, holiday...etc)
	 * @param date, Date value
	 * @exception Exception
	*/
	public static boolean validateNewSegmentAdministrativeReview(JSONObject rObj,
																	String dateName,
																	String dateType,
																	String date) throws Exception
	{
		boolean allMatches = true;
		String actualValue  = "" ;
		int prelimExtensionDays = readNumberFromDb(noNullVal(rObj.getString("Prelim_Extension__c")));
		String finalDateOfAnniversaryMonth = noNullVal(rObj.getString("Final_Date_of_Anniversary_Month__c"));
		String actualPreliminarySignature = noNullVal(rObj.getString("Actual_Preliminary_Signature__c"));
		int finalExtensionDays = readNumberFromDb(noNullVal(rObj.getString("Final_Extension_of_days__c")));
		String actualFinalSignature = noNullVal(rObj.getString("Actual_Final_Signature__c"));
		String actualAmendedFinalSignature = noNullVal(rObj.getString("Actual_Amended_Final_Signature__c"));
		String willYouAmendTheFinal = noNullVal(rObj.getString("Will_you_Amend_the_Final__c"));
		//String calculatedAmendedFinalSignature = noNullVal(rObj.getString("Calculated_Amended_Final_Signature__c"));
		String actualPrelimIssuesToDas = noNullVal(rObj.getString("Actual_Prelim_Issues_to_DAS__c"));
		String actualPrelimConcurrenceToDas = noNullVal(rObj.getString("Actual_Prelim_Concurrence_to_DAS__c"));
		String segmentOutcome = noNullVal(rObj.getString("Segment_Outcome__c"));
		String actualFinalIssuesToDas = noNullVal(rObj.getString("Actual_Final_Issues_to_DAS__c"));
		String actualFinalConcurrenceToDas = noNullVal(rObj.getString("Actual_Final_Concurrence_to_DAS__c"));
		String actualAmendFinalIssuesToDas = noNullVal(rObj.getString("Actual_Amend_Final_Issues_to_DAS__c"));
		String actualAmendFinalConcurrenceToDas = noNullVal(rObj.getString("Actual_Amend_Final_Concurrence_to_DAS__c"));
		/////////d////////////////////////////// !actualInitiationSignature.equals("")?actualInitiationSignature:calculatedInitiationSignature
		//Calculated Preliminary Signature
		String calculatedAmendedFinalSignature = ""+"";
		String calculatedPreliminarySignature="";
		String publishedDate = "";
		String calculatedFinalSignature="";
		String nextMajorDeadline = "";
		String prelimIssuesDueToDas = "";
		String prelimConcurrenceDueToDas = "";
		String finalIssuesDueToDas="";
		String finalConcurrenceDueToDas= "";
		String nextOfficeDeadline = "";
		String prelimTeamMeetingDeadline="";
		String nextDueToDasDeadline = "";
		String preliminaryAnnouncementDate="";
		String finalAnnouncementDate="";
		String finalTeamMeetingDeadline="";
		String amendFinalIssuesDueToDas;
		String amendFinalConcurrenceDueToDas;
		String amendedFinalAnnouncementDate;
		calculatedPreliminarySignature = calculateDate(prelimExtensionDays + 245, 
						"Calculated Preliminary Signature", "calendar",finalDateOfAnniversaryMonth);
		System.out.println("calculatedPreliminarySignature = " +calculatedPreliminarySignature);
		if(publishedDate.equals("") && !calculatedPreliminarySignature.equals(""))
		{
			calculatedFinalSignature = calculateDate(120 + finalExtensionDays, 
					"Calculated Final Signature", "Calendar", calculatedPreliminarySignature);
		}
		else
		{
			calculatedFinalSignature = calculateDate(120 + finalExtensionDays, 
					"Calculated Final Signature", "Calendar", publishedDate);
		}
		System.out.println("calculatedFinalSignature = " +calculatedFinalSignature);
		//String calculatedAmendedFinalSignature = noNullVal(rObj.getString("Calculated_Amended_Final_Signature__c"));
		//Calculated Amended Final Signature
		if (!willYouAmendTheFinal.equalsIgnoreCase("Yes"))
		{
			calculatedAmendedFinalSignature=""+""; 
		} else if (actualFinalSignature.equals("") && !calculatedFinalSignature.equals(""))
		{
			calculatedAmendedFinalSignature = calculateDate(37, "Calculated Amended Final Signature", 
					"calendar", calculatedFinalSignature);
		} else if(!actualFinalSignature.equals(""))
		{
			calculatedAmendedFinalSignature = calculateDate(37, "Calculated Amended Final Signature", 
					"calendar", actualFinalSignature);
		}
		prelimTeamMeetingDeadline = calculateDate(-21, "Prelim Team Meeting Deadline", "calendar", 
		calculatedPreliminarySignature);
		prelimIssuesDueToDas = calculateDate(-10, "Prelim Issues Due to DAS", 
				"business", calculatedPreliminarySignature);
		prelimConcurrenceDueToDas = calculateDate(-5, "Prelim Concurrence Due to DAS", 
				"business", calculatedPreliminarySignature);
		preliminaryAnnouncementDate = calculateDate(1, 
				"Preliminary Announcement Date", "business", 
				!actualPreliminarySignature.equals("")?actualPreliminarySignature:
					calculatedPreliminarySignature);
		///
		amendFinalIssuesDueToDas = calculateDate(-10, "Amend Final Issues Due to DAS", 
				"business", calculatedAmendedFinalSignature);
		amendFinalConcurrenceDueToDas = calculateDate(-5, "Amend Final Concurrence Due to DAS", 
				"business", calculatedAmendedFinalSignature);
		amendedFinalAnnouncementDate = calculateDate(1, 
				"Amended Final Announcement Date", "business", 
				!actualPreliminarySignature.equals("")?actualPreliminarySignature:
					calculatedAmendedFinalSignature);
		///
		finalTeamMeetingDeadline = calculateDate(-21, "Final Team Meeting Deadline", 
						"calendar", calculatedFinalSignature);
		finalIssuesDueToDas = calculateDate(-10, "Final Issues Due to DAS",  
				"business", calculatedFinalSignature);
		finalConcurrenceDueToDas = calculateDate(-5, "Final Concurrence Due to DAS",  
				"business", calculatedFinalSignature);
		finalAnnouncementDate = calculateDate(1, "Final Announcement Date", "business", 
				!actualFinalSignature.equals("")?actualFinalSignature:calculatedFinalSignature);		
		switch (dateName)
		{
			case "Calculated_Preliminary_Signature__c":
			{
				actualValue = noNullVal(rObj.getString("Calculated_Preliminary_Signature__c"));
				allMatches = allMatches & compareAndReport(dateType, date, calculatedPreliminarySignature, actualValue);
				break;
			}
			case "Calculated_Amended_Final_Signature__c":
			{
				actualValue = noNullVal(rObj.getString("Calculated_Amended_Final_Signature__c"));
				allMatches = allMatches & compareAndReport(dateType, date, calculatedAmendedFinalSignature, actualValue);
				break;
			}
			//Prelim Team Meeting Deadline
			case "Prelim_Team_Meeting_Deadline__c":
			{
				actualValue = noNullVal(rObj.getString("Prelim_Team_Meeting_Deadline__c"));
				allMatches = allMatches & compareAndReport(dateType, date, prelimTeamMeetingDeadline, actualValue);
				break;
			}
			//Prelim Issues Due to DAS
			case "Prelim_Issues_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Prelim_Issues_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, prelimIssuesDueToDas, actualValue);
				break;
			}
			//Calculated Final Signature
			case "Calculated_Final_Signature__c":
			{
				actualValue = noNullVal(rObj.getString("Calculated_Final_Signature__c"));
				allMatches = allMatches & compareAndReport(dateType, date, calculatedFinalSignature, actualValue);
				break;
			}
			//Calculated Amended Final Signature
			//Prelim Concurrence Due to DAS:
			case "Prelim_Concurrence_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Prelim_Concurrence_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, prelimConcurrenceDueToDas, actualValue);
				break;
			}
			case "Preliminary_Announcement_Date__c":
			{	
				actualValue = noNullVal(rObj.getString("Preliminary_Announcement_Date__c"));
				allMatches = allMatches & compareAndReport(dateType, date, preliminaryAnnouncementDate,	actualValue);
				break;
			}
			case "Final_Team_Meeting_Deadline__c":
			{
				actualValue = noNullVal(rObj.getString("Final_Team_Meeting_Deadline__c"));
				allMatches = allMatches & compareAndReport(dateType, date, finalTeamMeetingDeadline, actualValue);
				break;
			}
			case "Final_Issues_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Final_Issues_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, finalIssuesDueToDas, actualValue);
				break;
			}
			case "Final_Concurrence_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Final_Concurrence_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, finalConcurrenceDueToDas, actualValue);
				break;
			}
			case "Final_Announcement_Date__c":
			{
				actualValue = noNullVal(rObj.getString("Final_Announcement_Date__c"));
				allMatches = allMatches & compareAndReport(dateType, date, finalAnnouncementDate, actualValue);
				break;
			}
			case "Amend_Final_Concurrence_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Amend_Final_Concurrence_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, amendFinalConcurrenceDueToDas, actualValue);
				break;
			}
			case "Amend_Final_Issues_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Amend_Final_Issues_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, amendFinalIssuesDueToDas, actualValue);
				break;
			}
			case "Amended_Final_Announcement_Date__c":
			{
				actualValue = noNullVal(rObj.getString("Amended_Final_Announcement_Date__c"));
				allMatches = allMatches & compareAndReport(dateType, date, amendedFinalAnnouncementDate, actualValue);
				break;
			}
			default:
			{
				//
			}
		}
		return allMatches;
	}
	
	/**
	 * This method validates Anti Circumvention dates
	 * @param rObj, object containing dates
	 * @param dateName, Name of the date to be tested
	 * @param dateType, type of the date (weekend, holiday...etc)
	 * @param date, Date value
	 * @exception Exception
	*/
	public static boolean validateNewSegmentAntiCircumventionReview(JSONObject rObj,
																	String dateName,
																	String dateType,
																	String date) throws Exception
	{
		boolean allMatches = true;
		String actualValue  = "" ;
		readNumberFromDb(noNullVal(rObj.getString("Prelim_Extension__c")));
		noNullVal(rObj.getString("Final_Date_of_Anniversary_Month__c"));
		String actualPreliminarySignature = noNullVal(rObj.getString("Actual_Preliminary_Signature__c"));
		int finalExtensionDays = readNumberFromDb(noNullVal(rObj.getString("Final_Extension_of_days__c")));
		String actualFinalSignature = noNullVal(rObj.getString("Actual_Final_Signature__c"));
		noNullVal(rObj.getString("Actual_Amended_Final_Signature__c"));
		noNullVal(rObj.getString("Will_you_Amend_the_Final__c"));
		noNullVal(rObj.getString("Calculated_Amended_Final_Signature__c"));
		String actualPrelimIssuesToDas = noNullVal(rObj.getString("Actual_Prelim_Issues_to_DAS__c"));
		String actualPrelimConcurrenceToDas = noNullVal(rObj.getString("Actual_Prelim_Concurrence_to_DAS__c"));
		String segmentOutcome = noNullVal(rObj.getString("Segment_Outcome__c"));
		String actualFinalIssuesToDas = noNullVal(rObj.getString("Actual_Final_Issues_to_DAS__c"));
		String actualFinalConcurrenceToDas = noNullVal(rObj.getString("Actual_Final_Concurrence_to_DAS__c"));
		/*noNullVal(rObj.getString("Amend_Final_Issues_Due_to_DAS__c")); 
		noNullVal(rObj.getString("Amend_Final_Concurrence_Due_to_DAS__c"));
		noNullVal(rObj.getString("Actual_Amend_Final_Issues_to_DAS__c"));
		noNullVal(rObj.getString("Actual_Amend_Final_Concurrence_to_DAS__c"));*/
		int initiationExtensionDays = readNumberFromDb(noNullVal(rObj.getString("Initiation_Extension_of_days__c")));
		String applicationAccepted = noNullVal(rObj.getString("Application_Accepted__c"));
		String actualInitiationIssuesToDas = noNullVal(rObj.getString("Actual_Initiation_Issues_to_DAS__c"));
		String actualInitiationConcurrenceToDas = noNullVal(rObj.getString("Actual_Initiation_Concurrence_to_DAS__c"));
		String actualInitiationSignature = "";
		///////////////////////////////////////////
		String calculatedInitiationSignature =  calculateDate(45+initiationExtensionDays,
				"Calculated Initiation Signature",
				"calendar",applicationAccepted);
		String calculatedFinalSignature =  calculateDate(300 + finalExtensionDays, 
						"Calculated Final Signature", "calendar", 
				!actualInitiationSignature.equals("")?actualInitiationSignature:calculatedInitiationSignature);
		String finalAnnouncementDate = calculateDate(1, "Final Announcement Date", "calendar", 
				!actualFinalSignature.equals("")?actualFinalSignature:calculatedFinalSignature);
		String initiationIssuesDueToDas = "";
		initiationIssuesDueToDas = calculateDate(-10, "Initiation Issues Due to DAS", "business", 
				calculatedInitiationSignature);
		String initiationConcurrenceDueToDas = "";
		initiationConcurrenceDueToDas = calculateDate(-5, "Initiation Concurrence Due to DAS", 
				"business", calculatedInitiationSignature);
		String calculatedPreliminarySignature = calculateDate(120, "Calculated Preliminary Signature", "calendar", 
				!actualInitiationSignature.equals("")?actualInitiationSignature:calculatedInitiationSignature);
		String prelimTeamMeetingDeadline = calculateDate(-21, "Prelim Team Meeting Deadline", 
				"calendar", calculatedPreliminarySignature);
		String prelimIssuesDueToDas = calculateDate(-10, "Prelim Issues Due to DAS", 
				"business", calculatedPreliminarySignature);
		String prelimConcurrenceDueToDas = calculateDate(-5, "Prelim Concurrence Due to DAS",
				"business", calculatedPreliminarySignature);
		String finalTeamMeetingDeadline = calculateDate(-21, "Final Team Meeting Deadline", 
				"calendar", calculatedFinalSignature);
		String finalIssuesDueToDas = calculateDate(-10, "Final Issues Due to DAS",  
				"business", calculatedFinalSignature);
		String finalConcurrenceDueToDas = calculateDate(-5, "Final Concurrence Due to DAS", 
				"business", calculatedFinalSignature);
		switch (dateName)
		{
			//Calculated Initiation Signature
			case "Calculated_Initiation_Signature__c":
			{
				actualValue = noNullVal(rObj.getString("Calculated_Initiation_Signature__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
						calculatedInitiationSignature, actualValue);
				break;
			}
			//Calculated Final Signature
			case "Calculated_Final_Signature__c":
			{
				actualValue = noNullVal(rObj.getString("Calculated_Final_Signature__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
						calculatedFinalSignature, actualValue);
				break;
			}
			//Final Announcement Date
			case "Final_Announcement_Date__c":
			{
				actualValue = noNullVal(rObj.getString("Final_Announcement_Date__c"));
				allMatches = allMatches & compareAndReport(dateType, date, finalAnnouncementDate, actualValue);
				break;
			}
			//Initiation Issues Due to DAS
			case "Initiation_Issues_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Initiation_Issues_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, 
						initiationIssuesDueToDas, actualValue);
				break;
			}
			//Initiation Concurrence Due to DAS
			case "Initiation_Concurrence_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Initiation_Concurrence_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, 
						initiationConcurrenceDueToDas, actualValue);
				break;
			}
			//Calculated Preliminary Signature
			case "Calculated_Preliminary_Signature__c":
			{
				actualValue = noNullVal(rObj.getString("Calculated_Preliminary_Signature__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
						calculatedPreliminarySignature, actualValue);
				break;
			}
			//Prelim Team Meeting Deadline
			case "Prelim_Team_Meeting_Deadline__c":
			{
				actualValue = noNullVal(rObj.getString("Prelim_Team_Meeting_Deadline__c"));
				allMatches = allMatches & compareAndReport(dateType, date, prelimTeamMeetingDeadline, 
						actualValue);
				break;
			}
			//Prelim Issues Due to DAS
			case "Prelim_Issues_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Prelim_Issues_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, prelimIssuesDueToDas,
						actualValue);
				break;
			}
			//Prelim Concurrence Due to DAS
			case "Prelim_Concurrence_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Prelim_Concurrence_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, prelimConcurrenceDueToDas,
						actualValue);
				break;
			}
			//Final Team Meeting Deadline
			case "Final_Team_Meeting_Deadline__c":
			{
				actualValue = noNullVal(rObj.getString("Final_Team_Meeting_Deadline__c"));
				allMatches = allMatches & compareAndReport(dateType, date, finalTeamMeetingDeadline,
						actualValue);
				break;
			}
			//Final Issues Due to DAS
			case "Final_Issues_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Final_Issues_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, finalIssuesDueToDas, 
						actualValue);
				break;
			}
			//Final Concurrence Due to DAS
			case "Final_Concurrence_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Final_Concurrence_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, finalConcurrenceDueToDas,
						actualValue);
				break;
			}
			default:
			{
				break;
			}
		}
		return allMatches;
	}
	
	/**
	 * This method validates changed circumstance dates
	 * @param rObj, object containing dates
	 * @param dateName, Name of the date to be tested
	 * @param dateType, type of the date (weekend, holiday...etc)
	 * @param date, Date value
	 * @exception Exception
	*/
	public static boolean validateNewSegmentChangedCircumstancesReview(JSONObject rObj,
																	String dateName,
																	String dateType,
																	String date) throws Exception
	{
		boolean allMatches = true;
		String actualValue  = "" ;
		noNullVal(rObj.getString("Final_Date_of_Anniversary_Month__c"));
		//String actualPreliminarySignature = noNullVal(rObj.getString("Actual_Preliminary_Signature__c"));
		int finalExtensionDays = readNumberFromDb(noNullVal(rObj.getString("Final_Extension_of_days__c")));
		String actualFinalSignature = noNullVal(rObj.getString("Actual_Final_Signature__c"));
		noNullVal(rObj.getString("Actual_Amended_Final_Signature__c"));
		noNullVal(rObj.getString("Will_you_Amend_the_Final__c"));
		noNullVal(rObj.getString("Calculated_Amended_Final_Signature__c"));
		String actualPrelimIssuesToDas = noNullVal(rObj.getString("Actual_Prelim_Issues_to_DAS__c"));
		String actualPrelimConcurrenceToDas = noNullVal(rObj.getString("Actual_Prelim_Concurrence_to_DAS__c"));
		String segmentOutcome = noNullVal(rObj.getString("Segment_Outcome__c"));
		String actualFinalIssuesToDas = noNullVal(rObj.getString("Actual_Final_Issues_to_DAS__c"));
		String actualFinalConcurrenceToDas = noNullVal(rObj.getString("Actual_Final_Concurrence_to_DAS__c"));
		String allPartiesInAgreementToTheOutcome = noNullVal(rObj.getString("All_parties_in_agreement_to_the_outcome__c"));
		int prelimExtensionDays = readNumberFromDb(noNullVal(rObj.getString("Prelim_Extension__c")));
		noNullVal(rObj.getString("Amend_Final_Issues_Due_to_DAS__c")); 
		noNullVal(rObj.getString("Amend_Final_Concurrence_Due_to_DAS__c"));
		noNullVal(rObj.getString("Actual_Amend_Final_Issues_to_DAS__c"));
		noNullVal(rObj.getString("Actual_Amend_Final_Concurrence_to_DAS__c"));
		String requestFiled  = noNullVal(rObj.getString("Request_Filed__c"));
		int initiationExtensionDays = readNumberFromDb(noNullVal(rObj.getString("Initiation_Extension_of_days__c")));
		String applicationAccepted = noNullVal(rObj.getString("Application_Accepted__c"));
		String actualInitiationIssuesToDas = noNullVal(rObj.getString("Actual_Initiation_Issues_to_DAS__c"));
		String actualInitiationConcurrenceToDas = noNullVal(rObj.getString("Actual_Initiation_Concurrence_to_DAS__c"));
		String isThisReviewExpedited = noNullVal(rObj.getString("Is_this_review_expedited__c"));
		String actualInitiationSignature = "";
		///////////////////////////////////////////
		String calculatedInitiationSignature =  calculateDate(45+initiationExtensionDays,
				"Calculated Initiation Signature",
				"calendar",requestFiled);
		//Calculated Final Signature
		String calculatedFinalSignature = "";
		if(isThisReviewExpedited.equalsIgnoreCase("True") && actualInitiationSignature.equals("") 
				&& !calculatedInitiationSignature.equals(""))
		{
			calculatedFinalSignature  = calculateDate(45 + finalExtensionDays, 
					"Calculated Final Signature", "calendar", 
					calculatedInitiationSignature);
		}
		else if(isThisReviewExpedited.equalsIgnoreCase("True") && !actualInitiationSignature.equals("") )
		{
			calculatedFinalSignature  = calculateDate(45 + finalExtensionDays, 
					"Calculated Final Signature", 
					"calendar",actualInitiationSignature);
		}
		else if(!isThisReviewExpedited.equalsIgnoreCase("True") && allPartiesInAgreementToTheOutcome.equalsIgnoreCase("True") 
				&& actualInitiationSignature.equals("") && !calculatedInitiationSignature.equals(""))
		{
			calculatedFinalSignature  = calculateDate(45 + finalExtensionDays, "Calculated Final Signature", "calendar",
					calculatedInitiationSignature);
		}
		else if(!isThisReviewExpedited.equalsIgnoreCase("True") && allPartiesInAgreementToTheOutcome.equalsIgnoreCase("True") 
				&& !actualInitiationSignature.equals(""))
		{
			calculatedFinalSignature  = calculateDate(45 + finalExtensionDays, "Calculated Final Signature", "calendar",
					actualInitiationSignature);
		}
		else if(!isThisReviewExpedited.equalsIgnoreCase("True") && !allPartiesInAgreementToTheOutcome.equalsIgnoreCase("True") 
				&& actualInitiationSignature.equals("") && !calculatedInitiationSignature.equals(""))
		{
			calculatedFinalSignature  = calculateDate(270 + finalExtensionDays, "Calculated Final Signature",
					"calendar",calculatedInitiationSignature);
		}
		else if(!isThisReviewExpedited.equalsIgnoreCase("True") && !allPartiesInAgreementToTheOutcome.equalsIgnoreCase("True")
				&& !actualInitiationSignature.equals(""))
		{
			calculatedFinalSignature  = calculateDate(270 + finalExtensionDays, "Calculated Final Signature", 
					"calendar",actualInitiationSignature);
		}
		String finalAnnouncementDate = calculateDate(1, "Final Announcement Date", "calendar", 
				!actualFinalSignature.equals("")?actualFinalSignature:calculatedFinalSignature);
		String initiationIssuesDueToDas = "";
		initiationIssuesDueToDas = calculateDate(-10, "Initiation Issues Due to DAS", "business", 
				calculatedInitiationSignature);
		String initiationConcurrenceDueToDas = "";
		initiationConcurrenceDueToDas = calculateDate(-5, "Initiation Concurrence Due to DAS", 
				"business", calculatedInitiationSignature);
		//Calculated Preliminary Signature
		String calculatedPreliminarySignature = calculateDate(180 + prelimExtensionDays,
						"Calculated Preliminary Signature", "calendar", 
						!actualInitiationSignature.equals("")?actualInitiationSignature:calculatedInitiationSignature);
		String prelimTeamMeetingDeadline = calculateDate(-21, "Prelim Team Meeting Deadline", 
				"calendar", calculatedPreliminarySignature);
		String prelimIssuesDueToDas = calculateDate(-10, "Prelim Issues Due to DAS", 
				"business", calculatedPreliminarySignature);
		String prelimConcurrenceDueToDas = calculateDate(-5, "Prelim Concurrence Due to DAS",
				"business", calculatedPreliminarySignature);
		String finalTeamMeetingDeadline = calculateDate(-21, "Final Team Meeting Deadline", 
				"calendar", calculatedFinalSignature);
		String finalIssuesDueToDas = calculateDate(-10, "Final Issues Due to DAS",  
				"business", calculatedFinalSignature);
		String finalConcurrenceDueToDas = calculateDate(-5, "Final Concurrence Due to DAS", 
				"business", calculatedFinalSignature);
		
		switch (dateName)
		{
			//Calculated Initiation Signature
			case "Calculated_Initiation_Signature__c":
			{
				actualValue = noNullVal(rObj.getString("Calculated_Initiation_Signature__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
						calculatedInitiationSignature, actualValue);
				break;
			}
			//Calculated Final Signature
			case "Calculated_Final_Signature__c":
			{
				actualValue = noNullVal(rObj.getString("Calculated_Final_Signature__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
						calculatedFinalSignature, actualValue);
				break;
			}
			//Final Announcement Date
			case "Final_Announcement_Date__c":
			{
				actualValue = noNullVal(rObj.getString("Final_Announcement_Date__c"));
				allMatches = allMatches & compareAndReport(dateType, date, finalAnnouncementDate, actualValue);
				break;
			}
			//Initiation Issues Due to DAS
			case "Initiation_Issues_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Initiation_Issues_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, 
						initiationIssuesDueToDas, actualValue);
				break;
			}
			//Initiation Concurrence Due to DAS
			case "Initiation_Concurrence_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Initiation_Concurrence_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, 
						initiationConcurrenceDueToDas, actualValue);
				break;
			}
			//Calculated Preliminary Signature
			case "Calculated_Preliminary_Signature__c":
			{
				actualValue = noNullVal(rObj.getString("Calculated_Preliminary_Signature__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
						calculatedPreliminarySignature, actualValue);
				break;
			}
			//Prelim Team Meeting Deadline
			case "Prelim_Team_Meeting_Deadline__c":
			{
				actualValue = noNullVal(rObj.getString("Prelim_Team_Meeting_Deadline__c"));
				allMatches = allMatches & compareAndReport(dateType, date, prelimTeamMeetingDeadline, 
						actualValue);
				break;
			}
			//Prelim Issues Due to DAS
			case "Prelim_Issues_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Prelim_Issues_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, prelimIssuesDueToDas,
						actualValue);
				break;
			}
			//Prelim Concurrence Due to DAS
			case "Prelim_Concurrence_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Prelim_Concurrence_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, prelimConcurrenceDueToDas,
						actualValue);
				break;
			}
			//Final Team Meeting Deadline
			case "Final_Team_Meeting_Deadline__c":
			{
				actualValue = noNullVal(rObj.getString("Final_Team_Meeting_Deadline__c"));
				allMatches = allMatches & compareAndReport(dateType, date, finalTeamMeetingDeadline,
						actualValue);
				break;
			}
			//Final Issues Due to DAS
			case "Final_Issues_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Final_Issues_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, finalIssuesDueToDas, 
						actualValue);
				break;
			}
			//Final Concurrence Due to DAS
			case "Final_Concurrence_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Final_Concurrence_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, finalConcurrenceDueToDas,
						actualValue);
				break;
			}
			
			default:
			{
				break;
			}
		}
		return allMatches;
	}
	
	/**
	 * This method validates new shipper review dates
	 * @param rObj, object containing dates
	 * @param dateName, Name of the date to be tested
	 * @param dateType, type of the date (weekend, holiday...etc)
	 * @param date, Date value
	 * @param litigationType, type of litigation
	 * @exception Exception
	*/
	public static boolean validateNewSegmentShipperReview(JSONObject rObj,
															String dateName,
															String dateType,
															String date) throws Exception
	{
		boolean allMatches = true;
		String actualValue  = "" ;
		readNumberFromDb(noNullVal(rObj.getString("Prelim_Extension__c")));
		noNullVal(rObj.getString("Final_Date_of_Anniversary_Month__c"));
		String actualPreliminarySignature = noNullVal(rObj.getString("Actual_Preliminary_Signature__c"));
		int finalExtensionDays = readNumberFromDb(noNullVal(rObj.getString("Final_Extension_of_days__c")));
		noNullVal(rObj.getString("Actual_Amended_Final_Signature__c"));
		noNullVal(rObj.getString("Will_you_Amend_the_Final__c"));
		String calculatedInitiationSignature = noNullVal(rObj.getString("Calculated_Initiation_Signature__c"));
		String actualInitiationSignature = noNullVal(rObj.getString("Actual_Initiation_Signature__c"));
		///////////////////////////////////////////
		//Calculated Preliminary Signature
		String calculatedPreliminarySignature = calculateDate(180, "Calculated Preliminary Signature", "calendar", 
		!actualInitiationSignature.equals("")?actualInitiationSignature:calculatedInitiationSignature);
		//Prelim Team Meeting Deadline
		String prelimTeamMeetingDeadline = calculateDate(-21, "Prelim Team Meeting Deadline", "calendar", 
		calculatedPreliminarySignature);
		//Prelim Issues Due to DAS
		String prelimIssuesDueToDas = calculateDate(-10, "Prelim Issues Due to DAS", "business", 
		calculatedPreliminarySignature);
		//Prelim Concurrence Due to DAS
		String prelimConcurrenceDueToDas = calculateDate(-5, "Prelim Concurrence Due to DAS", 
		"business", 
		calculatedPreliminarySignature);
		//Preliminary Announcement Date
		String preliminaryAnnouncementDate = calculateDate(1, "Preliminary Announcement Date", 
		"business", 
		!actualPreliminarySignature.equals("")?actualPreliminarySignature:calculatedPreliminarySignature);
		//Calculated Final Signature
		String calculatedFinalSignature = calculateDate(90 + finalExtensionDays, 
		"Calculated Final Signature", "Calendar",
		!actualPreliminarySignature.equals("")?actualPreliminarySignature:calculatedPreliminarySignature);
		//Final Team Meeting Deadline
		String finalTeamMeetingDeadline = calculateDate(-21, "Final Team Meeting Deadline", "calendar", 
		calculatedFinalSignature);
		//Final Issues Due to DAS
		String finalIssuesDueToDas = calculateDate(-10, "Final Issues Due to DAS",  
		"business", calculatedFinalSignature);
		//Final Concurrence Due to DAS
		String finalConcurrenceDueToDas = calculateDate(-5, "Final Concurrence Due to DAS", 
		"business", calculatedFinalSignature);
		//Final Announcement Date
		String finalAnnouncementDate = calculateDate(1, "Final Announcement Date", 
		"business", calculatedFinalSignature);
		//Calculated Amended Final Signature
		String calculatedAmendedFinalSignature = calculateDate(37, "Calculated Amended Final Signature", 
		"calendar", calculatedFinalSignature);
		//Amend Final Issues Due to DAS
		String amendFinalIssuesDueToDas = calculateDate(-10, "Amend Final Issues Due to DAS", 
		"business", calculatedAmendedFinalSignature);
		//Amend Final Concurrence Due to DAS
		String amendFinalConcurrenceDueToDas = calculateDate(-5, "Amend Final Concurrence Due to DAS",
		"business", calculatedAmendedFinalSignature);
		//Amended Final Announcement Date
		String amendedFinalAnnouncementDate = calculateDate(1, "Amended Final Announcement Date",
		"business", calculatedAmendedFinalSignature);
		switch (dateName)
		{
			case "Calculated_Amended_Final_Signature__c":
			{
				actualValue = noNullVal(rObj.getString("Calculated_Amended_Final_Signature__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
				calculatedAmendedFinalSignature, actualValue);
				break;
			}
			//Amended_Final_Announcement_Date__c
			case "Amend_Final_Issues_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Amend_Final_Issues_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
				amendFinalIssuesDueToDas, actualValue);
				break;
			}
			case "Amend_Final_Concurrence_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Amend_Final_Concurrence_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
				amendFinalConcurrenceDueToDas, actualValue);
				break;
			}
			case "Amended_Final_Announcement_Date__c":
			{
				actualValue = noNullVal(rObj.getString("Amended_Final_Announcement_Date__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
				amendedFinalAnnouncementDate, actualValue);
				break;
			}
			//Preliminary Announcement Date
			case "Preliminary_Announcement_Date__c":
			{
				actualValue = noNullVal(rObj.getString("Preliminary_Announcement_Date__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
				preliminaryAnnouncementDate, actualValue);
				break;
			}
			//Calculated Initiation Signature
			case "Calculated_Initiation_Signature__c":
			{
				actualValue = noNullVal(rObj.getString("Calculated_Initiation_Signature__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
				calculatedInitiationSignature, actualValue);
				break;
			}
			//Calculated Final Signature
			case "Calculated_Final_Signature__c":
			{
				actualValue = noNullVal(rObj.getString("Calculated_Final_Signature__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
				calculatedFinalSignature, actualValue);
				break;
			}
			//Final Announcement Date
			case "Final_Announcement_Date__c":
			{
				actualValue = noNullVal(rObj.getString("Final_Announcement_Date__c"));
				allMatches = allMatches & compareAndReport(dateType, date, finalAnnouncementDate, actualValue);
				break;
			}
			//Calculated Preliminary Signature
			case "Calculated_Preliminary_Signature__c":
			{
				actualValue = noNullVal(rObj.getString("Calculated_Preliminary_Signature__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
				calculatedPreliminarySignature, actualValue);
				break;
			}
			//Prelim Team Meeting Deadline
			case "Prelim_Team_Meeting_Deadline__c":
			{
				actualValue = noNullVal(rObj.getString("Prelim_Team_Meeting_Deadline__c"));
				allMatches = allMatches & compareAndReport(dateType, date, prelimTeamMeetingDeadline, 
				actualValue);
				break;
			}
			//Prelim Issues Due to DAS
			case "Prelim_Issues_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Prelim_Issues_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, prelimIssuesDueToDas,
				actualValue);
				break;
			}
			//Prelim Concurrence Due to DAS
			case "Prelim_Concurrence_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Prelim_Concurrence_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, prelimConcurrenceDueToDas,
				actualValue);
				break;
			}
			//Final Team Meeting Deadline
			case "Final_Team_Meeting_Deadline__c":
			{
				actualValue = noNullVal(rObj.getString("Final_Team_Meeting_Deadline__c"));
				allMatches = allMatches & compareAndReport(dateType, date, finalTeamMeetingDeadline,
				actualValue);
				break;
			}
			//Final Issues Due to DAS
			case "Final_Issues_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Final_Issues_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, finalIssuesDueToDas, 
				actualValue);
				break;
			}
			//Final Concurrence Due to DAS
			case "Final_Concurrence_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Final_Concurrence_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, finalConcurrenceDueToDas,
				actualValue);
				break;
			}
			default:
			{
				break;
			}
		}
		return allMatches;
	}
	/**
	 * This method validates Scope Inquiry dates
	 * @param rObj, object containing dates
	 * @param dateName, Name of the date to be tested
	 * @param dateType, type of the date (weekend, holiday...etc)
	 * @param date, Date value
	 * @exception Exception
	*/
	public static boolean validateNewSegmentScopeInquiry(JSONObject rObj,
														 String dateName,
														 String dateType,
														 String date) throws Exception
	{
		boolean allMatches = true;
		String actualValue  = "" ;
		readNumberFromDb(noNullVal(rObj.getString("Prelim_Extension__c")));
		noNullVal(rObj.getString("Final_Date_of_Anniversary_Month__c"));
		int finalExtensionDays = readNumberFromDb(noNullVal(rObj.getString("Final_Extension_of_days__c")));
		noNullVal(rObj.getString("Actual_Amended_Final_Signature__c"));
		noNullVal(rObj.getString("Will_you_Amend_the_Final__c"));
		noNullVal(rObj.getString("Calculated_Amended_Final_Signature__c"));
		noNullVal(rObj.getString("Amend_Final_Issues_Due_to_DAS__c")); 
		noNullVal(rObj.getString("Amend_Final_Concurrence_Due_to_DAS__c"));
		noNullVal(rObj.getString("Actual_Amend_Final_Issues_to_DAS__c"));
		noNullVal(rObj.getString("Actual_Amend_Final_Concurrence_to_DAS__c"));
		readNumberFromDb(noNullVal(rObj.getString("Initiation_Extension_of_days__c")));
		noNullVal(rObj.getString("Application_Accepted__c"));
		noNullVal(rObj.getString("Actual_Initiation_Issues_to_DAS__c"));
		noNullVal(rObj.getString("Actual_Initiation_Concurrence_to_DAS__c"));
		noNullVal(rObj.getString("Request_Filed__c"));
		noNullVal(rObj.getString("Is_this_review_expedited__c"));
		noNullVal(rObj.getString("All_parties_in_agreement_to_the_outcome__c"));
		String decisionOnHowToProceed = noNullVal(rObj.getString("Decision_on_How_to_Proceed__c"));
		String actualDateOfDecisionOnHowToProceed = noNullVal(rObj.getString("Actual_Date_of_Decision_on_HoP__c"));
		String requestFiled = noNullVal(rObj.getString("Request_Filed__c"));
		////////////////////////////////////////////////////////////
		//Calculated Preliminary Signature
		String calculatedPreliminarySignature = "";
		if(decisionOnHowToProceed.equalsIgnoreCase("Formal") && !actualDateOfDecisionOnHowToProceed.equals("") )
		{
			calculatedPreliminarySignature = calculateDate(75, "Calculated Preliminary Signature", "calendar", 
					actualDateOfDecisionOnHowToProceed);
		}
		//Prelim Team Meeting Deadline
		String prelimTeamMeetingDeadline = "";
		if(decisionOnHowToProceed.equalsIgnoreCase("Formal"))
		{
			prelimTeamMeetingDeadline = calculateDate(-21, "Prelim Team Meeting Deadline", "calendar",
					calculatedPreliminarySignature);
		}
		//Prelim Issues Due to DAS
		String prelimIssuesDueToDas = calculateDate(-10, "Prelim Issues Due to DAS", "business", 
				calculatedPreliminarySignature);
		//Prelim Concurrence Due to DAS
		String prelimConcurrenceDueToDas = calculateDate(-5, "Prelim Concurrence Due to DAS", "business", 
				calculatedPreliminarySignature);
		//Calculated Final Signature
		String calculatedFinalSignature = "";
		if(decisionOnHowToProceed.equalsIgnoreCase("Formal"))
		{
			calculatedFinalSignature = calculateDate(120 + finalExtensionDays, "Calculated Final Signature", 
					"Calendar", actualDateOfDecisionOnHowToProceed);
		}
		//Final Team Meeting Deadline
		String finalTeamMeetingDeadline = "";
		if(decisionOnHowToProceed.equalsIgnoreCase("Formal"))
		{
			finalTeamMeetingDeadline = calculateDate(-21, "Final Team Meeting Deadline", 
					"calendar", calculatedFinalSignature);
		}
		//Final Issues Due to DAS
		String finalIssuesDueToDas = "";
		if(decisionOnHowToProceed.equalsIgnoreCase("Formal"))
		{
			finalIssuesDueToDas = calculateDate(-10, "Final Issues Due to DAS", 
					"business", calculatedFinalSignature);
		}
		//Final Concurrence Due to DAS
		String finalConcurrenceDueToDas = "";
		if(decisionOnHowToProceed.equalsIgnoreCase("Formal"))
		{
			finalConcurrenceDueToDas = calculateDate(-5, "Final Concurrence Due to DAS",  
					"business", calculatedFinalSignature);
		}
		//Deadline for Decision on How to Proceed
		String deadlineForDecisionOnHowToProceed  = calculateDate(45, "Deadline for Decision on How to Proceed",
		"calendar", requestFiled);
		//Decision on HOP Issues Due to DAS
		String decisionOnHopIssuesDueToDas  = calculateDate(-10, "Decision on HOP Issues Due to DAS",
				"business", deadlineForDecisionOnHowToProceed);
		//Decision on HOP Concurrence Due to DAS
		String decisionOnHopConcurrenceDueToDas  = calculateDate(-5, "Decision on HOP Concurrence Due to DAS",
				"business", deadlineForDecisionOnHowToProceed);
		switch (dateName)
		{
			//Deadline for Decision on How to Proceed
			case "Deadline_for_Decision_on_How_to_Proceed__c":
			{
				actualValue = noNullVal(rObj.getString("Deadline_for_Decision_on_How_to_Proceed__c"));
				allMatches = allMatches & compareAndReport(dateType, date, deadlineForDecisionOnHowToProceed, actualValue);
				break;
			}
			//Decision on HOP Concurrence Due to DAS
			case "Decision_on_HOP_Concurrence_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Decision_on_HOP_Concurrence_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, decisionOnHopConcurrenceDueToDas, actualValue);
				break;
			}
			//Decision on HOP Issues Due to DAS
			case "Decision_on_HOP_Issues_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Decision_on_HOP_Issues_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, decisionOnHopIssuesDueToDas, actualValue);
				break;
			}
			//Calculated Initiation Signature
			case "Calculated_Initiation_Signature__c":
			{
				actualValue = noNullVal(rObj.getString("Calculated_Initiation_Signature__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
				calculatedInitiationSignature, actualValue);
				break;
			}
			//Calculated Final Signature
			case "Calculated_Final_Signature__c":
			{
				actualValue = noNullVal(rObj.getString("Calculated_Final_Signature__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
				calculatedFinalSignature, actualValue);
				break;
			}
			
			//Calculated Preliminary Signature
			case "Calculated_Preliminary_Signature__c":
			{
				actualValue = noNullVal(rObj.getString("Calculated_Preliminary_Signature__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
				calculatedPreliminarySignature, actualValue);
				break;
			}
			//Prelim Team Meeting Deadline
			case "Prelim_Team_Meeting_Deadline__c":
			{
				actualValue = noNullVal(rObj.getString("Prelim_Team_Meeting_Deadline__c"));
				allMatches = allMatches & compareAndReport(dateType, date, prelimTeamMeetingDeadline, 
				actualValue);
				break;
			}
			//Prelim Issues Due to DAS
			case "Prelim_Issues_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Prelim_Issues_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, prelimIssuesDueToDas,
				actualValue);
				break;
			}
			//Prelim Concurrence Due to DAS
			case "Prelim_Concurrence_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Prelim_Concurrence_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, prelimConcurrenceDueToDas,
				actualValue);
				break;
			}
			//Final Team Meeting Deadline
			case "Final_Team_Meeting_Deadline__c":
			{
				actualValue = noNullVal(rObj.getString("Final_Team_Meeting_Deadline__c"));
				allMatches = allMatches & compareAndReport(dateType, date, finalTeamMeetingDeadline,
				actualValue);
				break;
			}
			//Final Issues Due to DAS
			case "Final_Issues_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Final_Issues_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, finalIssuesDueToDas, 
				actualValue);
				break;
			}
			//Final Concurrence Due to DAS
			case "Final_Concurrence_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Final_Concurrence_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, finalConcurrenceDueToDas,
				actualValue);
				break;
			}
			
			default:
			{
				break;
			}
		}
		return allMatches;
	}
	/**
	 * This method validates Expedited review dates
	 * @param rObj, object containing dates
	 * @param dateName, Name of the date to be tested
	 * @param dateType, type of the date (weekend, holiday...etc)
	 * @param date, Date value
	 * @exception Exception
	*/
	public static boolean validateNewSegmentExpiditedReview(JSONObject rObj,
															String dateName,
															String dateType,
															String date) throws Exception
	{
		boolean allMatches = true;
		String actualValue  = "" ;
		readNumberFromDb(noNullVal(rObj.getString("Prelim_Extension__c")));
		noNullVal(rObj.getString("Final_Date_of_Anniversary_Month__c"));
		String actualPreliminarySignature = noNullVal(rObj.getString("Actual_Preliminary_Signature__c"));
		int finalExtensionDays = readNumberFromDb(noNullVal(rObj.getString("Final_Extension_of_days__c")));
		noNullVal(rObj.getString("Actual_Amended_Final_Signature__c"));
		noNullVal(rObj.getString("Will_you_Amend_the_Final__c"));
		String calculatedInitiationSignature = noNullVal(rObj.getString("Calculated_Initiation_Signature__c"));
		String actualInitiationSignature = noNullVal(rObj.getString("Actual_Initiation_Signature__c"));
		///////////////////////////////////////////
		//Calculated Preliminary Signature
		String calculatedPreliminarySignature = calculateDate(180, "Calculated Preliminary Signature", "calendar", 
				!actualInitiationSignature.equals("")?actualInitiationSignature:calculatedInitiationSignature);
		//Prelim Team Meeting Deadline
		String prelimTeamMeetingDeadline = calculateDate(-21, "Prelim Team Meeting Deadline", "calendar", 
				calculatedPreliminarySignature);
		//Prelim Issues Due to DAS
		String prelimIssuesDueToDas = calculateDate(-10, "Prelim Issues Due to DAS", "business", 
				calculatedPreliminarySignature);
		//Prelim Concurrence Due to DAS
		String prelimConcurrenceDueToDas = calculateDate(-5, "Prelim Concurrence Due to DAS", 
				"business", 
				calculatedPreliminarySignature);
		//Preliminary Announcement Date
		String preliminaryAnnouncementDate = calculateDate(1, "Preliminary Announcement Date", 
				"business", 
				!actualPreliminarySignature.equals("")?actualPreliminarySignature:calculatedPreliminarySignature);
		//Calculated Final Signature
		String calculatedFinalSignature = calculateDate(90 + finalExtensionDays, 
				"Calculated Final Signature", "Calendar",
				!actualPreliminarySignature.equals("")?actualPreliminarySignature:calculatedPreliminarySignature);
		//Final Team Meeting Deadline
		String finalTeamMeetingDeadline = calculateDate(-21, "Final Team Meeting Deadline", "calendar", 
				calculatedFinalSignature);
		//Final Issues Due to DAS
		String finalIssuesDueToDas = calculateDate(-10, "Final Issues Due to DAS",  
				"business", calculatedFinalSignature);
		//Final Concurrence Due to DAS
		String finalConcurrenceDueToDas = calculateDate(-5, "Final Concurrence Due to DAS", 
				"business", calculatedFinalSignature);
		//Final Announcement Date
		String finalAnnouncementDate = calculateDate(1, "Final Announcement Date", 
				"business", calculatedFinalSignature);
		//Calculated Amended Final Signature
		String calculatedAmendedFinalSignature = calculateDate(37, "Calculated Amended Final Signature", 
				"calendar", calculatedFinalSignature);
		//Amend Final Issues Due to DAS
		String amendFinalIssuesDueToDas = calculateDate(-10, "Amend Final Issues Due to DAS", 
				"business", calculatedAmendedFinalSignature);
		//Amend Final Concurrence Due to DAS
		String amendFinalConcurrenceDueToDas = calculateDate(-5, "Amend Final Concurrence Due to DAS",
				"business", calculatedAmendedFinalSignature);
		//Amended Final Announcement Date
		String amendedFinalAnnouncementDate = calculateDate(1, "Amended Final Announcement Date",
				"business", calculatedAmendedFinalSignature);
		switch (dateName)
		{
		
			case "Calculated_Amended_Final_Signature__c":
			{
				actualValue = noNullVal(rObj.getString("Calculated_Amended_Final_Signature__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
						calculatedAmendedFinalSignature, actualValue);
				break;
			}
			
			//Amended_Final_Announcement_Date__c
			case "Amend_Final_Issues_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Amend_Final_Issues_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
						amendFinalIssuesDueToDas, actualValue);
				break;
			}
			
			
			case "Amend_Final_Concurrence_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Amend_Final_Concurrence_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
						amendFinalConcurrenceDueToDas, actualValue);
				break;
			}
			case "Amended_Final_Announcement_Date__c":
			{
				actualValue = noNullVal(rObj.getString("Amended_Final_Announcement_Date__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
						amendedFinalAnnouncementDate, actualValue);
				break;
			}
			//Preliminary Announcement Date
			case "Preliminary_Announcement_Date__c":
			{
				actualValue = noNullVal(rObj.getString("Preliminary_Announcement_Date__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
						preliminaryAnnouncementDate, actualValue);
				break;
			}
			//Calculated Initiation Signature
			case "Calculated_Initiation_Signature__c":
			{
				actualValue = noNullVal(rObj.getString("Calculated_Initiation_Signature__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
						calculatedInitiationSignature, actualValue);
				break;
			}
			//Calculated Final Signature
			case "Calculated_Final_Signature__c":
			{
				actualValue = noNullVal(rObj.getString("Calculated_Final_Signature__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
						calculatedFinalSignature, actualValue);
				break;
			}
			//Final Announcement Date
			case "Final_Announcement_Date__c":
			{
				actualValue = noNullVal(rObj.getString("Final_Announcement_Date__c"));
				allMatches = allMatches & compareAndReport(dateType, date, finalAnnouncementDate, actualValue);
				break;
			}
			//Calculated Preliminary Signature
			case "Calculated_Preliminary_Signature__c":
			{
				actualValue = noNullVal(rObj.getString("Calculated_Preliminary_Signature__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
						calculatedPreliminarySignature, actualValue);
				break;
			}
			//Prelim Team Meeting Deadline
			case "Prelim_Team_Meeting_Deadline__c":
			{
				actualValue = noNullVal(rObj.getString("Prelim_Team_Meeting_Deadline__c"));
				allMatches = allMatches & compareAndReport(dateType, date, prelimTeamMeetingDeadline, 
						actualValue);
				break;
			}
			//Prelim Issues Due to DAS
			case "Prelim_Issues_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Prelim_Issues_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, prelimIssuesDueToDas,
						actualValue);
				break;
			}
			//Prelim Concurrence Due to DAS
			case "Prelim_Concurrence_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Prelim_Concurrence_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, prelimConcurrenceDueToDas,
						actualValue);
				break;
			}
			//Final Team Meeting Deadline
			case "Final_Team_Meeting_Deadline__c":
			{
				actualValue = noNullVal(rObj.getString("Final_Team_Meeting_Deadline__c"));
				allMatches = allMatches & compareAndReport(dateType, date, finalTeamMeetingDeadline,
						actualValue);
				break;
			}
			//Final Issues Due to DAS
			case "Final_Issues_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Final_Issues_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, finalIssuesDueToDas, 
						actualValue);
				break;
			}
			//Final Concurrence Due to DAS
			case "Final_Concurrence_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Final_Concurrence_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, finalConcurrenceDueToDas,
						actualValue);
				break;
			}
			
			default:
			{
				break;
			}
		}
		return allMatches;
	}
	/**
	 * This method validates Litigation dates
	 * @param rObj, object containing dates
	 * @param dateName, Name of the date to be tested
	 * @param dateType, type of the date (weekend, holiday...etc)
	 * @param date, Date value
	 * @param sunSetType, type sunset segment
	 * @exception Exception
	*/
	public static boolean validateSunSetReviewDatesByTypeJSONObject (JSONObject rObj,
															  String dateName,
															  String dateType,
															  String date, 
															  String sunSetType) throws Exception
	{
		boolean allMatches = true;
		String actualValue = "";
		String publishedDateFinal = ""+"";
		String actualFinalSignature = "";
		//Notice of Intent to Participate
		String noticeOfIntentToParticipate = calculateDate(15, 
				"Notice of Intent to Participate", "calendar", date);
		//Notify Cluster Coordinator No Interest
		String notifyClusterCoordinatorNoInterest = calculateDate(1, 
				"Notify Cluster Coordinator No Interest", "calendar",
				noticeOfIntentToParticipate);
		//Substantive responses Due For All Parties
		String substantiveResponsesDueForAllParties = calculateDate(30, 
				"Substantive Response Due For All Parties", 
				"calendar", date);
		//Inform Cluster Coordinator if No Response
		String informClusterCoordinatorIfNoRespons = calculateDate(1, 
				"Inform Cluster Coordinator if No Respons", "calendar",
				substantiveResponsesDueForAllParties);
		//Inadequate Domestic Response note to ITC
		if(sunSetType.endsWith("90 Day"))
		{
			String inadequateDomesticResponseNoteToITC = calculateDate(40, 
					"Inadequate Domestic Response note to ITC", "calendar",
					date);
		}
		//Calculated Final Signature
		int cfsDays;
		if(sunSetType.endsWith("90 Day")) cfsDays = 90; 
		else if(sunSetType.endsWith("120 Day")) cfsDays = 120;
		else cfsDays = 240;
		String calculatedFinalSignature = calculateDate(cfsDays, 
				"Calculated Final Signature", "calendar",
				date);
		String calculatedFinalFRSignature =  calculateDate(7, 
				"Calculated Final FR Signature", "calendar",
				calculatedFinalSignature);
		if(sunSetType.endsWith("90 Day"))
		{
			//Final Announcement Date
			String finalAnnouncementDate = calculateDate(1, "Final Announcement Date", "business",
					!actualFinalSignature.equals("")?actualFinalSignature:calculatedFinalSignature);
			//Update ACE (Customs Module)
			String UpdateAceCustomsModule="";
			if(actualFinalSignature.equals("") && publishedDateFinal.equals("") && 
					!calculatedFinalSignature.equals(""))
			{
				UpdateAceCustomsModule = calculateDate(6, "Update ACE (Customs Module)", 
						"calendar",	calculatedFinalSignature);
			}
			else if(actualFinalSignature.equals("") && publishedDateFinal.equals(""))
			{
				UpdateAceCustomsModule = calculateDate(-1, "Update ACE (Customs Module)", "calendar",
						actualFinalSignature);
			}
			else if(!publishedDateFinal.equals(""))
			{
				UpdateAceCustomsModule = calculateDate(-1, "Update ACE (Customs Module)", "calendar",
						publishedDateFinal);
			}
		}
		//Final Issues Due to DAS
		String finalIssuesDueToDas = calculateDate(-10, "Final Issues Due to DAS", "business",
				calculatedFinalSignature);
		//Final Concurrencec Due to DAS
		String finalConcurrenceDueToDas = calculateDate(-5, "Final Concurrence Due to DAS", "business",
				calculatedFinalSignature);
		String finalTeamMeetingDeadline = "";
		if(sunSetType.equals("240 Day"))
		{
			//Final Team Meeting Deadline
			finalTeamMeetingDeadline = calculateDate(-21, "Final Team Meeting Deadline", 
					"calendar", calculatedFinalSignature);
		}
		//Final Announcement Date
		if(sunSetType.equals("120 Day"))
		{
			String finalAnnouncementDate = calculateDate(1, "Final Announcement Date", "business",
					!actualFinalSignature.equals("")?actualFinalSignature:calculatedFinalSignature);
		}
		//Issue Liquidation/Revocation Instruction
		if(sunSetType.equals("90 Day"))
		{
			String issueLiquidationRevocationInstruction="";
			if(actualFinalSignature.equals("") && publishedDateFinal.equals("") && 
					!calculatedFinalSignature.equals(""))
			{
				issueLiquidationRevocationInstruction = calculateDate(22, 
						"Issue Liquidation/Revocation Instruction", "calendar",
						calculatedFinalSignature);
			}
			else if(actualFinalSignature.equals("") && publishedDateFinal.equals(""))
			{
				issueLiquidationRevocationInstruction = calculateDate(22, 
						"Issue Liquidation/Revocation Instruction", "calendar",
						actualFinalSignature);
			}
			else if(!publishedDateFinal.equals(""))
			{
				issueLiquidationRevocationInstruction = calculateDate(15, 
						"Issue Liquidation/Revocation Instruction", "calendar",
						publishedDateFinal);
			}
		}
		if(sunSetType.endsWith("120 Day"))
		{
			//Memorandum on Adequacy Determination
			String memorandumOnAdequacyDetermination = calculateDate(110, 
					"Memorandum on Adequacy Determination", "calendar",date);
			//Rebuttal Comments Due
			String rebuttalCommentsDue = calculateDate(35, 
					"Rebuttal Comments Due", "calendar",date);
			//Comments on Adequacy Determination Filed
			String commentsOnAdequacyDeterminationFiled = calculateDate(70, 
					"Comments on Adequacy Determination Filed", "calendar",date);
		}
		String calculatedPreliminarySignature = "";
		String prelimConcurrenceDueToDas = "";
		String prelimIssuesDueToDas  = "";
		String prelimTeamMeetingDeadline = "";
		String adequacyDeterminationLetter ="";
		String rebuttalCommentsDue="";
		if(sunSetType.endsWith("240 Day"))
		{
			//Adequacy Determination & Letter to ITC
			adequacyDeterminationLetter = calculateDate(50, 
					"Adequacy Determination & Letter to ITC", "calendar",date);
			//Calculated Preliminary Signature
			calculatedPreliminarySignature = calculateDate(110, 
					"Calculated Preliminary Signature", "calendar",date);
			//Prelim Issues Due to DAS
			prelimIssuesDueToDas = calculateDate(-10, "Prelim Issues Due to DAS", 
					"business", calculatedPreliminarySignature);
			//Prelim Concurrence Due to DAS
			prelimConcurrenceDueToDas = calculateDate(-5, "Prelim Concurrence Due to DAS", 
					"business",calculatedPreliminarySignature);
			//Prelim Team Meeting Deadline
			prelimTeamMeetingDeadline = calculateDate(-21, "Prelim Team Meeting Deadline", 
					"calendar",calculatedPreliminarySignature);
			//Rebuttal Comments Due
			rebuttalCommentsDue = calculateDate(35, "Rebuttal Comments Due", "calendar",date);
		}	
		switch (dateName)
		{
			//Calculated Final Signature 
			case "Calculated_Final_Signature__c":
			{
				actualValue = noNullVal(rObj.getString("Calculated_Final_Signature__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
						calculatedFinalSignature, actualValue);
				break;
			}
			//Notice of Intent to Participate,	, 
			case "Notice_of_Intent_to_Participate__c":
			{
				actualValue = noNullVal(rObj.getString("Notice_of_Intent_to_Participate__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
						noticeOfIntentToParticipate, actualValue);
				break;
			}
			//Notify Cluster Coordinator No Interest, , 
			case "Notify_Cluster_Coordinator_No_Interest__c":
			{
				actualValue = noNullVal(rObj.getString("Notify_Cluster_Coordinator_No_Interest__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
						notifyClusterCoordinatorNoInterest, actualValue);
				break;
			}
			//Substantive responses Due For All Parties, , 
			case "Substantive_Response_Due_For_All_Parties__c":
			{
				actualValue = noNullVal(rObj.getString("Substantive_Response_Due_For_All_Parties__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
						substantiveResponsesDueForAllParties, actualValue);
				break;
			}
			//Inform Cluster Coordinator if No Response, , 
			case "Inform_Cluster_Coordinator_if_No_Respons__c":
			{
				actualValue = noNullVal(rObj.getString("Inform_Cluster_Coordinator_if_No_Respons__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
						informClusterCoordinatorIfNoRespons, actualValue);
				break;
			}
			//Calculated Final Signature, , 
			case "Calculated_Final_FR_Signature__c":
			{
				actualValue = noNullVal(rObj.getString("Calculated_Final_FR_Signature__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
						calculatedFinalFRSignature, actualValue);
				break;
			}
			//Final Issues Due to DAS, , 
			case "Final_Issues_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Final_Issues_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
						finalIssuesDueToDas, actualValue);
				break;
			}
			//Final Concurrencec Due to DAS, , 
			case "Final_Concurrence_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Final_Concurrence_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
						finalConcurrenceDueToDas, actualValue);
				break;
			}
			//Final Team Meeting Deadline, , 
			case "Final_Team_Meeting_Deadline__c":
			{
				actualValue = noNullVal(rObj.getString("Final_Team_Meeting_Deadline__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
						finalTeamMeetingDeadline, actualValue);
				break;
			}
			//Adequacy Determination & Letter to ITC, , 
			case "Adequacy_Determination_Letter_to_ITC__c":
			{
				actualValue = noNullVal(rObj.getString("Adequacy_Determination_Letter_to_ITC__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
						adequacyDeterminationLetter, actualValue);
				break;
			}
			//Calculated Preliminary Signature, , 
			case "Calculated_Preliminary_Signature__c":
			{
				actualValue = noNullVal(rObj.getString("Calculated_Preliminary_Signature__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
						calculatedPreliminarySignature, actualValue);
				break;
			}
			//Prelim Issues Due to DAS, , 
			case "Prelim_Issues_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Prelim_Issues_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
						prelimIssuesDueToDas, actualValue);
				break;
			}
			//Prelim Concurrence Due to DAS, , 
			case "Prelim_Concurrence_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Prelim_Concurrence_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
						prelimConcurrenceDueToDas, actualValue);
				break;
			}
			//Prelim Team Meeting Deadline, , 
			case "Prelim_Team_Meeting_Deadline__c":
			{
				actualValue = noNullVal(rObj.getString("Prelim_Team_Meeting_Deadline__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
						prelimTeamMeetingDeadline, actualValue);
				break;
			}
			//Rebuttal Comments Due, , 
			case "Rebuttal_Comments_Due__c":
			{
				actualValue = noNullVal(rObj.getString("Rebuttal_Comments_Due__c"));
				allMatches = allMatches & compareAndReport(dateType, date,  
						rebuttalCommentsDue, actualValue);
				break;
			}
			default:
			{
				break;
			}
		}
		return allMatches;
	}
	
	/**
	 * This method validates Litigation fields
	 * @param rObj, object containing dates
	 * @param litigationType, type of litigation
	 * @exception Exception
	*/
	public static boolean validateLitigationFields(JSONObject rObj, 
												   String litigationType) throws Exception
	{
		boolean allMatches = true;
		String actualValue, requestFiled = "", expectedFinalSignatureBeforeExt = "";
		String calculatedFinalSignature = "", calculatedPreliminarySignature="",
			   calculatedDraftRemandreleaseToparty = "",  prelimIssuesDueToDas="",
					   prelimConcurrenceDueToDas="", prelimTeamMeetingDeadline="";
		String draftRemandIssuesDueToDas="", draftRemandConcurrenceDueToDas="",
			   finalTeamMeetingDeadline = "";
		//Prelim Extension (# of days)
		int prelimExtensionDays = readNumberFromDb(noNullVal(rObj.getString("Prelim_Extension_of_days__c")));
		//
		int finalExtensionDays = readNumberFromDb(noNullVal(rObj.getString("Final_Extension_of_days__c")));
		//Request Filed
		if(litigationType.equalsIgnoreCase("International Litigation"))
		{
			requestFiled = noNullVal(rObj.getString("Request_Filed__c"));
		}else //Calculated Draft Remand release to party
		{
			expectedFinalSignatureBeforeExt	 = noNullVal(rObj.getString("Expected_Final_Signature_Before_Ext__c"));
		}
		//International
		if(litigationType.equalsIgnoreCase("International Litigation"))
		{
			//Calculated Preliminary Signature
			calculatedPreliminarySignature = calculateLitigationDate(45 + prelimExtensionDays, "Calculated Preliminary Signature", 
					"calendar",requestFiled);
			actualValue = noNullVal(rObj.getString("Calculated_Preliminary_Signature__c"));
			allMatches = allMatches & compareAndReport("Calculated Preliminary Signature", calculatedPreliminarySignature, actualValue);
			//Prelim Issues Due to DAS
			prelimIssuesDueToDas = calculateLitigationDate(-10, "Prelim Issues Due to DAS", "business", calculatedPreliminarySignature);
			actualValue = noNullVal(rObj.getString("Prelim_Issues_Due_to_DAS__c"));
			allMatches = allMatches & compareAndReport("Prelim Issues Due to DAS", prelimIssuesDueToDas, actualValue);
			//Prelim Concurrence Due to DAS
			prelimConcurrenceDueToDas = calculateLitigationDate(-5, "Prelim Concurrence Due to DAS", "business", calculatedPreliminarySignature);
			actualValue = noNullVal(rObj.getString("Prelim_Concurrence_Due_to_DAS__c"));
			allMatches = allMatches & compareAndReport("Prelim Concurrence Due to DAS", prelimConcurrenceDueToDas, actualValue);
			//Prelim Team Meeting Deadline
			prelimTeamMeetingDeadline = calculateLitigationDate(-21, "Prelim Team Meeting Deadline", "calendar", calculatedPreliminarySignature);
			actualValue = noNullVal(rObj.getString("Prelim_Team_Meeting_Deadline__c"));
			allMatches = allMatches & compareAndReport("Prelim Team Meeting Deadline", prelimTeamMeetingDeadline, actualValue);
			
			//Calculated Final Signature
			calculatedFinalSignature = calculateLitigationDate(180 + finalExtensionDays, "Calculated Final Signature", "Calendar", 
						requestFiled.equals("")?expectedFinalSignatureBeforeExt:requestFiled);
			actualValue = noNullVal(rObj.getString("Calculated_Final_Signature__c"));
			allMatches = allMatches & compareAndReport("Calculated Final Signature", calculatedFinalSignature, actualValue);
		}
		else//Remand
		{
			//Calculated Draft Remand release to party
			calculatedDraftRemandreleaseToparty = calculateLitigationDate(-28 + prelimExtensionDays, 
					"Calculated Draft Remand release to party",	"Calendar", 
					expectedFinalSignatureBeforeExt);
			actualValue = noNullVal(rObj.getString("Calculated_Draft_Remand_release_to_party__c"));
			allMatches = allMatches & compareAndReport("Calculated Draft Remand release to party", 
					calculatedDraftRemandreleaseToparty, actualValue);
			//Draft Remand Issues Due to DAS
			draftRemandIssuesDueToDas = calculateLitigationDate(-10, "Draft Remand Issues Due to DAS", 
					"business", calculatedDraftRemandreleaseToparty);
			actualValue = noNullVal(rObj.getString("Draft_Remand_Issues_Due_to_DAS__c"));
			allMatches = allMatches & compareAndReport("Draft Remand Issues Due to DAS", 
					draftRemandIssuesDueToDas, actualValue);
			//Draft Remand Concurrence Due to DAS
			draftRemandConcurrenceDueToDas = calculateLitigationDate(-5, "Draft Remand Concurrence Due to DAS", 
					"business",calculatedDraftRemandreleaseToparty);
			actualValue = noNullVal(rObj.getString("Draft_Remand_Concurrence_Due_to_DAS__c"));
			allMatches = allMatches & compareAndReport("Draft Remand Concurrence Due to DAS", 
					draftRemandConcurrenceDueToDas, actualValue);
			//Calculated Final Signature
			calculatedFinalSignature = calculateLitigationDate(finalExtensionDays, "Calculated Final Signature", "Calendar", 
						requestFiled.equals("")?expectedFinalSignatureBeforeExt:requestFiled);
			actualValue = noNullVal(rObj.getString("Calculated_Final_Signature__c"));
			allMatches = allMatches & compareAndReport("Calculated Final Signature", 
					calculatedFinalSignature, actualValue);
		}
		//Final Issues Due to DAS
		String finalIssuesDueToDas = "";
		finalIssuesDueToDas = calculateLitigationDate(-10, "Final Issues Due to DAS",  "business", calculatedFinalSignature);
		actualValue = noNullVal(rObj.getString("Final_Issues_Due_to_DAS__c"));
		allMatches = allMatches & compareAndReport("Final Issues Due to DAS", finalIssuesDueToDas, actualValue);
		//Final Concurrence Due to DAS
		String finalConcurrenceDueToDas = "";
		finalConcurrenceDueToDas = calculateLitigationDate(-5, "Final Concurrence Due to DAS",  "business", calculatedFinalSignature);
		actualValue = noNullVal(rObj.getString("Final_Concurrence_Due_to_DAS__c"));
		allMatches = allMatches & compareAndReport("Final Concurrence Due to DAS", finalConcurrenceDueToDas, actualValue);
		if(litigationType.equalsIgnoreCase("International Litigation"))
		{
		//Final Team Meeting Deadline
		finalTeamMeetingDeadline = calculateLitigationDate(-21, "Final Team Meeting Deadline", "calendar", calculatedFinalSignature);
		actualValue = noNullVal(rObj.getString("Final_Team_Meeting_Deadline__c"));
		allMatches = allMatches & compareAndReport("Final Team Meeting Deadline", finalTeamMeetingDeadline, actualValue);
		}
		String actualPreliminarySignature = "";
		String actualFinalSignature = "";
		String nextMajorDeadline = "";
		String actualPrelimIssuesToDas="";
		String actualPrelimConcurrenceToDas="";
		//next dates
		if(litigationType.equalsIgnoreCase("International Litigation"))
		{
			//Next Major Deadline
			if("".equals(actualPreliminarySignature))
			{
				nextMajorDeadline = calculatedPreliminarySignature;
			}else if ("".equals(actualFinalSignature))
			{
				nextMajorDeadline = calculatedFinalSignature;
			}
			actualValue = noNullVal(rObj.getString("Next_Major_Deadline__c"));
			allMatches = allMatches & compareAndReport("Next Major Deadline", nextMajorDeadline, actualValue);
			//Next Due to DAS Deadline
			String nextDueToDasDeadline = "", actualFinalIssuesToDas="", actualFinalConcurrenceToDas="";
			if("".equals(actualPreliminarySignature) && "".equals(actualPrelimIssuesToDas))
			{
				nextDueToDasDeadline = prelimIssuesDueToDas;
			}else if("".equals(actualPreliminarySignature) && "".equals(actualPrelimConcurrenceToDas))
			{
				nextDueToDasDeadline = prelimConcurrenceDueToDas;
			}else if("".equals(actualPreliminarySignature))
			{
				nextDueToDasDeadline = calculatedPreliminarySignature;
			}
			else if("".equals(actualFinalSignature) && "".equals(actualFinalIssuesToDas))
			{
				nextDueToDasDeadline = finalIssuesDueToDas;
			}else if("".equals(actualFinalSignature) && "".equals(actualFinalConcurrenceToDas))
			{
				nextDueToDasDeadline = finalConcurrenceDueToDas;
			}else if("".equals(actualFinalSignature))
			{
				nextDueToDasDeadline = calculatedFinalSignature;
			}
			actualValue = noNullVal(rObj.getString("Next_Due_to_DAS_Deadline__c"));
			allMatches = allMatches & compareAndReport("Next Due to DAS Deadline", nextDueToDasDeadline, actualValue);
			//Next Office Deadline
			String nextOfficeDeadline="";
			if ("".equals(actualPreliminarySignature) && !datePassed(prelimTeamMeetingDeadline))
			{
				nextOfficeDeadline = prelimTeamMeetingDeadline;
			}
			else if("".equals(actualPreliminarySignature) && "".equals(actualPrelimIssuesToDas))
			{
				nextOfficeDeadline = prelimIssuesDueToDas;
			}else if("".equals(actualPreliminarySignature) && "".equals(actualPrelimConcurrenceToDas))
			{
				nextOfficeDeadline = prelimConcurrenceDueToDas;
			}else if("".equals(actualPreliminarySignature))
			{
				nextOfficeDeadline = calculatedPreliminarySignature;
			}
			else if ("".equals(actualFinalSignature) && !datePassed(finalTeamMeetingDeadline))
			{
				nextOfficeDeadline = finalTeamMeetingDeadline;
			}
			else if("".equals(actualFinalSignature) && "".equals(actualFinalIssuesToDas))
			{
				nextOfficeDeadline = finalIssuesDueToDas;
			}else if("".equals(actualFinalSignature) && "".equals(actualFinalConcurrenceToDas))
			{
				nextOfficeDeadline = finalConcurrenceDueToDas;
			}else if("".equals(actualFinalSignature))
			{
				nextOfficeDeadline = calculatedFinalSignature;
			}
			actualValue = noNullVal(rObj.getString("Next_Office_Deadline__c"));
			allMatches = allMatches & compareAndReport("Next Office Deadline", nextOfficeDeadline, actualValue);
		}
		else //remand
		{
			//Next Major Deadline
			String actualDraftRemandReleasedToParty = "";
			if("".equals(actualDraftRemandReleasedToParty))
			{
				nextMajorDeadline = calculatedDraftRemandreleaseToparty;
			}else if ("".equals(actualFinalSignature))
			{
				nextMajorDeadline = calculatedFinalSignature;
			}
			actualValue = noNullVal(rObj.getString("Next_Major_Deadline__c"));
			allMatches = allMatches & compareAndReport("Next Major Deadline", nextMajorDeadline, actualValue);
			
			//Next Due to DAS Deadline
			String nextDueToDasDeadline = "", actualFinalIssuesToDas="", actualFinalConcurrenceToDas="",
					actualDraftRemandIssuesToDas = "", actualDraftRemandConcurrencceToDas="";
			if("".equals(actualDraftRemandReleasedToParty) && "".equals(actualDraftRemandIssuesToDas))
			{
				nextDueToDasDeadline = draftRemandIssuesDueToDas;
			}else if("".equals(actualDraftRemandReleasedToParty) && "".equals(actualDraftRemandConcurrencceToDas))
			{
				nextDueToDasDeadline = draftRemandConcurrenceDueToDas;
			}else if("".equals(actualDraftRemandReleasedToParty))
			{
				nextDueToDasDeadline = calculatedDraftRemandreleaseToparty;
			}
			else if("".equals(actualFinalSignature) && "".equals(actualFinalIssuesToDas))
			{
				nextDueToDasDeadline = finalIssuesDueToDas;
			}else if("".equals(actualFinalSignature) && "".equals(actualFinalConcurrenceToDas))
			{
				nextDueToDasDeadline = finalConcurrenceDueToDas;
			}else if("".equals(actualFinalSignature))
			{
				nextDueToDasDeadline = calculatedFinalSignature;
			}	
			actualValue = noNullVal(rObj.getString("Next_Due_to_DAS_Deadline__c"));
			allMatches = allMatches & compareAndReport("Next Due to DAS Deadline", nextDueToDasDeadline, actualValue);
			//Next Office Deadline
			String nextOfficeDeadline="";
			if ("".equals(actualDraftRemandReleasedToParty) && datePassed(prelimTeamMeetingDeadline))
			{
				nextOfficeDeadline = prelimTeamMeetingDeadline;
			}
			else if("".equals(actualDraftRemandReleasedToParty) && "".equals(actualDraftRemandIssuesToDas))
			{
				nextOfficeDeadline = draftRemandIssuesDueToDas;
			}else if("".equals(actualDraftRemandReleasedToParty) && "".equals(actualDraftRemandConcurrencceToDas))
			{
				nextOfficeDeadline = draftRemandConcurrenceDueToDas;
			}else if("".equals(actualDraftRemandReleasedToParty))
			{
				nextOfficeDeadline = calculatedDraftRemandreleaseToparty;
			}
			else if ("".equals(actualFinalSignature) && datePassed(finalTeamMeetingDeadline))
			{
				nextOfficeDeadline = finalTeamMeetingDeadline;
			}
			else if("".equals(actualFinalSignature) && "".equals(actualFinalIssuesToDas))
			{
				nextOfficeDeadline = finalIssuesDueToDas;
			}else if("".equals(actualFinalSignature) && "".equals(actualFinalConcurrenceToDas))
			{
				nextOfficeDeadline = finalConcurrenceDueToDas;
			}else if("".equals(actualFinalSignature))
			{
				nextOfficeDeadline = calculatedFinalSignature;
			}	
			actualValue = noNullVal(rObj.getString("Next_Office_Deadline__c"));
			allMatches = allMatches & compareAndReport("Next Office Deadline", nextOfficeDeadline, actualValue);
		}
			return allMatches;
	}
	
	/**
	 * This method validates Litigation dates
	 * @param rObj, object containing dates
	 * @param dateName, Name of the date to be tested
	 * @param dateType, type of the date (weekend, holiday...etc)
	 * @param date, Date value
	 * @param litigationType, type of litigation
	 * @exception Exception
	*/
	public static boolean validateLitigationFields(JSONObject rObj, 
												   String dateName,
												   String dateType,
												   String date,
												   String litigationType) throws Exception
	{
		boolean allMatches = true;
		String actualValue, requestFiled = "", expectedFinalSignatureBeforeExt = "";
		String calculatedFinalSignature = "", calculatedPreliminarySignature="",
			   calculatedDraftRemandreleaseToparty = "",  prelimIssuesDueToDas="",
					   prelimConcurrenceDueToDas="", prelimTeamMeetingDeadline="";
		String draftRemandIssuesDueToDas="", draftRemandConcurrenceDueToDas="",
			   finalTeamMeetingDeadline = "";
		//Prelim Extension (# of days)
		int prelimExtensionDays = readNumberFromDb(noNullVal(rObj.getString("Prelim_Extension_of_days__c")));
		//
		int finalExtensionDays = readNumberFromDb(noNullVal(rObj.getString("Final_Extension_of_days__c")));
		//Request Filed
		if(litigationType.equalsIgnoreCase("International Litigation"))
		{
			requestFiled = noNullVal(rObj.getString("Request_Filed__c"));
		}else //Calculated Draft Remand release to party
		{
			expectedFinalSignatureBeforeExt	 = noNullVal(rObj.getString("Expected_Final_Signature_Before_Ext__c"));
		}
		//International
		if(litigationType.equalsIgnoreCase("International Litigation"))
		{
			//Calculated Preliminary Signature
			calculatedPreliminarySignature = calculateLitigationDate(45 + prelimExtensionDays, "Calculated Preliminary Signature", 
					"calendar",requestFiled);
			//Prelim Issues Due to DAS
			prelimIssuesDueToDas = calculateLitigationDate(-10, "Prelim Issues Due to DAS", "business", calculatedPreliminarySignature);
			//Prelim Concurrence Due to DAS
			prelimConcurrenceDueToDas = calculateLitigationDate(-5, "Prelim Concurrence Due to DAS", "business", calculatedPreliminarySignature);
			//Prelim Team Meeting Deadline
			prelimTeamMeetingDeadline = calculateLitigationDate(-21, "Prelim Team Meeting Deadline", "calendar", calculatedPreliminarySignature);
			//Calculated Final Signature
			calculatedFinalSignature = calculateLitigationDate(180 + finalExtensionDays, "Calculated Final Signature", "Calendar", 
						requestFiled.equals("")?expectedFinalSignatureBeforeExt:requestFiled);
		}
		else//Remand
		{
			//Calculated Draft Remand release to party
			calculatedDraftRemandreleaseToparty = calculateLitigationDate(-28 + prelimExtensionDays, 
					"Calculated Draft Remand release to party",	"Calendar", 
					expectedFinalSignatureBeforeExt);
			//Draft Remand Issues Due to DAS
			draftRemandIssuesDueToDas = calculateLitigationDate(-10, "Draft Remand Issues Due to DAS", 
					"business", calculatedDraftRemandreleaseToparty);
			//Draft Remand Concurrence Due to DAS
			draftRemandConcurrenceDueToDas = calculateLitigationDate(-5, "Draft Remand Concurrence Due to DAS", 
					"business",calculatedDraftRemandreleaseToparty);
			//Calculated Final Signature
			calculatedFinalSignature = calculateLitigationDate(finalExtensionDays, "Calculated Final Signature", "Calendar", 
						requestFiled.equals("")?expectedFinalSignatureBeforeExt:requestFiled);
		}
		//Final Issues Due to DAS
		String finalIssuesDueToDas = "";
		finalIssuesDueToDas = calculateLitigationDate(-10, "Final Issues Due to DAS",  "business", calculatedFinalSignature);
		//Final Concurrence Due to DAS
		String finalConcurrenceDueToDas = "";
		finalConcurrenceDueToDas = calculateLitigationDate(-5, "Final Concurrence Due to DAS",  "business", calculatedFinalSignature);
		if(litigationType.equalsIgnoreCase("International Litigation"))
		{
			//Final Team Meeting Deadline
			finalTeamMeetingDeadline = calculateLitigationDate(-21, "Final Team Meeting Deadline", "calendar", calculatedFinalSignature);
		}
		switch (dateName)
		{
			case "Calculated_Preliminary_Signature__c":
			{
				actualValue = noNullVal(rObj.getString("Calculated_Preliminary_Signature__c"));
				allMatches = allMatches & compareAndReport(dateType, date, calculatedPreliminarySignature, actualValue);
				break;
			}
			//Prelim Team Meeting Deadline
			case "Prelim_Team_Meeting_Deadline__c":
			{
				actualValue = noNullVal(rObj.getString("Prelim_Team_Meeting_Deadline__c"));
				allMatches = allMatches & compareAndReport(dateType, date, prelimTeamMeetingDeadline, actualValue);
				break;
			}
			//Prelim Issues Due to DAS
			case "Prelim_Issues_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Prelim_Issues_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, prelimIssuesDueToDas, actualValue);
				break;
			}
			//Prelim Concurrence Due to DAS:
			case "Prelim_Concurrence_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Prelim_Concurrence_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, prelimConcurrenceDueToDas, actualValue);
				break;
			}
			//Calculated Final Signature
			case "Calculated_Final_Signature__c":
			{
				actualValue = noNullVal(rObj.getString("Calculated_Final_Signature__c"));
				allMatches = allMatches & compareAndReport(dateType, date, calculatedFinalSignature, actualValue);
				break;
			}
			case "Final_Team_Meeting_Deadline__c":
			{
				actualValue = noNullVal(rObj.getString("Final_Team_Meeting_Deadline__c"));
				allMatches = allMatches & compareAndReport(dateType, date, finalTeamMeetingDeadline, actualValue);
				break;
			}
			case "Final_Issues_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Final_Issues_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, finalIssuesDueToDas, actualValue);
				break;
			}
			case "Final_Concurrence_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Final_Concurrence_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, finalConcurrenceDueToDas, actualValue);
				break;
			}
			//Calculated Draft Remand release to party
			case "Calculated_Draft_Remand_release_to_party__c":
			{
				actualValue = noNullVal(rObj.getString("Calculated_Draft_Remand_release_to_party__c"));
				allMatches = allMatches & compareAndReport(dateType, date, 
						calculatedDraftRemandreleaseToparty, actualValue);
				break;
			}
			//Draft Remand Issues Due to DAS
			case "Draft_Remand_Issues_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Draft_Remand_Issues_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, 
						draftRemandIssuesDueToDas, actualValue);
				break;
			}
			//Draft Remand Concurrence Due to DAS
			case "Draft_Remand_Concurrence_Due_to_DAS__c":
			{
				actualValue = noNullVal(rObj.getString("Draft_Remand_Concurrence_Due_to_DAS__c"));
				allMatches = allMatches & compareAndReport(dateType, date, 
						draftRemandConcurrenceDueToDas, actualValue);
				break;
			}
			default:
			{
				break;
			}
		}
		return allMatches;
	}
	/**
	 * This method verifies if given date is passed or not
	 * @param date: date to check
	 * @return pass if passed, false if not
	*/
	public static boolean datePassed(String date) throws ParseException
	{
		if (date.contains("#")) date = date.split("#")[1];
		if (date == null || date.equals("")) return false;
		Date todayDate = new Date();
		Format format = new SimpleDateFormat("yyyy-MM-dd");
		//Calendar calendar = Calendar.getInstance();
		String todayDateStr = new SimpleDateFormat("yyyy-MM-dd").format(todayDate);
		if(((DateFormat) format).parse(date).compareTo(((DateFormat) format).parse(todayDateStr))<0) return true;
		else return false;
	}
	/**
	 * This method creates random case number
	 * @return case number
	*/
	public static String getCaseName()
	{
		String str1 = "00"+new Random().nextInt(999);
		String str2 = "00"+new Random().nextInt(999);
		return str1.substring(str1.length()-3)+ "-" +  str2.substring(str2.length()-3);
	}
	/**
	 * This method calculates date based on other dates
	 * @param val, dates value
	 * @param params: given dates
	 * @return calculated date
	 * @throws JSONException 
	*/
	static String calculateDate(int val, String ...params) throws ParseException, JSONException
	{
		//Date todayDate = new Date();
		int iterator, numberBusinessDays ;
		iterator = (val<0)? -1:1;
		Date date;
		numberBusinessDays = iterator * val;
		if(params[2].equals(""))
		{
			return "Empty"+"";
		}
		else if(params[2].contains("#"))
		{
			date = format.parse(params[2].split("#")[0]);
			calendar.setTime(date);
		}
		else
		{
			date = format.parse(params[2]);
			calendar.setTime(date);
		}
		String landsOn = "";
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		if(!params[1].equals("business"))
		{
			c.add(Calendar.DATE, val);
			landsOn = format.format(c.getTime());
		}else
		{
			int nbd = numberBusinessDays;
			while((nbd > 0 )) 
			{
				if(!isHoliday(c) && (!isWeekEnd(c)))
				{
					nbd--;
				}
				c.add(Calendar.DAY_OF_MONTH, iterator);
			}
			landsOn = format.format(c.getTime());
		}
		if(params[1].equals("business"))
		{
			int toll=0;
			if(iterator>0)
			{
				for(int i = 1; i <= numberBusinessDays ; i++ ) 
				{
					calendar.add(Calendar.DAY_OF_MONTH, iterator);
					if(!isBusinessDay(calendar))
					{
						i--;
					}
				}
				while(!isBusinessDay(calendar)) 
				{
					calendar.add(Calendar.DAY_OF_MONTH, iterator);
				}
			}
			else  //iteration<0
			{
				for(int i = 1; i <= numberBusinessDays ; i++ ) 
				{
					if(isTollingDay(calendar))
					{
						toll++;
						calendar.add(Calendar.DAY_OF_MONTH, iterator);
					}else if(isWeekEnd(calendar) || isHoliday(calendar))
					{
						calendar.add(Calendar.DAY_OF_MONTH, iterator);
						i--;
					} else //if (!isWeekEnd(calendar) && !isHoliday(calendar))
					{
						calendar.add(Calendar.DAY_OF_MONTH, iterator);
					}
				}
				while(toll>0)
				{
					if(isBusinessDay(calendar))
					{
						toll--;
					}
					if(params[0].contains("DAS"))
					{
						calendar.add(Calendar.DAY_OF_MONTH, iterator);
					}
					else
					{
						calendar.add(Calendar.DAY_OF_MONTH, 1);
					}
				}
				while((isTollingDay(calendar)) 
						|| (isHoliday(calendar)) || isWeekEnd(calendar)) 
				{
					if(params[0].contains("DAS"))
					{
						calendar.add(Calendar.DAY_OF_MONTH, iterator);
					}
					else
					{
						calendar.add(Calendar.DAY_OF_MONTH, 1);
					}
				}
			}
		}
		else //calendar
		{	
			int toll=0;
			if(iterator>0)
			{
				for(int i = 1; i <= numberBusinessDays ; i++ ) 
				{
					//System.out.println(i + "________" + calendar.getTime());
					calendar.add(Calendar.DAY_OF_MONTH, iterator);
					if(isTollingDay(calendar))
					{
						i--;
					}
				}
				while(!isBusinessDay(calendar)) 
				{
					if(params[0].contains("DAS"))
					{
						calendar.add(Calendar.DAY_OF_MONTH, iterator);
					}
					else
					{
						calendar.add(Calendar.DAY_OF_MONTH, 1);
					}
				}
			}
			else  //iteration<0
			{
				for(int i = 1; i <= numberBusinessDays ; i++ ) 
				{
					calendar.add(Calendar.DAY_OF_MONTH, iterator);
					if(isTollingDay(calendar))
					{
						toll++;
					}
				}
				while(toll>0)
				{
					if(params[0].contains("DAS"))
					{
						calendar.add(Calendar.DAY_OF_MONTH, iterator);
					}
					else
					{
						calendar.add(Calendar.DAY_OF_MONTH, 1);
					}
						toll--;
				}
				while(!isBusinessDay(calendar)) 
				{
					if(params[0].contains("DAS"))
					{
						calendar.add(Calendar.DAY_OF_MONTH, iterator);
					}
					else
					{
						calendar.add(Calendar.DAY_OF_MONTH, 1);
					}
				}
			}
		}
		return format.format(calendar.getTime())+"#"+landsOn;
	}
	/**
	 * This method calculates date based on other dates
	 * @param params: given dates
	 * @return calculated date
	*/
	static String calculateLitigationDate(int val, String ...params) throws ParseException
	{
		int iterator, numberBusinessDays ;
		iterator = (val<0)? -1:1;
		Date date;
		numberBusinessDays = iterator * val;
		if(params[2].equals(""))
		{
			return "Empty"+"";
		}
		else if(params[2].contains("#"))
		{
			date = format.parse(params[2].split("#")[0]);
			calendar.setTime(date);
		}
		else
		{
			date = format.parse(params[2]);
			calendar.setTime(date);
		}
		//
		String landsOn = "";
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, val);
		landsOn = format.format(c.getTime());
		//System.out.print(format.format(calendar.getTime())+"#"+landsOn);
		return landsOn+"#"+landsOn;
	}
	/**
	 * This method verify if a date is business day or not
	 * @param cal: calendar date
	 * @return true if it is a business date, false if not
	 * @throws ParseException 
	*/
	public static boolean isWeekEnd(Calendar cal) throws ParseException{
		// check if weekend
		if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || 
				cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
			return true;
		}else return false;
	}
	/**
	 * This method verify if a date is business day or not
	 * @param cal: calendar date
	 * @return true if it is a business date, false if not
	 * @throws ParseException 
	*/
	public static boolean isHoliday(Calendar cal) throws ParseException{
		// check if weekend
		/*if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || 
				cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
			return true;
		}*/
		// check if New Year's Day
		if (cal.get(Calendar.MONTH) == Calendar.JANUARY
			&& cal.get(Calendar.DAY_OF_MONTH) == 1) {
			return true;
		}
		//if New Year's Eve
		if (cal.get(Calendar.MONTH) == Calendar.DECEMBER
				&& cal.get(Calendar.DAY_OF_MONTH) == 31
				&& cal.get(Calendar.YEAR)==2021) {
				return true;
			}
		// check if Christmas
		if (cal.get(Calendar.MONTH) == Calendar.DECEMBER
			&& cal.get(Calendar.DAY_OF_MONTH) == 25) {
			return true;
		}
		// check if 4th of July
		if (cal.get(Calendar.MONTH) == Calendar.JULY
			&& cal.get(Calendar.DAY_OF_MONTH) == 4) {
			return true;
		}
		// check if 3th of July Observed
		/*if (cal.get(Calendar.MONTH) == Calendar.JULY
			&& cal.get(Calendar.DAY_OF_MONTH) == 3 
			&& cal.get(Calendar.YEAR) == 2019){
			return true;
		}*/
		// check if 3th of July Observed
		if (cal.get(Calendar.MONTH) == Calendar.JULY
			&& cal.get(Calendar.DAY_OF_MONTH) == 3 
			&& cal.get(Calendar.YEAR) == 2020){
			return true;
		}
		// check Thanksgiving (4th Thursday of November)
		if (cal.get(Calendar.MONTH) == Calendar.NOVEMBER
			&& cal.get(Calendar.DAY_OF_WEEK_IN_MONTH) == 4
			&& cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
			return true;
		}
		// check Memorial Day (last Monday of May)
		if (cal.get(Calendar.MONTH) == Calendar.MAY
			&& cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
			&& cal.get(Calendar.DAY_OF_MONTH) > (31 - 7) ) {
			return true;
		}
		// check Labor Day (1st Monday of September)
		if (cal.get(Calendar.MONTH) == Calendar.SEPTEMBER
			&& cal.get(Calendar.DAY_OF_WEEK_IN_MONTH) == 1
			&& cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
			return true;
		}
		// check President's Day (3rd Monday of February)
		if (cal.get(Calendar.MONTH) == Calendar.FEBRUARY
			&& cal.get(Calendar.DAY_OF_WEEK_IN_MONTH) == 3
			&& cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
			return true;
		}
		// check Columbus Day (2rd Monday of October)
		if (cal.get(Calendar.MONTH) == Calendar.OCTOBER
			&& cal.get(Calendar.DAY_OF_WEEK_IN_MONTH) == 2
			&& cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
			return true; 
		}
		// check Veterans Day (November 11)
			if (cal.get(Calendar.MONTH) == Calendar.NOVEMBER
			&& cal.get(Calendar.DAY_OF_MONTH) == 11) {
			return true;
		}
		// check Veterans Day (November 11) OBSERVED
		if (cal.get(Calendar.MONTH) == Calendar.NOVEMBER
				&& cal.get(Calendar.DAY_OF_MONTH) == 12 
				&& cal.get(Calendar.YEAR) == 2018){
				return true;
			}
		// check MLK Day (3rd Monday of January)
		if (cal.get(Calendar.MONTH) == Calendar.JANUARY
			&& cal.get(Calendar.DAY_OF_WEEK_IN_MONTH) == 3
			&& cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
			return true;
		}
		// IF NOTHING ELSE, IT'S A BUSINESS DAY
		return false;
	}
	/**
	 * This method verify if a date is business day or not
	 * @param cal: calendar date
	 * @return true if it is a business date, false if not
	 * @throws ParseException 
	 * @throws JSONException 
	*/
	public static boolean isBusinessDay(Calendar cal) throws ParseException, JSONException{
		// check if weekend
		if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || 
				cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
			return false;
		}
		// check if New Year's Day
		if (cal.get(Calendar.MONTH) == Calendar.JANUARY
			&& cal.get(Calendar.DAY_OF_MONTH) == 1) {
			
			return false;
		}
		//if New Year's Eve
		if (
			cal.get(Calendar.MONTH) == Calendar.DECEMBER
			&& cal.get(Calendar.DAY_OF_MONTH) == 31
			&& cal.get(Calendar.YEAR) == 2021
			) 
		{
			return false;
		}
		// check if Christmas
		if (cal.get(Calendar.MONTH) == Calendar.DECEMBER
			&& cal.get(Calendar.DAY_OF_MONTH) == 25) {
			return false;
		}
		// check if 4th of July
		if (cal.get(Calendar.MONTH) == Calendar.JULY
			&& cal.get(Calendar.DAY_OF_MONTH) == 4) {
			return false;
		}
		/*// check if 3th of July Observed
		if (cal.get(Calendar.MONTH) == Calendar.JULY
			&& cal.get(Calendar.DAY_OF_MONTH) == 3 
			&& cal.get(Calendar.YEAR) == 2019){
			return false;
		}*/
		// check if 3th of July Observed
		if (cal.get(Calendar.MONTH) == Calendar.JULY
			&& cal.get(Calendar.DAY_OF_MONTH) == 3 
			&& cal.get(Calendar.YEAR) == 2020){
			return false;
		}
		// check Thanksgiving (4th Thursday of November)
		if (cal.get(Calendar.MONTH) == Calendar.NOVEMBER
			&& cal.get(Calendar.DAY_OF_WEEK_IN_MONTH) == 4
			&& cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
			return false;
		}
		// check Memorial Day (last Monday of May)
		if (cal.get(Calendar.MONTH) == Calendar.MAY
			&& cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
			&& cal.get(Calendar.DAY_OF_MONTH) > (31 - 7) ) {
			return false;
		}
		// check Labor Day (1st Monday of September)
		if (cal.get(Calendar.MONTH) == Calendar.SEPTEMBER
			&& cal.get(Calendar.DAY_OF_WEEK_IN_MONTH) == 1
			&& cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
			return false;
		}
		// check President's Day (3rd Monday of February)
		if (cal.get(Calendar.MONTH) == Calendar.FEBRUARY
			&& cal.get(Calendar.DAY_OF_WEEK_IN_MONTH) == 3
			&& cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
			return false;
		}
		// check Columbus Day (2rd Monday of October)
		if (cal.get(Calendar.MONTH) == Calendar.OCTOBER
			&& cal.get(Calendar.DAY_OF_WEEK_IN_MONTH) == 2
			&& cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
			return false; 
		}
		// check Veterans Day (November 11)
			if (cal.get(Calendar.MONTH) == Calendar.NOVEMBER
			&& cal.get(Calendar.DAY_OF_MONTH) == 11) {
			return false;
		}
			// check Veterans Day (November 11)
		if (cal.get(Calendar.MONTH) == Calendar.NOVEMBER
				&& cal.get(Calendar.DAY_OF_MONTH) == 12 
				&& cal.get(Calendar.YEAR) == 2018){
				return false;
			}
		// check MLK Day (3rd Monday of January)
		if (cal.get(Calendar.MONTH) == Calendar.JANUARY
			&& cal.get(Calendar.DAY_OF_WEEK_IN_MONTH) == 3
			&& cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
			return false;
		}
		// IF NOTHING ELSE, IT'S A BUSINESS DAY
		if(isTollingDay(cal))
		{
			return false;
		}
		return true;
	}
	/**
	 * This method reads number from the screen
	 * @param strNumber: number is string format
	 * @return number in integer format
	 * @exception Exception
	*/
	public static int readNumberFromScreen(String strNumber)
	{
		if (strNumber.equals("") || strNumber.equals("0"))
		{
			return 0;
		}
		else
		{
			return Integer.parseInt(strNumber);
		}
	}
	
	/**
	 * This method reads number from DB
	 * @param strNumber: number is string format
	 * @return number in integer format
	 * @exception Exception
	*/
	public static int readNumberFromDb(String strNumber)
	{
		if (strNumber.equals("") || strNumber.equals("0"))
		{
			return 0;
		}
		else
		{
			return Integer.parseInt(strNumber.replace(".0", ""));
		}
	}
	/**
	 * This method gets validate element and report
	 * @param dateType: weekend, holiday or tolling date
	 * @param landsOn: calculated date before get pushed to business date
	 * @param date: calculated date after get pushed to business date
	 * @param actualValue: actual value
	 * @return if the element value is as expected, false if not
	 * @throws Exception
	 */
	public static boolean compareAndReport(String dateType,
										   String date, 
										   String expectedValues, 
										   String actualValue) throws Exception 
	{
		String startDate, endDate;
		JSONObject tollingDaysObjDisplay;
		String hover= "";
		String sbStr = "";
		if (standardTollingDaysStr.length()>2) sbStr = standardTollingDaysStr.substring(2);
		hover = "Updated with: \n"+ upadtedWith + "\n\n Standard Tolling Days: \n"+sbStr;
		if(!standardTolling)
		{
			hover = hover+"\n\n" + "Record Tolling Days: \n"+ recordTollingDaysStr;
		}
		String expectedValue, landsOn;
		if(expectedValues.equals("Empty") || expectedValues.equals("")) 
		{
			expectedValue = "Empty";
			landsOn = "Empty";
		}
		else
		{
			if(expectedValues.contains("#"))
			{
				expectedValue =  expectedValues.split("#")[0];
				landsOn = expectedValues.split("#")[1];
			}
			else
			{
				landsOn =  expectedValues;
				expectedValue = expectedValues;
			}
		}
		String fallsOn = landsOn.equals("Empty")?"Empty":format.parse(landsOn)+"";
		if(actualValue.equals("")) actualValue = "Empty";
		printLog("expectedValue: "+expectedValue + " Versus " + "actualValue: "  + actualValue);
		if (expectedValue.equalsIgnoreCase(actualValue))
		{
			updateHtmlReport("<abbr title='"+hover+"'>Lands on "+dateType+"-->["+fallsOn+"]</abbr>...more info!", 
					expectedValue, actualValue, 
					"VP", "pass", "");
			return true;
		}
		else
		{
			updateHtmlReport("<abbr title='"+hover+"'>Lands on "+dateType+"-->["+fallsOn+"]</abbr> ...more info!", 
					expectedValue, actualValue, 
					"VP", "fail", "");
			return false;
		}
	}
	
	/**
	 * This method gets validate element and report
	 * @param elementName: element name
	 * @param expectedValue: expected value
	 * @param actualValue: actual value
	 * @return if the element value is as expected, false if not
	 * @throws Exception
	 */
	public static boolean compareAndReport(String elementName, 
										   String expectedValue, 
										   String actualValue) throws Exception 
	{
		if(expectedValue.contains("#")) expectedValue = expectedValue.split("#")[1];
		if(expectedValue.equals("")) expectedValue = "Empty";
		if(actualValue.equals("")) actualValue = "Empty";
		printLog(elementName + " -- " + expectedValue + " Versus " + actualValue);
		if (expectedValue.equalsIgnoreCase(actualValue))
		{
			updateHtmlReport("Validate ["+ elementName +"]", expectedValue, actualValue, "VP", "pass", "");
			return true;
		}
		else
		{
			updateHtmlReport("Validate ["+ elementName +"]", expectedValue, actualValue, "VP", "fail", "");
			return false;
		}
	}
	
	/**
	 * This method gets validate element and report
	 * @param ScenarioType: Positive or Negative
	 * @param testedStatus: status, tested status
	 * @param actualstatus: actual status
	 * @return true if the status is as expected, false if not
	 * @throws Exception
	 */
	public static boolean validateObjectStatus(String ScenarioType, 
											   String testedStatus, 
											   String actualstatus,
											   String conditions) throws Exception 
	{
		if (ScenarioType.equalsIgnoreCase("Positive"))
		{
			if (testedStatus.equalsIgnoreCase(actualstatus))
			{
				updateHtmlReport(ScenarioType+ " scenario: ["+ conditions +"]", testedStatus, 
						actualstatus, "VP", "pass", "");
				return true;
			}
			else
			{
				updateHtmlReport(ScenarioType+ " scenario: ["+ conditions +"]", 
						testedStatus, actualstatus, "VP", "fail", "");
				return false;
			}
		}
		else
		{
			if (testedStatus.equalsIgnoreCase(actualstatus))
			{
				updateHtmlReport(ScenarioType+ " scenario: ["+ conditions +"]", "Not "+testedStatus,
						actualstatus, "VP", "fail", "");
				return false;
			}
			else
			{
				updateHtmlReport(ScenarioType+ " scenario: ["+ conditions +"]", "Not "+ testedStatus,
						actualstatus, "VP", "pass", "");
				return true;
			}
		}
	}
	
	/**
	 * This method checks if calendar date lands in tolling dates
	 * @param cal: calendar date
	 * @return true if tolling date, false if not
	 * @throws ParseException, JSONException 
	*/
	public static boolean isTollingDay(Calendar cal) throws ParseException, JSONException
	{	
		boolean isTolling = false;
		Date startDate, endDate;
		JSONObject tollingDaysObj;
		if(standardTolling)
		{
			tollingDaysObj = standardTollingDaysObj;
		}else
		{
			tollingDaysObj = recordTollingDaysObj;
		}
		if(tollingDaysObj != null)
		{ 
			JSONArray jsonArray = tollingDaysObj.getJSONArray("records");
			for (int ite = 0; 
					ite< Integer.parseInt(tollingDaysObj.getString("totalSize"))
					;ite++)
			{
				JSONObject jsonObject = jsonArray.getJSONObject(ite);
				startDate = format.parse( (String) jsonObject.get("Start_Date__c"));
				endDate = format.parse( (String) jsonObject.get("End_Date__c"));
				
				if (
						(cal.getTime().after(startDate) && cal.getTime().before(endDate))
						||(cal.getTime().compareTo(startDate)==0)
						||(cal.getTime().compareTo(endDate)==0)
					)
				{
					return true;
				}
			}
			
		}else
		{
			return isTolling;
		}
		return isTolling;
	}
	
	/**
	 * This method convert null value to blank
	 * @param str, a string that may have a null value
	 * @return blank if null or keep same string
	*/    
    public static String noNullVal(String str)
    {
    	String  strC = (str==null||str.equals("null"))?"":str;
    	return strC;
    }
    
    /**
	 * This method gets a tolling days from JSONObject
	 * @param tdObj, the JSONObject
	 * @return Tolling days in a formated string
	 * @throws Exception
	*/  
    public static String getTollingDaysStr(JSONObject tdObj) throws Exception
    {
    	String startDate, endDate;
		JSONObject tollingDaysObjDisplay;
		String tdays = "";
		if(standardTolling)
		{
			tollingDaysObjDisplay = standardTollingDaysObj;
		}else
		{
			tollingDaysObjDisplay = recordTollingDaysObj;
		}
		if(tollingDaysObjDisplay != null)
		{ 
			JSONArray jsonArray = tdObj.getJSONArray("records");
			for (int ite = 0; 
					ite< Integer.parseInt(tdObj.getString("totalSize"))
					;ite++)
			{
				JSONObject jsonObject = jsonArray.getJSONObject(ite);
				startDate = (String) jsonObject.get("Start_Date__c");
				endDate = (String) jsonObject.get("End_Date__c");
				tdays = tdays+", ["+startDate+"#"+endDate+"]";
			}
		}
		return "{"+tdays+"}";
    }
	/**
	 * This method gets validate element and report
	 * @param clause: conditions
	 * @param actualValue: actual value
	 * @param expectedValues, expected value
	 * @return if the element value is as expected, false if not
	 * @throws Exception
	 */
	public static boolean validateNextDeadlineDate( String clause,
													String expectedValue, 
													String actualValue) throws Exception 
	{
		if( expectedValue.equals("")) expectedValue = "Empty";
		if(actualValue.equals("")) actualValue = "Empty";
		printLog(expectedValue + " Versus " + actualValue);
		if (expectedValue.equalsIgnoreCase(actualValue))
		{
			updateHtmlReport("Scenario: "+ clause, 
					expectedValue, actualValue, 
					"VP", "pass", "");
			return true;
		}
		else
		{
			updateHtmlReport("Scenario: "+ clause, 
					expectedValue, actualValue, 
					"VP", "fail", "");
			return false;
		}
	}
	
	/**
	 * This method checks self initiated dates from the object
	 * @param obj: JSONObject
	 * @param populated, dates are populated or not
	 * @return true if all dates are as expected, false, if not.
	 * @throws Exception
	 */
	public static boolean checkSelfInitiatedDates(JSONObject obj, boolean populated) throws Exception 
	{
		boolean matches = true;
		matches = matches & checkAndreport("Calculated_Preliminary_Signature__c", obj.getString("Calculated_Preliminary_Signature__c"), populated);
		matches = matches & checkAndreport("Prelim_Team_Meeting_Deadline__c", obj.getString("Prelim_Team_Meeting_Deadline__c"), populated);
		matches = matches & checkAndreport("Prelim_Issues_Due_to_DAS__c", obj.getString("Prelim_Issues_Due_to_DAS__c"), populated);
		matches = matches & checkAndreport("Prelim_Concurrence_Due_to_DAS__c", obj.getString("Prelim_Concurrence_Due_to_DAS__c"), populated);
		matches = matches & checkAndreport("Calculated_Final_Signature__c", obj.getString("Calculated_Final_Signature__c"), populated);
		matches = matches & checkAndreport("Final_Issues_Due_to_DAS__c", obj.getString("Final_Issues_Due_to_DAS__c"), populated);
		matches = matches & checkAndreport("Final_Concurrence_Due_to_DAS__c", obj.getString("Final_Concurrence_Due_to_DAS__c"), populated);
		matches = matches & checkAndreport("Final_Team_Meeting_Deadline__c", obj.getString("Final_Team_Meeting_Deadline__c"), populated);
		matches = matches & checkAndreport("Final_Announcement_Date__c", obj.getString("Final_Announcement_Date__c"), populated);
		matches = matches & checkAndreport("Est_ITC_Notification_to_DOC_of_Final_Det__c", obj.getString("Est_ITC_Notification_to_DOC_of_Final_Det__c"), populated);
		matches = matches & checkAndreport("Estimated_Order_FR_Published__c", obj.getString("Estimated_Order_FR_Published__c"), populated);
		matches = matches & checkAndreport("Next_Announcement_Date__c", obj.getString("Next_Announcement_Date__c"), populated);
		matches = matches & checkAndreport("Next_Major_Deadline__c", obj.getString("Next_Major_Deadline__c"), populated);
		
	return matches;
	}
	
	/**
	 * This method checks if dates are populated/Empty and report 
	 * @param date: Date name
	 * @param dateVal, date's value
	 * @param populated, populated or empty
	 * @return true if all dates are as expected, false, if not.
	 * @throws Exception
	 */
	public static boolean checkAndreport(String date, 
										 String dateVal, 
										 boolean populated) throws Exception
	{
		if (populated)
		{
			if (!dateVal.equals("null") && !dateVal.equals(""))
			{
				updateHtmlReport("Verify Investigation dates", date + " is populated ["+dateVal+"]", 
						"As expected", "VP", "pass", "");
				return true;
			}else
			{
				updateHtmlReport("Verify Investigation dates", date + " is populated ["+dateVal+"]", 
						"Not as expected", "VP", "fail", "");
				return false;
			}
		}
		else//empty
		{
			if (dateVal == "null" || dateVal.equals(""))
			{
				updateHtmlReport("Verify Investigation dates", date + " is Empty ["+dateVal+"]", 
						"As expected", "VP", "pass", "");
				return true;
			}else
			{
				updateHtmlReport("Verify Investigation dates", date + " is Empty ["+dateVal+"]", 
						"Not as expected", "VP", "fail", "");
				return false;
			}
		}
	}
	
	/**
	 * This method read and save segment dates
	 * @return msg message to be displayed in the report
	 * @throws Exception
	 */
	public static LinkedHashMap<String, String> readSegmentDates(String msg) throws Exception
	{
		LinkedHashMap<String, String> dates  = new  LinkedHashMap<String, String> ();
		scrollToElement(replaceGui(guiMap.get("genericSegmentField"),"Prelim Team Meeting Deadline"));
		updateHtmlReport(msg, "Dates displyed", "As expected", "Step", "pass", msg + " - 1");
		scrollToElement(replaceGui(guiMap.get("genericSegmentField"),"Next Announcement Date"));
		updateHtmlReport(msg, "Dates displyed", "As expected", "Step", "pass", msg + " - 2");
		dates.put("Prelim Team Meeting Deadline", getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Prelim Team Meeting Deadline"), "text"));
		dates.put("Prelim Issues Due to DAS", getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Prelim Issues Due to DAS"), "text"));
		dates.put("Prelim Concurrence Due to DAS", getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Prelim Concurrence Due to DAS"), "text"));
		dates.put("Calculated Preliminary Signature", getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Calculated Preliminary Signature"), "text"));
		//dates.put("Preliminary Announcement Date", getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
		//		"Preliminary Announcement Date"), "text"));
		dates.put("Final Team Meeting Deadline", getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Final Team Meeting Deadline"), "text"));
		dates.put("Final Issues Due to DAS", getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Final Issues Due to DAS"), "text"));
		dates.put("Final Concurrence Due to DAS", getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Final Concurrence Due to DAS"), "text"));
		dates.put("Calculated Final Signature", getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Calculated Final Signature"), "text"));
		dates.put("Final Announcement Date", getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Final Announcement Date"), "text"));
		dates.put("Next Office Deadline", getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Next Office Deadline"), "text"));
		dates.put("Next Due to DAS Deadline", getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Next Due to DAS Deadline"), "text"));
		dates.put("Next Major Deadline", getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Next Major Deadline"), "text"));
		dates.put("Next Announcement Date", getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Next Announcement Date"), "text"));
		return dates;
	}
	
	
	/**
	 * This method read and save segment dates
	 * @return HashMap of dates
	 * @throws Exception
	 */
	public static LinkedHashMap<String, String> readSegmentDates(JSONObject obj) throws Exception
	{
		LinkedHashMap<String, String> dates  = new  LinkedHashMap<String, String> ();
		dates.put("Prelim Team Meeting Deadline", obj.getString("Prelim_Team_Meeting_Deadline__c"));
		dates.put("Prelim Issues Due to DAS", obj.getString("Prelim_Issues_Due_to_DAS__c"));
		dates.put("Prelim Concurrence Due to DAS", obj.getString("Prelim_Concurrence_Due_to_DAS__c"));
		dates.put("Calculated Preliminary Signature", obj.getString("Calculated_Preliminary_Signature__c"));
		//dates.put("Preliminary Announcement Date", obj.getString(""));
		dates.put("Final Team Meeting Deadline", obj.getString("Final_Team_Meeting_Deadline__c"));
		dates.put("Final Issues Due to DAS", obj.getString("Final_Issues_Due_to_DAS__c"));
		dates.put("Final Concurrence Due to DAS", obj.getString("Final_Concurrence_Due_to_DAS__c"));
		dates.put("Calculated Final Signature", obj.getString("Calculated_Final_Signature__c"));
		dates.put("Final Announcement Date", obj.getString("Final_Announcement_Date__c"));
		dates.put("Next Office Deadline", obj.getString("Next_Office_Deadline__c"));
		dates.put("Next Due to DAS Deadline", obj.getString("Next_Due_to_DAS_Deadline__c"));
		dates.put("Next Major Deadline", obj.getString("Next_Major_Deadline__c"));
		dates.put("Next Announcement Date", obj.getString("Next_Announcement_Date__c"));
		return dates;
	}
	/**
	 * This method checks dates when AR is linked to NSR
	 * @return arDates, dates of AR
	 * @param nsrDatesBefore, Dates of NSR before linking
	 * @param nsrDatesAfter, Dates of NSR after linking
	 * @return true if dates are as expected, false if not
	 * @throws Exception
	 */
	public static boolean alignNsrToArAndValidate(LinkedHashMap<String, String> arDates, 
												  LinkedHashMap<String, String> nsrDatesBefore,
												  LinkedHashMap<String, String> nsrDatesAfter)
												  throws Exception
	{
		boolean matches=true;
		for(Entry<String, String> entry: nsrDatesBefore.entrySet()) 
		{
            System.out.println(entry.getKey() + " => " + entry.getValue());
            matches = matches & compareArNsrDates(entry.getKey(), arDates.get(entry.getKey()), entry.getValue(),
            		nsrDatesAfter.get(entry.getKey()));
        }
		return matches;
	}
	
	/**
	 * This method gets element attribute
	 * @param dateName: the date name
	 * @param adDate: AD date value
	 * @param nfrDateBefore: NFR date at the beginning 
	 * @param nfrDateAfter: NFR date after has changed
	 * @return true if NFR date has changed to AD Date, false if not
	 * @throws Exception
	 */
	public static boolean compareArNsrDates(String dateName, 
										   String adDate, 
										   String nfrDateBefore, 
										   String nfrDateAfter) throws Exception 
	{
		printLog(dateName + "  -AR- " + adDate + " NSRBefore " + nfrDateBefore+ " NSRAfter " + nfrDateAfter);
		String yNo ="";
		if (dateName.equals("Next Due to DAS Deadline")||dateName.equals("Next Office Deadline"))
		{yNo = "not ";}
		if (
				(!adDate.equalsIgnoreCase(nfrDateBefore) && adDate.equalsIgnoreCase(nfrDateAfter))||
				( nfrDateBefore.equalsIgnoreCase(nfrDateAfter)
				&&(dateName.equals("Next Due to DAS Deadline")||dateName.equals("Next Office Deadline")))
			)
		{
			updateHtmlReport("Validate ["+ dateName +"]", "NSR date should "+yNo+"be adjusted to AR date", 
					"AR Date: " +adDate+", NSR Date: "+nfrDateAfter, "VP", "pass", "");
			return true;
		}
		else
		{
			updateHtmlReport("Validate ["+ dateName +"]", "NSR date should "+yNo+"be adjusted to AR date", 
					"AR Date: " +adDate+", NSR Date: "+nfrDateAfter, "VP", "fail", "");
			return false;
		}
	}
	
	
	/**
	 * This method validate investigation's statuses
	 * @param investigationId, investigation identifier
	 * @return true if all statuses worked as expected false if not
	 * @throws Exception
	 */
	public static boolean validateInvestigationStatus(String investigationId) throws Exception
	{
		boolean match = true;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date todayDate = new Date();
		String todayStr = dateFormat.format(todayDate);
		LinkedHashMap<String, String> record = new LinkedHashMap<String, String>();
		String sqlString = "select+Status__c+from+Investigation__c+where+id='"+investigationId+"'";
		//Prelim
		String condition = "Investigation Outcome is not 'ITC Negative Prelim'or 'Petition Withdrawn "
				+ "After initiation' or 'Suspension Agreement' "
				+ "and if Published Date (Type: Preliminary) is blank then the status is true";		
		JSONObject jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Prelim", jObj.getString("Status__c"), condition);
		
		//Amend Prelim
		HtmlReport.addHtmlStepTitle("Validate Status - Amend Prelim", "Title"); 
		condition = "FR Published Date (Type:  Preliminary) is not blank AND Will_You_Amend_the_Prelim_Determination is "
				+ "yes AND Actual_Amended_Prelim_Determination_Sig is blank AND Investigation Outcome is not"
				+ " ('ITC Negative Prelim' or 'Petition Withdrawn After Initiation' or 'Suspension Agreement') "
				+ "THEN status is true";
		record.clear();
      	record.put("Investigation__c", investigationId);
		record.put("Published_Date__c", todayStr);
		record.put("Cite_Number__c", "None");
		record.put("Type__c", "Preliminary");
		String frIdP = APITools.createObjectRecord("Federal_Register__c", record);
		record.clear();
		record.put("Actual_Preliminary_Signature__c", todayStr);
       	record.put("Amend_the_Preliminary_Determination__c", "Yes");
		//record.put("Calculated_Preliminary_Signature__c", todayStr);
		String code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Amend Prelim", jObj.getString("Status__c"), condition);
		//Final
		condition = "IF the Published Date (Type:  final) is blank AND Actual_Preliminary_Signature is not blank AND Published_Date_c "
				+ "(Type: Preliminary) is not blank AND Actual_Final_Signature__c is not blank AND Actual_Amended_Prelim_Determination_Sig "
				+ "is not blank AND Investigation Outcome "
				+ " is not ('ITC Negative Prelim' or 'Petition Withdrawn After Initiation' or 'Suspension Agreement') THEN Status is true";
		record.clear();
		//record.put("Actual_Preliminary_Signature__c", todayStr);
       	record.put("Actual_Amended_Prelim_Determination_Sig__c", todayStr);
		record.put("Actual_Final_Signature__c", todayStr);
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		
		match = match & 
		ADCVDLib.validateObjectStatus("Final", jObj.getString("Status__c"), condition);
		//Pending Order
		condition = "IF Published_Date__c (Type:  Preliminary) is not blank AND Published_Date__c (Type:  Final) is not blank "
				+ "AND Actual_Preliminary_Signature__c is not blank AND Actual_Final_Signature__c is not blank "
				+ "AND Investigation_Outcome__c is null THEN status is true";
		record.clear();
      	record.put("Investigation__c", investigationId);
		record.put("Published_Date__c", todayStr);
		record.put("Cite_Number__c", "None");
		record.put("Type__c", "Final");
		String frIdF = APITools.createObjectRecord("Federal_Register__c", record);
		record.clear();
		record.put("Actual_Final_Signature__c", todayStr);
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Pending Order", jObj.getString("Status__c"), condition);
		//Suspended
		condition = "The Investigation Outcome is 'Suspension Agreement' THEN Status is true";
		record.clear();
		record.put("Investigation_Outcome__c", "Suspension Agreement");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Suspended", jObj.getString("Status__c"), condition);
		//Hold
		condition = "IF The Litigation Picklist is Null AND the Investigation Outcome is “ITC Negative Prelim” THEN Published Date "
				+ "(Type:  ITC Prelim) + 30 or 45 days AND status true";
		condition = "The Investigation Outcome is 'Suspension Agreement' THEN Status is true";
		record.clear();
		record.put("Investigation_Outcome__c", "DOC Negative Final");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Hold", jObj.getString("Status__c"), condition);
		//Litigation
		condition = "IF the Litigation picklist is Yes AND Litigation_Resolved is No AND Litigation_Status is 'blank' OR Litigation_Status "
				+ "is “Not Active” THEN status is true  ";
		record.clear();
		record.put("Litigation_YesNo__c", "Yes");
		record.put("Litigation_Resolved__c", "No");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Litigation", jObj.getString("Status__c"), condition);
		//Customs
		condition = "IF the Litigation picklist is Yes AND Litigation_Resolved is Yes AND Have_Custom_Instruction_been_sent "
				+ "is No THEN status is true";
		record.clear();
		record.put("Litigation_YesNo__c", "Yes");
		record.put("Litigation_Resolved__c", "Yes");
		record.put("Have_Custom_Instruction_been_sent__c", "No");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Customs", jObj.getString("Status__c"), condition);
		//Closed
		condition = "IF the Litigation picklist is Yes AND Litigation_Resolved is Yes AND Have_Custom_Instruction_been_sent "
				+ "is Yes THEN status is true";
		record.clear();
		record.put("Have_Custom_Instruction_been_sent__c", "Yes");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Closed", jObj.getString("Status__c"), condition);
		return match;
	}
	
	/**
	 * This method validate Administrative Review, Expedited Review, New Shipper Review
	 * statuses
	 * @param row, row of elements
	 * @return true if all statuses worked as expected false if not
	 * @throws Exception
	 */
	public static boolean validateSegmentStatus_A(String sgementId, String segType) throws Exception
	{
		 boolean match = true;
		 HtmlReport.addHtmlStepTitle("Validate '"+segType+"' statuses","Title");
		 //prelim
		 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		 Date todayDate = new Date();
		 String todayStr = dateFormat.format(todayDate);
		 LinkedHashMap<String, String> record = new LinkedHashMap<String, String>();
		 String condition = "Initial Status";
		 String sqlString = "select+Status__c+from+segment__c+where+id='"+sgementId+"'";
		 JSONObject jObj = APITools.getRecordFromObject(sqlString);
		 match = match & 
		 ADCVDLib.validateObjectStatus("prelim", jObj.getString("Status__c"), condition);
		 //Final
		 condition = "Actual_Preliminary_Signature not null, Calculated_Preliminary_Signature not null,"
		 		+ " Published_Date(Preliminary FR) not null";
		 record.clear();
       	 record.put("segment__c", sgementId);
		 record.put("Published_Date__c", todayStr);
		 record.put("Cite_Number__c", "None");
		 record.put("Type__c", "Preliminary");
		 String frIdP = APITools.createObjectRecord("Federal_Register__c", record);
		 record.clear();
       	 record.put("Actual_Preliminary_Signature__c", todayStr);
		 record.put("Calculated_Preliminary_Signature__c", todayStr);
		 String code = APITools.updateRecordObject("segment__c", sgementId, record);
		 jObj = APITools.getRecordFromObject(sqlString);
		 match = match & 
				 ADCVDLib.validateObjectStatus("Final", jObj.getString("Status__c"), condition);
		//Amend Final
		 condition = "Actual_Final_Signature not nul, Segment_Outcome = 'Completed' "
		 		+ "Will_you_Amend_the_Final is 'YES', Published_Date(Final FR) not null";
		 record.clear();
       	 record.put("segment__c", sgementId);
		 record.put("Published_Date__c", todayStr);
		 record.put("Cite_Number__c", "None");
		 record.put("Type__c", "Final");
		 String frIdF = APITools.createObjectRecord("Federal_Register__c", record);		 
		 record.clear();
       	 record.put("Actual_Final_Signature__c", todayStr);
		 record.put("Segment_Outcome__c", "Completed");
		 record.put("Will_you_Amend_the_Final__c", "Yes");
		 code = APITools.updateRecordObject("segment__c", sgementId, record);
		 jObj = APITools.getRecordFromObject(sqlString);
		 match = match & 
				 ADCVDLib.validateObjectStatus("Amend Final", jObj.getString("Status__c"), condition);
		//Hold----------------------------------confirm with Paul
		 condition = "Will_you_Amend_the_Final is 'No'";
		 record.clear();
		 record.put("Will_you_Amend_the_Final__c", "No");
		 code = APITools.updateRecordObject("segment__c", sgementId, record);
		 jObj = APITools.getRecordFromObject(sqlString);
		 match = match & 
				 ADCVDLib.validateObjectStatus("Hold", jObj.getString("Status__c"), condition);
		 //Litigation
		 condition = "Litigation_YesNo is 'Yes', Litigation_Resolved is 'No'";
		 record.clear();
		 record.put("Litigation_YesNo__c", "Yes");
		  record.put("Litigation_Resolved__c", "No");
		 code = APITools.updateRecordObject("segment__c", sgementId, record);
		 jObj = APITools.getRecordFromObject(sqlString);
		 match = match & 
				 ADCVDLib.validateObjectStatus("Litigation", jObj.getString("Status__c"), condition);
		//Customs
		 condition = "Litigation_YesNo is 'Yes', Litigation_Resolved is 'Yes'";
		 record.clear();
		 record.put("Litigation_YesNo__c", "Yes");
		 record.put("Litigation_Resolved__c", "Yes");
		 code = APITools.updateRecordObject("segment__c", sgementId, record);
		 jObj = APITools.getRecordFromObject(sqlString);
		 match = match & 
				 ADCVDLib.validateObjectStatus("Customs", jObj.getString("Status__c"), condition);
		 //Closed
		 condition = "Litigation_YesNo is 'Yes', Litigation_Resolved is 'Yes' "
		 		+ "Have_Custom_Instruction_been_sent = 'Yes'";
		 record.clear();
		 record.put("Litigation_YesNo__c", "Yes");
		 record.put("Litigation_Resolved__c", "Yes");
		 record.put("Have_Custom_Instruction_been_sent__c", "Yes");
		 code = APITools.updateRecordObject("segment__c", sgementId, record);
		 jObj = APITools.getRecordFromObject(sqlString);
		 match = match & 
		 ADCVDLib.validateObjectStatus("Closed", jObj.getString("Status__c"), condition);
		 return match;
		 
	}
	
	/**
	 * This method validate hanged circumstance and Anticircumvention
	 * statuses
	 * @param sgementId, segment identifier
	 * @param segType, type of segment
	 * @return true if all statuses worked as expected false if not
	 * @throws Exception
	 */
	public static boolean validateSegmentStatus_B(String sgementId, String segType) throws Exception
	{
		 boolean match = true;
		 HtmlReport.addHtmlStepTitle("Validate '"+segType+"' statuses","Title");
		 //Initiation
		 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		 Date todayDate = new Date();
		 String todayStr = dateFormat.format(todayDate);
		 LinkedHashMap<String, String> record = new LinkedHashMap<String, String>();
		 String condition = "Initial Status";
		 String sqlString = "select+Status__c+from+segment__c+where+id='"+sgementId+"'";
		 JSONObject jObj = APITools.getRecordFromObject(sqlString);
		 match = match & 
		 ADCVDLib.validateObjectStatus("Initiation", jObj.getString("Status__c"), condition);
		 record.clear();
		 //Prelim
		 record.clear();
	     record.put("segment__c", sgementId);
		 record.put("Published_Date__c", todayStr);
		 record.put("Cite_Number__c", "None");
		 record.put("Type__c", "Initiation");
		 String frIdI = APITools.createObjectRecord("Federal_Register__c", record);
		 record.clear();
		 record.put("Actual_Initiation_Signature__c", todayStr);
		 if(segType.equalsIgnoreCase("Changed Circumstances Review"))
		{	 
			 condition = "Edit All parties in agreement to the outcome? is 'No' and Published_Date is not null(Initiation FR)"; 
		     record.put("All_parties_in_agreement_to_the_outcome__c", "No");
		}
		else //Anti-circumvention
		{
			condition = "Type_of_Circumvention_Inquiry equal to 'Later-Developed Merchandise' and Published_Date "
					+ "is not null(Initiation FR)"; 
		    record.put("Type_of_Circumvention_Inquiry__c", "Later-Developed Merchandise");
		}
		 String code = APITools.updateRecordObject("segment__c", sgementId, record);
		 jObj = APITools.getRecordFromObject(sqlString);
		 match = match & 
				 ADCVDLib.validateObjectStatus("Prelim", jObj.getString("Status__c"), condition);
		 //Final
		 condition = "Actual_Preliminary_Signature is not null, Calculated_Preliminary_Signature is not null,"
		 		+ " Published_Date__c(Preliminary FR) is not null";
		 record.clear();
      	 record.put("segment__c", sgementId);
		 record.put("Published_Date__c", todayStr);
		 record.put("Cite_Number__c", "None");
		 record.put("Type__c", "Preliminary");
		 String frIdP = APITools.createObjectRecord("Federal_Register__c", record);
		 record.clear();
      	 record.put("Actual_Preliminary_Signature__c", todayStr);
		 record.put("Calculated_Preliminary_Signature__c", todayStr);
		 code = APITools.updateRecordObject("segment__c", sgementId, record);
		 jObj = APITools.getRecordFromObject(sqlString);
		 match = match & 
				 ADCVDLib.validateObjectStatus("Final", jObj.getString("Status__c"), condition);
		//Hold----------------------------------confirm with Paul
		 condition = "Litigation_Hold_Expiration_Date is not null, Segment_Outcome is not null";
		 record.clear();
		 record.put("Litigation_Hold_Expiration_Date__c", todayStr);
		 record.put("Segment_Outcome__c", "Withdrawn");
		 code = APITools.updateRecordObject("segment__c", sgementId, record);
		 jObj = APITools.getRecordFromObject(sqlString);
		 match = match & 
				 ADCVDLib.validateObjectStatus("Hold", jObj.getString("Status__c"), condition);
		 //Litigation
		 condition = "Litigation_YesNo is 'Yes', Litigation_Resolved is 'No'";
		 record.clear();
		 record.put("Litigation_YesNo__c", "Yes");
		  record.put("Litigation_Resolved__c", "No");
		 code = APITools.updateRecordObject("segment__c", sgementId, record);
		 jObj = APITools.getRecordFromObject(sqlString);
		 match = match & 
				 ADCVDLib.validateObjectStatus("Litigation", jObj.getString("Status__c"), condition);
		//Customs
		 condition = "Litigation_YesNo is 'Yes', Litigation_Resolved is 'Yes'";
		 record.clear();
		 record.put("Litigation_YesNo__c", "Yes");
		 record.put("Litigation_Resolved__c", "Yes");
		 code = APITools.updateRecordObject("segment__c", sgementId, record);
		 jObj = APITools.getRecordFromObject(sqlString);
		 match = match & 
				 ADCVDLib.validateObjectStatus("Customs", jObj.getString("Status__c"), condition);
		 //Closed
		 condition = "Litigation_YesNo is 'Yes', Litigation_Resolved is 'Yes' "
		 		+ "Have_Custom_Instruction_been_sent = 'Yes'";
		 record.clear();
		 record.put("Litigation_YesNo__c", "Yes");
		 record.put("Litigation_Resolved__c", "Yes");
		 record.put("Have_Custom_Instruction_been_sent__c", "Yes");
		 code = APITools.updateRecordObject("segment__c", sgementId, record);
		 jObj = APITools.getRecordFromObject(sqlString);
		 match = match & 
		 ADCVDLib.validateObjectStatus("Closed", jObj.getString("Status__c"), condition);
		 return match;
	}
	
	/**
	 * This method validate statuses of other segment types
	 * statuses
	 * @param sgementId, segment identifier
	 * @param segType, type of segment
	 * @return true if all statuses worked as expected false if not
	 * @throws Exception
	 */
	public static boolean validateSegmentStatus_C(String sgementId, String segType) throws Exception
	{
		boolean match = true;
		HtmlReport.addHtmlStepTitle("Validate '"+segType+"' statuses","Title");
		 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		 Date todayDate = new Date();
		 String todayStr = dateFormat.format(todayDate);
		 LinkedHashMap<String, String> record = new LinkedHashMap<String, String>();
		//prelim
		 String condition = "Initial Status";
		 String sqlString = "select+Status__c+from+segment__c+where+id='"+sgementId+"'";
		 JSONObject jObj = APITools.getRecordFromObject(sqlString);
		 match = match & 
		 ADCVDLib.validateObjectStatus("Prelim", jObj.getString("Status__c"), condition);
		/*//prelim
		 condition = "Initial Status";
		 sqlString = "select+Status__c+from+segment__c+where+id='"+sgementId+"'";
		 jObj = APITools.getRecordFromObject(sqlString);
		 match = match & 
		 ADCVDLib.validateObjectStatus("prelim", jObj.getString("Status__c"), condition);*/
		 //Final
		 condition = "Decision_on_How_to_Proceed equal to 'Formal', Preliminary_Determination is 'No'";
		 record.clear();
      	 record.put("Decision_on_How_to_Proceed__c", "Formal");
		 record.put("Preliminary_Determination__c", "No");
		 String code = APITools.updateRecordObject("segment__c", sgementId, record);
		 jObj = APITools.getRecordFromObject(sqlString);
		 match = match & 
				 ADCVDLib.validateObjectStatus("Final", jObj.getString("Status__c"), condition);
		//Hold----------------------------------confirm with Paul
		 condition = "Actual_Final_Signature is not null, Segment_Outcome equal to complete";
		 record.clear();
		 record.put("Actual_Final_Signature__c", todayStr);
		 record.put("Segment_Outcome__c", "Completed");
		 code = APITools.updateRecordObject("segment__c", sgementId, record);
		 jObj = APITools.getRecordFromObject(sqlString);
		 match = match & 
				 ADCVDLib.validateObjectStatus("Hold", jObj.getString("Status__c"), condition);
		 //Litigation
		 condition = "Litigation_YesNo is 'Yes', Litigation_Resolved is 'No'";
		 record.clear();
		 record.put("Litigation_YesNo__c", "Yes");
		 record.put("Litigation_Resolved__c", "No");
		 record.put("Segment_Outcome__c", "Completed");
		 code = APITools.updateRecordObject("segment__c", sgementId, record);
		 jObj = APITools.getRecordFromObject(sqlString);
		 match = match & 
				 ADCVDLib.validateObjectStatus("Litigation", jObj.getString("Status__c"), condition);
		//Customs
		 condition = "Litigation_YesNo is 'Yes', Litigation_Resolved is 'Yes'";
		 record.clear();
		 record.put("Litigation_YesNo__c", "Yes");
		 record.put("Litigation_Resolved__c", "Yes");
		 code = APITools.updateRecordObject("segment__c", sgementId, record);
		 jObj = APITools.getRecordFromObject(sqlString);
		 match = match & 
				 ADCVDLib.validateObjectStatus("Customs", jObj.getString("Status__c"), condition);
		 //Closed
		 condition = "Litigation_YesNo is 'Yes', Litigation_Resolved is 'Yes' "
		 		+ "Have_Custom_Instruction_been_sent__c = 'Yes'";
		 record.clear();
		 record.put("Litigation_YesNo__c", "Yes");
		 record.put("Litigation_Resolved__c", "Yes");
		 record.put("Have_Custom_Instruction_been_sent__c", "Yes");
		 code = APITools.updateRecordObject("segment__c", sgementId, record);
		 jObj = APITools.getRecordFromObject(sqlString);
		 match = match & 
		 ADCVDLib.validateObjectStatus("Closed", jObj.getString("Status__c"), condition);
		 return match;
	}
	
	/**
	 * This method validate changed circumstance and Anticircumvention
	 * statuses
	 * @param row, row of elements
	 * @return true if all statuses worked as expected false if not
	 * @throws Exception
	 */
	public static boolean validateSegmentStatus_D(String sgementId, String segType) throws Exception
	{
		boolean match = true;
		HtmlReport.addHtmlStepTitle("Validate '"+segType+"' statuses","Title");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Date todayDate = new Date();
		String todayStr = dateFormat.format(todayDate);
		LinkedHashMap<String, String> record = new LinkedHashMap<String, String>();
		//prelim
		String condition = "Initial Status";
		String sqlString = "select+Status__c+from+segment__c+where+id='"+sgementId+"'";
		JSONObject jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Prelim", jObj.getString("Status__c"), condition);
		 //Final
		condition = "IF 240day sunset review AND Segment.Actual_Preliminary_Signature__c is not blank "
				+ "AND Published Date Type:Preliminary is not blank AND (Segment.Actual_Final_Signature__c"
				+ " is blank OR Published Date Type:Final is blank OR Segment.Segment_Outcome__c is blank ) THEN Status is TRUE ";
		 record.clear();
         record.put("segment__c", sgementId);
		 record.put("Published_Date__c", todayStr);
		 record.put("Cite_Number__c", "None");
		 record.put("Type__c", "Preliminary");
		 String frIdP = APITools.createObjectRecord("Federal_Register__c", record);
		 record.clear();
		 record.put("Actual_Preliminary_Signature__c", todayStr);
		 record.put("Calculated_Preliminary_Signature__c", todayStr);
		 String code = APITools.updateRecordObject("segment__c", sgementId, record);
		 jObj = APITools.getRecordFromObject(sqlString);
		 match = match & 
				 ADCVDLib.validateObjectStatus("Final", jObj.getString("Status__c"), condition);
		//Amend Final
		/* condition = "Actual_Final_Signature__c not nul, Segment_Outcome__c = 'Completed' "
		 		+ "Will_you_Amend_the_Final__c is 'YES', Published_Date__c(Final FR) not null";
		 record.clear();
      	 record.put("segment__c", sgementId);
		 record.put("Published_Date__c", todayStr);
		 record.put("Cite_Number__c", "None");
		 record.put("Type__c", "Final");
		 String frIdF = APITools.createObjectRecord("Federal_Register__c", record);
		 
		 record.clear();
      	 record.put("Actual_Final_Signature__c", todayStr);
		 record.put("Segment_Outcome__c", "Completed");
		 record.put("Will_you_Amend_the_Final__c", "Yes");
		 code = APITools.updateRecordObject("segment__c", sgementId, record);
		 jObj = APITools.getRecordFromObject(sqlString);
		 match = match & 
				 ADCVDLib.validateObjectStatus("Amend Final", jObj.getString("Status__c"), condition);*/
		//Hold----------------------------------confirm with Paul
		 condition = "Litigation_Hold_Expiration_Date__c is not null, segment outcome equal to complete and "
		 		+ "Litigation_Hold_Expiration_Date__c is not blank";
		 record.clear();
		 record.put("Litigation_Hold_Expiration_Date__c", todayStr);
		 record.put("Segment_Outcome__c", "Completed");
		 code = APITools.updateRecordObject("segment__c", sgementId, record);
		 jObj = APITools.getRecordFromObject(sqlString);
		 match = match & 
				 ADCVDLib.validateObjectStatus("Hold", jObj.getString("Status__c"), condition);
		 //Litigation
		 condition = "Litigation_YesNo__c is 'Yes', Litigation_Resolved__c is 'No'";
		 record.clear();
		 record.put("Litigation_YesNo__c", "Yes");
		 record.put("Litigation_Resolved__c", "No");
		 record.put("Segment_Outcome__c", "Completed");
		 code = APITools.updateRecordObject("segment__c", sgementId, record);
		 jObj = APITools.getRecordFromObject(sqlString);
		 match = match & 
				 ADCVDLib.validateObjectStatus("Litigation", jObj.getString("Status__c"), condition);
		//Customs
		 condition = "Litigation_YesNo__c is 'Yes', Litigation_Resolved__c is 'Yes'";
		 record.clear();
		 record.put("Litigation_YesNo__c", "Yes");
		 record.put("Litigation_Resolved__c", "Yes");
		 code = APITools.updateRecordObject("segment__c", sgementId, record);
		 jObj = APITools.getRecordFromObject(sqlString);
		 match = match & 
				 ADCVDLib.validateObjectStatus("Customs", jObj.getString("Status__c"), condition);
		 //Closed
		 condition = "Litigation_YesNo__c is 'Yes', Litigation_Resolved__c is 'Yes' "
		 		+ "Have_Custom_Instruction_been_sent__c = 'Yes'";
		 record.clear();
		 record.put("Litigation_YesNo__c", "Yes");
		 record.put("Litigation_Resolved__c", "Yes");
		 record.put("Have_Custom_Instruction_been_sent__c", "Yes");
		 code = APITools.updateRecordObject("segment__c", sgementId, record);
		 jObj = APITools.getRecordFromObject(sqlString);
		 match = match & 
		 ADCVDLib.validateObjectStatus("Closed", jObj.getString("Status__c"), condition);
		 return match;		 
	}
	
	/**
	 * This method gets validate status and report
	 * @param testedStatus: tested status name
	 * @param actualstatus: actual status value
	 * @param condition: scenario's conditions
	 * @return true if the status is as expected, false if not
	 * @throws Exception
	 */
	public static boolean validateObjectStatus(String testedStatus, 
											   String actualstatus,
											   String conditions) throws Exception 
	{
		if (testedStatus.equalsIgnoreCase(actualstatus))
		{
			updateHtmlReport("scenario: ["+ conditions +"]", testedStatus, 
					actualstatus, "VP", "pass", "");
			return true;
		}
		else
		{
			updateHtmlReport("scenario: ["+ conditions +"]", 
					testedStatus, actualstatus, "VP", "fail", "");
			return false;
		}
	}
	
	/**
	 * This method validate petition's statuses
	 * @param petitionId, Petition identifier
	 * @return true if all statuses worked as expected false if not
	 * @throws Exception
	 */
	public static boolean validatePetitionStatus(String petitionId) throws Exception
	{
	  //In progress
	  boolean match = true;
	  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	  Date todayDate = new Date();
	  String todayStr = dateFormat.format(todayDate);
	  LinkedHashMap<String, String> record = new LinkedHashMap<String, String>();
	  String condition = "Initial Status";
	  String sqlString = "select+Status__c+from+petition__c+where+id='"+petitionId+"'";
	  HtmlReport.addHtmlStepTitle("Validate Status - In Progress","Title");
	  JSONObject jObj = APITools.getRecordFromObject(sqlString);
	  match = match & 
	  ADCVDLib.validateObjectStatus("In Progress", jObj.getString("Status__c"), condition);
	  //Litigation
	  HtmlReport.addHtmlStepTitle("Validate Status - Litigation","Title");
      condition = "In the petition pick 'YES' for litigation and pick 'NO' for Litigation Resolved"
      		+ " and petition outcome equal to 'Self-Initiated'";
      record.clear();
      record.put("Actual_Initiation_Signature__c", todayStr);
      record.put("Calculated_Initiation_Signature__c", todayStr);
		record.put("Petition_Outcome__c", "Self-Initiated");
	  record.put("Litigation_YesNo__c", "Yes");
	  record.put("Litigation_Resolved__c", "No");
      String code = APITools.updateRecordObject("petition__c", petitionId, record);
      sqlString = "select+Status__c+from+petition__c+where+id='"+petitionId+"'";
      jObj = APITools.getRecordFromObject(sqlString);
      match = match & 
	  ADCVDLib.validateObjectStatus("Litigation", jObj.getString("Status__c"), condition);
      //C) Closed
      HtmlReport.addHtmlStepTitle("Validate Status - Closed","Title");
      //1
	  condition = "System should allow user to close the petiiton  if the Petition Outcome is'Petition"
				+ " Withdrawn/Did Not Initiate' and  in the petition pick 'YES' for llitigation and  pick  "
				+ "'YES' for  litigation resolved";
	  record.clear();
	  record.put("Petition_Outcome__c", "Deficient Petition/Did Not Initiate");
	  record.put("Actual_Initiation_Signature__c", "");
	  record.put("Litigation_YesNo__c", "Yes");
	  record.put("Litigation_Resolved__c", "Yes");
      code = APITools.updateRecordObject("petition__c", petitionId, record);
      sqlString = "select+Status__c+from+petition__c+where+id='"+petitionId+"'";
      jObj = APITools.getRecordFromObject(sqlString);
      match = match & 
	  ADCVDLib.validateObjectStatus("Closed", jObj.getString("Status__c"), condition);
      return match;
	}
	
	/**
	 * This method validate Litigation statuses
	 * statuses
	 * @param litigId, litigation identifier
	 * @param litigType, litigation type
	 * @return true if all statuses worked as expected false if not
	 * @throws Exception
	 */
	public static boolean validateLitigationStatus(String litigId, String litigType) throws Exception
	{
		boolean match = true;
		HtmlReport.addHtmlStepTitle("Validate '"+litigType+"' statuses","Title");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Date todayDate = new Date();
		String todayStr = dateFormat.format(todayDate);
		LinkedHashMap<String, String> record = new LinkedHashMap<String, String>();
		//prelim
		String condition = "Initial Status";
		String sqlString = "select+Status__c+from+Litigation__c+where+id='"+litigId+"'";
		JSONObject jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Preliminary", jObj.getString("Status__c"), condition);
		//Final
		condition = "Actual_Draft_Remand_released_to_party is not null";
		record.clear();
		record.put("Actual_Draft_Remand_released_to_party__c", todayStr);
		String code = APITools.updateRecordObject("Litigation__c", litigId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
				 ADCVDLib.validateObjectStatus("Final", jObj.getString("Status__c"), condition);
		//Closed
		condition = "Actual_Final_Signature__c is not Null";
		record.clear();
		record.put("Actual_Final_Signature__c", todayStr);
		code = APITools.updateRecordObject("Litigation__c", litigId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Closed", jObj.getString("Status__c"), condition);
		return match;
	}
}
