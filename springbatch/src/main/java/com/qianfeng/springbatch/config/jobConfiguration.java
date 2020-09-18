package com.qianfeng.springbatch.config;

import java.util.Date;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.qianfeng.springbatch.HanDan;
import com.qianfeng.springbatch.UsurInfo;
import com.qianfeng.springbatch.exitcode.CustomJobExecutionExitCodeGenerator;
import com.qianfeng.springbatch.utils.impl.DefaultSystemDateGenerator;

import listener.MyJobListener;
import listener.MyStepListener;

@Configuration
@EnableBatchProcessing
public class jobConfiguration {

    // タスク対象の対象を作る
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

//    @Autowired
//    private SystemDateGenerator systemDateGenerator;

    // タスクの実行はステップで決定
    // stepを注入する
    @Autowired
    private StepBuilderFactory stepBuilderFactory;



    HanDan hanDan = new HanDan();

    @Bean
    public ExitCodeGenerator exitCodeGenerator() {
      // 終了コードを加工します.
      // 警告終了の想定がないため、引数なし
      return new CustomJobExecutionExitCodeGenerator(1,2,3,4,5);
    }

    // タスクをオブジェクトを作成
    @Bean
    public Job helloWorldJob()
    {

        final JobExecutionDecider decider = (jobExecution, stepExecution) -> {
            // 実行ジョブ中のステップにおいて何らかの失敗がある場合は、失敗扱い
            final boolean isAllCompleted = jobExecution.getStepExecutions().stream()
                .allMatch(s -> ExitStatus.COMPLETED.equals(s.getExitStatus()));

            return (isAllCompleted)
                ? new FlowExecutionStatus(ExitStatus.COMPLETED.getExitCode())
                : new FlowExecutionStatus(ExitStatus.FAILED.getExitCode());
          };


        return jobBuilderFactory.get("helloWorldJob" + new Date().getTime())
                .incrementer(new RunIdIncrementer())
                .listener(new MyJobListener())
//                .start(step1())
//                .next(step2())
//                .next(step3())
//                .build();
                .start(step3())
                .build();

//重要START
//                .start(step1())
//                .on("*").to(step2())
//                .from(step1()).on("FAILED").to(step3())
//                .from(step2()).on("*").to(step3())
//                .from(step2()).on("FAILED").to(step3())
//                .next(decider).on(ExitStatus.FAILED.getExitCode()).fail()
//                .end()
//                .build();
//重要END

    }


    @Bean
    public Step step1() {

    	DefaultSystemDateGenerator systemDateGenerator = new DefaultSystemDateGenerator();

        return stepBuilderFactory.get("step1")
                .listener(new MyStepListener())
                .tasklet(new Tasklet() {

                	ItemStreamReader<UsurInfo> itemReader;
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

                    	System.out.println("systemDateGenerator1= " + systemDateGenerator.getCurrentDateTime());
                    	Thread.sleep(2000);
                    	System.out.println("systemDateGenerator2= " + systemDateGenerator.getCurrentDateTime());
                    	Thread.sleep(2000);
                    	System.out.println("systemDateGenerator3= " + systemDateGenerator.getCurrentDateTime());

                    	try {
                    		itemReader.open(chunkContext.getStepContext().getStepExecution().getExecutionContext());

                    	} finally {
                    		itemReader.close();
                    	}

                        System.out.println("step1");
//                        try {
//                            int a = 2;
//                            int b;
//                            b = a/0;
//                        } catch (Exception e) {
//                            hanDan.setHanDanFlg("1");
//                            throw e;
//                        }
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step step2() {

        return stepBuilderFactory.get("step2")
                .listener(new MyStepListener())
                .tasklet(new Tasklet() {

                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("step2");
                        try {
                            int a = 2;
                            int b;
                            b = a/0;
                        } catch (Exception e) {
                            hanDan.setHanDanFlg("1");
                            throw e;
                        }
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step step3() {

        return stepBuilderFactory.get("step3")
                .listener(new MyStepListener())
                .tasklet(new Tasklet() {

                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("step3");

    try {
        int a = 2;
        int b;
        b = a/0;
    } catch (Exception e) {
        hanDan.setHanDanFlg("1");
        throw e;
    }

                        if ("1".equals(hanDan.getHanDanFlg())) {
                            System.out.println("#####################step3#####################");
                        }
                        chunkContext.getStepContext().getStepExecution().getExecutionContext().put("errorFlg","1");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    private Step step4() {
        return stepBuilderFactory.get("step4")
                .listener(new MyStepListener())
                .tasklet(new Tasklet() {

                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {


                        System.out.println("step4");
                        return RepeatStatus.FINISHED;
                    }
                }).build();

    }

    // Flowを作る
    @Bean
    public Flow flowDemoFlow() {

    	return new FlowBuilder<Flow>("flowDemoFlow")
                .start(step1())
                .on("*").to(step2())
                .from(step1()).on("FAILED").to(step3())
                .from(step2()).on("*").to(step3())
                .from(step2()).on("FAILED").to(step3())
                .build();
    }


}
