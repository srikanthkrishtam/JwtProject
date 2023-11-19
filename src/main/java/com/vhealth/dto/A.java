package com.vhealth.dto;

public interface A {

	default void print()
	{
		System.out.println("A.print()");
	}
	
	void dup(String s);
}
