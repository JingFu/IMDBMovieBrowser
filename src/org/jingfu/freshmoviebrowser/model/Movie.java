package org.jingfu.freshmoviebrowser.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
	private String title;
	private String mpaaRating;
	private String id;
	private String imdbURL;
	private String thumbURL;
	private double rating;
	private String synopsis;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMpaaRating() {
		return mpaaRating;
	}

	public void setMpaaRating(String mpaaRating) {
		this.mpaaRating = mpaaRating;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImdbURL() {
		return imdbURL;
	}

	public void setImdbURL(String imdbURL) {
		this.imdbURL = imdbURL;
	}

	public String getThumbURL() {
		return thumbURL;
	}

	public void setThumbURL(String thumbURL) {
		this.thumbURL = thumbURL;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public Movie() {
	}

	private Movie(Parcel in) {

		title = in.readString();
		mpaaRating = in.readString();
		id = in.readString();
		imdbURL = in.readString();
		thumbURL = in.readString();
		rating = in.readFloat();
		synopsis = in.readString();
	}
	
	

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) {

		out.writeString(title);
		out.writeString(mpaaRating);
		out.writeString(id);
		out.writeString(imdbURL);
		out.writeString(thumbURL);
		out.writeDouble(rating);
		out.writeString(synopsis);
	}

	public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
		public Movie createFromParcel(Parcel in) {
			Movie m = new Movie(in);
			return m;
		}

		public Movie[] newArray(int size) {
			return new Movie[size];
		}
	};
}
