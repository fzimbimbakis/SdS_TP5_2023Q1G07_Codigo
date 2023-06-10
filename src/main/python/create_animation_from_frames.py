import glob
import imageio
import os

# Ruta a la carpeta que contiene las imágenes
name = 'F20'
animation_frames_folder = f'../resources/animations/{name}/frames/'
frames_files_name_format = 'frame*.png'
animation_name = f'../resources/animations/{name}/{name}.mp4'


def main():
    # Obtener la lista de archivos de imagen en la carpeta
    images = sorted(glob.glob(os.path.join(animation_frames_folder, frames_files_name_format)))

    # Crear la animación con las imágenes en la lista
    with imageio.get_writer(animation_name, fps=10) as writer:
        for image in images:
            # Leer la imagen
            img = imageio.imread(image)

            # Añadir la imagen al archivo de video
            writer.append_data(img)

    print('La animación se ha guardado correctamente en ' + animation_name)


if __name__ == "__main__":
    main()
