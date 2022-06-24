package com.srvblues.stockinfo.engine;

import lombok.Data;

import java.util.List;
import java.util.concurrent.BlockingQueue;

@Data
public abstract class StockAbstract {

    protected BlockingQueue blockingQueue;
    protected List resultListData;
    protected String threadName;

    public abstract void getStockInfo() throws Exception;
}
