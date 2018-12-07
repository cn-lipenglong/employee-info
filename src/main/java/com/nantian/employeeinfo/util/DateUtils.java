package com.nantian.employeeinfo.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	public static String dateToString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

}
