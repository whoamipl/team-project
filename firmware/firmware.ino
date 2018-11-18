
#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <ESP8266WebServer.h>
#include <ESP8266mDNS.h>

MDNSResponder mdns;

// Replace with your network credentials
const char* ssid = "YOUR_WIFI_SSID";
const char* password = "YOUR_WIFI_PASSWORD";

ESP8266WebServer server(80);

String web_on_html = "{\"status\": \"on\"}";
String web_off_html = "{\"status\": \"on\"}";

int gpio_13_led = 13;
int gpio_12_relay = 12;

void setup(void){  
  //  Init
  pinMode(gpio_13_led, OUTPUT);
  digitalWrite(gpio_13_led, HIGH);
  
  pinMode(gpio_12_relay, OUTPUT);
  digitalWrite(gpio_12_relay, HIGH);
 
  Serial.begin(115200); 
  delay(5000);

  WiFi.begin(ssid, password);
  Serial.println("Connecting to wifi..");

  // Wait for connection
  while (WiFi.status() != WL_CONNECTED) {
    digitalWrite(gpio_13_led, LOW);
    delay(500);
    Serial.print(".");
    Serial.println(WiFi.localIP());
    Serial.println(WiFi.status());
    digitalWrite(gpio_13_led, HIGH);
    delay(500);
  }
  
  Serial.println("");
  Serial.print("Connected to ");
  Serial.println(ssid);
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());
  
  if (mdns.begin("esp8266", WiFi.localIP())) {
    Serial.println("MDNS responder started");
  }
  
  server.on("/", [](){
    if(digitalRead(gpio_12_relay)==HIGH) {
      server.send(200, "text/html", web_on_html);
    } else {
      server.send(200, "text/html", web_off_html);
    }
  });
  
  server.on("/on", [](){
    server.send(200, "text/html", web_on_html);
    digitalWrite(gpio_13_led, LOW);
    digitalWrite(gpio_12_relay, HIGH);
    delay(1000);
  });
  
  server.on("/off", [](){
    server.send(200, "text/html", web_off_html);
    digitalWrite(gpio_13_led, HIGH);
    digitalWrite(gpio_12_relay, LOW);
    delay(1000); 
  });
  
  server.begin();
  Serial.println("Server ready..");
}
 
void loop(void){
  server.handleClient();
} 