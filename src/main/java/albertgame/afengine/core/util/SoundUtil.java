package albertgame.afengine.core.util;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.*;
import javax.sound.sampled.*;

public class SoundUtil {

    static SoundUtil util;

    public static SoundUtil get() {
        if (util == null) {
            util = new SoundUtil();
        }
        return util;
    }

    private final MidiPlayer midiManager;
    private final SoundPlayer soundManager;
    private final Map<Long, Sound> soundMap;
    private final Map<Long, Sequence> midiMap;
    private static long sid = 0;

    private SoundUtil() {
        this.midiManager = new MidiPlayer();
        this.soundManager = new SoundPlayer();
        this.soundMap = new HashMap<>();
        this.midiMap = new HashMap<>();
    }

    public long addSound(String stringfile) {
        Sound sound;
        sound = soundManager.getSound(stringfile);
        long id = sid++;
        soundMap.put(id, sound);
        return id;
    }

    public long addSound(URL url) {
        Sound sound;
        sound = soundManager.getSound(url);
        long id = sid++;
        soundMap.put(id, sound);
        return id;
    }

    public long addMidi(String filepath) {
        Sequence sequence = null;
        try {
            sequence = midiManager.getSequence(filepath);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SoundUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        long id = sid++;
        midiMap.put(id, sequence);
        return id;
    }

    public long addMidi(URL url) {
        if (url == null) {
            System.err.println("url is null!");
            return 0L;
        }
        Sequence sequence = null;
        try {
            sequence = midiManager.getSequence(url.openStream());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SoundUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SoundUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        long id = sid++;
        midiMap.put(id, sequence);
        return id;
    }

    public void playMidi(long midiId, boolean loop) {
        Sequence sequence = midiMap.get(midiId);
        midiManager.play(sequence, loop);
    }

    public void playSound(long soundId, int count) {
        Sound sound = soundMap.get(soundId);
        soundManager.play(sound, count);
    }

    public void stopMidi() {
        midiManager.stop();
    }

    public boolean isPauseMidi() {
        return midiManager.isPaused();
    }

    public boolean isPauseSound() {
        return soundManager.isPaused();
    }

    public void setPauseMidi(boolean pause) {
        midiManager.setPaused(pause);
    }

    public void setPauseSound(boolean pause) {
        soundManager.setPaused(pause);
    }

    private static class SoundPlayer {

        private boolean ifpause;
        private int loopCount;
        private int loopnow;
        private final ThreadPool pool;

        private final Object pausedLock;

        public SoundPlayer() {
            ifpause = false;
            loopCount = loopnow = 1;
            pool = new ThreadPool(1000);
            pausedLock = new Object();

        }

        public Sound getSound(URL url) {
            Sound sound = null;
            // open the audio input stream
            AudioInputStream stream = null;
            try {
                stream = AudioSystem.getAudioInputStream(
                        new File(url.toURI()));
            } catch (UnsupportedAudioFileException | IOException ex) {
                Logger.getLogger(SoundPlayer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (URISyntaxException ex) {
                Logger.getLogger(SoundUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
            AudioFormat format = stream.getFormat();
            // get the audio samples
            sound = new Sound(getSamples(stream));
            sound.setFormat(format);
            return sound;
        }

        public Sound getSound(String filename) {
            Sound sound = null;
            // open the audio input stream
            AudioInputStream stream = null;
            try {
                stream = AudioSystem.getAudioInputStream(
                        new File(filename));
            } catch (UnsupportedAudioFileException | IOException ex) {
                Logger.getLogger(SoundPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
            AudioFormat format = stream.getFormat();
            // get the audio samples
            sound = new Sound(getSamples(stream));
            sound.setFormat(format);
            return sound;
        }

        private static byte[] getSamples(AudioInputStream audioStream) {
            // get the number of bytes to read
            int length = (int) (audioStream.getFrameLength()
                    * audioStream.getFormat().getFrameSize());

            // read the entire stream
            byte[] samples = new byte[length];
            DataInputStream is = new DataInputStream(audioStream);
            try {
                is.readFully(samples);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // return the samples
            return samples;
        }

        public int getLoopCount() {
            return loopCount;
        }

        public void setLoopCount(int count) {
            this.loopCount = loopnow = count;
        }

        protected class SoundPlayerIner implements Runnable {

            private final Sound sound;
            private int count;

            public SoundPlayerIner(Sound sound, int count) {
                this.sound = sound;
                this.count = count;
            }

            @Override
            public void run() {
                try {
                    while (count-- > 0) {
                        InputStream source = new ByteArrayInputStream(sound.getSamples());
                        // TODO Auto-generated method stub
                        // use a short, 100ms (1/10th sec) buffer for real-time
                        // change to the sound stream
                        int bufferSize = sound.getFormat().getFrameSize()
                                * Math.round(sound.getFormat().getSampleRate() / 10);
                        byte[] buffer = new byte[bufferSize];

                        // create a line to play to
                        SourceDataLine line;
                        try {
                            DataLine.Info info
                                    = new DataLine.Info(SourceDataLine.class, sound.getFormat());
                            line = (SourceDataLine) AudioSystem.getLine(info);
                            line.open(sound.getFormat(), bufferSize);
                        } catch (LineUnavailableException ex) {
                            ex.printStackTrace();
                            return;
                        }

                        // start the line
                        line.start();

                        int numBytesRead = 0;
                        while (numBytesRead != -1) {
                            // if paused, wait until unpaused
                            synchronized (pausedLock) {
                                if (ifpause) {
                                    try {
                                        pausedLock.wait();
                                    } catch (InterruptedException ex) {
                                        return;
                                    }
                                }
                            }
                            numBytesRead
                                    = source.read(buffer, 0, buffer.length);
                            if (numBytesRead != -1) {
                                line.write(buffer, 0, numBytesRead);
                            }
                        }
                        line.drain();
                        line.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        public void play(Sound sound, int count) {
            pool.runTask(new SoundPlayerIner(sound, count));
        }

        public void setPaused(boolean paused) {
            if (this.ifpause != paused) {
                synchronized (pausedLock) {
                    this.ifpause = paused;
                    if (!paused) {
                        // restart sounds
                        pausedLock.notifyAll();
                    }
                }
            }
        }

        /**
         * Returns the paused state.
         */
        public boolean isPaused() {
            return ifpause;
        }

        protected class SoundPlayer22 implements Runnable {

            private final InputStream source;
            private final AudioFormat format;

            public SoundPlayer22(InputStream source, AudioFormat format) {
                this.source = source;
                this.format = format;
            }

            @Override
            public void run() {

                int bufferSize = format.getFrameSize()
                        * Math.round(format.getSampleRate() / 10);
                byte[] buffer = new byte[bufferSize];

                // create a line to play to
                SourceDataLine line;
                try {
                    DataLine.Info info
                            = new DataLine.Info(SourceDataLine.class, format);
                    line = (SourceDataLine) AudioSystem.getLine(info);
                    line.open(format, bufferSize);
                } catch (LineUnavailableException ex) {
                    ex.printStackTrace();
                    return;
                }

                // start the line
                line.start();

                // copy data to the line
                try {
                    int numBytesRead = 0;
                    while (numBytesRead != -1) {
                        synchronized (pausedLock) {
                            if (ifpause) {
                                try {
                                    pausedLock.wait();
                                } catch (InterruptedException ex) {
                                    return;
                                }
                            }
                        }
                        numBytesRead
                                = source.read(buffer, 0, buffer.length);
                        if (numBytesRead != -1) {
                            line.write(buffer, 0, numBytesRead);
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                // wait until all data is played, then close the line
                line.drain();
                line.close();
            }
        }
    }

    private static class Sound {

        private byte[] samples;
        private AudioFormat format;

        /**
         * Create a new Sound object with the specified byte array. The array is
         * not copied.
         */
        public Sound(byte[] samples) {
            this.samples = samples;
        }

        /**
         * Returns this Sound's objects samples as a byte array.
         */
        public byte[] getSamples() {
            return samples;
        }

        public void setFormat(AudioFormat format) {
            this.format = format;
        }

        public AudioFormat getFormat() {
            return format;
        }

    }

    private static class MidiPlayer implements MetaEventListener {

        // Midi meta event
        public static final int END_OF_TRACK_MESSAGE = 47;

        private Sequencer sequencer;
        private boolean loop;
        private boolean paused;
        private boolean ifend;
        private Sequence lastsequence = null;

        /**
         * Creates a new MidiPlayer object.
         */
        public MidiPlayer() {
            try {
                sequencer = MidiSystem.getSequencer();
                sequencer.open();
                sequencer.addMetaEventListener(this);
            } catch (MidiUnavailableException ex) {
                sequencer = null;
            }
        }

        /**
         * Loads a sequence from the file system. Returns null if an error
         * occurs.
         */
        public Sequence getSequence(String filename) throws FileNotFoundException {
            return getSequence(new FileInputStream(filename));
        }

        /**
         * Loads a sequence from an input stream. Returns null if an error
         * occurs.
         */
        public Sequence getSequence(InputStream is) {
            try {
                if (!is.markSupported()) {
                    is = new BufferedInputStream(is);
                }
                Sequence s = MidiSystem.getSequence(is);
                is.close();
                return s;
            } catch (InvalidMidiDataException ex) {
                ex.printStackTrace();
                return null;
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
        }

        /**
         * Plays a sequence, optionally looping. This method returns
         * immediately. The sequence is not played if it is invalid.
         */
        public void play(Sequence sequence, boolean loop) {
            lastsequence = sequence;
            if (sequencer != null && sequence != null && sequencer.isOpen()) {
                try {
                    sequencer.setSequence(sequence);
                    sequencer.start();
                    this.loop = loop;
                } catch (InvalidMidiDataException ex) {
                    ex.printStackTrace();
                }
            }
        }

        /**
         * This method is called by the sound system when a meta event occurs.
         * In this case, when the end-of-track meta event is received, the
         * sequence is restarted if looping is on.
         */
        @Override
        public void meta(MetaMessage event) {
            if (event.getType() == END_OF_TRACK_MESSAGE) {
                if (sequencer != null && sequencer.isOpen() && loop) {
                    sequencer.setMicrosecondPosition(0);
                    sequencer.start();
                } else {
                    ifend = true;
                }
            }
        }

        /**
         * Stops the sequencer and resets its position to 0.
         */
        public void stop() {
            if (sequencer != null && sequencer.isOpen()) {
                sequencer.stop();
                sequencer.setMicrosecondPosition(0);
            }
        }

        /**
         * Closes the sequencer.
         */
        public void close() {
            if (sequencer != null && sequencer.isOpen()) {
                sequencer.close();
            }
        }

        /**
         * Gets the sequencer.
         */
        public Sequencer getSequencer() {
            return sequencer;
        }

        /**
         * Sets the paused state. Music may not immediately pause.
         */
        public void setPaused(boolean paused) {
            if (this.paused != paused && sequencer != null && sequencer.isOpen() && lastsequence != null) {
                this.paused = paused;
                if (paused) {
                    sequencer.stop();
                } else {
                    sequencer.start();
                }
            }
        }

        /**
         * Returns the paused state.
         */
        public boolean isPaused() {
            return paused;
        }

        public boolean isIfend() {
            return ifend;
        }
    }

    private static class ThreadPool extends ThreadGroup {

        private boolean isAlive;
        private final LinkedList<Runnable> taskQueue;
        private int threadID;
        private static int threadPoolID;

        /**
         * Creates a new ThreadPool.
         *
         * @param numThreads The number of threads in the pool.
         */
        public ThreadPool(int numThreads) {
            super("ThreadPool-" + (threadPoolID++));
            setDaemon(true);

            isAlive = true;

            taskQueue = new LinkedList();
            for (int i = 0; i < numThreads; i++) {
                new PooledThread().start();
            }
        }

        /**
         * Requests a new task to run. This method returns immediately, and the
         * task executes on the next available idle thread in this ThreadPool.
         * <p>
         * Tasks start execution in the order they are received.
         *
         * @param task The task to run. If null, no action is taken.
         * @throws IllegalStateException if this ThreadPool is already closed.
         */
        public synchronized void runTask(Runnable task) {
            if (!isAlive) {
                throw new IllegalStateException();
            }
            if (task != null) {
                taskQueue.add(task);
                notify();
            }

        }

        protected synchronized Runnable getTask()
                throws InterruptedException {
            while (taskQueue.size() == 0) {
                if (!isAlive) {
                    return null;
                }
                wait();
            }
            return (Runnable) taskQueue.removeFirst();
        }

        /**
         * Closes this ThreadPool and returns immediately. All threads are
         * stopped, and any waiting tasks are not executed. Once a ThreadPool is
         * closed, no more tasks can be run on this ThreadPool.
         */
        public synchronized void close() {
            if (isAlive) {
                isAlive = false;
                taskQueue.clear();
                interrupt();
            }
        }

        /**
         * Closes this ThreadPool and waits for all running threads to finish.
         * Any waiting tasks are executed.
         */
        public void join() {
            // notify all waiting threads that this ThreadPool is no
            // longer alive
            synchronized (this) {
                isAlive = false;
                notifyAll();
            }

            // wait for all threads to finish
            Thread[] threads = new Thread[activeCount()];
            int count = enumerate(threads);
            for (int i = 0; i < count; i++) {
                try {
                    threads[i].join();
                } catch (InterruptedException ex) {
                }
            }
        }

        /**
         * Signals that a PooledThread has started. This method does nothing by
         * default; subclasses should override to do any thread-specific startup
         * tasks.
         */
        protected void threadStarted() {
            // do nothing
        }

        /**
         * Signals that a PooledThread has stopped. This method does nothing by
         * default; subclasses should override to do any thread-specific cleanup
         * tasks.
         */
        protected void threadStopped() {
            // do nothing
        }

        /**
         * A PooledThread is a Thread in a ThreadPool group, designed to run
         * tasks (Runnables).
         */
        private class PooledThread extends Thread {

            public PooledThread() {
                super(ThreadPool.this,
                        "PooledThread-" + (threadID++));
            }

            public void run() {
                // signal that this thread has started
                threadStarted();

                while (!isInterrupted()) {

                    // get a task to run
                    Runnable task = null;
                    try {
                        task = getTask();
                    } catch (InterruptedException ex) {
                    }

                    // if getTask() returned null or was interrupted,
                    // close this thread.
                    if (task == null) {
                        break;
                    }

                    // run the task, and eat any exceptions it throws
                    try {
                        task.run();
                    } catch (Throwable t) {
                        uncaughtException(this, t);
                    }
                }
                // signal that this thread has stopped
                threadStopped();
            }
        }
    }
}
