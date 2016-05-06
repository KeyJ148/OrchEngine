package tow.player.bullet;

import tow.image.TextureManager;
import tow.player.Bullet;
import tow.player.Player;

public class BStreamlined extends Bullet {

	public BStreamlined(Player player, double x, double y, double dir, double damage, int range) {
		super(player, x, y, dir, damage, range, TextureManager.b_streamlined);
	}

}
