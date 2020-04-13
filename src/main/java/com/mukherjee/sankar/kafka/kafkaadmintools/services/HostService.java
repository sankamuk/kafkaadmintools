package com.mukherjee.sankar.kafka.kafkaadmintools.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

@Service
public class HostService {
    private static Logger LOG = LoggerFactory.getLogger(HostService.class);


    public String getServiceStatus(String serviceName) throws InterruptedException, IOException  {
        Process p = Runtime.getRuntime().exec("systemctl status "+serviceName);
        p.wait();
        if ( p.exitValue() == 0 ) {
            return "running";
        } else {
            return "not running";
        }

    }

    public int setServiceStatus(String serviceName, String serviceStatus)
            throws InterruptedException, IOException{
        Process p = Runtime.getRuntime().exec("systemctl "+serviceStatus+ " " +serviceName);
        p.wait();
        return p.exitValue();
    }

    public Collection<String> getAllService() throws InterruptedException, IOException {
        String line = null;
        Process p = Runtime.getRuntime().exec("systemctl -a");
        p.wait();
        Collection<String> result = new ArrayList<String>();
        BufferedReader rd = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while ((line = rd.readLine()) != null){
            result.add(line
                    .replaceAll("\\s+"," ")
                    .split(" ")[0]);
        }
        return result;
    }
}
