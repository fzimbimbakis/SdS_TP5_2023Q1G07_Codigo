import matplotlib.pyplot as plt
import numpy as np

radio = 0.0098440453
density = 3300
g_sqrt = 0.05 ** 0.5


def beverloo(x, c):
    aux = x - c * radio
    return density * g_sqrt * (aux ** 1.5)


def beverlooError(Qs, Ds, c):
    result = 0
    for q, d in zip(Qs, Ds):
        b = beverloo(d, c)
        result = result + (q - b) ** 2
    return result


def get_Qs(path):
    # Leer el archivo de tiempos
    with open(path) as file:
        tiempos_str = file.read()

    # Convertir los tiempos a una lista de números
    Qs = list(map(float, tiempos_str.split('\n')))
    # data = np.array(Qs)
    return Qs


def times_graph(directory):
    Cs = [num / 100.0 for num in range(0, 200, 1)]
    Qs = get_Qs('../resources/caudals_D1.txt')
    beverloo_err = [beverlooError(Qs, [0.03 + 2 * radio, 0.04 + 2 * radio, 0.05 + 2 * radio, 0.06 + 2 * radio], c) for c
                    in Cs]
    print(Qs)
    plt.plot(Cs, beverloo_err, color='black')

    c = Cs[np.argmin(beverloo_err)]
    print(c)
    print(beverloo_err[np.argmin(beverloo_err)])
    plt.scatter(Cs[np.argmin(beverloo_err)], beverloo_err[np.argmin(beverloo_err)], color='red')

    plt.xlabel('Parámetro libre c')
    plt.ylabel('Error')

    plt.savefig(directory + 'beverloo_err.png')
    plt.clf()

    x = [0.001 * i for i in range(20, 100)]
    y = [beverloo(x_i, c) for x_i in x]

    plt.plot(x, y, color='black', label='Beverloo')
    plt.scatter([0.03 + 2 * radio, 0.04 + 2 * radio, 0.05 + 2 * radio, 0.06 + 2 * radio], Qs, color='red',
                label='Resultados')
    plt.ylabel('Caudal (bolas/s)')
    plt.xlabel('Apertura (m)')
    plt.legend()
    plt.savefig(directory + 'beverloo.png')
    plt.clf()


if __name__ == "__main__":
    times_graph('../resources/graphs/')