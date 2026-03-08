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

// Clase para el punto control de estaciones(2)
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
            System.out.println("Desbordamiento: Estaciones llenas.");
            return;
        }
        rear = (rear + 1) % capacidad;
        estaciones[rear] = estacion;
        size++;
    }

    public String dequeue() {
        if (size == 0) {
            System.out.println("Error: No hay estaciones.");
            return null;
        }
        String dato = estaciones[front];
        front = (front + 1) % capacidad;
        size--;
        return dato;
    }

    public void mostrarEstado() {
        System.out.print("Estaciones en servicio: [ ");
        for (int i = 0; i < size; i++) {
            System.out.print(estaciones[(front + i) % capacidad] + " ");
        }
        System.out.println("]");
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Queue<Integer> cola = new LinkedList<>();
        Random rand = new Random();
        int totalPersonas = 0;
        int limite = 60;
        int ventanilla = 0;
        Stack<String> historialPedidos = new Stack<>();

        // Crear las 3 estaciones requeridas que son bebidas, comida caliente y snacks
        ColaCircularEstaciones estacionesCC = new ColaCircularEstaciones(3);
        estacionesCC.enqueue("Bebidas");
        estacionesCC.enqueue("Comida Caliente");
        estacionesCC.enqueue("Snacks");

        while (totalPersonas < limite) {
            // 1. Llegan clientes (Aquí solo entran a la fila)
            int llegadas = rand.nextInt(1, 4);
            for (int i = 0; i < llegadas; i++) {
                if (totalPersonas < limite) {
                    totalPersonas++;
                    cola.add(totalPersonas);
                    System.out.println("Cliente #" + totalPersonas + " entró a la cola.");
                }
            }

            System.out.println("Clientes esperando: " + cola.size());

            estacionesCC.mostrarEstado(); // Mostrar el estado actual de las estaciones

            // Siguiente en ser atendido
            if (!cola.isEmpty()) {
                System.out.println("Siguiente en ser atendido: Cliente #" + cola.peek());
            }

            Thread.sleep(4000);

            // 2. Cajero atiende al primero (AQUÍ ES DONDE VA LA PILA)
            if (!cola.isEmpty()) {
                ventanilla++;
                int atendido = cola.poll(); // Aquí sacamos al cliente de la cola
                System.out.println("Cliente #" + atendido + " está siendo atendido en ventanilla " + ventanilla);

                // --- AGREGADO CORRECTAMENTE: Punto 3 (Pila) ---
                String pedidoActual = "Pedido_Cliente_" + atendido;
                historialPedidos.push(pedidoActual); // Se guarda al ser procesado
                System.out.println("Historial: Se registró " + pedidoActual);
                
                // Simular un "Deshacer" (Punto 3: operación de extracción)
                if (rand.nextInt(10) < 2) { 
                    System.out.println("¡ERROR! El cajero deshizo el pedido: " + historialPedidos.pop());
                }
                // ----------------------------------------------

                int atendido = cola.poll();
                
                // --- Integración del Punto 2 ---
                String estacionActual = estacionesCC.dequeue(); 
                System.out.println("Cliente #" + atendido + " esta siendo atendido en " + estacionActual + " en ventanilla " + ventanilla);
                estacionesCC.enqueue(estacionActual);
                // -------------------------------
                
                System.out.println("Clientes en espera tras llamado: " + cola.size());
            } else {
                System.out.println("No hay clientes en espera.");
            }

            Thread.sleep(3000);
        }

        System.out.println("Se alcanzó el límite de " + limite + " clientes.");
        
        System.out.println("Se alcanzo el limite de " + limite + " clientes.");
    }
}