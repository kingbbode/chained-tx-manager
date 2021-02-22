package com.github.kingbbode.chainedtxmanager.data.a;

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
public class AData {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String text;

	@Builder(access = AccessLevel.PRIVATE)
	private AData(String text) {
		this.text = text;
	}

	public static AData of(String text) {
		return AData.builder()
			.text(text)
			.build();
	}
}
