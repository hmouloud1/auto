/**
 * MilCorp
 * Mouloud Hamdidouche
 * October, 2019
*/
package libs;

import static GuiLibs.GuiTools.checkElementExists;
import static GuiLibs.GuiTools.failTestSuite;
import static GuiLibs.GuiTools.guiMap;
import static GuiLibs.GuiTools.highlightElement;
import static GuiLibs.GuiTools.unHighlightElement;
import static GuiLibs.GuiTools.holdSeconds;
import static GuiLibs.GuiTools.navigateTo;
import static GuiLibs.GuiTools.replaceGui;
import static GuiLibs.GuiTools.setBrowserTimeOut;
import static GuiLibs.GuiTools.updateHtmlReport;
import static GuiLibs.GuiTools.clickElementJs;
import static GuiLibs.GuiTools.enterText;
import static GuiLibs.GuiTools.getElementAttribute;
import static GuiLibs.GuiTools.elementExists;
import static GuiLibs.GuiTools.holdSeconds;
import static GuiLibs.GuiTools.selectElementByText;
import static GuiLibs.GuiTools.scrollToElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import javax.swing.JOptionPane;
public class UserManagementLib{
	public UserManagementLib() throws IOException {
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
	public static boolean loginUserManagement(String url, 
									   String user, 
									   String password) throws Exception
	{
		boolean loginStatus = true;
		navigateTo(url);
		int currentTimeOut = setBrowserTimeOut(3);
		
		//clickElementJs(guiMap.get("Agreement"));
		JOptionPane.showMessageDialog(null,
			    "Please login manually and click 'OK'");
		holdSeconds(1);
		//clickElementJs(guiMap.get("loginTo"));
	//	holdSeconds(5);
		if(! checkElementExists(guiMap.get("HomePage")))
		{
			failTestSuite("Login to User Management App with user "+user, 
					"User is able to login", 
				"Not as expected", "Step", "fail", "Login failed");
			loginStatus = false;
		}else
		{
			highlightElement(guiMap.get("HomePage"), "green");
			holdSeconds(2);
			updateHtmlReport("Login to User Management App with user "+user,  "User is able to login", "As expected", 
					"Step", "pass", "Login to User Management application");
			clickElementJs(guiMap.get("MainMenu"));
			clickElementJs(guiMap.get("AddUser"));
		}
		return loginStatus;
	}
	
	public static boolean validateFiledsErrors(String fieldName, 
			ArrayList<LinkedHashMap<String, String>> vlaues) throws Exception
	{
		boolean match = true;
		String fieldValue, expectedError, id, actualError;
		System.out.println(fieldName + "-----------" );
		for(LinkedHashMap<String, String> map : vlaues)
		{
			
			fieldValue = map.get("Given_Value");			                    
			id = map.get("Id");
			actualError = "";
			expectedError = map.get("Expected_Error");
			enterText(replaceGui(guiMap.get("UserField"), fieldName), fieldValue);
			enterText(replaceGui(guiMap.get("UserField"), "Suffix"), "");
			holdSeconds(2);
			//clickElementJs(replaceGui(guiMap.get("UserField"), "Suffix")); 
			//holdSeconds(1);
			if (expectedError.trim().equals("") || expectedError.trim().equalsIgnoreCase("Accepted"))
			{
				int currentWait = setBrowserTimeOut(3);
				if(! elementExists(replaceGui(guiMap.get("ErrorMessage"), fieldName)))
				{ 
					highlightElement(replaceGui(guiMap.get("UserField"), fieldName), "green");
					updateHtmlReport("Validate "+fieldName+ ", Value = ["+fieldValue+"]", "No error should be displaying",
							"displayed error ["+actualError+"]", 
							"VP", "pass", "Validate "+fieldName+" - "+id);
					unHighlightElement(replaceGui(guiMap.get("UserField"), fieldName));
				}
				else
				{
					match = false;
					highlightElement(replaceGui(guiMap.get("UserField"), fieldName), "red");
					updateHtmlReport("Validate "+fieldName+ ", Value = ["+fieldValue+"]", "No error should be displaying", 
							"displayed error ["+actualError+"]", 
							"VP", "fail", "Validate "+fieldName+"-"+id);
					unHighlightElement(replaceGui(guiMap.get("UserField"), fieldName));
					
				}
				setBrowserTimeOut(currentWait);
			}	
			else
			{
				if(elementExists(replaceGui(guiMap.get("ErrorMessage"), fieldName)))
				{ 
					actualError = getElementAttribute(replaceGui(guiMap.get("ErrorMessage"), fieldName), "text");
					if(actualError.trim().equalsIgnoreCase(expectedError.trim()))
					{
						highlightElement(replaceGui(guiMap.get("ErrorMessage"), fieldName), "green");
						updateHtmlReport("Validate "+fieldName+ ", Value = ["+fieldValue+"]", "Expected error ["+expectedError+"]",
								"displayed error ["+actualError+"]", 
								"VP", "pass", "Validate "+fieldName+" - "+id );
						unHighlightElement(replaceGui(guiMap.get("ErrorMessage"), fieldName));
					}
					else
					{
						match = false;
						highlightElement(replaceGui(guiMap.get("ErrorMessage"), fieldName), "red");
						updateHtmlReport("Validate "+fieldName+ ", Value = ["+fieldValue+"]", "Expected error ["+expectedError+"]", 
								"displayed error ["+actualError+"]", 
								"VP", "fail", "Validate "+fieldName+" - "+id );
						unHighlightElement(replaceGui(guiMap.get("ErrorMessage"), fieldName));
					}
				}
				else
				{
					match = false;
					highlightElement(replaceGui(guiMap.get("UserField"), fieldName), "red");
					updateHtmlReport("Validate "+fieldName+ ", Value = ["+fieldValue+"]", "Expected error ["+expectedError+"]", 
							"displayed error ["+actualError+"]", 
							"VP", "fail", "Validate "+fieldName+" - "+id );
					unHighlightElement(replaceGui(guiMap.get("UserField"), fieldName));
					
				}
			}
		}
		return match;
	}
	
	
	public static boolean validateEmployeeType(String fieldName, 
			ArrayList<LinkedHashMap<String, String>> vlaues) throws Exception
	{
		boolean match = true;
		String fieldValue, expectedError, id, actualError;
		System.out.println(fieldName + "-----------" );
		for(LinkedHashMap<String, String> map : vlaues)
		{
			
			fieldValue = map.get("Given_Value");			                    
			id = map.get("Id");
			actualError = "";
			expectedError = map.get("Expected_Error");
			if(fieldValue.equalsIgnoreCase(""))
			{
				clickElementJs(replaceGui(guiMap.get("UserField"), fieldName));
				clickElementJs(replaceGui(guiMap.get("UserField"), "Fax"));
				if(elementExists(replaceGui(guiMap.get("ErrorMessageSelect"), fieldName)))
				{ 
					actualError = getElementAttribute(replaceGui(guiMap.get("ErrorMessageSelect"), fieldName), "text");
					if(actualError.trim().equalsIgnoreCase(expectedError.trim()))
					{
						highlightElement(replaceGui(guiMap.get("ErrorMessageSelect"), fieldName), "green");
						updateHtmlReport("Validate "+fieldName+ ", Value = ["+fieldValue+"]", "Expected error ["+expectedError+"]",
								"displayed error ["+actualError+"]", 
								"VP", "pass", "Validate "+fieldName+" - "+id );
						unHighlightElement(replaceGui(guiMap.get("ErrorMessageSelect"), fieldName));
					}
					else
					{
						match = false;
						highlightElement(replaceGui(guiMap.get("ErrorMessageSelect"), fieldName), "red");
						updateHtmlReport("Validate "+fieldName+ ", Value = ["+fieldValue+"]", "Expected error ["+expectedError+"]", 
								"displayed error ["+actualError+"]", 
								"VP", "fail", "Validate "+fieldName+" - "+id );
						unHighlightElement(replaceGui(guiMap.get("ErrorMessageSelect"), fieldName));
					}
				}
				else
				{
					match = false;
					highlightElement(replaceGui(guiMap.get("UserField"), fieldName), "red");
					updateHtmlReport("Validate "+fieldName+ ", Value = ["+fieldValue+"]", "Expected error ["+expectedError+"]", 
							"displayed error ["+actualError+"]", 
							"VP", "fail", "Validate "+fieldName+" - "+id );
					unHighlightElement(replaceGui(guiMap.get("UserField"), fieldName));
				}
			}
			else
			{
				//selectElementByText(replaceGui(guiMap.get("UserField"), fieldName), fieldValue);
				clickElementJs(replaceGui(guiMap.get("UserField"), fieldName));
				clickElementJs(replaceGui(guiMap.get("EmployeeType"), fieldValue));
				holdSeconds(1);
				int currentWait = setBrowserTimeOut(3);
				if(! elementExists(replaceGui(guiMap.get("ErrorMessageSelect"), fieldName)))
				{ 
					highlightElement(replaceGui(guiMap.get("UserField"), fieldName), "green");
					updateHtmlReport("Validate "+fieldName+ ", Value = ["+fieldValue+"]", "No error should be displaying",
							"displayed error ["+actualError+"]", 
							"VP", "pass", "Validate "+fieldName+" - "+id);
					unHighlightElement(replaceGui(guiMap.get("UserField"), fieldName));
				}
				else
				{
					match = false;
					highlightElement(replaceGui(guiMap.get("ErrorMessageSelect"), fieldName), "red");
					updateHtmlReport("Validate "+fieldName+ ", Value = ["+fieldValue+"]", "No error should be displaying", 
							"displayed error ["+actualError+"]", 
							"VP", "fail", "Validate "+fieldName+"-"+id);
					unHighlightElement(replaceGui(guiMap.get("UserField"), fieldName));
					
				}
				setBrowserTimeOut(currentWait);
				
			}
			
		}
		return match;
	}
	
	
	
	
	public static boolean validateOffice(String fieldName, 
			ArrayList<LinkedHashMap<String, String>> vlaues) throws Exception
	{
		boolean match = true;
		String fieldValue, expectedError, id, actualError;
		System.out.println(fieldName + "----w2--4-h----" );
		String oldVal = "";
		scrollToElement(guiMap.get("SaveForLater"));
		for(LinkedHashMap<String, String> map : vlaues)
		{
			fieldValue = map.get("Given_Value");			                    
			id = map.get("Id");
			actualError = "";
			expectedError = map.get("Expected_Error");			
			if(!fieldValue.equals(""))
			{
				clickElementJs(replaceGui(guiMap.get("EmployeeOfficeClick"), fieldName));
				holdSeconds(1);
				clickElementJs(replaceGui(guiMap.get("EmployeeOffice"), fieldValue));
				
			}
			else
			{
				clickElementJs(replaceGui(guiMap.get("EmployeeOfficeDelete"), oldVal));
				holdSeconds(1);
			}
			int currentWait = setBrowserTimeOut(3);
			actualError = getElementAttribute(replaceGui(guiMap.get("EmployeeOfficeError"), fieldName), "text");
			if(actualError.equals(actualError))
			{ 
				highlightElement(replaceGui(guiMap.get("EmployeeOfficeError"), fieldName), "green");
				updateHtmlReport("Validate "+fieldName+ ", Value = ["+fieldValue+"]", "No error should be displaying",
						"displayed error ["+actualError+"]", 
						"VP", "pass", "Validate "+fieldName+" - "+id);
				unHighlightElement(replaceGui(guiMap.get("UserField"), fieldName));
			}
			else
			{
				match = false;
				highlightElement(replaceGui(guiMap.get("EmployeeOfficeError"), fieldName), "red");
				updateHtmlReport("Validate "+fieldName+ ", Value = ["+fieldValue+"]", "No error should be displaying", 
						"displayed error ["+actualError+"]", 
						"VP", "fail", "Validate "+fieldName+"-"+id);
				unHighlightElement(replaceGui(guiMap.get("UserField"), fieldName));
			} 
			setBrowserTimeOut(currentWait);
			oldVal = fieldValue;
		}
		return match;
	}
	
	public static boolean validateManager(String fieldName, 
			ArrayList<LinkedHashMap<String, String>> vlaues) throws Exception
	{
		boolean match = true;
		String fieldValue, expectedError, id, actualError;
		System.out.println(fieldName + "----2-------" );
		String oldVal = "";
		scrollToElement(guiMap.get("SaveForLater"));
		for(LinkedHashMap<String, String> map : vlaues)
		{
			fieldValue = map.get("Given_Value");			                    
			id = map.get("Id");
			actualError = "";
			expectedError = map.get("Expected_Error");
			
			if(!fieldValue.equals(""))
			{
				enterText(replaceGui(guiMap.get("UserField"), fieldName), fieldValue);
				holdSeconds(1);
				clickElementJs(replaceGui(guiMap.get("UserField"), "Office"));
				holdSeconds(2);
				int currentWait = setBrowserTimeOut(3);
				if(!elementExists(replaceGui(guiMap.get("EmployeeManagerError"), fieldName)))
				{ 
					highlightElement(replaceGui(guiMap.get("UserField"), fieldName), "green");
					updateHtmlReport("Validate "+fieldName+ ", Value = ["+fieldValue+"]", "No error should be displaying",
							"displayed error ["+actualError+"]", 
							"VP", "pass", "Validate "+fieldName+" - "+id);
					unHighlightElement(replaceGui(guiMap.get("UserField"), fieldName));
				}
				else
				{

					match = false;
					highlightElement(replaceGui(guiMap.get("EmployeeManagerError"), fieldName), "red");
					updateHtmlReport("Validate "+fieldName+ ", Value = ["+fieldValue+"]", "No error should be displaying", 
							"displayed error ["+actualError+"]", 
							"VP", "fail", "Validate "+fieldName+"-"+id);
					unHighlightElement(replaceGui(guiMap.get("UserField"), fieldName));
				} 
				setBrowserTimeOut(currentWait);
			}
			else
			{
				clickElementJs(guiMap.get("EmployeeManagerDelete"));
				if(elementExists(replaceGui(guiMap.get("EmployeeManagerError"), fieldName)))
				{ 
					actualError = getElementAttribute(replaceGui(guiMap.get("EmployeeManagerError"), fieldName), "text");
					if(actualError.trim().equalsIgnoreCase(expectedError.trim()))
					{
						highlightElement(replaceGui(guiMap.get("EmployeeManagerError"), fieldName), "green");
						updateHtmlReport("Validate "+fieldName+ ", Value = ["+fieldValue+"]", "Expected error ["+expectedError+"]",
								"displayed error ["+actualError+"]", 
								"VP", "pass", "Validate "+fieldName+" - "+id );
						unHighlightElement(replaceGui(guiMap.get("EmployeeManagerError"), fieldName));
					}
					else
					{
						match = false;
						highlightElement(replaceGui(guiMap.get("EmployeeManagerError"), fieldName), "red");
						updateHtmlReport("Validate "+fieldName+ ", Value = ["+fieldValue+"]", "Expected error ["+expectedError+"]", 
								"displayed error ["+actualError+"]", 
								"VP", "fail", "Validate "+fieldName+" - "+id );
						unHighlightElement(replaceGui(guiMap.get("EmployeeManagerError"), fieldName));
					}
				}
				else
				{
					match = false;
					highlightElement(replaceGui(guiMap.get("UserField"), fieldName), "red");
					updateHtmlReport("Validate "+fieldName+ ", Value = ["+fieldValue+"]", "Expected error ["+expectedError+"]", 
							"displayed error ["+actualError+"]", 
							"VP", "fail", "Validate "+fieldName+" - "+id );
					unHighlightElement(replaceGui(guiMap.get("UserField"), fieldName));
				}
			}
			oldVal = fieldValue;
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
