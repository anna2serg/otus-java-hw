package ru.otus.messagesystem.message;

public enum MessageType {
    USER_DATA("UserData"),
    CLIENT_LIST("ClientList"),
    CLIENT_DATA("ClientData"),
    ADD_CLINT("AddClient");

    private final String name;

    MessageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
