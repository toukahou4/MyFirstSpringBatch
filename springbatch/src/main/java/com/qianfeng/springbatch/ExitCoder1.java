package com.qianfeng.springbatch;

public class ExitCoder1 implements ExitCodeGenerator {
    @Override
    public int getExitCode() {
        System.out.println("get exit code from class: ExitCoder1");
        return 221;
    }
}