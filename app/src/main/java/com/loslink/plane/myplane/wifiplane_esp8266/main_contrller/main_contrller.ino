#include <ESP8266WiFi.h>

static const uint8_t pinPWM = D4;
int SpeedValue;
const int TrigPin = D6;
const int EchoPin = D5;
float cm;
float temp; 
WiFiServer server(80);

#define IN1 D0 //右轮
#define IN2 D1
#define IN3 D2 //左轮
#define IN4 D3

#define LEFT '3' //左转编码
#define RIGHT '4'//右转编码
#define GO '1'//前进编码
#define BACK '2'//后退编码
#define STOP '0'//停止编码

#define motorPwmL D7//左轮速度针脚
#define motorPwmR D8//右轮速度针脚

int speedLeft=250;
int speedRight=250;

char left_code = '1';

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  pinMode(IN1, OUTPUT);
  pinMode(IN2, OUTPUT);
  pinMode(IN3, OUTPUT);
  pinMode(IN4, OUTPUT);

  pinMode(motorPwmL, OUTPUT);
  pinMode(motorPwmR, OUTPUT);

  pinMode(TrigPin, OUTPUT);
  pinMode(EchoPin, INPUT);
  initCar();
}

void loop() {
go();
  getDistance();//获取障碍物距离
  //delay(100);
  //Serial.println("outside");
  // put your main code here, to run repeatedly:
  if (Serial.available() > 0) {
    //    Serial.println("inside");
    char ch = Serial.read();
    //      Serial.println(ch);
    if (ch == GO) {
      //前进
      go();
      Serial.println("go");
    } else if (ch == BACK) {
      //后退
      back();
      Serial.println("back");
    } else if (ch == LEFT) {
      //左转
      turnLeft();
      Serial.println("turnLeft");
    } else if (ch == RIGHT) {
      //右转
      turnRight();
      Serial.println("turnRight");
    } else if (ch == '0') {
      //停车
      stopCar();
      Serial.println("stopCar");
    }
  }
}

void initCar() {
  //默认全是低电平 停止状态
  digitalWrite(IN1, LOW);
  digitalWrite(IN2, LOW);
  digitalWrite(IN3, LOW);
  digitalWrite(IN4, LOW);
}

void setCarSpeed(int left,int right){
  
  analogWrite(motorPwmL,left); 
  analogWrite(motorPwmR,right); 
  }
/**
  左转
*/
void turnLeft() {
  setCarSpeed(speedLeft,speedRight);
  digitalWrite(IN1, HIGH);
  digitalWrite(IN2, LOW);        //右轮前进
  digitalWrite(IN3, LOW);
  digitalWrite(IN4, LOW);        //左轮不动
}

/**
  右转
*/
void turnRight() {
  setCarSpeed(speedLeft,speedRight);
  digitalWrite(IN1, LOW);
  digitalWrite(IN2, LOW);        //右轮不动
  digitalWrite(IN3, HIGH);
  digitalWrite(IN4, LOW);        //左轮前进
}

/**
  前进
*/
void go() {
  setCarSpeed(speedLeft,speedRight);
  digitalWrite(IN1, HIGH);
  digitalWrite(IN2, LOW);        //右轮前进
  digitalWrite(IN3, HIGH);
  digitalWrite(IN4, LOW);        //左轮前进
}

/**
  倒车
*/
void back() {
  setCarSpeed(speedLeft,speedRight);
  digitalWrite(IN1, LOW);
  digitalWrite(IN2, HIGH);       //右轮后退
  digitalWrite(IN3, LOW);
  digitalWrite(IN4, HIGH);       //左轮后退
}

/**
  停车
*/
void stopCar() {
  initCar();
}






