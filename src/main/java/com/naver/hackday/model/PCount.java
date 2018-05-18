package com.naver.hackday.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PCount {
	
	public PCount(int id, int count) {
		super();
		this.id = id;
		this.count = count;
	}
	
	int id;
	int count;
}
