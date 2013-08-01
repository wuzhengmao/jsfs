package org.mingy.kernel.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mingy.kernel.config.ApplicationConfig;

/**
 * 用于字符串通用处理的应用类
 * 
 * @author Mingy
 * 
 */
public abstract class Strings {

	private static final Log logger = LogFactory.getLog(Strings.class);

	/**
	 * 默认的字符分割符，包含了：逗号，分号，空格，TAB，回车换行。
	 */
	public static final String DELIMITERS = ",; \t\n";

	/**
	 * 根据输入的种子串和长度创建一个字符串
	 * 
	 * @param seed
	 *            种子串
	 * @param length
	 *            字符串长度
	 * @return 创建好的字符串
	 */
	public static String createString(String seed, int length) {
		StringBuffer sb = new StringBuffer();
		while (sb.length() < length) {
			sb.append(seed);
		}
		if (sb.length() > length && length > 0) {
			return sb.substring(0, length);
		} else {
			return sb.toString();
		}
	}

	/**
	 * 将转义过的XML串恢复编码
	 * 
	 * @param str
	 *            转义过的XML字符串
	 * @return 恢复后的XML数据
	 * @see #Xml2Escape(String)
	 */
	public static String escape2Xml(String str) {
		if (str == null || str.length() <= 0) {
			return "";
		}
		str = str.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
				.replaceAll("&amp;", "&").replaceAll("&apos;", "'")
				.replaceAll("&quot;", "\"");
		for (int i = str.indexOf("<br>"); i >= 0; i = str.indexOf("<br>")) {
			str = str.substring(0, i) + "\n" + str.substring(i + 5);
		}
		return str;
	}

	/**
	 * 查找并替换配置文件列表中可能存在的系统变量<br>
	 * 优先从System中获取变量值，其次从ApplicationConfig中获取变量值。
	 * 
	 * @param src
	 *            待处理字符串
	 * @return 已经使用系统变量自动替换后的字符串
	 */
	public static String fillVariable(String src) {
		Collection<String> variables = Strings.getVariables(src);
		if (variables == null) {
			return src;
		}
		for (String variable : variables) {
			// 首先从System中查找
			String value = System.getProperty(variable, null);
			if (value == null) {
				// 其次从ApplicationConfig中查找
				value = ApplicationConfig.getInstance().getConfig(variable);
				if (value == null) {
					// 如果都查找不到，则使用变量名替换
					value = variable;
				}
			}
			value = value.replace('\\', '/');
			src = src.replaceAll("\\$\\{" + variable + "\\}", value);
		}
		return src;
	}

	/**
	 * 根据指定的类型名称，创建该类（基本类型可以转换为对应的类）。
	 * 
	 * @param type
	 *            类型名称
	 * @return 创建的类
	 * @throws ClassNotFoundException
	 */
	public static Class<?> forNameByObject(String type)
			throws ClassNotFoundException {
		if ("byte".equals(type)) {
			return Byte.class;
		} else if ("char".equals(type)) {
			return Character.class;
		} else if ("int".equals(type)) {
			return Integer.class;
		} else if ("boolean".equals(type)) {
			return Boolean.class;
		} else if ("short".equals(type)) {
			return Short.class;
		} else if ("float".equals(type)) {
			return Float.class;
		} else if ("double".equals(type)) {
			return Double.class;
		} else if ("long".equals(type)) {
			return Long.class;
		} else {
			return Class.forName(type);
		}
	}

	/**
	 * 根据指定的类型名称，创建该类（基本类型不被转换）。
	 * 
	 * @param type
	 *            类型名称
	 * @return 创建的类
	 * @throws ClassNotFoundException
	 */
	public static Class<?> forNameByPrimitive(String type)
			throws ClassNotFoundException {
		if ("byte".equals(type)) {
			return byte.class;
		} else if ("char".equals(type)) {
			return char.class;
		} else if ("int".equals(type)) {
			return int.class;
		} else if ("boolean".equals(type)) {
			return boolean.class;
		} else if ("short".equals(type)) {
			return short.class;
		} else if ("float".equals(type)) {
			return float.class;
		} else if ("double".equals(type)) {
			return double.class;
		} else if ("long".equals(type)) {
			return long.class;
		} else {
			return Class.forName(type);
		}
	}

	/**
	 * 获取一个固定长度的字符串，如果长度不够，则用空格按照自左向右填充顺序填充；如果长度超出，则截断之。
	 * 
	 * @param str
	 *            待处理的字符串
	 * @param len
	 *            固定长度
	 * @return 处理好的字符串
	 */
	public static String getFixLengthString(String str, int len) {
		return getFixLengthString(str, len, ' ');
	}

	/**
	 * 获取一个固定长度的字符串，如果长度不够，则用空格按照填充顺序填充；如果长度超出，则截断之。
	 * 
	 * @param str
	 *            待处理的字符串
	 * @param len
	 *            固定长度
	 * @param leftAlign
	 *            填充顺序，设置为true则自左向右填充，否则自右向左填充
	 * @return 处理好的字符串
	 */
	public static String getFixLengthString(String str, int len,
			boolean leftAlign) {
		return getFixLengthString(str, len, ' ', leftAlign);
	}

	/**
	 * 获取一个固定长度的字符串，如果长度不够，则用指定字符按照自左向右填充顺序填充；如果长度超出，则截断之。
	 * 
	 * @param str
	 *            待处理的字符串
	 * @param len
	 *            固定长度
	 * @param ch
	 *            填充字符
	 * @return 处理好的字符串
	 */
	public static String getFixLengthString(String str, int len, char ch) {
		return getFixLengthString(str, len, ch, true);
	}

	/**
	 * 获取一个固定长度的字符串，如果长度不够，则用指定字符按照填充顺序填充；如果长度超出，则截断之。
	 * 
	 * @param str
	 *            待处理的字符串
	 * @param len
	 *            固定长度
	 * @param ch
	 *            填充字符
	 * @param leftAlign
	 *            填充顺序，设置为true则自左向右填充，否则自右向左填充
	 * @return 处理好的字符串
	 */
	public static String getFixLengthString(String str, int len, char ch,
			boolean leftAlign) {
		if (str == null) {
			str = "";
		}
		if (str.length() > len) {
			return str.substring(0, len - 3) + "...";
		} else if (str.length() < len) {
			StringBuffer sb = new StringBuffer(str);
			for (int i = str.length(); i < len; i++) {
				if (leftAlign) {
					sb.append(ch);
				} else {
					sb.insert(0, ch);
				}
			}
			return sb.toString();
		}
		return str;
	}

	/**
	 * 从一个字符串中找出包含的变量定义，一个变量定义为${variable}，例如：${user.name}
	 * 定义了一个变量，变量名为“user.name”。
	 * 
	 * @param src
	 *            字符串
	 * @return 输入字符串中包含的变量列表，相同变量名已经被排除（只保留一个）。
	 */
	public static Collection<String> getVariables(String src) {
		if (src == null || src.length() <= 0) {
			return null;
		}
		Map<String, String> variable = new HashMap<String, String>();
		int start = 0;
		while ((start = src.indexOf("${", start)) != -1) {
			int end = src.indexOf("}", start);
			if (end != -1) {
				String v = src.substring(start + 2, end);
				variable.put(v, v);
			}
			start += 2;
		}
		return variable.values();
	}

	/**
	 * 判断输入的字符串是否为空，可能是：null，或者字符串长度为0。
	 * 
	 * @param str
	 *            待判断的字符串
	 * @return true表示为空，否则表示为非空。
	 */
	public static boolean isBlank(String str) {
		if (str == null) {
			return true;
		} else {
			str = str.trim();
			if (str.length() <= 0) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * 组合字符串列表成为一个字符串
	 * 
	 * @param str
	 *            字符串列表
	 * @return 组合后的字符串
	 */
	public static String merge(List<String> str) {
		return merge(str, ',');
	}

	/**
	 * 组合字符串列表成为一个字符串
	 * 
	 * @param str
	 *            字符串列表
	 * @param seperate
	 *            组合分割符
	 * @return 组合后的字符串
	 */
	public static String merge(List<String> str, char seperate) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.size(); i++) {
			sb.append(str.get(i));
			if (i < str.size() - 1) {
				sb.append(seperate);
			}
		}
		return sb.toString();
	}

	/**
	 * 组合字符串列表成为一个字符串
	 * 
	 * @param str
	 *            字符串列表
	 * @param seperate
	 *            组合分割符
	 * @return 组合后的字符串
	 */
	public static String merge(List<String> str, String seperate) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.size(); i++) {
			sb.append(str.get(i));
			if (i < str.size() - 1) {
				sb.append(seperate);
			}
		}
		return sb.toString();
	}

	/**
	 * 组合字符串数组成为一个字符串，使用逗号作为分割符。
	 * 
	 * @param str
	 *            字符串数组
	 * @return 组合后的字符串
	 * @see #merge(String[])
	 */
	public static String merge(String[] str) {
		return merge(str, ',');
	}

	/**
	 * 组合字符串数组成为一个字符串
	 * 
	 * @param str
	 *            字符串数组
	 * @param seperate
	 *            组合分割符
	 * @return 组合后的字符串
	 */
	public static String merge(String[] str, char seperate) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length; i++) {
			sb.append(str[i].toString());
			if (i < str.length - 1) {
				sb.append(seperate);
			}
		}
		return sb.toString();
	}

	/**
	 * 组合字符串数组成为一个字符串
	 * 
	 * @param str
	 *            字符串数组
	 * @param seperate
	 *            组合分割符
	 * @return 组合后的字符串
	 */
	public static String merge(String[] str, String seperate) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length; i++) {
			sb.append(str[i]);
			if (i < str.length - 1) {
				sb.append(seperate);
			}
		}
		return sb.toString();
	}

	/**
	 * 密码强度检测，使用：密码长度（是否大于6位）、是否包含数字、小写、大写或者符号等字符来进行检测。
	 * 
	 * @param password
	 *            待检测的密码串
	 * @return 密码强度值，0为最弱，5为最强。
	 */
	public static int passwordStrengthen(String password) {
		int strengthen = 0;
		if (Strings.isBlank(password)) {
			return strengthen;
		}
		if (password.length() >= 6) {
			strengthen++;
		}
		if (password.matches(".*[\\d]+.*")) {
			strengthen++;
		}
		if (password.matches(".*[a-z]+.*")) {
			strengthen++;
		}
		if (password.matches(".*[A-Z]+.*")) {
			strengthen++;
		}
		if (password.matches(".*[^\\da-zA-Z]+.*")) {
			strengthen++;
		}
		return strengthen;
	}

	/**
	 * 使用默认的分割符对输入的字符进行分割，同时对分割的项去除空字符和忽略空字符
	 * 
	 * @param s
	 *            待分割的字符串
	 * @return 分割后的字符数组
	 * @see #split(String, boolean, boolean)
	 * @see #DELIMITERS
	 */
	public static String[] split(String s) {
		return split(s, DELIMITERS, true, true);
	}

	/**
	 * 使用默认的分割符对输入的字符进行分割
	 * 
	 * @param s
	 *            待分割的字符串
	 * @param trimTokens
	 *            对分割的项去除空字符
	 * @param ignoreEmptyTokens
	 *            忽略空字符
	 * @return 分割后的字符数组
	 * @see #split(String, String, boolean, boolean)
	 * @see #DELIMITERS
	 */
	public static String[] split(String s, boolean trimTokens,
			boolean ignoreEmptyTokens) {
		return split(s, DELIMITERS, trimTokens, ignoreEmptyTokens);
	}

	/**
	 * 使用特定的分割符对输入的字符串进行分割
	 * 
	 * @param s
	 *            待分割的字符串
	 * @param delimiters
	 *            分割符（可以同时是多个分割符组合）
	 * @param trimTokens
	 *            对分割的项去除空字符
	 * @param ignoreEmptyTokens
	 *            忽略空字符
	 * @return 分割后的字符数组
	 */
	public static String[] split(String s, String delimiters,
			boolean trimTokens, boolean ignoreEmptyTokens) {
		if (s == null) {
			s = "";
		}
		StringTokenizer st = new StringTokenizer(s, delimiters);
		List<String> tokens = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (logger.isDebugEnabled()) {
				logger.debug(token);
			}
			if (trimTokens) {
				token = token.trim();
			}
			if (!(ignoreEmptyTokens && token.length() == 0)) {
				tokens.add(token);
			}
		}
		return tokens.toArray(new String[tokens.size()]);
	}

	/**
	 * 将XML串进行转义编码
	 * 
	 * @param str
	 *            XML串
	 * @return 转义编码后的串
	 * @see #Escape2Xml(String)
	 */
	public static String xml2Escape(String str) {
		if (str == null || str.length() <= 0) {
			return "";
		}
		for (int i = str.indexOf('\n'); i >= 0; i = str.indexOf('\n')) {
			str = str.substring(0, i) + "<br>" + str.substring(i + 1);
		}
		return str.replaceAll("&", "&amp;").replaceAll(">", "&gt;")
				.replaceAll("<", "&lt;").replaceAll("'", "&apos;")
				.replaceAll("\"", "&quot;");
	}

	/**
	 * 将Object转换为字符串，如果为null则返回空字符串
	 * 
	 * @param obj
	 *            要转换字符串的对象
	 * @return 字符串
	 */
	public static String objToString(Object obj) {
		if (obj == null) {
			return "";
		}
		return obj.toString();
	}

	/**
	 * 判断字符串是否为汉字 只要出现汉字 就返回true
	 * 
	 * @param str判断字符串
	 * @return 是否为汉字
	 */
	public static boolean isChinese(String str) {
		boolean flg = false;
		if (!Strings.isBlank(str)) {
			char[] ch = str.toCharArray();
			for (int i = 0; i < ch.length; i++) {
				char c = ch[i];
				if (isChinese(c)) {
					flg = true;
					break;
				}
			}
		}
		return flg;
	}

	/**
	 * 判断单个字符是否为汉字
	 * 
	 * @param c判断字符
	 * @return 是否为汉字
	 */
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A) {
			return true;
		}
		return false;
	}

	/**
	 * 调整字串
	 * 
	 * @param original
	 *            判断的字符串,必须为221px或者221格式
	 * @param operation
	 *            操作运算符号，只能为：加法+、减法-、乘法*、除法/、求余%
	 * @param compareDigit
	 *            相减或者相加的数字
	 * @return 转换后的字符串 eg:original=221PX 结果可能为以下多种的一个:(221+compareDigit)px
	 *         (221-compareDigit)px (221*compareDigit)px ....
	 */
	public static String adjustString(String original, String operation,
			int compareDigit) {
		String target = original;
		Double doubleHeight = 0.0;
		if (null == original) {
		} else {
			if (original.toLowerCase().matches("^\\d+(px){0,1}$")) {
				if (original.toLowerCase().contains("px")) {
					int index = original.lastIndexOf("px");
					String originalTemp = original.substring(0, index);
					doubleHeight = Double.parseDouble(originalTemp);
				} else {
					doubleHeight = Double.parseDouble(original);
				}
				if ("+".equals(operation)) {
					doubleHeight = doubleHeight + compareDigit;
				} else if ("-".equals(operation)) {
					doubleHeight = doubleHeight - compareDigit;
				} else if ("*".equals(operation)) {
					doubleHeight = doubleHeight * compareDigit;
				} else if ("/".equals(operation)) {
					doubleHeight = doubleHeight / compareDigit;
				} else if ("%".equals(operation)) {
					doubleHeight = doubleHeight % compareDigit;
				}
				String strHeight = (doubleHeight + "").split("\\.")[0];
				target = strHeight + "px";
			}
		}
		return target;
	}

	/**
	 * 在一个字符串的指定位置插入一个字符串 如：在"11113333"中插入"2222"后想得到"111122223333"
	 * 应该insertString("11113333", "2222", 4)
	 * 
	 * @param original
	 *            原字符串
	 * @param insertStr
	 *            待插入字符串
	 * @param posIndex
	 *            插入位置
	 * @return 插入后的字符串
	 */
	public static String insertString(String original, String insertStr,
			int posIndex) {
		if (isBlank(original) || isBlank(insertStr)) {
			return original;
		}
		String firstHalf = original.substring(0, posIndex);
		String secondHalf = original.substring(posIndex);
		return firstHalf + insertStr + secondHalf;
	}
	
	public static Boolean parseBoolean(String text) {
		try {
			return Boolean.parseBoolean(text);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Integer parseInt(String text) {
		try {
			return Integer.parseInt(text);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Long parseLong(String text) {
		try {
			return Long.parseLong(text);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Float parseFloat(String text) {
		try {
			return Float.parseFloat(text);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Double parseDouble(String text) {
		try {
			return Double.parseDouble(text);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String format(Double value, int digit) {
		if (value == null)
			return "";
		String s = "#,##0";
		for (int i = 0; i < digit; i++) {
			s+= i > 0? "0" : ".0";
		}
		return new DecimalFormat(s).format(value);
	}
}
