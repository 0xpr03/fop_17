package de.tudarmstadt.informatik.fop.breakout.states;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.newdawn.slick.GameContainer;
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
 * Class representing the main menu state of the game
 * 
 * @author Aron Heinecke
 */
public class MainMenuState extends GameState<Breakout> {

	private final Logger logger = LogManager.getLogger(this);

	/**
	 * Creates a new instance of MainMenuState
	 * 
	 * @param stateID
	 * @param stateData
	 */
	public MainMenuState(int stateID, Breakout stateData) {
		super(stateID, stateData,stateData.getWidth(),stateData.getHeight());
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		logger.entry();
		AssetManager am = stateData.getAssetManager();
		
		am.loopSound(GameParameters.BACKGROUND_SONG_PATH);
		
		objects.add(new Background(am.getImg("images/menu.png"), this));

		objects.add(new Button(new Vector2f(200, 200), 200, 50, am.getImg("images/newgame_btn_d.png"),
				am.getImg("images/newgame_btn_m.png"), new ButtonAction() {
					@SuppressWarnings("rawtypes")
					@Override
					public void action(GameContainer container, StateBasedGame game, GameState state, int delta) {
						logger.trace("NewGame clicked");
						game.enterState(GameParameters.GAMEPLAY_STATE);
					}
				}));

		objects.add(new Button(new Vector2f(200, 260), 200, 50, am.getImg("images/highscore_btn_d.png"),
				am.getImg("images/highscore_btn_m.png"), new ButtonAction() {
					@SuppressWarnings("rawtypes")
					@Override
					public void action(GameContainer container, StateBasedGame game, GameState state, int delta) {
						logger.trace("Stats clicked");
						game.enterState(GameParameters.HIGHSCORE_STATE);
					}
				}));

		objects.add(new Button(new Vector2f(200, 320), 200, 50, am.getImg("images/options_btn_d.png"),
				am.getImg("images/options_btn_m.png"), new ButtonAction() {
					@SuppressWarnings("rawtypes")
					@Override
					public void action(GameContainer container, StateBasedGame game, GameState state, int delta) {
						logger.trace("Options clicked");
						game.enterState(GameParameters.OPTION_STATE);
					}
				}));

		objects.add(new Button(new Vector2f(200, 380), 200, 50, am.getImg("images/editor_btn_d.png"),
				am.getImg("images/editor_btn_m.png"), new ButtonAction() {
					@SuppressWarnings("rawtypes")
					@Override
					public void action(GameContainer container, StateBasedGame game, GameState state, int delta) {
						logger.trace("Editor clicked");
						game.enterState(GameParameters.EDITOR_STATE);
					}
				}));

		objects.add(new Button(new Vector2f(200, 440), 200, 50, am.getImg("images/about_btn_d.png"),
				am.getImg("images/about_btn_m.png"), new ButtonAction() {
					@SuppressWarnings("rawtypes")
					@Override
					public void action(GameContainer container, StateBasedGame game, GameState state, int delta) {
						logger.trace("About clicked");
						game.enterState(GameParameters.ABOUT_STATE);
					}
				}));

		objects.add(new Button(new Vector2f(200, 500), 200, 50, am.getImg("images/exit_btn_d.png"),
				am.getImg("images/exit_btn_m.png"), new ButtonAction() {
					@SuppressWarnings("rawtypes")
					@Override
					public void action(GameContainer container, StateBasedGame game, GameState state, int delta) {
						logger.trace("Exit clicked");
						container.exit();
					}
				}));

		logger.exit();
	}

}
