package com.srvblues.stockinfo.engine;

import lombok.Data;

@Data
public class StockVo {
    private String code;
    private String name;
    private String 시장구분;
}
