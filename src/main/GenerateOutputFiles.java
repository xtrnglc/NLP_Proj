package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class GenerateOutputFiles {

	static List<File> answer_files = new ArrayList<File>();
	static HashSet<String> perp_orgs = new HashSet<String>();
	static HashSet<String> weapons = new HashSet<String>();
	final static String PERP_ORGS_FILENAME = "perp-orgs.txt";
	final static String WEAPONS_FILENAME = "weapons.txt";

	public static void main(String args[]) throws FileNotFoundException, IOException {

		File answer_folder = new File(args[0]);
		File[] listOfAnswerFolders = answer_folder.listFiles();

		for (File file : listOfAnswerFolders) {
			if (file.isFile()) {
				answer_files.add(file);
			}
		}

		generatePerpOrgsFile();
	}

	public static void generatePerpOrgsFile() throws FileNotFoundException, UnsupportedEncodingException {
		for (File file : answer_files) {
			Scanner scanner = null;
			try {
				scanner = new Scanner(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				System.out.println(line);
				String slot = line.split(":\\s+")[0];

				if (slot.equals("PERP ORG")) {
					String answer = line.split(":\\s+")[1];
					if (!answer.equals("-")) {
						perp_orgs.add(answer);
					}
				}

				if (slot.equals("WEAPON")) {
					String answer = line.split(":\\s+")[1];
					if (!answer.equals("-")) {
						weapons.add(answer);
					}
				}
			}

			System.out.println();

			scanner.close();
		}

		PrintWriter writer = new PrintWriter(PERP_ORGS_FILENAME, "UTF-8");
		for (String org : perp_orgs) {
			writer.println(org);
		}
		writer.close();

		writer = new PrintWriter(WEAPONS_FILENAME, "UTF-8");
		for (String weapon : weapons) {
			writer.println(weapon);
		}
		writer.close();
	}
}
