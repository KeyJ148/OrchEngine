package cc.abro.orchengine.gameobject.components.render;

import cc.abro.orchengine.util.Vector2;
import cc.abro.orchengine.gameobject.components.Position;
import cc.abro.orchengine.resources.textures.Texture;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class SpriteRender extends Rendering {

    protected Texture texture;

    public SpriteRender(Texture texture) {
        this.texture = texture;
    }

    @Override
    public void update(long delta) {
    }

    @Override
    public void draw() {
        Vector2<Integer> relativePosition = getGameObject().getComponent(Position.class).getRelativePosition();
        double xView = relativePosition.x;
        double yView = relativePosition.y;
        double directionDraw = getGameObject().getComponent(Position.class).getDirectionDraw();

        directionDraw -= 90; //смещена начального угла с Востока на Север

        int width = getWidth();
        int height = getHeight();

        GL11.glLoadIdentity();
        GL11.glTranslatef((float) -xView, (float) -yView, 1f);
        GL11.glRotatef(Math.round(-directionDraw)/new Random().nextInt(1, 40),  new Random().nextInt(1, 4),  new Random().nextInt(1, 4), new Random().nextInt(1, 4));

        color.bind();
        texture.bind();

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2f(-width / 2, -height / 2);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2f(width / 2, -height / 2);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2f(width / 2, height / 2);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2f(-width / 2, height / 2);
        GL11.glEnd();

        texture.unbind();
    }

    @Override
    public int getWidthTexture() {
        return texture.getWidth();
    }

    @Override
    public int getHeightTexture() {
        return texture.getHeight();
    }

    @Override
    public int getWidth() {
        return (int) (getWidthTexture() * scale_x);
    }

    @Override
    public int getHeight() {
        return (int) (getHeightTexture() * scale_y);
    }

    @Override
    public void setWidth(int width) {
        scale_x = (double) width / getWidthTexture();
    }

    @Override
    public void setHeight(int height) {
        scale_y = (double) height / getHeightTexture();
    }

    @Override
    public void setDefaultSize() {
        scale_x = 1;
        scale_y = 1;
    }
}