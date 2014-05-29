package com.qdcatplayer.main.Libraries;


public class MyStringHelper {
	/**
	 * Ho tro kiem tra null=>"". Loai bo nhung ky tu ' khoi chuoi, tranh
	 * SQL Injection
	 * Sử dụng riêng cho FileName, FolderName, AlbumName,...
	 * Không sử dụng ho absPath
	 * 
	 * @param input
	 * @param direction_in
	 * @return
	 */
	public static String filterSQLSpecial(String input,
			String nullOrBlankDefault) {
		if (input == null) {
			return nullOrBlankDefault;
		}
		String re = input;
		re = re.replace("'", "");
		if (re.equals("")) {
			return nullOrBlankDefault;
		} else {
			return re;
		}
	}
	/**
	 * Sử dụng khi muốn lọc thay thế ' bằng cách dùng chuỗi thay thế
	 * Sử dụng cho absPath
	 * @param input
	 * @param nullOrBlankDefault
	 * @param direction_in
	 * @return
	 */
	public static String filterSQLSpecialAbsPath(String input,
			String nullOrBlankDefault, Boolean direction_in) {
		String replace= "[_SiNglEqUOte_]";
		if (input == null) {
			return nullOrBlankDefault;
		}
		String re;
		if(direction_in)
		{
			re = input.replace("'", replace);
		}
		else
		{
			re = input.replace(replace, "'");
		}
		
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
			return input;//input.trim();
		}
	}
}
