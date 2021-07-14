package ru.otus.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import ru.otus.db.model.ClientDataSet;
import ru.otus.db.service.ClientService;
import ru.otus.dto.ClientDto;
import ru.otus.messagesystem.message.SerializerError;

@Controller
public class ClientWebsocketController {

    private final ClientService clientService;

    public ClientWebsocketController(ClientService clientService) {
        this.clientService = clientService;
    }

    @MessageMapping("/client/{id}")
    @SendTo("/topic/clientById")
    public ClientDataSet getClientById(@DestinationVariable Long id) {
        return clientService.get(id).orElse(null);
    }

    @MessageMapping("/clients")
    @SendTo("/topic/clients")
    public /*List<ClientDataSet>*/ String getClients(Model model) {
        List<ClientDataSet> clients = clientService.findAll();
        model.addAttribute("clients", clients);
        return "clients";
        //return clientService.findAll();
    }

    @MessageMapping("/client/add")
    @SendTo("/topic/client")
    public ClientDataSet addClientFromJson(String json) {
        var objectMapper = new ObjectMapper();
        ClientDto client;
        try {
            client = objectMapper.readValue(json, ClientDto.class);
        } catch (JsonProcessingException e) {
            throw new SerializerError("Serialization error", e);
        }
        return clientService.save(client.toModel());
    }

}
