package at.jku.apidesign.orfapi.model;

import java.util.Arrays;
import java.util.List;

public enum TvStation {
	ORF1("Orf1"), ORF2("Orf2"), ORF3("Orf2"), ORFSPORTPLUS("Orf Sport +");

	private String tvStationName;

	private TvStation(String tvStationName) {
		this.tvStationName = tvStationName;
	}

	public String getTvStationname() {
		return tvStationName;
	}

	public static TvStation fromLabel(String tvStationName) throws IllegalArgumentException {
		List<TvStation> stations = Arrays.asList(TvStation.values());

		return stations.stream().filter(c -> c.getTvStationname().equalsIgnoreCase(tvStationName)).findFirst()
				.orElse(TvStation.ORF1);
	}
}
