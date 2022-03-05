import java.io.IOException;
import java.time.LocalTime;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CurrencyRates {

	public void connectZonda(long timeStart, long timeEnd, String currency) throws IOException {
		ReadJSON read = new ReadJSON();
		boolean longBool = false;
		boolean shortBool = false;
		String zondaAnswer = "";
		String timeStartString = Long.toString(timeStart);
		String timeEndString = Long.toString(timeEnd);
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url("https://api.zonda.exchange/rest/trading/candle/history/"+currency+"-PLN/3600?from="+timeStartString+"&to="+timeEndString).get().addHeader("Accept", "application/json").build();
		Response response = client.newCall(request).execute();
		if (response.isSuccessful()) {
			zondaAnswer = response.body().string();
		}
		read.readAnswer(zondaAnswer, longBool, shortBool);

	}
	
	

}