Este proyecto simula un sistema de control de temperatura basado en una arquitectura cliente-servidor, cuyo objetivo es garantizar que los datos no puedan ser modificados sin ser detectados.

Un cliente actúa como sensor, enviando lecturas de temperatura a un servidor central. El servidor valida los datos recibidos, comprueba si la temperatura supera un umbral de seguridad y, en caso contrario, registra la información.

Para asegurar la integridad de los datos, el sistema utiliza una blockchain sencilla, donde cada registro se almacena en un bloque enlazado criptográficamente con el anterior mediante hashes SHA-256. De esta forma, cualquier intento de modificar un dato previamente almacenado rompe la cadena y es detectado automáticamente.

Además, el proyecto separa claramente las responsabilidades:

- El sensor únicamente envía datos.
- El servidor gestiona la lógica de control y seguridad.
- La base de datos (simulada) almacena el historial.
- La blockchain garantiza que los registros no han sido alterados.
