package dev.thangngo.lmssoftdreams.repository;

import dev.thangngo.lmssoftdreams.entities.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowRepository extends JpaRepository<Borrow, Long> {
}
