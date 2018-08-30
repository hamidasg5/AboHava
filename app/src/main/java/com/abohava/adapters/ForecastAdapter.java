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
import com.abohava.data.db.model.ForecastWeather;
import com.abohava.utils.PersianDate;
import com.abohava.utils.StringUtils;
import com.abohava.utils.UIUtils;

import java.util.Calendar;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class ForecastAdapter extends BaseRecyclerViewAdapter<ForecastWeather, ForecastAdapter.ForecastHolder> {

    private String mUnits = "M";

    @Inject
    public ForecastAdapter(Context context, CompositeDisposable compositeDisposable) {
        super(context, compositeDisposable);
    }

    @NonNull
    @Override
    public ForecastHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ForecastHolder(LayoutInflater.from(getContext()), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastHolder holder, int position) {
        holder.bind(getItems().get(position));
    }

    @Override
    public int getItemCount() {
        return getItems().size();
    }

    @Override
    protected boolean areItemsTheSameImpl(ForecastWeather oldItem, ForecastWeather newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSameImpl(ForecastWeather oldItem, ForecastWeather newItem) {
        return false;
    }

    @Override
    protected Object getChangePayloadImpl(ForecastWeather oldItem, ForecastWeather newItem) {
        return null;
    }

    public void setUnits(String units) {
        mUnits = units;
    }

    class ForecastHolder extends RecyclerView.ViewHolder {

        private TextView mHourText;
        private ImageView mIconImage;
        private TextView mDescriptionText;
        private TextView mTempText;
        private TextView mPrecipitationProbText;
        private TextView mHumidityText;
        private TextView mCloudsText;
        private TextView mWindText;
        private TextView mWindDirText;
        private TextView mUVText;

        ForecastHolder(LayoutInflater layoutInflater, ViewGroup parent) {
            super(layoutInflater.inflate(R.layout.item_forecast_weather, parent, false));

            mHourText = itemView.findViewById(R.id.item_forecast_hour);
            mIconImage = itemView.findViewById(R.id.item_forecast_icon);
            mDescriptionText = itemView.findViewById(R.id.item_forecast_description);
            mTempText = itemView.findViewById(R.id.item_forecast_temp);
            mPrecipitationProbText = itemView.findViewById(R.id.item_forecast_pp);
            mHumidityText = itemView.findViewById(R.id.item_forecast_rh);
            mCloudsText = itemView.findViewById(R.id.item_forecast_cc);
            mWindText = itemView.findViewById(R.id.item_forecast_ws);
            mWindDirText = itemView.findViewById(R.id.item_forecast_wd);
            mUVText = itemView.findViewById(R.id.item_forecast_uv);
        }

        private void bind(ForecastWeather forecastWeather) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(forecastWeather.getTimeStamp() * 1000);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            mHourText.setText(StringUtils.getHourLabel(hour + ":00" + new PersianDate(calendar).getDayOfWeek()));
            mIconImage.setImageResource(UIUtils.getWeatherIconRes(forecastWeather.getWeatherIcon()));
            mDescriptionText.setText(StringUtils.translateDescription(forecastWeather.getWeatherCode()));
            mTempText.setText(StringUtils.getTempLabel(forecastWeather.getTemp(), mUnits));
            mPrecipitationProbText.setText(StringUtils.getPProbText(forecastWeather.getPrecipitationProbability()));
            mHumidityText.setText(StringUtils.getRelHumLabel(forecastWeather.getRelativeHumidity()));
            mCloudsText.setText(StringUtils.getCloudCovLabel(forecastWeather.getCloudCoverage()));
            mWindText.setText(StringUtils.getWindSpeedLabel(forecastWeather.getWindSpeed(), mUnits));
            mWindDirText.setText(StringUtils.translateWindDir(forecastWeather.getWindDirection()));
            mUVText.setText(StringUtils.getUVLabel(forecastWeather.getUV()));
        }
    }
}
