package com.vhealth.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class D extends C{
	private String city;
	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "D [city=" + city + "]";
	}

	public static void main(String[] args) {
		D d=new D();
		d.setCity("HYD");
		d.setName("SRI");
		System.out.println("D.main()"+d.getName());
	    SimpleDateFormat obj = new SimpleDateFormat("MM/dd/yyyy");  

String date=obj.format(new Date());
try {
	Date date2=obj.parse(date);
	System.out.println("D.main()");
} catch (ParseException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}

	}



}
