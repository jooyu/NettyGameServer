package cpgame.demo; 


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
/** 
* @project:		nettygame
* @Title:		ClientInboundHandler.java
* @Package:		cpgame.nettygame
  @author: 		chenpeng
* @email: 		46731706@qq.com
* @date:		2015年8月27日 上午9:48:49 
* @description:
* @version:
*/
public class ClientInboundHandler2 extends SimpleChannelInboundHandler<Object> {

//	@SuppressWarnings("deprecation")
//	@Override
//	public void channelRead(ChannelHandlerContext ctx, Object msg)
//			throws Exception {
//		if (msg instanceof HttpResponse) {
//			HttpResponse response = (HttpResponse) msg;
//			System.out.println("CONTENT_TYPE:"
//					+ response.headers().get(HttpHeaders.Names.CONTENT_TYPE));
//		}
//		if (msg instanceof HttpContent) {
//			HttpContent content = (HttpContent) msg;
//			ByteBuf buf = content.content();
//			System.out.println(buf.toString(io.netty.util.CharsetUtil.UTF_8));
//			buf.release();
//		}
//		if (msg instanceof ByteBuf) {
//			ByteBuf messageData = (ByteBuf) msg;
//			int commandId = messageData.readInt();
//			int length = messageData.readInt();
//			byte[] c = new byte[length];
//			messageData.readBytes(c);
//			System.out.println("commandId:"+commandId+"\tmessage:"+new String(c));
//		}
//	}



	@SuppressWarnings("deprecation")
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof HttpResponse) {
		System.out.println("test....");
		if (msg instanceof HttpResponse) {
		HttpResponse response = (HttpResponse) msg;
		System.out.println("CONTENT_TYPE:"
				+ response.headers().get(HttpHeaders.Names.CONTENT_TYPE));
		System.out.println("写数据");
	
	}
	if (msg instanceof HttpContent) {
		HttpContent content = (HttpContent) msg;
		ByteBuf buf = content.content();
		System.out.println(buf.toString(io.netty.util.CharsetUtil.UTF_8));
		buf.release();
	}
	if (msg instanceof ByteBuf) {
		ByteBuf messageData = (ByteBuf) msg;
		int commandId = messageData.readInt();
		int length = messageData.readInt();
		byte[] c = new byte[length];
		messageData.readBytes(c);
		System.out.println("commandId:"+commandId+"\tmessage:"+new String(c));
	}
		}
	}
	
	
}

