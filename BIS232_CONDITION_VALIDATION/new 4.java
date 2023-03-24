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
		System.out.println("");
		LinkedHashMap<String, String> record = new LinkedHashMap<String, String>();
		String sqlString = "select+Status__c+from+Investigation__c+where+id='"+investigationId+"'";
		//Prelim
		//1
		HtmlReport.addHtmlStepTitle("Validate Status - Prelim", "Title"); 
		String condition = "Investigation Outcome is not 'ITC Negative Prelim'or 'Petition Withdrawn "
				+ "After initiation' or 'Suspension Agreement' "
				+ "and if Published Date (Type: Preliminary) is blank then the status is true";		
		JSONObject jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Positive", "Prelim", jObj.getString("Status__c"), condition);
		
		//2
		condition ="Investigation Outcome is 'ITC Negative Prelim' or 'Petition Withdrawn After initiation' or "
				+ "'Suspension Agreement' and if Published Date (Type: Preliminary) is blank then the status is true";
		record.clear();
		record.put("Investigation_Outcome__c", "Suspension Agreement");
		//record.put("Actual_Final_Signature__c", todayStr);
		String code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Prelim", jObj.getString("Status__c"), condition);
		
		//3
		condition ="Investigation Outcome is not 'ITC Negative Prelim'or 'Petition Withdrawn After initiation' or 'Suspension"
				+ " Agreement' and if Published Date (Type: Preliminary) is not blank then the status is true";
		record.clear();
      	record.put("Investigation__c", investigationId);
		record.put("Published_Date__c", todayStr);
		record.put("Cite_Number__c", "None"); 
		record.put("Type__c", "Preliminary");
		String frIdP = APITools.createObjectRecord("Federal_Register__c", record);
		record.clear();
		record.put("Investigation_Outcome__c", "DOC Negative Final");
		record.put("Actual_Final_Signature__c", todayStr);
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Prelim", jObj.getString("Status__c"), condition);
		//4
		condition ="Investigation Outcome is not 'ITC Negative Prelim'or 'Petition Withdrawn After initiation' or 'Suspension Agreement'"
				+ " and if Published Date (Type: ITC Final) is not blank then the status is true";
		code = APITools.deleteRecordObject("Federal_Register__c", frIdP);
		record.clear();
      	record.put("Investigation__c", investigationId);
		record.put("Published_Date__c", todayStr);
		record.put("Cite_Number__c", "None");
		record.put("Type__c", "ITC Final");
		String frIdITCF = APITools.createObjectRecord("Federal_Register__c", record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Prelim", jObj.getString("Status__c"), condition);
		//Amend Prelim
		//1
		HtmlReport.addHtmlStepTitle("Validate Status - Amend Prelim", "Title"); 
		condition = "FR Published Date (Type: Preliminary) is not blank AND Will_You_Amend_the_Prelim_Determination is "
				+ "yes AND Actual_Amended_Prelim_Determination_Sig__c is blank AND Investigation Outcome is not"
				+ " ('ITC Negative Prelim' or 'Petition Withdrawn After Initiation' or 'Suspension Agreement') "
				+ "THEN status is true";
		code = APITools.deleteRecordObject("Federal_Register__c", frIdITCF);
		/*record.clear();
      	record.put("Investigation__c", investigationId);
		record.put("Published_Date__c", todayStr);
		record.put("Cite_Number__c", "None");
		record.put("Type__c", "Preliminary");
		frIdP = APITools.createObjectRecord("Federal_Register__c", record);*/
		record.clear();
       	record.put("Amend_the_Preliminary_Determination__c", "Yes");
		//record.put("Calculated_Preliminary_Signature__c", todayStr);
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Positive", "Amend Prelim", jObj.getString("Status__c"), condition);
		//2
		condition = "If the Published Date (Type: Preliminary) is blank AND Will_You_Amend_the_Prelim_Determination "
				+ "is yes AND Actual_Amended_Prelim_Determination_Sig__c is blank AND Investigation Outcome is "
				+ "not ('ITC Negative Prelim' or 'Petition Withdrawn After Initiation' or 'Suspension Agreement')";
		//code = APITools.deleteRecordObject("Federal_Register__c", frIdP);
		code = APITools.deleteRecordObject("Federal_Register__c", frIdITCF);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Amend Prelim", jObj.getString("Status__c"), condition);
		//3
		condition = "If the Published Date (Type: Preliminary) is blank AND Will_You_Amend_the_Prelim_Determination"
				+ " is yes AND Actual_Amended_Prelim_Determination_Sig__c is not blank AND Investigation Outcome  "
				+ "is not ('ITC Negative Prelim' or 'Petition Withdrawn After Initiation' or 'Suspension Agreement')";
		record.clear();
		record.put("Actual_Amended_Prelim_Determination_Sig__c", todayStr);
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Nagative", "Amend Prelim", jObj.getString("Status__c"), condition);
		//4
		condition = "If the Published Date (Type: Preliminary) is not blank AND Will_You_Amend_the_Prelim_Determination"
				+ " is yes AND Actual_Amended_Prelim_Determination_Sig__c is not blank AND Investigation Outcome "
				+ "is not ('ITC Negative Prelim' or 'Petition Withdrawn After Initiation' or 'Suspension Agreement')";
		record.clear();
      	record.put("Investigation__c", investigationId);
		record.put("Published_Date__c", todayStr);
		record.put("Cite_Number__c", "None");
		record.put("Type__c", "Preliminary");
		frIdP = APITools.createObjectRecord("Federal_Register__c", record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Amend Prelim", jObj.getString("Status__c"), condition);
		//5
		condition = "If the Published Date (Type: Preliminary) is not blank AND Will_You_Amend_the_Prelim_Determination"
				+ " is no AND Actual_Amended_Prelim_Determination_Sig__c is not blank AND Investigation Outcome  "
				+ "is not ('ITC Negative Prelim' or 'Petition Withdrawn After Initiation' or 'Suspension Agreement')";
		record.clear();
       	record.put("Amend_the_Preliminary_Determination__c", "No");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Amend Prelim", jObj.getString("Status__c"), condition);
		//6
		condition = "If the Published Date (Type: Preliminary) is not blank AND Will_You_Amend_the_Prelim_Determination"
				+ " is no AND Actual_Amended_Prelim_Determination_Sig__c is blank AND Investigation Outcome "
				+ "is not ('ITC Negative Prelim' or 'Petition Withdrawn After Initiation' or 'Suspension Agreement')";
		record.clear();
		record.put("Actual_Amended_Prelim_Determination_Sig__c", "");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Amend Prelim", jObj.getString("Status__c"), condition);
		//7
		condition = "If the Published Date (Type: Preliminary) is not blank AND Will_You_Amend_the_Prelim_Determination"
				+ " is no AND Actual_Amended_Prelim_Determination_Sig__c is blank AND Investigation Outcome is"
				+ " ('ITC Negative Prelim' or 'Petition Withdrawn After Initiation' or 'Suspension Agreement')";
		record.clear();
		record.put("Investigation_Outcome__c", "Suspension Agreement");
		record.put("Actual_Final_Signature__c", "");
		//record.put("Actual_Final_Signature__c", todayStr);
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Amend Prelim", jObj.getString("Status__c"), condition);
		//8
		condition = "FR Published Date (Type: Preliminary) is not blank AND Will_You_Amend_the_Prelim_Determination"
				+ " is yes AND Actual_Amended_Prelim_Determination_Sig__c is blank AND Investigation Outcome "
				+ "is ('ITC Negative Prelim' or 'Petition Withdrawn After Initiation' or 'Suspension Agreement') THEN status is true";
		record.clear();
       	record.put("Amend_the_Preliminary_Determination__c", "Yes");
		//record.put("Calculated_Preliminary_Signature__c", todayStr);
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Amend Prelim", jObj.getString("Status__c"), condition);
		//9
		condition = "FR Published Date (Type: Final) is not blank AND Will_You_Amend_the_Prelim_Determination"
				+ " is yes AND Actual_Amended_Prelim_Determination_Sig__c is blank AND Investigation Outcome"
				+ " is ('ITC Negative Prelim' or 'Petition Withdrawn After Initiation' or 'Suspension Agreement')"
				+ " THEN status is true";
		record.clear();
      	record.put("Investigation__c", investigationId);
		record.put("Published_Date__c", todayStr);
		record.put("Cite_Number__c", "None");
		record.put("Type__c", "Final");
		String frIdF = APITools.createObjectRecord("Federal_Register__c", record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Amend Prelim", jObj.getString("Status__c"), condition);
		
		
		
		
		
		
		
		
		
		
		//Final
		//1
		HtmlReport.addHtmlStepTitle("Validate Status - Final", "Title");
		condition = "IF the Published Date (Type: final) is blank AND Actual_Preliminary_Signature is not blank AND Published_Date_c "
				+ "(Type: Preliminary) is not blank AND Actual_Amended_Prelim_Determination_Sig__c is not blank AND Investigation Outcome "
				+ " is not ('ITC Negative Prelim' or 'Petition Withdrawn After Initiation' or 'Suspension Agreement') THEN Status is true";
		code = APITools.deleteRecordObject("Federal_Register__c", frIdF);
		record.clear();
		record.put("Actual_Preliminary_Signature__c", todayStr);
       	record.put("Actual_Amended_Prelim_Determination_Sig__c", todayStr);
		record.put("Calculated_Preliminary_Signature__c", todayStr); 
		record.put("Investigation_Outcome__c", "");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Positive", "Final", jObj.getString("Status__c"), condition);
		
		//2
		condition = "IF the Published Date (Type: final) is not blank AND Actual_Preliminary_Signature is not "
				+ "blank AND Published_Date_c (Type: Preliminary) is not blank AND Actual_Amended_Prelim_Determination_Sig__c"
				+ " is not blank AND Investigation Outcome is not ('ITC Negative Prelim' or 'Petition Withdrawn "
				+ "After Initiation' or 'Suspension Agreement') THEN Status is true";
		record.clear();
      	record.put("Investigation__c", investigationId);
		record.put("Published_Date__c", todayStr);
		record.put("Cite_Number__c", "None");
		record.put("Type__c", "Final");
		frIdF = APITools.createObjectRecord("Federal_Register__c", record);
		record.clear();
		record.put("Actual_Amended_Prelim_Determination_Sig__c", todayStr);
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Final", jObj.getString("Status__c"), condition);
		//3
		condition = "IF the Published Date (Type: final) is blank AND Actual_Preliminary_Signature is blank AND"
				+ "Published_Date_c (Type: Preliminary) is not blank AND Actual_Amended_Prelim_Determination_Sig__c "
				+ "is not blank AND Investigation Outcome  is not ('ITC Negative Prelim' or 'Petition Withdrawn After"
				+ " Initiation' or 'Suspension Agreement') THEN Status is true";
		code = APITools.deleteRecordObject("Federal_Register__c", frIdF);
		record.clear();
		record.put("Actual_Preliminary_Signature__c", "");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Final", jObj.getString("Status__c"), condition);
		//4
		condition = "IF the Published Date (Type: final) is blank AND Actual_Preliminary_Signature is not "
				+ "blank AND Published_Date_c (Type: Preliminary) is blank AND Actual_Amended_Prelim_Determination_Sig__c"
				+ " is not blank AND Investigation Outcome  is not ('ITC Negative Prelim' or 'Petition Withdrawn "
				+ "After Initiation' or 'Suspension Agreement') THEN Status is true";
		code = APITools.deleteRecordObject("Federal_Register__c", frIdP);
		record.clear();
		record.put("Actual_Preliminary_Signature__c", todayStr);
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Final", jObj.getString("Status__c"), condition);
		//5
		condition = "IF the Published Date (Type: final) is blank AND Actual_Preliminary_Signature "
				+ "is not blank AND Published_Date_c (Type: Preliminary) is not blank AND Actual_Amended_Prelim_Determination_Sig__c"
				+ " is blank AND Investigation Outcome  is not ('ITC Negative Prelim' or 'Petition Withdrawn After"
				+ " Initiation' or 'Suspension Agreement') THEN Status is true";
		record.clear();
      	record.put("Investigation__c", investigationId);
		record.put("Published_Date__c", todayStr);
		record.put("Cite_Number__c", "None");
		record.put("Type__c", "Preliminary");
		frIdP = APITools.createObjectRecord("Federal_Register__c", record);
		record.clear();
		record.put("Actual_Amended_Prelim_Determination_Sig__c", "");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Final", jObj.getString("Status__c"), condition);
		
		//6
		condition = "IF the Published Date (Type: final) is blank AND Actual_Preliminary_Signature "
				+ "is not blank AND Published_Date_c (Type: Preliminary) is not blank AND Actual_Amended_Prelim_Determination_Sig__c"
				+ " is not blank AND Investigation Outcome  is ('ITC Negative Prelim' or 'Petition Withdrawn After "
				+ "Initiation' or 'Suspension Agreement') THEN Status is true";
		record.clear();
		record.put("Actual_Amended_Prelim_Determination_Sig__c", todayStr);
		record.put("Investigation_Outcome__c", "Suspension Agreement");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Final", jObj.getString("Status__c"), condition);
		//7
		condition = "IF the Published Date (Type: ITC Final) is blank AND Actual_Preliminary_Signature "
				+ "is not blank AND Published_Date_c (Type: Preliminary) is not blank AND Actual_Amended_Prelim_Determination_Sig__c"
				+ " is not blank AND Investigation Outcome  is not ('ITC Negative Prelim' or 'Petition Withdrawn "
				+ "After Initiation' or 'Suspension Agreement') THEN Status is true";
		record.clear();
		record.put("Actual_Amended_Prelim_Determination_Sig__c", todayStr);
		record.put("Actual_Preliminary_Signature__c", todayStr);
		record.put("Investigation_Outcome__c", "");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Final", jObj.getString("Status__c"), condition);
		
		
		//8
		/*condition = "IF the Published Date (Type: final) is blank AND Actual_Preliminary_Signature "
				+ "is not blank AND Published_Date_c (Type: preliminary) is not blank AND Actual_Amended_Prelim_Determination_Sig__c "
				+ "is not blank AND Investigation Outcome  is not ('ITC Negative Prelim' or 'Petition Withdrawn "
				+ "After Initiation' or 'Suspension Agreement') THEN Status is true";
		record.clear();
		record.put("Actual_Amended_Prelim_Determination_Sig__c", todayStr);
		record.put("Actual_Preliminary_Signature__c", todayStr);
		record.put("Investigation_Outcome__c", "");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Final", jObj.getString("Status__c"), condition);*/
		//9
		condition = "IF  Publication Date (Type: final) is  blank AND Actual_Preliminary_Signature "
				+ "is not blank AND Published Date (Type: Preliminary) is not blank AND Investigation "
				+ "Outcome is not ('ITC Negative Prelim' or 'Petition Withdrawn After Initiation' or 'Suspension Agreement')"
				+ " AND Will_You_Amend_the_Prelim_Determination is No AND Actual_Amended_Prelim_Determination_Sig__c "
				+ "is blank THEN status is true";
		record.clear();
		record.put("Amend_the_Preliminary_Determination__c", "No");
		record.put("Investigation_Outcome__c", "Suspension Agreement");
		record.put("Actual_Amended_Prelim_Determination_Sig__c", "");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
				ADCVDLib.validateObjectStatus("Positive", "Final", jObj.getString("Status__c"), condition);
		
		//10
		condition = "IF  Publication Date (Type: final) is not blank AND Actual_Preliminary_Signature "
				+ "is not blank AND Published Date (Type: Preliminary) is not blank AND Investigation Outcome "
				+ "is not ('ITC Negative Prelim' or 'Petition Withdrawn After Initiation' or 'Suspension Agreement') "
				+ "AND Will_You_Amend_the_Prelim_Determination is No AND Actual_Amended_Prelim_Determination_Sig__c "
				+ "is blank THEN status is true";
		record.clear();
      	record.put("Investigation__c", investigationId);
		record.put("Published_Date__c", todayStr);
		record.put("Cite_Number__c", "None");
		record.put("Type__c", "Final");
		frIdF = APITools.createObjectRecord("Federal_Register__c", record);
		record.clear();
		record.put("Actual_Preliminary_Signature__c", todayStr);
       	record.put("Actual_Amended_Prelim_Determination_Sig__c", "");
		record.put("Investigation_Outcome__c", "DOC Negative Final");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
				ADCVDLib.validateObjectStatus("Negative", "Final", jObj.getString("Status__c"), condition);
		//11
		condition = "IF  Publication Date (Type: final) is  blank AND Actual_Preliminary_Signature is blank AND Published Date"
				+ " (Type: Preliminary) is not blank AND Investigation Outcome is not ('ITC Negative Prelim' or 'Petition "
				+ "Withdrawn After Initiation' or 'Suspension Agreement') AND	Will_You_Amend_the_Prelim_Determination is No"
				+ " AND Actual_Amended_Prelim_Determination_Sig__c is blank	THEN status is true";
		code = APITools.deleteRecordObject("Federal_Register__c", frIdF);
		record.clear();
		record.put("Actual_Preliminary_Signature__c", "");
       	record.put("Actual_Amended_Prelim_Determination_Sig__c", "");
		record.put("Investigation_Outcome__c", "");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
				ADCVDLib.validateObjectStatus("Negative", "Final", jObj.getString("Status__c"), condition);
		//12
		condition = "IF  Publication Date (Type: final) is  blank AND Actual_Preliminary_Signature is not blank AND Published "
				+ "Date (Type: Preliminary) is blank AND Investigation Outcome is not ('ITC Negative Prelim' or 'Petition Withdrawn "
				+ "After Initiation' or 'Suspension Agreement') AND Will_You_Amend_the_Prelim_Determination is No AND "
				+ "Actual_Amended_Prelim_Determination_Sig__c is blank THEN status is true";
		code = APITools.deleteRecordObject("Federal_Register__c", frIdP);
		record.clear();
		record.put("Actual_Preliminary_Signature__c", todayStr);
       	record.put("Actual_Amended_Prelim_Determination_Sig__c", "");
		record.put("Investigation_Outcome__c", "");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
				ADCVDLib.validateObjectStatus("Negative", "Final", jObj.getString("Status__c"), condition);
		
		//13
		condition = "IF  Publication Date (Type: final) is  blank AND Actual_Preliminary_Signature is not blank AND Published "
				+ "Date (Type: Preliminary) is not blank AND Investigation Outcome is ('ITC Negative Prelim' or 'Petition"
				+ " Withdrawn After Initiation' or 'Suspension Agreement') AND Will_You_Amend_the_Prelim_Determination is No "
				+ "AND Actual_Amended_Prelim_Determination_Sig__c is blank THEN status is true";
		record.clear();
      	record.put("Investigation__c", investigationId);
		record.put("Published_Date__c", todayStr);
		record.put("Cite_Number__c", "None");
		record.put("Type__c", "Preliminary");
		frIdP = APITools.createObjectRecord("Federal_Register__c", record);
		record.clear();
		record.put("Actual_Preliminary_Signature__c", todayStr);
       	record.put("Actual_Amended_Prelim_Determination_Sig__c", "");
		record.put("Investigation_Outcome__c", "Suspension Agreement");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
				ADCVDLib.validateObjectStatus("Negative", "Final", jObj.getString("Status__c"), condition);
		//14
		condition = "IF  Publication Date (Type: final) is  blank AND Actual_Preliminary_Signature is not blank AND Published "
				+ "Date (Type: Preliminary) is not blank AND Investigation Outcome is not ('ITC Negative Prelim' or 'Petition "
				+ "Withdrawn After Initiation' or 'Suspension Agreement') AND Will_You_Amend_the_Prelim_Determination is YES "
				+ "AND Actual_Amended_Prelim_Determination_Sig__c is blank THEN status is true";
		record.clear();
		record.put("Actual_Preliminary_Signature__c", todayStr);
       	record.put("Actual_Amended_Prelim_Determination_Sig__c", "");
       	record.put("Amend_the_Preliminary_Determination__c", "Yes");
		record.put("Investigation_Outcome__c", "");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
				ADCVDLib.validateObjectStatus("Negative", "Final", jObj.getString("Status__c"), condition);
		//15
		condition = "IF  Publication Date (Type: final) is  blank AND Actual_Preliminary_Signature is not blank AND Published "
				+ "Date (Type: Preliminary) is not blank AND Investigation Outcome is not ('ITC Negative Prelim' or 'Petition "
				+ "Withdrawn After Initiation' or 'Suspension Agreement') AND Will_You_Amend_the_Prelim_Determination is No AND "
				+ "Actual_Amended_Prelim_Determination_Sig__c is not  blank	THEN status is true";
		record.clear();
		record.put("Actual_Preliminary_Signature__c", todayStr);
       	record.put("Actual_Amended_Prelim_Determination_Sig__c", todayStr);
       	record.put("Amend_the_Preliminary_Determination__c", "No");
		record.put("Investigation_Outcome__c", "");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
				ADCVDLib.validateObjectStatus("Negative", "Final", jObj.getString("Status__c"), condition);
		//16
		condition = "IF  Publication Date (Type: Initiation) is  blank AND Actual_Preliminary_Signature is not blank AND "
				+ "Published Date (Type: Preliminary) is not blank AND Investigation Outcome is not ('ITC Negative Prelim'"
				+ " or 'Petition Withdrawn After Initiation' or 'Suspension Agreement') AND Will_You_Amend_the_Prelim_Determination "
				+ "is No AND Actual_Amended_Prelim_Determination_Sig__c is blank THEN status is true";
		record.clear();
		record.put("Actual_Preliminary_Signature__c", todayStr);
       	record.put("Actual_Amended_Prelim_Determination_Sig__c", "");
       	record.put("Amend_the_Preliminary_Determination__c", "No");
		record.put("Investigation_Outcome__c", "");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
				ADCVDLib.validateObjectStatus("Negative", "Final", jObj.getString("Status__c"), condition);
		
		//17
		condition = "IF  Publication Date (Type: final) is  blank AND Actual_Preliminary_Signature is not blank AND"
				+ " Published Date (Type: preliminary) is not blank AND Investigation Outcome is not ('ITC Negative Prelim' "
				+ "or 'Petition Withdrawn After Initiation' or 'Suspension Agreement') AND Will_You_Amend_the_Prelim_Determination "
				+ "is No AND Actual_Amended_Prelim_Determination_Sig__c is blank THEN status is true";
		record.clear();
		record.put("Actual_Preliminary_Signature__c", todayStr);
       	record.put("Actual_Amended_Prelim_Determination_Sig__c", "");
       	record.put("Amend_the_Preliminary_Determination__c", "No");
		record.put("Investigation_Outcome__c", "");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
				ADCVDLib.validateObjectStatus("Negative", "Final", jObj.getString("Status__c"), condition);
		
		
		
		
		
		//Pending Order
		HtmlReport.addHtmlStepTitle("Validate Status - Pending Order", "Title");
		condition = "IF Published_Date__c (Type: Preliminary) is not blank AND Published_Date__c (Type: Final) is not blank "
				+ "AND Actual_Preliminary_Signature__c is not blank AND Actual_Final_Signature__c is not blank "
				+ "AND Investigation_Outcome__c is null THEN status is true";
		record.clear();
      	record.put("Investigation__c", investigationId);
		record.put("Published_Date__c", todayStr);
		record.put("Cite_Number__c", "None");
		record.put("Type__c", "Final");
		frIdF = APITools.createObjectRecord("Federal_Register__c", record);
		record.clear();
      	record.put("Investigation__c", investigationId);
		record.put("Published_Date__c", todayStr);
		record.put("Cite_Number__c", "None");
		record.put("Type__c", "Preliminary");
		frIdP = APITools.createObjectRecord("Federal_Register__c", record);
		record.clear();
		record.put("Actual_Final_Signature__c", todayStr);
		record.put("Actual_Preliminary_Signature__c", todayStr);
		record.put("Investigation_Outcome__c", "");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Positive", "Pending Order", jObj.getString("Status__c"), condition);
		
		
		//2
		condition = "The FR Published Date (Type: Preliminary) is blank AND Published Date (Type: Final) "
				+ "is not blank AND Investigation Outcome is null THEN status is true";
		code = APITools.deleteRecordObject("Federal_Register__c", frIdP);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Pending Order", jObj.getString("Status__c"), condition);
		//3
		condition = "The FR Published Date (Type: Preliminary) is not blank AND Published Date (Type: Final) "
				+ "is blank AND Investigation Outcome is null THEN status is true";
		code = APITools.deleteRecordObject("Federal_Register__c", frIdF);
		record.clear();
      	record.put("Investigation__c", investigationId);
		record.put("Published_Date__c", todayStr);
		record.put("Cite_Number__c", "None");
		record.put("Type__c", "Preliminary");
		frIdF = APITools.createObjectRecord("Federal_Register__c", record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Pending Order", jObj.getString("Status__c"), condition);
		
		//4
		condition = "The FR Published Date (Type: Preliminary) is not blank AND Published Date (Type: ITC Final)"
				+ " is not blank AND Investigation Outcome is null THEN status is true";
		record.clear();
      	record.put("Investigation__c", investigationId);
		record.put("Published_Date__c", todayStr);
		record.put("Cite_Number__c", "None");
		record.put("Type__c", "Final");
		frIdF = APITools.createObjectRecord("Federal_Register__c", record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Pending Order", jObj.getString("Status__c"), condition);
		
		//5
		condition = "The FR Published Date (Type: ITC prelim) is not blank AND Published Date (Type: Final) "
				+ "is not blank AND Investigation Outcome is null THEN status is true";
		record.clear();
      	record.put("Investigation__c", investigationId);
		record.put("Published_Date__c", todayStr);
		record.put("Cite_Number__c", "None");
		record.put("Type__c", "ITC Preliminary");
		String frIdITCP = APITools.createObjectRecord("Federal_Register__c", record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Pending Order", jObj.getString("Status__c"), condition);
		//6
		condition = "The FR Published Date (Type: Preliminary) is not blank AND Published Date (Type: Final) "
				+ "is not blank AND Investigation Outcome is not null THEN status is true";
		code = APITools.deleteRecordObject("Federal_Register__c", frIdITCP);
		record.clear();
		record.put("Investigation_Outcome__c", "Suspension Agreement");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Pending Order", jObj.getString("Status__c"), condition);
		
		//7
		condition = "The FR Published Date (Type: Preliminary) is blank AND Published Date (Type: Final) "
				+ "is blank AND Investigation Outcome is null THEN status is true";
		code = APITools.deleteRecordObject("Federal_Register__c", frIdF);
		code = APITools.deleteRecordObject("Federal_Register__c", frIdP);
		record.clear();
		record.put("Investigation_Outcome__c", "");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Pending Order", jObj.getString("Status__c"), condition);
		
		
		
		
		
		
		
		
		
		
		
		
		//Suspended
		//1
		HtmlReport.addHtmlStepTitle("Validate Status - Suspended", "Title");
		condition = "The Investigation Outcome is 'Suspension Agreement' THEN Status is true";
		record.clear();
		record.put("Investigation_Outcome__c", "Suspension Agreement");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Positive", "Suspended", jObj.getString("Status__c"), condition);
		
		//2
		condition = "IF the Investigation Outcome is not 'Suspension Agreement' THEN Status is true";
		record.clear();
		record.put("Investigation_Outcome__c", "");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Suspended", jObj.getString("Status__c"), condition);
		
		
		
		
		
		
		
		//Hold
		HtmlReport.addHtmlStepTitle("Validate Status - Hold", "Title");
		condition = "IF The Litigation Picklist is Null AND the Investigation Outcome is “ITC Prelim” THEN Published Date "
				+ "(Type: ITC Prelim) + 30 or 45 days AND status true";
		condition = "The Investigation Outcome is 'Suspension Agreement' THEN Status is true";
		
		todayCal.setTime(todayDate);
		todayCal.add(Calendar.DATE, 46);
		record.clear();
		record.put("segment__c", investigationId);
		record.put("Published_Date__c", dateFormat.format(todayCal.getTime())); 
		record.put("Cite_Number__c", "None");
		record.put("Type__c", "ITC Prelim");
		String frIdITC = APITools.createObjectRecord("Federal_Register__c", record);
		
		record.clear();
		record.put("Investigation_Outcome__c", "ITC Preliminary");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Hold", jObj.getString("Status__c"), condition);
		
		
		//2
		condition = "IF The Litigation Picklist is not Null AND the Investigation Outcome is “ITC Negative Prelim” THEN"
				+ " Published Date (Type:  ITC Prelim) + 30 or 45 days AND status true";
		record.clear();
		record.put("Litigation_YesNo__c", "Yes");
		record.put("Litigation_Resolved__c", "No");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Hold", jObj.getString("Status__c"), condition);
		
		//3
		condition = "IF The Litigation Picklist is Null AND the Investigation Outcome is not “ITC Negative Prelim” THEN "
				+ "Published Date (Type:  ITC Prelim) + 30 or 45 days AND status true";
		record.clear();
		record.put("Litigation_YesNo__c", "");
		record.put("Litigation_Resolved__c", "");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Hold", jObj.getString("Status__c"), condition);
		
		//4
		condition = "IF The Litigation Picklist is Null AND the Investigation Outcome is “ITC Negative Prelim” "
				+ "THEN Published Date (Type:  Final) + 30 or 45 days AND status true";
		code = APITools.deleteRecordObject("Federal_Register__c", frIdITC);
		todayCal.setTime(todayDate);
		todayCal.add(Calendar.DATE, 46);
		record.clear();
		record.put("segment__c", investigationId);
		record.put("Published_Date__c", dateFormat.format(todayCal.getTime())); 
		record.put("Cite_Number__c", "None");
		record.put("Type__c", "Final");
		frIdF = APITools.createObjectRecord("Federal_Register__c", record);
		
		record.clear();
		record.put("Litigation_YesNo__c", "");
		record.put("Litigation_Resolved__c", "");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Nagative", "Hold", jObj.getString("Status__c"), condition);
		/*
		//5
		condition = "IF The Litigation Picklist is not Null AND the Investigation Outcome is not “ITC Negative Prelim”"
				+ " THEN Published Date (Type:  ITC Prelim) + 30 or 45 days AND status true";
		//6
		condition = "IF the Litigation picklist is Null AND Investigation Outcome is “ITC Negative Final” "
				+ "THEN Published Date (Type:  ITC Final) +30 or 45 days AND status is true";
		//7
		condition = "IF the Litigation picklist is not Null AND Investigation Outcome is “ITC Negative Final” "
				+ "THEN Published Date (Type:  ITC Final) +30 or 45 days AND status is true";
		//8
		condition = "IF the Litigation picklist is Null AND Investigation Outcome is not “ITC Negative Final” "
				+ "THEN Published Date (Type:  ITC Final) +30 or 45 days AND status is true";
		//9
		condition = "IF the Litigation picklist is Null AND Investigation Outcome is “ITC Negative Final” "
				+ "THEN Published Date (Type:  Initiation) +30 or 45 days AND status is true";
		//10
		condition = "IF the Litigation picklist is not Null AND Investigation Outcome is not “ITC Negative Final” "
				+ "THEN Published Date (Type:  ITC Final) +30 or 45 days AND status is true";
		//11
		condition = "IF The Litigation picklist is Null AND Petition_Withdrawn is not null AND Investigation "
				+ "Outcome is “Petition Withdrawn” THEN Petition_Withdrawn + 30 or 45 days AND status is true";
		//12
		condition = "IF The Litigation picklist is not  Null AND Petition_Withdrawn is not null AND Investigation"
				+ " Outcome is “Petition Withdrawn” THEN Petition_Withdrawn + 30 or 45 days AND status is true";
		//13
		condition = "IF The Litigation picklist is Null AND Petition_Withdrawn is null AND Investigation "
				+ "Outcome is “Petition Withdrawn” THEN Petition_Withdrawn + 30 or 45 days AND status is true";
		//14
		condition = "IF The Litigation picklist is Null AND Petition_Withdrawn is not null AND Investigation "
				+ "Outcome is not “Petition Withdrawn” THEN Petition_Withdrawn + 30 or 45 days AND status is true";
		//15
		condition = "IF The Litigation picklist is not Null AND Petition_Withdrawn is null AND Investigation "
				+ "Outcome is not “Petition Withdrawn” THEN Petition_Withdrawn + 30 or 45 days AND status is true";
		//16
		condition = "IF the Litigation picklist is Null AND Investigation Outcome is “DOC Negative Final” THEN  "
				+ "Actual_Final_Signature + 30 or 45 days AND status is true";
		//17
		condition = "IF the Litigation picklist is not Null AND Investigation Outcome is “DOC Negative Final”"
				+ " THEN  Actual_Final_Signature + 30 or 45 days AND status is true";
		//18
		condition = "IF the Litigation picklist is Null AND Investigation Outcome is not “DOC Negative Final” "
				+ "THEN  Actual_Final_Signature + 30 or 45 days AND status is true";
		//19
		condition = "IF the Litigation picklist is not Null AND Investigation Outcome is not “DOC Negative Final”"
				+ " THEN  Actual_Final_Signature + 30 or 45 days AND status is true";*/

		
		
		
		
		
		
		//Litigation
		//1
		HtmlReport.addHtmlStepTitle("Validate Status - Litigation", "Title");
		condition = "IF the Litigation picklist is Yes AND Litigation_Resolved is No AND Litigation_Status is 'blank' OR Litigation_Status "
				+ "is “Not Active” THEN status is true  ";
		record.clear();
		record.put("Litigation_YesNo__c", "Yes");
		record.put("Litigation_Resolved__c", "No");
		record.put("Litigation_Status__c", "");		
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Positive", "Litigation", jObj.getString("Status__c"), condition);
		
		//2
		condition = "IF the Litigation picklist is NO AND Litigation_Resolved is No AND Litigation_Status is 'blank' OR "
				+ "Litigation_Status is “Not Active” THEN status is true";
		record.clear();
		record.put("Litigation_YesNo__c", "No");
		record.put("Litigation_Resolved__c", "No");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Litigation", jObj.getString("Status__c"), condition);
		
		//3
		condition = "IF the Litigation picklist is Yes AND Litigation_Resolved is YES AND Litigation_Status is 'blank' OR "
				+ "Litigation_Status is “Not Active” THEN status is true";
		record.clear();
		record.put("Litigation_YesNo__c", "Yes");
		record.put("Litigation_Resolved__c", "Yes");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Litigation", jObj.getString("Status__c"), condition);
		
		//4
		condition = "IF the Litigation picklist is NO AND Litigation_Resolved is YES AND Litigation_Status is 'blank' OR "
				+ "Litigation_Status is “Not Active” THEN status is true";
		record.clear();
		record.put("Litigation_YesNo__c", "No");
		record.put("Litigation_Resolved__c", "Yes");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Litigation", jObj.getString("Status__c"), condition);
		
		//5
		condition = "IF the Litigation picklist is Yes AND Litigation_Resolved is No AND Litigation_Status is not 'blank' OR "
				+ "Litigation_Status is “Active” THEN status is true";
		record.clear();
		record.put("Litigation_YesNo__c", "No");
		record.put("Litigation_Resolved__c", "No");
		record.put("Litigation_Status__c", "Active");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Litigation", jObj.getString("Status__c"), condition);
		
		
		
		
		
		
		//Customs
		//1
		HtmlReport.addHtmlStepTitle("Validate Status - Customs", "Title");
		condition = "IF the Litigation picklist is Yes AND Litigation_Resolved is Yes AND Have_Custom_Instruction_been_sent "
				+ "is No THEN status is true";
		record.clear();
		record.put("Litigation_YesNo__c", "Yes");
		record.put("Litigation_Resolved__c", "Yes");
		record.put("Have_Custom_Instruction_been_sent__c", "No");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Positive", "Customs", jObj.getString("Status__c"), condition);


		
		//2
		condition = "IF the Litigation picklist is NO AND Litigation_Resolved is Yes AND Have_Custom_Instruction_been_sent "
				+ "is No THEN status is true";
		record.clear();
		record.put("Litigation_YesNo__c", "No");
		record.put("Litigation_Resolved__c", "Yes");
		record.put("Have_Custom_Instruction_been_sent__c", "No");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Customs", jObj.getString("Status__c"), condition);
		//3
		condition = "IF the Litigation picklist is Yes AND Litigation_Resolved is NO AND Have_Custom_Instruction_been_sent "
				+ "is No THEN status is true";
		record.clear();
		record.put("Litigation_YesNo__c", "Yes");
		record.put("Litigation_Resolved__c", "No");
		record.put("Have_Custom_Instruction_been_sent__c", "No");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Customs", jObj.getString("Status__c"), condition);
		//4
		condition = "IF the Litigation picklist is NO AND Litigation_Resolved is NO AND Have_Custom_Instruction_been_sent "
				+ "is No THEN status is true";
		record.clear();
		record.put("Litigation_YesNo__c", "No");
		record.put("Litigation_Resolved__c", "No");
		record.put("Have_Custom_Instruction_been_sent__c", "No");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Customs", jObj.getString("Status__c"), condition);
		//5
		condition = "IF the Litigation picklist is Yes AND Litigation_Resolved is Yes AND Have_Custom_Instruction_been_sent "
				+ "is YES THEN status is true";
		record.clear();
		record.put("Litigation_YesNo__c", "Yes");
		record.put("Litigation_Resolved__c", "Yes");
		record.put("Have_Custom_Instruction_been_sent__c", "Yes");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Customs", jObj.getString("Status__c"), condition);
		//6
		condition = "IF the Litigation picklist is No AND Have_Custom_Instruction_been_sent is No THEN status is true";
		record.clear();
		record.put("Litigation_YesNo__c", "No");
		record.put("Litigation_Resolved__c", "");
		record.put("Have_Custom_Instruction_been_sent__c", "No");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("positive", "Customs", jObj.getString("Status__c"), condition);
		//7
		condition = "IF the Litigation picklist is YES AND Have_Custom_Instruction_been_sent is No THEN status is true";
		record.clear();
		record.put("Litigation_YesNo__c", "Yes");
		record.put("Litigation_Resolved__c", "");
		record.put("Have_Custom_Instruction_been_sent__c", "No");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Customs", jObj.getString("Status__c"), condition);
		//8
		condition = "IF the Litigation picklist is No AND Have_Custom_Instruction_been_sent is YES THEN status is true";
		record.clear();
		record.put("Litigation_YesNo__c", "No");
		record.put("Litigation_Resolved__c", "");
		record.put("Have_Custom_Instruction_been_sent__c", "Yes");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Customs", jObj.getString("Status__c"), condition);
		//9
		condition = "IF the Litigation picklist is YES AND Have_Custom_Instruction_been_sent is YES THEN status is true";
		record.clear();
		record.put("Litigation_YesNo__c", "Yes");
		record.put("Litigation_Resolved__c", "");
		record.put("Have_Custom_Instruction_been_sent__c", "Yes");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Customs", jObj.getString("Status__c"), condition);

		
		
		//Closed
		//1
		HtmlReport.addHtmlStepTitle("Validate Status - Closed", "Title");
		condition = "IF the Litigation picklist is Yes AND Litigation_Resolved is Yes AND Have_Custom_Instruction_been_sent "
				+ "is Yes THEN status is true";
		record.clear();
		record.put("Litigation_YesNo__c", "Yes");
		record.put("Litigation_Resolved__c", "Yes");
		record.put("Have_Custom_Instruction_been_sent__c", "Yes");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Positive", "Closed", jObj.getString("Status__c"), condition);
		//2
		condition = "IF the Litigation picklist is NO AND Litigation_Resolved is Yes AND Have_Custom_Instruction_been_sent "
				+ "is Yes THEN status is true";
		record.clear();
		record.put("Litigation_YesNo__c", "No");
		record.put("Litigation_Resolved__c", "Yes");
		record.put("Have_Custom_Instruction_been_sent__c", "Yes");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Closed", jObj.getString("Status__c"), condition);
		//3
		condition = "IF the Litigation picklist is Yes AND Litigation_Resolved is NO AND Have_Custom_Instruction_been_sent"
				+ " is Yes THEN status is true";
		record.clear();
		record.put("Litigation_YesNo__c", "Yes");
		record.put("Litigation_Resolved__c", "No");
		record.put("Have_Custom_Instruction_been_sent__c", "Yes");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Closed", jObj.getString("Status__c"), condition);
		//4
		condition = "IF the Litigation picklist is Yes AND Litigation_Resolved is Yes AND Have_Custom_Instruction_been_sent"
				+ " is NO THEN status is true";
		record.clear();
		record.put("Litigation_YesNo__c", "Yes");
		record.put("Litigation_Resolved__c", "Yes");
		record.put("Have_Custom_Instruction_been_sent__c", "No");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Closed", jObj.getString("Status__c"), condition);
		//5
		condition = "IF the Litigation picklist is No AND Have_Custom_Instruction_been_sent is Yes THEN status is true";
		record.clear();
		record.put("Litigation_YesNo__c", "No");
		record.put("Litigation_Resolved__c", "");
		record.put("Have_Custom_Instruction_been_sent__c", "Yes");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Positive", "Closed", jObj.getString("Status__c"), condition);
		//6
		condition = "IF the Litigation picklist is YES AND Have_Custom_Instruction_been_sent is Yes THEN status is true";
		record.clear();
		record.put("Litigation_YesNo__c", "Yes");
		record.put("Litigation_Resolved__c", "");
		record.put("Have_Custom_Instruction_been_sent__c", "Yes");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Closed", jObj.getString("Status__c"), condition);
		//7
		condition = "IF the Litigation picklist is No AND Have_Custom_Instruction_been_sent is NO THEN status is true";
		record.clear();
		record.put("Litigation_YesNo__c", "No");
		record.put("Litigation_Resolved__c", "");
		record.put("Have_Custom_Instruction_been_sent__c", "No");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Negative", "Closed", jObj.getString("Status__c"), condition);
		//8
		/*condition = "IF the Litigation picklist is No AND Have_Custom_Instruction_been_sent is NO THEN status is true";
		record.clear();
		record.put("Litigation_YesNo__c", "Yes");
		record.put("Litigation_Resolved__c", "Yes");
		record.put("Have_Custom_Instruction_been_sent__c", "Yes");
		code = APITools.updateRecordObject("Investigation__c", investigationId, record);
		jObj = APITools.getRecordFromObject(sqlString);
		match = match & 
		ADCVDLib.validateObjectStatus("Positive", "Closed", jObj.getString("Status__c"), condition);*/

		
		
		
		
		
		
		
		
		
		
		
		
		return match;
	}
	