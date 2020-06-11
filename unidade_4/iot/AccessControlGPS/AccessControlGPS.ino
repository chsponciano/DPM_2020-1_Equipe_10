// Academics: Carlos Henrique Ponciano da Silva & Vinicius Luis da Silva
// Access control project by GPS - React Native & Arduino
// Using the module GPS Ublox Neo-6m

#include <Process.h>
#include <SoftwareSerial.h>
#include <TinyGPS.h>
#include <YunServer.h>
#include <YunClient.h>

SoftwareSerial serialGps(6, 7); // RX, TX
TinyGPS gps;
YunServer server;
String ip;

void setup() {
  Bridge.begin();  
  server.listenOnLocalhost();
  server.begin();
  serialGps.begin(9600);
  Serial.begin(9600);  
  getIp();
}

void loop() {
  YunClient client = server.accept();
  
  if (client) {
    String action = client.readString();
    action.trim();
    
    if (action == "ip") {
      client.print(ip);
    }
    
    client.stop();
  }
  
  delay(100); 
}

//void getSatelliteData() {
//  bool received = false;
//  static unsigned long delayPrint;
//
//  while (serialGps.available()) {
//     char cIn = serialGps.read();
//     received = (gps.encode(cIn) || received);  //Verifica até receber o primeiro sinal dos satelites
//  }
//  
//  if ( (recebido) && ((millis() - delayPrint) > 1000) ) {  //Mostra apenas após receber o primeiro sinal. Após o primeiro sinal, mostra a cada segundo.
//     delayPrint = millis();
//     
//     Serial.println("----------------------------------------");
//     
//     float latitude, longitude; //As variaveis podem ser float, para não precisar fazer nenhum cálculo
//     unsigned long info;
//     
//     gps.f_get_position(&latitude, &longitude, &info); //O método f_get_position é mais indicado para retornar as coordenadas em variáveis float, para não precisar fazer nenhum cálculo
//     
//     if (latitude != TinyGPS::GPS_INVALID_F_ANGLE) {
//        Serial.print("Latitude: ");
//        Serial.println(latitude, 6);  //Mostra a latitude com a precisão de 6 dígitos decimais
//     }
//
//     if (longitude != TinyGPS::GPS_INVALID_F_ANGLE) {
//        Serial.print("Longitude: ");
//        Serial.println(longitude, 6);  //Mostra a longitude com a precisão de 6 dígitos decimais
//     }
//
//     if ((latitude != TinyGPS::GPS_INVALID_F_ANGLE) && (longitude != TinyGPS::GPS_INVALID_F_ANGLE) ) {
//        Serial.print("Link para Google Maps:   https://maps.google.com/maps/?&z=10&q=");
//        Serial.print(latitude, 6);
//        Serial.print(",");
//        Serial.println(longitude, 6);           
//     }
//
//     if (info != TinyGPS::GPS_INVALID_AGE) {
//        Serial.print("Informacao (ms): ");
//        Serial.println(info);
//     }    
//     //float distancia_entre;
//     //distancia_entre = gps1.distance_between(lat1, long1, lat2, long2);
//}

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
    } 
  }
}
