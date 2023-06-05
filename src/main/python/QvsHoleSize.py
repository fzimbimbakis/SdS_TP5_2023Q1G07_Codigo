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


##TODO: borrar los tiempos del transitorio INICIAL de cada txt
def times_graph(directory):
    x1 = get_times('../resources/times1.txt')
    x2 = get_times('../resources/times2.txt')
    x3 = get_times('../resources/times3.txt')
    x4 = get_times('../resources/times4.txt')

    Q_list = []
    error_list = []

    plt.xlabel('Ancho de apertura de salida')
    plt.ylabel('Caudal')

    for x, label in zip([x1, x2, x3, x4], ['3', '4', '5', '6']):

        Q = len(x)/x[-1]

        Q_list.append(Q)
        x_mean = np.mean(x)

        f = []
        for i in range(len(x)):
            f.append(Q*x[i]) ## dudoso

        S = np.sqrt(np.sum((x-f)**2)/(len(x)/2))

        Sxx = np.sum((x - x_mean)**2)

        error = S / np.sqrt(Sxx)

        error_list.append(error)
        plt.errorbar(int(label), Q, yerr=error, label="D = " + label)

    plt.savefig(directory + 'QvsHoleSize.png')
    plt.clf()


if __name__ == "__main__":
    times_graph('../resources/graphs/')