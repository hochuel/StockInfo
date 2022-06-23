package com.srvblues.stockinfo.engine;

import org.apache.poi.ss.formula.functions.T;
import org.srv.vo.HMap;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class StockMain {

    public static void main(String[] args) throws Exception {

        CrawlerMain crawlerMain = new CrawlerMain();
        List<HMap> list = crawlerMain.ExcelRead();

        BlockingQueue blockingQueue = new LinkedBlockingDeque();
        for(HMap data : list){
            blockingQueue.put(data);
        }

        List resultListData = new ArrayList();


        Thread thread[] = new Thread[100];
        for(int i = 0; i < 100; i++) {
            StockThread stockThread = new StockThread("threaName_"+i, crawlerMain, blockingQueue, resultListData);
            thread[i] = new Thread(stockThread);
            thread[i].start();
        }


        Thread stockResultThread = new StockResultThread(list.size(), resultListData);
        stockResultThread.start();


    }
}
