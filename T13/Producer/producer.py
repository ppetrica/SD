import threading
import time

from kafka import KafkaProducer
from pynput.mouse import Controller


class Producer(threading.Thread):
    def __init__(self, topic):
        super().__init__()
        self.topic = topic

    def run(self) -> None:
        producer = KafkaProducer()

        mouse = Controller()

        for i in range(100):
            message = '{}'.format(mouse.position)
            # thread-ul producator trimite mesaje catre un topic
            producer.send(topic=self.topic, value=bytearray(message, encoding="utf-8"))

            print("Am produs mesajul: {}".format(message))
            time.sleep(1)


if __name__ == '__main__':
    prod = Producer('tema_13')
    prod.run()
