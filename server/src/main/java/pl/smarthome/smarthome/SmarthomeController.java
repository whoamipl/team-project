package pl.smarthome.smarthome;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
public class SmarthomeController {
    private final static String baseSwitchUrl = "http://192.168.0.15:8081";
    private final static String switchCredentials = "TheBestTeam:WiesioKiller";
    private static SwitchStatus status = new SwitchStatus();
    String encodedCredentials = Base64.getEncoder().encodeToString(switchCredentials.getBytes(StandardCharsets.UTF_8));

    @GetMapping("/toggle")
    public String changeSwitchStatus() throws IOException {
        status.setStatus(getStatus("/status"));

        if (status.getStatus().equals("off")) {
            return getStatus("/on");

        } else {
            return getStatus("/off");
        }
    }

    @GetMapping("/status")
    public String getSwitchStatus() throws IOException {

        return getStatus("/status");
    }

    private String getStatus(String s) throws IOException {
        URL url = new URL(baseSwitchUrl + s);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("Authorization", "Basic " + encodedCredentials);
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println(response);
        Gson g = new Gson();
        SwitchStatus p = g.fromJson(String.valueOf(response), SwitchStatus.class);
        return p.getStatus();
    }
}
