package jTextFormatter;

import java.io.File;
import java.io.PrintWriter;

// This class stores the output file and writes to it.
public class OutputWriter {
	
	private File outFile;
	private String outBuffer = Settings.indent;
	private PrintWriter fileWriter;

	// This constructor opens the file for writing.
	OutputWriter () {
		final String outFileName = Settings.outputFileName;	
		outFile = new File(outFileName);
		try {
			fileWriter = new PrintWriter(outFile);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// This method adds input to the output buffer and does any necessary processing.
	void add(String text) throws Exception {
		text = text.trim();
		if (text.length() == 0) {
			// We interpret a blank line in the input file to mean the end of a paragraph.
			if (outBuffer.trim().length() > 0) fileWriter.println(outBuffer);
			outBuffer = Settings.indent;
		}
		else {
			if (outBuffer.trim().length() == 0) outBuffer += text;
			else outBuffer += " " + text;
			while (outBuffer.length() >= Settings.outputLineLength) {
				int endIndex = cutoff();
				fileWriter.println(formatLine(outBuffer.substring(0, endIndex+1)));
				outBuffer = outBuffer.substring(endIndex+1).trim();
			}
		}
	}

	// This method determines the "cutoff" point at which to stop a line of output.
	private int cutoff() throws Exception {
		if (outBuffer.length() == Settings.outputLineLength)
			return Settings.outputLineLength - 1;
		// At this point, I assume that the output buffer
		// length is greater than the output line length.
		for (int temp = Settings.outputLineLength - 1; temp > 0; temp--)
			if (outBuffer.charAt(temp+1) == ' ' || outBuffer.charAt(temp) == '-')
					return temp;
		// If we're down to the 0th character then I think
		// some joker is coming up with a silly test case.
		throw new Exception("One word is too long.");
	}

	// This method formats a line by padding it and returns the result.
	private String formatLine(String inputLine) {
		if (inputLine.length() == Settings.outputLineLength) return inputLine;
		int spacesToAdd = Settings.outputLineLength - inputLine.length();
		int availableSpaces = inputLine.trim().replaceAll("[^ ]", "").length();
		String standardPadding = padding(spacesToAdd / availableSpaces);
		int remainder = spacesToAdd % availableSpaces;
		StringBuilder sb = new StringBuilder(inputLine);
		int index = sb.lastIndexOf(" ");
		do {
			sb.insert(index, standardPadding);
			if (remainder > 0) {
				sb.insert(index, " ");
				remainder--;
			}
			index = sb.lastIndexOf(" ", index-1);
		} while (sb.length() < Settings.outputLineLength);
		return sb.toString();
	}


	// This method returns a string consisting of the given number of spaces.
	private static String padding(int paddingLength) {
		String temp = "";
		for (int index = 0; index < paddingLength; index++)
			temp += " ";
		return temp;
	}

	// This method functions as a "destructor".
	void close () {
		// First, flush out whatever text remains.
		fileWriter.println(outBuffer);
		// Then close the file.
		fileWriter.close();
	}
}
