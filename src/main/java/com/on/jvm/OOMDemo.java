package com.on.jvm;

import java.util.ArrayList;

/**
 * -Xms600m -Xmx600m
 *
 * @author Nordlicht
 */
public class OOMDemo {
    public static void main(String[] args) {
        ArrayList arrayList = new ArrayList<>();
        while (true) {
            arrayList.add(new Byte[1024 * 1024 * 1024]);
        }
    }
}

