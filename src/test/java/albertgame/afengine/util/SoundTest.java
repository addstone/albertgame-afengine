/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.util;

import java.net.URL;

/**
 *
 * @author Administrator
 */
public class SoundTest{
    public static void main(String[] args) {
        new SoundTest().test1();
    }
    
    public void test1(){
        SoundUtil util=SoundUtil.get();
        URL url1=getClass().getClassLoader().getResource("midi1.mid");
        URL url2=getClass().getClassLoader().getResource("sound1.wav");
        System.out.println(url1.toString()); 
        System.out.println(url2.toString()); 
        long midiId=util.addMidi(url1);
        long soundId=util.addSound(url2);
        util.playMidi(midiId, true);
        util.playSound(soundId,5);
    }
}
