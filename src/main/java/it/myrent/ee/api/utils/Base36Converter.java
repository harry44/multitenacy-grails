/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.myrent.ee.api.utils;

/**
 *
 * @author shivangani
 */
public class Base36Converter {
    public static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyz";

	public static final int BASE = ALPHABET.length();

	private Base36Converter() {}

	public static String fromBase10(int i) {
		StringBuilder sb = new StringBuilder("");
		if (i == 0) {
			return "0";
		}
		while (i > 0) {
			i = fromBase10(i, sb);
		}
		return sb.reverse().toString();
	}

	private static int fromBase10(int i, final StringBuilder sb) {
		int rem = i % BASE;
		sb.append(ALPHABET.charAt(rem));
		return i / BASE;
	}

	public static int toBase10(String str) {
		return toBase10(new StringBuilder(str).reverse().toString().toCharArray());
	}

	private static int toBase10(char[] chars) {
		int n = 0;
		for (int i = chars.length - 1; i >= 0; i--) {
			n += toBase10(ALPHABET.indexOf(chars[i]), i);
		}
		return n;
	}

	private static int toBase10(int n, int pow) {
		return n * (int) Math.pow(BASE, pow);
	}
        
        public static String next (String str36) {
            int int10 = Base36Converter.toBase10(str36) + 1;
                str36 = Base36Converter.fromBase10(int10);
                if (str36.length() < 5) {
                    int lunghezzaProgressivo36 = str36.length();
                    str36 = new StringBuilder(str36).reverse().toString();
                    for (int j = lunghezzaProgressivo36; j < 5; j++) {
                        str36 = str36 + "0";
                    }
                    str36 = new StringBuilder(str36).reverse().toString();
                }
                return str36;
        }
        
        public static String last (String str36) {
            int int10 = Base36Converter.toBase10(str36) - 1;
                str36 = Base36Converter.fromBase10(int10);
                if (str36.length() < 5) {
                    int lunghezzaProgressivo36 = str36.length();
                    str36 = new StringBuilder(str36).reverse().toString();
                    for (int j = lunghezzaProgressivo36; j < 5; j++) {
                        str36 = str36 + "0";
                    }
                    str36 = new StringBuilder(str36).reverse().toString();
                }
                return str36;
        }
    
}
