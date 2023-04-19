package com.rubenialima.minhasfinancas.model.repositoy;

import com.rubenialima.minhasfinancas.model.entity.Lancamento;
import com.rubenialima.minhasfinancas.model.enums.StatusLancamento;
import com.rubenialima.minhasfinancas.model.enums.TipoLancamento;
import com.rubenialima.minhasfinancas.model.repository.LancamentoRepository;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class LancamentoRepositoryTest {

    @Autowired
    LancamentoRepository repository;
    @Autowired
    TestEntityManager entityManager;

    @Test
    public void deveSalvarUmLancamento() {

        Lancamento lancamento = criarLancamento();
        lancamento = repository.save(lancamento);
        assertThat(lancamento.getId()).isNotNull();
    }

    @Test
    public void deveDeletarUmLancamento() {
        Lancamento lancamento = criarEPersistirUmLancamento();

        lancamento = entityManager.find(Lancamento.class, lancamento.getId());
        repository.delete(lancamento);
        Lancamento lancamentoInexistente = entityManager.find(Lancamento.class, lancamento.getId());

        assertThat(lancamentoInexistente).isNull();

    }

    @Test
    public void deveAtualizarUmLancamento() {
        Lancamento lancamento = criarEPersistirUmLancamento();

        lancamento.setAno(2021);
        lancamento.setDescricao("Aplicando teste atualizar");
        lancamento.setStatus(StatusLancamento.CANCELADO);

        repository.save(lancamento);

        Lancamento lancamentoAtualizado = entityManager.find(Lancamento.class, lancamento.getId());

        assertThat(lancamentoAtualizado.getAno()).isEqualTo(2021);
        assertThat(lancamentoAtualizado.getDescricao()).isEqualTo("Aplicando teste atualizar");
        assertThat(lancamentoAtualizado.getStatus()).isEqualTo(StatusLancamento.CANCELADO);

    }

    @Test
    public void deveBuscarUmLancamentoPorId() {
        Lancamento lancamento = criarEPersistirUmLancamento();
        Optional<Lancamento> lancamentoEncontrado = repository.findById(lancamento.getId());
        assertThat(lancamentoEncontrado.isPresent()).isTrue();

    }

    private Lancamento criarEPersistirUmLancamento() {
        Lancamento lancamento = criarLancamento();
        entityManager.persist(lancamento);
        return lancamento;
    }

    public static Lancamento criarLancamento() {
        return Lancamento.builder()
                .ano(2023)
                .mes(04)
                .descricao("Lançamento Novo")
                .valor(BigDecimal.valueOf(26))
                .tipo(TipoLancamento.RECEITA)
                .status(StatusLancamento.PENDENTE)
                .dataCadastro(LocalDate.now()).build();
    }
}