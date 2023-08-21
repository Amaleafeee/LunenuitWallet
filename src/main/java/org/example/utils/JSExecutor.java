package org.example.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class JSExecutor {
    public static String runScriptAndReturnOutput(String path) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("node", path);
        Process scriptExecution = processBuilder.start();

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(scriptExecution.getInputStream()));

        String line;
        while ((line = bufferedReader.readLine()) != null){
            stringBuilder.append(line).append("\n");
        }

        return stringBuilder.toString();
    }

    public static String runScriptAndReturnOutput(String path, String parameter) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("node", path, parameter);
        Process scriptExecution = processBuilder.start();

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(scriptExecution.getInputStream()));

        String line;
        while ((line = bufferedReader.readLine()) != null){
            stringBuilder.append(line).append("\n");
        }

        return stringBuilder.toString();
    }
}
