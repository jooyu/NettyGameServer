package cpgame.demo.core.db.sql;

/**
 * SQL语句基类
 * @author 
 *
 * @param <T>
 */
public abstract class AbstractStatement {
	protected static final String PLACEHOLDER = " ? ";
	protected static final String AND = " AND ";
	protected static final String COMMA = " ,";
	protected static final String EQUATE = " = ";
	protected static final String SELECT = "SELECT ";
	protected static final String FROM = " FROM ";
	protected static final String WHERE = " WHERE ";
	protected static final String LIMIT = " LIMIT ";
	protected static final String DELETE = "DELETE ";
	protected static final String UPDATE = "UPDATE ";
	protected static final String SET = " SET ";
	protected static final String INSERT_INTO = "INSERT INTO ";
	protected static final String VALUES = " VALUES ";
	protected static final String REPLACE_INTO = "REPLACE INTO ";
	protected static final String QUOTES="`";

	public AbstractStatement() {
	}

	/**
	 * 获取列名(xxx, xxx,)
	 * @return
	 */
	protected String getColumStrWithComma(String pkName, String[] allDbColumName) {
		StringBuffer columString = new StringBuffer();
		for (int i = 0; i < allDbColumName.length; i++) {
			String f = allDbColumName[i];
			columString.append(QUOTES).append(f).append(QUOTES);
			if (i != (allDbColumName.length - 1)) {
				columString.append(COMMA);
			}
		}
		return columString.toString();
	}

}
