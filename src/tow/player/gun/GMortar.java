package tow.player.gun;

import tow.image.TextureManager;
import tow.player.Gun;
import tow.player.Player;

public class GMortar extends Gun {

	public GMortar(Player player) {
		super(player, TextureManager.g_mortar);
	}

}
