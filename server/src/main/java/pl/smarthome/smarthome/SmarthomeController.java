package pl.smarthome.smarthome;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;
import org.apache.catalina.authenticator.BasicAuthenticator;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.IOException;
import java.net.URI;
import java.util.Base64;

@RestController
public class SmarthomeController {
    private final static String baseSwitchUrl = "http://192.168.0.100";
    private final static String switchCredentials = "TheBestTeam:WiesioKiller";
    private static SwitchStatus status;

    @GetMapping("/toggle")
    public void changeSwitchStatus() {
        // Determine how to get switch status
        // Temporary set initial status to on
        status.setStatus("on");
        var httpClient = HttpClient.newHttpClient();

        var encodedCredentials = Base64.getEncoder().encode(switchCredentials.getBytes());
        var requestBuilder = HttpRequest.newBuilder();
        HttpResponse response;

        if (status.getStatus().equals("off")) {
            try {
                response = httpClient.send(
                        requestBuilder
                                .GET()
                                .header("Authorization", "Basic" + encodedCredentials)
                                .uri(URI.create(baseSwitchUrl + "/on"))
                                .build(),
                        HttpResponse.BodyHandler.asString()
                );
            }
            catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                response = httpClient.send(
                        requestBuilder
                                .GET()
                                .header("Authorization", "Basic" + encodedCredentials)
                                .uri(URI.create(baseSwitchUrl + "/off"))
                                .build(),
                        HttpResponse.BodyHandler.asString()
                );
            }
            catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping("/status")
    public String getSwitchStatus() {
        // We get status from switch and sent it back.
        var request = HttpRequest.newBuilder(URI.create(baseSwitchUrl)).build();
        var client = HttpClient.newHttpClient();
        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandler.asString());
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return response.body();
    }

}
