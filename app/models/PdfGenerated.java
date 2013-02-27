package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;


@Entity
public class PdfGenerated extends Model {
    
    private static final long serialVersionUID = 1L;

    @Id
    public Long id;

    @Constraints.Required
    public String name;

    @Formats.DateTime(pattern="dd/MM/yyyy")
    public Date date = new Date();
    
    @Lob
    private byte[] file;
    
    public static Finder<Long, PdfGenerated> finder = new Finder<Long, PdfGenerated>(
            Long.class, PdfGenerated.class);
    
    public PdfGenerated(String name, byte[] file) {
        this.name = name;
        this.file = file;
    }
    
}
