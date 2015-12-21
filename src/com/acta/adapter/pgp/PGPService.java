package com.acta.adapter.pgp;
//lksjflkasjf;
//TEST
//ddd

import com.acta.adapter.sdk.AdapterException;
import com.acta.adapter.sdk.Operation;
import com.acta.adapter.sdk.OperationEnvironment;
import com.acta.adapter.sdk.StreamOperation;

public class PGPService implements Operation, StreamOperation {
    private OperationEnvironment    _adapterOperationEnvironment ;
    private PGPAdapter              _adapter ;
	public String param1;  
	public String param2; 
	@Override
	public void begin() throws AdapterException {
		// TODO Auto-generated method stub
        _adapterOperationEnvironment.println ( "PGPAdapter::PGPService:begin" ) ;
	}

	@Override
	public void end() throws AdapterException {
		// TODO Auto-generated method stub
        _adapterOperationEnvironment.println ( "PGPAdapter::PGPService:end" ) ;
	}

	@Override
	public void metadata(Object object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize(OperationEnvironment adapterOperationEnvironment) {
        _adapterOperationEnvironment = adapterOperationEnvironment ;
        _adapter = (PGPAdapter)_adapterOperationEnvironment.getAdapter() ;
	}

	@Override
	public void start() throws AdapterException {
		// TODO Auto-generated method stub
        _adapterOperationEnvironment.println ( "PGPAdapter::PGPService:start" ) ;
	}

	@Override
	public void stop() throws AdapterException {
		// TODO Auto-generated method stub
        _adapterOperationEnvironment.println ( "PGPAdapter::PGPService:end" ) ;
	}

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
