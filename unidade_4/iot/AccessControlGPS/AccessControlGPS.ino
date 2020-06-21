// Academics: Carlos Henrique Ponciano da Silva & Vinicius Luis da Silva
// Access control project by GPS - React Native & Arduino
// Using the module GPS Ublox Neo-6m

#include <Process.h>
#include <SoftwareSerial.h>
#include <TinyGPS.h>
#include <YunServer.h>
#include <YunClient.h>
#include <HttpClient.h>

SoftwareSerial serialGps(11, 10); // RX, TX
TinyGPS gps;
YunServer server;

String ip;
String mac;
float latitude, longitude;
unsigned long info;

void setup() {
  Bridge.begin();  
  mac = "B4:21:8A:F8:53:BC";
  server.listenOnLocalhost();
  server.begin();
  serialGps.begin(9600);
  Serial.begin(9600);
  getIp();
}

void loop() {
  YunClient client = server.accept();
  getSatelliteData();
  
  if (client) {
    String action = client.readString();
    action.trim();
    
    client.println("Status: 200");
    client.println("Content-type: text/html");
    client.println();
    
    if (action.indexOf("distance") != -1) {
      float userLatitude, userLongitude;
      extractLocationParameters(action, &userLatitude, &userLongitude);
      client.println(getDistance(userLatitude, userLongitude));
    } else {
      client.println("<html><body>");
      client.println("<p><b>IP:</b> "+ ip +"</p>");
      client.println("<p><b>Latitude:</b> "+ floatToString(latitude) +"</p>");
      client.println("<p><b>Longitude:</b> "+ floatToString(longitude) +"</p><br>");
      client.println("</body></html>");
    }
    
    client.stop();
  }
  
  delay(100); 
}

void sendIp() {
  HttpClient client;
  const String &body = "{\"mac\":\"" + mac + "\",\"ip\":\"" + ip + "\",\"description\":\"Arduino Yun\",\"use\":\"true\"}";
  client.setHeader("Content-Type: application/json");
  client.post("http://192.168.2.11:8080/device", &body);
}

void getIp() {
  Process p;
  String data = "";
  
  p.runShellCommand("ifconfig");
  while(p.running());  
  while (p.available()) {
    char c = p.read();
    data += c;           
  }
  
  int start = 0;
  while((start = data.indexOf("inet addr", start)) != -1) {
    start += 10;
    String found = data.substring(start, data.indexOf(" ", start));
    if (found != "127.0.0.1") {
      ip = found;
      sendIp();
    } 
  }
}

void getSatelliteData() {
  bool received = false;
  static unsigned long delayReceived;

  while(serialGps.available()) {
    char cIn = serialGps.read();
    received = (gps.encode(cIn) || received);
  }

  if ((received) && ((millis() - delayReceived) > 1000)) {
    delayReceived = millis();
    gps.f_get_position(&latitude, &longitude, &info);
  } 
}

float getDistance(float userLatitude, float userLongitude) {
  return gps.distance_between(latitude, longitude, userLatitude, userLongitude);
}

void extractLocationParameters(String parameters, float *userLatitude, float *userLongitude) {
  int startParam = parameters.indexOf("/") + 1;
  int endParam = parameters.indexOf("/", startParam + 1);
  *userLatitude = parameters.substring(startParam, endParam).toFloat();
  
  startParam = parameters.indexOf("/", endParam);
  *userLongitude = parameters.substring(startParam + 1).toFloat();
}

String floatToString(float value) {
  char temp[10];
  dtostrf(value, 1, 2, temp);
  return String(temp);
}
