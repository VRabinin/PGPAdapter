package com.acta.adapter.pgp;

import com.acta.adapter.sdk.ImportByName;

public class PGPImportByName implements ImportByName {
    private String tableName = null;
    private String tableDescription = null;    
    private String tableDefinition = null;
    private String fileSubDirectory = null;
    private String fileNameMask = null;    
    private String recordFormat = null;
    private String fieldDelimiter = null;
    private String decimalSeparator = null;
    private String fetchSize = null;
    private String skipHeaderRows = null;
    
    public String getTableDefinition() {
        return tableDefinition;
    }
    public void setTableDefinition(String param) {
        this.tableDefinition = param;
    }
    public String getTableDescription() {
        return tableDescription;
    }
    public void setTableDescription(String param) {
        this.tableDescription = param;
    }
    public String getTableName() {
        return tableName;
    }
    public void setTableName(String param) {
        this.tableName = param;
    }    
    public String getFileSubDirectory() {
        return fileSubDirectory;
    }
    public void setFileSubDirectory(String param) {
        this.fileSubDirectory = param;
    } 
    public String getFileNameMask() {
        return fileNameMask;
    }
    public void setFileNameMask(String param) {
        this.fileNameMask = param;
    }  
    public String getRecordFormat() {
        return recordFormat;
    }
    public void setRecordFormat(String param) {
        this.recordFormat = param;
    }      
    public String getFieldDelimiter() {
        return fieldDelimiter;
    }
    public void setFieldDelimiter(String param) {
        this.fieldDelimiter = param;
    }  
    public String getDecimalSeparator() {
        return decimalSeparator;
    }
    public void setDecimalSeparator(String param) {
        this.decimalSeparator = param;  
    }
    public String getFetchSize(){
    	return fetchSize;
    }   
    public void setFetchSize(String param){
    	this.fetchSize = param;
    }
    public String getSkipHeaderRows(){
    	return skipHeaderRows;
    }  
    public void setSkipHeaderRows(String param){
    	this.skipHeaderRows = param;
    }
    
    
}
