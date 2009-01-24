package JWPL.tools;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
/**
 * 
 * Classe Wshingling:
 *
 * Implementa il metodo del w-shingling per determinare il grado di somiglianza fra due stringhe.
 * Questo metodo è stato utilizzato per l'intersezione delle liste di polirematiche prese in considerazione, 
 * quindi per evitare doppioni nella lista intersecata finale. 
 * 
 * 
 */

public class Wshingling {

	private double prec;
	private int w;
	private ArrayList<String> listaPoli;
	private Hashtable<String, Set<String>> shingHash;

	/**
	 * Costruttore di classe, setta le liste da confrontare, il valore di w e la precisione.
	 * @param fileListe array contenente i nomi dei file delle liste da confrontare.
	 * @param numW setta il valore w che indica la cardinalità dei sottoinsiemi di ciascun shingling.
	 * @param precision setta il valore di precisione da utilizzare da 0 a 1 (indice di Jaccard). 
	 */
	public Wshingling(ArrayList<String> lista, int numW, double precision) {
		setListe(lista);
		setW(numW);
		setPrecision(precision);
	}


	/**
	 * Setta il valore w.
	 * @param numW setta il valore w che indica la cardinalità dei sottoinsiemi di ciascun shingling.
	 */
	public void setW(int numW){
		this.w=numW;
	}

	/**
	 * Setta la precisione.
	 * @param precision setta il valore di precisione da utilizzare da 0 a 1 (indice di Jaccard). 
	 */
	public void setPrecision(double precision){
		if(precision>=0&precision<=1)
			this.prec=precision;
	}

	/**
	 * Setta l'array in cui sono inseriti i nomi dei file che contengono le liste.
	 * @param fileListe array contenente i nomi dei file delle liste da confrontare.
	 */
	public void setListe(ArrayList<String> lista){
		this.listaPoli = lista;
	}


	/**
	 * Consente di intersecare le liste specificate da fileListe e crearne una in output.
	 * @param nomeFileOut nome del file in cui salvare la lista intersecata.
	 */
	public void intersezioneListe(String nomeFileOut){
		shingHash = new Hashtable<String, Set<String>>();
		Set<String> shingSet = new HashSet<String>();
		String listaConc="";
		StopWord swClear = new StopWord("stoplist.txt");
		for (int i = 0; i < listaPoli.size(); i++) {
			String poli = listaPoli.get(i);
			poli = swClear.stopwordClear(poli);
			String[] tokens = poli.split(" ");
			shingSet = new HashSet<String>();
			for (int k = 0; k < tokens.length; k++) {
				int z = k;
				tokens[k]=tokens[k].toLowerCase().trim();
				if(z<=tokens.length-w){
					String shing = "";
					for (z=k ;z < k+w; z++) {
						shing=shing.concat(tokens[z]);
					}
					shingSet.add(shing);
				}else{
					shingSet.add("");
				}
			}
			shingHash.put(listaPoli.get(i), shingSet);
		}
		eliminaDuplicati();
		for (int i = 0; i < listaPoli.size(); i++) {
			listaConc = listaConc.concat(listaPoli.get(i)+"\n");
		}
		GestioneFile.scritturaFile(listaConc, nomeFileOut);


	}

	//metodo che elimina i duplicati
	private void eliminaDuplicati() {
		Set<String> setShingA;
		Set<String> setShingB;

		for (int i = 0; i < listaPoli.size(); i++) {
			setShingA = new HashSet<String>(shingHash.get(listaPoli.get(i).toLowerCase()));
			for (int j = i+1; j < listaPoli.size(); j++) {

				setShingB = new HashSet<String>(shingHash.get(listaPoli.get(j).toLowerCase()));
				if(!setShingA.isEmpty() & !setShingB.isEmpty()){
					if(confronto(setShingA, setShingB)){
						if(listaPoli.get(i).split(" ").length>=listaPoli.get(j).split(" ").length)
							listaPoli.remove(j);
						else
							listaPoli.remove(i);
					}
				}
			}
		}	
	}

	/*attraverso l'indice di Jaccard calcola la somiglianza fra 2 insiemi e in base alla precisione
	  settata stabilisce se questi sono simili (true) o no (false)*/ 
	private boolean confronto(Set<String> a, Set<String> b) {
		int intersezione=0, unione = 0;
		double somiglianza=0;
		if (!a.isEmpty() && !b.isEmpty()) {
			for (String elementa : a) {
				for (String elementb : b) {
					if(elementa.equalsIgnoreCase(elementb)){					
						intersezione++;		
					}
				}
			}
			unione=(a.size()+b.size())-intersezione;
		}
		somiglianza=intersezione/unione;
		if (somiglianza>=prec)
			return true;
		else
		
			return false;
	}
	public Set caricaSet(String nomeFile) throws IOException{
		Set insiemeCitazioni = null;
		
		File file = new File(nomeFile);
		FileInputStream fis=new FileInputStream(file);
		InputStreamReader isr=new InputStreamReader(fis);
		BufferedReader br=new BufferedReader(isr);
		String linea=br.readLine();
		while (linea!=null){
			insiemeCitazioni.add(linea);
		}
		return insiemeCitazioni;
	}
}
