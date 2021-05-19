package de.rexlmanu.reyfm;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.rexlmanu.reyfm.gui.StationSelectionGui;
import de.rexlmanu.reyfm.module.ReyFMCoverModule;
import de.rexlmanu.reyfm.module.category.ModuleCategory;
import de.rexlmanu.reyfm.module.simple.ArtistTextModule;
import de.rexlmanu.reyfm.module.simple.ChannelTextModule;
import de.rexlmanu.reyfm.module.simple.DurationTextModule;
import de.rexlmanu.reyfm.module.simple.TitleTextModule;
import de.rexlmanu.reyfm.music.StationPlayer;
import de.rexlmanu.reyfm.sender.RadioSong;
import de.rexlmanu.reyfm.sender.RadioStation;
import de.rexlmanu.reyfm.utility.HttpUtils;
import de.rexlmanu.reyfm.utility.Language;
import de.rexlmanu.reyfm.utility.Quality;
import de.rexlmanu.reyfm.utility.SocketClient;
import lombok.Getter;
import net.labymod.api.LabyModAddon;
import net.labymod.gui.elements.DropDownMenu;
import net.labymod.gui.elements.Tabs;
import net.labymod.ingamegui.ModuleCategoryRegistry;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.*;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ReyFMAddon extends LabyModAddon {

    public static final String SERVER_ID = "-7bf1d0e98a5db320f9a1508bcc77b2b2dff155d4";

    /**
     * The singleton instance of the addon
     */
    @Getter
    private static ReyFMAddon addon;

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * The radiostations that are available
     */
    private List<RadioStation> radioStations;
    private Map<Notification, Boolean> notificationValues;
    private int totalListeners;
    private LanguageProvider languageProvider;
    private ModuleCategory moduleCategory;
    /**
     * The streaming sound quality
     */
    private Quality quality;
    private int volume;
    private StationPlayer stationPlayer;
    private SocketClient socket;
    private SliderElement volumeSlider;

    public ReyFMAddon() {
        this.radioStations = new ArrayList<>();
        this.notificationValues = new HashMap<>();
        this.totalListeners = 0;
        this.moduleCategory = new ModuleCategory();
        this.volume = 20;
    }

    /**
     * Gets called when the addon gets enabled
     */
    @Override
    public void onEnable() {
        ReyFMAddon.addon = this;

        this.languageProvider = new LanguageProvider();

        this.fetchRadioStations();

        Tabs.getTabUpdateListener().add(new Consumer<Map<String, Class<? extends GuiScreen>[]>>() {
            @Override
            public void accept(Map<String, Class<? extends GuiScreen>[]> stringMap) {
                stringMap.put(Language.format("tab.name"), new Class[]{StationSelectionGui.class});
            }
        });

        this.stationPlayer = new StationPlayer();
        this.connectToWebsocket();

        ModuleCategoryRegistry.loadCategory(this.moduleCategory);

        getApi().registerModule(new ArtistTextModule());
        getApi().registerModule(new TitleTextModule());
        getApi().registerModule(new DurationTextModule());
        getApi().registerModule(new ChannelTextModule());
        getApi().registerModule(new ReyFMCoverModule());
    }

    @Override
    public void loadConfig() {
        this.quality = this.getConfig().has("quality") ? Quality.valueOf(this.getConfig().get("quality").getAsString().toUpperCase()) : Quality.HIGH;
        this.volume = this.getConfig().has("volume") ? this.getConfig().get("volume").getAsInt() : 20;
        for (Notification notification : Notification.values()) {
            boolean value = this.getConfig().has(notification.name()) ? this.getConfig().get(notification.name()).getAsBoolean() : notification.isValue();
            this.notificationValues.put(notification, value);
        }
    }

    @Override
    protected void fillSettings(List<SettingsElement> list) {
        final DropDownMenu<String> dropDown = new DropDownMenu<>(Language.format("quality"), 0, 0, 0, 0);
        final List<String> qualityOptions = new ArrayList<>();

        for (Quality quality : Quality.values()) {
            qualityOptions.add(this.languageProvider.format("quality." + quality.name()));
        }
        for (String qualityOption : qualityOptions) {
            dropDown.addOption(qualityOption);
        }
        dropDown.setSelected(this.languageProvider.format("quality." + this.quality.name()));

        DropDownElement<String> dropDownElement = new DropDownElement<>(I18n.format("quality"), dropDown);
        dropDownElement.setChangeListener(new Consumer<String>() {
            @Override
            public void accept(String s) {
                Quality newQuality = Quality.values()[qualityOptions.indexOf(dropDown.getSelected())];
                if (newQuality.equals(quality)) return;
                quality = newQuality;
                getConfig().addProperty("quality", quality.name());
                loadConfig();
                stationPlayer.start(stationPlayer.getRadioStation());
            }
        });
        list.add(dropDownElement);

        this.volumeSlider = new SliderElement(Language.format("volume"), new ControlElement.IconData(Material.NOTE_BLOCK), this.volume);
        volumeSlider.setMaxValue(100).setMinValue(1).setSteps(1).addCallback(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                volume = integer;
                getConfig().addProperty("volume", integer);
                stationPlayer.setVolume(volume);
            }
        });
        list.add(volumeSlider);
        for (final Notification notification : Notification.values()) {
            BooleanElement booleanElement = new BooleanElement(
                    Language.format("notifcation.setting." + notification.name()),
                    new ControlElement.IconData(notification.getMaterial()),
                    new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) {
                            getConfig().addProperty(notification.name(), aBoolean);
                            notificationValues.put(notification, aBoolean);
                        }
                    },
                    this.notificationValues.get(notification)

            );
            list.add(booleanElement);
        }
    }

    private void fetchRadioStations() {
        try {
            this.radioStations.clear();
            JsonObject jsonObject = HttpUtils.sendRequestAndReceiveJson("https://api.reyfm.de/v4");
            this.totalListeners = jsonObject.get("all_listeners").getAsInt();
            for (JsonElement jsonElement : jsonObject.getAsJsonArray("sequence")) {
                JsonObject radioStationObject = jsonObject.getAsJsonObject("channels").getAsJsonObject(jsonElement.getAsString());
                try {
                    JsonObject nowSong = radioStationObject.getAsJsonObject("now");
                    this.radioStations.add(
                            new RadioStation(
                                    radioStationObject.get("name").getAsString(),
                                    radioStationObject.get("description").getAsString(),
                                    radioStationObject.get("live").getAsBoolean(),
                                    Color.decode(radioStationObject.get("color").getAsString()),
                                    radioStationObject.get("listeners").getAsInt(),
                                    new RadioSong(
                                            nowSong.get("title").getAsString(),
                                            nowSong.get("artist").getAsString(),
                                            nowSong.getAsJsonObject("cover_urls").get("500x500").getAsString(),
                                            SIMPLE_DATE_FORMAT.parse(nowSong.getAsJsonObject("time").get("start").getAsString()).getTime(),
                                            SIMPLE_DATE_FORMAT.parse(nowSong.getAsJsonObject("time").get("end").getAsString()).getTime()
                                    )
                            )
                    );
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connectToWebsocket() {
        try {
            this.socket = new SocketClient(new URI("wss://socket.reyfm.de/socket.io/?EIO=3&transport=websocket"), new SocketClient.Listener() {
                @Override
                public void handle(String channelName, JsonElement element) {
                    if (channelName.equals("update_channel")) {
                        JsonObject jsonObject = element.getAsJsonObject();
                        JsonObject channel = jsonObject.getAsJsonObject("channel");
                        for (RadioStation radioStation : radioStations) {
                            if (!radioStation.getName().equals(channel.get("name").getAsString())) continue;
                            JsonObject nowSong = channel.getAsJsonObject("now");
                            try {
                                radioStation.setCurrentSong(
                                        new RadioSong(
                                                nowSong.get("title").getAsString(),
                                                nowSong.get("artist").getAsString(),
                                                nowSong.getAsJsonObject("cover_urls").get("500x500").getAsString(),
                                                SIMPLE_DATE_FORMAT.parse(nowSong.getAsJsonObject("time").get("start").getAsString()).getTime(),
                                                SIMPLE_DATE_FORMAT.parse(nowSong.getAsJsonObject("time").get("end").getAsString()).getTime()
                                        )
                                );
                                radioStation.setListeners(channel.get("listeners").getAsInt());
                                // check if the station was before not live
                                if (channel.get("live").getAsBoolean() && !radioStation.isLive()) {
                                    LabyMod.getInstance().getGuiCustomAchievement().displayAchievement(
                                            Language.format("achievement.live.title", "ORIGINAL"),
                                            Language.format("achievement.live.text")
                                    );
                                }
                                radioStation.setLive(channel.get("live").getAsBoolean());
                                totalListeners = jsonObject.get("all_listeners").getAsInt();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                    } else if (channelName.equals("level_broadcast") && isEnabled(Notification.LEVEL_BROADCAST)) {
                        JsonObject jsonObject = element.getAsJsonObject();
                        LabyMod.getInstance().getGuiCustomAchievement().displayAchievement(
                                jsonObject.get("img").getAsString(),
                                Language.format("achievement.level.title"),
                                Language.format("achievement.level.text", jsonObject.get("name").getAsString(), jsonObject.get("level").getAsInt())
                        );
                    }
                }
            });
            this.socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public List<RadioStation> getRadioStations() {
        return radioStations;
    }

    public void setVolume(int volume) {
        this.volume = volume;
        this.volumeSlider.setCurrentValue(volume);
        if (this.stationPlayer.isPlaying()) {
            this.stationPlayer.setVolume(volume);
        }
    }

    public boolean isEnabled(Notification notification) {
        return this.notificationValues.containsKey(notification) ? this.notificationValues.get(notification) : true;
    }
}
