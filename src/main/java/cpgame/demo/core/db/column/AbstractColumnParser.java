package cpgame.demo.core.db.column;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import cpgame.demo.core.db.table.Table;
import cpgame.demo.core.db.table.TableInfo.TableColumnInfo;

public abstract class AbstractColumnParser {

	public abstract void readColumn(Table instance, TableColumnInfo columnInfo, ResultSet rs) throws SQLException;

	public abstract void writeColumn(ArrayList<Object> writeList, TableColumnInfo columnInfo, Object fieldValue);
}
