package cpgame.demo.core.db.table;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.FieldAccess;

import cpgame.demo.core.db.annotation.Column;
import cpgame.demo.core.db.annotation.TableName;
import cpgame.demo.core.utils.ReflectUtils;
import cpgame.demo.core.utils.StringUtils;

/**
 * 实体信息类,用于记录反射后的一些常用信息
 * @author 0x737263
 *
 */
public class TableInfo {
	private static final Logger LOGGER = LoggerFactory.getLogger(TableInfo.class);
	
	public String className;

	public String tableName;
	
	public boolean isIdTable;
	
	public long startId;

	public String pkName;
	
	public List<TableColumnInfo> columnInfoList = new ArrayList<>();
	
	public ConstructorAccess<Table> classAccess;

	public FieldAccess fieldAccess;
	
	public static TableInfo valueOf(Class<Table> clazz, TableName tableName) {
		TableInfo tableInfo = new TableInfo();
		tableInfo.className = clazz.getCanonicalName();
		tableInfo.tableName = tableName.name();
		tableInfo.isIdTable = tableName.isIdBuilder();
		tableInfo.startId = tableName.startId();

		tableInfo.classAccess = ConstructorAccess.get(clazz);
		tableInfo.fieldAccess = FieldAccess.get(clazz);
		tableInfo.reflectColumn(clazz.getDeclaredFields());

		if (StringUtils.isBlank(tableInfo.pkName)) {
			LOGGER.error(tableInfo.className + "实体缺少主键");
		}
		return tableInfo;
	}

	public void reflectColumn(Field[] fields) {
		for (Field field : fields) {
			Column column = field.getAnnotation(Column.class);
			if (column == null) {
				continue;
			}
			
			if (!Modifier.isPublic(field.getModifiers())) {
				throw new RuntimeException(this.className + " 被@Column标注的Field必需为public.");
			}

			String pkName = field.getName();
			String pkAliasName = column.alias();

			if (column.pk()) {
				if (StringUtils.isNotBlank(this.pkName)) {
					throw new RuntimeException(this.className + " 禁止设置多个主键.");
				}
				this.pkName = StringUtils.isNotBlank(pkAliasName) ? pkAliasName : pkName;
			}

			List<Class<?>> fileTypeList = ReflectUtils.reflectFieldType(field);
			String aliasName = StringUtils.isBlank(pkAliasName) ? pkName : pkAliasName;
			columnInfoList.add(new TableColumnInfo(pkName, aliasName, fileTypeList));
		}
	}
	
	public String[] buildDbColumns() {
		String[] columns = new String[columnInfoList.size()];
		for (int i = 0; i < columnInfoList.size(); i++) {
			columns[i] = columnInfoList.get(i).aliasName;
		}
		return columns;
	}
	
	@Override
	public String toString() {
		LOGGER.info("-----------" + this.tableName + "-------------------");
		for (TableColumnInfo columnInfo : columnInfoList) {
			LOGGER.info(columnInfo.toString());
		}
		LOGGER.info("-----------" + this.tableName + "-------------------");
		return super.toString();
	}
	
	/**
	 * 
	 * @author 0x737263
	 *
	 */
	public class TableColumnInfo {
		public String fieldName;
		public String aliasName;
		public List<Class<?>> columnTypeList;
		
		public TableColumnInfo(String fieldName, String aliasName, List<Class<?>> columnTypeList) {
			this.fieldName = fieldName;
			this.aliasName = aliasName;
			this.columnTypeList = columnTypeList;
		}

		@Override
		public String toString() {
			return "TableColumnInfo [columnName=" + fieldName + ", aliasName=" + aliasName + ", columnTypeList=" + columnTypeList + "]";
		}

		public Class<?> getColumnType(int index) {
			return columnTypeList.get(index);
		}

	}
}
