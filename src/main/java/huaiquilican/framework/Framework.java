
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
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream(configFilePath),
                StandardCharsets.UTF_8))) {

            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.startsWith("acciones:")) {
                    String lista = linea.substring("acciones:".length()).trim();
                    String[] clases = lista.split(";");
                    for (String clase : clases) {
                        try {
                            Class<?> c = Class.forName(clase.trim());
                            Accion accion = (Accion) c.getDeclaredConstructor().newInstance();
                            acciones.add(accion);
                        } catch (Exception e) {
                            System.out.println("Error cargando: " + clase + " - " + e.getMessage());
                        }
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Error leyendo config: " + e.getMessage());
        }
    }

    public void ejecutar() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Bienvenido, estas son sus opciones:");
            for (int i = 0; i < acciones.size(); i++) {
                Accion a = acciones.get(i);
                System.out.printf("%d. %s (%s)%n", i + 1, a.nombreItemMenu(), a.descripcionItemMenu());
            }
            System.out.printf("%d. Salir%n", acciones.size() + 1);

            System.out.print("Ingrese su opción: ");
            String input = sc.nextLine();
            try {
                int opcion = Integer.parseInt(input);
                if (opcion == acciones.size() + 1) {
                    System.out.println("Saliendo...");
                    break;
                } else if (opcion >= 1 && opcion <= acciones.size()) {
                    Accion accion = acciones.get(opcion - 1);
                    System.out.println("Ejecutando " + accion.nombreItemMenu() + "...");
                    accion.ejecutar();
                } else {
                    System.out.println("Opción inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número.");
            }
        }
    }
}
