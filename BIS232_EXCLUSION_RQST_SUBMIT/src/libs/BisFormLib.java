/**
 * MilCorp
 * Mouloud Hamdidouche
 * January, 2019
*/

package libs;

import static GuiLibs.GuiTools.checkElementExists;
import static GuiLibs.GuiTools.clickElementJs;
import static GuiLibs.GuiTools.enterText;
import static GuiLibs.GuiTools.failTestSuite;
import static GuiLibs.GuiTools.getElementAttribute;
import static GuiLibs.GuiTools.guiMap;
import static GuiLibs.GuiTools.highlightElement;
import static GuiLibs.GuiTools.holdSeconds;
import static GuiLibs.GuiTools.navigateTo;
import static GuiLibs.GuiTools.replaceGui;
import static GuiLibs.GuiTools.scrollByPixel;
import static GuiLibs.GuiTools.scrollToElement;
import static GuiLibs.GuiTools.selectElementByText;
import static GuiLibs.GuiTools.setBrowserTimeOut;
import static GuiLibs.GuiTools.unHighlightElement;
import static GuiLibs.GuiTools.updateHtmlReport;
import static GuiLibs.GuiTools.updateHtmlReportOverall;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

public class BisFormLib{
	public static String filedDate,
	actualInitiationSignature, calculatedInitiationSignature, petitionOutcome;
	public static int petitionInitiationExtension;
	static DateFormat format;
	static Calendar calendar;
	static String caseType;
	static String investigationId;
	static String orderId;
	
	static String[] ElementsInRange = {"Aluminum","Antimony","Bismuth","Boron","Carbon","Chromium",
			"Cobalt","Copper","Iron","Lead","Magnesium","Manganese","Molybdenum","Nickel",
			"Niobium","Nitrogen","Phosphorus","Selenium","Silicon","Sulfur","Tellurium",
			"Titanium",	"Tungsten",	"Vanadium",	"Zinc",	"Zirconium",	"Other Chemical",
			"Elogation",	"Reduction in Area",	"Hole Expansion"};
	
	public BisFormLib() throws IOException {
		//super();
		//this.format = new SimpleDateFormat("M/d/yyyy");
		//this.calendar = Calendar.getInstance();
	}
	/**
	 * This method login to ADCVD web application
	 * @param url: url for the application
	 * @param user: user
	 * @param password: password
	 * @exception Exception
	 */
	public static boolean loginToBis(String url, 
									 String user, 
									 String password) throws Exception
	{
		boolean loginStatus = true;
		navigateTo(url);
		if(! checkElementExists(guiMap.get("CreateNewExclusionRequest")))
		{
			failTestSuite("navigate to bis 232 url", "User is not able to navigate", 
				"Not as expected", "Step", "fail", "navigation failed");
			loginStatus = false;
		}else
		{
			highlightElement(guiMap.get("CreateNewExclusionRequest"), "green");
			holdSeconds(2);
			clickElementJs(guiMap.get("CreateNewExclusionRequest"));
			int currentWait = setBrowserTimeOut(5);
			if(! checkElementExists(guiMap.get("ExclusiveRequestForm")))
			{
				setBrowserTimeOut(currentWait);
				enterText(guiMap.get("UserName"), user);
				enterText(guiMap.get("Password"), password);
				clickElementJs(guiMap.get("loginB"));
				if(! checkElementExists(guiMap.get("ExclusiveRequestForm")))
				{
					updateHtmlReport("Login to BIS232 App",  "User is not able to login", "Not as expected", 
							"Step", "fail", "Login to BIS 232");
					loginStatus = false;
				}else
				{
					updateHtmlReport("Login to BIS232 App",  "User is able to login", "As expected", 
						"Step", "pass", "Login to BIS 232");
				}
			}else
			{
				setBrowserTimeOut(currentWait);
				updateHtmlReport("Login to BIS232 App",  "User is able to login", "As expected", 
						"Step", "pass", "Login to BIS 232");
			}
			
		}
		return loginStatus;
	}
	/**
	 * This method fills up step 1
	 * @param row: map of test case's data
	 * @return true if all displayed messages are correct, false if not
	 * @exception Exception
	*/
	public static void fillUpStepOne(HashMap<String, String> row) throws Exception
	{
		String elementName = "";
		holdSeconds(1);
		clickElementJs(replaceGui(guiMap.get("tabName"), "Step 1") );
		holdSeconds(2);
		for (HashMap.Entry<String, String> entry : row.entrySet()) 
		{
			System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
			elementName = entry.getKey().trim();
			if(elementName.equalsIgnoreCase("Scenarios") || elementName.equalsIgnoreCase("Active") )
			{
				continue;
			}else if (elementName.equalsIgnoreCase("Requesting Organization State") || 
					  elementName.equalsIgnoreCase("Importer State")||elementName.equalsIgnoreCase("Metal Type")
					  ||elementName.equalsIgnoreCase("Metal Class"))
			{
				selectElementByText(replaceGui(guiMap.get("StepOneElementSelect"), elementName), entry.getValue());
			}
			else
			{
				enterText(replaceGui(guiMap.get("StepOneElement"), elementName), entry.getValue());
			}
		}
		updateHtmlReportOverall("Fillup the form of step 1",  "User fill up the form of step 1", "As expected", 
				"Step", "pass", "Fill up the form step 1");
	}
	
	/**
	 * This method fills up step 2
	 * @param row: map of test case's data
	 * @return true if all displayed messages are correct, false if not
	 * @exception Exception
	*/
	public static void fillUpStepTwo(HashMap<String, String> row) throws Exception
	{
		String elementName = "";
		holdSeconds(1);
		clickElementJs(replaceGui(guiMap.get("tabName"), "Step 2") );
		holdSeconds(2);
		for (HashMap.Entry<String, String> entry : row.entrySet()) 
		{
			System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
			elementName = entry.getKey().trim();
			if(elementName.equalsIgnoreCase("Ownership Activity")) clickElementJs(guiMap.get("OwnershipAnswer_true"));
			if(elementName.equalsIgnoreCase("Non US Producer Name")) clickElementJs(guiMap.get("BehalfOf_true"));
			if(elementName.equalsIgnoreCase("Scenarios") || elementName.equalsIgnoreCase("Active") )
			{
				continue;
			}else if (elementName.equalsIgnoreCase("Ownership Activity") || 
					  elementName.equalsIgnoreCase("Ownership Headquarters Country")||
					  elementName.equalsIgnoreCase("Exclusion Explanation")
					  ||elementName.equalsIgnoreCase("Non US Producer Headquarters Country"))
			{
				selectElementByText(replaceGui(guiMap.get("StepOneElementSelect"), elementName), entry.getValue());
			}
			else
			{
				enterText(replaceGui(guiMap.get("StepOneElement"), elementName), entry.getValue());
			}
		}
		updateHtmlReportOverall("Fillup the form of step 2",  "User fill up the form of step 2", "As expected", 
				"Step", "pass", "Fill up the form step 2");
	}
	
	/**
	 * This method fills up step 3 form
	 * @param row: map of test case's data
	 * @return true if all displayed messages are correct, false if not
	 * @exception Exception
	*/
	public static void fillUpStepThree(HashMap<String, String> row) throws Exception
	{
		String minVal="", maxVal="", elementName = "";
		holdSeconds(1);
		clickElementJs(replaceGui(guiMap.get("tabName"), "Step 3"));
		holdSeconds(2);
		enterText(guiMap.get("ProductDescription"), row.get("Product Description"));
		
		
		////
		scrollToElement(guiMap.get("OrganizationSelect"));
		if(!row.get("Organization").equals(""))
		{
			selectElementByText(guiMap.get("OrganizationSelect"), row.get("Organization").trim());
			enterText(guiMap.get("DesignationInput"), row.get("Designation").trim());
		}
		
		////
		
		scrollToElement(replaceGui(guiMap.get("ElementMinValue"), "Aluminum"));
		for (HashMap.Entry<String, String> entry : row.entrySet()) 
		{
			System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
			elementName = entry.getKey().trim();
			if(!elementName.equalsIgnoreCase("Scenarios") && !elementName.equalsIgnoreCase("Active")
					&& !elementName.equalsIgnoreCase("Product Description") 
					&& !elementName.equalsIgnoreCase("Organization") 
					&& !elementName.equalsIgnoreCase("Designation") )
			{
				minVal = entry.getValue().substring(0,entry.getValue().indexOf("|"));
				maxVal = entry.getValue().substring(entry.getValue().indexOf("|")+1,entry.getValue().length());
				enterText(replaceGui(guiMap.get("ElementMinValue"), elementName), minVal);
				enterText(replaceGui(guiMap.get("ElementMaxValue"), elementName), maxVal);
			}
		}
		updateHtmlReportOverall("Fillup the form of step 3",  "User fill up the form of step 3", "As expected", 
				"Step", "pass", "Fill up the form step 3");
	}
	
	/**
	 * This method fills up step 4 form
	 * @param row: map of test case's data
	 * @return true if all displayed messages are correct, false if not
	 * @exception Exception
	*/
	public static void fillUpStepFour(HashMap<String, String> row) throws Exception
	{
		holdSeconds(1);
		clickElementJs(replaceGui(guiMap.get("tabName"), "Step 4"));
		holdSeconds(2);
		enterText(guiMap.get("ApplicationSuitability"), row.get("Application Suitability"));
		scrollToElement(guiMap.get("OriginCountry"));
		selectElementByText(guiMap.get("OriginCountry"), row.get("Origin Country"));
		selectElementByText(guiMap.get("ExportCountry"), row.get("Export Country"));
		enterText(guiMap.get("ExclusionQuantity"), row.get("Exclusion Quantity"));
		enterText(guiMap.get("CBPDistinguishComments"), row.get("CBP Distinguish Comments"));
		updateHtmlReportOverall("Fillup the form of step 4",  "User fill up the form of step 4", "As expected", 
				"Step", "pass", "Fill up the form step 4");
	}
	
	/**
	 * This method fills up step 5
	 * @param row: map of test case's data
	 * @return true if all displayed messages are correct, false if not
	 * @exception Exception
	*/
	public static void fillUpStepFive(HashMap<String, String> row) throws Exception
	{
		String elementName = "";
		holdSeconds(1);
		clickElementJs(replaceGui(guiMap.get("tabName"), "Step 5") );
		holdSeconds(2);
		for (HashMap.Entry<String, String> entry : row.entrySet()) 
		{
			System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
			elementName = entry.getKey().trim();
			if(elementName.equalsIgnoreCase("Scenarios") || elementName.equalsIgnoreCase("Active") )
			{
				continue;
			}
			else
			{
				enterText(replaceGui(guiMap.get("StepFiveElement"), elementName), entry.getValue());
			}
		}
		updateHtmlReportOverall("Fillup the form of step 5",  "User fill up the form of step 5", "As expected", 
				"Step", "pass", "Fill up the form step 5");
	}
	/**
	 * This method Validates Step 1
	 * @param row: map of test case's data
	 * @return true if all displayed messages are correct, false if not
	 * @exception Exception
	*/
	public static boolean ValidateStepOne(HashMap<String, String> row) throws Exception
	{
		String  elementName = "", errorMsg="", htmlToValidate="";
		String mType = row.get("Metal Type");
		holdSeconds(1);
		clickElementJs(replaceGui(guiMap.get("tabName"), "Step 1") );
		holdSeconds(2);
		boolean matches = true;
		//Validate HTSUS Here
		for (HashMap.Entry<String, String> entry : row.entrySet()) 
		{
			System.out.println("Key : " + entry.getKey() + " Value :  " + entry.getValue());
			elementName = entry.getKey().trim();
			String elementValue = entry.getValue();
			if(!elementName.equalsIgnoreCase("Scenarios") && !elementName.equalsIgnoreCase("Active") &&
					!elementName.equalsIgnoreCase("Metal Type") && !elementName.equalsIgnoreCase("Metal Class")	)
			{
				if (elementName.equalsIgnoreCase("Organization Legal Name"))
				{
					scrollToElement(replaceGui(guiMap.get("StepOneElement"), "Requesting Organization Zipcode"));
				}
				else if (elementName.equalsIgnoreCase("Importer Legal Name"))
				{
					scrollToElement(replaceGui(guiMap.get("StepOneElement"), "Importer Zipcode"));
				}
				if (elementName.equalsIgnoreCase("HTSUS Code") && !elementValue.trim().equals(""))
				{
					errorMsg = getElementAttribute(replaceGui(guiMap.get("StepOneElementMessage"), elementName, "2"), "text");	
					htmlToValidate = "StepOneElementDiv";
				}
				else if (elementName.equalsIgnoreCase("Requesting Organization State") || elementName.equalsIgnoreCase("Importer State"))
				{
					errorMsg = getElementAttribute(replaceGui(guiMap.get("StepOneElementSelectMessage"), elementName), "text");
					htmlToValidate ="StepOneElementSelectDiv";
				}
				else
				{
					errorMsg = getElementAttribute(replaceGui(guiMap.get("StepOneElementMessage"), elementName, "1"), "text");	
					htmlToValidate = "StepOneElementDiv";
				}
				switch(elementName)
				{
					case "HTSUS Code":
					{
						if(elementValue.trim().equals(""))
						{
							matches = matches & validateElementAndReport(elementName, htmlToValidate, 
									"The HTSUSCode field is required.", errorMsg);
						}
						else if(
								(mType.equals("Steel") && ! Arrays.toString(Materials.materialSteels).contains(elementValue))
							||  (mType.equals("Aluminum") && (! Arrays.toString(Materials.materialAluminum).contains(elementValue)))
								)
						{
							matches = matches & validateElementAndReport(elementName, htmlToValidate, 
									"HTSUSCode is not valid", errorMsg);
						}else
						{
							matches = matches & validateElementAndReport(elementName, htmlToValidate, 
									"", errorMsg);
						}
						break;
					}
					case "Organization Legal Name": case "Importer Legal Name":
					{
						if(elementValue.trim().equals(""))
						{
							matches = matches & validateElementAndReport(elementName, htmlToValidate, 
									"The Full Organization Legal Name field is required.", errorMsg);
						}
						else
						{
							matches = matches & validateElementAndReport(elementName, htmlToValidate, 
									"", errorMsg);
						}
						break;
					}
					case "Requesting Organization Street Address": case "Importer Street Address":
					{
						if(elementValue.trim().equals(""))
						{
							matches = matches & validateElementAndReport(elementName, htmlToValidate, 
									"The Street Address field is required.", errorMsg);
						}
						else
						{
							matches = matches & validateElementAndReport(elementName, htmlToValidate, 
									"", errorMsg);
						}
						break;
					}
					case "Requesting Organization City": case "Importer City":
					{
						if(elementValue.trim().equals(""))
						{
							matches = matches & validateElementAndReport(elementName, htmlToValidate, 
									"The City field is required.", errorMsg);
						}
						else
						{
							matches = matches & validateElementAndReport(elementName, htmlToValidate, 
									"", errorMsg);
						}
						break;
					}
					case "Requesting Organization State":  case "Importer State":
					{
						if(elementValue.trim().equals("Please Select"))
						{
							matches = matches & validateElementAndReport(elementName, htmlToValidate, 
									"The State field is required.", errorMsg);
						}
						else
						{
							matches = matches & validateElementAndReport(elementName, htmlToValidate, 
									"", errorMsg);
						}
						break;
					}
					case "Requesting Organization Zipcode":  case "Importer Zipcode":
					{
						if(elementValue.trim().equals(""))
						{
							matches = matches & validateElementAndReport(elementName, htmlToValidate, 
									"The Zip Code field is required.", errorMsg);
						}
						else
						{
							if(!elementValue.matches("^[0-9]{5}$"))
							{
								matches = matches & validateElementAndReport(elementName, htmlToValidate, 
									"Zip Code can be 5 characters only and contain only digits", errorMsg);
							}
							else
							{
								matches = matches & validateElementAndReport(elementName, htmlToValidate, 
										"", errorMsg);
							}
						}
						break;
					}
					case "Requesting Organization POC Name": case "Importer POC Name":
					{
						if(elementValue.trim().equals(""))
						{
							matches = matches & validateElementAndReport(elementName, htmlToValidate, 
									"The Point of Contact Name field is required.", errorMsg);
						}
						else
						{
							matches = matches & validateElementAndReport(elementName, htmlToValidate, 
									"", errorMsg);
						}
						break;
					}
					case "Requesting Organization Phone Number":  case "Importer Phone Number":
					{
						if(elementValue.trim().equals(""))
						{
							matches = matches & validateElementAndReport(elementName, htmlToValidate, 
									"The Phone Number field is required.", errorMsg);
						}
						else
						{
							if(!elementValue.matches("^[0-9]*$"))
							{
								matches = matches & validateElementAndReport(elementName, htmlToValidate, 
									"The Phone Number field is not a valid phone number.", errorMsg);
							}
							else
							{
								matches = matches & validateElementAndReport(elementName, htmlToValidate, 
										"", errorMsg);
							}
						}
						break;
					}
					case "Requesting Organization Email Address": case "Importer Email Address":
					{
						if(elementValue.trim().equals(""))
						{
							matches = matches & validateElementAndReport(elementName, htmlToValidate, 
									"The E-mail Address field is required.", errorMsg);
						}
						else
						{
							if(!elementValue.matches("^(.+)@(.+)$"))
							{
								matches = matches & validateElementAndReport(elementName, htmlToValidate, 
									"The E-mail Address field is not a valid e-mail address.", errorMsg);
							}
							else
							{
								matches = matches & validateElementAndReport(elementName, htmlToValidate, 
										"", errorMsg);
							}
						}
						break;
					}
					case "Requesting Organization Website Address": case "Importer Website Address":
					{
						if(!elementValue.contains("http://") && !elementValue.contains("https://") &&
								!elementValue.contains("ftp://") && !elementValue.equals(""))
						{
							matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"The Web site Address field is not a valid fully-qualified http, https, or ftp URL.", errorMsg);
						}
						else
						{
							matches = matches & validateElementAndReport(elementName, htmlToValidate, 
									"", errorMsg);
						}
						break;
					}
					default:
					{
						break;
					}
				}
			}
		}
		return matches;
	}
	
	/**
	 * This method Validates Step 3
	 * @param row: map of test case's data
	 * @return true if all displayed messages are correct, false if not
	 * @exception Exception
	*/
	public static boolean ValidateStepTwo(HashMap<String, String> row) throws Exception
	{
		String  elementName = "", errorMsg="", htmlToValidate="";
		holdSeconds(1);
		clickElementJs(replaceGui(guiMap.get("tabName"), "Step 2") );
		holdSeconds(2);
		boolean matches = true;
		//Validate HTSUS Here
		for (HashMap.Entry<String, String> entry : row.entrySet()) 
		{
			System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
			elementName = entry.getKey().trim();
			String elementValue = entry.getValue();
			if(!elementName.equalsIgnoreCase("Scenarios") && !elementName.equalsIgnoreCase("Active"))
			{
				if (elementName.equalsIgnoreCase("Ownership Activity"))
				{
					scrollToElement(replaceGui(guiMap.get("StepTwoElement"), "Ownership Organization Name"));
				}
				else if (elementName.equalsIgnoreCase("Total Requested Annual Exclusion Quantity"))
				{
					scrollToElement(replaceGui(guiMap.get("StepTwoElement"), "Total Requested Annual Exclusion Quantity"));
				}else if (elementName.equalsIgnoreCase("Percentage Not Available"))
				{
					scrollToElement(replaceGui(guiMap.get("StepTwoElement"), "Manufacture Estimate"));
				}
				else if (elementName.equalsIgnoreCase("Non US Producer Name"))
				{
					scrollToElement(replaceGui(guiMap.get("StepTwoElement"), "Non US Producer Name"));
				}
				else if (elementName.equalsIgnoreCase("Authorized Representative Phone Number"))
				{
					scrollToElement(replaceGui(guiMap.get("StepTwoElement"), "Authorized Representative Website Address"));
				}
				if (elementName.equalsIgnoreCase("Ownership Activity") || 
						  elementName.equalsIgnoreCase("Ownership Headquarters Country")||
						  elementName.equalsIgnoreCase("Exclusion Explanation")
						  ||elementName.equalsIgnoreCase("Non US Producer Headquarters Country"))
				{
					errorMsg = getElementAttribute(replaceGui(guiMap.get("StepTwoElementSelectMessage"), elementName), "text");
					htmlToValidate ="StepTwoElementSelectDiv";
				}
				else
				{
					errorMsg = getElementAttribute(replaceGui(guiMap.get("StepTwoElementMessage"), elementName), "text");	
					htmlToValidate = "StepTwoElementDiv";
				}
				switch(elementName)
				{
				case "Authorized Representative Phone Number":  
				{
					if(!elementValue.matches("[0-9]*"))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
							"The Phone Number field is not a valid phone number.", errorMsg);
					}
					else
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"", errorMsg);
					}
						
					break;
				}
				case "Authorized Representative Email Address": 
				{
					if(!elementValue.matches("^(.+)@(.+)$"))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
							"The E-Mail Address field is not a valid e-mail address.", errorMsg);
					}
					else
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"", errorMsg);
					}
					break;
				}
				case "Authorized Representative Website Address": 
				{
					if(!elementValue.contains("http://") && !elementValue.contains("https://") &&
							!elementValue.contains("ftp://") && !elementValue.equals(""))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
							"The Website Address field is not a valid fully-qualified http, https, or ftp URL.", errorMsg);
					}
					else
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"", errorMsg);
					}
					break;
				}
				case "Ownership Activity":
				{
					if(elementValue.trim().equals("Please Select"))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"Activity is required if Ownership is selected", errorMsg);
					}
					else
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"", errorMsg);
					}
					break;
				}
				case "Ownership Organization Name":
				{
					if(elementValue.trim().equals(""))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"Organization is required if Ownership is selected", errorMsg);
					}
					else
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"", errorMsg);
					}
					break;
				}
				case "Ownership Headquarters Country":
				{
					if(elementValue.trim().equals("Please Select"))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"Headquarters Country is required if Ownership is selected", errorMsg);
					}
					else
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"", errorMsg);
					}
					break;
				}
				case "Total Requested Annual Exclusion Quantity":  
				{
					if(elementValue.trim().equals(""))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"The TotalRequestedAnnualExclusionQuantity field is required.", errorMsg);
					}
					else
					{
						if(!elementValue.matches("^[0-9]*$"))
						{
							matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"Invalid Value", errorMsg);
						}
						else
						{
							matches = matches & validateElementAndReport(elementName, htmlToValidate, 
									"", errorMsg);
						}
					}
					break;
				}
				case "Total Requested Average Annual Consumption":  
				{
					if(elementValue.trim().equals(""))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"The AvgAnnualConsumption field is required.", errorMsg);
					}
					else
					{
						if(!elementValue.matches("^[0-9]*$"))
						{
							matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"Invalid Value", errorMsg);
						}
						else
						{
							matches = matches & validateElementAndReport(elementName, htmlToValidate, 
									"", errorMsg);
						}
					}
					break;
				}
				case "Exclusion Explanation":
				{
					if(elementValue.trim().equals("Please Select"))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"Please select a value from dropdown", errorMsg);
					}
					else
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"", errorMsg);
					}
					break;
				}
				case "Percentage Not Available":
				{
					if(!elementValue.matches("[0-9]+|[0-9]+\\.?[0-9]+") && !elementValue.equals(""))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"Invalid Value", errorMsg);
					}
					else if(elementValue.matches("[0-9]+|[0-9]+\\.?[0-9]+") 
							&& (Float.parseFloat(elementValue)<0 
							|| Float.parseFloat(elementValue)>100))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"Allowed range (0-100)", errorMsg);
					}else
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"", errorMsg);
					}
					break;
				}
				 case "Delivery Estimate": 
				{
					if(!elementValue.matches("[0-9]*"))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
							"Invalid Value", errorMsg);
					}
					else if(elementValue.equals(""))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"The DeliveryEstimate field is required.", errorMsg);
					}else
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"", errorMsg);
					}
					break;
				}
				 case "Manufacture Estimate": 
				{
					if(!elementValue.matches("[0-9]*"))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
							"Invalid Value", errorMsg);
					}
					else if(elementValue.equals(""))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"The ManufactureEstimate field is required.", errorMsg);
					}else
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"", errorMsg);
					}
					break;
				}
				 case "Shipment Days Estimate": 
				{
					if(!elementValue.matches("[0-9]*"))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
							"Invalid Value", errorMsg);
					}
					else if(elementValue.equals(""))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"The ShipmentDaysEstimate field is required.", errorMsg);
					}else
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"", errorMsg);
					}
					break;
				}
				case "Shipment Quantity Estimate":  
				{
					if(elementValue.trim().equals(""))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"The ShipmentQtyEstimate field is required.", errorMsg);
					}
					else
					{
						if(!elementValue.matches("^[0-9]*$"))
						{
							matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"Invalid Value", errorMsg);
						}
						else
						{
							matches = matches & validateElementAndReport(elementName, htmlToValidate, 
									"", errorMsg);
						}
					}
					break;
				}
				case "Non US Producer Name":
				{
					if(elementValue.trim().equals(""))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"Producer Name is required if On-Behalf field is selected", errorMsg);
					}
					else
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"", errorMsg);
					}
					break;
				}
				case "Non US Producer Headquarters Country":
				{
					if(elementValue.trim().equals("Please Select"))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"Headquarter Country is required if On-Behalf Field is selected", errorMsg);
					}
					else
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"", errorMsg);
					}
					break;
				}
				default:
				{
					break;
				}
				}
			}
		}
		return matches;
	}
	
	/**
	 * This method Validates Step 3
	 * @param row: map of test case's data
	 * @return true if all displayed messages are correct, false if not
	 * @exception Exception
	*/
	public static boolean ValidateStepThree(HashMap<String, String> row) throws Exception
	{
		boolean matches = true;
		String minVal="", maxVal="", elementName = "";
		String minMessage, maxMessage;
		holdSeconds(1);
		clickElementJs(replaceGui(guiMap.get("tabName"), "Step 3"));
		holdSeconds(2);
		int movePixel = 1;
		clickElementJs(replaceGui(guiMap.get("tabName"), "Step 3"));
		holdSeconds(1);
		scrollToElement(guiMap.get("ProductDescription"));
		String descMessage = getElementAttribute(guiMap.get("ProductDescriptionMessage"), "text");
		if (row.get("Product Description").trim().equals("") )
		{
			matches = matches & validateElementAndReport("Product Description", "ProductDescription", 
					"The Description field is required.",descMessage);
		}
		else
		{
			matches = matches & validateElementAndReport("Product Description", "ProductDescription", 
					"",descMessage);
		}
		scrollToElement(replaceGui(guiMap.get("ElementMinValue"), "Aluminum"));
		int currentWait = setBrowserTimeOut(2);
		for (HashMap.Entry<String, String> entry : row.entrySet()) 
		{
			movePixel++;
			System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
			elementName = entry.getKey().trim();
			if(!elementName.equalsIgnoreCase("Scenarios") && !elementName.equalsIgnoreCase("Active") 
					&& !elementName.equalsIgnoreCase("Product Description"))
			{
				if (elementName.equalsIgnoreCase("Thickness") )
				{
					scrollToElement(replaceGui(guiMap.get("ElementMinValue"), "Thickness"));
					movePixel=1;
				}else if (elementName.equalsIgnoreCase("Tensile Strength"))
				{
					scrollToElement(replaceGui(guiMap.get("ElementMinValue"), "Tensile Strength"));
					movePixel=1;
				}else if (elementName.equalsIgnoreCase("Tensile Strength"))
				{
					scrollToElement(replaceGui(guiMap.get("ElementMinValue"), "Tensile Strength"));
					movePixel=1;
				}else if (elementName.equalsIgnoreCase("Elogation"))
				{
					scrollToElement(replaceGui(guiMap.get("ElementMinValue"), "Elogation"));
					movePixel=1;
				}
				else if (elementName.equalsIgnoreCase("Epstein"))
				{
					scrollToElement(replaceGui(guiMap.get("ElementMinValue"), "Epstein"));
					movePixel=1;
				}else
				{
					if (movePixel % 5 ==0) scrollByPixel(215);
				}
				minVal = entry.getValue().substring(0,entry.getValue().indexOf("|"));
				maxVal = entry.getValue().substring(entry.getValue().indexOf("|")+1,entry.getValue().length());
				minMessage = getElementAttribute(replaceGui(guiMap.get("ElementMinValueMessage"), elementName), "text");
				maxMessage = getElementAttribute(replaceGui(guiMap.get("ElementMaxValueMessage"), elementName), "text");
				/////minimum
				//minimum is not a number
				String htmlToValidate = "ElementMinValueMessageTd";
				if(!minVal.matches("[0-9]+|[0-9]+\\.?[0-9]+"))
				{
					if (minVal.equals(""))
					{
						matches = matches &  validateMinMaxElementAndReport(elementName, htmlToValidate, "The Minimum field is required.", 
								minMessage, minVal, "Minimum");
					}
					else 
					{
						matches = matches & validateMinMaxElementAndReport(elementName, htmlToValidate, "Invalid Value", 
								minMessage, minVal, "Minimum");
					}
				}
				else //minimum is a number
				{ 
					if(Arrays.asList(ElementsInRange).contains(elementName)&&(Float.parseFloat(minVal)<0 
							|| Float.parseFloat(minVal)>100) )// min out of range
					{
						matches = matches & validateMinMaxElementAndReport(elementName, htmlToValidate, "Allowed range (0-100)", 
								minMessage, minVal, "Minimum");
						
					}
					else // min in range range
					{
						matches = matches & validateMinMaxElementAndReport(elementName, htmlToValidate, "", 
								minMessage, minVal, "Minimum");
					}
				}
				htmlToValidate = "ElementMaxValueMessageTd";
				/////maximum is not a number
				if(!maxVal.matches("[0-9]+|[0-9]+\\.?[0-9]+"))
				{
					if (maxVal.equals(""))
					{
						matches = matches & validateMinMaxElementAndReport(elementName, htmlToValidate, 
								"The Maximum field is required.", maxMessage, maxVal, "Maximum");
					}
					else 
					{
						matches = matches & validateMinMaxElementAndReport(elementName, htmlToValidate, "Invalid Value", 
								maxMessage, maxVal, "Maximum");
					}
				}
				else
				{ // maximum is a number
					if(Arrays.asList(ElementsInRange).contains(elementName)&&(Float.parseFloat(maxVal)<0 
							|| Float.parseFloat(maxVal)>100) )// min out of range
					{
						matches = matches & validateMinMaxElementAndReport(elementName, htmlToValidate, "Allowed range (0-100)", 
								maxMessage, maxVal, "Maximum");
						
					}
					else // min in range
					{
						matches = matches & validateMinMaxElementAndReport(elementName, htmlToValidate, "", 
								maxMessage, maxVal, "Maximum");
					}
				}
			}
		}
		setBrowserTimeOut(currentWait);
		return matches;
	}
	/**
	 * This method Validates Step 5
	 * @param row: map of test case's data
	 * @return true if all displayed messages are correct, false if not
	 * @exception Exception
	*/
	public static boolean ValidateStepFour(HashMap<String, String> row) throws Exception
	{
		boolean matches = true;
		holdSeconds(1);
		clickElementJs(replaceGui(guiMap.get("tabName"), "Step 4") );
		holdSeconds(2);
		String errorMsg, elementValue;
		scrollToElement(guiMap.get("ApplicationSuitability"));
		elementValue = row.get("Application Suitability");
		errorMsg = getElementAttribute(guiMap.get("ApplicationSuitabilityMessage"), "text");
		if(elementValue.trim().equals(""))
		{
			matches = matches & validateElementAndReport("Application Suitability", "ApplicationSuitability", 
					"The ApplicationSuitability field is required.", errorMsg);
		}
		else
		{
			matches = matches & validateElementAndReport("Application Suitability", "ApplicationSuitability", 
					"", errorMsg);
		}
		scrollToElement(guiMap.get("OriginCountry"));
		String originCountry = row.get("Origin Country");
		String exportCountry = row.get("Export Country");
		errorMsg = getElementAttribute(guiMap.get("OriginExportCountryMessage"), "text");
		if(originCountry.equals("Please Select") || exportCountry.equals("Please Select"))
		{
			matches = matches & validateElementAndReport("Application Suitability", "OriginExportCountryTable", 
					"At least one country should be provided.", errorMsg);
		}
		else
		{
			matches = matches & validateElementAndReport("Application Suitability", "OriginExportCountryTable", 
					"", errorMsg);
		}
		scrollToElement(guiMap.get("CBPDistinguishComments"));
		elementValue = row.get("CBP Distinguish Comments");
		errorMsg = getElementAttribute(guiMap.get("CBPDistinguishCommentsMessage"), "text");
		if(elementValue.trim().equals(""))
		{
			matches = matches & validateElementAndReport("CBP Distinguish Comments", "CBPDistinguishComments", 
					"The Provide a detailed explanation as to how U.S. Customs and Border Protection (CBP) will "
					+ "be able to reasonably distinguish the product subject to the Exclusion Request at time of entry, without adding "
					+ "undue burden to their current entry system and procedures. field is required.", errorMsg);
		}
		else
		{
			matches = matches & validateElementAndReport("Application Suitability", "CBPDistinguishComments", 
					"", errorMsg);
		}
		return matches;
	}
	
	/**
	 * This method Validates Step 5
	 * @param row: map of test case's data
	 * @return true if all displayed messages are correct, false if not
	 * @exception Exception
	*/
	public static boolean ValidateStepFive(HashMap<String, String> row) throws Exception
	{
		boolean matches = true;
		holdSeconds(1);
		clickElementJs(replaceGui(guiMap.get("tabName"), "Step 5") );
		holdSeconds(2);
		String errorMsg, elementValue;
		scrollToElement(replaceGui(guiMap.get("StepFiveElement"),
				"Submission Certification Authorizing Official Phone Number"));
		elementValue = row.get("Submission Certification Authorizing Official Company Name");
		errorMsg = getElementAttribute(replaceGui(guiMap.get("StepFiveElementMessage"),
				"Submission Certification Authorizing Official Company Name"), "text");
		if(elementValue.trim().equals(""))
		{
			matches = matches & validateElementAndReport("Submission Certification Authorizing Official Company Name", 
					"StepFiveElementDiv", "The Company Name field is required.", errorMsg);
		}
		else
		{
			matches = matches & validateElementAndReport("Submission Certification Authorizing Official Company Name", 
					"StepFiveElementDiv", "", errorMsg);
		}
		elementValue = row.get("Submission Certification Authorizing Official Name");
		errorMsg = getElementAttribute(replaceGui(guiMap.get("StepFiveElementMessage"),
				"Submission Certification Authorizing Official Name"), "text");
		if(elementValue.trim().equals(""))
		{
			matches = matches & validateElementAndReport("Submission Certification Authorizing Official Name", 
					"StepFiveElementDiv", "The Name of Authorizing Official field is required.", errorMsg);
		}
		else
		{
			matches = matches & validateElementAndReport("Submission Certification Authorizing Official Name", 
					"StepFiveElementDiv", "", errorMsg);
		}
		elementValue = row.get("Submission Certification Authorizing Official Title");
		errorMsg = getElementAttribute(replaceGui(guiMap.get("StepFiveElementMessage"),
				"Submission Certification Authorizing Official Title"), "text");
		if(elementValue.trim().equals(""))
		{
			matches = matches & validateElementAndReport("Submission Certification Authorizing Official Title", 
					"StepFiveElementDiv", "The Title of Authorizing Official field is required.", errorMsg);
		}
		else
		{
			matches = matches & validateElementAndReport("Submission Certification Authorizing Official Title", 
					"StepFiveElementDiv", "", errorMsg);
		}
		elementValue = row.get("Submission Certification Authorizing Official Phone Number");
		errorMsg = getElementAttribute(replaceGui(guiMap.get("StepFiveElementMessage"),
				"Submission Certification Authorizing Official Phone Number"), "text");
		
		if(elementValue.equals(""))
		{
			matches = matches & validateElementAndReport("Submission Certification Authorizing Official Phone Number", 
					"StepFiveElementDiv", "The Phone Number field is required.", errorMsg);
		}
		else if(!elementValue.matches("[0-9]+"))
		{
			matches = matches & validateElementAndReport("Submission Certification Authorizing Official Phone Number", 
					"StepFiveElementDiv", "The Phone Number field is not a valid phone number.", errorMsg);
		}else
		{
			matches = matches & validateElementAndReport("Submission Certification Authorizing Official Phone Number", 
					"StepFiveElementDiv", "", errorMsg);
		}
		elementValue = row.get("Submission Certification Authorizing Official Email");
		errorMsg = getElementAttribute(replaceGui(guiMap.get("StepFiveElementMessage"),
				"Submission Certification Authorizing Official Email"), "text");
		if(elementValue.matches(""))
		{
			matches = matches & validateElementAndReport("Submission Certification Authorizing Official Email", 
					"StepFiveElementDiv", "The Email of Authorizing Official field is required.", errorMsg);
		}
		else if(!elementValue.matches("^(.+)@(.+)$"))
		{
			matches = matches & validateElementAndReport("Submission Certification Authorizing Official Email", 
					"StepFiveElementDiv", "The Email of Authorizing Official field is not a valid e-mail address.", errorMsg);
		}else
		{
			matches = matches & validateElementAndReport("Submission Certification Authorizing Official Email", 
					"StepFiveElementDiv", "", errorMsg);
		}
		elementValue = row.get("Submission Certification POC Email");
		errorMsg = getElementAttribute(replaceGui(guiMap.get("StepFiveElementMessage"),
				"Submission Certification POC Email"), "text");
		if(elementValue.matches("^(.+)@(.+)$") || elementValue.equals(""))
		{
			matches = matches & validateElementAndReport("Submission Certification POC Email", 
					"StepFiveElementDiv", "", errorMsg);
		}
		else
		{
			matches = matches & validateElementAndReport("Submission Certification POC Email", 
					"StepFiveElementDiv", "The E-mail Address field is not a valid e-mail address.", errorMsg);
		}
		elementValue = row.get("Submission Certification POC Phone Number");
		errorMsg = getElementAttribute(replaceGui(guiMap.get("StepFiveElementMessage"),
				"Submission Certification POC Phone Number"), "text");
		
		if(!elementValue.matches("[0-9]*"))
		{
			matches = matches & validateElementAndReport("Submission Certification POC Phone Number", 
					"StepFiveElementDiv", "The Phone Number field is not a valid phone number.", errorMsg);
		}
		else
		{
			matches = matches & validateElementAndReport("Submission Certification POC Phone Number", 
					"StepFiveElementDiv", "", errorMsg);
		}
		return matches;
	}
	
	/**
	 * This method preview the form after all steps are 
	 * filled 
	 * @exception Exception
	*/
	public static boolean previewBisForm() throws Exception
	{
		holdSeconds(2);//
		clickElementJs(replaceGui(guiMap.get("tabName"), "Step 5"));
		holdSeconds(2);//
		scrollToElement(guiMap.get("PreviewBeforeSubmit"));
		clickElementJs(guiMap.get("PreviewBeforeSubmit"));
		holdSeconds(2);
		int currentWait = setBrowserTimeOut(4);
		//scrollToElement(guiMap.get("submitForm"));
		//clickElementJs(guiMap.get("submitForm"));
		if(checkElementExists(guiMap.get("submitForm")))
		{
			setBrowserTimeOut(currentWait);
			return true;
		}
		else
		{
			setBrowserTimeOut(currentWait);
			return false;
		}
	}
	
	/**
	 * This method validates and reports min/max values
	 * @param elementName: element name
	 * @param htmlElment: HTML name on GUI
	 * @param expectedMsg: expected message
	 * @param actualMsg: actual message
	 * @return true if the expected value is as actual, false if not
	 * @exception Exception
	*/
	public static boolean validateElementAndReport(String elementName,
													String htmlElment,
													String expectedMsg,
													String actualMsg) throws Exception
	{
		String displayedMessage = expectedMsg.equals("")? "No error message":expectedMsg;
		boolean match = true;
		if(expectedMsg.equalsIgnoreCase(actualMsg))
		{
			highlightElement(replaceGui(guiMap.get(htmlElment), elementName), "green");
			updateHtmlReport("validate ["+elementName+"]",  
			"'"+displayedMessage+"' should display", "As expected", 
			"VP", "pass", elementName);
			unHighlightElement(replaceGui(guiMap.get(htmlElment), elementName));
		}
		else
		{
			highlightElement(replaceGui(guiMap.get(htmlElment), elementName), "red");
			updateHtmlReport("validate ["+elementName+"]",  
			"'"+displayedMessage+"' should display", "Not as expected", 
			"VP", "fail", elementName);
			unHighlightElement(replaceGui(guiMap.get(htmlElment), elementName));
			match = false;
		}
		return match;
	}
	/**
	 * This method validates and reports min/max values
	 * @param elementName: element name
	 * @param htmlElment: HTML name on GUI
	 * @param expectedMsg: expected message
	 * @param actualMsg: actual message
	 * @param minMaxVal element's value
	 * @param MinOrMax min or max
	 * @return  true if the expected value is as actual, false if not
	 * @exception Exception
	*/
	public static boolean validateMinMaxElementAndReport(String elementName,
														 String htmlElment,
														 String expectedMsg,
														 String actualMsg,
														 String minMaxVal,
														 String MinOrMax) throws Exception
	{
		String displayedMessage = expectedMsg.equals("")? "No error message":expectedMsg;
		boolean match = true;
		if(expectedMsg.equalsIgnoreCase(actualMsg))
		{
			highlightElement(replaceGui(guiMap.get(htmlElment), elementName), "green");
			updateHtmlReport("validate ["+elementName+ " - " + MinOrMax +" = "+minMaxVal+"]",  
					"'"+displayedMessage+"' should display", "As expected", 
					"VP", "pass", elementName+ " - " + MinOrMax);
			unHighlightElement(replaceGui(guiMap.get(htmlElment), elementName));
		}
		else
		{
			highlightElement(replaceGui(guiMap.get(htmlElment), elementName), "red");
			updateHtmlReport("validate ["+elementName+ " - " + MinOrMax +" = "+minMaxVal+"]",  
					"'"+displayedMessage+"' should display", "Not as expected", 
					"VP", "fail", elementName+ " - " + MinOrMax);
			unHighlightElement(replaceGui(guiMap.get(htmlElment), elementName));
			match = false;
		}
		return match;
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
}
