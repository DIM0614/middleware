package br.ufrn.dimap.middleware.remotting.interfaces;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

public interface ClientRequestHandler {

	/**
	 * default port 22334
	 */
	int defaultPort = 22334;

	/**
	 * Function used by the requestor to send the data,
	 * using the specific protocol
	 *  
	 * @param host the hostname to send the data
	 * @param port the port to be used
	 * @param msg the data to be sent
	 * @return the server reply
	 */
	ByteArrayInputStream send(String host, int port, ByteArrayOutputStream msg) throws RemoteError;

	/**
	 * @return the protocol
	 */
	ClientProtocolPlugin getProtocol();

	/**
	 * @param protocol the protocol to set
	 */
	void setProtocol(ClientProtocolPlugin protocol);

	/**
	 * @return the port
	 */
	int getPort();

	/**
	 * @param port the port to set
	 */
	void setPort(int port);

}