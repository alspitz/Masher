package Masher;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class Masher extends Applet implements ActionListener {
    public Button play;
    public Button stop;

    int numToMash = 2; // how many songs to be played at once

    public static class Song implements AudioClip { // should this extend AudioClip?  implementing seems like black magic
        private AudioClip clip;
        private String title;

        public Song(AudioClip clip, String title) {
            this.clip = clip;
            this.title = title;
        }

        public void play() {
            clip.play();
        }
        
        public void stop() {
            clip.stop();
        }

        public void loop() {
            clip.loop();
        }

        public String getTitle() {
            return title;
        }
    }

    ArrayList<Song> songs = new ArrayList<Song>();   // stores all the songs available to play

    /** Called when applet loads; initializes most of our objects */
    public void init() {
        play = new Button("Play");
        add(play);
        play.addActionListener(this);

        stop = new Button("Stop");
        add(stop);
        stop.addActionListener(this);

        fetchSongs();
    }

    /** Populates the list of all the songs on the server */
    private void fetchSongs() {
        URL url;
        try {
            url = new URL(getCodeBase(), "Masher/songs");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String filename;
            while ((filename = in.readLine()) != null) {
                filename = "Masher/" + filename;
                Song song = new Song(getAudioClip(getCodeBase(), filename), filename);
                songs.add(song);
            }
            in.close();
        }
        catch (MalformedURLException e) {
            System.out.println("Bad URL in fetchSongs");
        }
        catch (IOException e) {
            System.out.println("IOError in fetchSongs");
        }
    }

    /** Stops any songs that are playing */
    private void stopAllSongs() {
        for (AudioClip song : songs)
            song.stop();
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == stop) {
            stopAllSongs();
        }

        // play numToMash random songs
        else if (ae.getSource() == play) {
            stopAllSongs();
            Collections.shuffle(songs);
            System.out.print("Playing:");
            for (int i = 0; i < numToMash; i++) { 
                songs.get(i).play();
                System.out.print(" " + songs.get(i).getTitle());
            }
            System.out.println();
        }
    }

    /** Helper wrapper of drawArc used to draw circles */
    private void drawCircle(Graphics g, int radius, int x, int y) {
        g.drawArc(x, y, radius*2, radius*2, 0, 360);
    }

    /** Draw stuff on the canvas, called once when applet loads */
    public void paint(Graphics g) {
        g.drawString("Welcome to SongMash!", getWidth()/2 - 75, 50);

        // draw the welcoming face
        int r = (Math.min(getWidth(), getHeight()) - 70)/2;
        int x = getWidth()/2 - r;
        int y = 60;
        drawCircle(g, r, x, y);

        // draw the eyes
        int eyeRadius = r/5; 
        drawCircle(g, eyeRadius, x + r/2, y + r/2);
        drawCircle(g, eyeRadius, x + 3*r/2 - 2*eyeRadius, y + r/2); 

        // draw the smile
        int smileArc = 120; 
        g.drawArc(x + r/2, y + r/2, r, r, (540 - smileArc)/2, smileArc);
    }
}

