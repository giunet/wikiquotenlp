package JWPL.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import JWPL.model.Language;

public class DetectionLanguage {

	private static ArrayList<Language> langs;

	public DetectionLanguage() {
		String[] lingue = { "AFRIKAANS", "ALBANIAN", "AMHARIC", "ARABIC",
				"ARMENIAN", "AZERBAIJANI", "BASQUE", "BELARUSIAN", "BENGALI",
				"BIHARI", "BULGARIAN", "BURMESE", "CATALAN", "CHEROKEE",
				"CHINESE", "CHINESE_SIMPLIFIED", "CHINESE_TRADITIONAL",
				"CROATIAN", "CZECH", "DANISH", "DHIVEHI", "DUTCH", "ENGLISH",
				"ESPERANTO", "ESTONIAN", "FILIPINO", "FINNISH", "FRENCH",
				"GALICIAN", "GEORGIAN", "GERMAN", "GREEK", "GUARANI",
				"GUJARATI", "HEBREW", "HINDI", "HUNGARIAN", "ICELANDIC",
				"INDONESIAN", "INUKTITUT", "ITALIAN", "JAPANESE", "KANNADA",
				"KAZAKH", "KHMER", "KOREAN", "KURDISH", "KYRGYZ", "LAOTHIAN",
				"LATVIAN", "LITHUANIAN", "MACEDONIAN", "MALAY", "MALAYALAM",
				"MALTESE", "MARATHI", "MONGOLIAN", "NEPALI", "NORWEGIAN",
				"ORIYA", "PASHTO", "PERSIAN", "POLISH", "PORTUGUESE",
				"PUNJABI", "ROMANIAN", "RUSSIAN", "SANSKRIT", "SERBIAN",
				"SINDHI", "SINHALESE", "SLOVAK", "SLOVENIAN", "SPANISH",
				"SWAHILI", "SWEDISH", "TAJIK", "TAMIL", "TAGALOG", "TELUGU",
				"THAI", "TIBETAN", "TURKISH", "UKRAINIAN", "URDU", "UZBEK",
				"UIGHUR", "VIETNAMESE", "UNKNOWN" };

		String[] lng = { "af", "sq", "am", "ar", "hy", "az", "eu", "be", "bn",
				"bh", "bg", "my", "ca", "chr", "zh", "zh-CN", "zh-TW", "hr",
				"cs", "da", "dv", "nl", "en", "eo", "et", "tl", "fi", "fr",
				"gl", "ka", "de", "el", "gn", "gu", "iw", "hi", "hu", "is",
				"id", "iu", "it", "ja", "kn", "kk", "km", "ko", "ku", "ky",
				"lo", "lv", "lt", "mk", "ms", "ml", "mt", "mr", "mn", "ne",
				"no", "or", "ps", "fa", "pl", "pt-PT", "pa", "ro", "ru", "sa",
				"sr", "sd", "si", "sk", "sl", "es", "sw", "sv", "tg", "ta",
				"tl", "te", "th", "bo", "tr", "uk", "ur", "uz", "ug", "vi", "" };

		langs = new ArrayList<Language>();

		for (int i = 0; i < lng.length; i++) {
			Language l = new Language();
			l.addLang(lingue[i], lng[i]);
			langs.add(l);
		}

	}

	public String detectLang(String text) throws IOException {
		
		int pipeIndex;
		if (text.indexOf("|")!=-1) {
			while (text.indexOf("|")!=-1) {
				pipeIndex = text.indexOf("|");
				String a = text.substring(0, pipeIndex);
				
				String b = text.substring(++pipeIndex, text.length());
				text = a.concat(b );
			}			
		}
		
		
		text = text.replaceAll(" ", "%20");
		text = text.replaceAll("è", "%E8");
		text = text.replaceAll("ì", "%EC");
		text = text.replaceAll("ù", "u");
		text = text.replaceAll("\"", "%22");
		text = text.replaceAll("%20%E8", "%E8");
		text = text.replaceAll("%E8%20", "%E8");
		
		URL url = new URL(
				"http://ajax.googleapis.com/ajax/services/language/detect?v=1.0&q="
						+ text);
		BufferedReader in = new BufferedReader(new InputStreamReader(url
				.openStream()));
		String line = "";
		String lingua = "";
		if ((line = in.readLine()) != null) {
			int i = line.indexOf("language");
			i += 11;
			int j = line.indexOf("\",\"isReliable");
			line = line.substring(i, j);
			Iterator<Language> iterator = langs.iterator();
			while (iterator.hasNext()) {
				Language lcurrent = (Language) iterator.next();
				if (lcurrent.getLang().equals(line)) {
					lingua = lcurrent.getLanguage();
				}
			}
		} else {
			System.out.println("problemi di riconoscimento");
		}
		return lingua;
	}

	

	
	
	
}
