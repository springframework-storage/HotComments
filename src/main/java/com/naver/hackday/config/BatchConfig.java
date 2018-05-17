package com.naver.hackday.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

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
		return jobBuilderFactory.get("increaseEmpathyJob").flow(increaseEmpathyStep()).end().build();
	}

	@Bean
	public Step increaseEmpathyStep() {
		return stepBuilderFactory.get("increaseEmpathyStep").<Integer, Integer>chunk(3000).reader(increaseReader)
				.writer(increaseWriter).build();
	}

	@Bean
	public Job decreaseEmpathyJob() {
		return jobBuilderFactory.get("decreaseEmpathyJob").flow(decreaseEmpathyStep()).end().build();
	}

	@Bean
	public Step decreaseEmpathyStep() {
		return stepBuilderFactory.get("decreaseEmpathyStep").<Integer, Integer>chunk(3000).reader(decreaseReader)
				.writer(decreaseWriter).build();
	}

	@Bean
	public ThreadPoolTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(15);
		taskExecutor.setMaxPoolSize(20);
		taskExecutor.setQueueCapacity(30);
		return taskExecutor;
	}

	@Bean
	public JobLauncher jobLauncher(ThreadPoolTaskExecutor taskExecutor, JobRepository jobRepository) {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setTaskExecutor(taskExecutor);
		jobLauncher.setJobRepository(jobRepository);
		return jobLauncher;
	}
}
