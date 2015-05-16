package org.jingfu.freshmoviebrowser;

import org.jingfu.freshmoviebrowser.fragments.MovieDetailsFragment;
import org.jingfu.freshmoviebrowser.fragments.MovieListFragment;
import org.jingfu.freshmoviebrowser.imdb.IMDBClientFragment;
import org.jingfu.freshmoviebrowser.imdb.JSONParser;
import org.jingfu.freshmoviebrowser.interfaces.IMDBResponseListener;
import org.jingfu.freshmoviebrowser.interfaces.MovieSelectionListener;
import org.jingfu.freshmoviebrowser.model.Movie;
import org.json.JSONArray;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

public class MovieBrowserActivity extends Activity implements IMDBResponseListener, MovieSelectionListener {
	private static final String TAG = "MovieBrowserActivity";
	private static final String IMDB_FRAGMENT_TAG = "IMDBClientFragment";
	private static final String MOVIE_LIST_FRAGMENT_TAG = "MovieListFragment";
	private static final String MOVIE_DETAILS_FRAGMENT_TAG = "MovieDetailsFragment";
	private static final String MOVIE_TYPE_IN_THEATRES = "In Theatres";
	private static final String MOVIE_TYPE_OPENING = "Opening";
	
	private static final String	SELECTED_TYPE_IDX = "SELECTED_TYPE_IDX";
	private static final String TYPE_CHANGED = "TYPE_CHANGED";
	private static final int NO_SELECTION = -1;

	private int	selectedTypePosition = NO_SELECTION;
	private boolean	typeChanged = false;
	
	private FragmentManager	fragmentManager;
	private IMDBClientFragment imdbClientFragment;
	private ArrayAdapter<String> actionBarNavAdapter;
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate()");
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_movie_browser);

		SlidingPaneLayout sliding_layout = (SlidingPaneLayout) findViewById(R.id.sliding_layout);
		sliding_layout.setSliderFadeColor(getResources().getColor(android.R.color.transparent));
		sliding_layout.setPanelSlideListener(new SliderListener());
		sliding_layout.openPane();
		

		fragmentManager = getFragmentManager();

		addNonUIFragments();

		if (savedInstanceState != null) {
			selectedTypePosition = savedInstanceState.getInt(SELECTED_TYPE_IDX);
			typeChanged = savedInstanceState.getBoolean(TYPE_CHANGED);
			Log.d(TAG, "onCreate() : retrieved selected suburb position = " + selectedTypePosition);
			Log.d(TAG, "onCreate() : retrieved typeChanged = " + typeChanged);
		}

		setUpActionBar();
	}

	/*
	 * get reference to the fragment. If we haven't already instantiated it (this is a 'clean' start of the activity) then create an
	 * instance and add it to the activity (but not in the UI)
	 */
	private void addNonUIFragments() {
		Log.d(TAG, "addNonUIFragments()");

		imdbClientFragment = (IMDBClientFragment) fragmentManager.findFragmentByTag(IMDB_FRAGMENT_TAG);

		FragmentTransaction ft = fragmentManager.beginTransaction();

		if (imdbClientFragment == null) {
			imdbClientFragment = new IMDBClientFragment();
			ft.add(imdbClientFragment, IMDB_FRAGMENT_TAG);
		}

		ft.commit();
		fragmentManager.executePendingTransactions();
	}


	/*
	 * Configure the ActionBar appearance and behaviour. Add a predefined list of items to the ActionBar navigation drop down list. Register
	 * this activity to handle navigation selections.
	 */
	private void setUpActionBar() {
		Log.d(TAG, "setUpActionBar()");

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		String[] type_list = getResources().getStringArray(R.array.type_list);

		actionBarNavAdapter = new ArrayAdapter<String>(actionBar.getThemedContext(), android.R.layout.simple_list_item_1,
				android.R.id.text1);
		actionBarNavAdapter.addAll(type_list);

		actionBar.setListNavigationCallbacks(actionBarNavAdapter, new TypeListNavigationListener());
	}
	
	class TypeListNavigationListener implements ActionBar.OnNavigationListener {

		@Override
		public boolean onNavigationItemSelected(int position, long id) {
			Log.d(TAG, "onNavigationItemSelected : " + position);
			
			if(typeChanged) {
				clearMovieDetails();
			}
			// change navigation item and then wait for data loading 
			if (selectedTypePosition != position) {
				typeChanged = true;
				selectedTypePosition = position;
				String[] movieTypeList = getResources().getStringArray(R.array.type_list);
				String movieType = movieTypeList[position];
				
				if(MOVIE_TYPE_IN_THEATRES.equals(movieType)) {
					imdbClientFragment.getInTheatresMoviesList();
				} else if(MOVIE_TYPE_OPENING.equals(movieType)) {
					imdbClientFragment.getOpenningMoviesList();
				}

				MovieListFragment movieListFragment = (MovieListFragment) fragmentManager.findFragmentByTag(MOVIE_LIST_FRAGMENT_TAG);
				if (movieListFragment != null) {
					movieListFragment.setIsLoading(true);
					movieListFragment.setEmptyText(null);
				}
				clearMovieDetails();
				
			}
			return true;
		}

		private void clearMovieDetails() {
			MovieDetailsFragment movieDetailsFragment = (MovieDetailsFragment) fragmentManager
					.findFragmentByTag(MOVIE_DETAILS_FRAGMENT_TAG);
			if (movieDetailsFragment != null) {
				movieDetailsFragment.clear();
			}
		}
		
	}


	@Override
	public void onInTheatresMovieListResponse(JSONArray jsonArray) {
		Log.d(TAG, "onRTMovieListResponse()");
		MovieListFragment movieListFragment = (MovieListFragment) fragmentManager.findFragmentByTag(MOVIE_LIST_FRAGMENT_TAG);
		if (movieListFragment != null) {
			movieListFragment.update(JSONParser.parseInTheatresMovieListJSON(jsonArray), typeChanged);
		}
		typeChanged = false;
	}
	
	@Override
	public void onOpeningMovieListResponse(JSONArray jsonArray) {
		Log.d(TAG, "onRTMovieListResponse()");
		MovieListFragment movieListFragment = (MovieListFragment) fragmentManager.findFragmentByTag(MOVIE_LIST_FRAGMENT_TAG);
		if (movieListFragment != null) {
			movieListFragment.update(JSONParser.parseOpeningMovieListJSON(jsonArray), typeChanged);
		}
		typeChanged = false;
	}
	
	@Override
	public void onError(VolleyError error) {
		Log.d(TAG, "onError()");
		MovieListFragment movieListFragment = (MovieListFragment) fragmentManager.findFragmentByTag(MOVIE_LIST_FRAGMENT_TAG);
		if(movieListFragment != null) {
			if(error instanceof TimeoutError) {
				movieListFragment.updateErrorMessage("Connection timeout, please try again next time");
			} else {
				movieListFragment.updateErrorMessage("Cannot connect to the server, please check your Internet connection");
			}
			
		}
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			SlidingPaneLayout sliding_layout = (SlidingPaneLayout) findViewById(R.id.sliding_layout);
			if (sliding_layout.isOpen() == false) {
				sliding_layout.openPane();
			}
			return true;
		
		case R.id.action_browser:
			MovieListFragment movieListFragment = (MovieListFragment) fragmentManager.findFragmentByTag(MOVIE_LIST_FRAGMENT_TAG);
			if(movieListFragment.getSelectedItemPosition() == NO_SELECTION) {
				Toast.makeText(this, "No item selected", Toast.LENGTH_SHORT).show();
				return true;
			}
			int selectedPosition = movieListFragment.getListView().getCheckedItemPosition();
			if(selectedPosition != -1) {
				Movie movie = (Movie) movieListFragment.getListView().getAdapter().getItem(selectedPosition);
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getImdbURL()));
				startActivity(browserIntent);
			} else {
				Toast.makeText(this, "No item selected", Toast.LENGTH_SHORT).show();
			}
			return true;
		
		case R.id.action_legal:
			Toast.makeText(this, "Legal", Toast.LENGTH_SHORT).show();
			return true;
		
		case R.id.action_settings:
			Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
		
	}

	@Override
	public void onMovieSelected(Movie movie) {
		Log.d(TAG, "onMovieSelected() : " + movie.getTitle());

		MovieDetailsFragment movieDetailsFragment = (MovieDetailsFragment) fragmentManager
				.findFragmentByTag(MOVIE_DETAILS_FRAGMENT_TAG);
		if (movieDetailsFragment != null) {
			movieDetailsFragment.update(movie);
		}

		SlidingPaneLayout slidingLayout = (SlidingPaneLayout) findViewById(R.id.sliding_layout);
		slidingLayout.closePane();
	}
	

	public ImageLoader getImageLoader() {
		return imdbClientFragment.getImageLoader();
	}


	// make sure all pending network requests are cancelled when this activity stops
	@Override
	protected void onStop() {
		Log.d(TAG, "onStop");
		super.onStop();
		if (imdbClientFragment != null) {
			imdbClientFragment.cancelAllRequests();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		Log.d(TAG, "onSaveInstanceState");
		int selectedTypeIndex = getActionBar().getSelectedNavigationIndex();
		outState.putInt(SELECTED_TYPE_IDX, selectedTypeIndex);
		outState.putBoolean(TYPE_CHANGED, typeChanged);
		super.onSaveInstanceState(outState);

		Log.d(TAG, "onSaveInstanceState() : stored selected type position = " + selectedTypeIndex);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.d(TAG, "onCreateOptionsMenu()");

		getMenuInflater().inflate(R.menu.movie_browser, menu);

		return true;
	}

	@Override
	public void onBackPressed() {
		SlidingPaneLayout slidingLayout = (SlidingPaneLayout) findViewById(R.id.sliding_layout);
		if (slidingLayout.isOpen()) {
			super.onBackPressed();
		} else {
			slidingLayout.openPane();
		}
	}

	/**
	 * This panel slide listener updates the action bar accordingly for each panel state.
	 */
	private class SliderListener extends SlidingPaneLayout.SimplePanelSlideListener {
		@Override
		public void onPanelOpened(View panel) {
			getActionBar().setHomeButtonEnabled(false);
			getActionBar().setDisplayHomeAsUpEnabled(false);
		}

		@Override
		public void onPanelClosed(View panel) {
			getActionBar().setHomeButtonEnabled(true);
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
}
