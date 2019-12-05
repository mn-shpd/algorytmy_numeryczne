import java.math.BigInteger;

public class MyOwnType extends Number {

    BigInteger numerator;
    BigInteger denominator;

    MyOwnType() {}

    MyOwnType(BigInteger numerator, BigInteger denominator) {
        this.numerator = numerator;
        if (denominator.compareTo(BigInteger.valueOf(0)) == 0) {
            throw new IllegalArgumentException("Denominator cannot be equal to 0!");
        }
        this.denominator = denominator;
        this.negation();
        this.shortenFraction();
    }

    //Nadpisanie metody wyswietlania obiektu.
    public String toString() {
        return getNumerator() + "/" + getDenominator();
    }

    public void shortenFraction() {
        //Skraca ulamek poprzez dzielenie kolejno licznika i mianownika przez ich NWD.
        BigInteger gcd = this.getNumerator().gcd(getDenominator());
        //compareTo zwraca 0, gdy oba obiekty BigInteger maja taka sama wartosc.
        if (gcd.compareTo(BigInteger.valueOf(1)) != 0) {
            this.setNumerator(this.getNumerator().divide(gcd));
            this.setDenominator(this.getDenominator().divide(gcd));
        }
    }

    public void negation() {
        //Sprawdza czy ulamki maja odpowiednio ustawiony znak ujemnosci.
        if ((this.getNumerator().compareTo(BigInteger.valueOf(0)) == -1 && this.getDenominator().compareTo(BigInteger.valueOf(0)) == -1) ||
                (this.getNumerator().compareTo(BigInteger.valueOf(0)) == 1 && this.getDenominator().compareTo(BigInteger.valueOf(0)) == -1)) {
            this.setNumerator(this.getNumerator().negate());
            this.setDenominator(this.getDenominator().negate());
        }
    }

    public BigInteger getNumerator() {
        return numerator;
    }

    public void setNumerator(BigInteger numerator) {
        this.numerator = numerator;
    }

    public BigInteger getDenominator() {
        return denominator;
    }

    public void setDenominator(BigInteger denominator) {
        this.denominator = denominator;
    }

    @Override
    public int intValue() {
        return 0;
    }

    @Override
    public long longValue() {
        return 0;
    }

    @Override
    public float floatValue() {
        return 0;
    }

    @Override
    public double doubleValue() {
        return 0;
    }

}
