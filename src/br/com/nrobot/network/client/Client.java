package br.com.nrobot.network.client;

import br.com.etyllica.core.event.KeyEvent;
import br.com.midnight.client.TCPClient;
import br.com.nrobot.game.GameMode;
import br.com.nrobot.network.server.model.NetworkRole;

public class Client extends TCPClient {

	public static final int BATTLE_PORT = 9967;
	public static final int STORY_PORT = 9968;
	private static final int CLIENT_DELAY = 100;

	private ClientProtocol protocol;
	private NetworkRole role = NetworkRole.CLIENT;

	public static Client create(GameMode mode, ClientListener listener, String ip) {
		int port = mode == GameMode.BATTLE ? BATTLE_PORT : STORY_PORT;
		return new Client(ip, port, listener);
	}

	public Client(String ip, int port, ClientListener listener) {
		super(ip, port, true);

		protocol = new ClientProtocol(listener);
		addProtocol(protocol);
		start(CLIENT_DELAY);
	}

	public ClientProtocol getProtocol() {
		return protocol;
	}

	public void setRole(NetworkRole role) {
		this.role = role;
	}

	public NetworkRole getRole() {
		return role;
	}

	public void handleEvent(KeyEvent event) {
		handleKey(event, KeyEvent.VK_D, ClientProtocol.KEY_RIGHT);
		handleKey(event, KeyEvent.VK_RIGHT_ARROW, ClientProtocol.KEY_RIGHT);
		handleKey(event, KeyEvent.VK_A, ClientProtocol.KEY_LEFT);
		handleKey(event, KeyEvent.VK_LEFT_ARROW, ClientProtocol.KEY_LEFT);
		handleKey(event, KeyEvent.VK_SPACE, ClientProtocol.KEY_ITEM);
		handleKey(event, KeyEvent.VK_SHIFT_LEFT, ClientProtocol.KEY_ATTACK);
		handleKey(event, KeyEvent.VK_SHIFT_RIGHT, ClientProtocol.KEY_ATTACK);
		handleKey(event, KeyEvent.VK_CTRL_LEFT, ClientProtocol.KEY_JUMP);
		handleKey(event, KeyEvent.VK_CTRL_RIGHT, ClientProtocol.KEY_JUMP);
	}

	private void handleKey(KeyEvent event, int keyCode, String keyText) {
		if (event.isKeyDown(keyCode)) {
			protocol.sendPressKey(keyText);
		} else if (event.isKeyUp(keyCode)) {
			protocol.sendReleaseKey(keyText);
		}
	}

	public void setListener(ClientListener listener) {
		protocol.setListener(listener);
	}
}
