/* RAZip bitstream specification, version 1.0 PR 2 * Copyright (C) 2001 Elifarley Callado Coelho  * This program is free software; you can redistribute it and/or * modify it under the terms of the GNU General Public License, version 2, * as published by the Free Software Foundation; * This program is distributed in the hope that it will be useful, * but WITHOUT ANY WARRANTY; without even the implied warranty of * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the * GNU General Public License for more details. * You should have received a copy of the GNU General Public License * along with this program; if not, write to the Free Software * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA. * See the file "COPYING" for the software license.  * Elifarley Callado Coelho * Belo Horizonte, Brazil * elifarley@yahoo.com * http://www.geocities.com/elifarley/ *  *//*In this version, CM must be CM_DEFAULT,and the following flags should be set to zero:F0_RESFORKFNC0_ECCFNC0_ENCRYPTED*/package elifarley.razip;public interface RAZipConstants {	/*			RAZip file layout:				+------+------+		| RAZIP_MAGIC |		+------+------+		+=============+		| FILE_HEADER |		+=============+		+===========+		|	BLOCK	|		+===========+		{		+===========+		|	BLOCK	|		+===========+		.		.		.		}		+===============+		| FILE-TRAILER  |		+===============+		--------------------------------------------------------------		FILE_HEADER:                	+--------+         	|FLG_NC_0|         	+--------+			if (F_HAS_MORE_FLAG_BYTES_NC) {	        	+--------+	         	|FLG_NC_1|	         	+--------+			}						.			.			.			if (F_HAS_MORE_FLAG_BYTES_NC) {	         	+--------+	         	|FLG_NC_n|	         	+--------+					}				if (FNC0_NON_STANDARD_COMPRESSION) {         	        	+---+----+	         	|CM |CDTL|	         	+---+----+						}					if (FNC0_ENCRYPTED) {								(cipher, mode, padding scheme, key size, block size, rounds)	         	+-------------+	         	|CIPHER_FLAGS0|	         	+-------------+						if (!CF0_UNDISCLOSED) {						         	if (CF0_HAS_MORE_CF) {			         	+-------------+			         	|CIPHER_FLAGS1|			         	+-------------+										         	if (CF1_SET_CIPHER) {				         	+-----------+				         	|S_CIPHER_ID|				         	+-----------+												}									         	if (CF1_SET_MODE) {							+-------------+							| CIPHER_MODE | // 3 bits suffice							+-------------+												}								         				         	if (CF1_SET_KEY_SIZE) {				         	+----+----+				         	|KEY_SIZE |  (in bytes)				         	+----+----+														}									         	if (CF1_SET_BLOCK_SIZE) {				         	+-----+-----+				         	|BLOCK_SIZE |				         	+-----+-----+														}									         	if (CF1_SET_ITERATIONS) {				         	+-----+-----+				         	|ITERATIONS |				         	+-----+-----+												}													         	if (CF1_SET_PADDING) {				         	+-----------+				         	|  PADDING  |				         	+-----------+														}					} // if (CF0_HAS_MORE_CF)															if (CF0_USE_ASYMMETRIC_KEY) {			         	+-------------+			         	|CIPHER_FLAGS2|			         	+-------------+					         	if (CF2_SET_CIPHER) {				         					         	+-----------+				         	|A_CIPHER_ID|				         	+-----------+				         				        }			         	if (CF2_SET_KEY_SIZE) {				         	+----+----+				         	|KEY_SIZE |  (in bytes)				         	+----+----+														}			         	if (CF2_SET_PADDING) {				         	+---------+				         	|PADDING  |				         	+---------+														}					} // if (CF0_USE_ASYMMETRIC_KEY)				} // if (!CF0_UNDISCLOSED) 							} // if (FNC0_ENCRYPTED) 					if (FNC0_ECC) {	         	+------+	         	|ECC_ID|	         	+------+			         		         	(TO DO:  add more info)			}			if (!FNC0_HEADER_NOT_ENCODED) next bytes are inside compressed stream			+-----+			|FLG_0|			+-----+			if (F_HAS_MORE_FLAG_BYTES) {				+-----+				|FLG_1|				+-----+			}						.			.			.			if (F_HAS_MORE_FLAG_BYTES) {				+-----+				|FLG_n|				+-----+			}				if (FNC0_NON_STANDARD_INTEGRITY_CHECK) {				+---2-bits--+---------6-bits---------+				| CRCLENLEN |   INTEGRITY_CHECK_ID   |				+-----------+------------------------+				+-(1, 2, 3 or 4 bytes, according to CRCLENLEN)---+				| CRCLEN                                         |				+------------------------------------------------+							}			if (FNC0_BLOCK_INTEGRITY_CHECK) {				+---2-bits--+---------6-bits---------+				| CRCLENLEN |   INTEGRITY_CHECK_ID   |				+-----------+------------------------+				+-(1, 2, 3 or 4 bytes, according to CRCLENLEN)---+				| BLOCK_I_LEN                                    |				+------------------------------------------------+							}			if (F0_EXTENDED_MTIME_INFO) {				+---+---+---+---+				|    MTIME64    |				+---+---+---+---+			} else {				+---+---+---+---+				|    MTIME32    |				+---+---+---+---+			}					if (F0_EXTENDED_CTIME_INFO) {				+---+---+---+---+				|    CTIME64    |				+---+---+---+---+	        } 			      	if (F0_TYPE_CREATOR) {				+---+---+---+---+---+---+---+---+				|      TYPE     |    CREATOR    |				+---+---+---+---+---+---+---+---+						}		      			if (F1_NON_STANDARD_TEXT_ENCODING) {         	        	+--------+	         	|TEXT_ENC| 	         	+--------+						}			if (F0_NAME) {				+=========================================+				|...original file name, zero-terminated...| 				+=========================================+			}	     		if (F0_COMMENT) {				+===================================+				|...file comment, zero-terminated...| 				+===================================+			}     		if (F0_PROPERTIES) {				+---------+=================================+				| PROP_ID |...properties, zero-terminated...| 				+---------+=================================+				{        			(						+---------+=================================+						| PROP_ID |...properties, zero-terminated...| 						+---------+=================================+					) or					(				         +----------+				         | PROP_END |				         +----------+							)				}			}			if (F0_EXTRA_FIELD_8) {				+=================================+				|         EXTRA_FIELD_8           | 				+=================================+			}				if (F1_EXTRA_FIELD_16) {				+=================================+				|         EXTRA_FIELD_16          | 				+=================================+			}				if (F1_EXTRA_FIELD_32) {				+=================================+				|         EXTRA_FIELD_32          | 				+=================================+			}			if (FNC0_HEADER_CRC) {				+---+---+				| HCRC  |				+---+---+			}		------------------------------------------------------------------	EXTRA_FIELD_8 :			         +==============+         |  SUBFIELD8   |          +==============+	{        (         +==============+         |  SUBFIELD8   |          +==============+		) or		(         +---------+---------+         |      SID_NMS      |         +---------+---------+				)		.	.	.		}		------------------------------------------------------------------	EXTRA_FIELD_16 :			         +==============+         |  SUBFIELD16  |         +==============+	{        (         +==============+         |  SUBFIELD16  |          +==============+		) or		(         +---------+---------+         |      SID_NMS      |         +---------+---------+				)		.	.	.		}	------------------------------------------------------------------	EXTRA_FIELD_32 :			         +==============+         |  SUBFIELD32  |          +==============+	{        (         +==============+         |  SUBFIELD32  |         +==============+		) or		(         +---------+---------+         |      SID_NMS      |         +---------+---------+				)		.	.	.		}	------------------------------------------------------------------	SUBFIELD8 :            +---+---+----+===================================+            | SID8  |LEN8|... LEN8 bytes of subfield data ...|            +---+---+----+===================================+	------------------------------------------------------------------	SUBFIELD16 :            +---+---+---+---+====================================+            | SID16 | LEN16 |... LEN16 bytes of subfield data ...|            +---+---+---+---+====================================+	------------------------------------------------------------------	SUBFIELD32 :            +---+---+---+---+---+---+====================================+            | SID32 |     LEN32     |... LEN32 bytes of subfield data ...|            +---+---+---+---+---+---+====================================+	------------------------------------------------------------------	BLOCK :					+=================+			| compressed data |			+=================+						1 bit isLastBlock			31 bits cLen			+---+---+---+---+			|      uLen     | 			+---+---+---+---+			if (FNC0_BLOCK_INTEGRITY_CHECK) {				+===============================================+				|   BLOCK_I_LEN bytes of BLOCK_INTEGRITY_CHECK  | 				+===============================================+						}				------------------------------------------------------------------	FILE_TRAILER :			+==========================+			|    FT_INTEGRITY_CHECK    | 			+==========================+	------------------------------------------------------------------	MTIME32 :		unsigned number of seconds since January 1, 1970, 00:00:00 GMT (up to year 2106)		MTIME64 :		signed number of milliseconds since January 1, 1970, 00:00:00 GMT (from 292 469 238 BC up to 292 473 178) 		CTIME64 :		signed number of milliseconds since January 1, 1970, 00:00:00 GMT (from 292 469 238 BC up to 292 473 178) 			PROP_ID :		0 : PROP_END;		1 - 7 : reserved;		8 : record-like;			name (string), end-of-field, value (string), end-of-record					end-of-field: ascii 29			end-of-record: ascii 30					9 : XML;			SID8 :		0 : SID_NMS;		1 - 7 : reserved;		8 : MacOS file flags		9 : UNIX file flags			SID16 :		0 : SID_NMS;		1 - 255 : reserved;	SID32 :		0 : SID_NMS;		1 - 65535 : reserved;		TEXT_ENC :		0 - 7 : reserved;		8 : UTF-8;		BCRC8 :		8-bit CRC of block data; 		BCRC16 :		16-bit CRC of block data; 	BCRC32 :		32-bit CRC of block data; 	FT_INTEGRITY_CHECK :				"CRC" of uncompressed data (does not include header data if FNC0_HEADER_NOT_ENCODED is set) 						*/	public static final short RAZIP_MAGIC = (short) 60611;	// For any flag_nc byte    static final int F_HAS_MORE_FLAG_NC	= 1<<0;	// For any flag byte    static final int F_HAS_MORE_FLAG_BYTES	= 1<<0;        // ------------------------------------------------------------------------------    // FLG_NC_0 (outside compressed header)    static final int FNC0_NON_STANDARD_COMPRESSION 	= 1<<1;	// non-standard compression used    static final int FNC0_ENCRYPTED					= 1<<2;	// Encryption was applied (after compression)    static final int FNC0_ECC						= 1<<3;	// Error correction code used (after encryption)    static final int FNC0_HEADER_NOT_ENCODED 		= 1<<4;	// Header is not encoded    static final int FNC0_NON_STANDARD_INTEGRITY_CHECK		= 1<<5;	// non-standard integrity check used    static final int FNC0_HEADER_CRC				= 1<<6;	// Header CRC is present    static final int FNC0_BLOCK_INTEGRITY_CHECK		= 1<<7;	// Each block has its own integrity check        // ------------------------------------------------------------------------------        // FLG_0 (inside compressed header)    static final int F0_EXTENDED_MTIME_INFO	= 1<<1;	// Extended modification time info    static final int F0_EXTENDED_CTIME_INFO	= 1<<2;	// Extended creation time info    static final int F0_TYPE_CREATOR		= 1<<3;	// MacOS type and creator info    static final int F0_NAME				= 1<<4;	// File name    static final int F0_COMMENT				= 1<<5;	// File comment    static final int F0_PROPERTIES			= 1<<6;	// Properties are present    static final int F0_RESFORK				= 1<<7;	// Resource fork after data fork    // ------------------------------------------------------------------------------    // FLG_1    static final int F1_EXTRA_FIELD_8				= 1<<1;	// Extra field using 8 bit LEN    static final int F1_EXTRA_FIELD_16				= 1<<2;	// Extra field using 16 bit LEN    static final int F1_EXTRA_FIELD_32				= 1<<3;	// Extra field using 32 bit LEN    static final int F1_NON_STANDARD_TEXT_ENCODING	= 1<<4;	// Strings are encoded in non-standard format    //static final int F1_BLOCK_MARKERS_AT_END		= 1<<5;	// Block markers at end, encoded	// bits 5 - 7: set to ZERO    // CIPHER_FLAGS0    static final int CF0_HAS_MORE_CF		= 1<<0;    static final int CF0_UNDISCLOSED		= 1<<1;	// paranoids use this    static final int CF0_USE_ASYMMETRIC_KEY	= 1<<2;	// Encypts a symmetric key with with an asymmetric key	// bits 3 - 7: set to ZERO    // CIPHER_FLAGS1 (symmetric cipher)    static final int CF1_SET_CIPHER		= 1<<0;	// S_CIPHER_ID is present    static final int CF1_SET_MODE		= 1<<1;	// CIPHER_MODE is present    static final int CF1_SET_KEY_SIZE	= 1<<2;	// KEY_SIZE is present    static final int CF1_SET_BLOCK_SIZE	= 1<<3;	// BLOCK_SIZE is present    static final int CF1_SET_ITERATIONS	= 1<<4;	// ITERATIONS is present    static final int CF1_SET_PADDING	= 1<<5;	// PADDING is present	// bits 6 - 7: set to ZERO    // CIPHER_FLAGS2 (asymmetric cipher)    static final int CF2_HAS_MORE_ACF	= 1<<0;    static final int CF2_SET_CIPHER		= 1<<1;	// A_CIPHER_ID is present    static final int CF2_SET_KEY_SIZE	= 1<<2;	// KEY_SIZE is present    static final int CF2_SET_PADDING	= 1<<3;	// PADDING is present	// bits 4 - 7: set to ZERO    // CM    static final byte CM_NONE 		=  0; // Data is not compressed    static final byte CM_DEFLATE 	=  8; // "deflate" algorithm used    static final byte CM_BZIP2 		=  9; // bzip2 algorithm used    static final byte CM_PPM 		= 10; // PPM algorithm used    static final byte CM_DEFAULT = CM_DEFLATE; // Default compression method					    // S_CIPHER_ID    // 0 - 7 : Reserved;    static final byte S_CIPHER_RIJNDAEL 	= 8;    static final byte S_CIPHER_RC4 			= 9;    static final byte S_CIPHER_RC5 			= 10;    static final byte S_CIPHER_MARS 		= 11;    static final byte S_CIPHER_SERPENT 		= 12;    static final byte S_CIPHER_TWOFISH 		= 13;    static final byte S_CIPHER_BLOWFISH 	= 14;    static final byte S_CIPHER_DEFAULT = S_CIPHER_RIJNDAEL; // Default cipher. Implies default cipher mode, padding, key size, block size and iterations;    // A_CIPHER_ID    // 0 - 7 : Reserved;    static final byte A_CIPHER_RSA	= 8;    static final byte A_CIPHER_DH	= 9; // Diffie-Helman    static final byte A_CIPHER_DEFAULT = A_CIPHER_RSA; // Default cipher. Implies default cipher mode, padding, key size, block size and iterations;    //CIPHER_MODE    // 0 : Reserved;    static final byte CIPHER_MODE_ECB 	= 1; // Electronic Code Book    static final byte CIPHER_MODE_CBC 	= 2; // Cipher Block Chaining    static final byte CIPHER_MODE_PCBC 	= 3; // error-Propagating Cipher Block Chaining    static final byte CIPHER_MODE_CFB 	= 4; // Cipher Feedback    static final byte CIPHER_MODE_OFB 	= 5; // Output Feedback    static final byte CIPHER_MODE_DEFAULT = CIPHER_MODE_CBC; // Default cipher mode	// PADDING    static final byte PADDING_NONE 				= 0;    static final byte PADDING_PKCS1 			= 1;    static final byte PADDING_PKCS5 			= 1; // block size = 8 bytes    static final byte PADDING_PKCS7 			= 2; // block size <= 255 bytes    static final byte PADDING_ONE_AND_ZEROES 	= 3; // any block size	    // ECC_ID    // 0 : reserved;    static final byte ECC_RS 		= 1; // Reed-Solomon    static final byte ECC_TPC 		= 2; // Turbo Product Code    //static final byte ECC_RSV 		= 3; // Reed-Solomon Viterbi    static final byte ECC_DEFAULT = ECC_RS; // Default error correction code        // INTEGRITY_CHECK_ID    // 0 : Reserved;    static final byte INTEGRITY_CHECK_ADLER	= 1;    static final byte INTEGRITY_CHECK_PRZ24	= 2; // used in Cryptix    static final byte INTEGRITY_CHECK_CRC32	= 3; // Used in zip files   	//static final byte INTEGRITY_CHECK_SHA1	= 4;        static final byte INTEGRITY_CHECK_DEFAULT = INTEGRITY_CHECK_PRZ24; // Default integrity check        static final short SID_NMS = 0; // there are no more subfields. LEN is not present;    static final byte PROP_END 			= 0; // there are no more properties;    static final byte PROP_ID_RECORD 	= 8; // record-like properties;    static final byte PROP_ID_XML 		= 9; // XML properties;    static final byte TEXT_ENC_UTF8		= 8; // text is encoded in UTF-8;    static final int MIN_BLOCK_TRAILER_SIZE	= 8;    static final int MIN_TRAILER_SIZE = 0;}