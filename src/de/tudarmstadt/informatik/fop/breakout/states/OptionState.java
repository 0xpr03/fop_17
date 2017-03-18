package de.tudarmstadt.informatik.fop.breakout.states;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import de.tudarmstadt.informatik.fop.breakout.constants.GameParameters;
import de.tudarmstadt.informatik.fop.breakout.gui.Background;
import de.tudarmstadt.informatik.fop.breakout.gui.Button;
import de.tudarmstadt.informatik.fop.breakout.gui.Button.ButtonAction;
import de.tudarmstadt.informatik.fop.breakout.lib.AssetManager;
import de.tudarmstadt.informatik.fop.breakout.ui.Breakout;

/**
 * Class representing the Option state of the game
 * This gives several Options like enable and disable CE-Exercise
 * 
 * @author Tim J�ger
 *
 */

public class OptionState extends GameState<Breakout>{
	
	private Logger logger = LogManager.getLogger();

	public OptionState(final int stateID, final Breakout stateData) {
		super(stateID, stateData, stateData.getWidth(), stateData.getHeight());
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException{
		super.enter(container, game);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		logger.entry();
		AssetManager am = stateData.getAssetManager();
		objects.add(new Background(am.getImg("images/menu.png"), this));

		objects.add(new Button(new Vector2f(400, 500), 150, 50, am.getImg("images/back_btn_d.png"),
				am.getImg("images/back_btn_m.png"), new ButtonAction() {
					@SuppressWarnings("rawtypes")
					@Override
					public void action(GameContainer container, StateBasedGame game, GameState state, int delta) {
						logger.trace("Exit clicked");
						game.enterState(GameParameters.MAINMENU_STATE);
					}
				}));

		objects.add(new Button(new Vector2f(300, 200), 150, 50, am.getImg("images/clear_btn_d.png"),
				am.getImg("images/clear_btn_m.png"), new ButtonAction() {
					@SuppressWarnings("rawtypes")
					@Override
					public void action(GameContainer container, StateBasedGame game, GameState state, int delta) {
						logger.trace("Enable CE-Exercise clicked");
						stateData.getIngState().setEnableCE(true);
					}
				}));
		objects.add(new Button(new Vector2f(300, 275), 150, 50, am.getImg("images/clear_btn_d.png"),
				am.getImg("images/clear_btn_m.png"), new ButtonAction() {
					@SuppressWarnings("rawtypes")
					@Override
					public void action(GameContainer container, StateBasedGame game, GameState state, int delta) {
						logger.trace("Disable CE-Exercise clicked");
						stateData.getIngState().setEnableCE(false);
					}
				}));
	}
	
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException{
		super.render(container, game, g);
	}

	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException{
		super.update(container, game, delta);
		Input input = container.getInput();
		if(input.isKeyPressed(Input.KEY_ESCAPE)){
			game.enterState(GameParameters.MAINMENU_STATE);
		}
	}
}