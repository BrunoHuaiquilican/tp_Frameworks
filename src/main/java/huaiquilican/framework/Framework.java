
package huaiquilican.framework;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Framework {

    private List<Accion> acciones = new ArrayList<>();

    public Framework(String configFilePath) {
        cargarAcciones(configFilePath);
    }

    //Método que carga las acciones desde el archivo de configuración.

    private void cargarAcciones(String configFilePath) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream(configFilePath),
                StandardCharsets.UTF_8))) {

            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.startsWith("acciones:")) {
                    String lista = linea.substring("acciones:".length()).trim();
                    String[] clases = lista.split(";");
                    for (String clase : clases) {
                        cargarAccion(clase.trim());
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Error leyendo config: " + e.getMessage());
        }
    }

    // Método que instancia una acción a partir del nombre de la clase.

    private void cargarAccion(String nombreClase) {
        try {
            Class<?> c = Class.forName(nombreClase);
            Accion accion = (Accion) c.getDeclaredConstructor().newInstance();
            acciones.add(accion);
        } catch (Exception e) {
            System.out.println("Error cargando: " + nombreClase + " - " + e.getMessage());
        }
    }

    //Método principal que muestra el menú y ejecuta acciones.
    public void ejecutar() {
        Scanner sc = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            mostrarMenu();
            int opcion = leerOpcion(sc);

            if (opcion == acciones.size() + 1) {
                System.out.println("Saliendo...");
                continuar = false;
            } else if (esOpcionValida(opcion)) {
                ejecutarAccion(opcion);
            } else {
                System.out.println("Opción inválida.");
            }
        }
    }

    //Método que muestra el menú de opciones.

    private void mostrarMenu() {
        System.out.println("Bienvenido, estas son sus opciones:");
        for (int i = 0; i < acciones.size(); i++) {
            Accion a = acciones.get(i);
            System.out.printf("%d. %s (%s)%n", i + 1, a.nombreItemMenu(), a.descripcionItemMenu());
        }
        System.out.printf("%d. Salir%n", acciones.size() + 1);
        System.out.print("Ingrese su opción: ");
    }

    // Método que lee la opción elegida por el usuario.

    private int leerOpcion(Scanner sc) {
        try {
            return Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Debe ingresar un número.");
            return -1;
        }
    }

    // Método que valida si la opción es una acción disponible.

    private boolean esOpcionValida(int opcion) {
        return opcion >= 1 && opcion <= acciones.size();
    }

    //Método que ejecuta la acción elegida.

    private void ejecutarAccion(int opcion) {
        Accion accion = acciones.get(opcion - 1);
        System.out.println("Ejecutando " + accion.nombreItemMenu() + "...");
        accion.ejecutar();
    }
}
