package br.com.nrobot.fallen;

public enum FallenType {
	BOMB("B") {
		public Fallen create(int x, int y) {
			return new Bomb(x, y);
		}
	},
	GLUE("G") {
		public Fallen create(int x, int y) {
			return new Glue(x, y);
		}
	},
	HIVE("H") {
		public Fallen create(int x, int y) {
			return new Hive(x, y);
		}
	},
	LEAF("L") {
		public Fallen create(int x, int y) {
			return new Leaf(x, y);
		}
	};

	public final String param;

	FallenType(String param) {
		this.param = param;
	}

	public static FallenType fromParam(String param) {
		for (FallenType type : values()) {
			if (type.param.equals(param)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Unknown FallenType: " + param);
	}

	public abstract Fallen create(int x, int y);
}
