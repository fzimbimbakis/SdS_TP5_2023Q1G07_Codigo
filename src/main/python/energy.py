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
    x1 = get_times('../resources/energy1.txt')
    x2 = get_times('../resources/energy2.txt')
    x3 = get_times('../resources/energy3.txt')
    x4 = get_times('../resources/energy4.txt')
    x5 = get_times('../resources/energy5.txt')
    x6 = get_times('../resources/energy6.txt')
    x7 = get_times('../resources/energy7.txt')

    for x, label in zip([x1, x2, x3, x4, x5, x6, x7], ['5', '10', '15', '20', '30', '40', '50']):
        plt.plot([i * 0.01 for i in range(1000)], np.array(x) * 10000,  label=label + " Hz")

    # Etiquetas de los ejes
    plt.xlabel('Tiempo (s)')
    plt.ylabel('Energía (J)')

    plt.yscale('log')
    plt.legend()

    plt.savefig(directory + 'energy.png')
    plt.clf()

    mean_energy = []
    mean_energy_err = []
    for x in [x1, x2, x3, x4, x5, x6, x7]:
        array = x[-800:]
        mean_energy.append(np.mean(np.array(array) * 10000))
        mean_energy_err.append(np.std(np.array(array) * 10000)/((len(array))**0.5))

    plt.errorbar([5, 10, 15, 20, 30, 40, 50], mean_energy, yerr=mean_energy_err, marker='o', color='black', capsize=3)

    plt.ylabel('Energía promedio (J)')
    plt.xlabel('Frecuencia angular (Hz)')

    plt.savefig(directory + 'mean_energy.png')
    plt.clf()


if __name__ == "__main__":
    times_graph('../resources/graphs/')