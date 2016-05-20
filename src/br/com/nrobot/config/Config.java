package br.com.nrobot.config;

public class Config {

	private String serverIp = "127.0.0.1";
	private String name = "Robot";
	
	public String getServerIp() {
		return serverIp;
	}
	
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
}
