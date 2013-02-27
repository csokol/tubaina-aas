package controllers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.zip.ZipOutputStream;

import play.Configuration;
import play.Play;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import br.com.caelum.tubaina.ParseType;
import br.com.caelum.tubaina.TubainaBuilder;

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
        
        runTubaina(course, type, apostilasDir, outputDir, templateDir);
        
        InputStream bashStream = Application.class.getResourceAsStream("/resources/pdflatex.sh");
        String script = new Scanner(bashStream).useDelimiter("$$").next();
        File pdfDir = new File(outputDir, "latex");
        String output = new ArapucaCommandExecutor().execute("/usr/bin/bash", script, pdfDir);
        System.out.println(output);
        
        response().setHeader("Content-Disposition", "attachment; filename=" + course + ".pdf");
        return ok(new File(pdfDir, "book.pdf"));
    }

    private static void zipOutput(File outputDir, ByteArrayOutputStream byteArrayOutputStream)
            throws IOException {
        Zipper zipper = new Zipper();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
        zipper.addDirectory(zipOutputStream, outputDir);
        
        byteArrayOutputStream.close();
        zipOutputStream.finish();
    }

    private static void runTubaina(String course, String type, String apostilasDir, File outputDir,
            File templateDir) throws IOException {
        TubainaBuilder tubainaBuilder = new TubainaBuilder(ParseType.LATEX);
        tubainaBuilder.bookName(course);
        tubainaBuilder.templateDir(templateDir);
        tubainaBuilder.inputDir(new File(apostilasDir + course));
        outputDir.mkdirs();
        tubainaBuilder.outputDir(outputDir);
        
        if (type.equals("instrutor")) { 
            tubainaBuilder.showNotes();
        }
        
        tubainaBuilder.build();
    }
  
}
