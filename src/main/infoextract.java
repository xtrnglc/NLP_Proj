package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.SentenceUtils;
import edu.stanford.nlp.process.DocumentPreprocessor;

public class infoextract {

	static List<File> dev_files = new ArrayList<File>();
	static List<String> perp_orgs = new ArrayList<String>();
	static List<String> weapons = new ArrayList<String>();
	static HashSet<String> otherAnswers = new HashSet<String>();

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
			if (file.getName().equals("DEV-MUC3-0078")) {
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
				otherAnswers.clear();
			}
		}
	}

	public static HashSet<String> getAnswers(List<String> dictionary, String text) {
		HashSet<String> answers = new HashSet<String>();

		for (int j = 0; j < dictionary.size(); j++) {
			String[] splitAnswer = dictionary.get(j).split("\\s*/\\s*");

			if (splitAnswer.length > 1) {
				for (int k = 0; k < splitAnswer.length; k++) {
					if (isContain(text, splitAnswer[k]) && !otherAnswers.contains(splitAnswer[k])) {
						if (k != splitAnswer.length - 1) {
							addToOtherAnswers(splitAnswer[k], dictionary);

							for (String otherAnswer : otherAnswers) {
								// System.out.println("OTHER ANSWER:" + otherAnswer);
								if (!splitAnswer[0].equals(otherAnswer)) {
									// System.out.println("ANSWER:" + splitAnswer[k]);

									answers.add(splitAnswer[k]);
								}
							}

							if (k != 0) {
								for (int l = k - 1; l >= 0; l--) {
									otherAnswers.add(splitAnswer[l]);
								}
							}
							if (k != splitAnswer.length - 1) {
								for (int m = k + 1; m < splitAnswer.length; m++) {
									otherAnswers.add(splitAnswer[m]);
								}
							}
							break;
						}
					}
				}
			} else {
				if (isContain(text, splitAnswer[0])) {
					addToOtherAnswers(splitAnswer[0], dictionary);

					if (!otherAnswers.contains(splitAnswer[0])) {
						answers.add(splitAnswer[0]);
					}
				}
			}
		}

		if (answers.size() == 0) {
			answers.add("-");
		}

		return answers;
	}

	public static void addToOtherAnswers(String answer, List<String> dictionary) {
		for (String item : dictionary) {
			if (item.contains(answer) && item.contains("/")) {
				String[] itemSplit = item.split("\\s*/\\s*");

				for (int i = 0; i < itemSplit.length; i++) {
					if (!itemSplit[i].equals(answer)) {
						otherAnswers.add(itemSplit[i]);
					}
				}
			}
		}
	}

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

	private static boolean isContain(String source, String subItem) {
		String pattern = "\\b" + subItem + "\\b";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(source);
		return m.find();
	}
}
