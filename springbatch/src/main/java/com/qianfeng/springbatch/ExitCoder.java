package com.qianfeng.springbatch;

public class ExitCoder implements ExitCodeGenerator {
    @Override
    public int getExitCode() {
        System.out.println("get exit code from class: ExitCoder");
        return 222;
    }
}