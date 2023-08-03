package com.demo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherApp {
    private static final String API_URL =
        "https://samples.openweathermap.org/data/2.5/forecast/hourly?q=London,us&appid=b6907d289e10d714a6e88b30761fae22";

    
    private static BufferedReader sendRequest(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } else {
            System.out.println("Error: " + responseCode);
            return null;
        }
    }

    
    public static void main(String[] args) throws JSONException {
        try {
            int option;
            do {
                System.out.println("1. Get weather");
                System.out.println("2. Get Wind Speed");
                System.out.println("3. Get Pressure");
                System.out.println("0. Exit");
                System.out.print("Enter your choice: ");

                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                option = Integer.parseInt(reader.readLine());

                switch (option) {
                    case 1:
                        getWeatherData();
                        break;
                    case 2:
                        getWindSpeedData();
                        break;
                    case 3:
                        getPressureData();
                        break;
                    case 0:
                        System.out.println("Exiting the program.");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (option != 0);
        } catch (IOException e) {
            System.out.println("An error occurred while reading input.");
            e.printStackTrace();
        }
    }

    private static JSONObject getWeatherData() throws IOException, JSONException {
        BufferedReader reader = sendRequest(API_URL);
        if (reader != null) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(response.toString());
            JSONArray list = jsonObject.getJSONArray("list");

            System.out.print("Enter the date (yyyy-MM-dd HH:mm:ss): ");
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
            String inputDate = inputReader.readLine();

            for (int i = 0; i < list.length(); i++) {
                JSONObject forecast = list.getJSONObject(i);
                String dateTime = forecast.getString("dt_txt");

                if (dateTime.equals(inputDate)) {
                    JSONObject main = forecast.getJSONObject("main");
                    double temperature = main.getDouble("temp");
                    System.out.println("Temperature on " + inputDate + ": " + temperature + "°C");
                    return forecast;
                }
            }
            System.out.println("Weather data not found for the specified date.");
        }
        return null;
    }

    private static void getWindSpeedData() throws IOException, JSONException {
        JSONObject forecast = getWeatherData();
        if (forecast != null) {
            JSONObject wind = forecast.getJSONObject("wind");
            double windSpeed = wind.getDouble("speed");
            System.out.println("Wind Speed on " + forecast.getString("dt_txt") + ": " + windSpeed + " m/s");
        }
    }

    private static void getPressureData() throws IOException, JSONException {
//        JSONObject forecast = getWeatherData();
//        if (forecast != null) {
//            JSONObject main = forecast.getJSONObject("main");
//         
    	{
            JSONObject forecast = getWeatherData();
            if (forecast != null) {
                JSONObject main = forecast.getJSONObject("main");
                double pressure = main.getDouble("pressure");
                System.out.println("Pressure on " + forecast.getString("dt_txt") + ": " + pressure + " hPa");
            }
        }
            
        }
}
        
    
           
