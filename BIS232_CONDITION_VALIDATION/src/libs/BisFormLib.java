/**
 * MilCorp
 * Mouloud Hamdidouche
 * January, 0.2019
*/

package libs;
import static GuiLibs.GuiTools.checkElementExists;
import static GuiLibs.GuiTools.clickElementJs;
import static GuiLibs.GuiTools.enterText;
import static GuiLibs.GuiTools.failTestSuite;
import static GuiLibs.GuiTools.failTestCase;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import javax.sound.sampled.Line;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import ReportLibs.HtmlReport;
import bsh.ParseException;

public class BisFormLib{
	public static String filedDate, displayedResult,
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
	 * @param actualResult displayed Result
	 * @exception Exception
	*/
	public static boolean ValidateConditions(JSONObject jObj, 
											 String productType, 
											 String conditions,
											 String actualResult) throws Exception
	{
		boolean ovralResult=true;
		String ovralResultString="";
		displayedResult = actualResult;
		String conditionDetails, resultValues = "";
		if (conditions.equals("")) return true;
		String[] listConditions = conditions.split(",");
		boolean[] singleResults = new boolean[150];
		Arrays.fill(singleResults, Boolean.TRUE);
		
		for(int i=0; i<listConditions.length; i++)
		{ 
			String condition = listConditions[i];
			//Some codes needs to be sent directly to Manual
			if (productType.equalsIgnoreCase("Steel"))
			{
				switch(condition.toLowerCase())
				{
					case "condition 1":
					{
						/*conditionDetails ="Non-alloy ...then the maximum percentage must be less than 0.3 percent of aluminum,"
								+ " and less than 0.1 percent of antimony, and less than 0.1 percent of bismuth, and less than "
								+ "0.0008 percent of boron, and less than 0.3 percent of chromium, and less than 0.3 percent of "
								+ "cobalt, and less than 0.4 percent of copper, and less than 0.4 percent of lead, and less than "
								+ "1.65 percent of manganese, and less than 0.08 percent of molybdenum, and less than 0.3 percent "
								+ "of nickel, and less than 0.06 percent of niobium, and less than 0.1 percent selenium, and 0.6 "
								+ "percent of silicon, and less than 0.1 percent tellurium, and less than 0.05 percent of titanium, "
								+ "and less than 0.3 percent of tungsten (wolfram), and less than 0.1 percent of vanadium, and "
								+ "less than 0.05 percent of zirconium.";*/
						conditionDetails ="the maximum percentage must be less than 0.3 percent of aluminum, and less than 0.1 "
								+ "percent of antimony, and less than 0.1 percent of bismuth, and less than 0.0008 percent of boron, "
								+ "and less than 0.3 percent of chromium, and less than 0.3 percent of cobalt, and less than 0.4 percent"
								+ " of copper, and less than 0.4 percent of lead, ***and less than 0.1 percent magnesium***, and "
								+ "less than 1.65 percent of manganese, and less than 0.08 percent of molybdenum, and less than 0.3"
								+ " percent of nickel, and less than 0.06 percent of niobium, and less than 0.1 percent selenium, "
								+ "and 0.6 percent of silicon, and less than 0.1 percent tellurium, and less than 0.05 percent of "
								+ "titanium, and less than 0.3 percent of tungsten (wolfram), and less than 0.1 percent of vanadium, "
								+ "and less than 0.05 percent of zirconium.";
						String aluminumMax =  getProdValue(jObj, "ChemicalComposition", "Aluminum", "Maximum");
						String antimonyMax =  getProdValue(jObj, "ChemicalComposition", "Antimony", "Maximum");
						String bismuthMax =  getProdValue(jObj, "ChemicalComposition", "Bismuth", "Maximum");
						String boronMax =  getProdValue(jObj, "ChemicalComposition", "Boron", "Maximum");
						String chromiumMax =  getProdValue(jObj, "ChemicalComposition", "Chromium", "Maximum");
						String cobaltMax =  getProdValue(jObj, "ChemicalComposition", "Cobalt", "Maximum");
						String copperMax =  getProdValue(jObj, "ChemicalComposition", "Copper", "Maximum");
						String leadMax =  getProdValue(jObj, "ChemicalComposition", "Lead", "Maximum");
						String magnesiumMax =  getProdValue(jObj, "ChemicalComposition", "Magnesium", "Maximum");
						String manganeseMax =  getProdValue(jObj, "ChemicalComposition", "Manganese", "Maximum");
						String molybdenumMax =  getProdValue(jObj, "ChemicalComposition", "Molybdenum", "Maximum");
						String nickelMax =  getProdValue(jObj, "ChemicalComposition", "Nickel", "Maximum");
						String niobiumMax =  getProdValue(jObj, "ChemicalComposition", "Niobium", "Maximum");
						String seleniumMax =  getProdValue(jObj, "ChemicalComposition", "Selenium", "Maximum");
						String siliconMax =  getProdValue(jObj, "ChemicalComposition", "Silicon", "Maximum");
						String telluriumMax =  getProdValue(jObj, "ChemicalComposition", "Tellurium", "Maximum");
						String titaniumMax =  getProdValue(jObj, "ChemicalComposition", "Titanium", "Maximum");
						String tungstenMax =  getProdValue(jObj, "ChemicalComposition", "Tungsten", "Maximum");
						String vanadiumMax =  getProdValue(jObj, "ChemicalComposition", "Vanadium", "Maximum");
						String zirconiumMax =  getProdValue(jObj, "ChemicalComposition", "Zirconium", "Maximum");
						resultValues = getFormatedResultValues("aluminumMax", aluminumMax, "antimonyMax", antimonyMax,
								"bismuthMax", bismuthMax, "boronMax", boronMax, "chromiumMax", chromiumMax, "cobaltMax", cobaltMax,
								"copperMax", copperMax, "leadMax", leadMax, "magnesiumMax", magnesiumMax, "manganeseMax", manganeseMax,
								"molybdenumMax", molybdenumMax,	"nickelMax", nickelMax, "niobiumMax", niobiumMax, "seleniumMax", 
								seleniumMax, "siliconMax", siliconMax, "telluriumMax", telluriumMax, "titaniumMax", titaniumMax, "tungstenMax",
								tungstenMax, "vanadiumMax", vanadiumMax, "zirconiumMax", zirconiumMax);
								singleResults[1] = checkAndReport("Condition 1", conditionDetails, resultValues, 
								(Float.parseFloat(aluminumMax)<0.3f && Float.parseFloat(antimonyMax)<0.1f &&
								 Float.parseFloat(bismuthMax)<0.1f && Float.parseFloat(boronMax)<0.0008f &&
								 Float.parseFloat(chromiumMax)<0.3f && Float.parseFloat(cobaltMax)<0.3f &&	
								 Float.parseFloat(copperMax)<0.4f && Float.parseFloat(leadMax)<0.4f &&
								 Float.parseFloat(magnesiumMax)<0.1f && Float.parseFloat(manganeseMax)<1.65f && Float.parseFloat(molybdenumMax)<0.08f &&
								 Float.parseFloat(nickelMax)<0.3f && Float.parseFloat(niobiumMax)<0.06f &&
								 Float.parseFloat(seleniumMax)<0.1f && Float.parseFloat(siliconMax)<0.6f &&
								 Float.parseFloat(telluriumMax)<0.1f && Float.parseFloat(titaniumMax)<0.05f &&
								 Float.parseFloat(tungstenMax)<0.3f && Float.parseFloat(vanadiumMax)<0.1f &&
								 Float.parseFloat(zirconiumMax)<0.05));
						ovralResult = ovralResult & singleResults[1];
						break;
					}
					case "condition 2":
					{
						conditionDetails ="Stainless ...then the maximum percentage of carbon must be 1.2 percent or "
								+ "less and the minimum percentage of chromium must be 10.5 percent or more.";
						String carbonMax = getProdValue(jObj, "ChemicalComposition", "Carbon", "Maximum");
						String chromiumMin = getProdValue(jObj, "ChemicalComposition", "Chromium", "Minimum");
						resultValues = getFormatedResultValues("carbonMax", carbonMax, "chromiumMin", chromiumMin);
						singleResults[2] = checkAndReport("Condition 2", conditionDetails, resultValues, 
								(Float.parseFloat(carbonMax)<=1.2f && Float.parseFloat(chromiumMin)>=10.5f));
						ovralResult = ovralResult & singleResults[2];
						break;
					}
					case "condition 3":
					{
						/*conditionDetails ="Alloy ...then at least one of the minimum percentages must be equal to or greater "
								+ "than 0.3 percent of aluminum, or equal to or greater than 0.1 percent of antimony, or equal "
								+ "to or greater than 0.1 percent of bismuth, or equal to or greater than 0.0008 percent of "
								+ "boron, or equal to or greater than 0.3 percent of chromium, or equal to or greater than 0.3 percent "
								+ "of cobalt, or equal to or greater than 0.4 percent of copper, or equal to or greater than 0.4 percent"
								+ " of lead, or equal to or greater than 1.65 percent of manganese, or equal to or greater than 0.08 "
								+ "percent of molybdenum, or equal to or greater than 0.3 percent of nickel, or equal to or greater "
								+ "than 0.06 percent of niobium, or equal to or greater than 0.1 percent selenium, or equal to or "
								+ "greater than 0.6 percent of silicon, or equal to or greater than 0.1 percent tellurium, or equal "
								+ "to or greater than 0.05 percent of titanium, or equal to or greater than 0.3 percent of "
								+ "tungsten (wolfram), or equal to or greater than 0.1 percent of vanadium, or equal to or greater t"
								+ "han 0.05 percent of zirconium.";*/
						
						conditionDetails ="then at least one of the minimum percentages must be equal to or greater than 0.3 percent of aluminum, "
								+ "or equal to or greater than 0.1 percent of antimony, or equal to or greater than 0.1 percent of bismuth, "
								+ "or equal to or greater than 0.0008 percent of boron, or equal to or greater than 0.3 percent of chromium, "
								+ "or equal to or greater than 0.3 percent of cobalt, or equal to or greater than 0.4 percent of copper, "
								+ "or equal to or greater than 0.4 percent of lead, ***or equal to or greater than 0.1 percent magnesium***, "
								+ "or equal to or greater than 1.65 percent of manganese, or equal to or greater than 0.08 percent of molybdenum,"
								+ " or equal to or greater than 0.3 percent of nickel, or equal to or greater than 0.06 percent of niobium, "
								+ "or equal to or greater than 0.1 percent selenium, or equal to or greater than 0.6 percent of silicon, "
								+ "or equal to or greater than 0.1 percent tellurium, or equal to or greater than 0.05 percent of titanium, "
								+ "or equal to or greater than 0.3 percent of tungsten (wolfram), or equal to or greater than 0.1 percent of vanadium,"
								+ " or equal to or greater than 0.05 percent of zirconium.";
						
						
						String aluminumMin =  getProdValue(jObj, "ChemicalComposition", "Aluminum", "Minimum");
						String antimonyMin =  getProdValue(jObj, "ChemicalComposition", "Antimony", "Minimum");
						String bismuthMin =  getProdValue(jObj, "ChemicalComposition", "Bismuth", "Minimum");
						String boronMin =  getProdValue(jObj, "ChemicalComposition", "Boron", "Minimum");
						String chromiumMin =  getProdValue(jObj, "ChemicalComposition", "Chromium", "Minimum");
						String cobaltMin =  getProdValue(jObj, "ChemicalComposition", "Cobalt", "Minimum");
						String copperMin =  getProdValue(jObj, "ChemicalComposition", "Copper", "Minimum");
						String leadMin =  getProdValue(jObj, "ChemicalComposition", "Lead", "Minimum");
						String magnesiumMin =  getProdValue(jObj, "ChemicalComposition", "Magnesium", "Minimum");
						String manganeseMin =  getProdValue(jObj, "ChemicalComposition", "Manganese", "Minimum");
						String molybdenumMin =  getProdValue(jObj, "ChemicalComposition", "Molybdenum", "Minimum");
						String nickelMin =  getProdValue(jObj, "ChemicalComposition", "Nickel", "Minimum");
						String niobiumMin =  getProdValue(jObj, "ChemicalComposition", "Niobium", "Minimum");
						String seleniumMin =  getProdValue(jObj, "ChemicalComposition", "Selenium", "Minimum");
						String siliconMin =  getProdValue(jObj, "ChemicalComposition", "Silicon", "Minimum");
						String telluriumMin =  getProdValue(jObj, "ChemicalComposition", "Tellurium", "Minimum");
						String titaniumMin =  getProdValue(jObj, "ChemicalComposition", "Titanium", "Minimum");
						String tungstenMin =  getProdValue(jObj, "ChemicalComposition", "Tungsten", "Minimum");
						String vanadiumMin =  getProdValue(jObj, "ChemicalComposition", "Vanadium", "Minimum");
						String zirconiumMin =  getProdValue(jObj, "ChemicalComposition", "Zirconium", "Minimum");
						resultValues = getFormatedResultValues("aluminumMin", aluminumMin, "antimonyMin", antimonyMin,
						"bismuthMin", bismuthMin, "boronMin", boronMin, "chromiumMin", chromiumMin, "cobaltMin", cobaltMin,
						"copperMin", copperMin, "leadMin", leadMin, "magnesiumMin", magnesiumMin, "manganeseMin", manganeseMin, "molybdenumMin", molybdenumMin,
						"nickelMin", nickelMin, "niobiumMin", niobiumMin, "seleniumMin", seleniumMin, "siliconMin", siliconMin,
						"telluriumMin", telluriumMin, "titaniumMin", titaniumMin, "tungstenMin", tungstenMin, 
						"vanadiumMin", vanadiumMin, "zirconiumMin", zirconiumMin);
						singleResults[3] = checkAndReport("Condition 3", conditionDetails, resultValues, 
						(
						Float.parseFloat(aluminumMin)>=0.3f || Float.parseFloat(antimonyMin)>=0.1f ||
						Float.parseFloat(bismuthMin)>=0.1f || Float.parseFloat(boronMin)>=0.0008f ||
						Float.parseFloat(chromiumMin)>=0.3f || Float.parseFloat(cobaltMin)>=0.3f ||	
						Float.parseFloat(copperMin)>=0.4f || Float.parseFloat(leadMin)>=0.4f ||
						Float.parseFloat(magnesiumMin)>=0.1f || Float.parseFloat(manganeseMin)>=1.65f || Float.parseFloat(molybdenumMin)>=0.08f ||
						Float.parseFloat(nickelMin)>=0.3f || Float.parseFloat(niobiumMin)>=0.06f ||
						Float.parseFloat(seleniumMin)>=0.1f || Float.parseFloat(siliconMin)>=0.6f ||
						Float.parseFloat(telluriumMin)>=0.1f || Float.parseFloat(titaniumMin)>=0.05f ||
						Float.parseFloat(tungstenMin)>=0.3f || Float.parseFloat(vanadiumMin)>=0.1f ||
						Float.parseFloat(zirconiumMin)>=0.05f));
						ovralResult = ovralResult & singleResults[3];
						break;
					}
					case "condition 4":
					{
						conditionDetails = "...then the minimum width must be 600 mm or more.";
						String widthMin = getProdValue(jObj, "ProductDimensions", "Width", "Minimum");
						resultValues = getFormatedResultValues("widthMin", widthMin);
						singleResults[4] = checkAndReport("Condition 4", conditionDetails, resultValues, 
								(Float.parseFloat(widthMin)>=600f));
						ovralResult = ovralResult & singleResults[4];
						break;
					}
					case "condition 5":
					{
						conditionDetails = "...then the maximum width must be less than 600 mm.";
						String widthMax = getProdValue(jObj, "ProductDimensions", "Width", "Maximum");
						resultValues = getFormatedResultValues("widthMax", widthMax);
						singleResults[5]= checkAndReport("Condition 5", conditionDetails, resultValues,
								(Float.parseFloat(widthMax)<600f)); 
						ovralResult = ovralResult & singleResults[5];
						break;
					}
					case "condition 6":
					{
						conditionDetails = "Tool steel 6a ...then the minimum carbon must be greater than 1.2 percent and the minimum"
								+ " chromium must be greater than 10.5 percent OR "
								+ "6b ...then the minimum carbon must be equal to or greater than 0.3 percent and the minimum "
								+ "chromium must be equal to or greater than 1.25 percent and the maximum "
								+ "chromium must be less than 10.5 percent OR "
								+ "6c ...then the minimum carbon must be equal to or greater than 0.85 percent and the minimum manganese "
								+ "must be equal to or greater than 1 percent and the maximum manganese must be equal to or less than 1.8 percent OR "
								+ "6d ...then the minimum chromium must be equal to or greater than 0.9 percent and the maximum chromium must be equal"
								+ " to or less than 1.2 percent and the minimum molybdenum must be equal to or greater than 0.9 percent and "
								+ "the maximum molybdenum must be equal to or less than 1.4 percent OR "
								+ "6e ...then the minimum carbon is equal to or greater than 0.5 percent and the minimum molybdenum is equal to or "
								+ "greater than 3.5 percent OR "
								+ "6f ...then the minimum carbon is equal to or greater than 0.5 percent carbon and the minimum tungsten is equal"
								+ " to or greater than 5.5 percent.";
						String carbonMin = getProdValue(jObj, "ChemicalComposition", "Carbon", "Minimum");
						String chromiumMin =  getProdValue(jObj, "ChemicalComposition", "Chromium", "Minimum");
						String chromiumMax =  getProdValue(jObj, "ChemicalComposition", "Chromium", "Maximum");
						String manganeseMin =  getProdValue(jObj, "ChemicalComposition", "Manganese", "Minimum");
						String manganeseMax =  getProdValue(jObj, "ChemicalComposition", "Manganese", "Maximum");
						String molybdenumMin =  getProdValue(jObj, "ChemicalComposition", "Molybdenum", "Minimum");
						String molybdenumMax =  getProdValue(jObj, "ChemicalComposition", "Molybdenum", "Maximum");
						String tungstenMin =  getProdValue(jObj, "ChemicalComposition", "Tungsten", "Minimum");
						resultValues = getFormatedResultValues("carbonMin", carbonMin, "chromiumMin", chromiumMin, 
								"chromiumMax", chromiumMax,	"manganeseMin", manganeseMin, "manganeseMax", manganeseMax,
								"molybdenumMin", molybdenumMin, "molybdenumMax", molybdenumMax,	"tungstenMin", tungstenMin);
						
						singleResults[6] = checkAndReport("Condition 6", conditionDetails, resultValues, 
								(
								(Float.parseFloat(carbonMin)>1.2f && Float.parseFloat(chromiumMin)>10.5f)|| 
								(Float.parseFloat(carbonMin)>=0.3f && Float.parseFloat(chromiumMin)>=1.25f && Float.parseFloat(chromiumMax)<10.5f)|| 
								(Float.parseFloat(carbonMin)>=0.85f && Float.parseFloat(manganeseMin)>=1f && Float.parseFloat(manganeseMax)<=1.8f)|| 
								(Float.parseFloat(chromiumMin)>=0.9f && Float.parseFloat(chromiumMax)<=1.2f && Float.parseFloat(molybdenumMin)>=0.9f 
								&& Float.parseFloat(molybdenumMax)<=1.4f)|| 
								(Float.parseFloat(carbonMin)>=0.5f && Float.parseFloat(molybdenumMin)>=3.5f)|| 
								(Float.parseFloat(carbonMin)>=0.5f && Float.parseFloat(tungstenMin)>=5.5f))); 
						ovralResult = ovralResult & singleResults[6];
						break;
					}
					case "condition 7":
					{
						conditionDetails = "Ball bearing steel ...then the minimum carbon must be equal to or greater than 0.95 percent, "
								+ "and the maximum carbon must be equal to or less than 1.13 percent, and the minimum manganese must be equal "
								+ "to or greater than 0.22 percent, and the maximum manganese must be equal to or less than 0.48 percent, "
								+ "and the maximum sulfur must be equal to or less than 0.03 percent, and the maximum phosphorus must be equal "
								+ "to or less than 0.03 percent, and the minimum silicon must be equal to or greater than 0.18 percent, "
								+ "and the maximum silicon must be equal to or less than 0.37 percent, and the minimum chromium must be equal "
								+ "to or greater than 1.25 percent, and the maximum chromium must be equal to or less than 1.65 percent, "
								+ "and the maximum nickel must be equal to or less than 0.28 percent, and the maximum copper must be equal "
								+ "to or less than 0.38 percent, and the maximum molybdenum must be equal to or less than 0.09 percent.";
						String carbonMin = getProdValue(jObj, "ChemicalComposition", "Carbon", "Minimum");
						String carbonMax = getProdValue(jObj, "ChemicalComposition", "Carbon", "Maximum");
						String manganeseMin =  getProdValue(jObj, "ChemicalComposition", "Manganese", "Minimum");
						String manganeseMax =  getProdValue(jObj, "ChemicalComposition", "Manganese", "Maximum");
						String sulfurMax = getProdValue(jObj, "ChemicalComposition", "Sulfur", "Maximum");
						String phosphorusMax = getProdValue(jObj, "ChemicalComposition", "Phosphorus", "Maximum");
						String siliconMin =  getProdValue(jObj, "ChemicalComposition", "Silicon", "Minimum");
						String siliconMax =  getProdValue(jObj, "ChemicalComposition", "Silicon", "Maximum");
						String chromiumMin =  getProdValue(jObj, "ChemicalComposition", "Chromium", "Minimum");
						String chromiumMax =  getProdValue(jObj, "ChemicalComposition", "Chromium", "Maximum");
						String nickelMax =  getProdValue(jObj, "ChemicalComposition", "Nickel", "Maximum");
						String copperMax =  getProdValue(jObj, "ChemicalComposition", "Copper", "Maximum");
						String molybdenumMax =  getProdValue(jObj, "ChemicalComposition", "Molybdenum", "Maximum");
						resultValues = getFormatedResultValues("carbonMin", carbonMin, "carbonMax", carbonMax, "manganeseMin", manganeseMin,
								"manganeseMax", manganeseMax, "sulfurMax", sulfurMax, "phosphorusMax", phosphorusMax, "siliconMin", siliconMin,
								"siliconMax", siliconMax, "chromiumMin", chromiumMin, "chromiumMax", chromiumMax, "nickelMax", nickelMax,
								"copperMax", copperMax, "molybdenumMax", molybdenumMax);
						singleResults[7] = checkAndReport("Condition 7", conditionDetails, resultValues, 
								(	Float.parseFloat(carbonMin)>=0.95f && Float.parseFloat(carbonMax)<=1.13f &&
									Float.parseFloat(manganeseMin)>=0.22f && Float.parseFloat(manganeseMax)<=0.48f &&
									Float.parseFloat(sulfurMax)<=0.03f && Float.parseFloat(phosphorusMax)<=0.03f &&
									Float.parseFloat(siliconMin)>=0.18f && Float.parseFloat(siliconMax)<=0.37f &&
									Float.parseFloat(chromiumMin)>=1.25f && Float.parseFloat(chromiumMax)<=1.65f&&
									Float.parseFloat(nickelMax)<=0.28f && Float.parseFloat(copperMax)<=0.38f&&
									Float.parseFloat(molybdenumMax)<=0.09f
								)); 
						ovralResult = ovralResult & singleResults[7];
						break;
					}
					case "condition 8": 
					{
						conditionDetails = "High speed steel ...then the"
								+ " sum of the minimum percent molybdenum plus tungsten plus vanadium must be equal to or greater than 7 percent, "
								+ "or the sum of the minimum percent molybdenum plus tungsten must be equal to or greater than 7 percent, "
								+ "or the sum of the minimum percent molybdenum plus vanadium must be equal to or greater than 7 percent, "
								+ "or the sum of the minimum percent tungsten plus vanadium must be equal to or greater than 7 percent, "
								+ "and the minimum carbon must be equal to or greater than 0.6 percent, and the minimum chromium "
								+ "must be equal to or greater than 3 percent, and the maximum chromium must be equal to or less than 6 percent.";
						String molybdenumMin =  getProdValue(jObj, "ChemicalComposition", "Molybdenum", "Minimum");
						String tungstenMin =  getProdValue(jObj, "ChemicalComposition", "Tungsten", "Minimum");
						String vanadiumMin =  getProdValue(jObj, "ChemicalComposition", "Vanadium", "Minimum");
						String carbonMin = getProdValue(jObj, "ChemicalComposition", "Carbon", "Minimum");
						String chromiumMin =  getProdValue(jObj, "ChemicalComposition", "Chromium", "Minimum");
						String chromiumMax =  getProdValue(jObj, "ChemicalComposition", "Chromium", "Maximum");
						resultValues = getFormatedResultValues("molybdenumMin", molybdenumMin, "tungstenMin", tungstenMin, "vanadiumMin", vanadiumMin,
								"carbonMin", carbonMin, "chromiumMin", chromiumMin, "chromiumMax", chromiumMax);
						
						//((( (molybdenum_min + tungsten_min ) >= 7 ) || ((molybdenum_min + vanadium_min) >= 7) || ((tungsten_min + vanadium_min) >= 7))
						singleResults[8] = checkAndReport("Condition 8", conditionDetails, resultValues, 
							(
								(
								(Float.parseFloat(molybdenumMin)+Float.parseFloat(tungstenMin)+Float.parseFloat(vanadiumMin)>=7)||	
								(Float.parseFloat(molybdenumMin)+Float.parseFloat(tungstenMin)>=7f)||	
								(Float.parseFloat(molybdenumMin)+Float.parseFloat(vanadiumMin)>=7f)||	
								(Float.parseFloat(tungstenMin)+ Float.parseFloat(vanadiumMin)>=7f)
								) && 
								(Float.parseFloat(carbonMin)>=0.6f && Float.parseFloat(chromiumMin)>=3f && Float.parseFloat(chromiumMax)<=6f)
							)
							); 
						ovralResult = ovralResult & singleResults[8];
						break;
					}
					case "condition 9":
					{
						conditionDetails = "Non-alloy free-cutting steel ...then the minimum sulfur must be equal to or greater than 0.08 percent, "
								+ "or the minimum lead must be equal to or greater than 0.1 percent, or the minimum selenium must be greater than "
								+ "0.05 percent, or the minimum tellurium must be greater than 0.01 percent, or the minimum bismuth must be greater "
								+ "than 0.05 percent.";
						String sulfurMin = getProdValue(jObj, "ChemicalComposition", "Sulfur", "Minimum");
						String leadMin =  getProdValue(jObj, "ChemicalComposition", "Lead", "Minimum");
						String seleniumMin =  getProdValue(jObj, "ChemicalComposition", "Selenium", "Minimum");
						String telluriumMin =  getProdValue(jObj, "ChemicalComposition", "Tellurium", "Minimum");
						String bismuthMin =  getProdValue(jObj, "ChemicalComposition", "Bismuth", "Minimum");
						resultValues = getFormatedResultValues("sulfurMin", sulfurMin, "leadMin", leadMin, "seleniumMin", seleniumMin,
								"telluriumMin", telluriumMin, "bismuthMin", bismuthMin);
						singleResults[9] = checkAndReport("Condition 9", conditionDetails, resultValues, 
								(Float.parseFloat(sulfurMin)>=0.08f || Float.parseFloat(leadMin)>=0.1f ||
								Float.parseFloat(seleniumMin)>0.05f || Float.parseFloat(telluriumMin)>0.01f ||
								Float.parseFloat(bismuthMin)>0.05f
								)); 
						ovralResult = ovralResult & singleResults[9];
						break;
					}
					case "condition 10":
					{
						conditionDetails = "Flat-rolled (straight) less than 4.75 mm thickness ...then the minimum width must be"
								+ " greater than or equal to 10 times the maximum thickness.";
						String widthMin = getProdValue(jObj, "ProductDimensions", "Width", "Minimum");
						String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						resultValues = getFormatedResultValues("widthMin", widthMin, "thicknessMax", thicknessMax);
						singleResults[10] = checkAndReport("Condition 10", conditionDetails, resultValues, 
								(Float.parseFloat(widthMin)>= 10f*Float.parseFloat(thicknessMax)));
						ovralResult = ovralResult & singleResults[10];
						break;
					}
					case "condition 11":{
						conditionDetails = "Chipper knife steel ...then the minimum iron must be greater than 0, and the minimum carbon "
								+ "must be equal to or greater than 0.48 percent, and the maximum carbon must be equal to or less than 0.55 percent,"
								+ " and the minimum manganese must be equal to or greater than 0.2 percent, and the maximum manganese must be equal "
								+ "to or less than 0.5 percent, and the minimum silicon must be equal to or greater than 0.75 percent, and the maximum"
								+ " silicon must be equal to or less than 1.05 percent, and the minimum chromium must be equal to or greater than "
								+ "7.25 percent, and the maximum chromium must be equal to or less than 8.75 percent, and the minimum molybdenum must "
								+ "be equal to or greater than 1.25 percent, and the maximum molybdenum must be equal to or less than 1.75 percent, and "
								+ "the maximum tungsten must be equal to or less than 1.75 percent, and the minimum vanadium must be equal to or greater"
								+ " than 0.2 percent, and the maximum vanadium must be equal to or less than 0.55 percent.";
						String ironMin = getProdValue(jObj, "ChemicalComposition", "Iron", "Minimum");
						String carbonMin = getProdValue(jObj, "ChemicalComposition", "Carbon", "Minimum");
						String carbonMax = getProdValue(jObj, "ChemicalComposition", "Carbon", "Maximum");
						String manganeseMin =  getProdValue(jObj, "ChemicalComposition", "Manganese", "Minimum");
						String manganeseMax =  getProdValue(jObj, "ChemicalComposition", "Manganese", "Maximum");
						String siliconMin =  getProdValue(jObj, "ChemicalComposition", "Silicon", "Minimum");
						String siliconMax =  getProdValue(jObj, "ChemicalComposition", "Silicon", "Maximum");
						String chromiumMin =  getProdValue(jObj, "ChemicalComposition", "Chromium", "Minimum");
						String chromiumMax =  getProdValue(jObj, "ChemicalComposition", "Chromium", "Maximum");
						String molybdenumMin =  getProdValue(jObj, "ChemicalComposition", "Molybdenum", "Minimum");
						String molybdenumMax =  getProdValue(jObj, "ChemicalComposition", "Molybdenum", "Maximum");
						String tungstenMax =  getProdValue(jObj, "ChemicalComposition", "Tungsten", "Maximum");
						String vanadiumMin =  getProdValue(jObj, "ChemicalComposition", "Vanadium", "Minimum");
						String vanadiumMax =  getProdValue(jObj, "ChemicalComposition", "Vanadium", "Maximum");
						resultValues = getFormatedResultValues("ironMin", ironMin, "carbonMin", carbonMin, "carbonMax", carbonMax, 
								"manganeseMin", manganeseMin, "manganeseMax", manganeseMax, "siliconMin", siliconMin, "siliconMax", siliconMax, 
								"chromiumMin", chromiumMin, "chromiumMax", chromiumMax, "molybdenumMin", molybdenumMin, "molybdenumMax", molybdenumMax,
								"tungstenMax", tungstenMax, "vanadiumMin", vanadiumMin, "vanadiumMax", vanadiumMax);
						
						singleResults[11] = checkAndReport("Condition 11", conditionDetails, resultValues, 
								(Float.parseFloat(ironMin)> 0f && Float.parseFloat(carbonMin)>=0.48f && Float.parseFloat(carbonMax) <= 0.55f &&
								 Float.parseFloat(manganeseMin)>=0.2f && Float.parseFloat(manganeseMax) <=0.5f && Float.parseFloat(siliconMin)>=0.75f &&
								 Float.parseFloat(siliconMax)<=1.05f && Float.parseFloat(chromiumMin)>=7.25f && Float.parseFloat(chromiumMax)<=8.75f &&
								 Float.parseFloat(molybdenumMin)>=1.25f && Float.parseFloat(molybdenumMax)<=1.75f && Float.parseFloat(tungstenMax)<=1.75f &&
								 Float.parseFloat(vanadiumMin)>=0.2f && Float.parseFloat(vanadiumMax)<=0.55f));
								
						ovralResult = ovralResult & singleResults[11];
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
						resultValues = getFormatedResultValues("carbonMax", carbonMax, "siliconMin", siliconMin, "siliconMax", 
								siliconMax, "aluminumMax", aluminumMax);
						singleResults[12] = checkAndReport("Condition 12", conditionDetails, resultValues, (Float.parseFloat(carbonMax)<=0.08f 
								&& Float.parseFloat(siliconMin)>=0.6f && Float.parseFloat(siliconMax) <=6f 
								&& Float.parseFloat(aluminumMax) <=1f)); 
						ovralResult = ovralResult & singleResults[12];
						break;
					}
					case "condition 13":
					{
						conditionDetails = "Silico-manganese steel ...then the maximum carbon must be equal to or less than 0.7 "
								+ "percent, and the minimum manganese must be equal to or greater than 0.5 percent, and the maximum "
								+ "manganese must be equal to or less than 1.9 percent, and the minimum silicon must be equal to or "
								+ "greater than 0.6 percent, and the maximum silicon must be equal to or less than 2.3 percent.";
						String carbonMax = getProdValue(jObj, "ChemicalComposition", "Carbon", "Maximum");
						String manganeseMin = getProdValue(jObj, "ChemicalComposition", "Manganese", "Minimum");
						String manganeseMax = getProdValue(jObj, "ChemicalComposition", "Manganese", "Maximum");
						String siliconMin = getProdValue(jObj, "ChemicalComposition", "Silicon", "Minimum");
						String siliconMax = getProdValue(jObj, "ChemicalComposition", "Silicon", "Maximum");
						resultValues = getFormatedResultValues("carbonMax", carbonMax, "manganeseMin", manganeseMin,
								"manganeseMax", manganeseMax,"siliconMin", siliconMin, "siliconMax", siliconMax);
						singleResults[13] = checkAndReport("Condition 13", conditionDetails, resultValues, 
								(Float.parseFloat(carbonMax)<=0.7f 	&& Float.parseFloat(manganeseMin)>=0.5f && Float.parseFloat(manganeseMax) <=1.9f 
								&& Float.parseFloat(siliconMin)>=0.6f && Float.parseFloat(siliconMax)<=2.3f)); 
						ovralResult = ovralResult & singleResults[13];
						break;
					}
					case "condition 14":
					{
						/*conditionDetails = "High-strength steel ...(then the maximum thickness must be less than 3 mm and the minimum yield "
								+ "point must be  equal to 275 MPa) OR ( the minimum thickness is equal to or greater than"
								+ " 3 mm and the minimum yield point is 355 MPa.)";*/
						conditionDetails ="the maximum thickness must be less than 3 mm and the minimum yield point"
								+ " must be greater than or equal to 275 MPa or the minimum thickness is equal to"
								+ " or greater than 3 mm and the minimum yield point is greater than or equal to 355 MPa.";
						
						String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
						String yieldStrengthMin = getProdValue(jObj, "ProductStrength", "YieldStrength", "Minimum");
						resultValues = getFormatedResultValues("thicknessMax", thicknessMax, "thicknessMin", thicknessMin,
								"yieldStrengthMin", yieldStrengthMin);
						singleResults[14] = checkAndReport("Condition 14", conditionDetails, resultValues, 
						(		(Float.parseFloat(thicknessMax) < 3f && Float.parseFloat(yieldStrengthMin) >= 275f) 
								||( Float.parseFloat(thicknessMin)>=3f && Float.parseFloat(yieldStrengthMin) >= 355f)));
						
						ovralResult = ovralResult & singleResults[14];
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
						resultValues = getFormatedResultValues("thicknessMax", thicknessMax, "widthMax", widthMax, 
								"chromiumMax", chromiumMax);
						singleResults[15] = checkAndReport("Condition 15", conditionDetails, resultValues, (Float.parseFloat(thicknessMax)<=0.25f 
								&& Float.parseFloat(widthMax)<=23f && Float.parseFloat(chromiumMax)<=14.7f));
						ovralResult = ovralResult & singleResults[15];
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
						resultValues = getFormatedResultValues("widthMin", widthMin, "widthMax", widthMax, "thicknessMin", thicknessMin);
						singleResults[16] = checkAndReport("Condition 16", conditionDetails, resultValues, (Float.parseFloat(widthMin)>150f
								&& Float.parseFloat(widthMax)<=1250f && Float.parseFloat(thicknessMin)>=4f)); 
						ovralResult = ovralResult & singleResults[16];
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
						resultValues = getFormatedResultValues("carbonMax", carbonMax, "chromiumMin", chromiumMin, "chromiumMax", chromiumMax);
						singleResults[17] = checkAndReport("Condition 17", conditionDetails, resultValues, 
								(Float.parseFloat(carbonMax)<0.3f && Float.parseFloat(chromiumMin)>=4f
								&& Float.parseFloat(chromiumMax)<10.5f)); 
						ovralResult = ovralResult & singleResults[17];
						break;
					}
					case "condition 18":
					{
						conditionDetails = "Tire cord-quality steel wire rod ...then the minimum carbon must be equal to or greater than 0.68 percent, "
								+ "and the minimum aluminum must be less than 0.01 percent, and the sum of the maximum percent phosphorus and sulfur"
								+ " must be equal to or less than 0.040 percent, and the maximum nitrogen must be less than 0.008 percent, "
								+ "and the sum of the maximum copper, nickel, and chromium must be equal to or less than 0.55 percent, "
								+ "and the minimum outside diameter must be equal to or greater than 5.0 mm, and the maximum outside diameter must "
								+ "be equal to or less than 6.0 mm.";
						String carbonMin = getProdValue(jObj, "ChemicalComposition", "Carbon", "Minimum");
						String aluminumMin =  getProdValue(jObj, "ChemicalComposition", "Aluminum", "Minimum");
						String sulfurMax = getProdValue(jObj, "ChemicalComposition", "Sulfur", "Maximum");
						String phosphorusMax = getProdValue(jObj, "ChemicalComposition", "Phosphorus", "Maximum");
						String nitrogenMax = getProdValue(jObj, "ChemicalComposition", "Nitrogen", "Maximum");
						String copperMax =  getProdValue(jObj, "ChemicalComposition", "Copper", "Maximum");
						String nickelMax =  getProdValue(jObj, "ChemicalComposition", "Nickel", "Maximum");
						String chromiumMax =  getProdValue(jObj, "ChemicalComposition", "Chromium", "Maximum");	
						String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
						String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
						resultValues = getFormatedResultValues("carbonMin", carbonMin, "aluminumMin", aluminumMin, "sulfurMax", sulfurMax, 
								"phosphorusMax", phosphorusMax, "nitrogenMax", nitrogenMax, "copperMax", copperMax, "nickelMax", nickelMax,
								"chromiumMax", chromiumMax, "outsideDiameterMin", outsideDiameterMin, "outsideDiameterMax", outsideDiameterMax);
						singleResults[18] = checkAndReport("Condition 18", conditionDetails, resultValues, 
									(Float.parseFloat(carbonMin) >=0.68f && Float.parseFloat(aluminumMin) < 0.01f &&
									(Float.parseFloat(sulfurMax) + Float.parseFloat(phosphorusMax) <= 0.040f) &&
									Float.parseFloat(nitrogenMax) < 0.008f && 
									(Float.parseFloat(copperMax) + Float.parseFloat(nickelMax) + Float.parseFloat(chromiumMax)  <= 0.55f)&&
									Float.parseFloat(outsideDiameterMin) >= 5f  && Float.parseFloat(outsideDiameterMax) <= 6f)); 		
								
						ovralResult = ovralResult & singleResults[18];
						break;
					}
					case "condition 19":
					{
						conditionDetails = "Welding quality wire rod ...then the maximum percent carbon must be less than 0.2, and the maximum "
								+ "percent sulfur must be less than 0.04, and the maximum percent phosphorus must be less than 0.04, and the "
								+ "maximum outside diameter must be less than 10.0 mm.";
						String carbonMax = getProdValue(jObj, "ChemicalComposition", "Carbon", "Maximum");
						String sulfurMax = getProdValue(jObj, "ChemicalComposition", "Sulfur", "Maximum");
						String phosphorusMax = getProdValue(jObj, "ChemicalComposition", "Phosphorus", "Maximum");
						String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
						resultValues = getFormatedResultValues("carbonMax", carbonMax, "sulfurMax", sulfurMax,  
								"phosphorusMax", phosphorusMax, "outsideDiameterMax", outsideDiameterMax);
						singleResults[19] = checkAndReport("Condition 19", conditionDetails, resultValues, 
								(Float.parseFloat(carbonMax)<0.2f && Float.parseFloat(sulfurMax)<0.04f
								&& Float.parseFloat(phosphorusMax)<0.04f && Float.parseFloat(outsideDiameterMax)<10f)); 
						ovralResult = ovralResult & singleResults[19];
						break;
					}
					case "condition 20":
					{
						conditionDetails = "...then ASTM A313 must be listed or the cell may be blank.";
						String org = getProdValue(jObj, "ProductStandards", "Organization");
						String des = getProdValue(jObj, "ProductStandards", "Designation");
						resultValues = getFormatedResultValues("Organization", org, "Designation", des );
						singleResults[20] = checkAndReport("Condition 20", conditionDetails, resultValues, 
										("ASTM".equalsIgnoreCase(org) && "A313".equalsIgnoreCase(des))||
										("".equalsIgnoreCase(org) && "".equalsIgnoreCase(des)));
						ovralResult = ovralResult & singleResults[20];
						break;
					}
					case "condition 21":
					{
						conditionDetails = "Cold heading quality steel ...then ASTM F2282 must be listed or the cell may be blank.";
						String org = getProdValue(jObj, "ProductStandards", "Organization");
						String des = getProdValue(jObj, "ProductStandards", "Designation");
						resultValues = getFormatedResultValues("Organization", org, "Designation", des);
						singleResults[21] = checkAndReport("Condition 21", conditionDetails, resultValues, 
								("ASTM".equalsIgnoreCase(org) && "F2282".equalsIgnoreCase(des))||
								("".equalsIgnoreCase(org) && "".equalsIgnoreCase(des)));
						ovralResult = ovralResult & singleResults[21];
						break;
					}
					case "condition 22":
					{
						conditionDetails = "High nickel alloy steel ...then the minimum percent nickel must be "
								+ "equal to or greater than 22.";
						String nickelMin = getProdValue(jObj, "ChemicalComposition", "Nickel", "Minimum");
						resultValues = getFormatedResultValues("nickelMin", nickelMin);
						singleResults[22] = checkAndReport("Condition 22", conditionDetails, resultValues, 
						(Float.parseFloat(nickelMin)>=24f)); 
						ovralResult = ovralResult & singleResults[22];
						break;
					}
					case "condition 23":
					{
						/*conditionDetails ="Non-alloy ...then the maximum percentage must be less than 0.3 percent of aluminum,"
								+ " and less than 0.1 percent of antimony, and less than 0.1 percent of bismuth, and less than "
								+ "0.0008 percent of boron, and less than 0.3 percent of chromium, and less than 0.3 percent of "
								+ "cobalt, and less than 0.4 percent of copper, and less than 0.4 percent of lead, and less than "
								+ "1.65 percent of manganese, and less than 0.08 percent of molybdenum, and less than 0.3 percent "
								+ "of nickel, and less than 0.06 percent of niobium, and less than 0.1 percent selenium, and 0.6 "
								+ "percent of silicon, and less than 0.1 percent tellurium, and less than 0.05 percent of titanium, "
								+ "and less than 0.3 percent of tungsten (wolfram), and less than 0.1 percent of vanadium, and "
								+ "less than 0.05 percent of zirconium.";*/
						
						conditionDetails ="then the maximum percentage must be less than 0.3 percent of aluminum, and less than 0.1 "
								+ "percent of antimony, and less than 0.1 percent of bismuth, and less than 0.0008 percent of boron, "
								+ "and less than 0.3 percent of chromium, and less than 0.3 percent of cobalt, and less than 0.4 percent "
								+ "of copper, and less than 0.4 percent of lead, ***and less than 0.1 percent magnesium***, and less than"
								+ " 1.65 percent of manganese, and less than 0.08 percent of molybdenum, and less than 0.3 percent of nickel,"
								+ " and less than 0.06 percent of niobium, and less than 0.1 percent selenium, and 0.6 percent of silicon,"
								+ " and less than 0.1 percent tellurium, and less than 0.05 percent of titanium, and less than 0.3 percent "
								+ "of tungsten (wolfram), and less than 0.1 percent of vanadium, and less than 0.05 percent of zirconium.";
						String aluminumMax =  getProdValue(jObj, "ChemicalComposition", "Aluminum", "Maximum");
						String antimonyMax =  getProdValue(jObj, "ChemicalComposition", "Antimony", "Maximum");
						String bismuthMax =  getProdValue(jObj, "ChemicalComposition", "Bismuth", "Maximum");
						String boronMax =  getProdValue(jObj, "ChemicalComposition", "Boron", "Maximum");
						String chromiumMax =  getProdValue(jObj, "ChemicalComposition", "Chromium", "Maximum");
						String cobaltMax =  getProdValue(jObj, "ChemicalComposition", "Cobalt", "Maximum");
						String copperMax =  getProdValue(jObj, "ChemicalComposition", "Copper", "Maximum");
						String leadMax =  getProdValue(jObj, "ChemicalComposition", "Lead", "Maximum");
						String magnesiumMax =  getProdValue(jObj, "ChemicalComposition", "Magnesium", "Maximum");
						String manganeseMax =  getProdValue(jObj, "ChemicalComposition", "Manganese", "Maximum");
						String molybdenumMax =  getProdValue(jObj, "ChemicalComposition", "Molybdenum", "Maximum");
						String nickelMax =  getProdValue(jObj, "ChemicalComposition", "Nickel", "Maximum");
						String niobiumMax =  getProdValue(jObj, "ChemicalComposition", "Niobium", "Maximum");
						String seleniumMax =  getProdValue(jObj, "ChemicalComposition", "Selenium", "Maximum");
						String siliconMax =  getProdValue(jObj, "ChemicalComposition", "Silicon", "Maximum");
						String telluriumMax =  getProdValue(jObj, "ChemicalComposition", "Tellurium", "Maximum");
						String titaniumMax =  getProdValue(jObj, "ChemicalComposition", "Titanium", "Maximum");
						String tungstenMax =  getProdValue(jObj, "ChemicalComposition", "Tungsten", "Maximum");
						String vanadiumMax =  getProdValue(jObj, "ChemicalComposition", "Vanadium", "Maximum");
						String zirconiumMax =  getProdValue(jObj, "ChemicalComposition", "Zirconium", "Maximum");
						resultValues = getFormatedResultValues("aluminumMax", aluminumMax, "antimonyMax", antimonyMax,
						"bismuthMax", bismuthMax, "boronMax", boronMax, "chromiumMax", chromiumMax, "cobaltMax", cobaltMax,
						"copperMax", copperMax, "leadMax", leadMax, "magnesiumMax", magnesiumMax, "manganeseMax", manganeseMax, "molybdenumMax", molybdenumMax,
						"nickelMax", nickelMax, "niobiumMax", niobiumMax, "seleniumMax", seleniumMax, "siliconMax", siliconMax,
						"telluriumMax", telluriumMax, "titaniumMax", titaniumMax, "tungstenMax", tungstenMax, 
						"vanadiumMax", vanadiumMax, "zirconiumMax", zirconiumMax);
						singleResults[23] = checkAndReport("Condition 23", conditionDetails, resultValues, 
						(
						Float.parseFloat(aluminumMax)<0.3f && Float.parseFloat(antimonyMax)<0.1f &&
						Float.parseFloat(bismuthMax)<0.1f && Float.parseFloat(boronMax)<0.0008f &&
						Float.parseFloat(chromiumMax)<0.3f && Float.parseFloat(cobaltMax)<0.3f &&	
						Float.parseFloat(copperMax)<0.4f && Float.parseFloat(leadMax)<0.4f &&
						Float.parseFloat(magnesiumMax)<0.1f && Float.parseFloat(manganeseMax)<1.65f && Float.parseFloat(molybdenumMax)<0.08f &&
						Float.parseFloat(nickelMax)<0.3f && Float.parseFloat(niobiumMax)<0.06f &&
						Float.parseFloat(seleniumMax)<0.1f && Float.parseFloat(siliconMax)<0.6f &&
						Float.parseFloat(telluriumMax)<0.1f && Float.parseFloat(titaniumMax)<0.05f &&
						Float.parseFloat(tungstenMax)<0.3f && Float.parseFloat(vanadiumMax)<0.1f &&
						Float.parseFloat(zirconiumMax)<0.05f));
						ovralResult = ovralResult & singleResults[23]; 
						break;
					}
					case "condition 24":
					{
						/*conditionDetails ="Alloy ...then at least one of the minimum percentages must be equal to or greater "
								+ "than 0.3 percent of aluminum, or equal to or greater than 0.1 percent of antimony, or equal "
								+ "to or greater than 0.1 percent of bismuth, or equal to or greater than 0.0008 percent of "
								+ "boron, or equal to or greater than 0.3 percent of chromium, or equal to or greater than 0.3 percent "
								+ "of cobalt, or equal to or greater than 0.4 percent of copper, or equal to or greater than 0.4 percent"
								+ " of lead, or equal to or greater than 1.65 percent of manganese, or equal to or greater than 0.08 "
								+ "percent of molybdenum, or equal to or greater than 0.3 percent of nickel, or equal to or greater "
								+ "than 0.06 percent of niobium, or equal to or greater than 0.1 percent selenium, or equal to or "
								+ "greater than 0.6 percent of silicon, or equal to or greater than 0.1 percent tellurium, or equal "
								+ "to or greater than 0.05 percent of titanium, or equal to or greater than 0.3 percent of "
								+ "tungsten (wolfram), or equal to or greater than 0.1 percent of vanadium, or equal to or greater t"
								+ "han 0.05 percent of zirconium.";*/
						
						conditionDetails ="then at least one of the minimum percentages must be equal to or greater than 0.3 percent of aluminum,"
								+ " or equal to or greater than 0.1 percent of antimony, or equal to or greater than 0.1 percent of bismuth, "
								+ "or equal to or greater than 0.0008 percent of boron, or equal to or greater than 0.3 percent of chromium, "
								+ "or equal to or greater than 0.3 percent of cobalt, or equal to or greater than 0.4 percent of copper, "
								+ "or equal to or greater than 0.4 percent of lead, ***or equal to or greater than 0.1 percent magnesium***, "
								+ "or equal to or greater than 1.65 percent of manganese, or equal to or greater than 0.08 percent of molybdenum,"
								+ " or equal to or greater than 0.3 percent of nickel, or equal to or greater than 0.06 percent of niobium, "
								+ "or equal to or greater than 0.1 percent selenium, or equal to or greater than 0.6 percent of silicon, "
								+ "or equal to or greater than 0.1 percent tellurium, or equal to or greater than 0.05 percent of titanium, "
								+ "or equal to or greater than 0.3 percent of tungsten (wolfram), or equal to or greater than 0.1 percent of vanadium, "
								+ "or equal to or greater than 0.05 percent of zirconium.";
						String aluminumMin =  getProdValue(jObj, "ChemicalComposition", "Aluminum", "Minimum");
						String antimonyMin =  getProdValue(jObj, "ChemicalComposition", "Antimony", "Minimum");
						String bismuthMin =  getProdValue(jObj, "ChemicalComposition", "Bismuth", "Minimum");
						String boronMin =  getProdValue(jObj, "ChemicalComposition", "Boron", "Minimum");
						String chromiumMin =  getProdValue(jObj, "ChemicalComposition", "Chromium", "Minimum");
						String cobaltMin =  getProdValue(jObj, "ChemicalComposition", "Cobalt", "Minimum");
						String copperMin =  getProdValue(jObj, "ChemicalComposition", "Copper", "Minimum");
						String leadMin =  getProdValue(jObj, "ChemicalComposition", "Lead", "Minimum");
						String magnesiumMin =  getProdValue(jObj, "ChemicalComposition", "Magnesium", "Minimum");
						String manganeseMin =  getProdValue(jObj, "ChemicalComposition", "Manganese", "Minimum");
						String molybdenumMin =  getProdValue(jObj, "ChemicalComposition", "Molybdenum", "Minimum");
						String nickelMin =  getProdValue(jObj, "ChemicalComposition", "Nickel", "Minimum");
						String niobiumMin =  getProdValue(jObj, "ChemicalComposition", "Niobium", "Minimum");
						String seleniumMin =  getProdValue(jObj, "ChemicalComposition", "Selenium", "Minimum");
						String siliconMin =  getProdValue(jObj, "ChemicalComposition", "Silicon", "Minimum");
						String telluriumMin =  getProdValue(jObj, "ChemicalComposition", "Tellurium", "Minimum");
						String titaniumMin =  getProdValue(jObj, "ChemicalComposition", "Titanium", "Minimum");
						String tungstenMin =  getProdValue(jObj, "ChemicalComposition", "Tungsten", "Minimum");
						String vanadiumMin =  getProdValue(jObj, "ChemicalComposition", "Vanadium", "Minimum");
						String zirconiumMin =  getProdValue(jObj, "ChemicalComposition", "Zirconium", "Minimum");
						resultValues = getFormatedResultValues("aluminumMin", aluminumMin, "antimonyMin", antimonyMin,
						"bismuthMin", bismuthMin, "boronMin", boronMin, "chromiumMin", chromiumMin, "cobaltMin", cobaltMin,
						"copperMin", copperMin, "leadMin", leadMin, "magnesiumMin", magnesiumMin, "manganeseMin", manganeseMin, 
						"molybdenumMin", molybdenumMin, "nickelMin", nickelMin, "niobiumMin", niobiumMin, "seleniumMin", 
						seleniumMin, "siliconMin", siliconMin, "telluriumMin", telluriumMin, "titaniumMin", titaniumMin,
						"tungstenMin", tungstenMin, "vanadiumMin", vanadiumMin, "zirconiumMin", zirconiumMin);
						singleResults[24] = checkAndReport("Condition 24", conditionDetails, resultValues, 
						(
						Float.parseFloat(aluminumMin)>=0.3f || Float.parseFloat(antimonyMin)>=0.1f ||
						Float.parseFloat(bismuthMin)>=0.1f || Float.parseFloat(boronMin)>=0.0008f ||
						Float.parseFloat(chromiumMin)>=0.3f || Float.parseFloat(cobaltMin)>=0.3f ||	
						Float.parseFloat(copperMin)>=0.4f || Float.parseFloat(leadMin)>=0.4f || Float.parseFloat(magnesiumMin)>=0.1f ||
						Float.parseFloat(manganeseMin)>=1.65f || Float.parseFloat(molybdenumMin)>=0.08f ||
						Float.parseFloat(nickelMin)>=0.3f || Float.parseFloat(niobiumMin)>=0.06f ||
						Float.parseFloat(seleniumMin)>=0.1f || Float.parseFloat(siliconMin)>=0.6f ||
						Float.parseFloat(telluriumMin)>=0.1f || Float.parseFloat(titaniumMin)>=0.05f ||
						Float.parseFloat(tungstenMin)>=0.3f || Float.parseFloat(vanadiumMin)>=0.1f ||
						Float.parseFloat(zirconiumMin)>=0.05));
						ovralResult = ovralResult & singleResults[24];
						break;
					}
					case "condition 25":
					{
						conditionDetails = "Chapter 73 stainless ...then the maximum percentage of carbon must be 1.2 percent or "
								+ "less and the minimum percentage of chromium must be 10.5 percent or more.";
						String carbonMax = getProdValue(jObj, "ChemicalComposition", "Carbon", "Maximum");
						String chromiumMin = getProdValue(jObj, "ChemicalComposition", "Chromium", "Minimum");
						resultValues = getFormatedResultValues("carbonMax", carbonMax, "chromiumMin", chromiumMin);
						System.out.println(Float.parseFloat(carbonMax) <= 1.2f );
						System.out.println( Float.parseFloat(chromiumMin) >= 10.5f);
						singleResults[25] = checkAndReport("Condition 25", conditionDetails, resultValues,
								(Float.parseFloat(carbonMax)<=1.2f && Float.parseFloat(chromiumMin)>=10.5f)); 
						ovralResult = ovralResult & singleResults[25];
						break;
					}
					case "condition 26":
					{
						conditionDetails = "...then the maximum outside diameter and the minimum outside"
								+ " diameter must be equal to or less than 114.3 mm.";
						String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
						String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
						resultValues = getFormatedResultValues("outsideDiameterMin", outsideDiameterMin, "outsideDiameterMax", outsideDiameterMax);
						singleResults[26] = checkAndReport("Condition 26", conditionDetails, resultValues, 
						(Float.parseFloat(outsideDiameterMax)<=114.3f && Float.parseFloat(outsideDiameterMin)<=114.3f)); 
						ovralResult = ovralResult & singleResults[26];
						break;
					}
					case "condition 27":
					{
						/*conditionDetails = "...then the minimum outside diameter must be equal to or greater than 114.3 mm and"
								+ " less than 406.4 mm, and the maximum outside diameter must be equal to or greater than 114.3 "
								+ "mm and less than 406.4 mm.";*/
						conditionDetails = "...then the minimum outside diameter must be equal to or greater than 114.3 mm and equal"
								+ " to or less than 406.4 mm, and the maximum outside diameter must be equal to or greater"
								+ " than 114.3 mm and equal to or less than 406.4 mm.";
						String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
						String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
						resultValues = getFormatedResultValues("outsideDiameterMin", outsideDiameterMin, "outsideDiameterMax", outsideDiameterMax);
						singleResults[27] = checkAndReport("Condition 27", conditionDetails, resultValues, 
						(Float.parseFloat(outsideDiameterMin)>=114.3 && Float.parseFloat(outsideDiameterMin)<=406.4f 
						&& Float.parseFloat(outsideDiameterMax)>=114.3f	&& Float.parseFloat(outsideDiameterMax)<406.4f)); 
						ovralResult = ovralResult & singleResults[27];
						break;
					}
					case "condition 28":
					{
						conditionDetails = "...then the maximum outside diameter and the minimum outside "
								+ "diameter must be greater than 406.4 mm.";
						String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
						String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
						resultValues = getFormatedResultValues("outsideDiameterMin", outsideDiameterMin, "outsideDiameterMax", outsideDiameterMax);
						singleResults[28] = checkAndReport("Condition 28", conditionDetails, resultValues, 
						(Float.parseFloat(outsideDiameterMax)>406.4f && Float.parseFloat(outsideDiameterMin)>406.4f)); 
						ovralResult = ovralResult & singleResults[28];
						break;
					}
					case "condition 29":
					{
						conditionDetails = "...then the minimum outside diameter must be equal to or greater than 114.3 mm"
								+ " and less than 215.9 mm, and the maximum outside diameter must be equal to or greater "
								+ "than 114.3 mm and less than 215.9 mm.";
						String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
						String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
						resultValues = getFormatedResultValues("outsideDiameterMin", outsideDiameterMin, "outsideDiameterMax", outsideDiameterMax);
						singleResults[29] = checkAndReport("Condition 29", conditionDetails, resultValues, 
						(Float.parseFloat(outsideDiameterMin)>=114.3f &&	Float.parseFloat(outsideDiameterMin)<215.9f
						&& Float.parseFloat(outsideDiameterMax)>=114.3f 	&& Float.parseFloat(outsideDiameterMax)<215.9f)); 
						ovralResult = ovralResult & singleResults[29];
						break;
					}
					case "condition 30":
					{
						conditionDetails = "...then the maximum thickness must be less than 12.7 mm.";
						String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						resultValues = getFormatedResultValues("thicknessMax", thicknessMax);
						singleResults[30] = checkAndReport("Condition 30", conditionDetails, resultValues,
						(Float.parseFloat(thicknessMax)<12.7f ));
						ovralResult = ovralResult & singleResults[30];
						break;
					}
					case "condition 31":
					{
						conditionDetails = "...then the minimum thickness must be greater than or equal to 12.7 mm.";
						String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
						resultValues = getFormatedResultValues("thicknessMin", thicknessMin);
						singleResults[31] = checkAndReport("Condition 31", conditionDetails, resultValues, 
						(Float.parseFloat(thicknessMin)>=12.7f)); 
						ovralResult = ovralResult & singleResults[31];
						break;
					}
					case "condition 32":
					{
						conditionDetails = "...then the MINIMUM outside diameter must be equal to or less than 168.3 mm.";
						String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
						resultValues = getFormatedResultValues("outsideDiameterMin", outsideDiameterMin);
						singleResults[32] = checkAndReport("Condition 32", conditionDetails, resultValues,
								(Float.parseFloat(outsideDiameterMin) <= 168.3f));
						ovralResult = ovralResult & singleResults[32];
						break;
					}
					case "condition 33":
					{
						conditionDetails = "...then the minimum thickness must be greater than 9.5 mm.";
						String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
						resultValues = getFormatedResultValues("thicknessMin", thicknessMin);
						singleResults[33] = checkAndReport("Condition 33", conditionDetails, resultValues, 
						(Float.parseFloat(thicknessMin)>9.5f )); 
						ovralResult = ovralResult & singleResults[33];
						break;
					}
					case "condition 34":
					{
						conditionDetails = "...then the maximum thickness must be equal to or less than 9.5 mm.";
						String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						resultValues = getFormatedResultValues("thicknessMax", thicknessMax);
						singleResults[34] = checkAndReport("Condition 34", conditionDetails, resultValues, 
						(Float.parseFloat(thicknessMax)<=9.5f)); 
						ovralResult = ovralResult & singleResults[34];
						break;
					}
					//conditionDetails 52 = "...then the Minimum outside diameter must be greater than 609.6 mm.";
					case "condition 35":
					{
						conditionDetails = "...then minimum the outside diameter must be greater than 168.3 mm.";
						String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
						resultValues = getFormatedResultValues("outsideDiameterMin", outsideDiameterMin);
						singleResults[35] = checkAndReport("Condition 35", conditionDetails, resultValues,
								(Float.parseFloat(outsideDiameterMin) > 168.3f));
						ovralResult = ovralResult & singleResults[35];
						break;
					}
					case "condition 36":
					{
						conditionDetails = "...then the minimum outside diameter must be less than 215.9 mm.";
						String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
						resultValues = getFormatedResultValues("outsideDiameterMin", outsideDiameterMin);
						singleResults[36] = checkAndReport("Condition 36", conditionDetails, resultValues,
								(Float.parseFloat(outsideDiameterMin) < 215.9f));
						ovralResult = ovralResult & singleResults[36];
						break;
					}
					case "condition 37":
					{
						conditionDetails = "...then the minimum outside diameter must be equal to or greater than 215.9 mm "
								+ "and equal to or less than 406.4 mm, and the maximum outside diameter must be equal to or"
								+ " greater than 215.9 mm and equal to or less than 406.4 mm.";
						String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
						String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
						resultValues = getFormatedResultValues("outsideDiameterMin", outsideDiameterMin, "outsideDiameterMax", outsideDiameterMax);
						singleResults[37] = checkAndReport("Condition 37", conditionDetails, resultValues, 
						(Float.parseFloat(outsideDiameterMin)>=215.9f && Float.parseFloat(outsideDiameterMin)<=406.4f 
						&& Float.parseFloat(outsideDiameterMax)>=215.9f && Float.parseFloat(outsideDiameterMax)<=406.4f));
						ovralResult = ovralResult & singleResults[37]; 
						break;
					}
					case "condition 38":
					{
						conditionDetails = "...then the minimum outside diameter must be equal to or greater than 215.9 mm and equal "
								+ "to or less than 285.8 mm, and the maximum outside diameter must be equal to or greater than 215.9 mm "
								+ "and equal to or less than 285.8 mm.";
						
						String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
						String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
						resultValues = getFormatedResultValues("outsideDiameterMin", outsideDiameterMin, "outsideDiameterMax", outsideDiameterMax);
						singleResults[38] = checkAndReport("Condition 38", conditionDetails, resultValues, 
						(Float.parseFloat(outsideDiameterMin)>=215.9f && Float.parseFloat(outsideDiameterMin)<=285.8f 
						&& Float.parseFloat(outsideDiameterMax)>=215.9f && Float.parseFloat(outsideDiameterMax)<=285.8f));
						ovralResult = ovralResult & singleResults[38];
						break;
					}
					case "condition 39":
					{
						conditionDetails = "...then the minimum outside diameter must be greater than 285.8 mm and equal to or "
								+ "less than 406.4 mm, and the maximum outside diameter must be greater than 285.8 mm and equal "
								+ "to or less than 406.4 mm.";
						String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
						String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
						resultValues = getFormatedResultValues("outsideDiameterMin", outsideDiameterMin, "outsideDiameterMax", outsideDiameterMax);
						singleResults[39] = checkAndReport("Condition 39", conditionDetails, resultValues, 
						(Float.parseFloat(outsideDiameterMin) > 285.8f && Float.parseFloat(outsideDiameterMin)<=406.4f 
						&& Float.parseFloat(outsideDiameterMax)> 285.8f	&& Float.parseFloat(outsideDiameterMax)<=406.4f )); 
						ovralResult = ovralResult & singleResults[39];
						break;
					}
					case "condition 40":
					{
						conditionDetails = "...then the minimum outside diameter must be greater than 406.4 mm.";
						String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
						resultValues = getFormatedResultValues("outsideDiameterMin", outsideDiameterMin);
						singleResults[40] = checkAndReport("Condition 40", conditionDetails, resultValues, 
						(Float.parseFloat(outsideDiameterMin) > 406.4f)); 
						ovralResult = ovralResult & singleResults[40];
						break;
					}
					case "condition 41":
					{
						conditionDetails = "...then the minimum outside diameter and the maximum outside diameter must be less than 38.1 mm.";
						String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
						String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
						resultValues = getFormatedResultValues("outsideDiameterMin", outsideDiameterMin, "outsideDiameterMax", outsideDiameterMax);
						singleResults[41] = checkAndReport("Condition 41", conditionDetails, resultValues, 
						(Float.parseFloat(outsideDiameterMin) < 38.1f && Float.parseFloat(outsideDiameterMax) < 38.1f));
						ovralResult = ovralResult & singleResults[41]; 
						break;
					}
					case "condition 42":
					{
						conditionDetails = "...then the minimum outside diameter must be equal to or greater than 38.1 mm and less"
								+ " than 190.5 mm, and the maximum outside diameter must be greater than 38.1 mm and less than 190.5 mm";
						String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
						String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
						resultValues = getFormatedResultValues("outsideDiameterMin", outsideDiameterMin, "outsideDiameterMax", outsideDiameterMax);
						singleResults[42] = checkAndReport("Condition 42", conditionDetails, resultValues, 
						(Float.parseFloat(outsideDiameterMin) >= 38.1f && Float.parseFloat(outsideDiameterMin) < 190.5f	
						&& Float.parseFloat(outsideDiameterMax)> 38.1f && Float.parseFloat(outsideDiameterMax) < 190.5f )); 
						ovralResult = ovralResult & singleResults[42];
						break;
					}
					case "condition 43":
					{
						conditionDetails = "...then the minimum outside diameter and the maximum outside diameter must be greater than 285.8 mm.";
						String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
						String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
						resultValues = getFormatedResultValues("outsideDiameterMin", outsideDiameterMin, "outsideDiameterMax", outsideDiameterMax);
						singleResults[43] = checkAndReport("Condition 43", conditionDetails, resultValues, 
						(Float.parseFloat(outsideDiameterMin) > 285.8f && Float.parseFloat(outsideDiameterMax) > 285.8f));
						ovralResult = ovralResult & singleResults[43];
						break;
					}
					case "condition 44":
					{
						conditionDetails = "...then the maximum thickness must be less than 6.4 mm.";
						String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						resultValues = getFormatedResultValues("thicknessMax", thicknessMax);
						singleResults[44] = checkAndReport("Condition 44", conditionDetails, resultValues, 
						(Float.parseFloat(thicknessMax)< 6.4f )); 
						ovralResult = ovralResult & singleResults[44];
						break;
					}
					case "condition 45":
					{
						conditionDetails = "...then the minimum thickness must be equal to or greater than 6.4 mm, "
								+ "and the maximum thickness must be equal to or less than 12.7 mm.";
						String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
						String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						resultValues = getFormatedResultValues("thicknessMin", thicknessMin, "thicknessMax", thicknessMax);
						singleResults[45] = checkAndReport("Condition 45", conditionDetails, resultValues, 
						(Float.parseFloat(thicknessMin)>= 6.4f && Float.parseFloat(thicknessMax) <=12.7f)); 
						ovralResult = ovralResult & singleResults[45];
						break;
					}
					case "condition 46":
					{
						conditionDetails = "...then the minimum outside diameter must be greater than 114.3 mm and less than 190.5 mm, "
								+ "and the maximum outside diameter must be greater than 114.3 mm and less than 190.5 mm.";
						String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
						String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
						resultValues = getFormatedResultValues("outsideDiameterMin", outsideDiameterMin, "outsideDiameterMax", outsideDiameterMax);
						singleResults[46] = checkAndReport("Condition 46", conditionDetails, resultValues, 
						(Float.parseFloat(outsideDiameterMin) > 114.3f 	&& Float.parseFloat(outsideDiameterMin) < 190.5f 
						&& Float.parseFloat(outsideDiameterMax) > 114.3f && Float.parseFloat(outsideDiameterMax) < 190.5f));
						ovralResult = ovralResult & singleResults[46];
						break;
					}
					case "condition 47":
					{
						conditionDetails = "...then the minimum thickness must be equal to or greater than 12.7 mm, "
								+ "and the maximum thickness must be less than 19 mm.";
						String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
						String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						resultValues = getFormatedResultValues("thicknessMin", thicknessMin, "thicknessMax", thicknessMax);
						singleResults[47] = checkAndReport("Condition 47", conditionDetails, resultValues, 
						(Float.parseFloat(thicknessMin)>= 12.7f && Float.parseFloat(thicknessMax) < 19f)); 
						ovralResult = ovralResult & singleResults[47];
						break;
					}
					case "condition 48":
					{
						conditionDetails = "...then the minimum thickness must be equal to or greater than 19 mm.";
						String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
						resultValues = getFormatedResultValues("thicknessMin", thicknessMin);
						singleResults[48] = checkAndReport("Condition 48", conditionDetails, resultValues, 
						(Float.parseFloat(thicknessMin)>= 19f )); 
						ovralResult = ovralResult & singleResults[48];
						break;
					}
					case "condition 49":
					{
						conditionDetails = "...then the minimum outside diameter must be equal to or greater than 38.1 mm and less than 114.3 mm, "
								+ "and the maximum outside diameter must be greater than 38.1 mm and less than 114.3 mm.";
						String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
						String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
						resultValues = getFormatedResultValues("outsideDiameterMin", outsideDiameterMin, "outsideDiameterMax", outsideDiameterMax);
						singleResults[49] = checkAndReport("Condition 49", conditionDetails, resultValues, 
						(Float.parseFloat(outsideDiameterMin) >=38.1f && Float.parseFloat(outsideDiameterMin) < 114.3f 
						&&	Float.parseFloat(outsideDiameterMax) > 38.1f	&& Float.parseFloat(outsideDiameterMax) < 114.3f)); 
						ovralResult = ovralResult & singleResults[49];
						break;
					}
					case "condition 50":
					{
						/*conditionDetails = "...then the minimum outside diameter must be equal to or "
								+ "greater than 190.5 mm and equal to or less than 285.8 mm, "
								+ "and the maximum outside diameter must be equal to or greater than 190.5 mm "
								+ "and less than 285.8 mm.";*/
						conditionDetails = "the minimum outside diameter must be equal to or greater than 190.5 mm and equal to or less than 285.8 mm,"
								+ " and the maximum outside diameter must be equal to or greater than 190.5 mm and ***equal to*** or less than 285.8 mm.";
						
						
						String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
						String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
						resultValues = getFormatedResultValues("outsideDiameterMin", outsideDiameterMin, "outsideDiameterMax", outsideDiameterMax);
						singleResults[50] = checkAndReport("Condition 50", conditionDetails, resultValues, 
						(Float.parseFloat(outsideDiameterMin) >=190.5f && Float.parseFloat(outsideDiameterMin) <= 285.8f 
						&&	Float.parseFloat(outsideDiameterMax) >= 190.5f && Float.parseFloat(outsideDiameterMax) <= 285.8f));
						ovralResult = ovralResult & singleResults[50];
						break;
					}
					case "condition 51":
					{
						conditionDetails = "Semi-manufacture ...then the maximum percentage must be less than 0.3 percent of aluminum, "
								+ "and less than 0.1 percent of antimony, and less than 0.1 percent of bismuth, and less than 0.0008 percent of boron,"
								+ " and less than 2 percent of carbon, and less than 0.3 percent of chromium, and less than 0.3 percent of cobalt,"
								+ " and less than 0.4 percent of copper, and less than 0.4 percent of lead, and less than 1.65 percent of manganese,"
								+ " and less than 0.08 percent of molybdenum, and less than 0.3 percent of nickel, and less than 0.06 percent of niobium, "
								+ "and less than 0.1 percent selenium, and 0.6 percent of silicon, and less than 0.1 percent tellurium, "
								+ "and less than 0.05 percent of titanium, and less than 0.3 percent of tungsten (wolfram), and less than "
								+ "0.1 percent of vanadium, and less than 0.05 percent of zirconium. "
								+ "OR ...the minimum percent carbon must be equal to or greater than 2 percent "
								+ "(and a blank value will not be acceptable).";
						String aluminumMax =  getProdValue(jObj, "ChemicalComposition", "Aluminum", "Maximum");
						String antimonyMax =  getProdValue(jObj, "ChemicalComposition", "Antimony", "Maximum");
						String bismuthMax =  getProdValue(jObj, "ChemicalComposition", "Bismuth", "Maximum");
						String boronMax =  getProdValue(jObj, "ChemicalComposition", "Boron", "Maximum");
						String carbonMax = getProdValue(jObj, "ChemicalComposition", "Carbon", "Maximum");
						String chromiumMax =  getProdValue(jObj, "ChemicalComposition", "Chromium", "Maximum");
						String cobaltMax =  getProdValue(jObj, "ChemicalComposition", "Cobalt", "Maximum");
						String copperMax =  getProdValue(jObj, "ChemicalComposition", "Copper", "Maximum");
						String leadMax =  getProdValue(jObj, "ChemicalComposition", "Lead", "Maximum");
						String manganeseMax =  getProdValue(jObj, "ChemicalComposition", "Manganese", "Maximum");
						String molybdenumMax =  getProdValue(jObj, "ChemicalComposition", "Molybdenum", "Maximum");
						String nickelMax =  getProdValue(jObj, "ChemicalComposition", "Nickel", "Maximum");
						String niobiumMax =  getProdValue(jObj, "ChemicalComposition", "Niobium", "Maximum");
						String seleniumMax =  getProdValue(jObj, "ChemicalComposition", "Selenium", "Maximum");
						String siliconMax =  getProdValue(jObj, "ChemicalComposition", "Silicon", "Maximum");
						String telluriumMax =  getProdValue(jObj, "ChemicalComposition", "Tellurium", "Maximum");
						String titaniumMax =  getProdValue(jObj, "ChemicalComposition", "Titanium", "Maximum");
						String tungstenMax =  getProdValue(jObj, "ChemicalComposition", "Tungsten", "Maximum");
						String vanadiumMax =  getProdValue(jObj, "ChemicalComposition", "Vanadium", "Maximum");
						String zirconiumMax =  getProdValue(jObj, "ChemicalComposition", "Zirconium", "Maximum");
						String carbonMin = getProdValue(jObj, "ChemicalComposition", "Carbon", "Minimum");
						resultValues = getFormatedResultValues("aluminumMax", aluminumMax, "antimonyMax", antimonyMax,
								"bismuthMax", bismuthMax, "boronMax", boronMax, "carbonMax", carbonMax, "chromiumMax", chromiumMax,
								"cobaltMax", cobaltMax,	"copperMax", copperMax, "leadMax", leadMax, "manganeseMax", manganeseMax, 
								"molybdenumMax", molybdenumMax,	"nickelMax", nickelMax, "niobiumMax", niobiumMax, "seleniumMax", 
								seleniumMax, "siliconMax", siliconMax, "telluriumMax", telluriumMax, "titaniumMax", titaniumMax, 
								"tungstenMax", tungstenMax, "vanadiumMax", vanadiumMax, "zirconiumMax", zirconiumMax, "carbonMin", carbonMin);
						singleResults[51] = checkAndReport("Condition 51", conditionDetails, resultValues, 
						(
						(Float.parseFloat(aluminumMax) >= 2f)||
						(Float.parseFloat(aluminumMax)<0.3f && Float.parseFloat(antimonyMax)<0.1f && Float.parseFloat(bismuthMax)<0.1f &&
						 Float.parseFloat(boronMax)<0.0008f && Float.parseFloat(carbonMax)< 2f && Float.parseFloat(chromiumMax)<0.3f &&
						 Float.parseFloat(cobaltMax)<0.3f && Float.parseFloat(copperMax)<0.4f && Float.parseFloat(leadMax)<0.4f &&
						 Float.parseFloat(manganeseMax)<1.65f && Float.parseFloat(molybdenumMax)<0.08f && Float.parseFloat(nickelMax)<0.3f &&
						 Float.parseFloat(niobiumMax)<0.06f && Float.parseFloat(seleniumMax)<0.1f && Float.parseFloat(siliconMax)<0.6f &&
						 Float.parseFloat(telluriumMax)<0.1f && Float.parseFloat(titaniumMax)<0.05f && Float.parseFloat(tungstenMax)<0.3f &&
						 Float.parseFloat(vanadiumMax)<0.1f && Float.parseFloat(zirconiumMax)<0.05f)
						)	
						);
						ovralResult = ovralResult & singleResults[51];
						break;
					}
					case "condition 52":
					{
						conditionDetails = "...then the Minimum outside diameter must be greater than 609.6 mm.";
						String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
						resultValues = getFormatedResultValues("outsideDiameterMin", outsideDiameterMin);
						singleResults[52] = checkAndReport("Condition 52", conditionDetails, resultValues,
								(Float.parseFloat(outsideDiameterMin) > 609.6f));
						ovralResult = ovralResult & singleResults[52];
						break;
					}
					case "condition 53":
					{
						/*conditionDetails = "...then the minimum outside diameter must be greater than 406.4 mm and equal to or less than 609.6 mm,"
								+ " and the maximum outside diameter must be equal to or greater than 406.4 mm and less than 609.6 mm.";*/
						conditionDetails = "the minimum outside diameter must be greater than 406.4 mm and equal to or less than 609.6 mm,"
								+ " and the maximum outside diameter must be ***greater than 406.4 mm*** and equal to or less than 609.6 mm.";
						String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
						String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
						resultValues = getFormatedResultValues("outsideDiameterMin", outsideDiameterMin, "outsideDiameterMax", outsideDiameterMax);
						singleResults[53] = checkAndReport("Condition 53", conditionDetails, resultValues, 
						(Float.parseFloat(outsideDiameterMin) > 406.4f && Float.parseFloat(outsideDiameterMin) <= 609.6f 
						&&	Float.parseFloat(outsideDiameterMax) > 406.4f	&& Float.parseFloat(outsideDiameterMax) < 609.6f)); 
						ovralResult = ovralResult & singleResults[53];
						break;
					}
					case "condition 54":
					{
						conditionDetails = "...then the maximum thickness must be less than 1.65 mm.";
						String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						resultValues = getFormatedResultValues("thicknessMax", thicknessMax);
						singleResults[54] = checkAndReport("Condition 54", conditionDetails, resultValues, 
						(Float.parseFloat(thicknessMax) < 1.65f )); 
						ovralResult = ovralResult & singleResults[54];
						break;
					}
					case "condition 55":
					{
						conditionDetails = "...then the maximum thickness must be equal to or less than 2.54 mm.";
						String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						resultValues = getFormatedResultValues("thicknessMax", thicknessMax);
						singleResults[55] = checkAndReport("Condition 55", conditionDetails, resultValues, 
						(Float.parseFloat(thicknessMax) <= 2.54f )); 
						ovralResult = ovralResult & singleResults[55];
						break;
					}
					case "condition 56": //NOT IN USE ANYMORE
					{
						conditionDetails = "...then the maximum thickness must be less than 6.4 mm.";
						String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						resultValues = getFormatedResultValues("thicknessMax", thicknessMax);
						singleResults[56] = checkAndReport("Condition 56", conditionDetails, resultValues, 
						(Float.parseFloat(thicknessMax) < 6.4f )); 
						ovralResult = ovralResult & singleResults[56];
						break;
					}
					case "condition 57":
					{
						conditionDetails = "...then the minimum percent nickel must be greater than 0.5.";
						String nickelMin = getProdValue(jObj, "ChemicalComposition", "Nickel", "Minimum");
						resultValues = getFormatedResultValues("nickelMin", nickelMin);
						singleResults[57] = checkAndReport("Condition 57", conditionDetails, resultValues, 
						(Float.parseFloat(nickelMin)> 0.5f)); 
						ovralResult = ovralResult & singleResults[57];
						break;
					}
					case "condition 58":
					{
						conditionDetails = "...then the maximum percent nickel must be equal to or less than 0.5.";
						String nickelMax = getProdValue(jObj, "ChemicalComposition", "Nickel", "Maximum");
						resultValues = getFormatedResultValues("nickelMax", nickelMax);
						singleResults[58] = checkAndReport("Condition 58", conditionDetails, resultValues, 
						(Float.parseFloat(nickelMax)<= 0.5f)); 
						ovralResult = ovralResult & singleResults[58];
						break;
					}
					case "condition 59":
					{
						conditionDetails = "...then the minimum percent molybdenum must be greater than 1.5 and the "
								+ "maximum percent molybdenum must be less than 5.";
						String molybdenumMin = getProdValue(jObj, "ChemicalComposition", "Molybdenum", "Minimum");
						String molybdenumMax = getProdValue(jObj, "ChemicalComposition", "Molybdenum", "Maximum");
						resultValues = getFormatedResultValues("molybdenumMin", molybdenumMin, "molybdenumMax", molybdenumMax);
						singleResults[59] = checkAndReport("Condition 59", conditionDetails, resultValues, 
						(Float.parseFloat(molybdenumMin)> 1.5f && Float.parseFloat(molybdenumMax) < 5f)); 
						ovralResult = ovralResult & singleResults[59];
						break;
					}
					case "condition 60":
					{
						conditionDetails = "...then the minimum percent molybdenum must be equal to or less than 1.5 "
								+ "or the maximum percent molybdenum must be equal to or greater than 5.";
						String molybdenumMin = getProdValue(jObj, "ChemicalComposition", "Molybdenum", "Minimum");
						String molybdenumMax = getProdValue(jObj, "ChemicalComposition", "Molybdenum", "Maximum");
						resultValues = getFormatedResultValues("molybdenumMin", molybdenumMin, "molybdenumMax", molybdenumMax);
						singleResults[60] = checkAndReport("Condition 60", conditionDetails, resultValues, 
						(Float.parseFloat(molybdenumMin) <= 1.5f || Float.parseFloat(molybdenumMax) >= 5f)); 
					ovralResult = ovralResult & singleResults[60];
					break;
					}
					case "condition 61":
					{
						conditionDetails = "...then the minimum percent nickel must be greater than 0.5 and the "
								+ "maximum percent nickel must be less than 24.";
						String nickelMin = getProdValue(jObj, "ChemicalComposition", "Nickel", "Minimum");
						String nickelMax = getProdValue(jObj, "ChemicalComposition", "Nickel", "Maximum");
						resultValues = getFormatedResultValues("nickelMin", nickelMin, "nickelMax", nickelMax);
						singleResults[61] = checkAndReport("Condition 61", conditionDetails, resultValues, 
						(Float.parseFloat(nickelMin)> 0.5f && Float.parseFloat(nickelMax) < 24f)); 
						ovralResult = ovralResult & singleResults[61];
						break;
					}
					case "condition 62":
					{
						conditionDetails = "...then the minimum percent nickel must be equal to or less"
								+ " than 0.5 or the maximum percent nickel must be equal to or greater than 24.";
						String nickelMin = getProdValue(jObj, "ChemicalComposition", "Nickel", "Minimum");
						String nickelMax = getProdValue(jObj, "ChemicalComposition", "Nickel", "Maximum");
						resultValues = getFormatedResultValues("nickelMin", nickelMin, "nickelMax", nickelMax);
						singleResults[62] = checkAndReport("Condition 62", conditionDetails, resultValues, 
						(Float.parseFloat(nickelMin) <= 0.5f || Float.parseFloat(nickelMax) >= 24f)); 
						ovralResult = ovralResult & singleResults[62];
						break;
					}
					case "condition 63":
					{
						conditionDetails = "...then the maximum percent chromium must be less than 15.";
						String chromiumMax = getProdValue(jObj, "ChemicalComposition", "Chromium", "Maximum");
						resultValues = getFormatedResultValues("chromiumMax", "chromiumMax");
						singleResults[63] = checkAndReport("Condition 63", conditionDetails, resultValues, 
						(Float.parseFloat(chromiumMax)<15f)); 
						ovralResult = ovralResult & singleResults[63];
						break;
					}
					case "condition 64":
					{
						conditionDetails = "...then the maximum percent chromium must be equal to or greater than 15.";
						String chromiumMax = getProdValue(jObj, "ChemicalComposition", "Chromium", "Maximum");
						resultValues = getFormatedResultValues("chromiumMax", chromiumMax);
						singleResults[64] = checkAndReport("Condition 64", conditionDetails, resultValues, 
								(Float.parseFloat(chromiumMax) >= 15f)); 
						ovralResult = ovralResult & singleResults[64];
						break;
					}
					case "condition 65":
					{
						conditionDetails = "...then the minimum thickness must be equal to or greater than 1.65 mm.";
						String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
						resultValues = getFormatedResultValues("thicknessMin", thicknessMin);
						singleResults[65] = checkAndReport("Condition 65", conditionDetails, resultValues,
						(Float.parseFloat(thicknessMin) >= 1.65f ));
						ovralResult = ovralResult & singleResults[65];
						break;
					}
					case "condition 66":
					{
						conditionDetails = "...then the maximum thickness must be less than 4 mm.";
						String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						resultValues = getFormatedResultValues("thicknessMax", thicknessMax);
						singleResults[66] = checkAndReport("Condition 66", conditionDetails, resultValues, 
						(Float.parseFloat(thicknessMax) < 4f));
						ovralResult = ovralResult & singleResults[66]; 
						break;
					}
					case "condition 67":
					{
						conditionDetails = "...then the minimum thickness must be equal to or greater than 4 mm.";
						String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
						resultValues = getFormatedResultValues("thicknessMin", thicknessMin);
						singleResults[67] = checkAndReport( "condition 67", conditionDetails, resultValues,
						(Float.parseFloat(thicknessMin) >= 4f )); 
						ovralResult = ovralResult & singleResults[67];
						break;
					}
					case "condition 68":
					{
						conditionDetails = "...then the maximum carbon must be less than 0.25 percent.";
						String carbonMax = getProdValue(jObj, "ChemicalComposition", "Carbon", "Maximum");
						resultValues = getFormatedResultValues("carbonMax", carbonMax);
						singleResults[68] = checkAndReport("Condition 68", conditionDetails, resultValues,
						(Float.parseFloat(carbonMax) < 0.25f)); 
						ovralResult = ovralResult & singleResults[68];
						break;
					}
					case "condition 69":
					{
						conditionDetails = "...then the minimum carbon must be equal to or greater than 0.25 percent.";
						String carbonMin = getProdValue(jObj, "ChemicalComposition", "Carbon", "Minimum");
						resultValues = getFormatedResultValues("carbonMin", carbonMin);
						singleResults[69] = checkAndReport("Condition 69", conditionDetails, resultValues, 
								(Float.parseFloat(carbonMin) >= 0.25f)); 
						ovralResult = ovralResult & singleResults[69];
						break;
					}
					case "condition 70":
					{
						conditionDetails = "...then the minimum width must be less than two times (2*) the minimum thickness, and the minimum width must"
								+ " be less than 2* the maximum thickness, and the maximum width must be less than 2* the minimum thickness,"
								+ " and the maximum width must be less than 2* the maximum thickness.";
						String widthMin = getProdValue(jObj, "ProductDimensions", "Width", "Minimum");
						String widthMax = getProdValue(jObj, "ProductDimensions", "Width", "Maximum");
						String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
						String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						resultValues = getFormatedResultValues("widthMin", widthMin, "widthMax", widthMax, 
								"thicknessMin", thicknessMin, "thicknessMax", thicknessMax);
						singleResults[70] = checkAndReport("Condition 70", conditionDetails, resultValues, 
								(Float.parseFloat(widthMin) < 2f* Float.parseFloat(thicknessMin) && 
								 Float.parseFloat(widthMin) < 2f* Float.parseFloat(thicknessMax) &&
								 Float.parseFloat(widthMax) < 2f* Float.parseFloat(thicknessMin) && 
								 Float.parseFloat(widthMax) < 2f* Float.parseFloat(thicknessMax)
								));
						ovralResult = ovralResult & singleResults[70];
						break;
					}
					case "condition 71":
					{
						conditionDetails = "...then the minimum width must be less than four times (4*) the minimum thickness and the minimum width "
								+ "must be less than 4* the maximum thickness and the maximum width must be less than 4* the minimum thickness and "
								+ "the maximum width must be less than 4* the maximum thickness.";
						String widthMin = getProdValue(jObj, "ProductDimensions", "Width", "Minimum");
						String widthMax = getProdValue(jObj, "ProductDimensions", "Width", "Maximum");
						String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
						String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						resultValues = getFormatedResultValues("widthMin", widthMin, "widthMax", widthMax, 
								"thicknessMin", thicknessMin, "thicknessMax", thicknessMax);
						singleResults[71] = checkAndReport("Condition 71", conditionDetails, resultValues, 
								(Float.parseFloat(widthMin) < 4f* Float.parseFloat(thicknessMin) && 
								 Float.parseFloat(widthMin) < 4f* Float.parseFloat(thicknessMax) &&
								 Float.parseFloat(widthMax) < 4f* Float.parseFloat(thicknessMin) && 
								 Float.parseFloat(widthMax) < 4f* Float.parseFloat(thicknessMax)
								));
						ovralResult = ovralResult & singleResults[71];
						break;
					}
					case "condition 72":
					{
						conditionDetails = "...then the minimum width must be equal to or greater than four times (4*) the minimum thickness "
								+ "and the minimum width must be equal to or greater than 4* the maximum thickness and the maximum width must "
								+ "be equal to or greater than 4* the minimum thickness and the maximum width must be equal to or greater "
								+ "than 4* the maximum thickness.";
						String widthMin = getProdValue(jObj, "ProductDimensions", "Width", "Minimum");
						String widthMax = getProdValue(jObj, "ProductDimensions", "Width", "Maximum");
						String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
						String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						resultValues = getFormatedResultValues("widthMin", widthMin, "widthMax", widthMax, 
								"thicknessMin", thicknessMin, "thicknessMax", thicknessMax);
						singleResults[72] = checkAndReport("Condition 72", conditionDetails, resultValues, 
								(Float.parseFloat(widthMin) >= 4f* Float.parseFloat(thicknessMin) && 
								 Float.parseFloat(widthMin) >= 4f* Float.parseFloat(thicknessMax) &&
								 Float.parseFloat(widthMax) >= 4f* Float.parseFloat(thicknessMin) && 
								 Float.parseFloat(widthMax) >= 4f* Float.parseFloat(thicknessMax)
								));
						ovralResult = ovralResult & singleResults[72]; 
						break;
					}
					case "condition 73": //NOT IN USE ANYMORE
					{
						conditionDetails = "...then the minimum thickness must be equal to or greater than 4.75 mm.";
						String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
						resultValues = getFormatedResultValues("thicknessMin", thicknessMin);
						singleResults[73] = checkAndReport("Condition 73", conditionDetails, resultValues, 
						(Float.parseFloat(thicknessMin) >= 4.75f));
						ovralResult = ovralResult & singleResults[73];
						break;
					}
					case "condition 74":
					{
						conditionDetails = "...then the maximum thickness must be less than 4.75 mm.";
						String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						resultValues = getFormatedResultValues("thicknessMax", thicknessMax);
						singleResults[74] = checkAndReport("Condition 74", conditionDetails, resultValues, 
						(Float.parseFloat(thicknessMax) < 4.75f));
						ovralResult = ovralResult & singleResults[74]; 
						break;
					}
					case "condition 75":
					{
						conditionDetails = "...then the minimum thickness must be equal to or greater than 3 mm and the maximum "
								+ "thickness must be less than 4.75 mm.";
						String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
						String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						resultValues = getFormatedResultValues("thicknessMin", thicknessMin, "thicknessMax", thicknessMax);
						singleResults[75] = checkAndReport("Condition 75", conditionDetails, resultValues, 
								(Float.parseFloat(thicknessMin) >= 3f && Float.parseFloat(thicknessMax)<4.75f));
						ovralResult = ovralResult & singleResults[75];
						break;
					}
					case "condition 76":
					{
						conditionDetails = "...then the maximum thickness must be less than 3 mm.";
						String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						resultValues = getFormatedResultValues("thicknessMax", thicknessMax);
						singleResults[76] = checkAndReport("Condition 76", conditionDetails, resultValues, 
								(Float.parseFloat(thicknessMax)<3f));
						ovralResult = ovralResult & singleResults[76];
						break;
					}
					case "condition 77":
					{
						conditionDetails = "...then the minimum thickness must be greater than 10 mm.";
						String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
						resultValues = getFormatedResultValues("thicknessMin", thicknessMin);
						singleResults[77] = checkAndReport("Condition 77", conditionDetails, resultValues, 
								(Float.parseFloat(thicknessMin)>10f));
						ovralResult = ovralResult & singleResults[77];
						break;
					}
					case "condition 78":
					{
						//conditionDetails = "...then the minimum thickness must be equal to or greater "
						//		+ "than 4.75 mm and the maximum thickness must be less than 10 mm.";
						conditionDetails = "...then the minimum thickness must be equal to or greater than 4.75 "
								+ "mm and the maximum thickness must be less or equal than 10 mm.";
						
						String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
						String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						resultValues = getFormatedResultValues("thicknessMin", thicknessMin, "thicknessMax", thicknessMax);
						singleResults[78] = checkAndReport("Condition 78", conditionDetails, resultValues, 
								(Float.parseFloat(thicknessMin) >= 4.75f && Float.parseFloat(thicknessMax)<=10f));
						ovralResult = ovralResult & singleResults[78]; 
						break;
					}
					case "condition 79":
					{
						conditionDetails = "...then the minimum thickness must be greater than 1 mm "
								+ "and the maximum thickness must be less than 3 mm.";
						String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
						String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						resultValues = getFormatedResultValues("thicknessMin", thicknessMin, "thicknessMax", thicknessMax);
						singleResults[79] = checkAndReport("Condition 79", conditionDetails, resultValues, 
								(Float.parseFloat(thicknessMin) > 1f && Float.parseFloat(thicknessMax)<3f));
						ovralResult = ovralResult & singleResults[79]; 
						break;
					}
					case "condition 80":
					{
						/*conditionDetails = "...then the minimum thickness must be equal to or greater than 0.5 "
								+ "mm and the maximum thickness must be less than 1 mm.";*/
						conditionDetails = "…then the minimum thickness must be equal to or greater than 0.5 mm "
								+ "and the maximum thickness must be equal to or less than 1 mm";
						String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
						String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						resultValues = getFormatedResultValues("thicknessMin", thicknessMin, "thicknessMax", thicknessMax);
						singleResults[80] = checkAndReport("Condition 80", conditionDetails, resultValues, 
								(Float.parseFloat(thicknessMin) >= 0.5f && Float.parseFloat(thicknessMax)<=1f));
						ovralResult = ovralResult & singleResults[80];
						break;
					}
					case "condition 81":
					{
						conditionDetails = "...then the maximum thickness must be less than 0.361 mm.";
						String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						resultValues = getFormatedResultValues("thicknessMax", thicknessMax);
						singleResults[81] = checkAndReport("Condition 81", conditionDetails, resultValues, 
								(Float.parseFloat(thicknessMax)<0.361f));
						ovralResult = ovralResult & singleResults[81];
						break;
					}
					case "condition 82":
					{
						conditionDetails = "...then the maximum thickness must be equal to or greater than 3 mm.";
						String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						resultValues = getFormatedResultValues("thicknessMax", thicknessMax);
						singleResults[82] = checkAndReport("Condition 82", conditionDetails, resultValues, 
								(Float.parseFloat(thicknessMax)>=3f));
						ovralResult = ovralResult & singleResults[82];
						break;
					}
					case "condition 83":
					{
						conditionDetails = "...then the maximum thickness must be less than 0.5 mm.";
						String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						resultValues = getFormatedResultValues("thicknessMax", thicknessMax);
						singleResults[83] = checkAndReport("Condition 83", conditionDetails, resultValues, 
								(Float.parseFloat(thicknessMax)<0.5f));
						ovralResult = ovralResult & singleResults[83];
						break;
					}
					case "condition 84":
					{
						conditionDetails = "...then the minimum thickness must be equal to or greater than 0.5 mm.";
						String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
						resultValues = getFormatedResultValues("thicknessMin", thicknessMin);
						singleResults[84] = checkAndReport("Condition 84", conditionDetails, resultValues, 
								(Float.parseFloat(thicknessMin)>=0.5f));
						ovralResult = ovralResult & singleResults[84];
						break;
					}
					case "condition 85":
					{
						conditionDetails = "...then the minimum thickness must be equal to or greater than 0.4 mm.";
						String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
						resultValues = getFormatedResultValues("thicknessMin", thicknessMin);
						singleResults[85] = checkAndReport("Condition 85", conditionDetails, resultValues, 
								(Float.parseFloat(thicknessMin)>=0.4f));
						ovralResult = ovralResult & singleResults[85]; 
						break;
					}
					case "condition 86":
					{
						conditionDetails = "...then the minimum thickness must be equal to or greater than 4.75 mm.";
						String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
						resultValues = getFormatedResultValues("thicknessMin", thicknessMin);
						singleResults[86] = checkAndReport("Condition 86", conditionDetails, resultValues, 
								(Float.parseFloat(thicknessMin)>=4.75f));
						ovralResult = ovralResult & singleResults[86];
						break;
					}
					case "condition 87":
					{
						conditionDetails = "...then the maximum width must be less than 300 mm.";
						String widthMax = getProdValue(jObj, "ProductDimensions", "Width", "Maximum");
						resultValues = getFormatedResultValues("widthMax", widthMax);
						singleResults[87] = checkAndReport("Condition 87", conditionDetails, resultValues, 
								(Float.parseFloat(widthMax)<300f));
						ovralResult = ovralResult & singleResults[87];
						break;
					}
					case "condition 88":
					{
						conditionDetails = "...then the minimum thickness must be greater than 1.25 mm.";
						String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
						resultValues = getFormatedResultValues("thicknessMin", thicknessMin);
						singleResults[88] = checkAndReport("Condition 88", conditionDetails, resultValues, 
								(Float.parseFloat(thicknessMin) > 1.25f));
						ovralResult = ovralResult & singleResults[88];
						break;
					}
					case "condition 89":
					{
						conditionDetails = "...then the minimum thickness must be greater than"
								+ " 0.25 mm and the maximum thickness must be less than or equal to 1.25 mm.";
						String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
						String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						resultValues = getFormatedResultValues("thicknessMin", thicknessMin, "thicknessMax", thicknessMax);
						singleResults[89] = checkAndReport("Condition 89", conditionDetails, resultValues, 
								(Float.parseFloat(thicknessMin) > 0.25f && Float.parseFloat(thicknessMax)<=1.25f));
						ovralResult = ovralResult & singleResults[89];
						break;
					}
					case "condition 90":
					{
						conditionDetails = "...then the maximum thickness must be equal to or less than 0.25 mm.";
						String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						resultValues = getFormatedResultValues("thicknessMax", thicknessMax);
						singleResults[90] = checkAndReport("Condition 90", conditionDetails, resultValues, 
								(Float.parseFloat(thicknessMax)<=0.25f));
						ovralResult = ovralResult & singleResults[90]; 
						break;
					}
					case "condition 91":
					{
						conditionDetails = "...then the maximum width must be less than 51 mm.";
						String widthMax = getProdValue(jObj, "ProductDimensions", "Width", "Maximum");
						resultValues = getFormatedResultValues("widthMax", widthMax);
						singleResults[91] = checkAndReport("Condition 91", conditionDetails, resultValues, 
								(Float.parseFloat(widthMax)<51f));
					ovralResult = ovralResult & singleResults[91]; 
					break;
					}
					case "condition 92":
					{
						conditionDetails = "...then the minimum percentage of lead must be equal to or greater than 0.1 percent.";
						String leadMin = getProdValue(jObj, "ChemicalComposition", "Lead", "Minimum");
						resultValues = getFormatedResultValues("leadMin", leadMin);
						singleResults[92] = checkAndReport("Condition 92", conditionDetails, resultValues, 
								(Float.parseFloat(leadMin)>=0.1f));
						ovralResult = ovralResult & singleResults[92];
						break; 
					}
					case "condition 93":
					{
						conditionDetails = "...then the minimum percentage of carbon is equal to or greater than 0.6 percent.";
						String carbonMin = getProdValue(jObj, "ChemicalComposition", "Carbon", "Minimum");
						resultValues = getFormatedResultValues("carbonMin", carbonMin);
						singleResults[93] = checkAndReport("Condition 93", conditionDetails, resultValues, 
								(Float.parseFloat(carbonMin)>=0.6f));
			
						ovralResult = ovralResult & singleResults[93];
						break;
					}
					case "condition 94":
					{
						conditionDetails = "...then the minimum percentage of carbon is greater than or equal to 0.25 percent "
								+ "of carbon and the maximum percentage of carbon is less than 0.6 percent.";
						String carbonMin = getProdValue(jObj, "ChemicalComposition", "Carbon", "Minimum");
						String carbonMax = getProdValue(jObj, "ChemicalComposition", "Carbon", "Maximum");
						resultValues = getFormatedResultValues("carbonMin", carbonMin, "carbonMax", carbonMax);
						singleResults[94] = checkAndReport("Condition 94", conditionDetails, resultValues,
								( Float.parseFloat(carbonMin)>=0.25f && Float.parseFloat(carbonMax)<0.6f));
					
						ovralResult = ovralResult & singleResults[94];
						break;
					}
					case "condition 95":
					{
						conditionDetails = "...then the maximum percentage of carbon is less than 0.6 percent.";
						String carbonMax = getProdValue(jObj, "ChemicalComposition", "Carbon", "Maximum");
						resultValues = getFormatedResultValues("carbonMax", carbonMax);
						singleResults[95] = checkAndReport("Condition 95", conditionDetails, resultValues,
								(Float.parseFloat(carbonMax)<0.6f));
						ovralResult = ovralResult & singleResults[95];
						break;
					}
					case "condition 96":
					{
						conditionDetails = "...then the maximum outside diameter must be less than 76 mm.";
						String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
						resultValues = getFormatedResultValues("outsideDiameterMax", outsideDiameterMax);
						singleResults[96] = checkAndReport("Condition 96", conditionDetails, resultValues, 
								(Float.parseFloat(outsideDiameterMax) < 76f)); 
						ovralResult = ovralResult & singleResults[96];
						break;
					}
					case "condition 97":
					{
						conditionDetails = "...then the minimum outside diameter must be equal to or greater than 76 mm and "
								+ "less than 228 mm, and the maximum outside diameter must be equal to or greater than "
								+ "76 mm and less than 228 mm.";
						String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
						String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
						resultValues = getFormatedResultValues("outsideDiameterMin", outsideDiameterMin, "outsideDiameterMax", outsideDiameterMax);
						singleResults[97] = checkAndReport("Condition 97", conditionDetails, resultValues, 
								(Float.parseFloat(outsideDiameterMin) >= 76f && Float.parseFloat(outsideDiameterMin) < 228f
								&&Float.parseFloat(outsideDiameterMax) >= 76f && Float.parseFloat(outsideDiameterMax) < 228f)); 
						ovralResult = ovralResult & singleResults[97]; 
						break;
					}
					case "condition 98":
					{
						conditionDetails = "...then the minimum outside diameter must be greater than 228 mm.";
						String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
						resultValues = getFormatedResultValues("outsideDiameterMin", outsideDiameterMin);
						singleResults[98] = checkAndReport("Condition 98", conditionDetails, resultValues, 
								(Float.parseFloat(outsideDiameterMin) > 228f)); 
						ovralResult = ovralResult & singleResults[98];
						break;
					}
					case "condition 99":
					{
						conditionDetails = "...then the maximum height must be less than 80 mm.";
						String heightMax = getProdValue(jObj, "ProductDimensions", "Height", "Maximum");
						resultValues = getFormatedResultValues("heightMax", heightMax);
						singleResults[99] = checkAndReport("Condition 99", conditionDetails, resultValues, 
								(Float.parseFloat(heightMax) < 80f));
					ovralResult = ovralResult & singleResults[99];
					break;
					}
					case "condition 100":
					{
						conditionDetails = "...then the minimum height must be equal to or greater than 80 mm.";
						String heightMin = getProdValue(jObj, "ProductDimensions", "Height", "Minimum");
						resultValues = getFormatedResultValues("heightMin", heightMin);
						singleResults[100] = checkAndReport("Condition 100", conditionDetails, resultValues, 
								(Float.parseFloat(heightMin) >= 80f));
						ovralResult = ovralResult & singleResults[100];
						break;
					}
					case "condition 101":
					{
						conditionDetails = "...then the maximum outside diameter must be less than 1.5 mm.";
						String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
						resultValues = getFormatedResultValues("outsideDiameterMax", outsideDiameterMax);
						singleResults[101] = checkAndReport("Condition 101", conditionDetails, resultValues, 
								(Float.parseFloat(outsideDiameterMax) < 1.5f)); 
						ovralResult = ovralResult & singleResults[101];
						break;
					}
					case "condition 102":
					{
						conditionDetails = "...then the minimum outside diameter must be equal to or greater than 1.5 mm.";
						String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
						resultValues = getFormatedResultValues("outsideDiameterMin", outsideDiameterMin);
						singleResults[102] = checkAndReport("Condition 102", conditionDetails, resultValues, 
								(Float.parseFloat(outsideDiameterMin) >= 1.5f)); 
						
						ovralResult = ovralResult & singleResults[102]; 
						break;
					}
					case "condition 103":
					{
						conditionDetails = "...then the maximum width must be equal to or less than 1575 mm.";
						String widthMax = getProdValue(jObj, "ProductDimensions", "Width", "Maximum");
						resultValues = getFormatedResultValues("widthMax", widthMax);
						singleResults[103] = checkAndReport("Condition 103", conditionDetails, resultValues, 
								(Float.parseFloat(widthMax)<=1575f));
						ovralResult = ovralResult & singleResults[103]; 
						break;
					}
					case "condition 104":{
						conditionDetails = "...then the minimum width must be greater than 1575 mm.";
						String widthMin = getProdValue(jObj, "ProductDimensions", "Width", "Minimum");
						resultValues = getFormatedResultValues("widthMin", widthMin);
						singleResults[104] = checkAndReport("Condition 104", conditionDetails, resultValues, 
								(Float.parseFloat(widthMin)>1575f));
			
						ovralResult = ovralResult & singleResults[104];
						break;
					}
					case "condition 105":{
						conditionDetails = "...then the minimum width must be equal to or greater than 1370 mm.";
						String widthMin = getProdValue(jObj, "ProductDimensions", "Width", "Minimum");
						resultValues = getFormatedResultValues("widthMin", widthMin);
						singleResults[105] = checkAndReport("Condition 105", conditionDetails, resultValues, 
								(Float.parseFloat(widthMin)>=1370f));
						ovralResult = ovralResult & singleResults[105];
						break;
					}
					case "condition 106":{
						conditionDetails = "...then the minimum thickness must be greater than 6.8 mm.";
						String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
						resultValues = getFormatedResultValues("widthMin", thicknessMin);
						singleResults[106] = checkAndReport("Condition 106", conditionDetails, resultValues, 
								(Float.parseFloat(thicknessMin) > 6.8f));
						ovralResult = ovralResult & singleResults[106];
						break;
					}
					case "condition 107":{
						conditionDetails = "...then the minimum percentage of nickel must be greater than 0.5 percent.";
						String nickelMin = getProdValue(jObj, "ChemicalComposition", "Nickel", "Minimum");
						//String nickelMax = getProdValue(jObj, "ChemicalComposition", "Nickel", "Maximum");
						resultValues = getFormatedResultValues("nickelMin", nickelMin);
						singleResults[107] = checkAndReport("Condition 107", conditionDetails, resultValues, 
								(Float.parseFloat(nickelMin) > 0.5f )); 
						ovralResult = ovralResult & singleResults[107];
						break;
					}
					case "condition 108":{
						conditionDetails = "...then the minimum percentage of molybdenum must be greater than 1.5 percent "
								+ "and the maximum percentage of molybdenum must be less than 5 percent.";
						String molybdenumMin = getProdValue(jObj, "ChemicalComposition", "Molybdenum", "Minimum");
						String molybdenumMax = getProdValue(jObj, "ChemicalComposition", "Molybdenum", "Maximum");
						resultValues = getFormatedResultValues("molybdenumMin", molybdenumMin, "molybdenumMax", molybdenumMax);
						singleResults[108] = checkAndReport("Condition 108", conditionDetails, resultValues, 
								(Float.parseFloat(molybdenumMin) > 1.5f && Float.parseFloat(molybdenumMax) < 5f)); 
						ovralResult = ovralResult & singleResults[108];
						break;
					}
					case "condition 109":{
						conditionDetails = "...then the minimum percentage of molybdenum must equal to or less than 1.5 percent or "
								+ "the maximum percentage of molybdenum must be equal to or greater than 5 percent.";
						String molybdenumMin = getProdValue(jObj, "ChemicalComposition", "Molybdenum", "Minimum");
						String molybdenumMax = getProdValue(jObj, "ChemicalComposition", "Molybdenum", "Maximum");
						resultValues = getFormatedResultValues("molybdenumMin", molybdenumMin, "molybdenumMax", molybdenumMax);
						singleResults[109] = checkAndReport("Condition 109", conditionDetails, resultValues, 
								(Float.parseFloat(molybdenumMin) <= 1.5f || Float.parseFloat(molybdenumMax) >= 5f));
						ovralResult = ovralResult & singleResults[109];
						break;
					}
					case "condition 110":{
						conditionDetails = "...then the maximum thickness must be less than 4.75 mm and the minimum thickness must "
								+ "be equal to or greater than 3 mm.";
						String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
						String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						resultValues = getFormatedResultValues("thicknessMin", thicknessMin, "thicknessMax", thicknessMax);
						singleResults[110] = checkAndReport("Condition 110", conditionDetails, resultValues, 
								(Float.parseFloat(thicknessMax) < 4.75f && Float.parseFloat(thicknessMin) >= 3f ));
						ovralResult = ovralResult & singleResults[110]; 
						break;
					}
					case "condition 111":{
						conditionDetails = "...then the minimum percentage of nickel must be greater than 0.5 percent and the maximum "
								+ "percentage of nickel must be less than 24 percent.";
						String nickelMin = getProdValue(jObj, "ChemicalComposition", "Nickel", "Minimum");
						String nickelMax = getProdValue(jObj, "ChemicalComposition", "Nickel", "Maximum");
						resultValues = getFormatedResultValues("nickelMin", nickelMin, "nickelMax", nickelMax);
						singleResults[111] = checkAndReport("Condition 111", conditionDetails, resultValues, 
								(Float.parseFloat(nickelMin) > 0.5f  && Float.parseFloat(nickelMax) < 24f)); 
						ovralResult = ovralResult & singleResults[111]; 
						break;
					}
					case "condition 112":{
						conditionDetails = "...then the minimum width must be greater than 1575 mm, and the maximum width must "
								+ "equal to or less than 1880 mm.";
						String widthMin = getProdValue(jObj, "ProductDimensions", "Width", "Minimum");
						String widthMax = getProdValue(jObj, "ProductDimensions", "Width", "Maximum");
						resultValues = getFormatedResultValues("widthMin", widthMin, "widthMax", widthMax);
						singleResults[112] = checkAndReport("Condition 112", conditionDetails, resultValues, 
								(Float.parseFloat(widthMin)> 1575f && Float.parseFloat(widthMax) <= 1880f));
						ovralResult = ovralResult & singleResults[112]; 
						break;
					}
					case "condition 113":{
						conditionDetails = "...then the minimum width must be greater than 1880 mm.";
						String widthMin = getProdValue(jObj, "ProductDimensions", "Width", "Minimum");
						resultValues = getFormatedResultValues("widthMin", widthMin);
						singleResults[113] = checkAndReport("Condition 113", conditionDetails, resultValues, 
								(Float.parseFloat(widthMin)> 1880f));
						ovralResult = ovralResult & singleResults[113];
						break;
					}
					case "condition 114":{
						conditionDetails = "...then the maximum percentage of chromium must be less than 15 percent.";
						String chromiumMax = getProdValue(jObj, "ChemicalComposition", "Chromium", "Maximum");
						resultValues = getFormatedResultValues("chromiumMax", chromiumMax);
						singleResults[114] = checkAndReport("Condition 114", conditionDetails, resultValues, 
								(Float.parseFloat(chromiumMax) < 15f)); 
						ovralResult = ovralResult & singleResults[114];
						break;
					}
					case "condition 115":{
						conditionDetails = "...then the minimum width must be equal to or greater than 300 mm.";
						String widthMin = getProdValue(jObj, "ProductDimensions", "Width", "Minimum");
						resultValues = getFormatedResultValues("widthMin", widthMin);
						singleResults[115] = checkAndReport("Condition 115", conditionDetails, resultValues, 
								(Float.parseFloat(widthMin)>= 300f));
						ovralResult = ovralResult & singleResults[115];
						break;
					}
					case "condition 116":{
						conditionDetails = "...then the maximum outside diameter must be less than 14 mm.";
						String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
						resultValues = getFormatedResultValues("outsideDiameterMax", outsideDiameterMax);
						singleResults[116] = checkAndReport("Condition 116", conditionDetails, resultValues, 
								(Float.parseFloat(outsideDiameterMax) < 14f)); 
						ovralResult = ovralResult & singleResults[116];
						break;
					}
					case "condition 117":{
						conditionDetails = "...then the maximum percentage of nickel must be less than 8 percent.";
						//String nickelMin = getProdValue(jObj, "ChemicalComposition", "Nickel", "Minimum");
						String nickelMax = getProdValue(jObj, "ChemicalComposition", "Nickel", "Maximum");
						resultValues = getFormatedResultValues("nickelMax", nickelMax);
						singleResults[117] = checkAndReport("Condition 117", conditionDetails, resultValues, 
								(Float.parseFloat(nickelMax) < 8f ));
						ovralResult = ovralResult & singleResults[117]; 
						break;
					}
					case "condition 118":{
						conditionDetails = "...then the minimum percentage of nickel must be equal to or greater than 8 percent, "
								+ "and the maximum percentage of nickel must be less than 24 percent.";
						String nickelMin = getProdValue(jObj, "ChemicalComposition", "Nickel", "Minimum");
						String nickelMax = getProdValue(jObj, "ChemicalComposition", "Nickel", "Maximum");
						resultValues = getFormatedResultValues("nickelMin", nickelMin, "nickelMax", nickelMax);
						singleResults[118] = checkAndReport("Condition 118", conditionDetails, resultValues, 
								(Float.parseFloat(nickelMin) >= 8f && Float.parseFloat(nickelMax) < 24f ));
						ovralResult = ovralResult & singleResults[118]; 
						break;
					}
					case "condition 119":{
						conditionDetails = "...then the minimum outside diameter must be equal to or greater than 14 mm and "
								+ "less than 19 mm, and the maximum outside diameter must be equal to or greater than 14 mm and "
								+ "less than 19 mm.";
						String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
						String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
						resultValues = getFormatedResultValues("outsideDiameterMin", outsideDiameterMin, "outsideDiameterMax", outsideDiameterMax);
						singleResults[119] = checkAndReport("Condition 119", conditionDetails, resultValues, 
								(Float.parseFloat(outsideDiameterMin) >= 14f && Float.parseFloat(outsideDiameterMin) < 19f
								&& Float.parseFloat(outsideDiameterMax) >= 14f && Float.parseFloat(outsideDiameterMax) < 19f));
						ovralResult = ovralResult & singleResults[119];
						break;
					}
					case "condition 120":{
						conditionDetails = "...then the maximum outside diameter must be equal to or greater than 19 mm.";
						String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
						resultValues = getFormatedResultValues("outsideDiameterMax", outsideDiameterMax);
						singleResults[120] = checkAndReport("Condition 120", conditionDetails, resultValues, 
								(Float.parseFloat(outsideDiameterMax) >= 19f));
						ovralResult = ovralResult & singleResults[102];
						break;
					}
					case "condition 121":{
						conditionDetails = "...then the maximum outside diameter must be less than 0.25 mm.";
						String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
						resultValues = getFormatedResultValues("outsideDiameterMax", outsideDiameterMax);
						singleResults[121] = checkAndReport("Condition 121", conditionDetails, resultValues, 
								(Float.parseFloat(outsideDiameterMax) < 0.25f));
						ovralResult = ovralResult & singleResults[121]; 
						break;
					}
					case "condition 122":{
						conditionDetails = "...then the minimum outside diameter must be equal to or greater than 0.25 mm and less "
								+ "than 0.76 mm, and the maximum outside diameter must be equal to or greater than 0.25 mm and less than 0.76 mm.";
						String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
						String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
						resultValues = getFormatedResultValues("outsideDiameterMin", outsideDiameterMin, "outsideDiameterMax", outsideDiameterMax);
						singleResults[122] = checkAndReport("Condition 122", conditionDetails, resultValues, 
								(Float.parseFloat(outsideDiameterMin) >= 0.25f && Float.parseFloat(outsideDiameterMin) < 0.76f
								&& Float.parseFloat(outsideDiameterMax) >= 0.25f && Float.parseFloat(outsideDiameterMax) < 0.76f));
						ovralResult = ovralResult & singleResults[122];
						break;
					}
					case "condition 123":{
						conditionDetails = "...then the minimum outside diameter must be equal to or greater than 0.76 mm and less than "
								+ "1.52 mm, and the maximum outside diameter must be equal to or greater than 0.76 mm and less than 1.52 mm.";
						String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
						String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
						resultValues = getFormatedResultValues("outsideDiameterMin", outsideDiameterMin, "outsideDiameterMax", outsideDiameterMax);
						singleResults[123] = checkAndReport("Condition 123", conditionDetails, resultValues, 
								(Float.parseFloat(outsideDiameterMin) >= 0.76f && Float.parseFloat(outsideDiameterMin) < 1.52f
								&& Float.parseFloat(outsideDiameterMax) >= 0.76f && Float.parseFloat(outsideDiameterMax) < 1.52f));
						ovralResult = ovralResult & singleResults[123]; 
						break;
					}
					case "condition 124":{
						conditionDetails = "...then the minimum outside diameter must be equal to or greater than 1.52 mm and less "
								+ "than 5.1 mm, and the maximum outside diameter must be equal to or greater than 1.52 mm and less than 5.1 mm.";
						String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
						String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
						resultValues = getFormatedResultValues("outsideDiameterMin", outsideDiameterMin, "outsideDiameterMax", outsideDiameterMax);
						singleResults[124] = checkAndReport("Condition 124", conditionDetails, resultValues, 
								(Float.parseFloat(outsideDiameterMin) >= 1.52f && Float.parseFloat(outsideDiameterMin) < 5.1f
								&& Float.parseFloat(outsideDiameterMax) >= 1.52f && Float.parseFloat(outsideDiameterMax) < 5.1f));
						ovralResult = ovralResult & singleResults[124]; 
						break;
					}
					case "condition 125":{
						conditionDetails = "...then the minimum outside diameter must be equal to or greater than 5.1 mm.";
						String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
						resultValues = getFormatedResultValues("outsideDiameterMin", outsideDiameterMin);
						singleResults[125] = checkAndReport("Condition 125", conditionDetails, resultValues, 
								(Float.parseFloat(outsideDiameterMin) >= 5.1f));
						ovralResult = ovralResult & singleResults[125];
						break;
					}
					case "condition 126":{
						conditionDetails = "...then the maximum thickness must be less than 0.25 mm.";
						String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						resultValues = getFormatedResultValues("thicknessMax", thicknessMax);
						singleResults[126] = checkAndReport("Condition 126", conditionDetails, resultValues, 
								(Float.parseFloat(thicknessMax) < 0.25f));
						ovralResult = ovralResult & singleResults[126]; 
						break;
					}
					case "condition 127":{
						conditionDetails = "...then the minimum outside diameter must be equal to or greater than 76 mm and less than 152 mm, "
								+ "and the maximum outside diameter must be equal to or greater than 76 mm and less than 152 mm.";
						String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
						String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
						resultValues = getFormatedResultValues("outsideDiameterMin", outsideDiameterMin, "outsideDiameterMax", outsideDiameterMax);
						singleResults[127] = checkAndReport("Condition 127", conditionDetails, resultValues, 
								(Float.parseFloat(outsideDiameterMin) >= 76f && Float.parseFloat(outsideDiameterMin) < 152f
								&& Float.parseFloat(outsideDiameterMax) >= 76f && Float.parseFloat(outsideDiameterMax) < 152f));
						ovralResult = ovralResult & singleResults[127];
						break;
					}
					case "condition 128":{
						conditionDetails = "...then the minimum outside diameter must be equal to or greater than 152 mm and less than 228 mm,"
								+ " and the maximum outside diameter must be equal to or greater than 152 mm and less than 228 mm.";
						String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
						String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
						resultValues = getFormatedResultValues("outsideDiameterMin", outsideDiameterMin, "outsideDiameterMax", outsideDiameterMax);
						singleResults[128] = checkAndReport("Condition 128", conditionDetails, resultValues, 
								(Float.parseFloat(outsideDiameterMin) >= 152f && Float.parseFloat(outsideDiameterMin) < 228f
								&& Float.parseFloat(outsideDiameterMax) >= 152f && Float.parseFloat(outsideDiameterMax) < 228f));
						ovralResult = ovralResult & singleResults[128];
						break;
					}
					case "condition 129":{
						conditionDetails = "Flat-rolled straight at least 4.75 mm thickness ...then the minimum width must be greater than 150mm,"
								+ " and the minimum width must be greater than or equal to 2* the maximum thickness.";
						String widthMin = getProdValue(jObj, "ProductDimensions", "Width", "Minimum");
						String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						resultValues = getFormatedResultValues("widthMin", widthMin, "thicknessMax", thicknessMax);
						singleResults[129] = checkAndReport("Condition 129", conditionDetails, resultValues, 
								(Float.parseFloat(widthMin) > 150f && Float.parseFloat(widthMin) >= 2f * Float.parseFloat(thicknessMax)
								));
						ovralResult = ovralResult & singleResults[129];
						break;
					}
					case "condition 130":{
						conditionDetails = "Hot-rolled ...then the cold rolled cell must say 'No' or be blank.";
						String coldRolled = getProdValue(jObj, "ProductClassification", "Cold Rolled", "");
						resultValues = getFormatedResultValues("coldRolled", coldRolled);
						singleResults[130] = checkAndReport("Condition 130", conditionDetails, resultValues, 
								(coldRolled.equalsIgnoreCase("false") || coldRolled.equalsIgnoreCase("No") 
										|| coldRolled.equalsIgnoreCase("")));
						//
						ovralResult = ovralResult & singleResults[130]; 
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
						resultValues = getFormatedResultValues("outsideDiameterMax", outsideDiameterMax, "carbonMax", carbonMax,
								"nickelMin", nickelMin, "molybdenumMin", molybdenumMin);
						singleResults[131] = checkAndReport("Condition 131", conditionDetails, resultValues, 
								(Float.parseFloat(outsideDiameterMax) <= 1.6f && Float.parseFloat(carbonMax) < 0.20f
								&& Float.parseFloat(nickelMin)> 0.3f && Float.parseFloat(molybdenumMin)>0.08f));
						ovralResult = ovralResult & singleResults[131];
						break;
					}
					case "condition 132":{
						conditionDetails = "...then the maximum outside diameter must be equal to or less than 1.6 mm, and the maximum percentage "
								+ "of carbon must be less than 0.20 percent, and the minimum percentage of manganese must be greater than 0.9 percent, "
								+ "and the minimum percentage of silicon must be greater than 0.6 percent.";
						String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
						String carbonMax = getProdValue(jObj, "ChemicalComposition", "Carbon", "Maximum");
						String manganeseMin =  getProdValue(jObj, "ChemicalComposition", "Manganese", "Minimum");
						String siliconMin = getProdValue(jObj, "ChemicalComposition", "Silicon", "Minimum");
						resultValues = getFormatedResultValues("outsideDiameterMax", outsideDiameterMax, "carbonMax", carbonMax,
								"manganeseMin", manganeseMin, "siliconMin", siliconMin);
						singleResults[132] = checkAndReport("Condition 132", conditionDetails, resultValues, 
								(Float.parseFloat(outsideDiameterMax) <= 1.6f && Float.parseFloat(carbonMax) < 0.20f 
								&& Float.parseFloat(manganeseMin)> 0.9f && Float.parseFloat(siliconMin)>0.6f));
						ovralResult = ovralResult & singleResults[132];
						break;
					}
					case "condition 133":{
						conditionDetails = "...then the maximum outside diameter must be less than 1.0 mm.";
						String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
						resultValues = getFormatedResultValues("outsideDiameterMax", outsideDiameterMax);
						singleResults[133] = checkAndReport("Condition 133", conditionDetails, resultValues, 
								(Float.parseFloat(outsideDiameterMax) < 1.0f));
						ovralResult = ovralResult & singleResults[133];
						break;
					}
					case "condition 134":{
						conditionDetails = "...then the minimum outside diameter must be equal to or greater than 1.0 mm and less "
								+ "than 1.5 mm, and the maximum outside diameter must be equal to or greater than 1.0 mm and less than 1.5 mm.";
						String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
						String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
						resultValues = getFormatedResultValues("outsideDiameterMin", outsideDiameterMin, "outsideDiameterMax", outsideDiameterMax);
						singleResults[134] = checkAndReport("Condition 134", conditionDetails, resultValues, 
								(Float.parseFloat(outsideDiameterMin) >= 1f && Float.parseFloat(outsideDiameterMin) < 1.5f
								&& Float.parseFloat(outsideDiameterMax) >= 1f && Float.parseFloat(outsideDiameterMax) < 1.5f));
						ovralResult = ovralResult & singleResults[134]; 
						break;
					}
					case "condition 135":{
						conditionDetails = "...then the minimum thickness must be equal to or greater than 0.361 mm, and the maximum thickness "
								+ "must be less than 0.5 mm.";
						String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
						String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						resultValues = getFormatedResultValues("thicknessMin", thicknessMin, "thicknessMin", thicknessMin);
						singleResults[135] = checkAndReport("Condition 135", conditionDetails, resultValues, 
								(Float.parseFloat(thicknessMin) >= 0.361f && Float.parseFloat(thicknessMax)< 0.5f));
						ovralResult = ovralResult & singleResults[135];
						break;
					}
					case "condition 136":{
						conditionDetails = "...then the minimum thickness must be less than 0.361 mm.";
						String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
						resultValues = getFormatedResultValues("thicknessMin", thicknessMin);
						singleResults[136] = checkAndReport("Condition 136", conditionDetails, resultValues, 
								(Float.parseFloat(thicknessMin) < 0.361f ));
						ovralResult = ovralResult & singleResults[136]; 
						break;
					}
					case "condition 137":{
						conditionDetails = "...then the maximum outside diameter must be less than 19 mm.";
						String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
						resultValues = getFormatedResultValues("outsideDiameterMax", outsideDiameterMax);
						singleResults[137] = checkAndReport("Condition 137", conditionDetails, resultValues, 
								(Float.parseFloat(outsideDiameterMax) < 19f));
						ovralResult = ovralResult & singleResults[137];
						break;
					}
					case "condition 138":{
						conditionDetails = "...then the maximum outside diameter must be equal to or greater than 19 mm.";
						String outsideDiameterMax = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Maximum");
						resultValues = getFormatedResultValues("outsideDiameterMax", outsideDiameterMax);
						singleResults[138] = checkAndReport("Condition 138", conditionDetails, resultValues, 
								(Float.parseFloat(outsideDiameterMax) >= 19f));
						ovralResult = ovralResult & singleResults[138];
						break;
					}
					case "condition 139":{
						conditionDetails = "Stainless ...then the maximum percentage of carbon must be 1.2 percent or less and the "
								+ "minimum percentage of chromium must be 10.5 percent or more.";
						String carbonMax = getProdValue(jObj, "ChemicalComposition", "Carbon", "Maximum");
						String chromiumMin = getProdValue(jObj, "ChemicalComposition", "Chromium", "Minimum");
						resultValues = getFormatedResultValues("carbonMax", carbonMax, "chromiumMin", chromiumMin);
						System.out.println(Float.parseFloat(carbonMax) <= 1.2f );
						System.out.println( Float.parseFloat(chromiumMin) >= 10.5f);
						singleResults[139] =  checkAndReport("Condition 139", conditionDetails, resultValues, 
								(Float.parseFloat(carbonMax) <= 1.2f && Float.parseFloat(chromiumMin) >= 10.5f));
						ovralResult = ovralResult & singleResults[139];
						break;
					}
					case "condition 140":{
						conditionDetails = "Covered by the proclamation but no chemical or dimensional information, 'other' ..."
								+ "then it receives an automatic pass.";
						ovralResult = ovralResult & singleResults[140]; break;
					}
					case "condition 141":{
						conditionDetails = "...then the ASTM must be A178, A179, A192, A209, A210, A213, A214, A249, A250, A335, or A498.";
						String org = getProdValue(jObj, "ProductStandards", "Organization");
						String des = getProdValue(jObj, "ProductStandards", "Designation");
						resultValues = getFormatedResultValues("org", org, "des", des);
						singleResults[141] =  checkAndReport("Condition 141", conditionDetails, resultValues, 
								("ASTM".equalsIgnoreCase(org) && (des.equals("A178") || des.equals("A179") || des.equals("A192")
									|| des.equals("A209") || des.equals("A210") || des.equals("A213") || des.equals("A214")
									|| des.equals("A249") || des.equals("A250") || des.equals("A335") || des.equals("A498")
									)
								)
										);
						ovralResult = ovralResult & singleResults[141];
						break;
					}
					case "condition 144":{
						conditionDetails = "If an ER fails condition 21 but passes all other conditions,"
								+ " send it to the manual review.";
						boolean r1 = true, r2=true;
						for(int z=1; z<singleResults.length;z++)
						{
							if(z!=21) r1= r1 && singleResults[z];
						}
						r2 = r1 & singleResults[21];
						if(r2)
						{
							HtmlReport.addHtmlStep("Validate {condition 144}", conditionDetails ,
									"N/A", "N/A", "pass", "");
						}
						else
						{
							if(r1 & !singleResults[21] ) 
							{
								HtmlReport.addHtmlStep("<span class = 'Warning'>Validate {condition 144}</span>", 
										"<span class = 'Warning'>"+ conditionDetails +"</span>" ,
										"<span class = 'Warning'>Send it to the manual review.</span>" , 
										"<span class = 'Warning'>N/A</span>",
										"Warning", "");
							} else 
							{
								HtmlReport.addHtmlStep("Validate {condition 144}", 
										conditionDetails, "N/A", "N/A",	"fail", "");
							}
						}
						break;
					}
					case "condition 145":{
						conditionDetails = "...If the condition 145 is present, then send it to the manual review.";
						HtmlReport.addHtmlStep("<span class = 'Warning'>Validate {condition 145}</span>", 
								"<span class = 'Warning'>"+ conditionDetails +"</span>" ,
								"<span class = 'Warning'>Send it to the manual review.</span>" , 
								"<span class = 'Warning'>N/A</span>",
								"Warning", "");
						break;
					}
					
					
					
					default :
					{
						failTestCase("Validate condition", "All conditions are valid", 
								condition+" is not a valid condition", "Step", "fail", "");
						break;
					}
				}//switch
			}//if product type
			else //Aluminium
			{
				switch(condition.toLowerCase())
					{
					case "condition 1":
					{
						conditionDetails ="...then minimum thickness must be greater than 0.15 mm.";
						String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
						resultValues = getFormatedResultValues("thicknessMin", thicknessMin);
						ovralResult = ovralResult & checkAndReport("Condition 1", conditionDetails, resultValues, 
								(Float.parseFloat(thicknessMin) > 0.15f ));
						break;
					}
					case "condition 2":
					{
						conditionDetails ="Aluminum, not alloyed "
								+ "2a ...the minimum percentage of aluminum must be equal to or"
								+ " greater than 99 percent and the maximum percentage of iron plus silicon must be equal to or "
								+ "less than 1 percent "
								+ "2b ...and the maximum percentage of antimony, bismuth, boron, carbon, chromium,"
								+ " cobalt, copper, lead, magnesium, manganese, nickel, niobium, nitrogen, phosphorus, selenium,"
								+ " sulfur, tin, titanium, tungsten, vanadium, or zinc must be equal to or less than 0.1 percent "
								+ "2c ...or the minimum percentage of copper must be greater than 0.1 percent and the maximum "
								+ "percentage of copper must be less than or equal to 0.2 percent and the maximum chromium must "
								+ "be equal to or less than 0.05 percent and the maximum manganese must be equal to or less "
								+ "than 0.05 percent. (2a and 2b, or 2a and 2c)";
						String aluminumMin =  getProdValue(jObj, "ChemicalComposition", "Aluminum", "Minimum");
						String ironMax =  getProdValue(jObj, "ChemicalComposition", "Iron", "Maximum");
						String siliconMax =  getProdValue(jObj, "ChemicalComposition", "Silicon", "Maximum");
						String antimonyMax =  getProdValue(jObj, "ChemicalComposition", "Antimony", "Maximum");
						String bismuthMax =  getProdValue(jObj, "ChemicalComposition", "Bismuth", "Maximum");
						String boronMax =  getProdValue(jObj, "ChemicalComposition", "Boron", "Maximum");
						String carbonMax =  getProdValue(jObj, "ChemicalComposition", "Carbon", "Maximum");
						String chromiumMax =  getProdValue(jObj, "ChemicalComposition", "Chromium", "Maximum");
						String cobaltMax =  getProdValue(jObj, "ChemicalComposition", "Cobalt", "Maximum");
						String copperMax =  getProdValue(jObj, "ChemicalComposition", "Copper", "Maximum");
						String leadMax =  getProdValue(jObj, "ChemicalComposition", "Lead", "Maximum");
						String magnesiumMax =  getProdValue(jObj, "ChemicalComposition", "Magnesium", "Maximum");
						String manganeseMax =  getProdValue(jObj, "ChemicalComposition", "Manganese", "Maximum");
						String nickelMax =  getProdValue(jObj, "ChemicalComposition", "Nickel", "Maximum");
						String niobiumMax =  getProdValue(jObj, "ChemicalComposition", "Niobium", "Maximum");
						String nitrogenMax =  getProdValue(jObj, "ChemicalComposition", "Nitrogen", "Maximum");
						String phosphorusMax =  getProdValue(jObj, "ChemicalComposition", "phosphorus", "Maximum");
						String seleniumMax =  getProdValue(jObj, "ChemicalComposition", "Selenium", "Maximum");
						String sulfurMax =  getProdValue(jObj, "ChemicalComposition", "Sulfur", "Maximum");
						String TinMax =  getProdValue(jObj, "ChemicalComposition", "Tin", "Maximum");
						String titaniumMax =  getProdValue(jObj, "ChemicalComposition", "Titanium", "Maximum");
						String tungstenMax =  getProdValue(jObj, "ChemicalComposition", "Tungsten", "Maximum");
						String vanadiumMax =  getProdValue(jObj, "ChemicalComposition", "Vanadium", "Maximum");
						String zincMax =  getProdValue(jObj, "ChemicalComposition", "Zinc", "Maximum");
						String copperMin =  getProdValue(jObj, "ChemicalComposition", "Copper", "Minimum");
						resultValues = getFormatedResultValues("aluminumMin", aluminumMin, "ironMax", ironMax, "siliconMax", siliconMax,
								"antimonyMax", antimonyMax, "bismuthMax", bismuthMax, "boronMax", boronMax, "carbonMax", carbonMax, 
								"chromiumMax", chromiumMax, "cobaltMax", cobaltMax, "copperMax", copperMax, "leadMax", leadMax, 
								"magnesiumMax", magnesiumMax, "manganeseMax", manganeseMax, "nickelMax", nickelMax, "niobiumMax", niobiumMax,
								"nitrogenMax", nitrogenMax, "phosphorusMax", phosphorusMax, "seleniumMax", seleniumMax, "sulfurMax", sulfurMax,
								"TinMax", TinMax, "titaniumMax", titaniumMax, "tungstenMax", tungstenMax, "vanadiumMax", vanadiumMax,
								"zincMax", zincMax, "copperMin", copperMin);
						// (2a and 2b, or 2a and 2c)";
						boolean a2 = Float.parseFloat(aluminumMin) >= 99f && 
								Float.parseFloat(ironMax)+Float.parseFloat(siliconMax) <= 1;
						boolean b2 = Float.parseFloat(antimonyMax) <= 0.1f || Float.parseFloat(bismuthMax) <= 0.1f ||
								Float.parseFloat(boronMax) <= 0.1f || Float.parseFloat(carbonMax) <= 0.1f || 
								Float.parseFloat(chromiumMax) <= 0.1f || Float.parseFloat(cobaltMax) <= 0.1f || 
								Float.parseFloat(copperMax) <= 0.1f || Float.parseFloat(leadMax) <= 0.1f || 
								Float.parseFloat(magnesiumMax) <= 0.1f || Float.parseFloat(manganeseMax) <= 0.1f || 
								Float.parseFloat(nickelMax) <= 0.1f || Float.parseFloat(niobiumMax) <= 0.1f || 
								Float.parseFloat(nitrogenMax) <= 0.1f || Float.parseFloat(phosphorusMax) <= 0.1f || 
								Float.parseFloat(seleniumMax) <= 0.1f || Float.parseFloat(sulfurMax) <= 0.1f || 
								Float.parseFloat(TinMax) <= 0.1f || Float.parseFloat(titaniumMax) <= 0.1f || 
								Float.parseFloat(tungstenMax) <= 0.1f || Float.parseFloat(vanadiumMax) <= 0.1f || 
								Float.parseFloat(zincMax) <= 0.1f;
						boolean c2 = Float.parseFloat(copperMin) > 0.1f && Float.parseFloat(copperMax) <= 0.2f && 
								Float.parseFloat(chromiumMax) <= 0.05f && Float.parseFloat(manganeseMax) <= 0.05f; 
						ovralResult = ovralResult & checkAndReport("Condition 2", conditionDetails, resultValues, 
								((a2&&b2)||(a2&&c2)));
						break;
					}
					case "condition 3":
					{
						conditionDetails ="Aluminum alloys "
								+ "3a ...then the minimum percent of aluminum must be greater than the minimum percent of antimony, "
								+ "bismuth, boron, carbon, chromium, cobalt, copper, iron, lead, magnesium, manganese, nickel, niobium,"
								+ " nitrogen, phosphorus, selenium, silicon, sulfur, tin, titanium, tungsten, vanadium, zinc, and other "
								+ "OR The sum of the maximum percent of iron, silicon, antimony, bismuth, boron, carbon, chromium, "
								+ "cobalt, copper, lead, magnesium, manganese, nickel, niobium, nitrogen, phosphorus, selenium, "
								+ "sulfur, tin, titanium, tungsten, vanadium, zinc, and other must be less than 50 percent. "
								+ "[Note: this allows CBP to consider those requests where all aluminum content is left blank by the submitter.] "
								+ "AND 3b ...the minimum percentage of iron plus silicon must be greater than 1 percent. "
								+ "OR 3c ...the minimum percentage of antimony, bismuth, boron, carbon, chromium, cobalt, copper, lead, "
								+ "magnesium, manganese, nickel, niobium, nitrogen, phosphorus, selenium, sulfur, tin, titanium, tungsten,"
								+ " vanadium, zinc, or other must be greater than 0.1 percent. "
								+ "OR 3d ...the sum of the minimum percentage of antimony, bismuth, boron, carbon, chromium, cobalt, copper, "
								+ "iron, lead, magnesium, manganese, nickel, niobium, nitrogen, phosphorus, selenium, silicon, sulfur, tin, "
								+ "titanium, tungsten, vanadium, zinc, and other is greater than 1 percent."
								+ " (3a and 3b, or 3a and 3c, or 3a and 3d)";
						float aluminumMin =  Float.parseFloat(getProdValue(jObj, "ChemicalComposition", "Aluminum", "Minimum"));
						String antimonyMin =  getProdValue(jObj, "ChemicalComposition", "Antimony", "Minimum");
						String bismuthMin =  getProdValue(jObj, "ChemicalComposition", "Bismuth", "Minimum");
						String boronMin =  getProdValue(jObj, "ChemicalComposition", "Boron", "Minimum");
						String carbonMin =  getProdValue(jObj, "ChemicalComposition", "Carbon", "Minimum");
						String chromiumMin =  getProdValue(jObj, "ChemicalComposition", "Chromium", "Minimum");
						String cobaltMin =  getProdValue(jObj, "ChemicalComposition", "Cobalt", "Minimum");
						String copperMin =  getProdValue(jObj, "ChemicalComposition", "Copper", "Minimum");
						String ironMin =  getProdValue(jObj, "ChemicalComposition", "Iron", "Minimum");
						String leadMin =  getProdValue(jObj, "ChemicalComposition", "Lead", "Minimum");
						String magnesiumMin =  getProdValue(jObj, "ChemicalComposition", "Magnesium", "Minimum");
						String manganeseMin =  getProdValue(jObj, "ChemicalComposition", "Manganese", "Minimum");
						String nickelMin =  getProdValue(jObj, "ChemicalComposition", "Nickel", "Minimum");
						String niobiumMin =  getProdValue(jObj, "ChemicalComposition", "Niobium", "Minimum");
						String nitrogenMin =  getProdValue(jObj, "ChemicalComposition", "Nitrogen", "Minimum");
						String phosphorusMin =  getProdValue(jObj, "ChemicalComposition", "phosphorus", "Minimum");
						String seleniumMin =  getProdValue(jObj, "ChemicalComposition", "Selenium", "Minimum");
						String siliconMin =  getProdValue(jObj, "ChemicalComposition", "silicon", "Minimum");
						String sulfurMin =  getProdValue(jObj, "ChemicalComposition", "Sulfur", "Minimum");
						String TinMin =  getProdValue(jObj, "ChemicalComposition", "Tin", "Minimum");
						String titaniumMin =  getProdValue(jObj, "ChemicalComposition", "Titanium", "Minimum");
						String tungstenMin =  getProdValue(jObj, "ChemicalComposition", "Tungsten", "Minimum");
						String vanadiumMin =  getProdValue(jObj, "ChemicalComposition", "Vanadium", "Minimum");
						String zincMin =  getProdValue(jObj, "ChemicalComposition", "Zinc", "Minimum");
						String ironMax =  getProdValue(jObj, "ChemicalComposition", "Iron", "Maximum");
						String siliconMax =  getProdValue(jObj, "ChemicalComposition", "Silicon", "Maximum");
						String antimonyMax =  getProdValue(jObj, "ChemicalComposition", "Antimony", "Maximum");
						String bismuthMax =  getProdValue(jObj, "ChemicalComposition", "Bismuth", "Maximum");
						String boronMax =  getProdValue(jObj, "ChemicalComposition", "Boron", "Maximum");
						String carbonMax =  getProdValue(jObj, "ChemicalComposition", "Carbon", "Maximum");
						String chromiumMax =  getProdValue(jObj, "ChemicalComposition", "Chromium", "Maximum");
						String cobaltMax =  getProdValue(jObj, "ChemicalComposition", "Cobalt", "Maximum");
						String copperMax =  getProdValue(jObj, "ChemicalComposition", "Copper", "Maximum");
						String leadMax =  getProdValue(jObj, "ChemicalComposition", "Lead", "Maximum");
						String magnesiumMax =  getProdValue(jObj, "ChemicalComposition", "Magnesium", "Maximum");
						String manganeseMax =  getProdValue(jObj, "ChemicalComposition", "Manganese", "Maximum");
						String nickelMax =  getProdValue(jObj, "ChemicalComposition", "Nickel", "Maximum");
						String niobiumMax =  getProdValue(jObj, "ChemicalComposition", "Niobium", "Maximum");
						String nitrogenMax =  getProdValue(jObj, "ChemicalComposition", "Nitrogen", "Maximum");
						String phosphorusMax =  getProdValue(jObj, "ChemicalComposition", "phosphorus", "Maximum");
						String seleniumMax =  getProdValue(jObj, "ChemicalComposition", "Selenium", "Maximum");
						String sulfurMax =  getProdValue(jObj, "ChemicalComposition", "Sulfur", "Maximum");
						String TinMax =  getProdValue(jObj, "ChemicalComposition", "Tin", "Maximum");
						String titaniumMax =  getProdValue(jObj, "ChemicalComposition", "Titanium", "Maximum");
						String tungstenMax =  getProdValue(jObj, "ChemicalComposition", "Tungsten", "Maximum");
						String vanadiumMax =  getProdValue(jObj, "ChemicalComposition", "Vanadium", "Maximum");
						String zincMax =  getProdValue(jObj, "ChemicalComposition", "Zinc", "Maximum");	
						//String otherMax =  getProdValue(jObj, "ChemicalComposition", "Other", "Maximum");	
						boolean a3 = 
						(aluminumMin > Float.parseFloat(antimonyMin) && aluminumMin > Float.parseFloat(bismuthMin) &&
						aluminumMin > Float.parseFloat(boronMin) && aluminumMin > Float.parseFloat(carbonMin) &&
						aluminumMin > Float.parseFloat(chromiumMin) && aluminumMin > Float.parseFloat(cobaltMin) &&
						aluminumMin > Float.parseFloat(copperMin) && aluminumMin > Float.parseFloat(ironMin) &&
						aluminumMin > Float.parseFloat(leadMin) && aluminumMin > Float.parseFloat(magnesiumMin) &&
						aluminumMin > Float.parseFloat(manganeseMin) && aluminumMin > Float.parseFloat(nickelMin) &&
						aluminumMin > Float.parseFloat(niobiumMin) && aluminumMin > Float.parseFloat(nitrogenMin) &&
						aluminumMin > Float.parseFloat(phosphorusMin) && aluminumMin > Float.parseFloat(seleniumMin) &&
						aluminumMin > Float.parseFloat(siliconMin) && aluminumMin > Float.parseFloat(sulfurMin) &&
						aluminumMin > Float.parseFloat(TinMin) && aluminumMin > Float.parseFloat(titaniumMin) &&
						aluminumMin > Float.parseFloat(tungstenMin) && aluminumMin > Float.parseFloat(vanadiumMin) &&
						aluminumMin > Float.parseFloat(zincMin) )//&& aluminumMin > Float.parseFloat(otherMin)
						||
						(Float.parseFloat(ironMax) < 50f && Float.parseFloat(siliconMax) < 50f &&
						Float.parseFloat(antimonyMax) < 50f && Float.parseFloat(bismuthMax) < 50f &&
						Float.parseFloat(boronMax) < 50f && Float.parseFloat(carbonMax) < 50f && 
						Float.parseFloat(chromiumMax) < 50f && Float.parseFloat(cobaltMax) < 50f && 
						Float.parseFloat(copperMax) < 50f && Float.parseFloat(leadMax) < 50f && 
						Float.parseFloat(magnesiumMax) < 50f && Float.parseFloat(manganeseMax) < 50f && 
						Float.parseFloat(nickelMax) < 50f && Float.parseFloat(niobiumMax) < 50f && 
						Float.parseFloat(nitrogenMax) < 50f && Float.parseFloat(phosphorusMax) < 50f && 
						Float.parseFloat(seleniumMax) < 50f && Float.parseFloat(sulfurMax) < 50f && 
						Float.parseFloat(TinMax) < 50f && Float.parseFloat(titaniumMax) < 50f && 
						Float.parseFloat(tungstenMax) < 50f && Float.parseFloat(vanadiumMax) < 50f && 
						Float.parseFloat(zincMax) < 50f  );//&& Float.parseFloat(otherMax) < 50f
						// "AND 3b ...the minimum percentage of iron plus silicon must be greater than 1 percent. "
						boolean b3 = Float.parseFloat(ironMin) + Float.parseFloat(siliconMin) >1f; 
						boolean c3 = 
								(0.1f < Float.parseFloat(antimonyMin) && 0.1f < Float.parseFloat(bismuthMin) &&
								0.1f < Float.parseFloat(boronMin) && 0.1f < Float.parseFloat(carbonMin) &&
								0.1f < Float.parseFloat(chromiumMin) && 0.1f < Float.parseFloat(cobaltMin) &&
								0.1f < Float.parseFloat(copperMin) && 
								0.1f < Float.parseFloat(leadMin) && 0.1f < Float.parseFloat(magnesiumMin) &&
								0.1f < Float.parseFloat(manganeseMin) && 0.1f < Float.parseFloat(nickelMin) &&
								0.1f < Float.parseFloat(niobiumMin) && 0.1f < Float.parseFloat(nitrogenMin) &&
								0.1f < Float.parseFloat(phosphorusMin) && 0.1f < Float.parseFloat(seleniumMin) &&
								0.1f < Float.parseFloat(sulfurMin) &&
								0.1f < Float.parseFloat(TinMin) && 0.1f < Float.parseFloat(titaniumMin) &&
								0.1f < Float.parseFloat(tungstenMin) && 0.1f < Float.parseFloat(vanadiumMin) &&
								0.1f < Float.parseFloat(zincMin) );//&& 0.1f < Float.parseFloat(otherMin)
						boolean d3 = (Float.parseFloat(antimonyMin) + Float.parseFloat(bismuthMin) + Float.parseFloat(boronMin) + Float.parseFloat(carbonMin) +
								Float.parseFloat(chromiumMin) + Float.parseFloat(cobaltMin) + Float.parseFloat(copperMin) + Float.parseFloat(ironMin) +
								Float.parseFloat(leadMin) + Float.parseFloat(magnesiumMin) + Float.parseFloat(manganeseMin) + Float.parseFloat(nickelMin) +
								Float.parseFloat(niobiumMin) + Float.parseFloat(nitrogenMin) + Float.parseFloat(phosphorusMin) + Float.parseFloat(seleniumMin) +
								Float.parseFloat(sulfurMin) + Float.parseFloat(TinMin) + Float.parseFloat(titaniumMin) +
								Float.parseFloat(tungstenMin) + Float.parseFloat(vanadiumMin) +	Float.parseFloat(zincMin)  > 1);//+ Float.parseFloat(otherMin)
						
						resultValues = getFormatedResultValues("aluminumMin", aluminumMin+"", "ironMax", ironMax, "siliconMax", siliconMax,
								"antimonyMax", antimonyMax, "bismuthMax", bismuthMax, "boronMax", boronMax, "carbonMax", carbonMax, 
								"chromiumMax", chromiumMax, "cobaltMax", cobaltMax, "copperMax", copperMax, "leadMax", leadMax, 
								"magnesiumMax", magnesiumMax, "manganeseMax", manganeseMax, "nickelMax", nickelMax, "niobiumMax", niobiumMax,
								"nitrogenMax", nitrogenMax, "phosphorusMax", phosphorusMax, "seleniumMax", seleniumMax, "sulfurMax", sulfurMax,
								"TinMax", TinMax, "titaniumMax", titaniumMax, "tungstenMax", tungstenMax, "vanadiumMax", vanadiumMax,
								"zincMax", zincMax, "copperMin", copperMin, "ironMin", ironMin, "siliconMin", siliconMin,
								"antimonyMin", antimonyMin, "bismuthMin", bismuthMin, "boronMin", boronMin, "carbonMin", carbonMin, 
								"chromiumMin", chromiumMin, "cobaltMin", cobaltMin, "copperMin", copperMin, "leadMin", leadMin, 
								"magnesiumMin", magnesiumMin, "manganeseMin", manganeseMin, "nickelMin", nickelMin, "niobiumMin", niobiumMin,
								"nitrogenMin", nitrogenMin, "phosphorusMin", phosphorusMin, "seleniumMin", seleniumMin, "sulfurMin", sulfurMin,
								"TinMin", TinMin, "titaniumMin", titaniumMin, "tungstenMin", tungstenMin, "vanadiumMin", vanadiumMin,
								"zincMin", zincMin);
						ovralResult = ovralResult & checkAndReport("Condition 3", conditionDetails, resultValues, 
								((a3&&b3)||(a3&&c3)||(a3&&d3)));
						break;
					}
					case "condition 4":
					{
						conditionDetails ="...then the minimum percentage of copper must be equal to or less than 7.0 percent. Or ..."
								+ "then the minimum percentage of zinc must be equal to or less than 10.0 percent.";
						String copperMin = getProdValue(jObj, "ChemicalComposition", "Copper", "Minimum");
						String zincMin = getProdValue(jObj, "ChemicalComposition", "Zinc", "Minimum");
						resultValues = getFormatedResultValues("copperMin", copperMin, "zincMin", zincMin);
						ovralResult = ovralResult & checkAndReport("Condition 4", conditionDetails, resultValues, 
								(Float.parseFloat(copperMin) <= 7.0f  || Float.parseFloat(zincMin) <= 10.0f ));
						break;
					}
					case "condition 5":
					{
						conditionDetails ="...then the minimum percentage of magnesium must be equal to or less than 3.0 percent and"
								+ " the minimum percentage of silicon must be equal to or less than 3.0 percent."; 
						String magnesiumMin = getProdValue(jObj, "ChemicalComposition", "Magnesium", "Minimum");
						String siliconMin = getProdValue(jObj, "ChemicalComposition", "Silicon", "Minimum");
						//String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
						//String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						resultValues = getFormatedResultValues("magnesiumMin", magnesiumMin, "siliconMin", siliconMin);
						ovralResult = ovralResult & checkAndReport("Condition 5", conditionDetails, resultValues, 
								(Float.parseFloat(magnesiumMin) <= 3.0f  && Float.parseFloat(siliconMin) <= 3.0f ));
						break;
					}
					case "condition 6":
					{
						conditionDetails ="Aluminum vanadium master alloy ...then the minimum percentage "
								+ "of vanadium must be equal to or greater than 20 percent.";
						String vanadiumMin = getProdValue(jObj, "ChemicalComposition", "Vanadium", "Minimum");
						resultValues = getFormatedResultValues("vanadiumMin", vanadiumMin);
						ovralResult = ovralResult & checkAndReport("Condition 6", conditionDetails, resultValues, 
								( Float.parseFloat(vanadiumMin) >= 20f ));
						break;
					}
					case "condition 7":
					{
						conditionDetails ="Aluminum can stock ...then the minimum thickness must be greater "
								+ "than 0.175 mm and the maximum thickness must be equal to or less than"
								+ " 0.432 mm and the minimum width must be greater than 254 mm.";
						String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
						String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						String widthMin = getProdValue(jObj, "ProductDimensions", "Width", "Minimum");
						resultValues = getFormatedResultValues("thicknessMin", thicknessMin, "thicknessMax", thicknessMax, 
								"widthMin", widthMin);
						ovralResult = ovralResult & checkAndReport("Condition 7", conditionDetails, resultValues,
								( Float.parseFloat(thicknessMin) > 0.175f && Float.parseFloat(thicknessMax) <= 0.432f 
										&& Float.parseFloat(widthMin) > 254f));
						break;
					}
					case "condition 8":
					{
						conditionDetails ="Body stock ...then the minimum percent manganese must be greater than the minimum percentage "
								+ "of antimony, bismuth, boron, chromium, cobalt, copper, iron, lead, magnesium, nickel, niobium, nitrogen,"
								+ " phosphorous, selenium, silicon, sulfur, tin, titanium, tungsten, vanadium, or zinc and the maximum "
								+ "percent manganese must be greater than the maximum percentage of antimony, bismuth, boron, chromium, "
								+ "cobalt, copper, iron, lead, magnesium, nickel, niobium, nitrogen, phosphorous, selenium, silicon, "
								+ "sulfur, tin, titanium, tungsten, vanadium, or zinc and the minimum tensile strength must be greater "
								+ "than or equal to 262 MPa.";
						float manganeseMin =  Float.parseFloat(getProdValue(jObj, "ChemicalComposition", "Manganese", "Minimum"));
						float manganeseMax =  Float.parseFloat(getProdValue(jObj, "ChemicalComposition", "Manganese", "Maximum"));
						String antimonyMax =  getProdValue(jObj, "ChemicalComposition", "Antimony", "Maximum");
						String bismuthMax =  getProdValue(jObj, "ChemicalComposition", "Bismuth", "Maximum");
						String boronMax =  getProdValue(jObj, "ChemicalComposition", "Boron", "Maximum");
						String chromiumMax =  getProdValue(jObj, "ChemicalComposition", "Chromium", "Maximum");						
						String cobaltMax =  getProdValue(jObj, "ChemicalComposition", "Cobalt", "Maximum");
						String copperMax =  getProdValue(jObj, "ChemicalComposition", "Copper", "Maximum");
						String ironMax =  getProdValue(jObj, "ChemicalComposition", "Iron", "Maximum");
						String leadMax =  getProdValue(jObj, "ChemicalComposition", "Lead", "Maximum");
						String magnesiumMax =  getProdValue(jObj, "ChemicalComposition", "Magnesium", "Maximum");
						String nickelMax =  getProdValue(jObj, "ChemicalComposition", "Nickel", "Maximum");
						String niobiumMax =  getProdValue(jObj, "ChemicalComposition", "Niobium", "Maximum");
						String nitrogenMax =  getProdValue(jObj, "ChemicalComposition", "Nitrogen", "Maximum");
						String phosphorusMax =  getProdValue(jObj, "ChemicalComposition", "phosphorus", "Maximum");
						String seleniumMax =  getProdValue(jObj, "ChemicalComposition", "Selenium", "Maximum");
						String siliconMax =  getProdValue(jObj, "ChemicalComposition", "Silicon", "Maximum");
						String sulfurMax =  getProdValue(jObj, "ChemicalComposition", "Sulfur", "Maximum");
						String TinMax =  getProdValue(jObj, "ChemicalComposition", "Tin", "Maximum");
						String titaniumMax =  getProdValue(jObj, "ChemicalComposition", "Titanium", "Maximum");
						String tungstenMax =  getProdValue(jObj, "ChemicalComposition", "Tungsten", "Maximum");
						String vanadiumMax =  getProdValue(jObj, "ChemicalComposition", "Vanadium", "Maximum");
						String zincMax =  getProdValue(jObj, "ChemicalComposition", "Zinc", "Maximum");
						//
						String antimonyMin =  getProdValue(jObj, "ChemicalComposition", "Antimony", "Minimum");
						String bismuthMin =  getProdValue(jObj, "ChemicalComposition", "Bismuth", "Minimum");
						String boronMin =  getProdValue(jObj, "ChemicalComposition", "Boron", "Minimum");
						String chromiumMin =  getProdValue(jObj, "ChemicalComposition", "Chromium", "Minimum");						
						String cobaltMin =  getProdValue(jObj, "ChemicalComposition", "Cobalt", "Minimum");
						String copperMin =  getProdValue(jObj, "ChemicalComposition", "Copper", "Minimum");
						String ironMin =  getProdValue(jObj, "ChemicalComposition", "Iron", "Minimum");
						String leadMin =  getProdValue(jObj, "ChemicalComposition", "Lead", "Minimum");
						String magnesiumMin =  getProdValue(jObj, "ChemicalComposition", "Magnesium", "Minimum");
						String nickelMin =  getProdValue(jObj, "ChemicalComposition", "Nickel", "Minimum");
						String niobiumMin =  getProdValue(jObj, "ChemicalComposition", "Niobium", "Minimum");
						String nitrogenMin =  getProdValue(jObj, "ChemicalComposition", "Nitrogen", "Minimum");
						String phosphorusMin =  getProdValue(jObj, "ChemicalComposition", "phosphorus", "Minimum");
						String seleniumMin =  getProdValue(jObj, "ChemicalComposition", "Selenium", "Minimum");
						String siliconMin =  getProdValue(jObj, "ChemicalComposition", "Silicon", "Minimum");
						String sulfurMin =  getProdValue(jObj, "ChemicalComposition", "Sulfur", "Minimum");
						String TinMin =  getProdValue(jObj, "ChemicalComposition", "Tin", "Minimum");
						String titaniumMin =  getProdValue(jObj, "ChemicalComposition", "Titanium", "Minimum");
						String tungstenMin =  getProdValue(jObj, "ChemicalComposition", "Tungsten", "Minimum");
						String vanadiumMin =  getProdValue(jObj, "ChemicalComposition", "Vanadium", "Minimum");
						String zincMin =  getProdValue(jObj, "ChemicalComposition", "Zinc", "Minimum");
						String tensileStrengthMin = getProdValue(jObj, "ProductStrength", "TensileStrength", "Minimum");
						resultValues = getFormatedResultValues("manganeseMin", manganeseMin+"", "ironMax", ironMax, "siliconMax", siliconMax,
								"antimonyMax", antimonyMax, "bismuthMax", bismuthMax, "boronMax", boronMax,	"chromiumMax", chromiumMax, 
								"cobaltMax", cobaltMax, "copperMax", copperMax, "leadMax", leadMax, "magnesiumMax", magnesiumMax, 
								"nickelMax", nickelMax, "niobiumMax", niobiumMax, "nitrogenMax", nitrogenMax, "phosphorusMax", phosphorusMax, 
								"seleniumMax", seleniumMax, "sulfurMax", sulfurMax,	"TinMax", TinMax, "titaniumMax", titaniumMax, "tungstenMax",
								tungstenMax, "vanadiumMax", vanadiumMax, "zincMax", zincMax, "manganeseMax", manganeseMax+"", "ironMin", ironMin, 
								"siliconMin", siliconMin,
								"antimonyMin", antimonyMin, "bismuthMin", bismuthMin, "boronMin", boronMin,	"chromiumMin", chromiumMin, 
								"cobaltMin", cobaltMin, "copperMin", copperMin, "leadMin", leadMin, "magnesiumMin", magnesiumMin, 
								"nickelMin", nickelMin, "niobiumMin", niobiumMin, "nitrogenMin", nitrogenMin, "phosphorusMin", phosphorusMin, 
								"seleniumMin", seleniumMin, "sulfurMin", sulfurMin,	"TinMin", TinMin, "titaniumMin", titaniumMin, "tungstenMin",
								tungstenMin, "vanadiumMin", vanadiumMin, "zincMin", zincMin, "tensileStrengthMin", tensileStrengthMin);
						ovralResult = ovralResult & checkAndReport("Condition 8", conditionDetails, resultValues, 
								(
								(manganeseMin>Float.parseFloat(antimonyMin) || manganeseMin>Float.parseFloat(bismuthMin) ||manganeseMin>Float.parseFloat(boronMin) ||
								manganeseMin>Float.parseFloat(chromiumMin) ||manganeseMin>Float.parseFloat(cobaltMin) ||manganeseMin>Float.parseFloat(copperMin) ||
								manganeseMin>Float.parseFloat(ironMin) ||manganeseMin>Float.parseFloat(leadMin) ||manganeseMin>Float.parseFloat(magnesiumMin) ||
								manganeseMin>Float.parseFloat(nickelMin) ||manganeseMin>Float.parseFloat(niobiumMin) ||manganeseMin>Float.parseFloat(nitrogenMin) ||
								manganeseMin>Float.parseFloat(phosphorusMin) ||manganeseMin>Float.parseFloat(seleniumMin) ||manganeseMin>Float.parseFloat(siliconMin) ||
								manganeseMin>Float.parseFloat(sulfurMin) ||manganeseMin>Float.parseFloat(TinMin) ||manganeseMin>Float.parseFloat(titaniumMin) ||
								manganeseMin>Float.parseFloat(tungstenMin) ||manganeseMin>Float.parseFloat(vanadiumMin) ||manganeseMin>Float.parseFloat(zincMin) ) 
								&&
								(manganeseMax>Float.parseFloat(antimonyMax) || manganeseMax>Float.parseFloat(bismuthMax) ||manganeseMax>Float.parseFloat(boronMax) ||
								manganeseMax>Float.parseFloat(chromiumMax) ||manganeseMax>Float.parseFloat(cobaltMax) ||manganeseMax>Float.parseFloat(copperMax) ||
								manganeseMax>Float.parseFloat(ironMax) ||manganeseMax>Float.parseFloat(leadMax) ||manganeseMax>Float.parseFloat(magnesiumMax) ||
								manganeseMax>Float.parseFloat(nickelMax) ||manganeseMax>Float.parseFloat(niobiumMax) ||manganeseMax>Float.parseFloat(nitrogenMax) ||
								manganeseMax>Float.parseFloat(phosphorusMax) ||manganeseMax>Float.parseFloat(seleniumMax) ||manganeseMax>Float.parseFloat(siliconMax) ||
								manganeseMax>Float.parseFloat(sulfurMax) ||manganeseMax>Float.parseFloat(TinMax) ||manganeseMax>Float.parseFloat(titaniumMax) ||
								manganeseMax>Float.parseFloat(tungstenMax) ||manganeseMax>Float.parseFloat(vanadiumMax) ||manganeseMax>Float.parseFloat(zincMax) )
								)
								&&
								(Float.parseFloat(tensileStrengthMin)>=262f)
								);
						break;
					}
					case "condition 9":
					{
						conditionDetails ="Lid stock ... then the minimum percent magnesium must be greater than the minimum percentage of antimony,"
								+ " bismuth, boron, chromium, cobalt, copper, iron, lead, manganese, nickel, niobium, nitrogen, phosphorous, selenium,"
								+ " silicon, sulfur, tin, titanium, tungsten, vanadium, or zinc and the maximum percent magnesium must be greater than"
								+ " the maximum percentage of antimony, bismuth, boron, chromium, cobalt, copper, iron, lead, manganese, nickel, niobium, "
								+ "nitrogen, phosphorous, selenium, silicon, sulfur, tin, titanium, tungsten, vanadium, "
								+ "or zinc and the minimum tensile strength must be greater than or equal to 345 MPa.";
						float magnesiumMin =  Float.parseFloat(getProdValue(jObj, "ChemicalComposition", "Magnesium", "Minimum"));
						float magnesiumMax =  Float.parseFloat(getProdValue(jObj, "ChemicalComposition", "Magnesium", "Maximum"));
						String manganeseMin =  getProdValue(jObj, "ChemicalComposition", "Manganese", "Minimum");//
						String manganeseMax =  getProdValue(jObj, "ChemicalComposition", "Manganese", "Maximum");
						String antimonyMax =  getProdValue(jObj, "ChemicalComposition", "Antimony", "Maximum");
						String bismuthMax =  getProdValue(jObj, "ChemicalComposition", "Bismuth", "Maximum");
						String boronMax =  getProdValue(jObj, "ChemicalComposition", "Boron", "Maximum");
						String chromiumMax =  getProdValue(jObj, "ChemicalComposition", "Chromium", "Maximum");						
						String cobaltMax =  getProdValue(jObj, "ChemicalComposition", "Cobalt", "Maximum");
						String copperMax =  getProdValue(jObj, "ChemicalComposition", "Copper", "Maximum");
						String ironMax =  getProdValue(jObj, "ChemicalComposition", "Iron", "Maximum");
						String leadMax =  getProdValue(jObj, "ChemicalComposition", "Lead", "Maximum");
						String nickelMax =  getProdValue(jObj, "ChemicalComposition", "Nickel", "Maximum");
						String niobiumMax =  getProdValue(jObj, "ChemicalComposition", "Niobium", "Maximum");
						String nitrogenMax =  getProdValue(jObj, "ChemicalComposition", "Nitrogen", "Maximum");
						String phosphorusMax =  getProdValue(jObj, "ChemicalComposition", "phosphorus", "Maximum");
						String seleniumMax =  getProdValue(jObj, "ChemicalComposition", "Selenium", "Maximum");
						String siliconMax =  getProdValue(jObj, "ChemicalComposition", "Silicon", "Maximum");
						String sulfurMax =  getProdValue(jObj, "ChemicalComposition", "Sulfur", "Maximum");
						String TinMax =  getProdValue(jObj, "ChemicalComposition", "Tin", "Maximum");
						String titaniumMax =  getProdValue(jObj, "ChemicalComposition", "Titanium", "Maximum");
						String tungstenMax =  getProdValue(jObj, "ChemicalComposition", "Tungsten", "Maximum");
						String vanadiumMax =  getProdValue(jObj, "ChemicalComposition", "Vanadium", "Maximum");
						String zincMax =  getProdValue(jObj, "ChemicalComposition", "Zinc", "Maximum");
						String antimonyMin =  getProdValue(jObj, "ChemicalComposition", "Antimony", "Minimum");
						String bismuthMin =  getProdValue(jObj, "ChemicalComposition", "Bismuth", "Minimum");
						String boronMin =  getProdValue(jObj, "ChemicalComposition", "Boron", "Minimum");
						String chromiumMin =  getProdValue(jObj, "ChemicalComposition", "Chromium", "Minimum");						
						String cobaltMin =  getProdValue(jObj, "ChemicalComposition", "Cobalt", "Minimum");
						String copperMin =  getProdValue(jObj, "ChemicalComposition", "Copper", "Minimum");
						String ironMin =  getProdValue(jObj, "ChemicalComposition", "Iron", "Minimum");
						String leadMin =  getProdValue(jObj, "ChemicalComposition", "Lead", "Minimum");
						String nickelMin =  getProdValue(jObj, "ChemicalComposition", "Nickel", "Minimum");
						String niobiumMin =  getProdValue(jObj, "ChemicalComposition", "Niobium", "Minimum");
						String nitrogenMin =  getProdValue(jObj, "ChemicalComposition", "Nitrogen", "Minimum");
						String phosphorusMin =  getProdValue(jObj, "ChemicalComposition", "phosphorus", "Minimum");
						String seleniumMin =  getProdValue(jObj, "ChemicalComposition", "Selenium", "Minimum");
						String siliconMin =  getProdValue(jObj, "ChemicalComposition", "Silicon", "Minimum");
						String sulfurMin =  getProdValue(jObj, "ChemicalComposition", "Sulfur", "Minimum");
						String TinMin =  getProdValue(jObj, "ChemicalComposition", "Tin", "Minimum");
						String titaniumMin =  getProdValue(jObj, "ChemicalComposition", "Titanium", "Minimum");
						String tungstenMin =  getProdValue(jObj, "ChemicalComposition", "Tungsten", "Minimum");
						String vanadiumMin =  getProdValue(jObj, "ChemicalComposition", "Vanadium", "Minimum");
						String zincMin =  getProdValue(jObj, "ChemicalComposition", "Zinc", "Minimum");
						String tensileStrengthMin = getProdValue(jObj, "ProductStrength", "TensileStrength", "Minimum");
						resultValues = getFormatedResultValues("magnesiumMax", magnesiumMax+"", "magnesiumMin", magnesiumMin+"", "manganeseMin", 
								manganeseMin+"", "ironMax", ironMax, "siliconMax", siliconMax, "antimonyMax", antimonyMax, "bismuthMax", bismuthMax,
								"boronMax", boronMax,	"chromiumMax", chromiumMax, "cobaltMax", cobaltMax, "copperMax", copperMax, "leadMax", leadMax,  
								"nickelMax", nickelMax, "niobiumMax", niobiumMax, "nitrogenMax", nitrogenMax, "phosphorusMax", phosphorusMax, 
								"seleniumMax", seleniumMax, "sulfurMax", sulfurMax,	"TinMax", TinMax, "titaniumMax", titaniumMax, "tungstenMax",
								tungstenMax, "vanadiumMax", vanadiumMax, "zincMax", zincMax, "manganeseMax", manganeseMax+"", "ironMin", ironMin, 
								"siliconMin", siliconMin, "antimonyMin", antimonyMin, "bismuthMin", bismuthMin, "boronMin", boronMin, "chromiumMin", 
								chromiumMin, "cobaltMin", cobaltMin, "copperMin", copperMin, "leadMin", leadMin, "nickelMin", nickelMin, "niobiumMin",
								niobiumMin, "nitrogenMin", nitrogenMin, "phosphorusMin", phosphorusMin, "seleniumMin", seleniumMin, "sulfurMin", 
								sulfurMin,	"TinMin", TinMin, "titaniumMin", titaniumMin, "tungstenMin", tungstenMin, "vanadiumMin", vanadiumMin, 
								"zincMin", zincMin, "tensileStrengthMin", tensileStrengthMin);
						ovralResult = ovralResult & checkAndReport("Condition 9", conditionDetails, resultValues, 
								(
								(
										magnesiumMin>Float.parseFloat(antimonyMin) || magnesiumMin>Float.parseFloat(bismuthMin) ||magnesiumMin>Float.parseFloat(boronMin) ||
										magnesiumMin>Float.parseFloat(chromiumMin) ||magnesiumMin>Float.parseFloat(cobaltMin) ||magnesiumMin>Float.parseFloat(copperMin) ||
										magnesiumMin>Float.parseFloat(ironMin) ||magnesiumMin>Float.parseFloat(leadMin) ||magnesiumMin>Float.parseFloat(manganeseMin) ||
										magnesiumMin>Float.parseFloat(nickelMin) ||magnesiumMin>Float.parseFloat(niobiumMin) ||magnesiumMin>Float.parseFloat(nitrogenMin) ||
										magnesiumMin>Float.parseFloat(phosphorusMin) ||magnesiumMin>Float.parseFloat(seleniumMin) ||magnesiumMin>Float.parseFloat(siliconMin) ||
										magnesiumMin>Float.parseFloat(sulfurMin) ||magnesiumMin>Float.parseFloat(TinMin) ||magnesiumMin>Float.parseFloat(titaniumMin) ||
										magnesiumMin>Float.parseFloat(tungstenMin) ||magnesiumMin>Float.parseFloat(vanadiumMin) ||magnesiumMin>Float.parseFloat(zincMin) ) 
								&&
								(		magnesiumMax>Float.parseFloat(antimonyMax) || magnesiumMax>Float.parseFloat(bismuthMax) ||magnesiumMax>Float.parseFloat(boronMax) ||
										magnesiumMax>Float.parseFloat(chromiumMax) ||magnesiumMax>Float.parseFloat(cobaltMax) ||magnesiumMax>Float.parseFloat(copperMax) ||
										magnesiumMax>Float.parseFloat(ironMax) ||magnesiumMax>Float.parseFloat(leadMax) ||magnesiumMax>Float.parseFloat(manganeseMax) ||
										magnesiumMax>Float.parseFloat(nickelMax) ||magnesiumMax>Float.parseFloat(niobiumMax) ||magnesiumMax>Float.parseFloat(nitrogenMax) ||
										magnesiumMax>Float.parseFloat(phosphorusMax) ||magnesiumMax>Float.parseFloat(seleniumMax) ||magnesiumMax>Float.parseFloat(siliconMax) ||
										magnesiumMax>Float.parseFloat(sulfurMax) ||magnesiumMax>Float.parseFloat(TinMax) ||magnesiumMax>Float.parseFloat(titaniumMax) ||
										magnesiumMax>Float.parseFloat(tungstenMax) ||magnesiumMax>Float.parseFloat(vanadiumMax) ||magnesiumMax>Float.parseFloat(zincMax) )
								)
								&&
								(Float.parseFloat(tensileStrengthMin)>=345f)
								
								);
						break;
					}
					case "condition 10":
					{
						conditionDetails ="...then the minimum percentage of silicon must be equal to or greater than 25 percent.";
						String siliconMin =  getProdValue(jObj, "ChemicalComposition", "Silicon", "Minimum");
						resultValues = getFormatedResultValues("siliconMin", siliconMin);
						ovralResult = ovralResult & checkAndReport("Condition 10", conditionDetails, resultValues, 
								(Float.parseFloat(siliconMin) >= 25f));
						break;
					}
					case "condition 11":
					{
						conditionDetails ="...then the minimum percentage of lead must be equal to or greater than 0.03 percent.";
						String leadMin =  getProdValue(jObj, "ChemicalComposition", "Lead", "Minimum");
						resultValues = getFormatedResultValues("leadMin", leadMin);
						ovralResult = ovralResult & checkAndReport("Condition 11", conditionDetails, resultValues, 
								(Float.parseFloat(leadMin) >= 0.03f));
						break;
					}
					case "condition 12":
					{
						conditionDetails ="...then minimum percentage of aluminum must be greater than 99.8 percent.";
						String aluminumMin =  getProdValue(jObj, "ChemicalComposition", "aluminum", "Minimum");
						resultValues = getFormatedResultValues("aluminumMin", aluminumMin);
						ovralResult = ovralResult & checkAndReport("Condition 12", conditionDetails, resultValues, 
								(Float.parseFloat(aluminumMin) > 99.8f));
						break;
					}
					case "condition 13":
					{
						conditionDetails ="Aluminum wire ...then the square root of the sum of the maximum width squared"
								+ " and the maximum height squared or the minimum outside diameter must be greater than 7 mm.";
						float widthMax = Float.parseFloat(getProdValue(jObj, "ProductDimensions", "Width", "Maximum"));
						float heightMax = Float.parseFloat(getProdValue(jObj, "ProductDimensions", "Height", "Maximum"));
						float squaredWidthMax = widthMax*widthMax;
						float squaredHeightMax = heightMax*heightMax;
						float sqrt = (float) Math.sqrt(squaredWidthMax+squaredHeightMax);
						String outsideDiameterMin = getProdValue(jObj, "ProductDimensions", "OutsideDiameter", "Minimum");
						resultValues = getFormatedResultValues("widthMax", widthMax+"", "heightMax", heightMax+"",
								"sqroot of (SquaredWidthMax + SquaredHeightMax)", sqrt+"", "outsideDiameterMin", outsideDiameterMin);
						ovralResult = ovralResult & checkAndReport("Condition 13", conditionDetails, resultValues, 
								(sqrt >= 7f || Float.parseFloat(outsideDiameterMin) > 7f));
						break;
					}
					case "condition 14":
					{
						conditionDetails ="Aluminum plates, sheets, and strip ...then the minimum thickness must "
								+ "be greater than 0.2 mm.";
						String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
						resultValues = getFormatedResultValues("thicknessMin", thicknessMin);
						ovralResult = ovralResult & checkAndReport("Condition 14", conditionDetails, resultValues, 
								( (Float.parseFloat(thicknessMin) > 0.2f) 
								));
						break;
					}
					case "condition 15":
					{
						conditionDetails ="...then the minimum thickness must be greater than 6.3 mm.";
						String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
						resultValues = getFormatedResultValues("thicknessMin", thicknessMin);
						ovralResult = ovralResult & checkAndReport("Condition 15", conditionDetails, resultValues, 
								( Float.parseFloat(thicknessMin) > 6.3f));
						break;
					}
					case "condition 16":
					{
						conditionDetails ="...then the maximum thickness must be less than or equal to 6.3 mm.";
						String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						resultValues = getFormatedResultValues("thicknessMax", thicknessMax);
						ovralResult = ovralResult & checkAndReport("Condition 16", conditionDetails, resultValues, 
								(Float.parseFloat(thicknessMax) <= 6.3f ));
						break;
					}
					case "condition 17":
					{
						conditionDetails ="Aluminum foil ...then the maximum thickness must be less than or equal to 0.2 mm."; 
						String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						resultValues = getFormatedResultValues("thicknessMax", thicknessMax);
						ovralResult = ovralResult & checkAndReport("Condition 17", conditionDetails, resultValues, 
								(Float.parseFloat(thicknessMax) <= 0.2f ));
						break;
					}
					case "condition 18":
					{
						conditionDetails ="...then the maximum thickness must be less than or equal to 0.15 mm.";
						String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						resultValues = getFormatedResultValues("thicknessMax", thicknessMax);
						ovralResult = ovralResult & checkAndReport("Condition 18", conditionDetails, resultValues, 
								(Float.parseFloat(thicknessMax) <= 0.15f ));
						break;
					}
					case "condition 19":
					{
						conditionDetails ="...then the maximum thickness must be less than or equal to 0.01 mm.";
						String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						resultValues = getFormatedResultValues("thicknessMax", thicknessMax);
						ovralResult = ovralResult & checkAndReport("Condition 19", conditionDetails, resultValues, 
								(Float.parseFloat(thicknessMax) <= 0.01f ));
						break;
					}
					case "condition 20":
					{
						conditionDetails ="...then the minimum thickness must be greater than 0.01 mm.";
						String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						resultValues = getFormatedResultValues("thicknessMin", thicknessMin);
						ovralResult = ovralResult & checkAndReport("Condition 20", conditionDetails, resultValues, 
								(Float.parseFloat(thicknessMin) > 0.01f ));
						break;
					}
					case "condition 21":
					{
						//conditionDetails ="...then the maximum thickness must be less than or equal to 0.01 mm.";
						//String thicknessMax = getProdValue(jObj, "ProductDimensions", "Thickness", "Maximum");
						conditionDetails ="…then the maximum silicon must be less than 25 percent"; 
						String siliconMax =  getProdValue(jObj, "ChemicalComposition", "Silicon", "Maximum");
						resultValues = getFormatedResultValues("siliconMax", siliconMax);
						ovralResult = ovralResult & checkAndReport("Condition 21", conditionDetails, resultValues, 
								(Float.parseFloat(siliconMax) < 25f ));
						break;
					}
					case "condition 22":
					{
						conditionDetails ="...then the minimum thickness must be greater than 0.01 mm. Note: "
								+ "7607116000 is in the 2018 version of the HTSUS. In the 2019 version, "
								+ "this number is split into 7607116010 and 7607116090. Once all 2018 requests "
								+ "are processed, 7607116000 will be deleted from this condition.";
						String thicknessMin = getProdValue(jObj, "ProductDimensions", "Thickness", "Minimum");
						resultValues = getFormatedResultValues("thicknessMin", thicknessMin);
						ovralResult = ovralResult & checkAndReport("Condition 22", conditionDetails, resultValues, 
								(Float.parseFloat(thicknessMin) > 0.01f ));
						break;
					}
					default:
					{
						failTestCase("Validate condition", "All conditions are valid", 
								condition+" is not a valid condition", "Step", "fail", "");
						break;
					}
				}//switch
			}//else aluminum
		}
		return ovralResult;
		/*if(!ovralResultString.equals("")) return ovralResultString;
		else
		{
			if (ovralResult) return "true";
			else return "false";
		}*/
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
										 String resValues,
										 boolean expectedSts)
	{
		String expectedStsStr = expectedSts? "Pass":"Fail";
		String actualStsStr;	
		boolean checked = true;
		String conditionNumber = conditionName.split(" ")[1];
		String passedConditions = displayedResult.substring(0, displayedResult.indexOf("Failed")-1);
		String failedConditions = displayedResult.substring(displayedResult.indexOf("Failed")-1, displayedResult.length() ); 
		String conditionIdF1 = "\"ConditionID\":\""+conditionNumber+"\"";
		String conditionIdP1 = "\"ConditionID\":\""+conditionNumber+"\"";
		List<String>  conditionIdP2 = new ArrayList<String>();
		if(!passedConditions.contains("Condition"))
		{//{"Pass":[],
			int b = passedConditions.indexOf(":\"");
			int e = passedConditions.indexOf("\",");
			if(b!=-1 && e!=-1)
			{
				String s1 = passedConditions.substring(b+2, e);
				
				if(s1.contains(","))
				{
					conditionIdP2 = Arrays.asList(s1.split(","));
				}
				else
				{
					conditionIdP2.add(s1);
				}
			}
		}
		String conditionIdF2 = conditionNumber+":";
		String displayedResults = passedConditions+System.lineSeparator()+failedConditions;
		if(passedConditions.contains(conditionIdP1) || conditionIdP2.contains(conditionNumber))
		{
			actualStsStr = "Pass";
		}
		else if (failedConditions.contains(conditionIdF1)|| failedConditions.contains(conditionIdF2))
		{
			actualStsStr = "Fail";
		}
		else
		{
			HtmlReport.addHtmlStep("Validate {"+conditionName+"}", 
					"<abbr title='"+condDetails+"'>" + expectedStsStr +": "+conditionName+" details ...(!)</abbr>",
					"<abbr title='"+displayedResults+"'>"+conditionName+" wasn't tested ...(!)</abbr>", 
					"<abbr title='"+resValues+"'>Product's values ...(!)</abbr>",
					"fail", "");
			return false;
		}
		if (expectedStsStr.equals(actualStsStr))
		{
			checked = true;
			HtmlReport.addHtmlStep("Validate {"+conditionName+"}", 
					"<abbr title='"+condDetails+"'>" + expectedStsStr +": "+conditionName+" details ...(!)</abbr>",
					"<abbr title='"+displayedResults+"'>"+actualStsStr+": Displayed results ...(!)</abbr>", 
					"<abbr title='"+resValues+"'>Product's values ...(!)</abbr>", 
					"pass", "");
		}
		else
		{
			checked = false;
			HtmlReport.addHtmlStep("Validate {"+conditionName+"}", 
					"<abbr title='"+condDetails+"'>" + expectedStsStr +": "+conditionName+" details ...(!)</abbr>",
					"<abbr title='"+displayedResults+"'>"+actualStsStr+": Displayed results ...(!)</abbr>", 
					"<abbr title='"+resValues+"'>Product's values ...(!)</abbr>", 
					"fail", "");
		}
		return checked;
	}
	/**
	 * This method reads values from json object
	 * @param jObj: json object containing form information
	 * @param blockName: name of the container
	 * @param minMax: the value searched, min or max
	 * @return the min/max value
	 * @throws Exception 
	*/
	public static String getProdValue(JSONObject obj, 
									String blockName, 
									String productName,
									String minMax) throws Exception
	{
		String prodVal = "";
		String key;
		JSONArray blockArray = (JSONArray) obj.get(blockName);
		if (blockArray==null || blockArray.size()==0)
		{
			failTestCase("Validate {"+productName+"}", "Container "+ blockName+" should be "
					+ "found in the JSON file", 
					"Not as expected", "VP", "fail", "");
		}
		for (Object block : blockArray) 
		{
		    JSONObject blockItem = (JSONObject) block;
		    key = (String) blockItem.get("Key");
		    System.out.println(key);
		    if((key!=null) && key.equalsIgnoreCase(productName) )
		    {
		    	Object valueObject = blockItem.get("Value");
		    	if(valueObject.getClass().toString().contains("Object"))
		    	{
		    		JSONObject jValueObject = (JSONObject) valueObject;
		    		prodVal = (String) jValueObject.get(minMax);
		    		break;
		    	}else
		    	{
		    		prodVal = (String) valueObject.toString();
		    		break;
		    	}
		    }
		}
		if (prodVal==null || prodVal.equals(""))
		{
			failTestCase("Validate {"+productName+"}", "Product ["+ 
					productName+"]_["+minMax+"] should be found in container["+blockName+"]", 
					"Not as expected", "VP", "fail", "");
		}
		return prodVal;
	}
	/**
	 * This method reads values from json object
	 * @param jObj: json object containing form information
	 * @param blockName: name of the container
	 * @param elementName: element Name
	 * @return the element value
	 * @throws Exception 
	*/
	public static String getProdValue(JSONObject obj, 
									String blockName, 
									String elementName) throws Exception
	{
		String elem = "";
		JSONArray blockArray = (JSONArray) obj.get(blockName);
		if (blockArray==null)
		{
			failTestCase("Validate {"+elementName+"}", "Container "+ blockName+" should be "
					+ "found in the JSON file.", 
					"Not as expected", "VP", "fail", "");
		}else if(blockArray.size()!=0)
		{
			JSONObject item = (JSONObject)blockArray.get(0);
			elem = (String) item.get(elementName);
		}
		return elem;
	}
	
	/**
	 * This method returns a concatenated string of values
	 * @param values: The set of values to be displayed
	 * @return the element value
	*/
	public static String getFormatedResultValues(String... values)
	{
		String formatedRes="";
		for(int i=0; i<values.length;i=i+2)
		{
			formatedRes =  formatedRes + values[i]+" = "+values[i+1]+System.lineSeparator();
		}
		return formatedRes;
	}
	
	
}
