package com.nath.concorrencia.controller;

import com.nath.concorrencia.model.Client;
import com.nath.concorrencia.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

  @Autowired
  private ClientService clientService;

  @GetMapping
  public List<Client> findAll() {
    return clientService.getAll();
  }
}
