package pl.smarthome.smarthome;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
public class SmarthomeController {
    private final String baseSwitchUrl = "http://192.168.0.15:8081";
    private static SwitchStatus status = new SwitchStatus();
    private String encodedCredentials;
    @GetMapping("/toggle")
    public String changeSwitchStatus(@RequestHeader("Authorization") String auth) throws IOException {
        status.setStatus(sendRequestToSonof("/status"));
        encodedCredentials = auth;

        if (status.getStatus().equals("off")) {
            return sendRequestToSonof("/on");

        } else {
            return sendRequestToSonof("/off");
        }
    }

    @GetMapping("/status")
    public String getSwitchStatus(@RequestHeader("Authorization") String auth) throws IOException {
        encodedCredentials = auth;
        return sendRequestToSonof("/status");
    }

    private String sendRequestToSonof(String s) throws IOException {
        URL url = new URL(baseSwitchUrl + s);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("Authorization", encodedCredentials);
        con.setRequestMethod("GET");
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } catch (IOException e) {
            //e.printStackTrace();

            System.out.println("error " + con.getResponseCode());
            status.setStatus("error "+con.getResponseCode());
            return status.getStatus();
        }
        String inputLine;
        var response = new StringBuffer();

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
