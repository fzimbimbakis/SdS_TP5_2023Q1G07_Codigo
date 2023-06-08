import matplotlib.pyplot as plt
import numpy as np

def get_times(path):
    # Leer el archivo de tiempos
    with open(path) as file:
        tiempos_str = file.read()

    # Convertir los tiempos a una lista de números
    tiempos = list(map(float, tiempos_str.split('\n')))
    data = np.array(tiempos)

    return data

def times_graph(directory):
    x1 = get_times('../resources/times13.txt')
    x2 = get_times('../resources/times14.txt')
    x3 = get_times('../resources/times15.txt')
    x4 = get_times('../resources/times16.txt')

    for x, label in zip([x1, x2, x3, x4], ['3', '4', '5', '6']):
        # Calcular los conteos de eventos en cada intervalo
        conteos, bordes = np.histogram(x, bins=1000)

        # Calcular los conteos acumulativos
        conteos_acumulados = np.cumsum(conteos)

        # Graficar el histograma acumulativo
        plt.step(bordes[:-1], conteos_acumulados, where='post', label=label+ " cm")

    # Etiquetas de los ejes
    plt.xlabel('Tiempo (s)')
    plt.ylabel('Cantidad de partículas que salieron')
    plt.xlim(0, 1000)
    plt.legend()

    plt.savefig(directory + 'histogram_D.png')
    plt.clf()


if __name__ == "__main__":
    times_graph('../resources/graphs/')