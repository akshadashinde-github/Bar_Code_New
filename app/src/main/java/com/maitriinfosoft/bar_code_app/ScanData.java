package com.maitriinfosoft.bar_code_app;

public class ScanData {
    private String itemCode;
    private String description;
    private String quality;
    private String scan_qlt;
    private String batchNo;
    private String batchQty;
    private String draftNumber;

    public String getBatchQty() {
        return batchQty;
    }

    public void setBatchQty(String batchQty) {
        this.batchQty = batchQty;
    }

    public ScanData() {
    }

    public ScanData(String itemCode, String description, String quality, String batchNo , String scan_qty ) {
        this.itemCode = itemCode;
        this.description = description;
        this.quality = quality;
        this.batchNo = batchNo;
        this.scan_qlt = scan_qty;
    }

    public ScanData(String itemCode, String description, String quality, String batchNo , String batch_qlt ,String scan_qlt ) {
        this.itemCode = itemCode;
        this.description = description;
        this.quality = quality;
        this.batchNo = batchNo;
        this.batchQty = batch_qlt;
        this.scan_qlt = scan_qlt;
    }


    public ScanData(String itemCode, String description, String quality, String batchNo  ) {
        this.itemCode = itemCode;
        this.description = description;
        this.quality = quality;
        this.batchNo = batchNo;
       // this.scan_qlt = scan_qlt;
    }

    public String getDraftNumber() {
        return draftNumber;
    }

    public void setDraftNumber(String draftNumber) {
        this.draftNumber = draftNumber;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getScan_qlt() {
        return scan_qlt;
    }

    public void setScan_qlt(String scan_qlt) {
        this.scan_qlt = scan_qlt;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }






}
