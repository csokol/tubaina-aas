package controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import models.PDFGenerator;
import models.TextBookGenerator;
import play.Configuration;
import play.Play;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {
  
    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }
    
    public static Result generate(String course, String type) throws IOException, InterruptedException {
        Configuration configuration = Play.application().configuration();
        String tempDir = configuration.getString("tubaina.output.dir");
        String apostilasDir = configuration.getString("tubaina.apostilas.dir");
        String templatePath = configuration.getString("tubaina.templates.dir");
        File outputDir = new File(tempDir, System.currentTimeMillis() + "");
        File templateDir = new File(templatePath);
        
        TextBookGenerator textBookGenerator = new TextBookGenerator(course, type, apostilasDir, outputDir, templateDir);
		textBookGenerator.run();
        
        File pdfDir = new File(outputDir, "latex");
        new PDFGenerator("/resources/pdflatex.sh").generate(pdfDir);
        
        response().setHeader("Content-Disposition", "attachment; filename=" + course + ".pdf");
        return ok(new File(pdfDir, "book.pdf"));
    }

  
}
