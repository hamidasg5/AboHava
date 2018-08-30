package com.abohava.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abohava.R;
import com.abohava.base.BaseRecyclerViewAdapter;
import com.abohava.data.db.model.HistoryWeather;
import com.abohava.utils.StringUtils;
import com.abohava.utils.UIUtils;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class HistoryAdapter extends BaseRecyclerViewAdapter<HistoryWeather, HistoryAdapter.HistoryHolder> {

    private String mUnits = "M";

    @Inject
    public HistoryAdapter(Context context, CompositeDisposable compositeDisposable) {
        super(context, compositeDisposable);
    }

    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HistoryHolder(LayoutInflater.from(getContext()), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryHolder holder, int position) {
        holder.bind(getItems().get(position));
    }

    @Override
    public int getItemCount() {
        return getItems().size();
    }

    @Override
    protected boolean areItemsTheSameImpl(HistoryWeather oldItem, HistoryWeather newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSameImpl(HistoryWeather oldItem, HistoryWeather newItem) {
        return false;
    }

    @Override
    protected Object getChangePayloadImpl(HistoryWeather oldItem, HistoryWeather newItem) {
        return null;
    }

    public void setUnits(String units) {
        mUnits = units;
    }

    class HistoryHolder extends RecyclerView.ViewHolder {

        private TextView mHourText;
        private ImageView mIconImage;
        private TextView mDescriptionText;
        private TextView mTempText;
        private TextView mHumidityText;
        private TextView mCloudsText;
        private TextView mWindText;
        private TextView mUVText;

        HistoryHolder(LayoutInflater layoutInflater, ViewGroup parent) {
            super(layoutInflater.inflate(R.layout.item_history_weather, parent, false));

            mHourText = itemView.findViewById(R.id.item_history_hour);
            mIconImage = itemView.findViewById(R.id.item_history_icon);
            mDescriptionText = itemView.findViewById(R.id.item_history_description);
            mTempText = itemView.findViewById(R.id.item_history_temp);
            mHumidityText = itemView.findViewById(R.id.item_history_rh);
            mCloudsText = itemView.findViewById(R.id.item_history_cc);
            mWindText = itemView.findViewById(R.id.item_history_ws);
            mUVText = itemView.findViewById(R.id.item_history_uv);
        }

        private void bind(HistoryWeather historyWeather) {
            mHourText.setText(StringUtils.getHourLabel(historyWeather.getDateTime()));
            mIconImage.setImageResource(UIUtils.getWeatherIconRes(historyWeather.getWeatherIcon()));
            mDescriptionText.setText(StringUtils.translateDescription(historyWeather.getWeatherCode()));
            mTempText.setText(StringUtils.getTempLabel(historyWeather.getTemp(), mUnits));
            mHumidityText.setText(StringUtils.getRelHumLabel(historyWeather.getRelativeHumidity()));
            mCloudsText.setText(StringUtils.getCloudCovLabel(historyWeather.getCloudCoverage()));
            mWindText.setText(StringUtils.getWindSpeedLabel(historyWeather.getWindSpeed(), mUnits));
            mUVText.setText(StringUtils.getUVLabel(historyWeather.getUV()));
        }
    }
}
