package com.github.kingbbode.chainedtxmanager.data.b;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BDataRequiredService implements BDataService {
	private final BDataRepository bDataRepository;

	@Transactional(propagation = Propagation.REQUIRED)
	public void save(String text) {
		bDataRepository.save(BData.of(text));
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void saveWithException(String text) {
		bDataRepository.save(BData.of(text));
		throw new IllegalStateException();
	}
}
