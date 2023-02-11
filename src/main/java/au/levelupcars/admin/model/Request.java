package au.levelupcars.admin.model;

import lombok.Getter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Request {
    @Getter
    private final UUID requestID;
    private final Date requestTime;
    @Getter
    private boolean responded;
    @Getter
    private final String clientName;
    @Getter
    private final String clientEmail;
    @Getter
    private final String clientPhone;
    @Getter
    private final String postCode;
    private final Date requestedDate;
    @Getter
    private final String carModel;
    @Getter
    private final String details;
    @Getter
    private final String[] serviceDetails;

    private final SimpleDateFormat requestTimeFormat;
    private final SimpleDateFormat requestedDateFormat;

    public Request(
        UUID requestID,
        Date requestTime,
        boolean responded,
        String clientName,
        String clientEmail,
        String clientPhone,
        String postCode,
        Date requestedDate,
        String carModel,
        String details,
        String[] serviceDetails
    ) {
        this.requestID = requestID;
        this.requestTime = requestTime;
        this.responded = responded;
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.clientPhone = clientPhone;
        this.postCode = postCode;
        this.requestedDate = requestedDate;
        this.carModel = carModel;
        this.details = details;
        this.serviceDetails = serviceDetails;
        requestTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh.mm aa");
        requestedDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    public String getRequestTime() {
        return requestTimeFormat.format(requestTime);
    }

    public String getRequestedDate() {
        return requestedDateFormat.format(requestedDate);
    }

    public void toggleResponded() {
        responded = !responded;
    }
}
