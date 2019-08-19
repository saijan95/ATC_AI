package ATC_Core;

import java.sql.Timestamp;

/**
 * Created by saija on 2017-04-08.
 */
public class Message {
    private Timestamp timestamp;
    private String message;

    public Message(Timestamp timestamp, String message) {
        this.timestamp = timestamp;
        this.message = message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
