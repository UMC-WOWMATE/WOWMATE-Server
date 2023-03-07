package com.wowmate.server.user.service;

public interface EmailService {
    String sendSimpleMessage(String to)throws Exception;
    void sendAccusationMessage(String accuser, String accused, String category, String ID, String content) throws Exception;
}
