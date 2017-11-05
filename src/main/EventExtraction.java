package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class EventExtraction {

	static List<File> dev_files = new ArrayList<File>();
	static List<String> perp_orgs = new ArrayList<String>();

	public static void main(String args[]) throws FileNotFoundException, IOException {
		File dev_folder = new File(args[0]);
		File[] listOfDevFiles = dev_folder.listFiles();
		File perp_orgs_file = new File(args[1]);

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

		for (File file : dev_files) {
			String id = "";
			String perp_org = "-";
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

			for (int j = 0; j < perp_orgs.size(); j++) {
				if (text.contains(perp_orgs.get(j))) {
					perp_org = perp_orgs.get(j);
					break;
				}
			}

			if (id.startsWith("DEV") || id.startsWith("TST")) {
				System.out.println(printTemplate(id, "-", "-", new ArrayList<String>(Arrays.asList("-")), perp_org,
						new ArrayList<String>(Arrays.asList("-")), new ArrayList<String>(Arrays.asList("-"))));
				System.out.println();
			}
		}
	}

	public static String printTemplate(String id, String incident, String weapon, List<String> perpIndiv,
			String perpOrg, List<String> target, List<String> victim) {
		String template = "";
		template += "ID: " + id + "\n";
		template += "INCIDENT: " + incident + "\n";
		template += "WEAPON: " + weapon + "\n";
		template += "PERP INDIV: ";
		for (String s : perpIndiv) {
			template += " " + s;
		}
		template += "\n";
		template += "PERP ORG: " + perpOrg + "\n";
		template += "TARGET: ";
		for (String s : target) {
			template += " " + s;
		}
		template += "\n";
		template += "VICTIM: ";
		for (String s : victim) {
			template += " " + s;
		}
		template += "\n";
		return template;
	}
}
