package data;

import logging.LoggerManager;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import view.APODGeneratorUI;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NasaAPODClient {

    public interface ApiErrorCallback {
        void onError(Exception e);
    }

    private static final Logger LOGGER = LoggerManager.getLogger(APODGeneratorUI.class.getName());
    private static String apiKey = null;
    private ApiErrorCallback errorCallback;

    public NasaAPODClient(ApiErrorCallback errorCallback) {
        try(FileInputStream is = new FileInputStream("api_info.properties")) {
            Properties props = new Properties();
            props.load(is);
            apiKey = props.getProperty("NASA_API_KEY");
            this.errorCallback = errorCallback;
        } catch (IOException | RuntimeException e) {
            LOGGER.log(Level.SEVERE,e.getMessage());
            errorCallback.onError(e);
        }
    }

    private String getUrl(){
        try(FileInputStream is = new FileInputStream("api_info.properties")) {
            Properties props = new Properties();
            props.load(is);
            return props.getProperty("NASA_APOD_URL");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE,e.getMessage());
            errorCallback.onError(e);
        }
        return "";
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
            LOGGER.log(Level.SEVERE,e.getMessage());
            errorCallback.onError(e);
        }
        return "";
    }
}
