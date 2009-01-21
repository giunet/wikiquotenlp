import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class GestioneFile {

	public static ArrayList<String> letturaFile(String nomeFile){
		File file = new File(nomeFile);
		ArrayList<String> righe = new ArrayList<String>();
		try {
			FileInputStream fis=new FileInputStream(file);
			InputStreamReader isr=new InputStreamReader(fis);
			BufferedReader br=new BufferedReader(isr);
			String linea=br.readLine();
			while(linea!=null) {
				//System.out.println(linea);;
				righe.add(linea);
				linea=br.readLine();
			}
			for(int i=0; i<righe.size(); i++) {
				//System.out.println(righe.get(i));

			}

		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return righe;
	}


	public static void scritturaFile(String dati, String nomeFile) {

		try {
			FileWriter out = new FileWriter (nomeFile);
			out.write(dati);
			out.close();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
