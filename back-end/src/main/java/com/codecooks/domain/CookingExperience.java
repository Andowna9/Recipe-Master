package com.codecooks.domain;

public enum CookingExperience {
	
	BEGINNER("Beginner"),
	AMATEUR("Amateur"),
	INTERMEDIATE("Intermediate"),
	EXPERT("Expert"),
	MASTER("Master");

	private final String name;
	CookingExperience(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
