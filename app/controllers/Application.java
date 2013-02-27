package controllers;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

import models.PDFGenerator;
import models.TextBookGenerator;
import play.Configuration;
import play.Play;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {

	public static Result index() {
		return ok(index.render("Your new application is ready."));
	}

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
				System.out.println("agora falta salvar no banco");
				return null;
			}
		});

		// response().setHeader("Content-Disposition",
		// "attachment; filename=" + course + ".pdf");
		return ok("Sua apostila está sendo gerada, daqui a pouco você olha nesse link");
	}
}
