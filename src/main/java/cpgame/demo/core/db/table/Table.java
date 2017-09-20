package cpgame.demo.core.db.table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.esotericsoftware.reflectasm.FieldAccess;

import cpgame.demo.core.db.column.AbstractColumnParser;
import cpgame.demo.core.db.column.ColumnContext;
import cpgame.demo.core.db.table.TableInfo.TableColumnInfo;

/**
 * 所有实体继承于此,实现已下方法
 * @author 0x737263
 *
 * @param <PK>主键类型
 */
public abstract class Table extends SuperTable implements RowMapper<Table> {
	private static final Logger LOGGER = LoggerFactory.getLogger(Table.class);

	/**
	 * 获取主键值
	 * @return
	 */
	public abstract long getPkId();

	/**
	 * 设置主键值
	 * @param pk
	 */
	public abstract void setPkId(long pk);

	/**
	 * 读取完表数据后如需处理额外逻辑，请重写此方法
	 */
	protected void readComplete() {
	}

	protected void readBefore(ResultSet rs) {
	}

	@Override
	public Table mapRow(ResultSet rs, int rowNum) throws SQLException {
		TableInfo tableInfo = getTableInfo(this.getClass());
		Table entity = tableInfo.classAccess.newInstance();
		readBefore(rs);
		entity.readData(tableInfo, rs);
		// 读取完表数据后处理额外逻辑
		entity.readComplete();
		return entity;
	}

	/**
	 * 从db读取每一行记录,续承类可以重写自定义读取方式
	 * @param tableInfo
	 * @param entity
	 * @param rs
	 */
	public void readData(TableInfo tableInfo, ResultSet rs) throws SQLException {

		for (TableColumnInfo columnInfo : tableInfo.columnInfoList) {
			Class<?> columnType = columnInfo.getColumnType(0);

			AbstractColumnParser parser = ColumnContext.getParser(columnType);
			if (parser == null) {
				LOGGER.error("Read data error.column type {} not implemented.", columnInfo);
				continue;
			}
			try {
				parser.readColumn(this, columnInfo, rs);
			} catch (Exception ex) {
				LOGGER.error("read table error! className={} fieldName={}", tableInfo.className, columnInfo.fieldName);
				LOGGER.error("", ex);
			}
		}
	}

	/**
	 * 获取所有字段的值,,续承类可以重写自定义读取方式
	 * @return
	 */
	public Object[] writeData() {
		TableInfo tableInfo = getTableInfo(getClass());
		FieldAccess fieldAccess = tableInfo.fieldAccess;

		ArrayList<Object> writeList = new ArrayList<>();
		for (TableColumnInfo columnInfo : tableInfo.columnInfoList) {
			Class<?> columnType = columnInfo.getColumnType(0);
			AbstractColumnParser parser = ColumnContext.getParser(columnType);
			if (parser == null) {
				LOGGER.error("Read data error.column type {} not implemented.", columnInfo);
				continue;
			}
			Object fieldValue = null;
			try {
				fieldValue = fieldAccess.get(this, columnInfo.fieldName);
				parser.writeColumn(writeList, columnInfo, fieldValue);
			} catch (Exception ex) {
				LOGGER.error("write table error! className={} fieldName={} value={}", tableInfo.className, columnInfo.fieldName, fieldValue);
				LOGGER.error("{}", ex);
			}
		}

		return writeList.toArray();
	}
}
