package com.ilovemusic.labymod;

import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.ilovemusic.labymod.player.BasicPlayer;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import net.labymod.api.LabyModAPI;
import net.labymod.main.LabyMod;
import net.labymod.utils.DrawUtils;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public final class ILoveMusicModule extends AbstractModule {
  private final String iLoveMusicApiBaseUrl;

  private ILoveMusicModule(String iLoveMusicApiBaseUrl) {
    this.iLoveMusicApiBaseUrl = iLoveMusicApiBaseUrl;
  }

  public static ILoveMusicModule create(String iLoveMusicApiBaseUrl) {
    Preconditions.checkNotNull(iLoveMusicApiBaseUrl);
    return new ILoveMusicModule(iLoveMusicApiBaseUrl);
  }

  @Override
  protected void configure() {
    requestStaticInjection(ILoveMusicGuiScreen.class);
    requestStaticInjection(StreamSelectionListEntry.class);
  }

  @Provides
  @Singleton
  BasicPlayer provideBasicPlayer() {
    return new BasicPlayer();
  }

  @Provides
  @Singleton
  ILoveMusicClient provideILoveMusicClient(Retrofit retrofit) {
    return retrofit.create(ILoveMusicClient.class);
  }

  @Provides
  @Singleton
  Retrofit provideRetrofit(OkHttpClient httpClient) {
    return new Retrofit.Builder()
        .client(httpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl(iLoveMusicApiBaseUrl)
        .build();
  }

  @Provides
  @Singleton
  OkHttpClient provideHttpClient() {
    return new OkHttpClient.Builder()
        .callTimeout(10, TimeUnit.SECONDS)
        .build();
  }

  @Provides
  @Singleton
  DrawUtils providesDrawUtils() {
    return LabyMod.getInstance().getDrawUtils();
  }

  @Provides
  @Singleton
  LabyModAPI provideLabyModApi() {
    return LabyMod.getInstance().getLabyModAPI();
  }
}
