package models;

import java.io.File;
import java.io.IOException;

import br.com.caelum.tubaina.ParseType;
import br.com.caelum.tubaina.TubainaBuilder;

public class TextBookGenerator {

	private final String course;
	private final PdfType type;
	private final String apostilasDir;
	private final File outputDir;
	private final File templateDir;

	public TextBookGenerator(String course, PdfType type, String apostilasDir,
			File outputDir, File templateDir) {
		this.course = course;
		this.type = type;
		this.apostilasDir = apostilasDir;
		this.outputDir = outputDir;
		this.templateDir = templateDir;

	}

	public void run() {
		TubainaBuilder tubainaBuilder = new TubainaBuilder(ParseType.LATEX);
		tubainaBuilder.bookName(course);
		tubainaBuilder.templateDir(templateDir);
		tubainaBuilder.inputDir(new File(apostilasDir + course));
		play.Logger.debug("Creating output dir at " + outputDir.getAbsolutePath());
		outputDir.mkdirs();
		tubainaBuilder.outputDir(outputDir);

		if (type.equals(PdfType.INSTRUCTOR)) {
			tubainaBuilder.showNotes();
		}

		try {
			play.Logger.debug("calling TubainaBuilder.build");
			tubainaBuilder.build();
			play.Logger.debug("end TubainaBuilder.build");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
