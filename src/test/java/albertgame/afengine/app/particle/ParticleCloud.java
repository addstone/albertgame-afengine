/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.app.particle;

import albertgame.afengine.graphics.IColor;
import albertgame.afengine.util.math.Vector;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleCloud {

    private final List<Particle> plist;

    private Vector initpos;//初始位置
    private Vector posoff = new Vector(0.1,0.1,0, 0);//位置偏移

    private Vector initvel;//初始速度
    private Vector veloff = new Vector(0.00001,0.00001, 0, 0);//速度偏移

    private Vector initacc;//初始加速度

    private IColor initcolor;//初始颜色

    private int initlife, initwidth, initheight;//初始生命周期

    private boolean loop;
    
    private Random random;
    
    private boolean cloudend;

    public ParticleCloud(Vector pos, Vector initvel, Vector initacc,
            IColor initcolor,boolean loop,
            int initlife, int initwidth, int initheight) {
        this.initpos = pos;
        this.loop=loop;
        this.initvel = initvel;
        this.initacc = initacc;
        this.initcolor = initcolor;
        this.initlife = initlife;
        this.initwidth = initwidth;
        this.initheight = initheight;
        this.plist = new ArrayList<>();
        random = new Random();
        cloudend=true;
    }
    
    public void emit(int size) {
        if (size < 1) {
            return;
        }
        for (int i = 0; i != size; ++i) {
            plist.add(createParticle());
        }
        cloudend=false;
        new Thread(()->{
            long nowtime,last=System.currentTimeMillis(),delt;
            while(!plist.isEmpty()){
                nowtime=System.currentTimeMillis();
                delt=nowtime-last;
                last=nowtime;
                
                updateCloud(delt);
            }
        }).start();
    }

    public boolean isCloudend() {
        return cloudend;
    }

    public void updateCloud(long delttime){
        if(plist.isEmpty()){
            cloudend=true;
            return;
        }
        for (int i = plist.size() - 1; i >= 0; --i) {
            Particle particle = plist.get(i);
            if (particle.age > particle.life){
                if(loop){
                    plist.set(i,createParticle());
                }
                else plist.remove(i);
            } else {
                updateParticle(particle, delttime);
            }
        }
    }

    public List<Particle> getPlist() {
        return plist;
    }

    private void updateParticle(Particle particle, long delttime) {
        Vector pos = particle.position;
        Vector vel = particle.velocity;
        Vector acc = particle.accelerate;
        //pos'=v
        //pos''=a
        //已知上一个点的位置p0(x0,y0,z0)，已知上一个点的速度v和加速度a，求时间过后的位置p1(x1,y1,z1)

        //work for destv
        //v=a*t+v0
        Vector flap = acc.copy();
        flap.dotNumber(delttime);
        //速度随机偏移
        Vector veloffvalue = new Vector(caloff(veloff.getX()), caloff(veloff.getY()), caloff(veloff.getZ()));
        particle.velocity = flap.addVector(vel).addVector(veloffvalue);

        //work for destp
        //p=1/2*a*t^2+v0*t+p0
        Vector flap2 = acc.copy();
        flap2.dotNumber(1 / 2 * delttime);
        Vector flap3 = vel.copy();
        flap3.dotNumber(delttime);
        //位置随机偏移
        Vector posoffvalue = new Vector(caloff(posoff.getX()), caloff(posoff.getY()), caloff(posoff.getZ()));
        particle.position = flap2.addVector(flap3).addVector(pos).addVector(posoffvalue);

        //work for age
        particle.age += delttime;
    }

    private Particle createParticle() {
        Vector pos = new Vector(initpos.getX(), initpos.getY(), initpos.getZ());
        Vector ve = new Vector(initvel.getX(), initvel.getY(), initvel.getZ());
        Vector acc = new Vector(initacc.getX(), initacc.getY(), initacc.getZ());
        Particle particle = new Particle(pos, ve, acc, initcolor, initlife, initwidth, initheight);
        return particle;
    }

    private double caloff(double ful) {
        int x = random.nextInt(10000);
        x -= 5000;
        double percent = (double)(x) / 5000.0;
        return ful * percent;
    }
}
