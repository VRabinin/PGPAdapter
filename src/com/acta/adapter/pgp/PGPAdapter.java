package com.acta.adapter.pgp;

import com.acta.adapter.sdk.Adapter;
import com.acta.adapter.sdk.AdapterEnvironment;
import com.acta.adapter.sdk.AdapterException;

//

public class PGPAdapter implements Adapter {
    private String[] _operationClassNames = {"com.acta.adapter.pgp.PGPService"};
	public String globalParam1;  
	public String globalParam2; 	
	@Override

	
	public void initialize(AdapterEnvironment environment) {
		// TODO Auto-generated method stub
        environment.setOperationDescriptor(_operationClassNames) ;
        environment.setMetadataImportClassName("com.acta.adapter.pgp.Import");
        environment.setMetadataBrowsingClassName
        ("com.acta.adapter.pgp.Browse");
        environment.setMetadataImportClassName
        ("com.acta.adapter.pgp.Import");
        environment.setSessionClassName
        ("com.acta.adapter.pgp.Session");
	}

	@Override
	public void start() throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() throws AdapterException {
		// TODO Auto-generated method stub

	}

    public String getGlobalParam1() {
        return globalParam1;
    }

    public void setGlobalParam1(String param) {
        this.globalParam1 = param;
    }
    
    public String getGlobalParam2() {
        return globalParam2;
    }

    public void setGlobalParam2(String param) {
        this.globalParam2 = param;
    }	
	
}
