package com.acta.adapter.pgp;

import com.acta.adapter.sdk.*;
//import com.acta.metadata.*;

public class PGPColumnNode implements MetadataNode 
	{
		  private String name = null;
		  private String datatype = null;
		  private String description = null;
		  private int length = 0;
		  private int precision = 0;
		  private int scale = 0 ;
		  private boolean nullable  = true ;
		  
		  /**
		   * Returns the column name.
		    */
		  public String getName ()
		  {
		    return name ;
		  }
		  
		  /**
		   * Sets the column name.
		   * @param s the column name
		   */
		  public void  setName ( String s)
		  {
		    name = s ;
		  }
		  
		  /**
		   * Returns the column data type. See {@link com.acta.metadata.AWColumn AWColumn} for data types.
		   */
		  public String getDatatype ()
		  {
		    return datatype ;
		  }

		  /**
		   * Sets the column data type.
		   * @param s  the column data type.
		   * See {@link com.acta.metadata.AWColumn AWColumn} for data types.
		   */
		  public void  setDatatype ( String s)
		  {
		    datatype = s ;
		  }
		  
		  /**
		   * Returns the column description.
		   */
		  public String getDescription ()
		  {
		    return description ;
		  }

		  /**
		   * Sets the column description.
		   * @param s the column description
		   */
		  public void  setDescription ( String s)
		  {
		    description = s ;
		  }
		  /**
		   * Returns the length of the com.acta.metadata.AWColumn.AWT_VARCHAR data.
		   */
		  public int getLength ()
		  {
		    return length ;
		  }
		  /**
		   *Sets the length of the com.acta.metadata.AWColumn.AWT_VARCHAR data.
		   *@param s the data length.
		   */
		  public void  setLength ( int s )
		  {
		    length = s ;
		  }

		  /**
		   * Returns the length of the data other than com.acta.metadata.AWColumn.AWT_VARCHAR.
		   */
		  public int getPrecision ()
		  {
		    return precision ;
		  }
		  /**
		   *Sets the length of the data other than com.acta.metadata.AWColumn.AWT_VARCHAR.
		   *@param s the data length
		   */
		  public void  setPrecision ( int s )
		  {
		    precision = s ;
		  }

		  /**
		   *Returns the scale for the com.acta.metadata.AWColumn.AWT_DECIMAL data type.
		   */
		  public int getScale ()
		  {
		    return scale ;
		  }
		  /**
		   *Sets the scale of the com.acta.metadata.AWColumn.AWT_DECIMAL data type.
		   *@param s the scale
		   */
		  public void  setScale ( int s)
		  {
		    scale = s ;
		  }

		  /**
		   *Returns true if column is nullable.
		   */
		  public boolean getNullable ()
		  {
		    return nullable ;
		  }
		  /**
		   *Sets nullability for the column.
		   *@param n true is nullable, false is not nullable
		   */
		  public void  setNullable ( boolean n)
		  {
		    nullable = n ;
		  }

		  /**
		   *Returns the unique column name.
		   */
		  public String getUniqueName()
		  {
		    return getName() ;
		  }
		  /**
		   * This is not the root node -- will return false.
		   */
		  public boolean isRoot()
		  {
		    return false ;
		  }
		  /**
		   *This node is not importable - will return false.
		   */
		  public boolean isImportable()
		  {
		    return false ;
		  }
		  /**
		   *This node is not expandable - will return false.
		   */
		  public boolean isExpandable()
		  {
		    return false ;
		  }
		  /**
		   *Returns the column's display name.
		   */
		  public String getDisplayName()
		  {
		    return getUniqueName() ;
		  }
		  /**
		   * For debugging. Converts the colunm to a string.
		   * @return the string
		   */
		  public String toString ()
		  {
		    String ret =  "\n:name  " +  name
		      + "\ndatatype  " + datatype
		      + "\ndescription " + description
		      + "\nlength  " +  length
		      + "\nprecision " + precision
		      + "\nscale  " + scale
		      + "\nnullable  " + nullable ;
		    return ret ;
		  }	  
}
