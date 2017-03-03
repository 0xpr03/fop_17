package de.tudarmstadt.informatik.fop.breakout.lib;

import gameObjects.Ball;
import gameObjects.Block;

/**
 * Interface required for Ball, to be callable<br>
 * Containing game events for ball changes
 * 
 * @author Aron Heinecke
 *
 */
public interface GameEvent {
	/**
	 * Event fired on ball loss
	 * 
	 * @param ball
	 *            Ball lost
	 */
	public void ballLost(Ball ball);

	/**
	 * Event fired on block hit
	 * 
	 * @param block
	 *            hit block
	 */
	public void blockHit(Block block);
	// public void itemCollected(Item e);
}
