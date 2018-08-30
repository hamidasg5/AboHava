package com.abohava.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.abohava.R;
import com.abohava.adapters.SearchAdapter;
import com.abohava.base.BaseActivity;
import com.abohava.presenters.ISearchPresenter;

import java.util.List;

import javax.inject.Inject;

public class SearchActivity extends BaseActivity implements ISearchActivity {

    private static final String TAG = "SearchActivity";
    private static final String STATE_SEARCH_QUERY = "state_search_query";
    public static final String EXTRA_CITY_NAME = "extra_city_name";

    private String mSearchQuery;

    @Inject
    public ISearchPresenter<ISearchActivity> mPresenter;
    @Inject
    public LinearLayoutManager mLayoutManager;
    @Inject
    public SearchAdapter mSearchAdapter;

    private SearchView mSearchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_search);

        getActivityComponent().inject(this);

        if (savedInstanceState != null) {
            mSearchAdapter.onRestoreInstanceState(savedInstanceState);
            mSearchQuery = savedInstanceState.getString(STATE_SEARCH_QUERY);
        }

        setSupportActionBar(findViewById(R.id.search_toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerView = findViewById(R.id.search_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mSearchAdapter);

        getCompositeDisposable().add(mSearchAdapter.getItemClicks()
                .subscribe(position -> {
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_CITY_NAME, mSearchAdapter.getItems().get(position));
                    setResult(RESULT_OK, intent);
                    finish();
                })
        );

        mPresenter.onAttach(this);
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mSearchAdapter.onSaveInstanceState(outState);
        outState.putString(STATE_SEARCH_QUERY, mSearchView.getQuery().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.menu_item_search_view);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setQueryHint(getString(R.string.hint_search_view));
        mSearchView.setMaxWidth(Integer.MAX_VALUE);
        mSearchView.setSubmitButtonEnabled(false);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                mPresenter.searchCities(newText);
                return true;
            }
        });
        mSearchView.setOnCloseListener(() -> {
            mPresenter.searchCities("");
            return false;
        });
        if (mSearchQuery != null && !mSearchQuery.isEmpty()) {
            searchItem.expandActionView();
            mSearchView.setQuery(mSearchQuery, true);
            mSearchView.clearFocus();
        }
        return true;
    }

    @Override
    public void showCityItems(List<String> items) {
        mSearchAdapter.setItems(items);
    }
}