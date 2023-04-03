package control;

import data.NasaAPODClient;

public class APODDataParser extends DataParser {

    public APODDataParser() {
        super(new NasaAPODClient(e -> {
        }));
    }

    public String parseImageToJson(){
        return getJsonObject().getString("url");
    }

    public String parseTitleToJson(){
        return getJsonObject().getString("title");
    }

    public String parseDescriptionToJson(){
        return getJsonObject().getString("explanation");
    }

}
