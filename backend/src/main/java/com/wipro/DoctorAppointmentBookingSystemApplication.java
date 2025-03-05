package com.wipro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DoctorAppointmentBookingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoctorAppointmentBookingSystemApplication.class, args);
	}

}
