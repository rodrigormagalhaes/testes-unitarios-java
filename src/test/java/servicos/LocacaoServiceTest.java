package servicos;

import br.ce.wcaquino.br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.servicos.LocacaoService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LocacaoServiceTest {

    private LocacaoService service;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();


    @Before
    public void setUp() {
        service = new LocacaoService();
    }

    @Test
    public void testeLocacao() throws Exception {

        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));

        Locacao locacao;

        locacao = service.alugarFilme(usuario, filmes);

        error.checkThat(locacao.getValor(), is(equalTo(5.0)));
        error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));

        assertThat(locacao.getValor(), is(5.00));

    }

    @Test(expected = FilmeSemEstoqueException.class)
    public void testeLocacao_filmeSemEstoque() throws Exception {
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 5.0));

        service.alugarFilme(usuario, filmes);
    }

    @Test
    public void testeLocacao_usuarioVazio() throws FilmeSemEstoqueException {
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));

        try {
            service.alugarFilme(null, filmes);
            Assert.fail();
        } catch (LocadoraException e) {
            assertThat(e.getMessage(), is("Usuário vazio"));

        }
    }

    @Test
    public void testelocacao_filmeVazio() throws FilmeSemEstoqueException, LocadoraException {
        Usuario usuario = new Usuario("Usuario 1");

        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        service.alugarFilme(usuario, null);

    }
}
