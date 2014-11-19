package in.twizmwaz.cardinal.util;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;

/**
 * Created by kevin on 11/18/14.
 */
public class DomUtil {

    public static Document parse(File file) throws JDOMException, IOException {
        SAXBuilder saxBuilder = new SAXBuilder();
        return saxBuilder.build(file);

    }


}
