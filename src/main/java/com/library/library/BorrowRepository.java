package com.library.library;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BorrowRepository extends JpaRepository<BorrowRecord, Integer> {
    List<BorrowRecord> findByStatus(String status);
    boolean existsByBookAndMemberAndStatus(Book book, Member member, String status);
}