#!/bin/fish

find . -name '*.kt' -or -name '*.java' -or -name '*.kts' -or -name '*.vert' -or -name '*.frag' -or -name '*.toml' | xargs wc -l