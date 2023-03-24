package XmlLibs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class XmlTools {

	
	/**
	 * This method build test ng file
	 * @param dataPool: arraylist of test cases
	 * @param fileSource, where the file will be saved
	 * @throws Exception 
	 * 
	 */
	public static void buildTestNgFromDataPool(
			ArrayList<LinkedHashMap<String, String>> dataPool,
			String fileSource) throws Exception
	{
		Document document = readXmlFile(fileSource);
		Node nodeP = document.getElementsByTagName("class").item(0);
		Node node = document.getElementsByTagName("methods").item(0);
		nodeP.removeChild(node);
		Element element =  	(Element) document.createElement("methods");
		document.getElementsByTagName("class").item(0).appendChild(element);
		//document.createElement("class").removeChild(node);
		for (HashMap<String, String> map: dataPool)
		{
			element =  	(Element) document.createElement("include");
			element.setAttribute("name", map.get("Test_Case_Name"));		
			document.getElementsByTagName("methods").item(0).appendChild(element);
		}
		saveXmlFile(document, fileSource);
		Document doc = readXmlFile(fileSource);
		Transformer tFormer = 
		TransformerFactory.newInstance().newTransformer();
		//  Set system id
		//tFormer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://testng.org/testng-1.0.dtd");
		Source source = new DOMSource(doc);
		Result result = new StreamResult(fileSource);
		tFormer.transform(source, result);
		//"<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd" >"
	}

	/**
	 * This method read xml file
	 * @param filePath: file path
	 * @throws Exception 
	 * 
	 */
	public static Document readXmlFile(String filePath) throws ParserConfigurationException, SAXException, IOException
	{
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance(); 
		domFactory.setIgnoringComments(true);
		DocumentBuilder builder = domFactory.newDocumentBuilder(); 
		File file = new File(filePath);
		Document doc = builder.parse(file);
		return doc;
	}
	/**
	 * This method save xml file
	 * @param doc: document
	 * @param filePath: the path where the file will be saved
	 * @throws Exception 
	 * 
	 */
	public static void saveXmlFile(Document doc, String filePath) throws TransformerException
	{
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(filePath));
        transformer.transform(source, result);
	}
}
