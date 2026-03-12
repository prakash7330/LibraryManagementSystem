package com.library.library;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    // Show all books
    @GetMapping("/books")
    public String getAllBooks(@RequestParam(required = false) String keyword, Model model) {
        List<Book> books;
        if (keyword != null && !keyword.isEmpty()) {
            books = bookRepository.findByTitleContainingOrAuthorContaining(keyword, keyword);
        } else {
            books = bookRepository.findAll();
        }
        model.addAttribute("books", books);
        model.addAttribute("keyword", keyword);
        return "books";
    }

    // Add new book
    @PostMapping("/books/add")
    public String addBook(@ModelAttribute Book book) {
        book.setAvailable(book.getQuantity());
        bookRepository.save(book);
        return "redirect:/books";
    }

    // Show edit form
    @GetMapping("/books/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Book book = bookRepository.findById(id).orElse(null);
        model.addAttribute("book", book);
        model.addAttribute("books", bookRepository.findAll());
        return "books";
    }

    // Update book
    @PostMapping("/books/update")
    public String updateBook(@ModelAttribute Book book) {
        Book existing = bookRepository.findById(book.getId()).orElse(null);
        if (existing != null) {
            existing.setTitle(book.getTitle());
            existing.setAuthor(book.getAuthor());
            existing.setQuantity(book.getQuantity());
            bookRepository.save(existing);
        }
        return "redirect:/books";
    }

    // Delete book
    @GetMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable int id, Model model) {
        try {
            bookRepository.deleteById(id);
        } catch (Exception e) {
            model.addAttribute("books", bookRepository.findAll());
            model.addAttribute("error", "Cannot delete! Book has borrow records.");
            return "books";
        }
        return "redirect:/books";
    }
}