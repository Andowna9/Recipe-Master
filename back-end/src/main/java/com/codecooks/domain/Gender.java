package com.codecooks.domain;

public enum Gender {

	MALE("Male"),
	FEMALE("Female"),
	OTHER("Other");

	private final String gender;
	Gender(String gender) {
		this.gender = gender;
	}

	public String toString() {

		return this.gender;
	}


}
