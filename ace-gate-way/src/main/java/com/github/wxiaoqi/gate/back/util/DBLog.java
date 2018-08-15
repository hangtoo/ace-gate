package com.github.wxiaoqi.gate.back.util;

import com.github.wxiaoqi.gate.agent.vo.log.LogInfo;
import com.github.wxiaoqi.gate.back.service.LogService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 往队列中写入数据库日志
 *
 * @author wanghaobin
 * @create 2017-07-01 15:28
 */
@Slf4j
public class DBLog extends Thread {
    private static DBLog dblog = null;
    // 阻塞队列 capacity:初始容量
    private static BlockingQueue<LogInfo> logInfoQueue = new LinkedBlockingQueue<LogInfo>(1024);

    public LogService getLogService() {
        return logService;
    }

    public DBLog setLogService(LogService logService) {
        this.logService = logService;
        return this;
    }

    private LogService logService;

    /**
     * 单例
     * @return
     */
    public static synchronized DBLog getInstance() {
        if (dblog == null) {
            dblog = new DBLog();
        }
        return dblog;
    }

    private DBLog() {
        super("CLogOracleWriterThread");
    }

    public void offerQueue(LogInfo logInfo) {
        try {
            // 将元素设置到队列中去
            logInfoQueue.offer(logInfo);
        } catch (Exception e) {
            log.error("日志写入失败", e);
        }
    }

    public void run() {
        List<LogInfo> bufferedLogList = new ArrayList<LogInfo>(); // 缓冲队列
        while (true) {
            try {
                bufferedLogList.add(logInfoQueue.take());
                logInfoQueue.drainTo(bufferedLogList);
                if (bufferedLogList != null && bufferedLogList.size() > 0) {
                    // 写入日志
                    for(LogInfo log:bufferedLogList){
                        logService.saveLog(log);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                // 防止缓冲队列填充数据出现异常时不断刷屏
                try {
                    Thread.sleep(1000);
                } catch (Exception eee) {
                }
            } finally {
                if (bufferedLogList != null && bufferedLogList.size() > 0) {
                    try {
                        bufferedLogList.clear();
                    } catch (Exception e) {
                    }
                }
            }
        }
    }
}