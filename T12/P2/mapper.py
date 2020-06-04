#!/usr/bin/env python
"""mapper.py"""


import sqlite3

db_path = r'C:\Users\ppetrica\AppData\Roaming\Mozilla\Firefox\Profiles\ybwny396.default-release\places.sqlite'
conn = sqlite3.connect(db_path)
c = conn.cursor()

for row in c.execute(' SELECT url, frecency FROM moz_places;'):
        url = row[0]
        freq = row[1]
        hostname = url.split('//')[-1].split('/')[0]   #primul split obtine partea de url dupa protocol (ex:dupa http://, htttps://)
                                                     #al doilea split va separa hostname-ul de restul url-ului
        if hostname != "":
                print('%s\t%s' % (hostname, freq))

