package com.acta.adapter.pgp;

import com.acta.adapter.pgp.crypto.AbstractTrace;
import com.acta.adapter.sdk.*;
import com.acta.util.log.ActaLoggerManager;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public class PGPReadTable implements TableSource, Delimited {
	protected static Logger _logger = ActaLoggerManager
			.getLogger(PGPReadTable.class);
	private OperationEnvironment _adapterOperationEnvironment;
	private AdapterEnvironment _adapterEnvironment;
	private PGPAdapter _adapter;
	// private PGPSession _session ;
	private PGPFileNode _fileNode; // adapter defined metadata
	private String _fullFileName; // the name of the file with the data
	private int _recordFormat;
	// private String _xml = new String () ;
	private String _fileContent = new String();
	private StringTokenizer _lines = null;
	private String _characterSet;
	private String _fullFileDirectory;
	// ---------------------------------------------------
	// Automatically configured properties (not from Designer)
	// ---------------------------------------------------
	private String _recordSeparator = "\r\n";
	// VBY private String _fieldSeparator = ";";
	private String _fieldSeparator = null;
	private String _decimalSeparator = null;
	private int _skipHeaderRows = 0;
	private int _packetCount = 0;

	// ------------------------------------------------
	// Configurable Properties
	// Batch size
	// Duplication factor
	// ---------------------------------------------------
	private int batchSize = 1;
	private int duplication = 1;

	public int getBatchSize() {
		// _adapterOperationEnvironment.println ( "VBY getBatchSize start" ) ;
		return batchSize;
	}

	/**
		 *
		 */
	public void setBatchSize(int batch) {
		// _adapterOperationEnvironment.println (
		// "VBY PGPReadTable::setBatchSize start" ) ;
		batchSize = batch;
		// _adapterOperationEnvironment.println (
		// "VBY PGPReadTable:: setBatchSize end" ) ;
	}

	/**
	 * Note, batch size is not required by the interface.
	 */
	public int getDuplication() {
		return duplication;
	}

	/**
		 *
		 */
	public void setDuplication(int dup) {
		duplication = dup;
	}

	/**
		 *
		 */
	public void metadata(Object metadata) {
		// Get table metadata composed in PGPImport
		_adapterOperationEnvironment.println("PGPReadTable::metadata");
		_fileNode = (PGPFileNode) metadata;
		_adapterOperationEnvironment.println(_fileNode.toString());
		_decimalSeparator = _fileNode.getDecimalSeparator();
		_adapterOperationEnvironment.println("VBY PGPReadTable::metadata. Decimal Separator "+_decimalSeparator);
		_adapterOperationEnvironment.println("VBY PGPReadTable::metadata end");
	}

	public String getDateFormat() {
		return "yyyy.mm.dd hh24:mi:ss";
	}

	/**
	 * Returns the record format com.acta.adapter.sdk.Table.RecordFormatXml or
	 * com.acta.adapter.sdk.Table.RecordFormatDelimited.
	 * 
	 * @see FileNode
	 */
	public int getRecordFormat() {
		if (null != _fileNode) {
			// TEST row, to be removed
			_fileNode.setTableType(PGPBrowse.METADATA_TABLE_XML);
			if (_fileNode.getTableType().equals(
					PGPBrowse.METADATA_TABLE_DELIMITED)) {
				_recordFormat = Table.RecordFormatDelimeted;
				_adapterOperationEnvironment
						.println("PGPReadTable::getRecordFormat - Delimited");
			} else {
				_recordFormat = Table.RecordFormatXml;
				_adapterOperationEnvironment
						.println("PGPReadTable::getRecordFormat - XML");
			}
		} else
			return -1; // will blow up later
		return _recordFormat;
	}

	public String getRowDelimiter() {
		return _recordSeparator;
	}

	public String getColumnDelimiter() {
		if (null != _fileNode)
			_fieldSeparator = _fileNode.getFieldSeparator();
		else
			; // will blow up later if metadata (FileNode)is not set
		return _fieldSeparator;
	}

	public String getTextDelimiter() {
		return "";
	}

	public String getEscapeChar() {
		return "";
	}

	// ------------------------------------------------
	// Interface implementations
	// ------------------------------------------------

	/**
		  *
		  */
	public void initialize(OperationEnvironment adapterOperationEnvironment) {
		_adapterOperationEnvironment = adapterOperationEnvironment;
		_adapterOperationEnvironment
				.println("VBY PGPReadTable::intialize start");
		_adapter = (PGPAdapter) _adapterOperationEnvironment.getAdapter();
		_adapterEnvironment = _adapterOperationEnvironment
				.getAdapterEnvironment();
		// _session = (PGPSession)adapterOperationEnvironment.getSession() ;
		_characterSet = _adapterEnvironment.getCharacterSet();
		_adapterOperationEnvironment.println("VBY PGPReadTable::intialize end");
	}

	/**
	 * Create the root directory.
	 */
	public void start() throws AdapterException {
		if (null == _fileNode) // must be defined at this point. If
			throw new AdapterException("The adapter's metadata is not defined.");
		_adapterOperationEnvironment.println("PGPReadTable::start");
		_adapterOperationEnvironment.println(_fileNode.toString());
		// _fullFileName = _adapter.getRootDirectory() + File.separator +
		// _session.getSubDirectory() + File.separator + _fileNode.getFileName()
		// + ".dat" ;

	}

	/**
	 * Does nothing useful.
	 */
	public void stop() throws AdapterException {
		_adapterOperationEnvironment.println("PGPReadTable::stop");
	}

	/**
	 * 1. Open the file and check if the file used for this adapter operation
	 * exist. 2. Read file (UTF8). For this test case we will read the entire
	 * file in memory.
	 */
	public void begin() throws AdapterException {
		_packetCount = 0;
		ByteArrayOutputStream decryptedStream = null;
		_adapterOperationEnvironment.println("PGPReadTable::begin");
		// Should receive back from engine metadata, saved during metadata
		// import
		_adapterOperationEnvironment
				.println("PGPReadTable::begin. RecordFormat="
						+ String.valueOf(getRecordFormat()));
		if (null == _fileNode) {
			_adapterOperationEnvironment
					.println("PGPReadTable::begin. Metadata objecty is not set");
			throw new AdapterException("Metadata object is not set.");
		}
		_adapterOperationEnvironment
				.println("PGPReadTable::begin. Metadata received");
		_adapterOperationEnvironment.println(_fileNode.toString());
		_fieldSeparator = _fileNode.getFieldSeparator();
		_adapterOperationEnvironment
				.println("VBY PGPReadTable::begin. _fieldSeparator: "
						+ _fieldSeparator);
		batchSize = Integer.valueOf(_fileNode.getFetchSize());
		_adapterOperationEnvironment
				.println("VBY PGPReadTable::begin. batchSize: " + batchSize);
		_skipHeaderRows = Integer.valueOf(_fileNode.getSkipHeaderRows());
		_adapterOperationEnvironment
				.println("VBY PGPReadTable::begin. _skipHeaderRows: "
						+ _skipHeaderRows);
		_fullFileDirectory = _fileNode.getFileDirectory()
				+ _fileNode.getFileSubDirectory();
		_adapterOperationEnvironment
				.println("VBY PGPReadTable::begin. _fullFileDirectory: "
						+ _fullFileDirectory);
		File fd = new File(_fullFileDirectory);
		// For this test adapter we will read the entire file with assumption
		// that
		// file contains data in UTF8 encoding
		// For real adapter it will not be practical because files could be big
		// and we at risk to get out of memory exception
		// FileFilter fileFilter = ;
		File[] files = fd.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				String fileMask = _fileNode.getFileName().replaceAll("\\*",
						"(.*)");
				// _adapterOperationEnvironment.println (fileMask) ;
				return name.matches(fileMask);
			}
		});
		if (files.length == 0)
			_adapterOperationEnvironment
					.println("PGPReadTable::begin. No files found");
		else {
			int flen;
			byte[] buffer;
			int bytes_read;
			FileInputStream fi = null;
			for (int i = 0; i < files.length; i++) {
				flen = (int) files[i].length();
				if (files[i].exists()) {
					try {
						// read file
						decryptedStream = new ByteArrayOutputStream();
						_adapterOperationEnvironment
								.println("PGPReadTable::begin. Processing file"
										+ files[i].getName());
						fi = new FileInputStream(files[i]);
						_adapter.crypto.decrypt(fi, decryptedStream, _adapter
								.getPassphrasePGP(), new AbstractTrace(
								_adapterEnvironment));
						if (decryptedStream == null) {
							_adapterOperationEnvironment
									.println("PGPReadTable::begin. Decryption failed"
											+ files[i].getName());
							throw new AdapterException(
									"Decryption of file failed"
											+ files[i].getName());
						}
						// buffer = new byte[flen];
						// while ( (bytes_read = fi.read(buffer)) != -1 )
						// _fileContent += new String( buffer, 0, bytes_read,
						// _characterSet) ;//$JL-I18N$
						_fileContent = decryptedStream.toString();
						_adapterOperationEnvironment
								.println("PGPReadTable::begin. File content: "
										+ _fileContent);
						fi.close();
						// If there is no record separator after the last line -
						// add it
						if (!_fileContent.endsWith(_recordSeparator))
							_fileContent += _recordSeparator;
					} catch (Exception e) {
						throw new AdapterException(e, "Cannot read input file "
								+ _fullFileName + ". ");
					}
				} else {
					_adapterOperationEnvironment
							.println("PGPReadTable::begin. Metadata received");
					throw new AdapterException("File " + _fullFileName
							+ " does not exist.");
				}
			}
			// split all records on lines
			_lines = new StringTokenizer(_fileContent, _recordSeparator);
			_adapterOperationEnvironment.println("PGPReadTable::begin. file Lines: "
					+ String.valueOf(_lines.countTokens()));
			_fileContent = new String();
		}
	}

	/**
		   *
		   */
	public void end() throws AdapterException {
		_adapterOperationEnvironment.println("PGPReadTable::end");
		_fileContent = new String();
	}

	public String readNext() throws AdapterException,
			RecoverableOperationAdapterException {
		_packetCount++;
		_adapterOperationEnvironment
				.println("PGPReadTable::readNext. Package: " + _packetCount);
		String resultRow = new String();
		int recordsProcessed = 0;
		int recordsCount = 0;
		String columnValue = null;
		_packetCount++;
		try {
			if (_recordFormat == Table.RecordFormatXml)
				resultRow = "<AWA_BatchWrapper>";
			while (recordsProcessed < batchSize) {
				String line = null;
				try {
					line = _lines.nextToken(); // should have at least one
					// ;; to ;NuLl;
					_adapterOperationEnvironment
							.println("PGPReadTable::readNext. Line: " + line);
					if ((recordsCount < _skipHeaderRows) && (_packetCount == 1)){
						recordsCount++;
						_adapterOperationEnvironment
								.println("PGPReadTable::readNext. Number of SkipHeader Rows: "
										+ _skipHeaderRows);
						_adapterOperationEnvironment
								.println("PGPReadTable::readNext. Records skipped as Header: "
										+ recordsCount);
						continue;
					}
				} catch (NoSuchElementException e) {
					_logger.warning(e.getLocalizedMessage());
				} catch (NullPointerException e1) {
					_logger.warning(e1.getLocalizedMessage());
					_logger.warning("No data to transfer");
				}
				if (null != line) {
					for (int k = 0; k < duplication; k++) {
						recordsProcessed++;
						if (_recordFormat == Table.RecordFormatXml) {
							// generate xml
							String[] st = line.split(_fieldSeparator, -1);
							resultRow += _recordSeparator + "<AWA_Row>";
							try {
								for (int j = 0; j < _fileNode.getColumns().length; j++) {
									String colName = _fileNode.getColumns()[j]
											.getName();
									try {
										columnValue = st[j];
									} catch (Exception e) {
										_adapterOperationEnvironment
												.println("Line "
														+ Integer.toString(j)
														+ " has less fields than table definition");
										columnValue = "";
									}
								    // Replace ampersand as it will not be properly processed by XML Engine;
									columnValue = columnValue.replaceAll("&(?!amp;)", "&amp;");
									if (_decimalSeparator != ".")
									columnValue = columnValue.replaceAll(_decimalSeparator, ".");
									_adapterOperationEnvironment
											.println("PGPReadTable::readNext. Next token "
													+ columnValue);
									resultRow += "<" + colName + ">"
											+ columnValue + "</" + colName
											+ ">";
								}
							} catch (NoSuchElementException e) {
								throw new AdapterException(
										"Number of fields in < " + line
												+ "> does not match metadata.");
							}
							resultRow += "</AWA_Row>";
						} else {
							resultRow += line + _recordSeparator;
						}
						_adapterOperationEnvironment
								.println("PGPReadTable::readNext. Records processed: "
										+ recordsProcessed);
					}
				} else
					// no more data
					break;
			}
		} catch (Exception e) {
			throw new AdapterException(e);
		}
		// Batch size reached, or no more data
		if (_recordFormat == Table.RecordFormatXml)
			resultRow += "</AWA_BatchWrapper>";
		_adapterOperationEnvironment
				.println("PGPReadTable::readNext. XML Result row: " + resultRow);
		if (recordsProcessed > 0)
			return resultRow;
		else
			return "";
	}

}
