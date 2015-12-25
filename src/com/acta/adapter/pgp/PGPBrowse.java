package com.acta.adapter.pgp;

//import com.acta.metadata.*;
import com.acta.adapter.sdk.*;
import java.util.*;
//import java.io.*;

public class PGPBrowse implements MetadataBrowsing, MetadataBrowsingIcons{
	/**
	 * This sample file demonstrates the {@Link com.acta.adapter.sdk.MetadataBrowsing MetadataBrowsing}
	 * interface implementation, showing the three metadata node types used with stream operations:
	 * <p>1. {@link RootNode RootNode} -- represents a subset of the session directory files. Each subset
	 * is defined by an alphabetical range so that the first letter of a file name determines the
	 * subset into which that file will be grouped.</p>
	 * <p>2. {@link FileNode FileNode} -- represents the individual table, document, or function call. </p>
	 * <p>3. {@link ColumnNode ColumnNode}-- represents table columns.</p>
	 */

	  /**
	   * The extension for the table metadata description.
	   */
	  public static final String  TABLE_EXT = new String ("tbl") ;
	  /**
	   * The extension for the document metadata description.
	   */
	  public static final String  DOCUMENT_EXT = new String ("dtd") ;
	  /**
	   * The extension for the input function call metadata description.
	   */
	  public static final String  FUNCTION_IN_EXT = new String ("in") ;
	  /**
	   * The extension for the output function call metadata description.
	   */
	  public static final String  FUNCTION_OUT_EXT = new String ("out") ;
	  /**
	   * The table metadata type name.
	   */
	  public static final String  METADATA_TABLE = new String ("table") ;
	  /**
	   * The document metadata type name.
	   */
	  public static final String  METADATA_DOCUMENT = new String ("document") ;
	  /**
	   * The function call metadata type name.
	   */
	  public static final String  METADATA_FUNCTION_CALL = new String ("function call") ;
	  /**
	   * The name used for delimited metadata table type.
	   */
	  public static final String  METADATA_TABLE_DELIMITED = new String ("delimited") ;
	  /**
	   * The name used for xml metadata table type.
	   */
	  public static final String  METADATA_TABLE_XML = new String ("xml") ;

	  private AdapterEnvironment  _adapterEnvironment  ;
//	  private PGPAdapter         _adapter  ;
//	  private PGPSession         _session ;
	  private String           _directory ;
//	  private String		   _metadataCharacterSet;
	  
	  public String [] getIcons ()
	  {
	    String [] arr = {"Icon1.bmp" , "Icon2.bmp" , "Icon3.bmp" };
	    return arr ;
	  }
	 
	  
	  /**
	   * The Adapter SDK calls this method first, passing the references
	   * to the other adapter's objects.
	   * The root directory name for the session-related metadata is built here.
	   */
	  public void initialize ( Adapter adapter, AdapterEnvironment adapterEnvironment, Session session )
	  {
	    _adapterEnvironment = adapterEnvironment ;
	    _adapterEnvironment.println("Initializing browser.");
//	    _adapter = (PGPAdapter)adapter ;
//	    _session = (PGPSession)session ;
	    //_directory = _adapter.getRootDirectory() + File.separator + _session.getSubDirectory() + File.separator  ;
//	    _directory = _adapter.getRootDirectory() + _session.getSubDirectory() + File.separator  ;
	    _adapterEnvironment.println("WAdpter Browser directory " + _directory);
//	    _metadataCharacterSet = _adapterEnvironment.getMetadataCharacterSet();
	  }


	  /**
	   * Does nothing for this simple test adapter.
	   */
	  public void start() throws AdapterException
	  {
	  }

	  /**
	   * Does nothing for this simple test adapter.
	   */
	  public void stop() throws AdapterException
	  {
	  }

	  /**
	   * Demonstrates how to build root nodes for metadata browsing.
	   * <p>Returns the vector of the root nodes. This sample shows three root metadata browsing
	   * nodes, representing the adapter's metadata in three groups, based on the first letter
	   * of the metadata file name:
	   * <p>From letter A to F.
	   * <p>From letter G to P.
	   * <p>From letter Q to Z.
	   * <p>View these groups in the Data Services Designer by choosing the "Open" command
	   * or by double-clicking the adapter's datastore icon.
	   * @return The vector of the root nodes.
	   */
	  public Vector getRootNodes () throws AdapterException
	  {
	    // get root directory
		    // get root directory
		    Vector v = new Vector () ;
/*		    WRootNode rn = new WRootNode () ;
		    rn.setFirstLetter('A');
		    rn.setLastLetter('F');
		    rn.setDirectory(_directory);
		    v.addElement ( rn ) ;
		    //
		    rn = new WRootNode () ;
		    rn.setFirstLetter('G');
		    rn.setLastLetter('P');
		    rn.setDirectory(_directory);
		    v.addElement ( rn ) ;
		    //
		    rn = new WRootNode () ;
		    rn.setFirstLetter('Q');
		    rn.setLastLetter('Z');
		    rn.setDirectory(_directory);
		    v.addElement ( rn ) ;
		    */
		    return v ;
	  }

	  /**
	   *Demonstrates implementation of the {@link com.acta.adapter.sdk.MetadataBrowsing#expandNode expandNode} method.
	   *<p>This method returns the vector of the nodes expanded from a node chosen when browsing adapter
	   *metadata in the Designer. When the node is chosen in the Designer, the Adapter SDK calls this method,
	   *using data representing this node as the parameter for the call.
	   *<p>NOTE: Not all nodes can be expanded as defined, only those nodes for which
	   *implementations of the {@link com.acta.adapter.sdk.MetadataNode#isExpandable isExpandable()} method
	   *return true.
	   *@return the vector of the nodes comprising the expansion of the parameter node.
	   */
	  
	  public Vector expandNode ( MetadataNode node ) throws AdapterException
	  {
		    Vector v = new Vector () ;
	/*	    try
		    {
		      if ( node instanceof wadapter.WRootNode )
		      {
		        // Expand the root node
		        WRootNode rn = (WRootNode)node ;
		        File f = new File ( _directory ) ;
		        // find all table files with the first letter in the range ...
		        AlphabeticalFileFilter filter = new AlphabeticalFileFilter (rn.getFirstLetter(), rn.getLastLetter(), Browse.TABLE_EXT) ;
		        String filesList[] = f.list(filter) ;
		        if ( null != filesList && filesList.length > 0 )
		        {
		          for ( int i = 0; i < filesList.length ; i++ )
		          {
		            WFileNode fn = new WFileNode (fileNameNoExtension ( filesList[i], Browse.TABLE_EXT), rn.getDirectory() );
		            fn.setFileType ( Browse.METADATA_TABLE ) ;
		            v.addElement(fn);
		            _adapterEnvironment.println("Adding node: " + fn.toString());
		          }
		        }
		        // find all document files with the first letter in the range ...
		        filter = new AlphabeticalFileFilter (rn.getFirstLetter(), rn.getLastLetter(), Browse.DOCUMENT_EXT) ;
		        filesList = f.list(filter) ;
		        if ( null != filesList && filesList.length > 0 )
		        {
		          for ( int i = 0; i < filesList.length ; i++ )
		          {
		            WFileNode fn = new WFileNode (fileNameNoExtension ( filesList[i], Browse.DOCUMENT_EXT), rn.getDirectory() );
		            fn.setFileName ( fileNameNoExtension ( filesList[i], Browse.DOCUMENT_EXT));
		            fn.setFileType ( Browse.METADATA_DOCUMENT ) ;
		            v.addElement(fn);
		            _adapterEnvironment.println("Adding node: " + fn.toString());
		          }
		        }
		        // find all function call files with the first letter in the range ...
		        filter = new AlphabeticalFileFilter (rn.getFirstLetter(), rn.getLastLetter(), Browse.FUNCTION_IN_EXT) ;
		        filesList = f.list(filter) ;
		        if ( null != filesList && filesList.length > 0 )
		        {
		          for ( int i = 0; i < filesList.length ; i++ )
		          {
		            WFileNode fn = new WFileNode (fileNameNoExtension ( filesList[i], Browse.FUNCTION_IN_EXT), rn.getDirectory() );
		            fn.setFileType ( Browse.METADATA_FUNCTION_CALL ) ;
		            v.addElement(fn);
		            _adapterEnvironment.println("VBY: Adding node: " + fn.toString());
		          }
		        }
		        if ( v.size() == 0 )
		            _adapterEnvironment.println("VBY: No metadata available.");

		      }
		      else if ( node instanceof wadapter.WFileNode )
		      {
		        // Expand the file node
		        // can expand only the file node of type table
		        WFileNode fn = (WFileNode)node ;
		        _adapterEnvironment.println("VBY: Expanding node: " + fn.toString());
		        if ( fn.getFileType().equals (Browse.METADATA_TABLE) )
		        {
		          // read tbl file and create ColumnNodes
		          Vector mnv = fn.readTableDescriptor (_metadataCharacterSet) ;
		          for ( int i = 0 ; i < mnv.size(); i++ )
		            v.addElement(mnv.elementAt(i));
		        }
		      }
		    }
		    catch ( Exception e )
		    {
		      throw new AdapterException ( "VBY: Error when browse metadata. Check directory " + _directory ) ;
		    }*/
		    return v ;
	  }
	  /**
	   * Helper method. Returns the file name without extension
	   */
	  private String fileNameNoExtension ( String name, String extension )
	  {
	    int index = name.lastIndexOf("." + extension) ;
	    if ( index == -1 )
	      return name ;
	    else
	      return name.substring(0,index) ;
	  }	  
	  
}
