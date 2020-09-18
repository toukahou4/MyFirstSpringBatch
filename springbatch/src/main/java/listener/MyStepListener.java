package listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class MyStepListener implements StepExecutionListener{

    @Override
    public void beforeStep(StepExecution stepExecution) {
        System.out.println(stepExecution.getStepName() + " before...");

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println(stepExecution.getStepName() + " after... stepExecution.getExitStatus().getExitCode() = " + stepExecution.getExitStatus().getExitCode());
        System.out.println("errorFlg =" + stepExecution.getExecutionContext().get("errorFlg"));

        String errorFlg =  stepExecution.getExecutionContext().get("errorFlg").toString();
        if ("1".equals(errorFlg)) {
        	return new ExitStatus("FAILED");
        }

        return stepExecution.getExitStatus();
    }

}
