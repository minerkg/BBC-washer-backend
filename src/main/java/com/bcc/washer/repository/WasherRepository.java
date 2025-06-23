package com.bcc.washer.repository;

import com.bcc.washer.domain.washer.Washer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface WasherRepository extends JpaRepository<Washer, Long> {

    Optional<Washer> findByName(String name);

}
