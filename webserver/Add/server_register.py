from query_date import QueryDate
from server_objects import Passagem, Hospedagem
import requests

headers = {'content-type': 'application/json'}
server_url = 'http://localhost:8084/WebApplication/webresources/Server/'


def parse_command(command: str):
    if command.lower() == 'add h':
        adiciona_hospedagem()

    elif command.lower() == 'add p':
        adiciona_passagem()

    else:
        print('Not supported')


def adiciona_hospedagem():
    """
    Creates a new lodging to be added and sends to server.
    """
    location = input('Local: ')
    entry_date = input('Dia de entrada (DD/MM/AAAA): ')
    n_days = int(input('Dias disponiveis: '))
    n_rooms = int(input('Número de quartos: '))
    price = str(float(input('Preço: ')))

    entry_date = QueryDate(entry_date)

    available_dates = {}

    for i in range(n_days):
        available_dates[entry_date.repr_day+i] = n_rooms

    h = Hospedagem(location=location, price=price, available_dates=available_dates)

    h_json = h.to_json_dict()

    response = requests.put(
        server_url + 'add/hospedagens',
        headers=headers,
        json=h_json
    )

    verify_response(response)


def adiciona_passagem():
    """
    Creates a new ticket to be added and sends to server.
    """
    origin = input('Origem: ')
    destination = input('Destino: ')
    date = input('Dia do voo (DD/MM/AAAA)')
    n_spots = int(input('Número de passagens: '))
    price = str(float(input('Preço: ')))

    query_date = QueryDate(date)

    p = Passagem(origin=origin, destination=destination, date=query_date.repr_day, n_spots_left=n_spots, price=price)

    p_json = p.to_json_dict()

    response = requests.put(
        server_url + 'add/passagens',
        headers=headers,
        json=p_json
    )

    verify_response(response)


def verify_response(response):
    """
    Verifies if response's status code is 200 and returns a boolean.
    True if response is OK. False otherwise.
    """

    if response.status_code != 200:
        print('Servidor nao respondeu OK')
        return False

    return True


while True:
    cmd = input()
    parse_command(cmd)


