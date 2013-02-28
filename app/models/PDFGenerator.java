package models;

import java.io.File;
import java.io.InputStream;
import java.util.Scanner;

import controllers.Application;
import controllers.ArapucaCommandExecutor;

public class PDFGenerator {

	private String script;
    private final String bash;

	public PDFGenerator(String bash, String scriptPath) {
        this.bash = bash;
        InputStream bashStream = Application.class.getResourceAsStream(scriptPath);
        script = new Scanner(bashStream).useDelimiter("$$").next();
	}

	public String generate(File pdfDir) {
		String output = new ArapucaCommandExecutor().execute(bash, script, pdfDir);
		return output;
	}
	
	

}
