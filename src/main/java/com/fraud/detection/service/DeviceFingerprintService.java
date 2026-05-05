package com.fraud.detection.service;

import org.springframework.stereotype.Service;
import ua_parser.Client;
import ua_parser.Parser;

@Service
public class DeviceFingerprintService {

    private final Parser uaParser;

    public DeviceFingerprintService() {
        this.uaParser = new Parser();
    }

    public Client parseUserAgent(String userAgentString) {
        if (userAgentString == null || userAgentString.isEmpty()) {
            return null;
        }
        return uaParser.parse(userAgentString);
    }

    public boolean isSuspiciousDevice(Client client) {
        if (client == null) return true;

        String os = client.os.family;
        String browser = client.userAgent.family;

        // Example logic: Flag curl, postman, or uncommon OS
        if (browser.toLowerCase().contains("curl") || browser.toLowerCase().contains("postman")) {
            return true;
        }

        return false;
    }
}
