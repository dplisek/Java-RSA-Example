package org.plech.RSA;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Dominik Plisek <dominik.plisek@plech.org>
 */
public class RSA {

    public static void main(String[] args) {
        if (args.length < 5) {
			printUsage();
			return;
		}
		BigInteger p = new BigInteger(args[1]);
		BigInteger q = new BigInteger(args[2]);
		BigInteger e = new BigInteger(args[3]);
		switch (args[0]) {
			case "encrypt":
				encrypt(p, q, e, args[4]);
				break;
			case "decrypt":
				List<Integer> st = new ArrayList<>();
				for (int i = 4; i < args.length; i++) {
					st.add(Integer.parseInt(args[i]));
				}
				decrypt(p, q, e, st);
				break;
			case "decryptCRT":
				st = new ArrayList<>();
				for (int i = 4; i < args.length; i++) {
					st.add(Integer.parseInt(args[i]));
				}
				decryptCRT(p, q, e, st);
				break;
			default:
				printUsage();
		}
    }

	private static void printUsage() {
		System.err.println("Usage: <action> <p> <q> <e> <OT>");
		System.err.println("Action: encrypt/decrypt/decryptCRT");
	}

	private static void encrypt(BigInteger p, BigInteger q, BigInteger e, String ot) {
		BigInteger n = p.multiply(q);
		for (int i = 0; i < ot.length(); i++) {
			BigInteger m = new BigInteger("" + (int) ot.charAt(i));
			System.out.print(m.modPow(e, n) + " ");
		}
		System.out.println();
	}

	private static void decrypt(BigInteger p, BigInteger q, BigInteger e, List<Integer> st) {
		BigInteger n = p.multiply(q);
		BigInteger phiN = p.subtract(BigInteger.ONE).multiply((q.subtract(BigInteger.ONE)));
		int d = e.modInverse(phiN).intValue();
		long t1 = new Date().getTime();
		for (int c : st) {
			char m = (char) new BigInteger("" + c).pow(d).mod(n).intValue();
			System.out.print(m);
		}
		long t2 = new Date().getTime();
		System.out.println();
		System.out.println(t2 - t1);
	}

	private static void decryptCRT(BigInteger p, BigInteger q, BigInteger e, List<Integer> st) {
		
		BigInteger phiN = p.subtract(BigInteger.ONE).multiply((q.subtract(BigInteger.ONE)));
		BigInteger d = e.modInverse(phiN);
		BigInteger dp = d.mod(p.subtract(BigInteger.ONE));
		BigInteger dq = d.mod(q.subtract(BigInteger.ONE));
		BigInteger qInv = q.modInverse(p);
		long t1 = new Date().getTime();
		for (int c : st) {
			BigInteger m1 = new BigInteger("" + c).modPow(dp, p);
			BigInteger m2 = new BigInteger("" + c).modPow(dq, q);
			BigInteger h = qInv.multiply(m1.subtract(m2)).mod(p);
			char m = (char) m2.add(h.multiply(q)).intValue();
			System.out.print(m);
		}
		long t2 = new Date().getTime();
		System.out.println();
		System.out.println(t2 - t1);
	}

}
