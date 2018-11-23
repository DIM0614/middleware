package br.ufrn.dimap.middleware.remotting.impl;

import br.ufrn.dimap.middleware.extension.interfaces.ResponseHandler;
import br.ufrn.dimap.middleware.extension.interfaces.ServerProtocolPlugin;
import br.ufrn.dimap.middleware.remotting.interfaces.ServerRequestHandler;

/**
 * Class to handle connections from clients
 * 
 * @author victoragnez
 *
 */
public class ServerRequestHandlerImpl implements ServerRequestHandler {
	/**
	 * port to listen
	 */
	private final int port;
	
	/**
	 * Communication protocol
	 */
	private final ServerProtocolPlugin protocol;
	
	/**
	 * The response handler
	 */
	private final ResponseHandler responseHandler;
	
	private Thread listenThread;
	
	/**
	 * Constructors, set port and protocol to default ones when not specified
	 * @throws RemoteError if any error occurs
	 */
	public ServerRequestHandlerImpl() throws RemoteError {
		this(defaultPort, new DefaultServerProtocolTCP());
	}
	
	public ServerRequestHandlerImpl(int port) throws RemoteError {
		this(port, new DefaultServerProtocolTCP());
	}
	
	public ServerRequestHandlerImpl(ServerProtocolPlugin protocol) throws RemoteError {
		this(defaultPort, protocol);
	}
	
	public ServerRequestHandlerImpl(int port, ServerProtocolPlugin protocol) throws RemoteError {
		this.port = port;
		this.protocol = protocol;
		this.responseHandler = new ResponseHandlerImpl();
	}
	
	
	/* (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ServerRequestHandler#init()
	 */
	@Override
	public void init() throws RemoteError {
		listenThread = new Thread(() -> {
			try {
				protocol.listen(port, responseHandler);
			} catch (RemoteError e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		});
		listenThread.start();
	}

	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.ServerRequestHandler#shutdown()
	 */
	@Override
	public void shutdown() throws RemoteError {
		protocol.shutdown();
		try {
			listenThread.join();
		} catch (InterruptedException e) {
			throw new RemoteError(e);
		}
	}

}
