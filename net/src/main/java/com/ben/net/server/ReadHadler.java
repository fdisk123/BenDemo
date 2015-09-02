package com.ben.net.server;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Callable;

import com.ben.net.server.util.ByteBufferUtil;

/***
 * 返回对象并释放
 * 
 * @author Ben-P
 *
 */
public class ReadHadler implements Callable<Object> {

	public ReadHadler(SelectionKey key) {
		super();
		this.key = key;
	}

	private SelectionKey key;
	private SocketChannel client;

	public Object call() throws Exception {
		client = (SocketChannel) key.channel();
		
		
		
		System.out.println(new String(ByteBufferUtil.instance().getByte(client).toByteArray()));
		
		
		
		client.close();
		key.cancel();
		return key.attachment();// 清空
	}

}
