package com.sptech.school.controller;

import com.sptech.school.service.RelatorioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RelatorioController {

    @GetMapping("/relatorio")
    public ResponseEntity<String> gerarRelatorio(
            @RequestParam String usuario,
            @RequestParam String email,
            @RequestParam String mac_address,
            @RequestParam String servidor,
            @RequestParam int id_empresa,
            @RequestParam String tipoComponente
    ) {
        try {
            RelatorioService service = new RelatorioService();
            String url = service.BotaoRelatorio(usuario, email, servidor, mac_address, id_empresa, tipoComponente);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}