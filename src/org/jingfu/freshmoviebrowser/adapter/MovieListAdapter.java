package org.jingfu.freshmoviebrowser.adapter;

import java.util.List;

import org.jingfu.freshmoviebrowser.R;
import org.jingfu.freshmoviebrowser.model.Movie;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public class MovieListAdapter extends ArrayAdapter<Movie> {
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader = null;

	public MovieListAdapter(Context context, int itemLayoutId, int defaultTextId, List<Movie> movies, ImageLoader imageLoader) {
		super(context, itemLayoutId, defaultTextId, movies);
		layoutInflater = LayoutInflater.from(context);
		this.imageLoader = imageLoader;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.movie_list_item, null);

			holder = new ViewHolder();
			
			// use the Volley library's NetworkImageView
			holder.setThumbnail((NetworkImageView) convertView.findViewById(R.id.movie_thumbnail));;
			holder.setTitle((TextView) convertView.findViewById(R.id.movie_title));
			holder.setMpaaRating(((TextView) convertView.findViewById(R.id.movie_mpaa_rating)));
			holder.setRating((RatingBar) convertView.findViewById(R.id.movie_rating));

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Movie movie = this.getItem(position);

		// Volley method to load network image into the view
		holder.getThumbnail().setImageUrl(movie.getThumbURL(), imageLoader);
		holder.getTitle().setText(movie.getTitle());
		holder.getMpaaRating().setText(movie.getMpaaRating());
		holder.getRating().setRating((float)movie.getRating());

		return convertView;
	}

	class ViewHolder {
		private NetworkImageView thumbnail;
		private TextView title;
		private TextView mpaaRating;
		private RatingBar rating;
		public NetworkImageView getThumbnail() {
			return thumbnail;
		}
		public void setThumbnail(NetworkImageView thumbnail) {
			this.thumbnail = thumbnail;
		}
		public TextView getTitle() {
			return title;
		}
		public void setTitle(TextView title) {
			this.title = title;
		}
		public TextView getMpaaRating() {
			return mpaaRating;
		}
		public void setMpaaRating(TextView mpaaRating) {
			this.mpaaRating = mpaaRating;
		}
		public RatingBar getRating() {
			return rating;
		}
		public void setRating(RatingBar rating) {
			this.rating = rating;
		}
		
	}

}
