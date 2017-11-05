package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class infoextract {

	static List<File> dev_files = new ArrayList<File>();
	static List<String> perp_orgs = new ArrayList<String>();
	static List<String> weapons = new ArrayList<String>();

	public static void main(String args[]) throws FileNotFoundException, IOException {
		File dev_folder = new File(args[0]);
		File[] listOfDevFiles = dev_folder.listFiles();
		File perp_orgs_file = new File(args[1]);
		File weapons_file = new File(args[2]);

		for (File file : listOfDevFiles) {
			if (file.isFile()) {
				dev_files.add(file);
			}
		}

		Scanner scanner = new Scanner(perp_orgs_file);
		while (scanner.hasNext()) {
			perp_orgs.add(scanner.nextLine());
		}
		scanner.close();

		Scanner scanner2 = new Scanner(weapons_file);
		while (scanner2.hasNext()) {
			weapons.add(scanner2.nextLine());
		}
		scanner2.close();

		for (File file : dev_files) {
			String id = "";
			String text = "";
			int i = 0;

			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String line;
				while ((line = br.readLine()) != null) {
					if (i == 0) {
						id = line.split("\\s+")[0];
					} else {
						text += line;
					}
					i++;
				}
			}

			HashSet<String> perpetrator_orgs = getAnswers(perp_orgs, text);
			HashSet<String> _weapons = getAnswers(weapons, text);

			if (id.startsWith("DEV") || id.startsWith("TST")) {
				System.out.println(printTemplate(id, getIncident(text), _weapons,
						new ArrayList<String>(Arrays.asList("-")), perpetrator_orgs,
						new ArrayList<String>(Arrays.asList("-")), new ArrayList<String>(Arrays.asList("-"))));
				System.out.println();
			}
		}
	}

	public static HashSet<String> getAnswers(List<String> inputFile, String text) {
		HashSet<String> answers = new HashSet<String>();
		for (int j = 0; j < inputFile.size(); j++) {
			String[] splitAnswer = inputFile.get(j).split("\\s+/\\s+");

			if (splitAnswer.length > 1) {
				for (int k = 0; k < splitAnswer.length; k++) {
					if (text.contains(splitAnswer[k])) {
						answers.add(splitAnswer[k]);
						break;
					}
				}
			} else {
				if (text.contains(splitAnswer[0])) {
					answers.add(splitAnswer[0]);
				}
			}
		}

		if (answers.size() == 0) {
			answers.add("-");
		}

		return answers;
	}

	/*
	 * public static HashSet<String> generateAnswerHashSet(List<String> slot) {
	 * HashSet<String> perpetrator_orgs = new HashSet<String>(); }
	 */

	public static String printTemplate(String id, String incident, HashSet<String> weapon, List<String> perpIndiv,
			HashSet<String> perpOrg, List<String> target, List<String> victim) {
		String template = "";
		template += "ID: " + id + "\n";
		template += "INCIDENT: " + incident + "\n";
		template += "WEAPON: ";
		int count = 0;
		for (String s : weapon) {
			if (count == 0) {
				template += s;
				template += "\n";
			} else {
				template += "        " + s;
				template += "\n";
			}
			count++;
		}
		template += "PERP INDIV: ";
		for (String s : perpIndiv) {
			template += " " + s;
		}
		template += "\n";
		template += "PERP ORG: ";
		int count2 = 0;
		for (String s : perpOrg) {
			if (count2 == 0) {
				template += s;
				template += "\n";
			} else {
				template += "          " + s;
				template += "\n";
			}
			count2++;
		}
		template += "TARGET: ";
		for (String s : target) {
			template += s;
			template += "\n";
		}
		template += "VICTIM: ";
		for (String s : victim) {
			template += " " + s;
		}
		template += "\n";
		return template;
	}

	public static String getIncident(String text) {
		String incident = "ATTACK";

		HashSet<String> arsonKeyWords = new HashSet<String>(
				Arrays.asList("BURN", "BURNING", "INCINERATE", "COMBUST", "COMBUSTED", "ON FIRE"));

		for (String s : arsonKeyWords) {
			if (text.contains(s)) {
				return "ARSON";
			}
		}

		HashSet<String> kidnapKeyWords = new HashSet<String>(Arrays.asList("KIDNAP", "ABDUCT", "KIDNAPPING",
				"KIDNAPPED", "RANSOM", "REGISTRATION", "SEIZE", "SEIZED", "SNATCH", "SNATCHED", "HOSTAGE", "ABDUCTED"));

		for (String s : kidnapKeyWords) {
			if (text.contains(s)) {
				return "KIDNAPPING";
			}
		}

		HashSet<String> bombingKeyWords = new HashSet<String>(Arrays.asList("BOMB", "BOMBING", "EXPLOSION", "EXPLODE",
				"EXPLODED", "EXPLOSIVE", "BLAST", "BLASTED", "BLEW UP", "DYNAMITE"));

		for (String s : bombingKeyWords) {
			if (text.contains(s)) {
				return "BOMBING";
			}
		}

		HashSet<String> robberyKeyWords = new HashSet<String>(Arrays.asList("ROBBED", "ROBBERY", "STOLE", "THEFT",
				"MUGGING", "THIEF", "HEIST", "ROB", "BURGLAR", "BURGLARY", "STEAL"));

		for (String s : robberyKeyWords) {
			if (text.contains(s)) {
				return "ROBBERY";
			}
		}

		return incident;
	}
}
