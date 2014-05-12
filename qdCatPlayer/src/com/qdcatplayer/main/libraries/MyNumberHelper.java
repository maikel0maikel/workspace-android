package com.qdcatplayer.main.Libraries;

import android.text.format.Time;

public class MyNumberHelper {
	public static Long stringToLong(String input) {
		try {
			return Long.parseLong(input);
		} catch (NumberFormatException e) {
			// e.printStackTrace();
			return 0l;
		}
	}

	public static Time toTime(String milisecs) {
		Time tmp = new Time();
		// convert
		try {
			tmp = MyNumberHelper.toTime(Long.parseLong(milisecs));
			return tmp;
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
			tmp.set(0, 0, 0, 0, 0, 0);
			return tmp;
		}
	}

	public static Double round(Double number) {
		return Double.parseDouble(String.valueOf(Math.round(number)));
	}

	public static Time toTime(Long milisecs) {
		Time tmp = new Time();
		if (milisecs == null) {
			tmp.set(0, 0, 0, 0, 0, 0);
			return tmp;
		}

		// convert
		Long mili = milisecs;
		int seconds = (int) (mili / 1000) % 60;
		int minutes = (int) ((mili / (1000 * 60)) % 60);
		int hours = (int) ((mili / (1000 * 60 * 60)) % 24);
		tmp.set(seconds, minutes, hours, 0, 0, 0);
		return tmp;
	}
}
