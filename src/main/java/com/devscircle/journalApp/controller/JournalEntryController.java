package com.devscircle.journalApp.controller;

import com.devscircle.journalApp.entity.JournalEntry;
import com.devscircle.journalApp.entity.User;
import com.devscircle.journalApp.service.JournalEntryService;
import com.devscircle.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesOfUser(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> all= user.getJournalEntries();
        if(all != null && !all.isEmpty()){
            return new ResponseEntity<>(all,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry newEntry){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            journalEntryService.saveEntry(newEntry, userName);
            return new ResponseEntity<>(newEntry,HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/id/{myId}")
    public ResponseEntity<JournalEntry> getById(@PathVariable ObjectId myId){
         Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user= userService.findByUserName(userName);
        List<JournalEntry> journalEntry = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());
        if(journalEntry!=null && !journalEntry.isEmpty()){
            new ResponseEntity<JournalEntry>(journalEntry.get(0),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<JournalEntry> updateById(@PathVariable ObjectId id, @RequestBody JournalEntry newEntry){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);

        for (JournalEntry userJournalIds : user.getJournalEntries()) {
            boolean equals = userJournalIds.getId().equals(id);
            if(equals){
                JournalEntry oldEntry = journalEntryService.getEntryById(id).orElse(null);
                oldEntry.setTitle((newEntry.getTitle()==null || newEntry.getTitle().isEmpty())? oldEntry.getTitle(): newEntry.getTitle());
                oldEntry.setContent((newEntry.getContent()==null || newEntry.getContent().isEmpty()) ? oldEntry.getContent() : newEntry.getContent());
                journalEntryService.saveEntry(oldEntry);
                return new ResponseEntity<JournalEntry>(oldEntry,HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{id}")
    public boolean deleteById(@PathVariable ObjectId id){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        journalEntryService.deleteEntryById(id,userName);
        return true;
    }
}

