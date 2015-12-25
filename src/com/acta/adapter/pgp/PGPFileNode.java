package com.acta.adapter.pgp;

import com.acta.adapter.sdk.*;
import com.acta.metadata.*;
import com.acta.util.log.ActaLoggerManager;
import java.util.*;
import java.util.logging.Logger;
//import java.io.*;

public class PGPFileNode implements MetadataNode,MetadataNodeIcon 
	{
		  protected static Logger _logger = ActaLoggerManager.getLogger(PGPFileNode.class);
		  private String fileName ; //without extension
		  private String fileDirectory ; //directory
		  private String fileType ; // table or document or function
		  private String fieldSeparator ; // field (column) separator
		  private String tableType ;  //XML or delimited

		  private PGPColumnNode [] columns ; // the array of columns

		  public int getIconID()
		  {
		    return 1;
		  }
		  
		  /**
		   * Returns the field separator.
		   */
		  public String getFieldSeparator()
		  {
		    return fieldSeparator ;
		  }
		  /**
		   * Sets the field separator
		   */
		  public void setFieldSeparator(String f)
		  {
		    fieldSeparator = f ;
		  }
		  /**
		   *Default constructor.
		   */
		  public PGPFileNode ()
		  {
		  }
		  /**
		   *Constructor.
		   *@param name the file name
		   *@param dir the file directory
		   */
		  public PGPFileNode (String name, String dir)
		  {
		    fileName = name ;
		    fileDirectory = dir ;
		  }

		  /**
		   * Returns the file name.
		   */
		  public String getFileName()
		  {
		    return fileName ;
		  }
		  /**
		   * Sets the file name.
		   */
		  public void setFileName(String file)
		  {
		    fileName = file ;
		  }	  
		  
		  /**
		   * Returns the table type {@link Browse#METADATA_TABLE_DELIMITED METADATA_TABLE_DELIMITED}
		   * or  {@link Browse#METADATA_TABLE_XML METADATA_TABLE_XML}.
		   */
		  public String getTableType()
		  {
		    return tableType ;
		  }
		  
		  /**
		   * Sets the table type {@link Browse#METADATA_TABLE_DELIMITED METADATA_TABLE_DELIMITED}
		   * or  {@link Browse#METADATA_TABLE_XML METADATA_TABLE_XML}.
		   */
		  public void setTableType(String t)
		  {
		    tableType = t ;
		  }
		  /**
		   * Returns the file directory.
		   */
		  public String getFileDirectory()
		  {
		    return fileDirectory ;
		  }
		  /**
		   * Sets the file directory.
		   */
		  public void setFileDirectory(String d)
		  {
		    fileDirectory = d ;
		  }
		  /**
		   * Returns the file type {@link Browse#METADATA_TABLE METADATA_TABLE}
		   * or  {@link Browse#METADATA_DOCUMENT METADATA_DOCUMENT}
		   * or  {@link Browse#METADATA_FUNCTION_CALL METADATA_FUNCTION_CALL}.
		   */
		  public String getFileType()
		  {
		    return fileType ;
		  }
		  /**
		   * Sets the file type {@link Browse#METADATA_TABLE METADATA_TABLE}
		   * or  {@link Browse#METADATA_DOCUMENT METADATA_DOCUMENT}
		   * or  {@link Browse#METADATA_FUNCTION_CALL METADATA_FUNCTION_CALL}.
		   */
		  public void setFileType (String t)
		  {
		    fileType = t ;
		  }
		  /**
		   * Returns the array of columns.
		   */
		  public PGPColumnNode [] getColumns ()
		  {
		    return columns ;
		  }
		  /**
		   * Sets the array of the columns.
		   */
		  public void setColumns (PGPColumnNode [] c)
		  {
		    columns = c ;
		  }
		  /**
		   * Returns the columns.
		   */
		  public PGPColumnNode getColumns (int i)
		  {
		    return columns[i] ;
		  }
		  /**
		   * Sets the column.
		   */
		  public void setColumns (int i, PGPColumnNode c)
		  {
		    columns[i] = c ;
		  }	  
		  public String getUniqueName()
		  {
		    return fileName + "(" + fileType + ")" ;
		  }
		  /**
		   * Returns false because the file node is not the root node.
		   */
		  public boolean isRoot()
		  {
		    return false ;
		  }
		  /**
		   * Returns true because this node can be imported. See {@link Import Import} interface implementation.
		   */
		  public boolean isImportable()
		  {
		    return true ;
		  }
		  /**
		   * Returns true because this node can be expanded to {@link ColumnNode ColumnNodes}.
		   */
		  public boolean isExpandable()
		  {
		    return true ;
		  }
		  /**
		   * Returns the display name.
		   */
		  public String getDisplayName()
		  {
		    return getUniqueName() ;
		  }
		  /**
		   * Returns the description.
		   */
		  public String getDescription()
		  {
		    return "Name of the data file. The metadata type is  " + fileType ;
		  }	  

		  /**
		   *Reads the metadata description file for a table to populate columns,
		   *metadata type (delimited or XML), column delimiter.
		   */
		  public Vector<PGPColumnNode> readTableDescriptor  (String tableDef, String characterSet) throws AdapterException
		  {

		    Vector<PGPColumnNode> v = new Vector<PGPColumnNode>() ;
		    String fieldSeparator = "|" ;
		    String line = null;
		    StringTokenizer fields = new StringTokenizer(tableDef, ";") ;
		    setTableType ( PGPBrowse.METADATA_TABLE_DELIMITED) ;		    
		    try
		    {
		      setFieldSeparator (fieldSeparator) ;
		      while ( null != ( line = fields.nextToken() ) )
		      {		    	  
		        v.addElement(readColumns(line) ) ;
		      }
		    }
		    catch ( AdapterException e )
		    {
		      throw e ;
		    }
		    catch ( Exception e )
		    {
		      new AdapterException ( e, "Cannot parse the table description" ) ;
		    }
		    System.out.println("FileNode " + toString() );
		    return v ;
		  }


		  private PGPColumnNode readColumns( String line ) throws AdapterException
		  {
		    //Format
		    // column name, datatype(length or precision, scale), nullable(true or false), ,description
		    String columnName = "";
		    String description = "No description available" ;
		    String datatype = AWColumn.AWT_VARCHAR ;
		    boolean nullable = true ;
		    int lengthOrPrecision = 0;
		    int scale = 0 ;

		    PGPColumnNode cn = new PGPColumnNode() ;
		    StringTokenizer st = new StringTokenizer(line, "|") ;
		    try
		    {
		      columnName = st.nextToken() ;
		      //datatype		      	
			  datatype = st.nextToken();			  
		      String ln = st.nextToken() ;
		      lengthOrPrecision = Integer.parseInt(ln) ;
		      String sc = st.nextToken() ;
		      scale = Integer.parseInt(sc) ;		      
		      
		      String nl = st.nextToken() ;
		      nullable = Boolean.getBoolean(nl);
		      description = st.nextToken() ;
		    }
		    catch ( NoSuchElementException e )
		    {
		    
		      _logger.warning(e.getLocalizedMessage());
		    }
		    catch ( Exception e )
		    {
		      throw new AdapterException ( e, "Error when read table description form line: " + line ) ;
		    }

		    if ( columnName.length() < 1 )
		      throw new AdapterException ( "Must define column name." ) ;

		    cn.setName(columnName);
		    cn.setDatatype(datatype);
		    cn.setDescription(description);
		    cn.setNullable(nullable);
		    cn.setLength(lengthOrPrecision);
		    cn.setPrecision(lengthOrPrecision);
		    cn.setScale(scale);
		    return cn ;
		  }

		  /**
		   * For debugging. Converts the FileNode to a string.
		   * @return the string
		   */
		  public String toString ()
		  {
		    String ret = "FileNode: fileName: " + fileName
		    + "\n:fileDirectory  " + fileDirectory
		    + "\n:fileType  " + fileType
		    + "\n:fieldSeparator  " + fieldSeparator
		    + "\n:tableType  " +  tableType
		    + "\n:Columns:\n " ;
		    if ( null != columns )
		    {
		      for ( int i = 0; i < columns.length; i++ )
		        ret += "Column [" + i + "] = " + columns[i].toString() ;
		    }
		    else
		      ret += "None" ;
		    return ret ;
		  }
}
