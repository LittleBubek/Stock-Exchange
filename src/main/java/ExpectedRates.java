import java.io.IOException;
import java.io.StringReader;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ExpectedRates {
	static ArrayList<Double> rateLong = new ArrayList<>();
	static ArrayList<Double> rateShort = new ArrayList<>();
	static ArrayList<Long> dates = new ArrayList<>();
	static ArrayList<Double> percentLong = new ArrayList<>();
	static ArrayList<Double> percentShort = new ArrayList<>();
	static int index = 0;
	
	public void connectZonda(String currency) throws IOException {
		long[] results = new long[3];
		results = getDates(results);
		ReadJSON read = new ReadJSON();
		String zondaAnswerLong = "";
		String zondaAnswerShort = "";
		boolean longBool = false;
		boolean shortBool = false;
		OkHttpClient client = new OkHttpClient();
		Request request1 = new Request.Builder().url("https://api.zonda.exchange/rest/trading/candle/history/"+currency+"-PLN/604800?from="+results[0]+"&to="+results[1]).get().addHeader("Accept", "application/json").build();
		Request request2 = new Request.Builder().url("https://api.zonda.exchange/rest/trading/candle/history/"+currency+"-PLN/604800?from="+results[1]+"&to="+results[2]).get().addHeader("Accept", "application/json").build();

		Response response1 = client.newCall(request1).execute();
		if (response1.isSuccessful()) {
			zondaAnswerLong = response1.body().string();
		}
		Response response2 = client.newCall(request2).execute();
		if (response2.isSuccessful()) {
			zondaAnswerShort = response2.body().string();
		}
		longBool = true;
		read.readAnswer(zondaAnswerLong, longBool, shortBool);
		longBool = false;
		shortBool = true;
		read.readAnswer(zondaAnswerShort, longBool, shortBool);
	}
	
	public void addLong(double number) {
		rateLong.add(number);
	}
	
	public void addShort(double number) {
		rateShort.add(number);
	}
	
	public void addDate(String value) {
		dates.add(Long.parseLong(value));
	}
	
	private long[] getDates(long[] results ) throws IOException {
		LocalDateTime timeStamp = LocalDateTime.of(1970, 1, 1, 0, 0);
		LocalDateTime startDateLong = LocalDateTime.now().minusYears(5).withHour(0).withMinute(0).withSecond(0).withNano(0);
		LocalDateTime startDateShort = LocalDateTime.now().minusMonths(4).withHour(0).withMinute(0).withSecond(0).withNano(0);
		LocalDateTime endDateShort = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
		
		long miliSecStartDifferenceLong = ChronoUnit.MILLIS.between(timeStamp, startDateLong);
		long miliSecStartDifferenceShort = ChronoUnit.MILLIS.between(timeStamp, startDateShort);
		long miliSecEndDifferenceShort = ChronoUnit.MILLIS.between(timeStamp, endDateShort);
		results[0] = miliSecStartDifferenceLong;
		results[1] = miliSecStartDifferenceShort;
		results[2] = miliSecEndDifferenceShort;
		return results;
	}
	
	public void porownanie() {
		
		for(int i = 1; i<rateLong.size(); i++) {
			double percent = ((rateLong.get(i)-rateLong.get(i-1))/rateLong.get(i))*100;
			percentLong.add(percent);
		}
		
		for(int i = 1; i<rateShort.size(); i++) {
			double percent = ((rateShort.get(i)-rateShort.get(i-1))/rateShort.get(i))*100;
			percentShort.add(percent);
		}
		
		int k = 0;
		int counter = 0;
		int maxCounter = 0;
		while(k<(percentLong.size()-percentShort.size())) {
			
			counter = 0;
			for (int i = 0, j = k; i < percentShort.size() && j < percentShort.size()+k; i++, j++) {
				double difference = Math.abs(percentShort.get(i) - percentLong.get(j));

				if (difference < 10) {
					counter++;
				}

			}
		
		if (counter > maxCounter) {
			maxCounter = counter;
			index = k;
		}

		k++;
	
		}
		
	}


}
