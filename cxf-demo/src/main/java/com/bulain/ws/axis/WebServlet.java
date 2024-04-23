package com.bulain.ws.axis;

import org.apache.axis.transport.http.AxisServlet;

@javax.servlet.annotation.WebServlet(
        urlPatterns =  {"/axis/", "/axis/*"},
        loadOnStartup = 1,
        name = "AxisServlet"
)
public class WebServlet extends AxisServlet {

}
