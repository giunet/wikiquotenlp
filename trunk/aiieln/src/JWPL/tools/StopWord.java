package JWPL.tools;
import java.io.*;
import java.util.*;
/**
 * 
 * Classe StopWord:
 *
 * Fornisce i metodi per l'eliminazione delle Stopword all'interno di un testo.
 * 
 * 
 */

public class StopWord {
	private Set<String> stopwords;

	/**
	 * Costruttore di classe, permette di definire la lista delle stopword da caricare.
	 * @param nomeFile nome del file che contiene la lista di stopword. 
	 */
	public StopWord(String nomeFile) {
		stopwords = new HashSet<String>();		
		createSet(nomeFile);
	}

	//carica la lista delle stopword in un set, quindi evita anche gli eventuali doppioni.
	private void createSet(String nomeFile){ 
		try {
			BufferedReader fileRead = new BufferedReader(new FileReader(nomeFile));
			int i = 0; 
			String item;
			while (fileRead.ready()) {
				item = fileRead.readLine();
				item = item.toLowerCase().trim();
				stopwords.add(item);
				i++;
			} 
			fileRead.close();

		} 
		catch (IOException e)
		{
			System.out.println(e);
		} 
	}

	/**
	 * Elimina dalla stringa presa in input le stopword, segni di punteggiatura, spazi in eccesso.
	 * @param token stringa da cui eliminare le stopword, la punteggiatura, spazi in eccesso.
	 * @return out stringa elaborata.
	 */
	public String stopwordClear(String token){
		String out = "";
		token = token.replaceAll ("[\\p{Punct}]", " ");
		String[] words = token.split("\\s");
		for (int i = 0; i<words.length; i++){
			words[i]=words[i].trim().toLowerCase();
			if(stopwords.contains(words[i])) {	
				words[i] = "";
			}
			if (!(words[i].equalsIgnoreCase(""))) {
				out = out.concat(words[i]+" ");			
			}

		}
		int finalws = out.length()-1;
		if(!out.isEmpty())
			out = out.substring(0, finalws);
		return out;
	}


}