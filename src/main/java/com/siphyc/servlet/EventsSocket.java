package com.siphyc.servlet;

/*
 * The MIT License
 *
 * Copyright 2016 korena.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
/**
 *
 * @author korena
 */
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.slf4j.LoggerFactory;

@ServerEndpoint("/stream")
public class EventsSocket {

    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(EventsSocket.class);
    private Session session;
    private RemoteEndpoint.Async remote;
    
    @OnClose
    public void onWebSocketClose(CloseReason close) {
        this.session = null;
        this.remote = null;
        logger.info("WebSocket Close: {} - {}", close.getCloseCode(), close.getReasonPhrase());
    }

    @OnOpen
    public void onWebSocketOpen(Session session) {
        this.session = session;
        this.remote = this.session.getAsyncRemote();
        logger.info("WebSocket Connect: {}", session);
        this.remote.sendText("Welcome!");
    }

    @OnError
    public void onWebSocketError(Throwable cause) {
        logger.warn("WebSocket Error", cause);
    }

    @OnMessage
    public String onWebSocketText(String message) {
        logger.info("Recieved message [{}]", message);
        // echo
        return message;
    }
}
