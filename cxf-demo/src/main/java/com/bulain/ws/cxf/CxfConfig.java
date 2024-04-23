package com.bulain.ws.cxf;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;


@Configuration
public class CxfConfig {

    private Bus bus;
    private CxfWebService cxfWebService;

    @Autowired
    public CxfConfig(Bus bus, CxfWebService cxfWebService) {
        this.bus = bus;
        this.cxfWebService = cxfWebService;
    }

    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, cxfWebService);
        endpoint.publish("/CxfWebService");
        return endpoint;
    }

    @Bean
    public ServletRegistrationBean<CXFServlet> cxfServlet() {
        return new ServletRegistrationBean<>(new CXFServlet(), "/cxf/*");
    }

}