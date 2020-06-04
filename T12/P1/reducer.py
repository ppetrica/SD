#!/usr/bin/env python
"""reducer.py"""

import sys

page2urls = {}


for line in sys.stdin:
    line = line.strip()

    page_url, intern_url = line.split()

    if page_url in page2urls.keys():
        if intern_url not in page2urls[page_url]:
            page2urls[page_url].append(intern_url)
    else:
        page2urls[page_url] = []
        page2urls[page_url].append(intern_url)

for k, v in page2urls.items():
    print(k,":", v, "\n")