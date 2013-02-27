package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import models.PDFGenerator;
import models.PdfGenerated;
import models.TextBookGenerator;

import org.apache.commons.io.IOUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;

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
		final Configuration configuration = Play.application().configuration();
		String tempDir = configuration.getString("tubaina.output.dir");
		final String apostilasDir = configuration
				.getString("tubaina.apostilas.dir");
		final String templatePath = configuration
				.getString("tubaina.templates.dir");
		final File outputDir = new File(tempDir, System.currentTimeMillis()
				+ "");
		final File templateDir = new File(templatePath);
		final String emailToSend = session().get("email");

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
				if (emailToSend != null) {
					sendEmail(course, configuration, emailToSend, pdf);
				}

				return null;
			}

		});

		return ok("Acesse "
				+ routes.Application.listPdfs().absoluteURL(request())
				+ " para ver se a apostila foi gerada");
	}

	private static void sendEmail(final String course,
			final Configuration configuration, final String emailToSend,
			File pdf) throws EmailException {
		MultiPartEmail email = new MultiPartEmail();
		email.setHostName("smtp.gmail.com");
		email.setSmtpPort(587);
		email.setAuthentication("aagendatech@gmail.com",
				configuration.getString("email.password"));
		email.setSubject("Sua apostila do curso " + course);
		email.addTo("tubainasaas@caelum.com.br");
		email.setFrom(emailToSend);
		email.attach(pdf);
		email.setMsg("Obrigado por usar o tubaina as a service. Sua apostila encontra-se em anexo");
		email.send();
	}

	@With(LoggedAction.class)
	public static Result listPdfs() {
		List<PdfGenerated> pdfs = PdfGenerated.finder.all();
		Collections.sort(pdfs);
		return ok(views.html.pdfs.render(pdfs));
	}

	@With(LoggedAction.class)
	public static Result download(Long id) {
		PdfGenerated pdf = PdfGenerated.finder.byId(id);
		response().setHeader("Content-Disposition",
				"attachment; filename=" + pdf.getName() + ".pdf");
		return ok(pdf.getFile());
	}

}
