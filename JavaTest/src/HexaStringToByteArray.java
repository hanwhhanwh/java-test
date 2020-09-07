/**
 * 
 */

/**
 * example : hexa-string to byte array.
 * @author hbesthee@naver.com
 * @date 2020-09-07
 */
public class HexaStringToByteArray {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String hexaString = "0000e04fd020ea3a6910a2d808002b30309de04fd020ea3a6910a2d808002b30309de04fd020ea3a6910a2d808002b30309d";

		System.out.printf("\n== use BigInteger class ==\n");
		byte[] bigIntegerData = new java.math.BigInteger(hexaString, 16).toByteArray();
		System.out.printf("original HexaString    = %s\n", hexaString);
		System.out.printf("BigInteger HexaString  = %s\n", new java.math.BigInteger(1, bigIntegerData).toString(16));
		System.out.printf("BigInteger Data(0 ~ 3) = %02x %02x %02x %02x\n", bigIntegerData[0], bigIntegerData[1], bigIntegerData[2], bigIntegerData[3]);
		
		System.out.printf("\n== use DatatypeConverter class ==\n");
		byte[] convertedData = javax.xml.bind.DatatypeConverter.parseHexBinary(hexaString);
		System.out.printf("original HexaString   = %s\n", hexaString);
		System.out.printf("converted HexaString  = %s\n", javax.xml.bind.DatatypeConverter.printHexBinary(convertedData));
		System.out.printf("converted Data(0 ~ 3) = %02x %02x %02x %02x\n", convertedData[0], convertedData[1], convertedData[2], convertedData[3]);

//		byte[] notEvenLengthData = javax.xml.bind.DatatypeConverter.parseHexBinary("123");
		/* Exception in thread "main" java.lang.IllegalArgumentException: hexBinary needs to be even-length: 123
			at javax.xml.bind.DatatypeConverterImpl.parseHexBinary(DatatypeConverterImpl.java:442)
			at javax.xml.bind.DatatypeConverter.parseHexBinary(DatatypeConverter.java:357)
		*/

//		byte[] illegalCharacterData = javax.xml.bind.DatatypeConverter.parseHexBinary("12 34 56");
		/* Exception in thread "main" java.lang.IllegalArgumentException: contains illegal character for hexBinary: 12 34 56
			at javax.xml.bind.DatatypeConverterImpl.parseHexBinary(DatatypeConverterImpl.java:451)
			at javax.xml.bind.DatatypeConverter.parseHexBinary(DatatypeConverter.java:357)
		*/
	}

}
