package stohio.barnraisr;

import org.json.JSONObject;
import org.json.JSONArray;

/**
 * Created by Blaise on 12/18/2015.
 */
public class event {
    String eventTitle, eventDesc, eventDate, eventTime, eventTimestamp, eventLong, eventLat, eventHostID;
        public event(String eventTitle, String eventDesc, String eventDate, String eventTime, String eventTimestamp, String eventLong, String eventLat, String eventHostID) {
            this.eventTitle = eventTitle;
            this.eventDesc = eventDesc;
            this.eventDate = eventDate;
            this.eventTime = eventTime;
            this.eventTimestamp = eventTimestamp;
            this.eventLong = eventLong;
            this.eventLat = eventLat;
            this.eventHostID = eventHostID;
        }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventHostID() {
        return eventHostID;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public String getEventLat() {
        return eventLat;
    }

    public String getEventLong() {
        return eventLong;
    }

    public String getEventTime() {
        return eventTime;
    }

    public String getEventTimestamp() {
        return eventTimestamp;
    }

    public String getEventTitle() {
        return eventTitle;
    }


}

