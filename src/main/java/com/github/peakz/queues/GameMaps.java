package com.github.peakz.queues;

import java.util.Random;

public class GameMaps {

	// String arrays of each game mode and its respective maps.
	private final String[] ESCORT = {"Dorado", "Route 66", "Watchpoint: Gibraltar", "Junkertown"};
	private final String[] HYBRID = {"Eichenwalde", "Hollywood", "King's Row", "Numbani"};
	private final String[] CONTROL = {"Ilios", "Lijiang Tower", "Nepal", "Oasis"};
	private final String[] ASSAULT = {"Hanamura", "Horizon Lunar Colony", "Temple of Anubis", "Volskaya Industries"};
	private final String[][] MODES = {ESCORT, HYBRID, CONTROL, ASSAULT};
	private int antiRNG;

	public GameMaps() {
		this.antiRNG = 0;
	}

	public String selectMap() {
		Random random = new Random();
		int n = random.nextInt(3);
		int m;

		switch (n) {
			case 0:
				m = random.nextInt(2);
				return MODES[n][m];
			case 1:
				m = random.nextInt(3);
				return MODES[n][m];
			case 2:
				m = random.nextInt(3);
				return MODES[n][m];
			case 3:
				if (antiRNG < 2) {
					m = random.nextInt(3);
					antiRNG++;
					return MODES[n][m];
				} else {
					antiRNG = 0;
					return MODES[random.nextInt(2)][1];
				}
		}
		return null;
	}

	String getMapImgUrl(String map) {
		switch (map) {
			case "Dorado":
				return "https://vignette.wikia.nocookie.net/overwatch/images/5/51/Dorado_screenshot_9.png/revision/latest/scale-to-width-down/165?cb=20160630045807";

			case "Route 66":
				return "https://vignette.wikia.nocookie.net/overwatch/images/8/87/Route66_screenshot_1.png/revision/latest/scale-to-width-down/165?cb=20160708033615";

			case "Watchpoint: Gibraltar":
				return "https://vignette.wikia.nocookie.net/overwatch/images/7/73/Gibraltar_screenshot_1.png/revision/latest/scale-to-width-down/165?cb=20160710225230";

			case "Eichenwalde":
				return "https://vignette.wikia.nocookie.net/overwatch/images/5/5e/Eichenwalde_sreenshot_1.png/revision/latest/scale-to-width-down/165?cb=20160823041053";

			case "Hollywood":
				return "https://vignette.wikia.nocookie.net/overwatch/images/9/98/Hollywood_screenshot_4.jpg/revision/latest/scale-to-width-down/165?cb=20160605011629";

			case "King's Row":
				return "https://vignette.wikia.nocookie.net/overwatch/images/2/21/Kingsrow_screenshot_8.png/revision/latest/scale-to-width-down/165?cb=20160711195544";

			case "Numbani":
				return "https://vignette.wikia.nocookie.net/overwatch/images/e/eb/Numbani_screenshot_5.jpg/revision/latest/scale-to-width-down/165?cb=20160504022045";

			case "Ilios":
				return "https://vignette.wikia.nocookie.net/overwatch/images/7/7d/Ilios_screenshot_1.png/revision/latest/scale-to-width-down/165?cb=20160712060605";

			case "Lijiang Tower":
				return "https://vignette.wikia.nocookie.net/overwatch/images/e/ed/Lijiang_screenshot_34.jpg/revision/latest/scale-to-width-down/165?cb=20160711182404";

			case "Junkertown":
				return "https://vignette.wikia.nocookie.net/overwatch/images/7/72/Junkertown_screenshot_1.png/revision/latest/scale-to-width-down/165?cb=20170921224435";

			case "Nepal":
				return "https://vignette.wikia.nocookie.net/overwatch/images/7/7a/Nepal_screenshot_1.png/revision/latest/scale-to-width-down/165?cb=20160711235021";

			case "Oasis":
				return "https://vignette.wikia.nocookie.net/overwatch/images/7/70/Oasis_screenshot_first.png/revision/latest/scale-to-width-down/165?cb=20161203031638";

			case "Hanamura":
				return "https://vignette.wikia.nocookie.net/overwatch/images/d/dd/Hanamura_screenshot_21.png/revision/latest/scale-to-width-down/165?cb=20160626193608";

			case "Horizon Lunar Colony":
				return "https://vignette.wikia.nocookie.net/overwatch/images/b/b8/Horizon_screenshot_1.png/revision/latest/scale-to-width-down/165?cb=20170620220601";

			case "Temple of Anubis":
				return "https://vignette.wikia.nocookie.net/overwatch/images/1/12/Anubis_screenshot_4.png/revision/latest/scale-to-width-down/165?cb=20160628005051";

			case "Volskaya Industries":
				return "https://vignette.wikia.nocookie.net/overwatch/images/9/93/Volskaya_screenshot_13.png/revision/latest/scale-to-width-down/165?cb=20160704000922";

			default:
				return null;
		}
	}
}
