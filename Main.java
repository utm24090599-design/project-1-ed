/* 

Una cafetería universitaria enfrenta serios problemas de organización en la atención a sus clientes. Actualmente, el proceso de registro de pedidos, asignación de turnos y control del historial de atención se realiza de forma manual, lo que genera largas filas, errores en los pedidos, pérdida de información y una experiencia negativa para los estudiantes y docentes.

    Planteamento: La administración de la cafetería ha solicitado el desarrollo de un sistema con el lenguaje de Java, gestionado desde el entorno de Visual Studio Code, que permita automatizar y optimizar los procesos internos mediante el uso de estructuras de datos dinámicas.

Requerimientos Funcionales del Sistema
    1. Gestión de Turnos de Atención:
        El sistema debe manejar una cola de clientes que esperan ser atendidos en la ventanilla principal. Cada vez que un cliente llega, su número de turno debe agregarse al final de la cola. Cuando un cajero queda libre, el primer cliente en espera debe ser eliminado de la cola y llamado a la ventanilla. El sistema debe mostrar en todo momento cuántos clientes están esperando y quién es el siguiente en ser atendido.
    
    2. Control de Estaciones de Servicio:
        La cafetería cuenta con tres estaciones de preparación de alimentos (bebidas, comida caliente y snacks), las cuales deben atenderse de forma circular y continua sin que ninguna quede desatendida por mucho tiempo. Para esto, el sistema implementará una cola circular que recorra cíclicamente las estaciones, asignando personal de apoyo de manera equitativa. El manejo de la cola circular debe contemplar los casos en que la cola esté llena o vacía, evitando sobreescritura de datos.
    
    3. Historial de Pedidos Recientes
        El sistema debe registrar los últimos pedidos procesados en cada caja utilizando una pila. Esto permite al cajero deshacer el último pedido en caso de error, operación por operación. Las operaciones básicas con pilas incluyen: consultar el pedido en el tope, verificar si la pila está vacía antes de extraer, e insertar nuevos pedidos, considerando el caso especial de inserción en una pila vacía al iniciar el turno de cada cajero.
    
    4. Catálogo Dinámico de Productos
        El menú de la cafetería cambia con frecuencia: se agregan productos nuevos, se eliminan los agotados y se actualizan precios. El sistema utilizará una lista enlazada abierta para representar el catálogo de productos disponibles. Las operaciones básicas con listas incluyen recorrer el catálogo completo para mostrarlo en pantalla. Además, el sistema debe permitir insertar nuevos elementos en cualquier posición de la lista (al inicio, en medio o al final), así como eliminar elementos cuando un producto se agote o sea retirado del menú, reorganizando los nodos correctamente sin perder la integridad de la cadena.

*/
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

// ============================================================
// PUNTO 2: Cola Circular para Estaciones de Servicio
// ============================================================
class ColaCircularEstaciones {
    private String[] estaciones;
    private int front, rear, size, capacidad;

    public ColaCircularEstaciones(int n) {
        this.capacidad = n;
        this.estaciones = new String[capacidad];
        this.front = 0;
        this.rear = -1;
        this.size = 0;
    }

    public void enqueue(String estacion) {
        if (size == capacidad) {
            System.out.println("  [Cola Circular] Desbordamiento: Estaciones llenas.");
            return;
        }
        rear = (rear + 1) % capacidad;
        estaciones[rear] = estacion;
        size++;
    }

    public String dequeue() {
        if (size == 0) {
            System.out.println("  [Cola Circular] Error: No hay estaciones.");
            return null;
        }
        String dato = estaciones[front];
        front = (front + 1) % capacidad;
        size--;
        return dato;
    }

    public boolean estaVacia() {
        return size == 0;
    }

    public void mostrarEstado() {
        System.out.print("  [Estaciones] En servicio: [ ");
        for (int i = 0; i < size; i++) {
            System.out.print(estaciones[(front + i) % capacidad] + " ");
        }
        System.out.println("]");
    }
}

// ============================================================
// PUNTO 4: Nodo y Lista Enlazada para Catálogo de Productos
// ============================================================
class Producto {
    String nombre;
    double precio;
    Producto siguiente;

    public Producto(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
        this.siguiente = null;
    }
}

class CatalogoProductos {
    private Producto cabeza;

    public CatalogoProductos() {
        this.cabeza = null;
    }

    // Insertar al inicio
    public void insertarAlInicio(String nombre, double precio) {
        Producto nuevo = new Producto(nombre, precio);
        nuevo.siguiente = cabeza;
        cabeza = nuevo;
        System.out.println("  [Catálogo] Producto agregado al inicio: " + nombre);
    }

    // Insertar al final
    public void insertarAlFinal(String nombre, double precio) {
        Producto nuevo = new Producto(nombre, precio);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Producto actual = cabeza;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo;
        }
        System.out.println("  [Catálogo] Producto agregado al final: " + nombre);
    }

    // Insertar en posición específica (0 = inicio)
    public void insertarEnPosicion(String nombre, double precio, int posicion) {
        if (posicion <= 0) {
            insertarAlInicio(nombre, precio);
            return;
        }
        Producto nuevo = new Producto(nombre, precio);
        Producto actual = cabeza;
        for (int i = 0; i < posicion - 1 && actual != null; i++) {
            actual = actual.siguiente;
        }
        if (actual == null) {
            insertarAlFinal(nombre, precio);
        } else {
            nuevo.siguiente = actual.siguiente;
            actual.siguiente = nuevo;
            System.out.println("  [Catálogo] Producto agregado en posición " + posicion + ": " + nombre);
        }
    }

    // Eliminar por nombre
    public void eliminar(String nombre) {
        if (cabeza == null) {
            System.out.println("  [Catálogo] El catálogo está vacío.");
            return;
        }
        if (cabeza.nombre.equals(nombre)) {
            cabeza = cabeza.siguiente;
            System.out.println("  [Catálogo] Producto eliminado: " + nombre);
            return;
        }
        Producto actual = cabeza;
        while (actual.siguiente != null && !actual.siguiente.nombre.equals(nombre)) {
            actual = actual.siguiente;
        }
        if (actual.siguiente == null) {
            System.out.println("  [Catálogo] Producto no encontrado: " + nombre);
        } else {
            actual.siguiente = actual.siguiente.siguiente;
            System.out.println("  [Catálogo] Producto eliminado: " + nombre);
        }
    }

    // Actualizar precio
    public void actualizarPrecio(String nombre, double nuevoPrecio) {
        Producto actual = cabeza;
        while (actual != null) {
            if (actual.nombre.equals(nombre)) {
                actual.precio = nuevoPrecio;
                System.out.println("  [Catálogo] Precio actualizado: " + nombre + " -> $" + nuevoPrecio);
                return;
            }
            actual = actual.siguiente;
        }
        System.out.println("  [Catálogo] Producto no encontrado: " + nombre);
    }

    // Recorrer e imprimir catálogo
    public void mostrarCatalogo() {
        if (cabeza == null) {
            System.out.println("  [Catálogo] No hay productos disponibles.");
            return;
        }
        System.out.print("  [Catálogo] Productos disponibles: ");
        Producto actual = cabeza;
        while (actual != null) {
            System.out.print(actual.nombre + "($" + actual.precio + ")");
            if (actual.siguiente != null) System.out.print(" -> ");
            actual = actual.siguiente;
        }
        System.out.println();
    }
}

// ============================================================
// CLASE PRINCIPAL
// ============================================================
public class Main {
    public static void main(String[] args) throws InterruptedException {

        // ---------- PUNTO 1: Cola de turnos ----------
        Queue<Integer> cola = new LinkedList<>();
        int totalPersonas = 0;
        int limite = 10; // Reducido para demo más clara
        int ventanillaNum = 0;
        Random rand = new Random();

        // ---------- PUNTO 2: Cola circular de estaciones ----------
        ColaCircularEstaciones estaciones = new ColaCircularEstaciones(3);
        estaciones.enqueue("Bebidas");
        estaciones.enqueue("Comida Caliente");
        estaciones.enqueue("Snacks");

        // ---------- PUNTO 3: Pila de historial de pedidos ----------
        Stack<String> historialPedidos = new Stack<>();

        // ---------- PUNTO 4: Lista enlazada - Catálogo de productos ----------
        CatalogoProductos catalogo = new CatalogoProductos();
        System.out.println("\n========== CATÁLOGO INICIAL ==========");
        catalogo.insertarAlFinal("Café", 15.0);
        catalogo.insertarAlFinal("Torta de Jamón", 35.0);
        catalogo.insertarAlFinal("Papas Fritas", 20.0);
        catalogo.insertarAlInicio("Agua", 10.0);
        catalogo.insertarEnPosicion("Jugo Natural", 25.0, 2);
        catalogo.mostrarCatalogo();

        System.out.println("\n  [Catálogo] Actualizando precio de Café...");
        catalogo.actualizarPrecio("Café", 18.0);

        System.out.println("\n  [Catálogo] Eliminando producto agotado: Papas Fritas...");
        catalogo.eliminar("Papas Fritas");
        catalogo.mostrarCatalogo();

        // ---------- SIMULACIÓN PRINCIPAL ----------
        System.out.println("\n========== INICIO DE ATENCIÓN ==========");

        while (totalPersonas < limite) {

            // PUNTO 1: Llegan clientes a la cola
            int llegadas = rand.nextInt(1, 4);
            System.out.println("\n--- Nueva ronda ---");
            for (int i = 0; i < llegadas && totalPersonas < limite; i++) {
                totalPersonas++;
                cola.add(totalPersonas);
                System.out.println("  [Cola] Cliente #" + totalPersonas + " entró a la fila.");
            }

            System.out.println("  [Cola] Clientes esperando: " + cola.size());

            // PUNTO 2: Mostrar estado de estaciones
            estaciones.mostrarEstado();

            // PUNTO 1: Mostrar siguiente en ser atendido
            if (!cola.isEmpty()) {
                System.out.println("  [Cola] Próximo a ser atendido: Cliente #" + cola.peek());
            }

            Thread.sleep(2000);

            // PUNTO 1 + 2 + 3: Atender al primer cliente
            if (!cola.isEmpty()) {
                ventanillaNum++;
                int atendido = cola.poll(); // Sacar de la cola (PUNTO 1)

                // PUNTO 2: Asignar estación de forma circular
                String estacionActual = estaciones.dequeue();
                estaciones.enqueue(estacionActual); // Se reintegra al final (ciclo continuo)
                System.out.println("  [Ventanilla " + ventanillaNum + "] Cliente #" + atendido
                        + " atendido en estación: " + estacionActual);

                // PUNTO 3: Registrar pedido en la pila
                String pedido = "Pedido_C" + atendido + "_" + estacionActual.replace(" ", "");
                historialPedidos.push(pedido);
                System.out.println("  [Pila] Pedido registrado: " + pedido);
                System.out.println("  [Pila] Pedido en tope: " + historialPedidos.peek());

                // PUNTO 3: Simular deshacer pedido con ~20% de probabilidad
                if (!historialPedidos.isEmpty() && rand.nextInt(10) < 2) {
                    System.out.println("  [Pila] ¡ERROR! Deshaciendo pedido: " + historialPedidos.pop());
                    System.out.println("  [Pila] Pila vacía tras deshacer: " + historialPedidos.isEmpty());
                }

                System.out.println("  [Cola] Clientes en espera tras atención: " + cola.size());
            } else {
                System.out.println("  [Cola] No hay clientes en espera.");
            }

            Thread.sleep(2000);
        }

        // ---------- RESUMEN FINAL ----------
        System.out.println("\n========== FIN DE JORNADA ==========");
        System.out.println("Total de clientes atendidos: " + totalPersonas);
        System.out.println("Clientes aún en cola: " + cola.size());

        System.out.println("\n[Pila] Historial de pedidos pendientes:");
        if (historialPedidos.isEmpty()) {
            System.out.println("  No hay pedidos en el historial.");
        } else {
            while (!historialPedidos.isEmpty()) {
                System.out.println("  - " + historialPedidos.pop());
            }
        }

        System.out.println("\n[Catálogo] Menú final del día:");
        catalogo.mostrarCatalogo();
    }
}