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
            PropertyDescriptor globalParam1 = new PropertyDescriptor ( "globalParam1",com.acta.adapter.pgp.PGPAdapter.class, null, "setGlobalParam1" ) ;
            globalParam1.setDisplayName("globalParam1") ;
            globalParam1.setShortDescription("Blaaaaaa") ;
            globalParam1.setValue(GuiAttributes.ATTR_ORDER, "1" );
            
            PropertyDescriptor globalParam2 = new PropertyDescriptor ( "globalParam2",com.acta.adapter.pgp.PGPAdapter.class, null, "setGlobalParam2" ) ;
            globalParam2.setDisplayName("globalParam2 - Secret") ;
            globalParam2.setShortDescription("Blaaaaaaaaaaaaaaaaa.") ;
            globalParam2.setValue(GuiAttributes.ATTR_ECHO, "false" );
            globalParam2.setValue(GuiAttributes.ATTR_ORDER, "2" );
            
            pd = new PropertyDescriptor [] { globalParam1, globalParam2 } ;
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
