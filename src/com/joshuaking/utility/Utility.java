package com.joshuaking.utility;

public class Utility {

	public static int[] convertByteArrayToInt(byte buf[]) {
		   int intArr[] = new int[buf.length / 4];
		   int offset = 0;
		   // 8 = RED
		   // 16 = BLUE
		   for(int i = 0; i < intArr.length; i++) {
		      intArr[i] = (buf[3 + offset] & 0xFF)  << 16 | ((buf[2 + offset] & 0xFF) << 8) |
		                  ((buf[1 + offset] & 0xFF) <<0) | ((buf[0 + offset] & 0xFF) << 24 );  
		   offset += 4;
		   }
		   return intArr;
		}
}
