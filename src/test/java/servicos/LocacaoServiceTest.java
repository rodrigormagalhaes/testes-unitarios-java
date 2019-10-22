package servicos;

import br.ce.wcaquino.br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.servicos.LocacaoService;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Date;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LocacaoServiceTest {

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testeLocacao() {

        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Usuario 1");
        Filme filme = new Filme("Filme 1", 2, 5.0);

        Locacao locacao;
        try {
            locacao = service.alugarFilme(usuario, filme);

            error.checkThat(locacao.getValor(), is(equalTo(5.0)));
            error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
            error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));

            assertThat(locacao.getValor(), is(5.00));
        } catch (FilmeSemEstoqueException e) {
            e.printStackTrace();
        } catch (LocadoraException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testeLocacao_filmeSemEstoque() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Usuario 1");
        Filme filme = new Filme("Filme 1", 0, 5.0);

        service.alugarFilme(usuario, filme);

    }

    @Test
    public void testeLocacao_usuarioVazio() throws FilmeSemEstoqueException {
        LocacaoService service = new LocacaoService();
        Filme filme = new Filme("Filme 1", 2, 5.0);

        try {
            service.alugarFilme(null, filme);
            Assert.fail();
        } catch (LocadoraException e) {
            e.printStackTrace();
            assertThat(e.getMessage(), is("Usuário vazio"));

        }
    }

    @Test
    public void testelocacao_filmeVazio() throws FilmeSemEstoqueException, LocadoraException {
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Usuario 1");

        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme sem estoque");

        service.alugarFilme(usuario, null);

    }
}
