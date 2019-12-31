package com.leavesystem;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.leavesystem.service.GoogleCal;

@SpringBootApplication
public class LeaveSystemApplication {

	public static void main(String[] args) throws IOException, GeneralSecurityException {
		SpringApplication.run(LeaveSystemApplication.class, args);
//		GoogleCal.test();
//		GoogleCal googleCal = new GoogleCal();
//		googleCal.getTokens();
	}

}
