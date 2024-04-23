package com.bulain.ws.cxf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.jws.WebService;

@Slf4j
@Service
@WebService(name = "CxfWebService",
        targetNamespace = "http://ws.bulain.com",
        endpointInterface = "com.bulain.ws.cxf.CxfWebService"
)
public class CxfWebServiceImpl implements CxfWebService {

    private static final String RESPONSE = "<Response><Header><SourceSystem>%s</SourceSystem><MessageID>%s</MessageID></Header><Body><ResultCode>%s</ResultCode><ResultContent>%s</ResultContent></Body></Response>";

    @Override
    public String emrService(String data) {
        log.info("接收参数 => [ {} ]", data);
        if (data.isEmpty()) {
            return "传入的参数为空";
        }

        return String.format(RESPONSE, "01", "", "0", "成功");
    }

}
