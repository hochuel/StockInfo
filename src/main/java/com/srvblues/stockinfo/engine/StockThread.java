package com.srvblues.stockinfo.engine;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.BlockingQueue;

@Slf4j
public class StockThread implements Runnable{

    private CrawlerMain crawlerMain;
    private BlockingQueue blockingQueue;

    private List resultListData;

    private String threadName;

    public StockThread(String threadName, CrawlerMain crawlerMain, BlockingQueue blockingQueue, List resultListData){
        this.threadName = threadName;
        this.crawlerMain = crawlerMain;
        this.blockingQueue = blockingQueue;
        this.resultListData = resultListData;
    }

    @Override
    public void run() {
        try {
            while (!blockingQueue.isEmpty()) {
                crawlerMain.getStockInfo(this.threadName, blockingQueue, resultListData);


                //if(blockingQueue.isEmpty()) {
                 //   this.execute = false;
                //    log.info("종료...............................");
               // }

                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
