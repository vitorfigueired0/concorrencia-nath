package com.nath.concorrencia.service;

import com.nath.concorrencia.model.Client;
import com.nath.concorrencia.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class ClientService {

  @Autowired
  ClientRepository clientRepository;

  public Client getById(Long id) {
    return clientRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatusCode.valueOf(404), "Client not found"));
  }


}
