package com.kf.stests;

import org.testng.TestNG;

import java.io.File;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        TestNG testng = new TestNG();
        List<String> suites = List.of(new File(System.getProperty("user.dir")) + "/testng.xml");
        testng.setTestSuites(suites);
        testng.run();
    }
}
