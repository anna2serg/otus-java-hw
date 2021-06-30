package ru.otus.controllers;

import org.springframework.web.bind.annotation.*;
import ru.otus.db.model.ClientDataSet;
import ru.otus.db.service.ClientService;

@RestController
public class ClientRestController {

    private final ClientService clientService;

    public ClientRestController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/api/client/{id}")
    public ClientDataSet getClientById(@PathVariable(name = "id") long id) {
        return clientService.get(id).orElse(null);
    }

    @PostMapping("/api/client")
    public ClientDataSet saveClient(@RequestBody ClientDataSet client) {
        return clientService.save(client);
    }

}
