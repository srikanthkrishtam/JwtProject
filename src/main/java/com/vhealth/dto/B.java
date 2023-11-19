package com.vhealth.dto;

public interface B {
	 
	default void print()
	{
		System.out.println("B.print()");
	}
	
	 public void dup(String x);

	 public static void get() {
		 System.out.println("B.get()");
	 }
}
