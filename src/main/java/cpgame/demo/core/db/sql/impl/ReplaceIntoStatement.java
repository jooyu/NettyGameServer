package cpgame.demo.core.db.sql.impl;

import cpgame.demo.core.db.sql.AbstractStatement;

/**
 * Replace into sql语句实现
 * @author 
 *
 */
public class ReplaceIntoStatement extends AbstractStatement {
	protected static final String PARENTHESES_LEFT = " ( ";
	protected static final String PARENTHESES_RIGHT = " ) ";

	public ReplaceIntoStatement() {
		super();
	}

	/**
	 * 获取占位符(?,?,)
	 * @return
	 */
	private String getPlaceHolderWithComma(String[] allDbColumName) {
		StringBuffer sql = new StringBuffer();
		String[] fields = allDbColumName;
		for (int i = 0; i < fields.length; i++) {
			sql.append(PLACEHOLDER);
			if (i != (fields.length - 1)) {
				sql.append(COMMA);
			}
		}
		return sql.toString();
	}

	public String toSqlString(String pkName, String tableName, String[] columnName) {
		StringBuffer sql = new StringBuffer();
		sql.append(REPLACE_INTO);
		sql.append(QUOTES);
		sql.append(tableName);
		sql.append(QUOTES);
		sql.append(PARENTHESES_LEFT);
		sql.append(getColumStrWithComma(pkName, columnName));
		sql.append(PARENTHESES_RIGHT);
		sql.append(VALUES);
		sql.append(PARENTHESES_LEFT);
		sql.append(getPlaceHolderWithComma(columnName));
		sql.append(PARENTHESES_RIGHT);
		return sql.toString();
	}

}
