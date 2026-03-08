# 🍽️ Sistema de Gestión — Cafetería Universitaria
### Documentación Técnica | Java + Estructuras de Datos Dinámicas

---

## 📋 Descripción General

Este sistema automatiza los procesos internos de una cafetería universitaria usando **cuatro estructuras de datos dinámicas**. Cada una resuelve un problema específico de operación:

| Punto | Estructura | Problema que resuelve |
|-------|-----------|----------------------|
| 1 | Cola simple (`Queue`) | Orden justo de atención a clientes |
| 2 | Cola circular (arreglo) | Rotación equitativa entre estaciones |
| 3 | Pila (`Stack`) | Historial y deshacer pedidos |
| 4 | Lista enlazada abierta | Catálogo dinámico de productos |

---

## 🔄 Flujo General del Sistema

```
INICIO
  │
  ├─── [PUNTO 4] Se carga el catálogo de productos (Lista Enlazada)
  │         Café → Torta de Jamón → Papas Fritas
  │         Se agregan, eliminan y actualizan productos
  │
  └─── BUCLE PRINCIPAL (mientras totalPersonas < límite)
            │
            ├─── [PUNTO 1] Llegan 1-3 clientes → se agregan al final de la Cola
            │
            ├─── [PUNTO 2] Se muestra el estado actual de las Estaciones (Cola Circular)
            │
            ├─── [PUNTO 1] Se consulta quién es el próximo cliente (peek)
            │
            ├─── [PUNTO 1] Se saca al primer cliente de la Cola (poll)
            │
            ├─── [PUNTO 2] Se asigna estación circular → se rota (dequeue + enqueue)
            │
            ├─── [PUNTO 3] Se registra el pedido en la Pila (push)
            │
            └─── [PUNTO 3] 20% de probabilidad → se deshace el pedido (pop)

FIN → Resumen de clientes, historial de pila y catálogo final
```

---

## 📦 Importaciones

```java
import java.util.LinkedList;  // Implementación interna de Queue
import java.util.Queue;       // Interfaz para la cola de turnos (Punto 1)
import java.util.Random;      // Genera números aleatorios para simular llegadas
import java.util.Stack;       // Pila para historial de pedidos (Punto 3)
```

---

## 📌 Punto 1 — Gestión de Turnos (Cola Simple)

### ¿Qué es una Cola?
Una cola es una estructura **FIFO** (First In, First Out): el primero en entrar es el primero en salir. Igual que una fila real en una ventanilla.

```
Entrada →  [ C5 | C4 | C3 | C2 | C1 ]  → Salida
```

### Declaración e inicialización

```java
Queue<Integer> cola = new LinkedList<>();
// Queue<Integer> → interfaz que define las operaciones de cola
// new LinkedList<>() → implementación concreta que usa nodos enlazados internamente
// Integer → cada elemento es el número de turno del cliente

int totalPersonas = 0;   // Contador acumulado de todos los clientes que llegaron
int limite = 10;         // Tope máximo de clientes a simular
int ventanillaNum = 0;   // Número de ventanilla actual (se incrementa por atención)
Random rand = new Random(); // Objeto para generar valores aleatorios
```

### Agregar clientes a la cola

```java
int llegadas = rand.nextInt(1, 4);
// Genera un número aleatorio entre 1 y 3 (el 4 es exclusivo)
// Simula que llegan entre 1 y 3 clientes por ronda

for (int i = 0; i < llegadas && totalPersonas < limite; i++) {
    totalPersonas++;         // Incrementa el contador global de clientes
    cola.add(totalPersonas); // Agrega el número de turno al FINAL de la cola
    System.out.println("  [Cola] Cliente #" + totalPersonas + " entró a la fila.");
}
```

### Consultar sin eliminar (peek)

```java
if (!cola.isEmpty()) {
    System.out.println("  [Cola] Próximo a ser atendido: Cliente #" + cola.peek());
}
// cola.isEmpty() → verifica si hay elementos antes de consultar (evita error)
// cola.peek()    → devuelve el primer elemento SIN sacarlo de la cola
```

### Atender y sacar al cliente (poll)

```java
int atendido = cola.poll();
// cola.poll() → extrae y devuelve el primer elemento de la cola (el más antiguo)
// Si la cola estuviera vacía, devolvería null (por eso se verifica isEmpty() antes)

System.out.println("  [Cola] Clientes en espera tras atención: " + cola.size());
// cola.size() → devuelve cuántos elementos quedan actualmente en la cola
```

---

## 📌 Punto 2 — Control de Estaciones (Cola Circular)

### ¿Qué es una Cola Circular?
Es una cola implementada sobre un **arreglo fijo** donde el final se conecta con el inicio. Evita desperdiciar espacio y permite rotación continua sin perder posiciones.

```
Índices del arreglo:   [ 0 ][ 1 ][ 2 ]
Contenido:           Bebidas | C.Caliente | Snacks
                        ↑ front              ↑ rear
```

### Atributos de la clase

```java
class ColaCircularEstaciones {
    private String[] estaciones; // Arreglo que almacena los nombres de las estaciones
    private int front;           // Índice del primer elemento (frente de la cola)
    private int rear;            // Índice del último elemento insertado
    private int size;            // Cantidad actual de elementos en la cola
    private int capacidad;       // Tamaño máximo del arreglo
```

### Constructor

```java
public ColaCircularEstaciones(int n) {
    this.capacidad = n;              // Define el tamaño máximo (se crea con 3)
    this.estaciones = new String[capacidad]; // Crea el arreglo de ese tamaño
    this.front = 0;                  // El frente empieza en la posición 0
    this.rear = -1;                  // -1 indica que aún no hay elementos
    this.size = 0;                   // Cola vacía al inicio
}
```

### Insertar estación (enqueue)

```java
public void enqueue(String estacion) {
    if (size == capacidad) {
        // Control de desbordamiento: no se puede agregar si el arreglo está lleno
        System.out.println("  [Cola Circular] Desbordamiento: Estaciones llenas.");
        return;
    }
    rear = (rear + 1) % capacidad;
    // El operador % hace que rear "dé vuelta" al llegar al final del arreglo
    // Ejemplo: rear=2, capacidad=3 → (2+1)%3 = 0 (vuelve al inicio)
    estaciones[rear] = estacion; // Guarda la estación en esa posición
    size++;                      // Incrementa el contador de elementos
}
```

### Extraer estación (dequeue)

```java
public String dequeue() {
    if (size == 0) {
        // Control de subdesbordamiento: no se puede extraer si está vacía
        System.out.println("  [Cola Circular] Error: No hay estaciones.");
        return null;
    }
    String dato = estaciones[front]; // Guarda el valor del frente antes de moverlo
    front = (front + 1) % capacidad; // Avanza el frente de forma circular
    size--;                          // Reduce el contador
    return dato;                     // Devuelve la estación extraída
}
```

### Rotación cíclica en la simulación

```java
String estacionActual = estaciones.dequeue(); // Saca la estación del frente
estaciones.enqueue(estacionActual);           // La reinserta al final
// Esto garantiza que siempre haya 3 estaciones y se turnen en orden:
// Ronda 1: Bebidas → Ronda 2: Comida Caliente → Ronda 3: Snacks → Ronda 4: Bebidas...
```

### Mostrar estado

```java
public void mostrarEstado() {
    System.out.print("  [Estaciones] En servicio: [ ");
    for (int i = 0; i < size; i++) {
        System.out.print(estaciones[(front + i) % capacidad] + " ");
        // (front + i) % capacidad → navega circularmente desde el frente
    }
    System.out.println("]");
}
```

---

## 📌 Punto 3 — Historial de Pedidos (Pila)

### ¿Qué es una Pila?
Una pila es una estructura **LIFO** (Last In, First Out): el último en entrar es el primero en salir. Como una pila de platos.

```
         ← tope (último ingresado)
[ Pedido_C5 ]
[ Pedido_C3 ]
[ Pedido_C1 ]
         ← fondo
```

### Declaración

```java
Stack<String> historialPedidos = new Stack<>();
// Stack<String> → pila que almacena cadenas de texto
// Cada elemento es el identificador de un pedido procesado
```

### Registrar un nuevo pedido (push)

```java
String pedido = "Pedido_C" + atendido + "_" + estacionActual.replace(" ", "");
// Construye un identificador único: ej. "Pedido_C3_ComidaCaliente"
// .replace(" ", "") elimina los espacios del nombre de la estación

historialPedidos.push(pedido);
// push() → apila el pedido en el tope de la pila
// Si la pila estaba vacía, este se convierte en el único elemento (caso especial de inicio)
```

### Consultar el tope sin extraer (peek)

```java
System.out.println("  [Pila] Pedido en tope: " + historialPedidos.peek());
// peek() → devuelve el elemento en el tope SIN sacarlo
// Útil para verificar el último pedido registrado sin modificar el historial
```

### Deshacer el último pedido (pop)

```java
if (!historialPedidos.isEmpty() && rand.nextInt(10) < 2) {
    // isEmpty() → valida que la pila no esté vacía antes de extraer (evita EmptyStackException)
    // rand.nextInt(10) < 2 → condición aleatoria con ~20% de probabilidad (valores 0 y 1)
    
    System.out.println("  [Pila] ¡ERROR! Deshaciendo pedido: " + historialPedidos.pop());
    // pop() → extrae y devuelve el elemento del tope (el pedido más reciente)
    
    System.out.println("  [Pila] Pila vacía tras deshacer: " + historialPedidos.isEmpty());
    // Informa si la pila quedó vacía después del deshacer
}
```

### Vaciar pila en el resumen final

```java
while (!historialPedidos.isEmpty()) {
    System.out.println("  - " + historialPedidos.pop());
    // Se recorre y extrae elemento por elemento hasta vaciar la pila
    // Muestra todos los pedidos que quedaron sin ser deshecho al cerrar el turno
}
```

---

## 📌 Punto 4 — Catálogo de Productos (Lista Enlazada Abierta)

### ¿Qué es una Lista Enlazada?
Es una secuencia de nodos donde **cada nodo apunta al siguiente**. No tiene tamaño fijo y permite insertar o eliminar en cualquier posición sin desplazar elementos.

```
[Agua] → [Café] → [Jugo Natural] → [Torta de Jamón] → null
  ↑
cabeza
```

### Clase Nodo (Producto)

```java
class Producto {
    String nombre;      // Dato: nombre del producto
    double precio;      // Dato: precio del producto
    Producto siguiente; // Enlace: referencia al siguiente nodo en la lista

    public Producto(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
        this.siguiente = null; // Al crearse, el nodo no apunta a ningún siguiente
    }
}
```

### Clase Lista (CatalogoProductos)

```java
class CatalogoProductos {
    private Producto cabeza; // Referencia al primer nodo de la lista

    public CatalogoProductos() {
        this.cabeza = null; // Lista vacía al iniciar
    }
```

### Insertar al inicio

```java
public void insertarAlInicio(String nombre, double precio) {
    Producto nuevo = new Producto(nombre, precio); // Crea el nuevo nodo
    nuevo.siguiente = cabeza; // El nuevo nodo apunta al que antes era el primero
    cabeza = nuevo;           // La cabeza ahora apunta al nuevo nodo
}
// Antes: [Café] → [Torta]
// Después: [Agua] → [Café] → [Torta]
```

### Insertar al final

```java
public void insertarAlFinal(String nombre, double precio) {
    Producto nuevo = new Producto(nombre, precio);
    if (cabeza == null) {
        cabeza = nuevo; // Caso especial: lista vacía, el nuevo es el único nodo
    } else {
        Producto actual = cabeza;
        while (actual.siguiente != null) {
            actual = actual.siguiente; // Recorre hasta encontrar el último nodo
        }
        actual.siguiente = nuevo; // El último nodo ahora apunta al nuevo
    }
}
```

### Insertar en posición específica

```java
public void insertarEnPosicion(String nombre, double precio, int posicion) {
    if (posicion <= 0) {
        insertarAlInicio(nombre, precio); // Si posición es 0 o negativa, va al inicio
        return;
    }
    Producto nuevo = new Producto(nombre, precio);
    Producto actual = cabeza;
    for (int i = 0; i < posicion - 1 && actual != null; i++) {
        actual = actual.siguiente; // Avanza hasta el nodo ANTERIOR a la posición deseada
    }
    if (actual == null) {
        insertarAlFinal(nombre, precio); // Si se pasó del límite, inserta al final
    } else {
        nuevo.siguiente = actual.siguiente; // El nuevo apunta al nodo que seguía
        actual.siguiente = nuevo;           // El nodo anterior apunta al nuevo
    }
}
// Ejemplo insertar en posición 2:
// Antes:  [Agua] → [Café] → [Torta]
// Después: [Agua] → [Café] → [Jugo Natural] → [Torta]
```

### Eliminar por nombre

```java
public void eliminar(String nombre) {
    if (cabeza == null) { return; } // Lista vacía: no hay nada que eliminar

    if (cabeza.nombre.equals(nombre)) {
        cabeza = cabeza.siguiente; // Caso especial: eliminar el primer nodo
        return;                    // La cabeza salta al siguiente nodo directamente
    }

    Producto actual = cabeza;
    while (actual.siguiente != null && !actual.siguiente.nombre.equals(nombre)) {
        actual = actual.siguiente; // Busca el nodo ANTERIOR al que se quiere eliminar
    }
    if (actual.siguiente == null) {
        // No encontró el nodo con ese nombre
    } else {
        actual.siguiente = actual.siguiente.siguiente;
        // "Salta" el nodo a eliminar: el anterior apunta al que seguía después del eliminado
    }
}
// Antes:  [Agua] → [Café] → [Papas Fritas] → [Torta]
// Después: [Agua] → [Café] → [Torta]
```

### Actualizar precio

```java
public void actualizarPrecio(String nombre, double nuevoPrecio) {
    Producto actual = cabeza;
    while (actual != null) {             // Recorre toda la lista
        if (actual.nombre.equals(nombre)) {
            actual.precio = nuevoPrecio; // Modifica el precio del nodo encontrado
            return;
        }
        actual = actual.siguiente;       // Avanza al siguiente nodo
    }
}
```

### Recorrer e imprimir el catálogo

```java
public void mostrarCatalogo() {
    if (cabeza == null) {
        System.out.println("  [Catálogo] No hay productos disponibles.");
        return;
    }
    Producto actual = cabeza;
    while (actual != null) {
        System.out.print(actual.nombre + "($" + actual.precio + ")");
        if (actual.siguiente != null) System.out.print(" -> "); // Separador visual
        actual = actual.siguiente; // Avanza al siguiente nodo
    }
}
// Salida: Agua($10.0) -> Café($18.0) -> Jugo Natural($25.0) -> Torta de Jamón($35.0)
```

---

## 🖥️ Salida esperada en consola

```
========== CATÁLOGO INICIAL ==========
  [Catálogo] Producto agregado al final: Café
  [Catálogo] Producto agregado al final: Torta de Jamón
  [Catálogo] Producto agregado al final: Papas Fritas
  [Catálogo] Producto agregado al inicio: Agua
  [Catálogo] Producto agregado en posición 2: Jugo Natural
  [Catálogo] Productos disponibles: Agua($10.0) -> Café($15.0) -> Jugo Natural($25.0) -> Torta de Jamón($35.0) -> Papas Fritas($20.0)
  [Catálogo] Precio actualizado: Café -> $18.0
  [Catálogo] Producto eliminado: Papas Fritas
  [Catálogo] Productos disponibles: Agua($10.0) -> Café($18.0) -> Jugo Natural($25.0) -> Torta de Jamón($35.0)

========== INICIO DE ATENCIÓN ==========

--- Nueva ronda ---
  [Cola] Cliente #1 entró a la fila.
  [Cola] Cliente #2 entró a la fila.
  [Cola] Clientes esperando: 2
  [Estaciones] En servicio: [ Bebidas Comida Caliente Snacks ]
  [Cola] Próximo a ser atendido: Cliente #1
  [Ventanilla 1] Cliente #1 atendido en estación: Bebidas
  [Pila] Pedido registrado: Pedido_C1_Bebidas
  [Pila] Pedido en tope: Pedido_C1_Bebidas
  [Cola] Clientes en espera tras atención: 1
  ...

========== FIN DE JORNADA ==========
Total de clientes atendidos: 10
Clientes aún en cola: 2
```

---

## 📊 Comparativa de Estructuras Usadas

| Característica | Cola (P1) | Cola Circular (P2) | Pila (P3) | Lista Enlazada (P4) |
|---|---|---|---|---|
| Orden | FIFO | Circular | LIFO | Libre |
| Tamaño | Dinámico | Fijo (3) | Dinámico | Dinámico |
| Inserción | Al final | Al final (circular) | Al tope | Cualquier posición |
| Extracción | Del frente | Del frente (circular) | Del tope | Cualquier posición |
| Caso especial | Cola vacía | Llena / Vacía | Pila vacía | Lista vacía / un nodo |

---

*Documentación generada para el proyecto de Cafetería Universitaria — Estructuras de Datos en Java*