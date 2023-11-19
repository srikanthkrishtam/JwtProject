package com.vhealth.dto;

public class TestDto {

	private String uname;
	private String pname;

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}
	
	public static void main(String[] args) {
		int a=8;
		if(a/2 == 0) {
			System.out.println("TestDto.main()");
		}else {
			System.out.println("its a prime number");
		}
	}

}
