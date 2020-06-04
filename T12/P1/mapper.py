#!/usr/bin/env python
"""mapper.py"""

import sys
import requests
import re

regex = re.compile(r'''<a\s+?href="(.*?)"''')

for line in sys.stdin:
    line = line.strip()

    resp = requests.get(line)
    
    anchors = re.findall(regex, resp.text)
    
    for anchor in anchors:
        print('%s\t%s' % (line, anchor))
