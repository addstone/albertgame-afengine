/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.app.particle;

import albertgame.afengine.graphics.IColor;
import albertgame.afengine.util.math.Vector;

public class Particle {
    public Vector position,velocity,accelerate;
    public IColor color;
    public int life,age,width,height;

    public Particle(Vector position, Vector velocity, Vector accelerate,
            IColor color, int life,int width, int height) {
        this.position = position;
        this.velocity = velocity;
        this.accelerate=accelerate;
        this.color = color;
        this.life = life;
        this.age = 0;
        this.width = width;
        this.height = height;
    }
}
