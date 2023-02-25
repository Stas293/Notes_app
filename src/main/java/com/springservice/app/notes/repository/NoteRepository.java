package com.springservice.app.notes.repository;

import com.springservice.app.notes.models.Note;
import com.springservice.app.notes.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends MongoRepository<Note, String> {
    Page<Note> findAllByUser(User user, PageRequest createdAt);

    void deleteAllByUser_Id(String id);
}

