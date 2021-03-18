package io.mihaimorosan.contacts.contact;

import org.springframework.stereotype.Service;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.List;

@Service
public class ContactService {

    private ContactRepository contactRepository;

    public ContactService() {
        this.contactRepository = ContactRepository.getInstance();
    }

    public void generateContactsFile() throws TransformerException, ParserConfigurationException {
        contactRepository.generateXMLFile();
    }

    public List<Contact> getContacts() throws XMLStreamException, IOException {
        return contactRepository.getContacts();
    }

    public void addNewContact(Contact contact) throws XMLStreamException, IOException {
        contactRepository.addContact(contact);
    }

    public void editContact(Contact contact) throws IOException, XMLStreamException {
        contactRepository.editContact(contact);
    }

    public void deleteContact(Contact contact) throws IOException, XMLStreamException {
        contactRepository.deleteContact(contact);
    }
}
