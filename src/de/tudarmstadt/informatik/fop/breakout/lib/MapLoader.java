package de.tudarmstadt.informatik.fop.breakout.lib;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import de.tudarmstadt.informatik.fop.breakout.gameObjects.Block;

/**
 * Map (game) Loader<br>
 * 
 * @author Aron Heinecke
 *
 */
public class MapLoader {
	private final Logger logger = LogManager.getLogger(this);
	private final int width;
	private final int height;
	private final AssetManager am;
	private final int widthStone = 50;
	private final int heightStone = 30;

	/**
	 * Creates a new MapLoader instance
	 * 
	 * @param width
	 *            Game display width
	 * @param height
	 *            Game dsplay height
	 * @param state
	 *            GameState to which the objects should be added
	 */
	public MapLoader(int width, int height, AssetManager am) {
		this.width = width;
		this.height = height;
		this.am = am;
	}

	/**
	 * Load map into scenery & return LoadData with theme information
	 * 
	 * @param map
	 * @throws SlickException
	 */
	public LoadData loadMap(Map map) throws SlickException {
		logger.entry("Loading map {}", map.getAbsolutePath());
		ArrayList<Block> blockList = new ArrayList<>(25);
		ArrayList<Block> destroyableBlockList = new ArrayList<>(1);
		ArrayList<ArrayList<Block>> testMap = new ArrayList<>(5);
		map.load();
		int maxRowElements = width / widthStone;
		if (maxRowElements < map.getMaxRowLength()) {
			logger.error("Unable to load Map {}, map too big. Too much elements: {}/{}", map
					.getAbsolutePath(), maxRowElements, map.getMaxRowLength());
		}
		int maxRows = height / heightStone;
		if (maxRows < map.getMap().size()) {
			logger.error("Unable to load Map {], map too big. Too much Rows: {}/{}", map.getMap().size(), maxRows);
		}

		// Set X-offset so it's centered + half the width of a stone
		int startOffsetX = (width - map.getMaxRowLength() * widthStone) / 2 + widthStone / 2;
		// Set Y-offset
		int offsetY = heightStone / 2;
		for (ArrayList<Integer> row : map.getMap()) {
			int offsetX = startOffsetX;
			ArrayList<Block> rowMap = new ArrayList<>(row.size());
			for (int vStone : row) {
				Block block = null;
				if (vStone != 0) {
					block = new Block(new Vector2f(offsetX, offsetY), widthStone, heightStone, vStone, am,
							map.getTheme());
					if(vStone > 0)
						blockList.add(block);
					else
						destroyableBlockList.add(block);
				}
				rowMap.add(block);
				offsetX += widthStone;
			}
			testMap.add(rowMap);
			offsetY += heightStone;
		}
		logger.exit();
		return getLoadData(map.getTheme(), blockList,destroyableBlockList,testMap);
	}

	/**
	 * Loader Data for the specified theme<br>
	 * This class stores images to be used for some elements
	 * 
	 * @author Aron Heinecke
	 *
	 */
	public class LoadData {
		public Image pBackground;
		public Image pStick;
		public Image pBall;
		public ArrayList<Block> destroyableBlockList;
		public ArrayList<Block> undestroyableBlockList;
		public ArrayList<ArrayList<Block>> testBlockMap;
	}

	/**
	 * Returns LoadData for the specified theme
	 * 
	 * @param theme Theme to use
	 * @param destroyableBlockListestBlockMapt List of destroyable blocks
	 * @param undestroyableBlockList List of undestroyable blocks
	 * @param testData 2d List of the Blocks for testing purposes
	 * @return
	 * @throws SlickException
	 */
	private LoadData getLoadData(int theme, ArrayList<Block> destroyableBlockList, ArrayList<Block> undestroyableBlockList, ArrayList<ArrayList<Block>> testData) throws SlickException {
		logger.entry(theme);
		LoadData ld = new LoadData();
		ld.destroyableBlockList = destroyableBlockList;
		ld.undestroyableBlockList = undestroyableBlockList;
		ld.testBlockMap = testData;
		switch (theme) {
		case 0:
		default:
			ld.pBackground = am.getImg("images/background.png");
			ld.pBall = am.getImg("images/ball.png");
			ld.pStick = am.getImg("images/stick.png");
		}
		return ld;
	}
}
