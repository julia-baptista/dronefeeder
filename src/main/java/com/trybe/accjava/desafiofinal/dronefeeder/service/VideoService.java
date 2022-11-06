package com.trybe.accjava.desafiofinal.dronefeeder.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.trybe.accjava.desafiofinal.dronefeeder.dtos.VideoResponseDto;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.ErroInesperadoException;
import com.trybe.accjava.desafiofinal.dronefeeder.exception.PedidoNaoEncontradoException;
import com.trybe.accjava.desafiofinal.dronefeeder.model.Pedido;
import com.trybe.accjava.desafiofinal.dronefeeder.model.Video;
import com.trybe.accjava.desafiofinal.dronefeeder.repository.PedidoRepository;
import com.trybe.accjava.desafiofinal.dronefeeder.repository.VideoRepository;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class VideoService {

  final VideoRepository videoRepository;
  final PedidoRepository pedidoRepository;

  public VideoService(VideoRepository videoRepository, PedidoRepository pedidoRepository) {
    super();
    this.videoRepository = videoRepository;
    this.pedidoRepository = pedidoRepository;
  }

  /**
   * Cadastrar.
   */
  @Transactional
  public void salvar(Long idPedido, String nomeDoArquivo) {
    try {
      Optional<Pedido> pedido = pedidoRepository.findById(idPedido);
      if (!pedido.isPresent()) {
        throw new PedidoNaoEncontradoException();
      }

      Video video = new Video();
      video.setNomeArquivo(nomeDoArquivo);
      video.setPedido(pedido.get());
      Video novoVideo = this.videoRepository.save(video);
      pedido.get().setVideo(novoVideo);
    } catch (Exception e) {
      log.error("Error ao salvar o video da entrega", e);
      if (e instanceof PedidoNaoEncontradoException) {
        throw (PedidoNaoEncontradoException) e;
      }
      throw new ErroInesperadoException();
    }
  }

  /**
   * Listar.
   */
  public List<VideoResponseDto> listar() {
    try {
      List<VideoResponseDto> videosDto = new ArrayList<VideoResponseDto>();
      List<Video> videos = videoRepository.findAll();
      videos.stream().forEach(video -> {
        videosDto.add(converterVideoParavideoDto(video));
      });
      return videosDto;
    } catch (Exception e) {
      log.error("Erro ao recuperar os videos", e);
      throw new ErroInesperadoException();
    }
  }

  private VideoResponseDto converterVideoParavideoDto(Video video) {
    return VideoResponseDto.builder().id(video.getId()).nomeArquivo(video.getNomeArquivo())
        .pedidoId(Objects.isNull(video.getPedido()) ? null : video.getPedido().getId()).build();
  }

}


