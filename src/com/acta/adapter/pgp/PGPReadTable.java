package com.acta.adapter.pgp;
import com.acta.adapter.sdk.*;
import com.acta.util.log.ActaLoggerManager;
import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public class PGPReadTable  implements TableSource, Delimited {
		protected static Logger _logger = ActaLoggerManager.getLogger(PGPReadTable.class);
		private OperationEnvironment  _adapterOperationEnvironment ;
		private AdapterEnvironment    _adapterEnvironment ;
		private PGPAdapter           _adapter ;
//		private PGPSession           _session ;
		private PGPFileNode              _fileNode ; // adapter defined metadata
		private String                _fullFileName ; // the name of the file with the data	
		private int                   _recordFormat ;
//		private String                _xml = new String () ;
		private String                _fileContent = new String() ;
		private StringTokenizer       _lines  = null ;
		private String				_characterSet;
		//---------------------------------------------------
		// Automatically configured properties (not from Designer)
		//---------------------------------------------------
		private String               _recordSeparator = "\r\n";
		private String               _fieldSeparator = ",";
		//------------------------------------------------
		// Configurable Properties
		// Batch size
		// Duplication factor
		//---------------------------------------------------
		private int batchSize = 1 ;
		private int duplication = 1 ;

		public int getBatchSize ( )
		{
			return batchSize ;
		}	  
		/**
		 *
		 */
		public void setBatchSize ( int batch )
		{
			batchSize = batch ;
		}
		/**
		 * Note, batch size is not required by the interface.
		 */
		public int getDuplication ( )
		{
			return duplication ;
		}
		/**
		 *
		 */
		public void setDuplication ( int dup )
		{
			duplication = dup ;
		}
		/**
		 *
		 */
		public void metadata ( Object metadata )
		{
//Get table metadata composed in PGPImport
		    _adapterOperationEnvironment.println ( "PGPReadTable::metadata" ) ;
			_fileNode = (PGPFileNode)metadata ;
			 _adapterOperationEnvironment.println (_fileNode.toString()); 
		}
		public String  getDateFormat ()
		{
			return "yyyy.mm.dd hh24:mi:ss" ;
		}	  
		/**
		 * Returns the record format com.acta.adapter.sdk.Table.RecordFormatXml or
		 * com.acta.adapter.sdk.Table.RecordFormatDelimited.
		 * @see FileNode
		 */
		public int getRecordFormat ()
		{
			if ( null != _fileNode )
			{
				if ( _fileNode.getTableType().equals ( PGPBrowse.METADATA_TABLE_DELIMITED ) )
					_recordFormat =  Table.RecordFormatDelimeted ;
				else
					_recordFormat = Table.RecordFormatXml;
			}
			else
				return -1 ;  // will blow up later
				return _recordFormat ;
		}	

		public String  getRowDelimiter ()
		{
			return _recordSeparator ;
		}

		public String getColumnDelimiter ()
		{
			if ( null != _fileNode )
				_fieldSeparator = _fileNode.getFieldSeparator() ;
			else
				; // will blow up later if metadata (FileNode)is not set
			return _fieldSeparator ;
		}

		public String getTextDelimiter ()
		{
			return "" ;
		}

		public String getEscapeChar ()
		{
			return "" ;
		}   
		
		
		
		  //------------------------------------------------
		  // Interface implementations
		  //------------------------------------------------

		 /**
		  *
		  */
		  public void initialize ( OperationEnvironment adapterOperationEnvironment )
		  {
		    _adapterOperationEnvironment  = adapterOperationEnvironment ;
		    _adapter                      = (PGPAdapter)_adapterOperationEnvironment.getAdapter() ;
		    _adapterEnvironment           = _adapterOperationEnvironment.getAdapterEnvironment() ;
//		    _session                      = (PGPSession)adapterOperationEnvironment.getSession() ;
		    _characterSet				  = _adapterEnvironment.getCharacterSet();
		  }	
		  
		  /**
		   *Create the root directory.
		   */
		  public void start() throws AdapterException
		  {
		    if ( null == _fileNode ) // must be defined at this point. If
		      throw new AdapterException ("The adapter's metadata is not defined.") ;
		    _adapterOperationEnvironment.println ( "PGPReadTable::start" ) ;
	//	    _fullFileName  = _adapter.getRootDirectory() + File.separator + _session.getSubDirectory() + File.separator + _fileNode.getFileName() + ".dat" ;
		  }	  

		  /**
		   *Does nothing useful.
		   */
		  public void stop()  throws AdapterException
		  {
		    _adapterOperationEnvironment.println("PGPReadTable::stop");
		  }
		  
		  /**
		   *1. Open the file and check if the file used for this adapter operation exist.
		   *2. Read file (UTF8). For this test case we will read the entire file in memory.
		   */
		  public void begin() throws AdapterException
		  {
			    _adapterOperationEnvironment.println ( "PGPReadTable::begin" ) ;
		    //Should receive back from engine metadata, saved during metadata import
		    if ( null == _fileNode ){
			     _adapterOperationEnvironment.println ( "PGPReadTable::begin. Metadata objecty is not set" ) ;
		         throw new AdapterException ( "Metadata object is not set." ) ;
		    }
		     _adapterOperationEnvironment.println ( "PGPReadTable::begin. Metadata received" ) ;
			 _adapterOperationEnvironment.println (_fileNode.toString()); 
		      File fd = new File ( _fileNode.getFileDirectory() );
		    //For this test adapter we will read the entire file with assumption that
		    // file contains data in UTF8 encoding
		    // For real adapter it will not be practical because files could be big
		    // and we at risk to get out of memory exception
		    File[] files = fd.listFiles(new FilenameFilter() {
		          @Override
		          public boolean accept(File dir, String name) {
		              return name.matches(_fileNode.getFileName());
		          }
		    });
		    if (files.length == 0)
		    	_adapterOperationEnvironment.println ( "PGPReadTable::begin. No files found" ) ;
		    else{
		    	int flen;
		    	byte[] buffer;
		    	int bytes_read;
		    	FileInputStream fi = null;
		        for ( int i = 0; i < files.length ; i++ ){
		        	flen = (int)files[i].length() ;
		    	if ( files[i].exists() )
		    	{
		    		try
		    		{
		    			// read file
		    			fi = new FileInputStream(files[i]) ;
		    			buffer = new byte[flen]; 			
		    			while ( (bytes_read = fi.read(buffer)) != -1 )
		    				_fileContent += new String( buffer, 0, bytes_read, _characterSet) ;//$JL-I18N$
		    			fi.close();
		    			//If there is no record separator after the last line - add it
		    			if (!_fileContent.endsWith(_recordSeparator))
		    				_fileContent +=_recordSeparator;
		    		}
		    		catch ( Exception e )
		    		{
		    			throw new AdapterException ( e, "Cannot read input file " + _fullFileName + ". " ) ;
		    		}	    		
		    	}
		    	else{
		    		_adapterOperationEnvironment.println ( "PGPReadTable::begin. Metadata received" ) ;
		    		throw new AdapterException ( "File " +  _fullFileName + " does not exist." ) ;
		        	}
		    	}
    			//split all records on lines
    			_lines = new StringTokenizer(_fileContent, _recordSeparator ) ;
    			_fileContent = new String() ;
		    }
		  }	  

		  /**
		   *
		   */
		  public void end() throws AdapterException
		  {
			_adapterOperationEnvironment.println ( "PGPReadTable::end" ) ;			  
		    _fileContent = new String() ;
		  }
		  
		  public String readNext() throws AdapterException, RecoverableOperationAdapterException
		  {
		    String resultRow = new String() ;
		    int recordsProcessed = 0 ;
		    try
		    {
		      if ( _recordFormat == Table.RecordFormatXml )
		        resultRow = "<AWA_BatchWrapper>" ;
		      while (recordsProcessed < batchSize )
		      {
		        String line = null ;
		        try
		        {
		          line = _lines.nextToken(); // should have at least one
		        }
		        catch ( NoSuchElementException e )
		        {
		        	_logger.warning(e.getLocalizedMessage());
		        }
		        catch (NullPointerException e1){
		        	_logger.warning(e1.getLocalizedMessage());	
		        	_logger.warning("No data to transfer");	
		        }
		        if ( null != line )
		        {
		        	for(int k = 0; k < duplication; k++)
		        	{
			          recordsProcessed++ ;
			          if ( _recordFormat == Table.RecordFormatXml )
			          {
			            // generate xml
			            StringTokenizer st = new StringTokenizer(line, _fieldSeparator ) ;
			            resultRow += _recordSeparator + "<AWA_Row>" ;
			            try
			            {
			              for ( int j = 0; j < _fileNode.getColumns().length; j++ )
			              {
			                String colName = _fileNode.getColumns()[j].getName() ;
			                String columnValue = st.nextToken();
			                resultRow += "<" + colName + ">" + columnValue + "</" + colName + ">" ;
			              }
			            }
			            catch (NoSuchElementException e )
			            {
			              throw new AdapterException ( "Number of fields in < " + line + "> does not match metadata." ) ;
			            }
			            resultRow += "</AWA_Row>" ;
			          }
			          else
			          {
			            resultRow += line + _recordSeparator ;
			          }
			        }
		        }
		        else
		          break ;
		      }
		    }
		    catch ( Exception e )
		    {
		      throw new AdapterException ( e ) ;
		    }

		      if ( _recordFormat == Table.RecordFormatXml )
		        resultRow += "</AWA_BatchWrapper>" ;
		    if ( recordsProcessed > 0 )
		      return resultRow ;
		    else
		      return "" ;
		  }	
	
}
