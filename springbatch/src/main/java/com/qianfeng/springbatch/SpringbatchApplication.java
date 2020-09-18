package com.qianfeng.springbatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SpringbatchApplication implements ExitCodeGenerator{


//    public static void main(String[] args) {
//
//        ApplicationContext context = SpringApplication.run(SpringbatchApplication.class, args);
//
//        int exitCode = SpringApplication.exit(context);
//
////        Collection<ExitCodeGenerator> beans = context.getBeansOfType(ExitCodeGenerator.class).values();
////
////        for (Iterator i = beans.iterator(); i.hasNext();) {
////        	JobExecutionExitCodeGenerator jobExecutionExitCodeGenerator = (JobExecutionExitCodeGenerator)i.next();
////        }
//
//
//
//        System.out.println("$$$$$exitCode$$$$$ = " + exitCode);
////        System.out.println("1111111111111111111111111=" + context.getBean("step3"));
//        System.exit((exitCode == 0) ? 0 : 1);
//    }

    public static void main(String[] args) {

    	ApplicationContext context = SpringApplication.run(SpringbatchApplication.class, args);

    	int exitCode = SpringApplication.exit(context);
    	System.out.println("$$$$$exitCode$$$$$ = " + exitCode);
    	System.exit(exitCode);
    }

	@Override
	public int getExitCode() {
		return 11;
	}

}
