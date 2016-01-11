package com.acta.adapter.pgp.crypto;

import com.acta.adapter.sdk.AdapterEnvironment;

//import java.io.Console;

public class AbstractTrace {
	  AdapterEnvironment  _adapterEnvironment = null ;
	  public AbstractTrace (AdapterEnvironment env){
		  _adapterEnvironment = env;
	  }
	
	public void addInfo(String info){
	//	System.out.println(info);
	    _adapterEnvironment.println("PGPImport:importMetadata(MetaDateNode)");
	}

}
