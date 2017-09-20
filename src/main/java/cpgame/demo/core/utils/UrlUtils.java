package cpgame.demo.core.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UrlUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(UrlUtils.class);

	public static String encode(String s) {
		try {
			return URLEncoder.encode(s, "utf-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("{}", e);
		}
		return s;
	}
	
	public static String decode(String s) {
		try {
			return URLDecoder.decode(s, "utf-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("{}", e);
		}
		return s;
	}
}
