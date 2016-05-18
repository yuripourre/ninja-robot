package br.com.nrobot.network.client;

public interface ActionClientListener {
	public void exitClient(String id);
	public void joinedClient(String id);	
	public void receiveMessage(String id, String message);
	public void init(String[] ids);
}
