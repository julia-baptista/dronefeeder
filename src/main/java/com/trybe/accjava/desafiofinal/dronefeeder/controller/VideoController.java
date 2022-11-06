package com.trybe.accjava.desafiofinal.dronefeeder.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import com.trybe.accjava.desafiofinal.dronefeeder.dtos.AtualizaCoordenadaPedidoDto;
import com.trybe.accjava.desafiofinal.dronefeeder.dtos.VideoResponseDto;
import com.trybe.accjava.desafiofinal.dronefeeder.enums.StatusPedidoEnum;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.CarregarVideoEntregaException;
import com.trybe.accjava.desafiofinal.dronefeeder.service.PedidoService;
import com.trybe.accjava.desafiofinal.dronefeeder.service.VideoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "v1/video")
public class VideoController {

  private final PedidoService pedidoService;
  private final VideoService videoService;
  private final String pathArquivos;

  public VideoController(PedidoService pedidoService, VideoService videoService,
      @Value("${app.path.arquivos}") String pathArquivos) {
    this.pedidoService = pedidoService;
    this.videoService = videoService;
    this.pathArquivos = pathArquivos;
  }

  // Upload de arquivos:
  // https://github.com/feltex/upload-arquivos-api
  // https://www.youtube.com/watch?v=3ZUaE6Xh3qk
  @ApiOperation(value = "Operação responsável por cadastrar o video enviado pelo drone",
      notes = "Cadastrar o envio do video")
  @ApiResponses(value = {@ApiResponse(code = 202, message = "Video cadastrado com sucesso"),
      @ApiResponse(code = 401, message = "Não autorizado"),
      @ApiResponse(code = 404, message = "Pedido não encontrado"),
      @ApiResponse(code = 409, message = "Pedido já entregue"),
      @ApiResponse(code = 500, message = "Erro inesperado e/ou Erro ao carregar o video")})
  @PostMapping("/upload")
  public ResponseEntity<Void> salvarArquivo(@RequestParam("file") MultipartFile file,
      @RequestParam("pedidoId") Long pedidoId, @RequestParam("latitude") Integer latitude,
      @RequestParam("longitude") Integer longitude) {
    log.info("Recebendo o arquivo: ", file.getOriginalFilename());
    // var path = "/home/samsung/Documentos/pedidos/upload/";
    AtualizaCoordenadaPedidoDto dto = AtualizaCoordenadaPedidoDto.builder().pedidoId(pedidoId)
        .latitude(latitude).longitude(longitude).build();
    this.pedidoService.atualizarCoordenadas(dto);
    this.pedidoService.alterarStatus(dto.getPedidoId(), StatusPedidoEnum.EN);
    String nomeArquivo = "";
    try {
      nomeArquivo = UUID.randomUUID() + "." + extrairExtensao(file.getOriginalFilename());
      String caminhoCompleto = pathArquivos.concat(nomeArquivo);
      log.info("Novo nome do arquivo: " + caminhoCompleto);
      Files.copy(file.getInputStream(), Path.of(caminhoCompleto),
          StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      log.error("Erro ao processar arquivo", e);
      throw new CarregarVideoEntregaException();
    }
    this.videoService.salvar(dto.getPedidoId(), nomeArquivo);
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
  }

  @ApiOperation(value = "Operação responsável por listar os videos enviados pelos drones",
      notes = "Listar Videos")
  @ApiResponses(value = {
      @ApiResponse(code = 200,
          message = "Lista de videos enviados pelos drones recuperada com sucesso"),
      @ApiResponse(code = 401, message = "Não autorizado"),
      @ApiResponse(code = 500, message = "Erro inesperado")})
  @GetMapping(produces = {"application/json"})
  public ResponseEntity<List<VideoResponseDto>> listarVideos() {
    List<VideoResponseDto> lista = this.videoService.listar();
    return ResponseEntity.ok(lista);
  }

  private String extrairExtensao(String nomeArquivo) {
    int i = nomeArquivo.lastIndexOf(".");
    return nomeArquivo.substring(i + 1);
  }

}
