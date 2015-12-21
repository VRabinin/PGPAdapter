package com.acta.adapter.pgp;
<<<<<<< Upstream, based on origin/master
// VBY test 3

=======
// VBY test 2
>>>>>>> 57374aa test message
import java.util.Vector;

import com.acta.adapter.sdk.Adapter;
import com.acta.adapter.sdk.AdapterEnvironment;
import com.acta.adapter.sdk.AdapterException;
import com.acta.adapter.sdk.ImportByName;
import com.acta.adapter.sdk.MetadataImport;
import com.acta.adapter.sdk.MetadataNode;
import com.acta.adapter.sdk.Session;
import com.acta.metadata.AWTableMetadata;
import com.acta.metadata.AWMetadata;
import com.acta.metadata.AWColumn;
import com.acta.metadata.AWAttribute;
import com.acta.metadata.ResourceMetadata;

public class Import implements MetadataImport {
	  AdapterEnvironment  _adapterEnvironment = null ;
	  PGPAdapter         _adapter = null ;
	@Override
	public ImportByName importByName() {
	    return new PGPImportByName () ;
	}

	@Override
	public Vector importMetadata(MetadataNode arg0) throws AdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector importMetadata(ImportByName r) throws AdapterException {
	    Vector resourceMetadata = new Vector() ;
	    PGPImportByName or = (PGPImportByName)r ;
	    resourceMetadata.addElement ( importData( or.getParam1(), or.getParam2() ) ) ;
	    return resourceMetadata ;
	}

	@Override
	public void initialize(Adapter adapter, AdapterEnvironment adapterEnvironment, Session session) {
	    _adapterEnvironment = adapterEnvironment ;
	    _adapter = (PGPAdapter)adapter ;
	}

	@Override
	public boolean isDocuments() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFunctionCalls() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isMessageFunctionCalls() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOutboundMessages() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTables() {
		// TODO Auto-generated method stub
		return true;
	}

	 private AWMetadata importData(String Param1, String Param2) throws AdapterException
	  {
		    AWTableMetadata  awTable = new AWTableMetadata () ;		    
		    AWColumn col1 = new AWColumn();
		    col1.setDatatype (AWColumn.AWT_VARCHAR);
		    col1.setName ("col1");
		    col1.setNullable(true);
		    col1.setLength(64);
		    AWColumn col2 = new AWColumn();
		    col2.setDatatype (AWColumn.AWT_INT);
		    col2.setName ("col2");
		    col2.setNullable(false);
		    col2.setLength(4);
		    col2.setDescription("Order number");
		//    col2.addElement(col);
		    AWAttribute attr1 =
		    new AWAttribute("Attribute1", "Attribute value 1") ;
		    AWAttribute attr2 =
		    new AWAttribute("Attribute2", "Attribute value 2") ;
		    awTable.setColumns ( new AWColumn [] { col1, col2} );
		    awTable.setAttributes(new AWAttribute [] { attr1, attr2} );	 
		    return awTable;
	  
	  }
	
}
