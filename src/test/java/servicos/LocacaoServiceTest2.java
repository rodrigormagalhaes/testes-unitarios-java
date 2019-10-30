package servicos;

import br.ce.wcaquino.br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.daos.LocacaoDAOFake;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.servicos.EmailService;
import br.ce.wcaquino.servicos.LocacaoService;
import br.ce.wcaquino.servicos.SPCService;
import br.ce.wcaquino.utils.DataUtils;
import buildermaster.BuilderMaster;
import builders.LocacaoBuilder;
import builders.UsuarioBuilder;
import matchers.DiaSemanaMatcher;
import matchers.MatchersProprios;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static builders.FilmeBuilder.umFilme;
import static builders.FilmeBuilder.umFilmeSemEstoque;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class LocacaoServiceTest2 {

    private LocacaoService service;
    private SPCService spcService;
    private LocacaoDAO dao;
    private EmailService email;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        service = new LocacaoService();

        //LocacaoDAO dao = new LocacaoDAOFake();
        dao = Mockito.mock(LocacaoDAO.class);
        service.setLocacaoDAO(dao);

        spcService = Mockito.mock(SPCService.class);
        service.setSpcService(spcService);

        email = Mockito.mock(EmailService.class);
        service.setEmailService(email);
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

    @Test
    public void shouldNotRentFilmUserInSPC() throws FilmeSemEstoqueException {
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        //Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("Usuário 2").agora();

        List<Filme> filmes = Arrays.asList(umFilme().agora());

        Mockito.when(spcService.possuiNegativacao(usuario)).thenReturn(true);

        try {
            service.alugarFilme(usuario, filmes);
            Assert.fail();
        } catch (LocadoraException e) {
            Assert.assertThat(e.getMessage(), is("Usuário negativado"));
        }

        Mockito.verify(spcService).possuiNegativacao(usuario);
    }

    @Test
    public void shouldSendEmailDelayedLocations() {
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        //Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("Usuário 2").agora();

        List<Locacao> locacoes =
                Arrays.asList(LocacaoBuilder.umLocacao()
                        .comUsuario(usuario)
                        .comDataRetorno(DataUtils.obterDataComDiferencaDias(-2))
                        .agora());

        Mockito.when(dao.getPendentLocations()).thenReturn(locacoes);

        service.notifyDelays();

        Mockito.verify(email).notifyDelay(usuario);
    }
}
