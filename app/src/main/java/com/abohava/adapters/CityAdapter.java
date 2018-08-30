package com.abohava.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.abohava.R;
import com.abohava.base.BaseRecyclerViewAdapter;
import com.abohava.data.db.model.City;
import com.abohava.dependencies.ActivityContext;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;

public class CityAdapter extends BaseRecyclerViewAdapter<City, CityAdapter.CityHolder> {

    private PublishSubject<String> mOnItemDeleteSubject;

    @Inject
    public CityAdapter(@ActivityContext Context context, CompositeDisposable compositeDisposable) {
        super(context, compositeDisposable);
        mOnItemDeleteSubject = PublishSubject.create();
    }

    @NonNull
    @Override
    public CityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CityHolder(LayoutInflater.from(getContext()), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull CityHolder holder, int position) {
        holder.mTextView.setText(getItems().get(position).getName());
    }

    @Override
    public int getItemCount() {
        return getItems().size();
    }

    @Override
    protected boolean areItemsTheSameImpl(City oldItem, City newItem) {
        return oldItem.equals(newItem);
    }

    @Override
    protected boolean areContentsTheSameImpl(City oldItem, City newItem) {
        return false;
    }

    @Override
    protected Object getChangePayloadImpl(City oldItem, City newItem) {
        return null;
    }

    public Observable<String> getItemDeletes() {
        return mOnItemDeleteSubject;
    }

    class CityHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTextView;
        private ImageButton mDeleteButton;

        CityHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_city, parent, false));
            itemView.setOnClickListener(this);
            mTextView = itemView.findViewById(R.id.item_city_name);
            mDeleteButton = itemView.findViewById(R.id.item_city_delete);
            mDeleteButton.setOnClickListener(v -> mOnItemDeleteSubject.onNext(getItems().get(getAdapterPosition()).getName()));
        }

        @Override
        public void onClick(View v) {
            mOnItemClickSubject.onNext(getAdapterPosition());
        }
    }
}
