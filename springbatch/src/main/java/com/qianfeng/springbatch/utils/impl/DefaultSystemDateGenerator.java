package com.qianfeng.springbatch.utils.impl;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import com.qianfeng.springbatch.utils.SystemDateGenerator;

public class DefaultSystemDateGenerator implements SystemDateGenerator{

	@Override
	public Date getCurrentDateTime() {
		return new Date();
	}

	@Override
	public Date getCurrentDate() {
		return DateUtils.truncate(new Date(),Calendar.DATE);
	}
}
