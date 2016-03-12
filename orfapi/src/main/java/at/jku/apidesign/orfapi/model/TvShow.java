package at.jku.apidesign.orfapi.model;

import java.util.Date;

public class TvShow {
	private String title, subtitle;
	private String tvSender; // Fernsehsender?
	private Date from, to;

	public TvShow() {
	}

	public TvShow(String title, String subtitle, Date from, Date to) {
		super();
		this.title = title;
		this.subtitle = subtitle;
		this.from = from;
		this.to = to;
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

	public String getTvSender() {
		return tvSender;
	}

	public void setTvSender(String tvSender) {
		this.tvSender = tvSender;
	}

	public long getDurationInMinutes() {
		long diff = to.getTime() - from.getTime();
		long diffMinutes = diff / (60 * 1000) % 60;
		return diffMinutes;
	}
}
