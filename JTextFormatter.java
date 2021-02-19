package jTextFormatter;

import java.io.*;
import java.util.Scanner;

// This class has the main method.
public class JTextFormatter {

	// Read a whole input file and write a whole output file with formatting.
	public static void main(String[] args) {
		try {
			File inFile = new File(Settings.inputFileName);
			Scanner inScan = new Scanner(inFile);
			OutputWriter outFile = new OutputWriter();
			while (inScan.hasNext()) {
				String inLine = inScan.nextLine();
				outFile.add(inLine);
			}
			inScan.close();
			outFile.close();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
