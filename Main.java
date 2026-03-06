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

class Main {
    public static void main(String[] args) {

    }
}