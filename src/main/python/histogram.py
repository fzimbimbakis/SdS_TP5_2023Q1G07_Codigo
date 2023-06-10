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
    x1 = get_times('../resources/times_F1.txt')
    x2 = get_times('../resources/times_F2.txt')
    x3 = get_times('../resources/times_F3.txt')
    x4 = get_times('../resources/times_F4.txt')
    x5 = get_times('../resources/times_F5.txt')
    x6 = get_times('../resources/times_F6.txt')
    x7 = get_times('../resources/times_F7.txt')

    for x, label in zip([x1, x4, x5, x7], ['5', '20', '30', '50']):
        # Calcular los conteos de eventos en cada intervalo
        conteos, bordes = np.histogram(x, bins=1000)

        # Calcular los conteos acumulativos
        conteos_acumulados = np.cumsum(conteos)

        # Graficar el histograma acumulativo
        plt.step(bordes[:-1], conteos_acumulados, where='post', label=label + " Hz")

    # Etiquetas de los ejes
    plt.xlabel('Tiempo (s)')
    plt.ylabel('Cantidad de partículas que salieron')
    plt.legend()

    ax_zoom = plt.axes([0.6, 0.2, 0.25, 0.25])  # Posición y tamaño del recuadro
    for x, label in zip([x1, x4, x5, x7], ['5', '20', '30', '50']):
        # Calcular los conteos de eventos en cada intervalo
        conteos, bordes = np.histogram(x, bins=1000)

        # Calcular los conteos acumulativos
        conteos_acumulados = np.cumsum(conteos)

        # Agregar recuadro con zoom
        ax_zoom.step(bordes[:-1], conteos_acumulados, where='post')
        ax_zoom.set_xlim(950, 1000)
        ax_zoom.set_ylim(4000, 4700)

    plt.savefig(directory + 'histogram.png')
    plt.clf()


if __name__ == "__main__":
    times_graph('../resources/graphs/')
