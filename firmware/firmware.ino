#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <ESP8266WebServer.h>
#include <ESP8266mDNS.h>

// This header is not attached to repo, also is added to .gitgnore.
// Setup it with your web credentials.QQQQQQ
#include "credentials_shema.h"
MDNSResponder mdns;
ESP8266WebServer server(80);

String web_on_html = "{\"status\": \"on\"}";
String web_off_html = "{\"status\": \"off\"}";

char* www_username = "TheBestTeam";
char* www_password = "WiesioKiller";

int gpio_13_led = 13;
int gpio_12_relay = 12;

void setup(void) {
  //  Init
  pinMode(gpio_13_led, OUTPUT);
  digitalWrite(gpio_13_led, HIGH);

  pinMode(gpio_12_relay, OUTPUT);
  digitalWrite(gpio_12_relay, HIGH);

  Serial.begin(115200);
  delay(5000);

  WiFi.begin(ssid, password);
  Serial.println("Connecting to wifi..");

  if (WiFi.waitForConnectResult() != WL_CONNECTED) {
    Serial.println("WiFi Connect Failed! Rebooting...");
    delay(1000);
    ESP.restart();
  }

  Serial.println("");
  Serial.print("Connected to ");
  Serial.println(ssid);
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());

  if (mdns.begin("esp8266", WiFi.localIP())) {
    Serial.println("MDNS responder started");
  }

  server.on("/", []() {
    if (digitalRead(gpio_12_relay) == HIGH) {
      server.send(200, "application/json", web_on_html);
    } else {
      server.send(200, "application/json", web_off_html);
    }
  });

  server.on("/on", []() {
    if (!server.authenticate(www_username, www_password)) {
      return server.requestAuthentication();
    }
    server.send(200, "application/json", web_on_html);
    digitalWrite(gpio_13_led, LOW);
    digitalWrite(gpio_12_relay, HIGH);
    delay(1000);
  });

  server.on("/off", []() {
    if (!server.authenticate(www_username, www_password)) {
      return server.requestAuthentication();
    }
    server.send(200, "application/json", web_off_html);
    digitalWrite(gpio_13_led, HIGH);
    digitalWrite(gpio_12_relay, LOW);
    delay(1000);
  });

  server.begin();
  Serial.println("Server ready..");
}

void loop(void) {
  server.handleClient();
}
