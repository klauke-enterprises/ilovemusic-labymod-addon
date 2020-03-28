package com.ilovemusic.labymod;

import com.google.common.base.Preconditions;
import com.ilovemusic.labymod.player.BasicPlayer;
import com.ilovemusic.labymod.player.BasicPlayerException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class MusicPlayer {
  private final BasicPlayer basicPlayer = new BasicPlayer();

  @Inject
  private MusicPlayer() {
  }

  public synchronized void play() {
    try {
      basicPlayer.resume();
    } catch (BasicPlayerException e) {
      e.printStackTrace();
    }
  }

  public boolean isPlaying() {
    return basicPlayer.getStatus() == BasicPlayer.PLAYING;
  }

  public void setVolume(double gain) {
    try {
      basicPlayer.pause();
      basicPlayer.setGain(gain);
      basicPlayer.resume();
    } catch (BasicPlayerException e) {
      e.printStackTrace();
    }
  }

  public float getVolume() {
    return basicPlayer.getGainValue();
  }

  public synchronized void play(URL url) {
    Preconditions.checkNotNull(url);
    try {
      basicPlayer.stop();
      basicPlayer.open(url.openStream());
      basicPlayer.play();
    } catch (BasicPlayerException | IOException e) {
      e.printStackTrace();
    }
  }

  public synchronized void stop() {
    try {
      basicPlayer.pause();
    } catch (BasicPlayerException e) {
      e.printStackTrace();
    }
  }
}
