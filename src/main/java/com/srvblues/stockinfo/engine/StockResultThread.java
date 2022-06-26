package com.srvblues.stockinfo.engine;

import lombok.extern.slf4j.Slf4j;
import org.srv.utils.excel.Excel;

import java.io.File;
import java.io.IOException;
import java.util.List;


@Slf4j
public class StockResultThread extends Thread{

    private int cnt = 0;
    private List resultList;

    private boolean isExecute = true;
    public StockResultThread(int cnt, List resultList){
        this.cnt = cnt;
        this.resultList = resultList;
    }


    @Override
    public void run() {
        int num = -1;
        while(isExecute){


            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if(num == this.resultList.size()) {
                log.info("result 종료.....::" + resultList);

                Excel excel = Excel.make(Excel.EXCEL_TYPE_XSSF);
                String extension = excel.getExtension();

                File file = null;
                try {
                    file = excel.writeForHMap(excel, "C:\\stockInfo\\", "result.xlsx", resultList);
                    excel.getSheet(0).defaultHF("주식스크리너_정보");
                    excel.write(file);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                isExecute = false;
            }

            num = this.resultList.size();
        }


    }
}
