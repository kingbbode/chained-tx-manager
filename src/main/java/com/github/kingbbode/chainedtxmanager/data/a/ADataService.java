package com.github.kingbbode.chainedtxmanager.data.a;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ADataService {
	private final ADataRepository aDataRepository;

	@Transactional
	public void save(String text) {
		aDataRepository.save(AData.of(text));
	}

	@Transactional
	public void saveWithException(String text) {
		aDataRepository.save(AData.of(text));
		throw new IllegalStateException();
	}
}
