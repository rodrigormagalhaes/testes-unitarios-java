import br.ce.wcaquino.entidades.Usuario;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class AssertTest {

    @Test
    public void test() {
        assertTrue(true);
        assertFalse(false);

        assertEquals(1, 1);
        assertEquals(0.5123, 0.51, 0.01);
        assertEquals(Math.PI, 3.14, 0.01);

        int i = 5;
        Integer i2 = 5;

        //assertEquals(i, i2);

        assertEquals("bola", "bola");
        assertTrue("bola".equalsIgnoreCase("Bola"));

        Usuario user1 = new Usuario("User 1");
        Usuario user2 = new Usuario("User 1");
        Usuario user3 = user2;
        Usuario user4 = null;

        assertEquals(user1, user2);
        assertSame(user2, user3);
        assertNull(user4);


    }
}
