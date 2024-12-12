package com.challenge.bancaya.entities;

import javax.persistence.*;
import java.sql.Date;

@Entity
public class RequestTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ip;
    private Date requestDate;
    private String method;

    private long duration;

    @Lob
    private String soapRequest;

    @Lob
    private String soapResponse;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getSoapRequest() {
        return soapRequest;
    }

    public void setSoapRequest(String soapRequest) {
        this.soapRequest = soapRequest;
    }

    public String getSoapResponse() {
        return soapResponse;
    }

    public void setSoapResponse(String soapResponse) {
        this.soapResponse = soapResponse;
    }
}
