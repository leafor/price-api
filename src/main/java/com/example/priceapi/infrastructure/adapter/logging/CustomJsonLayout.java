package com.example.priceapi.infrastructure.adapter.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.slf4j.MDC;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Plugin(name = "CustomJsonLayout", category = "Core", elementType = Layout.ELEMENT_TYPE, printObject = true)
public class CustomJsonLayout extends AbstractStringLayout {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String serviceName;
    private final String serviceVersion;
    private final String podUid;

    protected CustomJsonLayout(Charset charset, String serviceName, String serviceVersion, String podUid) {
        super(charset);
        this.serviceName = serviceName;
        this.serviceVersion = serviceVersion;
        this.podUid = podUid;
    }

    @PluginFactory
    public static CustomJsonLayout createLayout(
            @PluginAttribute(value = "charset", defaultString = "UTF-8") Charset charset,
            @PluginAttribute("serviceName") String serviceName,
            @PluginAttribute("serviceVersion") String serviceVersion,
            @PluginAttribute("podUid") String podUid) {
        return new CustomJsonLayout(charset, serviceName, serviceVersion, podUid);
    }

    @Override
    public String toSerializable(LogEvent event) {
        Map<String, Object> logEntry = new HashMap<>();

        logEntry.put("Timestamp", event.getTimeMillis());

        Map<String, Object> attributes = new HashMap<>(ThreadContext.getImmutableContext());
        attributes.put("http.url",MDC.get("http.url"));
        attributes.put("http.status_code",MDC.get("http.status_code"));
        attributes.put("my.custom.application.tag",MDC.get("my.custom.application.tag"));

        logEntry.put("TraceId", MDC.get("traceId"));
        logEntry.put("SpanId", MDC.get("spanId"));
        
        attributes.remove("traceId");
        attributes.remove("spanId");
        logEntry.put("Attributes", attributes);

        Map<String, Object> resource = new HashMap<>();
        resource.put("service.name", serviceName);
        resource.put("service.version", serviceVersion);
        resource.put("k8s.pod.uid", podUid);
        logEntry.put("Resource", resource);

        logEntry.put("SeverityText", event.getLevel().name());
        logEntry.put("SeverityNumber", mapLevelToNumber(event.getLevel()));

        logEntry.put("Body", event.getMessage().getFormattedMessage());

        try {
            return objectMapper.writeValueAsString(logEntry) + System.lineSeparator();
        } catch (JsonProcessingException e) {
            return "{\"error\": \"failed to serialize log event\"}" + System.lineSeparator();
        }
    }

    private int mapLevelToNumber(Level level) {
        switch (level.name()) {
            case "INFO":
                return 9;
            case "WARN":
                return 13;
            case "ERROR":
                return 17;
            default:
                return 10;
        }
    }
}