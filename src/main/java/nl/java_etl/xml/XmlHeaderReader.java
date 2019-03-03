package nl.java_etl.xml;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * Get the header information from an XML file.
 *
 * @author Gertjan Idema
 *
 * @param <T>
 */
public class XmlHeaderReader {
    private final File file;
    private String version;
    private String encoding;
    private QName rootElementQName;
    private final List<Namespace> namespaces = new ArrayList<>();

    public XmlHeaderReader(File file) {
        super();
        this.file = file;
    }

    public String getVersion() {
        return version;
    }

    public String getEncoding() {
        return encoding;
    }

    public QName getRootElementQName() {
        return rootElementQName;
    }

    public void run() {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = null;
        try (Reader reader = new FileReader(file)) {
            eventReader = factory.createXMLEventReader(reader);
            XMLEvent event;
            do {
                event = eventReader.nextEvent();
                if (event.isStartDocument()) {
                    StartDocument start = (StartDocument) event;
                    this.version = start.getVersion();
                    this.encoding = start.getCharacterEncodingScheme();
                } else if (event.isStartElement()) {
                    StartElement element = event.asStartElement();
                    this.rootElementQName = element.getName();
                    @SuppressWarnings("unchecked")
                    Iterator<Namespace> nsIterator = element.getNamespaces();
                    nsIterator.forEachRemaining(namespaces::add);
                }
            } while (event.getEventType() != XMLStreamConstants.START_ELEMENT);
        } catch (FactoryConfigurationError | XMLStreamException | IOException
                | NullPointerException e) {
            e.printStackTrace();
        } finally {
            if (eventReader != null) {
                try {
                    eventReader.close();
                } catch (XMLStreamException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
