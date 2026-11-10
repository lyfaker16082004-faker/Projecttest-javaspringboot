package com.javaweb.utils;

public class NumberUntil {
	public static boolean isNumber(String value) {
		try {
			Long number = Long.parseLong(value);
		} catch (NumberFormatException e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

}
