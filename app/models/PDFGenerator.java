package models;

import java.io.File;
import java.io.InputStream;
import java.util.Scanner;

import controllers.Application;
import controllers.ArapucaCommandExecutor;

public class PDFGenerator {

	private String script;

	public PDFGenerator(String scriptPath) {
        InputStream bashStream = Application.class.getResourceAsStream(scriptPath);
        script = new Scanner(bashStream).useDelimiter("$$").next();
	}

	public String generate(File pdfDir) {
		String output = new ArapucaCommandExecutor().execute("/usr/bin/bash", script, pdfDir);
		return output;
	}
	
	

}
