package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.SentenceUtils;
import edu.stanford.nlp.process.DocumentPreprocessor;

public class PrintSentencesWithAnswers {
	static List<File> answer_files = new ArrayList<File>();
	static List<File> text_files = new ArrayList<File>();
	static String body = "";

	public static void main(String args[]) throws FileNotFoundException, IOException {
		File answer_folder = new File(args[0]);
		File text_folder = new File(args[1]);
		File[] listOfAnswerFiles = answer_folder.listFiles();
		File[] listOfTextFiles = text_folder.listFiles();

		for (File file : listOfAnswerFiles) {
			if (file.isFile()) {
				answer_files.add(file);
			}
		}

		for (File file : listOfTextFiles) {
			if (file.isFile()) {
				text_files.add(file);
			}
		}

		for (int i = 0; i < answer_files.size(); i++) {
			if (answer_files.get(i).getName().startsWith("DEV") || answer_files.get(i).getName().startsWith("TST")) {
				Scanner scanner = new Scanner(answer_files.get(i));
				String id = scanner.nextLine();
				scanner.nextLine();
				// printWeaponsContextSentences(scanner, id, i);
				// printPerpIndivContextSentences(scanner, id, i);
				// printPerpOrgContextSentences(scanner, id, i);
				// printTargetContextSentences(scanner, id, i);
				printVictimContextSentences(scanner, id, i);
			}
		}
		
		PrintWriter printWriter = new PrintWriter("victims-context", "UTF-8");
		printWriter.write(body);
		printWriter.close();
	}

	public static void printWeaponsContextSentences(Scanner scanner, String id, int i)
			throws FileNotFoundException, UnsupportedEncodingException {
		ArrayList<String> weapons = new ArrayList<String>();
		weapons.add(scanner.nextLine());
		String line = "";
		while (!(line = scanner.nextLine()).startsWith("PERP")) {
			weapons.add(line);
		}

		for (String weapon : weapons) {
			if (!getItem(weapon).equals("-")) {
				System.out.println(getFormattedId(id));
				System.out.println(weapon);

				File relevantTextFile = text_files.get(i);
				Reader reader = new StringReader(getStringFromFile(relevantTextFile));
				DocumentPreprocessor dp = new DocumentPreprocessor(reader);
				List<String> sentenceList = new ArrayList<String>();
				for (List<HasWord> sentence : dp) {
					String sentenceString = SentenceUtils.listToString(sentence);
					sentenceList.add(sentenceString);
				}

				String sentenceToPrint = "";
				for (String sentence : sentenceList) {
					String[] weaponsSplit = getItem(weapon).split("\\s*/\\s*");
					if (sentence.contains(weaponsSplit[0])) {
						sentenceToPrint += sentence;
						sentenceToPrint += "\n";
					}
				}
				System.out.println(sentenceToPrint);
			}
		}
	}

	public static void printPerpIndivContextSentences(Scanner scanner, String id, int i) {
		scanner.nextLine();
		ArrayList<String> perpIndiv = new ArrayList<String>();
		String line = "";
		while (!(line = scanner.nextLine()).startsWith("PERP INDIV")) {
			;
		}
		perpIndiv.add(line);
		String line2 = "";
		while (!(line2 = scanner.nextLine()).startsWith("PERP ORG")) {
			perpIndiv.add(line2);
		}

		for (String perp : perpIndiv) {
			if (!getItem(perp).equals("-")) {
				System.out.println(getFormattedId(id));
				System.out.println(perp.replaceAll("^\\s+", ""));

				File relevantTextFile = text_files.get(i);
				Reader reader = new StringReader(getStringFromFile(relevantTextFile));
				DocumentPreprocessor dp = new DocumentPreprocessor(reader);
				List<String> sentenceList = new ArrayList<String>();
				for (List<HasWord> sentence : dp) {
					String sentenceString = SentenceUtils.listToString(sentence);
					sentenceList.add(sentenceString);
				}

				String sentenceToPrint = "";
				for (String sentence : sentenceList) {
					String[] weaponsSplit = getItem(perp).split("\\s*/\\s*");
					if (sentence.contains(weaponsSplit[0])) {
						sentenceToPrint += sentence;
						sentenceToPrint += "\n";
					}
				}
				System.out.println(sentenceToPrint);
			}
		}
	}

	public static void printPerpOrgContextSentences(Scanner scanner, String id, int i) {
		scanner.nextLine();
		scanner.nextLine();
		ArrayList<String> perpOrg = new ArrayList<String>();
		String line = "";
		while (!(line = scanner.nextLine()).startsWith("PERP ORG")) {
			;
		}
		perpOrg.add(line);
		String line2 = "";
		while (!(line2 = scanner.nextLine()).startsWith("TARGET")) {
			perpOrg.add(line2);
		}

		for (String perp : perpOrg) {
			if (!getItem(perp).equals("-")) {
				System.out.println(getFormattedId(id));
				System.out.println(perp.replaceAll("^\\s+", ""));

				File relevantTextFile = text_files.get(i);
				Reader reader = new StringReader(getStringFromFile(relevantTextFile));
				DocumentPreprocessor dp = new DocumentPreprocessor(reader);
				List<String> sentenceList = new ArrayList<String>();
				for (List<HasWord> sentence : dp) {
					String sentenceString = SentenceUtils.listToString(sentence);
					sentenceList.add(sentenceString);
				}

				String sentenceToPrint = "";
				for (String sentence : sentenceList) {
					String[] weaponsSplit = getItem(perp).split("\\s*/\\s*");
					if (sentence.contains(weaponsSplit[0])) {
						sentenceToPrint += sentence;
						sentenceToPrint += "\n";
					}
				}
				System.out.println(sentenceToPrint);
			}
		}
	}

	public static void printTargetContextSentences(Scanner scanner, String id, int i) {
		scanner.nextLine();
		scanner.nextLine();
		scanner.nextLine();
		ArrayList<String> targets = new ArrayList<String>();
		String line = "";
		while (!(line = scanner.nextLine()).startsWith("TARGET")) {
			;
		}
		targets.add(line);
		String line2 = "";
		while (!(line2 = scanner.nextLine()).startsWith("VICTIM")) {
			targets.add(line2);
		}

		for (String target : targets) {
			if (!getItem(target).equals("-")) {
				System.out.println(getFormattedId(id));
				System.out.println(target.replaceAll("^\\s+", ""));

				File relevantTextFile = text_files.get(i);
				Reader reader = new StringReader(getStringFromFile(relevantTextFile));
				DocumentPreprocessor dp = new DocumentPreprocessor(reader);
				List<String> sentenceList = new ArrayList<String>();
				for (List<HasWord> sentence : dp) {
					String sentenceString = SentenceUtils.listToString(sentence);
					sentenceList.add(sentenceString);
				}

				String sentenceToPrint = "";
				for (String sentence : sentenceList) {
					String[] weaponsSplit = getItem(target).split("\\s*/\\s*");
					if (sentence.contains(weaponsSplit[0])) {
						sentenceToPrint += sentence;
						sentenceToPrint += "\n";
					}
				}
				System.out.println(sentenceToPrint);
			}
		}
	}

	public static void printVictimContextSentences(Scanner scanner, String id, int i) {
		ArrayList<String> victims = new ArrayList<String>();
		String line = "";
		while (!(line = scanner.nextLine()).startsWith("VICTIM")) {
			;
		}
		victims.add(line);
		String line2 = "";
		try {
			while (!(line2 = scanner.nextLine()).matches("\\s+") || !(line2 = scanner.nextLine()).startsWith("ID")) {
				System.out.println(line2);
				victims.add(line2);
			}
		} catch (NoSuchElementException e) {
			;
		}

		for (String victim : victims) {
			if (!getItem(victim).equals("-") && !victim.isEmpty()) {
				System.out.println("ID: " + getFormattedId(id));
				body += "ID: " + getFormattedId(id);
				body += "\n" + victim.replaceAll("^\\s+", "");
				System.out.println("VICTIM: " + victim);

				File relevantTextFile = text_files.get(i);
				Reader reader = new StringReader(getStringFromFile(relevantTextFile));
				DocumentPreprocessor dp = new DocumentPreprocessor(reader);
				List<String> sentenceList = new ArrayList<String>();
				for (List<HasWord> sentence : dp) {
					String sentenceString = SentenceUtils.listToString(sentence);
					sentenceList.add(sentenceString);
				}

				String sentenceToPrint = "";
				for (String sentence : sentenceList) {
					String[] weaponsSplit = getItem(victim).split("\\s*/\\s*");
					if (sentence.contains(weaponsSplit[0])) {
						sentenceToPrint += sentence;
						sentenceToPrint += "\n";
					}
				}
				System.out.println(sentenceToPrint);
				body += "\n" + sentenceToPrint + "\n";
			}
		}
	}

	public static String getStringFromFile(File file) {
		String text = "";
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				text += line;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return text;
	}

	public String getSentence(List<String> sentenceList, String keyword) {
		String append = "";
		for (int i = 0; i < sentenceList.size(); i++) {
			if (isContain(sentenceList.get(i), keyword)) {
				append += sentenceList.get(i);
				append += " ";
			}
		}

		return append;
	}

	private static boolean isContain(String source, String subItem) {
		String pattern = "\\b" + subItem + "\\b";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(source);
		return m.find();
	}

	private static String getFormattedId(String id) {
		return id.split(":\\s+")[1];
	}

	private static String getItem(String item) {
		try {
			return item.split(":\\s+")[1];
		} catch (ArrayIndexOutOfBoundsException e) {
			return item.replaceAll("^\\s+", "");
		}
	}
}
