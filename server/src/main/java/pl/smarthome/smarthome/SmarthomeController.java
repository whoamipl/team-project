package pl.smarthome.smarthome;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@RestController
public class SmarthomeController {
    private final Map<String, String> baseSwitchUrls = Map
            .of("FIRST", "http://192.168.0.101", "SECOND", "http://192.168.0.102");
    private String baseSwitchUrl = "";
    private static SwitchStatus status = new SwitchStatus();
    private String encodedCredentials;

    @GetMapping("/toggle")
    public String changeSwitchStatus(@RequestHeader("Authorization") String auth, @RequestParam(value="sw") String sw ) throws IOException {
        System.out.println(sw);
        if (sw.equals("FIRST")) {
            baseSwitchUrl = baseSwitchUrls.get("FIRST");
        }
        else {
            baseSwitchUrl = baseSwitchUrls.get("SECOND");
        }

        status.setStatus(sendRequestToSonof("/"));
        encodedCredentials = auth;

        if (status.getStatus().equals("off")) {
            return sendRequestToSonof("/on");

        }
        else {
            return sendRequestToSonof("/off");
        }
    }


    @GetMapping("/status")
    public String getSwitchStatus(@RequestHeader("Authorization") String auth, @RequestParam(value="sw") String sw) throws IOException {
        if (sw.equals("FIRST")) {
            baseSwitchUrl = baseSwitchUrls.get("FIRST");
        }
        else {
            baseSwitchUrl = baseSwitchUrls.get("SECOND");
        }

        encodedCredentials = auth;
        return sendRequestToSonof("/");
    }

    private String sendRequestToSonof(String s) throws IOException {
        URL url = new URL(baseSwitchUrl + s);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("Authorization", encodedCredentials);
        con.setRequestMethod("GET");
        BufferedReader in = null;

        try {
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        }
        catch (ConnectException e) {
            System.out.println("error 501");
            status.setStatus("error 501");
            return status.getStatus();
        }
        catch (IOException e) {
            System.out.println("error " + con.getResponseCode());
            status.setStatus("error " + con.getResponseCode());
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
