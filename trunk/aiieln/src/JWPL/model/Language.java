package JWPL.model;

public class Language {
	
	private String language;
	private String lang;
	
	
	public String getLang() {
		return lang;
	}


	public void setLang(String lang) {
		this.lang = lang;
	}


	public String getLanguage() {
		return language;
	}


	public void setLanguage(String language) {
		this.language = language;
	}


	public Language() {
		// TODO Auto-generated constructor stub
	}
	
	public void addLang(String lingua, String lang){
		this.language = lingua;
		this.lang = lang;
	}

}
