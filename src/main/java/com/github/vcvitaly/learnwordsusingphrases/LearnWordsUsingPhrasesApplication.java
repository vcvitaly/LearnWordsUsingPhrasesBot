package com.github.vcvitaly.learnwordsusingphrases;

import com.github.vcvitaly.learnwordsusingphrases.configuration.CloudwatchProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackageClasses = {CloudwatchProperties.class})
public class LearnWordsUsingPhrasesApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearnWordsUsingPhrasesApplication.class, args);
	}

}
