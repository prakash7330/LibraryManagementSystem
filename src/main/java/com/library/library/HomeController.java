package com.library.library;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BorrowRepository borrowRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("totalBooks", bookRepository.count());
        model.addAttribute("totalMembers", memberRepository.count());
        model.addAttribute("totalBorrowed", borrowRepository.findByStatus("borrowed").size());
        return "index";
    }
}