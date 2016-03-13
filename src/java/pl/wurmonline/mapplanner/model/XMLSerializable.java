package pl.wurmonline.mapplanner.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface XMLSerializable {

    public abstract Element serialize(Document doc);
    
}
