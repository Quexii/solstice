#!/bin/fish
# Description: Count lines of code in various file types but exclude certain directories
# Usage: loc.fish [directory]

find $argv[1] . -type f \( -name "*.c" -o -name "*.h" -o -name "*.cpp" -o -name "*.hpp" -o -name "*.py" -o -name "*.java" -o -name "*.kt" -o -name "*.kts" \) ! -path "*/.git/*" ! -path "*/build/*" ! -path "*/node_modules/*" ! -path "*/vendor/*" | xargs wc -l | tail -n 1