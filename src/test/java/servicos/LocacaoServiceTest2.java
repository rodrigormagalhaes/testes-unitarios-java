package servicos;

import br.ce.wcaquino.br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.servicos.LocacaoService;
import br.ce.wcaquino.utils.DataUtils;
import buildermaster.BuilderMaster;
import builders.FilmeBuilder;
import builders.UsuarioBuilder;
import matchers.DiaSemanaMatcher;
import matchers.MatchersProprios;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static builders.FilmeBuilder.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class LocacaoServiceTest2 {

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
    public void shouldRentFilm() throws Exception {

        Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());

        Locacao locacao;

        locacao = service.alugarFilme(usuario, filmes);

        error.checkThat(locacao.getValor(), is(equalTo(5.0)));
        error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));

        error.checkThat(locacao.getDataLocacao(), MatchersProprios.isToday());
        error.checkThat(locacao.getDataRetorno(), MatchersProprios.isTodayWithDifferenceOfDays(1));

        assertThat(locacao.getValor(), is(5.00));

    }

    @Test(expected = FilmeSemEstoqueException.class)
    public void shouldThrowExceptionWhenRentFilmWithoutStock() throws Exception {
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
       // List<Filme> filmes = Arrays.asList(umFilme().semEstoque().agora());
        List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora());

        service.alugarFilme(usuario, filmes);
    }

    @Test
    public void shouldNotRentFilmWithoutUser() throws FilmeSemEstoqueException {
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        try {
            service.alugarFilme(null, filmes);
            Assert.fail();
        } catch (LocadoraException e) {
            assertThat(e.getMessage(), is("Usuário vazio"));

        }
    }

    @Test
    public void shouldNotRentFilm() throws FilmeSemEstoqueException, LocadoraException {
        Usuario usuario = UsuarioBuilder.umUsuario().agora();

        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        service.alugarFilme(usuario, null);
    }

    @Test
    public void shouldPay75PctFilm3() throws FilmeSemEstoqueException, LocadoraException {
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(
                umFilme().agora(),
                umFilme().agora(),
                umFilme().agora())
                ;

        Locacao resultado = service.alugarFilme(usuario, filmes);

        assertThat(resultado.getValor(), is(11.0)) ;

    }

    @Test
    public void shouldPay50PctFilm4() throws FilmeSemEstoqueException, LocadoraException {
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        List<Filme> filmes = Arrays.asList(
                umFilme().agora(),
                umFilme().agora(),
                umFilme().agora(),
                umFilme().agora())
                ;

        Locacao resultado = service.alugarFilme(usuario, filmes);

        assertThat(resultado.getValor(), is(13.0)) ;

    }

    @Test
    public void shouldPay25PctFilm5() throws FilmeSemEstoqueException, LocadoraException {
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        List<Filme> filmes = Arrays.asList(
                umFilme().agora(),
                umFilme().agora(),
                umFilme().agora(),
                umFilme().agora(),
                umFilme().agora())
                ;

        Locacao resultado = service.alugarFilme(usuario, filmes);

        assertThat(resultado.getValor(), is(14.0)) ;

    }

    @Test
    public void shouldPay0Film6() throws FilmeSemEstoqueException, LocadoraException {
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        List<Filme> filmes = Arrays.asList(
                umFilme().agora(),
                umFilme().agora(),
                umFilme().agora(),
                umFilme().agora(),
                umFilme().agora(),
                umFilme().agora())
                ;

        Locacao resultado = service.alugarFilme(usuario, filmes);

        assertThat(resultado.getValor(), is(14.0)) ;

    }

    @Test
    public void shouldGiveBackOnMondayWhenRentOnSaturday() throws FilmeSemEstoqueException, LocadoraException {
        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        Locacao retorno = service.alugarFilme(usuario, filmes);

//        boolean IsMonday = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
//
//        Assert.assertTrue(IsMonday);

        assertThat(retorno.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));

        assertThat(retorno.getDataRetorno(), MatchersProprios.caiEm(Calendar.MONDAY));
        assertThat(retorno.getDataRetorno(), MatchersProprios.caiNumaSegunda());

    }

    public static void main(String[] args) {
        new BuilderMaster().gerarCodigoClasse(Locacao.class);
    }
}
