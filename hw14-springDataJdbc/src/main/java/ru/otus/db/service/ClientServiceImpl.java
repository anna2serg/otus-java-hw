package ru.otus.db.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.db.model.ClientDataSet;
import ru.otus.db.repository.ClientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {
    private static final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional
    public ClientDataSet save(ClientDataSet clientDataSet) {
        var savedClient = clientRepository.save(clientDataSet);
        log.info("saved client: {}", savedClient);
        return savedClient;
    }

    @Override
    public Optional<ClientDataSet> get(long id) {
        var clientOptional = clientRepository.findById(id);
        log.info("client: {}", clientOptional);
        return clientOptional;
    }

    @Override
    public List<ClientDataSet> findAll() {
        var clientList = new ArrayList<ClientDataSet>();
        clientRepository.findAll().forEach(clientList::add);
        log.info("clientList:{}", clientList);
        return clientList;
    }
}
