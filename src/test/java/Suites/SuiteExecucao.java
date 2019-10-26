package Suites;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import servicos.CalculadoraTest;
import servicos.CalculoValorLocacaoTest;
import servicos.LocacaoServiceTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CalculadoraTest.class,
        CalculoValorLocacaoTest.class,
        LocacaoServiceTest.class
})
public class SuiteExecucao {

    @BeforeClass
    public static void before() {
        System.out.println("before");

    }

    @AfterClass
    public static void after() {
        System.out.println("after");
    }


}
