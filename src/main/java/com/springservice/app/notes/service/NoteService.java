package com.springservice.app.notes.service;

import com.springservice.app.notes.constants.ExceptionConstants;
import com.springservice.app.notes.dto.NewNoteDto;
import com.springservice.app.notes.dto.NoteDto;
import com.springservice.app.notes.exceptions.NotNullableException;
import com.springservice.app.notes.exceptions.UnauthorizedActionException;
import com.springservice.app.notes.models.Like;
import com.springservice.app.notes.models.Note;
import com.springservice.app.notes.models.User;
import com.springservice.app.notes.repository.NoteRepository;
import com.springservice.app.notes.security.UserDetailsImpl;
import com.springservice.app.notes.utility.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
public class NoteService {
    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Page<NoteDto> getAllNotes(int page, int size) {
        if (size <= 0) size = 5;
        if (page < 0) page = 0;
        Page<Note> notes = noteRepository.findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()));
        return Mapper.toPage(notes);
    }

    public void create(NewNoteDto newNoteDto) {
        Note note = Mapper.newNoteDtoToNote(newNoteDto);
        setUser(note);
        note.setCreatedAt(LocalDate.now());
        note.setLikes(new ArrayList<>());
        noteRepository.save(note);
    }

    private void setUser(Note note) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl user) {
            user.user().getNotes().add(note);
            note.setUser(user.user());
        } else {
            note.setUser(null);
        }
    }

    public void delete(String id) {
        User user = getUser();
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NotNullableException(ExceptionConstants.NOTE_NOT_FOUND));
        if (note != null && note.getUser().getUsername().equals(user.getUsername())) {
            noteRepository.deleteById(id);
        }
    }

    public void addLike(Note note) {
        Like like = Like.builder()
                .user(((UserDetailsImpl) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal())
                        .user())
                .build();
        note.like(like);
        noteRepository.save(note);
    }

    public Page<NoteDto> getAllNotesByUser(int page, int size) {
        if (size <= 0) size = 5;
        if (page < 0) page = 0;
        User user = getUser();
        Page<Note> notes = noteRepository.findAllByUser(
                user,
                PageRequest.of(
                        page,
                        size,
                        Sort.by("createdAt")
                                .descending()));
        return Mapper.toPage(notes);
    }

    private static User getUser() {
        UserDetailsImpl auth = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return auth.user();
    }

    public Note getNoteById(String id) {
        return noteRepository.findById(id).orElseThrow(() -> new NotNullableException(ExceptionConstants.NOTE_NOT_FOUND));
    }

    public void removeLike(Note note) {
        User currentUser = getUser();
        note.getLikes().removeIf(like ->
                like.getUser()
                        .getUsername()
                        .equals(currentUser.getUsername()));
        noteRepository.save(note);
    }

    public void update(String id, NewNoteDto note) {
        Note noteFromDb = noteRepository.findById(id)
                .orElseThrow(() -> new NotNullableException(ExceptionConstants.NOTE_NOT_FOUND));
        User currentUser = getUser();
        if (!noteFromDb.getUser().getUsername().equals(currentUser.getUsername())) {
            throw new UnauthorizedActionException(ExceptionConstants.NOT_AUTHORIZED);
        }
        noteFromDb.setData(note.getData());
        noteRepository.save(noteFromDb);
    }

    public Note getNoteForUser(String id) {
        User currentUser = getUser();
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NotNullableException(ExceptionConstants.NOTE_NOT_FOUND));
        if (!note.getUser().getUsername().equals(currentUser.getUsername())) {
            throw new UnauthorizedActionException(ExceptionConstants.NOT_AUTHORIZED);
        }
        return note;
    }

    public void deleteNoteById(String id) {
        noteRepository.deleteById(id);
    }
}
