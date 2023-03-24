/**
 * MilCorp
 * Mouloud Hamdidouche
 * February, 2019
*/
package libs;
import static GuiLibs.GuiTools.checkElementExists;
import static GuiLibs.GuiTools.clickElementJs;
import static GuiLibs.GuiTools.enterText;
import static GuiLibs.GuiTools.failTestCase;
import static GuiLibs.GuiTools.failTestSuite;
import static GuiLibs.GuiTools.getElementAttribute;
import static GuiLibs.GuiTools.guiMap;
import static GuiLibs.GuiTools.highlightElement;
import static GuiLibs.GuiTools.holdSeconds;
import static GuiLibs.GuiTools.navigateTo;
import static GuiLibs.GuiTools.replaceGui;
import static GuiLibs.GuiTools.scrollToElement;
import static GuiLibs.GuiTools.scrollToTheBottomOfPage;
import static GuiLibs.GuiTools.selectElementByText;
import static GuiLibs.GuiTools.setBrowserTimeOut;
import static GuiLibs.GuiTools.unHighlightElement;
import static GuiLibs.GuiTools.updateHtmlReport;
import static GuiLibs.GuiTools.updateHtmlReportOverall;
import java.io.IOException;
import java.text.DateFormat;
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
	 * @param url: URL for the application
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
			}else if (
					elementName.equalsIgnoreCase("State") || elementName.equalsIgnoreCase("SubstituteOrIdenticalProduct") ||
					elementName.equalsIgnoreCase("ManufacturingTime") || elementName.equalsIgnoreCase("Quality") ||
					elementName.equalsIgnoreCase("AvailableQuantity") || elementName.equalsIgnoreCase("DeliveryTime") 
					)
			{
				selectElementByText(replaceGui(guiMap.get("RE_StepOneElementSelect"), elementName), entry.getValue());
			}
			else
			{
				enterText(replaceGui(guiMap.get("RE_StepOneElement"), elementName), entry.getValue());
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
		String loc;
		String[] locations = 
				{
					"BIS232Objection_JSONData_ReasonForObjection_ManufacturingLocations_0__",
					"BIS232Objection_JSONData_ReasonForObjection_ProductionLocations_0__",
					"BIS232Objection_JSONData_ReasonForObjection_SubstitueLocations_0__",
					"BIS232Objection_JSONData_ReasonForObjection_ProductionSubstituteLocations_0__"
				};
		for(int i=0; i<locations.length;i++)
		{
			loc = locations[i];
			for (HashMap.Entry<String, String> entry : row.entrySet()) 
			{
				System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
				elementName = entry.getKey().trim();
				if
				(
				   elementName.equalsIgnoreCase("Scenarios") || elementName.equalsIgnoreCase("Active") 
				   ||(elementName.equalsIgnoreCase("Days") && (i==0 || i==2))
				 )
				{
					continue;
				}
				else
				{
					enterText(replaceGui(guiMap.get("OR_StepTwoElement"), loc, elementName), entry.getValue());
				}
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
		String elementName = "";
		holdSeconds(1);
		clickElementJs(replaceGui(guiMap.get("tabName"), "Step 3"));
		holdSeconds(2);
		for (HashMap.Entry<String, String> entry : row.entrySet()) 
		{
			System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
			elementName = entry.getKey().trim();
			if(!elementName.equalsIgnoreCase("Scenarios") && !elementName.equalsIgnoreCase("Active"))
			{
				selectElementByText(replaceGui(guiMap.get("OR_StepThreeElementSelect"), elementName), entry.getValue());
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
		String elementName="";
		clickElementJs(replaceGui(guiMap.get("tabName"), "Step 4"));
		holdSeconds(2);
		for (HashMap.Entry<String, String> entry : row.entrySet()) 
		{
			System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
			elementName = entry.getKey().trim();
			if(!elementName.equalsIgnoreCase("Scenarios") && !elementName.equalsIgnoreCase("Active"))
			{
				enterText(replaceGui(guiMap.get("OR_StepFourElement"), elementName), entry.getValue());
			}
		}
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
		holdSeconds(1);
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
				enterText(replaceGui(guiMap.get("RE_StepFiveElement"), elementName), entry.getValue());
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
		String  elementName = "", elementValue, errorMsg="", htmlToValidate="";
		holdSeconds(1);
		clickElementJs(replaceGui(guiMap.get("tabName"), "Step 1") );
		holdSeconds(2);
		boolean matches = true;
		//Validate HTSUS Here
		for (HashMap.Entry<String, String> entry : row.entrySet()) 
		{
			System.out.println("Key : " + entry.getKey() + " Value: " + entry.getValue());
			elementName = entry.getKey().trim();
			elementValue = entry.getValue();
			if(elementName.equalsIgnoreCase("State"))
			scrollToElement(replaceGui(guiMap.get("RE_StepOneElementSelect"),elementName));
			if(elementName.equalsIgnoreCase("SubstituteOrIdenticalProduct"))
			scrollToElement(replaceGui(guiMap.get("RE_StepOneElementSelect"),elementName));
			if(elementName.equalsIgnoreCase("ManufacturingTime"))
			scrollToElement(replaceGui(guiMap.get("RE_StepOneElementSelect"),elementName));
			if(elementName.equalsIgnoreCase("Scenarios") || elementName.equalsIgnoreCase("Active") )
			{
				continue;
			}else if (
					elementName.equalsIgnoreCase("State") || elementName.equalsIgnoreCase("SubstituteOrIdenticalProduct") ||
					elementName.equalsIgnoreCase("ManufacturingTime") || elementName.equalsIgnoreCase("Quality") ||
					elementName.equalsIgnoreCase("AvailableQuantity") || elementName.equalsIgnoreCase("DeliveryTime") 
					)
			{
				errorMsg = getElementAttribute(replaceGui(guiMap.get("RE_StepOneElementSelectMessage"), 
						 elementName), "text");
				htmlToValidate = "RE_StepOneElementSelectDiv";
			}
			else
			{
				errorMsg = getElementAttribute(replaceGui(guiMap.get("RE_StepOneElementMessage"), 
						elementName), "text");
				htmlToValidate = "RE_StepOneElementDiv";
			}
			switch(elementName)
			{
				case "OrgLegalName":
				{
					if ("".equals(elementValue))
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
				case "StreetAddress":
				{
					if ("".equals(elementValue))
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
				case "City":
				{
					if ("".equals(elementValue))
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
				case "POCName":
				{
					if ("".equals(elementValue))
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

				case "State": 
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
				case "ZipCode":
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
				case "PhoneNumber":
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
				}//									
				case "EmailAddress":
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
				case "WebsiteAddress": 
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
				case "ManufacturingTime": case "Quality": 
				case "AvailableQuantity": case "DeliveryTime": 
				{
					if(elementValue.trim().equals("Please Select"))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"This field is required.", errorMsg);
					}
					else
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"", errorMsg);
					}
					break;
				}
				case "SubstituteOrIdenticalProduct": 
				{
					if(elementValue.trim().equals("Please Select"))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"This field is required", errorMsg);
					}
					else
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"", errorMsg);
					}
					break;
				}
				default:
				break;
			}
		}//for
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
		String  elementName = "", elementValue="", errorMsg="", htmlToValidate="OR_StepTwoElementDiv";
		holdSeconds(1);
		clickElementJs(replaceGui(guiMap.get("tabName"), "Step 2") );
		//holdSeconds(2);
		boolean matches = true;
		//Validate HTSUS Here
		String loc;
		String[] locations = 
				{"BIS232Objection_JSONData_ReasonForObjection_ManufacturingLocations_0__",
				"BIS232Objection_JSONData_ReasonForObjection_ProductionLocations_0__",
				"BIS232Objection_JSONData_ReasonForObjection_SubstitueLocations_0__",
				"BIS232Objection_JSONData_ReasonForObjection_ProductionSubstituteLocations_0__"};
		for(int i=0; i<locations.length;i++)
		{
			loc = locations[i];
			for (HashMap.Entry<String, String> entry : row.entrySet()) 
			{
				elementName = entry.getKey().trim();
				elementValue = entry.getValue();
				System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
				
				if
				(
				   elementName.equalsIgnoreCase("Scenarios") 
				|| elementName.equalsIgnoreCase("Active") 
				|| elementName.equalsIgnoreCase("City")
				|| elementName.equalsIgnoreCase("State")
				||(elementName.equalsIgnoreCase("Days") && (i==0 || i==2))
				 )
				{
					if (elementName.equalsIgnoreCase("City"))
						scrollToElement(replaceGui(guiMap.get("OR_StepTwoElement"), loc, elementName));
					continue;
				}
				else
				{
						errorMsg = getElementAttribute(replaceGui(guiMap.get("OR_StepTwoElementMessage"), 
								loc, elementName), "text");
					if (elementName.equalsIgnoreCase("Days") || 
						elementName.equalsIgnoreCase("CurrentProductionCapacity"))
					{
						if(!elementValue.matches("^[0-9]*$"))
						{
							matches = matches & validateElementAndReport(elementName, htmlToValidate+i, 
								"Invalid Value", errorMsg);
						}
						else
						{
							matches = matches & validateElementAndReport(elementName, htmlToValidate+i, 
									"", errorMsg);
						}
					}
					else if (elementName.equalsIgnoreCase("PlantUtilization"))
					{
						if(!elementValue.matches("^[0-9]*$"))
						{
							matches = matches & validateElementAndReport(elementName, htmlToValidate+i, 
								"Invalid Value", errorMsg);
						}
						else if(elementValue.matches("[0-9]+|[0-9]+\\.?[0-9]+") 
								&& (Float.parseFloat(elementValue)<0 
								|| Float.parseFloat(elementValue)>100))
						{
							matches = matches & validateElementAndReport(elementName, htmlToValidate+i, 
									"Allowed range (0-100)", errorMsg);
						}else
						{
							matches = matches & validateElementAndReport(elementName, htmlToValidate+i, 
									"", errorMsg);
						}
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
		String errorMsg="", elementName = "", elementValue="";
		holdSeconds(1);
		clickElementJs(replaceGui(guiMap.get("tabName"), "Step 3"));
		holdSeconds(2);
		for (HashMap.Entry<String, String> entry : row.entrySet()) 
		{
			System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
			elementName = entry.getKey().trim();
			elementValue = entry.getValue().trim();
			if("Insufficient Volume".equals(elementName))
				scrollToElement(replaceGui(guiMap.get("OR_StepThreeElementSelectMessage"), elementName));
			if(!elementName.equalsIgnoreCase("Scenarios") && !elementName.equalsIgnoreCase("Active"))
			{
				errorMsg = getElementAttribute(replaceGui(guiMap.get("OR_StepThreeElementSelectMessage"), elementName), "text");
				if(elementValue.trim().equals("Please Select"))
				{
					matches = matches & validateElementAndReport(elementName, "OR_StepThreeElementSelectDiv", 
							"This field is required.", errorMsg);
				}
				else
				{
					matches = matches & validateElementAndReport(elementName, "OR_StepThreeElementSelectDiv", 
							"", errorMsg);
				}
			}
		}
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
		clickElementJs(replaceGui(guiMap.get("tabName"), "Step 4") );
		holdSeconds(1);
		String errorMsg, elementValue, elementName;
		String htmlToValidate="OR_StepFourElementDiv";
		for (HashMap.Entry<String, String> entry : row.entrySet()) 
		{
			elementName = entry.getKey();
			elementValue = entry.getValue();
			if(elementName.equalsIgnoreCase("Scenarios") || elementName.equalsIgnoreCase("Active"))
			continue;
			if(elementName.equalsIgnoreCase("PctManugactureByReqOrg"))
			errorMsg = getElementAttribute(replaceGui(guiMap.get("OR_StepFourElementMessage2"), elementName), "text");
			else				
			errorMsg = getElementAttribute(replaceGui(guiMap.get("OR_StepFourElementMessage"), elementName), "text");
			switch(elementName)
			{
				case "PctManugactureByReqOrg":
				{
					if ("".equals(elementValue))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate+"2", 
								"This field is required", errorMsg);
					}
					else if(!elementValue.matches("[0-9]+|[0-9]+\\.?[0-9]+"))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate+"2", 
								"Invalid Value", errorMsg);
					}
					else if(elementValue.matches("[0-9]+|[0-9]+\\.?[0-9]+") 
							&& (Float.parseFloat(elementValue)<0 
							|| Float.parseFloat(elementValue)>100))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate+"2", 
								"Allowed range (0-100)", errorMsg);
					}else
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate+"2", 
								"", errorMsg);
					}
					break;
				}
				case "DaysRequiredToManufacture":
				{
					if ("".equals(elementValue))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"This field is required", errorMsg);
					}
					else if(!elementValue.matches("[0-9]+|[0-9]+\\.?[0-9]+"))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"Invalid Value", errorMsg);
					}
					else
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"", errorMsg);
					}
					break;
				}
				case "DaysRequiredToManufactureFromBindingPurchase":
				{
					if ("".equals(elementValue))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"This field is required", errorMsg);
					}
					else if(!elementValue.matches("[0-9]+|[0-9]+\\.?[0-9]+"))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"Invalid Value", errorMsg);
					}
					else
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"", errorMsg);
					}
					break;
				}
				case "DaysToDeliver":
				{
					scrollToElement(replaceGui(guiMap.get(htmlToValidate), elementName));
					if ("".equals(elementValue))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"This field is required", errorMsg);
					}
					else if(!elementValue.matches("[0-9]+|[0-9]+\\.?[0-9]+"))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"Invalid Value", errorMsg);
					}
					else
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"", errorMsg);
					}
					break;
				}
				default:
					break;
			}
		}//for
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
		clickElementJs(replaceGui(guiMap.get("tabName"), "Step 5") );
		holdSeconds(1);
		String errorMsg, elementValue, elementName;
		String htmlToValidate="RE_StepFiveElementDiv";
		scrollToElement(replaceGui(guiMap.get(htmlToValidate), "PhoneNumber"));
		for (HashMap.Entry<String, String> entry : row.entrySet()) 
		{
			elementName = entry.getKey();
			elementValue = entry.getValue();
			if(elementName.equalsIgnoreCase("Scenarios") || elementName.equalsIgnoreCase("Active"))
			continue;
			errorMsg = getElementAttribute(replaceGui(guiMap.get("RE_StepFiveElementMessage"), elementName), "text");
			switch(elementName)
			{
				case "CompanyName": 
				{
					if("".equals(elementValue))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
							"The Company Name field is required.", errorMsg);
					}
					else
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"", errorMsg);
					}
					break;
				}
				case "AuthOfficialName":
				{
					if("".equals(elementValue))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
							"The Name of Authorizing Official field is required.", errorMsg);
					}
					else
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"", errorMsg);
					}
					break;
				}
				case "AuthOfficialTitle":  
				{
					if("".equals(elementValue))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
							"The Title of Authorizing Official field is required.", errorMsg);
					}
					else
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"", errorMsg);
					}
					break;
				}
				case "PhoneNumber":  
				{
					if ("".equals(elementValue))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"The Phone Number field is required.", errorMsg);
					}
					else if(!elementValue.matches("[0-9]+"))
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
				case "AuthOfficialEmail": 
				{
					if ("".equals(elementValue) || elementValue.matches("^(.+)@(.+)$"))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"", errorMsg);
					}
					else
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
							"The Email of Authorizing Official (Email address will be used for "
							+ "submission and withdrawal verification!) field is not a valid e-mail address.", errorMsg);
					}
					break;
				}
				case "POCPhoneNumber":  
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
				case "POCEmail": 
				{
					if(!elementValue.matches("^(.+)@(.+)$") && !"".equals(elementValue))
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
							"The E-mail Address field is not a valid e-mail address.", errorMsg);
					}
					else
					{
						matches = matches & validateElementAndReport(elementName, htmlToValidate, 
								"", errorMsg);
					}
					break;
				}
				default:
					break;
			}
		}//for
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
	 * @param MinOrMax MIN or MAX
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
	 * This method search for Item
	 * @param reqId: request exclusive Identifier
	 * @exception Exception
	*/
	public static void searchExclusiveRequestById(String reqId) throws Exception
	{
		enterText(guiMap.get("SearchId"), reqId);
		enterText(guiMap.get("SearchComp"), "");
		holdSeconds(2);
		int currentTimeOut = setBrowserTimeOut(3);
		if(checkElementExists(replaceGui(guiMap.get("tdSearchedId"),reqId)) )
		{
			setBrowserTimeOut(currentTimeOut);
			holdSeconds(2);
			String statusReq = getElementAttribute(replaceGui(guiMap.get("tdSearchedStatus"),reqId), "text");
			if(statusReq.equalsIgnoreCase("Pending-Rebuttal Window Open"))
			{
				highlightElement(replaceGui(guiMap.get("tdSearchedStatus"),reqId), "green");
				updateHtmlReport("Search for exclusive request", "Exclusive request was found with the right status", 
						"As expectedd", "Step", "pass", "Search for exclusive request");
				clickElementJs(replaceGui(guiMap.get("tdSearchedDetail"),reqId));
				scrollToTheBottomOfPage();
				clickElementJs(guiMap.get("RE_details"));
				scrollToTheBottomOfPage();
				clickElementJs(guiMap.get("createObjectionRebuttal"));
				holdSeconds(3);
			}
			else
			{
				highlightElement(replaceGui(guiMap.get("tdSearchedStatus"),reqId), "red");
				failTestCase("Search for exclusive request: "+reqId, "The request is at 'Pending-Rebuttal Window Open'", 
						"Status of the request is '"+statusReq+"'", "Step", 
						"fail", "Search for exclusive request");
			}
		}
		else
		{
			setBrowserTimeOut(currentTimeOut);
			failTestCase("Search for exclusive request: "+reqId, "Exclusive request should be found",
					"Not as expected", "Step", "fail", "Search for exclusive request");
		}
	}
}
