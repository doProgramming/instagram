package com.example.demo.Login;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface LoginSessionService {

    boolean login(String username, String password) throws IOException, ClassNotFoundException;
}
