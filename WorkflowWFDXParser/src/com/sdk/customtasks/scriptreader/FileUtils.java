package com.sdk.customtasks.scriptreader;

/**
 * 
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.log4j.Logger;

/**
 * @author lkandasa
 * 
 */
public class FileUtils {
	static Logger logger = Logger.getLogger(FileUtils.class);
	private static int noOfLines  =0 ; 
	/**
	 * 
	 */
	public FileUtils() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 * Fetch the entire contents of a text file, and return it in a String. This
	 * style of implementation does not throw Exceptions to the caller.
	 * 
	 * @param aFile
	 *            is a file which already exists and can be read.
	 */
	static public String getContents(File aFile) {
		// ...checks on aFile are elided
		StringBuilder contents = new StringBuilder();

		try {
			// use buffering, reading one line at a time
			// FileReader always assumes default encoding is OK!
			BufferedReader input = new BufferedReader(new FileReader(aFile));
			try {
				String line = null; // not declared within while loop
				/*
				 * readLine is a bit quirky : it returns the content of a line
				 * MINUS the newline. it returns null only for the END of the
				 * stream. it returns an empty String if two newlines appear in
				 * a row.
				 */
				while ((line = input.readLine()) != null) {
					contents.append(line);
					//System.out.println("--- file Utils line = "+line);
					contents.append(System.getProperty("line.separator"));
					noOfLines++;
				}
			} finally {
				input.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return contents.toString();
	}

	public static void createOutputFile(String filePath, String aContents) throws IOException {
		File f = new File(filePath);
		if (f.exists())
			f.delete();
		if (!f.exists())
		{
			System.out.println(filePath);
			System.out.println(f.getParent());
			System.out.println(f.getName());
			f.createNewFile();
			logger.info("New file \"" + filePath + "\" has been created to the current directory");
		}

		// use buffering
		Writer output = new BufferedWriter(new FileWriter(f));
		try {
			// FileWriter always assumes default encoding is OK!
			output.write(aContents);
		} finally {
			output.close();
		}

	}

	/**
	 * @return the noOfLines
	 */
	public static int getNoOfLines() {
		return noOfLines;
	}

	/**
	 * @param noOfLines the noOfLines to set
	 */
	public void setNoOfLines(int noOfLines) {
		this.noOfLines = noOfLines;
	}

}
