package com.qianfeng.springbatch.exitcode;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.autoconfigure.batch.JobExecutionExitCodeGenerator;

/**
 * 終了コードを加工します.
 *
 * @author tie027140
 *
 */
public class CustomJobExecutionExitCodeGenerator extends JobExecutionExitCodeGenerator {

  private final int[] warnMapperCodes;

  /**
   * コンストラクタ.
   *
   * @param warnCodes 警告終了に該当するコード（BatchStatusのordinal）
   */
  public CustomJobExecutionExitCodeGenerator(int... warnCodes) {
    super();
    if (ArrayUtils.isEmpty(warnCodes)) {
      warnMapperCodes = new int[0];
    } else {
      warnMapperCodes = warnCodes;
    }
  }

  /**
   * 終了コードを返却する. <br>
   * 正常終了の場合は0、異常終了の場合は1、警告終了の場合は2を返却.
   *
   */
  @Override
  public int getExitCode() {
    final int exitCode = super.getExitCode();
    return (exitCode == 0) ? 0 : convertErrorCode(exitCode);
  }

  private int convertErrorCode(int code) {
    return (ArrayUtils.contains(warnMapperCodes, code)) ? 2 : 1;
  }
}
