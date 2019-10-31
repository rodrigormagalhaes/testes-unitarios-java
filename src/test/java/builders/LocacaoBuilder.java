package builders;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Usuario;
import java.util.Arrays;
import java.lang.Double;
import java.util.Date;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.utils.DataUtils;

import static builders.UsuarioBuilder.*;


public class LocacaoBuilder {
    private Locacao locacao;
    private LocacaoBuilder(){}

    public static LocacaoBuilder umLocacao() {
        LocacaoBuilder builder = new LocacaoBuilder();
        inicializarDadosPadroes(builder);
        return builder;
    }

    public static void inicializarDadosPadroes(LocacaoBuilder builder) {
        builder.locacao = new Locacao();
        Locacao locacao = builder.locacao;

        locacao.setUsuario(umUsuario().agora());
        locacao.setFilmes(Arrays.asList(FilmeBuilder.umFilme().agora()));
        locacao.setDataLocacao(new Date());
        locacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(1));
        locacao.setValor(4.0);
    }

    public LocacaoBuilder comUsuario(Usuario param) {
        locacao.setUsuario(param);
        return this;
    }

    public LocacaoBuilder comListaFilmes(Filme... params) {
        locacao.setFilmes(Arrays.asList(params));
        return this;
    }

    public LocacaoBuilder comDataLocacao(Date param) {
        locacao.setDataLocacao(param);
        return this;
    }

    public LocacaoBuilder comDataRetorno(Date param) {
        locacao.setDataRetorno(param);
        return this;
    }

    public LocacaoBuilder comValor(Double param) {
        locacao.setValor(param);
        return this;
    }

    public Locacao agora() {
        return locacao;
    }

    public LocacaoBuilder delayed() {
        locacao.setDataLocacao(DataUtils.obterDataComDiferencaDias(-4));
        locacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(-2));

        return this;
    }
}
