package br.com.nrobot.network.client;

import br.com.midnight.client.TCPClient;

public class NRobotClient extends TCPClient {

	private NRobotClientProtocol actionProtocol;
	
	public NRobotClient(String ip, int tcpPort, NRobotClientListener listener) {
		super(ip, tcpPort, true);
		
		actionProtocol = new NRobotClientProtocol(listener);
		
		addProtocol(actionProtocol);
	}

	public NRobotClientProtocol getActionProtocol() {
		return actionProtocol;
	}
	
}
