package nl.java_etl.xml;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.namespace.QName;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import nl.java_etl.core.StreamConsumer;
import nl.java_etl.core.StreamGenerator;

public class XmlElementReader<T> implements StreamGenerator<T> {

    private final File file;
    private final Class<T> clazz;
    private final QName qName;
    private StreamConsumer<T> target;

    public XmlElementReader(File file, Class<T> clazz) {
        super();
        this.file = file;
        this.clazz = clazz;
        String name = clazz.getAnnotation(XmlRootElement.class).name();
        String ns = clazz.getPackage().getAnnotation(XmlSchema.class).namespace();
        this.qName = new QName(ns, name);
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

    @Override
    public void run() throws IOException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        try (Reader reader = new FileReader(file)) {
            target.onStart();
            XMLEventReader eventReader = factory.createXMLEventReader(reader);
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            while(eventReader.hasNext()) {
                XMLEvent event = eventReader.peek();
                if (event.isStartElement() && event.asStartElement().getName()
                        .equals(qName)) {
                    @SuppressWarnings("unchecked")
                    T object = (T)unmarshaller.unmarshal(eventReader);
                    target.accept(object);
                }
                eventReader.nextEvent();
            }
            target.onComplete();
        } catch (FactoryConfigurationError | XMLStreamException
                | IOException | JAXBException | NullPointerException e) {
            target.onError(e);
            e.printStackTrace();
        }
    }
}
