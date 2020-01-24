package izly;

import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Matches;

import java.util.Random;

public class CodeSecretUnitTest {
    
    @Test
    public void testRevelerCode() {
        CodeSecret codeSecret = new CodeSecret(new Random());
        //Assert.assertTrue(isCode(codeSecret.revelerCode()));
        Assert.assertThat(codeSecret.revelerCode(), new Matches("\\d{4}"));
        Assert.assertEquals("xxxx", codeSecret.revelerCode());
    }

    private boolean isCode(String code) {
        if (code.length() != 4) return false;
/*        for (int i = 0; i < 4; i++) {
            if (code.charAt(i)<'0' || code.charAt(i) > '9')
                return false;
        }
        */
        try {
            Integer.valueOf(code);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Test
    public void testCreationAleatoireCode() {
        Random random = Mockito.mock(Random.class);
        Mockito.when(random.nextInt(10)).thenReturn(0,1,2,3);
        CodeSecret codeSecret = new CodeSecret(random);
        Assert.assertEquals("0123", codeSecret.revelerCode());
    }
    
    @Test
    public void testVérifierCode() throws Exception {
        Random random = Mockito.mock(Random.class);
        Mockito.when(random.nextInt(10)).thenReturn(0,1,2,3);
        CodeSecret codeSecret = new CodeSecret(random);
        Assert.assertTrue(codeSecret.verifierCode("0123"));
        Assert.assertFalse(codeSecret.verifierCode("1245"));
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testCodeBloquéAprès3EssaisFauxSuccessifs() throws Exception {
        Random random = Mockito.mock(Random.class);
        Mockito.when(random.nextInt(10)).thenReturn(0,1,2,3);
        CodeSecret codeSecret = new CodeSecret(random);
        codeSecret.verifierCode("0000");
        codeSecret.verifierCode("1111");
        codeSecret.verifierCode("2222");
        expectedException.expect(CodeBloquéException.class);
        codeSecret.verifierCode("0123");
    }

    @Test
    public void testNbEssaisRemisAuMaxAprèsEssaisJusteAvantTroisiemeEssaisFauxConsécutifs() throws Exception {
        Random random = Mockito.mock(Random.class);
        Mockito.when(random.nextInt(10)).thenReturn(0,1,2,3);
        CodeSecret codeSecret = new CodeSecret(random);
        codeSecret.verifierCode("0000");
        codeSecret.verifierCode("1111");
        Assert.assertTrue(codeSecret.verifierCode("0123"));
        codeSecret.verifierCode("2222");
        codeSecret.verifierCode("3333");
        codeSecret.verifierCode("4444");
        expectedException.expect(CodeBloquéException.class);
        codeSecret.verifierCode("0123");
    }
}
