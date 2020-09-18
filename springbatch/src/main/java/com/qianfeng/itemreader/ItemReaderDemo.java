package com.qianfeng.itemreader;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.BindException;

@Configuration
public class ItemReaderDemo {

	// タスク対象の対象を作る
	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	// タスクの実行はステップで決定
	// stepを注入する
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	@Qualifier("itemWriteDemo")
	private ItemWriter<? super Customer> itemWriteDemo;
	@Bean
	public Job itemReaderDemoJob() {

		return jobBuilderFactory.get("itemReaderDemoJob")
				.start(itemReaderDemoStep())
				.build();
	}

	@Bean
	public Step itemReaderDemoStep() {
		return stepBuilderFactory.get("itemReaderDemoStep")
				.<Customer,Customer>chunk(2)
				.reader(itemReaderDemoRead())
				.writer(itemWriteDemo)
				.build();
	}

	@Bean
	@StepScope
	public FlatFileItemReader<Customer> itemReaderDemoRead() {
		FlatFileItemReader<Customer> reader = new FlatFileItemReader<Customer>();
		reader.setResource(new ClassPathResource("customer.txt"));
		reader.setLinesToSkip(1);

		// データを解析する
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[]{"id","firstName","lastName","birthday"});
		DefaultLineMapper<Customer> mapper = new DefaultLineMapper<>();
		mapper.setLineTokenizer(tokenizer);
		mapper.setFieldSetMapper(new FieldSetMapper<Customer>() {

			@Override
			public Customer mapFieldSet(FieldSet fieldSet) throws BindException {
				Customer customer = new Customer();
				customer.setId(fieldSet.readLong("id"));
				customer.setFirstName(fieldSet.readString("firstName"));
				customer.setLastName(fieldSet.readString("lastName"));
				customer.setBirthday(fieldSet.readString("birthday"));
				return customer;
			}
		});

		mapper.afterPropertiesSet();
		reader.setLineMapper(mapper);

		return reader;
	}

}
