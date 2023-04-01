package data;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class NasaAPODClient {
    private static String apiKey = null;

    public NasaAPODClient() {
        try(FileInputStream is = new FileInputStream("api_info.properties")) {
            Properties props = new Properties();
            props.load(is);
            apiKey = props.getProperty("NASA_API_KEY");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getUrl(){
        try(FileInputStream is = new FileInputStream("api_info.properties")) {
            Properties props = new Properties();
            props.load(is);
            return props.getProperty("NASA_APOD_URL");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getAPODJson() {
       try(CloseableHttpClient httpClient = HttpClients.createDefault()){
           HttpGet getRequest = new HttpGet(getUrl() + "?api_key=" + apiKey);
           HttpResponse response =  httpClient.execute(getRequest);
           HttpEntity entity = response.getEntity();
           if (entity != null) {
               return EntityUtils.toString(entity);
           } else {
               throw new IOException("Empty response from API");
           }
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
    }
}
