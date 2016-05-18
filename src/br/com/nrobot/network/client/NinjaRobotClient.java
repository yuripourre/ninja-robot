package br.com.nrobot.network.client;

import br.com.nrobot.network.NetworkRole;

public class NinjaRobotClient {

	public static final int PORT = 9967;
	private static final int CLIENT_DELAY = 100;
	
	private NRobotClientProtocol protocol;
		
	private NetworkRole role = NetworkRole.CLIENT;
	
	public NinjaRobotClient(NRobotClientListener listener, String ip) {
		super();
		
		NRobotClient client = new NRobotClient(ip, PORT, listener);
		client.start(CLIENT_DELAY);
		protocol = client.getActionProtocol();
	}
	
	public NRobotClientProtocol getProtocol() {
		return protocol;
	}

	public void setRole(NetworkRole role) {
		this.role = role;
	}
	
	public NetworkRole getRole() {
		return role;
	}
	
}