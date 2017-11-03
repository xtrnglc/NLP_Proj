package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import edu.stanford.*;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;

public class infoextract {
	public static void main(String args[]) throws FileNotFoundException, IOException {
		
		String file = args[0];
		String id = "";
		String text = "";
		int i = 0;
			
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	if(i==0) {
		    		id = line;
		    	} else {
			    	text += line;
		    	}
		    	i++;
		    }
		}
		
		Document doc = new Document(text);
		for(Sentence s : doc.sentences()) {
			System.out.println(s.text());
			for(int j = 0; j < s.words().size(); j++) {
				System.out.println(s.word(j) + " " + s.posTag(j) + " " + s.nerTag(j));
			}
			System.out.println("\n\n");			
		}
		
		System.out.println("Done");
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