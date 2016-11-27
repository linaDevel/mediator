package ru.linachan.mediator;

import com.sun.net.httpserver.HttpServer;

import ru.linachan.mediator.collector.dp.DPSearch;
import ru.linachan.mediator.collector.zz.ZZSearch;
import ru.linachan.mediator.handler.dp.DPDataHandler;
import ru.linachan.mediator.handler.dp.DPSearchHandler;
import ru.linachan.mediator.handler.zz.ZZDataHandler;
import ru.linachan.mediator.handler.zz.ZZSearchHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MediatorCore {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", 8080), 1);

        ZZSearch zzSearch = new ZZSearch();
        server.createContext("/zz/search", new ZZSearchHandler(zzSearch));
        server.createContext("/zz/data", new ZZDataHandler(zzSearch));

        DPSearch dpSearch = new DPSearch();
        server.createContext("/dp/search", new DPSearchHandler(dpSearch));
        server.createContext("/dp/data", new DPDataHandler(dpSearch));

        System.out.println("Starting Mediator...");
        server.start();
    }
}
