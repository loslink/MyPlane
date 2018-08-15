void getDistance() {
  //发一个10ms的高脉冲去触发TrigPin
  digitalWrite(TrigPin, LOW);
  delayMicroseconds(2);
  digitalWrite(TrigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(TrigPin, LOW);
  
  int in=pulseIn(EchoPin, HIGH);
//  Serial.print("Echo = ");
//  Serial.print(in);
//  Serial.print("us");
  temp = float(in) / 58.0; //算成厘米
  cm = (int(temp * 100.0)) / 100.0; //保留两位小数
  Serial.print("dis=");
  Serial.print(cm);
  delay(100);

}

void getDis2() {
  //给Trig发送一个低高低的短时间脉冲,触发测距
  digitalWrite(EchoPin, LOW); //给Trig发送一个低电平
  delayMicroseconds(2);    //等待 2微妙
  digitalWrite(EchoPin, HIGH); //给Trig发送一个高电平
  delayMicroseconds(10);    //等待 10微妙
  digitalWrite(EchoPin, LOW); //给Trig发送一个低电平

  temp = float(pulseIn(EchoPin, HIGH)); //存储回波等待时间,
  //pulseIn函数会等待引脚变为HIGH,开始计算时间,再等待变为LOW并停止计时
  //返回脉冲的长度

  //声速是:340m/1s 换算成 34000cm / 1000000μs => 34 / 1000
  //因为发送到接收,实际是相同距离走了2回,所以要除以2
  //距离(厘米)  =  (回波时间 * (34 / 1000)) / 2
  //简化后的计算公式为 (回波时间 * 17)/ 1000

  cm = (temp * 17 ) / 1000; //把回波时间换算成cm

  Serial.print("Echo =");
  Serial.print(temp);//串口输出等待时间的原始数据
  Serial.print(" || Distance = ");
  Serial.print(cm);//串口输出距离换算成cm的结果
  Serial.println("cm");
  delay(100);
}
