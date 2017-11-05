package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import edu.stanford.*;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class infoextract {
	public static void main(String args[]) throws FileNotFoundException, IOException {
		
		//String file = args[0];
		String file = "developset/texts/DEV-MUC3-0803";
		String id = "";
		String text = "";
		int i = 0;
		HashMap<String, String> wordContext = new HashMap<String, String>();
			
//		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
//		    String line;
//		    while ((line = br.readLine()) != null) {
//		    	if(i==0) {
//		    		id = line;
//		    	} else {
//			    	text += line;
//		    	}
//		    	i++;
//		    }
//		}
		
		String s2 = "John Smith used a gun to kill David Tan";
		MaxentTagger tagger = new MaxentTagger("stanford-postagger-full-2017-06-09/models/english-left3words-distsim.tagger");
		String taggedString = tagger.tagString(s2);
		Sentence s1 = new Sentence(s2);
		SemanticGraph sg = s1.dependencyGraph();
		System.out.println(sg.toList());
		
		String s3 = "John placed a car bomb in front of the embassy";
		taggedString = tagger.tagString(s3);
		s1 = new Sentence(s3);
		sg = s1.dependencyGraph();
		System.out.println(sg.toList());
		
		System.out.println("Done");
	}
	
	public static void parseTemplate(String text, String id) {
		System.out.println(id + " " + getIncident(text));
	}
	
	public static String getIncident(String text) {
		String incident = "ATTACK";
		
		HashSet<String> arsonKeyWords = 
				new HashSet<String>(Arrays.asList("BURN", "BURNING", "INCINERATE", "COMBUST", "COMBUSTED", "ON FIRE"));

		for(String s : arsonKeyWords) {
			if(text.contains(s)) {
				return "ARSON";
			}
		}
		
		HashSet<String> kidnapKeyWords = 
				new HashSet<String>(Arrays.asList("KIDNAP", "ABDUCT", "RELEASE", "KIDNAPPING", "KIDNAPPED", "RANSOM", "REGISTRATION", "CAPTURE", "SEIZE", "SEIZED", "SNATCH", "SNATCHED", "HOSTAGE", "ABDUCTED"));

		for(String s : kidnapKeyWords) {
			if(text.contains(s)) {
				return "KIDNAP";
			}
		}

		HashSet<String> bombingKeyWords = 
				new HashSet<String>(Arrays.asList("BOMB", "BOMBING", "EXPLOSION", "EXPLODE", "EXPLODED", "EXPLOSIVE", "BLAST", "BLASTED", "BLEW UP"));

		for(String s : bombingKeyWords) {
			if(text.contains(s)) {
				return "BOMBING";
			}
		}
		
		HashSet<String> robberyKeyWords = 
				new HashSet<String>(Arrays.asList("ROBBED", "ROBBERY", "STOLE", "THEFT", "MUGGING", "THIEF", "HEIST", "ROB", "BURGLAR", "BURGLARY", "STEAL"));

		for(String s : robberyKeyWords) {
			if(text.contains(s)) {
				return "ROBBERY";
			}
		}
		
		return incident;
	}
	
	public static String printTemplate(String id, String incident, String weapon, List<String> perpIndiv, List<String> perpOrg, List<String> target, List<String> victim) {
		String template = "";
		template += "ID: " + id + "\n";
		template += "INCIDENT: " + incident + "\n";
		template += "WEAPON: " + weapon + "\n";
		template += "PERP INDIV: ";
		for(String s : perpIndiv) {
			template += " " + s;
		}
		template += "\n";
		template += "PERP ORG: ";
		for(String s : perpOrg) {
			template += " " + s;
		}
		template += "\n";
		template += "TARGET: ";
		for(String s : target) {
			template += " " + s;
		}
		template += "\n";
		template += "VICTIM: ";
		for(String s : victim) {
			template += " " + s;
		}
		template += "\n";
		return template;
	}
}

//Number
//Tag
//Description
//1.	CC	Coordinating conjunction
//2.	CD	Cardinal number
//3.	DT	Determiner
//4.	EX	Existential there
//5.	FW	Foreign word
//6.	IN	Preposition or subordinating conjunction
//7.	JJ	Adjective
//8.	JJR	Adjective, comparative
//9.	JJS	Adjective, superlative
//10.	LS	List item marker
//11.	MD	Modal
//12.	NN	Noun, singular or mass
//13.	NNS	Noun, plural
//14.	NNP	Proper noun, singular
//15.	NNPS	Proper noun, plural
//16.	PDT	Predeterminer
//17.	POS	Possessive ending
//18.	PRP	Personal pronoun
//19.	PRP$	Possessive pronoun
//20.	RB	Adverb
//21.	RBR	Adverb, comparative
//22.	RBS	Adverb, superlative
//23.	RP	Particle
//24.	SYM	Symbol
//25.	TO	to
//26.	UH	Interjection
//27.	VB	Verb, base form
//28.	VBD	Verb, past tense
//29.	VBG	Verb, gerund or present participle
//30.	VBN	Verb, past participle
//31.	VBP	Verb, non-3rd person singular present
//32.	VBZ	Verb, 3rd person singular present
//33.	WDT	Wh-determiner
//34.	WP	Wh-pronoun
//35.	WP$	Possessive wh-pronoun
//36.	WRB	Wh-adverb