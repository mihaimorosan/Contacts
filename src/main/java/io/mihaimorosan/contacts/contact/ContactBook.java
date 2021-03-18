package io.mihaimorosan.contacts.contact;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ContactBook {
    private ArrayList<Contact> contacts;

    public ContactBook() {
        this.contacts = new ArrayList<>();
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
    }
}
