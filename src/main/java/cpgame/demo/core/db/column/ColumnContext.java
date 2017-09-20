package cpgame.demo.core.db.column;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cpgame.demo.core.db.JsonEntity;
import cpgame.demo.core.db.table.Table;
import cpgame.demo.core.db.table.TableInfo.TableColumnInfo;
import cpgame.demo.core.utils.StringUtils;
import com.esotericsoftware.reflectasm.FieldAccess;

public class ColumnContext {
	private static final Logger LOGGER = LoggerFactory.getLogger(ColumnContext.class);

	private static Map<Class<?>, AbstractColumnParser> PARSER_MAP = new HashMap<>();

	
	public ColumnContext() {
		
	}
	
	private void register(AbstractColumnParser parser, Class<?>... classes) {
		for (Class<?> clazz : classes) {
			PARSER_MAP.put(clazz, parser);
		}
	}

	/**
	 * 获取解析器
	 * @param clazz
	 * @return
	 */
	public static AbstractColumnParser getParser(Class<?> clazz) {
		if (JsonEntity.class.isAssignableFrom(clazz)) {
			return PARSER_MAP.get(JsonEntity.class);
		}

		return PARSER_MAP.get(clazz);
	}

	public void init() {
		register(new AtomicLongColumn(), AtomicLong.class);
		register(new BooleanColumn(), Boolean.class, boolean.class);
		register(new NumberColumn(), Byte.class, byte.class, int.class, Integer.class, Long.class, long.class, String.class, Short.class,
				short.class);
		register(new DecimalColumn(), BigDecimal.class);
		register(new ByteArrayColumn(), byte[].class);
		register(new ListColumn(), List.class);
		register(new MapColumn(), Map.class);
		// register(new BlobColumn(), ProtoEntity.class);
		// register(new BlobTypeColumn(), ProtoTypeEntity.class);
		register(new JsonColumn(), JsonEntity.class);

		LOGGER.info("ColumnContext parser init complete!");
	}

	public class AtomicLongColumn extends AbstractColumnParser {
		@Override
		public void readColumn(Table instance, TableColumnInfo columnInfo, ResultSet rs) throws SQLException {
			FieldAccess fieldAccess = instance.getTableInfo().fieldAccess;

			AtomicLong atomLong = (AtomicLong) fieldAccess.get(instance, columnInfo.fieldName);
			atomLong.set(rs.getLong(columnInfo.aliasName));
		}

		@Override
		public void writeColumn(ArrayList<Object> writeList, TableColumnInfo columnInfo, Object fieldValue) {
			writeList.add(fieldValue.toString());
		}
	}

	public class BooleanColumn extends AbstractColumnParser {
		@Override
		public void readColumn(Table instance, TableColumnInfo columnInfo, ResultSet rs) throws SQLException {
			FieldAccess fieldAccess = instance.getTableInfo().fieldAccess;
			fieldAccess.set(instance, columnInfo.fieldName, rs.getObject(columnInfo.aliasName));
		}

		@Override
		public void writeColumn(ArrayList<Object> writeList, TableColumnInfo columnInfo, Object fieldValue) {
			Boolean value = (Boolean) fieldValue;
			writeList.add(value ? 1 : 0);
		}
	}

	public class NumberColumn extends AbstractColumnParser {
		@Override
		public void readColumn(Table instance, TableColumnInfo columnInfo, ResultSet rs) throws SQLException {
			FieldAccess fieldAccess = instance.getTableInfo().fieldAccess;
			Object x = rs.getObject(columnInfo.aliasName);
			fieldAccess.set(instance, columnInfo.fieldName, x);
		}

		@Override
		public void writeColumn(ArrayList<Object> writeList, TableColumnInfo columnInfo, Object fieldValue) {
			writeList.add(fieldValue.toString());
		}
	}

	public class DecimalColumn extends AbstractColumnParser {
		@Override
		public void readColumn(Table instance, TableColumnInfo columnInfo, ResultSet rs) throws SQLException {
			FieldAccess fieldAccess = instance.getTableInfo().fieldAccess;
			fieldAccess.set(instance, columnInfo.fieldName, rs.getObject(columnInfo.aliasName));
		}

		@Override
		public void writeColumn(ArrayList<Object> writeList, TableColumnInfo columnInfo, Object fieldValue) {
			writeList.add(fieldValue);
		}
	}

	public class ByteArrayColumn extends AbstractColumnParser {
		@Override
		public void readColumn(Table instance, TableColumnInfo columnInfo, ResultSet rs) throws SQLException {
			FieldAccess fieldAccess = instance.getTableInfo().fieldAccess;
			fieldAccess.set(instance, columnInfo.fieldName, rs.getBytes(columnInfo.aliasName));
		}

		@Override
		public void writeColumn(ArrayList<Object> writeList, TableColumnInfo columnInfo, Object fieldValue) {
			writeList.add(fieldValue);
		}
	}

	public class ListColumn extends AbstractColumnParser {

		@Override
		public void readColumn(Table instance, TableColumnInfo columnInfo, ResultSet rs) throws SQLException {
			FieldAccess fieldAccess = instance.getTableInfo().fieldAccess;

			Class<?> valueType = columnInfo.getColumnType(1);
			String json = rs.getString(columnInfo.aliasName);
			List<?> value = JSON.parseArray(json, valueType);
			fieldAccess.set(instance, columnInfo.fieldName, value);
		}

		@Override
		public void writeColumn(ArrayList<Object> writeList, TableColumnInfo columnInfo, Object fieldValue) {			
			if (fieldValue == null) {
				writeList.add("");
			} else {
				String jsonString = JSON.toJSONString(fieldValue);
				writeList.add(jsonString);
			}
		}
	}

	public class MapColumn extends AbstractColumnParser {

		@SuppressWarnings("unchecked")
		@Override
		public void readColumn(Table instance, TableColumnInfo columnInfo, ResultSet rs) throws SQLException {
			FieldAccess fieldAccess = instance.getTableInfo().fieldAccess;

			Map<Object, Object> originMaps = (Map<Object, Object>) fieldAccess.get(instance, columnInfo.fieldName);
			if (originMaps == null) {
				originMaps = new ConcurrentHashMap<>();
				fieldAccess.set(instance, columnInfo.fieldName, originMaps);
			}			
			
			String json = rs.getString(columnInfo.aliasName);
			if (StringUtils.isBlank(json)) {
				return;
			}
			try {
				JSONObject jsonObject = JSON.parseObject(json);
				originMaps.putAll(jsonObject);				
			} catch (Exception ex) {
				LOGGER.info("{}", ex);
			}

		}

		@Override
		public void writeColumn(ArrayList<Object> writeList, TableColumnInfo columnInfo, Object fieldValue) {
			if (fieldValue == null) {
				writeList.add("");
			} else {
				String jsonString = JSON.toJSONString(fieldValue);
				writeList.add(jsonString);
			}
		}
	}

	/**
	 * JSON column
	 * @author 0x737263
	 *
	 */
	public class JsonColumn extends AbstractColumnParser {
		@Override
		public void readColumn(Table instance, TableColumnInfo columnInfo, ResultSet rs) throws SQLException {
			FieldAccess fieldAccess = instance.getTableInfo().fieldAccess;

			String json = rs.getString(columnInfo.aliasName);
			if (StringUtils.isBlank(json)) {
				return;
			}

			Object obj = JSON.parseObject(json, columnInfo.getColumnType(0));
			fieldAccess.set(instance, columnInfo.fieldName, obj);

			JsonEntity jsonEntity = (JsonEntity) fieldAccess.get(instance, columnInfo.fieldName);
			if (jsonEntity != null) {
				jsonEntity.afterRead(obj);
			}
		}

		@Override
		public void writeColumn(ArrayList<Object> writeList, TableColumnInfo columnInfo, Object fieldValue) {
			if (fieldValue == null) {
				writeList.add("");
			} else {
				JsonEntity jsonEntity = (JsonEntity) fieldValue;
				jsonEntity.beginWrite();
				String jsonString = JSON.toJSONString(fieldValue);
				writeList.add(jsonString);
			}
		}
	}

}
