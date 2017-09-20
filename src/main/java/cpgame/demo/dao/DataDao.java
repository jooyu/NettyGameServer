package cpgame.demo.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cpgame.demo.core.db.Jdbc;
import cpgame.demo.core.db.JdbcFactory;

@Component
public class DataDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(DataDao.class);
	@Autowired
	JdbcFactory jdbcFactory;
	Jdbc dataJdbc;

	@PostConstruct
	public void init() {

		dataJdbc = jdbcFactory.jdbc("user");
	}

	public String getUserInfo(String id) {
		String sql = String.format("select * from user  limit 1");
		List<Map<String, Object>> result = dataJdbc.queryForList(sql);
		LOGGER.debug("结果：" + (String) result.get(0).get("name"));
		return (String) result.get(0).get("name");

	}
}
