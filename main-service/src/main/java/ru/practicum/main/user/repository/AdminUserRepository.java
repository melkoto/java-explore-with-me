package ru.practicum.main.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.main.user.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminUserRepository extends JpaRepository<User, Long> {
    Page<User> findByIdIn(List<Long> ids, Pageable pageable);

    Optional<User> findByEmail(String email);
}
