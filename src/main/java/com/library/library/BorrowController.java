package com.library.library;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class BorrowController {

    @Autowired
    private BorrowRepository borrowRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    // Show borrow page
    @GetMapping("/borrow")
    public String borrowPage(Model model) {
        model.addAttribute("books", bookRepository.findAll());
        model.addAttribute("members", memberRepository.findAll());
        model.addAttribute("activeRecords", borrowRepository.findByStatus("borrowed"));
        model.addAttribute("allRecords", borrowRepository.findAll());
        return "borrow";
    }

    // Borrow a book
    @PostMapping("/borrow/add")
    public String borrowBook(@RequestParam int bookId,
                             @RequestParam int memberId,
                             Model model) {
        Book book = bookRepository.findById(bookId).orElse(null);
        Member member = memberRepository.findById(memberId).orElse(null);

        if (book == null || member == null) {
            return "redirect:/borrow";
        }

        if (book.getAvailable() <= 0) {
            return "redirect:/borrow?error=notavailable";
        }

        if (borrowRepository.existsByBookAndMemberAndStatus(book, member, "borrowed")) {
            return "redirect:/borrow?error=alreadyborrowed";
        }

        // Create borrow record
        BorrowRecord record = new BorrowRecord();
        record.setBook(book);
        record.setMember(member);
        record.setBorrowDate(LocalDate.now());
        record.setStatus("borrowed");
        borrowRepository.save(record);

        // Update availability
        book.setAvailable(book.getAvailable() - 1);
        bookRepository.save(book);

        return "redirect:/borrow";
    }

    // Return a book
    @GetMapping("/borrow/return/{id}")
    public String returnBook(@PathVariable int id) {
        BorrowRecord record = borrowRepository.findById(id).orElse(null);

        if (record != null && record.getStatus().equals("borrowed")) {
            record.setReturnDate(LocalDate.now());
            record.setStatus("returned");
            borrowRepository.save(record);

            // Update availability
            Book book = record.getBook();
            book.setAvailable(book.getAvailable() + 1);
            bookRepository.save(book);
        }

        return "redirect:/borrow";
    }
}