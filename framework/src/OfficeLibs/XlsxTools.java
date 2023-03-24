package OfficeLibs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XlsxTools {
	
	/**
	 * This read excel file into arraylist with filters
	 * @param filePath: file path
	 * @param tabName: tab name
	 * @param filter: the applied filter
	 * @throws Exception 
	 * 
	 */
	public static ArrayList<LinkedHashMap<String, String>> 
										  readXlsxSheetInOrderAndFilter(String filePath, 
										  String tabName, 
										  String filter) throws IOException
	{
		ArrayList<LinkedHashMap<String, String>> allPool = new ArrayList<LinkedHashMap<String, String>>();
		ArrayList<LinkedHashMap<String, String>> filteredTestCases = new ArrayList<LinkedHashMap<String, String>>();
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		File excelFile = new File(filePath);
	    FileInputStream fileInputStream = new FileInputStream(excelFile);
	    Workbook  workbook = new XSSFWorkbook(fileInputStream);
	    XSSFSheet sheet;
	    if(tabName.equals("")){
	    sheet = (XSSFSheet) workbook.getSheetAt(0);
	    }else{
	    sheet = (XSSFSheet) workbook.getSheet(tabName);}
	    Iterator<Row> rowIt = sheet.iterator();
	    Row header = rowIt.next();
	    DataFormatter df = new DataFormatter();
	    while(rowIt.hasNext()) 
	    {
	      Iterator<Cell> cellHeaderIterator = header.cellIterator();
	      Row row = rowIt.next();
	      Iterator<Cell> cellIterator = row.cellIterator();
	      while (cellIterator.hasNext() && cellHeaderIterator.hasNext()) 
	      {
	    	  Cell cellHeader = cellHeaderIterator.next();
	          Cell cell = cellIterator.next();
	          map.put(cellHeader.toString(), df.formatCellValue(cell));
	      }
	      System.out.println(map);
	      allPool.add(new LinkedHashMap<String, String>(map));
	      map.clear();
	    }
	    workbook.close();
	    fileInputStream.close();
	    if(!"".equalsIgnoreCase(filter))
	    {
		    filteredTestCases = filterArrayList(allPool, filter);
			return filteredTestCases;
	    }else
	    {
	    	return allPool;
	    }
	}
	/**
	 * This read excel file into hashmap with columns as keys
	 * @param filePath: file path
	 * @param tabName: tab name
	 * @throws Exception 
	 * 
	 */
	public static LinkedHashMap<String, String> readXlsxSheetWithFirstColKey(String filePath, 
			  																String tabName) throws IOException
	{
		String key="";
		String conditions ="";
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		File excelFile = new File(filePath);
		FileInputStream fileInputStream = new FileInputStream(excelFile);
		Workbook  workbook = new XSSFWorkbook(fileInputStream);
		XSSFSheet sheet = (XSSFSheet) workbook.getSheet(tabName);
		Iterator<Row> rowIt = sheet.iterator();
		Row header = rowIt.next();
		while(rowIt.hasNext()) 
		{
			//Iterator<Cell> cellHeaderIterator = header.cellIterator();
			Row row = rowIt.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			DataFormatter df = new DataFormatter();
			Cell cell1= cellIterator.next();
			key = df.formatCellValue(cell1);
			while (cellIterator.hasNext()) 
			{
				Cell cell = cellIterator.next();
				if(!cell.toString().equals(""))
				conditions = conditions+","+df.formatCellValue(cell);
			}
			if (conditions.startsWith(","))
			map.put(key, conditions.substring(1));
			else
			map.put(key, conditions);
			conditions="";
		}
		workbook.close();
		fileInputStream.close();
		for (HashMap.Entry<String, String> entry : map.entrySet()) 
		{
			System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
		}
		return map;
	}
	/**
	 * This read excelsX file into hashmap with columns as keys
	 * @param filePath: file path
	 * @param tabName: tab name
	 * @throws Exception 
	 * 
	 */
	public static ArrayList<LinkedHashMap<String, String>> readXlsxSheetAndFilter(String filePath, 
										  String tabName, 
										  String filter) throws IOException
	{
		ArrayList<LinkedHashMap<String, String>> allPool = new ArrayList<LinkedHashMap<String, String>>();
		ArrayList<LinkedHashMap<String, String>> filteredTestCases = new ArrayList<LinkedHashMap<String, String>>();
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		File excelFile = new File(filePath);
	    FileInputStream fileInputStream = new FileInputStream(excelFile);
	    Workbook  workbook = new XSSFWorkbook(fileInputStream);
	    XSSFSheet sheet = (XSSFSheet) workbook.getSheet(tabName);
	    Iterator<Row> rowIt = sheet.iterator();
	    Row header = rowIt.next();
	    
	    while(rowIt.hasNext()) 
	    {
	      Iterator<Cell> cellHeaderIterator = header.cellIterator();
	      Row row = rowIt.next();
	      Iterator<Cell> cellIterator = row.cellIterator();
	      while (cellIterator.hasNext() && cellHeaderIterator.hasNext()) 
	      {
	    	  Cell cellHeader = cellHeaderIterator.next();
	          Cell cell = cellIterator.next();
	          map.put(cellHeader.toString().trim(), cell.toString().trim());
	      }
	      System.out.println(map);
	      allPool.add(new LinkedHashMap<String, String>(map));
	      map.clear();
	    }
	    workbook.close();
	    fileInputStream.close();
	    if(!"".equalsIgnoreCase(filter))
	    {
		    filteredTestCases = filterArrayList(allPool, filter);
			return filteredTestCases;
	    }else
	    {
	    	return allPool;
	    }
	}
	
	/**
	 * This read GuiMap file into hashmap
	 * @param guiPool: file path
	 * @throws Exception 
	 * 
	 */
	public static LinkedHashMap<String, LinkedHashMap<String, String>> 
	readGuiMap(ArrayList<LinkedHashMap<String, String>> guiPool) throws IOException
	{
		LinkedHashMap<String, LinkedHashMap<String, String>> guiMap = 
				new LinkedHashMap<String, LinkedHashMap<String, String>>();
		String filedTag;
		@SuppressWarnings("unused")
		String locType;
		@SuppressWarnings("unused")
		String locValue;
		for(HashMap<String, String> map : guiPool)
		{
			filedTag = map.get("field_tag");
			locType = map.get("locator_type");
			locValue = map.get("locator_value");
			//tempMap.put(locType, locValue);
			guiMap.put(filedTag, new LinkedHashMap<String, String>(map));
			//tempMap.clear();
		}
		return guiMap;
	}
	/**
	 * This method read an arraylist
	 * @param inputArrayList: input list
	 * @param filterWith filter
	 * @throws Exception 
	 * 
	 */
	public static ArrayList<LinkedHashMap<String, String>> filterArrayList(
				  ArrayList<LinkedHashMap<String, String>> inputArrayList,
				  String filterWith)
	{
		ArrayList<LinkedHashMap<String, String>> outputArrayList = 
					new ArrayList<LinkedHashMap<String, String>>();
		String [] filters = filterWith.split("=");
		
		for(int i = 0; i< inputArrayList.size(); i++)
		{
			if(filters[1].equalsIgnoreCase(inputArrayList.get(i).get(filters[0])))
			{
				outputArrayList.add(inputArrayList.get(i));
			}
		}
		return outputArrayList;
	}
}
