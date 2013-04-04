package controllers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import models.PDFGenerator;
import models.PdfGenerated;
import models.TextBookGenerator;

import org.apache.commons.io.IOUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;

import play.Configuration;
import play.Play;
import play.api.templates.Html;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import views.html.index;

public class Application extends Controller {

	public static Result index() {
		Configuration configuration = Play.application().configuration();
		return ok(index.render(configuration.getString("github.client_id")));
	}

	@With(LoggedAction.class)
	public static Result generate(final String course, final String type)
			throws IOException, InterruptedException {
		final Configuration configuration = Play.application().configuration();
		final String apostilasDir = configuration
				.getString("tubaina.apostilas.dir");
		final File outputDir = new File(configuration.getString("tubaina.output.dir"), System.currentTimeMillis()
				+ "");
		final File templateDir = new File(configuration.getString("tubaina.templates.dir"));
		final String userEmail = session().get("email");

		Promise<File> generatingTextBook = play.libs.Akka
				.future(new Callable<File>() {
					public File call() {
						new TextBookGenerator(course, type, apostilasDir,
								outputDir, templateDir).run();
						File pdfDir = new File(outputDir, "latex");
						play.Logger.debug("executing pdflatex...");
						new PDFGenerator(configuration.getString("bash.path"), "/resources/pdflatex.sh")
								.generate(pdfDir);
						play.Logger.debug("finished generating pdf...");
						File pdf = new File(pdfDir, "book.pdf");
						if (!pdf.exists()) {
							throw new RuntimeException("não foi possível gerar o pdf, procure alguém que possa ajudá-lo");
						}
						return pdf;
					}
				});
		
		generatingTextBook.recover(new Function<Throwable, File>() {
            @Override
            public File apply(Throwable error) throws Throwable {
                try {
                	play.Logger.error("sending error email");
                    sendErrorMail(configuration, error);
                } catch (EmailException e) {
                	play.Logger.error("error", e);
                }
                return null;
            }

            private void sendErrorMail(final Configuration configuration, Throwable error)
                    throws EmailException {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                PrintWriter printWriter = new PrintWriter(out);
                error.printStackTrace(printWriter);
                printWriter.close();
                String stackTrace = out.toString();
                
                SimpleEmail simpleEmail = new SimpleEmail();
                setEmailConf(configuration, simpleEmail);
                play.Logger.error("error while building pdf, sending email to user");
                play.Logger.error(stackTrace);
                String message = "Infelizmente não foi possivel gerar sua apostila, veja o erro abaixo:\n" + stackTrace;
                simpleEmail.setMsg(message);
                simpleEmail.setSubject("Erro na geração de apostila");
                simpleEmail.addTo(userEmail);
                simpleEmail.send();
            }
        });
		
		generatingTextBook.map(new Function<File, Void>() {
			@Override
			public Void apply(File pdf) throws Throwable {
				play.Logger.info("reading pdf: " + pdf);
				byte[] contents = IOUtils.toByteArray(new FileInputStream(pdf));
				PdfGenerated pdfGenerated = new PdfGenerated(course, contents);
				pdfGenerated.save();
				if (userEmail != null) {
				    try {
				        sendEmail(course, configuration, userEmail, pdf);
				    } catch (EmailException e) {
				    	play.Logger.error("error", e);
                    }
				}

				return null;
			}

		});
		
		flash().put("successMessage", "Sua apostila está sendo gerada, aguarde um email pacientemente.");
		return redirect(routes.Application.listPdfs());
	}

	private static void sendEmail(final String course,
			final Configuration configuration, final String emailToSend,
			File pdf) throws EmailException {
		MultiPartEmail email = new MultiPartEmail();
		
		setEmailConf(configuration, email);
        
        email.attach(pdf);
		email.setSubject("Sua apostila do curso " + course);
		email.addTo(emailToSend);
		email.setMsg("Obrigado por usar o tubaina as a service. Sua apostila encontra-se em anexo");
		email.send();
	}

    private static void setEmailConf(final Configuration configuration, Email email)
            throws EmailException {
        email.setHostName("smtp.gmail.com");
		email.setSmtpPort(587);
		email.setTLS(true);
        email.setAuthenticator(new DefaultAuthenticator("tubaina.aas@gmail.com",
                configuration.getString("email.password")));
        email.setFrom("tubainasaas@caelum.com.br");
    }

	@With(LoggedAction.class)
	public static Result listPdfs() {
		List<PdfGenerated> pdfs = PdfGenerated.finder.all();
		Collections.sort(pdfs);
		List<String> courses = Arrays.<String>asList("WD-01", "WD-43", "WD-47", "FJ-11", 
				"FJ-21", "FJ-22", "FJ-25", "FJ-26", "FJ-27", "FJ-31", "FJ-34", "FJ-91", "FJ-57"
				, "RR-71", "RR-75", "IP-67", "PM-83", "PM-87");
		return ok(views.html.pdfs.render(pdfs, courses));
	}

	@With(LoggedAction.class)
	public static Result download(Long id) {
		PdfGenerated pdf = PdfGenerated.finder.byId(id);
		response().setHeader("Content-Disposition",
				"attachment; filename=" + pdf.getName() + ".pdf");
		return ok(pdf.getFile());
	}

}
