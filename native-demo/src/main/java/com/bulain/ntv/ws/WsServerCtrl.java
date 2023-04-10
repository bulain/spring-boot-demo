package com.bulain.ntv.ws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class WsServerCtrl {

    @Autowired
    private WsService wsService;

    @GetMapping("echo")
    public void echo() {
        List<String> ids = wsService.ids();
        for (String id : ids) {
            wsService.send(id, "test-" + id);
        }
    }

    @GetMapping("services")
    public List<String> services() {
        List<String> list = new ArrayList<>();
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService ps : printServices) {
            list.add(ps.getName());
        }
        return list;
    }

    @GetMapping("print/{name}")
    public void print(@PathVariable("name") String name) {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        PrintService printService = null;
        for (PrintService ps : printServices) {
            if (ps.getName().equals(name)) {
                printService = ps;
                break;
            }
        }
        if (printService != null) {
            log.info("print - {}", printService.getName());
        }
    }

}

