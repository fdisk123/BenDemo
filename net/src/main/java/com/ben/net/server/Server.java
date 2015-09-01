package com.ben.net.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

public final class Server implements Runnable {

	protected static Selector SELECTOR;
	protected static ServerSocketChannel SERVER;
	protected Properties config;

	private static Thread thread;

	private void init() throws Exception {
		if (thread != null)
			thread.interrupt();
		SELECTOR = Selector.open();
		SERVER = ServerSocketChannel.open();
		SERVER.bind(new InetSocketAddress(1));
		SERVER.configureBlocking(false);
		SERVER.register(SELECTOR, SelectionKey.OP_ACCEPT);
		thread = new Thread(this);
		thread.start();
	}

	public boolean start() {
		try {
			init();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void run() {
		SocketChannel client;
		Set<SelectionKey> keys;
		SelectionKey key;
		Iterator<SelectionKey> it;
		while (!Thread.interrupted()) {
			try {
				if (SELECTOR.select(50) == 0) {
					continue;
				}
				keys = SELECTOR.selectedKeys();
				it = keys.iterator();
				while (it.hasNext()) {
					key = it.next();
					it.remove();
					//分解
					if (key.channel().isOpen()) {
						if (key.isAcceptable()) {
							// executor.execute(new SocketMsgAcceptHandler(key));
							client = ((ServerSocketChannel) key.channel()).accept();
							client.configureBlocking(false);
							//client.register(key.selector(), SelectionKey.OP_READ, GenerateIdPool.getID()).selector().wakeup();
							client.register(key.selector(), SelectionKey.OP_READ).selector().wakeup();
							// DoQueue.doFinish(key.attachment());
						} else if (key.isReadable()) {
							// System.err.println("读");
							//							executor.execute(new SocketMsgReaderHandler(key));
						} else if (key.isWritable()) {
							// System.err.println("写");
							//							executor.execute(new SocketMsgWriterHandler(key));
						}
					} else {
						key.channel().close();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}