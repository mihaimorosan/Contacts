package io.mihaimorosan.contacts.contact;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/contact")
public class ContactController {

    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public List<Contact> getContacts() {
        try {
            return contactService.getContacts();
        } catch (Exception e) {
            try {
                contactService.generateContactsFile();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    @PostMapping
    public void postNewContact(@RequestBody Contact contact) {
        try {
            contactService.addNewContact(contact);
        } catch (Exception e) {
            try {
                contactService.generateContactsFile();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    @PutMapping
    public void putContact(@RequestBody Contact contact) {
        try {
            contactService.editContact(contact);
        } catch (Exception e) {
            try {
                contactService.generateContactsFile();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    @DeleteMapping
    public void deleteContact(@RequestBody Contact contact) {
        try {
            contactService.deleteContact(contact);
        } catch (Exception e) {
            try {
                contactService.generateContactsFile();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
}
