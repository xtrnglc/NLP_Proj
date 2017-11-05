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
	static HashSet<String> perp_orgs_split = new HashSet<String>();
	static HashSet<String> weapons = new HashSet<String>();
	static HashSet<String> weapons_split = new HashSet<String>();
	final static String PERP_ORGS_FILENAME = "perp-orgs.txt";
	final static String WEAPONS_FILENAME = "weapons.txt";
	static String id;

	public static void main(String args[]) throws FileNotFoundException, IOException {

		File answer_folder = new File(args[0]);
		File[] listOfAnswerFolders = answer_folder.listFiles();

		for (File file : listOfAnswerFolders) {
			if (file.isFile()) {
				answer_files.add(file);
			}
		}

		//generatePerpOrgsFile();
		generateWeaponsFile();
	}
	
	public static void generateWeaponsFile() throws FileNotFoundException, UnsupportedEncodingException {
		for (File file : answer_files) {
			Scanner scanner = null;
			try {
				scanner = new Scanner(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				String slot = line.split(":\\s+")[0];
				if(slot.equals("ID")) {
					String answer = line.split(":\\s+")[1];
					id = answer;
					//System.out.println(answer);
				}
				if (slot.equals("WEAPON")) {
					String answer = line.split(":\\s+")[1];
					System.out.println(id + ": " + answer);
					if (!answer.equals("-")) {
						weapons.add(answer);
					}
				}
			}

			scanner.close();
		}

		for (String org : weapons) {
			String[] splitOrg = org.split("\\s+/\\s+");
			if (splitOrg.length > 1) {
				for (int i = 0; i < splitOrg.length; i++) {
					weapons_split.add(splitOrg[i]);
				}
			} else {
				weapons_split.add(org);
			}
		}

		PrintWriter writer = new PrintWriter("weapons.txt", "UTF-8");

		for (String org1 : weapons_split) {
			writer.println(org1);
		}

		writer.close();
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
				String slot = line.split(":\\s+")[0];

				if (slot.equals("PERP ORG")) {
					String answer = line.split(":\\s+")[1];
					if (!answer.equals("-")) {
						perp_orgs.add(answer);
					}
				}
			}

			scanner.close();
		}

		for (String org : perp_orgs) {
			String[] splitOrg = org.split("\\s+/\\s+");
			if (splitOrg.length > 1) {
				for (int i = 0; i < splitOrg.length; i++) {
					perp_orgs_split.add(splitOrg[i]);
				}
			} else {
				perp_orgs_split.add(org);
			}
		}

		PrintWriter writer = new PrintWriter("perp-orgs.txt", "UTF-8");

		for (String org1 : perp_orgs_split) {
			writer.println(org1);
		}

		writer.close();
	}
}
