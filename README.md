# Sistema de Monitorización de Temperatura con Blockchain

### Descripción

Este proyecto simula un sistema de control de temperatura basado en una arquitectura cliente-servidor. El objetivo es garantizar que los datos registrados no puedan modificarse sin ser detectados.

Un cliente actúa como sensor y envía lecturas de temperatura a un servidor. El servidor valida los datos, comprueba si superan un límite de seguridad y, si son correctos, los guarda en una base de datos.

Para asegurar la integridad, cada registro se certifica mediante una blockchain sencilla. Cada bloque almacena el hash del registro y el hash del bloque anterior, utilizando SHA-256. Si un dato es modificado, la cadena se rompe y la manipulación se detecta automáticamente.

---

### Componentes del sistema

- **Cliente**: Envía la temperatura al servidor.  
- **Servidor**: Valida la cadena, controla el límite de seguridad y gestiona el almacenamiento.  
- **Base de datos (MySQL)**: Guarda el histórico de temperaturas.  
- **Blockchain**: Garantiza que los registros no han sido alterados.  

---

### Funcionamiento básico

1. El cliente envía una temperatura.  
2. El servidor valida la blockchain.  
3. Si la temperatura es menor de 50°C, se guarda en la base de datos.  
4. Se genera un hash del registro.  
5. Se crea un nuevo bloque enlazado al anterior.  
6. El hash del bloque se guarda en la base de datos.  

---

### Seguridad

Si la temperatura supera los **50°C**, el servidor envía el mensaje:

SISTEMA_APAGADO

y detiene el proceso.

La integridad se garantiza mediante la fórmula:

SHA256(index + timestamp + dataHash + previousHash)

Cualquier modificación en la base de datos rompe la cadena y es detectada automáticamente.
