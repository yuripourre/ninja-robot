package br.com.nrobot.network.client;

import br.com.midnight.client.TCPClient;

public class ActionClient extends TCPClient {

	private ActionClientProtocol actionProtocol;
	
	public ActionClient(String ip, int tcpPort, ActionClientListener listener) {
		super(ip, tcpPort, true);
		
		actionProtocol = new ActionClientProtocol(listener);
		
		addProtocol(actionProtocol);
	}

	public ActionClientProtocol getActionProtocol() {
		return actionProtocol;
	}
	
}
