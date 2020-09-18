package com.qianfeng.springbatch;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class UsurInfo {
	private String name;
	private String sex;
	private String age;
}
