package cpgame.demo.core.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 字符串工具类
 *
 * @author 0x737263
 */
public class StringUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(StringUtils.class);



	public static boolean isBlank(String str) {
		if (str == null || "".equals(str.trim())) {
			return true;
		}
		return false;
	}

	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	/**
	 * 截取字符的长度
	 * @param str		原字符
	 * @param length	要求长度(不足以str最长为准)
	 * @return
	 */
	public static String subString(String str, int length) {
		if (isBlank(str)) {
			return "";
		}
		return str.substring(0, Math.min(str.length(), length));
	}

	/**
	 * byte[] 转 String
	 *
	 * @param stream
	 * @return
	 */
	public static String inputStream2String(InputStream stream) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		String returnString = "";
		try {
			while ((length = stream.read(buffer)) != -1) {
				bos.write(buffer, 0, length);
			}
			bos.close();
			stream.close();

			returnString = bos.toString().trim();
		} catch (Exception ex) {
			LOGGER.error("{}", ex);
		}

		return returnString;
	}

	/**
	 * 数组转字符串
	 *
	 * @param arr
	 * @return
	 */
	public static String array2String(Object[] arr) {
		StringBuffer sb = new StringBuffer();
		for (Object object : arr) {
			sb.append(object.toString());
			sb.append(",");
		}
		if (sb.length() > 1) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	/**
	 * 填充指定数组到指定长度
	 *
	 * @param src     源数组
	 * @param len     指定长度
	 * @param content 填充内容
	 * @return 生成的新数组 如果长度小于源数组长度，返回源数组
	 */
	public static String[] fillStringArray(String[] src, int len, String content) {
		if (src == null || src.length >= len) {
			return src;
		}

		if (content == null) {
			content = "";
		}
		String[] data;
		data = new String[len];
		for (int i = 0; i < len; i++) {
			if (i >= src.length) {
				data[i] = content;
			} else {
				if (src[i].isEmpty()) {
					src[i] = content;
				}
				data[i] = src[i];
			}
		}
		return data;
	}

	public static String[] split(String src, String splitable) {
		if (isBlank(src) || isBlank(splitable)) {
			return null;
		}

		String[] items = src.split(splitable);
		String[] result = new String[items.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = String.valueOf(items[i]);
		}
		return result;
	}



	/**
	 * 用大括号"{}"为占位符，填充字符串
	 * @param str
	 * @param args
	 * @return
	 */
	public static String format(String str, Object... args) {
		for (Object arg : args) {
			str = str.replaceFirst("\\{\\}", arg.toString());
		}
		return str;
	}

	/**
	 * 控制台格式输出
	 * @param str
	 * @param args
	 */
	public static void print(String str, Object... args) {
		System.out.println(format(str, args));
	}
	
	/**
	 * 首字母小写
	 * @param str
	 * @return
	 */
	public static String firstCharLowerCase(String str) {
		if (str == null || str.length() < 1) {
			return "";
		}
		char[] chars = new char[1];
		chars[0] = str.charAt(0);
		String temp = new String(chars);
		String temp2 = temp.toLowerCase();
		return str.replaceFirst(temp, temp2);
	}
	
	/**
	 * 首字母大写
	 * @param str
	 * @return
	 */
	public static String firstCharUpperCase(String str) {
		if (str == null || str.length() < 1) {
			return "";
		}
		char[] chars = new char[1];
		chars[0] = str.charAt(0);
		String temp = new String(chars);
		String temp2 = temp.toUpperCase();
		return str.replaceFirst(temp, temp2);
	}
}
