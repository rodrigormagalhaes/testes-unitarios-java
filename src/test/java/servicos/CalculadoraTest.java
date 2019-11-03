package servicos;

import br.ce.wcaquino.br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;
import br.ce.wcaquino.servicos.Calculadora;
import org.junit.*;
import org.junit.runner.RunWith;
import runners.ParallelRunner;

//@RunWith(ParallelRunner.class)
public class CalculadoraTest {

    public static StringBuffer ordem = new StringBuffer();

    Calculadora calculadora;

    @Before
    public void setUp() {
        calculadora = new Calculadora();

        System.out.println("Iniciando...");
        ordem.append("1");
    }

    @After
    public void tearDown() {
        System.out.println("Finalizando...");
    }

    @AfterClass
    public static void tearDownClass() {
        System.out.println(ordem.toString());
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
