package main.java.izly;

import main.java.izly.CodeErronnéException;

public class Purse {
    private double solde;
    private double plafond;
    private int nbOperationsMax;
    private izly.CodeSecret codeSecret;


    private Purse(double plafond, int nbOperationsMax, izly.CodeSecret codeSecret) {
        this.plafond = plafond;
        this.nbOperationsMax = nbOperationsMax;
        this.codeSecret = codeSecret;
    }

    public static Purse createPurse(double plafond, int nbOperationMax, izly.CodeSecret codeSecret) throws izly.CreationPurseException {
        if (nbOperationMax <=0 || plafond <=0 ) throw new izly.CreationPurseException();
        return new Purse(plafond, nbOperationMax, codeSecret);
    }

    public double getSolde() {
        return solde;
    }

    public void debite(double montant, String codeProposé) throws izly.RejetTransactionException {
        try {
            if (!codeSecret.verifierCode(codeProposé))
                throw new izly.RejetTransactionException(new CodeErronnéException());
        } catch (izly.CodeBloquéException e) {
            throw new izly.RejetTransactionException(e);
        }
        prepareTransaction(montant);
        if (montant > solde)
            throw new izly.RejetTransactionException(new izly.SoldeNegatifException());
        solde -= montant;
        postTransaction();
    }

    public void credite(double montant) throws izly.RejetTransactionException {
        prepareTransaction(montant);
        if (montant+solde > plafond)
            throw new izly.RejetTransactionException(new izly.DepassementPlafondException());
        solde += montant;
        postTransaction();
    }

    private void postTransaction() {
        nbOperationsMax--;
    }

    private void prepareTransaction(double montant) throws izly.RejetTransactionException {
        if (nbOperationsMax<=0)
            throw new izly.RejetTransactionException(new izly.NbOperationsMaxAtteindException());
        if (montant <0)
            throw new izly.RejetTransactionException(new izly.MontantNegatifException());
    }
}
