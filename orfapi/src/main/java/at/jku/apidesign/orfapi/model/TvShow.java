package at.jku.apidesign.orfapi.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TvShow {
	private String title, subtitle;
	private String tvStation; // Fernsehsender?
	private Date from, to;

	public static final DateFormat TV_DATE_FORMAT = new SimpleDateFormat("HH:mm");

	public TvShow() {
	}

	public TvShow(String title, String subtitle, Date from, Date to, String tvSender) {
		super();
		this.title = title;
		this.subtitle = subtitle;
		this.from = from;
		this.to = to;
		this.tvStation = tvSender;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public String getTvStation() {
		return tvStation;
	}

	public void setTvStation(String tvSender) {
		this.tvStation = tvSender;
	}

	public long getDurationInMinutes() {
		long diff = to.getTime() - from.getTime();
		long diffMinutes = diff / (60 * 1000) % 60;
		return diffMinutes;
	}

	@Override
	public String toString() {
		return getTvStation() + ": " + getTitle() + " - " + getSubtitle() + " (" + TV_DATE_FORMAT.format(getFrom())
				+ ")";
	}

}
