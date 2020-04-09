import logging
import threading

import pika
from retry import retry


logging.basicConfig(level=logging.DEBUG)


class RabbitMq:
    config = {
        'host': '0.0.0.0',
        'port': 5678,
        'username': 'student',
        'password': 'student',
        'exchange': 'libraryapp.direct',
        'send_routing_key': 'libraryapp.routingkey1',
        'send_queue': 'libraryapp.queue1',
        'receive_routing_key': 'libraryapp.routingkey',
        'receive_queue': 'libraryapp.queue',
    }
    credentials = pika.PlainCredentials(config['username'], config['password'])
    parameters = pika.ConnectionParameters(host='localhost', port=5672, credentials=credentials)

    def __init__(self, signal):
        self._signal = signal

        self._write_connection = pika.SelectConnection(parameters=self.parameters,
                                                       on_open_callback=self.on_write_open_connection)
        self._channel = None
        threading.Thread(target=self._write_connection.ioloop.start).start()

    def on_write_open_connection(self, _unused_connection):
        logging.info("Opened write connection")
        self._write_connection.channel(on_open_callback=self.on_write_channel_open)

    def on_write_channel_open(self, channel):
        logging.info('Write channel opened')
        self._channel = channel
        self._channel.basic_consume(self.config['receive_queue'], self.receive_message)

    def receive_message(self, _unused_channel, basic_deliver, properties, body):
        self._signal.emit(body.decode('utf-8'))

    def send_message(self, message):
        # automatically close the connection
        self._channel.basic_publish(exchange=self.config['exchange'],
                                    routing_key=self.config['send_routing_key'],
                                    body=message)