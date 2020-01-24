package izly;

import java.util.Random;

public class CodeSecret {
    private final String code;
    private boolean codeDejaRevele;
    private Random random;
    private int nbEssaisRestants;

    public CodeSecret(Random random) {
        this.random = random;
        codeDejaRevele = false;
        code = createCode(random);
        nbEssaisRestants = 3;
    }

    private String createCode(Random random) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }

    public String revelerCode() {
        if (!codeDejaRevele) {
            codeDejaRevele = true;
            return code;
        }
        return "xxxx";
    }

    public boolean verifierCode(String codeProposé) throws CodeBloquéException {
        if (nbEssaisRestants <= 0) throw new CodeBloquéException();
        if (!code.equals(codeProposé)){
            nbEssaisRestants--;
            return false;
        }
        nbEssaisRestants=3;
        return true;
    }
}
