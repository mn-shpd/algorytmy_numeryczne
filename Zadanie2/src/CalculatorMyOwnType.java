import java.math.BigInteger;

public class CalculatorMyOwnType implements Calculator<MyOwnType> {

    @Override
    public MyOwnType add(MyOwnType first, MyOwnType second) {

        MyOwnType newMyOwnType = new MyOwnType();

        //Jesli mianowniki takie same.
        if (first.getDenominator().equals(second.getDenominator())) {
            newMyOwnType.setNumerator(first.numerator.add(second.getNumerator()));
            newMyOwnType.setDenominator(first.getDenominator());
        } else {
            //Jesli nie, to liczy NWW, ktora staje sie nowym mianownikiem.
            BigInteger lcm = leastCommonMultiple(first.getDenominator(), second.getDenominator());
            newMyOwnType.setNumerator(first.getNumerator().multiply(lcm.divide(first.getDenominator())));
            BigInteger temp = second.getNumerator().multiply(lcm.divide(second.getDenominator()));
            newMyOwnType.setNumerator(newMyOwnType.getNumerator().add(temp));
            newMyOwnType.setDenominator(lcm);
        }
        //Skraca ulamki, jesli sie da.
        newMyOwnType.shortenFraction();
        return newMyOwnType;
    }

    @Override
    public MyOwnType subtract(MyOwnType first, MyOwnType second) {

        MyOwnType newMyOwnType = new MyOwnType();
        newMyOwnType = add(first, multiply(second, new MyOwnType(new BigInteger("-1"), new BigInteger("1"))));
        return newMyOwnType;
    }

    @Override
    public MyOwnType multiply(MyOwnType first, MyOwnType second) {

        MyOwnType newMyOwnType = new MyOwnType(first.getNumerator().multiply(second.getNumerator()), first.getDenominator().multiply(second.getDenominator()));
        newMyOwnType.shortenFraction();
        return newMyOwnType;
    }

    //Zamienia licznik z mianownikiem w obiekcie podanym jako argument, przypisujac go do nowego obiektu i wywoluje metode multiply.
    @Override
    public MyOwnType divide(MyOwnType first, MyOwnType second) {

        MyOwnType multiplier = new MyOwnType(second.getDenominator(), second.getNumerator());
        return multiply(first, multiplier);
    }

    //Sprowadza do wspolnego mianownika i porownuje liczniki
    @Override
    public int compareTo(MyOwnType first, MyOwnType second) {

        MyOwnType firstTemp = new MyOwnType(first.getNumerator(), first.getDenominator());
        MyOwnType secondTemp = new MyOwnType(second.getNumerator(), second.getDenominator());

        BigInteger lcm = leastCommonMultiple(first.getDenominator(), second.getDenominator());
        firstTemp.setNumerator(firstTemp.getNumerator().multiply(lcm.divide(firstTemp.getDenominator())));
        secondTemp.setNumerator(secondTemp.getNumerator().multiply(lcm.divide(secondTemp.getDenominator())));

        return firstTemp.getNumerator().compareTo(secondTemp.getNumerator());
    }

    //Zwraca zero

    @Override
    public MyOwnType returnZero() {
        return new MyOwnType(new BigInteger("0"), new BigInteger("1"));
    }

    @Override
    public MyOwnType abs(MyOwnType x) {
        return new MyOwnType(x.getNumerator().abs(), x.getDenominator().abs());
    }

    @Override
    public MyOwnType convertInt(int x) {
        return new MyOwnType(new BigInteger(Integer.toString(x)), new BigInteger("1"));
    }

    @Override
    public MyOwnType parse(String s) {
        int position = s.indexOf("/");
        BigInteger numerator = new BigInteger(s.substring(0, position));
        BigInteger denumerator = new BigInteger(s.substring(position+1));
        return new MyOwnType(numerator, denumerator);
    }

    //Najmniejsza wspolna wielokrotnosc
    //https://stackoverflow.com/questions/3154454/what-is-the-most-efficient-way-to-calculate-the-least-common-multiple-of-two-int
    public BigInteger leastCommonMultiple(BigInteger first, BigInteger second) {
        //NWD
        BigInteger temp = first.gcd(second);
        //NWW = a * b / NWD(a,b)
        return first.multiply(second).divide(temp);
    }
}
