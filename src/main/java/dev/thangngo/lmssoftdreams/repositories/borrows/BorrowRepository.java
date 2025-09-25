package dev.thangngo.lmssoftdreams.repositories.borrows;

import dev.thangngo.lmssoftdreams.entities.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Long> {
}
