import java.io.IOException;
import java.io.StringReader;
import java.time.LocalTime;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

public class ReadJSON {
	public void readAnswer(String answer, boolean longBool, boolean shortBool) throws NumberFormatException, IOException{
		JsonReader jsonReader = new JsonReader(new StringReader(answer));
		ExpectedRates expected = new ExpectedRates();
		boolean closed = false;
		String name = "";
		String value = "";
		int counter = 0;
		int dateCounter = 5;
		double number = 0;
		double previous = 0;
		LocalTime time = LocalTime.MIDNIGHT;
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
				name = jsonReader.nextName();
			} else if (JsonToken.STRING.equals(nextToken)) {
				value = jsonReader.nextString();
				if (name.equals("h")) {
					number = Double.parseDouble(value);
					if(!longBool && !shortBool) {
						if (counter == 0) {
							JPanelStockExchange.l1.addElement("Time " + time + " rate: " + value);
						} else {
							double percent = ((number - previous) / previous) * 100;
							double roundPercent = Math.round(percent * 100.0) / 100.0;
							JPanelStockExchange.l1.addElement("Time " + time + " Rate: " + value);
							JPanelStockExchange.l1.addElement(roundPercent + "%");
						}
						time = time.plusHours(1);
						counter++;
						previous = number;
					}
					else if (longBool) {
						expected.addLong(number);
					}
					else if (shortBool) {
						expected.addShort(number);
					}
				}
				if(longBool) {
					if (dateCounter == 6) {
						expected.addDate(value);
					}
					dateCounter++;
					if (dateCounter == 7) {
						dateCounter = 0;
					}
				}
			} else if (JsonToken.BEGIN_ARRAY.equals(nextToken)) {
				jsonReader.beginArray();

			} else if (JsonToken.END_ARRAY.equals(nextToken)) {
				jsonReader.endArray();
			}
		}
	}
}
