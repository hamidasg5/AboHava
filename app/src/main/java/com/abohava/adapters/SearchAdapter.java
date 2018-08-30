package com.abohava.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abohava.R;
import com.abohava.base.BaseRecyclerViewAdapter;
import com.abohava.dependencies.ActivityContext;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class SearchAdapter extends BaseRecyclerViewAdapter<String, SearchAdapter.SearchItemHolder> {

    @Inject
    public SearchAdapter(@ActivityContext Context context, CompositeDisposable compositeDisposable) {
        super(context, compositeDisposable);
    }

    @NonNull
    @Override
    public SearchItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchItemHolder(LayoutInflater.from(getContext()), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchItemHolder holder, int position) {
        holder.mTextView.setText(getItems().get(position));
    }

    @Override
    public int getItemCount() {
        return getItems().size();
    }

    @Override
    protected boolean areItemsTheSameImpl(String oldItem, String newItem) {
        return oldItem.equals(newItem);
    }

    @Override
    protected boolean areContentsTheSameImpl(String oldItem, String newItem) {
        return oldItem.equals(newItem);
    }

    @Override
    protected Object getChangePayloadImpl(String oldItem, String newItem) {
        return null;
    }

    public class SearchItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTextView;

        SearchItemHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_search, parent, false));
            mTextView = itemView.findViewById(R.id.item_city_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnItemClickSubject.onNext(getAdapterPosition());
        }
    }
}
