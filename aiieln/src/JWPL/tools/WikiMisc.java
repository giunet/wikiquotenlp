package JWPL.tools;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.wikipedia.parser.NestedList;
import de.tudarmstadt.ukp.wikipedia.parser.NestedListContainer;


public class WikiMisc {
public static ArrayList<String> caricaBlackList() throws IOException {
	ArrayList<String> blackList = new ArrayList<String>();
	File bl = new File("C:/blacklist.txt");
	try{
		FileInputStream fis = new FileInputStream(bl);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);
		String line = br.readLine();
		while (line != null){
			blackList.add(line);
			line=br.readLine();
		}
			
	}catch(FileNotFoundException fnfEx){
		System.out.print(fnfEx.toString());
		}
	return blackList;
	}


	public static ArrayList<String> pulisciVoci(List <NestedList> citazioni) throws IOException{
		ArrayList<String> bl = caricaBlackList();
		ArrayList<String> vociValide = new ArrayList<String>();
			   for(NestedList citazione : citazioni) {
				  // System.out.println("citazione: "+citazione.getText());
				   boolean stop = false;
				   for (int i = 0; i < bl.size()-1; i++) {
					  
					   
					  if (citazione.getText().contains(" " + bl.get(i) + " ")){
						  stop=true;
						  System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>CONTIENE:"+bl.get(i));
						  
						}
					  
					}
				   if (stop==false) vociValide.add(citazione.getText()); else
					   System.out.println("ELIMINATA ----->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+citazione.getText());
						
			   }	
	    return vociValide;		
	}
	
	
	
}


