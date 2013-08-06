package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;

import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;


@Entity
public class PdfGenerated extends Model implements Comparable<PdfGenerated> {
    
    private static final long serialVersionUID = 1L;

    @Id
    public Long id;

    @Constraints.Required
    public String name;

    @Formats.DateTime(pattern="dd/MM/yyyy")
    public Date date = new Date();
    
    @Lob
    private byte[] file;
    
    @Enumerated(EnumType.STRING)
    private PdfType type;
    
    public static Finder<Long, PdfGenerated> finder = new Finder<Long, PdfGenerated>(
            Long.class, PdfGenerated.class);
    
    public PdfGenerated(String name, PdfType type, byte[] file) {
        this.name = name;
		this.type = type;
        this.file = file;
    }

    
    public byte[] getFile() {
        return file;
    }
    
    @Override
	public String toString() {
		return "PdfGenerated [id=" + id + ", name=" + name + ", date=" + date
				+ ", type=" + type + "]";
	}

	public String getName() {
        return name;
    }
	
	public PdfType getType() {
		return type;
	}

	@Override
	public int compareTo(PdfGenerated other) {
		return this.date.compareTo(other.date);
	}
}
