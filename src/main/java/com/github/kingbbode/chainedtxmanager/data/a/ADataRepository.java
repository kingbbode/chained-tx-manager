package com.github.kingbbode.chainedtxmanager.data.a;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ADataRepository extends JpaRepository<AData, Long> {
	Optional<AData> findByText(String text);
}
