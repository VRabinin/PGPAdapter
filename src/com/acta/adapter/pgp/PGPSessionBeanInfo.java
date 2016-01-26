package com.acta.adapter.pgp;

import java.beans.*;

import com.acta.adapter.sdk.*;

public class PGPSessionBeanInfo extends SimpleBeanInfo {
	public PGPSessionBeanInfo() {
		super() ;
	}

	public BeanDescriptor getBeanDescriptor() {
	return null ;
	}
	
	public PropertyDescriptor[] getPropertyDescriptors() {
		PropertyDescriptor[] pdS = null;
		try {
		PropertyDescriptor importDirectory = new PropertyDescriptor	( "importDirectory",com.acta.adapter.pgp.PGPSession.class ) ;
		importDirectory.setDisplayName("Import Directory");
		importDirectory.setShortDescription("Main directory for source file storage. Can contain subdirectories defined on the Table level.");
		importDirectory.setValue(GuiAttributes.ATTR_DEFAULT, "d:\\tmp" );
			
		pdS = new PropertyDescriptor [] { importDirectory } ;
		}
		catch (Exception exc) {
		new AdapterException ( exc, "Introspection error. Cannot construct the property descriptor for Session.") ;
		}
		return pdS;		
	}
}
