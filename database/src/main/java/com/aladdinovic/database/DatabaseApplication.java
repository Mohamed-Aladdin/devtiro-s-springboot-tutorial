package com.aladdinovic.database;

import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import lombok.extern.java.Log;

@SpringBootApplication
@Log
public class DatabaseApplication implements CommandLineRunner {

	public final DataSource dataSource;

	public DatabaseApplication(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public static void main(String[] args) {
		SpringApplication.run(DatabaseApplication.class, args);
	}

	public void run(final String... args) {
		log.info("Datasource: " + dataSource.toString());
		final JdbcTemplate resTemplate = new JdbcTemplate(dataSource);
		resTemplate.execute("select 1");
	}

}
