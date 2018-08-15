#define BTSerial Serial1
#define MyDebugSerial Serial

//该条语句用于使能DEBUG输出信息，屏蔽掉就不会输出debug调试信息
#define DEBUG

#ifdef DEBUG
#define DBGLN(message)    MyDebugSerial.println(message)
#else
#define DBGLN(message)
#endif

/**
* 读取串口缓冲区里面的数据
* 
*/
int ReceiveBTMessage(){
  /**
     * @Desc 把RGB组合成协议内的数据格式
     *  开始符+长度+rgb+结束符
     * "+C,n:rgbString;"
     * 例子:"+C,10:100,255,60;"
     **/
  String data = "";
  if (BTSerial.available()>0){
    unsigned long start;
    start = millis();
    char c0 = BTSerial.read();
    if (c0 == '+')
    {
      char c;
      while (millis()-start<200) 
      {
        if (BTSerial.available()>0)
        {
          c = BTSerial.read();
          data += c;
        }
        if (c==';')
          break;
      }
 
      int sLen = strlen(data.c_str());
      int i,r,g,b;
      for (i = 0; i <= sLen; i++){
        if (data[i] == ':'){
          break;
        }
      }
      DBGLN(data);
      int iSize;
      //"+C,10:100,255,60;"    
      String _size = data.substring(2, i);
      iSize = _size.toInt();
 
      for(r=i+1;r<=sLen;r++){
         if(data[r]==','){
           break;
          }  
      }
 
      for(g=r+1;g<=sLen;g++){
         if(data[g]==','){
           break;
          }  
      }
 
      for(b=g+1;b<=sLen;b++){
         if(data[b]==';'){
           break;
          }  
      }
 
      String sred = data.substring(i+1,r);
      String sgreen = data.substring(r+1,g);
      String sblue = data.substring(g+1,b);
 
//      red = sred.toInt();
//      green = sgreen.toInt();
//      blue = sblue.toInt();
// 
//      DBGLN("red:"+sred);
//      DBGLN("green:"+sgreen);
//      DBGLN("blue:"+sblue);
      return iSize;
    }else{
      while(BTSerial.read() >= 0){}  
    }
  }
  return 0;
}
