/**
 * MilCorp
 * Mouloud Hamdidouche
 * December, 2018
*/
package GuiLibs;
import static ReportLibs.ReportTools.printLog;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.monte.screenrecorder.ScreenRecorder;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import InitLibs.InitTools;
import ReportLibs.HtmlReport;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
public class GuiTools extends InitTools{
	public GuiTools() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}
	private static WebDriver driver;
	public static String browserProcessId;
	private String currentTestCaseName;
	private String browserName;
	private long threadId;
	public static LinkedHashMap<String, LinkedHashMap<String, String>> guiMap;
	public static int imgIterator = 1;
	public static boolean tearDown = false, testCaseStatus=true;
	public static boolean recordOn = false;
	public static ScreenRecorder screenRecorder;
	/**
	 * This function will start recording
	 * @param folder: where the video will be saved
	 * @param fileName: the name of the video file
	 * @param voice: with o without voice
	 * @throws Exception
	 */
	/*public static void startRecording(String folder, String fileName, boolean voice) throws Exception
	{
		recordOn = true;
		File file = new File(folder + "/"+ fileName);

		 // set the graphics configuration
	    GraphicsConfiguration gc = GraphicsEnvironment
        .getLocalGraphicsEnvironment()
        .getDefaultScreenDevice()
        .getDefaultConfiguration();


		screenRecorder = new ScreenRecorder(gc,null,
		new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
		new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
		CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
		DepthKey, 24, FrameRateKey, Rational.valueOf(15),
		QualityKey, 1.0f,
		KeyFrameIntervalKey, 15 * 60),
		new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black",
		FrameRateKey, Rational.valueOf(30)),
		null, file);
		screenRecorder.start();
	    
	}
	*//**
	 * This function will stop recording
	 * @param folder: where the video will be saved
	 * @param fileName: the name of the video file
	 * @param voice: with o without voice
	 * @throws Exception
	 *//*
	public static void stopRecording() throws Exception
	{
		recordOn  = false;
		screenRecorder.stop();
	}*/
	/**
	 * This function will open browser
	 * @param browser: browser type
	 * @throws Exception
	 */
	public void openBrowser(String browser) throws Exception
	{
		String browserExe = "chrome.exe";
		if(browser.equalsIgnoreCase("ie"))
		{
			browserExe = "iexplore.exe";
			killProcesses(getAllProcessIds(browserExe));
			killProcesses(getAllProcessIds("IEDriverServer.exe"));
			setDriver(getIeDriver());
			//getDriver().findElement(By.tagName("html")).sendKeys(Keys.chord(Keys.CONTROL, "0"));
		}
		else if (browser.equalsIgnoreCase("firefox"))
		{
			browserExe = "firefox.exe";
			killProcesses(getAllProcessIds(browserExe));
			killProcesses(getAllProcessIds("geckodriver.exe"));
			setDriver(getFirefoxDriver());
		}
		else if (browser.equalsIgnoreCase("chrome"))
		{
			killProcesses(getAllProcessIds(browserExe));
			killProcesses(getAllProcessIds("chromedriver.exe"));
			setDriver(getChromeDriver());
		}
		else
		{
			printLog("The browser name should be ie, firefox, "
					+ "chrome in settings.txt file");
		}
		setBrowserTimeOut(20);
		//setUpBrowserProcessId(browserExe);
		getDriver().manage().window().setPosition(new Point(0,0));
		getDriver().manage().window().maximize();
	}
	/**
	 * This function set browser timeout
	 * @param browserTimeOut: time for wait, in seconds
	*/
	public static int setBrowserTimeOut(int browserTimeOut)
	{
		getDriver().manage().timeouts().implicitlyWait(browserTimeOut,
				TimeUnit.SECONDS);
		int currentTimeOut = setTimeOut(browserTimeOut);
		return currentTimeOut;
	}
	/**
	 * This function set browser process ID
	 * @param browserExe: browser type
	 * @throws Exception
	*/
	public static void setUpBrowserProcessId(String browserExe) throws Exception
	{
		String title = "New Browser[" +new Random().nextInt(100)+ "]";
		executeScript("document.title = \"" +title+ "\";"); 
		try
		{
			setBrowserProcessId(getNewProcessId(browserExe, title));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * This function get browser process ID
	 * @param browserType: browser type
	 * @param title: Browser title
	 * @return process ID
	*/
	public static String getNewProcessId(String browserType, String title)
	{
		try {
			String str, commandeLine = "tasklist /v /fo csv | findstr /i \""+ browserType+"\"";
	         System.out.println("Creating Process...");
	         Process p = Runtime.getRuntime().exec("cmd /c "+commandeLine);
	         InputStream in = p.getInputStream();
	         BufferedReader br = new BufferedReader(new InputStreamReader(in));
	         while((str = br.readLine())!=null)
	         {
	        	 printLog(str);
	        	 if(str.contains(title))
	        	 {return str.split(",")[1].replaceAll("\"", "");}
	         }
	         p.destroy();
	      } catch (Exception ex) {
	         ex.printStackTrace();
	      }
		return title;
	}
	/**
	 * This function get all browser process IDs
	 * @param browserType: browser type
	 * @return list of process IDs
	 */
	public static ArrayList<String> getAllProcessIds(String browserType)
	{
		ArrayList<String> processes = new ArrayList<String>() ;
		try {
	         // create a new process
			 String str, commandeLine = "tasklist /v /fo csv | findstr /i \""+ browserType+"\"";
	         System.out.println("Creating Process...");
	         Process p = Runtime.getRuntime().exec("cmd /c "+commandeLine);
	         InputStream in = p.getInputStream();
	         BufferedReader br = new BufferedReader(new InputStreamReader(in));
	         while((str = br.readLine())!=null)
	         {
	        	printLog(str);
	        	processes.add(str.split(",")[1].replaceAll("\"", ""));
	         }
	         p.destroy();
	      } catch (Exception ex) {
	         ex.printStackTrace();
	      }
		return processes;
	}
	/**
	 * This function execute script
	 * @param script: script name
	 * @return Object Js object
	 */
	public static Object executeScript(String script) throws Exception
	{
		JavascriptExecutor js = (JavascriptExecutor) getDriver();
		String stringScript = " try { "+script+ ";} catch(error) {"
				+ "var stringerror = 'Js exception: '+ error.message; "
				+ "return stringerror}";
		Object obj  = js.executeScript(stringScript);
		if(obj instanceof String)
		{
			if(((String) obj).contains("Js exception: ") )
			{
				throw new Exception((String ) obj);
			}
		}
		return obj;
	}
	/**
	 * This function closes browser
	 * @exception: IOException
	 */
	public void closeBrowser() throws IOException
	{
		for(String window : getAllWindows())
		{
			switchToWindow(window);
			closeCurrentWindow();
		}
		System.out.println(getBrowserProcessId());
		getDriver().close();
		getDriver().quit();
		Runtime.getRuntime().exec("cmd /c taskkill /PID " + 
		getBrowserProcessId());
	}
	/**
	 * This function kills process
	 * @param listProcesses: list of processes
	 * @exception: IOException
	 */
	public void killProcesses(ArrayList<String> listProcesses) throws IOException
	{
		if(listProcesses!=null)
		{
			for(String pId : listProcesses)
			{
				System.out.println(pId);
				Runtime.getRuntime().exec("cmd /c taskkill /F /PID " +pId);
			}
		}
	}
	/**
	 * This function gets IE driver
	 * @return IE webdriver
	 */
	@SuppressWarnings("deprecation")
	public WebDriver getIeDriver()
	{
		printLog("Get IE Driver");
		System.setProperty("webdriver.ie.driver", getLibFolder()+"/IEDriverServer.exe");
		DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
		capabilities.setCapability("EnableNativeEvents", false);
		capabilities.setCapability("ignoreZoomSetting", true);
		capabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
		capabilities.setCapability(InternetExplorerDriver.
				INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		capabilities.setCapability("requireWindowFocus", true);
		return new InternetExplorerDriver(capabilities);
	}
	/**
	 * This function gets Firefox driver
	 * @return Firefox webdriver
	 */
	public WebDriver getFirefoxDriver()
	{
	/*	DesiredCapabilities capabilities = DesiredCapabilities.firefox();
		FirefoxProfile firefoxProfile = new FirefoxProfile();
		firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", 
				"application/octet-stream");
		capabilities.setCapability(FirefoxDriver.PROFILE, firefoxProfile);
		return new FirefoxDriver(capabilities);*/
		System.out.println(getLibFolder());
		System.setProperty("WebDriver.gecko.driver",getLibFolder()+"/geckodriver.exe");
		return new FirefoxDriver();
		//geckodriver.exe
	}
	/**
	 * This function gets Chrome driver
	 * @return chrome webdriver
	 */
	public WebDriver getChromeDriver()
	{
		String osName = System.getProperty("os.name").toLowerCase();
		String userDir = System.getProperty("user.dir");
		System.out.println("userDir "+userDir);
		String Chrome_Profile_Path = "";
		if(!osName.contains("mac"))
		{
			System.out.println("this a windows machine");
			System.out.println(getLibFolder());
			System.setProperty("webdriver.chrome.driver", userDir+"/libs/chromedriver.exe");
			 Chrome_Profile_Path = "C:/Users/"+getTesterName()+"/AppData/Local/Google/Chrome/User Data";
		}
		else
		{
			System.out.println("this a mac machine");
			System.out.println(getLibFolder());
			System.setProperty("webdriver.chrome.driver", getLibFolder()+"/libs/chromedrive");
			 Chrome_Profile_Path = "/Users/"+getTesterName()+"/Library/Application Support/Google";
		}
		/* Storing the Chrome Profile Path in Chrome_Profile_Path variable. */
		/* Creating an instance of ChromeOptions (i.e objChrome_Profile) */
		ChromeOptions Chrome_Profile = new ChromeOptions();
		/* Disabling the chrome browser extensions */
		Chrome_Profile.addArguments("chrome.switches","--disable-extensions"); 
		/* Adding Chrome profile by .addArguments to objChrome_Profile  */
		Chrome_Profile.addArguments("user-data-dir=" + Chrome_Profile_Path);
		/*Initializing the Webdriver instance (i.e. driver) to open Chrome Browser and passing the Chrome Profile as argument */
		//driver = new ChromeDriver(Chrome_Profile);
		return new ChromeDriver(Chrome_Profile);
		//System.setProperty("webdriver.chrome.driver", getLibFolder()+"/chromedriver.exe");
		//DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		//capabilities.setBrowserName("chrome");
		//return new ChromeDriver(capabilities);
	}
	
	/**
	 * This method fail test case
	 * @param stepDesc, Step description
	 * @param stepExpectResult, Step expected result
	 * @param stepActualResult, Step actual result
	 * @param StepVpStep, Verification point or step
	 * @param StepPassFail, always fail
	 * @param msgError, Error message, and screen shot name
	 */
	public static void failTestCase(String stepDesc, 
									String stepExpectResult, 
									String stepActualResult, 
									String StepVpStep, 
									String StepPassFail,
									String msgError) throws IOException
	{
		//HtmlReport.setTcStatus(false);
		testCaseStatus = false;
		String aLink = "";
		if(!msgError.equals(""))
		{
			String screenShotPath = takeScreenShot(msgError, true);
			aLink = "<a href = '"+screenShotPath+"'>"+msgError+"</a>";
		}
		
		HtmlReport.addHtmlStep(stepDesc, stepExpectResult, stepActualResult,
							   StepVpStep, StepPassFail, aLink);
		Assert.fail( getTestCaseName()+ ": " +msgError);
	}
	
	/**
	 * This method fail test suite
	 * @param stepDesc, Step description
	 * @param stepExpectResult, Step expected result
	 * @param stepActualResult, Step actual result
	 * @param StepVpStep, Verification point or step
	 * @param StepPassFail, always fail
	 * @param msgError, Error message, and screen shot name
	 */
	public static void failTestSuite(String stepDesc, 
									 String stepExpectResult, 
									 String stepActualResult, 
									 String StepVpStep, 
									 String StepPassFail,
									 String msgError) throws IOException
	{
		String aLink = "";
		testCaseStatus = false;
		if(!msgError.equals(""))
		{
			String screenShotPath = takeScreenShot(msgError, true);
			aLink = "<a href = '"+screenShotPath+"'>"+msgError+"</a>";
		}
		HtmlReport.addHtmlStep(stepDesc, stepExpectResult, stepActualResult, 
				StepVpStep, StepPassFail, aLink);
		tearDown = true;
		Assert.fail( getTestCaseName()+ ": " +msgError);
	}
	
	/**
	 * This function gets chrome driver
	 */
	public WebDriver getChromeDriverOff()
	{
		String osName = System.getProperty("os.name").toLowerCase();
		String userDir = System.getProperty("user.dir");
		System.out.println("userDir "+userDir);
		if(!osName.contains("mac"))
		{
			System.out.println(getLibFolder());
			System.setProperty("webdriver.chrome.driver", userDir+"/libs/chromedriver.exe");
		}
		else
		{
			System.out.println(getLibFolder());
			System.setProperty("webdriver.chrome.driver", userDir+"/libs/chromedrive");
		}
		/* Storing the Chrome Profile Path in Chrome_Profile_Path variable. */
		//String Chrome_Profile_Path = "C:/Users/"+getTest/Name()+"/AppData/Local/Google/Chrome/AutomationProfile";
		/* Creating an instance of ChromeOptions (i.e objChrome_Profile) */
		ChromeOptions Chrome_Profile = new ChromeOptions();
		/* Disabling the chrome browser extensions */
		Chrome_Profile.addArguments("chrome.switches","--disable-extensions"); 
		/* Adding Chrome profile by .addArguments to objChrome_Profile  */
		//Chrome_Profile.addArguments("user-data-dir=" + Chrome_Profile_Path);
		/*Initializing the Webdriver instance (i.e. driver) to open Chrome Browser and passing the Chrome Profile as argument */
		//driver = new ChromeDriver(Chrome_Profile);
		return new ChromeDriver(Chrome_Profile);
		//System.setProperty("webdriver.chrome.driver", getLibFolder()+"/chromedriver.exe");
		//DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		//capabilities.setBrowserName("chrome");
		//return new ChromeDriver(capabilities);
	}
	
	/**
	 * This method takes screenshot
	 * @param ssName: screenshot's name
	 * @param fullPage, full page or visible part
	 * @return string containing the path to the screen shot 
	 * @throws IOException 
	 * 
	 */
	public static String takeScreenShot(String ssName, boolean fullPage) 
			throws IOException
	{
		String ssPath;
		if(fullPage)
		{
			ssPath =  takeScreenShotAllPage(getDriver(), 
			   getOutputResultFolder()+"/html", ssName);
		}else
		{
			ssPath =  takeVisibleScreenShot(getDriver(), 
					   getOutputResultFolder()+"/html", ssName);
		}
		//HtmlReport.addLinkStepToHtmlReport(ssName, ssDescription, ssPath);
		return ssPath;
	}
	
	/**
	 * This method takes screenshot of all page
	 * @param driver: driver
	 * @param ssPath: screenshot's path
	 * @param ssName: screenshot name
	 * @return string containing the path to the screen shot
	 * @throws IOException 
	 * 
	 */
	public static String takeScreenShotAllPage(WebDriver driver, 
											   String ssPath, 
											   String ssName) 
											throws IOException
	{
		
		String file = ssPath+"/"+ssName+imgIterator+".png";
		imgIterator=imgIterator+1;
		Screenshot screenshot = new AShot().
				shootingStrategy(ShootingStrategies.viewportPasting(100)).
				takeScreenshot(driver);
		ImageIO.write(screenshot.getImage(),"PNG",new File(file));
		return file;
	}
	
	/**
	 * This method takes screenshot of the visible part
	 * @param driver: driver
	 * @param ssPath: screenshot's path
	 * @param ssName: screenshot name
	 * @return string containing the path to the screen shot
	 * @throws IOException 
	 * 
	 */
	public static String takeVisibleScreenShot(WebDriver driver, 
								 			   String ssPath, 
								 			   String ssName) 
								 			   throws IOException
	{
		File source = null;
		try{
		TakesScreenshot takeScreenShot = (TakesScreenshot) driver;
		source =  takeScreenShot.getScreenshotAs(OutputType.FILE);
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		String file = ssPath+"/"+ssName+imgIterator+".png";
		imgIterator=imgIterator+1;
		FileUtils.copyFile(source, new File(file));
		return file;
	}
	
	/**
	 * This function navigate to Given url
	 * @param url: URL to navigate to to the page
	*/
	public static void navigateTo(String url)
	{
		WebDriver dr = getDriver();
		//dr.navigate()
		dr.navigate().to(url);
	}
	/**
	 * This function get all the opened windows
	 * @return set of window handles
	 */
	Set<String> getAllWindows()
	{
		return getDriver().getWindowHandles();
	}
	/**
	 * This function switch windows
	 */
	void switchToWindow(String window)
	{
		getDriver().switchTo().window(window);
	}
	/**
	 * This method close current window
	 */
	void closeCurrentWindow()
	{
		if(getDriver()!=null)
		{
			getDriver().close();
		}
	}
	
	/**
	 * This method takes screen shot of the visible part
	 * @param ssName: screenshot's name
	 * @param ssDescription: screenshot's description
	 * @throws IOException 
	 * 
	 */
	/*public static String takePrintScreen(String ssName, String ssDescription) 
			throws IOException
	{
		String ssPath = takeVisibleScreenShot(getDriver(), 
				getOutputResultFolder()+"/html", ssName);
		HtmlReport.addLinkStepToHtmlReport(ssName, ssDescription, ssPath);
		return ssPath;
	}*/
	
	/**
	 * This method checks if element exists
	 * @param map: Web Element
	 * @return true if element exist, false if not
	 * @throws Exception 
	 * 
	 */
	public static boolean checkElementExists(LinkedHashMap<String, String> map) throws Exception
	{
		return checkElementExists(map.get("locator_type"), map.get("locator_value"));
	}
	
	/**
	 * This method checks if element Visible
	 * @param map: Web Element
	 * @return true if element visible, false if not
	 * @throws Exception 
	 * 
	 */
	public static boolean checkElementVisible(LinkedHashMap<String, String> map) throws Exception
	{
		return checkElementVisible(map.get("locator_type"), map.get("locator_value"));
	}
	
	/**
	 * This method checks if element Visible
	 * @param map: Web Element
	 * @return true if element visible, false if not
	 * @throws Exception 
	 * 
	 */
	public static boolean checkElementVisible(String locType, String locValue) throws Exception
	{
		boolean visible;
		try {
		   if(getDriver().findElement(byType(locType, locValue)).isEnabled())
		   visible = true;
		   else visible = false;
		} catch (NoSuchElementException e) {
			visible = false;
			e.printStackTrace();
		   
		}
		return visible;
	}
	
	/**
	 * This method checks if element Enabled
	 * @param map: Web Element
	 * @return true if element visible, false if not
	 * @throws Exception 
	 * 
	 */
	public static boolean checkElementIsEnabled(LinkedHashMap<String, String> map) throws Exception
	{
		return checkElementIsEnabled(map.get("locator_type"), map.get("locator_value"));
	}
	
	/**
	 * This method checks if element Enabled
	 * @param map: Web Element
	 * @return true if element visible, false if not
	 * @throws Exception 
	 * 
	 */
	public static boolean checkElementIsEnabled(String locType, String locValue) throws Exception
	{
		boolean enabled;
		try {
		   if(getDriver().findElement(byType(locType, locValue)).isEnabled())
			   enabled = true;
		   else enabled = false;
		} catch (NoSuchElementException e) {
			enabled = false;
			e.printStackTrace();
		}
		return enabled;
	}
	
	/**
	 * This method checks if element exists
	 * @param map: Web Element
	 * @return true if element exist, false if not
	 * @throws Exception 
	 * 
	 */
	public static boolean checkElementExists(String locType, String locValue) throws Exception
	{
		//int currentWait= setBrowserTimeOut(5);;
		boolean present;
		try {
		   getDriver().findElement(byType(locType, locValue));
		   //setBrowserTimeOut(currentWait);
		   present = true;
		} catch (NoSuchElementException e) {
			//setBrowserTimeOut(currentWait);
		   present = false;
		}
		return present;
	}
	
	/**
	 * This method Enter text into a text field
	 * @param map: Web Element
	 * @param value: value to put in the text field
	 * @throws Exception 
	 * 
	 */
	public static void enterText(LinkedHashMap<String, String> map, String value) throws Exception
	{
		printLog("Enter text for "+ map.get("field_name"));
		String locType = map.get("locator_type");
		String locValue = map.get("locator_value");
		if (!elementExists(locType, locValue))
		{
			printLog("Element "+ map.get("field_name")+" was "
					+ "not found on Gui");
			testCaseStatus = false;
			failTestCase("Enter "+ map.get("field_name"), "Element exists", 
					"Element not found", "Step", "fail", map.get("field_name")+" not found");
		}else
		{
			WebElement element = driver.findElement(byType(locType, locValue));
			List<WebElement> items = driver.findElements(byType(locType, locValue));
			for(WebElement e: items)
			{
			    if(e.isDisplayed()) element = e;
			}
			try
			{
			element.sendKeys(value);
			//element.sendKeys(Keys.UP);
			}catch (Exception e)
			{
				e.printStackTrace();
			}
			System.out.println("element.sendKeys(value);");
		}
		System.out.println(value + " entred");
	}
	/**
	 * This method Enter text into a text field
	 * @param map: Web Element
	 * @param value: value to put in the text field
	 * @throws Exception 
	 * 
	 */
	public static void enterTextTextArea(LinkedHashMap<String, String> map, String value) throws Exception
	{
		printLog("Enter text for "+ map.get("field_name"));
		String locType = map.get("locator_type");
		String locValue = map.get("locator_value");
		if (!elementExists(locType, locValue))
		{
			printLog("Element "+ map.get("field_name")+" was "
					+ "not found on Gui");
			testCaseStatus = false;
			failTestCase("Enter "+ map.get("field_name"), "Element exists", 
					"Element not found", "Step", "fail", map.get("field_name")+" not found");
		}else
		{
			WebElement element = driver.findElement(byType(locType, locValue));
			List<WebElement> items = driver.findElements(byType(locType, locValue));
			for(WebElement e: items)
			{
			    if(e.isDisplayed()) element = e;
			}
			try
			{
				JavascriptExecutor jse = (JavascriptExecutor)driver;
				jse.executeScript("arguments[0].value='"+value+"';", element);
			/*element.sendKeys(Keys.TAB);
			element.clear();
			element.sendKeys(value);
			holdSeconds(1);
			element.sendKeys(Keys.TAB);
			element.clear();
			element.sendKeys(value);*/
			}catch (Exception e)
			{
				e.printStackTrace();
			}
			System.out.println("element.sendKeys(value);");
		}
		System.out.println(value + " entred");
	}
	
	/**
	 * This method Enter text into a text field
	 * @param map: Web Element
	 * @param value: value to put in the text field
	 * @param niem: the niem element
	 * @throws Exception 
	 * 
	 */
	public static void enterText(LinkedHashMap<String, String> map, String value, int niem) throws Exception
	{
		printLog("Enter text for  "+ map.get("field_name"));
		String locType = map.get("locator_type");
		String locValue = map.get("locator_value");
		int count = 1;
		if (!elementExists(locType, locValue))
		{
			printLog("Element "+ map.get("field_name")+" was "
					+ "not found on Gui");
			testCaseStatus = false;
			failTestCase("Enter "+ map.get("field_name"), "Element exists", 
					"Element not found", "Step", "fail", map.get("field_name")+" not found");
		}else
		{
			WebElement element = driver.findElement(byType(locType, locValue));
			List<WebElement> items = driver.findElements(byType(locType, locValue));
			for(WebElement e: items)
			{
			    if(e.isDisplayed()) element = e;
			    if (niem == count) break;
			    count ++;
			}
			try
			{
			element.sendKeys(value);
			element.sendKeys(Keys.UP);
			}catch (Exception e)
			{
				e.printStackTrace();
			}
			System.out.println("element.sendKeys(value);");
		}
		System.out.println(value + " entred");
	}
	
	/**
	 * This method Enter text into a text field after clearing the field content
	 * @param map: Web Element
	 * @param value: value to put in the text field
	 * @throws Exception 
	 * 
	 */
	public static void enterTextAndClear(LinkedHashMap<String, String> map,
			String value) throws Exception
	{
		printLog("Enter text for  "+ map.get("field_name"));
		String locType = map.get("locator_type");
		String locValue = map.get("locator_value");
		if (!elementExists(locType, locValue))
		{
			printLog("Element "+ map.get("field_name")+" was "
					+ "not found on Gui");
			testCaseStatus = false;
			failTestCase("Enter "+ map.get("field_name"), "Element exists", 
					"Element not found", "Step", "fail", map.get("field_name")+" not found");
		}else
		{
			WebElement element = driver.findElement(byType(locType, locValue));
			List<WebElement> items = driver.findElements(byType(locType, locValue));
			for(WebElement e: items)
			{
			    if(e.isDisplayed()) element = e;
			}
			element.click();
			element.clear();
			//System.out.println("element.clear();");
			/*element.sendKeys(Keys.CONTROL + "a");
			System.out.println("element.sendKeys(Keys.CONTROL + );");
			element.sendKeys(Keys.DELETE);
			System.out.println("element.sendKeys(Keys.DELETE);");
			holdSeconds(1);*/
			try
			{
				element.sendKeys(value);
			}catch (Exception e)
			{
				e.printStackTrace();
			}
			System.out.println("element.sendKeys(value);");
		}
		System.out.println(value + " entred");
	}
	/**
	 * This method uploads file
	 * @param file: the file to be uploaded
	 * @throws Exception 
	*/
	public static void uploadFile(String file) throws Exception
	{
		StringSelection ss = new StringSelection(file);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);		
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);	
	}

	/**
	 * This method checks if element exists
	 * @param map: Web element
	 * @return true if element found, false if not
	 * @throws Exception 
	 * 
	 */
	public static boolean elementExists(HashMap<String, String> map) throws Exception
	{
		return elementExists(map.get("locator_type"), map.get("locator_value"));
	}
	
	/**
	 * This method verifies if element exists
	 * @param locType: The type of locator
	 * @param locValue: the value of locator
	 * @return true if element found, false if not
	 * @throws Exception 
	 * 
	 */
	public static boolean elementExists(String locType, String locValue) throws Exception
	{
		boolean present;
		try {
		   getDriver().findElement(byType(locType, locValue));
		   present = true;
		} catch (NoSuchElementException e) {
		   present = false;
		}
		return present;
	}
	/**
	 * This method sets attribute value of element
	 * @param map: Web Element
	 * @param attribute: element attribute
	 * @param value: value to put in the text field
	 * @throws Exception 
	 * 
	 */
	public static void setAttributeValue(HashMap<String, String> map, 
										 String attribute, 
										 String value) throws Exception
	{
		printLog("Enter text for "+ map.get("field_name"));
		String locType = map.get("locator_type");
		String locValue = map.get("locator_value");
		if (!elementExists(locType, locValue))
		{
			printLog("Element "+ map.get("field_name")+" was "
					+ "not found on Gui");
		}else
		{
			WebElement element = driver.findElement(byType(locType, locValue));
			List<WebElement> items = driver.findElements(byType(locType, locValue));
			for(WebElement e: items)
			{
			    if(e.isDisplayed()) element = e;
			}
			JavascriptExecutor executor = (JavascriptExecutor)getDriver();
			executor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2]);", 
		             element, attribute, value);
		}
	}
	
	/**
	 * This method clicks on element
	 * @param map: Web Element
	 * @throws Exception 
	 * 
	 */
	public static void clickElementJs(HashMap<String, String> map) throws Exception
	{
		printLog("Click  on  element "+ map.get("field_name"));
		String locType = map.get("locator_type");
		String locValue = map.get("locator_value");
		if (elementExists(locType, locValue))
		{
			WebElement element = driver.findElement(byType(locType, locValue));
			List<WebElement> items = driver.findElements(byType(locType, locValue));
			for(WebElement e: items)
			{
			    if(e.isEnabled()) 
			    	{
			    		element = e;
			    	}
			}
			try{
			JavascriptExecutor executor = (JavascriptExecutor)driver;
			executor.executeScript("arguments[0].click();", element);
			}catch(Exception e){e.printStackTrace();}
			
		}else
		{
			printLog("Element "+ map.get("field_name")+" was "
					+ "not found on Gui");
			failTestCase("Clicking element " + map.get("field_name"), map.get("field_name") + " wasn't found",
					"Not as expected", "VP", "fail", "Klicking element " + map.get("field_name"));
		}
	}
	
	
	/**
	 * This method double clicks on element
	 * @param map: Web Element
	 * @throws Exception 
	 * 
	 */
	public static void doubleClickElementJs(HashMap<String, String> map) throws Exception
	{
		printLog("Click  on  element "+ map.get("field_name"));
		String locType = map.get("locator_type");
		String locValue = map.get("locator_value");
		if (elementExists(locType, locValue))
		{
			WebElement element = driver.findElement(byType(locType, locValue));
			List<WebElement> items = driver.findElements(byType(locType, locValue));
			for(WebElement e: items)
			{
			    if(e.isEnabled()) 
			    	{
			    		element = e;
			    	}
			}
			try{
			JavascriptExecutor executor = (JavascriptExecutor)driver;
			executor.executeScript("arguments[0].click();", element);
			executor.executeScript("arguments[0].click();", element);
			}catch(Exception e){e.printStackTrace();}
			
		}else
		{
			printLog("Element "+ map.get("field_name")+" was "
					+ "not found on Gui");
			failTestCase("Clicking element " + map.get("field_name"), map.get("field_name") + " wasn't found",
					"Not as expected", "VP", "fail", "Klicking element " + map.get("field_name"));
		}
	}
	/**
	 * This method clicks on element
	 * @param map: Web Element
	 * @throws Exception 
	*/
	public static void clickElement(HashMap<String, String> map) throws Exception
	{
		printLog("Click on element "+ map.get("field_name"));
		String locType = map.get("locator_type");
		String locValue = map.get("locator_value");
		if (!elementExists(locType, locValue))
		{
			printLog("Element "+ map.get("field_name")+" was "
					+ "not found on Gui");
			failTestCase("Clicking element " + map.get("field_name"), map.get("field_name") + " wasn't found",
					"Not as expected", "VP", "fail", "Klicking element " + map.get("field_name"));
		}else
		{
			WebElement element = driver.findElement(byType(locType, locValue));
			List<WebElement> items = driver.findElements(byType(locType, locValue));
			for(WebElement e: items)
			{
			    if(e.isDisplayed()) element = e;
			}
			try{
				highlightElement(map, "red");
				element.sendKeys(Keys.ENTER);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * This method clicks on niem element
	 * @param map, Web Element
	 * @param niem, niem element
	 * @throws Exception 
	 * 
	 */
	public static void clickNiemElementJs(HashMap<String, String> map, int niem) throws Exception
	{
		printLog("Click  on  element "+ map.get("field_name"));
		String locType = map.get("locator_type");
		String locValue = map.get("locator_value");
		int count = 1;
		if (elementExists(locType, locValue))
		{
			WebElement element = driver.findElement(byType(locType, locValue));
			List<WebElement> items = driver.findElements(byType(locType, locValue));
			for(WebElement e: items)
			{
			    if(e.isEnabled()) 
			    {
			    		element = e;
			    		if (count == niem) break;
			    		count++;
			    }
			}
			try{
			JavascriptExecutor executor = (JavascriptExecutor)driver;
			executor.executeScript("arguments[0].click();", element);
			}catch(Exception e){e.printStackTrace();}
			
		}else
		{
			printLog("Element "+ map.get("field_name")+" was "
					+ "not found on Gui");
			failTestCase("Clicking element " + map.get("field_name"), map.get("field_name") + " wasn't found",
					"Not as expected", "VP", "fail", "Klicking element " + map.get("field_name"));
		}
	}
	
	/**
	 * This method enter file name into file type input
	 * @param map: Web Element
	 * @param file, the file name to be entered 
	 * @throws Exception 
	 * 
	 */
	public static void enterTextFile(HashMap<String, String> map, String file) throws Exception
	{	
		printLog("Enter text for "+ map.get("field_name"));
		String locType = map.get("locator_type");
		String locValue = map.get("locator_value");
		if (!elementExists(locType, locValue))
		{
			printLog("Element "+ map.get("field_name")+" was "
					+ "not found on Gui");
			testCaseStatus = false;
			failTestCase("Enter "+ map.get("field_name"), "Element exists", 
					"Element not found", "Step", "fail", map.get("field_name")+" not found");
		}else
		{
			WebElement element = driver.findElement(byType(locType, locValue));
			List<WebElement> items = driver.findElements(byType(locType, locValue));
			for(WebElement e: items)
			{
			    if(e.isDisplayed()) element = e;
			}
			 ((JavascriptExecutor) getDriver()).executeScript(
	         "arguments[0].style.visibility = 'visible'; arguments[0].style = ''; arguments[0].style.display = 'block';",
	         element);
			 try
			 {
				 element.sendKeys(file);
			 }catch(Exception e)
			 {
				  System.out.println("File issue "+ file);
		          failTestCase("Entering File", "File should be entered", 
		        		  "File doesn't exist or path is too long: "+file, "Step", "fail",
		        		  "file upload fail");
			 }
		}
	}
	/*file_input = driver.find_element_by_xpath("//input[@class = 'dz-hidden-input']")
	# make the input visible:
	driver.execute_script('arguments[0].style = ""; arguments[0].style.display = "block";
	arguments[0].style.visibility = "visible";', file_input)
	# send file:
	file_input.send_keys("C:\\Users\\nicolas\\Documents\\CT\\Séance_du_Lundi_15_février.pdf")*/
	
	

	/**
	 * This method clicks on element
	 * @param map: Web Element
	 * @throws Exception 
	 * @param niem, niem element
	*/
	public static void clickNiemElement(HashMap<String, String> map, int niem) throws Exception
	{
		printLog("Click on element "+ map.get("field_name"));
		String locType = map.get("locator_type");
		String locValue = map.get("locator_value");
		int count = 1;
		if (!elementExists(locType, locValue))
		{
			printLog("Element "+ map.get("field_name")+" was "
					+ "not found on Gui");
			failTestCase("Clicking element " + map.get("field_name"), map.get("field_name") + " wasn't found",
					"Not as expected", "VP", "fail", "Klicking element " + map.get("field_name"));
		}else
		{
			WebElement element = null;
			List<WebElement> items = driver.findElements(byType(locType, locValue));
			for(WebElement e: items)
			{
			    if(e.isDisplayed()) element = e;
			    if (count == niem) break;
			    count++;
			}
			element.sendKeys("\n");
		}
	}
	
	
	/**
	 * This method selects an option from drop-down list by text    
	 * @param map: Web Element
	 * @param textValue: value to put in the text field
	 * @throws Exception 
	 * 
	 */
	public static void selectNiemElementByText(HashMap<String, String> map, 
										   String textValue, int niem) throws Exception
	{
		printLog("select "+textValue+" from element "+ map.get("field_name"));
		String locType = map.get("locator_type");
		String locValue = map.get("locator_value");
		int count = 1;
		WebElement element = null;
		
		if (!elementExists(locType, locValue))
		{
			printLog("Element '"+ map.get("field_name")+"' was "
					+ "not found on Gui");
			failTestCase("Filling element " + map.get("field_name"), map.get("field_name") + " wasn't found",
					"Not as expected", "VP", "fail", "Filling element " + map.get("field_name"));
		}else
		{
		List<WebElement> items = driver.findElements(byType(locType, locValue));
		for(WebElement e: items)
		{
		    if(e.isEnabled()) 
		    {
		    		element = e;
		    		if (count == niem) break;
		    		count++;
		    }
		}
		try{
			Select dropdown = new Select(element);
			try
			{
				dropdown.selectByVisibleText(textValue);
			}catch(Exception e){
				failTestCase("Select element "+ textValue, "Element ["+textValue+ "] should be in"
						+ " the list of options",
						"Not as expected", "VP", "fail", "Select element "+ textValue);
			}
		}catch(Exception e){e.printStackTrace();}
		
		}
		System.out.println(textValue+ " selected");
	}
	
	/**
	 * This method selects an option from drop-down list by text    
	 * @param map: Web Element
	 * @param textValue: value to put in the text field
	 * @throws Exception 
	 * 
	 */
	public static void selectElementByText(HashMap<String, String> map, 
										   String textValue) throws Exception
	{
		printLog("select "+textValue+" from element "+ map.get("field_name"));
		String locType = map.get("locator_type");
		String locValue = map.get("locator_value");
		if (!elementExists(locType, locValue))
		{
			printLog("Element '"+ map.get("field_name")+"' was "
					+ "not found on Gui");
			failTestCase("Filling element " + map.get("field_name"), map.get("field_name") + " wasn't found",
					"Not as expected", "VP", "fail", "Filling element " + map.get("field_name"));
		}else
		{
			//clickElementJs(map);
			Select dropdown = new Select(driver.findElement(byType(locType, locValue)));
			try
			{
				dropdown.selectByVisibleText(textValue);
			}catch(Exception e){
				failTestCase("Select element "+ textValue, "Element ["+textValue+ "] should be in the list of options",
						"Not as expected", "VP", "fail", "Select element "+ textValue);
			}
		}
		System.out.println(textValue+ " selected");
	}
	
	/**
	 * This method selects an option from drop-down list by index
	 * @param map: Web Element
	 * @param index: index of the option
	 * @throws Exception 
	 * 
	 */
	public static void selectElementByIndex(HashMap<String, String> map, int index) throws Exception
	{
		printLog("select element index "+index+" from element "+ map.get("field_name"));
		String locType = map.get("locator_type");
		String locValue = map.get("locator_value");
		if (!elementExists(locType, locValue))
		{
			printLog("Element "+ map.get("field_name")+" was "
					+ "not found on Gui");
		}else
		{
			Select dropdown = new Select(driver.findElement(byType(locType, locValue)));
			dropdown.selectByIndex(index);
			//WebElement element=driver.findElement(byType(locType, locValue));
		}
	}
	
	/**
	 * This method selects an option from drop-down list by value
	 * @param map: Web Element
	 * @param value: value of the option
	 * @throws Exception 
	 * 
	 */
	public static void selectElementByValue(HashMap<String, String> map, 
											String value) throws Exception
	{
		printLog("select "+value+" from element "+ map.get("field_name"));
		String locType = map.get("locator_type");
		String locValue = map.get("locator_value");
		if (!elementExists(locType, locValue))
		{
			printLog("Element "+ map.get("field_name")+" was "
					+ "not found on Gui");
		}else
		{
			Select dropdown = new Select(driver.findElement(byType(locType, locValue)));
			dropdown.selectByValue(value);
		}
	}
	
	/**
	 * This method return all options of a select
	 * @param map, Web Element
	 * @return a list of options in the dropdown list
	 * @throws Exception 
	 * 
	 */
	public static String[] getSelectValues(HashMap<String, String> map) throws Exception
	{
		printLog("select element "+ map.get("field_name"));
		String locType = map.get("locator_type");
		String locValue = map.get("locator_value");
		String[] vals = null; int i=0;
		if (!elementExists(locType, locValue))
		{
			printLog("Element "+ map.get("field_name")+" was "
					+ "not found on Gui");
		}else
		{
			WebElement element = driver.findElement(byType(locType, locValue)); 
	        Select select = new Select(element);  
	       java.util.List<WebElement> options = select.getOptions();
	       vals = new String[options.size()];
	        for(WebElement item:options)  
	        {  
	        	vals[i] = item.getText().trim(); i++;
	        	System.out.println("Dropdown values are "+ item.getText()); 
	        }
		}
		return vals;
	}
	
	/**
	 * This method sleeps for a moment
	 * @param seconds: number of seconds to hold
	 * @throws InterruptedException 
	 * 
	 */
	public static void holdSeconds(int seconds) throws InterruptedException
	{
		Thread.sleep(1000*seconds);
	}
	/**
	 * This method sleeps for a moment
	 * @param milliSeconds: number of milliseconds to hold
	 * @throws InterruptedException 
	 * 
	 */
	public static void holdMilliSeconds(int milliSeconds) throws InterruptedException
	{
		Thread.sleep(milliSeconds);
	}
	/**
	 * This method finds element by type
	 * @param locType: The type of locator
	 * @param locValue: the value of locator
	 * @return object BY
	 * @throws Exception 
	 * 
	*/
	public static By byType(String locType, String locValue) throws Exception
	{
		switch(locType.toUpperCase())
		{
			case "ID":
				return By.id(locValue);
			case "NAME":
				return By.name(locValue);
			case "CLASS":
				return By.className(locValue);
			case "Tag":
				return By.tagName(locValue);
			case "XPATH":
				return By.xpath(locValue);
			case "LINKTEXT":
				return By.linkText(locValue);
			case "CSS":
				return By.cssSelector(locValue);
			default:
			{
				String error = "The Type of locator needs to be id, name, "
						+ "class, tag, xpath, linktext or css";
				printLog(error);
				throw new  Exception(error);
			}
		}
	}

	/**
	 * This method scrolls to a given element
	 * @param map: Web Element
	 * @throws Exception 
	 * 
	*/
	public static void scrollToElement(HashMap<String, String> map) throws Exception
	{
		printLog("Scroll to element "+ map.get("field_name"));
		String locType = map.get("locator_type");
		String locValue = map.get("locator_value");
		if (!elementExists(locType, locValue))
		{
			printLog("Element ' "+ map.get("field_name")+"' was "
					+ "not found on GUI");
		}else
		{
			WebElement element = driver.findElement(byType(locType, locValue));
			List<WebElement> items = driver.findElements(byType(locType, locValue));
			for(WebElement e: items)
			{
			    if(e.isDisplayed()) 
			    	{
			    		element = e;
			    	}
			}
			JavascriptExecutor executor = (JavascriptExecutor)getDriver();
			Point point = element.getLocation();
			executor.executeScript("window.scrollTo(0, -document.body.scrollHeight);");
			holdSeconds(1);
			executor.executeScript("javascript:window.scrollBy("+point.getX()+","+(point.getY()-300)+")");
		}
	}
	
	/**
	 * This method scrolls a niem given element
	 * @param map, Web Element
	 * @param niem, the niem element
	 * @throws Exception 
	 * 
	 */
	public static void scrollToNiemElement(HashMap<String, String> map, int niem) throws Exception
	{
		printLog("Scroll to element "+ map.get("field_name"));
		int position = 1;
		String locType = map.get("locator_type");
		String locValue = map.get("locator_value");
		if (!elementExists(locType, locValue))
		{
			printLog("Element ' "+ map.get("field_name")+"' was "
					+ "not found on GUI");
		}else
		{
			WebElement element = driver.findElement(byType(locType, locValue));
			List<WebElement> items = driver.findElements(byType(locType, locValue));
			for(WebElement e: items)
			{
			    if(e.isDisplayed()) 
			    	{
			    		element = e;
			    		if (niem == position) break;
			    		position++;
			    	}
			}
			JavascriptExecutor executor = (JavascriptExecutor)getDriver();
			Point point = element.getLocation();
			executor.executeScript("window.scrollTo(0, -document.body.scrollHeight);");
			holdSeconds(1);
			executor.executeScript("javascript:window.scrollBy("+point.getX()+","+(point.getY()-300)+")");
		}
	}
	/**
	 * This method scrolls by pixels
	 * @param pixels: number of pixels to scroll down
	 * 
	 */
	public static void scrollByPixel(int pixels)
	{
		JavascriptExecutor executor = (JavascriptExecutor)getDriver();
		scrollToTheTopOfPage();
		executor.executeScript("window.scrollBy(0,"+pixels+")");
	}
	
	/**
	 * This method scrolls to the top of the page
	 */
	public static void scrollToTheTopOfPage()
	{
		JavascriptExecutor executor = (JavascriptExecutor)getDriver();
		executor.executeScript("window.scrollBy(0,0)");
		executor.executeScript("window.scrollTo(0, -document.body.scrollHeight);");
	}
	
	/**
	 * This method scrolls to the bottom of the page
	*/
	public static void scrollToTheBottomOfPage()
	{
		JavascriptExecutor executor = (JavascriptExecutor)getDriver();
		executor.executeScript("window.scrollTo(0, document.body.scrollHeight)");
	}
	
	/**
	 * This method updates report
	 * @param stepDesc:step descrition
	 * @param stepExpectResult: step expected result
	 * @param stepActualResult: step actual result
	 * @param StepVpStep: step or VP
	 * @param StepPassFail: pass or fail
	 * @param StepSs: error message/screen shot
	 * @throws IOException 
	 * 
	*/
	public static void updateHtmlReport(String stepDesc, 
										String stepExpectResult, 
										String stepActualResult, 
										String StepVpStep,
										String StepPassFail,
										String StepSs) throws IOException
	{
		String aLink  = "";
		if (!"".equals(StepSs))
		{
			String screenShotPath = takeScreenShot(StepSs, false);
			aLink = "<a href = '"+screenShotPath+"'>"+StepSs+"</a>";
		}
		HtmlReport.addHtmlStep(stepDesc, stepExpectResult, stepActualResult, StepVpStep, StepPassFail, aLink);
	}
	
	
	/**
	 * This method updates report
	 * @param stepDesc:step descrition
	 * @param stepExpectResult: step expected result
	 * @param stepActualResult: step actual result
	 * @param StepVpStep: step or VP
	 * @param StepPassFail: pass or fail
	 * @param StepSs: error message/screen shot
	 * @param fullPage: fall page or the visible part
	 * @throws IOException 
	 * 
	*/
	public static void updateHtmlReport(String stepDesc, 
										String stepExpectResult, 
										String stepActualResult, 
										String StepVpStep,
										String StepPassFail,
										String StepSs, 
										boolean fullPage) throws IOException
	{
		String aLink  = "";
		if (!"".equals(StepSs))
		{
			String screenShotPath = takeScreenShot(StepSs, fullPage);
			aLink = "<a href = '"+screenShotPath+"'>"+StepSs+"</a>";
		}
		HtmlReport.addHtmlStep(stepDesc, stepExpectResult, stepActualResult, StepVpStep, StepPassFail, aLink);
	}
	
	/**
	 * This method updates overall report
	 * @param stepDesc:step descrition
	 * @param stepExpectResult: step expected result
	 * @param stepActualResult: step actual result
	 * @param StepVpStep: step or VP
	 * @param StepPassFail: pass or fail
	 * @param StepSs: error message/screen shot
	 * @throws IOException 
	 * 
	 */
	public static void updateHtmlReportOverall(String stepDesc, 
										String stepExpectResult, 
										String stepActualResult, 
										String StepVpStep,
										String StepPassFail,
										String StepSs) throws IOException
	{
		String aLink  = "";
		if (!"".equals(StepSs))
		{
			String screenShotPath = takeScreenShot(StepSs, true);
			aLink = "<a href = '"+screenShotPath+"'>"+StepSs+"</a>";
		}
		HtmlReport.addHtmlStep(stepDesc, stepExpectResult, stepActualResult, StepVpStep, StepPassFail, aLink);
	}
	
	/**
	 * This method replace the GUI element value
	 * @param guiRow, gui element
	 * @param newValue, gui element new value
	 * @return gui element
	*/
	public static  LinkedHashMap<String, String> replaceGui (HashMap<String, String> guiRow, 
													   		 String...  newValues)
	{
		HashMap<String, String> newGuiRow = new HashMap<String, String>(guiRow);
		int i=0;
		for(String str: newValues)
		{
			if (i==0)
			{
				newGuiRow.put("locator_value", newGuiRow.get("locator_value").replace("~val~", str));
			}else
			{
				newGuiRow.put("locator_value", newGuiRow.get("locator_value").replace("~val"+i+"~", str));
			}
			i++;
		}
		return new LinkedHashMap<String, String>(newGuiRow); 
	}
	
	/**
	 * This method changes element HTML value
	 * @param map: gui element
	 * @param value: new HTML value
	 * @throws Exception
	 */
	public static void changeElementHtmlValue(HashMap<String, String> map, 
											  String value) throws Exception
	{
		printLog("Enter text for "+ map.get("field_name"));
		String locType = map.get("locator_type");
		String locValue = map.get("locator_value");
		if (!elementExists(locType, locValue))
		{
			printLog("Element "+ map.get("field_name")+" was "
					+ "not found on Gui");
		}else
		{
			WebElement element = null;
			List<WebElement> items = driver.findElements(byType(locType, locValue));
			for(WebElement e: items)
			{
			    if(e.isDisplayed()) element = e;
			}
			JavascriptExecutor executor = (JavascriptExecutor)getDriver();
			executor.executeScript("arguments[0].innerHTML ='"+value+"'", element);
		}
	}
	
	/**
	 * This method highlights element on the page
	 * @param map, gui element
	 * @param color, the used color for highlighting 
	 * @throws Exception
	 */
	public static void highlightElement(HashMap<String, String> map,
										String color) throws Exception
	{
		printLog("highlight "+ map.get("field_name"));
		String locType = map.get("locator_type");
		String locValue = map.get("locator_value");
		
		if (!elementExists(locType, locValue))
		{
			printLog("Element "+ map.get("field_name")+" was "
					+ "not found on GUI");
		}else
		{
			WebElement element = driver.findElement(byType(locType, locValue));
			List<WebElement> items = driver.findElements(byType(locType, locValue));
			try
			{
				for(WebElement e: items)
				{
				    if(e.isDisplayed()) element = e;
				}
				JavascriptExecutor executor = (JavascriptExecutor)getDriver();
				executor.executeScript("arguments[0].setAttribute('style', 'border:"
						+ " 2px dashed "+color+";');", element);
				printLog(map.get("field_name") + " highlighted ");
			}catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * This method highlights niem element on the page
	 * @param map: gui element
	 * @param color: the used color for highlighting 
	 * @param niem, the niem element
	 * @throws Exception
	 */
	public static void highlightNiemElement(HashMap<String, String> map,
										String color, int niem) throws Exception
	{
		printLog("highlight "+ map.get("field_name"));
		String locType = map.get("locator_type");
		String locValue = map.get("locator_value");
		int count=1;
		if (!elementExists(locType, locValue))
		{
			printLog("Element "+ map.get("field_name")+" was "
					+ "not found on GUI");
		}else
		{
			WebElement element = driver.findElement(byType(locType, locValue));
			List<WebElement> items = driver.findElements(byType(locType, locValue));
			try
			{
				for(WebElement e: items)
				{
				    if(e.isDisplayed()) element = e;
				    if (count == niem) break;
				    count++;
				}
				JavascriptExecutor executor = (JavascriptExecutor)getDriver();
				executor.executeScript("arguments[0].setAttribute('style', 'border:"
						+ " 2px dashed "+color+";');", element);
				printLog(map.get("field_name") + " highlighted ");
			}catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * This method unhighlights element on the page
	 * @param map: gui element
	 * @throws Exception
	 */
	public static void unHighlightElement(HashMap<String, String> map) throws Exception
	{
		printLog("unhighlight "+ map.get("field_name"));
		String locType = map.get("locator_type");
		String locValue = map.get("locator_value");
		if (!elementExists(locType, locValue))
		{
			printLog("Element "+ map.get("field_name")+" was "
					+ "not found on Gui");
			failTestCase("Unhighlight element " + map.get("field_name"), map.get("field_name") + " wasn't found",
					"Not as expected", "VP", "fail", "unhighlight element " + map.get("field_name"));
		}else
		{
			try{
				WebElement element = driver.findElement(byType(locType, locValue));
				List<WebElement> items = driver.findElements(byType(locType, locValue));
				for(WebElement e: items)
				{
				    if(e.isDisplayed()) element = e;
				}
				JavascriptExecutor executor = (JavascriptExecutor)getDriver();
				executor.executeScript("arguments[0].style.border='0px'", element);
				printLog(map.get("field_name") + "unhighlighted ");
			}catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * This method gets element attribute
	 * @param map: gui element
	 * @param attribute: HTML Element's property(text, value, href...)
	 * @throws Exception
	 */
	public static String[] getElementValuesIntoArray(HashMap<String, String> map, 
											 String attribute) throws Exception
	{
		printLog("Get the "+attribute+" of the element "+ map.get("field_name"));
		String locType = map.get("locator_type");
		String locValue = map.get("locator_value");
		String [] listOfValues = null;
		int j=0;
		if (!elementExists(locType, locValue))
		{
			printLog("Element "+ map.get("field_name")+" was "
					+ "not found on GUI");
			failTestCase("Get values of element " + map.get("field_name"), map.get("field_name") + " wasn't found",
					"Not as expected", "VP", "fail", "Klicking element " + map.get("field_name"));
		}else
		{
			//WebElement element = driver.findElement(byType(locType, locValue));
			List<WebElement> items = driver.findElements(byType(locType, locValue));
			listOfValues = new String [items.size()];
			for(WebElement e: items)
			{
			    if(e.isDisplayed()) 
			    	{
			    		if ("text".equalsIgnoreCase(attribute))
			    			listOfValues[j] = e.getText();
						else
							listOfValues[j] = e.getAttribute(attribute) ;
			    		j++;
			    	}
			}
		}
		return listOfValues;
	}
	/**
	 * This method unhighlights niem element on the page
	 * @param map: gui element
	 * @param niem, the niem element
	 * @throws Exception
	 */
	public static void unHighlightNiemElement(HashMap<String, String> map, 
			int niem) throws Exception
	{
		printLog("unhighlight "+ map.get("field_name"));
		String locType = map.get("locator_type");
		String locValue = map.get("locator_value");
		int position = 1;
		if (!elementExists(locType, locValue))
		{
			printLog("Element "+ map.get("field_name")+" was "
					+ "not found on Gui");
			failTestCase("Unhighlight element " + map.get("field_name"), map.get("field_name") + " wasn't found",
					"Not as expected", "VP", "fail", "Klicking element " + map.get("field_name"));
		}else
		{
			try{
				WebElement element = driver.findElement(byType(locType, locValue));
				List<WebElement> items = driver.findElements(byType(locType, locValue));
				for(WebElement e: items)
				{
				    if(e.isDisplayed()) element = e;
				    if(niem==position) break;
				    position++;
				}
				JavascriptExecutor executor = (JavascriptExecutor)getDriver();
				executor.executeScript("arguments[0].style.border='0px'", element);
				printLog(map.get("field_name") + "unhighlighted ");
			}catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * This method switches to frame
	 * @param map: gui element
	 * @throws Exception
	 */
	public static void switchToFrame(HashMap<String, String> map) throws Exception
	{
		printLog("switch to frame  "+ map.get("field_name"));
		String locType = map.get("locator_type");
		String locValue = map.get("locator_value");
		
		
		if (!elementExists(locType, locValue))
		{
			printLog("frame "+ map.get("field_name")+" was "
					+ "not found on Gui");
			failTestCase("Switch to Form " + map.get("field_name"), map.get("field_name") + " wasn't found",
					"Not as expected", "VP", "fail", "farm element " + map.get("field_name"));
		}else
		{
			WebElement element = driver.findElement(byType(locType, locValue));
			List<WebElement> items = driver.findElements(byType(locType, locValue));
			for(WebElement e: items)
			{
			    if(e.isDisplayed()) element = e;
			}
			getDriver().switchTo().frame(element);
		}
	}
	/**
	 * This method switches back from frame
	 * @param map: gui element
	 * @throws Exception
	 */
	public static void switchBackFromFrame() throws Exception
	{
		getDriver().switchTo().defaultContent();
	}
	/**
	 * This method gets element attribute
	 * @param map, gui element
	 * @param attribute, Element's HTML property
	 * @throws Exception
	 */
	public static String getElementAttribute(HashMap<String, String> map, 
											 String attribute) throws Exception
	{
		printLog("Get the "+attribute+" of the element "+ map.get("field_name"));
		String locType = map.get("locator_type");
		String locValue = map.get("locator_value");
		if (!elementExists(locType, locValue))
		{
			printLog("Element "+ map.get("field_name")+" was "
					+ "not found on GUI");
			failTestCase("Get attribute element " + map.get("field_name"), map.get("field_name") + " wasn't found",
					"Not as expected", "VP", "fail", "Klicking element " + map.get("field_name"));
		}else
		{
			WebElement element = driver.findElement(byType(locType, locValue));
			List<WebElement> items = driver.findElements(byType(locType, locValue));
			for(WebElement e: items)
			{
			    if(e.isDisplayed()) 
			    	{
			    		element = e;
			    	}
			}
			if ("text".equalsIgnoreCase(attribute))
			return element.getText();
			else
			return element.getAttribute(attribute);
		}
		return null;
	}
	
	
	
	/**
	 * This method gets NIEM element attribute
	 * @param map, gui element
	 * @param attribute, Element's HTML property
	 * @throws Exception
	 */
	public static String getNiemElementAttribute(HashMap<String, String> map, 
											 String attribute, int niem) throws Exception
	{
		printLog("Get the "+attribute+" of the element "+ map.get("field_name"));
		String locType = map.get("locator_type");
		String locValue = map.get("locator_value");
		int index = 0;
		if (!elementExists(locType, locValue))
		{
			printLog("Element "+ map.get("field_name")+" was "
					+ "not found on GUI");
			failTestCase("Get attribute element " + map.get("field_name"), map.get("field_name") + " wasn't found",
					"Not as expected", "VP", "fail", "Klicking element " + map.get("field_name"));
		}else
		{
			WebElement element = driver.findElement(byType(locType, locValue));
			List<WebElement> items = driver.findElements(byType(locType, locValue));
			for(WebElement e: items)
			{
				index++;
			    if(e.isDisplayed()) 
		    	{
		    		element = e;
		    		if (index == niem) break;
		    	}
			}
			if ("text".equalsIgnoreCase(attribute))
			return element.getText();
			else
			return element.getAttribute(attribute);
		}
		return null;
	}
	
	
	
	/**
	 * This method switch to web alert
	 */
	public static void switchToAlert()
	{
		try {
		    WebDriverWait wait = new WebDriverWait(driver, 2);
		    wait.until(ExpectedConditions.alertIsPresent());
		    Alert alert = driver.switchTo().alert();
		    System.out.println(alert.getText());
		    alert.accept();
		    Assert.assertTrue(alert.getText().contains("Thanks."));
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	/**
	 * This method switch to window
	 */
	public static String switchToWindow()
	{
		String winHandleBefore = null;
		try {
			 winHandleBefore = driver.getWindowHandle();
			 for(String winHandle : driver.getWindowHandles()){
			    driver.switchTo().window(winHandle);
			}

			
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return winHandleBefore;
	}
	
	
	/**
	 * This method switch back to window
	 */
	public static void switchBackToWindow(String handleBefore)
	{
		try {
			
			driver.close();
			// Switch back to original browser (first window)
			driver.switchTo().window(handleBefore);

		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	/**
	 * This method refreshes the current page
	 */
	public static void pageRefresh()
	{
		String currentUrl = getDriver().getCurrentUrl();
		getDriver().navigate().to(currentUrl);
	}
	
	/**
	 * This method gets driver
	 */
	public static WebDriver getDriver() {
		return driver;
	}
	
	/**
	 * This method sets process identifier
	 * @param pId: process identifier
	 */
	public static void setBrowserProcessId(String pId) {
		browserProcessId = pId;
	}
	
	/**
	 * This method gets process identifier
	 * @return process ID
	 */
	public String getBrowserProcessId() {
		return browserProcessId;
	}
	
	/**
	 * This method set driver name
	 * @param driver: driver name
	 */
	public void setDriver(WebDriver driver) {
		GuiTools.driver = driver;
	}
	
	/**
	 * This method gets current test case name
	 * @return current test case name
	 */
	public String getCurrentTestCaseName() {
		return currentTestCaseName;
	}
	
	/**
	 * This method sets test case name
	 * @param currentTestCaseName: test case name
	 */
	public void setCurrentTestCaseName(String currentTestCaseName) {
		this.currentTestCaseName = currentTestCaseName;
	}
	/**
	 * This method gets browser name
	 * @return browser name
	 */
	public String getBrowserName() {
		return browserName;
	}
	/**
	 * This method sets browser name
	 */
	public void setBrowserName(String browserName) {
		this.browserName = browserName;
	}
	
	/**
	 * This method gets thread id
	 * @return thread ID
	 */
	public long getThreadId() {
		return threadId;
	}
	
	/**
	 * This method sets thread
	 * @param threadId, thread identifier
	 */
	public void setThreadId(long threadId) {
		this.threadId = threadId;
	}
	
	/**
	 * This method removes special characters fron a string
	 * @param str, string to remove special char from
	 * @return string without special characters
	 */
	public static String removeSpecialChar(String str)
	{
		return str.replace(":", "").replace(">", "-").replace("<", "-").replace("?", "");
	}
	
}
