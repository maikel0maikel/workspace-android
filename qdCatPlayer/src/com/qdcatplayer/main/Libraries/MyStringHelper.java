package com.qdcatplayer.main.Libraries;

public class MyStringHelper {
	/**
	 * Ho tro kiem tra null=>"". Loai bo nhung ky tu dac biet khoi chuoi, tranh
	 * SQL Injection
	 * 
	 * @param input
	 * @return
	 */
	public static String filterSQLSpecial(String input,
			String nullOrBlankDefault) {
		if (input == null) {
			return nullOrBlankDefault;
		}
		String re = input;
		re = input.replace("'", " ");
		re = re.replace("\"", " ");
		re = re.replace("-", " ");
		re = re.replace(";", " ");
		re = re.replace("`", " ");
		while (re.contains("  ")) {
			re = re.replace("  ", " ");
		}
		re = re.trim();
		if (re.equals("")) {
			return nullOrBlankDefault;
		} else {
			return re;
		}
	}

	public static String filterNullOrBlank(String input,
			String nullOrBlankDefault) {
		if (input == null || input.equals("") || input.trim().equals("")) {
			return nullOrBlankDefault;
		} else {
			return input.trim();
		}
	}
}
