package com.abohava.data.files;

import android.content.Context;
import android.content.res.AssetManager;

import com.abohava.dependencies.ApplicationContext;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class FileHelperImpl implements FileHelper {

    private AssetManager mAssetManager;

    @Inject
    FileHelperImpl(@ApplicationContext Context context) {
        mAssetManager = context.getAssets();
    }

    @Override
    public Single<List<String>> getCityNames() {
        return Single.create((SingleOnSubscribe<String>) emitter -> {
                    InputStream inputStream = mAssetManager.open("iran.txt");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    emitter.onSuccess(reader.readLine());
                })
                .map(s -> Arrays.asList(s.split("\"")))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
