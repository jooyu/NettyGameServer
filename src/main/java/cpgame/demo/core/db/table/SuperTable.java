package cpgame.demo.core.db.table;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cpgame.demo.core.db.annotation.TableName;
import cpgame.demo.core.utils.PackageScanner;

public abstract class SuperTable {
	private static final Logger LOGGER = LoggerFactory.getLogger(SuperTable.class);

	/** table info */
	protected static Map<Class<Table>, TableInfo> TABLE_INFO_MAPS = new HashMap<>();
	
	/**
	 * 
	 * @param packageScan
	 */
	public static void tableScan(String... packageScanArray) {
		
		for (String packageName : packageScanArray) {
			try {
				Collection<Class<Table>> collection = PackageScanner.scanPackages(packageName.trim());
				for (Class<Table> clz : collection) {
					if (clz == null) {
						continue;
					}

					TableName tname = clz.getAnnotation(TableName.class);
					if (tname == null) {
						continue;
					}
					TABLE_INFO_MAPS.put(clz, TableInfo.valueOf(clz, tname));
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		TABLE_INFO_MAPS.values().forEach(tableInfo -> {
			LOGGER.info("Table:{}, Columns:{}", tableInfo.tableName, tableInfo.buildDbColumns());
		});
	}

	public <T extends Table> TableInfo getTableInfo() {
		return TABLE_INFO_MAPS.get(this.getClass());
	}
	
	public String tableName() {
		return getTableInfo().tableName;
	}

	public static <T extends Table> TableInfo getTableInfo(Class<T> clazz) {
		return TABLE_INFO_MAPS.get(clazz);
	}
	
	public static Collection<TableInfo> getTableInfoList() {
		return TABLE_INFO_MAPS.values();
	}
}
