package de.tudarmstadt.informatik.fop.breakout.lib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tudarmstadt.informatik.fop.breakout.test.adapter.IHighscoreEntry;

/**
 * Highscore library<br>
 * Handling the highscore loading / saving and sorting
 * 
 * @author Aron Heinecke
 *
 */
public class HighscoreLib {
	private final Logger logger = LogManager.getLogger(this);
	private ArrayList<HighscoreEntry> highscore = new ArrayList<>();
	private final File file;
	private final int maxEntries;

	private final static int C_NAME_POS = 0;
	private final static int C_BLOCKS_POS = 1;
	private final static int C_TIME_POS = 2;
	private final static char C_SEPARATOR = ':';

	/**
	 * Creates a new HighscoreLib instance
	 * 
	 * @param maxEntries
	 *            Maximum Entries the highscore list should carry and return
	 */
	public HighscoreLib(final int maxEntries) {
		this(new File("highschore.hsc"), maxEntries);
	}

	/**
	 * Creates a new HighscoreLib instance<br>
	 * Test constructor for highschore tests
	 * 
	 * @param maxEntries
	 *            Maximum Entries the highscore list should carry and return
	 * @param file
	 *            the File that should be read/written to
	 */
	public HighscoreLib(final File file, final int maxEntries) {
		this.file = file;
		logger.debug(file.getAbsolutePath());
		this.maxEntries = maxEntries;
		if (file.exists())
			load();
	}

	/**
	 * Load highscore from file
	 */
	private void load() {
		synchronized (highscore) {
			String line = "";
			highscore.clear();
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				while ((line = br.readLine()) != null) {
					String[] split = line.split(":");
					if (split.length < 3) {
						logger.error("Unable to parse line! {}", line);
					} else {
						HighscoreEntry highscoreEntry = new HighscoreEntry(split[C_NAME_POS],
								Integer.valueOf(split[C_BLOCKS_POS]), Float.valueOf(split[C_TIME_POS]));
						highscore.add(highscoreEntry);
					}
				}
				Collections.sort(highscore);
			} catch (IOException e) {
				logger.error("Unable to read highscore file: ", e);
			} catch (NumberFormatException e) {
				logger.error("Malformed highscore file\nLine:{}\n{}", line, e);
			}
		}
	}

	/**
	 * Save highscore to file<br>
	 * But only the highest 10 entries
	 */
	private void save() {
		synchronized (highscore) {
			try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false)))) {
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < maxEntries && i < highscore.size(); i++) {
					HighscoreEntry e = highscore.get(i);
					sb.append(e.getPlayerName()); // C_NAME_POS
					sb.append(C_SEPARATOR);
					sb.append(e.getNumberOfDestroyedBlocks()); // C_BLOCKS_POS
					sb.append(C_SEPARATOR);
					sb.append(e.getElapsedTime()); // C_TIME_POS
					sb.append("\n");
				}

				bw.write(sb.toString());
				bw.flush();
			} catch (IOException e) {
				logger.error("Unable to write highscore file: ", e);
			}
		}
	}

	/**
	 * Clear highscore
	 */
	public void clear() {
		synchronized (highscore) {
			highscore.clear();
			save();
		}
	}

	/**
	 * Add new entry to the highscore<br>
	 * This includes different values for the same player
	 * 
	 * @param e
	 *            Entry
	 */
	public void addEntry(final HighscoreEntry e) {
		synchronized (highscore) {
			e.name = e.name.replaceAll(":", ""); // sanitize
			highscore.add(e);
			Collections.sort(highscore);
			save();
		}
	}

	/**
	 * Returns the highest X entries<br>
	 * while x was defined at constructing this class
	 * 
	 * @return ArrayList<Entry>
	 */
	public ArrayList<HighscoreEntry> getHighscore() {
		ArrayList<HighscoreEntry> output = new ArrayList<>();
		synchronized (highscore) {
			for (int i = 0; i < maxEntries && i < highscore.size(); i++) {
				output.add(highscore.get(i));
			}
		}
		return output;
	}

	/**
	 * An entry in the highscore<br>
	 * Comparable for Collections
	 * 
	 * @author Aron Heinecke
	 *
	 */
	public static class HighscoreEntry implements Comparable<HighscoreEntry>, IHighscoreEntry {
		private String name;
		private final int blocks;
		private final float time;

		/**
		 * Creates a new Entry
		 * 
		 * @param name
		 *            Player name
		 * @param score
		 *            Player score
		 */
		public HighscoreEntry(final String name, final int score, final float time) {
			this.name = name;
			this.blocks = score;
			this.time = time;
		}

		/**
		 * Returns the numer of destroyed blocks
		 * 
		 * @return the amount of destroyed blocks
		 */
		public int getNumberOfDestroyedBlocks() {
			return blocks;
		}

		/**
		 * Returns the name of the player
		 * 
		 * @return the name
		 */
		public String getPlayerName() {
			return name;
		}

		/**
		 * Returns the elapsed time
		 * 
		 * @return the time
		 */
		public float getElapsedTime() {
			return time;
		}

		@Override
		public int compareTo(final HighscoreEntry o) {
			if (this.blocks == o.blocks) {
				if (this.time < o.getElapsedTime())
					return -1;
				else if (this.time == o.getElapsedTime())
					return 0;
				else
					return 1;
			} else {
				return this.blocks < o.getNumberOfDestroyedBlocks() ? 1 : -1;
			}
		}

		@Override
		public double getPoints() {
			return getNumberOfDestroyedBlocks();
		}
	}
}
