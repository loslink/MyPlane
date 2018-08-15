const char* ssid     = "Loslink";
const char* password = "laoqilian";
void connectWifi(){
    Serial.begin(9600);
  delay(10);
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  WiFi.begin(ssid, password);
  printMac();

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  server.begin();
  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
  }

void doWifi(){
  WiFiClient client = server.available();
  if (!client) {
    return;
  }

  Serial.println("new client");

  String req = client.readStringUntil('\r');
  Serial.println(req);
  client.flush();

  String respose = "mada:";
  if (req.indexOf("/gpio/0") != -1) {
    respose += "no";
    digitalWrite(pinPWM, LOW);    // 输出低电平（关闭LED）
    delay(1);
  } else if (req.indexOf("/gpio/1") != -1) {
    respose += "yes";
    digitalWrite(pinPWM, HIGH);   // 输出高电平（打开LED）
    delay(1);
  } else {
    Serial.println("invalid request");
    client.stop();
    return;
  }

  client.print(respose);
  delay(1);
  Serial.println("client disconneted");
  }
