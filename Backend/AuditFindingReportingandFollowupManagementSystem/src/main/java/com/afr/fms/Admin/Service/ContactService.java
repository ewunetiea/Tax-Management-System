package com.afr.fms.Admin.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Admin.Entity.Contact;
import com.afr.fms.Admin.Entity.Feedback;
import com.afr.fms.Admin.Mapper.ContactMaper;

@Service
public class ContactService {
    @Autowired
    private ContactMaper contactMapper;

    public void createContact(Contact contact) {

        if (contact.getId() == null) {
            contactMapper.createContact(contact);
        } else {
            contactMapper.updateContact(contact);
        }
    }

    public List<Contact> getContacts() {
        return contactMapper.getContacts();
    }

    public void deleteContact(Contact contact) {
        contactMapper.deleteContact(contact);
    }

    public void deleteFeedback(Feedback feedback) {
        contactMapper.deleteFeedback(feedback);
    }

    public void closeFeedback(Feedback feedback) {
        contactMapper.closeFeedback(feedback);
    }

    public void respondFeedback(Feedback feedback) {
        contactMapper.respondFeedback(feedback);
    }

    public Contact getContactById(Long id) {
        return contactMapper.getContactById(id);
    }

    public void createFeedback(Feedback feedback) {
        contactMapper.createFeedback(feedback);
    }

    public List<Feedback> getFeedbacks() {
        return contactMapper.getFeedbacks();
    }

    public List<Feedback> getFeedbacksByUserID(Long id) {
        return contactMapper.getFeedbacksByUserID(id);
    }

    
}