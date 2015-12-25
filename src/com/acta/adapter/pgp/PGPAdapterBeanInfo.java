package com.acta.adapter.pgp;

import java.beans.*;
import com.acta.adapter.sdk.*;

public class PGPAdapterBeanInfo extends SimpleBeanInfo {

    public PGPAdapterBeanInfo ()
    {
        super() ;
    }	
    public BeanDescriptor getBeanDescriptor()
    {
        BeanDescriptor bn = new BeanDescriptor (com.acta.adapter.pgp.PGPAdapter.class) ;
        bn.setName("PGPAdapter."  );
        bn.setShortDescription ("Run-time configuration for the PGPAdapter."  );
        return bn;
    }    
    public PropertyDescriptor[] getPropertyDescriptors() //throws AdapterException
    {
        PropertyDescriptor[] pd = null;
        try
        {
            //
            PropertyDescriptor generatePGP = new PropertyDescriptor ( "generatePGP",com.acta.adapter.pgp.PGPAdapter.class) ;
            generatePGP.setDisplayName("Generate PGP") ;
            generatePGP.setShortDescription("(Attention!!! This option triggers generation of the new PGP environment)") ;
            generatePGP.setValue(GuiAttributes.ATTR_CHOICES, "true"+"|"+"false" );
            generatePGP.setValue(GuiAttributes.ATTR_DEFAULT, "false" );    
            generatePGP.setValue(GuiAttributes.ATTR_ORDER, "1" );
    //        generatePGP.setBound(true);
     //       generatePGP.setConstrained(true);
     //       generatePGP.setExpert(true);
      //      generatePGP.setHidden(true);
      //      generatePGP.setPreferred(true);
            
            
            PropertyDescriptor userNamePGP = new PropertyDescriptor ( "userNamePGP",com.acta.adapter.pgp.PGPAdapter.class) ;
            userNamePGP.setDisplayName("User Name") ;
            userNamePGP.setShortDescription("User name for PGP keypair") ;
            userNamePGP.setValue(GuiAttributes.ATTR_ORDER, "2" );
            userNamePGP.setValue(GuiAttributes.ATTR_REQUIRED, "true" );
            
            PropertyDescriptor emailPGP = new PropertyDescriptor ( "emailPGP",com.acta.adapter.pgp.PGPAdapter.class) ;
            emailPGP.setDisplayName("User E-Mail") ;
            emailPGP.setShortDescription("User E-Mail for PGP keypair") ;
            emailPGP.setValue(GuiAttributes.ATTR_ORDER, "3" );
            emailPGP.setValue(GuiAttributes.ATTR_REQUIRED, "true" );
            
            PropertyDescriptor passphrasePGP = new PropertyDescriptor ( "passphrasePGP",com.acta.adapter.pgp.PGPAdapter.class) ;
            passphrasePGP.setDisplayName("Passphrase") ;
            passphrasePGP.setShortDescription("Passphrase for PGP secure keyring") ;
            passphrasePGP.setValue(GuiAttributes.ATTR_ORDER, "4" );
            passphrasePGP.setValue(GuiAttributes.ATTR_ECHO, "false" );
            passphrasePGP.setValue(GuiAttributes.ATTR_REQUIRED, "true" );
            
            PropertyDescriptor directoryPGP = new PropertyDescriptor ( "directoryPGP",com.acta.adapter.pgp.PGPAdapter.class) ;
            directoryPGP.setDisplayName("PGP Directory") ;
            directoryPGP.setShortDescription("Directory for storing PGP keyrings") ;
            directoryPGP.setValue(GuiAttributes.ATTR_ORDER, "5" );
            directoryPGP.setValue(GuiAttributes.ATTR_REQUIRED, "true" );
            
            PropertyDescriptor publicKeyPGP = new PropertyDescriptor ( "publicKeyPGP",com.acta.adapter.pgp.PGPAdapter.class) ;
            publicKeyPGP.setDisplayName("publicKeyPGP - Secret") ;
            publicKeyPGP.setShortDescription("Generated Public Key - to be copied into a file and transferred to remote party") ;
            publicKeyPGP.setValue(GuiAttributes.ATTR_ORDER, "6" );
            publicKeyPGP.setValue(GuiAttributes.ATTR_EDITABLE, "false" );
            
            pd = new PropertyDescriptor [] { generatePGP, userNamePGP, emailPGP, passphrasePGP, directoryPGP, publicKeyPGP } ;
        }
        catch (Exception exc)
        {
            // If introspection fails, that's just too bad.
            // Must be compatible with superclass signature (API design flaw)
            // Here we just log an error but blow up later.
            new AdapterException ( exc, "Introspection error. Cannot construct the property descriptor." ) ;
        }
        return pd;
    }
    	
}
