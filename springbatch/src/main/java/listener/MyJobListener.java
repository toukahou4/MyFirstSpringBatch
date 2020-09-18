package listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class MyJobListener implements JobExecutionListener{

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println(jobExecution.getJobInstance().getJobName() + " before...");

    }

    @SuppressWarnings("unlikely-arg-type")
	@Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println(jobExecution.getJobInstance().getJobName() + " after... jobExecution.getStatus()======================" + jobExecution.getStatus());

//       StepExecution stepExecution = (StepExecution) jobExecution.getStepExecutions();
//       ExitStatus exitStatus = stepExecution.getExitStatus();
//       if ("COMPLETED WITH SKIPS".equals(exitStatus)){
//		jobExecution.setStatus(null);
//       }

        System.out.println("jobExecution.getStatus() ================" + jobExecution.getStatus());
    }

}
