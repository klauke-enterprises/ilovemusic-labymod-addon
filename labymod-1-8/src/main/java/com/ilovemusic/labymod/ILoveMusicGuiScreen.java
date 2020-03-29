package com.ilovemusic.labymod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import javax.inject.Inject;
import net.labymod.gui.elements.Tabs;
import net.labymod.utils.DrawUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList.GuiResponder;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ILoveMusicGuiScreen extends GuiScreen implements Observer, GuiResponder {
  @Inject
  private static MusicPlayer musicPlayer;
  @Inject
  private static DrawUtils drawUtils;
  @Inject
  private static StreamRepository streamRepository;
  private final Logger log = LogManager.getLogger("ILoveMusic");
  private GuiSlider volumeSlider;
  private GuiButton startButton;
  private GuiButton stopButton;

  private StreamSelectionListGui streamSelection;

  @Override
  public void initGui() {
    super.initGui();
    createStreamSelection();
    Tabs.initGuiScreen(this.buttonList, this);
    createGuiElements();
    subscribeMusicState();
  }

  private void createGuiElements() {
    volumeSlider = createGuiSlider();
    startButton = new GuiButton(0xC0DE_1, this.width / 2 - 74, this.height - 28, 70, 20, "Start");
    stopButton = new GuiButton(0xC0DE_2, this.width / 2 + 4, this.height - 28, 70, 20, "Stop");
    buttonList.add(startButton);
    buttonList.add(stopButton);
    buttonList.add(volumeSlider);
    updateButtonState();
  }

  private void updateButtonState() {
    startButton.enabled = !musicPlayer.isPlaying();
    stopButton.enabled = musicPlayer.isPlaying();
  }

  private void subscribeMusicState() {
    musicPlayer.addObserver(this);
  }

  @Override
  public void update(Observable o, Object arg) {
    updateButtonState();
  }

  private void createStreamSelection() {
    List<StreamSelectionListEntry> streamSelectionListEntries = new ArrayList<>();
    streamSelection = createSelectionGui(streamSelectionListEntries);
    streamSelectionListEntries.addAll(createStreamEntries());
  }

  private StreamSelectionListGui createSelectionGui(
      List<StreamSelectionListEntry> streamSelectionListEntries
  ) {
    return new StreamSelectionListGui(
        mc,
        width,
        height,
        32,
        height - 64,
        36,
        streamSelectionListEntries
    );
  }

  private GuiSlider createGuiSlider() {
    return new GuiSlider(
        this,
        0xC0DE_0,
        this.width / 2 - 74,
        this.height - 52,
        "Lautstärke",
        0,
        100,
        musicPlayer.getVolume() * 100,
        (id, name, value) -> String.valueOf((int) value)
    );
  }

  private List<StreamSelectionListEntry> createStreamEntries() {
    try {
      return streamRepository.findAll().get().stream()
          .map(stream -> new StreamSelectionListEntry(streamSelection, stream))
          .collect(Collectors.toList());
    } catch (InterruptedException | ExecutionException e) {
      return new ArrayList<>();
    }
  }

  @Override
  public void handleMouseInput() throws IOException {
    super.handleMouseInput();
    streamSelection.handleMouseInput();
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    drawDefaultBackground();
    streamSelection.drawScreen(mouseX, mouseY, partialTicks);
    super.drawScreen(mouseX, mouseY, partialTicks);
  }

  @Override
  protected void mouseClicked(
      int mouseX,
      int mouseY,
      int mouseButton
  ) throws IOException {
    super.mouseClicked(mouseX, mouseY, mouseButton);
    streamSelection.mouseClicked(mouseX, mouseY, mouseButton);
  }

  @Override
  protected void mouseClickMove(
      int mouseX,
      int mouseY,
      int clickedMouseButton,
      long timeSinceLastClick
  ) {
    super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
  }

  @Override
  protected void mouseReleased(int mouseX, int mouseY, int state) {
    super.mouseReleased(mouseX, mouseY, state);
    streamSelection.mouseReleased(mouseX, mouseY, state);
  }

  @Override
  protected void keyTyped(char typedChar, int keyCode) throws IOException {
    super.keyTyped(typedChar, keyCode);
  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    super.actionPerformed(button);
    Tabs.actionPerformedButton(button);

    int buttonId = button.id;

    if (!button.enabled) {
      return;
    }

    switch (buttonId) {
      case 0xC0DE1: //started
        musicPlayer.play();
        break;
      case 0xC0DE2: //stopped
        musicPlayer.stop();
        break;
    }
  }

  @Override
  public void func_175321_a(int i, boolean b) {

  }

  @Override
  public void onTick(int i, float v) {
    musicPlayer.setVolume((v / 100));
  }

  @Override
  public void func_175319_a(int i, String s) {

  }
}
