package com.acta.adapter.pgp;
import java.beans.*;
import com.acta.adapter.sdk.*;

public class PGPImportByNameBeanInfo extends SimpleBeanInfo {
	

	public PGPImportByNameBeanInfo() {
	super() ;
	}
	
	public BeanDescriptor getBeanDescriptor() {
	return null ;
	}
	public PropertyDescriptor[] getPropertyDescriptors() {
	PropertyDescriptor[] pd = null;
	try {
	PropertyDescriptor fieldDelimiter = new PropertyDescriptor
	( "fieldDelimiter",com.acta.adapter.pgp.PGPImportByName.class ) ;
	fieldDelimiter.setDisplayName("Column delimiter") ;
	fieldDelimiter.setShortDescription("Choose column delimiter") ;
	fieldDelimiter.setValue ( GuiAttributes.ATTR_CHOICES,
	";" + "|" +
	"," + "|" +
	"\\|") ;
	fieldDelimiter.setValue(GuiAttributes.ATTR_DEFAULT, ";" );
	
	/*PropertyDescriptor metadataName = new PropertyDescriptor
	("metadataName",com.acta.adapter.pgp.PGPImportByName.class ) ;
	metadataName.setDisplayName("Metadata Name") ;
	metadataName.setShortDescription("Enter the metadata name") ;*/
	pd = new PropertyDescriptor [] { fieldDelimiter } ;
	}
	catch (Exception exc) {
	new AdapterException ( exc, "Introspection error. Cannot construct the property descriptor.") ;
	}
	return pd;
	}
}
