package com.loslink.plane.myplane;

/**
 * @author loslink
 * @time 2018/8/10 14:16
 */

import hypermedia.net.UDP;
import ketai.sensors.KetaiSensor;
import ketai.ui.KetaiVibrate;
import processing.core.PApplet;

public class wifiplane
        extends PApplet
{
    int DC_UPDATE = 1;
    byte P_ID = 1;
    float accelerometerX;
    float accelerometerY;
    float accelerometerZ;
    int app_start = 1;
    int dc_count = 0;
    float diff_power = 2.2F;
    int exprt_flag = 0;
    int gas = 0;
    int l_speed = 0;
    int localPort = 2390;
    int lock = 0;
    int offsetl = 0;
    int offsetr = 0;
    int r_speed = 0;
    String remotIp = "192.168.43.255";
    int remotPort = 6000;
    int rssi = 0;
    int rst_count = 0;
    KetaiSensor sensor;
    UDP udp;
    int vcc = 0;
    int vib_count = 0;
    KetaiVibrate vibe;

    public static void main(String[] paramArrayOfString)
    {
        String[] arrayOfString = new String[4];
        arrayOfString[0] = "--present";
        arrayOfString[1] = "--window-color=#666666";
        arrayOfString[2] = "--stop-color=#cccccc";
        arrayOfString[3] = "wifiplane";
        if (paramArrayOfString != null)
        {
            PApplet.main(concat(arrayOfString, paramArrayOfString));
            return;
        }
        PApplet.main(arrayOfString);
    }

    public void draw()
    {
        background(125.0F, 255.0F, 200.0F);
        fill(255);
        stroke(163);
        rect(0.0F, 0.0F, this.width / 4, this.height / 4);
        rect(this.width * 3 / 4, 0.0F, this.width / 4, this.height / 4);
        rect(0.0F, this.height / 4, this.width / 4, this.height / 4);
        rect(this.width * 3 / 4, this.height / 4, this.width / 4, this.height / 4);
        rect(0.0F, this.height * 7 / 8, this.width, this.height / 8);
        fill(color(255, 100, 60));
        rect(this.width / 4, 0.0F, this.width / 2, this.height * 7 / 8);
        fill(color(100, 150, 255));
        rect(this.width / 4, 0.0F, this.width / 2, this.height * 7 / 8 - this.gas * 7 * this.height / 1016);
        textSize(this.height / 12);
        textAlign(3, 3);
        fill(color(50, 100, 255));
        text("+", this.width / 8, this.height / 8 - 10);
        text("-", this.width / 8, this.height * 3 / 8 - 10);
        text("+", this.width * 3 / 4 + this.width / 8, this.height / 8 - 10);
        text("-", this.width * 3 / 4 + this.width / 8, this.height * 3 / 8 - 10);
        fill(0);
        text(this.gas * 100 / 127, this.width / 2, this.height / 2);
        text(this.offsetl, this.width / 8, this.height / 4 - 10);
        text(this.offsetr, this.width * 3 / 4 + this.width / 8, this.height / 4 - 10);
        label593:
        label984:
        label999:
        Object localObject;
        if (this.exprt_flag == 0)
        {
            text("BG", this.width / 8, this.height / 2 + this.height / 6);
            if (this.lock != 0) {
                break label1186;
            }
            text("LOCKED", this.width / 2, this.height * 7 / 8 + this.height / 16);
            textSize(this.height / 14);
            fill(255);
            if (this.rssi != 0) {
                break label1229;
            }
            text("-" + Character.toString('∞') + "dBm", this.width / 2, this.height * 3 / 4);
            label665:
            text(this.vcc / 10 + "." + this.vcc % 10 + "V", this.width / 2, this.height * 3 / 4 + this.height / 12);
            fill(0);
            textSize(this.height / 30);
            textAlign(3, 3);
            text("Instructables", this.width / 2, this.height / 20);
            text("WiFi Plane App", this.width / 2, this.height * 2 / 20);
            text("By Ravi Butani", this.width / 2, this.height * 3 / 20);
            textSize(this.height / 12);
            delay(1);
            this.dc_count += 1;
            if (this.dc_count >= this.DC_UPDATE)
            {
                this.rst_count += 1;
                if (this.rst_count >= 200)
                {
                    this.vcc = 0;
                    this.rssi = 0;
                }
                this.dc_count = 0;
                if (this.accelerometerX <= 1.5F) {
                    break label1276;
                }
                this.accelerometerX -= 1.5F;
                label917:
                this.l_speed = ((int)(this.gas + this.offsetl + this.accelerometerX * this.diff_power));
                this.r_speed = ((int)(this.gas + this.offsetr - this.accelerometerX * this.diff_power));
                if (this.l_speed < 127) {
                    break label1308;
                }
                this.l_speed = 127;
                if (this.r_speed < 127) {
                    break label1324;
                }
                this.r_speed = 127;
                localObject = new byte[3];
                Object tmp1004_1003 = localObject;
                tmp1004_1003[0] = 49;
                Object tmp1009_1004 = tmp1004_1003;
                tmp1009_1004[1] = 50;
                Object tmp1014_1009 = tmp1009_1004;
                tmp1014_1009[2] = 51;
                tmp1014_1009;
                localObject[0] = this.P_ID;
                if (this.lock != 1) {
                    break label1340;
                }
                localObject[1] = ((byte)this.l_speed);
                localObject[2] = ((byte)this.r_speed);
                this.vib_count += 1;
                if ((this.vcc < 35) && (this.vib_count < 5)) {
                    this.vibe.vibrate(1000L);
                }
                if (this.vib_count >= 40) {
                    this.vib_count = 0;
                }
            }
        }
        for (;;)
        {
            println(localObject[1]);
            println(localObject[2]);
            localObject = new String((byte[])localObject);
            this.udp.send((String)localObject, this.remotIp, this.remotPort);
            println("msgsend");
            return;
            if (this.exprt_flag != 1) {
                break;
            }
            text("EX", this.width / 8, this.height / 2 + this.height / 6);
            break;
            label1186:
            if (this.lock != 1) {
                break label593;
            }
            text("ACTIVATED", this.width / 2, this.height * 7 / 8 + this.height / 16);
            break label593;
            label1229:
            text("-" + this.rssi + "dBm", this.width / 2, this.height * 3 / 4);
            break label665;
            label1276:
            if (this.accelerometerX < -1.5F)
            {
                this.accelerometerX += 1.5F;
                break label917;
            }
            this.accelerometerX = 0.0F;
            break label917;
            label1308:
            if (this.l_speed > 1) {
                break label984;
            }
            this.l_speed = 1;
            break label984;
            label1324:
            if (this.r_speed > 1) {
                break label999;
            }
            this.r_speed = 1;
            break label999;
            label1340:
            if (this.lock == 0)
            {
                localObject[1] = 1;
                localObject[2] = 1;
            }
        }
    }

    public void mouseDragged()
    {
        if ((this.mouseY < this.height * 7 / 8) && (this.mouseX > this.width / 4) && (this.mouseX < this.width * 3 / 4) && (this.lock == 1)) {
            this.gas = (127 - (int)(this.mouseY / (this.height * 7 / 8) * 127.0F));
        }
    }

    public void mousePressed()
    {
        if ((this.mouseX < this.width / 4) && (this.mouseY < this.height / 4)) {
            this.offsetl += 1;
        }
        do
        {
            return;
            if ((this.mouseX < this.width / 4) && (this.mouseY < this.height / 2))
            {
                this.offsetl -= 1;
                return;
            }
            if ((this.mouseX > this.width * 3 / 4) && (this.mouseY < this.height / 4))
            {
                this.offsetr += 1;
                return;
            }
            if ((this.mouseX > this.width * 3 / 4) && (this.mouseY < this.height / 2))
            {
                this.offsetr -= 1;
                return;
            }
            if ((this.mouseX < this.width / 4) && (this.mouseY < this.height * 3 / 4))
            {
                if (this.exprt_flag == 0)
                {
                    this.exprt_flag = 1;
                    this.diff_power = 3.9F;
                    return;
                }
                this.exprt_flag = 0;
                this.diff_power = 2.2F;
                return;
            }
        } while (this.mouseY <= this.height * 7 / 8);
        this.gas = 0;
        if (this.lock == 0)
        {
            this.lock = 1;
            return;
        }
        this.lock = 0;
    }

    public void onAccelerometerEvent(float paramFloat1, float paramFloat2, float paramFloat3)
    {
        this.accelerometerX = paramFloat1;
        this.accelerometerY = paramFloat2;
        this.accelerometerZ = paramFloat3;
    }

    public void receive(byte[] paramArrayOfByte, String paramString, int paramInt)
    {
        this.rst_count = 0;
        this.rssi = paramArrayOfByte[1];
        this.vcc = (paramArrayOfByte[2] + 3);
    }

    public void settings()
    {
        size(this.displayWidth, this.displayHeight);
    }

    public void setup()
    {
        orientation(1);
        this.udp = new UDP(this, this.localPort);
        this.udp.listen(true);
        this.sensor = new KetaiSensor(this);
        this.vibe = new KetaiVibrate(this);
        this.sensor.start();
    }
}
