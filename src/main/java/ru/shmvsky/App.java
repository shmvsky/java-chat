package ru.shmvsky;

import java.io.IOException;

import ru.shmvsky.server.Server;

public class App 
{
    public static void main( String[] args )
    {
        try {
            new Server().start();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
