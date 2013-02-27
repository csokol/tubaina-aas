package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import models.PDFGenerator;
import models.PdfGenerated;
import models.TextBookGenerator;

import org.apache.commons.io.IOUtils;

import play.Configuration;
import play.Play;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import views.html.index;

import com.avaje.ebean.Ebean;

public class Application extends Controller {

	public static Result index() {
		Configuration configuration = Play.application().configuration();
		return ok(index.render(configuration.getString("github.client_id")));
	}

	@With(LoggedAction.class)
	public static Result generate(final String course, final String type)
			throws IOException, InterruptedException {
		Configuration configuration = Play.application().configuration();
		String tempDir = configuration.getString("tubaina.output.dir");
		final String apostilasDir = configuration
				.getString("tubaina.apostilas.dir");
		final String templatePath = configuration
				.getString("tubaina.templates.dir");
		final File outputDir = new File(tempDir, System.currentTimeMillis()
				+ "");
		final File templateDir = new File(templatePath);

		Promise<File> generatingTextBook = play.libs.Akka
				.future(new Callable<File>() {
					public File call() {
						new TextBookGenerator(course, type, apostilasDir,
								outputDir, templateDir).run();
						File pdfDir = new File(outputDir, "latex");
						new PDFGenerator("/resources/pdflatex.sh")
								.generate(pdfDir);
						return new File(pdfDir, "book.pdf");
					}
				});
		generatingTextBook.map(new Function<File, Void>() {

			@Override
			public Void apply(File pdf) throws Throwable {
			    byte[] contents = IOUtils.toByteArray(new FileInputStream(pdf));
			    PdfGenerated pdfGenerated = new PdfGenerated(course, contents);
			    pdfGenerated.save();			    
				return null;
			}
		});

		return ok("Sua apostila está sendo gerada, daqui a pouco você olha no link");
	}
	
	
	@With(LoggedAction.class)
	public static Result listPdfs() {
	    List<PdfGenerated> pdfs = PdfGenerated.finder.all();
	    return ok(views.html.pdfs.render(pdfs));
	}
	
	@With(LoggedAction.class)
	public static Result download(Long id) {
	    PdfGenerated pdf = PdfGenerated.finder.byId(id);
	    response().setHeader("Content-Disposition", "attachment; filename=" + pdf.getName() + ".pdf");	    
	    return ok(pdf.getFile());
	}
	
	
}
