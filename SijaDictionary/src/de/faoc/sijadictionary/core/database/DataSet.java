package de.faoc.sijadictionary.core.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class DataSet extends ArrayList<HashMap<String, Object>> {

	public static DataSet fromResulSet(ResultSet resultSet) {
		try {
			DataSet dataSet = new DataSet();
			
			if (resultSet.getType() != ResultSet.TYPE_FORWARD_ONLY)
				resultSet.beforeFirst();

			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();

			while (resultSet.next()) {
				HashMap<String, Object> data = new HashMap<>();
				for (int i = 1; i <= columnCount; i++) {
					data.put(metaData.getColumnLabel(i), resultSet.getObject(i));
				}
				dataSet.add(data);
			}
			return dataSet;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
