package com.sptech.school.controller;

import com.sptech.school.service.RelatorioService;
import com.sptech.school.dto.RelatorioDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;




@RestController
@CrossOrigin(origins = "*")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/relatorio")
    public ResponseEntity<?> gerarRelatorio(@RequestBody RelatorioDTO dto) {
        try {
            String url = relatorioService.BotaoRelatorio(
                    dto.getUsuario(),
                    dto.getEmail(),
                    dto.getServidor(),
                    dto.getMac_address(),
                    dto.getId_empresa(),
                    dto.getTipoComponente()
            );
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}