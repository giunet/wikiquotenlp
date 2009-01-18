package JWPL.parser_tutorial;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import JWPL.tools.DetectionLanguage;
import JWPL.tools.WikiMisc;

import de.tudarmstadt.ukp.wikipedia.parser.Content;
import de.tudarmstadt.ukp.wikipedia.parser.NestedList;
import de.tudarmstadt.ukp.wikipedia.parser.NestedListContainer;
import de.tudarmstadt.ukp.wikipedia.parser.Paragraph;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import de.tudarmstadt.ukp.wikipedia.parser.Section;
import de.tudarmstadt.ukp.wikipedia.parser.Table;
import de.tudarmstadt.ukp.wikipedia.parser.html.HtmlWriter;
import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParser;
import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParserFactory;
import de.tudarmstadt.ukp.wikipedia.api.DatabaseConfiguration;
import de.tudarmstadt.ukp.wikipedia.api.Page;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import de.tudarmstadt.ukp.wikipedia.api.WikiConstants.Language;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiApiException;


public class WikiPerSezioni {
	private static final String LF = "\n";
	 static Writer out;
	 static Writer outins;
	 static int count = 0;
	public static void main(String[] args) throws WikiApiException, SQLException, IOException {
		//db connection settings
		DatabaseConfiguration dbConfig = new DatabaseConfiguration();
        dbConfig.setDatabase("wikiquote");
        dbConfig.setHost("localhost");
        dbConfig.setUser("root");
        dbConfig.setPassword("15081985");
        dbConfig.setLanguage(Language.italian);
        out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:/scrivimi.txt"), "UTF8"));
		outins = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:/insert.sql"), "UTF8"));
		//initialize a wiki
		Wikipedia wiki = new Wikipedia(dbConfig);
		DetectionLanguage dl = new DetectionLanguage();
		DBManager.connect();
		
		String nameVal= new String();
		Statement s = DBManager.getConnection().createStatement();
		ResultSet rs= null;
		StampaPagineLatine(wiki,dl,s,rs);
		StampaPagineStaticheItaliane(wiki,dl,s,rs);
		StampaPagineSezioneProverbiModi(wiki,dl,s,rs);
		//s.executeQuery("SELECT * FROM page p where p.text like \"%= modi di%\" or p.text like \"%=modi di%\" or p.text like \"%= [[modi di%\" or p.text like \"%=[[modi di%\" or p.text like \"%= ''modi di%\" or p.text like \"%=  ''modi di%\" order by name;");
		   rs.close ();
		   s.close ();
		   out.close();
		   outins.close();
		      
		   System.out.println (count + " rows were retrieved");
		   
	}
	
	public static void StampaPagineLatine(Wikipedia wiki,DetectionLanguage dl,Statement s,ResultSet rs)throws WikiApiException, SQLException, IOException{
		s.executeQuery("Select * from page p where p.name='Proverbi_latini' or p.name='Modi_di_dire_latini';");
		   rs = s.getResultSet ();
		   String nameVal = "";
		   while (rs.next ())
		   {
			   nameVal = rs.getString ("name");    
		       System.out.println ("Nome PAG = " + nameVal);
		   
		       List <String> cits=giveCitLatine(wiki, nameVal);
		       
		       //ArrayList<String>cit= WikiMisc.pulisciVoci(cits);
		       //System.out.println("-->"+cit.size()+" "+cits.size());
		       for (String citazione: cits){
				//for(NestedList citazione : cits) {
					//controllare lingua e lunghezza
				     String cita= getSubstr(citazione);
					if (tokenizeString(cita, 13)){
						if (cita.length()>0){
						   System.out.println("citazione:"+cita);
						   String risultato =dl.detectLang(cita); 
						   System.out.println(risultato);
						   if (risultato.equalsIgnoreCase("ITALIAN")){
							     cita=cita.replaceAll("È", "e'");
						   	     cita=cita.replaceAll("à", "a'");
						   	     cita=cita.replaceAll("è", "e'");
						   	     cita=cita.replaceAll("ì", "i'");
						   	     cita=cita.replaceAll("ò", "o'");
						   	     cita=cita.replaceAll("ù", "u'");
						   	     ++count;
							     writeLine(cita,count,nameVal); //scrive su file
						   }
						}
					}

				}
		       
		   }
	}
	
	public static void StampaPagineSezioneProverbiModi(Wikipedia wiki,DetectionLanguage dl,Statement s,ResultSet rs)throws WikiApiException, SQLException, IOException{
		   s.executeQuery ("SELECT * FROM page p where p.text like \"%= proverbi%\" or p.text like \"%=proverbi%\" or p.text like \"%= [[proverbi%\" or p.text like \"%=[[proverbi%\" or p.text like \"%= ''proverbi%\" or p.text like \"%=  ''proverbi%\" or  p.text like \"%= modi di%\" or p.text like \"%=modi di%\" or p.text like \"%= [[modi di%\" or p.text like \"%=[[modi di%\" or p.text like \"%= ''modi di%\" or p.text like \"%=  ''modi di%\" order by name;");
		   rs = s.getResultSet ();
		   String nameVal = "";
		   while (rs.next ())
		   {
			   nameVal = rs.getString ("name");    
		       System.out.println ("Nome PAG = " + nameVal);
		   
		       List <NestedList> cits=giveCit(wiki, nameVal);
		       
		       ArrayList<String>cit= WikiMisc.pulisciVoci(cits);
		       //System.out.println("-->"+cit.size()+" "+cits.size());
		       for (String citazione: cit){
				//for(NestedList citazione : cits) {
					//controllare lingua e lunghezza
				     String cita= getSubstr(citazione);
					if (tokenizeString(cita, 13)){
						if (cita.length()>0){
						   System.out.println("citazione:"+cita);
						   String risultato =dl.detectLang(cita); 
						   System.out.println(risultato);
						   if (risultato.equalsIgnoreCase("ITALIAN")){
							     cita=cita.replaceAll("È", "e'");
						   	     cita=cita.replaceAll("à", "a'");
						   	     cita=cita.replaceAll("è", "e'");
						   	     cita=cita.replaceAll("ì", "i'");
						   	     cita=cita.replaceAll("ò", "o'");
						   	     cita=cita.replaceAll("ù", "u'");
						   	     ++count;
							     writeLine(cita,count,nameVal); //scrive su file
						   }
						}
					}

				}
		       
		   }
	}
	
	//pagine proverbi e modi di dire italiani + slogan
	public static void StampaPagineStaticheItaliane(Wikipedia wiki,DetectionLanguage dl,Statement s,ResultSet rs) throws WikiApiException, SQLException, IOException{
		s.executeQuery("Select * from page p where p.name='Proverbi_italiani' or p.name='Modi_di_dire_italiani' or p.name='Slogan';");
		rs = s.getResultSet();
		String nameVal = "";
		while (rs.next ())  {
			   nameVal = rs.getString ("name");    
		       System.out.println ("Nome PAG = " + nameVal); 
		       List <NestedList> cits=giveCitProv_Mod_Slogan_Ita(wiki, nameVal);
		       ArrayList<String>cit= WikiMisc.pulisciVoci(cits);
		       for (String citazione: cit){			
					//controllare lingua e lunghezza
				     String cita= getSubstr(citazione);
					if (tokenizeString(cita, 13)){
						if (cita.length()>0){
						   System.out.println("citazione:"+cita);
						   String risultato =dl.detectLang(cita); 
						   System.out.println(risultato);
						   if (risultato.equalsIgnoreCase("ITALIAN")){
							     cita=cita.replaceAll("È", "e'");
						   	     cita=cita.replaceAll("à", "a'");
						   	     cita=cita.replaceAll("è", "e'");
						   	     cita=cita.replaceAll("ì", "i'");
						   	     cita=cita.replaceAll("ò", "o'");
						   	     cita=cita.replaceAll("ù", "u'");
						   	     ++count;
							     writeLine(cita,count,nameVal); //scrive su file
						   }
						}
					}
				}  
		       
		   }
	}
	
	public static List <NestedList> giveCit(Wikipedia wiki,String pag) throws WikiApiException{
		//get the page 'Dog'
		Page p = wiki.getPage(pag);
		List <NestedList> citazioniTot=new ArrayList<NestedList>() ;

		//get a ParsedPage object
		MediaWikiParserFactory pf = new MediaWikiParserFactory();

		MediaWikiParser parser = pf.createParser();
		ParsedPage pp = parser.parse(p.getText());

		HtmlWriter html= new HtmlWriter();
		//System.out.println("HTML:"+html.parsedPageToHtml(pp));


		//get the sections of the page
		List<Section> sections = pp.getSections();
		for(Section section : sections) {
			//System.out.println("---------------------------------------------\n SEZIONE:"+section.getTitle()+"\n");
//			List <Content> contents=section.getContentList();
//			for(Content content: contents){
//			   System.out.println("content:"+content.getText()+"\n--");
//			}
			//	String prova= section.getTitle()+ section.getText() + "\n";
			//System.out.println(section.nrOfDefinitionLists() +" "+ section.nrOfNestedLists() + " "+section.nrOfParagraphs() +" "+ section.nrOfTables()+"");
			//System.out.println(section.getTitle()+"\n");
			//stampa paragrafi di una sezione
			//List<Paragraph> paragraphs =section.getParagraphs();

			//for(Paragraph paragraph : paragraphs) {
			//	System.out.println("paragrafo:"+ paragraph.getText());
			//}
			//stampa liste puntate di una sezione
			String nomeSezione=section.getTitle();
			if (nomeSezione!=null){
			  nomeSezione= nomeSezione.toLowerCase();
			}
			if ((nomeSezione==null) || (nomeSezione.contains("proverbi")) || (nomeSezione.contains("modi di "))){
			List <NestedListContainer> nestedlists =section.getNestedLists();
			
			for(NestedListContainer nestedlist : nestedlists) {
				//System.out.println("nested list:"+nestedlist.getText());	
				List <NestedList> citazioni=nestedlist.getNestedLists();
				//for(NestedList citazione : citazioni) {
				//	System.out.println("citazione:"+citazione.getText());   
				//}
                 citazioniTot.addAll(citazioni);
				//System.out.println("FINE LISTA CITAZIONI:");
			}
			} else{
				System.out.println("------------------SEZIONE NON OK--------:"+ nomeSezione);
				
			}

			//List <Table> tables= section.getTables();

			//for(Table table  : tables) {
				//System.out.println("tables:"+ table);	
			//}

           
		}
		return citazioniTot;	
	}
	

	public static List <NestedList> giveCitProv_Mod_Slogan_Ita(Wikipedia wiki,String pag) throws WikiApiException{
		//get the page 'Dog'
		Page p = wiki.getPage(pag);
		List <NestedList> citazioniTot=new ArrayList<NestedList>() ;

		//get a ParsedPage object
		MediaWikiParserFactory pf = new MediaWikiParserFactory();

		MediaWikiParser parser = pf.createParser();
		ParsedPage pp = parser.parse(p.getText());

		HtmlWriter html= new HtmlWriter();
		//System.out.println("HTML:"+html.parsedPageToHtml(pp));

		//get the sections of the page
		List<Section> sections = pp.getSections();
		for(Section section : sections) {

			//stampa liste puntate di una sezione
			String nomeSezione=section.getTitle();
			if (nomeSezione!=null){
				  nomeSezione= nomeSezione.toLowerCase();
				}
			if ((nomeSezione==null) || !(nomeSezione.contains("voci correlate"))){
				List <NestedListContainer> nestedlists =section.getNestedLists();
				
				for(NestedListContainer nestedlist : nestedlists) {
					//System.out.println("nested list:"+nestedlist.getText());	
					List <NestedList> citazioni=nestedlist.getNestedLists();
	                 citazioniTot.addAll(citazioni);
				}
			}else{
				System.out.println("------------------SEZIONE NON OK--------:"+ nomeSezione);
			}
		}
		return citazioniTot;	
	}

	public static List <String> giveCitLatine(Wikipedia wiki,String pag) throws WikiApiException{
		//get the page 'Dog'
		Page p = wiki.getPage(pag);
		List <String> citazioniTot=new ArrayList<String>() ;

		//get a ParsedPage object
		MediaWikiParserFactory pf = new MediaWikiParserFactory();

		MediaWikiParser parser = pf.createParser();
		ParsedPage pp = parser.parse(p.getText());

		//HtmlWriter html= new HtmlWriter();
		//System.out.println("HTML:"+html.parsedPageToHtml(pp));

		//get the sections of the page
		List<Section> sections = pp.getSections();
		for(Section section : sections) {
			//stampa liste puntate di una sezione
			String nomeSezione=section.getTitle();
			if (nomeSezione!=null){
				  nomeSezione= nomeSezione.toLowerCase();
				}
			if (!(nomeSezione==null)){ 
				if (!((nomeSezione.contains("voci correlate")) || (nomeSezione.contains("note")))){
					List <Paragraph> paragrafo =section.getParagraphs();
					
					for(Paragraph nestedlist : paragrafo) {
						//System.out.println("nested list:"+nestedlist.getText());	
						 String citazioni=nestedlist.getText();
		                 citazioniTot.add(citazioni);
					}
				}
			}else{
				System.out.println("------------------SEZIONE NON OK--------:"+ nomeSezione);
			}
		}
		return citazioniTot;	
	}
	
	public static String  getSubstr(String s){
		try{
			String stringa = "";
			int index = s.length();
			if (s.indexOf("(")!=-1) {
				index = s.indexOf("(");
			}
			stringa = s.substring(0, index);
			return stringa;
			}catch (StringIndexOutOfBoundsException exc) {
				System.out.println("ECCEZIONE"+ s);
				  return "";
		}
	}
	
	public static boolean tokenizeString(String stringa, int numMaxToken){
		
		   boolean b = false;
		   StringTokenizer st = new StringTokenizer(stringa, " ");
		   int numToken = st.countTokens();
		   if (numToken<numMaxToken) {
			 b = true;
		   }
		   return b;
		
		
	}
	
	
	public static void writeLine(String line,int progressivo,String nomePagina){
		FileWriter fw = null;
		FileWriter insert = null;
		String linemod = null;
		String titlemod = null;
		try {
			fw = new FileWriter("C:/scrivimi.txt", true);
			insert = new FileWriter("C:/insert.sql",true);
			
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		BufferedWriter bw = new BufferedWriter (fw);
		BufferedWriter iw = new BufferedWriter (insert);
		PrintWriter outFile = new PrintWriter (bw);
		PrintWriter outinsert = new PrintWriter (iw);

		outFile.println(line);
		linemod = line.replace("'", "\\'");	
		titlemod= nomePagina.replace("'", "\'");
		outinsert.println("Insert into citazioni values (" + progressivo + ",'B" + progressivo + "','" + titlemod + "','" + linemod + "');"+"\n");
		
		outFile.close();
		outinsert.close();
		   try {
		       
		        out.append(line+"\n"); 
		        outins.append("Insert into citazioni values (" + progressivo + ",'B" + progressivo + "','" + titlemod + "','" + linemod + "');"+"\n");
		    
		    } catch (IOException e) {
		    }

		
		
	}
	
	
}

