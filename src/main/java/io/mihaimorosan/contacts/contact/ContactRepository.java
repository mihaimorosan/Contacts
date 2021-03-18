package io.mihaimorosan.contacts.contact;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Repository;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Repository
public class ContactRepository {
    private static ContactRepository firstInstance = null;

    public static ContactRepository getInstance() {
        if (firstInstance == null)
            firstInstance = new ContactRepository();

        return firstInstance;
    }

    public void generateXMLFile() throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document doc = documentBuilder.newDocument();

        Element root = doc.createElement("contactbook");
        doc.appendChild(root);

        Element contacts = doc.createElement("contacts");
        root.appendChild(contacts);

        Element contact = doc.createElement("contact");
        contacts.appendChild(contact);

        Element id = doc.createElement("id");
        id.appendChild(doc.createTextNode("-1"));
        contact.appendChild(id);

        Element firstName = doc.createElement("firstname");
        firstName.appendChild(doc.createTextNode("TEMP"));
        contact.appendChild(firstName);

        Element lastName = doc.createElement("lastname");
        lastName.appendChild(doc.createTextNode("TEMP"));
        contact.appendChild(lastName);

        Element dob = doc.createElement("dob");
        dob.appendChild(doc.createTextNode("TEMP"));
        contact.appendChild(dob);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult((new File("src/main/resources/contacts.xml")));

        transformer.transform(source,result);
    }

    private int generateId() {
        String id = "";
        Random rand = new Random();
        for (int i = 0; i < 5; i++)
            id += "" + rand.nextInt(10);
        return Integer.parseInt(id);
    }

    private XMLStreamReader getStreamReader() throws FileNotFoundException, XMLStreamException {
        InputStream xmlResource = new FileInputStream("src/main/resources/contacts.xml");
        XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
        return xmlInputFactory.createXMLStreamReader(xmlResource);
    }

    private XMLStreamWriter getStreamWriter() throws FileNotFoundException, XMLStreamException {
        OutputStream xmlResourceOut = new FileOutputStream("src/main/resources/contacts.xml");
        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newFactory();
        return xmlOutputFactory.createXMLStreamWriter(xmlResourceOut);
    }

    public List<Contact> getContacts() throws XMLStreamException, IOException {
        ContactBook contactBook = new XmlMapper().readValue(getStreamReader(), ContactBook.class);
        return contactBook.getContacts();
    }


    public void addContact(Contact contact) throws XMLStreamException, IOException {
        contact.setId(generateId());

        ContactBook contactBook = new XmlMapper().readValue(getStreamReader(), ContactBook.class);
        contactBook.addContact(contact);

        XmlMapper mapperOut = new XmlMapper();
        mapperOut.enable(SerializationFeature.INDENT_OUTPUT);
        mapperOut.writeValue(getStreamWriter(), contactBook);
    }

    public void editContact(Contact contact) throws IOException, XMLStreamException {
        ContactBook contactBook = new XmlMapper().readValue(getStreamReader(), ContactBook.class);

        ArrayList<Contact> contacts = contactBook.getContacts();
        int index = getReferenceIndex(contacts, contact);

        contacts.set(index, contact);
        contactBook.setContacts(contacts);

        XmlMapper mapperOut = new XmlMapper();
        mapperOut.enable(SerializationFeature.INDENT_OUTPUT);
        mapperOut.writeValue(getStreamWriter(), contactBook);
    }

    public void deleteContact(Contact contact) throws IOException, XMLStreamException {
        ContactBook contactBook = new XmlMapper().readValue(getStreamReader(), ContactBook.class);

        ArrayList<Contact> contacts = contactBook.getContacts();
        int index = getReferenceIndex(contacts, contact);

        contacts.remove(index);
        contactBook.setContacts(contacts);

        XmlMapper mapperOut = new XmlMapper();
        mapperOut.enable(SerializationFeature.INDENT_OUTPUT);
        mapperOut.writeValue(getStreamWriter(), contactBook);
    }

    private int getReferenceIndex(ArrayList<Contact> contacts, Contact contact) {
        int index = -1;
        for (Contact contactRef: contacts) {
            if (contactRef.getId() == contact.getId()){
                index = contacts.indexOf(contactRef);
                break;
            }
        }
        return index;
    }
}
