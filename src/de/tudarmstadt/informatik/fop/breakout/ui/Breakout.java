package de.tudarmstadt.informatik.fop.breakout.ui;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.newdawn.slick.AppGameContainer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.ScalableGame;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.tudarmstadt.informatik.fop.breakout.constants.GameParameters;
import de.tudarmstadt.informatik.fop.breakout.states.HighscoreState;
import de.tudarmstadt.informatik.fop.breakout.states.InGameState;
import de.tudarmstadt.informatik.fop.breakout.states.MainMenuState;

/**
 * Main class creating the game
 * 
 * @author Aron Heinecke
 */
public class Breakout extends StateBasedGame implements GameParameters {

	// This has to be created in every class which wants to log smth
	// It doesn't have to be static
	private static Logger logger = LogManager.getLogger(Breakout.class);

	// Remember if the game runs in debug mode
	private static boolean debug = false;
	
	private static AppGameContainer app;

	/**
	 * Main function initiating the game
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		logger.info("Starting Breakout..");
		// () -> lambda: don't do "Arrays.toString.." when the info level isn't
		// even logged
		// -> only do this when the string returned is actually used
		logger.debug("args: {}", () -> Arrays.toString(args));
		logger.info("==== Logging test ====");
		loggerTest("Arg");
		logger.info("==== Test end ====");
		logger.debug("searching for binaries..");
		// Set the library path depending on the operating system
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			logger.trace("detected windows");
			System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + "/native/windows");
		} else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
			logger.trace("detected macos");
			System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + "/native/macosx");
		} else {
			logger.trace("detected linux");
			System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + "/native/"
					+ System.getProperty("os.name").toLowerCase());
		}

		try {
			Breakout breakout = new Breakout(true);
			app = new AppGameContainer(
					new ScalableGame(breakout, WINDOW_WIDTH, WINDOW_HEIGHT, true));
			app.setDisplayMode(WINDOW_WIDTH, WINDOW_HEIGHT, false);
			app.setTargetFrameRate(FRAME_RATE);
			app.setResizable(true);
			app.setMaximumLogicUpdateInterval(150);
			app.setTitle("Breakout");
			app.start();
			breakout.enterState(MAINMENU_STATE);
		} catch (Exception e) {
			logger.fatal("Crash escalation in main process: ", e);
		}
	}
	
	/**
	 * Returns the AppGameContainer
	 * @return AppGameContainer
	 */
	public static AppGameContainer getAppGameContainer(){
		return app;
	}

	/**
	 * Creates a new Breakout instance
	 * 
	 * @param debug
	 *            if true, runs in debug mode
	 */
	public Breakout(boolean debug) {
		super("Breakout");
		logger.trace("Debug: {}", debug);
		Breakout.debug = debug;
	}

	/**
	 * Returns current debug state
	 * 
	 * @return true on debug
	 */
	public static boolean getDebug() {
		return debug;
	}

	/**
	 * Logger demonstration class<br>
	 * Demonstrating multiple log levels, arguments etc
	 * 
	 * @param arg
	 *            An argument to be printed
	 */
	private static boolean loggerTest(String arg) {
		logger.entry(arg); // log function entry and args
		logger.trace(arg); // lowest log priority
		// second lowest
		logger.debug("Multiple {} INfos {} To {} Print {}", "asd", 45, true, new Exception("asd"));
		// third lowest
		// we will probably disable everything below info in production
		logger.info("Info MSG {}", true);
		// fourth, warning
		logger.warn("Some warning stuff {}", 4645L);
		// fith
		logger.error("Error Test: {}", new Exception("My Exception"));
		// highest
		logger.fatal("Fatal log");
		boolean ret = true;
		logger.exit(ret); // log function exit & return
		return ret;
	}

	@Override
	public void initStatesList(GameContainer arg0) throws SlickException {
		addState(new MainMenuState(MAINMENU_STATE));
		addState(new InGameState(GAMEPLAY_STATE));
		addState(new HighscoreState(HIGHSCORE_STATE));
	}
}