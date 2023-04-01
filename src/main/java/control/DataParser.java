package control;

import data.NasaAPODClient;
import org.json.JSONObject;

public abstract class DataParser {
    protected final NasaAPODClient nasaAPODClient;

    protected DataParser(NasaAPODClient nasaAPODClient) {
        this.nasaAPODClient = nasaAPODClient;
    }

    protected JSONObject getJsonObject() {
        return new JSONObject(nasaAPODClient.getAPODJson());
    }
}
