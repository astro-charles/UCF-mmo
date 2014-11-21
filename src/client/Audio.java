package client;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;



public class Audio implements LineListener, Runnable {
	public enum Soundtrack { 
		FOREST,
		ACTION,
		QUIET
	}
	
	private boolean playCompleted=true;
	private String audioFilePath="res/forest.wav"; 
	
	@Override
	public void update(LineEvent event) {
       LineEvent.Type type = event.getType();
         
       if (type == LineEvent.Type.START) {
           System.out.println("Playback started.");
             
       } else if (type == LineEvent.Type.STOP) {
           playCompleted = true;
           System.out.println("Playback completed.");
       }
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(playCompleted) {
			playCompleted=false;
			play();
		}
	}
	/**
	 * This function begins the mayhem!
	 */
	void begin() {
	    Thread t = new Thread(this);
	    t.start();
	}
	
	void play() {
        File audioFile = new File(this.audioFilePath);
 
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
 
            AudioFormat format = audioStream.getFormat();
 
            DataLine.Info info = new DataLine.Info(Clip.class, format);
 
            Clip audioClip = (Clip) AudioSystem.getLine(info);
 
            audioClip.addLineListener(this);
 
            audioClip.open(audioStream);
             
            audioClip.start();
             
            while (!playCompleted) {
                // wait for the playback completes
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
             
            audioClip.close();
             
        } catch (UnsupportedAudioFileException ex) {
            System.out.println("The specified audio file is not supported.");
            ex.printStackTrace();
        } catch (LineUnavailableException ex) {
            System.out.println("Audio line for playing back is unavailable.");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Error playing the audio file.");
            ex.printStackTrace();
        }
         
    }
 
}