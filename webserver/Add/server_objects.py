

class Hospedagem:

    def __init__(self, location: str, price: str, available_dates: dict):
        self.location = location
        self.price = price
        self.available_dates = available_dates

    def to_json_dict(self):
        json_dict = {
            'location': self.location,
            'price': self.price,
            'availableDates': self.available_dates
        }

        return json_dict

    @staticmethod
    def from_server_json(json_response):

        location = json_response['location']
        price = json_response['price']
        available_dates = json_response['available_dates']

        return Hospedagem(location, price, available_dates)


class Passagem:

    def __init__(self, origin: str, destination: str, date: int, n_spots_left: int, price: str):
        self.origin = origin
        self.destination = destination
        self.date = date
        self.n_spots_left = n_spots_left
        self.price = price

    def to_json_dict(self):
        json_dict = {
            'origin': self.origin,
            'destination': self.destination,
            'date': self.date,
            'nSpotsLeft': self.n_spots_left,
            'price': self.price
        }

        return json_dict

    @staticmethod
    def from_server_json(json_response):

        origin = json_response['origin']
        destination = json_response['destination']
        date = json_response['date']
        n_spots_left = json_response['nSpotsLeft']
        price = json_response['price']

        return Passagem(origin, destination, date, n_spots_left, price)
