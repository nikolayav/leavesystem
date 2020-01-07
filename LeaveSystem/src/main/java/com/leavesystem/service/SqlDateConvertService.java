package com.leavesystem.service;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leavesystem.entity.Request;
import com.leavesystem.repositories.LeaveTypeRepository;
import com.leavesystem.repositories.RequestRepository;
import com.leavesystem.repositories.UserRepository;

@Service
public class SqlDateConvertService {
	
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RequestRepository requestRepo;

	@Autowired
	private LeaveTypeRepository leaveTypeRepo;
	
	public int calculateTotalDaysOfLeaveReturn(Request request) {
		
		Date dateStart = (Date) request.getDateFrom();
		Date dateEnd = (Date) request.getDateTo();
		
		LocalDate localDateStart = dateStart.toLocalDate();
		
		LocalDate localDateTo = dateEnd.toLocalDate();
		
		long leaveDays = ChronoUnit.DAYS.between(localDateStart, localDateTo);
		long totalDays = leaveDays;
		
		for (int i = 0; i < leaveDays; i++) {
			LocalDate date = localDateStart.plusDays(i);
			switch (date.getDayOfWeek()) {
			case SATURDAY:
			case SUNDAY:
				totalDays -= 1;
				break;
			}
		}

		return (int) totalDays;
	}
	
}


