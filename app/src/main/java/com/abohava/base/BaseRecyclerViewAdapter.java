package com.abohava.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.abohava.events.ItemCheckEvent;
import com.abohava.utils.ParcelableSparseBooleanArray;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;

public abstract class BaseRecyclerViewAdapter<M, T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    private static final String TAG = "BaseRecyclerViewAdapter";
    private static final String STATE_CHECK_STATES = "state_check_states";

    private Context mContext;
    private List<M> mItems;
    private ParcelableSparseBooleanArray mCheckStates;
    private CompositeDisposable mCompositeDisposable;

    protected PublishSubject<Integer> mOnItemClickSubject;
    protected PublishSubject<Integer> mOnItemLongClickSubject;
    protected PublishSubject<ItemCheckEvent> mOnItemCheckSubject;

    protected BaseRecyclerViewAdapter(Context context, CompositeDisposable compositeDisposable) {
        Log.d(TAG, "BaseRecyclerViewAdapter: ");
        mContext = context;
        mCompositeDisposable = compositeDisposable;
        mItems = new ArrayList<>();
        mCheckStates = new ParcelableSparseBooleanArray();
        mOnItemClickSubject = PublishSubject.create();
        mOnItemLongClickSubject = PublishSubject.create();
        mOnItemCheckSubject = PublishSubject.create();
    }

    public void onSaveInstanceState(Bundle state) {
        state.putParcelable(STATE_CHECK_STATES, mCheckStates);
        mCompositeDisposable.dispose();
    }

    public void onRestoreInstanceState(Bundle state) {
        mCheckStates = state.getParcelable(STATE_CHECK_STATES);
    }

    public Context getContext() {
        return mContext;
    }

    public List<M> getItems() {
        return mItems;
    }

    public void setItems(List<M> items) {
        updateItems(items);
    }

    private void updateItems(List<M> newItems) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtilCallback(mItems, newItems), true);
        mItems = newItems;
        diffResult.dispatchUpdatesTo(BaseRecyclerViewAdapter.this);
    }

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    public boolean isChecked(int position) {
        return mCheckStates.get(position, false);
    }

    public void setChecked(int position, boolean isChecked) {
        if (isChecked) {
            mCheckStates.put(position, true);
        } else {
            mCheckStates.delete(position);
        }
        mOnItemCheckSubject.onNext(new ItemCheckEvent(position, isChecked));
    }

    public int getCheckedCount() {
        return mCheckStates.size();
    }

    public List<M> getCheckedItems() {
        List<M> checkedItems = new ArrayList<>(mCheckStates.size());
        for (int i = 0; i < mCheckStates.size(); i++) {
            checkedItems.add(mItems.get(mCheckStates.keyAt(i)));
        }
        return checkedItems;
    }

    public void clearChecks() {
        for (int i = 0; i < mCheckStates.size(); i++) {
            setChecked(i, false);
        }
    }

    public Observable<Integer> getItemClicks() {
        return mOnItemClickSubject;
    }

    public Observable<Integer> getItemLongClicks() {
        return mOnItemLongClickSubject;
    }

    public Observable<ItemCheckEvent> getItemChecks() {
        return mOnItemCheckSubject;
    }

    protected abstract boolean areItemsTheSameImpl(M oldItem, M newItem);
    protected abstract boolean areContentsTheSameImpl(M oldItem, M newItem);
    protected abstract Object getChangePayloadImpl(M oldItem, M newItem);

    private class DiffUtilCallback extends DiffUtil.Callback {

        private List<M> mOldList;
        private List<M> mNewList;

        private DiffUtilCallback(List<M> oldList, List<M> newList) {
            mOldList = oldList;
            mNewList = newList;
        }

        @Override
        public int getOldListSize() {
            return mOldList==null ? 0 : mOldList.size();
        }

        @Override
        public int getNewListSize() {
            return mNewList==null ? 0 : mNewList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return areItemsTheSameImpl(mOldList.get(oldItemPosition), mNewList.get(newItemPosition));
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return areContentsTheSameImpl(mOldList.get(oldItemPosition), mNewList.get(newItemPosition));
        }

        @Nullable
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            return getChangePayloadImpl(mOldList.get(oldItemPosition), mNewList.get(newItemPosition));
        }
    }
}
