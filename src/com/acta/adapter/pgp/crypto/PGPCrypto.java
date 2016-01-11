package com.acta.adapter.pgp.crypto;

import java.io.ByteArrayInputStream;	
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchProviderException;
//import java.security.SecureRandom;
import java.security.Security;
import java.util.Date;
import java.util.Iterator;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPCompressedData;
import org.bouncycastle.openpgp.PGPCompressedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedDataList;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPLiteralDataGenerator;
import org.bouncycastle.openpgp.PGPObjectFactory;
import org.bouncycastle.openpgp.PGPOnePassSignatureList;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyEncryptedData;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.operator.bc.BcKeyFingerprintCalculator;
import org.bouncycastle.openpgp.operator.bc.BcPublicKeyDataDecryptorFactory;
import org.bouncycastle.openpgp.operator.bc.BcPGPDigestCalculatorProvider;
import org.bouncycastle.openpgp.operator.bc.BcPGPDataEncryptorBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPBESecretKeyDecryptorBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPublicKeyKeyEncryptionMethodGenerator;
import org.bouncycastle.openpgp.operator.PBESecretKeyDecryptor;
import org.bouncycastle.openpgp.PGPUtil;


import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.bcpg.sig.Features;
import org.bouncycastle.bcpg.sig.KeyFlags;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.DSAKeyPairGenerator;
import org.bouncycastle.crypto.generators.DSAParametersGenerator;
import org.bouncycastle.crypto.generators.ElGamalKeyPairGenerator;
import org.bouncycastle.crypto.params.DSAKeyGenerationParameters;
import org.bouncycastle.crypto.params.DSAParameters;
import org.bouncycastle.crypto.params.DSAValidationParameters;
import org.bouncycastle.crypto.params.ElGamalKeyGenerationParameters;
import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPKeyPair;
import org.bouncycastle.openpgp.PGPKeyRingGenerator;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPSignatureSubpacketGenerator;
import org.bouncycastle.openpgp.operator.PBESecretKeyEncryptor;
import org.bouncycastle.openpgp.operator.PGPDigestCalculator;
import org.bouncycastle.openpgp.operator.bc.BcPBESecretKeyEncryptorBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPKeyPair;



import com.acta.adapter.pgp.crypto.AbstractTrace;
import com.acta.adapter.sdk.AdapterEnvironment;
 
public class PGPCrypto {
	
    private AbstractTrace trace = null;
  	private static String pkr_extension = ".pkr";
  	private static String skr_extension = ".skr";
  	private String instanceName;
  	private String keyPath;
	
	public static final int S2KCOUNT = 0xc0;
//	private Date now = new Date();
	///  
	private String uName;
	private String eMail;
	private char[] password;
	private static int signSize = 1024;
	private static int encryptSize = 3072;
	/// 




	/// Start section for getters	 
	private String getUserId()
	{
		return String.format("%s <%s>", uName, eMail);
	}

	public String getPublicKeyRing() throws Exception
	{
		String publicKeyPath = keyPath + instanceName + pkr_extension;
		String pkr_key = "";
		PGPPublicKeyRing pkr = getPGPPublicKeyRing();
		if(  pkr == null) 
		{
			String response = "No Public Key Ring Found with path and name: " + publicKeyPath;
			return response;
		}
		else
		{
			ByteArrayOutputStream baos = new  ByteArrayOutputStream();
			ArmoredOutputStream aos = new ArmoredOutputStream(baos);
			pkr.encode(aos);
			aos.close();
			baos.close();
			pkr_key = baos.toString();
			return pkr_key;
		}
	}

	private PGPPublicKeyRing getPGPPublicKeyRing() throws Exception
	{
		PGPPublicKeyRing pkr = null;
		InputStream inPubKey = null;
		String publicKeyPath = keyPath + instanceName + pkr_extension;
		try{
			inPubKey = new FileInputStream(publicKeyPath);
		}
		catch (Exception e)
		{
			trace.addInfo("PGPCrypto::getPublicKeyRing. failed to create FileInputStream from "+publicKeyPath);
			throw e;
		}
		if(  inPubKey != null) 
		{
			Security.addProvider(new BouncyCastleProvider());
			pkr = readPublicKeyRingFromCol(inPubKey);
		}
		return pkr;
	}
	/// End section for getters	 

	public boolean validatePGPkeys() throws Exception
	{
		boolean bKeyCheck = false;
		InputStream inSKeyRing = null;
		PGPSecretKey sKey = null;
		PGPSecretKeyRing skr = null;
		PBESecretKeyDecryptor pbSKdecr = null;
		PGPPrivateKey spk = null;
		String secureKeyPath = keyPath + instanceName + skr_extension;
		PGPPublicKeyRing pkr = getPGPPublicKeyRing();
		if (pkr == null){
			trace.addInfo("PGPCrypto::getPublicKeyRing. Failed to get Public keyring");
			throw new Exception("PGPCrypto::getPublicKeyRing:Exception");			
		}			
		try{
			inSKeyRing = new FileInputStream(secureKeyPath);
		}
		catch (Exception e)
		{
			trace.addInfo("PGPCrypto::getPublicKeyRing. failed to create FileInputStream from "+secureKeyPath);
			throw e;
		}
		if(  inSKeyRing == null ) 
		{
			trace.addInfo("PGPCrypto::getPublicKeyRing. Failed to get Secure keyring");
			throw new Exception("PGPCrypto::getPublicKeyRing:Exception");
		}
		else
		{
			String sk_userID = ""; 
			PGPPublicKey pKey = pkr.getPublicKey();
			skr = findSecretKeyRing(inSKeyRing, pKey.getKeyID());
			sKey = skr.getSecretKey();
			Iterator its = sKey.getUserIDs();
			if (its.hasNext()) {
				sk_userID = (String) its.next();
			}
//			Exception executionException;
			try
			{
				pbSKdecr = new BcPBESecretKeyDecryptorBuilder(new BcPGPDigestCalculatorProvider()).build(password);
				spk = sKey.extractPrivateKey(pbSKdecr); // if wrong password, then exception
			}
			catch (Exception e) {
				trace.addInfo("PGPCrypto::getPublicKeyRing. Failed to validate private key - wrong passphrase");
				throw e;
			}
			
			if (spk == null){
				trace.addInfo("PGPCrypto::getPublicKeyRing. Failed to get Secure keyring");
				throw new Exception("PGPCrypto::getPublicKeyRing:Exception");
			}
			if ( !sk_userID.equals(getUserId())) {
				trace.addInfo("PGPCrypto::getPublicKeyRing. User Name or E-Mail is not matching to '"+sk_userID.toString()+"'");
				throw new Exception("PGPCrypto::getPublicKeyRing:Exception");				
			}

				bKeyCheck = true;
		}		 
		return bKeyCheck;
	}

	public void createPGPkeys()
			throws IOException, PGPException,
			NoSuchProviderException, NoSuchAlgorithmException,
			InterruptedException {

		PGPKeyPair kp_sign = null;
		kp_sign = mkDSASign();


		PGPKeyPair kp_enc = null;
		kp_enc = mkELGAMALEncrypt(); // mkRSAEncrypt(); //mkELGAMALEncrypt();


		PGPKeyRingGenerator keyRingGenerator = mkRingGenerator(kp_sign, kp_enc);


		{
			PGPPublicKeyRing pkr = keyRingGenerator.generatePublicKeyRing();
			System.out.println("Public Key:");
			System.out.println(pkr.toString());
			ByteArrayOutputStream baos = new  ByteArrayOutputStream();
			ArmoredOutputStream aos = new ArmoredOutputStream(baos);
			pkr.encode(aos);
			aos.close();
			baos.close();
			System.out.println( baos.toString());
			// Generate public key ring to file.
			String pkrFileName = keyPath + instanceName + pkr_extension;
			BufferedOutputStream pubout = new BufferedOutputStream
					(new FileOutputStream(pkrFileName));
			pkr.encode(pubout);
			pubout.close();
		}
		{
			PGPSecretKeyRing skr = keyRingGenerator.generateSecretKeyRing();
			System.out.println("Secure KEy:");
			System.out.println(skr.toString());
			ByteArrayOutputStream baos = new  ByteArrayOutputStream();
			ArmoredOutputStream aos = new ArmoredOutputStream(baos);
			skr.encode(aos);
			aos.close();
			baos.close();
			System.out.println( baos.toString());
			// Generate secret key ring to file.
			String skrFileName = keyPath + instanceName + skr_extension;
			BufferedOutputStream secout = new BufferedOutputStream
					(new FileOutputStream(skrFileName));
			skr.encode(secout);
			secout.close();
		}

	}	  

	private PGPKeyPair mkDSASign() // this is for signing it gives in properties KeyType = Diffie-Hellman/DSS
			throws NoSuchProviderException, NoSuchAlgorithmException, PGPException {

		DSAParametersGenerator pGen = new DSAParametersGenerator();
		pGen.init(signSize, 20, new SecureRandom()); 
		DSAParameters  params = pGen.generateParameters();
//		DSAValidationParameters pValid = params.getValidationParameters();
		DSAKeyPairGenerator         dsaKeyGen = new DSAKeyPairGenerator();
		DSAKeyGenerationParameters  genParam = new DSAKeyGenerationParameters(new SecureRandom(), params);
		dsaKeyGen.init(genParam);
		AsymmetricCipherKeyPair  pair = dsaKeyGen.generateKeyPair();
		BcPGPKeyPair bckp  = new BcPGPKeyPair(PGPPublicKey.DSA, pair,  new Date());
		return bckp;

	}  	   

 /// to test RSA encryption type
	/*private PGPKeyPair  mkRSAEncrypt()
		 throws NoSuchProviderException, NoSuchAlgorithmException, PGPException {
	 // This object generates individual key-pairs.
     RSAKeyPairGenerator  kpg = new RSAKeyPairGenerator();
     kpg.init (new RSAKeyGenerationParameters(BigInteger.valueOf(0x10001),new SecureRandom(), encryptSize, 12));
     // First create the master (signing) key with the generator.
     BcPGPKeyPair rsakp_sign =  new BcPGPKeyPair (PGPPublicKey.RSA_SIGN, kpg.generateKeyPair(), new Date());
    return rsakp_sign;
 }*/


	////=====================
	private PGPKeyPair mkELGAMALEncrypt()
			throws NoSuchProviderException, NoSuchAlgorithmException, PGPException {
		BigInteger p  = new BigInteger(encryptSize, 20, new SecureRandom());
		BigInteger g = new BigInteger(encryptSize, 20, new SecureRandom());
		ElGamalParameters   dhParams = new ElGamalParameters(p, g);
		ElGamalKeyGenerationParameters  params = new ElGamalKeyGenerationParameters(new SecureRandom(), dhParams);
		ElGamalKeyPairGenerator         kpGen = new ElGamalKeyPairGenerator();
		kpGen.init(params);
//		AsymmetricCipherKeyPair         pair = kpGen.generateKeyPair();
		BcPGPKeyPair bckp = new BcPGPKeyPair(PGPPublicKey.ELGAMAL_ENCRYPT, kpGen.generateKeyPair(), new Date()) ;

		return bckp;
	}	  
	//////=====================   

	private PGPKeyRingGenerator mkRingGenerator(PGPKeyPair kp_sign, PGPKeyPair kp_enc)
			throws PGPException {
		// Add a self-signature on the id
		PGPSignatureSubpacketGenerator signhashgen =
				new PGPSignatureSubpacketGenerator();

		// Add signed metadata on the signature.
		// 1) Declare its purpose
		signhashgen.setKeyFlags
		(false, KeyFlags.SIGN_DATA | KeyFlags.CERTIFY_OTHER);
		// 2) Set preferences for secondary crypto algorithms to use
		//    when sending messages to this key.
		signhashgen.setPreferredSymmetricAlgorithms
		(false, new int[]{
				/*                     SymmetricKeyAlgorithmTags.AES_256,
                       SymmetricKeyAlgorithmTags.AES_192,
                       SymmetricKeyAlgorithmTags.AES_128,
                       SymmetricKeyAlgorithmTags.CAST5,
                       SymmetricKeyAlgorithmTags.TRIPLE_DES,
				 */
				SymmetricKeyAlgorithmTags.CAST5,
				SymmetricKeyAlgorithmTags.NULL,
				SymmetricKeyAlgorithmTags.TRIPLE_DES
		});
		signhashgen.setPreferredHashAlgorithms
		(false, new int[]{
				/*   HashAlgorithmTags.SHA256,
               HashAlgorithmTags.SHA1,
               HashAlgorithmTags.SHA384,
               HashAlgorithmTags.SHA512,
               HashAlgorithmTags.SHA224,*/

		});
		// 3) Request senders add additional checksums to the
		//    message (useful when verifying unsigned messages.)
		signhashgen.setFeature
		(false, Features.FEATURE_MODIFICATION_DETECTION);
		/* 
        signhashgen.setPreferredCompressionAlgorithms
               (false, new int[]{
                       CompressionAlgorithmTags.ZLIB,
                       CompressionAlgorithmTags.BZIP2,
                       CompressionAlgorithmTags.ZIP,
               });

		 */
		// Create a signature on the encryption subkey.
		PGPSignatureSubpacketGenerator enchashgen =
				new PGPSignatureSubpacketGenerator();
		// Add metadata to declare its purpose
		enchashgen.setKeyFlags(false, KeyFlags.ENCRYPT_COMMS | KeyFlags.ENCRYPT_STORAGE); 

		// Objects used to encrypt the secret key. 
		PGPDigestCalculator sha1Calc =
				new BcPGPDigestCalculatorProvider()
		.get(HashAlgorithmTags.SHA1);
		PGPDigestCalculator sha256Calc =
				new BcPGPDigestCalculatorProvider()
		.get(HashAlgorithmTags.SHA256);  

		// bcpg 1.48 exposes this API that includes s2kcount. Earlier
		// versions use a default of 0x60.
		PBESecretKeyEncryptor pske =
				(new BcPBESecretKeyEncryptorBuilder
						(PGPEncryptedData.CAST5, sha256Calc, S2KCOUNT))//CAST5 
						.build(password); 


		System.out.println(kp_sign.getPublicKey().getAlgorithm());

		// create the keyring itself. The constructor
		// takes parameters that allow it to generate the self
		// signature. 
		PGPKeyRingGenerator keyRingGen =
				new PGPKeyRingGenerator
				(PGPSignature.POSITIVE_CERTIFICATION,  //1 certificationLevel
						kp_sign,                              //2  masterKey
						getUserId(),                          //3 id
						sha1Calc,							 //4 PGPDigestCalculator checksumCalculator
						signhashgen.generate(),				 //5  PGPSignatureSubpacketVector hashedPcks
						null,								 //6 PGPSignatureSubpacketVector unhashedPcks
						new BcPGPContentSignerBuilder(kp_sign.getPublicKey().getAlgorithm(),
								HashAlgorithmTags.SHA1),			//7  PGPContentSignerBuilder keySignerBuilder
								pske);								//8 PBESecretKeyEncryptor keyEncryptor

		// Add our encryption subkey, together with its signature.
		keyRingGen.addSubKey
		(kp_enc, enchashgen.generate(), null);

		return keyRingGen;
	}    


	private PGPPublicKeyRing readPublicKeyRingFromCol(InputStream in) throws Exception {
		PGPPublicKeyRing pkr = null;

		try {
			pkr = new PGPPublicKeyRing(in, new BcKeyFingerprintCalculator());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.toString());
		}    	   
		return pkr;
	}

	
	private static PGPSecretKeyRing findSecretKeyRing(InputStream keyIn, long keyID)
			throws IOException, PGPException,   NoSuchProviderException
	{
		PGPSecretKeyRing skr = new PGPSecretKeyRing(keyIn, new BcKeyFingerprintCalculator());
		return skr;
	}   

//=============================================	
	//=============================================
	//=============================================
	//=============================================
	//=============================================
	//=============================================
	//=============================================
	//=============================================
	//=============================================
	//=============================================
	//=============================================
	
	//=============================================//=============================================
	//=============================================
	//=============================================
	
	//=============================================
	//=============================================
	//=============================================
	//=============================================
	//=============================================
	
	//=============================================
	
  	public PGPCrypto (AdapterEnvironment env){
  		trace = new AbstractTrace(env);
  	}
  	
  	
      public void encrypt(InputStream in, OutputStream out) throws Exception {
           // String publicKeyPath = "./resources/keys/pubring.pkr";
    	  //String publicKeyPath = "./resources/keys/dummy.pkr";
    	  String publicKeyPath = keyPath + instanceName + pkr_extension;
            try {
                  encrypt(publicKeyPath, inputStreamToString(in), out, trace);
            } catch (Exception e) {
                  e.printStackTrace();
                  throw new Exception(e.toString());
            }
      }
 
      public void encrypt(String publicKeyPath, InputStream in, OutputStream out, AbstractTrace trace) throws Exception {
            try {
                  encrypt(publicKeyPath, inputStreamToString(in), out, trace);
            } catch (Exception e) {
                  e.printStackTrace();
                  throw new Exception(e.toString());
            }
      }
 
      public void encrypt(String publicKeyPath, String inString, OutputStream out, AbstractTrace trace) throws Exception {
            this.trace = trace;
            try {
                  Security.addProvider(new BouncyCastleProvider());
                  InputStream keyStream = getClass().getResourceAsStream(publicKeyPath);
                  if (keyStream == null) {
                        throw new Exception("Unable to find Resource at " + publicKeyPath, new Exception("Resource \"" + publicKeyPath
                                    + "\" not available. Please check the path or file name."));
                  }
                  // Get Public key
                  PGPPublicKey key = readPublicKeyFromCol(keyStream);
                  out = new DataOutputStream(out);
                  ByteArrayOutputStream bOut = new ByteArrayOutputStream();
                  PGPCompressedDataGenerator comData = new PGPCompressedDataGenerator(PGPCompressedDataGenerator.ZIP);
                  writeStringToLiteralData(comData.open(bOut), inString);
                  comData.close();
                  // object that encrypts the data
                  if (trace != null) {
                        trace.addInfo("Trace1: Going to encrypt the data");
                  }
                  PGPEncryptedDataGenerator cPk = new PGPEncryptedDataGenerator(new BcPGPDataEncryptorBuilder(PGPEncryptedDataGenerator.CAST5), true);
                  cPk.addMethod(new BcPublicKeyKeyEncryptionMethodGenerator(key));
 
                  byte[] bytes = bOut.toByteArray();
                  out = cPk.open(out, bytes.length);
                  out.write(bytes);
                  cPk.close();
                  out.close();
            } catch (Exception e) {
                  e.printStackTrace();
                  throw new Exception(e.toString());
            }
      }
 
      private String inputStreamToString(InputStream in) {
            // read in stream into string.
            StringBuffer buf = new StringBuffer();
            try {
                  InputStreamReader isr = null;
                  // try UTF-8 conversion
                  try {
                        isr = new InputStreamReader(in, "UTF-8");
                  } catch (UnsupportedEncodingException e) {
                        // or atleast in natural encoding
                        isr = new InputStreamReader(in);
                  }
                  int c = 0;
                  while ((c = isr.read()) != -1) {
                        buf.append((char) c);
                  }
                  in.close();
                  isr.close();
            } catch (IOException e) {
                  e.printStackTrace();
            }
            return buf.toString();
      }
 
      private void writeStringToLiteralData(OutputStream out, String inString) throws IOException {
            PGPLiteralDataGenerator lData = new PGPLiteralDataGenerator();
            OutputStream pOut = lData.open(out, PGPLiteralData.BINARY, "", inString.length(), new Date());
            pOut.write(inString.getBytes());
            lData.close();
      }
 
      private PGPPublicKey readPublicKeyFromCol(InputStream in) throws Exception {
            PGPPublicKeyRing pkRing = null;
            PGPPublicKey result = null, key = null;
            try {
                  PGPPublicKeyRingCollection pkCol = new PGPPublicKeyRingCollection(in, new BcKeyFingerprintCalculator());
                  Iterator<PGPPublicKeyRing> it = pkCol.getKeyRings();
                  while (it.hasNext()) {
                        pkRing = (PGPPublicKeyRing) it.next();
                        Iterator<PGPPublicKey> pkIt = pkRing.getPublicKeys();
                        while (pkIt.hasNext()) {
                              key = (PGPPublicKey) pkIt.next();
                              if (key.isEncryptionKey()) {
                                    result = key;
                                    break;
                              }
                        }
                  }
            } catch (Exception e) {
                  e.printStackTrace();
                  throw new Exception(e.toString());
            }
            return result;
      }
 
      public void decrypt(InputStream in, OutputStream out, String passphrase, AbstractTrace trace) throws Exception {
            //String privateKeyPath = "./resources/keys/secring.skr";
    	  //String privateKeyPath = "./resources/keys/dummy.skr";
    	  String privateKeyPath = keyPath + instanceName + skr_extension;
            try {
                  InputStream inKey = null;
                  try {
                        inKey = new FileInputStream(privateKeyPath); 
                        //inKey = getClass().getResourceAsStream(privateKeyPath);
                  } catch (Exception e) {
                        e.printStackTrace();
                        throw new Exception(e.toString());
                  }
                  decrypt(in, out, inKey, passphrase.toCharArray(), trace);
            } catch (Exception e) {
                  e.printStackTrace();
                  throw new Exception(e.toString());
            }
      }
 
      public void decrypt(InputStream encData, OutputStream out, String privateKeyPath, String passphrase, AbstractTrace trace)
                  throws Exception {
            try {
                  InputStream inKey = null;
                  try {
                        inKey = new Object().getClass().getResourceAsStream(privateKeyPath);
                  } catch (Exception e) {
                        e.printStackTrace();
                        throw new Exception(e.toString());
                  }
                  decrypt(encData, out, inKey, passphrase.toCharArray(), trace);
            } catch (Exception e) {
                  e.printStackTrace();
                  throw new Exception(e.toString());
            }
      }
 
      public void decrypt(String encData, OutputStream out, String privateKeyPath, String passphrase, AbstractTrace trace)
                  throws Exception {
            try {
                  InputStream in = null;
                  InputStream inKey = null;
                  try {
                        in = new ByteArrayInputStream(encData.getBytes("UTF-8"));
                  } catch (UnsupportedEncodingException e) {
                        in = new ByteArrayInputStream(encData.getBytes());
                  }
                  try {
                        inKey = getClass().getResourceAsStream(privateKeyPath);
                  } catch (Exception e) {
                        e.printStackTrace();
                        throw new Exception(e.toString());
                  }
                  decrypt(in, out, inKey, passphrase.toCharArray(), trace);
            } catch (Exception e) {
                  e.printStackTrace();
                  throw new Exception(e.toString());
            }
      }
 
      public void decrypt(InputStream encdata, OutputStream out, InputStream key, char passphrase[], AbstractTrace trace)
                  throws Exception {
            Security.addProvider(new BouncyCastleProvider());
            try {
                  BcKeyFingerprintCalculator bcKeyCalc = new BcKeyFingerprintCalculator();
                  encdata = PGPUtil.getDecoderStream(encdata);
                  PGPObjectFactory pgpObjectFactory = new PGPObjectFactory(encdata, new BcKeyFingerprintCalculator());
                  PGPEncryptedDataList enc;
                  Object o = pgpObjectFactory.nextObject();
 
                  if (o instanceof PGPEncryptedDataList) {
                        enc = (PGPEncryptedDataList) o;
                  } else {
                        enc = (PGPEncryptedDataList) pgpObjectFactory.nextObject();
                  }
 
                  Iterator it = enc.getEncryptedDataObjects();
                  PGPPrivateKey sKey = null;
                  PGPPublicKeyEncryptedData pbe = null;
                  while (sKey == null && it.hasNext()) {
                        pbe = (PGPPublicKeyEncryptedData) it.next();
                        sKey = findSecretKey(key, pbe.getKeyID(), passphrase);
                  }
                  if (sKey == null) {
                        throw new IllegalArgumentException("secret key for message not found.");
                  }
                  BcPublicKeyDataDecryptorFactory bcPubFact = new BcPublicKeyDataDecryptorFactory(sKey);
                  InputStream clear = pbe.getDataStream(bcPubFact);
                  PGPObjectFactory plainFact = new PGPObjectFactory(clear, bcKeyCalc);
                  Object message = plainFact.nextObject();
                  if (message instanceof PGPCompressedData) {
                        PGPCompressedData cData = (PGPCompressedData) message;
                        PGPObjectFactory pgpFact = new PGPObjectFactory(cData.getDataStream(), bcKeyCalc);
                        message = pgpFact.nextObject();
                  }
                  ByteArrayOutputStream baos = new ByteArrayOutputStream();
                  if (message instanceof PGPLiteralData) {
                        PGPLiteralData ld = (PGPLiteralData) message;
                        InputStream unc = ld.getInputStream();
                        int ch;
                        while ((ch = unc.read()) >= 0) {
                              baos.write(ch);
                        }
                  } else if (message instanceof PGPOnePassSignatureList) {
                        throw new PGPException("encrypted message contains a signed message - not literal data.");
                  } else {
                        throw new PGPException("message is not a simple encrypted file - type unknown.");
                  }
 
                  // write outputstream
                  out.write(baos.toString().getBytes());
            } catch (Exception e) {
                  e.printStackTrace();
                  throw new Exception(e.toString());
            }
      }
 
      private static PGPPrivateKey findSecretKey(InputStream keyIn, long keyID, char[] pass) throws IOException, PGPException,
                  NoSuchProviderException {
            PGPSecretKeyRingCollection pgpSec = new PGPSecretKeyRingCollection(keyIn, new BcKeyFingerprintCalculator());
            PGPSecretKey pgpSecKey = pgpSec.getSecretKey(keyID);
            if (pgpSecKey == null) {
                  return null;
            }
            PBESecretKeyDecryptor pbSKdecr = new BcPBESecretKeyDecryptorBuilder(new BcPGPDigestCalculatorProvider()).build(pass);
            return pgpSecKey.extractPrivateKey(pbSKdecr);
      }
      
  	///// start setters section	 
  	public void setUserName(String uName)
  	{
  		this.uName = uName; 
  	}
  	
  	public void setEmail (String eMail) 
  	{
  		this.eMail = eMail;
  	}
  	public void setPasssword(String password)
  	{
  		this.password = password.toCharArray();
  	}
  	public void setKeyPath(String keyPath)
  	{
  		this.keyPath = keyPath;
  	}
  	public void setInstanceName(String instanceName)
  	{
  		this.instanceName = instanceName;
  	}
  	/// end setters section

}

