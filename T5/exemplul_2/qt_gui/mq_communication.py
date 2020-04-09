import logging
import threading

import pika


logging.basicConfig(level=logging.DEBUG)


class RabbitMq:
    credentials = pika.PlainCredentials('student', 'student')
    parameters = pika.ConnectionParameters(host='localhost', port=5672, credentials=credentials)

    def __init__(self, signal):
        self._signal = signal

        self._connection = pika.SelectConnection(parameters=self.parameters,
                                                 on_open_callback=self.on_open_connection)
        self._channel = None
        threading.Thread(target=self._connection.ioloop.start).start()

    def on_open_connection(self, _unused_connection):
        logging.info("Opened write connection")
        self._connection.channel(on_open_callback=self.on_channel_open)

    def on_channel_open(self, channel):
        logging.info('Write channel opened')
        self._channel = channel
        self._channel.basic_consume('libraryapp.queue', self.receive_message)

    def receive_message(self, _unused_channel, _, _2, body):
        self._signal.emit(body.decode('utf-8'))

    def send_message(self, message):
        self._channel.basic_publish(exchange='libraryapp.direct',
                                    routing_key='libraryapp.routingkey1',
                                    body=message)