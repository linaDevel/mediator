package ru.linachan.mediator.common;

public class Id {

    public final Integer id;
    public final String ref;

    public Id(String id) {
        String[] idParts = id.split("/");

        this.id = Integer.parseInt(idParts[0]);
        this.ref = idParts[1];
    }
}
