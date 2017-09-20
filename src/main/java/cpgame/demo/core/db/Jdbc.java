package cpgame.demo.core.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.alibaba.druid.pool.DruidDataSource;

import cpgame.demo.core.db.sql.impl.DeleteStatement;
import cpgame.demo.core.db.sql.impl.InsertStatement;
import cpgame.demo.core.db.sql.impl.ReplaceIntoStatement;
import cpgame.demo.core.db.sql.impl.SelectStatement;
import cpgame.demo.core.db.sql.impl.UpdateStatement;
import cpgame.demo.core.db.table.SuperTable;
import cpgame.demo.core.db.table.Table;
import cpgame.demo.core.db.table.TableInfo;

/**
 * jdbc封装类
 * 
 * @author 0x737263
 *
 */
public class Jdbc extends JdbcTemplate {

	private String driverClassName = "com.mysql.jdbc.Driver";
	private int initialSize = 20;
	private int maxActive = 20;
	private int maxIdle = 10;
	private int minIdle = 10;
	private int timeBetweenEvictionRunsMillis = 90000;
	private boolean testOnBorrow = false;
	private boolean testWhileIdle = true;
	private String validationQuery = "select 4";
	private boolean useUnicode = true;
	private String characterEncoding = "UTF-8";
	private boolean autoReconnect = true;

	// ----------------------------------------------------------------------
	SelectStatement selectSql = new SelectStatement();
	DeleteStatement delSql = new DeleteStatement();
	InsertStatement insertSql = new InsertStatement();
	ReplaceIntoStatement replaceSql = new ReplaceIntoStatement();
	UpdateStatement updateSql = new UpdateStatement();
	// ----------------------------------------------------------------------

	// ----------------------------------------------------------------------
	private String host = "";
	private String username = "";
	private String password = "";
	private String dbName = "";
	private List<String> tableList = new ArrayList<>();
	// ----------------------------------------------------------------------

	@SuppressWarnings("deprecation")
	public Jdbc(String host, String userName, String password, String dbName, String dbTable) {
		this.host = host;
		this.username = userName;
		this.password = password;
		this.dbName = dbName;

		for (String table : dbTable.split(",")) {
			this.tableList.add(table.toLowerCase());
		}

		DruidDataSource ds = new DruidDataSource();
		ds.setDriverClassName(this.driverClassName);
		ds.setInitialSize(this.initialSize);
		ds.setMaxActive(this.maxActive);
		ds.setMaxIdle(this.maxIdle);
		ds.setMinIdle(this.minIdle);
		ds.setTimeBetweenEvictionRunsMillis(this.timeBetweenEvictionRunsMillis);
		ds.setTestOnBorrow(this.testOnBorrow);
		ds.setTestWhileIdle(this.testWhileIdle);
		ds.setValidationQuery(this.validationQuery);
		ds.setUsername(this.username);
		ds.setPassword(this.password);
		String url = "jdbc:mysql://%s/%s?useUnicode=%s&amp;characterEncoding=%s&amp;autoReconnect=%s";
		ds.setUrl(String.format(url, this.host, this.dbName, this.useUnicode, this.characterEncoding, this.autoReconnect));
		super.setDataSource(ds);
		super.afterPropertiesSet();
	}

	public List<String> tableList() {
		return tableList;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public int getInitialSize() {
		return initialSize;
	}

	public void setInitialSize(int initialSize) {
		this.initialSize = initialSize;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public int getTimeBetweenEvictionRunsMillis() {
		return timeBetweenEvictionRunsMillis;
	}

	public void setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}

	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public boolean isTestWhileIdle() {
		return testWhileIdle;
	}

	public void setTestWhileIdle(boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}

	public String getValidationQuery() {
		return validationQuery;
	}

	public void setValidationQuery(String validationQuery) {
		this.validationQuery = validationQuery;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public boolean isUseUnicode() {
		return useUnicode;
	}

	public void setUseUnicode(boolean useUnicode) {
		this.useUnicode = useUnicode;
	}

	public String getCharacterEncoding() {
		return characterEncoding;
	}

	public void setCharacterEncoding(String characterEncoding) {
		this.characterEncoding = characterEncoding;
	}

	public boolean isAutoReconnect() {
		return autoReconnect;
	}

	public void setAutoReconnect(boolean autoReconnect) {
		this.autoReconnect = autoReconnect;
	}

	// -------------------------------------------------------------------------------------

	public <T extends Table> int update(T entity) {
		TableInfo info = entity.getTableInfo();
		String sql = replaceSql.toSqlString(info.pkName, entity.tableName(), info.buildDbColumns());
		Object[] values = entity.writeData();

		int update = super.update(sql, values);
		return update;
	}

	public <T extends Table> int update(T entity, String tableSuffix) {
		TableInfo info = entity.getTableInfo();
		String sql = replaceSql.toSqlString(info.pkName, entity.tableName() + tableSuffix, info.buildDbColumns());
		Object[] values = entity.writeData();
		int update = super.update(sql, values);
		return update;
	}

	public <T extends Table> int[] update(Collection<T> entitys) {
		Map<String, List<T>> groupMaps = groupEntity(entitys);
		List<Integer> executeResultList = new ArrayList<>();
		for (String str : groupMaps.keySet()) {
			// 获取同组的sql
			T param = groupMaps.get(str).get(0);
			TableInfo info = param.getTableInfo();

			String updateSql = replaceSql.toSqlString(info.pkName, info.tableName, info.buildDbColumns());
			ArrayList<Object[]> arr = new ArrayList<>();
			for (T object : groupMaps.get(str)) {
				Object[] oneObjectValue = object.writeData();
				arr.add(oneObjectValue);
			}

			int[] result = super.batchUpdate(updateSql, arr);
			for (int i : result) {
				executeResultList.add(i);
			}
		}
		int[] rs = new int[executeResultList.size()];
		for (int i : rs) {
			rs[i] = executeResultList.get(i);
		}
		return rs;
	}

	public <T extends Table> int delete(Class<T> clazz, LinkedHashMap<String, Object> condition) {
		TableInfo info = SuperTable.getTableInfo(clazz);

		String[] key = new String[condition.keySet().size()];
		String sql = delSql.toSqlString(info.pkName, info.tableName, condition.keySet().toArray(key));
		return super.update(sql, condition.values().toArray());
	}

	/**
	 * 获取实体
	 * 
	 * @param clazz
	 * @param pk
	 * @return
	 */
	public <T extends Table> T get(Class<T> clazz, Object pk) {
		TableInfo info = SuperTable.getTableInfo(clazz);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put(info.pkName, pk);
		return getFirst(clazz, map);
	}

	/**
	 * 获取首行记录
	 * 
	 * @param clazz
	 *            查询实体类
	 * @param params
	 *            查询条件 key:字段名 value:查询值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Table> T getFirst(Class<T> clazz, LinkedHashMap<String, Object> condition) {
		TableInfo info = SuperTable.getTableInfo(clazz);

		String[] key = new String[condition.keySet().size()];
		String sql = selectSql.toSqlString(info.pkName, info.tableName, info.buildDbColumns(), condition.keySet().toArray(key));
		List<T> result = (List<T>) super.query(sql, info.classAccess.newInstance(), condition.values().toArray());
		if (result.size() > 0) {
			return (T) result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 获取首行记录
	 * 
	 * @param clazz
	 *            查询实体类
	 * @param params
	 *            查询条件 key:字段名 value:查询值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Table> T getFirst(Class<T> clazz, String tableSuffix, LinkedHashMap<String, Object> condition) {
		TableInfo info = SuperTable.getTableInfo(clazz);

		String[] key = new String[condition.keySet().size()];
		String sql = selectSql.toSqlString(info.pkName, info.tableName + tableSuffix, info.buildDbColumns(), condition.keySet().toArray(key));
		List<T> result = (List<T>) super.query(sql, info.classAccess.newInstance(), condition.values().toArray());
		if (result.size() > 0) {
			return (T) result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 获取表中所有实体(基本不会用,偶尔有几条数据的表，例如Id表)
	 * 
	 * @param clazz
	 *            查询实体类
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Table> List<T> getList(Class<T> clazz) {
		TableInfo info = SuperTable.getTableInfo(clazz);
		return (List<T>) super.query(selectSql.toSqlString(info.pkName, info.tableName, info.buildDbColumns()), info.classAccess.newInstance());
	}

	/**
	 * 根据条件查询
	 * 
	 * @param clazz
	 *            查询实体类
	 * @param params
	 *            查询条件 key:字段名 value:查询值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Table> List<T> getList(Class<T> clazz, LinkedHashMap<String, Object> condition) {
		TableInfo info = SuperTable.getTableInfo(clazz);

		String[] key = new String[condition.keySet().size()];
		String sql = selectSql.toSqlString(info.pkName, info.tableName, info.buildDbColumns(), condition.keySet().toArray(key));
		return (List<T>) super.query(sql, condition.values().toArray(), info.classAccess.newInstance());
	}

	@SuppressWarnings("unchecked")
	public <T extends Table> List<T> getList(Class<T> clazz, LinkedHashMap<String, Object> condition, String userDefinedCondition, List<Object> userDefinedParams) {
		TableInfo info = SuperTable.getTableInfo(clazz);

		String[] key = new String[condition.keySet().size()];
		String[] array = condition.keySet().toArray(key);
		String sql = selectSql.toSqlString(info.pkName, info.tableName, info.buildDbColumns(), array, userDefinedCondition);
		Object[] args = condition.values().toArray();
		int uParamsNum = 0;
		if (userDefinedParams != null) {
			uParamsNum = userDefinedParams.size();
		}
		Object[] allArgs = Arrays.copyOf(args, args.length + uParamsNum);
		for (int i = 0; i < uParamsNum; i++) {
			allArgs[args.length + i] = userDefinedParams.get(i);
		}
		return (List<T>) super.query(sql, allArgs, info.classAccess.newInstance());
	}

	/**
	 * 分页查询
	 * 
	 * @param clazz
	 * @param params
	 * @param columName
	 * @param limitBegin
	 * @param limitEnd
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Table> List<T> getList(Class<T> clazz, LinkedHashMap<String, Object> condition, int limitBegin, int limitEnd) {
		TableInfo info = SuperTable.getTableInfo(clazz);

		String[] key = new String[condition.keySet().size()];
		String sql = selectSql.toSqlString(info.pkName, info.tableName, info.buildDbColumns(), limitBegin, limitEnd, condition.keySet().toArray(key));
		if (limitBegin > 0) {
			condition.put("limitBegin", limitBegin);
		}
		if (limitEnd > 0) {
			condition.put("limitEnd", limitEnd);
		}
		return (List<T>) super.query(sql, condition.values().toArray(), info.classAccess.newInstance());
	}

	/**
	 * 自定义sql查询
	 * 
	 * @param sql
	 *            sql语句
	 * @param condition
	 *            条件值
	 * @param clazz
	 *            对应实体类
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Table> List<T> getList(String sql, Object[] condition, Class<T> clazz) {
		TableInfo info = SuperTable.getTableInfo(clazz);

		if (condition == null || condition.length < 1) {
			sql = selectSql.toSqlString(info.pkName, info.tableName, info.buildDbColumns());
			return (List<T>) super.query(sql, info.classAccess.newInstance());
		} else {
			return (List<T>) super.query(sql, condition, info.classAccess.newInstance());
		}
	}

	/**
	 * 查询主键列表
	 * 
	 * @param clazz
	 *            类型
	 * @param params
	 *            查询条件 key:字段名 value:查询值
	 * @return
	 */
	public <T extends Table> List<Long> getPKList(Class<T> clazz, LinkedHashMap<String, Object> condition) {
		return getPKList(clazz, condition, 0, 0);
	}

	/**
	 * 查询主键分页列表
	 * 
	 * @param clazz
	 *            类型
	 * @param params
	 *            查询条件 key:字段名 value:查询值
	 * @param limit
	 *            记录条数
	 * @return
	 */
	public <T extends Table> List<Long> getPKList(Class<T> clazz, LinkedHashMap<String, Object> condition, int limitBegin, int limitEnd) {
		TableInfo info = SuperTable.getTableInfo(clazz);

		String[] key = new String[condition.keySet().size()];
		String sql = selectSql.toSqlString(info.pkName, info.tableName, info.buildDbColumns(), info.pkName, limitBegin, limitEnd, condition.keySet().toArray(key));
		if (limitBegin > 0) {
			condition.put("limintBegin", limitBegin);
		}
		if (limitEnd > 0 && limitEnd > limitBegin) {
			condition.put("limitEnd", limitEnd);
		}

		return super.queryForList(sql, condition.values().toArray(), Long.class);
	}

	/**
	 * 插入返回数据库自增id
	 * 
	 * @param entity
	 * @return
	 */
	public <T extends Table> Long saveAndIncreasePK(T entity) {
		TableInfo info = SuperTable.getTableInfo(entity.getClass());

		final String sql = replaceSql.toSqlString(info.pkName, info.tableName, info.buildDbColumns());
		final Object[] values = entity.writeData();
		KeyHolder keyHolder = new GeneratedKeyHolder();
		super.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement ps = arg0.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				for (int i = 0; i < values.length; i++) {
					Object value = values[i];
					ps.setObject(i + 1, value);
				}
				return ps;
			}
		}, keyHolder);
		long id = keyHolder.getKey().longValue();
		entity.setPkId(id);
		return id;
	}

	/**
	 * 分组entity
	 * 
	 * @param entitys
	 * @return
	 */
	protected <T extends Table> Map<String, List<T>> groupEntity(Collection<T> entitys) {
		Map<String, List<T>> map = new LinkedHashMap<>();
		for (T t : entitys) {
			TableInfo info = SuperTable.getTableInfo(t.getClass());
			String tableName = info.tableName;
			List<T> list = null;
			if (map.containsKey(tableName)) {
				list = map.get(tableName);
			} else {
				list = new ArrayList<>();
				map.put(tableName, list);
			}
			list.add(t);
		}
		return map;
	}

	@Override
	public String toString() {
		return "Jdbc [driverClassName=" + driverClassName + ", initialSize=" + initialSize + ", maxActive=" + maxActive + ", maxIdle=" + maxIdle + ", minIdle=" + minIdle
				+ ", timeBetweenEvictionRunsMillis=" + timeBetweenEvictionRunsMillis + ", testOnBorrow=" + testOnBorrow + ", testWhileIdle=" + testWhileIdle + ", validationQuery="
				+ validationQuery + ", username=" + username + ", password=" + password + ", host=" + host + ", dbName=" + dbName + ", useUnicode=" + useUnicode
				+ ", characterEncoding=" + characterEncoding + ", autoReconnect=" + autoReconnect + "]";
	}
}
