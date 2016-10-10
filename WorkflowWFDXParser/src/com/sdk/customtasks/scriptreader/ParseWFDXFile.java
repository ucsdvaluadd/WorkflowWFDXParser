package com.sdk.customtasks.scriptreader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sdk.compare.gson.GSONCompareMain;

public class ParseWFDXFile {

	static HashMap<String, String> scriptHashMap = new HashMap<String, String>();
	// static String osName;
	// static File file_ScriptNew;
	static String sourceDir;
	static String outputFile;
	

	public static void main(String args[]) {

		GSONCompareMain gsonCompareMain = new GSONCompareMain();
		
		sourceDir = args[0];
		//sourceDir = "E:\\TestingCustomTaskFolder";
		outputFile = args[1];
		//outputFile="Output.html";

		// String directoryOfWorkflows = checkOS(sourceDir);

		// Creating Directory For Script Files
		/*
		 * if (osName.contains("Windows")) { File fileScript = new
		 * File(directoryOfWorkflows); String rootDirectory =
		 * fileScript.getParent();
		 * 
		 * file_ScriptNew = new File(rootDirectory + "WorkFlowScript"); if
		 * (!file_ScriptNew.exists()) { if (file_ScriptNew.mkdir()) {
		 * System.out.println("Directory Created"); } else {
		 * System.out.println("NO"); } } } else if (osName.contains("Linux")) {
		 * File fileScript = new File(directoryOfWorkflows); String
		 * rootDirectory = fileScript.getParent();
		 * 
		 * file_ScriptNew = new File(rootDirectory + "/WorkFlowScript"); if
		 * (!file_ScriptNew.exists()) { if (file_ScriptNew.mkdir()) {
		 * System.out.println("Directory Created"); } else {
		 * System.out.println("NO"); } } }
		 */
		// String dirOfScriptFiles = file_ScriptNew.getAbsolutePath();
		parseWFDXFile(sourceDir, outputFile);

	}

	/**
	 * 
	 * @param directoryOfWorkflows
	 *            : Name of the directory where all the workflows resides
	 * @param dirOfScriptFiles
	 *            : Name of the directory where all the script files to be
	 *            generated The below method reads all the workflows(wfdx files)
	 *            from the location :directoryOfWorkflows and generates
	 *            respective script file in the location :dirOfScriptFiles
	 */

	public static void parseWFDXFile(String directoryOfWorkflows, String outputFile) {
		GSONCompareMain gsonCompareMain = new GSONCompareMain();
		String executionScriptTag = null;
		String destOfScriptFiles = null;
		String task_data_tag = null;
		FileWriter fstreamForScriptFiles = null;
		String custom_tag = "CUSTOM_TASKS";
		// Fetching all files from the folder
		File[] filesInWorkFlowDir = new File(directoryOfWorkflows).listFiles();
		// Looping through all files and reading all files
		for (File file : filesInWorkFlowDir) {
			String fileNameWithExt = file.getName();
			System.out.println(fileNameWithExt);
			String fileNameWithoutExt = FilenameUtils.removeExtension(fileNameWithExt);

			if (file.isFile()) {

				// System.out.println(file.getAbsolutePath());
				// System.out.println(file.getName());

				File fileForXmlParse = new File(file.getAbsolutePath());
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = null;
				try {
					dBuilder = dbFactory.newDocumentBuilder();
				} catch (ParserConfigurationException e) {

					e.printStackTrace();
				}
				Document docXml = null;
				try {
					docXml = dBuilder.parse(fileForXmlParse);
					// System.out.println(docXml);
				} catch (SAXException e) {

					e.printStackTrace();
				} catch (IOException e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}

				docXml.getDocumentElement().normalize();
				NodeList nodeListInXml = docXml.getElementsByTagName("UnifiedFeatureAssetInfo");
				// Parsing XML for the tag UnifiedFeatureAssetInfo
				for (int temp = 0; temp < nodeListInXml.getLength(); temp++) {
					Node nNode = nodeListInXml.item(temp);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode; //
						String typeTag = eElement.getElementsByTagName("type").item(0).getTextContent();
		//				System.out.println("Data : " + typeTag);
						if (typeTag.equalsIgnoreCase("workflows")) {
							String featureAssetEntryTag = eElement.getElementsByTagName("featureAssetEntry").item(0)
									.getTextContent();
		//					System.out.println("workflow featureAssetEntry : " + featureAssetEntryTag);
							JSONObject obj_featureAssetEntryTag, obj_returnedString;
							try {
								obj_featureAssetEntryTag = new JSONObject(featureAssetEntryTag);
								task_data_tag = obj_featureAssetEntryTag.getString("data");
		//						System.out.println("Workflow data is :" + task_data_tag);
								String returned_string = new String(Base64.decodeBase64(task_data_tag.getBytes()));
		//						System.out.println("Returned String Workflow data is : " + returned_string);
								try {
									gsonCompareMain.readcustomActionDefinition(returned_string);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								obj_returnedString = new JSONObject(returned_string);
							} catch (JSONException e1) {
								e1.printStackTrace();
							}
						}

						if (typeTag.equals(custom_tag)) {
							String featureAssetEntryTag = eElement.getElementsByTagName("featureAssetEntry").item(0)
									.getTextContent();
		//					System.out.println("featureAssetEntry : " + featureAssetEntryTag);

							// Parsing the JSON OBJECT feature_asset_entry for
							// script
							JSONObject obj_featureAssetEntryTag, obj_returnedString;
							try {
								obj_featureAssetEntryTag = new JSONObject(featureAssetEntryTag);
								task_data_tag = obj_featureAssetEntryTag.getString("taskData");
	//							System.out.println("taskData is :" + task_data_tag);
								String returned_string = new String(Base64.decodeBase64(task_data_tag.getBytes()));
	//							System.out.println("Returned String is : " + returned_string);
								
								obj_returnedString = new JSONObject(returned_string);
								executionScriptTag = obj_returnedString.getString("executionScript");
								// System.out.println("executionScript is : " +
								// executionScriptTag);
							} catch (JSONException e1) {
								e1.printStackTrace();
							}

							// Writing the Script in .txt file
							scriptHashMap.put(fileNameWithoutExt, executionScriptTag);

						}
					}

				}

			}

			// getAllClassNamesUsedInTheFile(destOfScriptFiles,dirOfScriptFiles);
		}
		
		
			/**
			 * 
			 *  Blocked here to write in output files because of testing purpose
			 */
		
		
		/*try {
			FileUtils.createOutputFile(outputFile, HtmlGenerator.generateHTMLOutput(scriptHashMap, "", ""));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		/*
		 * if (osName.contains("Windows")) { destOfScriptFiles =
		 * dirOfScriptFiles + "\\"+outputFile; System.out.println(
		 * "Output Files Created : " +destOfScriptFiles); } else if
		 * (osName.contains("Linux")) { destOfScriptFiles = dirOfScriptFiles +
		 * "/"+outputFile; System.out.println("Output Files Created : "
		 * +destOfScriptFiles); }
		 */

	}

	/**
	 * 
	 * @param fileName
	 *            :Name of the file contaning CloupiaScript The Below method
	 *            Lists Out all Name of the classes used in CloupiaScript and
	 *            writes in a seperate .txt File in location : dirOfClassFiles
	 */
	public static void getAllClassNamesUsedInTheFile(String fileName, String dirOfClassFiles) {
		File destFileNameForClasses = new File(fileName);

		Set<String> setForClasses = new HashSet<String>();
		FileInputStream fisForClasses = null;
		DataInputStream disForClasses = null;
		BufferedReader brForClasees = null;
		String[] arrForStringTokens = {};
		FileWriter fstreamForClassFiles = null;
		String classNames = null;

		// Creating Directory For ClassFiles
		File fileClass = new File(dirOfClassFiles);

		String rootDirectory = fileClass.getParent();

		File file_ClassNew = new File(rootDirectory + "WorkFlowClass");
		if (!file_ClassNew.exists()) {
			if (file_ClassNew.mkdir()) {
				System.out.println("Directory Created");
			} else {
				System.out.println("NO");
			}
		}

		String dirForClasses = file_ClassNew.getAbsolutePath();

		// Fetching Java Classes Used IN Script

		try {
			fisForClasses = new FileInputStream(fileName);
			disForClasses = new DataInputStream(fisForClasses);
			brForClasees = new BufferedReader(new InputStreamReader(disForClasses));
			String line = null;
			while ((line = brForClasees.readLine()) != null) {
				String strWithoutChars = line.replaceAll("[^a-zA-Z]+", " ");

				arrForStringTokens = strWithoutChars.split(" ");
				for (int i = 0; i < arrForStringTokens.length; i++) {
					if (arrForStringTokens[i].equalsIgnoreCase("new")) {
						int val = i;
						setForClasses.add(arrForStringTokens[val + 1]);

					}
					if (arrForStringTokens[i].contains("Util")) {

						setForClasses.add(arrForStringTokens[i]);

					}
				}

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (brForClasees != null)
					brForClasees.close();
			} catch (Exception ex) {

			}
		}

		Iterator<String> itr = setForClasses.iterator();
		while (itr.hasNext()) {
			classNames = itr.next();

			try {

				// Writing Classes Used in .txt file

				String fileNameWithExt = destFileNameForClasses.getName();
				String fileNameWithoutExt = FilenameUtils.removeExtension(fileNameWithExt);
				String class_dest = dirForClasses + "\\" + fileNameWithoutExt + ".html";
				File file_dest_class = new File(class_dest);
				fstreamForClassFiles = new FileWriter(file_dest_class, true);
			} catch (IOException e) {

				e.printStackTrace();
			}
			BufferedWriter out = new BufferedWriter(fstreamForClassFiles);
			try {
				out.write(classNames);
				out.newLine();
				out.close();
			} catch (IOException e) {

				e.printStackTrace();

			}

		}

	}
}
