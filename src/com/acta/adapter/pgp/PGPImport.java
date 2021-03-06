package com.acta.adapter.pgp;

import java.util.Vector;

import com.acta.adapter.sdk.Adapter;
import com.acta.adapter.sdk.AdapterEnvironment;
import com.acta.adapter.sdk.AdapterException;
import com.acta.adapter.sdk.ImportByName;
import com.acta.adapter.sdk.MetadataImport;
import com.acta.adapter.sdk.MetadataNode;
import com.acta.adapter.sdk.Session;
import com.acta.adapter.sdk.Table;
import com.acta.metadata.AWTableMetadata;
import com.acta.metadata.AWMetadata;
import com.acta.metadata.AWColumn;
import com.acta.metadata.AWAttribute;
import com.acta.metadata.ResourceMetadata;

public class PGPImport implements MetadataImport {
	  AdapterEnvironment  _adapterEnvironment = null ;
	  PGPAdapter         _adapter = null;
	  PGPSession         _session = null;
	  private String 	_metadataCharacterSet;
	  
	@Override
	public ImportByName importByName() {
	    return new PGPImportByName () ;
	}

	@Override
	public Vector<AWMetadata> importMetadata(MetadataNode arg0) throws AdapterException {
		// TODO Auto-generated method stub
	    _adapterEnvironment.println("PGPImport:importMetadata(MetaDateNode)");		
		return null;
	}

	@Override
	public Vector<AWMetadata> importMetadata(ImportByName r) throws AdapterException {
	    _adapterEnvironment.println("PGPImport:importMetadata(Vector)");		
	    Vector<AWMetadata> resourceMetadata = new Vector<AWMetadata>() ;
	    PGPImportByName or = (PGPImportByName)r ;
	    resourceMetadata.addElement ( importData( or.getFetchSize() , or.getFieldDelimiter(), or.getDecimalSeparator(), or.getRecordFormat(), or.getFileNameMask() , _session.getImportDirectory() , or.getFileSubDirectory() , or.getSkipHeaderRows() , or.getTableDefinition() , or.getTableDescription() , or.getTableName() ) ) ;
	    //resourceMetadata.addElement ( importData( or.getTableName(), or.getTableDescription(), or.getTableDefinition(), or.getFileSubDirectory(), or.getFileNameMask() ) ) ;
	    return resourceMetadata ;
	}

	@Override
	public void initialize(Adapter adapter, AdapterEnvironment adapterEnvironment, Session session) {
	    _adapterEnvironment = adapterEnvironment ;
	    _adapter = (PGPAdapter)adapter ;
	    _session = (PGPSession)session ;
	    _metadataCharacterSet = _adapterEnvironment.getMetadataCharacterSet();	    
	    _adapterEnvironment.println("PGPImport:initialize");
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
	/*
	or.getFetchSize() ,
	or.getFieldDelimiter(), 
	or.getFileNameMask() , 
	fileDirectory - from _session
	or.getFileSubDirectory() ,
	or.getSkipHeaderRows() , 
	or.getTableDefinition() ,
	or.getTableDescription() , 
	or.getTableName()
	*/
	// private AWMetadata importData(String tableName, String tableDescription, String tableDefinition, String fileDirectory, String fileNameMask) throws AdapterException
	private AWMetadata importData(String fetchSize,
			String fieldDelimiter,
			String decimalSeparator,
			String recordFormat,
			String fileNameMask,
			String fileDirectory,
			String fileSubDirectory,
			String skipHeaderRows,
			String tableDefinition,
			String tableDescription,
			String tableName) throws AdapterException  
	{
//C1|varchar|10|0|true|field1;C2|decimal|10|2|false|field2
		 _adapterEnvironment.println("PGPImport:importData");		
		 
	     PGPFileNode fn = new PGPFileNode(fileNameMask, fileDirectory , fileSubDirectory );
	     fn.setFileType ( PGPBrowse.METADATA_TABLE) ;
	     if (recordFormat == "XML")
	    	 fn.setTableType(PGPBrowse.METADATA_TABLE_XML);
	     else
		     fn.setTableType(PGPBrowse.METADATA_TABLE_DELIMITED);	    	 
	    // fn.setFieldSeparator(";");
	     fn.setFieldSeparator(fieldDelimiter);
	     fn.setDecimalSeparator(decimalSeparator);
	     fn.setRecordFormat(recordFormat);
	     fn.setFetchSize(fetchSize);
	     fn.setSkipHeaderRows(skipHeaderRows);
	     AWAttribute attr1 =  new AWAttribute("PGP: Field Delimiter", fieldDelimiter );
		 AWAttribute attr2 =  new AWAttribute("PGP: Decimal Separator", decimalSeparator);	
	     AWAttribute attr3 =  new AWAttribute("PGP: Filename Mask", fileNameMask);
	     AWAttribute attr4 =  new AWAttribute("PGP: Import Sub Directory", fileSubDirectory) ;
		 AWAttribute attr5 =  new AWAttribute("PGP: Skip Header Rows", skipHeaderRows);
		 AWAttribute attr6 =  new AWAttribute("PGP: Table Definition", tableDefinition);	 
		// AWAttribute attr6 =  new AWAttribute("PGP: Table Description", tableDescription);
		// AWAttribute attr7 =  new AWAttribute("PGP: Table Name", tableName) ;
				 
				 
         Vector<PGPColumnNode> cols = fn.readTableDescriptor(tableDefinition, _metadataCharacterSet) ;	 
		 AWTableMetadata  awTable = new AWTableMetadata();	
		 awTable.setCanBeLoader(false);
		 awTable.setCanBeReader(true);		 
		 if ( cols.size() > 0 ){
			 AWColumn[] columns = new AWColumn[cols.size()];			
			 for(int i=0;i<cols.size();i++){
			    PGPColumnNode cn = (PGPColumnNode)cols.elementAt(i);				 
				columns[i] = new AWColumn();
				columns[i].setName (cn.getName());
				columns[i].setDatatype (cn.getDatatype());
				columns[i].setLength(cn.getLength());		    
				columns[i].setPrecision(cn.getPrecision());
				columns[i].setScale(cn.getScale());
				columns[i].setNullable(cn.getNullable());			 
				columns[i].setDescription(cn.getDescription());
			 }
		    awTable.setColumns ( columns );
		 }
		    awTable.setAttributes(new AWAttribute [] { attr1, attr2, attr3, attr4, attr5 } );	 
		    awTable.setTableDescription(tableDescription);
		    awTable.setTableName(tableName);
		   // awTable.set
		    PGPColumnNode  [] colArray = new PGPColumnNode [ cols.size() ];
		    cols.toArray(colArray);
		    fn.setColumns(colArray);
		    ResourceMetadata ei = new ResourceMetadata() ;
		    ei.setMetadataObject(fn);		    
		    awTable.setResourceMetadata(ei);
		    return awTable;  
	  }
}
