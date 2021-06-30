package ru.otus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.db.model.ClientDataSet;
import ru.otus.db.service.ClientService;
import ru.otus.dto.ClientDto;
import ru.otus.dto.PhoneDto;

import java.util.List;

@Controller
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping({"/client/list"})
    public String clientsListView(Model model) {
        List<ClientDataSet> clients = clientService.findAll();
        model.addAttribute("clients", clients);
        return "clients";
    }

    @GetMapping("/client/add")
    public String clientCreateView(Model model) {
        ClientDto client = new ClientDto();
        client.setPhones(List.of(new PhoneDto()));
        model.addAttribute("client", client);
        return "addClient";
    }

    @PostMapping("/client/save")
    public RedirectView clientSave(@ModelAttribute ClientDto clientDto) {
        ClientDataSet clientDataSet = clientDto.toModel();
        clientService.save(clientDataSet);
        return new RedirectView("/client/list", true);
    }

    @PostMapping(value = "/client/save", params={"addPhone"})
    public String clientAddPhone(Model model, @ModelAttribute ClientDto client) {
        client.getPhones().add(new PhoneDto());
        model.addAttribute("client", client);
        return "addClient";
    }
}
