package gameObjects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import de.tudarmstadt.informatik.fop.breakout.gui.Background;
import de.tudarmstadt.informatik.fop.breakout.states.GameState;

/**
 * @author Tim Jäger
 *
 *         Class to abstract the Ball
 */
public class Ball extends Sprite {

	private Vector2f direction = new Vector2f(0, 0);

	private Logger logger = LogManager.getLogger(this);
	private float basicVelocity;
	private float gravity;
	private float radius;
	private int delta;
	private boolean collided = false;

	/**
	 * Create a new Ball instance
	 * 
	 * @param position
	 *            The position of the center of the ball
	 * @param radius
	 *            The radius of the ball
	 * @param image
	 *            The image to represent the Ball on screen
	 */
	public Ball(Vector2f position, float radius, Image image, float velocity, float gravity) {
		super(image, position, radius * 2, radius * 2, false);
		this.basicVelocity = basicVelocity;
		this.gravity = gravity;
		this.radius = radius;
	}

	/**
	 * Detect if the Ball collides with a GameObject and act according to it
	 * 
	 * @param o
	 *            The object which may be colliding with the Ball
	 * @return If the Ball collides with the object
	 */
	public void collide(GameObject o) {
		if(!o.isCollideable())
			return;
		
		Vector2f half = new Vector2f(o.width / 2.0f, o.height / 2.0f);	
		Vector2f distance = new Vector2f(Math.abs(position.x - o.position.x), 
				Math.abs(position.y - o.position.y));
		Vector2f delta = new Vector2f(distance.x - half.x, distance.y - half.y);
		
		if(distance.x > (half.x + radius) || distance.y > (half.y + radius))
			return;
		// Left/Right
		if(distance.x <= half.x + radius && distance.y <= half.y) {
			direction.x = -direction.x;
		    collided = true;
		}
		// Top/Bottom
		else if(distance.x <= half.x && distance.y <= half.y + radius){
			direction.y = -direction.y;
			collided = true;
		}
		// Corner
		else if(delta.x * delta.x + delta.y * delta.y <= radius * radius) {
			direction.setTheta(direction.getTheta() - 2 * new Vector2f(position.x - o.position.x, o.position.y - position.y).getTheta());
			collided = true;
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, GameState<?> state, int delta) {
		// Ball crossing ...
		// ... top border and bouncing back
		if (position.y - radius <= 0)
			direction.set(direction.x, -direction.y);
		// ... right or left border and bouncing back
		else if (position.x + radius >= container.getWidth() || position.x - radius <= 0)
			direction.set(-direction.x, direction.y);				
		// ... bottom edge and getting removed
		else if (position.y - radius >= container.getHeight())
			state.removeObject(this);
		
		// Start Ball movement by pressing space
		if (direction.length() == 0 && container.getInput().isKeyPressed(Input.KEY_SPACE))
			direction.set(1, delta/3);				
			
		// Test for collision with all GameObjects
		for (GameObject object : state.getStateObjects())			
			if(!collided)
				collide(object);
			else 
				break;
		
		collided = false;

		// Calculate new position
		position.set(position.x + direction.getX(), position.y + direction.getY());
	}

	/**
	 * Set the direction of the ball
	 * 
	 * @param dirx
	 *            direction in X-Achsis
	 * @param diry
	 *            direction in Y-Achsis
	 */
	public void setDirection(float dirx, float diry) {
		direction.set(dirx, diry);
	}
}
