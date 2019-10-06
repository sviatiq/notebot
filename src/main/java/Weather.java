import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class Weather {

    public static String getWeather(String message) throws IOException {
        Model model = new Model();
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q="+message+"&units=metric&&appid=36e818d7b7bf6e8b1378e23392aab50d");

        Scanner in = new Scanner((InputStream) url.getContent());
        String result = "";
        while (in.hasNext()) {
            result += in.nextLine();

        }

        JSONObject object = new JSONObject(result);
        model.setName(object.getString("name"));
        JSONObject jsonMain = object.getJSONObject("main");
        model.setTemp(jsonMain.getDouble("temp"));
        model.setHumidity(jsonMain.getDouble("humidity"));
        JSONArray jsonArray = object.getJSONArray("weather");
        for(int i = 0; i<jsonArray.length(); i++){
            JSONObject obj = jsonArray.getJSONObject(i);
            model.setMain((String)obj.get("main"));
        }
        return "\uD83C\uDFD9Город: " + model.getName() +
                "\n"+ "\uD83C\uDF26Температура : " + model.getTemp()+" ºC"+"\n"
                +"\uD83D\uDCA7Влажность: " + model.getHumidity() + "%" +"\n"
                +"\uD83D\uDDD2Описание: "+ model.getMain();
    }
}
