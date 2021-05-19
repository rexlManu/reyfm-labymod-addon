package de.rexlmanu.reyfm.music;

import com.goxr3plus.streamplayer.enums.Status;
import com.goxr3plus.streamplayer.stream.StreamPlayer;
import com.goxr3plus.streamplayer.stream.StreamPlayerEvent;
import com.goxr3plus.streamplayer.stream.StreamPlayerException;
import com.goxr3plus.streamplayer.stream.StreamPlayerListener;
import de.rexlmanu.reyfm.ReyFMAddon;
import de.rexlmanu.reyfm.sender.RadioStation;
import de.rexlmanu.reyfm.utility.Utils;
import lombok.Getter;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public class StationPlayer extends StreamPlayer implements StreamPlayerListener {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();
    private static volatile boolean STOPPED = false;

    private boolean playing;
    private RadioStation radioStation;

    public StationPlayer() {
        this.playing = false;
        this.radioStation = null;

        //sEXECUTOR_SERVICE.submit(this::run);
    }

    public void start(final RadioStation radioStation) {
        EXECUTOR_SERVICE.submit(new Runnable() {
            @Override
            public void run() {
                if (playing) {
                    deletePlayer();
                }
                StationPlayer.this.radioStation = radioStation;

                createPlayer();

            }
        });
    }

    private void createPlayer() {
        //todo: javax.sound.sampled.UnsupportedAudioFileException: could not get audio input stream from input stream
        try {
            System.out.println("START PLAYING");
            //URLConnection connection = new URL(Utils.streamUrl(radioStation.getName(), ReyFMAddon.getAddon().getQuality())).openConnection();
            URLConnection connection = new URL("https://reyfm-stream05.radiohost.de/reyfm-original_mp3-320").openConnection();
            System.out.println(Utils.streamUrl(radioStation.getName(), ReyFMAddon.getAddon().getQuality()));
            Utils.allowCerts(connection);
            System.out.println("REYFM-1");
            connection.setRequestProperty("User-Agent", "REYFM LabyMod Addon");
            System.out.println("REYFM-2");
            connection.connect();
            System.out.println("REYFM-3");
            this.addStreamPlayerListener(this);
            System.out.println("REYFM-4");
            this.open(connection.getInputStream());
            System.out.println("REYFM-5");
            this.play();
            System.out.println("REYFM-6");
            this.playing = true;
            this.setGain(ReyFMAddon.getAddon().getVolume() / 100d);
        } catch (IOException | StreamPlayerException e) {
            e.printStackTrace(System.out);
            System.out.println("Could not start player");
            System.out.println(e.getMessage());
        }
    }

    public void stopPlaying() {
        this.deletePlayer();
    }

    private void deletePlayer() {
        this.playing = false;
        this.stop();
    }

    public void setVolume(double volume) {
        this.setGain(volume / 100d);
    }


    @Override
    public void opened(Object dataSource, Map<String, Object> properties) {

    }

    @Override
    public void progress(int nEncodedBytes, long microsecondPosition, byte[] pcmData, Map<String, Object> properties) {

    }

    @Override
    public void statusUpdated(StreamPlayerEvent event) {
        if (event.getPlayerStatus() == Status.STOPPED && this.playing && this.radioStation != null) {
            this.start(this.radioStation);
        }
    }
}
