package ru.otus.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ru.otus.dto.ClientDto;
import ru.otus.frontend.FrontendService;
import ru.otus.messagesystem.message.SerializerError;

@Controller
public class ClientWebsocketController {

    private final FrontendService frontendService;
    private final SimpMessagingTemplate template;

    public ClientWebsocketController(FrontendService frontendService, SimpMessagingTemplate template) {
        this.frontendService = frontendService;
        this.template = template;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping({"/clients"})
    public String clientsView(Model model) {
        return "clients";
    }

    @GetMapping("/client/add")
    public String addClientView() {
        return "addClient";
    }

    @MessageMapping("/clientList")
    public void getClients() {
        frontendService.getClients(clients -> template.convertAndSend("/topic/clients", clients));
    }

    @MessageMapping("/client/{id}")
    public void getClientById(@DestinationVariable Long id) {
        frontendService.getClient(id, client -> template.convertAndSend("/topic/clientById", client));
    }

    @MessageMapping("/client/add")
    public void addClient(String clientJson) {
        var objectMapper = new ObjectMapper();
        ClientDto client;
        try {
            client = objectMapper.readValue(clientJson, ClientDto.class);
            frontendService.addClient(client, addedClient -> template.convertAndSend("/topic/client", addedClient));
        } catch (JsonProcessingException e) {
            throw new SerializerError("Serialization error", e);
        }
    }

}
