package com.ilovemusic.labymod;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.ilovemusic.labymod.ilovemusic.ILoveMusicClient;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ILoveMusicModule extends AbstractModule {
  private final String iLoveMusicApiBaseUrl;

  private ILoveMusicModule(String iLoveMusicApiBaseUrl) {
    this.iLoveMusicApiBaseUrl = iLoveMusicApiBaseUrl;
  }

  @Override
  protected void configure() {
  }

  @Provides
  @Singleton
  ILoveMusicClient provideILoveMusicClient(Retrofit retrofit) {
    return retrofit.create(ILoveMusicClient.class);
  }

  @Provides
  @Singleton
  Retrofit provideRetrofit(OkHttpClient httpClient, GsonConverterFactory converterFactory) {
    return new Retrofit.Builder()
        .client(httpClient)
        .addConverterFactory(converterFactory)
        .baseUrl(iLoveMusicApiBaseUrl)
        .build();
  }

  @Provides
  @Singleton
  GsonConverterFactory provideGsonConverterFactory(Gson gson) {
    return GsonConverterFactory.create(gson);
  }

  @Provides
  @Singleton
  Gson provideGson() {
    return new GsonBuilder()
        .setPrettyPrinting()
        .setLenient()
        .create();
  }

  @Provides
  @Singleton
  OkHttpClient provideHttpClient() {
    return new OkHttpClient.Builder()
        .callTimeout(10, TimeUnit.SECONDS)
        .build();
  }

  public static ILoveMusicModule create(String iLoveMusicApiBaseUrl) {
    Preconditions.checkNotNull(iLoveMusicApiBaseUrl);
    return new ILoveMusicModule(iLoveMusicApiBaseUrl);
  }
}