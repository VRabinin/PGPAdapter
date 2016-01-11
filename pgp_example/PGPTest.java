package testpgp;

import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.ByteArrayOutputStream;

import com.acta.adapter.pgp.crypto.AbstractTrace;

public class PGPTest {
	public static void main(String[] args) {
		FileInputStream fin = null;
		ByteArrayOutputStream out = null;;
	    AbstractTrace trace = null;
	    
		System.out.println("Start!!!");
		PGPCrypto crypto = new PGPCrypto();	
		try{
		fin = new FileInputStream("./resources/test.csv.pgp"); 
		}
		catch (FileNotFoundException e){
			e.printStackTrace();
		}
		out = new ByteArrayOutputStream();
		try{
		crypto.decrypt(fin, out, "Qwerty123", trace);
		}
		 catch (Exception e) {
			 e.printStackTrace();
		 }
		System.out.println(out.toString());

	}

}
