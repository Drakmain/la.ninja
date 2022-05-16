package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TestCLI {

    public static void main(String[] args) throws IOException, InterruptedException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        Boolean ex = true;

        String pre = "Oue [";
        StringBuilder progres = new StringBuilder("          ");
        String post = "] 0% (0/10)";

        System.out.print(pre + progres + post + "\r");

        Thread.sleep(4000);

        for (int i = 0; i < 10; i++) {
            progres.setCharAt(i, '|');
            int pource = (int) ((i+1)/10.0 * 100);
            post = "] " + pource + "% (" + (i+1) + "/10)";
            System.out.print(pre + progres + post + "\r");
            Thread.sleep(1000);
        }
    }
}
