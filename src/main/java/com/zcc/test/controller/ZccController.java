package com.zcc.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/zcc")
public class ZccController {

	@RequestMapping("/get")
	@ResponseBody
	public String get(){
		return "zcc hello";
	}
}
