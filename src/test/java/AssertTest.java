import br.ce.wcaquino.entidades.Usuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AssertTest {

    @Test
    public void test() {
        Assertions.assertTrue(true);
        Assertions.assertFalse(false);

        Assertions.assertEquals(1, 1);
        Assertions.assertEquals(0.5123, 0.51, 0.01);
        Assertions.assertEquals(Math.PI, 3.14, 0.01);

        int i = 5;
        Integer i2 = 5;

        Assertions.assertEquals(i, i2);

        Assertions.assertEquals("bola", "bola");
        Assertions.assertTrue("bola".equalsIgnoreCase("Bola"));

        Usuario user1 = new Usuario("User 1");
        Usuario user2 = new Usuario("User 1");
        Usuario user3 = user2;
        Usuario user4 = null;

        Assertions.assertEquals(user1, user2);
        Assertions.assertSame(user2, user3);
        Assertions.assertNull(user4);


    }
}
