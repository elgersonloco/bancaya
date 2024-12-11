package com.challenge.bancaya.interceptors;

import com.challenge.bancaya.entities.RequestTracking;
import com.challenge.bancaya.repositories.RequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.server.endpoint.MethodEndpoint;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpServletConnection;
import org.w3c.dom.Document;

import javax.servlet.http.HttpServletRequest;
import javax.xml.XMLConstants;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import java.io.ByteArrayOutputStream;
import java.sql.Date;
import java.util.Optional;


@Component
public class SoapRequestTimingInterceptor implements EndpointInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(SoapRequestTimingInterceptor.class);


    private final RequestRepository requestRepository;

    public SoapRequestTimingInterceptor(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }


    @Override
    public boolean handleRequest(MessageContext messageContext, Object endpoint) throws Exception {
        messageContext.setProperty("startTime", System.currentTimeMillis());

        TransportContext transportContext = TransportContextHolder.getTransportContext();
        HttpServletConnection connection = (HttpServletConnection) transportContext.getConnection();
        HttpServletRequest request = connection.getHttpServletRequest();
        String remoteIp = request.getRemoteAddr();
        String soapContent = getRequestBody(messageContext);


        RequestTracking requestTracking = new RequestTracking();
        requestTracking.setIp(remoteIp);
        requestTracking.setRequestDate(new Date(System.currentTimeMillis()));
        requestTracking.setMethod(((MethodEndpoint) endpoint).getMethod().getName());
        if (soapContent != null) {
            requestTracking.setSoapRequest(soapContent);
        } else {
            requestTracking.setSoapRequest("No SOAP Request");
        }

        RequestTracking save = requestRepository.save(requestTracking);

        messageContext.setProperty("id_tracking", save.getId());

        return true;
    }

    private static String getRequestBody(MessageContext messageContext) {
        if (messageContext.getRequest() instanceof SoapMessage soapRequest) {
            try {
                // Obtain the SOAP content as Document
                DOMResult domResult = new DOMResult();
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
                transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
                transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
                transformerFactory.newTransformer().transform(soapRequest.getPayloadSource(), domResult);
                Document soapDoc = (Document) domResult.getNode();

                // Convert to String for logging
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                transformerFactory.newTransformer().transform(new DOMSource(soapDoc), new javax.xml.transform.stream.StreamResult(outputStream));

                return outputStream.toString();

            } catch (Exception e) {
                logger.error("Error extracting request_body", e);
                return null;
            }
        } else {
            logger.warn("Request is not a SOAP message");
        }
        return null;
    }

    private static String getResponseBody(MessageContext messageContext) {
        if (messageContext.getRequest() instanceof SoapMessage) {
            SoapMessage soapRequest = (SoapMessage) messageContext.getResponse();
            try {
                // Obtain the SOAP content as Document
                DOMResult domResult = new DOMResult();
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
                transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
                transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
                transformerFactory.newTransformer().transform(soapRequest.getPayloadSource(), domResult);
                Document soapDoc = (Document) domResult.getNode();

                // Convert to String for logging
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                transformerFactory.newTransformer().transform(new DOMSource(soapDoc), new javax.xml.transform.stream.StreamResult(outputStream));

                return outputStream.toString();

            } catch (Exception e) {
                logger.error("Error extracting response_body", e);
                return null;
            }
        } else {
            logger.warn("Request is not a SOAP message");
        }
        return null;
    }


    @Override
    public boolean handleResponse(MessageContext messageContext, Object endpoint) throws Exception {
        long startTime = (Long) messageContext.getProperty("startTime");
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;


        Optional<RequestTracking> optionalRequest = requestRepository.findById((Long) messageContext.getProperty("id_tracking"));
        if (optionalRequest.isPresent()) {
            RequestTracking request = optionalRequest.get();
            request.setSoapResponse(getResponseBody(messageContext));
            request.setDuration(duration);
            requestRepository.save(request);
        }

        return true;
    }

    @Override
    public boolean handleFault(MessageContext messageContext, Object endpoint) throws Exception {
        return true;
    }

    @Override
    public void afterCompletion(MessageContext messageContext, Object endpoint, Exception ex) throws Exception {
        // No-op
    }
}
