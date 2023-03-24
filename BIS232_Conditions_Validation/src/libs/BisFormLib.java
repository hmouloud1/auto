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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import ReportLibs.HtmlReport;
import bsh.ParseException;

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
		if(! checkElementExists(guiMap.get("HomePage")))
		{
			failTestSuite("Login to BIS 232 App", "User is able to login", 
				"Not as expected", "Step", "fail", "Login failed");
			loginStatus = false;
		}else
		{
			highlightElement(guiMap.get("HomePage"), "green");
			holdSeconds(2);
			updateHtmlReport("Login to BIS232 App",  "User able to login", "As expected", 
					"Step", "pass", "Login to BIS 232");
			clickElementJs(guiMap.get("CreateNewExclusionRequest"));
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
		scrollToElement(replaceGui(guiMap.get("ElementMinValue"), "Aluminum"));
		for (HashMap.Entry<String, String> entry : row.entrySet()) 
		{
			System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
			elementName = entry.getKey().trim();
			if(!elementName.equalsIgnoreCase("Scenarios") && !elementName.equalsIgnoreCase("Active")
					&& !elementName.equalsIgnoreCase("Product Description"))
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
	 * This method submit the form after all steps are 
	 * filled 
	 * @exception Exception
	*/
	public static void submitBisForm() throws Exception
	{
		clickElementJs(replaceGui(guiMap.get("tabName"), "Step 5"));
		holdSeconds(2);
		scrollToElement(guiMap.get("submitForm"));
		clickElementJs(guiMap.get("submitForm"));
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
	/**
	 * This method reads number from the screen
	 * @param jObj: json object containing form information
	 * @param productType: product type, steel, aluminum
	 * @param conditions: list of conditions to be checked
	 * @return number in integer format
	 * @exception Exception
	*/
	public static boolean ValidateConditions(JSONObject jObj, 
											 String productType, 
											 String conditions) throws Exception
	{
		boolean ovralResult=true;
		String conditionDetails;
		if (conditions.equals("")) return true;
		String[] listConditions = conditions.split(",");
		for(int i=0; i<listConditions.length; i++)
		{ 
			String condition = listConditions[i];
			if (productType.equalsIgnoreCase("Steel"))
			{
				switch(condition.toLowerCase())
				{
				case "condition 1":
				{
					
					break;
				}
				case "condition 2":
				{
					conditionDetails ="Stainless ...then the maximum percentage of carbon must be 1.2 percent or "
							+ "less and the minimum percentage of chromium must be 10.5 percent or more.";
					String carbonMax = getProdValue(jObj, "ChemicalComposition", "Carbon", "Maximum");
					String chromiumMin = getProdValue(jObj, "ChemicalComposition", "Chromium", "Minimum");
					ovralResult = ovralResult & checkAndReport("Condition 2", conditionDetails, (Float.parseFloat(carbonMax)<=1.2 
							&& Float.parseFloat(chromiumMin)>=10.5));
					break;
				}
				case "condition 3":{
		
				break;
				}
				case "condition 4":
				{
					conditionDetails = "...then the minimum width must be 600 mm or more.";
					String widthMin = getProdValue(jObj, "ProductDimensions", "Width", "Minimum");
					ovralResult = ovralResult & checkAndReport("Condition 4", conditionDetails, (Float.parseFloat(widthMin)>=600));
					break;
				}
				case "condition 5":
				{
					conditionDetails = "...then the maximum width must be less than 600 mm.";
					String widthMax = getProdValue(jObj, "ProductDimensions", "Width", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 5", conditionDetails, (Float.parseFloat(widthMax)<600)); 
					break;
				}
				case "condition 6":{
		
				break;
				}
				case "condition 7":{
		
				break;
				}
				case "condition 8":{
		
				break;
				}
				case "condition 9":{
		
				break;
				}
				case "condition 10":{
		
				break;
				}
				case "condition 11":{
		
				break;
				}
				case "condition 12":
				{
					conditionDetails = "Silicon electrical steel ...then the maximum carbon must be equal to or less than 0.08 percent, "
							+ "and the minimum silicon must be equal to or greater than 0.6 percent, and the maximum silicon "
							+ "must be equal to or less than 6 percent, and the maximum aluminum must be equal to or less than 1 percent.";
					String carbonMax = getProdValue(jObj, "ChemicalComposition", "Carbon", "Maximum");
					String siliconMin = getProdValue(jObj, "ChemicalComposition", "Silicon", "Minimum");
					String siliconMax = getProdValue(jObj, "ChemicalComposition", "Silicon", "Maximum");
					String aluminumMax = getProdValue(jObj, "ChemicalComposition", "Aluminum", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 12", conditionDetails, (Float.parseFloat(carbonMax)<=0.08 
							&& Float.parseFloat(siliconMin)>=0.6 && Float.parseFloat(siliconMax) <=6 
							&& Float.parseFloat(aluminumMax) <=1)); 
					break;
				}
				case "condition 13":
				{
					conditionDetails = "Silico-manganese steel ...then the maximum carbon must be equal to or less than 0.7 "
							+ "percent, and the minimum manganese must be equal to or greater than 0.5 percent, and the maximum "
							+ "manganese must be equal to or less than 1.9 percent, and the minimum silicon must be equal to or "
							+ "greater than 0.6 percent, and the maximum silicon must be equal to or less than 2.3 percent.";
					String carbonMax = getProdValue(jObj, "ChemicalComposition", "Carbon", "Maximum");
					String manganeseMin = getProdValue(jObj, "ChemicalComposition", "Magnesium", "Minimum");
					String manganeseMax = getProdValue(jObj, "ChemicalComposition", "Magnesium", "Maximum");
					String siliconMin = getProdValue(jObj, "ChemicalComposition", "Silicon", "Minimum");
					String siliconMax = getProdValue(jObj, "ChemicalComposition", "Silicon", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 13", conditionDetails, (Float.parseFloat(carbonMax)<=0.7 
							&& Float.parseFloat(manganeseMin)>=0.5 && Float.parseFloat(manganeseMax) <=1.9 
							&& Float.parseFloat(siliconMin)>=0.6 && Float.parseFloat(siliconMax)<=2.3)); 
					break;
				}
				case "condition 14":{
		
				break;
				}
				case "condition 15":
				{
					conditionDetails = "Razor blade steel ...then the maximum thickness must be equal to or less than "
							+ "0.25 mm, and the maximum width must be equal to or less than 23 mm, and the maximum percent "
							+ "chromium must be equal to or less than 14.7.";
					String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
					String widthMax = getProdValue(jObj, "ProductDimensions", "Width", "Maximum");
					String chromiumMax = getProdValue(jObj, "ChemicalComposition", "Chromium", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 15", conditionDetails, (Float.parseFloat(thicknessMax)<=0.25 
							&& Float.parseFloat(widthMax)<=23 && Float.parseFloat(chromiumMax)<=14.7));
					break;
				}
				case "condition 16":
				{
					conditionDetails = "Universal mill plate ...then the minimum width must be greater than 150 mm,"
							+ " and the maximum width must be equal to or less than 1250 mm, and theminimum thickness must"
							+ " be equal to or greater than 4 mm.";
					String widthMin = getProdValue(jObj, "ProductDimensions", "Width", "Minimum");
					String widthMax = getProdValue(jObj, "ProductDimensions", "Width", "Maximum");
					String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");					
					ovralResult = ovralResult & checkAndReport("Condition 16", conditionDetails, (Float.parseFloat(widthMin)>150
							&& Float.parseFloat(widthMax)<=1250 && Float.parseFloat(thicknessMin)>=4)); 
					break;
				}
				case "condition 17":
				{
					conditionDetails = "Heat-resisting steel ...then the maximum carbon must be less than 0.3 percent,"
							+ " and the minimum chromium must be equal to or greater than 4 percent, and the maximum "
							+ "chromium must be less than 10.5 percent.";
					
					String carbonMax = getProdValue(jObj, "ChemicalComposition", "Carbon", "Maximum");
					String chromiumMin = getProdValue(jObj, "ChemicalComposition", "Chromium", "Minimum");
					String chromiumMax = getProdValue(jObj, "ChemicalComposition", "Chromium", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 17", conditionDetails, (Float.parseFloat(carbonMax)<0.3 
							&& Float.parseFloat(chromiumMin)>=4 && Float.parseFloat(chromiumMax)<10.5)); 
					break;
				}
				case "condition 18":{
		
				break;
				}
				case "condition 19":
				{
					conditionDetails = "Welding quality wire rod ...then the maximum percent carbon must be less than 0.2, and the maximum "
							+ "percent sulfur must be less than 0.04, and the maximum percentphosphorus must be less than 0.04, and the "
							+ "maximum outside diameter must be less than 10.0 mm.";
					String carbonMax = getProdValue(jObj, "ChemicalComposition", "Carbon", "Maximum");
					String sulfurMax = getProdValue(jObj, "ChemicalComposition", "Sulfur", "Maximum");
					String phosphorusMax = getProdValue(jObj, "ChemicalComposition", "Phosphorus", "Maximum");
					String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 19", conditionDetails, (Float.parseFloat(carbonMax)<0.2 && Float.parseFloat(sulfurMax)<0.04 
							&& Float.parseFloat(phosphorusMax)<0.04 && Float.parseFloat(outsideDiameterMax)<10)); 
					break;
				}
				case "condition 20":{
		
				break;
				}
				case "condition 21":{
		
				break;
				}
				case "condition 22":
				{
					conditionDetails = "High nickel alloy steel ...then the minimum percent nickel must be "
							+ "equal to or greater than 24.";
					String nickelMin = getProdValue(jObj, "ChemicalComposition", "Nickel", "Minimum");
					ovralResult = ovralResult & checkAndReport("Condition 22", conditionDetails, (Float.parseFloat(nickelMin)>=24)); 
					break;
				}
				case "condition 23":{
		
				break;
				}
				case "condition 24":{
		
				break;
				}
				case "condition 25":
				{
					conditionDetails = "Chapter 73 stainless ...then the maximum percentage of carbon must be 1.2 percent or "
							+ "less and the minimum percentage of chromium must be 10.5 percent or more.";
					String carbonMax = getProdValue(jObj, "ChemicalComposition", "Carbon", "Maximum");
					String chromiumMin = getProdValue(jObj, "ChemicalComposition", "Chromium", "Minimum");
					ovralResult = ovralResult & checkAndReport("Condition 25", conditionDetails, (Float.parseFloat(carbonMax)<=1.2
							&& Float.parseFloat(chromiumMin)>=10.5)); 
					break;
				}
				case "condition 26":
				{
					conditionDetails = "...then the maximum outside diameter and the minimum outside"
							+ " diameter must be equal to or less than 114.3 mm.";
					String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
					String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
					ovralResult = ovralResult & checkAndReport("Condition 26", conditionDetails, (Float.parseFloat(outsideDiameterMax)<=114.3 
							&& Float.parseFloat(outsideDiameterMin)<=114.3)); 
					break;
				}
				case "condition 27":
				{
					conditionDetails = "...then the minimum outside diameter must be equal to or greater than 114.3 mm and"
							+ " less than 406.4 mm, and the maximum outside diameter must be equal to or greater than 114.3 "
							+ "mm and less than 406.4 mm.";
					String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
					String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
					
					ovralResult = ovralResult & checkAndReport("Condition 27", conditionDetails, (Float.parseFloat(outsideDiameterMin)>=114.3 
							&& Float.parseFloat(outsideDiameterMin)<406.4 && Float.parseFloat(outsideDiameterMax)>=114.3
							&& Float.parseFloat(outsideDiameterMax)<406.4)); 
					break;
				}
				case "condition 28":
				{
					conditionDetails = "...then the maximum outside diameter and the minimum outside "
							+ "diameter must be greater than 406.4 mm.";
					String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
					String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
					ovralResult = ovralResult & checkAndReport("Condition 28", conditionDetails, (Float.parseFloat(outsideDiameterMax)>406.4 
							&& Float.parseFloat(outsideDiameterMin)>406.4)); 
					break;
				}
				case "condition 29":
				{
					conditionDetails = "...then the minimum outside diameter must be equal to or greater than 114.3 mm"
							+ " and less than 215.9 mm, and the maximum outside diameter must be equal to or greater "
							+ "than 114.3 mm and less than 215.9 mm.";
					String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
					String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 29", conditionDetails, (Float.parseFloat(outsideDiameterMin)>=114.3 &&
							Float.parseFloat(outsideDiameterMin)<215.9&& Float.parseFloat(outsideDiameterMax)>=114.3 
							&& Float.parseFloat(outsideDiameterMax)<215.9)); 
					break;
				}
				case "condition 30":
				{
					conditionDetails = "...then the maximum thickness must be less than 12.7 mm.";
					String cthicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 30", conditionDetails, (Float.parseFloat(cthicknessMax)<12.7 ));
					break;
				}
				case "condition 31":
				{
					conditionDetails = "...then the minimum thickness must be greater than or equal to 12.7 mm.";
					String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
					ovralResult = ovralResult & checkAndReport("Condition 31", conditionDetails, (Float.parseFloat(thicknessMin)>=12.7)); 
					break;
				}
				case "condition 32":
				{
					conditionDetails = "....then the outside diameter must be equal to or less than 168.3 mm.";
					break;
				}
				case "condition 33":
				{
					conditionDetails = "...then the minimum thickness must be greater than 9.5 mm.";
					String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
					ovralResult = ovralResult & checkAndReport("Condition 33", conditionDetails, (Float.parseFloat(thicknessMin)>9.5 )); 
					break;
				}
				case "condition 34":
				{
					conditionDetails = "...then the maximum thickness must be equal to or less than 9.5 mm.";
					String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 34", conditionDetails, (Float.parseFloat(thicknessMax)<=9.5)); 
					break;
				}
				case "condition 35":
				{
					conditionDetails = "...then the outside diameter must be greater than 168.3 mm.";
					break;
				}
				case "condition 36":
				{
					conditionDetails = "...then the outside diameter must be less than 215.9 mm.";
					break;
				}
				case "condition 37":
				{
					conditionDetails = "...then the minimum outside diameter must be equal to or greater than 215.9 mm "
							+ "and equal to or less than 406.4 mm, and the maximum outside diameter must be equal to or"
							+ " greater than 215.9 mm and equal to or less than 406.4 mm.";
					String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
					String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 37", conditionDetails, (Float.parseFloat(outsideDiameterMin)>=215.9
							&& Float.parseFloat(outsideDiameterMin)<=406.4 && Float.parseFloat(outsideDiameterMax)>=215.9
							&& Float.parseFloat(outsideDiameterMax)<=406.4));
					break;
				}
				case "condition 38":
				{
					conditionDetails = "...then the minimum outside diameter must be equal to or greater than 215.9 mm and equal "
							+ "to or less than 285.8 mm, and the maximum outside diameter must be equal to or greater than 285.8 mm "
							+ "and equal to or less than 406.4 mm.";
					String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
					String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 38", conditionDetails, (Float.parseFloat(outsideDiameterMin)>=215.9 
							&& Float.parseFloat(outsideDiameterMin)<=285.8 && Float.parseFloat(outsideDiameterMax)>=285.8 
							&& Float.parseFloat(outsideDiameterMax)<=406.4));
					break;
				}
				case "condition 39":
				{
					conditionDetails = "...then the minimum outside diameter must be greater than 285.8 mm and equal to or "
							+ "less than 406.4 mm, and the maximum outside diameter must be greater than 285.8 mm and equal "
							+ "to or less than 406.4 mm.";
					String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
					String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 39", conditionDetails, (Float.parseFloat(outsideDiameterMin) > 285.8 
							&& Float.parseFloat(outsideDiameterMin)<=406.4 && Float.parseFloat(outsideDiameterMax)> 285.8
							&& Float.parseFloat(outsideDiameterMax)<=406.4 )); 
					break;
				}
				case "condition 40":
				{
					conditionDetails = "...then the minimum outside diameter must be greater than 406.4 mm.";
					String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
					ovralResult = ovralResult & checkAndReport("Condition 40", conditionDetails, (Float.parseFloat(outsideDiameterMin) > 406.4)); 
					break;
				}
				case "condition 41":
				{
					conditionDetails = "...then the minimum outside diameter and the maximum outside diameter must be less than 38.1 mm.";
					String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
					String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 41", conditionDetails, (Float.parseFloat(outsideDiameterMin) < 38.1 
							&& Float.parseFloat(outsideDiameterMax) < 38.1));
					break;
				}
				case "condition 42":
				{
					conditionDetails = "...then the minimum outside diameter must be equal to or greater than 38.1 mm and less"
							+ " than 190.5 mm, and the maximum outside diameter must be greater than 38.1 mm and less than 190.5 mm";
					String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
					String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 42", conditionDetails, (Float.parseFloat(outsideDiameterMin) >= 38.1 
							&& Float.parseFloat(outsideDiameterMin) < 190.5	&& Float.parseFloat(outsideDiameterMax)> 38.1
							&& Float.parseFloat(outsideDiameterMax) < 190.5 )); 
					break;
				}
				case "condition 43":
				{
					conditionDetails = "...then the minimum outside diameter and the maximum outside diameter must be greater than 285.8 mm.";
					String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
					String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 43", conditionDetails, (Float.parseFloat(outsideDiameterMin) > 285.8 
							&& Float.parseFloat(outsideDiameterMax) > 285.8));
					break;
				}
				case "condition 44":
				{
					conditionDetails = "...then the maximum thickness must be less than 6.4 mm.";
					String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 44", conditionDetails, (Float.parseFloat(thicknessMax)< 6.4 )); 
					break;
				}
				case "condition 45":
				{
					conditionDetails = "...then the minimum thickness must be equal to or greater than 6.4 mm, "
							+ "and the maximum thickness must be equal to or less than 12.7 mm.";
					String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
					String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 45", conditionDetails, (Float.parseFloat(thicknessMin)>= 6.4 
							&& Float.parseFloat(thicknessMax) <=12.7)); 
					break;
				}
				case "condition 46":
				{
					conditionDetails = "...then the minimum outside diameter must be greater than 114.3 mm and less than 190.5 mm, "
							+ "and the maximum outside diameter must be greater than 114.3 mm and less than 190.5 mm.";
					String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
					String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 46", conditionDetails, (Float.parseFloat(outsideDiameterMin) > 114.3 
							&& Float.parseFloat(outsideDiameterMin) < 190.5 && Float.parseFloat(outsideDiameterMax) > 114.3 
							&& Float.parseFloat(outsideDiameterMax) < 190.5));
					break;
				}
				case "condition 47":
				{
					conditionDetails = "...then the minimum thickness must be equal to or greater than 12.7 mm, "
							+ "and the maximum thickness must be less than 19 mm.";
					String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
					String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 47", conditionDetails, (Float.parseFloat(thicknessMin)>= 12.7 
							&& Float.parseFloat(thicknessMax) < 19)); 
					break;
				}
				case "condition 48":
				{
					conditionDetails = "...then the minimum thickness must be equal to or greater than 19 mm.";
					String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
					ovralResult = ovralResult & checkAndReport("Condition 48", conditionDetails, (Float.parseFloat(thicknessMin)>= 19 )); 
					break;
				}
				case "condition 49":
				{
					conditionDetails = "...then the minimum outside diameter must be equal to or greater than 38.1 mm and less than 114.3 mm, "
							+ "and the maximum outside diameter must be greater than 38.1 mm and less than 114.3 mm.";
					String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
					String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 49", conditionDetails, (Float.parseFloat(outsideDiameterMin) >=38.1 
							&& Float.parseFloat(outsideDiameterMin) < 114.3 &&	Float.parseFloat(outsideDiameterMax) > 38.1
							&& Float.parseFloat(outsideDiameterMax) < 114.3)); 
					break;
				}
				case "condition 50":
				{
					conditionDetails = "...then the minimum outside diameter must be equal to or "
							+ "greater than 190.5 mm and equal to or less than 285.8 mm, "
							+ "and the maximum outside diameter must be equal to or greater than 190.5 mm "
							+ "and less than 285.8 mm.";
					String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
					String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 50", conditionDetails, (Float.parseFloat(outsideDiameterMin) >=190.5 
							&& Float.parseFloat(outsideDiameterMin) <= 285.8 &&	Float.parseFloat(outsideDiameterMax) >= 190.5 
							&& Float.parseFloat(outsideDiameterMax) < 285.8));
					break;
				}
				case "condition 51":
				{
					
				break;
				}
				case "condition 52":
				{
					conditionDetails = "...then the outside diameter must be greater than 609.6 mm.";
					break;
				}
				case "condition 53":
				{
					conditionDetails = "...then the minimum outside diameter must be greater than 406.4 mm and equal to or less than 609.6 mm,"
							+ " and the maximum outside diameter must be equal to or greater than 406.4 mm and less than 609.6 mm.";
					String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
					String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 53", conditionDetails, (Float.parseFloat(outsideDiameterMin) > 406.4
							&& Float.parseFloat(outsideDiameterMin) <= 609.6 &&	Float.parseFloat(outsideDiameterMax) >= 406.4
							&& Float.parseFloat(outsideDiameterMax) < 609.6)); 
					break;
				}
				case "condition 54":
				{
					conditionDetails = "...then the maximum thickness must be less than 1.65 mm.";
					String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 54", conditionDetails, (Float.parseFloat(thicknessMax) < 1.65 )); 
					break;
				}
				case "condition 55":
				{
					conditionDetails = "...then the maximum thickness must be equal to or less than 2.54 mm.";
					String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 55", conditionDetails, (Float.parseFloat(thicknessMax) <= 2.54 )); 
					break;
				}
				case "condition 56":
				{
					conditionDetails = "...then the maximum thickness must be less than 6.4 mm.";
					String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 56", conditionDetails, (Float.parseFloat(thicknessMax) < 6.4 )); 
					break;
				}
				case "condition 57":
				{
					conditionDetails = "...then the minimum percent nickel must be greater than 0.5.";
					String nickelMin = getProdValue(jObj, "ChemicalComposition", "Nickel", "Minimum");
					ovralResult = ovralResult & checkAndReport("Condition 57", conditionDetails, (Float.parseFloat(nickelMin)> 0.5)); 
					break;
				}
				case "condition 58":
				{
					conditionDetails = "...then the maximum percent nickel must be equal to or less than 0.5.";
					String nickelMax = getProdValue(jObj, "ChemicalComposition", "Nickel", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 58", conditionDetails, (Float.parseFloat(nickelMax)<= 0.5)); 
					break;
				}
				case "condition 59":
				{
					conditionDetails = "...then the minimum percent molybdenum must be greater than 1.5 and the "
							+ "maximum percent molybdenum must be less than 5.";
					String molybdenumMin = getProdValue(jObj, "ChemicalComposition", "Molybdenum", "Minimum");
					String molybdenumMax = getProdValue(jObj, "ChemicalComposition", "Molybdenum", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 59", conditionDetails, (Float.parseFloat(molybdenumMin)> 1.5 
							&& Float.parseFloat(molybdenumMax) < 5)); 
					break;
				}
				case "condition 60":
				{
					conditionDetails = "...then the minimum percent molybdenum must be equal to or less than 1.5 "
							+ "or the maximum percent molybdenum must be equal to or greater than 5.";
					String molybdenumMin = getProdValue(jObj, "ChemicalComposition", "Molybdenum", "Minimum");
					String molybdenumMax = getProdValue(jObj, "ChemicalComposition", "Molybdenum", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 60", conditionDetails, (Float.parseFloat(molybdenumMin) <= 1.5 
							&& Float.parseFloat(molybdenumMax) >= 5)); 
				break;
				}
				case "condition 61":
				{
					conditionDetails = "...then the minimum percent nickel must be greater than 0.5 and the "
							+ "maximum percent nickel must be less than 24.";
					String nickelMin = getProdValue(jObj, "ChemicalComposition", "Nickel", "Minimum");
					String nickelMax = getProdValue(jObj, "ChemicalComposition", "Nickel", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 61", conditionDetails, (Float.parseFloat(nickelMin)> 0.5 
							&& Float.parseFloat(nickelMax) < 24)); 
					break;
				}
				case "condition 62":
				{
					conditionDetails = "...then the minimum percent nickel must be equal to or less"
							+ " than 0.5 or the maximum percent nickel must be equal to or greater than 24.";
					String nickelMin = getProdValue(jObj, "ChemicalComposition", "Nickel", "Minimum");
					String nickelMax = getProdValue(jObj, "ChemicalComposition", "Nickel", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 62", conditionDetails, (Float.parseFloat(nickelMin) <= 0.5 
							|| Float.parseFloat(nickelMax) >= 24)); 
					break;
				}
				case "condition 63":
				{
					conditionDetails = "...then the maximum percent chromium must be less than 15.";
					String chromiumMax = getProdValue(jObj, "ChemicalComposition", "Chromium", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 63", conditionDetails, (Float.parseFloat(chromiumMax)<15)); 
					break;
				}
				case "condition 64":
				{
					conditionDetails = "...then the maximum percent chromium must be equal to or greater than 15.";
					String chromiumMax = getProdValue(jObj, "ChemicalComposition", "Chromium", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 64", conditionDetails, 
							(Float.parseFloat(chromiumMax) >= 15)); 
					break;
				}
				case "condition 65":
				{
					conditionDetails = "...then the minimum thickness must be equal to or greater than 1.65 mm.";
					String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
					ovralResult = ovralResult & checkAndReport("Condition 65", conditionDetails, (Float.parseFloat(thicknessMin) >= 1.65 ));
					break;
				}
				case "condition 66":
				{
					conditionDetails = "...then the maximum thickness must be less than 4 mm.";
					String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 66", conditionDetails, (Float.parseFloat(thicknessMax) < 4));
					break;
				}
				case "condition 67":
				{
					conditionDetails = "...then the minimum thickness must be equal to or greater than 4 mm.";
					String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
					ovralResult = ovralResult & checkAndReport( "condition 67", conditionDetails, (Float.parseFloat(thicknessMin) >= 4 )); 
					break;
				}
				case "condition 68":
				{
					conditionDetails = "...then the maximum carbon must be less than 0.25 percent.";
					String carbonMax = getProdValue(jObj, "ChemicalComposition", "Carbon", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 68", conditionDetails, (Float.parseFloat(carbonMax) < 0.25)); 
					break;
				}
				case "condition 69":
				{
					conditionDetails = "...then the minimum carbon must be equal to or greater than 0.25 percent.";
					String carbonMin = getProdValue(jObj, "ChemicalComposition", "Carbon", "Minimum");
					ovralResult = ovralResult & checkAndReport("Condition 69", conditionDetails, (Float.parseFloat(carbonMin) >= 0.25)); 
					break;
				}
				case "condition 70":{
		
				break;
				}
				case "condition 71":{
		
				break;
				}
				case "condition 72":{
		
				break;
				}
				case "condition 73":
				{
					conditionDetails = "...then the minimum thickness must be equal to or greater than 4.75 mm.";
					String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
					ovralResult = ovralResult & checkAndReport("Condition 73", conditionDetails, (Float.parseFloat(thicknessMin) >= 4.75));
					break;
				}
				case "condition 74":
				{
					conditionDetails = "...then the maximum thickness must be less than 4.75 mm.";
					String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 74", conditionDetails, (Float.parseFloat(thicknessMax) < 4.75));
					break;
				}
				case "condition 75":
				{
					conditionDetails = "...then the minimum thickness must be equal to or greater than 3 mm and the maximum "
							+ "thickness must be less than 4.75 mm.";
					String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
					String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 75", conditionDetails, 
							(Float.parseFloat(thicknessMin) >= 3 && Float.parseFloat(thicknessMax)<4.75));
					break;
				}
				case "condition 76":
				{
					conditionDetails = "...then the maximum thickness must be less than 3 mm.";
					String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 76", conditionDetails, 
							(Float.parseFloat(thicknessMax)<3));
					break;
				}
				case "condition 77":
				{
					conditionDetails = "...then the minimum thickness must be greater than 10 mm.";
					String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
					ovralResult = ovralResult & checkAndReport("Condition 77", conditionDetails, 
							(Float.parseFloat(thicknessMin)>10));
					break;
				}
				case "condition 78":
				{
					conditionDetails = "...then the minimum thickness must be equal to or greater "
							+ "than 4.75 mm and the maximum thickness must be less than 10 mm.";
					String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
					String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 78", conditionDetails, 
							(Float.parseFloat(thicknessMin) >= 4.75 && Float.parseFloat(thicknessMax)<10));
					break;
				}
				case "condition 79":
				{
					conditionDetails = "...then the minimum thickness must be greater than 1 mm "
							+ "and the maximum thickness must be less than 3 mm.";
					String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
					String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 79", conditionDetails, 
							(Float.parseFloat(thicknessMin) > 1 && Float.parseFloat(thicknessMax)<3));
					break;
				}
				case "condition 80":
				{
					conditionDetails = "...then the minimum thickness must be equal to or greater than 0.5 "
							+ "mm and the maximum thickness must be less than 1 mm.";
					String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
					String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 80", conditionDetails, 
							(Float.parseFloat(thicknessMin) >= 0.5 && Float.parseFloat(thicknessMax)<1));
					break;
				}
				case "condition 81":
				{
					conditionDetails = "...then the maximum thickness must be less than 0.361 mm.";
					String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 81", conditionDetails, 
							(Float.parseFloat(thicknessMax)<0.361));
					break;
				}
				case "condition 82":
				{
					conditionDetails = "...then the maximum thickness must be equal to or greater than 3 mm.";
					String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 82", conditionDetails, 
							(Float.parseFloat(thicknessMax)>=3));
					break;
				}
				case "condition 83":
				{
					conditionDetails = "...then the maximum thickness must be less than 0.5 mm.";
					String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 83", conditionDetails, 
							(Float.parseFloat(thicknessMax)<0.5));
					break;
				}
				case "condition 84":
				{
					conditionDetails = "...then the minimum thickness must be equal to or greater than 0.5 mm.";
					String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
					ovralResult = ovralResult & checkAndReport("Condition 84", conditionDetails, 
							(Float.parseFloat(thicknessMin)>=0.5));
					break;
				}
				case "condition 85":
				{
					conditionDetails = "...then the minimum thickness must be equal to or greater than 0.4 mm.";
					String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
					ovralResult = ovralResult & checkAndReport("Condition 85", conditionDetails, 
							(Float.parseFloat(thicknessMin)>=0.4));
					break;
				}
				case "condition 86":
				{
					conditionDetails = "...then the minimum thickness must be equal to or greater than 4.75 mm.";
					String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
					ovralResult = ovralResult & checkAndReport("Condition 86", conditionDetails, 
							(Float.parseFloat(thicknessMin)>=4.75));
					break;
				}
				case "condition 87":
				{
					conditionDetails = "...then the maximum width must be less than 300 mm.";
					String widthMax = getProdValue(jObj, "ProductDimensions", "Width", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 87", conditionDetails, 
							(Float.parseFloat(widthMax)<300));
					break;
				}
				case "condition 88":
				{
					conditionDetails = "...then the minimum thickness must be greater than 1.25 mm.";
					String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
					ovralResult = ovralResult & checkAndReport("Condition 88", conditionDetails, 
							(Float.parseFloat(thicknessMin) > 1.25));
					break;
				}
				case "condition 89":
				{
					conditionDetails = "...then the minimum thickness must be greater than"
							+ " 0.25 mm and the maximum thickness must be less than or equal to 1.25 mm.";
					String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
					String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 89", conditionDetails, 
							(Float.parseFloat(thicknessMin) > 0.25 && Float.parseFloat(thicknessMax)<=1.25));
					break;
				}
				case "condition 90":
				{
					conditionDetails = "...then the maximum thickness must be equal to or less than 0.25 mm.";
					String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 90", conditionDetails, 
							(Float.parseFloat(thicknessMax)<=0.25));
				break;
				}
				case "condition 91":
				{
					conditionDetails = "...then the maximum width must be less than 51 mm.";
					String widthMax = getProdValue(jObj, "ProductDimensions", "Width", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 91", conditionDetails, 
							(Float.parseFloat(widthMax)<51));
				break;
				}
				case "condition 92":
				{
					conditionDetails = "...then the minimum percentage of lead must be equal to or greater than 0.1 percent.";
					String leadMin = getProdValue(jObj, "ChemicalComposition", "Lead", "Minimum");
					ovralResult = ovralResult & checkAndReport("Condition 92", conditionDetails, 
							(Float.parseFloat(leadMin)>=0.1));
					break; 
				}
				case "condition 93":
				{
					conditionDetails = "...then the minimum percentage of carbon is equal to or greater than 0.6 percent.";
					String carbonMin = getProdValue(jObj, "ChemicalComposition", "Carbon", "Minimum");
					ovralResult = ovralResult & checkAndReport("Condition 93", conditionDetails, 
							(Float.parseFloat(carbonMin)>=0.6));
		
					break;
				}
				case "condition 94":
				{
					conditionDetails = "...then the minimum percentage of carbon is greater than or equal to 0.25 percent "
							+ "of carbon and the maximum percentage of carbon is less than 0.6 percent.";
					String carbonMin = getProdValue(jObj, "ChemicalComposition", "Carbon", "Minimum");
					String carbonMax = getProdValue(jObj, "ChemicalComposition", "Carbon", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 94", conditionDetails,
							( Float.parseFloat(carbonMin)>=0.25 && Float.parseFloat(carbonMax)<0.6));
				
					break;
				}
				case "condition 95":
				{
					conditionDetails = "...then the maximum percentage of carbon is less than 0.6 percent.";
					String carbonMax = getProdValue(jObj, "ChemicalComposition", "Carbon", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 95", conditionDetails,
							(Float.parseFloat(carbonMax)<0.6));
					break;
				}
				case "condition 96":
				{
					conditionDetails = "...then the maximum outside diameter must be less than 76 mm.";
					String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 96", conditionDetails, 
							(Float.parseFloat(outsideDiameterMax) < 76)); 
					break;
				}
				case "condition 97":
				{
					conditionDetails = "...then the minimum outside diameter must be equal to or greater than 76 mm and "
							+ "less than 228 mm, and the maximum outside diameter must be equal to or greater than "
							+ "76 mm and less than 228 mm.";
					String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
					String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 97", conditionDetails, 
							(Float.parseFloat(outsideDiameterMin) >= 76 && Float.parseFloat(outsideDiameterMin) < 228
							&&Float.parseFloat(outsideDiameterMax) >= 76 && Float.parseFloat(outsideDiameterMax) < 228)); 
					break;
				}
				case "condition 98":
				{
					conditionDetails = "...then the minimum outside diameter must be greater than 228 mm.";
					String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
					ovralResult = ovralResult & checkAndReport("Condition 98", conditionDetails, 
							(Float.parseFloat(outsideDiameterMin) > 228)); 
					break;
				}
				case "condition 99":
				{
					conditionDetails = "...then the maximum height must be less than 80 mm.";
					String heightMax = getProdValue(jObj, "ProductDimensions", "Height", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 99", conditionDetails, 
							(Float.parseFloat(heightMax) < 80));
				break;
				}
				case "condition 100":
				{
					conditionDetails = "...then the minimum height must be equal to or greater than 80 mm.";
					String heightMin = getProdValue(jObj, "ProductDimensions", "Height", "Minimum");
					ovralResult = ovralResult & checkAndReport("Condition 100", conditionDetails, 
							(Float.parseFloat(heightMin) >= 80));
					break;
				}
				case "condition 101":
				{
					conditionDetails = "...then the maximum outside diameter must be less than 1.5 mm.";
					String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 101", conditionDetails, 
							(Float.parseFloat(outsideDiameterMax) < 1.5)); 
					break;
				}
				case "condition 102":
				{
					conditionDetails = "...then the minimum outside diameter must be equal to or greater than 1.5 mm.";
					String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
					ovralResult = ovralResult & checkAndReport("Condition 102", conditionDetails, 
							(Float.parseFloat(outsideDiameterMin) >= 1.5)); 
					
					break;
				}
				case "condition 103":
				{
					conditionDetails = "...then the maximum width must be equal to or less than 1575 mm.";
					String widthMax = getProdValue(jObj, "ProductDimensions", "Width", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 103", conditionDetails, 
							(Float.parseFloat(widthMax)<=1575));
					break;
				}
				case "condition 104":{
					conditionDetails = "...then the minimum width must be greater than 1575 mm.";
					String widthMin = getProdValue(jObj, "ProductDimensions", "Width", "Minimum");
					ovralResult = ovralResult & checkAndReport("Condition 104", conditionDetails, 
							(Float.parseFloat(widthMin)>1575));
		
					break;
				}
				case "condition 105":{
					conditionDetails = "...then the minimum width must be equal to or greater than 1370 mm.";
					String widthMin = getProdValue(jObj, "ProductDimensions", "Width", "Minimum");
					ovralResult = ovralResult & checkAndReport("Condition 105", conditionDetails, 
							(Float.parseFloat(widthMin)>=1370));
					break;
				}
				case "condition 106":{
					conditionDetails = "...then the minimum width must be equal to or greater than 1370 mm.";
					String widthMin = getProdValue(jObj, "ProductDimensions", "Width", "Minimum");
					ovralResult = ovralResult & checkAndReport("Condition 106", conditionDetails, 
							(Float.parseFloat(widthMin)>=1370));
					break;
				}
				case "condition 107":{
					conditionDetails = "...then the minimum percentage of nickel must be greater than 0.5 percent.";
					String nickelMin = getProdValue(jObj, "ChemicalComposition", "Nickel", "Minimum");
					//String nickelMax = getProdValue(jObj, "ChemicalComposition", "Nickel", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 107", conditionDetails, 
							(Float.parseFloat(nickelMin) > 0.5 )); 
					break;
				}
				case "condition 108":{
					conditionDetails = "...then the minimum percentage of molybdenum must be greater than 1.5 percent "
							+ "and the maximum percentage of molybdenum must be less than 5 percent.";
					String molybdenumMin = getProdValue(jObj, "ChemicalComposition", "Molybdenum", "Minimum");
					String molybdenumMax = getProdValue(jObj, "ChemicalComposition", "Molybdenum", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 108", conditionDetails, 
							(Float.parseFloat(molybdenumMin) > 1.5 && Float.parseFloat(molybdenumMax) < 5)); 
					break;
				}
				case "condition 109":{
					conditionDetails = "...then the minimum percentage of molybdenum must equal to or less than 1.5 percent or "
							+ "the maximum percentage of molybdenum must be equal to or greater than 5 percent.";
					String molybdenumMin = getProdValue(jObj, "ChemicalComposition", "Molybdenum", "Minimum");
					String molybdenumMax = getProdValue(jObj, "ChemicalComposition", "Molybdenum", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 109", conditionDetails, 
							(Float.parseFloat(molybdenumMin) <= 1.5	|| Float.parseFloat(molybdenumMax) >= 5));
					break;
				}
				case "condition 110":{
					conditionDetails = "...then the maximum thickness must be less than 4.75 mm and the minimum thickness must "
							+ "be equal to or greater than 3 mm.";
					String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
					String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 110", conditionDetails, 
							(Float.parseFloat(thicknessMax)< 4.75 && Float.parseFloat(thicknessMin) >= 3 ));
					break;
				}
				case "condition 111":{
					conditionDetails = "...then the minimum percentage of nickel must be greater than 0.5 percent and the maximum "
							+ "percentage of nickel must be less than 24 percent.";
					String nickelMin = getProdValue(jObj, "ChemicalComposition", "Nickel", "Minimum");
					String nickelMax = getProdValue(jObj, "ChemicalComposition", "Nickel", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 111", conditionDetails, 
							(Float.parseFloat(nickelMin) > 0.5  && Float.parseFloat(nickelMax) < 24)); 
					break;
				}
				case "condition 112":{
					conditionDetails = "...then the minimum width must be greater than 1575 mm, and the maximum width must "
							+ "equal to or less than 1880 mm.";
					String widthMin = getProdValue(jObj, "ProductDimensions", "Width", "Minimum");
					String widthMax = getProdValue(jObj, "ProductDimensions", "Width", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 112", conditionDetails, 
							(Float.parseFloat(widthMin)> 1575 && Float.parseFloat(widthMax) <= 1880));
					break;
				}
				case "condition 113":{
					conditionDetails = "...then the minimum width must be greater than 1880 mm.";
					String widthMin = getProdValue(jObj, "ProductDimensions", "Width", "Minimum");
					ovralResult = ovralResult & checkAndReport("Condition 113", conditionDetails, 
							(Float.parseFloat(widthMin)> 1880));
					break;
				}
				case "condition 114":{
					conditionDetails = "...then the maximum percentage of chromium must be less than 15 percent.";
					String chromiumMax = getProdValue(jObj, "ChemicalComposition", "Chromium", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 114", conditionDetails, 
							(Float.parseFloat(chromiumMax) < 15)); 
					break;
				}
				case "condition 115":{
					conditionDetails = "...then the minimum width must be equal to or greater than 300 mm.";
					String widthMin = getProdValue(jObj, "ProductDimensions", "Width", "Minimum");
					ovralResult = ovralResult & checkAndReport("Condition 115", conditionDetails, 
							(Float.parseFloat(widthMin)>= 300));
					break;
				}
				case "condition 116":{
					conditionDetails = "...then the maximum outside diameter must be less than 14 mm.";
					String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 116", conditionDetails, 
							(Float.parseFloat(outsideDiameterMax) < 14)); 
					break;
				}
				case "condition 117":{
					conditionDetails = "...then the maximum percentage of nickel must be less than 8 percent.";
					//String nickelMin = getProdValue(jObj, "ChemicalComposition", "Nickel", "Minimum");
					String nickelMax = getProdValue(jObj, "ChemicalComposition", "Nickel", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 117", conditionDetails, 
							(Float.parseFloat(nickelMax) < 8 ));
					break;
				}
				case "condition 118":{
					conditionDetails = "...then the minimum percentage of nickel must be equal to or greater than 8 percent, "
							+ "and the maximum percentage of nickel must be less than 24 percent.";
					String nickelMin = getProdValue(jObj, "ChemicalComposition", "Nickel", "Minimum");
					String nickelMax = getProdValue(jObj, "ChemicalComposition", "Nickel", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 118", conditionDetails, 
							(Float.parseFloat(nickelMin) >= 8 && Float.parseFloat(nickelMax) < 24 ));
					break;
				}
				case "condition 119":{
					conditionDetails = "...then the minimum outside diameter must be equal to or greater than 14 mm and "
							+ "less than 19 mm, and the maximum outside diameter must be equal to or greater than 14 mm and "
							+ "less than 19 mm.";
					String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
					String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 119", conditionDetails, 
							(Float.parseFloat(outsideDiameterMin) >= 14 && Float.parseFloat(outsideDiameterMin) < 19
							&& Float.parseFloat(outsideDiameterMax) >= 14 && Float.parseFloat(outsideDiameterMax) < 19));
					break;
				}
				case "condition 120":{
					conditionDetails = "...then the maximum outside diameter must be equal to or greater than 19 mm.";
					String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 120", conditionDetails, 
							(Float.parseFloat(outsideDiameterMax) >= 19));
					break;
				}
				case "condition 121":{
					conditionDetails = "...then the maximum outside diameter must be less than 0.25 mm.";
					String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 121", conditionDetails, 
							(Float.parseFloat(outsideDiameterMax) < 0.25));
					break;
				}
				case "condition 122":{
					conditionDetails = "...then the minimum outside diameter must be equal to or greater than 0.25 mm and less "
							+ "than 0.76 mm, and the maximum outside diameter must be equal to or greater than 0.25 mm and less than 0.76 mm.";
					String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
					String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 122", conditionDetails, 
							(Float.parseFloat(outsideDiameterMin) >= 0.25 && Float.parseFloat(outsideDiameterMin) < 0.76
							&& Float.parseFloat(outsideDiameterMax) >= 0.25 && Float.parseFloat(outsideDiameterMax) < 0.76));
					break;
				}
				case "condition 123":{
					conditionDetails = "...then the minimum outside diameter must be equal to or greater than 0.76 mm and less than "
							+ "1.52 mm, and the maximum outside diameter must be equal to or greater than 0.76 mm and less than 1.52 mm.";
					String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
					String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 123", conditionDetails, 
							(Float.parseFloat(outsideDiameterMin) >= 0.76 && Float.parseFloat(outsideDiameterMin) < 1.52
							&& Float.parseFloat(outsideDiameterMax) >= 0.76 && Float.parseFloat(outsideDiameterMax) < 1.52));
					break;
				}
				case "condition 124":{
					conditionDetails = "...then the minimum outside diameter must be equal to or greater than 1.52 mm and less "
							+ "than 5.1 mm, and the maximum outside diameter must be equal to or greater than 1.52 mm and less than 5.1 mm.";
					String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
					String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 124", conditionDetails, 
							(Float.parseFloat(outsideDiameterMin) >= 1.52 && Float.parseFloat(outsideDiameterMin) < 5.1
							&& Float.parseFloat(outsideDiameterMax) >= 1.52 && Float.parseFloat(outsideDiameterMax) < 5.1));
					break;
				}
				case "condition 125":{
					conditionDetails = "...then the minimum outside diameter must be equal to or greater than 5.1 mm.";
					String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
					ovralResult = ovralResult & checkAndReport("Condition 125", conditionDetails, 
							(Float.parseFloat(outsideDiameterMin) >= 5.1));
					break;
				}
				case "condition 126":{
					conditionDetails = "...then the maximum thickness must be less than 0.25 mm.";
					String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 126", conditionDetails, 
							(Float.parseFloat(thicknessMax) < 0.25));
					break;
				}
				case "condition 127":{
					conditionDetails = "...then the minimum outside diameter must be equal to or greater than 76 mm and less than 152 mm, "
							+ "and the maximum outside diameter must be equal to or greater than 76 mm and less than 152 mm.";
					String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
					String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 127", conditionDetails, 
							(Float.parseFloat(outsideDiameterMin) >= 76 && Float.parseFloat(outsideDiameterMin) < 152
							&& Float.parseFloat(outsideDiameterMax) >= 76 && Float.parseFloat(outsideDiameterMax) < 152));
					break;
				}
				case "condition 128":{
					conditionDetails = "...then the minimum outside diameter must be equal to or greater than 152 mm and less than 228 mm,"
							+ " and the maximum outside diameter must be equal to or greater than 152 mm and less than 228 mm.";
					String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
					String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 128", conditionDetails, 
							(Float.parseFloat(outsideDiameterMin) >= 152 && Float.parseFloat(outsideDiameterMin) < 228
							&& Float.parseFloat(outsideDiameterMax) >= 152 && Float.parseFloat(outsideDiameterMax) < 228));
					break;
				}
				case "condition 129":{
					conditionDetails = "Flat-rolled straight at least 4.75 mm thickness ...then the minimum width must be greater than 150mm,"
							+ " and the minimum width must be greater than or equal to 2* the maximum thickness.";
					break;
				}
				case "condition 130":{
					conditionDetails = "Hot-rolled ...then the cold rolled cell must say 'No' or be blank.";
					break;
				}
				case "condition 131":{
					conditionDetails = "...then the maximum outside diameter must be equal to or less than 1.6 mm, "
							+ "and the maximum percentage of carbon must be less than 0.20 percent, and the minimum percentage of nickel must"
							+ " be greater than 0.3 percent, and the minimum percentage of molybdenum must be greater than 0.08 percent.";
					String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
					String carbonMax = getProdValue(jObj, "ChemicalComposition", "Carbon", "Maximum");
					String nickelMin = getProdValue(jObj, "ChemicalComposition", "Nickel", "Minimum");
					String molybdenumMin = getProdValue(jObj, "ChemicalComposition", "Molybdenum", "Minimum");
					ovralResult = ovralResult & checkAndReport("Condition 131", conditionDetails, 
							(Float.parseFloat(outsideDiameterMax) <= 1.6 && Float.parseFloat(carbonMax) < 0.20
							&& Float.parseFloat(nickelMin)> 0.3 && Float.parseFloat(molybdenumMin)>0.08));
					break;
				}
				case "condition 132":{
					conditionDetails = "...then the maximum outside diameter must be equal to or less than 1.6 mm, and the maximum percentage "
							+ "of carbon must be less than 0.20 percent, and the minimum percentage of manganese must be greater than 0.9 percent, "
							+ "and the minimum percentage of silicon must be greater than 0.6 percent.";
					String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
					String carbonMax = getProdValue(jObj, "ChemicalComposition", "Carbon", "Maximum");
					String manganeseMin = getProdValue(jObj, "ChemicalComposition", "Magnesium", "Minimum");
					String siliconMin = getProdValue(jObj, "ChemicalComposition", "Silicon", "Minimum");
					ovralResult = ovralResult & checkAndReport("Condition 132", conditionDetails, 
							(Float.parseFloat(outsideDiameterMax) <= 1.6 && Float.parseFloat(carbonMax) < 0.20 
							&& Float.parseFloat(manganeseMin)> 0.9 && Float.parseFloat(siliconMin)>0.6));
					break;
				}
				case "condition 133":{
					conditionDetails = "...then the maximum outside diameter must be less than 1.0 mm.";
					String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 133", conditionDetails, 
							(Float.parseFloat(outsideDiameterMax) < 1.0));
					break;
				}
				case "condition 134":{
					conditionDetails = "...then the minimum outside diameter must be equal to or greater than 1.0 mm and less "
							+ "than 1.5 mm, and the maximum outside diameter must be equal to or greater than 1.0 mm and less than 1.5 mm.";
					String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
					String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 134", conditionDetails, 
							(Float.parseFloat(outsideDiameterMin) >= 1 && Float.parseFloat(outsideDiameterMin) < 1.5
							&& Float.parseFloat(outsideDiameterMax) >= 1 && Float.parseFloat(outsideDiameterMax) < 1.5));
					break;
				}
				case "condition 135":{
					conditionDetails = "...then the minimum thickness must be equal to or greater than 0.361 mm, and the maximum thickness "
							+ "must be less than 0.5 mm.";
					String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
					String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 135", conditionDetails, 
							(Float.parseFloat(thicknessMin) >= 0.361 && Float.parseFloat(thicknessMax)< 0.5));
					break;
				}
				case "condition 136":{
					conditionDetails = "...then the minimum thickness must be less than 0.361 mm.";
					String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
					ovralResult = ovralResult & checkAndReport("Condition 136", conditionDetails, 
							(Float.parseFloat(thicknessMin) < 0.361 ));
					break;
				}
				case "condition 137":{
					conditionDetails = "...then the maximum outside diameter must be less than 19 mm.";
					String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 137", conditionDetails, 
							(Float.parseFloat(outsideDiameterMax) < 19));
					break;
				}
				case "condition 138":{
					conditionDetails = "...then the maximum outside diameter must be equal to or greater than 19 mm.";
					String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
					ovralResult = ovralResult & checkAndReport("Condition 138", conditionDetails, 
							(Float.parseFloat(outsideDiameterMax) >= 19));
					break;
				}
				case "condition 139":{
					conditionDetails = "Stainless ...then the maximum percentage of carbon must be 1.2 percent or less and the "
							+ "minimum percentage of chromium must be 10.5 percent or more.";
					String carbonMax = getProdValue(jObj, "ChemicalComposition", "Carbon", "Maximum");
					String chromiumMin = getProdValue(jObj, "ChemicalComposition", "Chromium", "Minimum");
					ovralResult = ovralResult & checkAndReport("Condition 139", conditionDetails, 
							(Float.parseFloat(carbonMax) <= 1.2 && Float.parseFloat(chromiumMin) >= 10.5));
					break;
				}
				case "condition 140":{
					conditionDetails = "Covered by the proclamation but no chemical or dimensional information, 'other' ..."
							+ "then it receives an automatic pass.";
					
					break;
				}
				default :
				{
					failTestSuite("Validate condition", "All conditions are valid", 
							condition+" is not a valid condition", "Step", "fail", "");
					break;
				}
	
	
				}//switch
			}//if product type
			else //Auminium
			{
				
			}
			
		}
		
		//HtmlReport.addHtmlStep("Validate "+jObj.get("HTSUSCode"), conditions, "stepActualResult", "VP", "pass", "");
		return ovralResult;
	}
	/**
	 * This method reports pass/failed to report based on condition value
	 * @param conditionName: condition name
	 * @param condDetails: condition's details
	 * @param condition: condition name
	 * @return true if condition is true, false if not
	*/
	public static boolean checkAndReport(String conditionName,
										String condDetails, 
										boolean condition)
	{
		boolean checked = true;
		if (condition)
		{
			HtmlReport.addHtmlStep("Validate {"+conditionName+"}", "<abbr title='"+condDetails+"'>"
					+ conditionName+" details ...(!)</abbr>", 
					conditionName+" is met", "VP", "pass", "");
		}
		else
		{
			checked = false;
			HtmlReport.addHtmlStep("Validate {"+conditionName+"}", "<abbr title='"+condDetails+"'>"
					+ conditionName+" details ...(!)</abbr>",
					conditionName+" is not met", "VP", "fail", "");
		}
		return checked;
	}
	
	/**
	 * This method reads values from json object
	 * @param jObj: json object containing form information
	 * @param blockName: name of the container
	 * @param minMax: the value searched, min or max
	 * @return the min/max value
	*/
	public static String getProdValue(JSONObject obj, 
									String blockName, 
									String productName,
									String minMax)
	{
		String prodVal = "";
		String key;
		JSONArray blockArray = (JSONArray) obj.get(blockName);
		for (Object block : blockArray) 
		{
		    JSONObject blockItem = (JSONObject) block;
		    key = (String) blockItem.get("Key");
		    if(key.equalsIgnoreCase(productName))
		    {
		    	Object valueObject = blockItem.get("Value");
		    	JSONObject jValueObject = (JSONObject) valueObject;
		    	prodVal = (String) jValueObject.get(minMax);
		    	break;
		    }
		}
		
		return prodVal;
	}
	
	
}
