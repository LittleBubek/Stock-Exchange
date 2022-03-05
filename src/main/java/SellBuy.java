import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SellBuy {
	static ArrayList<Double> lista = new ArrayList();
	static ArrayList<String> czasy = new ArrayList();
	public static void kurs() throws IOException {
		String tekst = "";
		
		OkHttpClient client = new OkHttpClient();

		// od 1.02.2022 do 22.02.2022
		Request request = new Request.Builder().url(
				"https://api.zonda.exchange/rest/trading/candle/history/BTC-PLN/14400?from=1643670000000&to=1645484400000")
				.get().addHeader("Accept", "application/json").build();
		Response response = client.newCall(request).execute();

		if (response.isSuccessful()) {
			tekst = response.body().string();
		}

		System.out.println(tekst);
		odczyt(tekst);
		bot();
	}
	public static void odczyt(String tekst) throws IOException {
		JsonReader jsonReader = new JsonReader(new StringReader(tekst));
		boolean closed = false;
		String nazwa = "";
		String wartosc = "";
		int licznikDat = 5;
		
		while (closed == false && (jsonReader.hasNext() || jsonReader.peek() == JsonToken.END_OBJECT
				|| jsonReader.peek() == JsonToken.END_ARRAY)) {
			JsonToken nextToken = jsonReader.peek();

			if (JsonToken.END_DOCUMENT.equals(nextToken)) {
				jsonReader.close();
				closed = true;
			} else if (JsonToken.BEGIN_OBJECT.equals(nextToken)) {

				jsonReader.beginObject();
			} else if (JsonToken.END_OBJECT.equals(nextToken)) {

				jsonReader.endObject();
			}

			else if (JsonToken.NAME.equals(nextToken)) {

				nazwa = jsonReader.nextName();

			} else if (JsonToken.STRING.equals(nextToken)) {

				wartosc = jsonReader.nextString();

				if (nazwa.equals("h")) {
					double kurs = Double.parseDouble(wartosc);
					lista.add(kurs);
					
				}
				if(licznikDat == 6)
					czasy.add(wartosc);
				licznikDat ++;
				if(licznikDat == 7)
					licznikDat = 0;
				
			} else if (JsonToken.BEGIN_ARRAY.equals(nextToken)) {
				jsonReader.beginArray();

			} else if (JsonToken.END_ARRAY.equals(nextToken)) {
				jsonReader.endArray();
			}
		}
	}
	
	public static void bot() {
		ArrayList<Double> listaProcenty = new ArrayList();
		for(int i = 1; i<lista.size(); i++) {
			double procent = ((lista.get(i)-lista.get(i-1))/lista.get(i))*100;
			double roundProcent = Math.round(procent * 100.0) / 100.0;
			listaProcenty.add(roundProcent);
		}
		boolean kupione = false;
		double kwota = 500000;
		double cenaKupna = lista.get(0);
		String datePattern = "dd.MM.yyyy HH:mm";
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);
		System.out.println();
		System.out.println("Portfel: "+kwota);
		for(int i = 0; i<lista.size(); i++) {
			double procent = ((lista.get(i)-cenaKupna)/lista.get(i))*100;
			double roundProcent = Math.round(procent * 100.0) / 100.0;
			String time = czasy.get(i).substring(0, 10);
			LocalDateTime data = LocalDateTime.of(1970, 1, 1, 0, 0).plusSeconds(Integer.parseInt(time)+3600);
			System.out.println(dateFormatter.format(data));
			if(i == 0) {
				System.out.println("Kurs: "+lista.get(i)+" Zysk: "+roundProcent+" %");
			}
			else {
				if(procent >= 1 && kupione == true) {
					cenaKupna = lista.get(i);
					kupione = false;
					kwota += lista.get(i);
					double roundKwota = Math.round(kwota * 100.0) / 100.0;
					System.out.println("Kurs: "+lista.get(i)+" Zysk: "+roundProcent+" % Sprzeda¿, Portfel: "+roundKwota);
				}
				else if(procent <= -1 && kupione == false) {
					cenaKupna = lista.get(i);
					kupione = true;
					kwota -= lista.get(i);
					double roundKwota = Math.round(kwota * 100.0) / 100.0;
					System.out.println("Kurs: "+lista.get(i)+" Zysk: "+roundProcent+" % Kupno, Portfel: "+roundKwota);
				}
				else
					System.out.println("Kurs: "+lista.get(i)+" Zysk: "+roundProcent+" %");
				}
			System.out.println();
		}
	}
	
	public static void main(String[] args) throws IOException {
		kurs();
	}

}

//LocaleTime, LocaleDate, LocaleDateTime
