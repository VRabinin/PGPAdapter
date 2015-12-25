package com.acta.adapter.pgp;
import java.beans.*;
import com.acta.adapter.sdk.*;

public class PGPServiceBeanInfo extends SimpleBeanInfo {

    public PGPServiceBeanInfo ()
    {
        super() ;
    }	
    public BeanDescriptor getBeanDescriptor()
    {
        BeanDescriptor bn = new BeanDescriptor (com.acta.adapter.pgp.PGPAdapter.class) ;
        bn.setName("PGPService."  );
        bn.setShortDescription ("Configuration for the PGPService."  );
        return bn;
    }    
    public PropertyDescriptor[] getPropertyDescriptors() //throws AdapterException
    {
        PropertyDescriptor[] pd = null;
        try
        {
            //
            PropertyDescriptor param1 = new PropertyDescriptor ( "param1",com.acta.adapter.pgp.PGPService.class) ;
            param1.setDisplayName("GlobalParam1") ;
            param1.setShortDescription("Blaaaaaa") ;
            param1.setValue(GuiAttributes.ATTR_ORDER, "1" );
            
            PropertyDescriptor param2 = new PropertyDescriptor ( "globalParam2",com.acta.adapter.pgp.PGPService.class ) ;
            param2.setDisplayName("globalParam2 - Secret") ;
            param2.setShortDescription("Blaaaaaaaaaaaaaaaaa.") ;
            param2.setValue(GuiAttributes.ATTR_ECHO, "false" );
            param2.setValue(GuiAttributes.ATTR_ORDER, "2" );
            
            pd = new PropertyDescriptor [] { param1, param2 } ;
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
