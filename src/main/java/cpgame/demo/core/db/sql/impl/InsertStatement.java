package cpgame.demo.core.db.sql.impl;

import cpgame.demo.core.db.sql.AbstractStatement;

/**
 * 插入sql语句实现
 * @author 
 *
 */
public class InsertStatement extends AbstractStatement {
	protected static final String PARENTHESES_LEFT = " ( ";
	protected static final String PARENTHESES_RIGHT = " ) ";

	public InsertStatement() {
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

	public String toSqlString(String pkName, String tableName, String[] allDbColumName) {
		StringBuffer sql = new StringBuffer();
		sql.append(INSERT_INTO);
		sql.append(QUOTES);
		sql.append(tableName);
		sql.append(QUOTES);
		sql.append(PARENTHESES_LEFT);
		sql.append(getColumStrWithComma(pkName, allDbColumName));
		sql.append(PARENTHESES_RIGHT);
		sql.append(VALUES);
		sql.append(PARENTHESES_LEFT);
		sql.append(getPlaceHolderWithComma(allDbColumName));
		sql.append(PARENTHESES_RIGHT);
		return sql.toString();
	}

}
