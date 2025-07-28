public class Numero {
    private int valor;

    public Numero(int valor) {
        this.valor = valor;
    }

    public boolean esPar() {
        return this.valor % 2 == 0;
    }

    public int getValor() {
        return valor;
    }

    public String toString() {
        return String.valueOf(valor);
    }
}