package com.github.kingbbode.chainedtxmanager.data.b;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BDataRepository extends JpaRepository<BData, Long> {
	Optional<BData> findByText(String text);
}
