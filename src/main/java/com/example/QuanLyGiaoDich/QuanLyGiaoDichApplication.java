package com.example.QuanLyGiaoDich;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


// Generated by https://start.springboot.io
// 优质的 spring/boot/data/security/cloud 框架中文文档尽在 => https://springdoc.cn
//@SpringBootApplication(scanBasePackages = {"com.example.QuanLyGiaoDich"})
@EnableJpaRepositories("com.example.QuanLyGiaoDich.reposotories")
@EntityScan("com.example.QuanLyGiaoDich.TableSpace")
@SpringBootApplication

public class QuanLyGiaoDichApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuanLyGiaoDichApplication	.class, args);
	}

}

