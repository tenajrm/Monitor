package com.example.utils;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.monitor.R;


public class Util {
	public static String convertDateToStringDate(long time) {
		
		Date date = new Date(time);
		Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
		
		return format.format(date);
		
	}
	
	public static int getColor(double mag) {
		int color = 0;
		
		if(mag >= 0 && mag < 0.9){
			//yellow color
			color = R.color.yellow;
		} else if(mag >= 0.9 && mag < 9){
			//green color
			color = R.color.green;
		} else if(mag >= 9 && mag <= 9.9){
			//red color
			color = R.color.red;
		}
		
		return color;
	}
}
