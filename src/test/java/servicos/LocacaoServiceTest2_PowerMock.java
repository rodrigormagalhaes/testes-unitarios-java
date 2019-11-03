package servicos;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.servicos.EmailService;
import br.ce.wcaquino.servicos.LocacaoService;
import br.ce.wcaquino.servicos.SPCService;
import br.ce.wcaquino.utils.DataUtils;
import matchers.DiaSemanaMatcher;
import matchers.MatchersProprios;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static builders.FilmeBuilder.umFilme;
import static builders.UsuarioBuilder.umUsuario;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LocacaoService.class)
public class LocacaoServiceTest2_PowerMock {

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

        service = PowerMockito.spy(service);

        System.out.println("Iniciando 5...");
        CalculadoraTest.ordem.append(5);
    }

    @AfterClass
    public static void tearDownClass() {
        System.out.println(CalculadoraTest.ordem.toString());
    }

    @After
    public void tearDown() {
        System.out.println("Finalizando 5...");
    }

    @Test
    public void shouldRentFilm() throws Exception {

        //Assume.assumeFalse(verificarDiaSemana(new Date(), Calendar.SATURDAY));

        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());

        PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(28, 4, 2017));

        Locacao locacao;

        locacao = service.alugarFilme(usuario, filmes);

        error.checkThat(locacao.getValor(), is(equalTo(5.0)));
        error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obterData(28, 4, 2017)), is(true));
        error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterData(29, 4, 2017)), is(true));

        assertThat(locacao.getValor(), is(5.00));

    }

    @Test
    public void shouldGiveBackOnMondayWhenRentOnSaturday() throws Exception {
        //Assume.assumeTrue(verificarDiaSemana(new Date(), Calendar.SATURDAY));

        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

       // PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(2, 11, 2019));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 29);
        calendar.set(Calendar.MONTH, Calendar.APRIL);
        calendar.set(calendar.YEAR, 2017);

        PowerMockito.mockStatic(Calendar.class);
        PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);

        Locacao retorno = service.alugarFilme(usuario, filmes);

//        boolean IsMonday = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
//
//        Assert.assertTrue(IsMonday);

        assertThat(retorno.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));

        assertThat(retorno.getDataRetorno(), MatchersProprios.caiEm(Calendar.MONDAY));
        assertThat(retorno.getDataRetorno(), MatchersProprios.caiNumaSegunda());

//        PowerMockito.verifyNew(Date.class, Mockito.times(2)).withNoArguments();

        PowerMockito.verifyStatic(Mockito.times(2));
        service.getData();

    }

    @Test
    public void shouldRentFilmWithoutCalcValue() throws Exception {
        //cenario
        Usuario usuario = umUsuario().agora();

        List<Filme> filmes = Arrays.asList(umFilme().agora());

        PowerMockito.doReturn(1.0).when(service, "calcularValorLocacao", filmes);

        //acao
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //verificacao
        Assert.assertThat(locacao.getValor(), is(1.0));
        PowerMockito.verifyPrivate(service).invoke("calcularValorLocacao", filmes);

    }

    @Test
    public void shouldCalcLocationValue() throws Exception {
        //cenario
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        //acao
        Double valor = (Double) Whitebox.invokeMethod(service, "calcularValorLocacao", filmes);

        //verificacao
        Assert.assertThat(valor, is(4.0));

    }
}
