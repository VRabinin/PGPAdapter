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

		PropertyDescriptor tableName =  new PropertyDescriptor
				( "tableName" , com.acta.adapter.pgp.PGPImportByName.class);	
		tableName.setDisplayName("Table Name") ;
		tableName.setShortDescription("Set Table Name");
		tableName.setValue(GuiAttributes.ATTR_ORDER, "0");
		tableName.setValue(GuiAttributes.ATTR_DEFAULT, "TableName");		
		
		PropertyDescriptor fileSubDirectory = new PropertyDescriptor
				( "fileSubDirectory" , com.acta.adapter.pgp.PGPImportByName.class);
		fileSubDirectory.setDisplayName("File Sub Directory") ;
		fileSubDirectory.setShortDescription("Set File Sub Directory. Sub directory from whre source files will taken") ;
		fileSubDirectory.setValue(GuiAttributes.ATTR_ORDER, "1");
		fileSubDirectory.setValue(GuiAttributes.ATTR_DEFAULT, "\\sub1");
		
		PropertyDescriptor fileNameMask =  new PropertyDescriptor
				( "fileNameMask" , com.acta.adapter.pgp.PGPImportByName.class);
		fileNameMask.setDisplayName("File Name Mask") ;
		fileNameMask.setShortDescription("Set File Name Mask. Set mask for files to be taken. Mask + wildcard* + file extension ");
		fileNameMask.setValue(GuiAttributes.ATTR_ORDER, "2");
		fileNameMask.setValue(GuiAttributes.ATTR_DEFAULT, "test*.csv");		

		PropertyDescriptor recordFormat = new PropertyDescriptor	( "recordFormat",com.acta.adapter.pgp.PGPImportByName.class ) ;
		recordFormat.setDisplayName("Record format") ;
		recordFormat.setShortDescription("Choose format for transferring revords from Adapter to BODS flow.") ;
		recordFormat.setValue(GuiAttributes.ATTR_ORDER, "3" );
		recordFormat.setValue ( GuiAttributes.ATTR_CHOICES,
				"Delimited" + "|" +
						"XML" + "|" ) ;
		recordFormat.setValue(GuiAttributes.ATTR_DEFAULT, "Delimited" );
		
		PropertyDescriptor fieldDelimiter = new PropertyDescriptor	( "fieldDelimiter",com.acta.adapter.pgp.PGPImportByName.class ) ;
		fieldDelimiter.setDisplayName("Column delimiter") ;
		fieldDelimiter.setShortDescription("Choose column delimiter") ;
		fieldDelimiter.setValue(GuiAttributes.ATTR_ORDER, "4" );
		fieldDelimiter.setValue ( GuiAttributes.ATTR_CHOICES,
				"," + "|" +
						"\u003B;" + "|" ) ;
		fieldDelimiter.setValue(GuiAttributes.ATTR_DEFAULT, ";" );		

		PropertyDescriptor decimalSeparator = new PropertyDescriptor	( "decimalSeparator",com.acta.adapter.pgp.PGPImportByName.class ) ;
		decimalSeparator.setDisplayName("Decimal Separator") ;
		decimalSeparator.setShortDescription("Choose Decimal Separator") ;
		decimalSeparator.setValue(GuiAttributes.ATTR_ORDER, "5" );
		decimalSeparator.setValue ( GuiAttributes.ATTR_CHOICES,
				"." + "|" +
						"," + "|" ) ;
		decimalSeparator.setValue(GuiAttributes.ATTR_DEFAULT, "," );		
	
		PropertyDescriptor skipHeaderRows = new PropertyDescriptor	( "skipHeaderRows",com.acta.adapter.pgp.PGPImportByName.class ) ;
		skipHeaderRows.setDisplayName("Skip Header Rows") ;
		skipHeaderRows.setShortDescription("Number of header rows to be skipped.") ;
		skipHeaderRows.setValue(GuiAttributes.ATTR_ORDER, "6" );
		skipHeaderRows.setValue(GuiAttributes.ATTR_DEFAULT, "1" );
	
		PropertyDescriptor tableDescription =  new PropertyDescriptor
				( "tableDescription" , com.acta.adapter.pgp.PGPImportByName.class);	
		tableDescription.setDisplayName("Table Description") ;
		tableDescription.setShortDescription("Set Table Description");
		tableDescription.setValue(GuiAttributes.ATTR_ORDER, "7" );
		tableDescription.setValue(GuiAttributes.ATTR_DEFAULT, "Default Table Description");	
		
		PropertyDescriptor tableDefinition =  new PropertyDescriptor
				( "tableDefinition" , com.acta.adapter.pgp.PGPImportByName.class);	
		tableDefinition.setDisplayName("Table Definition") ;
		tableDefinition.setShortDescription("Set Table Definition. Example table definition: FIELD1|varchar|10|0|false|Field 1;FIELD2|varchar|10|0|true|Field 2 ");
		tableDefinition.setValue(GuiAttributes.ATTR_ORDER, "8" );
		tableDefinition.setValue(GuiAttributes.ATTR_DEFAULT, "");	
	
		PropertyDescriptor fetchSize = new PropertyDescriptor
				( "fetchSize" , com.acta.adapter.pgp.PGPImportByName.class);	
		fetchSize.setDisplayName("Fetch Size") ;
		fetchSize.setShortDescription("Set Fetch Size");
		fetchSize.setValue(GuiAttributes.ATTR_ORDER, "9" );
		fetchSize.setValue(GuiAttributes.ATTR_DEFAULT, "1000");	
			
	pd = new PropertyDescriptor [] { 
			recordFormat ,
			fieldDelimiter , 
			decimalSeparator,
			fileSubDirectory ,  
			fileNameMask , 
			tableDefinition , 
			tableDescription ,
			tableName , 
			fetchSize , 
			skipHeaderRows} ;
	}
	catch (Exception exc) {
	new AdapterException ( exc, "Introspection error. Cannot construct the property descriptor for PRPImportByName.") ;
	}
	return pd;
	}
}
