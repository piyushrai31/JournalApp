package com.devscircle.journalApp.repository;

import com.devscircle.journalApp.entity.ConfigJournalAppEntity;
import com.devscircle.journalApp.entity.JournalEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ConfigJournalAppRepository extends MongoRepository<ConfigJournalAppEntity, ObjectId> {
}
