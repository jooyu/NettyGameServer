package cpgame.demo.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cpgame.demo.dao.DataDao;
import cpgame.demo.domain.GameRequest;
import cpgame.demo.domain.GameResponse;

/**
 * @project: demo
 * @Title: InitHandler.java
 * @Package: cpgame.demo.handler
 * @author: chenpeng
 * @email: 46731706@qq.com
 * @date: 2015年8月20日 下午2:27:11
 * @description:
 * @version:
 */
public class InitHandler implements GameHandler {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private DataDao dataDao;
	//具体的执行逻辑
	public void execute(GameRequest request, GameResponse response) {
		this.logger.error(request.readString());
		
		response.write(dataDao.getUserInfo("1"));
	}
}
