package com.acta.adapter.pgp;

import com.acta.adapter.sdk.Adapter;
import com.acta.adapter.sdk.AdapterEnvironment;
import com.acta.adapter.sdk.AdapterException;

//




public class PGPAdapter implements Adapter {
//    private String[] _operationClassNames = {"com.acta.adapter.pgp.PGPService"};
    private String[] _operationClassNames = {"com.acta.adapter.pgp.PGPReadTable"};
	private String globalParam1;  
	private String globalParam2; 	
	@Override

	
	public void initialize(AdapterEnvironment environment) {
		// TODO Auto-generated method stub
        environment.setOperationDescriptor(_operationClassNames) ;
//        environment.setMetadataBrowsingClassName
//        ("com.acta.adapter.pgp.PGPBrowse");
        environment.setMetadataImportClassName
        ("com.acta.adapter.pgp.PGPImport");
        environment.setSessionClassName
        ("com.acta.adapter.pgp.PGPSession");
	}

	@Override
	public void start() throws AdapterException {
 /*       //Check if the root directory exists
        try
        {
            if ( ! rootDirectory.endsWith(File.separator) )
                    rootDirectory += File.separator;
            File directory = new File ( rootDirectory ) ;
            if ( !directory.isDirectory() )
                throw new AdapterException ( "Cannot start WAdapter. The rootDirectory configuration parameter does not refer to the existing directory." ) ;
        }
        catch ( AdapterException e )
        {
            throw e ;
        }
        catch ( Exception e )
        {
            throw new AdapterException ( e, "Cannot start WAdapter." ) ;
        } 
*/
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
