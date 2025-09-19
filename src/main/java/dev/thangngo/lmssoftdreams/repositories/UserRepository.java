package dev.thangngo.lmssoftdreams.repository;

import dev.thangngo.lmssoftdreams.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
