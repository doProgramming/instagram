package com.example.demo.Login;

import org.brunocvcunha.instagram4j.Instagram4j;
import java.io.IOException;

public interface LoginSessionService {

    Instagram4j login(String username, String password) throws IOException, ClassNotFoundException;
}
