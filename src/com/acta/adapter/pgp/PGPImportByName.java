package com.acta.adapter.pgp;

import com.acta.adapter.sdk.ImportByName;

public class PGPImportByName implements ImportByName {
    private String param1;
    private String param2;
    
    public String getParam1() {
        return param1;
    }

    public void setParam1(String param) {
        this.param1 = param;
    }
    
    public String getParam2() {
        return param2;
    }

    public void setParam2(String param) {
        this.param2 = param;
    }
        
}
