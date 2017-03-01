package de.tudarmstadt.informatik.fop.breakout.gui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import de.tudarmstadt.informatik.fop.breakout.states.GameState;
import gameObjects.GameObject;
import gameObjects.Sprite;


/**
 * A Button with mouse over visualization and click recognition
 * 
 * @author Simon Kohaut 
 */
public class Button extends GUIElement {

	private Image defaultImage;
	private Image mouseOverImage;
	private boolean mouseOverButton;
	private ButtonAction action;
	
	/**
	 * Create a new Button
	 * 
	 * @param position
	 *            The position of the center of the button
	 * @param width
	 *            The width of the button
	 * @param height
	 *            The height of the button
	 * @param defaultImage
	 *            The default Image to represent the Button on screen
	 * @param mouseOverImage
	 *            The Image to represent the Button if the cursor is hovering
	 *            over the Button
	 */
	public Button(Vector2f position, float width, float height, Image defaultImage, Image mouseOverImage, ButtonAction action) {
		super(defaultImage, position, width, height);
		this.defaultImage = defaultImage;
		this.mouseOverImage = mouseOverImage;
		this.action = action;
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, GameState state, int delta) {
		if(isMouseOver(container))
			image = mouseOverImage;
		else
			image = defaultImage;
		
		if(isClicked(container))//mouseOverButton && in.isMousePressed(Input.MOUSE_LEFT_BUTTON))
			action.action(container, game, state, delta);
	}
	
	/**
	 * Button action class
	 * 
	 * @author Aron Heinecke
	 */
	public static abstract class ButtonAction {
		/**
		 * Action on button click
		 * @param container GameContainer
		 * @param game StateBasedGame
		 * @param state GameState
		 * @param delta Delta
		 */
		public abstract void action(GameContainer container, StateBasedGame game, GameState state, int delta);
	}
	
}
