package tr.edu.yeditepe.intrusiondetection.application.service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public interface PasswordService {

	byte[] getNextSalt();

	byte[] hash(char[] password, byte[] salt);

	boolean isExpectedPassword(char[] password, byte[] salt, byte[] expectedHash);

	String generateRandomPassword(int length);

	SecretKeySpec keySpecCreator(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException;

}
