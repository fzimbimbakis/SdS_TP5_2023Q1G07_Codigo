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

def get_Qs(path):
    # Leer el archivo de tiempos
    with open(path) as file:
        tiempos_str = file.read()

    # Convertir los tiempos a una lista de números
    Qs = list(map(float, tiempos_str.split('\n')))
    #data = np.array(Qs)
    return Qs


def times_graph(directory):
    x1 = get_times('../resources/times5.txt')
    x2 = get_times('../resources/times8.txt')
    x3 = get_times('../resources/times9.txt')
    x4 = get_times('../resources/times10.txt')

    Q_list = get_Qs('../resources/caudals2.txt')
    error_list = []

    plt.xlabel('Ancho de apertura de salida (cm)')
    plt.ylabel('Caudal')

    for x, label, Q in zip([x1, x2, x3, x4], ['3', '4', '5', '6'], Q_list):

        #Q = len(x)/x[-1]

        #Q_list.append(Q)
        x_mean = np.mean(x)

        f = []
        for i in range(len(x)):
            f.append(Q*x[i]) ## dudoso

        S = np.sqrt(np.sum((x-f)**2)/(len(x)-2))

        Sxx = np.sum((x - x_mean)**2)

        error = S / np.sqrt(Sxx)

        error_list.append(error)
        plt.scatter(int(label), Q)
        plt.errorbar(int(label), Q, yerr=error, label="D = " + label)

    plt.savefig(directory + 'QvsHoleSize.png')
    plt.clf()


if __name__ == "__main__":
    times_graph('../resources/graphs/')