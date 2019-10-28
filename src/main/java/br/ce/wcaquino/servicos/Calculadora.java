package br.ce.wcaquino.servicos;

import br.ce.wcaquino.br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;

public class Calculadora {

    public int somar(int a, int b) {
        return a + b;
    }

    public int subtract(int i, int i1) {
        return i - i1;
    }

    public int divide(int x, int y) throws NaoPodeDividirPorZeroException {
        if (y == 0) {
            throw new NaoPodeDividirPorZeroException();
        }

        return x/y;
    }

    public int divide(String x, String y) {
        return Integer.valueOf(x) / Integer.valueOf(y);
    }

}
