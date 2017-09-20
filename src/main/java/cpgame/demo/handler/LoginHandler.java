package cpgame.demo.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cpgame.demo.domain.GameRequest;
import cpgame.demo.domain.GameResponse;

public class LoginHandler implements GameHandler{
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Override
	public void execute(GameRequest request, GameResponse response) {
		//
		this.logger.error(request.readString());
		response.write("I am ok!");
	}

}
