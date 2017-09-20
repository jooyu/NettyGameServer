package cpgame.demo.netty;

import org.apache.log4j.Logger;

import cpgame.demo.dispatcher.HandlerDispatcher;
import cpgame.demo.domain.ERequestType;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.ReadTimeoutHandler;

//这个初始化的bean又继承自netty的channel初始化类
public class ServerInitializer extends ChannelInitializer<SocketChannel> {
	//超时时间
	private int timeout = 3600;
	//handler分发
	private HandlerDispatcher handlerDispatcher;
	//socket的接收类型
	private String requestType = ERequestType.SOCKET.getValue();
	private static Logger log=Logger.getLogger(ServerInitializer.class);
	public void init() {
		log.debug("handlerDispather线程执行...");
		new Thread(this.handlerDispatcher).start();
	}

	public void initChannel(SocketChannel ch) throws Exception {
		//管道流
		ChannelPipeline pipeline = ch.pipeline();
		//如果接受socket的类型匹配上
		if (ERequestType.SOCKET.getValue().equals(this.requestType.trim().toLowerCase())) {
			ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));

			ch.pipeline().addLast("frameEncoder", new LengthFieldPrepender(4));
		} else {
			pipeline.addLast("codec-http", new HttpServerCodec());
			pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
		}
		pipeline.addLast("timeout", new ReadTimeoutHandler(this.timeout));
		pipeline.addLast("handler", new ServerAdapter(this.handlerDispatcher));
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void setHandlerDispatcher(HandlerDispatcher handlerDispatcher) {
		this.handlerDispatcher = handlerDispatcher;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getRequestType() {
		return this.requestType;
	}
}