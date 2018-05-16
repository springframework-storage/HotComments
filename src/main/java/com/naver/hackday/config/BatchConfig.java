package com.naver.hackday.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.naver.hackday.step.DecreaseReader;
import com.naver.hackday.step.DecreaseWriter;
import com.naver.hackday.step.IncreaseReader;
import com.naver.hackday.step.IncreaseWriter;


@Configuration
@EnableBatchProcessing
public class BatchConfig {
	
	@Autowired 
	public JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private DecreaseReader decreaseReader;
	
	@Autowired
	private DecreaseWriter decreaseWriter;
	
	@Autowired
	private IncreaseReader increaseReader;
	
	@Autowired
	private IncreaseWriter increaseWriter;
	
	@Bean
	public Job increaseEmpathyJob() {
		return jobBuilderFactory.get("increaseEmpathyJob")
				.flow(increaseEmpathyStep())
				.end()
				.build();
	}
	
	@Bean
	public Step increaseEmpathyStep() {
		return stepBuilderFactory.get("increaseEmpathyStep")
				.<Integer, Integer>chunk(3000)
				.reader(increaseReader)
				.writer(increaseWriter)
				.build();
	}
	
	@Bean
	public Job decreaseEmpathyJob() {
		return jobBuilderFactory.get("decreaseEmpathyJob")
				.flow(decreaseEmpathyStep())
				.end()
				.build();
	}

	@Bean
	public Step decreaseEmpathyStep() {
		return stepBuilderFactory.get("decreaseEmpathyStep")
				.<Integer, Integer>chunk(3000)
				.reader(decreaseReader)
				.writer(decreaseWriter)
				.build();
	}
}

