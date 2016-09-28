package com.sdk.customtasks.scriptreader.html;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class HtmlGenerator {
	static Logger logger = Logger.getLogger(HtmlGenerator.class);

	public static Map finalHtmlTableMap = new HashMap();

	private static String branch = "https://wwwin-svn-sjc.cisco.com/cloupia/ciscoucsdirector";

	public static String branchOrTag1Details = "";
	public static String branchOrTag1Name = "";
	public static String branchOrTag2Details = "";
	public static String branchOrTag2Name = "";
	public static int noOfRESTAPIFiles = 0;
	/**
	 * Generate the final Html Output.
	 * 
	 * 
	 * @param reportFiles
	 * @param branch1
	 * @param branch2
	 * @return
	 */
	public static String generateHTMLOutput(Map reportFiles, String branch1, String branch2) {
		finalHtmlTableMap = reportFiles;
		StringBuffer html = new StringBuffer();
		try {

			html.append("<html> <head> ");
			html.append("<style type=\"text/css\">" + HtmlCssConstants.css + "</style>");
			html.append("</head><body> ");
			html.append("<div id=\"page\">");
			html.append("<div id=\"logo\">");
			html.append("<div class=\"title\"></div>");
			html.append("</div>");

			html.append("<br>");
			html.append("<a id=\"top\"></a>");
			html.append("<br>");
			html.append(createReportHeader(branch1, branch2));
			html.append("<br>");
			//html.append(createReportSummary(reportFiles.size()));
			html.append("<br>");
			if (reportFiles.size() > 0) {

				html.append("<br>");
				html.append("<div id=\"content\">");
				html.append(" <div class=\"Table\">");
				html.append("  <div class=\"Title\">");
				// html.append("<p>This is a Table</p>");
				html.append(" </div>");
				html.append(createReportTableHeader());
				html.append(createReportTableBody());
				html.append(" </div>");
				html.append("<br>");
				// html.append("<br>");

				// html.append("<br>");
				// html.append("<P style=\"color:red\">No. Of Report Files
				// Modified : "
				// + reportFiles.size());
				// html.append("</P>");

				html.append("<br>");
				html.append("<br>");
				
				Iterator itr = reportFiles.entrySet().iterator();
				while (itr.hasNext()) {
					boolean isNewAPI = false;
					// html.append("<P>");
					// html.append("<table> <tr><td>");
					
					String methodName = null;
					String methodBody = null;
					Map.Entry pairs = (Map.Entry) itr.next();
					methodName = (String) pairs.getKey();
					
					//////////////////////////////////
					/*String methodValue = (String) finalHtmlTableMap.get(methodName);
					StringTokenizer st = new StringTokenizer(methodValue.toString(), "^|^");
					while (st.hasMoreTokens()) {
						String newAPI = st.nextToken().toString();
						if (newAPI.equalsIgnoreCase("yes")) {
							// html.append("<td>" + newAPI + "</td>");
							//newAPICnt++;
							isNewAPI =true;
						} else {
							// html.append("<td>" + newAPI + "</td>");
						}

						String deleteAPI = st.nextToken().toString();
						
						String isRequestAPIParametersAdded = st.nextToken().toString();
						
						String isRequestAPIParametersModified = st.nextToken().toString();
						
						
						String isRequestAPIParametersDeleted = st.nextToken().toString();
						
						st.nextToken().toString();
						st.nextToken().toString();
						String newAPIParemetes = st.nextToken().toString();
						
						String isResponseAPIParemetersModified = st.nextToken().toString();
						
						
						String deleteAPIParemetes = st.nextToken().toString();
						
						
					}*/
					////////////////////////////////////
					
					html.append("<div class=\"roundbox boxshadow\" style=\"border: solid 2px steelblue\">    ");
					if(isNewAPI){
						html.append("<div class=\"gridheaderleft roundbox-top\" id=\""+methodName+"\"> New API : "+methodName+"</div>");
					}else{
						html.append("<div class=\"gridheaderleft roundbox-top\" id=\""+methodName+"\">"+methodName+"</div>");
					}
					html.append("<div class=\"boxcontenttext roundbox-bottom\" >");
					//html.append("<a name=\"" + methodName + "\">" + methodName + "</a>");
					// html.append("</td></tr><tr><td style=\"color:black\">");
					//html.append(" </div>");
					//html.append("<div class=\"innerBox\">");
					methodBody = pairs.getValue().toString();
					// logger.info(methodBody);
					methodBody = methodBody.replaceAll("\n", "<br>");
					html.append(methodBody);
					// html.append("</td></tr></table>");
					html.append("</div>");
					html.append("</div>");
					html.append("<a href=\"#top\">Go to top</a>");
					html.append("<br><br>");
					// html.append("</P>");
				}
			} else {
				html.append("<br>");
				html.append("<div id=\"content\">");
				// html.append(createReportHeader(branch1, branch2));
				html.append("<h2>No difference in the API Reports.</h2>");
				html.append("</div>");
			}
			html.append("</div>");
			html.append("<div id=\"footer\">Cisco</div>");
			html.append("</div>");

			html.append("</body> </html>");
		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println("HTML ------------>"+html.toString());
		return html.toString();
	}

	/**
	 * 
	 * @param reportFilesSize
	 * @return
	 */
	private static Object createReportSummary(int reportFilesSize) {
		StringBuffer html = new StringBuffer();
		// html.append("<table cellpadding=5 cellspacing='1'>");
		// html.append("<tr bgcolor='#8b8a8a'><th><h1>JSON API Comparison
		// Report</h1></th></tr>");
		// html.append("<tr bgcolor='#bab7b7'><td><b>Path 1: </b>" + branch + "/
		// " +branch1Details+"/"+ branch1 + "");
		html.append("<div id=\"reportpath\">");
		html.append(" <div id=\"reportPathTitle\">Cloupia Script Report</div>");
		int index = branchOrTag1Details.indexOf("/");
		String b1Name = branchOrTag1Details;
		if (index != -1) {
			b1Name = branchOrTag1Details.substring(index+1);			
			int branchEndIndex = b1Name.indexOf("/");
			if (branchEndIndex != -1) {
				b1Name = getBranch(branchOrTag1Details.substring(index+1));
			}
		}
		
		index = -1;
		index = branchOrTag2Details.indexOf("/");
		String b2Name = branchOrTag2Details;
		if (index != -1) {
			b2Name = branchOrTag2Details.substring(index+1);
			int branchEndIndex = b2Name.indexOf("/");
			if (branchEndIndex != -1) {
				b2Name = getBranch(branchOrTag2Details.substring(index+1));
			}
		}
		
		html.append("<div><b>Path 1: </b>" + branch + "/" + branchOrTag1Details+"/"+branchOrTag1Name + "" + "</div>");
		html.append("<div><b>Path 2: </b>" + branch + "/" + branchOrTag2Details+"/"+branchOrTag2Name +  "" + "</div>");
		html.append("<div><b>Applied Filters for XML API: </b>MoResourceIf" + "</div>");
		html.append("<div><b>No. Of XML Rest API files : </b>" + noOfRESTAPIFiles + "</div>");
		html.append("<div><b>APIs whose signature (input/output) has changed: </b>" + reportFilesSize + "</div>");

		/*Iterator itr = finalHtmlTableMap.entrySet().iterator();
		int newAPICnt = 0;
		int deleteAPICnt = 0;
		int isResponseAPIParametersAddedCnt = 0;
		int isResponseAPIParametersDeletedCnt = 0;
		int isResponseAPIParametersModifiedCnt = 0;
		
		int isRequestAPIParametersModifiedCnt = 0;
		int isRequestAPIParametersAddedCnt = 0;
		int isRequestAPIParametersDeletedCnt = 0;
		
		while (itr.hasNext()) {
			Map.Entry pair = (Map.Entry) itr.next();
			logger.info(pair.getKey() + "= "+ pair.getValue());

			StringTokenizer st = new StringTokenizer(pair.getValue().toString(), "^|^");
			while (st.hasMoreTokens()) {
				String newAPI = st.nextToken().toString();
				if (newAPI.equalsIgnoreCase("yes")) {
					// html.append("<td>" + newAPI + "</td>");
					newAPICnt++;
				} else {
					// html.append("<td>" + newAPI + "</td>");
				}

				String deleteAPI = st.nextToken().toString();
				if (deleteAPI.equalsIgnoreCase("yes")) {
					// html.append("<td>" + deleteAPI + "</td>");
					deleteAPICnt++;
				} else {
					// html.append("<td>" + deleteAPI + "</td>");
				}
				//st.nextToken().toString();
				String isRequestAPIParametersAdded = st.nextToken().toString();
				if (isRequestAPIParametersAdded.equalsIgnoreCase("yes")) {
					// html.append("<td>" + newAPI + "</td>");
					isRequestAPIParametersAddedCnt++;
				} else {
					// html.append("<td>" + newAPI + "</td>");
				}
				//st.nextToken().toString();
				String isRequestAPIParametersModified = st.nextToken().toString();
				if (isRequestAPIParametersModified.equalsIgnoreCase("yes")) {
					// html.append("<td>" + newAPI + "</td>");
					isRequestAPIParametersModifiedCnt++;
				} else {
					// html.append("<td>" + newAPI + "</td>");
				}
				
				String isRequestAPIParametersDeleted = st.nextToken().toString();
				if (isRequestAPIParametersDeleted.equalsIgnoreCase("yes")) {
					// html.append("<td>" + newAPI + "</td>");
					isRequestAPIParametersDeletedCnt++;
				} else {
					// html.append("<td>" + newAPI + "</td>");
				}
				//st.nextToken().toString();
				st.nextToken().toString();
				st.nextToken().toString();
				String newAPIParemetes = st.nextToken().toString();
				if (newAPIParemetes.equalsIgnoreCase("yes")) {
					// html.append("<td>" + newAPI + "</td>");
					isResponseAPIParametersAddedCnt++;
				} else {
					// html.append("<td>" + newAPI + "</td>");
				}
				//st.nextToken().toString();
				String isResponseAPIParemetersModified = st.nextToken().toString();
				if (isResponseAPIParemetersModified.equalsIgnoreCase("yes")) {
					// html.append("<td>" + newAPI + "</td>");
					isResponseAPIParametersModifiedCnt++;
				} else {
					// html.append("<td>" + newAPI + "</td>");
				}
				
				String deleteAPIParemetes = st.nextToken().toString();
				if (deleteAPIParemetes.equalsIgnoreCase("yes")) {
					// html.append("<td>" + newAPI + "</td>");
					isResponseAPIParametersDeletedCnt++;
				} else {
					// html.append("<td>" + newAPI + "</td>");
				}
				
				//st.nextToken().toString();
				//st.nextToken().toString();
				
			}

			// html.append("<td>Not supported in HTML5. Deprecated in HTML 4.01.
			// Specifies the alignment of a table according to surrounding
			// text</td>");
			// html.append("</tr>");
		}
		*/
		//html.append("<div><b>No. Of New APIs : </b>" + newAPICnt + "</div>");
		//html.append("<div><b>No. Of Deleted APIs : </b>" + deleteAPICnt + "</div>");
		//html.append("<div><b>Number of APIs that have additional input parameters : </b>" + isRequestAPIParametersAddedCnt + "</div>");
		//html.append("<div><b>Number of APIs that have deleted input parameters : </b>" + isRequestAPIParametersDeletedCnt + "</div>");
		//html.append("<div><b>Number of APIs that have modified input parameters  : </b>" + isRequestAPIParametersModifiedCnt + "</div>");
		//html.append("<div><b>Number of APIs that have additional output parameters : </b>" + isResponseAPIParametersAddedCnt + "</div>");
		//html.append("<div><b>Number of APIs that have deleted output parameters : </b>" + isResponseAPIParametersDeletedCnt + "</div>");
		//html.append("<div><b>Number of APIs that have modified output parameters : </b>" + isResponseAPIParametersModifiedCnt + "</div>");
		// html.append("</table>");
		html.append("</div>");
		return html.toString();
	}

	private static String createReportTableHeader() {

		StringBuffer html = new StringBuffer();
		// html.append("<section class=\"\">");
		// html.append("<div class=\"container\">");
		// html.append("<table border=1>");
		// html.append("<thead>");
		// html.append("<tr class=\"header\">");
		// html.append("<div class=\"Cell\">");

		html.append("<div class=\"Heading\">");
		html.append("<div class=\"Cell\">");
		html.append("<p>S.No.</p>");
		html.append("</div>");
		html.append("<div class=\"Cell\">");
		html.append("<p>API Name</p>");
		html.append("</div>");
		/*html.append("<div class=\"Cell\">");
		html.append("<p>is New API</p>");
		html.append("</div>");
		html.append("<div class=\"Cell\">");
		html.append("<p>is Deleted API</p>");
		html.append("</div>");
		html.append("<div class=\"Cell\">");
		html.append("<p>Request Parameters Added</p>");
		html.append("</div>");
		html.append("<div class=\"Cell\">");
		html.append("<p>Request Parameters Modified</p>");
		html.append("</div>");
		html.append("<div class=\"Cell\">");
		html.append("<p>Request Parameters Deleted</p>");
		html.append("</div>");
		html.append("<div class=\"Cell\">");
		html.append("<p>API Impacted</p>");
		html.append("</div>");
		html.append("<div class=\"Cell\">");
		html.append("<p>Doc Update Required</p>");
		html.append("</div>");
		html.append("<div class=\"Cell\">");
		html.append("<p>Response Parameter Added</p>");
		html.append("</div>");
		html.append("<div class=\"Cell\">");
		html.append("<p>Response Parameter Modified</p>");
		html.append("</div>");
		html.append("<div class=\"Cell\">");
		html.append("<p>Response Parameter Deleted</p>");
		html.append("</div>");*/
		
		
		html.append("</div>");
		// html.append("</tr>");
		// html.append("</thead>");

		return html.toString();
	}

	public static String createReportTableBody() {
		StringBuffer html = new StringBuffer();
		// html.append("<tbody>");
		logger.info("finalMap size "+ finalHtmlTableMap.size());
		Iterator itr = finalHtmlTableMap.entrySet().iterator();
		int incr = 1;
		while (itr.hasNext()) {
			Map.Entry pair = (Map.Entry) itr.next();
			logger.info(pair.getKey() + "= "+ pair.getValue());

			// html.append("<tr>");
			html.append("<div class=\"Row\">");
			html.append("<div class=\"Cell\">");
			html.append("<p>" + (incr++) + "</p>");
			html.append("</div>");
			html.append("<div class=\"Cell\">");
			html.append("<p><a href=\"#" + pair.getKey().toString() + "\">" + pair.getKey() + "</a></p>");
			html.append("</div>");
			
			/*StringTokenizer st = new StringTokenizer(pair.getValue().toString(), "^|^");
			while (st.hasMoreTokens()) {
				String newAPI = st.nextToken().toString();
				html.append("<div class=\"Cell\">");
				if (newAPI.equalsIgnoreCase("yes")) {
					html.append("<p>" + newAPI + "</p>");
				} else {
					html.append("<p>" + newAPI + "</p>");
				}
				html.append("</div>");
				html.append(" <div class=\"Cell\">");
				String deleteAPI = st.nextToken().toString();
				if (deleteAPI.equalsIgnoreCase("yes")) {
					html.append("<p>" + deleteAPI + "</p>");
				} else {
					html.append("<p>" + deleteAPI + "</p>");
				}
				html.append("</div>");
				html.append(" <div class=\"Cell\">");
				html.append("<p>" + st.nextToken().toString() + "</p>");
				html.append("</div>");
				html.append(" <div class=\"Cell\">");
				html.append("<p>" + st.nextToken().toString() + "</p>");
				html.append("</div>");
				html.append(" <div class=\"Cell\">");
				html.append("<p>" + st.nextToken().toString() + "</p>");
				html.append("</div>");
				html.append(" <div class=\"Cell\">");
				html.append("<p>" + st.nextToken().toString() + "</p>");
				html.append("</div>");
				html.append(" <div class=\"Cell\">");
				html.append("<p>" + st.nextToken().toString() + "</p>");
				html.append("</div>");
				html.append(" <div class=\"Cell\">");
				html.append("<p>" + st.nextToken().toString() + "</p>");
				html.append("</div>");
				html.append(" <div class=\"Cell\">");
				html.append("<p>" + st.nextToken().toString() + "</p>");
				html.append("</div>");
				html.append(" <div class=\"Cell\">");
				html.append("<p>" + st.nextToken().toString() + "</p>");
				html.append("</div>");
			}
			*/
			// html.append("<td>Not supported in HTML5. Deprecated in HTML 4.01.
			// Specifies the alignment of a table according to surrounding
			// text</td>");
			// html.append("</tr>");
			html.append("</div>");
		}
		// html.append("</tbody>");
		// html.append("</table>");
		// html.append("</div>");
		// html.append("</section>");

		return html.toString();
	}

	public static String createReportHeader(String branch1, String branch2) {
		StringBuffer html = new StringBuffer();
		// html.append("<table border=\"0\">");
		html.append("<div class=\"roundedcorners\">");
		// html.append(" <div id=\"pageTitle\">");
		// html.append("<td bgcolor=\"#ccccff\" class=\"titelbalk\">");
		int index = branch1.indexOf("/");
		String b1Name = branch1;
		if (index != -1) {
			b1Name = branch1.substring(index+1);			
			int branchEndIndex = b1Name.indexOf("/");
			if (branchEndIndex != -1) {
				b1Name = getBranch(branch1.substring(index+1));
			}
		}
		
		index = -1;
		index = branch2.indexOf("/");
		String b2Name = branch2;
		if (index != -1) {
			b2Name = branch2.substring(index+1);
			int branchEndIndex = b2Name.indexOf("/");
			if (branchEndIndex != -1) {
				b2Name = getBranch(branch2.substring(index+1));
			}
		}
		if(b1Name.trim().length() > 0 && b2Name.trim().length() > 0){
			html.append("<p>UCSD " + b1Name + " \t " + b2Name + "\t API Report Files</p>");
		}else{
			html.append("<p>UCSD\t" + branchOrTag1Name + "\t" + branchOrTag2Name + "\tAPI Report Files</p>");
		}
		// html.append("</div>");
		html.append("</div>");
		// html.append("<td align=\"right\" width=\"36\" class=\"noprint\">");
		// html.append("<A HREF="gb-hist.htm"><IMG
		// SRC="b-next.gif"ALT="Next"WIDTH="32"HEIGHT="32"BORDER="0"class="buttonnext"></A>
		// html.append("</td>");
		// html.append("</tr>");
		// html.append("</table>");
		// header = html.toString();
		// HtmlGenerator.branch1 = branch1;
		// HtmlGenerator.branch2 = branch2;
		return html.toString();

	}

	private static String getBranch(String input){
		String branchOutput = input;
		/*
		Pattern space = Pattern.compile("/?(.*)([a-zA-Z]*)/(.*)");
		 Matcher m = space.matcher(input);
	      if (m.find( )) {
	    	  
	    	  if(m.groupCount() >2){
	    		  logger.info("Found value: " + m.group(1) );
	    		  branchOutput =m.group(1);
	    	  }
	      }
	      */
			String[] afterSplit = input.split("/");
	      for (int i = 0; i < afterSplit.length; i++) {
				//logger.info(afterSplit[i]);			
			}
	      if(afterSplit.length >= 1 && afterSplit[0].trim().length() > 0 )
	    	  branchOutput  = afterSplit[0];
		return branchOutput;
	}
}
