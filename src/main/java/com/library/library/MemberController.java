package com.library.library;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;

    // Show all members
    @GetMapping("/members")
    public String getAllMembers(Model model) {
        List<Member> members = memberRepository.findAll();
        model.addAttribute("members", members);
        return "members";
    }

    // Add new member
    @PostMapping("/members/add")
    public String addMember(@ModelAttribute Member member) {
        memberRepository.save(member);
        return "redirect:/members";
    }

    // Delete member
    @GetMapping("/members/delete/{id}")
    public String deleteMember(@PathVariable int id) {
        memberRepository.deleteById(id);
        return "redirect:/members";
    }
}