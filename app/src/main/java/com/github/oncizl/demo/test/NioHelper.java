package com.github.oncizl.demo.test;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NioHelper {

	public static class Socket {

		private String mHost;
		private int mPort;
		private Callback mCallback;
		private Selector mSelector;
		private ExecutorService mExecutorRead = Executors.newSingleThreadExecutor();
		private ExecutorService mExecutorWrite = Executors.newSingleThreadExecutor();

		public Socket host(String host) {
			mHost = host;
			return this;
		}

		public Socket port(int port) {
			mPort = port;
			return this;
		}

		public Socket callback(Callback callback) {
			mCallback = callback;
			return this;
		}

		public void connect() {
			if (mSelector == null) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							SocketChannel channel = SocketChannel.open(new InetSocketAddress(mHost, mPort));
							channel.configureBlocking(false);
							mSelector = Selector.open();
							channel.register(mSelector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
							while (mCallback != null) {
								int readyChannels = mSelector.select();
								if (readyChannels == 0) continue;
								Set<SelectionKey> selectionKeys = mSelector.selectedKeys();
								Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
								while (keyIterator.hasNext()) {
									final SelectionKey key = keyIterator.next();
									if (key.isReadable()) {
										mExecutorRead.execute(new Runnable() {
											@Override
											public void run() {
												mCallback.read(key);
											}
										});
									}
									keyIterator.remove();
								}
							}
						} catch (IOException e) {

							e.printStackTrace();
						}
					}
				}).start();
			}
		}

		public void write(final byte[] bytes) {
			mExecutorWrite.execute(new Runnable() {
				@Override
				public void run() {
					boolean writable = true;
					while (mCallback != null && writable && mSelector != null) {
						try {
							int readyChannels = mSelector.select();
							if (readyChannels == 0) continue;
							Set<SelectionKey> selectionKeys = mSelector.selectedKeys();
							Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
							while (keyIterator.hasNext()) {
								SelectionKey key = keyIterator.next();
								if (key.isWritable()) {
									SocketChannel channel = (SocketChannel) key.channel();
									ByteBuffer bufferWrite = ByteBuffer.allocate(bytes.length);
									bufferWrite.put(bytes);
									bufferWrite.flip();
									mCallback.write(channel.write(bufferWrite) > 0);
									bufferWrite.clear();
									writable = false;
								}
								keyIterator.remove();
							}
						} catch (IOException e) {
							writable = true;
							e.printStackTrace();
						}
					}
				}
			});
		}

		public interface Callback {
			void read(SelectionKey key);

			void write(boolean success);
		}
	}
}
