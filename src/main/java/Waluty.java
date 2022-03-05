import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Waluty {
	static HashMap<String, ArrayList> mapa = new HashMap();
	static String rynek = "";
	//static String[] tab = {"DAI", "LML", "AVAX", "GRT"};
	static String[] tab = {"DAI", "LML", "AVAX", "GRT", "AMLT", "DASH", "SUSHI", "BOB", "COMP", "REP", "TRX", "DOT", "LINK", "REEF", "BTG", "BSV", "LUNA", "UNI", "MATIC", "ETH", "SXP", "BCP", "USDT", "ADA", "MANA", "BAT", "GAME", "ENJ", "WIS", "LSK", "SOL", "NEU", "ZRX", "CHZ", "XTZ", "USDC", "XIN", "LTC", "DOGE", "MKR", "ZEC", "XRP", "ATRI", "AAVE", "OMG", "XLM", "BCC", "BTC", "ALG", "PAY", "EOS", "GNT", "FTM"};
	
	public static void waluty() throws IOException {
		String tekst = "";
		OkHttpClient client = new OkHttpClient();
		
		
		for(int i = 0; i<tab.length; i++) {
			rynek = tab[i];
			// od 16.02 9.30 do 18.02 9.30
			Request request = new Request.Builder().url("https://api.zonda.exchange/rest/trading/candle/history/"+rynek+"-PLN/3600?from=1644760141000&to=1644932941000").get().addHeader("Accept", "application/json").build();
			Response response = client.newCall(request).execute();
			
	
			if (response.isSuccessful()) {
				tekst = response.body().string();
			}
		
			System.out.println(tekst);
			System.out.println();
			ArrayList<Double> kurs = new ArrayList<Double>();
			mapa.put(rynek, kurs);
			
			odczyt(tekst);
			
		}
		

	}

	public static void odczyt(String tekst) throws NumberFormatException, IOException{
		JsonReader jsonReader = new JsonReader(new StringReader(tekst));
		boolean closed = false;
		String nazwa = "";
		String wartosc = "";
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
				//System.out.println(nazwa);

			} else if (JsonToken.STRING.equals(nextToken)) {
			
				wartosc = jsonReader.nextString();
				if (nazwa.equals("h")) {
				mapa.get(rynek).add(Double.parseDouble(wartosc));
				}

			} else if (JsonToken.BEGIN_ARRAY.equals(nextToken)) {
				jsonReader.beginArray();

			} else if (JsonToken.END_ARRAY.equals(nextToken)) {
				jsonReader.endArray();
			}
		}
		
		

	}

	
	public static void analiza() {
		int licznikUjemnych = 0;
		int licznikDodatnich = 0;
		ArrayList<Integer> ujemne = new ArrayList();
		ArrayList<Integer> dodatnie = new ArrayList();
		for(int i = 0; i<tab.length; i++) {
			int trzyCzwarte = 3*(mapa.get(tab[i]).size())/4;
			for(int j =1; j<mapa.get(tab[i]).size(); j++) {
				double procent = (((double) mapa.get(tab[i]).get(j))-((double) mapa.get(tab[i]).get(j-1)))/((double) mapa.get(tab[i]).get(j))*100;
				if(j<= trzyCzwarte) {
					if(procent<0) {
						licznikUjemnych++;
					}	
				}
				else
					if(procent>0) {
						licznikDodatnich++;
					}
			
			}
			ujemne.add(licznikUjemnych);
			dodatnie.add(licznikDodatnich);
			
			licznikUjemnych = 0;
			licznikDodatnich = 0;
			
		}
		double maxSuma = 0;
		int indeks = 0;
		
		for(int i = 0; i<ujemne.size(); i++) {
			if((ujemne.get(i)+dodatnie.get(i)) > maxSuma) {
				maxSuma = ujemne.get(i)+dodatnie.get(i);
				indeks = i;
			}
		}
		System.out.println(tab[indeks]);
	}
	
	public static void main(String[] args) throws IOException {
		waluty();
		analiza();

	}
}
// jedna kryptowaluta, 5 dni, znalezc wczesniejsz¹ podobn¹ sytuacjê, zeby oszacowac co sie stanie