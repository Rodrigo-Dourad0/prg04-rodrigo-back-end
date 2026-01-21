package br.com.ifba.prg04_rodrigo_back_end.usuario.mapper;

import br.com.ifba.prg04_rodrigo_back_end.infraestructure.entity.Endereco;
import br.com.ifba.prg04_rodrigo_back_end.usuario.dto.UsuarioResponse;
import br.com.ifba.prg04_rodrigo_back_end.usuario.entity.Usuario;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    private final ModelMapper modelMapper;

    public UsuarioMapper() {
        this.modelMapper = new ModelMapper();
      
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);


        this.modelMapper.typeMap(Usuario.class, UsuarioResponse.class)
                .addMappings(mapper -> mapper.skip(UsuarioResponse::setOrganizadorAtivo));
    }

    public UsuarioResponse toResponse(Usuario usuario) {

        UsuarioResponse dto = modelMapper.map(usuario, UsuarioResponse.class);


        dto.setOrganizadorAtivo(usuario.isOrganizador());


        if (usuario.getPessoa() != null) {
            dto.setNome(usuario.getPessoa().getNome());

            dto.setEnderecoCompleto(formatarEndereco(usuario.getPessoa().getEndereco()));
        }

        return dto;
    }

    private String formatarEndereco(Endereco end) {
        if (end == null) return null;

        String enderecoFormatado = String.format("%s, nº %s - %s. %s-%s. CEP: %s",
                end.getRua() != null ? end.getRua() : "Rua não informada",
                end.getNumero() != null ? end.getNumero() : "S/N",
                end.getBairro() != null ? end.getBairro() : "",
                end.getCidade() != null ? end.getCidade() : "",
                end.getEstado() != null ? end.getEstado() : "",
                end.getCep() != null ? end.getCep() : ""
        );

        return enderecoFormatado.replace(" . - .", "").trim();
    }
}