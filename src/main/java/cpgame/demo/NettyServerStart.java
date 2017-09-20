package cpgame.demo;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import cpgame.demo.netty.ServerInitializer;

/**
 * @project: demo
 * @Title: NettyServerStart.java
 * @Package:
 * @author: yujoo
 * @description:
 * @version:
 */
public class NettyServerStart {
	private static int port;
	public static ApplicationContext factory;
	private static Logger log;

	//DOM读取xml文件的日志格式
	public static void main(String[] args) throws Exception {
		DOMConfigurator.configureAndWatch("logback.xml");
		if (args.length > 0)
			port = Integer.parseInt(args[0]);
		else {
			port = 8080;
		}
		//执行spring
		run();
	}

	private static void run() throws Exception {
		//加载spring容器
		factory = new FileSystemXmlApplicationContext("classpath*:spring.xml");
		//从容器中取得出事的channel的初始化bean
		ServerInitializer initializer = (ServerInitializer) factory.getBean(ServerInitializer.class);
		//再将这个spring容器管理的bean设置给netty 用作启动初始化的channel
		NettyServer server = new NettyServer(port);
		server.setInitializer(initializer);
		server.run();
		log.debug("server is running……");
	}
}