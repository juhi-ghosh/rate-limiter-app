package com.blueoptima.project.utility;

/*
 * Class used to validate the input and output values for null and spaces.
 */
public class Validator {

	public static boolean isNull(Object obj) {
		if (obj == null)
			return true;
		return false;
	}

	public static String removeSpaces(String obj) {
		String[] str = null;

		if(obj.contains(" ")) {
			str = obj.split("\\s+");
			
			String result="";
			int len = str.length;
			
			for(int i=0; i<len-1; i++) {
				result = result.concat(str[i]);
				result = result.concat("%2b");
			}
			return result;
		}
			return obj;
			
	}
}
