package com.clay.juc01;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by CLAY on 2017/3/21.
 */
public class TestCopyOnWriteArrayList {
    public static void main(String[] args) {
        Hellothread ht = new Hellothread();

        for(int i = 0; i < 10; i++){
            new Thread(ht).start();
        }
    }
}

class Hellothread implements Runnable {

    //通过Collections.synchronizedList获得同步容器
    private static List<String> list = Collections.synchronizedList(new ArrayList<String>());

    //并发容器：写入并复制
    // 每次修改会复制，所以添加操作不适合，适合迭代操作
//    private static CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<String>();

    static{
        list.add("AA");
        list.add("BB");
        list.add("CC");
    }

    @Override
    public void run() {
        Iterator<String> it = list.iterator();

        while (it.hasNext()) {
            System.out.println(it.next());

            list.add("AA");
        }
    }
}
