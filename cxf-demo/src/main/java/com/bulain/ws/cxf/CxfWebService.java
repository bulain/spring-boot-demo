package com.bulain.ws.cxf;

import org.springframework.stereotype.Service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@Service
@SOAPBinding(style = SOAPBinding.Style.RPC)
@WebService(name = "CxfWebService", targetNamespace = "http://ws.bulain.com"
)
public interface CxfWebService {

    @WebMethod
    String emrService(@WebParam String data);

}
