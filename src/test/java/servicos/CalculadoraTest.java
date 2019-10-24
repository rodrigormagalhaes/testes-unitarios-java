package servicos;

import br.ce.wcaquino.servicos.Calculadora;
import org.junit.Assert;
import org.junit.Test;

public class CalculadoraTest {

    @Test
    public void shouldSumValues() {
        int a = 5;
        int b = 3;

        Calculadora calculadora = new Calculadora();

        int result = calculadora.somar(a, b);

        Assert.assertEquals(8, result);

    }

    @Test
    public void shouldSubtractValues() {
        int a = 10;
        int b= 6;

        Calculadora calculadora = new Calculadora();

        int result = calculadora.subtract(10, 6);

        Assert.assertEquals(4, result);

    }
}
