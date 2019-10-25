package servicos;

import br.ce.wcaquino.br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;
import br.ce.wcaquino.servicos.Calculadora;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CalculadoraTest {

    Calculadora calculadora;

    @Before
    public void setUp() {
        calculadora = new Calculadora();
    }

    @Test
    public void shouldSumValues() {
        int a = 5;
        int b = 3;

        int result = calculadora.somar(a, b);

        Assert.assertEquals(8, result);
    }

    @Test
    public void shouldSubtractValues() {
        int a = 10;
        int b= 6;

        int result = calculadora.subtract(a, b);

        Assert.assertEquals(4, result);
    }

    @Test
    public void shouldDivideValues() throws NaoPodeDividirPorZeroException {
        int a = 10;
        int b = 2;

        int result = calculadora.divide(a, b);

        Assert.assertEquals(5, result);
    }

    @Test(expected = NaoPodeDividirPorZeroException.class)
    public void shouldThrowExceptionWhenDivideByZero() throws NaoPodeDividirPorZeroException {
        int a = 10;
        int b = 0;

        calculadora.divide(a, b);
    }
}
