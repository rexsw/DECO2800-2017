package com.deco2800.marswars.managers;

import com.deco2800.marswars.util.*;

import java.time.*;
import java.util.Timer;
import java.util.TimerTask;

public class TimeManager extends TimerTask{
	Time time = new Time();
	
	@Override
	public void run(){
		time.addTime(1000);
		if(time.getHours() > 18 || time.getHours() <6)
			time.setNight();
		else
			time.setDay();
		//System.out.println(time);
	}
	
	//bit of a lazy way of setting day/night tbh
}