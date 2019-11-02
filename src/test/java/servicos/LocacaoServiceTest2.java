package servicos;

import br.ce.wcaquino.br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.servicos.EmailService;
import br.ce.wcaquino.servicos.LocacaoService;
import br.ce.wcaquino.servicos.SPCService;
import matchers.DiaSemanaMatcher;
import matchers.MatchersProprios;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.*;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.utils.DataUtils.*;
import static builders.FilmeBuilder.umFilme;
import static builders.FilmeBuilder.umFilmeSemEstoque;
import static builders.LocacaoBuilder.umLocacao;
import static builders.UsuarioBuilder.umUsuario;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;


public class LocacaoServiceTest2 {

    @InjectMocks
    private LocacaoService service;

    @Mock
    private SPCService spcService;

    @Mock
    private LocacaoDAO dao;

    @Mock
    private EmailService email;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
//
//        service = new LocacaoService();
//
//        //LocacaoDAO dao = new LocacaoDAOFake();
//        dao = mock(LocacaoDAO.class);
//        service.setLocacaoDAO(dao);
//
//        spcService = mock(SPCService.class);
//        service.setSpcService(spcService);
//
//        email = mock(EmailService.class);
//        service.setEmailService(email);
    }

    @Test
    public void shouldRentFilm() throws Exception {

        Assume.assumeFalse(verificarDiaSemana(new Date(), Calendar.SATURDAY));

        Usuario usuario = umUsuario().agora();
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
        Usuario usuario = umUsuario().agora();
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
        Usuario usuario = umUsuario().agora();

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
        Usuario usuario = umUsuario().agora();
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
        Usuario usuario = umUsuario().agora();
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
        Usuario usuario = umUsuario().agora();
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
        Assume.assumeTrue(verificarDiaSemana(new Date(), Calendar.SATURDAY));

        Usuario usuario = umUsuario().agora();
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
    public void shouldNotRentFilmUserInSPC() throws Exception {
        Usuario usuario = umUsuario().agora();
        //Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("Usuário 2").agora();

        List<Filme> filmes = Arrays.asList(umFilme().agora());

        when(spcService.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true);

        try {
            service.alugarFilme(usuario, filmes);
            Assert.fail();
        } catch (LocadoraException e) {
            Assert.assertThat(e.getMessage(), is("Usuário negativado"));
        }

        verify(spcService).possuiNegativacao(usuario);
    }

    @Test
    public void shouldSendEmailDelayedLocations() {
        Usuario usuario = umUsuario().agora();
        Usuario usuario2 = umUsuario().comNome("Usuário em dia").agora();
        Usuario usuario3 = umUsuario().comNome("Outro usuário atrasado").agora();

        List<Locacao> locacoes = Arrays.asList(
                umLocacao().delayed().comUsuario(usuario).agora(),
                umLocacao().comUsuario(usuario2).agora(),
                umLocacao().delayed().comUsuario(usuario3).agora(),
                umLocacao().delayed().comUsuario(usuario3).agora());

        when(dao.getPendentLocations()).thenReturn(locacoes);

        service.notifyDelays();

        verify(email, Mockito.times(3)).notifyDelay(Mockito.any(Usuario.class));
        verify(email).notifyDelay(usuario);
        verify(email, atLeastOnce()).notifyDelay(usuario3);
        verify(email, never()).notifyDelay(usuario2);
        verifyNoMoreInteractions(email);
    }

    @Test
    public void shouldDealWithSPCErrors() throws Exception {
        Usuario usuario = umUsuario().agora();

        List<Filme> filmes = Arrays.asList(umFilme().agora());

        when(spcService.possuiNegativacao(usuario)).thenThrow(new Exception("Falha catastrófica"));

        //verificação
        exception.expect(LocadoraException.class);
        exception.expectMessage("Falha com serviço SPC, tente novamente!");

        //ação
        service.alugarFilme(usuario, filmes);
    }

    @Test
    public void shouldExtendLocation() {
        Locacao locacao = umLocacao().agora();

        service.extendLocation(locacao, 3);

        ArgumentCaptor<Locacao> argCapt = ArgumentCaptor.forClass(Locacao.class);
        Mockito.verify(dao).salvar(argCapt.capture());
        Locacao locacaoRetornada = argCapt.getValue();

        error.checkThat(locacaoRetornada.getValor(), is(4.0 * 3));
        error.checkThat(locacaoRetornada.getDataLocacao(), is(MatchersProprios.isToday()));
        error.checkThat(locacaoRetornada.getDataRetorno(), is(MatchersProprios.isTodayWithDifferenceOfDays(3)));


    }
}
