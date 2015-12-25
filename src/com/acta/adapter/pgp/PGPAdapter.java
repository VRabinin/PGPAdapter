package com.acta.adapter.pgp;

import com.acta.adapter.sdk.Adapter;
import com.acta.adapter.sdk.AdapterEnvironment;
import com.acta.adapter.sdk.AdapterException;

//




public class PGPAdapter implements Adapter {
//    private String[] _operationClassNames = {"com.acta.adapter.pgp.PGPService"};
    private String[] _operationClassNames = {"com.acta.adapter.pgp.PGPReadTable"};
    private String generatePGP = null;
	private String userNamePGP = null;  
	private String emailPGP = null;
	private String passphrasePGP = null;
	private String directoryPGP = null;
	private String publicKeyPGP = null;
	
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
      //  if( publicKeyPGP==null || publicKeyPGP=="")
        	setGeneratePGP("true");
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

    public String getGeneratePGP() {
        return generatePGP;
    }

    public void setGeneratePGP(String param) {
        this.generatePGP = param;
    }
	
    public String getUserNamePGP() {
        return userNamePGP;
    }

    public void setUserNamePGP(String param) {
        this.userNamePGP = param;
    }
    
    public String getEmailPGP() {
        return emailPGP;
    }

    public void setEmailPGP(String param) {
        this.emailPGP = param;
    }	
	
    public String getPassphrasePGP() {
        return passphrasePGP;
    }

    public void setPassphrasePGP(String param) {
        this.passphrasePGP = param;
    }
    
    public String getDirectoryPGP() {
        return directoryPGP;
    }

    public void setDirectoryPGP(String param) {
        this.directoryPGP = param;
    }
    public String getPublicKeyPGP() {
        return publicKeyPGP;
    }

    public void setPublicKeyPGP(String param) {
        this.publicKeyPGP = param;
    }
}
