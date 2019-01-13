# Prosty Firmware do Sonoff Basic
Prosty firmware do przełacznika Sonoff basic, wraz z podstawową autoryzacją (basic auth)

# Do zaimplementowania w przyszłości
- [ ] Dodanie prostego interfejsu do konfigurowania sieci WiFi



## Endpointy

    1. {IP}/
    2. {IP}/on
    3. {IP}/of
    
1. Zwraca status urządenia poniższej postaci.

    `{"status": "on"}`

2. Włącza przekaźnik
3. Wyłącza przekaźnik

### Instrukcja przepalania
    1. Utwórz swłasny plik credentials.h (spójrz na credentials_schema.h)
    2. Instrukcja jak przepalić + dodatkowe libki: https://medium.com/@jeffreyroshan/flashing-a-custom-firmware-to-sonoff-wifi-switch-with-arduino-ide-402e5a2f77b
    3. Jeżeli dane do logowania są podane poprawnie, połączy się z siecią, inaczej będzie się restartował i próbował od nowa

  
### Jak gadać
    Użytkownik: TheBestTeam
    Hasło: WiesioKiller
    Basic Auth:
        https://en.wikipedia.org/wiki/Basic_access_authentication
    Oznacza to że trzeba stworzyć Header w którym będą zakodowane informacje. 
    Tutaj link jak coś takiego w javie zrobić (wydaje się że wystarczy przekopiować):
        https://stackoverflow.com/questions/3283234/http-basic-authentication-in-java-using-httpclient
    
