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
    x1 = get_times('../resources/times_F1.txt')
    x2 = get_times('../resources/times_F2.txt')
    x3 = get_times('../resources/times_F3.txt')
    x4 = get_times('../resources/times_F4.txt')
    x5 = get_times('../resources/times_F5.txt')
    x6 = get_times('../resources/times_F6.txt')
    x7 = get_times('../resources/times_F7.txt')

    # Q_list = get_Qs('../resources/caudals2.txt')
    error_list = []

    plt.xlabel('Frecuencia (Hz)')
    plt.ylabel('Caudal')

    for x, label in zip([x1, x2, x3, x4, x5, x6, x7], ['5', '10', '15', '20', '30', '40', '50']):

        Q = (len(x))/(x[-1]-x[0])

        x_mean = np.mean(x)

        f = []
        for i in range(len(x)):
            f.append(Q*x[i])  ## dudoso

        S = np.sqrt(np.sum((x-f)**2)/(len(x)-2))

        Sxx = np.sum((x - x_mean)**2)

        error = S / np.sqrt(Sxx)

        error_list.append(error)
        plt.scatter(int(label), Q)
        plt.errorbar(int(label), Q, yerr=error, label="w = " + label)

    plt.savefig(directory + 'QvsFrecuency.png')
    plt.clf()

    # print(Q_list)


if __name__ == "__main__":
    times_graph('../resources/graphs/')