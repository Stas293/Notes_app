package com.springservice.app.notes.controller;

import com.springservice.app.notes.constants.Pages;
import com.springservice.app.notes.dto.NewNoteDto;
import com.springservice.app.notes.models.Note;
import com.springservice.app.notes.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notes")
public class NoteController {
    private final NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping()
    public String getAllNotes(Model model,
                              @RequestParam(value = "number", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "5") int size) {
        model.addAttribute("notes", noteService.getAllNotes(page, size));
        return Pages.ALL_NOTES;
    }

    @GetMapping("/add")
    public String addNote(@ModelAttribute("note") NewNoteDto note) {
        return Pages.ADD_NOTE;
    }

    @PostMapping("/create")
    public String createNote(@Valid @ModelAttribute("note") NewNoteDto note,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Pages.ADD_NOTE;
        }
        noteService.create(note);
        return Pages.REDIRECT_NOTES;
    }

    @GetMapping("/user")
    public String getUserNotes(Model model,
                               @RequestParam(value = "number", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "5") int size) {
        model.addAttribute("notes", noteService.getAllNotesByUser(page, size));
        return Pages.NOTES_BY_USER;
    }

    @PatchMapping("/{id}/like")
    public String like(@PathVariable("id") String id) {
        Note note = noteService.getNoteById(id);
        noteService.addLike(note);
        return Pages.REDIRECT_NOTES;
    }

    @PatchMapping("/{id}/dislike")
    public String dislike(@PathVariable("id") String id) {
        Note note = noteService.getNoteById(id);
        noteService.removeLike(note);
        return Pages.REDIRECT_NOTES;
    }

    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable("id") String id) {
        noteService.delete(id);
        return Pages.REDIRECT_NOTES_USER;
    }

    @GetMapping("/{id}/update")
    public String edit(@PathVariable("id") String id, Model model) {
        model.addAttribute("note", noteService.getNoteForUser(id));
        return Pages.NOTES_EDIT;
    }

    @PutMapping("/{id}/update")
    public String update(@PathVariable("id") String id,
                         @Valid @ModelAttribute("note") NewNoteDto note,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Pages.NOTES_EDIT;
        }
        noteService.update(id, note);
        return Pages.REDIRECT_NOTES_USER;
    }
}
