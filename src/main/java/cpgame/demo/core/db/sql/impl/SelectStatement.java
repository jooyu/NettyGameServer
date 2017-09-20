package cpgame.demo.core.db.sql.impl;

import cpgame.demo.core.db.sql.AbstractStatement;

/**
 * 查询sql语句实现
 * @author 
 *
 */
public class SelectStatement extends AbstractStatement {

	public SelectStatement() {
		super();
	}

	/**
	 * 获取列名（xxx,xxx,);
	 * @return
	 */
	private String getColumnNameWithComma(String[] allDbColumName) {
		StringBuffer colBuff = new StringBuffer();
		for (int i = 0; i < allDbColumName.length; i++) {
			String f = allDbColumName[i];
			colBuff.append("`" + f + "`");
			if (i != (allDbColumName.length - 1)) {
				colBuff.append(COMMA);
			}
		}
		return colBuff.toString();
	}

	/**
	 * 获取条件（xxx =?)
	 * @return
	 */
	private String getContionWithPlaceHolder(String[] keys) {
		StringBuffer sql = new StringBuffer();
		if (keys != null && keys.length > 0) {
			for (int i = 0; i < keys.length; i++) {
				sql.append(keys[i]);
				sql.append(EQUATE);
				sql.append(PLACEHOLDER);
				if (i != (keys.length - 1)) {
					sql.append(AND);
				}
			}
		}
		return sql.toString();
	}

	public String toSqlString(String pkName, String tableName, String[] allDbColumName) {
		return toSqlString(pkName, tableName, allDbColumName, null);
	}

	public String toSqlString(String pkName, String tableName, String[] allDbColumName, String[] keys) {
		return toSqlString(pkName, tableName, allDbColumName, null, -1, -1, keys);
	}
	
	public String toSqlString(String pkName, String tableName, String[] allDbColumName, String[] keys, String otherCondition) {
		return toSqlString(pkName, tableName, allDbColumName, null, -1, -1, keys, otherCondition);
	}

	public String toSqlString(String pkName, String tableName, String[] allDbColumName, String columnName, String[] keys) {
		return toSqlString(pkName, tableName, allDbColumName, columnName, -1, -1, keys);
	}
	
	public String toSqlString(String pkName, String tableName, String[] allDbColumName, String columnName, String[] keys, String otherCondition) {
		return toSqlString(pkName, tableName, allDbColumName, columnName, -1, -1, keys, otherCondition);
	}

	public String toSqlString(String pkName, String tableName, String[] allDbColumName, String columnName, int limitBegin, int limitEnd, String[] keys) {
		return toSqlString(pkName, tableName, allDbColumName, columnName, limitBegin, limitEnd, keys, null);
	}
	
	public String toSqlString(String pkName, String tableName, String[] allDbColumName, String columnName, int limitBegin, int limitEnd, String[] keys, String otherCondition) {
		String columnString = null;
		if (null != columnName) {
			columnString = columnName;
		} else {
			columnString = getColumnNameWithComma(allDbColumName);
		}

		StringBuffer sql = new StringBuffer();
		sql.append(SELECT);
		sql.append(columnString);
		sql.append(FROM);
		sql.append(QUOTES);
		sql.append( tableName );
		sql.append(QUOTES);
		if (keys != null && keys.length > 0) {
			sql.append(WHERE);
			sql.append(getContionWithPlaceHolder(keys));
			if (otherCondition != null && otherCondition.length() > 0) {
				sql.append(AND);
				sql.append(otherCondition);
			}
		} else if (otherCondition != null && otherCondition.length() > 0) {
			sql.append(WHERE);
			sql.append(otherCondition);
		}

		if (limitBegin > 0 && limitEnd > 0) {
			sql.append(LIMIT);
			sql.append(PLACEHOLDER);
			sql.append(COMMA);
			sql.append(PLACEHOLDER);
		} else if (limitBegin == 0 && limitEnd > 0) {
			sql.append(LIMIT);
			sql.append(PLACEHOLDER);
		}

		return sql.toString();
	}

	public String toSqlString(String pkName, String tableName, String[] allDbColumName, int limitBegin, int limitEnd, String[] condition) {
		return toSqlString(pkName, tableName, allDbColumName, null, limitBegin, limitEnd, condition);
	}

}
