package nl.java_etl.xml;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import nl.java_etl.core.StreamConsumer;
import nl.java_etl.core.StreamGenerator;
import nl.java_etl.core.StreamProducer;
import nl.java_etl.core.impl.PipeLine;

public class XmlDocumentReader implements StreamGenerator {
    private PipeLine pipeLine;
    private final Path path;
    private final Map<QName, ElementReader<?>> elementReaders = new HashMap<>(4, 1);
    private XMLEventReader eventReader;

    public XmlDocumentReader(File file) {
        super();
        this.path = Paths.get(file.toURI());
    }

    public XmlDocumentReader(Path path) {
        super();
        this.path = path;
    }

    public void setPipeLine(PipeLine pipeLine) {
        this.pipeLine = pipeLine;
    }

    @Override
    public <T> StreamProducer<T> generate(Class<T> clazz) {
        ElementReader<T> generator = new ElementReader<>(clazz);
        elementReaders.put(generator.getQName(), generator);
        return generator;
    }

    @Override
    public void onStart() {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        Source source = new StreamSource(path.toFile());
        try {
            eventReader = factory.createXMLEventReader(source);
            elementReaders.values().forEach(ElementReader::onStart);
        } catch (XMLStreamException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean tryAdvance() {
        while (eventReader.hasNext()) {
            try {
                XMLEvent event = eventReader.peek();
                if (event.isStartElement()) {
                    QName qName = event.asStartElement().getName();
                    ElementReader<?> reader = elementReaders.get(qName);
                    if (reader != null) {
                        reader.unmarshal(eventReader);
                        return true;
                    }
                }
                eventReader.nextEvent();
            } catch (XMLStreamException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    public static class ElementReader<T> implements StreamProducer<T> {
        private PipeLine pipeLine;
        private final Class<T> clazz;
        private final QName qName;
        private StreamConsumer<T> target;
        private Unmarshaller unmarshaller;

        public ElementReader(Class<T> clazz) {
            super();
            this.clazz = clazz;
            String name = clazz.getAnnotation(XmlRootElement.class).name();
            XmlSchema xmlSchema = clazz.getPackage()
                    .getAnnotation(XmlSchema.class);
            String ns = xmlSchema == null ? null : xmlSchema.namespace();
            this.qName = new QName(ns, name);
        }


        public QName getQName() {
            return qName;
        }

        @Override
        public void setPipeLine(PipeLine pipeLine) {
            this.pipeLine = pipeLine;
        }

        @Override
        public void setTarget(StreamConsumer<T> target) {
            this.target = target;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void setGenericTarget(StreamConsumer<?> target) {
            this.target = (StreamConsumer<T>) target;
        }

        public void onStart() {
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
                unmarshaller = jaxbContext.createUnmarshaller();
            } catch (JAXBException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        @SuppressWarnings("unchecked")
        public void unmarshal(XMLEventReader eventReader) {
            try {
                target.accept((T) unmarshaller.unmarshal(eventReader));
            } catch (JAXBException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

    }
}
