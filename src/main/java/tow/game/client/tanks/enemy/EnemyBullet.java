package tow.game.client.tanks.enemy;

import tow.engine.Global;
import tow.engine.image.TextureHandler;
import tow.engine.image.TextureManager;
import tow.engine.obj.Obj;
import tow.engine.obj.ObjFactory;
import tow.engine.obj.components.Movement;
import tow.engine.obj.components.Position;
import tow.engine.obj.components.particles.Particles;
import tow.engine.obj.components.render.Sprite;
import tow.game.client.particles.Explosion;

import java.util.Arrays;

public class EnemyBullet extends Obj {

	public int idEnemy;
	public long idNet;
	
	public EnemyBullet(double x, double y, double speed, double direction, TextureHandler texture, int idEnemy, long idNet){
		super(Arrays.asList(new Position(x, y, texture.depth, (int) direction), new Movement(speed, direction), new Sprite(texture)));

		this.idEnemy = idEnemy;
		this.idNet = idNet;

		if (texture.equals(TextureManager.getTexture("b_streamlined"))){
			getComponent(Movement.class).directionDrawEquals = true;
			getComponent(Position.class).setDirectionDraw(0);
		} else {
			getComponent(Movement.class).directionDrawEquals = false;
		}
	}

	public void destroy(int explosionSize){
		destroy();

		if (explosionSize > 0) {
			Obj explosion = ObjFactory.create(getComponent(Position.class).x, getComponent(Position.class).y, -100);
			explosion.setComponent(new Explosion(explosionSize));
			explosion.getComponent(Particles.class).destroyObject = true;
			Global.location.objAdd(explosion);
		}
	}
}
