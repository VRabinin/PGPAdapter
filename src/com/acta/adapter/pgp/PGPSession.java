package com.acta.adapter.pgp;
//dd
import com.acta.adapter.sdk.Adapter;
import com.acta.adapter.sdk.AdapterEnvironment;
import com.acta.adapter.sdk.AdapterException;
import com.acta.adapter.sdk.Session;

public class PGPSession implements Session {
	  //------------------------------------------------------
	  //adapter's configurable properties
	  //
	  private String importDirectory ;

	public void initialize(Adapter adapter, AdapterEnvironment adapterEnvironment) {
		// TODO Auto-generated method stub

	}

	@Override
	public void start() throws AdapterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() throws AdapterException {
		// TODO Auto-generated method stub

	}

	  /**
	   * Sets imort Directory.
	   */
	  public void setImportDirectory ( String dir )
	  {
	    importDirectory = dir ;
	  }
	  /**
	   * Returns the subdirectory under the adapter directory, {@link WAdapter#setRootDirectory rootDirectory}.
	   */
	  public String getImportDirectory ( )
	  {
	    return importDirectory ;
	  }	
	
}
