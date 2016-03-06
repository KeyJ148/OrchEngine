package tow.player.bullet;

import tow.*;
import tow.player.*;

public class DefaultBullet extends Bullet{
	
	public DefaultBullet(Player player, double x,double y, double dir,double damage, int range){
		super(player,x, y, dir,damage, range, Global.b_default);
	}
	
	@Override
	public void directionDrawEqulas(){
		setDirectionDraw(0);
	}
}
