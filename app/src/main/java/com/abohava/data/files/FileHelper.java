package com.abohava.data.files;

import java.util.List;

import io.reactivex.Single;

public interface FileHelper {

    Single<List<String>> getCityNames();
}
