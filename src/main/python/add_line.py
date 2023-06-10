D = 0.03
name = 'output_F_20.0'


def procesar_archivo(nombre_archivo_entrada, nombre_archivo_salida):
    # Abrir el archivo de entrada en modo lectura
    with open(nombre_archivo_entrada, 'r') as archivo_entrada:

        # Procesar el archivo de entrada línea por línea
        while True:

            # Leer la primera línea que contiene el número n
            n = int(archivo_entrada.readline().strip())

            # Leer una línea extra que no se utilizará
            archivo_entrada.readline()

            # Si no hay más líneas en el archivo de entrada, salir del ciclo
            if not n:
                break

            # Convertir el número n+10 a entero
            numero = int(n) + int((0.1 - D / 2) * 1000 * 2)

            # Crear un nuevo archivo de salida para cada iteración
            with open(nombre_archivo_salida, 'a') as archivo_salida:
                # Escribir el número n+10 en una nueva línea
                archivo_salida.write(str(numero) + '\n')

                # Escribir una línea vacía
                archivo_salida.write('\n')

                limits = [(0, 0.1 - D / 2), (0.1 + D / 2, 0.2)]
                for l in limits:
                    for x in range(int(l[0] * 1000), int(l[1] * 1000)):
                        archivo_salida.write(f'{x / 1000} 0.06999999999999999 0.0 0.0 1.0E-3 0 0 0' + '\n')

                # Escribir las siguientes n líneas del archivo de entrada
                for _ in range(int(n)):
                    linea = archivo_entrada.readline()
                    archivo_salida.write(linea)


# Ejemplo de uso
nombre_archivo_entrada = f'../resources/{name}_1.xyz'
nombre_archivo_salida = f'../resources/{name}_2.xyz'

procesar_archivo(nombre_archivo_entrada, nombre_archivo_salida)
