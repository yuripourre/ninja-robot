package br.com.nrobot.network;

import br.com.nrobot.player.Player;

public class PlayerData {

	public final String id;
	private final String state, item;
	private final int x, y, points;

	public static PlayerData fromFields(String[] fields, int position) {
		String id = fields[position];
		int x = Integer.parseInt(fields[position + 1]);
		int y = Integer.parseInt(fields[position + 2]);
		String state = fields[position + 3];
		String item = fields[position + 4];
		int points = Integer.parseInt(fields[position + 5]);

		return new PlayerData(id, x, y, state, item, points);
	}

	public PlayerData(String id, int x, int y, String state, String item, int points) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.state = state;
		this.item = item;
		this.points = points;
	}

	public void setTo(Player player) {
		player.setPosition(x, y);
		player.setState(state);
		player.setItem(item);
		player.setPoints(points);
	}
}
