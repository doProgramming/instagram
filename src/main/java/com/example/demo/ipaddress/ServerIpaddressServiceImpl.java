package com.example.demo.ipaddress;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;

@Service
public class ServerIpaddressServiceImpl implements ServerIpaddressService {
    // Sends ping request to a provided IP address
//    @Scheduled(fixedRate = 10000)
    public Boolean isIpaddressAlive() throws IOException {
        String ipAddress = "85.222.169.175";
        InetAddress geek = InetAddress.getByName(ipAddress);
        System.out.println("Sending Ping Request to " + ipAddress);
        if (geek.isReachable(5000)) {
            System.out.println("Host is reachable");
            return false;
        }
        System.out.println("Sorry !This host has restarted or doesn't have internet connection");
        return true;
    }

}
