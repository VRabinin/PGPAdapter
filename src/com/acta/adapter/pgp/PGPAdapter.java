package com.acta.adapter.pgp;

import java.io.File;

import com.acta.adapter.pgp.crypto.PGPCrypto;
import com.acta.adapter.sdk.Adapter;
import com.acta.adapter.sdk.AdapterEnvironment;
import com.acta.adapter.sdk.AdapterException;

//




public class PGPAdapter implements Adapter {
//    private String[] _operationClassNames = {"com.acta.adapter.pgp.PGPService"};
    private String[] _operationClassNames = {"com.acta.adapter.pgp.PGPReadTable"};
    private AdapterEnvironment _adapterEnvironment = null;
    private String generatePGP = null;
	private String userNamePGP = null;  
	private String emailPGP = null;
	private String passphrasePGP = null;
	private String directoryPGP = null;
	private String publicKeyPGP = null;
	public PGPCrypto crypto = null;
	  
	@Override

	
	public void initialize(AdapterEnvironment environment) {
		_adapterEnvironment = environment;
        environment.setOperationDescriptor(_operationClassNames) ;
//        environment.setMetadataBrowsingClassName
//        ("com.acta.adapter.pgp.PGPBrowse");
        environment.setMetadataImportClassName
        ("com.acta.adapter.pgp.PGPImport");
        environment.setSessionClassName
        ("com.acta.adapter.pgp.PGPSession");
//Initialize PGP Environment      
        crypto = new PGPCrypto(environment);
	}

	@Override
	public void start() throws AdapterException {
        //Check if the PGP directory exists
        try
        {
            if ( ! directoryPGP.endsWith(File.separator) )
                setDirectoryPGP(directoryPGP += File.separator);          
            File directory = new File ( directoryPGP ) ;
            if ( !directory.isDirectory() )
                throw new AdapterException ( "Cannot start PGPAdapter. The DirectoryPGP configuration parameter does not refer to the existing directory." ) ;
            if ( userNamePGP == null )
                throw new AdapterException ( "PGP User name is not defined" ) ;
            if ( emailPGP == null )
                throw new AdapterException ( "PGP E-Mail address is not defined" ) ;
            if ( passphrasePGP == null )
                throw new AdapterException ( "PGP Passphrase is not defined" ) ;   
      //Configure and Validate PGP Environment      
            crypto.setUserName(userNamePGP);
            crypto.setEmail(emailPGP);
            crypto.setPasssword(passphrasePGP);
            crypto.setKeyPath(directoryPGP);
            crypto.setInstanceName(_adapterEnvironment.getInstanceName());  
            boolean keyExist = false;
            try{
            	keyExist = crypto.validatePGPkeys();
            }catch (Exception e) {
               if (generatePGP == "false"){
                   _adapterEnvironment.println("PGP Configuration is wrong (check error message below).");
                   _adapterEnvironment.println( "Correct parameters or set 'Generate PGP' to 'true' to reset configuration");
            	   throw new AdapterException ( e, "Cannot start PGPAdapter." );
                }
               else{
                   _adapterEnvironment.println("!!!! New PGP Environment will be generated");
                   try
                   {
                   crypto.createPGPkeys();
                   }catch (Exception e1) {
                       _adapterEnvironment.println("Failed to crteate PGP Environment");              	   
                       throw new AdapterException ( e1, "Cannot start PGPAdapter." );
                   }
                   _adapterEnvironment.println("PGP Environment generated successfully");
               }
    	    }
            String str_pkey = "";
            try
            {
            	publicKeyPGP = crypto.getPublicKeyRing();
            }catch (Exception e) {
                _adapterEnvironment.println("Failed to gt Public Key");
                throw new AdapterException ( e, "Cannot start PGPAdapter." );                
            }        
            _adapterEnvironment.println(publicKeyPGP);

            
        }
        catch ( AdapterException e )
        {
            throw e ;
        }
        catch ( Exception e )
        {
            throw new AdapterException ( e, "Cannot start PGPAdapter." );
        }         
	}

	@Override
	public void stop() throws AdapterException {
		// TODO Auto-generated method stub

	}

    public String getGeneratePGP() {
        return generatePGP;
    }

    public void setGeneratePGP(String param) {
        generatePGP = param;
    }
	
    public String getUserNamePGP() {
        return userNamePGP;
    }

    public void setUserNamePGP(String param) {
        userNamePGP = param;
    }
    
    public String getEmailPGP() {
        return emailPGP;
    }

    public void setEmailPGP(String param) {
        emailPGP = param;
    }	
	
    public String getPassphrasePGP() {
        return passphrasePGP;
    }

    public void setPassphrasePGP(String param) {
        passphrasePGP = param;
    }
    
    public String getDirectoryPGP() {
        return directoryPGP;
    }

    public void setDirectoryPGP(String param) {
        directoryPGP = param;
    }
    public String getPublicKeyPGP() {
        return publicKeyPGP;
    }

    public void setPublicKeyPGP(String param) {
        publicKeyPGP = param;
    }
}
