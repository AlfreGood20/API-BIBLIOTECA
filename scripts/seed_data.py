import requests
from faker import Faker
import random

# LIBRERIA DE DATOS FAKE
fake = Faker('es_MX')

# BASE URL DE LA API EN LOCAL
base_url = 'http://localhost:8080/api'

# PARA PRUEBA UN TOKEN DE 1 HORA COMO ADMINISTRADOR
token_access = 'eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIwNDk4MTViNi1mZThjLTRmYzYtODdhYS01NzFkMDY3MjQ4OGQiLCJub21icmVfY29tcGxldG8iOiJTVVBFUiBBRE1JTiBPTkUiLCJyb2wiOlt7ImF1dGhvcml0eSI6IlJPTEVfQURNSU5JU1RSQURPUiJ9XSwic3ViIjoic3VwZXJBZG1pbkBtZS5jb20iLCJpYXQiOjE3ODYwNTcxNzAsImV4cCI6MTc4NjA2MDc3MH0.0bz4Rt9l6ahbHGW9IThBoOLnWdWCw-7LvYcYHirPnc8'

headers = {"Authorization": f"Bearer {token_access}"}

def poblar_idiomas_libros(cantidad=50):

    for _ in range(cantidad):
        response = requests.post(
            url=f'{base_url}/idiomas',
            json={'nombre': fake.language_name()},
            headers=headers
        )
        print(response.status_code, response.json())

def poblar_categorias_libros():

    # 50 CATEGORIAS DE LIBROS
    categorias = ["Ficción","No ficción","Ciencia ficción","Fantasía","Terror","Misterio","Suspenso","Romance","Drama","Aventura","Historia","Biografía","Autobiografía","Ensayo",
        "Poesía","Teatro","Filosofía","Psicología","Sociología","Política","Economía","Derecho","Ciencias naturales","Física","Química","Biología","Matemáticas","Astronomía","Medicina",
        "Tecnología","Informática","Programación","Ingeniería","Arquitectura","Arte","Música","Cine","Fotografía","Gastronomía","Viajes","Deportes","Salud y bienestar","Autoayuda",
        "Negocios","Marketing","Educación","Pedagogía","Literatura infantil","Literatura juvenil","Cómic y novela gráfica",
    ]

    for categoria in categorias:
        response = requests.post(
            url=f'{base_url}/categorias',
            json={ 'nombre':categoria},
            headers=headers
        )
        print(response.status_code, response.json())

def poblar_nacionalidad_autores(cantidad=50):

    for _ in range(cantidad):
        response = requests.post(
            url=f'{base_url}/nacionalidades',
            json={ 'nombre' : fake.country()},
            headers=headers
        )
        print(response.status_code, response.json())

def poblar_editorial_libros():

    # 50 EDITORIALES DE LIBROS
    editoriales = ["Penguin Random House","Planeta","Santillana","Alfaguara","Anagrama","Tusquets","Sudamericana","Salamandra","Debolsillo","Grijalbo","Espasa","Ediciones B","Almadía",
        "Era","Fondo de Cultura Económica","Siglo XXI Editores","Océano","Norma","SM Ediciones","Booket","Destino","Seix Barral","Alianza Editorial","Cátedra","Gredos",
        "Crítica","Debate","Paidós","Ariel","RBA Libros","Montena","Nube de Tinta","Roca Editorial","Plaza & Janés","Lumen",
        "Literatura Random House","Suma de Letras","V&R Editoras","Vergara","Urano","Kalandraka","Edelvives","Anaya",
        "McGraw-Hill Educación","Pearson","O'Reilly Media","Marcombo","Trillas","Limusa","Porrúa",
    ]

    for editorial in editoriales:
        response = requests.post(
            url=f'{base_url}/editoriales',
            json={ 'nombre':editorial},
            headers=headers
        )
        print(response.status_code, response.json())



def poblar_autores_libros(cantidad=40):

    for _ in range(cantidad):
        response = requests.post(
            url=f'{base_url}/autores',
            json={ 
                'nombre':fake.first_name(),
                'apellido_paterno': fake.last_name(),
                'apellido_materno': fake.last_name(),
                'nacionalidad_id': random.randint(1,110)
             },
             headers=headers
        )
        print(response.status_code, response.json())

if __name__ == "__main__":
    print("==== EJECUTANDO SCRIPTS PYTHON ====")
    #poblar_idiomas_libros(50)
    #poblar_categorias_libros()
    #poblar_nacionalidad_autores()
    #poblar_editorial_libros()
    poblar_autores_libros()