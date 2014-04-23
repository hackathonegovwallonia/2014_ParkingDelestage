package com.example.streetpark;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
* This class is used to send a rest request outside from the server
* @author fs
*/
public class RestRequest {

private final URL url;

public RestRequest(String baseUrl) throws MalformedURLException {
url = new URL(baseUrl);
}

/**
* Send a GET request to the url defined.
* The result is compared to the expected result given by the parameter
*
* e.g.: get("hola") will return true if the result is "hola", 
false otherwise
*
* @param result
* @return
* @throws IOException
*/
public boolean get(String expectedResult) throws IOException {
BufferedReader in = new BufferedReader(new 
InputStreamReader(url.openStream()));
String inputLine;
return ((inputLine = in.readLine()) != null && 
inputLine.equals(expectedResult));
}

/**
* Send a GET request to the url defined.
* @return
* @throws IOException
*/
public String get() throws IOException {
BufferedReader in = new BufferedReader(new 
InputStreamReader(url.openStream()));
return bufferReader(in);
}

private String bufferReader(BufferedReader in) throws IOException{
StringBuilder stringBuilder = new StringBuilder();
String inputLine = in.readLine();
while (inputLine != null) {
stringBuilder.append(inputLine);
inputLine = in.readLine();
}
return stringBuilder.toString();
}

public String post(String jsonValue) throws IOException {

HttpURLConnection conn = (HttpURLConnection) url.openConnection();
conn.setRequestMethod("POST");
conn.setDoOutput(true);
conn.setUseCaches(false);
conn.setRequestProperty("Content-Type", "application/json");
conn.setRequestProperty("Accept", "application/json");
conn.setRequestProperty("Content-Length", 
Integer.toString(jsonValue.length()));
conn.getOutputStream().write(jsonValue.getBytes());
conn.getOutputStream().flush();
conn.connect();

//reading response
return bufferReader(new BufferedReader(new 
InputStreamReader(conn.getInputStream())));
}

}