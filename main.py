#!/usr/bin/python
# Alex Spitzer (2013)
# Andrew Halpern <3 Amber

import os
import random

music_dir = "/home/alex/music"
exec_str = "mpg123 \"%s\" &"
num_of_songs = 2


files = [f for f in os.listdir(music_dir) if f.endswith(".mp3")]

indices = [random.randint(0, len(files)) for i in range(num_of_songs)]

for ind in indices:
    os.system(exec_str % os.path.join(music_dir, files[ind]))
