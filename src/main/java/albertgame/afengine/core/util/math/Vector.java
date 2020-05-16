/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package albertgame.afengine.core.util.math;

/**
 *
 * @author Administrator
 */
public class Vector {

    private double x, y, z, a;
    private double lengthcache;
    private boolean cachedirty;

    public Vector(double x, double y, double z, double a) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.a = a;
        cachedirty = true;
        getLength();
    }

    public Vector(double x, double y, double z) {
        this(x, y, z, 0f);
    }

    public Vector() {
        this(0f, 0f, 0f, 0f);
    }

    public synchronized double getX() {
        return x;
    }

    public synchronized void setX(double x) {
        cachedirty = true;
        this.x = x;
    }

    public synchronized double getY() {
        return y;
    }

    public synchronized void setY(double y) {
        cachedirty = true;
        this.y = y;
    }

    public synchronized double getZ() {
        return z;
    }

    public synchronized void setZ(double z) {
        cachedirty = true;
        this.z = z;
    }

    public synchronized double getA() {
        return a;
    }

    public synchronized void setA(double a) {
        this.a = a;
    }

    public synchronized double getLength() {
        if (cachedirty) {
            lengthcache = Math.sqrt(x * x + y * y + z * z);
            cachedirty = false;
        }

        return lengthcache;
    }

    public Vector addVector(Vector other) {
        double vx = this.x + other.x;
        double vy = this.y + other.y;
        double vz = this.z + other.z;
        return new Vector(vx, vy, vz, 0f);
    }

    public Vector delVector(Vector other) {
        double vx = this.x - other.x;
        double vy = this.y - other.y;
        double vz = this.z - other.z;
        return new Vector(vx, vy, vz, 0f);
    }

    public synchronized void dotNumber(double number) {
        x *= number;
        y *= number;
        z *= number;
        cachedirty = true;
    }

    public synchronized double dotVector(Vector other) {
        double result = x * other.x + y * other.y + z * other.z;
        return result;
    }

    public Vector crossVector(Vector other) {
        double vx = y * other.z - other.y * z;
        double vy = other.x * z - x * other.z;
        double vz = x * other.y - other.x * y;
        return new Vector(vx, vy, vz);
    }

    public synchronized void normal() {
        if (lengthcache == 0) {
            System.out.println("normal failed, cause divide by 0");
        } else {
            x /= lengthcache;
            y /= lengthcache;
            z /= lengthcache;
            cachedirty = true;
        }
    }

    public synchronized double cosAngleWithVector(Vector other) {
        double dots = dotVector(other);
        double length1 = getLength();
        double length2 = other.getLength();
        return dots / (length1 * length2);
    }
    
    public Vector copy(){
        return new Vector(x,y,z,a);
    }

    @Override
    public String toString() {
        return "[v:" + this.x + "," + this.y + "," + this.z + "]";
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 71 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        hash = 71 * hash + (int) (Double.doubleToLongBits(this.z) ^ (Double.doubleToLongBits(this.z) >>> 32));
        hash = 71 * hash + (int) (Double.doubleToLongBits(this.a) ^ (Double.doubleToLongBits(this.a) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Vector other = (Vector) obj;
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
            return false;
        }
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y)) {
            return false;
        }
        if (Double.doubleToLongBits(this.z) != Double.doubleToLongBits(other.z)) {
            return false;
        }
        if (Double.doubleToLongBits(this.a) != Double.doubleToLongBits(other.a)) {
            return false;
        }
        return true;
    }

}
