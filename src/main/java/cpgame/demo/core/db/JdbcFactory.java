package cpgame.demo.core.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import cpgame.demo.core.db.column.ColumnContext;
import cpgame.demo.core.db.table.SuperTable;
import cpgame.demo.core.db.table.Table;
import cpgame.demo.core.db.table.TableInfo;

/**
 * jdbc管理类(inject jdbc.xml)
 * 
 * @author 0x737263
 *
 */
public class JdbcFactory implements InitializingBean {
	private static final Logger LOGGER = LoggerFactory.getLogger(JdbcFactory.class);

	private String[] packageScanArray;

	/** key:tableName,value:jdbc */
	private Map<String, Jdbc> jdbcMaps = new HashMap<>();

	/**
	 * 
	 * @param packageScan
	 * @param jdbcList
	 */
	public JdbcFactory(String packageScan, List<Jdbc> jdbcList) {
		this.packageScanArray = packageScan.split(",");

		for (Jdbc jdbc : jdbcList) {
			addJdbc(jdbc);
		}
	}
	
	public void addJdbc(Jdbc jdbc) {
		for (String tableName : jdbc.tableList()) {
			jdbcMaps.put(tableName.toLowerCase(), jdbc);
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("jdbc create complete! info:{}", jdbc.toString());
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		ColumnContext context = new ColumnContext();
		context.init();
		SuperTable.tableScan(this.packageScanArray);
	}

	public Jdbc jdbc(Class<? extends Table> tableClazz) {
		TableInfo tableInfo = SuperTable.getTableInfo(tableClazz);
		return jdbcMaps.get(tableInfo.tableName);
	}

	public Jdbc jdbc(String tableName) {
		return jdbcMaps.get(tableName);
	}
	
}
