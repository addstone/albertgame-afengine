package afengine.part.sound;

import afengine.core.AbPartSupport;
import afengine.core.util.Debug;
import afengine.core.util.IXMLPartBoot;
import org.dom4j.Element;

/** 
 * <part name="SoundPart" path="afengine.part.sound.SoundPartBoot" />
 *
 * <SoundPart bgm=""/>
 * @author Albert Flex
 */
public class XMLSoundPartBoot implements IXMLPartBoot{
    
    @Override
    public AbPartSupport bootPart(Element element) {
        SoundCenter center=SoundCenter.getInstance();
        String bgmpath=element.attributeValue("bgm");
        if(bgmpath!=null){
            if(bgmpath.endsWith(".mid")||bgmpath.endsWith("midi")){
                long id=center.addMidi(bgmpath);
                center.playMidi(id, true);
            }else{
                Debug.log("warning:bgm file must be .midi or .mid file!");
            }
        }
        return new SoundPart();
    }    
}
