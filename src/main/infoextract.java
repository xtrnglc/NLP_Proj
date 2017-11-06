package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;

import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.trees.Tree;

public class infoextract {

	public static List<File> dev_files = new ArrayList<File>();
	public static List<String> perp_orgs = new ArrayList<String>();
	public static List<String> weapons = new ArrayList<String>();
	public static List<File> answer_files = new ArrayList<File>();
	
	public static HashMap<String, String> oursIncident = new HashMap<String, String>();
	public static HashMap<String, String> theirsIncident = new HashMap<String, String>();
	public static HashMap<String, String> oursPerpOrg = new HashMap<String, String>();
	public static HashMap<String, String> theirsPerpOrg = new HashMap<String, String>();
	
	public static HashMap<String, String> weaponSpecificRules = new HashMap<String, String>();
	public static HashMap<String, String> weaponGeneralRules = new HashMap<String, String>();

	public static void main(String args[]) throws FileNotFoundException, IOException {
		File dev_folder = new File(args[0]);
		File[] listOfDevFiles = dev_folder.listFiles();
		File answer_folder = new File(args[3]);
		File[] listOfAnswerFiles = answer_folder.listFiles();
		File perp_orgs_file = new File(args[1]);
		File weapons_file = new File(args[2]);

		for (File file : listOfDevFiles) {
			if (file.isFile()) {
				dev_files.add(file);
			}
		}
		for (File file : listOfAnswerFiles) {
			if (file.isFile()) {
				answer_files.add(file);
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

		//String s = "PROMISED TO CARRY OUT A PROMPT AND SERIOUS INVESTIGATION AND TO PUNISH THOSE RESPONSIBLE FOR THE MURDER OF SIX JESUITS AND TWO WOMEN ON 16 NOVEMBER.";
		//Sentence sentence = new Sentence(s);
		//System.out.println(sentence.parse());
		//Tree t = sentence.parse();
		//System.out.println(t.getChild(0).getChild(0));
		//parseNP(t.getChild(0).getChild(0));
		instantiateRules();
		generateTemplate();
//		getAnswerIncidents();
//		getAnswerPerpOrg();
//		metric();
		//parseWeaponRule("<WEAPON> BLASTS", "AND THERE WERE \"EXPLOSIONS, MACHINE-GUN BLASTS, AND SHOTS,\" SANDOVAL SAID.");
		
		
	}
	
	public static void instantiateRules() {
		weaponGeneralRules.put("BLASTS", "<WEAPON> BLASTS");
		weaponGeneralRules.put("HURLED", "HURLED <WEAPON>");
		weaponGeneralRules.put("THROWING", "THROWING <WEAPON>");
		weaponGeneralRules.put("HURLING", "HURLING <WEAPON>");
		weaponGeneralRules.put("BLAST", "<WEAPON> BLAST");
		weaponGeneralRules.put("FIRED", "FIRED <WEAPON>");
		weaponGeneralRules.put("EXPLODED", "<WEAPON> EXPLODED");
		weaponGeneralRules.put("DESTROYED BY", "DESTROYED BY <WEAPON>");
		weaponGeneralRules.put("DAMAGED BY", "DAMAGED BY <WEAPON>");
		//weaponGeneralRules.put("WENT OFF", "<WEAPON> WENT OFF");
		weaponGeneralRules.put("MADE UP OF", "MADE UP OF <WEAPON>");
		weaponGeneralRules.put("CAUSED BY", "CAUSED BY <WEAPON>");
	}
	
	public static void metric() {
		int success = 0;
		for(Entry<String, String> s1 : oursIncident.entrySet()) {
			System.out.println(s1.getKey() + " \t\tOURS: " + s1.getValue() + "\t\t\tTHEIRS: " + theirsIncident.get(s1.getKey()));
			if(s1.getValue().equals(theirsIncident.get(s1.getKey()))) {
				success++;
			}
		}
		System.out.println((double)success / (double) oursIncident.size());
	}
	
	public static void getAnswerIncidents() throws FileNotFoundException, IOException {
		for (File file : answer_files) {
			String id = "";
			String text = "";
			String incident = "";

			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String line;
				while ((line = br.readLine()) != null) {		
					if(line.contains("ID:")) {
						id = line.split("\\s+")[1];
					}

					if(line.contains("INCIDENT:")) {
						incident = line.split("\\s+")[1];
						theirsIncident.put(id, incident);
					}
				}
			}	
		}
	}
	
	public static void getAnswerPerpOrg() throws FileNotFoundException, IOException {
		for (File file : answer_files) {
			String id = "";
			String text = "";
			String perporg = "";

			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String line;
				while ((line = br.readLine()) != null) {		
					if(line.contains("ID:")) {
						id = line.split("\\s+")[1];
					}

					if(line.contains("PERP ORG:")) {
						perporg = line.split("\\s+")[2];
						theirsPerpOrg.put(id, perporg);
					}
				}
			}	
		}
	}
 	
	public static void parseNP(Tree nptree) {
		String np = "";
		for(Tree t : nptree.getChildrenAsList()) {
			String s = t.getLeaves().toString();
			s = s.substring(1, s.length()-1);
			np += s + " ";
		}
		System.out.println(np);
	}
	
	public static String parseWeaponRule(String rule, String s) {
		//String rule = "DESTROYED BY <WEAPON>";
		s = s.replaceAll("\\s*\\p{Punct}+\\s*$", "");
		s = s.replaceAll("\"", "");
		s = s.replaceAll(",", "");
		s = s.replaceAll("\\[", "").replaceAll("\\]","");
		s = s.replaceAll("\\(", "").replaceAll("\\)","");
		s = s.replaceAll("\\{", "").replaceAll("\\}","");
		s = s.replaceAll("\\$", "").replaceAll("\\$","");
		s = s.replaceAll("--", "");
		String[] rules = rule.split("\\s+");
		//String s = "BOGOTA WAS DESTROYED BY A BOMB, POLICE REPORTED.";
		String[] split = s.split("\\s+");
		String weapon = null;		
		
		boolean after = true;
		
		Sentence sentence = new Sentence(s).caseless();
		List<String> posSplit = sentence.caseless().posTags();
		//System.out.println(sentence.caseless().parse());
		int index = 0;
		int indexOfTriggerWord = 0;
		int indexOfWeapon = 0;
			
		for(int i = 0; i < rules.length; i++) {
			if(!rules[i].contains("<")) {
				indexOfTriggerWord = i;
			} else {
				indexOfWeapon = i;
			}
		}
		
		if(indexOfTriggerWord > indexOfWeapon) {
			after = false;
		} else if(indexOfTriggerWord < indexOfWeapon) {
			after = true;
		}
		String s1;
		for(int i = 0; i < split.length; i++) {
			s1 = split[i].replaceAll("\\s*\\p{Punct}+\\s*$", "");
			if(s1.equals(rules[indexOfTriggerWord])) {
				index = i;
				break;
			}
		}
		
		if(after) {
			for(int i = index+1; i < split.length; i++) {
				if(rules[indexOfTriggerWord].equals("BLASTS") || rules[indexOfTriggerWord].equals("BLAST")) {
					if(posSplit.get(i).contains("NN") || posSplit.get(i).equals("JJ")) {
						if(weapons.contains(split[i])) {
							weapon = split[i];
						}
						break;
					}
				} else {
					if(posSplit.get(i).contains("NN")) {
						if(weapons.contains(split[i])) {
							weapon = split[i];
						}
						break;
					}
				}
				
			}
		} else {
			for(int i = index-1; i > -1; i--) {
				if(rules[indexOfTriggerWord].equals("BLASTS") || rules[indexOfTriggerWord].equals("BLAST")) {
					if(posSplit.get(i).contains("NN") || posSplit.get(i).contains("JJ")) {
						if(weapons.contains(split[i])) {
							weapon = split[i];
						}
						break;
					}
				} else{
					if(posSplit.get(i).contains("NN")) {
						if(weapons.contains(split[i])) {
							weapon = split[i];
						}
						break;
					}
				}
			}
		}
		
		
		
		return weapon;
	}
	
	public static void generateTemplate() throws FileNotFoundException, IOException {
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
						text += " " + line;
					}
					i++;
				}
			}
			
			oursIncident.put(id, getIncident(text));
			
			String po = "";
			for(String s : getAnswers(perp_orgs, text)) {
				po += s + "\n";
			}

				
			oursPerpOrg.put(id, po);
			HashSet<String> perpetrator_orgs = getAnswers(perp_orgs, text);
			
			HashSet<String> weaponsSet = new HashSet<String>();
						
			if(id.equals("DEV-MUC3-0873")) {
				System.out.println();
			}
			
			if(text.contains("RECEIVED") && text.contains("WOUNDS")) {
				weaponsSet.add("BULLETS");
			}
			if(text.contains("PLACED") && text.contains("UNDER")) {
				weaponsSet.add("DYNAMITES");
			}
			
			Document d = new Document(text);
			
			for(Sentence s : d.sentences()) {
				//System.out.println(s.text());
				for(String s1 : weaponGeneralRules.keySet()) {
					//System.out.println(s1);
					if(s.text().matches(".*\\b" + s1 + "\\b.*")) {
						String w = parseWeaponRule(weaponGeneralRules.get(s1), s.text());
						if(w != null) {
							weaponsSet.add(w);
						}
					}
				}
			}
//			System.out.print(id + " ");
//			for(String s : weaponsSet) {
//				System.out.print(s + " ");
//			}
//			System.out.println();
			
			if (id.startsWith("DEV") || id.startsWith("TST")) {
				System.out.println(printTemplate(id, getIncident(text), weaponsSet,
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

	public static String printTemplate(String id, String incident, HashSet<String> weapon, List<String> perpIndiv,
			HashSet<String> perpOrg, List<String> target, List<String> victim) {
		if(id.equals("DEV-MUC3-0012")) {
			System.out.println();
		}
		String template = "";
		template += "ID: " + id + "\n";
		template += "INCIDENT: " + incident + "\n";
		template += "WEAPON: ";
		int count = 0;
		if(weapon.size() == 0) {
			template += "-";
			template += "\n";
		}
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
		
		HashSet<String> attackKeyWords = new HashSet<String>(
				Arrays.asList("MURDER", "MURDERED", "ASSASSINATE", "ASSASSINATED"));
		
		for (String s : attackKeyWords) {
			if (text.contains(s)) {
				return "ATTACK";
			}
		}

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
			if(text.contains("BOMB BLAST")) {
				return "BOMBING";
			}
			if (text.contains(s) && !text.contains("GOVERNMENT")) {
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
