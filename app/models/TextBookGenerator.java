package models;

import java.io.File;
import java.io.IOException;

import br.com.caelum.tubaina.ParseType;
import br.com.caelum.tubaina.TubainaBuilder;

public class TextBookGenerator {

	private final String course;
	private final String type;
	private final String apostilasDir;
	private final File outputDir;
	private final File templateDir;

	public TextBookGenerator(String course, String type, String apostilasDir,
			File outputDir, File templateDir) {
		this.course = course;
		this.type = type;
		this.apostilasDir = apostilasDir;
		this.outputDir = outputDir;
		this.templateDir = templateDir;

	}

	public void run() throws IOException {
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
