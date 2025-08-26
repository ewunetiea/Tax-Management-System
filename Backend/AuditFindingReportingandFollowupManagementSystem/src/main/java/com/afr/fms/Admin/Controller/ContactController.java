package com.afr.fms.Admin.Controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afr.fms.Admin.Entity.Contact;
import com.afr.fms.Admin.Entity.Feedback;
import com.afr.fms.Admin.Service.ContactService;
import com.afr.fms.Common.Permission.Service.FunctionalitiesService;

@RestController
@RequestMapping("/api")
public class ContactController {
    @Autowired
    private ContactService contactService;
    @Autowired
    private FunctionalitiesService functionalitiesService;

    @PostMapping("/contact")
    public ResponseEntity<?> createContact(@RequestBody Contact contact, HttpServletRequest request) {
        try {
            contactService.createContact(contact);

            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/contact/delete")
    public ResponseEntity<?> deleteContact(@RequestBody List<Contact> contacts, HttpServletRequest request) {
        try {
            for (Contact contact : contacts) {
                contactService.deleteContact(contact);
            }
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/contacts")
    public ResponseEntity<List<Contact>> getContacts(HttpServletRequest request) {
        try {
            return new ResponseEntity<>(contactService.getContacts(), HttpStatus.OK);
        } catch (Exception ex) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/contact/{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable("id") long id, HttpServletRequest request) {
        Contact contact = contactService.getContactById(id);
        if (contact != null) {
            return new ResponseEntity<>(contact, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/feedback/{id}")
    public ResponseEntity<List<Feedback>> get(@PathVariable("id") long id, HttpServletRequest request) {
        try {
            return new ResponseEntity<>(contactService.getFeedbacksByUserID(id), HttpStatus.OK);
        } catch (Exception ex) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/feedback")
    public ResponseEntity<?> createFeedback(@RequestBody Feedback feedback, HttpServletRequest request) {
        try {

            contactService.createFeedback(feedback);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/feedback")
    public ResponseEntity<List<Feedback>> getFeedbacks(HttpServletRequest request) {
        try {
            return new ResponseEntity<>(contactService.getFeedbacks(), HttpStatus.OK);
        } catch (Exception ex) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/feedback/close")
    public ResponseEntity<?> closeFeedback(@RequestBody List<Feedback> feedbacks, HttpServletRequest request) {
        try {
            for (Feedback feedback : feedbacks) {
                contactService.closeFeedback(feedback);
            }
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/feedback/delete")
    public ResponseEntity<?> deleteFeedback(@RequestBody List<Feedback> feedbacks, HttpServletRequest request) {
        try {
            for (Feedback feedback : feedbacks) {
                contactService.deleteFeedback(feedback);
            }
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/feedback/respond")
    public ResponseEntity<?> respondFeedback(@RequestBody List<Feedback> feedbacks, HttpServletRequest request) {
        try {
            String response = feedbacks.get(0).getResponse();
            for (Feedback feedback : feedbacks) {
                feedback.setResponse(response);
                contactService.respondFeedback(feedback);
            }
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
