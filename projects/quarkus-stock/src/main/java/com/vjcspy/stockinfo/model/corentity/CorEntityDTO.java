package com.vjcspy.stockinfo.model.corentity;


import java.time.LocalDate;

public class CorEntityDTO {
    public Long id;
    public String code;
    public String exchange;
    public String name;
    public Integer refId;
    public Integer catId;
    public String industryName1;
    public String industryName2;
    public String industryName3;
    public Long totalShares;
    public LocalDate firstTradeDate;
}
