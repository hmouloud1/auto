/**
 * MilCorp
 * Mouloud Hamdidouche
 * December, 2018
*/

package libs;

import static GuiLibs.GuiTools.checkElementExists;
import static GuiLibs.GuiTools.clickElement;
import static GuiLibs.GuiTools.clickElementJs;
import static GuiLibs.GuiTools.elementExists;
import static GuiLibs.GuiTools.enterText;
import static GuiLibs.GuiTools.failTestCase;
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
import static GuiLibs.GuiTools.scrollToTheTopOfPage;
import static GuiLibs.GuiTools.selectElementByValue;
import static GuiLibs.GuiTools.setBrowserTimeOut;
import static GuiLibs.GuiTools.switchBackFromFrame;
import static GuiLibs.GuiTools.switchToFrame;
import static GuiLibs.GuiTools.testCaseStatus;
import static GuiLibs.GuiTools.unHighlightElement;
import static GuiLibs.GuiTools.updateHtmlReport;
import static ReportLibs.ReportTools.printLog;

import java.io.IOException;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.json.JSONObject;

import ReportLibs.HtmlReport;
import ServiceLibs.APITools;

import java.util.Random;

public class ADCVDLib{
	public static String filedDate,
	actualInitiationSignature, calculatedInitiationSignature, petitionOutcome;
	public static int petitionInitiationExtension;
	static DateFormat format;
	static Calendar calendar;
	static String caseType;
	static String investigationId, aInvestigation, cInvestigation;
	static String orderId, adCaseId, cvdCaseId, arSegmentId;
	static String todayStr;
	public ADCVDLib() throws IOException {
		//super();
		this.format = new SimpleDateFormat("M/d/yyyy");
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
			updateHtmlReport("Login to AD/CVD App with user "+user,  
					"User is able to login", "As expected", "Step", 
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
			failTestSuite("Login to AD/CVD App with user "+user, 
					"User is able to login", 
				"Not as expected", "Step", "fail", "Login failed");
			loginStatus = false;
		}else
		{
			highlightElement(guiMap.get("homeObjectLink"), "green");
			holdSeconds(2);
			updateHtmlReport("Login to AD/CVD App with user "+user,  "User is able to login", "As expected", 
					"Step", "pass", "Login to ADCVD");
		}
		return loginStatus;
	}
	
	/**
	 * This method creates new ADCVD case
	 * @param row: map of test case's data
	 * @return true case created correctly, false if not
	 * @exception Exception
	*/
	public static boolean createNewCase(LinkedHashMap<String, String> row) throws Exception
	{
		holdSeconds(2);
		clickElementJs(guiMap.get("adcvdCasesObjectLink"));
		caseType =  row.get("ADCVD_Case");
		holdSeconds(2);
		clickElementJs(guiMap.get("newCaseLink"));
		holdSeconds(3);
		String recType = "recTypeFiled";
		if(row.get("Record_Type").equalsIgnoreCase("Self-Initiate"))
		recType = "recTypeSelfInitiated";
		clickElementJs(guiMap.get(recType));
		holdSeconds(1);
		clickElementJs(guiMap.get("nextButton"));
		String caseName =  row.get("ADCVD_Case") + getCaseName();
		if(caseType.equals("A-")) 
			adCaseId = caseName;
		else 
			cvdCaseId=caseName;
		holdSeconds(1);
		enterText(guiMap.get("inputadcvdCase"),caseName);
		caseType = row.get("ADCVD_Case");
		//setAttributeValue("selectCommodity", "text", row.get("Commodity"));
		//clickElement(guiMap.get("selectCommodity"));
		clickElementJs(guiMap.get("selectCommodity")); 
		clickElementJs(replaceGui(guiMap.get("selectCommodityItem"),row.get("Commodity")));//
		//setAttributeValue("selectCaseType", "text", row.get("ADCVD_Case_Type"));
		clickElementJs(guiMap.get("selectCaseType"));
		clickElementJs(replaceGui(guiMap.get("selectCaseTypeItem"), caseType.equals("A-")? "AD ME":"CVD" ));
		enterText(guiMap.get("inputCbpCaseNumber"), row.get("CBP_Case_Number"));
		enterText(guiMap.get("inputProduct"), row.get("Product")+"_"+
		new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss").format(new Date()));
		enterText(guiMap.get("inputProductShortName"), row.get("Product_Short_Name"));
		//setAttributeValue("selectCountry", "text", row.get("Country"));
		clickElementJs(guiMap.get("selectCountry"));
		clickElementJs(replaceGui(guiMap.get("selectCountryItem"), row.get("Country")));
		//guiMap.get(replaceGui(guiMap.get("selectCountryItem")
		if(row.get("Lock_Record").equalsIgnoreCase("Yes"))
		clickElementJs(guiMap.get("checkRecordLock"));	
		holdSeconds(1);
		//takeScreenShot("Case form fill up", "filling up fields for new case");
		clickElementJs(guiMap.get("saveCaseButton"));
		holdSeconds(2);
		int currentTimeOut = setBrowserTimeOut(3);
		if(checkElementExists(guiMap.get("newCreateError")))
		{
			highlightElement(guiMap.get("newCreateError"), "red");
			holdSeconds(2);
			updateHtmlReport("Create ADCVD Case", "User is able to create a new case", "Not as expected", "Step", "fail", 
					"Create a new case");
			setBrowserTimeOut(currentTimeOut);
			clickElementJs(guiMap.get("CancelCaseButton"));
			
			return false;
		}else
		{
			setBrowserTimeOut(currentTimeOut);
			if(checkElementExists(replaceGui(guiMap.get("createdCase"),caseName)))
			{
				highlightElement(replaceGui(guiMap.get("createdCase"),caseName), "green");
				updateHtmlReport("Create ADCVD Case", "User is able to create a new case", 
						"Id: <span class = 'boldy'>"+" "+caseName+"</span>",
						"Step", "pass", "Ceate a new case");
				
				return true;
			}else
			{
				updateHtmlReport("Create ADCVD Case", "User is able to create a new case", "Not as expected", "Step",
						"fail", "Create a new case");
				return false;
			}
		}
	}
	/**
	 * This method creates new order
	 * @param row: map of test case's data
	 * @return true if order created correctly, false if not
	 * @exception Exception
	*/
	public static boolean createNewOrder (LinkedHashMap<String, String> row) throws Exception
	{
		holdSeconds(2);
		clickElementJs(guiMap.get("linkOrderSuspLitigation"));
		holdSeconds(1);
		clickElementJs(guiMap.get("linkNewOrderSuspLitigation"));
		
		
		/*holdSeconds(1);
		clickElementJs(guiMap.get("inputInvestigation"));
		holdSeconds(1);
		int currentWait = setBrowserTimeOut(3);
		
		//
		if(checkElementExists(replaceGui(guiMap.get("iInvestigationId"),investigationId)))
		{
			setBrowserTimeOut(currentWait);
			clickElementJs(replaceGui(guiMap.get("iInvestigationId"),investigationId));
		}
		else
		{
			setBrowserTimeOut(currentWait);
			clickElementJs(guiMap.get("buttonCancelOrder"));
			holdSeconds(1);
			clickElementJs(guiMap.get("buttonNewOrder"));
			holdSeconds(1);
			clickElementJs(guiMap.get("inputInvestigation"));
			clickElementJs(replaceGui(guiMap.get("iInvestigationId"),investigationId));
		}*/
		holdSeconds(1);
		clickElementJs(guiMap.get("saveCaseButton"));
		holdSeconds(2);
		int currentTimeOut = setBrowserTimeOut(3);
		if(checkElementExists(guiMap.get("newCreateError")))
		{
			highlightElement(guiMap.get("newCreateError"), "red");
			holdSeconds(2);
			updateHtmlReport("Create order", "User is able to create a order", 
					"Not as expected", "Step", "fail", "Create a new order");
			setBrowserTimeOut(currentTimeOut);
			return false;
		}else
		{
			setBrowserTimeOut(currentTimeOut);
			clickElementJs(guiMap.get("createdNewOrderLink"));
			holdSeconds(3);
			if(checkElementExists(guiMap.get("newCreatedOrder")))
			{
				orderId = getElementAttribute(guiMap.get("newCreatedOrder"), "text");
				scrollToElement(guiMap.get("newCreatedOrder"));
				highlightElement(guiMap.get("newCreatedOrder"), "green");
				updateHtmlReport("Create order", "User is able to create a new order", 
						"Id: <span class = 'boldy'>"+" "
						+ ""+orderId+"</span>", "Step", "pass", "Ceate a new order");
				holdSeconds(6);
				pageRefresh();
				holdSeconds(2);
				return true;
			}else
			{
				updateHtmlReport("Create order", "User is able to create a new order", 
						"Not as expected", "Step", "fail", 
						"Create a new order");
				return false;
			}
		}
	}
	/**
	 * This method creates new order
	 * @param row: map of test case's data
	 * @return true if order created correctly, false if not
	 * @exception Exception
	*/
	public static boolean createNewOrderOld(LinkedHashMap<String, String> row) throws Exception
	{
		holdSeconds(2);
		clickElementJs(guiMap.get("linkAdcvdOrder"));
		holdSeconds(1);
		clickElementJs(guiMap.get("buttonNewOrder"));
		holdSeconds(1);
		clickElementJs(guiMap.get("inputInvestigation"));
		holdSeconds(1);
		int currentWait = setBrowserTimeOut(3);
		if(checkElementExists(replaceGui(guiMap.get("iInvestigationId"),investigationId)))
		{
			setBrowserTimeOut(currentWait);
			clickElementJs(replaceGui(guiMap.get("iInvestigationId"),investigationId));
		}
		else
		{
			setBrowserTimeOut(currentWait);
			clickElementJs(guiMap.get("buttonCancelOrder"));
			holdSeconds(1);
			clickElementJs(guiMap.get("buttonNewOrder"));
			holdSeconds(1);
			clickElementJs(guiMap.get("inputInvestigation"));
			clickElementJs(replaceGui(guiMap.get("iInvestigationId"),investigationId));
		}
		holdSeconds(1);
		clickElementJs(guiMap.get("saveCaseButton"));
		holdSeconds(2);
		int currentTimeOut = setBrowserTimeOut(3);
		if(checkElementExists(guiMap.get("newCreateError")))
		{
			highlightElement(guiMap.get("newCreateError"), "red");
			holdSeconds(2);
			updateHtmlReport("Create order", "User is able to create a order", "Not as expected", "Step", "fail", 
					"Create a new order");
			setBrowserTimeOut(currentTimeOut);
			clickElementJs(guiMap.get("CancelCaseButton"));
			return false;
		}else
		{
			setBrowserTimeOut(currentTimeOut);
			if(checkElementExists(guiMap.get("newCreatedOrder")))
			{
				orderId = getElementAttribute(guiMap.get("newCreatedOrder"), "text");
				scrollToElement(guiMap.get("newCreatedOrder"));
				highlightElement(guiMap.get("newCreatedOrder"), "green");
				updateHtmlReport("Create order", "User is able to create a new order", "Id: <span class = 'boldy'>"+" "
						+ ""+orderId+"</span>", "Step", "pass", "Ceate a new order");
				holdSeconds(2);
				pageRefresh();
				holdSeconds(2);
				return true;
			}else
			{
				updateHtmlReport("Create order", "User is able to create a new order", "Not as expected", "Step", "fail", 
						"Create a new order");
				return false;
			}
		}
	}
	/**
	 * This method creates new petition
	 * @param row: map of test case data
	 * @return true if petition is created successfully, false if not
	 * @exception Exception
	*/
	public static boolean createNewPetition (LinkedHashMap<String, String> row) throws Exception
	{
		clickElementJs(guiMap.get("newPetitionButton"));
		String recType = "recTypeFiled";
		if(row.get("Record_Type").equalsIgnoreCase("Self-Initiate"))
		recType = "recTypeSelfInitiated";
		clickElementJs(guiMap.get(recType));
		holdSeconds(1);
		clickElementJs(guiMap.get("nextButton"));
		holdSeconds(2);
		enterText(replaceGui(guiMap.get("inputPetitionDate"),"Petition Filed"),
				row.get("Petition_Filed"));	
		holdSeconds(1);
		//enterText(replaceGui(guiMap.get("inputPetitionText"),"Initiation Extension (# of days)"), 
				//row.get("Initiation_Extension"));
		clickElement(replaceGui(guiMap.get("inputPetitionText"),"Lotus Notes Litigation ID"));
		updateHtmlReport("Create new Petition", "User is able fill up petition form", 
				"As expected", "Step", "pass", "Petition form");
		clickElementJs(guiMap.get("saveCaseButton"));
		//int currentTimeOut = 5;//setBrowserTimeOut(3);
		holdSeconds(3);
		int currentTimeOut = setBrowserTimeOut(4);
		if(checkElementExists(guiMap.get("newCreateError")))
		{
			highlightElement(guiMap.get("newCreateError"), "red");
			holdSeconds(5);
			updateHtmlReport("Create ADCVD Case", "User is able to create a new Petition", "Not as expected", "Step", 
					"fail", "Create a new Petition");
			setBrowserTimeOut(currentTimeOut);
			clickElementJs(guiMap.get("CancelCaseButton"));
			return false;
		}
		else
		{
			setBrowserTimeOut(currentTimeOut);
			if(!checkElementExists(guiMap.get("linkNewPetition")))
			{
				updateHtmlReport("Create new Petition", "User is not able to create petition",
						"Not as expected", "Step", "fail", "New petition failed");
				return false;
			}else
			{
				//holdSeconds(4);
				//setBrowserTimeOut(currentTimeOut);
				String petitionId = getElementAttribute(guiMap.get("linkNewPetition"), "text");
				highlightElement(guiMap.get("linkNewPetition"), "green");
				updateHtmlReport("Create new Petition", "User is able to create petition", 
						"id:<span class = 'boldy'>"+" "+petitionId.substring(petitionId.indexOf("P-"))+"</span>", "Step", 
						"pass", "New petition passed");
				//setBrowserTimeOut(currentTimeOut);
				clickElementJs(guiMap.get("linkNewPetition"));
				holdSeconds(4);
				//takeScreenShot("Petition Details", "screen shot of petition details");
				return true;
			}
		}
	}
	
	/**
	 * This method validates petition fields
	 * @return true if all dates matches, false if not
	 * @exception Exception
	*/
	public static boolean validatePetitionFields(LinkedHashMap<String, String> row) throws Exception
	{
		boolean allMatches = true;
		String actualValue;
		//scrollToTheButtomOfPage();
		scrollToElement(replaceGui(guiMap.get("genericPetitionDate"),"Next Office Deadline"));
		filedDate = getElementAttribute(replaceGui(guiMap.get("genericPetitionDate"),"Petition Filed"), "text");
		//highlightElement(replaceGui(guiMap.get("genericPetitionDate"),"Petition Filed"), "blue");
		System.out.println(filedDate);
		petitionOutcome = getElementAttribute(replaceGui(guiMap.get("genericPetitionDate"),"Petition Outcome"), "text");
		//highlightElement(replaceGui(guiMap.get("genericPetitionDate"),"Petition Outcome"), "blue");
		System.out.println(petitionOutcome);
		actualInitiationSignature = getElementAttribute(replaceGui(guiMap.get("genericPetitionDate"),
				"Actual Initiation Signature"), "text");
		//highlightElement(replaceGui(guiMap.get("genericPetitionDate"),"Actual Initiation Signature"), "blue");
		System.out.println(actualInitiationSignature);
		String actualInitiationIssuesToDas = getElementAttribute(replaceGui(guiMap.get("genericPetitionDate"),
				"Actual Initiation Issues to DAS"), "text");
		//highlightElement(replaceGui(guiMap.get("genericPetitionDate"),"Actual Initiation Issues to DAS"), "blue");
		System.out.println(actualInitiationIssuesToDas);
		String actualInitiationConcurrenceToDas = getElementAttribute(replaceGui(guiMap.get("genericPetitionDate"),
				"Actual Initiation Concurrence to DAS"), "text");
		//highlightElement(replaceGui(guiMap.get("genericPetitionDate"),"Actual Initiation Concurrence to DAS"), "blue");
		System.out.println(actualInitiationConcurrenceToDas);
		petitionInitiationExtension = readNumberFromScreen(getElementAttribute(replaceGui(guiMap.get("genericPetitionDate"),
				"Initiation Extension (# of days)"), "text"));
		//highlightElement(replaceGui(guiMap.get("genericPetitionDate"),"Initiation Extension (# of days)"), "blue");
		System.out.println(petitionInitiationExtension);
		String petitionOutcome = getElementAttribute(replaceGui(guiMap.get("genericPetitionDate"),
				"Petition Outcome"), "text");
		//highlightElement(replaceGui(guiMap.get("genericPetitionDate"),"Petition Outcome"), "blue");
		System.out.println(petitionOutcome);
		//Calculated Initiation Signature 
		calculatedInitiationSignature = calculateDate(petitionInitiationExtension+20, 
				"Calculated Initiation Signature", "calendar", filedDate);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericPetitionDate"),"Calculated Initiation Signature"), "text");
		allMatches = allMatches & compareAndReport("genericPetitionDate", "Calculated Initiation Signature", 
				calculatedInitiationSignature, actualValue);
		//Next Major Deadline	
		String nextMajorDeadline = calculateDate(0, "Next Major Deadline", "", actualInitiationSignature, 
				petitionOutcome, calculatedInitiationSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericPetitionDate"),"Next Major Deadline"), "text");
		allMatches = allMatches & compareAndReport("genericPetitionDate", "Next Major Deadline", 
				nextMajorDeadline, actualValue);
		//Next Announcement Date
		//???
		//Initiation Concurrence Due to DAS		 
		String initiationConcurrenceDueToDas = calculateDate(-1, "Initiation Concurrence Due to DAS",  
				"business", calculatedInitiationSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericPetitionDate"),
				"Initiation Concurrence Due to DAS"), "text");
		allMatches = allMatches & compareAndReport("genericPetitionDate", "Initiation Concurrence Due to DAS", 
				initiationConcurrenceDueToDas, actualValue);
		//Initiation Issues Due to DAS		
		String initiationIssueDueToDas = calculateDate(-3, "Initiation Issues Due to DAS",  "business", 
				calculatedInitiationSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericPetitionDate"),
				"Initiation Issues Due to DAS"), "text");
		allMatches = allMatches & compareAndReport("genericPetitionDate", "Initiation Issues Due to DAS", 
				initiationIssueDueToDas, actualValue);
		//Next Due to DAS Deadline	
		String nextDueToDasDeadline = calculateDate(0, "Next Due to DAS Deadline", "", 
				actualInitiationSignature, actualInitiationIssuesToDas,
				actualInitiationConcurrenceToDas, petitionOutcome, initiationIssueDueToDas,
				initiationConcurrenceDueToDas, calculatedInitiationSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericPetitionDate"),
				"Next Due to DAS Deadline"), "text");
		allMatches = allMatches &  compareAndReport("genericPetitionDate", "Next Due to DAS Deadline",
				nextDueToDasDeadline, actualValue);
		//Next Office Deadline
		String nextOfficeDeadline = calculateDate(0, "Next Office Deadline", "", actualInitiationSignature, 
				actualInitiationIssuesToDas,
		actualInitiationConcurrenceToDas, petitionOutcome, initiationIssueDueToDas,
		initiationConcurrenceDueToDas, calculatedInitiationSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericPetitionDate"),
				"Next Office Deadline"), "text");
		allMatches = allMatches &  compareAndReport("genericPetitionDate", "Next Office Deadline", 
				nextOfficeDeadline, actualValue);
		//Initiation Announcement Date
		String initiationAnnouncementDate = calculateDate(1, "Initiation Announcement Date", "business",  
				!actualInitiationSignature.equals("")?actualInitiationSignature:calculatedInitiationSignature);
		//Next Announcement Date
		String nextAnnouncementDate ="";
		if(!datePassed(initiationAnnouncementDate))
		{
				nextAnnouncementDate = initiationAnnouncementDate;
		}
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericPetitionDate"),
				"Next Announcement Date"), "text");
		allMatches = allMatches &  compareAndReport("genericPetitionDate", "Next Announcement Date", 
				nextAnnouncementDate, actualValue);
		return allMatches;
	}
	
	/**
	 * This method creates new Investigation
	 * @param row: map of test case data
	 * @return true if investigation created successfully, false if not
	 * @exception Exception
	*/
	public static boolean createNewInvestigation (LinkedHashMap<String, String> row) throws Exception
	{
		 pageRefresh();
		holdSeconds(2);
		clickElementJs(guiMap.get("linkInvestigation"));
		updateHtmlReport("Start creating new investigation", "User is able to click on investigation tab", 
				"As expected", "Step", "pass", "Investigation tab");
		holdSeconds(3);
		clickElementJs(guiMap.get("newInvestigationButton"));
		holdSeconds(2); 
		if(caseType.equals("C-"))
		clickElementJs(guiMap.get("recordTypeInvestigation"));
		updateHtmlReport("Choose record type", "Choose record-type pops up", "As expected", "Step", "pass", 
				"New investigation record popup");
		clickElementJs(guiMap.get("nextButtonInvestigation"));
		holdSeconds(2);
		updateHtmlReport("Open and fill up investigation form", "New investigation form opens", "As expected", 
				"Step", "pass", "New Investigation Form");
		clickElementJs(guiMap.get("saveCaseButton"));
		holdSeconds(6);
		int currentTimeOut = setBrowserTimeOut(4);
		if(checkElementExists(guiMap.get("newCreateError")))
		{
			highlightElement(guiMap.get("newCreateError"), "red");
			holdSeconds(2);
			updateHtmlReport("Create new Investigtion", "User is able to create a new investigation", 
					"Not as expected", "Step", "fail", "Create a new investigation");
			setBrowserTimeOut(currentTimeOut);
			clickElementJs(guiMap.get("CancelCaseButton"));
			return false;
		}
		else
		{
			setBrowserTimeOut(currentTimeOut);
			if(!checkElementExists(guiMap.get("linkNewCreatedInvestigation")))
			{
				updateHtmlReport("Create new Investigation", "User is able to create Investigation", 
						"Not as expected", "Step", "fail", "New investigation failed");
				setBrowserTimeOut(currentTimeOut);
				return false;
			}else
			{
				investigationId = getElementAttribute(guiMap.get("linkNewCreatedInvestigation"), "text");
				investigationId = investigationId.substring(investigationId.indexOf("I-"));
				if(caseType.equals("C-")) cInvestigation = investigationId;
				else aInvestigation = investigationId;
				highlightElement(guiMap.get("linkNewCreatedInvestigation"), "green");
				updateHtmlReport("Save the newly created investigation", "User is able to create/save investigation",
						"id: <span class = 'boldy'>"+" "+investigationId+"</span>", "Step", "pass", "New investigation created");
				setBrowserTimeOut(currentTimeOut);
				clickElement(guiMap.get("linkNewCreatedInvestigation"));
				holdSeconds(3);
				updateHtmlReport("Investigation details", "User is able to fill up the form", "As expected", 
						"Step", "pass", "Investigation details");
				holdSeconds(2);
				return true;
			}
		}
	}
	
	/**
	 * This method validates investigation fields
	 * @return true if all dates matches, false if not
	 * @exception Exception
	*/
	public static boolean validateInvestigationFields(LinkedHashMap<String, String> row) throws Exception
	{
		boolean allMatches = true;
		String actualValue;
		int daysNum;
		//Investigation Outcome
		String investigationOutcome= getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Investigation Outcome"), "text");
		//highlightElement(replaceGui(guiMap.get("genericPetitionDate"),"Actual Initiation Concurrence to DAS"), "blue");
		System.out.println(investigationOutcome);
		//ITC Notification to DOC of Final Determ
		String itcNotificationToDocOfFinalDeterm = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"ITC Notification to DOC of Final Determ"), "text");
		System.out.println(itcNotificationToDocOfFinalDeterm);
		//String estItcNotificationToDocOfFinalDeterm = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
			//	"Est ITC Notification to DOC of Final Det"), "text");
		//System.out.println(estItcNotificationToDocOfFinalDeterm);
		//Actual Preliminary Signature
		String actualPreliminarySignature = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Actual Preliminary Signature"), "text");
		System.out.println(actualPreliminarySignature);
		String actualfinalSignature = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Actual Final Signature"), "text");
		System.out.println(actualPreliminarySignature);
		//Final Extension (# of days)
		int finalExtension = readNumberFromScreen( getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Final Extension (# of days)"), "text"));
		System.out.println(finalExtension);
		int prelimExtension = readNumberFromScreen( getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Prelim Extension (# of days)"), "text"));
		//Actual Amended Prelim Determination Sig
		String actualAmendedPrelimDeterminationSig = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Actual Amended Prelim Determination Sig"), "text");
		//Calc Amended Prelim Determination Sig
		String CalcAmendedPrelimDeterminationSig = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Calc Amended Prelim Determination Sig"), "text");
		System.out.println(CalcAmendedPrelimDeterminationSig);
		//Actual Amended Final Signature
		String actualAmendedFinalSignature = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Actual Amended Final Signature"), "text");
		System.out.println(actualAmendedFinalSignature);
		//Will you Amend the Final
		String willYouAmendTheFinal = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Will you Amend the Final?"), "text");
		System.out.println(willYouAmendTheFinal);
		//Calculated Amended Final Signature
		String calculatedAmendedFinalSignature = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Calculated Amended Final Signature"), "text");
		System.out.println(calculatedAmendedFinalSignature);
		//Amend the Preliminary Determination?
		String amendThePreliminaryDetermination = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
						"Amend the Preliminary Determination?"), "text");
		System.out.println(amendThePreliminaryDetermination);	
		//Signature of Prelim Postponement FR
		String signatureOfPrelimPostponementFr = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Signature of Prelim Postponement FR"), "text");
		System.out.println(signatureOfPrelimPostponementFr);
		//Actual Prelim Issues to DAS
		String actualPrelimIssuesToDas = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Actual Prelim Issues to DAS"), "text");
		System.out.println(actualPrelimIssuesToDas);
		//Actual Prelim Concurrence to DAS
		String actualPrelimConcurrenceToDas = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Actual Prelim Concurrence to DAS"), "text");
		System.out.println(actualPrelimConcurrenceToDas);
		//Actual Amend Final Issues to DAS
		String actualAmendFinalIssuesToDas = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Actual Amend Final Issues to DAS"), "text");
		System.out.println(actualAmendFinalIssuesToDas);
		//Actual Amend Final Concurrence to DAS
		String actualAmendFinalConcurrenceToDas = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Actual Amend Final Concurrence to DAS"), "text");
		System.out.println(actualAmendFinalIssuesToDas);
		//Amend Prelim Issues Due to DAS
		String amendPrelimIssuesDueToDas = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Amend Prelim Issues Due to DAS"), "text");
		System.out.println(amendPrelimIssuesDueToDas);
		//Amend Prelim Concurrence Due to DAS
		String amendPrelimConcurrenceDueToDas = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Amend Prelim Concurrence Due to DAS"), "text");
		System.out.println(amendPrelimConcurrenceDueToDas);
		//Actual Final Issues to DAS
		String actualFinalIssuesToDas = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Actual Final Issues to DAS"), "text");
		System.out.println(actualFinalIssuesToDas);
		//Actual Final Concurrence to DAS
		String actualFinalConcurrenceToDas = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Actual Final Concurrence to DAS"), "text");
		System.out.println(actualFinalConcurrenceToDas);
		//Amend Final Issues Due to DAS
		String amendFinalIssuesDueToDas = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Amend Final Issues Due to DAS"), "text");
		System.out.println(amendFinalIssuesDueToDas);
		//	Amend Final Concurrence Due to DAS
		String amendFinalConcurrenceDueToDas = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Amend Final Concurrence Due to DAS"), "text");
		System.out.println(amendFinalConcurrenceDueToDas);
		//Amended Preliminary Announcement Date
		String amendedPreliminaryAnnouncementDate = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Amended Preliminary Announcement Date"), "text");
		System.out.println(amendedPreliminaryAnnouncementDate);
		//Actual Amend Prelim Issues to DAS
		String actualAmendPrelimIssuesToDas = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Actual Amend Prelim Issues to DAS"), "text");
		System.out.println(actualAmendPrelimIssuesToDas);
		//Actual Amend Prelim Concurrence to DAS 
		String actualAmendPrelimConcurrenceToDas = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Actual Amend Prelim Concurrence to DAS"), "text");
		System.out.println(actualAmendPrelimConcurrenceToDas);
		////////////////////////////////////////////////////////////////////////
		//Calculated ITC Prelim Determination
		scrollToElement(replaceGui(guiMap.get("genericInvestigationField"),"Calculated ITC Prelim Determination"));
		String calculatedITCPrelimDetermination = calculateDate(petitionInitiationExtension+45, 
				"Calculated ITC Prelim Determination", "calendar", filedDate);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),"Calculated ITC Prelim Determination"), "text");
		allMatches = allMatches & compareAndReport("genericInvestigationField", "Calculated ITC Prelim Determination", 
				calculatedITCPrelimDetermination, actualValue);
		//Calculated Prelim Extension Request File
		daysNum = caseType.equals("A-")? 115:40;
		String calculatedPrelimExtensionRequestFile = calculateDate(daysNum, "Calculated Prelim Extension Request File", "calendar", 
											!actualInitiationSignature.equals("")?actualInitiationSignature:calculatedInitiationSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),"Calculated Prelim Extension Request File"), "text");
		allMatches = allMatches & compareAndReport("genericInvestigationField", "Calculated Prelim Extension Request File", 
				calculatedPrelimExtensionRequestFile, actualValue);
		scrollToElement(replaceGui(guiMap.get("genericInvestigationField"),"Prelim Team Meeting Deadline"));
		//Calculated Postponement of PrelimDeterFR
		daysNum = caseType.equals("A-")? 120:45;
		String calculatedPostponementOfPrelimDeterFr = calculateDate(daysNum, "Calculated Postponement of PrelimDeterFR", "calendar", 
									!actualInitiationSignature.equals("")?actualInitiationSignature:calculatedInitiationSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),"Calculated Postponement of PrelimDeterFR"), "text");
		allMatches = allMatches & compareAndReport("genericInvestigationField", "Calculated Postponement of PrelimDeterFR", 
				calculatedPostponementOfPrelimDeterFr, actualValue);
		//Calculated Preliminary Signature  -------------QUESTION
		daysNum = caseType.equals("A-")? 140:65;
		String calculatedPreliminarySignature;
		String federalRegisterPublishDate="";
		if(petitionOutcome.equals("Self-Initiated") && ! federalRegisterPublishDate.equals(""))  
		{
			calculatedPreliminarySignature = calculateDate(prelimExtension + daysNum, "Calculated Preliminary Signature", "calendar",
					federalRegisterPublishDate);
		}
		else
		{
			calculatedPreliminarySignature = calculateDate(prelimExtension + daysNum, "Calculated Preliminary Signature", "calendar", 
											!actualInitiationSignature.equals("")?actualInitiationSignature:calculatedInitiationSignature);
		}
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),"Calculated Preliminary Signature"), "text");
		allMatches = allMatches & compareAndReport("genericInvestigationField", "Calculated Preliminary Signature", 
				calculatedPreliminarySignature, actualValue);
		//Prelim Team Meeting Deadline
		//scrollToElement(replaceGui(guiMap.get("genericInvestigationField"),"Prelim Team Meeting Deadline"));
		String prelimTeamMeetingDeadline = calculateDate(-21, "Prelim Team Meeting Deadline", "calendar", calculatedPreliminarySignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),"Prelim Team Meeting Deadline"), "text");
		allMatches = allMatches & compareAndReport("genericInvestigationField", "Prelim Team Meeting Deadline", 
				prelimTeamMeetingDeadline, actualValue);
		//Prelim Issues Due to DAS  Question business or Calendar
		String prelimIssuesDueToDas = calculateDate(-10, "Prelim Issues Due to DAS", "business", calculatedPreliminarySignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),"Prelim Issues Due to DAS"), "text");
		allMatches = allMatches & compareAndReport("genericInvestigationField", "Prelim Issues Due to DAS", 
				prelimIssuesDueToDas, actualValue);
		//Prelim Concurrence Due to DAS Question business or Calendar
		String prelimConcurrenceDueToDas = calculateDate(-5, "Prelim Concurrence Due to DAS", "business", 
				calculatedPreliminarySignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),"Prelim Concurrence Due to DAS"), "text");
		allMatches = allMatches & compareAndReport("genericInvestigationField", "Prelim Concurrence Due to DAS", 
				prelimConcurrenceDueToDas, actualValue);
		//Calculated Final Signature ------ Question finalExtension actualPreliminarySignature
		scrollToElement(replaceGui(guiMap.get("genericInvestigationField"),"Final Team Meeting Deadline"));
		federalRegisterPublishDate = "";
		String calculatedFinalSignature = "";
		if (finalExtension>0 && ! federalRegisterPublishDate.equals(""))
		{
			calculatedFinalSignature = calculateDate(75 + finalExtension, "Calculated Final Signature", "calendar",	
					federalRegisterPublishDate);
		}
		else 
		{
			calculatedFinalSignature = calculateDate(75 + finalExtension, "Calculated Final Signature", "calendar",  
				!actualPreliminarySignature.equals("")?actualPreliminarySignature:calculatedPreliminarySignature);
		}
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),"Calculated Final Signature"), "text");
		allMatches = allMatches & compareAndReport("genericInvestigationField", "Calculated Final Signature", 
				calculatedFinalSignature, actualValue);
		//Final Team Meeting Deadline --- Business or Calendar
		String finalTeamMeetingDeadline = calculateDate(-21, "Final Team Meeting Deadline", "calendar", calculatedFinalSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),"Final Team Meeting Deadline"), "text");
		allMatches = allMatches & compareAndReport("genericInvestigationField", "Final Team Meeting Deadline", 
				finalTeamMeetingDeadline, actualValue);
		scrollToElement(replaceGui(guiMap.get("genericInvestigationField"),"Calculated Final Signature"));
		//Est ITC Notification to DOC of Final Det
		String estItcNotificationToDocOfFinalDeterm = calculateDate(45, "Est ITC Notification to DOC of Final Det", "calendar",
									 !actualfinalSignature.equals("")?actualfinalSignature:calculatedFinalSignature);
		 actualValue = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
		"Est ITC Notification to DOC of Final Det"), "text");
		allMatches = allMatches & compareAndReport("genericInvestigationField", "Est ITC Notification to DOC of Final Det",
					 estItcNotificationToDocOfFinalDeterm, actualValue);
		//Estimated Order FR Published
		String estimatedOrderFRPublished;
		if(!investigationOutcome.equals("") && !investigationOutcome.equals("Order"))  
		{
			estimatedOrderFRPublished = "";
		}
		else
		{
			estimatedOrderFRPublished = calculateDate(7, "Estimated Order FR Published", "calendar", 
					!itcNotificationToDocOfFinalDeterm.equals("")?itcNotificationToDocOfFinalDeterm:estItcNotificationToDocOfFinalDeterm);
			actualValue = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),"Estimated Order FR Published"), "text");
			allMatches = allMatches & compareAndReport("genericInvestigationField", "Estimated Order FR Published",
					estimatedOrderFRPublished, actualValue);
		}
		//Calculated Order FR Signature
		scrollToElement(replaceGui(guiMap.get("genericInvestigationField"),"Calculated Order FR Signature"));
		String calculatedOrderFrSignature;
		if(!investigationOutcome.equals("") && !investigationOutcome.equals("Order"))  
		{
			calculatedOrderFrSignature = "";
		}
		else
		{
			calculatedOrderFrSignature = calculateDate(3, "Calculated Order FR Signature", "calendar", 
			!itcNotificationToDocOfFinalDeterm.equals("")?itcNotificationToDocOfFinalDeterm:estItcNotificationToDocOfFinalDeterm);
			actualValue = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),"Calculated Order FR Signature"), "text");
			allMatches = allMatches & compareAndReport("genericInvestigationField", "Calculated Order FR Signature", 
					calculatedOrderFrSignature, actualValue);
		}
		scrollToElement(replaceGui(guiMap.get("genericInvestigationField"),"Final Issues Due to DAS"));
		//Final Issues Due to DAS --- Business or Calendar
		String finalIssuesDueToDas = calculateDate(-10, "Final Issues Due to DAS", "business", calculatedFinalSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),"Final Issues Due to DAS"), "text");
		allMatches = allMatches & compareAndReport("genericInvestigationField", "Final Issues Due to DAS", finalIssuesDueToDas, actualValue);
		//Final Concurrence Due to DAS --- Business or Calendar
		String finalConcurrenceDueToDas = calculateDate(-5, "Final Concurrence Due to DAS", "business", calculatedFinalSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),"Final Concurrence Due to DAS"), "text");
		allMatches = allMatches & compareAndReport("genericInvestigationField", "Final Concurrence Due to DAS", finalConcurrenceDueToDas, actualValue);
		//Final Announcement Date --- Business or Calendar
		String finalAnnouncementDate = calculateDate(1, "Final Announcement Date", "business", 
				!actualfinalSignature.equals("")?actualfinalSignature:calculatedFinalSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),"Final Announcement Date"), "text");
		allMatches = allMatches & compareAndReport("genericInvestigationField", "Final Announcement Date", finalAnnouncementDate, actualValue);
		//Amended Final Announcement Date
		String amendedFinalAnnouncementDate;
		if(! willYouAmendTheFinal.equals("Yes"))
		{
			amendedFinalAnnouncementDate = "";
		}
		else if (!actualAmendedFinalSignature.equals(""))
		{
			amendedFinalAnnouncementDate = calculateDate(1, "Amended Final Announcement Date", "business", actualAmendedFinalSignature);
		}
		else if(!calculatedAmendedFinalSignature.equals(""))
		{
			amendedFinalAnnouncementDate = calculateDate(1, "Amended Final Announcement Date", "business", calculatedAmendedFinalSignature);
		}
		else
		{
			amendedFinalAnnouncementDate = "";
		}
		scrollToElement(replaceGui(guiMap.get("genericInvestigationField"),"Next Due to DAS Deadline"));
		//Preliminary Announcement Date
		String preliminaryAnnouncementDate = calculateDate(1, "Preliminary Announcement Date", "business",  
				!actualPreliminarySignature.equals("")?actualPreliminarySignature:calculatedPreliminarySignature);
		 //Next Announcement Date
		String nextAnnouncementDate = "";
		if(datePassed(preliminaryAnnouncementDate) && datePassed(amendedPreliminaryAnnouncementDate) && 
				datePassed(finalAnnouncementDate) && datePassed(amendedFinalAnnouncementDate))
		{
			nextAnnouncementDate = "";
		}
		else if(investigationOutcome.equals("Completed") || investigationOutcome.equals(""))
		{
			if(!datePassed(preliminaryAnnouncementDate))
			{
				nextAnnouncementDate = preliminaryAnnouncementDate;
			}
			else if(!datePassed(finalAnnouncementDate))
			{
				nextAnnouncementDate = finalAnnouncementDate;
			}
		}
		else
		{
			nextAnnouncementDate = "";
		}
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),"Next Announcement Date"), "text");
		allMatches = allMatches & compareAndReport("genericInvestigationField", "Next Announcement Date", nextAnnouncementDate, actualValue);
		//Next Due to DAS Deadline		
		String nextDueToDasDeadline = "";
		if(actualPreliminarySignature.equals("") && signatureOfPrelimPostponementFr.equals("") 
				&& !datePassed(calculatedPostponementOfPrelimDeterFr))
		{
			nextDueToDasDeadline = calculatedPostponementOfPrelimDeterFr;
		}
		else if(actualPreliminarySignature.equals("") && actualPrelimIssuesToDas.equals(""))
		{
			nextDueToDasDeadline = prelimIssuesDueToDas;
		}
		else if(actualPreliminarySignature.equals("") && actualPrelimConcurrenceToDas.equals(""))
		{
			nextDueToDasDeadline = prelimConcurrenceDueToDas;
		}
		else if(actualPreliminarySignature.equals(""))
		{
			nextDueToDasDeadline = calculatedPreliminarySignature;
		}
		else if(amendThePreliminaryDetermination.equalsIgnoreCase("Yes") && actualAmendedPrelimDeterminationSig.equals("")
				&& actualAmendFinalIssuesToDas.equals(""))
		{
			nextDueToDasDeadline = amendPrelimIssuesDueToDas;
		}
		else if(amendThePreliminaryDetermination.equalsIgnoreCase("Yes") && actualAmendedPrelimDeterminationSig.equals("")
				&& actualAmendFinalConcurrenceToDas.equals(""))
		{
			nextDueToDasDeadline = amendPrelimConcurrenceDueToDas;
		}
		else if(actualAmendedPrelimDeterminationSig.equals("") && amendThePreliminaryDetermination.equalsIgnoreCase("Yes"))
		{
			nextDueToDasDeadline = CalcAmendedPrelimDeterminationSig;
		}
		else if(actualfinalSignature.equals("") && actualFinalIssuesToDas.equalsIgnoreCase(""))
		{
			nextDueToDasDeadline = finalIssuesDueToDas;
		}
		else if(actualfinalSignature.equals("") && actualFinalConcurrenceToDas.equalsIgnoreCase(""))
		{
			nextDueToDasDeadline = finalConcurrenceDueToDas;
		}
		else if(actualfinalSignature.equals("") )
		{
			nextDueToDasDeadline = calculatedFinalSignature;
		}
		else if(willYouAmendTheFinal.equalsIgnoreCase("Yes") && actualAmendedFinalSignature.equals("") 
				&& actualAmendFinalIssuesToDas.equals(""))
		{
			nextDueToDasDeadline = amendFinalIssuesDueToDas;
		}
		else if(willYouAmendTheFinal.equalsIgnoreCase("Yes") && actualAmendedFinalSignature.equals("") 
				&& actualAmendFinalConcurrenceToDas.equals(""))
		{
			nextDueToDasDeadline = amendFinalConcurrenceDueToDas;
		}
		else if(willYouAmendTheFinal.equalsIgnoreCase("Yes") && actualAmendedFinalSignature.equals(""))
		{
			nextDueToDasDeadline = calculatedAmendedFinalSignature;
		}
		else if(federalRegisterPublishDate.equals(""))
		{
			nextDueToDasDeadline = calculatedOrderFrSignature;
		}
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),"Next Due to DAS Deadline"), "text");
		allMatches = allMatches & compareAndReport("genericInvestigationField", "Next Due to DAS Deadline", 
				nextDueToDasDeadline, actualValue);
		//Next Major Deadline
		String nextMajorDeadline="";
		if(actualPreliminarySignature.equals("") && investigationOutcome.equals(""))
		{
			nextMajorDeadline = calculatedPreliminarySignature;
		}
		else if (actualAmendedPrelimDeterminationSig.equals("")&& investigationOutcome.equals("")
				&& amendThePreliminaryDetermination.equalsIgnoreCase("Yes"))
		{
			nextMajorDeadline = CalcAmendedPrelimDeterminationSig;
		}
		else if(actualfinalSignature.equals("") && investigationOutcome.equals(""))
		{
			nextMajorDeadline = calculatedFinalSignature;
		}
		else if(actualAmendedFinalSignature.equals("") && investigationOutcome.equals("") && willYouAmendTheFinal.equalsIgnoreCase("Yes"))
		{
			nextMajorDeadline = calculatedAmendedFinalSignature;
		}
		else if(federalRegisterPublishDate.equals(""))
		{
			nextMajorDeadline = calculatedOrderFrSignature;
		}
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),"Next Major Deadline"), "text");
		allMatches = allMatches & compareAndReport("genericInvestigationField", "Next Major Deadline", nextMajorDeadline, actualValue);
		//Next Office Deadline
		String nextOfficeDeadline = "";
		if(actualPreliminarySignature.equals("") && ! datePassed(calculatedITCPrelimDetermination))
		{
			nextOfficeDeadline = calculatedITCPrelimDetermination;
		}
		else if(actualPreliminarySignature.equals("") && ! datePassed(calculatedPrelimExtensionRequestFile))
		{
			nextOfficeDeadline = calculatedPrelimExtensionRequestFile;
		}
		else if(actualPreliminarySignature.equals("") && signatureOfPrelimPostponementFr.equals("")
				&& ! datePassed(calculatedPostponementOfPrelimDeterFr))
		{
			nextOfficeDeadline = calculatedPostponementOfPrelimDeterFr;
		}
		else if(actualPreliminarySignature.equals("") && !datePassed(prelimTeamMeetingDeadline))
		{
			nextOfficeDeadline = prelimTeamMeetingDeadline;
		}
		else if(actualPreliminarySignature.equals("") && actualPrelimIssuesToDas.equals(""))
		{
			nextOfficeDeadline = prelimIssuesDueToDas;
		}
		else if(actualPreliminarySignature.equals("") && actualPrelimConcurrenceToDas.equals(""))
		{
			nextOfficeDeadline = prelimConcurrenceDueToDas;
		}
		else if(actualPreliminarySignature.equals(""))
		{
			nextOfficeDeadline = calculatedPreliminarySignature;
		}
		else if(amendThePreliminaryDetermination.equals("Yes") && actualAmendedPrelimDeterminationSig.equals("")
				&& actualAmendPrelimIssuesToDas.equals(""))
		{
			nextOfficeDeadline = amendPrelimIssuesDueToDas;
		}
		else if(amendThePreliminaryDetermination.equals("Yes") && actualAmendedPrelimDeterminationSig.equals("")
				&& actualAmendPrelimConcurrenceToDas.equals(""))
		{
			nextOfficeDeadline = amendPrelimConcurrenceDueToDas;
		}
		else if(amendThePreliminaryDetermination.equals("Yes") && actualAmendedPrelimDeterminationSig.equals(""))
		{
			nextOfficeDeadline = CalcAmendedPrelimDeterminationSig;
		}
		else if(actualfinalSignature.equals("") && ! datePassed(finalTeamMeetingDeadline))
		{
			nextOfficeDeadline = finalTeamMeetingDeadline;
		}
		else if(actualfinalSignature.equals("") && actualFinalIssuesToDas.equals(""))
		{
			nextOfficeDeadline = finalIssuesDueToDas;
		}
		else if(actualfinalSignature.equals("") && actualFinalConcurrenceToDas.equals(""))
		{
			nextOfficeDeadline = finalConcurrenceDueToDas;
		}
		else if(actualfinalSignature.equals("") )
		{
			nextOfficeDeadline = calculatedFinalSignature;
		}
		else if(willYouAmendTheFinal.equalsIgnoreCase("Yes") && actualAmendedFinalSignature.equals("") 
				&& actualAmendFinalIssuesToDas.equals(""))
		{
			nextOfficeDeadline = amendFinalIssuesDueToDas;
		}
		else if(willYouAmendTheFinal.equalsIgnoreCase("Yes") && actualAmendedFinalSignature.equals("") 
				&& actualAmendFinalConcurrenceToDas.equals(""))
		{
			nextOfficeDeadline = amendFinalConcurrenceDueToDas;
		}
		else if(willYouAmendTheFinal.equalsIgnoreCase("Yes") && actualAmendedFinalSignature.equals(""))
		{
			nextOfficeDeadline = calculatedAmendedFinalSignature;
		}
		else if(willYouAmendTheFinal.equalsIgnoreCase("Yes") && actualAmendedFinalSignature.equals("")
				&& actualAmendFinalConcurrenceToDas.equals(""))
		{
			nextOfficeDeadline = amendFinalConcurrenceDueToDas;
		}
		else if(willYouAmendTheFinal.equalsIgnoreCase("Yes") && actualAmendedFinalSignature.equals(""))
		{
			nextOfficeDeadline = calculatedAmendedFinalSignature;
		}
		else if(!datePassed(estItcNotificationToDocOfFinalDeterm))
		{
			nextOfficeDeadline = estItcNotificationToDocOfFinalDeterm;
		}
		else if(federalRegisterPublishDate.equals(""))
		{
			nextOfficeDeadline = calculatedOrderFrSignature;
		}
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),"Next Office Deadline"), "text");
		allMatches = allMatches & compareAndReport("genericInvestigationField", "Next Office Deadline", nextOfficeDeadline, actualValue);
		return allMatches;
	}
	
	/**
	 * This method creates new segment
	 * @param row: map of test case's data
	 * @return true if segment created correctly, false if not
	 * @exception Exception
	*/
	public static boolean createNewSegment(LinkedHashMap<String, String> row) throws Exception
	{
		String segmentType = row.get("Segment_Type").trim();
		String segmentId = "not displaying";
		holdSeconds(1);
		clickElementJs(guiMap.get("LinkSegmentFromOrder"));
		holdSeconds(1);
		clickElementJs(guiMap.get("NewButtonSegmentFromOrder"));
		holdSeconds(1);
		clickElementJs(replaceGui(guiMap.get("radioSegmentType"), segmentType) );
		updateHtmlReport("choosing segment type", "User is able to choose segment type", 
				"As Expected", "Step", "pass", "Segment "+segmentType);
		clickElementJs(guiMap.get("nextButtonSegment"));
		/*
		holdSeconds(1);
		scrollToElement(guiMap.get("inputOrder"));
		clickElementJs(guiMap.get("inputOrder"));
		int currentWait = setBrowserTimeOut(3);
		if(checkElementExists(replaceGui(guiMap.get("iOrderId"),orderId)))
		{
			setBrowserTimeOut(currentWait);
			clickElementJs(replaceGui(guiMap.get("iOrderId"),orderId));
		}
		else
		{
			setBrowserTimeOut(currentWait);
			clickElement(guiMap.get("buttonCancelSegment"));
			holdSeconds(1);
			clickElementJs(guiMap.get("linkSegments"));
			holdSeconds(1);
			clickElementJs(guiMap.get("buttonNewSegment"));
			holdSeconds(1);
			clickElementJs(replaceGui(guiMap.get("radioSegmentType"), segmentType) );
			holdSeconds(1);
			clickElementJs(guiMap.get("nextButtonSegment"));
			holdSeconds(1);
			//scrollToElement(guiMap.get("inputOrder"));
			clickElementJs(guiMap.get("inputOrder"));
			clickElementJs(replaceGui(guiMap.get("iOrderId"),orderId));//AO-0003084
		}
		*/
		holdSeconds(2);
		switch (segmentType)
		{
			case "Administrative Review":
			{
				enterText(replaceGui(guiMap.get("inputSegmentDate"), "Final Date of Anniversary Month"), 
						row.get("Final_Date_Of_Anniversary_Month"));
				clickElement(replaceGui(guiMap.get("inputSegmentText"), "Number of SR Companies"));
				break;
			}
			case "Anti-Circumvention Review":
			{
				enterText(replaceGui(guiMap.get("inputSegmentDate"), "Request Filed"), 
						row.get("Request_Filed_Date"));
				clickElement(replaceGui(guiMap.get("inputSegmentText"), "Initiation Extension (# of days)"));
				enterText(replaceGui(guiMap.get("inputSegmentDate"), "Application Accepted"), 
						row.get("Application_Accepted_Date"));
				clickElement(replaceGui(guiMap.get("inputSegmentText"), "Initiation Extension (# of days)"));
				clickElementJs(guiMap.get("preliminaryDetermination")); 
				clickElementJs(replaceGui(guiMap.get("preliminaryDeterminationItem"),row.get("Preliminary_Determination")));//
				break;
			}
			case "Changed Circumstances Review":
			{
				enterText(replaceGui(guiMap.get("inputSegmentDate"), "Request Filed"), 
						row.get("Request_Filed_Date"));
				clickElement(replaceGui(guiMap.get("inputSegmentText"), "Initiation Extension (# of days)"));
				clickElementJs(guiMap.get("preliminaryDetermination")); 
				clickElementJs(replaceGui(guiMap.get("preliminaryDeterminationItem"),row.get("Preliminary_Determination")));//
				clickElement(replaceGui(guiMap.get("inputSegmentText"), "Initiation Extension (# of days)"));
				break;
			}
			case "Expedited Review":
			{
				enterText(replaceGui(guiMap.get("inputSegmentDate"), "Calculated Initiation Signature"), 
						row.get("Calculated_Initiation_Signature"));
				clickElement(replaceGui(guiMap.get("inputSegmentText"), "Initiation Extension (# of days)"));
				break;
			}
			case "New Shipper Review":
			{
				enterText(replaceGui(guiMap.get("inputSegmentDate"), "Calculated Initiation Signature"), 
						row.get("Calculated_Initiation_Signature"));
				clickElement(replaceGui(guiMap.get("inputSegmentText"), "Initiation Extension (# of days)"));
				break;
			}
			case "Scope Inquiry":
			{
				enterText(replaceGui(guiMap.get("inputSegmentDate"), "Request Filed"), 
						row.get("Request_Filed_Date"));
				clickElement(replaceGui(guiMap.get("inputSegmentText"), "HTS Change(s)"));
				enterText(replaceGui(guiMap.get("inputSegmentDate"), "Actual date of Decision - How to Proceed"), 
						row.get("Actual_Date_Of_Decision_How_To_Proceed"));
				clickElement(replaceGui(guiMap.get("inputSegmentText"), "HTS Change(s)"));
				clickElementJs(guiMap.get("decisiononHowtoProceed")); 
				holdSeconds(1);
				clickElementJs(replaceGui(guiMap.get("decisiononHowtoProceedItem"),row.get("Decision_On_How_To_Proceed")));//
				clickElementJs(guiMap.get("scopeRollingType")); 
				clickElementJs(replaceGui(guiMap.get("scopeRollingTypeItem"),row.get("Type_Of_Scope_Ruling")));//
				clickElement(replaceGui(guiMap.get("inputSegmentText"), "HTS Change(s)"));
				break;
			}
			case "Sunset Review":
			{
				break;
			}
			default:
			{
				failTestSuite("Create new segment", "user is able to create segment", "Not as expected",
						"Step", "fail", "No such type of segment");
			}
		}
		clickElementJs(guiMap.get("buttonSaveSegment"));
		holdSeconds(2);
		int currentTimeOut = setBrowserTimeOut(4);
		if(checkElementExists(guiMap.get("newCreateError")))
		{
			setBrowserTimeOut(currentTimeOut);
			highlightElement(guiMap.get("newCreateError"), "red");
			holdSeconds(2);
			updateHtmlReport("Create Segment", "User is able to create segment", "Not as expected", 
							 "Step", "fail", "Create a new segment");
			return false;
		}else
		{
			if(checkElementExists(guiMap.get("SegmentsViewAll")))
			clickElementJs(guiMap.get("SegmentsViewAll"));
			holdSeconds(2);
			setBrowserTimeOut(currentTimeOut);
			if(checkElementExists(replaceGui(guiMap.get("segmentType"),segmentType)))
			{
				if(!segmentType.equals("Sunset Review"))
				{
					segmentId = getElementAttribute(replaceGui(guiMap.get("newCreatedSegment"),segmentType), "text");
				}
				if(segmentType.equals("Administrative Review"))
				{
					arSegmentId = segmentId;
				}
				highlightElement(replaceGui(guiMap.get("segmentType"),segmentType), "green");
				updateHtmlReport("Create segment " + segmentType, 
						"User is able to create a new segment", 
						"Id: <span class = 'boldy'>"+" "+segmentId+"</span>", "Step", "pass", 
						"Create segment " + segmentType);
				clickElementJs(replaceGui(guiMap.get("newCreatedSegment"),segmentType));
				return true;
			}else
			{
				updateHtmlReport("Create segment", "User is able to create a new segment", 
						"Not as expected", "Step", "fail", 
						"Create segment " + segmentType);
				setBrowserTimeOut(currentTimeOut);
				return false;
			}
		}
	}
	/**
	 * This method creates new segment
	 * @param row: map of test case's data
	 * @return true if segment created correctly, false if not
	 * @exception Exception
	*/
	public static boolean createNewSegmentOld(LinkedHashMap<String, String> row) throws Exception
	{
		String segmentType = row.get("Segment_Type").trim();
		String segmentId = "not displaying";
		holdSeconds(1);
		clickElementJs(guiMap.get("linkSegments"));
		holdSeconds(2);
		clickElementJs(guiMap.get("buttonNewSegment"));
		holdSeconds(3);
		clickElementJs(replaceGui(guiMap.get("radioSegmentType"), segmentType) );
		updateHtmlReport("choosing segment type", "User is able to choose segment type", 
				"As Expected", "Step", "pass", "Segment "+segmentType);
		clickElementJs(guiMap.get("nextButtonSegment"));
		holdSeconds(1);
		scrollToElement(guiMap.get("inputOrder"));
		clickElementJs(guiMap.get("inputOrder"));
		int currentWait = setBrowserTimeOut(3);
		if(checkElementExists(replaceGui(guiMap.get("iOrderId"),orderId)))
		{
			setBrowserTimeOut(currentWait);
			clickElementJs(replaceGui(guiMap.get("iOrderId"),orderId));
		}
		else
		{
			setBrowserTimeOut(currentWait);
			clickElement(guiMap.get("buttonCancelSegment"));
			holdSeconds(1);
			clickElementJs(guiMap.get("linkSegments"));
			holdSeconds(1);
			clickElementJs(guiMap.get("buttonNewSegment"));
			holdSeconds(1);
			clickElementJs(replaceGui(guiMap.get("radioSegmentType"), segmentType) );
			holdSeconds(1);
			clickElementJs(guiMap.get("nextButtonSegment"));
			holdSeconds(1);
			//scrollToElement(guiMap.get("inputOrder"));
			clickElementJs(guiMap.get("inputOrder"));
			clickElementJs(replaceGui(guiMap.get("iOrderId"),orderId));
		}
		switch (segmentType)
		{
			case "Administrative Review":
			{
				enterText(replaceGui(guiMap.get("inputSegmentDate"), "Final Date of Anniversary Month"), 
						row.get("Final_Date_Of_Anniversary_Month"));
				clickElement(replaceGui(guiMap.get("inputSegmentText"), "Number of SR Companies"));
				break;
			}
			case "Anti-Circumvention Review":
			{
				enterText(replaceGui(guiMap.get("inputSegmentDate"), "Request Filed"), 
						row.get("Request_Filed_Date"));
				clickElement(replaceGui(guiMap.get("inputSegmentText"), "Initiation Extension (# of days)"));
				enterText(replaceGui(guiMap.get("inputSegmentDate"), "Application Accepted"), 
						row.get("Application_Accepted_Date"));
				clickElement(replaceGui(guiMap.get("inputSegmentText"), "Initiation Extension (# of days)"));
				clickElementJs(guiMap.get("preliminaryDetermination")); 
				clickElementJs(replaceGui(guiMap.get("preliminaryDeterminationItem"),row.get("Preliminary_Determination")));//
				break;
			}
			case "Changed Circumstances Review":
			{
				enterText(replaceGui(guiMap.get("inputSegmentDate"), "Request Filed"), 
						row.get("Request_Filed_Date"));
				clickElement(replaceGui(guiMap.get("inputSegmentText"), "Initiation Extension (# of days)"));
				clickElementJs(guiMap.get("preliminaryDetermination")); 
				clickElementJs(replaceGui(guiMap.get("preliminaryDeterminationItem"),row.get("Preliminary_Determination")));//
				clickElement(replaceGui(guiMap.get("inputSegmentText"), "Initiation Extension (# of days)"));
				break;
			}
			case "Expedited Review":
			{
				enterText(replaceGui(guiMap.get("inputSegmentDate"), "Calculated Initiation Signature"), 
						row.get("Calculated_Initiation_Signature"));
				clickElement(replaceGui(guiMap.get("inputSegmentText"), "Initiation Extension (# of days)"));
				break;
			}
			case "New Shipper Review":
			{
				enterText(replaceGui(guiMap.get("inputSegmentDate"), "Calculated Initiation Signature"), 
						row.get("Calculated_Initiation_Signature"));
				clickElement(replaceGui(guiMap.get("inputSegmentText"), "Initiation Extension (# of days)"));
				break;
			}
			case "Scope Inquiry":
			{
				enterText(replaceGui(guiMap.get("inputSegmentDate"), "Request Filed"), 
						row.get("Request_Filed_Date"));
				clickElement(replaceGui(guiMap.get("inputSegmentText"), "HTS Change(s)"));
				enterText(replaceGui(guiMap.get("inputSegmentDate"), "Actual date of Decision - How to Proceed"), 
						row.get("Actual_Date_Of_Decision_How_To_Proceed"));
				clickElement(replaceGui(guiMap.get("inputSegmentText"), "HTS Change(s)"));
				clickElementJs(guiMap.get("decisiononHowtoProceed")); 
				clickElementJs(replaceGui(guiMap.get("decisiononHowtoProceedItem"),row.get("Decision_On_How_To_Proceed")));//
				clickElementJs(guiMap.get("scopeRollingType")); 
				clickElementJs(replaceGui(guiMap.get("scopeRollingTypeItem"),row.get("Type_Of_Scope_Ruling")));//
				clickElement(replaceGui(guiMap.get("inputSegmentText"), "HTS Change(s)"));
				break;
			}
			case "Sunset Review":
			{
				break;
			}
			default:
			{
				failTestSuite("Create new segment", "user is able to create segment", "Not as expected",
						"Step", "fail", "No such type of segment");
			}
		}
		clickElementJs(guiMap.get("buttonSaveSegment"));
		holdSeconds(2);
		int currentTimeOut = setBrowserTimeOut(3);
		if(checkElementExists(guiMap.get("newCreateError")))
		{
			highlightElement(guiMap.get("newCreateError"), "red");
			holdSeconds(2);
			updateHtmlReport("Create Segment", "User is able to create segment", "Not as expected", 
							 "Step", "fail", "Create a new segment");
			clickElementJs(guiMap.get("CancelCaseButton"));
			return false;
		}else
		{
			setBrowserTimeOut(currentTimeOut);
			if(checkElementExists(replaceGui(guiMap.get("segmentType"),segmentType)))
			{
				if(!segmentType.equals("Sunset Review"))
				{
					segmentId = getElementAttribute(guiMap.get("newCreatedSegment"), "text");
				}
				if(segmentType.equals("Administrative Review"))
				{
					arSegmentId = segmentId;
				}
				highlightElement(replaceGui(guiMap.get("segmentType"),segmentType), "green");
				updateHtmlReport("Create segment " + segmentType, "User is able to create a new segment", 
						"Id: <span class = 'boldy'>"+" "+segmentId+"</span>", "Step", "pass", "Create segment " + segmentType);
				return true;
			}else
			{
				updateHtmlReport("Create segment", "User is able to create a new segment", "Not as expected", "Step", "fail", 
						"Create segment " + segmentType);
				setBrowserTimeOut(currentTimeOut);
				return false;
			}
		}
	}
	
	/**
	 * This method validates new administrator review segment
	 * @return true if all dates matches, false if not
	 * @exception Exception
	*/
	public static boolean validateNewSegmentAdministrativeReview() throws Exception
	{
		boolean allMatches = true;
		String actualValue  = "" ;
		String finalDateOfAnniversaryMonth = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Final Date of Anniversary Month"), "text");
		//highlightElement(replaceGui(guiMap.get("genericSegmentField"),"Preliminary Extension Remaining"), "blue");
		System.out.println(finalDateOfAnniversaryMonth);
		//Final Extension (# of days)
		int finalExtensionDays = readNumberFromScreen(getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Final Extension (# of days)"), "text")) ;
		//highlightElement(replaceGui(guiMap.get("genericSegmentField"),"Final Extension (# of days)"), "blue");
		System.out.println(finalExtensionDays);
		//Prelim Extension (# of days)
		int prelimExtensionDays = readNumberFromScreen(getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Prelim Extension (# of days)"), "text")) ;
		//highlightElement(replaceGui(guiMap.get("genericSegmentField"),"Final Extension (# of days)"), "blue");
		System.out.println(finalExtensionDays);
		String actualPreliminarySignature = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Preliminary Signature"), "text");
		//highlightElement(replaceGui(guiMap.get("genericSegmentField"),"Preliminary Extension Remaining"), "blue");
		System.out.println(actualPreliminarySignature);
		//Actual Final Signature
		String actualFinalSignature = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Actual Final Signature"), "text");
		System.out.println(actualFinalSignature);
		String willYouAmendTheFinal = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Will you Amend the Final?"), "text");
		System.out.println(willYouAmendTheFinal);
		//Actual Amended Final Signature
		String actualAmendedFinalSignature = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Actual Amended Final Signature"), "text");
		System.out.println(actualAmendedFinalSignature);
		//Calculated Amended Final Signature
		String calculatedAmendedFinalSignature = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Calculated Amended Final Signature"), "text");
		System.out.println(calculatedAmendedFinalSignature);
		//Actual Prelim Issues to DAS
		String actualPrelimIssuesToDas = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Prelim Issues to DAS"), "text");
		//highlightElement(replaceGui(guiMap.get("genericSegmentField"),"Preliminary Extension Remaining"), "blue");
		System.out.println(actualPrelimIssuesToDas); 
		//Actual Prelim Concurrence to DAS
		String actualPrelimConcurrenceToDas = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Prelim Concurrence to DAS"), "text");
		//highlightElement(replaceGui(guiMap.get("genericSegmentField"),"Preliminary Extension Remaining"), "blue");
		System.out.println(actualPrelimConcurrenceToDas);
		//Actual Final Issues to DAS
		String actualFinalIssuesToDas = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Actual Final Issues to DAS"), "text");
		System.out.println(actualFinalIssuesToDas);
		//Actual Final Concurrence to DAS
		String actualFinalConcurrenceToDas = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Actual Final Concurrence to DAS"), "text");
		System.out.println(actualFinalConcurrenceToDas);
		//Segment Outcome
		String segmentOutcome = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Segment Outcome"), "text");
		//highlightElement(replaceGui(guiMap.get("genericSegmentField"),"Preliminary Extension Remaining"), "blue");
		System.out.println(segmentOutcome);
		String actualAmendFinalIssuesToDas = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Amend Final Issues to DAS"), "text");
		//highlightElement(replaceGui(guiMap.get("genericSegmentField"),"Preliminary Extension Remaining"), "blue");
		System.out.println(actualAmendFinalIssuesToDas);
		String amendFinalIssuesDueToDas = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Amend Final Issues Due to DAS"), "text");
		//highlightElement(replaceGui(guiMap.get("genericSegmentField"),"Preliminary Extension Remaining"), "blue");
		System.out.println(amendFinalIssuesDueToDas); 
		String amendFinalConcurrenceDueToDas = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Amend Final Concurrence Due to DAS"), "text");
		//highlightElement(replaceGui(guiMap.get("genericSegmentField"),"Preliminary Extension Remaining"), "blue");
		System.out.println(amendFinalConcurrenceDueToDas);
		String actualAmendFinalConcurrenceToDas = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Amend Final Concurrence to DAS"), "text");
		//highlightElement(replaceGui(guiMap.get("genericSegmentField"),"Preliminary Extension Remaining"), "blue");
		System.out.println(actualAmendFinalConcurrenceToDas);
		/////////////////////////////////////// !actualInitiationSignature.equals("")?actualInitiationSignature:calculatedInitiationSignature
		scrollToElement(replaceGui(guiMap.get("genericSegmentField"),"Prelim Team Meeting Deadline"));
		//Calculated Preliminary Signature
		String calculatedPreliminarySignature = calculateDate(prelimExtensionDays + 245, "Calculated Preliminary Signature", 
				"calendar",finalDateOfAnniversaryMonth );
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Calculated Preliminary Signature"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Calculated Preliminary Signature", 
				calculatedPreliminarySignature, actualValue);
		//Prelim Team Meeting Deadline
		String prelimTeamMeetingDeadline = calculateDate(-21, "Prelim Team Meeting Deadline", "calendar", 
				calculatedPreliminarySignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Prelim Team Meeting Deadline"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Prelim Team Meeting Deadline", prelimTeamMeetingDeadline, 
				actualValue);
		//Prelim Issues Due to DAS
		String prelimIssuesDueToDas = calculateDate(-10, "Prelim Issues Due to DAS", "business", calculatedPreliminarySignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Prelim Issues Due to DAS"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Prelim Issues Due to DAS", prelimIssuesDueToDas, 
				actualValue);
		//Calculated Amended Final Signature
		//Prelim Concurrence Due to DAS
		String prelimConcurrenceDueToDas = calculateDate(-5, "Prelim Concurrence Due to DAS", "business", calculatedPreliminarySignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Prelim Concurrence Due to DAS"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Prelim Concurrence Due to DAS", prelimConcurrenceDueToDas, 
				actualValue);
		//Preliminary Announcement Date
		String preliminaryAnnouncementDate = calculateDate(1, "Preliminary Announcement Date", "business", 
				!actualPreliminarySignature.equals("")?actualPreliminarySignature:calculatedPreliminarySignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Preliminary Announcement Date"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Preliminary Announcement Date", preliminaryAnnouncementDate, 
				actualValue);
		scrollToElement(replaceGui(guiMap.get("genericSegmentField"),"Calculated Final Signature"));
		//Calculated Final Signature
		String publishedDate = "";
		String calculatedFinalSignature;
		if(publishedDate.equals("") && ! calculatedPreliminarySignature.equals(""))
		{
			calculatedFinalSignature = calculateDate(120 + finalExtensionDays, "Calculated Final Signature", "Calendar", 
									   calculatedPreliminarySignature);
		}
		else
		{
			calculatedFinalSignature = calculateDate(120 + finalExtensionDays, "Calculated Final Signature", "Calendar", 
					publishedDate);
		}
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Calculated Final Signature"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Calculated Final Signature", calculatedFinalSignature, 
				actualValue);
		//Final Team Meeting Deadline
		String finalTeamMeetingDeadline = calculateDate(-21, "Final Team Meeting Deadline", "calendar", calculatedFinalSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Final Team Meeting Deadline"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Final Team Meeting Deadline", finalTeamMeetingDeadline, 
				actualValue);
		//Final Issues Due to DAS
		String finalIssuesDueToDas = calculateDate(-10, "Final Issues Due to DAS",  "business", calculatedFinalSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Final Issues Due to DAS"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Final Issues Due to DAS", finalIssuesDueToDas, 
				actualValue);
		//Final Concurrence Due to DAS
		String finalConcurrenceDueToDas = calculateDate(-5, "Final Concurrence Due to DAS",  "business", calculatedFinalSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Final Concurrence Due to DAS"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Final Concurrence Due to DAS", finalConcurrenceDueToDas, 
				actualValue);
		//Final Announcement Date
		String finalAnnouncementDate = calculateDate(1, "Final Announcement Date", "business", 
				!actualFinalSignature.equals("")?actualFinalSignature:calculatedFinalSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Final Announcement Date"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Final Announcement Date", finalAnnouncementDate, 
				actualValue);
		//Next Major Deadline
		String nextMajorDeadline = "";
		if(publishedDate.equals("") )
		{
			nextMajorDeadline = calculatedPreliminarySignature;
		} else if(actualFinalSignature.equals(""))
		{
			nextMajorDeadline = calculatedFinalSignature;
		}else if (actualAmendedFinalSignature.equals("") && publishedDate.equals("") && willYouAmendTheFinal.equalsIgnoreCase("Yes"))
		{
			nextMajorDeadline = calculatedAmendedFinalSignature;
		}
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Next Major Deadline"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Next Major Deadline", nextMajorDeadline, actualValue);
		//Next Due to DAS Deadline 
		String nextDueToDasDeadline = "";
		if(actualPreliminarySignature.equals("") && actualPrelimIssuesToDas.equals(""))
		{
			nextDueToDasDeadline = prelimIssuesDueToDas;
		}else if(actualPreliminarySignature.equals("") && actualPrelimConcurrenceToDas.equals(""))
		{
			nextDueToDasDeadline = prelimConcurrenceDueToDas;
		}else if(actualPreliminarySignature.equals("") && segmentOutcome.equals(""))								
		{
			nextDueToDasDeadline = calculatedPreliminarySignature;
		}else if(actualFinalSignature.equals("") && actualFinalIssuesToDas.equals(""))
		{
			nextDueToDasDeadline = finalIssuesDueToDas;
		}else if(actualFinalSignature.equals("") && actualFinalConcurrenceToDas.equals(""))
		{
			nextDueToDasDeadline = finalConcurrenceDueToDas;
		}else if(actualFinalSignature.equals("") && segmentOutcome.equals(""))
		{
			nextDueToDasDeadline = calculatedFinalSignature;
		}else if(willYouAmendTheFinal.equalsIgnoreCase("Yes") && actualAmendedFinalSignature.equals("") 
				&& actualAmendFinalIssuesToDas.equals(""))
		{
			nextDueToDasDeadline = amendFinalIssuesDueToDas; 
		}else if(willYouAmendTheFinal.equalsIgnoreCase("Yes") && actualAmendedFinalSignature.equals("") 
				&& actualAmendFinalConcurrenceToDas.equals(""))
		{
			nextDueToDasDeadline = amendFinalConcurrenceDueToDas;
		}else if(willYouAmendTheFinal.equalsIgnoreCase("Yes") && actualAmendedFinalSignature.equals(""))
		{
			nextDueToDasDeadline = calculatedAmendedFinalSignature;
		}
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Next Due to DAS Deadline"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Next Due to DAS Deadline", 
				nextDueToDasDeadline, actualValue);
		//Next Office Deadline
		String nextOfficeDeadline = "";
		if(actualPreliminarySignature.equals("") && !datePassed(prelimTeamMeetingDeadline))
		{
			nextOfficeDeadline = prelimTeamMeetingDeadline;
		}
		else if(actualPreliminarySignature.equals("") && actualPrelimIssuesToDas.equals(""))
		{
			nextOfficeDeadline = prelimIssuesDueToDas;
		}
		else if(actualPreliminarySignature.equals("") && actualPrelimConcurrenceToDas.equals(""))
		{
			nextOfficeDeadline = prelimConcurrenceDueToDas;
		}
		else if(actualPreliminarySignature.equals(""))
		{
			nextOfficeDeadline = calculatedPreliminarySignature;
		}
		else if (actualFinalSignature.equals("") && !datePassed(finalTeamMeetingDeadline) )
		{
			nextOfficeDeadline = finalTeamMeetingDeadline;
		}
		else if(actualFinalSignature.equals("") && actualFinalIssuesToDas.equals(""))
		{
			nextOfficeDeadline = finalIssuesDueToDas;
		}
		else if(actualFinalSignature.equals("") && actualFinalConcurrenceToDas.equals(""))
		{
			nextOfficeDeadline = finalConcurrenceDueToDas;
		}
		else if(actualFinalSignature.equals(""))
		{
			nextOfficeDeadline = calculatedFinalSignature;
		}
		//IF published_date_c (type: Final) is blank AND Actual_Final_Signaturec is not blank THEN Calculated_Final_FR_signature_c
		else if(willYouAmendTheFinal.equalsIgnoreCase("Yes") && actualAmendedFinalSignature.equals("") 
				&& actualAmendFinalIssuesToDas.equals(""))
		{
			nextOfficeDeadline = amendFinalIssuesDueToDas; 
		}else if(willYouAmendTheFinal.equalsIgnoreCase("Yes") && actualAmendedFinalSignature.equals("") 
				&& actualAmendFinalConcurrenceToDas.equals(""))
		{
			nextOfficeDeadline = amendFinalConcurrenceDueToDas;
		}else if(willYouAmendTheFinal.equalsIgnoreCase("Yes") && actualAmendedFinalSignature.equals(""))
		{
			nextOfficeDeadline = calculatedAmendedFinalSignature;
		}
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Next Office Deadline"), "text");
		System.out.println(actualValue);
		allMatches = allMatches & compareAndReport("genericSegmentField", "Next Office Deadline", nextOfficeDeadline, 
				actualValue);		
		//Next Announcement Date
		String nextAnnouncementDate = "";
		if(!datePassed(preliminaryAnnouncementDate) && (segmentOutcome.equals("") || segmentOutcome.equalsIgnoreCase("Completed")))
		{
			nextAnnouncementDate = preliminaryAnnouncementDate;
		}
		else if (!datePassed(finalAnnouncementDate) && (segmentOutcome.equals("") || segmentOutcome.equalsIgnoreCase("Completed")))
		{
			nextAnnouncementDate = finalAnnouncementDate;
		}
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Next Announcement Date"), "text");
		System.out.println(actualValue);
		allMatches = allMatches & compareAndReport("genericSegmentField", "Next Announcement Date", nextAnnouncementDate, actualValue);		
		return allMatches;
	}
	
	/**
	 * This method validates new Anti-Circumvention review segment
	 * @return true if all dates matches, false if not
	 * @exception Exception
	*/
	public static boolean validateNewSegmentAntiCircumventionReview() throws Exception
	{
		boolean allMatches = true;
		String actualValue  = "" ;
		/////////////////////////////////////// //////////////////////////////////////////////////////////////
		//Final Extension (# of days)
		int finalExtensionDays = readNumberFromScreen(getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Final Extension (# of days)"), "text")) ;
		System.out.println(finalExtensionDays);
		//Initiation Extension (# of days)
		int initiationExtensionDays = readNumberFromScreen(getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Initiation Extension (# of days)"), "text")) ;
		System.out.println(finalExtensionDays);
		//Actual Final Signature
		String actualFinalSignature = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Final Signature"), "text");
		System.out.println(actualFinalSignature);
		String applicationAccepted = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Application Accepted"), "text");
		System.out.println(applicationAccepted);
		//Actual Preliminary Signature
		String actualPreliminarySignature = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Preliminary Signature"), "text");
		System.out.println(actualPreliminarySignature);
		//Segment Outcome
		String segmentOutcome = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Segment Outcome"), "text");
		System.out.println(segmentOutcome);
		//Actual Initiation Issues to DAS
		String actualInitiationIssuesToDas = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Initiation Issues to DAS"), "text");
		System.out.println(segmentOutcome);
		//Actual Initiation Concurrence to DAS
		String actualInitiationConcurrenceToDas = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Initiation Concurrence to DAS"), "text");
		System.out.println(segmentOutcome);
		//Actual Prelim Issues to DAS
		String actualPrelimIssuesToDas = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Prelim Issues to DAS"), "text");
		System.out.println(actualPrelimIssuesToDas);
		//Actual Prelim Concurrence to DAS
		String actualPrelimConcurrenceToDas = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Prelim Concurrence to DAS"), "text");
		System.out.println(actualPrelimConcurrenceToDas);
		//Actual Final Issues to DAS
		String actualFinalIssuesToDas = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Final Issues to DAS"), "text");
		System.out.println(actualFinalIssuesToDas);
		//Actual Final Concurrence to DAS
		String actualFinalConcurrenceToDas = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Final Concurrence to DAS"), "text");
		System.out.println(actualFinalConcurrenceToDas);
		//Actual Initiation Signature
		String actualInitiationSignature = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Initiation Signature"), "text");
		System.out.println(actualInitiationSignature);
		///////////////////////////////////////////
		scrollToElement(replaceGui(guiMap.get("genericSegmentField"),"Calculated Final Signature"));
		//Calculated Initiation Signature
		String calculatedInitiationSignature =  calculateDate(45+initiationExtensionDays, "Calculated Initiation Signature",
				"calendar",applicationAccepted);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Calculated Initiation Signature"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Calculated Initiation Signature", 
				calculatedInitiationSignature, actualValue);
		//Calculated Final Signature
		String calculatedFinalSignature =  calculateDate(300 + finalExtensionDays, "Calculated Final Signature", "calendar", 
				!actualInitiationSignature.equals("")?actualInitiationSignature:calculatedInitiationSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Calculated Final Signature"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Calculated Final Signature", 
				calculatedFinalSignature, actualValue);
		scrollToElement(replaceGui(guiMap.get("genericSegmentField"),"Final Announcement Date"));
		//Final Announcement Date
		String finalAnnouncementDate = calculateDate(1, "Final Announcement Date", "calendar", 
				!actualFinalSignature.equals("")?actualFinalSignature:calculatedFinalSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Final Announcement Date"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Final Announcement Date", 
				finalAnnouncementDate, actualValue);
		
		//Initiation Issues Due to DAS
		String initiationIssuesDueToDas = "";
		initiationIssuesDueToDas = calculateDate(-10, "Initiation Issues Due to DAS", "business", calculatedInitiationSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Initiation Issues Due to DAS"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Initiation Issues Due to DAS", 
				initiationIssuesDueToDas, actualValue);
		//Initiation Concurrence Due to DAS
		String initiationConcurrenceDueToDas = "";
		initiationConcurrenceDueToDas = calculateDate(-5, "Initiation Concurrence Due to DAS", "business", calculatedInitiationSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Initiation Concurrence Due to DAS"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Initiation Concurrence Due to DAS", 
				initiationConcurrenceDueToDas, actualValue);
		scrollToElement(replaceGui(guiMap.get("genericSegmentField"),"Calculated Final Signature"));
		//Calculated Preliminary Signature
		String calculatedPreliminarySignature = calculateDate(120, "Calculated Preliminary Signature", "calendar", 
				!actualInitiationSignature.equals("")?actualInitiationSignature:calculatedInitiationSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Calculated Preliminary Signature"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Calculated Preliminary Signature", 
				calculatedPreliminarySignature, actualValue);
		//Prelim Team Meeting Deadline
		String prelimTeamMeetingDeadline = calculateDate(-21, "Prelim Team Meeting Deadline", "calendar", calculatedPreliminarySignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Prelim Team Meeting Deadline"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Prelim Team Meeting Deadline", prelimTeamMeetingDeadline, 
				actualValue);
		//Prelim Issues Due to DAS
		String prelimIssuesDueToDas = calculateDate(-10, "Prelim Issues Due to DAS", "business", calculatedPreliminarySignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Prelim Issues Due to DAS"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Prelim Issues Due to DAS", prelimIssuesDueToDas,
				actualValue);
		//Prelim Concurrence Due to DAS
		String prelimConcurrenceDueToDas = calculateDate(-5, "Prelim Concurrence Due to DAS", "business", calculatedPreliminarySignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Prelim Concurrence Due to DAS"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Prelim Concurrence Due to DAS", prelimConcurrenceDueToDas,
				actualValue);
		//Final Team Meeting Deadline
		String finalTeamMeetingDeadline = calculateDate(-21, "Final Team Meeting Deadline", "calendar", calculatedFinalSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Final Team Meeting Deadline"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Final Team Meeting Deadline", finalTeamMeetingDeadline,
				actualValue);
		//Final Issues Due to DAS
		String finalIssuesDueToDas = calculateDate(-10, "Final Issues Due to DAS",  "business", calculatedFinalSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Final Issues Due to DAS"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Final Issues Due to DAS", finalIssuesDueToDas, 
				actualValue);
		//Final Concurrence Due to DAS
		String finalConcurrenceDueToDas = calculateDate(-5, "Final Concurrence Due to DAS",  "business", calculatedFinalSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Final Concurrence Due to DAS"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Final Concurrence Due to DAS", finalConcurrenceDueToDas,
				actualValue);
		//Next Major Deadline
		String nextMajorDeadline = "";
		String publishedDate="";
		if(actualInitiationSignature.equals("") && publishedDate.equals(""))
		{
			nextMajorDeadline = calculatedInitiationSignature;
		}
		else if(actualPreliminarySignature.equals("") && publishedDate.equals(""))
		{
			nextMajorDeadline = calculatedPreliminarySignature;
		}
		else if(actualFinalSignature.equals("") && publishedDate.equals(""))
		{
			nextMajorDeadline = calculatedFinalSignature;
		}
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericPetitionDate"),"Next Major Deadline"), "text");
		allMatches = allMatches &  compareAndReport("genericPetitionDate", "Next Major Deadline", nextMajorDeadline, 
				actualValue);
		//Next Due to DAS Deadline 
		String nextDueToDasDeadline = "";
		if(actualInitiationSignature.equals("") && actualInitiationIssuesToDas.equals(""))
		{
			nextDueToDasDeadline = initiationIssuesDueToDas;
		}
		else if(actualInitiationSignature.equals("") && actualInitiationConcurrenceToDas.equals(""))
		{
			nextDueToDasDeadline = initiationConcurrenceDueToDas;
		}
		else if(actualInitiationSignature.equals(""))
		{
			nextDueToDasDeadline = calculatedInitiationSignature;
		}
		else if(actualPreliminarySignature.equals("") && actualPrelimIssuesToDas.equals(""))
		{
			nextDueToDasDeadline = prelimIssuesDueToDas;
		}
		else if(actualPreliminarySignature.equals("") && actualPrelimConcurrenceToDas.equals(""))
		{
			nextDueToDasDeadline = prelimConcurrenceDueToDas;
		}
		else if(actualPreliminarySignature.equals("") && segmentOutcome.equals(""))
		{
			nextDueToDasDeadline = calculatedPreliminarySignature;
		}
		else if(actualFinalSignature.equals("") && actualFinalIssuesToDas.equals(""))
		{
			nextDueToDasDeadline = finalIssuesDueToDas;
		}
		else if(actualFinalSignature.equals("") && actualFinalConcurrenceToDas.equals(""))
		{
			nextDueToDasDeadline = finalConcurrenceDueToDas;
		}
		else if(actualFinalSignature.equals("") && segmentOutcome.equals(""))
		{
			nextDueToDasDeadline = calculatedFinalSignature;
		}
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericPetitionDate"),"Next Due to DAS Deadline"), "text");
		allMatches = allMatches &  compareAndReport("genericPetitionDate", "Next Due to DAS Deadline", nextDueToDasDeadline, 
				actualValue);
		//Next Office Deadline
		String nextOfficeDeadline = "";
		if(actualInitiationSignature.equals("") && actualInitiationIssuesToDas.equals(""))
		{
			nextOfficeDeadline = initiationIssuesDueToDas;
		}
		else if(actualInitiationSignature.equals("") && actualInitiationConcurrenceToDas.equals(""))
		{
			nextOfficeDeadline = initiationConcurrenceDueToDas;
		}
		else if(actualInitiationSignature.equals(""))
		{
			nextOfficeDeadline = calculatedInitiationSignature;
		}
		else if(actualPreliminarySignature.equals("") && !datePassed(prelimTeamMeetingDeadline))
		{
			nextOfficeDeadline = prelimTeamMeetingDeadline;
		}
		else if(actualPreliminarySignature.equals("") && actualPrelimIssuesToDas.equals(""))
		{
			nextOfficeDeadline = prelimIssuesDueToDas;
		}
		else if(actualPreliminarySignature.equals("") && actualPrelimConcurrenceToDas.equals(""))
		{
			nextOfficeDeadline = prelimConcurrenceDueToDas;
		}
		else if (actualPreliminarySignature.equals(""))
		{
			nextOfficeDeadline = calculatedPreliminarySignature;
		}
		else if (actualFinalSignature.equals("") && !datePassed(finalTeamMeetingDeadline))
		{
			nextOfficeDeadline = finalTeamMeetingDeadline;
		}
		else if(actualFinalSignature.equals("") && actualFinalIssuesToDas.equals(""))
		{
			nextOfficeDeadline = finalIssuesDueToDas;
		}
		else if(actualFinalSignature.equals("") && actualFinalConcurrenceToDas.equals(""))
		{
			nextOfficeDeadline = finalConcurrenceDueToDas;
		}
		else if(actualFinalSignature.equals(""))
		{
			nextOfficeDeadline = calculatedFinalSignature;
		}
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericPetitionDate"),"Next Office Deadline"), "text");
		allMatches = allMatches &  compareAndReport("genericPetitionDate", "Next Office Deadline", nextOfficeDeadline, 
				actualValue);
		//IF published_date_c (type: Final) is blank AND Actual_Final_Signaturec is not blank THEN Calculated_Final_FR_signature_c????
		//Preliminary Announcement Date
		String preliminaryAnnouncementDate = calculateDate(1, "Preliminary Announcement Date", "business",  
		!actualPreliminarySignature.equals("")?actualPreliminarySignature:calculatedPreliminarySignature);
		//Next Announcement Date
		String nextAnnouncementDate = "";
		if(!datePassed(preliminaryAnnouncementDate) && (segmentOutcome.equals("") || segmentOutcome.equalsIgnoreCase("Completed")))
		{
			nextAnnouncementDate = preliminaryAnnouncementDate;
		}
		else if (!datePassed(finalAnnouncementDate) && (segmentOutcome.equals("") || segmentOutcome.equalsIgnoreCase("Completed")))
		{
			nextAnnouncementDate = finalAnnouncementDate;
		}
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Next Announcement Date"), "text");
		System.out.println(actualValue);
		allMatches = allMatches & compareAndReport("genericSegmentField", "Next Announcement Date", nextAnnouncementDate, 
				actualValue);		
		return allMatches;
	}
	
	/**
	 * This method validates new Changed Circumstances review segment
	 * @return true if all dates matches, false if not
	 * @exception Exception
	*/
	public static boolean validateNewSegmentChangedCircumstancesReview() throws Exception
	{
		boolean allMatches = true;
		String actualValue  = "" ;
		/////////////////////////////////////// //////////////////////////////////////////////////////////////
		//Actual Final Signature
		String actualFinalSignature = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Final Signature"), "text");
		System.out.println(actualFinalSignature);
		//Actual Preliminary Signature
		String actualPreliminarySignature = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Preliminary Signature"), "text");
		System.out.println(actualPreliminarySignature);
		//Segment Outcome
		String segmentOutcome = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Segment Outcome"), "text");
		System.out.println(segmentOutcome);
		//Actual Initiation Issues to DAS
		//String actualInitiationIssuesToDas = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
			//	"Actual Initiation Issues to DAS"), "text");
		System.out.println(segmentOutcome);
		//Actual Initiation Concurrence to DAS
		//String actualInitiationConcurrenceToDas = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
			//	"Actual Initiation Concurrence to DAS"), "text");
		System.out.println(segmentOutcome);
		//Actual Prelim Issues to DAS
		String actualPrelimIssuesToDas = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Prelim Issues to DAS"), "text");
		System.out.println(actualPrelimIssuesToDas);
		//Actual Prelim Concurrence to DAS
		String actualPrelimConcurrenceToDas = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Prelim Concurrence to DAS"), "text");
		System.out.println(actualPrelimConcurrenceToDas);
		//Actual Final Issues to DAS
		String actualFinalIssuesToDas = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Final Issues to DAS"), "text");
		System.out.println(actualFinalIssuesToDas);
		//Actual Final Concurrence to DAS
		String actualFinalConcurrenceToDas = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Final Concurrence to DAS"), "text");
		System.out.println(actualFinalConcurrenceToDas);
		//Actual Initiation Signature
		String actualInitiationSignature = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Initiation Signature"), "text");
		System.out.println(actualInitiationSignature);
		//Request Filed
		String requestFiled = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Request Filed"), "text");
		System.out.println(requestFiled);
		//Final Extension (# of days)
		int finalExtensionDays = readNumberFromScreen(getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Final Extension (# of days)"), "text")) ;
		System.out.println(finalExtensionDays);
		//Initiation Extension (# of days)
		int initiationExtensionDays = readNumberFromScreen(getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Initiation Extension (# of days)"), "text")) ;
		System.out.println(finalExtensionDays);
		//Is this review expedited?
		String isThisReviewExpedited = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Is this review expedited?"), "text");
		System.out.println(isThisReviewExpedited);
		//All parties in agreement to the outcome?
		String allPartiesInAgreementToTheOutcome = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"All parties in agreement to the outcome?"), "text");
		System.out.println(allPartiesInAgreementToTheOutcome);
		//Prelim Extension (# of days)
		int prelimExtensionDays = readNumberFromScreen(getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Prelim Extension (# of days)"), "text")) ;
		System.out.println(prelimExtensionDays);
		////////////////////////////////////// //////////////////////////////////////////////////////////////
		scrollToElement(replaceGui(guiMap.get("genericSegmentField"),"Calculated Initiation Signature"));
		//Calculated Initiation Signature
		String calculatedInitiationSignature =  calculateDate(45+initiationExtensionDays, "Calculated Initiation Signature", 
				"calendar",requestFiled);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Calculated Initiation Signature"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Calculated Initiation Signature", 
				calculatedInitiationSignature, actualValue);
		//scrollToElement(replaceGui(guiMap.get("genericSegmentField"),"Calculated Final Signature"));
		//Calculated Preliminary Signature
		String calculatedPreliminarySignature = calculateDate(180 + prelimExtensionDays, "Calculated Preliminary Signature", "calendar", 
				!actualInitiationSignature.equals("")?actualInitiationSignature:calculatedInitiationSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Calculated Preliminary Signature"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Calculated Preliminary Signature", calculatedPreliminarySignature, 
				actualValue);
		//Prelim Team Meeting Deadline
		String prelimTeamMeetingDeadline = calculateDate(-21, "Prelim Team Meeting Deadline", "calendar", calculatedPreliminarySignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Prelim Team Meeting Deadline"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Prelim Team Meeting Deadline", prelimTeamMeetingDeadline, 
				actualValue);
		//Prelim Issues Due to DAS
		String prelimIssuesDueToDas = calculateDate(-10, "Prelim Issues Due to DAS", "business", calculatedPreliminarySignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Prelim Issues Due to DAS"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Prelim Issues Due to DAS", prelimIssuesDueToDas, 
				actualValue);
		//Prelim Concurrence Due to DAS
		String prelimConcurrenceDueToDas = calculateDate(-5, "Prelim Concurrence Due to DAS", "business", calculatedPreliminarySignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Prelim Concurrence Due to DAS"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Prelim Concurrence Due to DAS", prelimConcurrenceDueToDas, 
				actualValue);
		//Calculated Final Signature
		String calculatedFinalSignature = "";
		if(isThisReviewExpedited.equalsIgnoreCase("True") && actualInitiationSignature.equals("") 
				&& !calculatedInitiationSignature.equals(""))
		{
			calculatedFinalSignature  = calculateDate(45 + finalExtensionDays, "Calculated Final Signature", "calendar", 
					calculatedInitiationSignature);
		}
		else if(isThisReviewExpedited.equalsIgnoreCase("True") && !actualInitiationSignature.equals("") )
		{
			calculatedFinalSignature  = calculateDate(45 + finalExtensionDays, "Calculated Final Signature", 
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
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Calculated Final Signature"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Calculated Final Signature", 
				calculatedFinalSignature, actualValue);
		//Final Issues Due to DAS
		scrollToElement(replaceGui(guiMap.get("genericSegmentField"),"Final Issues Due to DAS"));
		String finalIssuesDueToDas = calculateDate(-10, "Final Issues Due to DAS",  "business", calculatedFinalSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Final Issues Due to DAS"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Final Issues Due to DAS", finalIssuesDueToDas, actualValue);
		//Final Concurrence Due to DAS
		scrollToElement(replaceGui(guiMap.get("genericSegmentField"),"Final Concurrence Due to DAS"));
		String finalConcurrenceDueToDas = calculateDate(-5, "Final Concurrence Due to DAS",  "business", calculatedFinalSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Final Concurrence Due to DAS"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Final Concurrence Due to DAS", 
				finalConcurrenceDueToDas, actualValue);
		//Final Team Meeting Deadline
		String finalTeamMeetingDeadline = calculateDate(-21, "Final Team Meeting Deadline", "calendar", calculatedFinalSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Final Team Meeting Deadline"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Final Team Meeting Deadline",
				finalTeamMeetingDeadline, actualValue);
		//Final Announcement Date
		String finalAnnouncementDate = calculateDate(1, "Final Announcement Date", "calendar", 
				!actualFinalSignature.equals("")?actualFinalSignature:calculatedFinalSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Final Announcement Date"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Final Announcement Date", 
				finalAnnouncementDate, actualValue);
		scrollToElement(replaceGui(guiMap.get("genericSegmentField"),"Final Team Meeting Deadline"));
		//Next Major Deadline
		String nextMajorDeadline = "";
		String publishedDate="";
		if(actualInitiationSignature.equals("") && publishedDate.equals(""))
		{
			nextMajorDeadline = calculatedInitiationSignature;
		}
		else if(actualPreliminarySignature.equals("") && publishedDate.equals(""))
		{
			nextMajorDeadline = calculatedPreliminarySignature;
		}
		else if(actualFinalSignature.equals("") && publishedDate.equals(""))
		{
			nextMajorDeadline = calculatedFinalSignature;
		}
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericPetitionDate"),
				"Next Major Deadline"), "text");
		allMatches = allMatches &  compareAndReport("genericPetitionDate", "Next Major Deadline",
				nextMajorDeadline, actualValue);
		//Next Due to DAS Deadline ???????????????
		
		//Next Office Deadline ??????????????????
		
		//Preliminary Announcement Date
		String preliminaryAnnouncementDate = calculateDate(1, "Preliminary Announcement Date", "business",  
		!actualPreliminarySignature.equals("")?actualPreliminarySignature:calculatedPreliminarySignature);
		//Next Announcement Date
		String nextAnnouncementDate = "";
		if(!datePassed(preliminaryAnnouncementDate) && (segmentOutcome.equals("") || 
				segmentOutcome.equalsIgnoreCase("Completed")))
		{
			nextAnnouncementDate = preliminaryAnnouncementDate;
		}
		else if (!datePassed(finalAnnouncementDate) && (segmentOutcome.equals("") || 
				segmentOutcome.equalsIgnoreCase("Completed")))
		{
			nextAnnouncementDate = finalAnnouncementDate;
		}
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Next Announcement Date"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Next Announcement Date", 
				nextAnnouncementDate, actualValue);		
		return allMatches;
	}
	
	/**
	 * This method validates new Expedited review segment
	 * @return true if all dates matches, false if not
	 * @exception Exception
	*/
	public static boolean validateNewSegmentExpeditedReview() throws Exception
	{
		boolean allMatches = true;
		String actualValue  = "" ;
		//Final Extension (# of days)
		int finalExtensionDays = readNumberFromScreen(getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Final Extension (# of days)"), "text")) ;
		System.out.println(finalExtensionDays);
		//Actual Initiation Signature
		String actualInitiationSignature = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Initiation Signature"), "text");
		System.out.println(actualInitiationSignature);
		//Calculated Initiation Signature
		String calculatedInitiationSignature = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Calculated Initiation Signature"), "text");
		System.out.println(calculatedInitiationSignature);
		//Actual Preliminary Signature
		String actualPreliminarySignature = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Preliminary Signature"), "text");
		System.out.println(actualPreliminarySignature);
		String actualfinalSignature = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Actual Final Signature"), "text");
		System.out.println(actualfinalSignature);
		String calculatedAmendedFinalSignature = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Calculated Amended Final Signature"), "text");
		String actualAmendedFinalSignature = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Actual Amended Final Signature"), "text");
		System.out.println(calculatedAmendedFinalSignature);
		String willYouAmendTheFinal = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Will you Amend the Final?"), "text");
		System.out.println(willYouAmendTheFinal);
		//Actual Final Issues to DAS
		String actualFinalIssuesToDas = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Actual Final Issues to DAS"), "text");
		System.out.println(actualFinalIssuesToDas);
		String actualFinalConcurrenceToDas = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				"Actual Final Concurrence to DAS"), "text");
		System.out.println(actualFinalConcurrenceToDas);
		//Segment Outcome
		String segmentOutcome = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Segment Outcome"), "text");
		System.out.println(segmentOutcome);
		String actualAmendFinalIssuesToDas = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Amend Final Issues to DAS"), "text");
		System.out.println(actualAmendFinalIssuesToDas);
		String amendFinalIssuesDueToDas = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Amend Final Issues Due to DAS"), "text");
		System.out.println(amendFinalIssuesDueToDas); 
		String actualAmendFinalConcurrenceToDas = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Amend Final Concurrence to DAS"), "text");
		System.out.println(actualAmendFinalConcurrenceToDas);
		String amendFinalConcurrenceDueToDas = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Amend Final Concurrence Due to DAS"), "text");
		System.out.println(amendFinalConcurrenceDueToDas); 
		String calculatedFinalFrSignature = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Amend Final Concurrence Due to DAS"), "text");
		System.out.println(calculatedFinalFrSignature); 
		//Actual Final Signature
		String actualFinalSignature = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Final Signature"), "text");
		System.out.println(calculatedFinalFrSignature);
		//Actual Prelim Issues to DAS
		String actualPrelimIssuesToDas = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Prelim Issues to DAS"), "text");
		System.out.println(actualPrelimIssuesToDas); 
		//Actual Prelim Concurrence to DAS
		String actualPrelimConcurrenceToDas = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Prelim Concurrence to DAS"), "text");
		System.out.println(actualPrelimConcurrenceToDas); 
		/////////////////////////////////////// //////////////////////////////////////////////////////////////
		scrollToElement(replaceGui(guiMap.get("genericSegmentField"),"Amend Final Issues Due to DAS"));
		//Calculated Preliminary Signature
		String calculatedPreliminarySignature = calculateDate(180, "Calculated Preliminary Signature", "calendar", 
				!actualInitiationSignature.equals("")?actualInitiationSignature:calculatedInitiationSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Calculated Preliminary Signature"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Calculated Preliminary Signature", 
				calculatedPreliminarySignature, actualValue);
		//Prelim Team Meeting Deadline
		String prelimTeamMeetingDeadline = calculateDate(-21, "Prelim Team Meeting Deadline", "calendar", 
				calculatedPreliminarySignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Prelim Team Meeting Deadline"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Prelim Team Meeting Deadline", 
				prelimTeamMeetingDeadline, actualValue);
		//Prelim Issues Due to DAS
		String prelimIssuesDueToDas = calculateDate(-10, "Prelim Issues Due to DAS", "business", 
				calculatedPreliminarySignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Prelim Issues Due to DAS"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Prelim Issues Due to DAS", 
				prelimIssuesDueToDas, actualValue);
		//Prelim Concurrence Due to DAS
		String prelimConcurrenceDueToDas = calculateDate(-5, "Prelim Concurrence Due to DAS", "business", 
				calculatedPreliminarySignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Prelim Concurrence Due to DAS"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Prelim Concurrence Due to DAS", 
				prelimConcurrenceDueToDas, actualValue);
		//Preliminary Announcement Date
		String preliminaryAnnouncementDate = calculateDate(1, "Preliminary Announcement Date", "business", 
				!actualPreliminarySignature.equals("")?actualPreliminarySignature:calculatedPreliminarySignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Preliminary Announcement Date"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Preliminary Announcement Date",
				preliminaryAnnouncementDate, actualValue);
		scrollToElement(replaceGui(guiMap.get("genericSegmentField"),"Calculated Final Signature"));
		//Calculated Final Signature
		String calculatedFinalSignature = calculateDate(90 + finalExtensionDays, "Calculated Final Signature", "Calendar",
				!actualPreliminarySignature.equals("")?actualPreliminarySignature:calculatedPreliminarySignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Calculated Final Signature"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Calculated Final Signature", 
				calculatedFinalSignature, actualValue);
		//Final Team Meeting Deadline
		String finalTeamMeetingDeadline = calculateDate(-21, "Final Team Meeting Deadline", "calendar", 
				calculatedFinalSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Final Team Meeting Deadline"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Final Team Meeting Deadline", 
				finalTeamMeetingDeadline, actualValue);
		//Final Issues Due to DAS
		String finalIssuesDueToDas = calculateDate(-10, "Final Issues Due to DAS",  "business", calculatedFinalSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Final Issues Due to DAS"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Final Issues Due to DAS", finalIssuesDueToDas, actualValue);
		//Final Concurrence Due to DAS
		String finalConcurrenceDueToDas = calculateDate(-5, "Final Concurrence Due to DAS",  "business", calculatedFinalSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Final Concurrence Due to DAS"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Final Concurrence Due to DAS", 
				finalConcurrenceDueToDas, actualValue);
		//Final Announcement Date
		String finalAnnouncementDate = calculateDate(1, "Final Announcement Date", "business", calculatedFinalSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Final Announcement Date"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Final Announcement Date", finalAnnouncementDate, actualValue);
		//Next Major Deadline
		String nextMajorDeadline = "";
		String publishedDate="";
		if (actualPreliminarySignature.equals("") && publishedDate.equals(""))
		{
			nextMajorDeadline = calculatedPreliminarySignature;
		}else if (actualFinalSignature.equals(""))
		{
			nextMajorDeadline = calculatedFinalSignature;
		}else if (actualAmendedFinalSignature.equals("") && publishedDate.equals("") && willYouAmendTheFinal.equalsIgnoreCase("Yes"))
		{
			nextMajorDeadline = calculatedAmendedFinalSignature;
		}
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Next Major Deadline"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Next Major Deadline", nextMajorDeadline, actualValue);
		//Next Due to DAS Deadline 
		String nextDueToDasDeadline = "";
		if(actualPreliminarySignature.equals("") && actualPrelimIssuesToDas.equals(""))
		{
			nextDueToDasDeadline = prelimIssuesDueToDas;
		}else if(actualPreliminarySignature.equals("") && actualPrelimConcurrenceToDas.equals(""))
		{
			nextDueToDasDeadline = prelimConcurrenceDueToDas;
		}else if(actualPreliminarySignature.equals("") && segmentOutcome.equals(""))								
		{
			nextDueToDasDeadline = calculatedPreliminarySignature;
		}else if(actualFinalSignature.equals("") && actualFinalIssuesToDas.equals(""))
		{
			nextDueToDasDeadline = finalIssuesDueToDas;
		}else if(actualFinalSignature.equals("") && actualFinalConcurrenceToDas.equals(""))
		{
			nextDueToDasDeadline = finalConcurrenceDueToDas;
		}else if(actualFinalSignature.equals("") && segmentOutcome.equals(""))
		{
			nextDueToDasDeadline = calculatedFinalSignature;
		}else if(willYouAmendTheFinal.equalsIgnoreCase("Yes") && actualAmendedFinalSignature.equals("") 
				&& actualAmendFinalIssuesToDas.equals(""))
		{
			nextDueToDasDeadline = amendFinalIssuesDueToDas; 
		}else if(willYouAmendTheFinal.equalsIgnoreCase("Yes") && actualAmendedFinalSignature.equals("") 
				&& actualAmendFinalConcurrenceToDas.equals(""))
		{
			nextDueToDasDeadline = amendFinalConcurrenceDueToDas;
		}else if(willYouAmendTheFinal.equalsIgnoreCase("Yes") && actualAmendedFinalSignature.equals(""))
		{
			nextDueToDasDeadline = calculatedAmendedFinalSignature;
		}
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Next Due to DAS Deadline"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Next Due to DAS Deadline", 
				nextDueToDasDeadline, actualValue);
		//Next Office Deadline
		String nextOfficeDeadline = "";
		if(actualPreliminarySignature.equals("") && !datePassed(prelimTeamMeetingDeadline))
		{
			nextOfficeDeadline = prelimTeamMeetingDeadline;
		}
		else if(actualPreliminarySignature.equals("") && actualPrelimIssuesToDas.equals(""))
		{
			nextOfficeDeadline = prelimIssuesDueToDas;
		}
		else if(actualPreliminarySignature.equals("") && actualPrelimConcurrenceToDas.equals(""))
		{
			nextOfficeDeadline = prelimConcurrenceDueToDas;
		}
		else if(actualPreliminarySignature.equals(""))
		{
			nextOfficeDeadline = calculatedPreliminarySignature;
		}
		else if (actualFinalSignature.equals("") && !datePassed(finalTeamMeetingDeadline) )
		{
			nextOfficeDeadline = finalTeamMeetingDeadline;
		}
		else if(actualFinalSignature.equals("") && actualFinalIssuesToDas.equals(""))
		{
			nextOfficeDeadline = finalIssuesDueToDas;
		}
		else if(actualFinalSignature.equals("") && actualFinalConcurrenceToDas.equals(""))
		{
			nextOfficeDeadline = finalConcurrenceDueToDas;
		}
		else if(actualFinalSignature.equals(""))
		{
			nextOfficeDeadline = calculatedFinalSignature;
		}
		//IF published_date_c (type: Final) is blank AND Actual_Final_Signaturec is not blank THEN Calculated_Final_FR_signature_c
		else if(willYouAmendTheFinal.equalsIgnoreCase("Yes") && actualAmendedFinalSignature.equals("") 
				&& actualAmendFinalIssuesToDas.equals(""))
		{
			nextOfficeDeadline = amendFinalIssuesDueToDas; 
		}else if(willYouAmendTheFinal.equalsIgnoreCase("Yes") && actualAmendedFinalSignature.equals("") 
				&& actualAmendFinalConcurrenceToDas.equals(""))
		{
			nextOfficeDeadline = amendFinalConcurrenceDueToDas;
		}else if(willYouAmendTheFinal.equalsIgnoreCase("Yes") && actualAmendedFinalSignature.equals(""))
		{
			nextOfficeDeadline = calculatedAmendedFinalSignature;
		}
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Next Office Deadline"), "text");
		System.out.println(actualValue);
		allMatches = allMatches & compareAndReport("genericSegmentField", "Next Office Deadline", 
				nextOfficeDeadline, actualValue);		
		//Next Announcement Date
		String nextAnnouncementDate = "";
		if(!datePassed(preliminaryAnnouncementDate) && (segmentOutcome.equals("") || segmentOutcome.equalsIgnoreCase("Completed")))
		{
			nextAnnouncementDate = preliminaryAnnouncementDate;
		}
		else if (!datePassed(finalAnnouncementDate) && (segmentOutcome.equals("") || segmentOutcome.equalsIgnoreCase("Completed")))
		{
			nextAnnouncementDate = finalAnnouncementDate;
		}
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Next Announcement Date"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Next Announcement Date", 
				nextAnnouncementDate, actualValue);		
		return allMatches;
	}
	
	/**
	 * This method validates new Shipper review segment
	 * @return true if all dates matches, false if not
	 * @exception Exception
	*/
	public static boolean validateNewSegmentNewShipperReview() throws Exception
	{
		boolean allMatches = true;
		String actualValue  = "" ;

		///////////////////////////////////////////------------------------+++++++++++++++++++++++++++++++
		//Segment Outcome
		String segmentOutcome = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Segment Outcome"), "text");
		System.out.println(segmentOutcome);
		//Actual Preliminary Signature
		String actualPreliminarySignature = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Preliminary Signature"), "text");
		System.out.println(actualPreliminarySignature);
		//Actual Initiation Signature
		String actualInitiationSignature = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Initiation Signature"), "text");
		System.out.println(actualInitiationSignature);
		//Actual Final Signature
		String actualFinalSignature = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Final Signature"), "text");
		System.out.println(actualFinalSignature);		
		//Calculated Initiation Signature
		String calculatedInitiationSignature = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Calculated Initiation Signature"), "text");
		System.out.println(calculatedInitiationSignature);
		//Final Extension (# of days)
		int finalExtensionDays = readNumberFromScreen(getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Final Extension (# of days)"), "text")) ;
		System.out.println(finalExtensionDays);
		//Initiation Extension (# of days)
		//int initiationExtensionDays = readNumberFromScreen(getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
		//		"Initiation Extension (# of days)"), "text")) ;
		System.out.println(finalExtensionDays);
		//Prelim Extension (# of days)
		int prelimExtensionDays = readNumberFromScreen(getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Prelim Extension (# of days)"), "text")) ;
		System.out.println(prelimExtensionDays);
		///////////////////////////////////////////////////////////////
		scrollToElement(replaceGui(guiMap.get("genericSegmentField"),"Prelim Team Meeting Deadline"));
		//Calculated Preliminary Signature
		String calculatedPreliminarySignature = calculateDate(180+prelimExtensionDays, "Calculated Preliminary Signature", "calendar", 
				!actualInitiationSignature.equals("")?actualInitiationSignature:calculatedInitiationSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Calculated Preliminary Signature"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Calculated Preliminary Signature", 
				calculatedPreliminarySignature, actualValue);
		//Prelim Team Meeting Deadline
		String prelimTeamMeetingDeadline = calculateDate(-21, "Prelim Team Meeting Deadline", "calendar", 
				calculatedPreliminarySignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Prelim Team Meeting Deadline"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Prelim Team Meeting Deadline", 
				prelimTeamMeetingDeadline, actualValue);
		//Prelim Issues Due to DAS
		String prelimIssuesDueToDas = calculateDate(-10, "Prelim Issues Due to DAS", "business", calculatedPreliminarySignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Prelim Issues Due to DAS"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Prelim Issues Due to DAS", 
				prelimIssuesDueToDas, actualValue);
		//Prelim Concurrence Due to DAS
		String prelimConcurrenceDueToDas = calculateDate(-5, "Prelim Concurrence Due to DAS", "business", 
				calculatedPreliminarySignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Prelim Concurrence Due to DAS"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Prelim Concurrence Due to DAS",
				prelimConcurrenceDueToDas, actualValue);
		//Calculated Final Signature
		String calculatedFinalSignature = calculateDate(90 + finalExtensionDays, "Calculated Final Signature", "Calendar",
				!actualPreliminarySignature.equals("")?actualPreliminarySignature:calculatedPreliminarySignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Calculated Final Signature"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Calculated Final Signature", 
				calculatedFinalSignature, actualValue);
		//Final Issues Due to DAS
		String finalIssuesDueToDas = calculateDate(-10, "Final Issues Due to DAS",  "business",
				calculatedFinalSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Final Issues Due to DAS"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Final Issues Due to DAS",
				finalIssuesDueToDas, actualValue);
		//Final Concurrence Due to DAS
		String finalConcurrenceDueToDas = calculateDate(-5, "Final Concurrence Due to DAS",  "business", 
				calculatedFinalSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Final Concurrence Due to DAS"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Final Concurrence Due to DAS", 
				finalConcurrenceDueToDas, actualValue);
		scrollToElement(replaceGui(guiMap.get("genericSegmentField"),"Final Team Meeting Deadline"));
		//Final Team Meeting Deadline
		String finalTeamMeetingDeadline = calculateDate(-21, "Final Team Meeting Deadline", "calendar", 
				calculatedFinalSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Final Team Meeting Deadline"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Final Team Meeting Deadline", 
				finalTeamMeetingDeadline, actualValue);
		scrollToElement(replaceGui(guiMap.get("genericSegmentField"),"Final Concurrence Due to DAS"));
		//Final Announcement Date
		String finalAnnouncementDate = calculateDate(1, "Final Announcement Date", "calendar", 
				!actualFinalSignature.equals("")?actualFinalSignature:calculatedFinalSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Final Announcement Date"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Final Announcement Date", 
				finalAnnouncementDate, actualValue);
		//Next Major Deadline		
		//Next Due to DAS Deadline 
		//Next Office Deadline
		//Preliminary Announcement Date
		String preliminaryAnnouncementDate = calculateDate(1, "Preliminary Announcement Date", "business",  
		!actualPreliminarySignature.equals("")?actualPreliminarySignature:calculatedPreliminarySignature);
		//Next Announcement Date
		String nextAnnouncementDate = "";
		if(!datePassed(preliminaryAnnouncementDate) && (segmentOutcome.equals("") || 
				segmentOutcome.equalsIgnoreCase("Completed")))
		{
			nextAnnouncementDate = preliminaryAnnouncementDate;
		}
		else if (!datePassed(finalAnnouncementDate) && (segmentOutcome.equals("") || 
				segmentOutcome.equalsIgnoreCase("Completed")))
		{
			nextAnnouncementDate = finalAnnouncementDate;
		}
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Next Announcement Date"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Next Announcement Date", 
				nextAnnouncementDate, actualValue);		
		return allMatches;
	}
	
	/**
	 * This method validates new Scope Inquiry review segment
	 * @return true if all dates matches, false if not
	 * @exception Exception
	*/
	public static boolean validateNewSegmentNewScoprInquiry() throws Exception
	{
		boolean allMatches = true;
		String actualValue  = "" ;
		//Actual Initiation Signature
		//String actualInitiationSignature = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Actual Initiation Signature"), "text");
		//System.out.println(actualInitiationSignature);
		//Actual Final Signature
		String actualFinalSignature = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Final Signature"), "text");
		System.out.println(actualFinalSignature);	
		//Actual Preliminary Signature
		String actualPreliminarySignature = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Preliminary Signature"), "text");
		System.out.println(actualPreliminarySignature);
		//Initiation Extension (# of days)
		int initiationExtensionDays = readNumberFromScreen(getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Initiation Extension (# of days)"), "text")) ;
				System.out.println(initiationExtensionDays);
		//Final Extension (# of days)
		int finalExtensionDays = readNumberFromScreen(getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Final Extension (# of days)"), "text")) ;
		System.out.println(finalExtensionDays);
		//Prelim Extension (# of days)
		int prelimExtensionDays = readNumberFromScreen(getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Prelim Extension (# of days)"), "text")) ;
		System.out.println(prelimExtensionDays);
		//Decision on How to Proceed
		String decisionOnHowToProceed = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Decision on How to Proceed"), "text");
		System.out.println(decisionOnHowToProceed);
		//Actual date of Decision - How to Proceed
		String actualDateOfDecisionOnHowToProceed = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual date of Decision - How to Proceed"), "text");
		System.out.println(actualDateOfDecisionOnHowToProceed);
		//Deadline for Decision on How to Proceed
		String deadlineForDecisionOnHowToProceed = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Deadline for Decision on How to Proceed"), "text");
		System.out.println(deadlineForDecisionOnHowToProceed);
		//Actual Decision on HOP Issues to DAS
		String actualDecisionOnHopIssuesToDas = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Decision on HOP Issues to DAS"), "text");
		System.out.println(actualDecisionOnHopIssuesToDas);
		//Actual Decision on HOP Concurrence toDAS
		String actualDecisionOnHopConcurrenceToDas = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Decision on HOP Concurrence toDAS"), "text");
		System.out.println(actualDecisionOnHopConcurrenceToDas);
		//Decision on HOP Issues Due to DAS
		String decisionOnHopIssuesDueToDas = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Decision on HOP Issues Due to DAS"), "text");
		System.out.println(decisionOnHopIssuesDueToDas);
		//Decision on HOP Concurrence Due to DAS
		String decisionOnHopConcurrenceDueToDas = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Decision on HOP Concurrence Due to DAS"), "text");
		System.out.println(decisionOnHopConcurrenceDueToDas);
		//Actual Prelim Issues to DAS
		String actualPrelimIssuesToDas = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Prelim Issues to DAS"), "text");
		System.out.println(actualPrelimIssuesToDas);
		//Actual Prelim Concurrence to DAS
		String actualPrelimConcurrenceToDas = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Prelim Concurrence to DAS"), "text");
		System.out.println(actualPrelimConcurrenceToDas);
		//Segment Outcome
		String segmentOutcome = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Segment Outcome"), "text");
		System.out.println(segmentOutcome);
		//Actual Final Issues to DAS
		String actualFinalIssuesToDas = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Final Issues to DAS"), "text");
		System.out.println(actualFinalIssuesToDas);
		//Actual Final Concurrence to DAS
		String actualFinalConcurrenceToDas = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Actual Final Concurrence to DAS"), "text");
		System.out.println(actualFinalConcurrenceToDas);
		////////////////////////////////////////////////////////////
		//Calculated Preliminary Signature
		scrollToElement(replaceGui(guiMap.get("genericSegmentField"),"Calculated Preliminary Signature"));
		String calculatedPreliminarySignature = "";
		if(decisionOnHowToProceed.equalsIgnoreCase("Formal") && !actualDateOfDecisionOnHowToProceed.equals("") )
		{
			calculatedPreliminarySignature = calculateDate(75, "Calculated Preliminary Signature", "calendar", 
					actualDateOfDecisionOnHowToProceed);
		}
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Calculated Preliminary Signature"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Calculated Preliminary Signature", 
				calculatedPreliminarySignature, actualValue);
		//Prelim Team Meeting Deadline
		String prelimTeamMeetingDeadline = "";
		if(decisionOnHowToProceed.equalsIgnoreCase("Formal"))
		{
			prelimTeamMeetingDeadline = calculateDate(-21, "Prelim Team Meeting Deadline", "calendar",
					calculatedPreliminarySignature);
		}
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Prelim Team Meeting Deadline"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Prelim Team Meeting Deadline", 
				prelimTeamMeetingDeadline, actualValue);
		//Prelim Issues Due to DAS
		String prelimIssuesDueToDas = calculateDate(-10, "Prelim Issues Due to DAS", "business", 
				calculatedPreliminarySignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Prelim Issues Due to DAS"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Prelim Issues Due to DAS", 
				prelimIssuesDueToDas, actualValue);
		//Prelim Concurrence Due to DAS
		String prelimConcurrenceDueToDas = calculateDate(-5, "Prelim Concurrence Due to DAS", "business", 
				calculatedPreliminarySignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Prelim Concurrence Due to DAS"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Prelim Concurrence Due to DAS", 
				prelimConcurrenceDueToDas, actualValue);		
		//Calculated Final Signature
		scrollToElement(replaceGui(guiMap.get("genericSegmentField"),"Final Team Meeting Deadline"));
		String calculatedFinalSignature = "";
		if(decisionOnHowToProceed.equalsIgnoreCase("Formal"))
		{
			calculatedFinalSignature = calculateDate(120 + finalExtensionDays, "Calculated Final Signature", "Calendar", 
					actualDateOfDecisionOnHowToProceed);
		}
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Calculated Final Signature"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Calculated Final Signature",
				calculatedFinalSignature, actualValue);
		//Final Team Meeting Deadline
		String finalTeamMeetingDeadline = "";
		if(decisionOnHowToProceed.equalsIgnoreCase("Formal"))
		{
			finalTeamMeetingDeadline = calculateDate(-21, "Final Team Meeting Deadline", "calendar", calculatedFinalSignature);
		}
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Final Team Meeting Deadline"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Final Team Meeting Deadline",
				finalTeamMeetingDeadline, actualValue);
		//Final Issues Due to DAS
		String finalIssuesDueToDas = "";
		if(decisionOnHowToProceed.equalsIgnoreCase("Formal"))
		{
			finalIssuesDueToDas = calculateDate(-10, "Final Issues Due to DAS",  "business", calculatedFinalSignature);
		}
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Final Issues Due to DAS"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Final Issues Due to DAS", 
				finalIssuesDueToDas, actualValue);
		//Final Concurrence Due to DAS
		String finalConcurrenceDueToDas = "";
		if(decisionOnHowToProceed.equalsIgnoreCase("Formal"))
		{
			finalConcurrenceDueToDas = calculateDate(-5, "Final Concurrence Due to DAS",  "business", calculatedFinalSignature);
		}
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Final Concurrence Due to DAS"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Final Concurrence Due to DAS", 
				finalConcurrenceDueToDas, actualValue);
		//Next Major Deadline
		scrollToElement(replaceGui(guiMap.get("genericSegmentField"),"Next Major Deadline"));
		String nextMajorDeadline = "";
		String publishedDate = "";
		if(decisionOnHowToProceed.equalsIgnoreCase(""))
		{
			nextMajorDeadline = deadlineForDecisionOnHowToProceed;
		}
		else if(decisionOnHowToProceed.equalsIgnoreCase("Formal") && publishedDate.equals(""))
		{
			nextMajorDeadline = calculatedPreliminarySignature;
		}else if (actualPreliminarySignature.equals("") && publishedDate.equals(""))
		{
			nextMajorDeadline = calculatedFinalSignature;
		}
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Next Major Deadline"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Next Major Deadline", nextMajorDeadline, actualValue);
		//Next Due to DAS Deadline 
		
		String nextDueToDasDeadline = "";
		if(actualDateOfDecisionOnHowToProceed.equals("") && actualDecisionOnHopIssuesToDas.equals(""))
		{
			nextDueToDasDeadline = decisionOnHopIssuesDueToDas;
		}
		else if(actualDateOfDecisionOnHowToProceed.equals("") && actualDecisionOnHopConcurrenceToDas.equals(""))
		{
			nextDueToDasDeadline = decisionOnHopConcurrenceDueToDas;
		}
		else if(actualDateOfDecisionOnHowToProceed.equals(""))
		{
			nextDueToDasDeadline = deadlineForDecisionOnHowToProceed;
		}
		else if (actualPreliminarySignature.equals("")&& actualPrelimIssuesToDas.equals(""))
		{
			nextDueToDasDeadline = prelimIssuesDueToDas;
		}
		else if (actualPreliminarySignature.equals("")&& actualPrelimConcurrenceToDas.equals(""))
		{
			nextDueToDasDeadline = prelimConcurrenceDueToDas;
		}
		else if (actualPreliminarySignature.equals("") && segmentOutcome.equals(""))
		{
			nextDueToDasDeadline = calculatedPreliminarySignature;
		}
		
		else if (actualFinalSignature.equals("")&& actualFinalIssuesToDas.equals(""))
		{
			nextDueToDasDeadline = finalIssuesDueToDas;
		}
		else if (actualFinalSignature.equals("")&& actualFinalConcurrenceToDas.equals(""))
		{
			nextDueToDasDeadline = finalConcurrenceDueToDas;
		}
		else if (actualFinalSignature.equals("") && actualFinalConcurrenceToDas.equals("")
				&& decisionOnHowToProceed.equalsIgnoreCase("Formal"))
		{
			nextDueToDasDeadline = calculatedFinalSignature;
		}
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),
				"Next Due to DAS Deadline"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Next Due to DAS Deadline", 
				nextDueToDasDeadline, actualValue);
		
		//Next Office Deadline
		String nextOfficeDeadline = "";
		if(actualDateOfDecisionOnHowToProceed.equals("") && actualDecisionOnHopIssuesToDas.equals("") 
				&& segmentOutcome.equals("") )
		{
			nextOfficeDeadline = decisionOnHopIssuesDueToDas;
		}
		else if(actualDateOfDecisionOnHowToProceed.equals("") && actualDecisionOnHopConcurrenceToDas.equals(""))
		{
			nextOfficeDeadline = decisionOnHopConcurrenceDueToDas;
		}
		else if(actualDateOfDecisionOnHowToProceed.equals(""))
		{
			nextOfficeDeadline = deadlineForDecisionOnHowToProceed;
		}
		else if(actualPreliminarySignature.equals("") && ! datePassed(prelimTeamMeetingDeadline) )
		{
			nextOfficeDeadline = prelimTeamMeetingDeadline;
		}
		else if (actualPreliminarySignature.equals("")&& actualPrelimIssuesToDas.equals(""))
		{
			nextOfficeDeadline = prelimIssuesDueToDas;
		}
		else if (actualPreliminarySignature.equals("")&& actualPrelimConcurrenceToDas.equals(""))
		{
			nextOfficeDeadline = prelimConcurrenceDueToDas;
		}
		else if (actualPreliminarySignature.equals("") && segmentOutcome.equals(""))
		{
			nextOfficeDeadline = calculatedPreliminarySignature;
		}
		else if (actualFinalSignature.equals("") && ! datePassed(finalTeamMeetingDeadline))
		{
			nextOfficeDeadline = finalTeamMeetingDeadline;
		}
		else if (actualFinalSignature.equals("")&& actualFinalIssuesToDas.equals(""))
		{
			nextOfficeDeadline = finalIssuesDueToDas;
		}
		else if (actualFinalSignature.equals("")&& actualFinalConcurrenceToDas.equals(""))
		{
			nextOfficeDeadline = finalConcurrenceDueToDas;
		}
		else if (actualFinalSignature.equals("") && actualFinalConcurrenceToDas.equals("") 
				&& decisionOnHowToProceed.equalsIgnoreCase("Formal"))
		{
			nextOfficeDeadline = calculatedFinalSignature;
		}
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Next Office Deadline"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Next Office Deadline", nextOfficeDeadline, actualValue);
		//Preliminary Announcement Date
		String preliminaryAnnouncementDate = calculateDate(1, "Preliminary Announcement Date", "business",  
		!actualPreliminarySignature.equals("")?actualPreliminarySignature:calculatedPreliminarySignature);
		//Final Announcement Date
		String finalAnnouncementDate = calculateDate(1, "Final Announcement Date", "calendar", 
				!actualFinalSignature.equals("")?actualFinalSignature:calculatedFinalSignature);
		//Next Announcement Date
		String nextAnnouncementDate = "";
		if(!datePassed(preliminaryAnnouncementDate) && (segmentOutcome.equals("") || segmentOutcome.equalsIgnoreCase("Completed")))
		{
			nextAnnouncementDate = preliminaryAnnouncementDate;
		}
		else if (!datePassed(finalAnnouncementDate) && (segmentOutcome.equals("") || segmentOutcome.equalsIgnoreCase("Completed")))
		{
			nextAnnouncementDate = finalAnnouncementDate;
		}
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentField"),"Next Announcement Date"), "text");
		allMatches = allMatches & compareAndReport("genericSegmentField", "Next Announcement Date", nextAnnouncementDate, actualValue);		
		return allMatches;
	}
	
	/**
	 * This method validates new Scope Inquiry review segment
	 * @return true if all dates matches, false if not
	 * @exception Exception
	*/
	public static boolean validateNewSegmentSunsetReview(LinkedHashMap<String, String> row) throws Exception
	{
		boolean allMatches = true;
		clickElementJs(guiMap.get("objectFRs"));
		clickElementJs(guiMap.get("newFRButton"));
		holdSeconds(1);
		enterText(replaceGui(guiMap.get("inputFRDate"), "Published Date"), 
				row.get("FR_Published_Date"));
		enterText(replaceGui(guiMap.get("inputFRText"), "FR Parent"),"");
		clickElementJs(guiMap.get("FederalRegisterType")); 
		clickElementJs(replaceGui(guiMap.get("FederalRegisterTypeItem"),"Initiation"));
		enterText(replaceGui(guiMap.get("inputFRText"), "Cite Number"),"None");
		enterText(replaceGui(guiMap.get("inputFRText"), "FR Parent"),"");
		clickElementJs(guiMap.get("buttonSaveSegment"));
		holdSeconds(2);
		clickElementJs(guiMap.get("objectDetails"));
		switchToFrame(guiMap.get("frameOnSunsetReview"));
		//90
		clickElementJs(guiMap.get("EditButton"));
		selectElementByValue(replaceGui(guiMap.get("selectOnSunSet"),"Notice of intent to participate Ips?"), "Yes");
		selectElementByValue(replaceGui(guiMap.get("selectOnSunSet"),"Domestic Party File Substan. Response?"), "No");
		clickElementJs(guiMap.get("SaveButton"));
		switchBackFromFrame();
		scrollByPixel(270);
		switchToFrame(guiMap.get("frameOnSunsetReview"));
		holdSeconds(2);
		int currentTimeOut = setBrowserTimeOut(4);
		if(checkElementExists(replaceGui(guiMap.get("sunsetReviewType"),"90 Day")))
		{
			setBrowserTimeOut(currentTimeOut);
			highlightElement(replaceGui(guiMap.get("sunsetReviewType"),"90 Day"), "green");
			updateHtmlReport("Create sunset Review segment '90 Day'", "User is able to create a new segment", 
					"<span class = 'boldy'>Sunset Segment[90 Day]</span>", "Step", "pass", 
					"90 days sunset review");
		}
		else
		{
			setBrowserTimeOut(currentTimeOut);
			updateHtmlReport("Create sunset Review segment '90 Day'", "User is able to create a new segment",
					"Not ass expected", "Step", "fail", 
					"90 days sunset review");
		}
		allMatches = allMatches & validateSunSetReviewDatesByType("90 Day", row.get("FR_Published_Date"));
		
		//120
		clickElementJs(guiMap.get("EditButton"));
		selectElementByValue(replaceGui(guiMap.get("selectOnSunSet"),"Domestic Party File Substan. Response?"), "Yes");
		clickElementJs(guiMap.get("SaveButton"));
		holdSeconds(2);
		currentTimeOut = setBrowserTimeOut(4);
		if(checkElementExists(replaceGui(guiMap.get("sunsetReviewType"),"120 Day")))
		{
			setBrowserTimeOut(currentTimeOut);
			highlightElement(replaceGui(guiMap.get("sunsetReviewType"),"120 Day"), "green");
			updateHtmlReport("Create sunset Review segment '120 Day'", "User is able to create a new segment", 
					"<span class = 'boldy'>Sunset Segment[120 Day]</span>", "Step", "pass", 
					"90 days sunset review");
		}
		else
		{
			setBrowserTimeOut(currentTimeOut);
			updateHtmlReport("Create sunset Review segment '120 Day'", "User is able to create a new segment", 
					"Not ass expected", "Step", "fail", 
					"120 days sunset review");
		}
		//verify
		allMatches = allMatches & validateSunSetReviewDatesByType("120 Day", row.get("FR_Published_Date"));

		//240
		clickElementJs(guiMap.get("EditButton"));
		selectElementByValue(replaceGui(guiMap.get("selectOnSunSet"),"Review to address zeroing in Segments?"), "Yes");
		selectElementByValue(replaceGui(guiMap.get("selectOnSunSet"),"Respondent File Substantive Response?"), "Yes");
		clickElementJs(guiMap.get("SaveButton"));
		holdSeconds(2);
		currentTimeOut = setBrowserTimeOut(4);
		if(checkElementExists(replaceGui(guiMap.get("sunsetReviewType"),"240 Day")))
		{
			setBrowserTimeOut(currentTimeOut);
			highlightElement(replaceGui(guiMap.get("sunsetReviewType"),"240 Day"), "green");
			updateHtmlReport("Create sunset Review segment '240 Day'", "User is able to create a new segment", 
					"<span class = 'boldy'>Sunset Segment[240 Day]</span>", "Step", "pass", 
					"90 days sunset review");
		}
		else
		{
			setBrowserTimeOut(currentTimeOut);
			updateHtmlReport("Create sunset Review segment '240 Day'", "User is able to create a new segment",
					"Not ass expected", "Step", "fail", 
					"120 days sunset review");
		}
		allMatches = allMatches & validateSunSetReviewDatesByType("240 Day", row.get("FR_Published_Date"));
		return allMatches;
	}
	
	/**
	 * This method validates new Scope Inquiry review segment dates
	 * @return true if all dates matches, false if not
	 * @exception Exception
	*/
	static boolean validateSunSetReviewDatesByType(String sunSetType, String publishedDate) throws Exception
	{
		boolean matches = true;
		String actualValue = "";
		String publishedDateFinal = "";
		String actualFinalSignature = "";
		//Notice of Intent to Participate
		String noticeOfIntentToParticipate = calculateDate(15, "Notice of Intent to Participate", "calendar", publishedDate);
		actualValue = getElementAttribute(replaceGui(guiMap.get("sunSetReviewDate"),"Notice of Intent to Participate"), "text");
		matches = matches & compareAndReport("sunSetReviewDate", "Notice of Intent to Participate", 
				noticeOfIntentToParticipate, actualValue);
		//Notify Cluster Coordinator No Interest
		String notifyClusterCoordinatorNoInterest = calculateDate(1, "Notify Cluster Coordinator No Interest", "calendar",
				noticeOfIntentToParticipate);
		actualValue = getElementAttribute(replaceGui(guiMap.get("sunSetReviewDate"), 
				"Notify Cluster Coordinator No Interest"), "text");
		matches = matches & compareAndReport("sunSetReviewDate", "Notify Cluster Coordinator No Interest", 
				notifyClusterCoordinatorNoInterest, actualValue);
		//Substantive responses Due For All Parties
		String substantiveResponsesDueForAllParties = calculateDate(30, "Substantive Response Due For All Parties", 
				"calendar", publishedDate);
		actualValue = getElementAttribute(replaceGui(guiMap.get("sunSetReviewDate"),
				"Substantive Response Due For All Parties"), "text");
		System.out.println("");																			 
		matches = matches & compareAndReport("sunSetReviewDate", "Substantive Response Due For All Parties", 
				substantiveResponsesDueForAllParties, actualValue);
		//Inform Cluster Coordinator if No Response
		String informClusterCoordinatorIfNoRespons = calculateDate(1, "Inform Cluster Coordinator if No Respons", "calendar",
				substantiveResponsesDueForAllParties);
		actualValue = getElementAttribute(replaceGui(guiMap.get("sunSetReviewDate"), 
				"Inform Cluster Coordinator if No Respons"), "text");
		matches = matches & compareAndReport("sunSetReviewDate", "Inform Cluster Coordinator if No Respons", 
				informClusterCoordinatorIfNoRespons, actualValue);
		
		//Actual Revocation or Continuation FR - Actual_Revocation_or_Continuation_FR__c???
		/*String actualRevocationOrContinuationFr = calculateDate(1, "Actual Revocation or Continuation FR", "calendar",
				substantiveResponsesDueForAllParties);
		actualValue = getElementAttribute(replaceGui(guiMap.get("sunSetReviewDate"),"Actual Revocation or Continuation FR"), "text");
		matches = matches & compareAndReport("sunSetReviewDate", "Actual Revocation or Continuation FR", 
				actualRevocationOrContinuationFr, actualValue);*/
				
		if(!sunSetType.endsWith("240 Day"))
		{		
			scrollToElement(replaceGui(guiMap.get("sunSetReviewDate"),"Issue Liquidation/Revocation Instruction"));
		}
		else
		{
			scrollToElement(replaceGui(guiMap.get("sunSetReviewDate"),"Calculated Final Signature"));
		}
		//Inadequate Domestic Response note to ITC
		if(sunSetType.endsWith("90 Day"))
		{
			String inadequateDomesticResponseNoteToITC = calculateDate(40, "Inadequate Domestic Response note to ITC", "calendar",
					publishedDate);
			actualValue = getElementAttribute(replaceGui(guiMap.get("sunSetReviewDate"),"Inadequate Domestic Response note to ITC"), "text");
			matches = matches & compareAndReport("sunSetReviewDate", "Inadequate Domestic Response note to ITC", 
					inadequateDomesticResponseNoteToITC, actualValue);
		}
		//Calculated Final Signature
		int cfsDays;
		if(sunSetType.endsWith("90 Day")) cfsDays = 90; 
		else if(sunSetType.endsWith("120 Day")) cfsDays = 120;
		else cfsDays = 240;
		String calculatedFinalSignature = calculateDate(cfsDays, "Calculated Final Signature", "calendar",
				publishedDate);
		actualValue = getElementAttribute(replaceGui(guiMap.get("sunSetReviewDate"),"Calculated Final Signature"), "text");
		matches = matches & compareAndReport("sunSetReviewDate", "Calculated Final Signature", 
				calculatedFinalSignature, actualValue);
		//Update ACE (Customs Module)
		if(sunSetType.endsWith("90 Day"))
		{
			String UpdateAceCustomsModule="";
			if(actualFinalSignature.equals("") && publishedDateFinal.equals("") && !calculatedFinalSignature.equals(""))
			{
				UpdateAceCustomsModule = calculateDate(6, "Update ACE (Customs Module)", "calendar",
						calculatedFinalSignature);
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
			actualValue = getElementAttribute(replaceGui(guiMap.get("sunSetReviewDate"),"Update ACE (Customs Module)"), "text");
			matches = matches & compareAndReport("sunSetReviewDate", "Update ACE (Customs Module)", 
					UpdateAceCustomsModule, actualValue);
		}
		else
		{
			//Hide if continuation_or_revocation__c is not = Revocation
			//IF Actual_Revocation_or_Continuation_FR__c  is blank AND Publish_Date__c (type = Revocation) is blank THEN Calculated_Revocation_or_Continuation_FR__c  + 6; 
			//IF Actual_Revocation_or_Continuation_FR__c  is not blank AND Publish_Date__c (type = Revocation) is blank THEN Actual_Revocation_or_Continuation_FR__c  + 6; 
			//IF Publish_Date__c (type = Revocation) is not blank THEN Publish_Date__c (type = Revocation) -1
		}
		//Notify ITC of No Domestic Interest
		if(sunSetType.endsWith("90 Day"))
		{
			String notifyItcOfNoDomesticInterest = calculateDate(20, "Notify ITC of No Domestic Interest", "calendar",
					publishedDate);
			actualValue = getElementAttribute(replaceGui(guiMap.get("sunSetReviewDate"),"Notify ITC of No Domestic Interest"), "text");
			matches = matches & compareAndReport("sunSetReviewDate", "Notify ITC of No Domestic Interest", 
					notifyItcOfNoDomesticInterest, actualValue);
		}
		//Final Issues Due to DAS
		String finalIssuesDueToDas = calculateDate(-10, "Final Issues Due to DAS", "business",
				calculatedFinalSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("sunSetReviewDate"),"Final Issues Due to DAS"), "text");
		matches = matches & compareAndReport("sunSetReviewDate", "Final Issues Due to DAS", 
				finalIssuesDueToDas, actualValue);
		//Final Concurrencec Due to DAS
		String finalConcurrenceDueToDas = calculateDate(-5, "Final Concurrence Due to DAS", "business",
				calculatedFinalSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("sunSetReviewDate"),"Final Concurrence Due to DAS"), "text");
		matches = matches & compareAndReport("sunSetReviewDate", "Final Concurrence Due to DAS", 
				finalConcurrenceDueToDas, actualValue);
		String finalTeamMeetingDeadline = "";
		if(sunSetType.equals("240 Day"))
		{
			//Final Team Meeting Deadline
			finalTeamMeetingDeadline = calculateDate(-21, "Final Team Meeting Deadline", "calendar", calculatedFinalSignature);
			actualValue = getElementAttribute(replaceGui(guiMap.get("sunSetReviewDate"),"Final Team Meeting Deadline"), "text");
			matches = matches & compareAndReport("sunSetReviewDate", "Final Team Meeting Deadline",
					finalTeamMeetingDeadline, actualValue);
		}
		//Final Announcement Date
		if(sunSetType.equals("120 Day"))
		{
			String finalAnnouncementDate = calculateDate(1, "Final Announcement Date", "business",
					!actualFinalSignature.equals("")?actualFinalSignature:calculatedFinalSignature);
			actualValue = getElementAttribute(replaceGui(guiMap.get("sunSetReviewDate"),"Final Announcement Date"), "text");
			matches = matches & compareAndReport("sunSetReviewDate", "Final Announcement Date", 
					finalAnnouncementDate, actualValue);
		}
		
		//Issue Liquidation/Revocation Instruction
		if(sunSetType.equals("90 Day"))
		{
			String issueLiquidationRevocationInstruction="";
			if(actualFinalSignature.equals("") && publishedDateFinal.equals("") && !calculatedFinalSignature.equals(""))
			{
				issueLiquidationRevocationInstruction = calculateDate(22, "Issue Liquidation/Revocation Instruction", "calendar",
						calculatedFinalSignature);
			}
			else if(actualFinalSignature.equals("") && publishedDateFinal.equals(""))
			{
				issueLiquidationRevocationInstruction = calculateDate(22, "Issue Liquidation/Revocation Instruction", "calendar",
						actualFinalSignature);
			}
			else if(!publishedDateFinal.equals(""))
			{
				issueLiquidationRevocationInstruction = calculateDate(15, "Issue Liquidation/Revocation Instruction", "calendar",
						publishedDateFinal);
			}
			actualValue = getElementAttribute(replaceGui(guiMap.get("sunSetReviewDate"),"Issue Liquidation/Revocation Instruction"), "text");
			matches = matches & compareAndReport("sunSetReviewDate", "Issue Liquidation/Revocation Instruction", 
					issueLiquidationRevocationInstruction, actualValue);
		}
		else
		{
			//Hide if continuation_or_revocation__c is not = Revocation
			//IF Actual_Revocation_or_Continuation_FR__c  is blank AND Publish_Date__c (type = Revocation) is blank THEN Calculated_Revocation_or_Continuation_FR__c  + 6; 
			//IF Actual_Revocation_or_Continuation_FR__c  is not blank AND Publish_Date__c (type = Revocation) is blank THEN Actual_Revocation_or_Continuation_FR__c  + 6; 
			//IF Publish_Date__c (type = Revocation) is not blank THEN Publish_Date__c (type = Revocation) -1
		}
		
		if(sunSetType.endsWith("120 Day"))
		{
			//Memorandum on Adequacy Determination
			String memorandumOnAdequacyDetermination = calculateDate(110, "Memorandum on Adequacy Determination", "calendar",publishedDate);
			actualValue = getElementAttribute(replaceGui(guiMap.get("sunSetReviewDate"),"Memorandum on Adequacy Determination"), "text");
			matches = matches & compareAndReport("sunSetReviewDate", "Memorandum on Adequacy Determination", 
					memorandumOnAdequacyDetermination, actualValue);
			scrollToElement(replaceGui(guiMap.get("sunSetReviewDate"),"Adequacy Determination & Letter to ITC"));
			//Rebuttal Comments Due
			String rebuttalCommentsDue = calculateDate(35, "Rebuttal Comments Due", "calendar",publishedDate);
			actualValue = getElementAttribute(replaceGui(guiMap.get("sunSetReviewDate"),"Rebuttal Comments Due"), "text");
			matches = matches & compareAndReport("sunSetReviewDate", "Rebuttal Comments Due", 
					rebuttalCommentsDue, actualValue);
			scrollToElement(replaceGui(guiMap.get("sunSetReviewDate"),"Adequacy Determination & Letter to ITC"));
			//Comments on Adequacy Determination Filed
			String commentsOnAdequacyDeterminationFiled = calculateDate(70, "Comments on Adequacy Determination Filed", "calendar",publishedDate);
			actualValue = getElementAttribute(replaceGui(guiMap.get("sunSetReviewDate"),"Comments on Adequacy Determination Filed"), "text");
			matches = matches & compareAndReport("sunSetReviewDate", "Comments on Adequacy Determination Filed", 
					commentsOnAdequacyDeterminationFiled, actualValue);
		}
		String calculatedPreliminarySignature = "";
		String prelimConcurrenceDueToDas = "";
		String prelimIssuesDueToDas  = "";
		String prelimTeamMeetingDeadline = "";
		if(sunSetType.endsWith("240 Day"))
		{
			scrollToElement(replaceGui(guiMap.get("sunSetReviewDate"),"Adequacy Determination & Letter to ITC"));
			//Adequacy Determination & Letter to ITC
			String adequacyDeterminationLetter = calculateDate(50, "Adequacy Determination & Letter to ITC", "calendar",publishedDate);
			actualValue = getElementAttribute(replaceGui(guiMap.get("sunSetReviewDate"),"Adequacy Determination & Letter to ITC"), "text");
			matches = matches & compareAndReport("sunSetReviewDate", "Adequacy Determination & Letter to ITC", 
					adequacyDeterminationLetter, actualValue);
			//Calculated Preliminary Signature
			calculatedPreliminarySignature = calculateDate(110, "Calculated Preliminary Signature", "calendar",publishedDate);
			actualValue = getElementAttribute(replaceGui(guiMap.get("sunSetReviewDate"),"Calculated Preliminary Signature"), "text");
			matches = matches & compareAndReport("sunSetReviewDate", "Calculated Preliminary Signature", 
					calculatedPreliminarySignature, actualValue); 
			//Prelim Issues Due to DAS
			prelimIssuesDueToDas = calculateDate(-10, "Prelim Issues Due to DAS", "business", calculatedPreliminarySignature);
			actualValue = getElementAttribute(replaceGui(guiMap.get("sunSetReviewDate"),"Prelim Issues Due to DAS"), "text");
			matches = matches & compareAndReport("sunSetReviewDate", "Prelim Issues Due to DAS", 
					prelimIssuesDueToDas, actualValue); 
			//Prelim Concurrence Due to DAS
			prelimConcurrenceDueToDas = calculateDate(-5, "Prelim Concurrence Due to DAS", "business",calculatedPreliminarySignature);
			actualValue = getElementAttribute(replaceGui(guiMap.get("sunSetReviewDate"),"Prelim Concurrence Due to DAS"), "text");
			matches = matches & compareAndReport("sunSetReviewDate", "Prelim Concurrence Due to DAS", 
					prelimConcurrenceDueToDas, actualValue);
			//Prelim Team Meeting Deadline
			prelimTeamMeetingDeadline = calculateDate(-21, "Prelim Team Meeting Deadline", "calendar",calculatedPreliminarySignature);
			actualValue = getElementAttribute(replaceGui(guiMap.get("sunSetReviewDate"),"Prelim Team Meeting Deadline"), "text");
			matches = matches & compareAndReport("sunSetReviewDate", "Prelim Team Meeting Deadline", 
					prelimTeamMeetingDeadline, actualValue);
			scrollToElement(replaceGui(guiMap.get("sunSetReviewDate"),"Rebuttal Comments Due"));
			//Rebuttal Comments Due
			String rebuttalCommentsDue = calculateDate(35, "Rebuttal Comments Due", "calendar",publishedDate);
			actualValue = getElementAttribute(replaceGui(guiMap.get("sunSetReviewDate"),"Rebuttal Comments Due"), "text");
			matches = matches & compareAndReport("sunSetReviewDate", "Rebuttal Comments Due", 
					rebuttalCommentsDue, actualValue);
			//scrollToElement(replaceGui(guiMap.get("sunSetReviewDate"),"Adequacy Determination & Letter to ITC"));
		}
		switchBackFromFrame();
		scrollToElement(replaceGui(guiMap.get("genericSegmentDate"), "Next Office Deadline"));
		String nextMajorDeadline = "", nextDueToDasDeadline="", nextOfficeDeadline="";
		String actualPreliminarySignature = "", actualPrelimIssuesToDas = "", 
				actualPrelimConcurrenceToDas="", segmentOutcome="", actualFinalIssuesToDas = "",
						actualFinalConcurrenceToDas = "", calculatedFinalFRSignature="";
		//Next Major Deadline
		if(actualPreliminarySignature.equals("") && sunSetType.endsWith("240 Day"))
		{
			nextMajorDeadline = calculatedPreliminarySignature;
		}else if(actualFinalSignature.equals(""))
		{
			nextMajorDeadline = calculatedFinalSignature;
		}
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentDate"),"Next Major Deadline"), "text");
		matches = matches & compareAndReport("genericSegmentDate", "Next Major Deadline", 
				nextMajorDeadline, actualValue);
		
		//Next Due to DAS Deadline
		if(actualPreliminarySignature.equals("") && actualPrelimIssuesToDas.equals("") && sunSetType.endsWith("240 Day"))
		{
			nextDueToDasDeadline = prelimIssuesDueToDas;
		}else if(actualPreliminarySignature.equals("") && actualPrelimConcurrenceToDas.equals("") && sunSetType.endsWith("240 Day"))
		{
			nextDueToDasDeadline = actualPrelimConcurrenceToDas;
		}else if(actualPreliminarySignature.equals("") && segmentOutcome.equals("") && sunSetType.endsWith("240 Day"))
		{
			nextDueToDasDeadline = calculatedPreliminarySignature;
		}
		else if(actualFinalSignature.equals("") && actualFinalIssuesToDas.equals(""))
		{
			nextDueToDasDeadline = prelimIssuesDueToDas;
		}else if(actualFinalSignature.equals("") && actualFinalConcurrenceToDas.equals(""))
		{
			nextDueToDasDeadline = actualPrelimConcurrenceToDas;
		}else if (actualFinalSignature.equals("") && segmentOutcome.equals(""))
		{
			nextDueToDasDeadline = calculatedFinalSignature;
		}
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentDate"),"Next Due to DAS Deadline"), "text");
		matches = matches & compareAndReport("genericSegmentDate", "Next Due to DAS Deadline", 
				nextDueToDasDeadline, actualValue);
		
		//Next Office Deadline
		if(actualPreliminarySignature.equals("") && !datePassed(prelimTeamMeetingDeadline) && sunSetType.endsWith("240 Day"))
		{
			nextOfficeDeadline = prelimTeamMeetingDeadline;
		}else if(actualPreliminarySignature.equals("") && actualPrelimIssuesToDas.equals("") && sunSetType.endsWith("240 Day"))
		{
			nextOfficeDeadline = prelimIssuesDueToDas;
		}else if(actualPreliminarySignature.equals("") && actualPrelimConcurrenceToDas.equals("") && sunSetType.endsWith("240 Day"))
		{
			nextOfficeDeadline = actualPrelimConcurrenceToDas;
		}else if(actualPreliminarySignature.equals("") && segmentOutcome.equals("") && sunSetType.endsWith("240 Day"))
		{
			nextOfficeDeadline = calculatedPreliminarySignature;
		}else if(actualFinalSignature.equals("") && !datePassed(finalTeamMeetingDeadline))
		{
			nextOfficeDeadline = finalTeamMeetingDeadline;
		}else if(actualFinalSignature.equals("") && actualFinalIssuesToDas.equals(""))
		{
			nextOfficeDeadline = prelimIssuesDueToDas;
		}else if(actualFinalSignature.equals("") && actualFinalConcurrenceToDas.equals(""))
		{
			nextOfficeDeadline = actualPrelimConcurrenceToDas;
		}else if (actualFinalSignature.equals(""))
		{
			nextOfficeDeadline = calculatedFinalSignature;
		}else if(publishedDate.equals("") && actualFinalSignature.equals(""))
		{
			nextOfficeDeadline = calculatedFinalFRSignature;
		}
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericSegmentDate"),"Next Office Deadline"), "text");
		matches = matches & compareAndReport("genericSegmentDate", "Next Office Deadline", 
				nextOfficeDeadline, actualValue);
		switchToFrame(guiMap.get("frameOnSunsetReview"));
		return matches;
	}
	
	/**
	 * This method creates new Litigation
	 * @param row: map of test case data
	 * @return true if petition is created successfully, false if not
	 * @exception Exception
	*/
	public static boolean createNewLitigation (LinkedHashMap<String, String> row) throws Exception
	{
		String litigationType = row.get("Litigation_Type");
		clickElementJs(guiMap.get("LitigationObjectLink"));
		holdSeconds(2);
		clickElementJs(guiMap.get("newLitigationButton"));
		holdSeconds(1);
		
		clickElementJs(replaceGui(guiMap.get("typeOfLitigationRadion"), 
				litigationType));
		clickElementJs(guiMap.get("nextButton"));
		holdSeconds(1);
		if(litigationType.trim().equals("Remand"))
		{
			enterText(replaceGui(guiMap.get("inputLitigationDate"),"Expected Final Signature Before Ext"),
					row.get("Expected_Final_Signature_Before_Ext"));
			updateHtmlReport("Enter Expected Final Signature Before Ext", "user is able to enter Expected Final Signature Before Ext",
					"As expected", "Step", "pass", "Enter Expected Final Signature Before Ext");
		}
		else
		{
			enterText(replaceGui(guiMap.get("inputLitigationDate"),"Request Filed"),
					row.get("Litigation_Filed"));
			updateHtmlReport("Enter Request Filed", "user is able to enter Request Filed",
					"As expected", "Step", "pass", "Enter Request Filed");
		}
		clickElementJs(guiMap.get("petitionsFieldOnLitigation"));
		clickElementJs(guiMap.get("petitionsItemOnLitigation"));
		clickElement(replaceGui(guiMap.get("inputLitigationText"),"Final Extension (# of days)"));
		clickElementJs(guiMap.get("saveCaseButton"));
		int currentTimeOut = setBrowserTimeOut(4);
		if(checkElementExists(guiMap.get("newCreateError")))
		{
			highlightElement(guiMap.get("newCreateError"), "red");
			holdSeconds(5);
			updateHtmlReport("Create Litigation", "User is able to create a new Litigation", "Not as expected", "Step", 
					"fail", "Create a new Litigation");
			clickElementJs(guiMap.get("CancelCaseButton"));
			setBrowserTimeOut(currentTimeOut);
			return false;
		}
		else
		{
			setBrowserTimeOut(currentTimeOut);
			if(!checkElementExists(replaceGui(guiMap.get("createdLitigation"), litigationType)))
			{
				updateHtmlReport("Create new Litigation", "User is able to create litigation",
						"Not as expected", "VP", "fail", "New litigation failed");
				return false;
			}else
			{
				highlightElement(replaceGui(guiMap.get("createdLitigation"), litigationType), "green");
				updateHtmlReport("Create new Litigation", "User is able to create litigation",
						"As expected", "VP", "pass", "New litigation pass");
				return true;
			}
		}
	}
	
	/**
	 * This method validates Litigation fields
	 * @return true if all dates matches, false if not
	 * @exception Exception
	*/
	public static boolean validateLitigationFields(LinkedHashMap<String, String> row) throws Exception
	{
		boolean allMatches = true;
		String actualValue, requestFiled = "", expectedFinalSignatureBeforeExt = "";
		String calculatedFinalSignature = "", calculatedPreliminarySignature="",
			   calculatedDraftRemandreleaseToparty = "",  prelimIssuesDueToDas="",
					   prelimConcurrenceDueToDas="", prelimTeamMeetingDeadline="";
		String draftRemandIssuesDueToDas="", draftRemandConcurrenceDueToDas="",
			   finalTeamMeetingDeadline = "";
		//boolean matches = true;
		String litigationType = row.get("Litigation_Type");
		//Prelim Extension (# of days)
		int prelimExtensionDays = readNumberFromScreen(getElementAttribute(replaceGui(guiMap.get("genericLitigationField"),
				"Prelim Extension (# of days)"), "text"));
		//
		int finalExtensionDays = readNumberFromScreen(getElementAttribute(replaceGui(guiMap.get("genericLitigationField"),
				"Final Extension (# of days)"), "text"));
		//Request Filed
		if(litigationType.equalsIgnoreCase("International Litigation"))
		{
			requestFiled = getElementAttribute(replaceGui(guiMap.get("genericLitigationField"),"Request Filed"), "text");
		}else //Calculated Draft Remand release to party
		{
			expectedFinalSignatureBeforeExt	 = getElementAttribute(replaceGui(guiMap.get("genericLitigationField"),
					"Expected Final Signature Before Ext"), "text");
		}
		//scrollToTheButtomOfPage();
		scrollToElement(replaceGui(guiMap.get("genericLitigationField"),"Status"));
		//International
		if(litigationType.equalsIgnoreCase("International Litigation"))
		{
			//Calculated Preliminary Signature
			calculatedPreliminarySignature = calculateLitigationDate(45 + prelimExtensionDays, "Calculated Preliminary Signature", 
					"calendar",requestFiled);
			actualValue = getElementAttribute(replaceGui(guiMap.get("genericLitigationField"),
					"Calculated Preliminary Signature"), "text");
			allMatches = allMatches & compareAndReport("genericLitigationField", "Calculated Preliminary Signature", 
					calculatedPreliminarySignature, actualValue); 
			//Prelim Issues Due to DAS
			prelimIssuesDueToDas = calculateLitigationDate(-10, "Prelim Issues Due to DAS", "calendar", calculatedPreliminarySignature);
			actualValue = getElementAttribute(replaceGui(guiMap.get("genericLitigationField"),"Prelim Issues Due to DAS"), "text");
			allMatches = allMatches & compareAndReport("genericLitigationField", "Prelim Issues Due to DAS", 
					prelimIssuesDueToDas, actualValue); 
			//Prelim Concurrence Due to DAS
			prelimConcurrenceDueToDas = calculateLitigationDate(-5, "Prelim Concurrence Due to DAS", "calendar", calculatedPreliminarySignature);
			actualValue = getElementAttribute(replaceGui(guiMap.get("genericLitigationField"),"Prelim Concurrence Due to DAS"), "text");
			allMatches = allMatches & compareAndReport("genericLitigationField", "Prelim Concurrence Due to DAS", 
					prelimConcurrenceDueToDas, actualValue);
			//Prelim Team Meeting Deadline
			prelimTeamMeetingDeadline = calculateLitigationDate(-21, "Prelim Team Meeting Deadline", "calendar", calculatedPreliminarySignature);
			actualValue = getElementAttribute(replaceGui(guiMap.get("genericLitigationField"),"Prelim Team Meeting Deadline"), "text");
			allMatches = allMatches & compareAndReport("genericLitigationField", "Prelim Team Meeting Deadline", 
					prelimTeamMeetingDeadline, actualValue);
			//Calculated Final Signature
			calculatedFinalSignature = calculateLitigationDate(180 + finalExtensionDays, "Calculated Final Signature", "Calendar", 
						requestFiled.equals("")?expectedFinalSignatureBeforeExt:requestFiled);
			actualValue = getElementAttribute(replaceGui(guiMap.get("genericLitigationField"),"Calculated Final Signature"), "text");
			allMatches = allMatches & compareAndReport("genericLitigationField", "Calculated Final Signature",
					calculatedFinalSignature, actualValue);
		}
		else//Remand
		{
			//Expected Final Signature Before Ext
			//Calculated Draft Remand release to party
			calculatedDraftRemandreleaseToparty = calculateLitigationDate(-30 + prelimExtensionDays, "Calculated Draft Remand release to party",
					"Calendar", 
					expectedFinalSignatureBeforeExt);
			actualValue = getElementAttribute(replaceGui(guiMap.get("genericLitigationField"), "Calculated Draft Remand release to party"),
					"text");
			allMatches = allMatches & compareAndReport("genericSegmentField", "Calculated Draft Remand release to party",
					calculatedDraftRemandreleaseToparty, actualValue);
			//Draft Remand Issues Due to DAS
			draftRemandIssuesDueToDas = calculateLitigationDate(-10, "Draft Remand Issues Due to DAS", "calendar", calculatedDraftRemandreleaseToparty);
			actualValue = getElementAttribute(replaceGui(guiMap.get("genericLitigationField"),"Draft Remand Issues Due to DAS"), "text");
			allMatches = allMatches & compareAndReport("genericLitigationField", "Draft Remand Issues Due to DAS", 
					draftRemandIssuesDueToDas, actualValue); 
			//Draft Remand Concurrence Due to DAS
			draftRemandConcurrenceDueToDas = calculateLitigationDate(-5, "Draft Remand Concurrence Due to DAS", "calendar",calculatedDraftRemandreleaseToparty);
			actualValue = getElementAttribute(replaceGui(guiMap.get("genericLitigationField"),"Draft Remand Concurrence Due to DAS"), "text");
			allMatches = allMatches & compareAndReport("genericLitigationField", "Draft Remand Concurrence Due to DAS", 
					draftRemandConcurrenceDueToDas, actualValue);
			//Calculated Final Signature
				calculatedFinalSignature = calculateLitigationDate(finalExtensionDays, "Calculated Final Signature", "Calendar", 
						requestFiled.equals("")?expectedFinalSignatureBeforeExt:requestFiled);
			actualValue = getElementAttribute(replaceGui(guiMap.get("genericLitigationField"),"Calculated Final Signature"), "text");
			allMatches = allMatches & compareAndReport("genericLitigationField", "Calculated Final Signature",
					calculatedFinalSignature, actualValue);
		}
		
		//Final Issues Due to DAS
		String FinalIssuesDueToDas = "";
		FinalIssuesDueToDas = calculateLitigationDate(-10, "Final Issues Due to DAS",  "calendar", calculatedFinalSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericLitigationField"),"Final Issues Due to DAS"), "text");
		allMatches = allMatches & compareAndReport("genericLitigationField", "Final Issues Due to DAS", 
				FinalIssuesDueToDas, actualValue);
		//Final Concurrence Due to DAS
		String FinalConcurrenceDueToDas = "";
		FinalConcurrenceDueToDas = calculateLitigationDate(-5, "Final Concurrence Due to DAS",  "calendar", calculatedFinalSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericLitigationField"),"Final Concurrence Due to DAS"), "text");
		allMatches = allMatches & compareAndReport("genericLitigationField", "Final Concurrence Due to DAS", 
				FinalConcurrenceDueToDas, actualValue);
		if(litigationType.equalsIgnoreCase("International Litigation"))
		{
		//Final Team Meeting Deadline
		
		finalTeamMeetingDeadline = calculateLitigationDate(-21, "Final Team Meeting Deadline", "calendar", calculatedFinalSignature);
		actualValue = getElementAttribute(replaceGui(guiMap.get("genericLitigationField"),"Final Team Meeting Deadline"), "text");
		allMatches = allMatches & compareAndReport("genericLitigationField", "Final Team Meeting Deadline",
				finalTeamMeetingDeadline, actualValue);
		}
		String actualPreliminarySignature = "";
		String actualFinalSignature = "";
		String nextMajorDeadline = "";
		String actualPrelimIssuesToDas="";
		String actualPrelimConcurrenceToDas="";
		scrollToElement(replaceGui(guiMap.get("genericLitigationField"),"Next Major Deadline"));
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
			actualValue = getElementAttribute(replaceGui(guiMap.get("genericLitigationField"), 
					"Next Major Deadline"), "text");
			allMatches = allMatches & compareAndReport("genericLitigationField", "Next Major Deadline",
				nextMajorDeadline, actualValue);
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
				nextDueToDasDeadline = FinalIssuesDueToDas;
			}else if("".equals(actualFinalSignature) && "".equals(actualFinalConcurrenceToDas))
			{
				nextDueToDasDeadline = FinalConcurrenceDueToDas;
			}else if("".equals(actualFinalSignature))
			{
				nextDueToDasDeadline = calculatedFinalSignature;
			}
			actualValue = getElementAttribute(replaceGui(guiMap.get("genericLitigationField"), 
					"Next Due to DAS Deadline"), "text");
			allMatches = allMatches & compareAndReport("genericLitigationField", "Next Due to DAS Deadline",
					nextDueToDasDeadline, actualValue);
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
				nextOfficeDeadline = FinalIssuesDueToDas;
			}else if("".equals(actualFinalSignature) && "".equals(actualFinalConcurrenceToDas))
			{
				nextOfficeDeadline = FinalConcurrenceDueToDas;
			}else if("".equals(actualFinalSignature))
			{
				nextOfficeDeadline = calculatedFinalSignature;
			}
			actualValue = getElementAttribute(replaceGui(guiMap.get("genericLitigationField"), 
					"Next Office Deadline"), "text");
			allMatches = allMatches & compareAndReport("genericLitigationField", "Next Office Deadline",
					nextOfficeDeadline, actualValue);
		}
		else
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
			actualValue = getElementAttribute(replaceGui(guiMap.get("genericLitigationField"), 
					"Next Major Deadline"), "text");
			allMatches = allMatches & compareAndReport("genericLitigationField", "Next Major Deadline",
				nextMajorDeadline, actualValue);
			
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
				nextDueToDasDeadline = FinalIssuesDueToDas;
			}else if("".equals(actualFinalSignature) && "".equals(actualFinalConcurrenceToDas))
			{
				nextDueToDasDeadline = FinalConcurrenceDueToDas;
			}else if("".equals(actualFinalSignature))
			{
				nextDueToDasDeadline = calculatedFinalSignature;
			}	
			actualValue = getElementAttribute(replaceGui(guiMap.get("genericLitigationField"), 
					"Next Due to DAS Deadline"), "text");
			allMatches = allMatches & compareAndReport("genericLitigationField", "Next Due to DAS Deadline",
					nextDueToDasDeadline, actualValue);
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
				nextOfficeDeadline = FinalIssuesDueToDas;
			}else if("".equals(actualFinalSignature) && "".equals(actualFinalConcurrenceToDas))
			{
				nextOfficeDeadline = FinalConcurrenceDueToDas;
			}else if("".equals(actualFinalSignature))
			{
				nextOfficeDeadline = calculatedFinalSignature;
			}	
			actualValue = getElementAttribute(replaceGui(guiMap.get("genericLitigationField"), 
					"Next Office Deadline"), "text");
			allMatches = allMatches & compareAndReport("genericLitigationField", "Next Office Deadline",
					nextOfficeDeadline, actualValue);
		}
		return allMatches;
		
	}
	/**
	 * This method verifies if given date is passed or not
	 * @param date: date to check
	 * @return pass if passed, false if not
	*/
	public static boolean checkCvdAlignedWithAd() throws Exception
	{
		scrollToElement(guiMap.get("ADInvestigationAlignedToDiv"));
		highlightElement(guiMap.get("ADInvestigationAlignedToDiv"), "green");
		updateHtmlReport("before aligning", "CVD is initialy not aligned",
				"As Expected", "VP", "pass", "Before aligning");
		unHighlightElement(guiMap.get("ADInvestigationAlignedToDiv"));
		clickElementJs(guiMap.get("editAlignTo"));
		holdSeconds(2);
		clickElementJs(replaceGui(guiMap.get("itemfromalignTo"),adCaseId));
		holdSeconds(2);
		clickElementJs(guiMap.get("saveInvestigation"));
		holdSeconds(3);
		if(checkElementExists(replaceGui(guiMap.get("linkedADText"),aInvestigation)))
		{
			highlightElement(guiMap.get("ADInvestigationAlignedToDiv"), "green");
			updateHtmlReport("After aligning", "CVD is aligned with AD case", "As Expected",
					"VP", "pass", "After aligning");
			unHighlightElement(guiMap.get("ADInvestigationAlignedToDiv"));
			return true;
		}
		else
		{
			highlightElement(guiMap.get("ADInvestigationAlignedToDiv"), "red");
			updateHtmlReport("After aligning", "CVD is aligned with AD case", "Not as Expected",
					"VP", "fail", "After aligning");
			unHighlightElement(guiMap.get("ADInvestigationAlignedToDiv"));
			return false;
		}
	}
	
	/**
	 * This method gets element attribute
	 * @param row: row of elements
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
		
		/*
		
		
		
		
		
		
		
		
		
		
		
		
		
		//check dates at creattion
		validated = validated & validateDatesPopulatedEmpty("populated", " when petition is created");
		//convert it to self-initaited
		clickElementJs(replaceGui(guiMap.get("genericInvestigationLink"), "Petition"));
		scrollToElement(replaceGui(guiMap.get("genericPetitionDate"),"Actual Initiation Signature"));
		clickElementJs(guiMap.get("editPetitionOutCome"));
		enterText(replaceGui(guiMap.get("inputPetitionDate"),"Actual Initiation Signature"),
				row.get("Actual_Initiation_Signature"));
		clickElementJs(guiMap.get("selectPetitionOutcome"));
		clickElementJs(replaceGui(guiMap.get("selectPetitionOutcomeItem"),"Self-Initiated"));
		updateHtmlReport("Convert petition to Self-initiated", "User is able to convert petition to self-initiated",
				"As expected", "VP", "pass", "Convert petition to self-initiated");
		holdSeconds(2);
		clickElementJs(guiMap.get("saveEditedPetition"));
		holdSeconds(2);
		clickElementJs(guiMap.get("linkInvestigation"));
		holdSeconds(1);
		clickElement(guiMap.get("linkNewCreatedInvestigation"));
		holdSeconds(2);
		pageRefresh();
		holdSeconds(1);
		//validate after converted to self-initiated
		validated = validated & validateDatesPopulatedEmpty("empty", 
				" when petition converted to self-initiated");
		//add initiated FR to the petition
		clickElementJs(replaceGui(guiMap.get("genericInvestigationLink"), "Petition"));
		createPetitionFrNotice(row);
		clickElementJs(guiMap.get("linkInvestigation"));
		holdSeconds(2);
		clickElement(guiMap.get("linkNewCreatedInvestigation"));
		holdSeconds(2);
		pageRefresh();
		holdSeconds(1);
		validated = validated & validateDatesPopulatedEmpty("populated", 
				" when an initaited FR associated with petition");*/
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
	 * This method gets element attribute
	 * @param dateState: expected values of dates, empty or populated
	 * @param step, the name of the step to validate
	 * @return true if all dates are as expected, false, if not.
	 * @throws Exception
	 */
	public static boolean validateDatesPopulatedEmpty(JSONObject obj, 
													  String dateState, 
													  String step) throws Exception
	{
		boolean matches = true;
		
		matches = validateDateEmptyFilled("Prelim Team Meeting Deadline", dateState);
		//matches = matches & validateDateEmptyFilled("Calculated Postponement of PrelimDeterFR", dateState);
		matches = matches & validateDateEmptyFilled("Prelim Issues Due to DAS", dateState);
		matches = matches & validateDateEmptyFilled("Prelim Concurrence Due to DAS", dateState);
		matches = matches & validateDateEmptyFilled("Calculated Preliminary Signature", dateState);
		matches = matches & validateDateEmptyFilled("Final Team Meeting Deadline", dateState);
		matches = matches & validateDateEmptyFilled("Final Issues Due to DAS", dateState);
		matches = matches & validateDateEmptyFilled("Final Concurrence Due to DAS", dateState);
		matches = matches & validateDateEmptyFilled("Calculated Final Signature", dateState);
		matches = matches & validateDateEmptyFilled("Final Announcement Date", dateState);
		matches = matches & validateDateEmptyFilled("Est ITC Notification to DOC of Final Det", dateState);
		matches = matches & validateDateEmptyFilled("Estimated Order FR Published", dateState);
		matches = matches & validateDateEmptyFilled("Next Announcement Date", dateState);
		matches = matches & validateDateEmptyFilled("Next Major Deadline", dateState);
		
		
		String yN =(matches == true) ? "":"not ";
		String msg = "Dates are "+yN+dateState+ step;
		if(matches)
		{
			updateHtmlReport("Verify Investigation dates", msg, 
					"As expected", "VP", "pass", msg+" - 1");
		}
		else
		{
			updateHtmlReport("Verify Investigation dates", msg, 
					"As expected", "VP", "fail", msg+" - 1");
		}
		scrollToElement(replaceGui(guiMap.get("genericInvestigationField"),
				"Estimated Order FR Published") );
		
		//matches = matches & validateDateEmptyFilled("Next Office Deadline", dateState);
													 
		yN =(matches == true) ? "":"not ";
		msg = "Dates are "+yN+dateState+ step;
		if(matches)
		{
			updateHtmlReport("Verify Investigation dates", msg, 
					"As expected", "VP", "pass", msg+" - 2");
		}
		else
		{
			updateHtmlReport("Verify Investigation dates", msg, 
					"As expected", "VP", "fail", msg+" - 2");
		}
		return matches;
	}
	
	/**
	 * This method gets element attribute
	 * @param dateState: expected values of dates, empty or populated
	 * @param step, the name of the step to validate
	 * @return true if all dates are as expected, false, if not.
	 * @throws Exception
	 */
	public static boolean validateDatesPopulatedEmpty(String dateState, String step) throws Exception
	{
		boolean matches = true;
		matches = validateDateEmptyFilled("Prelim Team Meeting Deadline", dateState);
		//matches = matches & validateDateEmptyFilled("Calculated Postponement of PrelimDeterFR", dateState);
		matches = matches & validateDateEmptyFilled("Prelim Issues Due to DAS", dateState);
		matches = matches & validateDateEmptyFilled("Prelim Concurrence Due to DAS", dateState);
		matches = matches & validateDateEmptyFilled("Calculated Preliminary Signature", dateState);
		matches = matches & validateDateEmptyFilled("Final Team Meeting Deadline", dateState);
		matches = matches & validateDateEmptyFilled("Final Issues Due to DAS", dateState);
													 
		matches = matches & validateDateEmptyFilled("Final Concurrence Due to DAS", dateState);
		matches = matches & validateDateEmptyFilled("Calculated Final Signature", dateState);
		String yN =(matches == true) ? "":"not ";
		String msg = "Dates are "+yN+dateState+ step;
		if(matches)
		{
			updateHtmlReport("Verify Investigation dates", msg, 
					"As expected", "VP", "pass", msg+" - 1");
		}
		else
		{
			updateHtmlReport("Verify Investigation dates", msg, 
					"As expected", "VP", "fail", msg+" - 1");
		}
		scrollToElement(replaceGui(guiMap.get("genericInvestigationField"),
				"Estimated Order FR Published") );
		matches = matches & validateDateEmptyFilled("Final Announcement Date", dateState);
		matches = matches & validateDateEmptyFilled("Est ITC Notification to DOC of Final Det", dateState);
		matches = matches & validateDateEmptyFilled("Estimated Order FR Published", dateState);
		matches = matches & validateDateEmptyFilled("Next Announcement Date", dateState);
		matches = matches & validateDateEmptyFilled("Next Major Deadline", dateState);
		//matches = matches & validateDateEmptyFilled("Next Office Deadline", dateState);
													 
		yN =(matches == true) ? "":"not ";
		msg = "Dates are "+yN+dateState+ step;
		if(matches)
		{
			updateHtmlReport("Verify Investigation dates", msg, 
					"As expected", "VP", "pass", msg+" - 2");
		}
		else
		{
			updateHtmlReport("Verify Investigation dates", msg, 
					"As expected", "VP", "fail", msg+" - 2");
		}
		return matches;
	}
	
	/**
	 * This method gets element attribute
	 * @param dateState: expected values of dates, empty or populated
	 * @param step, the name of the step to validate
	 * @return true if all dates are as expected, false, if not.
	 * @throws Exception
	 */
	public static boolean validateDateEmptyFilled(String dateName, 
												  String dateState) throws Exception
	{
		boolean matches = true;
		String dateDisplayedValue = getElementAttribute(replaceGui(guiMap.get("genericInvestigationField"),
				dateName), "text");
		if((dateState.equalsIgnoreCase("populated") && dateDisplayedValue.equals("") )
			||(dateState.equalsIgnoreCase("empty") && !dateDisplayedValue.equals(""))
			)
		{
			matches = false;
			highlightElement(replaceGui(guiMap.get("genericInvestigationFieldDiv"), dateName), "red");
		}else
		{
			highlightElement(replaceGui(guiMap.get("genericInvestigationFieldDiv"), dateName), "green");
		}
		
		return matches;
	}
	
	/**
	 * This method gets element attribute
	 * @param row: row of elements
	 * @return true if FR created successfully, if not.
	 * @throws Exception
	 */
	public static boolean createPetitionFrNotice(LinkedHashMap<String, String>row) throws Exception
	{
		boolean matches = true;
		clickElementJs(guiMap.get("linkFRNotice"));
		clickElementJs(guiMap.get("newFRButton"));
		clickElementJs(guiMap.get("frType"));
		clickElementJs(replaceGui(guiMap.get("frTypeItem"),"Initiation"));
		enterText(replaceGui(guiMap.get("frDate"),"Published Date"), row.get("Published_Date"));
		enterText(replaceGui(guiMap.get("frText"),"Cite Number"), "None");
		clickElement(replaceGui(guiMap.get("frText"),"FR Parent"));
		updateHtmlReport("Enter fields to create a new FR", "User is able to fill up fields for FR", "As expected", 
				"Step", "pass", "Enter fields to create a new FR");
		clickElementJs(guiMap.get("saveFRButton"));
		if(elementExists(guiMap.get("craetedFR")))
		{
			highlightElement(guiMap.get("craetedFR"), "green");
			updateHtmlReport("create a new FR", "User is able to create a new FR", "As expected", 
					"Step", "pass", "FR created");
		}
		return matches;
	}
	
	/**
	 * This method creates FR for segment
	 * @param row: row of elements
	 * @param frType: type of the FR
	 * @return true if FR created successfully, if not.
	 * @throws Exception
	 */
	public static boolean createSegmentFrNotice(LinkedHashMap<String, String>row, String frType) throws Exception
	{
		boolean matches = true;
		clickElementJs(guiMap.get("linkFRNoticeForSegment"));
		holdSeconds(2);
		clickElementJs(guiMap.get("newFRButton"));
		clickElementJs(guiMap.get("frType"));
		clickElementJs(replaceGui(guiMap.get("frTypeItem"),frType));
		enterText(replaceGui(guiMap.get("frDate"),"Published Date"), row.get("Published_Date"));
		enterText(replaceGui(guiMap.get("frText"),"Cite Number"), "None");
		clickElement(replaceGui(guiMap.get("frText"),"FR Parent"));
		updateHtmlReport("Enter fields to create a new FR", "User is able to fill up fields for FR", "As expected", 
				"Step", "pass", "Enter fields to create a new FR");
		clickElementJs(guiMap.get("saveFRButton"));
		holdSeconds(4);
		if(elementExists(guiMap.get("craetedFR")))
		{
			highlightElement(guiMap.get("craetedFR"), "green");
			updateHtmlReport("create a new FR", "User is able to create a new FR - "+frType, "As expected", 
					"Step", "pass", frType+" FR created");
		}
		clickElementJs(guiMap.get("objectDetails"));
		
		return matches;
	}
	
	/**
	 * This method read and save segment dates
	 * @return HashMap of dates
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
	 * This method read and save segment dates
	 * @return HashMap of dates
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
	 * This method verifies if given date is passed or not
	 * @param date: date to check
	 * @return pass if passed, false if not
	*/
	static boolean datePassed(String date) throws ParseException
	{
		if (date.contains("#")) date = date.split("#")[1];
		if (date.equals("")) return false;
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
	 * @param params: given dates
	 * @return calculated date
	*/
	static String calculateDate(int val, String ...params) throws ParseException
	{
		int iterator, numberBusinessDays ;
		String newDate = null;
		iterator = (val<0)? -1:1;
		numberBusinessDays = iterator * val;
		switch(params[0])
		{
			case "Calculated Initiation Signature": 
			case "Initiation Issues Due to DAS": 
			case "Initiation Announcement Date":
			case "Initiation Concurrence Due to DAS":
			case "Calculated ITC Prelim Determination":
			case "Calculated Prelim Extension Request File":
			case "Calculated Order FR Signature":
			case "Prelim Issues Due to DAS":
			case "Prelim Team Meeting Deadline":
			case "Calculated Preliminary Signature":
			case "Calculated Postponement of PrelimDeterFR":
			case "Prelim Concurrence Due to DAS":
			case "Calculated Final Signature":
			case "Final Team Meeting Deadline":
			case "Final Issues Due to DAS":
			case "Final Concurrence Due to DAS":
			case "Final Announcement Date":
			case "Est ITC Notification to DOC of Final Det":
			case "Estimated Order FR Published":
			case "Amended Final Announcement Date":
			case "Preliminary Announcement Date":
			case "Notify ITC of No Domestic Interest":
			case "Update ACE (Customs Module)":
			case "Inadequate Domestic Response note to ITC":
			case "Substantive responses Due For All Parties":
			case "Notify Cluster Coordinator No Interest":
			case "Notice of Intent to Participate":
			case "Substantive Response Due For All Parties":
			case "Inform Cluster Coordinator if No Respons":
			case "Issue Liquidation/Revocation Instruction":
			case "Calculated Draft Remand release to party":
			case "Draft Remand Issues Due to DAS":
			case "Draft Remand Concurrence Due to DAS":	
			case "Adequacy Determination & Letter to ITC":
			case "Memorandum on Adequacy Determination":
			case "Rebuttal Comments Due":
			case "Comments on Adequacy Determination Filed":
			{
				if(params[2].equals(""))
				{
					newDate = "";
					break;
				}
				Date date = format.parse(params[2]);
				calendar.setTime(date);
				if(params[1].equals("business"))
				{
					while((numberBusinessDays!=0) || (! isBusinessDay(calendar)))
					{
						if(isBusinessDay(calendar))
						{
							numberBusinessDays--;
						}
						calendar.add(Calendar.DAY_OF_MONTH, iterator);
					}
				}
				else
				{
					calendar.add(Calendar.DAY_OF_MONTH, val);
					while(! isBusinessDay(calendar))
					{
						calendar.add(Calendar.DAY_OF_MONTH, iterator);
					}
				}
				newDate = format.format(calendar.getTime());
				break;
			}
			
			case "Next Major Deadline":
			{
				if (params[2].equals("") && (params[3].equals("") || params[1].equals("Self-Initiated")))
					newDate = params[4];
				break;
			}
			case "Next Due to DAS Deadline": case "Next Office Deadline":
			{			
				if (params[2].equals("") || params[3].equals(""))
				{
					newDate =params[6];
				}
				else if (params[2].equals("") || params[4].equals(""))
				{
					newDate =params[7];
				}
				else if (params[2].equals("") || params[5].equals(""))
				{
					newDate =params[8];
				}
				else
				{
					newDate = "";
				}
				break;
			}
		}
		
		return newDate;
	}
	/**
	 * This method calculates date based on other dates for Litigation
	 * @param params: given dates
	 * @return calculated date
	*/
	/*static String calculateLitigationDate(int val, String ...params) throws ParseException
	{
		int iterator, numberBusinessDays ;
		String newDate = null;
		iterator = (val<0)? -1:1;
		numberBusinessDays = iterator * val;
		Format format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		switch(params[0])
		{
			case "Calculated Initiation Signature": 
			case "Initiation Issues Due to DAS": 
			case "Initiation Announcement Date":
			case "Initiation Concurrence Due to DAS":
			case "Calculated ITC Prelim Determination":
			case "Calculated Prelim Extension Request File":
			case "Calculated Order FR Signature":
			case "Prelim Issues Due to DAS":
			case "Prelim Team Meeting Deadline":
			case "Calculated Preliminary Signature":
			case "Calculated Postponement of PrelimDeterFR":
			case "Prelim Concurrence Due to DAS":
			case "Calculated Final Signature":
			case "Final Team Meeting Deadline":
			case "Final Issues Due to DAS":
			case "Final Concurrence Due to DAS":
			case "Final Announcement Date":
			case "Est ITC Notification to DOC of Final Det":
			case "Estimated Order FR Published":
			case "Amended Final Announcement Date":
			case "Preliminary Announcement Date":
			case "Notify ITC of No Domestic Interest":
			case "Update ACE (Customs Module)":
			case "Inadequate Domestic Response note to ITC":
			case "Substantive responses Due For All Parties":
			case "Notify Cluster Coordinator No Interest":
			case "Notice of Intent to Participate":
			case "Substantive Response Due For All Parties":
			case "Inform Cluster Coordinator if No Respons":
			case "Issue Liquidation/Revocation Instruction":
			case "Calculated Draft Remand release to party":
			case "Draft Remand Issues Due to DAS":
			case "Draft Remand Concurrence Due to DAS":	
			case "Adequacy Determination & Letter to ITC":
			case "Memorandum on Adequacy Determination":
			case "Rebuttal Comments Due":
			case "Comments on Adequacy Determination Filed":
			{
				if(params[2].equals(""))
				{
					newDate = "";
					break;
				}
				Date date = ((DateFormat) format).parse(params[2]);
				calendar.setTime(date);
				if(params[1].equals("business"))
				{
					while((numberBusinessDays!=0) || (! isBusinessDay(calendar)))
					{
						if(isBusinessDay(calendar))
						{
							numberBusinessDays--;
						}
						calendar.add(Calendar.DAY_OF_MONTH, iterator);
					}
				}
				else
				{
					calendar.add(Calendar.DAY_OF_MONTH, val);
					while(! isBusinessDay(calendar))
					{
						calendar.add(Calendar.DAY_OF_MONTH, iterator);
					}
				}
				newDate = format.format(calendar.getTime());
				break;
			}
			
			case "Next Major Deadline":
			{
				if (params[2].equals("") && (params[3].equals("") || params[1].equals("Self-Initiated")))
					newDate = params[4];
				break;
			}
			case "Next Due to DAS Deadline": case "Next Office Deadline":
			{			
				if (params[2].equals("") || params[3].equals(""))
				{
					newDate =params[6];
				}
				else if (params[2].equals("") || params[4].equals(""))
				{
					newDate =params[7];
				}
				else if (params[2].equals("") || params[5].equals(""))
				{
					newDate =params[8];
				}
				else
				{
					newDate = "";
				}
				break;
			}
		}
		return newDate;
	}*/
	
	/**
	 * This method calculates date based on other dates
	 * @param params: given dates
	 * @return calculated date
	*/
	static String calculateLitigationDate(int val, String ...params) throws ParseException
	{
		//Date todayDate = new Date();
		int iterator, numberBusinessDays ;
		iterator = (val<0)? -1:1;
		Date date;
		numberBusinessDays = iterator * val;
		Format format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		if(params[2].equals(""))
		{
			return "Empty"+"";
		}
		else if(params[2].contains("#"))
		{
			date = ((DateFormat) format).parse(params[2].split("#")[0]);
			calendar.setTime(date);
		}
		else
		{
			date = ((DateFormat) format).parse(params[2]);
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
	*/
	public static boolean isBusinessDay(Calendar cal){
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
		// check MLK Day (3rd Monday of January)
		if (cal.get(Calendar.MONTH) == Calendar.JANUARY
			&& cal.get(Calendar.DAY_OF_WEEK_IN_MONTH) == 3
			&& cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
			return false;
		}
		// IF NOTHING ELSE, IT'S A BUSINESS DAY
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
	 * This method gets validate element and report
	 * @param htmlfield: Gui element
	 * @param elementName: Element's name
	 * @param expectedValue: expected value
	 * @param actualValue: actual value
	 * @return if the element value is as expected, false if not
	 * @throws Exception
	 */
	public static boolean compareAndReport(String htmlfield, 
										   String elementName, 
										   String expectedValue, 
										   String actualValue) throws Exception 
	{
		if(expectedValue.equals("")) expectedValue = "Empty";
		if(actualValue.equals("")) actualValue = "Empty";
		printLog(elementName + " -- " + expectedValue + " Versus " + actualValue);
		if (expectedValue.equalsIgnoreCase(actualValue))
		{
			highlightElement(replaceGui(guiMap.get(htmlfield), elementName), "green");
			updateHtmlReport("Validate ["+ elementName +"]", expectedValue, actualValue, "VP", "pass", "");
			unHighlightElement(replaceGui(guiMap.get(htmlfield), elementName));
			return true;
		}
		else
		{
			highlightElement(replaceGui(guiMap.get(htmlfield), elementName), "red");
			updateHtmlReport("Validate ["+ elementName +"]", expectedValue, actualValue, "VP", "fail", "");
			unHighlightElement(replaceGui(guiMap.get(htmlfield), elementName));
			return false;
		}
	}
	
	
	
	/**
	 * This method gets validate element and report
	 * @param htmlfield: Gui element
	 * @param elementName: Element's name
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
			//highlightElement(replaceGui(guiMap.get(htmlfield), elementName), "green");
			updateHtmlReport("Validate ["+ elementName +"]", expectedValue, actualValue, "VP", "pass", "");
			//unHighlightElement(replaceGui(guiMap.get(htmlfield), elementName));
			return true;
		}
		else
		{
			//highlightElement(replaceGui(guiMap.get(htmlfield), elementName), "red");
			updateHtmlReport("Validate ["+ elementName +"]", expectedValue, actualValue, "VP", "fail", "");
			//unHighlightElement(replaceGui(guiMap.get(htmlfield), elementName));
			return false;
		}
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
	 * This method validate petition's statuses
	 * @param row, row of elements
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
	 * This method validate investigation's statuses
	 * @param row, row of elements
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
				+ "(Type: Preliminary) is not blank AND Actual_Amended_Prelim_Determination_Sig is not blank AND Investigation Outcome "
				+ " is not ('ITC Negative Prelim' or 'Petition Withdrawn After Initiation' or 'Suspension Agreement') THEN Status is true";
		record.clear();
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
	 * @param row, row of elements
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
	 * This method validate changed circumstance and Anti-circumvention
	 * statuses
	 * @param row, row of elements
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
	 * This method validate Litigations statuses
	 * statuses
	 * @param row, row of elements
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
	/**
	 * This method validate validate and report statuses
	 * @param htmlElement, HTML code of the status to validate
	 * @param status, expected status
	 * @param object, object name
	 * @throws Exception
	 */
	public static void validateStatus(String htmlElement, String status, String object) throws Exception
	{
		scrollToTheTopOfPage();
		holdSeconds(2);
		String selected = getElementAttribute(replaceGui(guiMap.get(htmlElement), status), "aria-selected");
		if (selected.equalsIgnoreCase("true"))
		{
			highlightElement(replaceGui(guiMap.get(htmlElement+"Container"), status), "yellow");
			holdSeconds(1);
			updateHtmlReport(object+" - Check status", "Status Is "+ status, "As expected", "VP", "pass", object+" status is "+status);
			unHighlightElement(replaceGui(guiMap.get(htmlElement+"Container"), status));
		}
		else
		{
			highlightElement(replaceGui(guiMap.get(htmlElement+"Container"), status), "green");
			failTestCase(object+" - Check status", "Status Is "+ status, "Not as expected", "VP", "fail", object+" status is "+status+" Fail");
		}
	}
	
	/**
	 * This method validates Litigation fields
	 * @return true if all dates matches, false if not
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
		else
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
	
	
	 public static String noNullVal(String str)
	    {
	    	String  strC = (str==null||str.equals("null"))?"":str;
	    	return strC;
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
	 * @param ScenarioType: Positive or Negative
	 * @param testedStatus: status, tested status
	 * @param actualstatus: actual status
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
	
	
}
