package com.bogoslovov.kaloyan.simplecurrencyconvertor.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.bogoslovov.kaloyan.simplecurrencyconvertor.dtos.DataFromServerDTO;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.bogoslovov.kaloyan.simplecurrencyconvertor.constants.Constants.ECB_URL;

/**
 * Created by Kaloyan on 15.11.2016 г..
 */

public class ECBDataLoader extends AsyncTaskLoader<DataFromServerDTO> {


  public ECBDataLoader(Context context) {
      super(context);
  }

  @Override
  protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
  }

  @Override
  public DataFromServerDTO loadInBackground() {

      DataFromServerDTO data=null;
      try {
          OkHttpClient client = new OkHttpClient();
          Request request = new Request.Builder().url(ECB_URL).build();
          Response response = client.newCall(request).execute();
          InputStream is = new ByteArrayInputStream(response.body().bytes());
          InputStreamReader inputStreamReader  = new InputStreamReader(is);
          BufferedReader body = new BufferedReader(inputStreamReader);
          int responseCode = response.code();
          data = new DataFromServerDTO();
          data.setBody(body);
          data.setResponseCode(responseCode);
      } catch (IOException e) {
          e.printStackTrace();
      }

      return data;
  }

}