package com.github.kingbbode.chainedtxmanager.data.b;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BData {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String text;

	@Builder(access = AccessLevel.PRIVATE)
	private BData(String text) {
		this.text = text;
	}

	public static BData of(String text) {
		return BData.builder()
			.text(text)
			.build();
	}
}
