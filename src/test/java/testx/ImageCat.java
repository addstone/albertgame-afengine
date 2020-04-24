/*
 * Copyright 2020 Administrator.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package testx;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class ImageCat {
    public static void main(String[] args) {
        catImage("test/testx/Environment","test/testx/destimg.png",4,5);
    }
    public static void catImage(String imgpath,String destimgpath,int width,int height){
        File file=new File(imgpath);
        File[] files=file.listFiles();
        try {
            Image img=ImageIO.read(files[0]);
            BufferedImage bimg=new BufferedImage(img.getWidth(null)*width,img.getHeight(null)*height,BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics=(Graphics2D) bimg.getGraphics();
            int index=0;
            for(int i=0;i!=height;++i){
                for(int j=0;j!=width;++j){
                    Image iimage=ImageIO.read(files[index]);
                    graphics.drawImage(iimage, img.getWidth(null)*j, img.getHeight(null)*i,null);
                    ++index;
                }
            }
            graphics.dispose();
            File dest=new File(destimgpath);
            ImageIO.write(bimg,"PNG",dest);
                } catch (IOException ex) {
            Logger.getLogger(ImageCat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
