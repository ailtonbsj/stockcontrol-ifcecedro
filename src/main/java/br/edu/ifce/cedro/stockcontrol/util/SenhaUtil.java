package br.edu.ifce.cedro.stockcontrol.util;

import org.mindrot.jbcrypt.BCrypt;

public final class SenhaUtil {

	private SenhaUtil() {
	}

	public static String criptografar(String senhaPlana) {
		return BCrypt.hashpw(senhaPlana, BCrypt.gensalt());
	}

	public static boolean verificar(String senhaPlana, String senhaHash) {
		if (senhaPlana == null || senhaHash == null || senhaHash.isEmpty()) {
			return false;
		}
		try {
			return BCrypt.checkpw(senhaPlana, senhaHash);
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
}
