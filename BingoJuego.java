import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.text.DecimalFormat;

public class BingoJuego {

    private static double QTZaUSD = 7.67;
    private static double Costo = 4.00;
    private static int MAX_COMPRAS_NUMEROS = 4;

    private double saldoUsuario;
    private double inversionRondaActual;
    private int comprasNumerosRealizadas;
    private List<Numero> numerosGenerados;
    private Random random;
    private Scanner scanner;
    private DecimalFormat df;

    public BingoJuego(double saldoInicial) {
        this.saldoUsuario = saldoInicial;
        this.inversionRondaActual = 0.0;
        this.comprasNumerosRealizadas = 0;
        this.numerosGenerados = new ArrayList<>();
        this.random = new Random();
        this.scanner = new Scanner(System.in);
        this.df = new DecimalFormat("0.00");
    }

    public void iniciarJuego() {
        System.out.println("\n--- BINGO ---");
        mostrarSaldo();

        while (true) {
            mostrarMenu();
            String input = scanner.nextLine();
            int opcion;

            try {
                opcion = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                mostrarMensajeError("Ingrese un número 1, 2 o 3.");
                continue;
            }

            switch (opcion) {
                case 1:
                    jugarRonda();
                    break;
                case 2:
                    mostrarSaldo();
                    break;
                case 3:
                    System.out.println("¡Gracias por jugar!");
                    scanner.close();
                    return;
                default:
                    mostrarMensajeError("Opción inválida.");
            }
        }
    }

    private void mostrarMenu() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Jugar una ronda de Bingo");
        System.out.println("2. Ver mi saldo actual");
        System.out.println("3. Salir del juego");
        System.out.print("Ingrese su opción: ");
    }

    private void jugarRonda(){
        if (saldoUsuario < Costo) {
            mostrarMensajeError("Saldo insuficiente para comprar números.");
            return;
        }

        saldoUsuario -= Costo;
        inversionRondaActual = Costo;
        comprasNumerosRealizadas++;
        System.out.println("\n¡Números comprados por Q" + df.format(Costo) + "!");
        System.out.println("Número de compras realizadas: " + comprasNumerosRealizadas + " de " + MAX_COMPRAS_NUMEROS);
        mostrarSaldo();

        generarNumeros();
        System.out.print("Tus números generados son: ");
        for (Numero num : numerosGenerados) {
            System.out.print(num.getValor() + " ");
        }
        System.out.println();

        if (!verificarCondicionParidad()) {
            System.out.println("¡Gracias por su participación! No se cumplió la condición de tener al menos dos números pares.");
            inversionRondaActual = 0.0;
            return;
        }

        calcularGanancia();
    }

    private void generarNumeros() {
        numerosGenerados.clear();
        for (int i = 0; i < 3; i++) {
            numerosGenerados.add(new Numero(random.nextInt(901) + 100));
        }
    }

    private boolean verificarCondicionParidad() {
        int contadorPares = 0;
        for (Numero num : numerosGenerados) {
            if (num.esPar()) {
                contadorPares++;
            }
        }
        return contadorPares >= 2;
    }

    private void calcularGanancia() {
        List<Numero> numerosPares = new ArrayList<>();
        for (Numero num : numerosGenerados) {
            if (num.esPar()) {
                numerosPares.add(num);
            }
        }

        if (numerosPares.size() < 2) {
            System.out.println("No hay suficientes números pares");
            return;
        }

        int numPar1 = numerosPares.get(0).getValor();
        int numPar2 = numerosPares.get(1).getValor();
        int sumaPares = numPar1 + numPar2;

        double gananciaBruta = 0.0;

        if (numPar1 > 700 && numPar2 > 700) {
            gananciaBruta = 0.10 * sumaPares;
        } else if ((numPar1 >= 500 && numPar1 <= 700) && (numPar2 >= 500 && numPar2 <= 700)) {
            gananciaBruta = 0.20 * sumaPares;
        } else if ((numPar1 > 500 && numPar2 <= 500) || (numPar2 > 500 && numPar1 <= 500)) {
            gananciaBruta = 0.15 * sumaPares;
        } else if (numPar1 < 500 && numPar2 < 500) {
            gananciaBruta = 0.25 * sumaPares;
        } else {
             System.out.println("Condición de ganancia adicional encontrada.");
        }

        double gananciaNeta = gananciaBruta - inversionRondaActual;
        saldoUsuario += gananciaNeta;

        System.out.println("¡Felicidades! Has ganado Q" + df.format(gananciaNeta) + ".");
        mostrarSaldo();

        inversionRondaActual = 0.0;
    }

    public void mostrarSaldo() {
        double saldoDolares = saldoUsuario / QTZaUSD;
        System.out.println("Tu saldo actual es: Q" + df.format(saldoUsuario) + " ($" + df.format(saldoDolares) + ")");
    }

    private void mostrarMensajeError(String mensaje) {
        System.err.println("Hay un error: " + mensaje);
    }
}
