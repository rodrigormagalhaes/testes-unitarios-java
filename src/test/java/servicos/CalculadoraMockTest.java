package servicos;

import br.ce.wcaquino.servicos.Calculadora;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

public class CalculadoraMockTest {

    @Mock
    private Calculadora calcMock;

    @Spy
    private Calculadora calcSpy;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldShowDifferenceBetweenMockSpy() {
        //Mockito.when(calcMock.somar(1, 2)).thenReturn(8);
        Mockito.when(calcMock.somar(1, 2)).thenCallRealMethod();
        Mockito.when(calcSpy.somar(1, 2)).thenReturn(8);

        System.out.println("Mock: " + calcMock.somar(1, 2));
        System.out.println("Mock: " + calcMock.somar(2, 2));

        System.out.println("Spy: " + calcSpy.somar(1, 2));
        System.out.println("Spy: " + calcSpy.somar(2, 2));


    }

    @Test
    public void test() {
        Calculadora calc = Mockito.mock(Calculadora.class);

//        Mockito.when(calc.somar(Mockito.eq(1), Mockito.anyInt())).thenReturn(5);
//
//        System.out.println(calc.somar(1, 2));

        ArgumentCaptor<Integer> argCapt = ArgumentCaptor.forClass(Integer.class);
        Mockito.when(calc.somar(argCapt.capture(), argCapt.capture())).thenReturn(5);

//        System.out.println(calc.somar(1, 2));
//
//        System.out.println(argCapt.getAllValues());

    }
}
