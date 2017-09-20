package cpgame.demo.core.utils;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.owtelse.codec.Base64;

public class SecurityUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityUtils.class);
	private static final String SHA1_MAC_NAME = "HmacSHA1";
	private static final String SHA256_MAC_NAME = "HmacSHA256";
	private static final String ENCODING_CODE = "UTF-8";

	/**
	 * md5加密
	 * @param src
	 * @return
	 */
	public static String md5(String src) {
		try {
			MessageDigest alg = MessageDigest.getInstance("MD5");
			byte[] md5Bytes = alg.digest(src.getBytes(ENCODING_CODE));
			return byte2Hex(md5Bytes);
		} catch (Exception ex) {
			LOGGER.error(src, ex);
		}
		return "";
	}

	/**
	 * 使用 HMAC - SHA1 签名方法对对 encryptText 进行签名
	 * @param encryptText
	 * @param encryptKey
	 * @return
	 */
	public static String hmacSHA1Encrypt(String encryptText, String encryptKey) {
		return macEncrypt(SHA1_MAC_NAME, encryptText, encryptKey);
	}

	/**
	 * 使用 SHA-256 签名方法对对 encryptText 进行签名
	 * @param encryptText
	 * @param encryptKey
	 * @return
	 */
	public static String hmacSHA256Encrypt(String encryptText, String encryptKey) {
		return macEncrypt(SHA256_MAC_NAME, encryptText, encryptKey);
	}

	/**
	 * 使用指定指定 Mac 算法进行签名
	 * @param algorithm
	 * @param encryptText
	 * @param encryptKey
	 * @return
	 */
	public static String macEncrypt(String algorithm, String encryptText, String encryptKey) {
		try {
			byte[] data = encryptKey.getBytes(ENCODING_CODE);
			// 根据给定的字节数组构造一个密钥 , 第二参数指定一个密钥算法的名称
			SecretKey secretKey = new SecretKeySpec(data, algorithm);
			// 生成一个指定 Mac 算法 的 Mac 对象
			Mac mac = Mac.getInstance(algorithm);
			// 用给定密钥初始化 Mac 对象
			mac.init(secretKey);
			byte[] text = encryptText.getBytes(ENCODING_CODE);
			// 完成 Mac 操作
			byte[] digest = mac.doFinal(text);
			return byte2Hex(digest).toLowerCase();
		} catch (Exception ex) {
			LOGGER.warn(String.format("encryptText:[%s] encryptKey:[%s]", encryptText, encryptKey), ex);
		}
		return "";
	}

	public static String hmacSHA1Encrypt(byte[] encryptData, String encryptKey) {
		try {
			byte[] data = encryptKey.getBytes(ENCODING_CODE);
			// 根据给定的字节数组构造一个密钥 , 第二参数指定一个密钥算法的名称
			SecretKey secretKey = new SecretKeySpec(data, SHA1_MAC_NAME);
			// 生成一个指定 Mac 算法 的 Mac 对象
			Mac mac = Mac.getInstance(SHA1_MAC_NAME);
			// 用给定密钥初始化 Mac 对象
			mac.init(secretKey);
			// 完成 Mac 操作
			byte[] digest = mac.doFinal(encryptData);
			return byte2Hex(digest).toLowerCase();
		} catch (Exception ex) {
			LOGGER.warn(String.format("encryptText:[%s] encryptKey:[%s]", byte2Hex(encryptData), encryptKey), ex);
		}
		return "";
	}

	public static String byte2Hex(byte[] bytes) {
		StringBuilder builder = new StringBuilder();
		String stmp = "";
		for (int n = 0; (bytes != null) && (n < bytes.length); ++n) {
			stmp = Integer.toHexString(bytes[n] & 0xFF);
			if (stmp.length() == 1)
				builder.append("0").append(stmp);
			else {
				builder.append(stmp);
			}
		}
		return builder.toString().toLowerCase();
	}

	// AES加密
	public static String aesEncrypt(String input, String key) {
		byte[] crypted = null;
		try {
			SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skey);
			crypted = cipher.doFinal(input.getBytes());
			return new String(Base64.encode(crypted));
		} catch (Exception ex) {
			LOGGER.warn(String.format("encryptText:[%s] encryptKey:[%s]", input, key), ex);
		}
		return "";
	}

	// AES解密
	public static String aesDecrypt(String input, String key) {
		byte[] output = null;
		try {
			SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skey);
			output = cipher.doFinal(Base64.decode(input));
			return new String(output);
		} catch (Exception ex) {
			LOGGER.warn(String.format("decryptText:[%s] decryptKey:[%s]", input, key), ex);
		}
		return "";
	}
}
