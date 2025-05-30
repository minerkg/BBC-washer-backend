package com.bcc.washer.repository;

import com.bcc.washer.domain.Washer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WasherRepository extends JpaRepository<Washer, Long> {
}
