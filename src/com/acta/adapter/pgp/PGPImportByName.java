package com.acta.adapter.pgp;

import com.acta.adapter.sdk.ImportByName;

public class PGPImportByName implements ImportByName {
    private String tableName;
    private String tableDescription;    
    private String tableDefinition;
    private String fileDirectory;
    private String fileNameMask;    
    
    
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

    public String getFileDirectory() {
        return fileDirectory;
    }

    public void setFileDirectory(String param) {
        this.fileDirectory = param;
    } 
    
    public String getFileNameMask() {
        return fileNameMask;
    }

    public void setFileNameMask(String param) {
        this.fileNameMask = param;
    }  
}
